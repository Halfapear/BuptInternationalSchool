package com.shapeville.ui.panel_templates;

import com.shapeville.logic.TaskLogic;
import com.shapeville.model.Feedback;
import com.shapeville.model.Problem;

/**
 * Interface defining the contract for all Task UI Panels in the Shapeville application.
 * Any JPanel that represents a task screen should implement this interface to interact with the {@link com.shapeville.logic.TaskManager}.
 */
public interface TaskPanel {
    /**
     * Displays a new problem on the task panel.
     * @param problem The {@link Problem} object containing the problem details.
     */
    void displayProblem(Problem problem);

    /**
     * Displays feedback to the user based on their answer.
     * @param feedback The {@link Feedback} object containing the feedback message and correctness status.
     */
    void showFeedback(Feedback feedback);

    /**
     * Sets the callback reference to the corresponding task logic.
     * This allows the UI panel to notify the logic when user actions occur.
     * @param logic The {@link TaskLogic} instance managing this task's logic.
     */
    void setTaskLogicCallback(TaskLogic logic);

    /**
     * Gets the unique identifier for this task panel.
     * Used by {@link com.shapeville.main.MainFrame} and {@link com.shapeville.logic.TaskManager} for CardLayout management.
     * @return The unique String ID of the panel.
     */
    String getPanelId(); // For CardLayout registration

    /**
     * Resets the UI state of the task panel.
     * This method should clear any previous problem data, user input fields, and feedback messages.
     * Called when moving to the next problem or navigating away from the panel.
     */
    void resetState();   // To clear UI for next problem or when leaving panel
//    void updateProgress(int current, int total); // New method
}
