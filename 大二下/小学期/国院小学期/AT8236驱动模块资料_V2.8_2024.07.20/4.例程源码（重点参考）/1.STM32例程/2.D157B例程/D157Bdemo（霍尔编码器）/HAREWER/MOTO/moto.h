#ifndef __MOTO_H
#define	__MOTO_H

#include "stm32f10x.h"

void moto(int mode);
int Velocity_A(int TargetVelocity, int CurrentVelocity);
int Velocity_B(int TargetVelocity, int CurrentVelocity);

#endif
