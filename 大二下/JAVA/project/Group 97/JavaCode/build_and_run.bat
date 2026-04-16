@echo off
setlocal ENABLEDELAYEDEXPANSION

REM --- Configuration ---
SET "MAIN_CLASS=com.shapeville.main.MainApp"

REM --- Derived Variables ---
SET "PROJECT_ROOT=%~dp0"
SET "PROJECT_ROOT_NO_TRAILING_SLASH=%PROJECT_ROOT:~0,-1%"

SET "SRC_JAVA_DIR=%PROJECT_ROOT_NO_TRAILING_SLASH%\src\main\java"
SET "SRC_RESOURCES_DIR=%PROJECT_ROOT_NO_TRAILING_SLASH%\src\main\resources"
SET "SRC_RESOURCE_SINGULAR_DIR=%PROJECT_ROOT_NO_TRAILING_SLASH%\src\main\resource"
SET "OUTPUT_DIR=%PROJECT_ROOT_NO_TRAILING_SLASH%\bin"
SET "TEMP_SOURCES_LIST_FILE=%PROJECT_ROOT_NO_TRAILING_SLASH%\sources_list.txt"

REM --- Debug Flag ---
SET "DEBUG_MODE=1" REM Set to 1 for verbose debug output, 0 to disable

:main_script
echo ------------------------------------
echo Starting Build and Run Process
echo Project Root: "%PROJECT_ROOT_NO_TRAILING_SLASH%"
echo Main Class:   %MAIN_CLASS%
echo ------------------------------------
echo.

REM 0. Clean up previous temporary source list file
IF EXIST "%TEMP_SOURCES_LIST_FILE%" (
  echo INFO: Deleting previous temporary source list file: "%TEMP_SOURCES_LIST_FILE%"
  DEL /F /Q "%TEMP_SOURCES_LIST_FILE%"
  IF ERRORLEVEL 1 (
    echo WARNING: Failed to delete "%TEMP_SOURCES_LIST_FILE%". It might be locked.
  )
)

REM 1. Clean previous build output
IF EXIST "%OUTPUT_DIR%\" (
  echo INFO: Cleaning previous build directory: "%OUTPUT_DIR%"
  RMDIR /S /Q "%OUTPUT_DIR%"
  IF ERRORLEVEL 1 (
    echo WARNING: Failed to delete "%OUTPUT_DIR%". It might be locked or contain in-use files.
    echo Please ensure no programs are using files in the bin directory.
    REM We can try to create it anyway, mkdir will fail if rmdir didn't fully succeed and it's still there.
  )
)

REM 2. Create output directory
echo INFO: Creating output directory: "%OUTPUT_DIR%"
MKDIR "%OUTPUT_DIR%"
IF ERRORLEVEL 1 (
  echo ERROR: Failed to create output directory "%OUTPUT_DIR%". Exiting.
  goto :script_end_pause
)

REM 3. Find Java source files and create the @argfile
echo INFO: Finding Java source files in "%SRC_JAVA_DIR%" and creating "%TEMP_SOURCES_LIST_FILE%"...
SET "java_files_found=0"
(FOR /R "%SRC_JAVA_DIR%" %%F IN (*.java) DO (
    echo %%F
    SET "java_files_found=1"
)) > "%TEMP_SOURCES_LIST_FILE%"

IF "%java_files_found%"=="0" (
    echo ERROR: No Java source files found in "%SRC_JAVA_DIR%". Exiting.
    goto :script_end_pause
)
IF NOT EXIST "%TEMP_SOURCES_LIST_FILE%" (
    echo ERROR: Failed to create temporary source list file: "%TEMP_SOURCES_LIST_FILE%". Exiting.
    goto :script_end_pause
)
FOR %%A IN ("%TEMP_SOURCES_LIST_FILE%") DO IF %%~zA EQU 0 (
    echo ERROR: Temporary source list file ("%TEMP_SOURCES_LIST_FILE%") is empty. Exiting.
    goto :script_end_pause
)

IF "%DEBUG_MODE%"=="1" (
    echo.
    echo DEBUG: Contents of "%TEMP_SOURCES_LIST_FILE%":
    echo ============================================
    TYPE "%TEMP_SOURCES_LIST_FILE%"
    echo ============================================
    echo.
)

echo INFO: Compiling Java source files using @argfile...
SET "JAVAC_COMMAND=javac -d "%OUTPUT_DIR%" -sourcepath "%SRC_JAVA_DIR%" -encoding UTF-8 @"%TEMP_SOURCES_LIST_FILE%""
IF "%DEBUG_MODE%"=="1" (
    echo DEBUG: Executing JAVAC command:
    echo %JAVAC_COMMAND%
    echo.
)

REM Execute the command
%JAVAC_COMMAND%
SET "JAVAC_ERROR_LEVEL=!ERRORLEVEL!"

IF "!JAVAC_ERROR_LEVEL!" NEQ "0" (
  echo ERROR: Java compilation failed with ERRORLEVEL !JAVAC_ERROR_LEVEL!. Check javac error messages above. Exiting.
  goto :script_end_pause
)
echo INFO: Java compilation successful.
echo.

REM Check if bin directory has class files (simple check)
SET "class_files_exist=0"
IF EXIST "%OUTPUT_DIR%\*.class" SET "class_files_exist=1"
IF EXIST "%OUTPUT_DIR%\*\*.class" SET "class_files_exist=1" REM Check one level deep for packages

IF "%class_files_exist%"=="0" (
    echo WARNING: Java compilation reported success, but no .class files were found in the root of "%OUTPUT_DIR%" or one package level deep.
    echo Please check javac output carefully for any warnings or issues.
) ELSE (
    IF "%DEBUG_MODE%"=="1" (
        echo DEBUG: Listing contents of "%OUTPUT_DIR%" (root):
        DIR "%OUTPUT_DIR%" /B
    )
)
echo.


REM 4. Copy resource files
echo INFO: Copying resources...
IF EXIST "%SRC_RESOURCES_DIR%\" (
  SET "resource_dir_has_files=0"
  FOR %%A IN ("%SRC_RESOURCES_DIR%\*") DO ( SET "resource_dir_has_files=1" & goto :check_std_res_done_debug )
  :check_std_res_done_debug
  IF "%resource_dir_has_files%"=="1" (
    echo INFO: Copying from "%SRC_RESOURCES_DIR%" to "%OUTPUT_DIR%"...
    xcopy "%SRC_RESOURCES_DIR%" "%OUTPUT_DIR%\" /S /E /I /Y /Q > nul
    IF ERRORLEVEL 1 ( echo WARNING: Failed to copy some resources from "%SRC_RESOURCES_DIR%". ) ELSE ( echo INFO: Resources from "%SRC_RESOURCES_DIR%" copied. )
  ) ELSE (
    echo INFO: No files found in "%SRC_RESOURCES_DIR%" or directory is empty.
  )
) ELSE (
  echo INFO: Directory "%SRC_RESOURCES_DIR%" does not exist.
)

IF EXIST "%SRC_RESOURCE_SINGULAR_DIR%\" (
  SET "singular_res_dir_has_files=0"
  FOR %%A IN ("%SRC_RESOURCE_SINGULAR_DIR%\*") DO ( SET "singular_res_dir_has_files=1" & goto :check_sgl_res_done_debug )
  :check_sgl_res_done_debug
  IF "%singular_res_dir_has_files%"=="1" (
    echo INFO: Copying from "%SRC_RESOURCE_SINGULAR_DIR%" to "%OUTPUT_DIR%"...
    xcopy "%SRC_RESOURCE_SINGULAR_DIR%" "%OUTPUT_DIR%\" /S /E /I /Y /Q > nul
    IF ERRORLEVEL 1 ( echo WARNING: Failed to copy some resources from "%SRC_RESOURCE_SINGULAR_DIR%". ) ELSE ( echo INFO: Resources from "%SRC_RESOURCE_SINGULAR_DIR%" copied. )
  ) ELSE (
    echo INFO: No files found in "%SRC_RESOURCE_SINGULAR_DIR%" or directory is empty.
  )
) ELSE (
  echo INFO: Directory "%SRC_RESOURCE_SINGULAR_DIR%" does not exist.
)
echo.

REM 5. Run the application
echo INFO: Running application: %MAIN_CLASS%
echo INFO: Classpath: "%OUTPUT_DIR%"
echo --- Application Output Starts ---
java -cp "%OUTPUT_DIR%" %MAIN_CLASS% %*
echo --- Application Output Ends ---
echo.

:cleanup_and_exit
IF EXIST "%TEMP_SOURCES_LIST_FILE%" (
  echo INFO: Deleting temporary source list file: "%TEMP_SOURCES_LIST_FILE%"
  DEL /F /Q "%TEMP_SOURCES_LIST_FILE%"
)

:script_end_pause
echo.
echo =====================================================
echo Script execution finished or paused due to error.
IF DEFINED JAVAC_ERROR_LEVEL IF "!JAVAC_ERROR_LEVEL!" NEQ "0" (
    echo JAVAC exited with ERRORLEVEL: !JAVAC_ERROR_LEVEL!
)
echo Press any key to close this window...
pause > nul
endlocal
goto :eof