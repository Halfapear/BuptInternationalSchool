package com.shapeville.ui.panel_templates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.shapeville.main.MainFrame;
import com.shapeville.utils.Constants;

/**
 * The HomeScreenPanel class represents the initial screen of the Shapeville application.
 * It displays a welcome message and provides buttons for the user to select and start different learning tasks.
 * Interacts with the {@link MainFrame} to initiate tasks via the {@link com.shapeville.logic.TaskManager}.
 */
public class HomeScreenPanel extends JPanel {
    /** Reference to the main application frame. */
    private MainFrame mainFrameRef;

    /**
     * Constructs a new HomeScreenPanel.
     * Sets up the basic layout, background color, title, and instructions.
     * Initializes the task selection buttons.
     * @param mainFrame A reference to the main application frame ({@link MainFrame}).
     */
    public HomeScreenPanel(MainFrame mainFrame) {
        this.mainFrameRef = mainFrame;
        setBackground(new Color(30, 30, 30));

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JLabel title = new JLabel("Welcome to Shapeville!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        initializeUI();

        JLabel instructions = new JLabel("Click a task to start learning!", SwingConstants.CENTER);
        instructions.setFont(new Font("Arial", Font.ITALIC, 14));
        instructions.setForeground(Color.YELLOW);
        add(instructions, BorderLayout.SOUTH);
    }

    /**
     * Initializes the main UI components of the home screen.
     * Creates a panel to hold the task selection buttons and adds buttons for each available task.
     */
    private void initializeUI() {
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tasksPanel.setBackground(new Color(50, 50, 50));
        
        tasksPanel.add(createTaskButton("(Basic) 2D Shape Identification (KS1-TASK1)", Constants.TASK_ID_SHAPE_ID_2D));
        tasksPanel.add(createTaskButton("(Advanced) 3D Shape Identification (KS1-TASK1)", Constants.TASK_ID_SHAPE_ID_3D));
        tasksPanel.add(createTaskButton("(Basic) Angle Recognition (KS1-TASK2)", Constants.TASK_ID_ANGLE_TYPE));
        tasksPanel.add(createTaskButton("(Basic) Area Calculation (KS2-TASK3)", Constants.TASK_ID_AREA_CALC));
        tasksPanel.add(createTaskButton("(Basic) Circle Calculation (KS2-TASK4)", Constants.TASK_ID_CIRCLE_CALC));
        tasksPanel.add(createTaskButton("(Advanced) Compound Area (BONUS1)", Constants.TASK_ID_COMPOUND_AREA_CALC));
        tasksPanel.add(createTaskButton("(Advanced) Sector Circle Calculation (BONUS2)", Constants.TASK_ID_SECTOR_CIRCLE_CALC));

        add(tasksPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a styled JButton for selecting a specific task.
     * Adds an action listener to the button that triggers the start of the corresponding task
     * via the {@link com.shapeville.logic.TaskManager}.
     * @param label The text label to display on the button.
     * @param taskId The unique ID of the task associated with this button (from {@link Constants}).
     * @return The configured JButton.
     */
    private JButton createTaskButton(String label, String taskId) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setMaximumSize(new Dimension(500, 50));
        button.addActionListener(e -> mainFrameRef.getTaskManager().startSpecificTask(taskId));
        return button;
    }
}
