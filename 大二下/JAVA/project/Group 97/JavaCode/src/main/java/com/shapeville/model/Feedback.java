package com.shapeville.model;

/**
 * Represents the feedback provided to the user after they submit an answer to a task problem.
 * It contains information about the correctness of the answer, points earned, a message,
 * the correct answer (if needed), and flags to control the task flow (proceed to next problem or task type complete).
 */
public class Feedback {
    private final boolean correct;
    private final int pointsEarned;
    private final String message;
    private final String correctAnswerToDisplay; // The correct answer (if incorrect and should be shown)
    private final boolean proceedToNextProblem; // If true, current problem is done, move to next OR end task type
    private final boolean taskTypeComplete; // If true, this feedback concludes the entire task type (e.g., all 4 shapes done)

    /**
     * Constructs a new Feedback object.
     * @param correct True if the user's answer was correct, false otherwise.
     * @param pointsEarned The number of points earned for this attempt.
     * @param message A textual message providing feedback to the user.
     * @param correctAnswerToDisplay The correct answer to display to the user, or null if not applicable.
     * @param proceedToNextProblem True if the task should proceed to the next problem after this feedback, false otherwise.
     * @param taskTypeComplete True if completing this problem means the entire task type is finished, false otherwise.
     */
    public Feedback(boolean correct, int pointsEarned, String message, String correctAnswerToDisplay, boolean proceedToNextProblem, boolean taskTypeComplete) {
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.message = message;
        this.correctAnswerToDisplay = correctAnswerToDisplay;
        this.proceedToNextProblem = proceedToNextProblem;
        this.taskTypeComplete = taskTypeComplete;
    }

    /**
     * Checks if the user's answer was correct.
     * @return True if the answer was correct, false otherwise.
     */
     public boolean isCorrect() { return correct; }

    /**
     * Gets the number of points earned for this attempt.
     * @return The points earned.
     */
     public int getPointsEarned() { return pointsEarned; }

    /**
     * Gets the textual feedback message.
     * @return The feedback message.
     */
     public String getMessage() { return message; }

    /**
     * Gets the correct answer to display to the user.
     * @return The correct answer string, or null if not applicable.
     */
     public String getCorrectAnswerToDisplay() { return correctAnswerToDisplay; }

    /**
     * Checks if the task should proceed to the next problem after this feedback.
     * @return True if the task should proceed, false otherwise.
     */
     public boolean shouldProceedToNextProblem() { return proceedToNextProblem; }

    /**
     * Checks if this feedback signifies the completion of the entire task type.
     * @return True if the task type is complete, false otherwise.
     */
     public boolean isTaskTypeComplete() { return taskTypeComplete; }
}
