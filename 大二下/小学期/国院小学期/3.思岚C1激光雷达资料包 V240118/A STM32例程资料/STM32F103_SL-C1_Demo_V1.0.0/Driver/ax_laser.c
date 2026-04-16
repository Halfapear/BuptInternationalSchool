/**			                                                    
		   ____                    _____ _______ _____       XTARK@塔克创新
		  / __ \                  / ____|__   __|  __ \ 
		 | |  | |_ __   ___ _ __ | |       | |  | |__) |
		 | |  | | '_ \ / _ \ '_ \| |       | |  |  _  / 
		 | |__| | |_) |  __/ | | | |____   | |  | | \ \ 
		  \____/| .__/ \___|_| |_|\_____|  |_|  |_|  \_\
				| |                                     
				|_|                OpenCTR   机器人控制器
									 
  ****************************************************************************** 
  *           
  * 版权所有： XTARK@塔克创新  版权所有，盗版必究
  * 公司网站： www.xtark.cn   www.tarkbot.com
  * 淘宝店铺： https://xtark.taobao.com  
  * 塔克微信： 塔克创新（关注公众号，获取最新更新资讯）
  *      
  ******************************************************************************
  * @作  者  Musk Han@XTARK
  * @内  容  思岚C1雷达驱动
  *
  ******************************************************************************
  * @说  明
  *
  * 
  ******************************************************************************
  */

#include "ax_laser.h"
#include <stdio.h>

static uint8_t uart4_rx_con=0;       //接收计数器
static uint8_t uart4_rx_chksum;      //异或校验
static uint8_t uart4_rx_buf[100];     //接收缓冲
static uint8_t uart4_tx_buf[10];     //接收缓冲

//扫描一圈的雷达数据
LaserPointTypeDef ax_ls_point[250];

//函数定义
void LS_DataHandle(void);

/**
  * @简  述  雷达串口初始化
  * @参  数  无
  * @返回值	 无
  */
void AX_LASER_Init(void)
{
	GPIO_InitTypeDef GPIO_InitStructure;
	USART_InitTypeDef USART_InitStructure;
	NVIC_InitTypeDef NVIC_InitStructure;

	//**USART配置******
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOC, ENABLE);  //打开串口GPIO的时钟

	//将USART Tx的GPIO配置为推挽复用模式
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_10;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF_PP;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOC, &GPIO_InitStructure);

	//将USART Rx的GPIO配置为浮空输入模式
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_11;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;
	GPIO_Init(GPIOC, &GPIO_InitStructure);

	RCC_APB1PeriphClockCmd(RCC_APB1Periph_UART4, ENABLE);  //打开串口外设的时钟

	//配置USART参数
	USART_InitStructure.USART_BaudRate = 460800;		//波特率
	USART_InitStructure.USART_WordLength = USART_WordLength_8b;
	USART_InitStructure.USART_StopBits = USART_StopBits_1;
	USART_InitStructure.USART_Parity = USART_Parity_No;
	USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
	USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;
	USART_Init(UART4, &USART_InitStructure);

	//配置USART为中断源
	NVIC_InitStructure.NVIC_IRQChannel = UART4_IRQn;
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 1; //抢断优先级	
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 1;	//子优先级
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;	//使能中断
	NVIC_Init(&NVIC_InitStructure);//初始化配置NVIC

	//使能 USART， 配置完毕
	USART_Cmd(UART4, ENABLE);	
	
	//串口接收中断
	USART_ITConfig(UART4, USART_IT_RXNE, ENABLE);
	
	//发送空字符
	USART_SendData(UART4, 0x00);
	while(USART_GetFlagStatus(UART4,USART_FLAG_TC) != SET);
}


void UART4_IRQHandler(void)
{
	uint8_t Res,temp;
	
	if(USART_GetITStatus(UART4, USART_IT_RXNE) != RESET)  //接收中断
	{
		Res =USART_ReceiveData(UART4);	
		
		//接收数据
		if (uart4_rx_con < 3)
		{
			
			if(uart4_rx_con == 0)  //接收帧头1 
			{
				//判断帧头1
				if((Res>>4) == LS_HEADER1)
				{
					uart4_rx_buf[uart4_rx_con] = Res;
					uart4_rx_con = 1;					
				}
			}else if(uart4_rx_con == 1) //接收帧头2 
			{
				//判断帧头2
				if((Res>>4) == LS_HEADER2)
				{
					uart4_rx_buf[uart4_rx_con] = Res;
					uart4_rx_con = 2;
									
				}
				else
				{
					uart4_rx_con = 0;						
				}				
			}
			else  //接收第一个数据
			{
				uart4_rx_buf[uart4_rx_con] = Res;
				uart4_rx_con = 3;
				
				//赋值校验
				uart4_rx_chksum = Res;	
			}
		}			
		else  //接收数据
		{
			//判断是否接收完
			if(uart4_rx_con < (LS_F_LEN-1))
			{
				uart4_rx_buf[uart4_rx_con] = Res;
				uart4_rx_con++;
				uart4_rx_chksum = uart4_rx_chksum^Res;
			}
			else
			{
				//接收最后一个数据
				uart4_rx_buf[uart4_rx_con] = Res;
				uart4_rx_chksum = uart4_rx_chksum^Res;
				
				//复位
				uart4_rx_con = 0;
				
				//计算传输的校验数据
				temp = ((uint8_t)(uart4_rx_buf[1]<<4)) + (uint8_t)(uart4_rx_buf[0]&0x0F);
				//printf("%x %x\r\n",temp, uart4_rx_chksum);					
				//printf("%2x %2x %2x\r\n", uart4_rx_buf[0], uart4_rx_buf[1], uart4_rx_chksum);		
				
				//判断校验是否正确
				if( uart4_rx_chksum == temp)
				{
					//接收完毕，进行帧数据处理
					LS_DataHandle();	
				}
			}
		}
	} 
}


/**
  * @简  述  数据处理函数
  * @参  数  无
  * @返回值	 无
  */
void LS_DataHandle(void)
{
	uint8_t i;
	float temp;
	
	static uint16_t cnt = 0;
	
	static float angle_last = 0;
	
	//每秒采集5000次10HZ，转一圈采集500个点，方便单片机处理，2个点取一个点
	static LaserPointTypeDef point[250];
	
	//一帧数据起始和结束角度
	float angle_new = (((u16)((uart4_rx_buf[3]&0x7F)<<8)) + uart4_rx_buf[2])/64.0;
	float angle_area;
	
	//起始角度大于结束角度，跨过360°
	if(angle_new > angle_last)
	{
		angle_area  = (angle_new - angle_last)/20;
		
		for(i=0; i<20; i++)
		{
			temp = angle_new + angle_area*i;
			
			//计算角度
			if(temp > 360)
			{
				point[cnt+i].angle = (temp - 360) * 100;
			}
			else
			{
				point[cnt+i].angle = (temp) * 100;
			}
			
			//计算距离
			point[cnt+i].distance =  ((u16)(uart4_rx_buf[5+i*4]<<8)) + (u8)uart4_rx_buf[4+i*4];
		}
	}
	else
	{
		angle_area = (angle_new + 360 - angle_last)/20;
		
		for(i=0; i<20; i++)
		{
		
			temp = angle_new + angle_area*i;
			
			if(temp > 360)
			{
				point[cnt+i].angle = (temp - 360) * 100;
			}
			else
			{
				point[cnt+i].angle = (temp) * 100;
			}
			
			//计算距离
			point[cnt+i].distance =  ((u16)(uart4_rx_buf[5+i*4]<<8)) + (u8)uart4_rx_buf[4+i*4];
		}
		
	}

	//赋值上一次测量角度
	angle_last = angle_new;	
	
	//输出调试数据
	//printf("%d %d %d \r\n",cnt, point[0].angle, point[0].distance);	

	//一帧数据解析结束
	cnt = cnt+20;
	
	//判断是否转弯一圈（雷达转一圈有250个点）
	if(cnt > 260)
	{
		//将数组的数据转移到外部数组中，避免覆盖数据
		for(i=0; i<250; i++)
		{
			//计算角度
			ax_ls_point[i].angle = point[i].angle;
			ax_ls_point[i].distance = point[i].distance;
		}
		
		//复位
		cnt = 0;
	}

}

/**
  * @简  述  雷达启动（密实模式）
  * @参  数  无
  * @返回值	 无
  */
void AX_LASER_Start(void)   
{
	uint8_t i;	
	
	uart4_tx_buf[0] = 0xA5;  //帧头
	uart4_tx_buf[1] = 0x82;  //启动扫描命令
	uart4_tx_buf[2] = 05;    
	uart4_tx_buf[3] = 0;    
	uart4_tx_buf[4] = 0;    
	uart4_tx_buf[5] = 0;    
	uart4_tx_buf[6] = 0;    
	uart4_tx_buf[7] = 0;    
	uart4_tx_buf[8] = 0x22;  //校验和
	
	//查询传输方式
	for(i=0; i<9; i++)
	{
		USART_SendData(UART4, uart4_tx_buf[i]);
		while(USART_GetFlagStatus(UART4,USART_FLAG_TC) != SET);
	}
}

/**
  * @简  述  雷达关闭
  * @参  数  无
  * @返回值	 无
  */
void AX_LASER_Stop(void)   
{
	uint8_t i;	

	uart4_tx_buf[0] = 0xA5;       //帧头
	uart4_tx_buf[1] = 0x25;       //关闭命令
	uart4_tx_buf[2] = 0xA5+0x25;  //校验和
	
	//查询传输方式
	for(i=0; i<3; i++)
	{
		USART_SendData(UART4, uart4_tx_buf[i]);
		while(USART_GetFlagStatus(UART4,USART_FLAG_TC) != SET);
	}
}

/******************* (C) 版权 2023 XTARK **************************************/
