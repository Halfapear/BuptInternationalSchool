#include "stm32f10x.h"

#include "delay.h"
#include "gpio.h"
#include "moto.h"
#include "pwm.h"
#include "adc.h"
#include "usart.h"
#include "encoder.h"

 /**************************************************************************
作者：平衡小车之家
我的淘宝小店：http://shop114407458.taobao.com/
**************************************************************************/


int TargetVelocity=500;
float Velcity_Kp=1,  Velcity_Ki=3,  Velcity_Kd; //相关速度PID参数

int main(void)
 {	
	int encoder_A,encoder_B;
	int Velocity_PWM1,Velocity_PWM2;
	u16 adcx;
	float vcc;
	SystemInit(); //配置系统时钟为72M   
	delay_init();    //延时函数初始化
	uart_init(9600);		//串口初始化波特率为9600
	adc_Init();				//ADC1的初始化   
	PWM_Int(7199,0);      	//初始化pwm输出 72000 000 /7199+1=10000 
	Encoder_Init_Tim2();	//编码器初始化
	Encoder_Init_Tim4();	//编码器初始化
	while(1)
	{

		adcx=Get_adc_Average(ADC_Channel_2,10);  //获取adc的值
		vcc=(float)adcx*(3.3*11/4096);     		 //求当前电压
		/*
		开环控制：不使用编码器的数值来调整控制输入。控制输入是基于设定值直接设定的，不考虑系统的当前状态或输出。
		特点：如果电机的负载突然增加（外部干扰）或电机效率发生变化（电池电压、电机温度等干扰），开环控制可能无法及时调整，导致实际转速与设定值有所偏差。
		tips：使用时将以下注释打开即可
		*/
		printf("当前为开环控制教程：\r\n");
		Velocity_PWM1=3000;						//开环控制-直接设定PWM值
		Velocity_PWM2=3000;						//开环控制-直接设定PWM值
		Set_PWM(Velocity_PWM1,Velocity_PWM2);	//开环控制-根据PWM值驱动电机
		
		/*
		读取编码器数据方法：通过读取对应定时器的计数值来获取编码器的数据。在学习闭环控制前需要学习如何使用
		*/
		encoder_A=Read_Encoder(2);								//读取编码器1数值，  开环时：仅监测当前电机速度。   闭环时：监测当前电机速度，用于PID控制器闭环反馈 
		encoder_B=Read_Encoder(4);              				//读取编码器2数值，  开环时：仅监测当前电机速度。   闭环时：监测当前电机速度，用于PID控制器闭环反馈 
        /*
		闭环控制：将采样到的编码器数值输入PID控制器形成闭环反馈，再根据PID控制器闭环控制后输出的PWM值对电机进行控制
		特点：通过实时反馈调整，可以更精确地控制电机速度，减少误差，同时能够适应系统参数的变化（电池电压、电机效率等）和外部干扰（负载，摩擦系数等），保持系统的稳定性
		tips：使用时将以下注释打开即可
		*/
//		printf("当前为闭环控制教程：\r\n");
//		Velocity_PWM1=Velocity_A(TargetVelocity,encoder_A);		//PID闭环控制-输出PWM值
//		Velocity_PWM2=Velocity_B(TargetVelocity,encoder_B);		//PID闭环控制-输出PWM值
//		Set_PWM(Velocity_PWM1,Velocity_PWM2);					//闭环控制-根据PWM值驱动电机	
		
		printf("当前电压=%6.2f V  Encoder_A = %d  Encoder_B=%d\r\n",vcc,encoder_A,encoder_B);				//打印当前电压，保留小数点后两位	
	}
 }

