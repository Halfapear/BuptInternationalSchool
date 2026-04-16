package com.shapeville.logic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.shapeville.main.MainFrame;
import com.shapeville.model.TaskDefinition;
import com.shapeville.task.bonus.Compound;
import com.shapeville.task.bonus.Sector;
import com.shapeville.task.sk1.Task1Panel2D;
import com.shapeville.task.sk1.Task1Panel3D;
import com.shapeville.task.sk1.Task2Panel;
import com.shapeville.task.sk2.Task3Panel;
import com.shapeville.task.sk2.Task4Panel;
import com.shapeville.ui.panel_templates.TaskPanel;
import com.shapeville.utils.Constants;

/**
 * Manages the sequence and logic flow of different tasks within the Shapeville application.
 * Handles task loading, switching between task panels, and interaction with {@link MainFrame} and {@link ScoreManager}.
 * Defines the master list of available tasks and the session sequence.
 */
public class TaskManager {
    private MainFrame mainFrameRef;
    private ScoreManager scoreManagerRef;
    private List<TaskDefinition> masterTaskList; // All possible tasks
    private TaskLogic currentActiveTaskLogic;
    private JPanel currentActiveTaskPanel;       // The UI panel (which should implement TaskPanel)
    private String currentPanelId; // ID of the currently displayed task panel

    // To manage the overall session progress (sequence of different task types)
    private List<String> sessionTaskSequenceIds; // e.g., [TASK_ID_SHAPE_ID_2D, TASK_ID_ANGLE_TYPE, ...]
    private int currentSessionTaskIndex;


    /**
     * Constructs a new TaskManager.
     * Initializes task lists and sets references to the main frame and score manager.
     * Defines the master list of all available tasks.
     * @param mainFrame The reference to the main application frame ({@link MainFrame}).
     * @param scoreManager The reference to the score manager ({@link ScoreManager}).
     */
    public TaskManager(MainFrame mainFrame, ScoreManager scoreManager) {
        this.mainFrameRef = mainFrame;
        this.scoreManagerRef = scoreManager;
        this.masterTaskList = new ArrayList<>();
        this.sessionTaskSequenceIds = new ArrayList<>();
        this.currentSessionTaskIndex = -1;
        defineMasterTasks();
        //defineDefaultSessionSequence(); // Define the flow for a "full game"
    }

    /**
     * Defines the master list of all available tasks in the application.
     * Each task is represented by a {@link TaskDefinition}.
     */
    private void defineMasterTasks() {

        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_SHAPE_ID_2D, Constants.SHAPE_IDENTIFICATION_PANEL_ID, Constants.TASK_TYPE_SHAPE_IDENTIFICATION_2D, Constants.SCORE_BASIC));
        
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_SHAPE_ID_3D, Constants.SHAPE_IDENTIFICATION_PANEL_ID, Constants.TASK_TYPE_SHAPE_IDENTIFICATION_3D, Constants.SCORE_ADVANCED));
        // Task 2: Angle Type Identification
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_ANGLE_TYPE, Constants.ANGLE_TYPE_PANEL_ID, Constants.TASK_TYPE_ANGLE_IDENTIFICATION, Constants.SCORE_BASIC));
        // Task 3: Area Calculation
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_AREA_CALC, Constants.AREA_CALC_PANEL_ID, Constants.TASK_TYPE_AREA_CALC, Constants.SCORE_BASIC));
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_CIRCLE_CALC, Constants.CIRCLE_CALC_PANEL_ID, Constants.TASK_TYPE_CIRCLE_CALC, Constants.SCORE_BASIC));
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_COMPOUND_AREA_CALC, Constants.COMPOUND_AREA_PANEL_ID, Constants.TASK_TYPE_COMPOUND_AREA_CALC, Constants.SCORE_ADVANCED));
        masterTaskList.add(new TaskDefinition(Constants.TASK_ID_SECTOR_CIRCLE_CALC, Constants.SECTOR_CIRCLE_CALC_PANEL_ID, Constants.TASK_TYPE_SECTOR_CIRCLE_CALC, Constants.SCORE_ADVANCED));

    }

    private void defineDefaultSessionSequence() {
        // Defines a typical flow if user plays through everything or a "Start Game" button
        // For now, let's just add a couple.
        sessionTaskSequenceIds.add(Constants.TASK_ID_SHAPE_ID_2D);
        sessionTaskSequenceIds.add(Constants.TASK_ID_ANGLE_TYPE);
        // TODO: Add other task IDs to define the full game sequence
        System.out.println("Default session sequence defined with " + sessionTaskSequenceIds.size() + " tasks.");
        // Update progress bar max based on this sequence
        if (mainFrameRef != null && mainFrameRef.getScoreManager() != null && masterTaskList.size() > 0) {
             // Bit of a circular dependency for nav bar update, but can be managed
             // Or pass total tasks to NavigationBar constructor or an update method
             mainFrameRef.getScoreManager().setNavigationBar(mainFrameRef.getNavigationBar()); // ensure nav is set
             mainFrameRef.getNavigationBar().updateProgress(0, sessionTaskSequenceIds.size());
        }
    }

    /**
     * Starts a new full session, resetting the score and beginning the task sequence defined by {@code sessionTaskSequenceIds}.
     */
    public void startFullSessionSequence() {
        System.out.println("Starting full session task sequence...");
        scoreManagerRef.resetSession();
        currentSessionTaskIndex = -1;
        loadNextTaskInSequence();
    }

    /**
     * Starts a specific task identified by its ID, bypassing the default session sequence.
     * Loads the corresponding UI panel and task logic.
     * @param taskId The unique ID of the task to start.
     */
    public void startSpecificTask(String taskId) {
        System.out.println("Attempting to start specific task: " + taskId);
        TaskDefinition taskDefToStart = findTaskDefinitionById(taskId);

        if (taskDefToStart != null) {
            // scoreManagerRef.resetSession(); // Decide if starting a specific task resets overall session score/progress
            loadTaskUIAndLogic(taskDefToStart);
        } else {
            System.err.println("Error: Task definition not found for Task ID: " + taskId);
            JOptionPane.showMessageDialog(mainFrameRef, "Error: Could not load task " + taskId, "Task Error", JOptionPane.ERROR_MESSAGE);
            mainFrameRef.navigateToHome();
        }
    }

    /**
     * Finds a {@link TaskDefinition} object from the master task list based on its unique task ID.
     * @param taskId The unique ID of the task to find.
     * @return The {@link TaskDefinition} corresponding to the ID, or null if not found.
     */
    private TaskDefinition findTaskDefinitionById(String taskId) {
        for (TaskDefinition td : masterTaskList) {
            if (td.getTaskId().equals(taskId)) {
                return td;
            }
        }
        return null;
    }
    // Removed misplaced line as it is outside any method or block


    /**
     * Loads the appropriate UI panel and logic for a given task definition.
     * Instantiates the panel and logic based on the task type.
     * Registers and shows the panel in the main frame.
     * @param taskDef The {@link TaskDefinition} for the task to load.
     */
    private void loadTaskUIAndLogic(TaskDefinition taskDef) {
        System.out.println("Loading UI and Logic for task: " + taskDef.getTaskId() + " - Type: " + taskDef.getTaskType());
        currentActiveTaskLogic = null;
        currentActiveTaskPanel = null; 
        currentPanelId = taskDef.getPanelId();

        try {
            if (taskDef.getTaskId().equals(Constants.TASK_ID_SHAPE_ID_2D)) {
                // Task 1: Shape Identification (2D)
currentActiveTaskPanel = new Task1Panel2D(mainFrameRef);
            } 
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_SHAPE_ID_3D)) {
                // Task 1: Shape Identification (3D)
                currentActiveTaskPanel = new Task1Panel3D(mainFrameRef);
            } 
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_ANGLE_TYPE)) {
                // Task 2: Angle Type Identification
                currentActiveTaskPanel = new Task2Panel(mainFrameRef);
            } 
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_AREA_CALC)) {
                // Task 3 – Shapes area
                currentActiveTaskPanel = new Task3Panel(mainFrameRef);
            }
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_CIRCLE_CALC)) {
                // Task 4 – Circle area / circumference
                currentActiveTaskPanel = new Task4Panel(mainFrameRef);
            }
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_COMPOUND_AREA_CALC)) {
                // Bonus1 – Compound Shapes Area Calculation
                currentActiveTaskPanel = new Compound();
            }
            else if (taskDef.getTaskId().equals(Constants.TASK_ID_SECTOR_CIRCLE_CALC)) {
                // Bonus2 – Sector of a Circle Area
                currentActiveTaskPanel = new Sector();
            } 
            else {
                // Fallback for tasks that are not implemented yet
                JPanel placeholderPanel = new JPanel();
                placeholderPanel.add(new JLabel("Task UI Placeholder: " + taskDef.getTaskType()));
                currentActiveTaskPanel = placeholderPanel;
            }

            mainFrameRef.registerPanel(currentPanelId, currentActiveTaskPanel);
            mainFrameRef.showPanel(currentPanelId);
        } catch (Exception e) {
            System.err.println("Exception while loading task " + taskDef.getTaskId() + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrameRef, "Critical error loading task: " + taskDef.getTaskId(), "Error", JOptionPane.ERROR_MESSAGE);
            mainFrameRef.navigateToHome();
        }
    }
    

    /**
     * Called when a task type (a set of questions within a panel) is completed by the user.
     * Increments the overall session progress and loads the next task in the sequence if applicable.
     * If not in a sequence or the sequence ends, navigates back to the home screen.
     * @param completedLogic The {@link TaskLogic} instance that was just completed.
     */
    public void currentTaskTypeCompleted(TaskLogic completedLogic) {
        System.out.println("Task type completed: " + completedLogic.getClass().getSimpleName());
        scoreManagerRef.incrementTaskTypeCompletedCount(); // Increment overall session progress

        // If we are in a sequence, load the next one.
        // If a specific task was started, this might mean returning to home or a summary.
        // For now, assume it means load next in sequence if sequence was started.
        if (currentSessionTaskIndex != -1 && currentSessionTaskIndex < sessionTaskSequenceIds.size()) {
             loadNextTaskInSequence();
        } else {
             // Specific task finished, or sequence ended unexpectedly
             System.out.println("Specific task type finished or sequence ended. Navigating home.");
             // Maybe show a "Task Set Complete!" dialog before going home?
             JOptionPane.showMessageDialog(mainFrameRef, "You've completed this task set!", "Task Set Complete", JOptionPane.INFORMATION_MESSAGE);
             mainFrameRef.navigateToHome();
        }
    }

    /**
     * Loads the next task in the predefined session sequence.
     * Updates the progress bar in the navigation bar.
     * If the sequence is finished, ends the session.
     */
    private void loadNextTaskInSequence() {
        currentSessionTaskIndex++;
        if (currentSessionTaskIndex < sessionTaskSequenceIds.size()) {
            String nextTaskId = sessionTaskSequenceIds.get(currentSessionTaskIndex);
            TaskDefinition nextTaskDef = findTaskDefinitionById(nextTaskId);
            if (nextTaskDef != null) {
                loadTaskUIAndLogic(nextTaskDef);
                // Update progress bar display via ScoreManager or direct call if MainFrame has method
                mainFrameRef.getNavigationBar().updateProgress(currentSessionTaskIndex + 1, sessionTaskSequenceIds.size());
            } else {
                System.err.println("Error: Next task in sequence not found: " + nextTaskId);
                mainFrameRef.navigateToHome(); // Or handle error
                
            }
        } else {
            System.out.println("Entire session task sequence completed!");
            mainFrameRef.endSession(); // All predefined tasks in sequence are done
            mainFrameRef.showPanel(Constants.END_PANEL_ID);
        }
    }


    /**
     * Handles the interruption of the current task (e.g., user navigates away).
     * Resets the state of the current task panel and logic.
     */
    public void currentTaskInterrupted() {
        System.out.println("Current task interrupted.");
        if (currentActiveTaskPanel instanceof TaskPanel) {
            ((TaskPanel) currentActiveTaskPanel).resetState();
        }
        // TODO: Add any cleanup logic for the currentActiveTaskLogic if needed (e.g., stop timers)
        // if (currentActiveTaskLogic != null && currentActiveTaskLogic instanceof SomeTimerInterface) {
        //    ((SomeTimerInterface)currentActiveTaskLogic).stopTimer();
        // }
        currentActiveTaskLogic = null;
        currentActiveTaskPanel = null;
        currentPanelId = null;
        // Resetting currentSessionTaskIndex to -1 means if user clicks "Start Game" again, it starts from beginning.
        // If they click a specific task, that task starts. This seems reasonable.
        currentSessionTaskIndex = -1;
    }

    /**
     * Sets the task sequence for the session. This method can be customized to define a different order of tasks.
     * Currently provides an empty implementation.
     */
    public void setTaskSequence() {
        // 可以根据需要自定义任务序列，目前留空
    }

    /**
     * Resets the current task state. This is an alias for {@code currentTaskInterrupted()}.
     * Provided for compatibility with external calls.
     */
    public void resetCurrentTask() {
        currentTaskInterrupted();
    }
}
