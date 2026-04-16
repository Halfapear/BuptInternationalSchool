#!/bin/bash

# --- Configuration ---
# !!! IMPORTANT: SET YOUR MAIN CLASS HERE (fully qualified name) !!!
MAIN_CLASS="com.shapeville.main.MainApp"

# --- Derived Variables (usually no need to change) ---
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)" # Gets the directory where the script is located
PROJECT_ROOT="$SCRIPT_DIR"
SRC_JAVA_DIR="$PROJECT_ROOT/src/main/java"
SRC_RESOURCES_DIR="$PROJECT_ROOT/src/main/resources"
# Handle the non-standard "src/main/resource" (singular) directory as well
SRC_RESOURCE_SINGULAR_DIR="$PROJECT_ROOT/src/main/resource"
OUTPUT_DIR="$PROJECT_ROOT/bin"
# TEMP_SOURCES_LIST is no longer used in the primary compilation logic
# but we can keep its definition if needed for cleanup or other purposes.
# TEMP_SOURCES_LIST="$PROJECT_ROOT/sources.txt"

# --- Functions ---
cleanup() {
  echo "Cleaning up any stray temporary files (if any were created)..."
  # Example: if TEMP_SOURCES_LIST was still used:
  # if [ -f "$TEMP_SOURCES_LIST" ]; then
  #   rm "$TEMP_SOURCES_LIST"
  # fi
}

# Ensure cleanup runs on script exit or interruption
trap cleanup EXIT SIGINT SIGTERM

# --- Main Script ---
echo "------------------------------------"
echo "Starting Build and Run Process"
echo "Project Root: $PROJECT_ROOT"
echo "Main Class:   $MAIN_CLASS"
echo "------------------------------------"
echo ""

# 1. Clean previous build output (optional, but good for a fresh build)
if [ -d "$OUTPUT_DIR" ]; then
  echo "Cleaning previous build directory: $OUTPUT_DIR"
  rm -rf "$OUTPUT_DIR"
fi

# 2. Create output directory
echo "Creating output directory: $OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"
if [ $? -ne 0 ]; then
  echo "ERROR: Failed to create output directory $OUTPUT_DIR. Exiting."
  exit 1
fi

# 3. Find and compile Java source files
echo "Finding Java source files in $SRC_JAVA_DIR..."

# Check if any .java files exist before attempting compilation
# -print0 and grep -q "." is a way to check if find produces any output
# without actually storing the output if not needed immediately.
# The find command itself within the if condition is not strictly necessary
# if the main find | xargs handles the no-files-found case,
# but it provides an earlier, clearer error message.
if ! find "$SRC_JAVA_DIR" -name "*.java" -print0 | grep -q "."; then
    echo "ERROR: No Java source files found in $SRC_JAVA_DIR. Exiting."
    exit 1
fi

echo "Compiling Java source files using find and xargs..."
# Use xargs -0 to safely handle filenames with spaces or special characters from find -print0
# --no-run-if-empty ensures javac is not called if no .java files are found (though the check above should catch this)
# "$OUTPUT_DIR" and "$SRC_JAVA_DIR" are quoted to handle spaces in the project path itself.
find "$SRC_JAVA_DIR" -name "*.java" -print0 | xargs -0 --no-run-if-empty \
    javac -d "$OUTPUT_DIR" -sourcepath "$SRC_JAVA_DIR" -encoding UTF-8

# Check the exit status of the last command (which is javac via xargs)
if [ $? -ne 0 ]; then
  echo "ERROR: Compilation failed. Check javac error messages above. Exiting."
  exit 1
fi
echo "Compilation successful."
echo ""

# 4. Copy resource files
# Standard resources directory (src/main/resources)
if [ -d "$SRC_RESOURCES_DIR" ] && [ "$(ls -A "$SRC_RESOURCES_DIR" 2>/dev/null)" ]; then # Check if dir exists and is not empty
  echo "Copying resources from $SRC_RESOURCES_DIR to $OUTPUT_DIR..."
  # The trailing slash on SRC_RESOURCES_DIR/. is important to copy contents
  cp -R "$SRC_RESOURCES_DIR/." "$OUTPUT_DIR/"
  if [ $? -ne 0 ]; then
    echo "WARNING: Failed to copy some resources from $SRC_RESOURCES_DIR."
  else
    echo "Resources from $SRC_RESOURCES_DIR copied."
  fi
else
  echo "No resources found in $SRC_RESOURCES_DIR or directory does not exist/is empty."
fi

# Non-standard resource directory (src/main/resource - singular)
if [ -d "$SRC_RESOURCE_SINGULAR_DIR" ] && [ "$(ls -A "$SRC_RESOURCE_SINGULAR_DIR" 2>/dev/null)" ]; then # Check if dir exists and is not empty
  echo "Copying resources from $SRC_RESOURCE_SINGULAR_DIR to $OUTPUT_DIR..."
  cp -R "$SRC_RESOURCE_SINGULAR_DIR/." "$OUTPUT_DIR/"
  if [ $? -ne 0 ]; then
    echo "WARNING: Failed to copy some resources from $SRC_RESOURCE_SINGULAR_DIR."
  else
    echo "Resources from $SRC_RESOURCE_SINGULAR_DIR copied."
  fi
else
  echo "No resources found in $SRC_RESOURCE_SINGULAR_DIR or directory does not exist/is empty."
fi
echo ""


# 5. Run the application
echo "Running application: $MAIN_CLASS"
echo "Classpath: $OUTPUT_DIR"
echo "--- Application Output Starts ---"
# "$@" passes all arguments given to the script to the Java application
# "$OUTPUT_DIR" needs to be quoted here as well for the -cp argument.
java -cp "$OUTPUT_DIR" "$MAIN_CLASS" "$@"
# Check exit status of java command (optional)
# java_exit_status=$?
# if [ $java_exit_status -ne 0 ]; then
#   echo "WARNING: Java application exited with status $java_exit_status."
# fi

echo "--- Application Output Ends ---"
echo ""

echo "Build and Run process finished."