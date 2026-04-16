## 项目结构

```plaintext
com.shapeville
├── main                 // 主程序入口和主框架类
│   ├── MainApp.java     // 程序入口
│   └── MainFrame.java   // 主框架类，负责面板切换和任务管理
├── ui                   // 主框架UI组件 (如 HomeScreen, NavigationBar) 和通用UI工具
│   ├── NavigationBar.java       // 顶部导航栏，显示分数和任务进度
│   └── panel_templates         // 任务面板的基类或接口
│       ├── HomeScreenPanel.java // 主页面板
│       ├── EndPanel.java        // 结束面板
│       └── TaskPanel.java       // 任务面板接口
├── model                // 共享数据模型 (Shape, Angle, UserProgress, etc.)
│   ├── Shape.java       // 图形模型
│   ├── Angle.java       // 角度模型
│   ├── AngleType.java   // 角度类型枚举
│   ├── Problem.java     // 问题模型
│   ├── Feedback.java    // 用户反馈模型
│   └── TaskDefinition.java // 任务定义模型
├── logic                // 核心业务逻辑 (TaskManager, ScoreManager, etc.)
│   ├── TaskManager.java // 任务管理器，负责任务加载和切换
│   ├── ScoreManager.java // 分数管理器，负责分数计算和更新
│   └── TaskLogic.java   // 任务逻辑接口
├── tasks                // 各个任务模块的包
│   ├── sk1              // 第一组任务
│   │   ├── Task1Panel.java // 任务1：2D 和 3D 图形识别
│   │   └── Task2Panel.java // 任务2：角度类型识别
│   ├── sk2              // 第二组任务
│   │   ├── Task3Panel.java // 任务3：形状面积计算
│   │   └── Task4Panel.java // 任务4：圆的面积和周长计算
│   └── bonus            // 额外任务
│       ├── compound.java // 额外任务1：复合形状面积计算
│       └── sector.java   // 额外任务2：扇形面积计算
├── assets               // 存放图片、声音等资源 (通常在 src/main/resources/assets)
│   ├── 2d               // 2D 图形资源
│   │   ├── circle.png
│   │   ├── rectangle.png
│   │   └── ...          // 其他 2D 图形
│   ├── 3d               // 3D 图形资源
│   │   ├── cone.png
│   │   ├── cube.png
│   │   └── ...          // 其他 3D 图形
│   ├── compound         // 复合形状资源
│   │   ├── compound1.png
│   │   ├── compound2.png
│   │   └── ...          // 其他复合形状
│   └── sector           // 扇形资源
│       ├── sector1.png
│       ├── sector2.png
│       └── ...          // 其他扇形
└── utils                // 通用工具类
    ├── ImageLoader.java // 图片加载工具类
    └── Constants.java   // 常量定义
```

---

## 更新内容

### **5.21 更新**
1. **新增任务模块：**
   - `bonus/compound.java`：复合形状面积计算任务。
   - `bonus/sector.java`：扇形面积计算任务。

2. **完善资源目录：**
   - 添加了 `assets/compound` 和 `assets/sector` 目录，分别存放复合形状和扇形的图片资源。

3. **优化架构：**
   - 将任务模块划分为 `sk1`（第一组任务）、`sk2`（第二组任务）和 `bonus`（额外任务）。
   - 所有任务面板实现了 `TaskPanel` 接口，统一了任务面板的行为。

4. **新增功能：**
   - `NavigationBar` 显示任务进度和分数。
   - `HomeScreenPanel` 显示当前总分数。

5. **修复问题：**
   - 修复了 `sector` 模块图片路径问题，确保图片能够正确加载。
   - 修复了 `TaskManager` 中任务切换逻辑的潜在问题。

---

## 使用说明

1. **运行程序：**
   - 运行 `MainApp.java` 启动程序。

2. **任务导航：**
   - 在 `HomeScreenPanel` 中选择任务开始学习。
   - 使用 `NavigationBar` 返回主页或结束会话。

3. **资源管理：**
   - 确保所有图片资源存放在 `src/main/resources/assets` 目录下，并按照子目录分类。

4. **扩展任务：**
   - 新增任务时，需在 `tasks` 目录中创建对应的任务模块，并实现 `TaskPanel` 接口。
   - 在 `TaskManager` 中注册新任务。

---

## 注意事项

- 确保 Maven 项目配置正确，资源文件能够被打包到 `target/classes` 目录中。
- 图片路径需以 `/assets/` 开头，并存放在 `src/main/resources/assets` 目录下。
- 如果新增任务或修改现有任务，请更新此文档以便团队成员了解最新变化。



5.22 JZB命令行 使用框架暂存
# Java MiniProject - 手动命令行编译与执行指南

本项目设计为使用 JDK 21.0.2 (或兼容版本) 通过**手动**命令行方式进行编译和运行。**根据要求，编译或执行过程不允许使用任何脚本文件（如 `.sh` 或 `.bat`）。**

请务必仔细遵循以下步骤，特别是关于包含空格的路径处理。

## 前提条件

*   已安装 Java Development Kit (JDK) 21.0.2 或兼容版本。
*   `javac` (编译器) 和 `java` (运行器) 命令必须在系统的 PATH 环境变量中可用。

## 重要：选择正确的命令行环境

本指南中的 `javac` 和 `java` 命令是为标准的命令行环境设计的。

*   **Windows 用户：** 强烈建议使用 **命令提示符 (CMD)** 或 **Git Bash**。在 PowerShell 中，由于 `@` 符号有特殊含义，直接运行本指南中的 `javac ... @"sources.txt"` 命令可能会遇到解析错误。
    *   如果在 PowerShell 中必须执行，请尝试将 `@sources.txt` 参数整体用引号包围，如：`javac -d "bin" -sourcepath "src/main/java" -encoding UTF-8 "@sources.txt"`。即便如此，CMD 或 Git Bash 仍是更可靠的选择。
*   **Linux/macOS 用户：** 使用默认的终端即可。

## 关于路径中空格的重要说明

在以下所有指令中，如果文件路径或目录路径的任何部分包含空格（例如："My Documents", "Folder With Spaces"），当您在命令行中输入该路径时，**必须将整个路径用双引号 (`"..."`) 括起来**。

## 核心步骤概览

如果您已熟悉命令行操作，以下是编译和运行本项目的核心指令。详细步骤请参见后续说明。
**重要：** 以下所有命令均假设您已通过 `cd` 命令进入了本项目的根目录，并且项目根目录下已存在我们提供的 `sources.txt` 文件。

1.  **编译：**
    ```bash
    javac -d "bin" -sourcepath "src/main/java" -encoding UTF-8 @"sources.txt"
    ```
2.  **运行 (假设主类为 `com.shapeville.main.MainApp`):**
    ```bash
    java -cp "bin" com.shapeville.main.MainApp
    ```

**请注意：** 上述核心指令省略了创建 `bin` 目录和复制资源文件的步骤。为确保程序完整运行，请务必参考下面的详细步骤说明。

## 详细步骤说明

### 步骤 1：导航到项目根目录
   打开命令行终端，并使用 `cd` 命令导航到您存放本项目的根目录（即包含此 `README.md`、`src` 文件夹和 `sources.txt` 文件的目录）。
   (示例: `cd "D:\Path To Project\YourProjectName"`)

### 步骤 2：确保输出目录 `bin` 存在
   在项目根目录下，如果 `bin` 文件夹不存在，请创建它：
   (Windows: `md bin` | Linux/macOS/Git Bash: `mkdir bin`)
   *项目中已包含一个空的 `bin` 目录，如果评估者希望进行全新构建，可以先手动删除 `bin` 目录下的所有内容，然后再执行编译。*

### 步骤 3：`sources.txt` 文件
   本项目根目录下已包含一个名为 `sources.txt` 的文件。此文件列出了所有需要编译的 Java 源文件的相对路径（相对于项目根目录）。内容格式如下：