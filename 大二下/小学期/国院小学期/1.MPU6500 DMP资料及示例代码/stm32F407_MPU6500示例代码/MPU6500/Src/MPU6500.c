#include <MPU6500.h>
const signed char gyro_orientation[9] = {1, 0, 0,
                                          0, 1, 0,
                                          0, 0, 1};

static inline unsigned short inv_row_2_scale(const signed char *row)
{
    unsigned short b;

    if (row[0] > 0)
        b = 0;
    else if (row[0] < 0)
        b = 4;
    else if (row[1] > 0)
        b = 1;
    else if (row[1] < 0)
        b = 5;
    else if (row[2] > 0)
        b = 2;
    else if (row[2] < 0)
        b = 6;
    else
        b = 7;      // error
    return b;
}

static inline unsigned short inv_orientation_matrix_to_scalar(
    const signed char *mtx)
{
    unsigned short scalar;

    /*
       XYZ  010_001_000 Identity Matrix
       XZY  001_010_000
       YXZ  010_000_001
       YZX  000_010_001
       ZXY  001_000_010
       ZYX  000_001_010
     */

    scalar = inv_row_2_scale(mtx);
    scalar |= inv_row_2_scale(mtx + 3) << 3;
    scalar |= inv_row_2_scale(mtx + 6) << 6;


    return scalar;
}

int run_self_test(void)
{
    int result;
    long gyro[3], accel[3];

#if defined (MPU6500) || defined (MPU9250)
    result = mpu_run_6500_self_test(gyro, accel, 0);
#elif defined (MPU6050) || defined (MPU9150)
    result = mpu_run_self_test(gyro, accel);
#endif
    if (result == 0x7) {
        /* Test passed. We can trust the gyro data here, so now we need to update calibrated data*/

        /*
         * This portion of the code uses the HW offset registers that are in the MPUxxxx devices
         * instead of pushing the cal data to the MPL software library
         */
        unsigned char i = 0;

        for(i = 0; i<3; i++) {
        	gyro[i] = (long)(gyro[i] * 32.8f); //convert to +-1000dps
        	accel[i] *= 2048.f; //convert to +-16G
        	accel[i] = accel[i] >> 16;
        	gyro[i] = (long)(gyro[i] >> 16);
        }

//        mpu_set_gyro_bias_reg(gyro);

#if defined (MPU6500) || defined (MPU9250)
        mpu_set_accel_bias_6500_reg(accel);
#elif defined (MPU6050) || defined (MPU9150)
//        mpu_set_accel_bias_6050_reg(accel);
#endif
		return 0;
    }
	else
	{
		return -1;
	}
}

uint8_t Sensors_I2C_WriteRegister(unsigned char slave_addr, unsigned char reg_addr,unsigned char length, unsigned char *data)//0 if success else -1
{
	return HAL_I2C_Mem_Write(&MPU6500I2C, slave_addr, reg_addr, I2C_MEMADD_SIZE_8BIT, data, length, 5);
}
uint8_t Sensors_I2C_ReadRegister(unsigned char slave_addr, unsigned char reg_addr,unsigned char length, unsigned char *data)//0 if success else -1
{
	return HAL_I2C_Mem_Read(&MPU6500I2C, slave_addr, reg_addr, I2C_MEMADD_SIZE_8BIT, data, length, 5);
}

//**************************************
//³õÊ¼»¯MPU6500
//**************************************
HAL_StatusTypeDef MPU6500Init(void)
{
	struct int_param_s int_param;
	
	/*·ÀÖ¹I2C¿¨ËÀ*/
	HAL_I2C_DeInit(&MPU6500I2C);
	
	GPIO_InitTypeDef GPIO_InitStruct;
	
	GPIO_InitStruct.Pin = MPU_SCL_Pin | MPU_SDA_Pin;
	GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
	GPIO_InitStruct.Speed = GPIO_SPEED_HIGH;
	HAL_GPIO_Init(MPU_SCL_GPIO_Port, &GPIO_InitStruct);
	
	MPU_SCL_GPIO_Port->BSRR = MPU_SCL_Pin;
	MPU_SDA_GPIO_Port->BSRR = MPU_SDA_Pin;
	HAL_Delay(10);
	
	GPIO_InitStruct.Mode = GPIO_MODE_AF_OD;
	GPIO_InitStruct.Pull = GPIO_PULLUP;
	GPIO_InitStruct.Alternate = GPIO_AF4_I2C1;
	HAL_GPIO_Init(MPU_SCL_GPIO_Port, &GPIO_InitStruct);
	
	HAL_I2C_Init(&MPU6500I2C);
	/********/
  if(mpu_init(&int_param)){
    printf("mpu6500 init failed\n");
    return HAL_ERROR;
  }
  if(mpu_set_sensors(INV_XYZ_GYRO | INV_XYZ_ACCEL)){
    printf("mpu6500 set sensors failed\n");
    return HAL_ERROR;
    }
  if(mpu_configure_fifo(INV_XYZ_GYRO | INV_XYZ_ACCEL)){
    printf("mpu6500 configure_fifo failed\n");
    return HAL_ERROR;
  }
  if(mpu_set_sample_rate(DEFAULT_MPU_HZ)){
    printf("mpu6500 set_sample_rate failed\n");
    return HAL_ERROR;
  }
  if(dmp_load_motion_driver_firmware()){
    printf("mpu6500 load_motion_driver_firmware failed\n");
    return HAL_ERROR;
  }
  if(dmp_set_orientation( inv_orientation_matrix_to_scalar(gyro_orientation))){
    printf("mpu6500 set_orientation  failed\n");
    return HAL_ERROR;
  }
  if(dmp_enable_feature(DMP_FEATURE_6X_LP_QUAT | DMP_FEATURE_TAP |
                    DMP_FEATURE_ANDROID_ORIENT | DMP_FEATURE_SEND_RAW_ACCEL | 
                     DMP_FEATURE_SEND_CAL_GYRO | DMP_FEATURE_GYRO_CAL)){
    printf("mpu6500 dmp_enable_feature  failed\n");
    return HAL_ERROR;
  }
  if(dmp_set_fifo_rate(DEFAULT_MPU_HZ)){
    printf("mpu6500 set_fifo_rate failed\n");
    return HAL_ERROR;
  }
  if(mpu_set_dmp_state(1)){
    printf("mpu6500 set_dmp_state failed\n");
    return HAL_ERROR;
  }
  if(run_self_test()){
    printf("mpu6500 run_self_test failed\n");
    return HAL_ERROR;
  }
  ;
  
  return HAL_OK;
}

void mdelay(__IO uint32_t ms)
{
	HAL_Delay(ms);
}
uint32_t get_tick_count(unsigned long *ms)
{
	*ms = HAL_GetTick();
	return 0;
}
