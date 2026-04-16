package com.shapeville.model;

/**
 * A generic container for the data representing a single problem or question presented to the user in a task.
 * The specific content held within depends on the type of task.
 * Used to pass problem details from the task logic to the UI panel.
 */
public class Problem {
    private String taskId;      // Identifier for the task this problem belongs to
    private Object problemData; // Can hold a Shape, Angle, Map<String, Double> for dimensions, etc.
    private String questionText; // The textual question or instruction for this problem

    /**
     * Constructs a new Problem object.
     * @param taskId The identifier for the task this problem belongs to.
     * @param questionText The textual question or instruction for the user.
     * @param problemData An object containing the specific data for this problem (e.g., a Shape object, dimensions).
     */
    public Problem(String taskId, String questionText, Object problemData) {
        this.taskId = taskId;
        this.questionText = questionText;
        this.problemData = problemData;
    }

    /**
     * Gets the identifier of the task this problem belongs to.
     * @return The task ID.
     */
    public String getTaskId() { return taskId;}

    /**
     * Gets the textual question or instruction for this problem.
     * @return The question text.
     */
    public String getQuestionText() { return questionText; }

    /**
     * Gets the raw problem data object.
     * The actual type of this object depends on the specific task.
     * @return The problem data object.
     */
    public Object getProblemData() { return problemData; }

    /**
     * Gets the problem data object cast to a specific type.
     * Use this method to safely retrieve the specific data type expected by the task panel.
     * @param <T> The target type to cast the problem data to.
     * @param type The Class object representing the target type.
     * @return The problem data cast to the target type, or null if the data is null or not an instance of the target type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getProblemDataAs(Class<T> type) {
         if (problemData != null && type.isInstance(problemData)) {
             return (T) problemData;
         }
         return null;
    }
}