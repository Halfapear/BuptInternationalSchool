package com.shapeville.utils;

/**
 * Utility class containing constant values used throughout the Shapeville application.
 * This class should not be instantiated.
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {
        // Prevent instantiation
    }

    /**
     * --- Panel Identifiers (for CardLayout and TaskDefinition.panelId) ---
     * These constants define the unique String identifiers for the different UI panels used in the application.
     * Used by {@link com.shapeville.main.MainFrame} and {@link com.shapeville.logic.TaskManager} for switching panels.
     */
    /** Home screen panel ID. */
    public static final String HOME_PANEL_ID = "HOME_PANEL";
    /** End screen panel ID. */
    public static final String END_PANEL_ID = "END_PANEL";
    /** Shape identification task panel ID. */
    public static final String SHAPE_IDENTIFICATION_PANEL_ID = "SHAPE_ID_PANEL";
    /** Angle type identification task panel ID. */
    public static final String ANGLE_TYPE_PANEL_ID = "ANGLE_TYPE_PANEL";
    /** Area calculation task panel ID. */
    public static final String AREA_CALC_PANEL_ID = "AREA_CALC_PANEL";
    /** Circle calculation task panel ID. */
    public static final String CIRCLE_CALC_PANEL_ID = "CIRCLE_CALC_PANEL";
    /** Compound area calculation task panel ID. */
    public static final String COMPOUND_AREA_PANEL_ID = "COMPOUND_AREA_PANEL";
    /** Sector and circle calculation task panel ID. */
    public static final String SECTOR_CIRCLE_CALC_PANEL_ID = "SECTOR_CIRCLE_CALC_PANEL";
    // TODO: Add Panel IDs for Task 3, 4, Bonus 1, 2

    /**
     * --- Task Type Identifiers (for TaskDefinition.taskType and TaskManager logic mapping) ---
     * These constants categorize tasks by their type (e.g., shape identification, area calculation).
     * Used by {@link com.shapeville.model.TaskDefinition} and {@link com.shapeville.logic.TaskManager}.
     */
    /** Task type for 2D shape identification. */
    public static final String TASK_TYPE_SHAPE_IDENTIFICATION_2D = "SHAPE_ID_2D"; // Task 1
    /** Task type for 3D shape identification. */
    public static final String TASK_TYPE_SHAPE_IDENTIFICATION_3D = "SHAPE_ID_3D";// Task 1

    /** Task type for angle identification. */
    public static final String TASK_TYPE_ANGLE_IDENTIFICATION = "ANGLE_ID";// Task 2
    /** Task type for area calculation. */
    public static final String TASK_TYPE_AREA_CALC = "AREA_CALC";// Task 3
    /** Task type for circle calculation. */
    public static final String TASK_TYPE_CIRCLE_CALC = "CIRCLE_CALC";// Task 4
    /** Task type for compound area calculation. */
    public static final String TASK_TYPE_COMPOUND_AREA_CALC = "COMPOUND_AREA";// Bonus 1
    /** Task type for sector and circle calculation. */
    public static final String TASK_TYPE_SECTOR_CIRCLE_CALC = "SECTOR_CIRCLE";// Bonus 2
    // TODO: Add Task Types for Task 3, 4, Bonus 1, 2

    /**
     * --- Task Identifiers (for TaskDefinition.taskId and HomeScreen buttons) ---
     * These are unique IDs for specific task instances or configurations that can be started directly,
     * e.g., from the home screen buttons.
     * Used by {@link com.shapeville.model.TaskDefinition} and {@link com.shapeville.ui.panel_templates.HomeScreenPanel}.
     */
    // These are unique IDs for specific task instances/configurations that can be started.
    /** Task ID for 2D shape identification. */
    public static final String TASK_ID_SHAPE_ID_2D = "TASK_SHAPE_ID_2D"; // User selects 2D shapes
    /** Task ID for 3D shape identification. */
    public static final String TASK_ID_SHAPE_ID_3D = "TASK_SHAPE_ID_3D"; // User selects 3D shapes

    /** Task ID for angle type identification. */
    public static final String TASK_ID_ANGLE_TYPE = "TASK_ANGLE_TYPE";
    /** Task ID for area calculation. */
    public static final String TASK_ID_AREA_CALC = "TASK_AREA_CALC";
    /** Task ID for circle calculation. */
    public static final String TASK_ID_CIRCLE_CALC = "TASK_CIRCLE_CALC";
    /** Task ID for compound area calculation. */
    public static final String TASK_ID_COMPOUND_AREA_CALC = "TASK_COMPOUND_AREA";
    /** Task ID for sector and circle calculation. */
    public static final String TASK_ID_SECTOR_CIRCLE_CALC = "TASK_SECTOR_CIRCLE";
    // TODO: Add Task IDs for Task 3, 4, Bonus 1, 2 (e.g., "TASK_AREA_RECT", "TASK_AREA_TRIANGLE")

    /**
     * --- Scoring levels ---
     * Constants defining the scoring levels (Basic and Advanced) as per the project specification.
     * Used by {@link com.shapeville.logic.ScoreManager}.
     */
    /** Scoring level: Advanced. */
    public static final boolean SCORE_ADVANCED = true;
    /** Scoring level: Basic. */
    public static final boolean SCORE_BASIC = false;

    /**
     * --- Default settings ---
     * Constants for default configuration values used in tasks.
     */
    /** Default maximum number of attempts allowed for a problem. */
    public static final int DEFAULT_MAX_ATTEMPTS = 3;
    /** Number of shapes presented in a shape identification quiz. */
    public static final int SHAPES_PER_IDENTIFICATION_QUIZ = 4;
    /** Number of angles presented in an angle identification quiz. */
    public static final int ANGLES_PER_IDENTIFICATION_QUIZ = 4;
    // TODO: Add other constants (e.g., time limits in milliseconds)

    /**
     * The default time limit for timed tasks in seconds.
     */
    public static final int TASK_TIME_LIMIT_SEC = 180;  // 3 minutes in seconds

}

