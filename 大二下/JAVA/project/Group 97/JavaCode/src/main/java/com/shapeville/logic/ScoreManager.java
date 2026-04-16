package com.shapeville.logic;

import javax.swing.JOptionPane;

import com.shapeville.ui.NavigationBar; // For "Great Job!"

/**
 * Manages the user's score and progress within the Shapeville application session.
 * Calculates points based on task attempts and provides feedback.
 * Interacts with the {@link NavigationBar} to display updated scores.
 */
public class ScoreManager {
    private int currentScore = 0;
    private int tasksOfTypeCompletedInSession; // Renamed for clarity
    private NavigationBar navigationBar;

    /**
     * Constructs a new ScoreManager and resets the session score and task count.
     */
    public ScoreManager() {
        resetSession();
    }

    /**
     * Sets the NavigationBar instance to allow updating the UI with score changes.
     * @param navigationBar The {@link NavigationBar} instance.
     */
    public void setNavigationBar(NavigationBar navigationBar) {
        this.navigationBar = navigationBar;
        updateUI();
    }

    /**
     * Calculates points earned for a task based on the number of attempts used and task type.
     * Points are awarded according to the scoring table in the project specification.
     * @param attemptsUsed The number of attempts the user took to answer correctly (1 to 3).
     * @param isAdvancedTask True if the task is an advanced task (KS2 Bonus), false for basic tasks.
     * @return The number of points earned.
     */
    public int calculatePoints(int attemptsUsed, boolean isAdvancedTask) {
        int points = 0;
        if (attemptsUsed < 1 || attemptsUsed > 3) return 0; // Invalid attempts

        if (isAdvancedTask) { // Advanced Scoring from project spec Table 1
            if (attemptsUsed == 1) points = 6;
            else if (attemptsUsed == 2) points = 4;
            else if (attemptsUsed == 3) points = 2;
        } else { // Basic Scoring
            if (attemptsUsed == 1) points = 3;
            else if (attemptsUsed == 2) points = 2;
            else if (attemptsUsed == 3) points = 1;
        }
        return points;
    }

    /**
     * Records the points earned and displays a feedback message to the user.
     * Only adds points and shows feedback if pointsToAdd is greater than 0.
     * Updates the UI after recording the score.
     * @param pointsToAdd The number of points to add to the current score.
     */
    public void recordScoreAndFeedback(int pointsToAdd) {
        if (pointsToAdd > 0) {
            this.currentScore += pointsToAdd;
            // Display "Great job!" message as per project spec
            JOptionPane.showMessageDialog(null, // Parent component (can be MainFrame if passed)
                    "Great job! You earned " + pointsToAdd + " points!",
                    "Feedback",
                    JOptionPane.INFORMATION_MESSAGE);
            updateUI();
        }
    }

    /**
     * Increments the count of completed task types within the current session.
     * Updates the UI after incrementing the count.
     */
    public void incrementTaskTypeCompletedCount() {
        this.tasksOfTypeCompletedInSession++;
        updateUI();
    }

    /**
     * Gets the current total score of the user in the session.
     * @return The current score.
     */
    public int getCurrentScore() { return currentScore; }
    //public int getCurrentScore() { return 2; }

    /**
     * Gets the count of distinct task types completed in the current session.
     * @return The number of completed task types.
     */
    public int getTasksOfTypeCompletedCount() { return tasksOfTypeCompletedInSession; }

    /**
     * Resets the current score and the completed task type count to zero.
     * Also updates the UI if the navigation bar is set.
     */
    public void resetSession() {
        this.currentScore = 0;
        this.tasksOfTypeCompletedInSession = 0;
        if (navigationBar != null) { // Ensure nav bar exists before updating
            updateUI();
        }
    }

    /**
     * Updates the UI elements (specifically the NavigationBar) to reflect the current score and progress.
     * This method is called internally after score or progress changes.
     */
    private void updateUI() {
        if (navigationBar != null) {
            navigationBar.updateScore();
            // Progress bar update needs total number of tasks in the *entire session flow*
            // This might be managed by TaskManager
            // For now, let's assume TaskManager informs NavigationBar directly or via MainFrame
        }
    }

}