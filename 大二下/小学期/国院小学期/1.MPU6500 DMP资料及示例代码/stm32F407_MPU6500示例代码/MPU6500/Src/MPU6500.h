#ifndef __MPU6500_H__
#define __MPU6500_H__

#include "stm32f4xx_hal.h"
#include "inv_mpu.h"
#include "inv_mpu_dmp_motion_driver.h"
#include <stdio.h>

extern I2C_HandleTypeDef hi2c1;

#define MPU6500I2C hi2c1

/* Starting sampling rate. */
#define DEFAULT_MPU_HZ  (200)


HAL_StatusTypeDef  MPU6500Init(void);	
uint8_t Sensors_I2C_WriteRegister(unsigned char slave_addr, unsigned char reg_addr,unsigned char length, unsigned char *data);//0 if success else -1
uint8_t Sensors_I2C_ReadRegister(unsigned char slave_addr, unsigned char reg_addr,unsigned char length, unsigned char *data);//0 if success else -1
void mdelay(__IO uint32_t ms);
uint32_t get_tick_count(unsigned long *ms);

#endif
