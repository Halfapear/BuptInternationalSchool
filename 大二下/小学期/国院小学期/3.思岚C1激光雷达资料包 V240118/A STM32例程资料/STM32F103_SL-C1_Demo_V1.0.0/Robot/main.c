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
  * @内  容  主程序
  * 
  ******************************************************************************
  */ 


/* Includes ------------------------------------------------------------------*/
#include "stm32f10x.h"
#include <stdio.h>
#include <math.h>   

#include "ax_sys.h"    //系统设置
#include "ax_delay.h"  //软件延时
#include "ax_uart1.h"  //调试串口
#include "ax_laser.h"   //雷达驱动


/***** 雷达接线说明************************************************************* 

	雷达       单片机
	P5V--------5V
	GND--------GND
	Rx---------PC10（UART4-TX）
	Tx---------PC11（UART4-RX）

******************************************************************************/ 


/******************************************************************************
例程1：采集激光雷达数据并通过串口输出显示
说明 ：数据量较大，存下显示不全问题
*******************************************************************************/
int main(void)
{	
	uint8_t i;
	
	//设置中断优先级分组
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);   
	
	//延时函数初始化
	AX_DELAY_Init();      
	
	//调试串口初始化
	AX_UART1_Init(460800);
	
	//激光雷达初始化
	AX_LASER_Init();
	
	//等待雷达上电完成
	AX_Delayms(2000);
	
	//雷达启动检测
	AX_LASER_Start();
	
	while (1)
	{
		//遍历雷达检测数据
		for(i = 0; i < 250; i++)
		{
			printf("%d %d \r\n",ax_ls_point[i].angle, ax_ls_point[i].distance);
		}
		
		//延时
		AX_Delayms(50);
	}
}


///******************************************************************************
//例程2：指定角度下雷达检测距离，串口输出
//说明 ：检测角度可根据需要自行修改
//*******************************************************************************/
//int main(void)
//{	
//	uint8_t i;
//	
//	//A点 45度
//	uint16_t a_angle = 0;    //角度
//    uint16_t a_distance = 0; //距离
//	
//	//B点 -30度（330）
//	uint16_t b_angle = 0;    //角度
//    uint16_t b_distance = 0; //距离
//	
//	//设置中断优先级分组
//	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);   
//	
//	//延时函数初始化
//	AX_DELAY_Init();      
//	
//	//调试串口初始化
//	AX_UART1_Init(460800);
//	
//	//激光雷达初始化
//	AX_LASER_Init();
//	
//	//等待雷达上电完成
//	AX_Delayms(2000);
//	
//	//雷达启动检测
//	AX_LASER_Start();
//	
//	while (1)
//	{
//		//遍历雷达检测数据
//		for(i = 0; i < 250; i++)
//		{
//			//A点45度 雷达探测数据
//			if((ax_ls_point[i].angle> 4450) && (ax_ls_point[i].angle<4550))  
//			{	
//				//有效测量
//				a_angle = ax_ls_point[i].angle;
//				a_distance = ax_ls_point[i].distance;								
//			}
//			
//			//B点-30度 雷达探测数据
//			if((ax_ls_point[i].angle> 32950) && (ax_ls_point[i].angle<33050))  
//			{	
//				//有效测量
//				b_angle = ax_ls_point[i].angle;
//				b_distance = ax_ls_point[i].distance;								
//			}
//		}
//		
//		//输出信息
//      printf("A：角度：45° 距离：%05d   B：角度：-30° 距离：%05d \r\n",a_distance, b_distance);
//		//printf("A：角度：%d° 距离：%d   B：角度：%d° 距离：%d \r\n",a_angle, a_distance,b_angle,b_distance);
//		
//		//延时
//		AX_Delayms(100);
//	}
//}

///******************************************************************************
//例程3：寻找前方±90°范围内的最近障碍物，串口输出角度和距离信息
//说明 ：检测角度范围和距离范围，可根据需要自行修改
//*******************************************************************************/
//#define LS_ALARM_DistanceMin  150   //检测最小距离，单位mm
//#define LS_ALARM_DistanceMax 1000   //检测最大距离，单位mm

//int main(void)
//{	
//	uint8_t i;
//	
//	uint16_t min_distance = 5000;   //最近物体距离，单位mm	
//	uint16_t min_angle = 0;          //最近物体角度	
//    int16_t  follow_angle = 0;		 //跟随角度	
//	
//	//设置中断优先级分组
//	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);   
//	
//	//延时函数初始化
//	AX_DELAY_Init();      
//	
//	//调试串口初始化
//	AX_UART1_Init(460800);
//	
//	//激光雷达初始化
//	AX_LASER_Init();
//	
//	//等待雷达上电完成
//	AX_Delayms(2000);
//	
//	//雷达启动检测
//	AX_LASER_Start();
//	
//	while (1)
//	{
//		min_distance = 5000;
//		
//		//遍历雷达检测数据
//		for(i = 0; i < 250; i++)
//		{
//			//跟随有效角度270-90之间
//			if((ax_ls_point[i].angle>27000) || (ax_ls_point[i].angle<9000))  
//			{	
//				//检测有效距离内的数据
//				if((ax_ls_point[i].distance >LS_ALARM_DistanceMin) && (ax_ls_point[i].distance < LS_ALARM_DistanceMax))
//				{	
//					//寻找最近物体
//					if(ax_ls_point[i].distance < min_distance)
//					{
//						min_distance = ax_ls_point[i].distance;
//						min_angle = ax_ls_point[i].angle;
//					}
//				}
//			}
//		}
//		
//		//转换到±180°，单位度每秒
//		if(min_angle > 18000)
//			follow_angle = (min_angle - 36000)/100.0;	
//		else
//			follow_angle = min_angle/100.0;
//		
//		//输出信息
//      	printf("角度：%02d° 距离：%d \r\n",follow_angle ,min_distance);
//		
//		//延时
//		AX_Delayms(100);
//	}
//}

/******************* (C) 版权 2023 XTARK **************************************/

