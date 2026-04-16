package com.shapeville.model;

/**
 * Defines the metadata for a specific task within the Shapeville application.
 * It holds identifiers for the task, its corresponding UI panel, the task type for logic mapping,
 * and whether the task uses advanced scoring.
 */
public class TaskDefinition {
    private final String taskId;          // Unique identifier for the task (e.g., Constants.TASK_ID_SHAPE_ID_2D)
    private final String panelId;         // Identifier used by CardLayout in MainFrame (e.g., Constants.SHAPE_ID_PANEL_ID)
    private final String taskType;        // Type identifier used by TaskManager to load correct logic (e.g., Constants.TASK_TYPE_SHAPE_ID_2D)
    private final boolean isAdvancedScoring; // Does this task use advanced scoring table?

    /**
     * Constructs a new TaskDefinition.
     * @param taskId A unique identifier for this specific task.
     * @param panelId The ID of the UI panel associated with this task.
     * @param taskType The type of the task, used for logic mapping.
     * @param isAdvancedScoring True if this task uses the advanced scoring rules, false otherwise.
     */
    public TaskDefinition(String taskId, String panelId, String taskType, boolean isAdvancedScoring) {
        this.taskId = taskId;
        this.panelId = panelId;
        this.taskType = taskType;
        this.isAdvancedScoring = isAdvancedScoring;
    }

    /**
     * Gets the unique identifier for the task.
     * @return The task ID.
     */
     public String getTaskId() { return taskId; }

    /**
     * Gets the identifier for the UI panel associated with this task.
     * @return The panel ID.
     */
     public String getPanelId() { return panelId; }

    /**
     * Gets the type identifier for the task.
     * @return The task type.
     */
     public String getTaskType() { return taskType; }

    /**
     * Checks if the task uses advanced scoring.
     * @return True if advanced scoring is used, false otherwise.
     */
     public boolean isAdvancedScoring() { return isAdvancedScoring; }
}