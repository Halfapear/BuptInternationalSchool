#ifndef __PWM_H
#define	__PWM_H

#include "stm32f10x.h"
#define   PWMA_IN1  TIM3->CCR1
#define   PWMA_IN2  TIM3->CCR2
#define   PWMB_IN1  TIM3->CCR3
#define   PWMB_IN2  TIM3->CCR4

void PWM_Int(u16 arr,u16 psc);
void Set_PWM(int motor_left,int motor_right);

#endif

