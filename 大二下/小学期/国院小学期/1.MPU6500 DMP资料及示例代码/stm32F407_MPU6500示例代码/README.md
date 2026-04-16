# 基于STM32F4的MPU6500 DMP测试程序(已调通)

此项目为STM32F4Discovery开发板上使用MPU6500的DMP测试程序，已经成功调试通过。程序通过SWO端口输出Roll、Pitch、Yaw三轴角度数据，方便开发者快速验证MPU6500-DMP功能。

## 程序描述

- 程序基于STM32CubeMx搭建，使用官方DMP库。
- 数据通过SWO端口输出，直接显示Roll、Pitch、Yaw角度信息。
- 调试时需确保传感器放置平稳，否则自检(test)无法通过。

## 使用说明

- 将程序烧录至STM32F4Discovery开发板。
- 确保传感器平稳放置，通过SWO端口观察输出数据。
- 查看Roll、Pitch、Yaw角度信息，以验证DMP功能正常工作。

本项目旨在提供一个简单的测试程序，帮助开发者快速上手STM32F4与MPU6500-DMP的组合使用。在使用过程中如有问题，请自行研究或寻求技术支持。