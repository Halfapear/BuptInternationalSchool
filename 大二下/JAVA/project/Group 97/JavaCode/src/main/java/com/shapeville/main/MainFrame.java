package com.shapeville.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane; // Assuming TaskPanel is an interface or class Panels implement/extend
import javax.swing.JPanel;

import com.shapeville.logic.ScoreManager;
import com.shapeville.logic.TaskManager;
import com.shapeville.ui.NavigationBar;
import com.shapeville.ui.panel_templates.EndPanel;
import com.shapeville.ui.panel_templates.HomeScreenPanel;
import com.shapeville.ui.panel_templates.TaskPanel;
import com.shapeville.utils.Constants;

/**
 * The main application window (JFrame) for Shapeville v3.
 * This class sets up the main frame, including the navigation bar and a content area managed by CardLayout.
 * It holds and initializes core application managers such as {@link com.shapeville.logic.TaskManager} and {@link com.shapeville.logic.ScoreManager}.
 * It also provides methods for switching between different content panels (screens) of the application.
 */
public class MainFrame extends JFrame {

    /** The CardLayout manager for switching between different panels. */
    private CardLayout cardLayout;
    /** The main panel container that holds all switchable panels (cards). */
    private JPanel contentPaneContainer; // Panel holding all the switchable panels (cards)
    /** The navigation bar component displayed at the top of the frame. */
    private NavigationBar navigationBar;
    /** The score manager instance handling scoring logic. */
    private ScoreManager scoreManager;
    /** The task manager instance handling task flow and logic. */
    private TaskManager taskManager;

    /** A map to store registered panels, with panel IDs as keys and panel instances as values. */
    private Map<String, JPanel> registeredPanels;

    // Store registered panels for easy access if needed, managed by CardLayout
    // private Map<String, JPanel> registeredPanels;

    // Panel Identifiers (Constants are better defined in a dedicated Constants class)
    // Using Constants from com.shapeville.utils.Constants is preferred.
    // public static final String HOME_PANEL_ID = "HOME_PANEL"; // Use Constants.HOME_PANEL_ID
    // public static final String END_PANEL_ID = "END_PANEL"; // Use Constants.END_PANEL_ID
    // Add IDs for all task panels, e.g., public static final String SHAPE_ID_PANEL_ID = "SHAPE_ID";


    /**
     * Constructs the main frame of the Shapeville application.
     * Initializes managers, UI components, registers core panels,
     * sets the task sequence, and displays the home screen.
     */
    public MainFrame() {
        registeredPanels = new HashMap<>();
        initializeCoreManagers();
        initializeUI();
        registerCorePanels(); // Register home and end panels
        taskManager.setTaskSequence(); // Define the order of tasks
        showPanel(Constants.HOME_PANEL_ID); // Show home screen initially
    }

    /**
     * Initializes the core managers of the application: {@link com.shapeville.logic.ScoreManager} and {@link com.shapeville.logic.TaskManager}.
     * Sets up dependencies between managers where necessary.
     */
    private void initializeCoreManagers() {
        // Important: Handle dependencies. ScoreManager might need NavigationBar later.
        // TaskManager needs MainFrame to switch panels.
        /**
         * Initializes the core managers of the application: {@link ScoreManager} and {@link TaskManager}.
         * Sets up dependencies between managers where necessary.
         */
        scoreManager = new ScoreManager();
        taskManager = new TaskManager(this, scoreManager); // Pass dependencies
    }

    /**
     * Initializes the main UI components of the frame.
     * Sets frame properties like title, size, location, and layout.
     * Creates and adds the {@link NavigationBar} and the content container with {@link CardLayout}.
     */
    private void initializeUI() {
        /**
         * Initializes the main UI components of the frame.
         * Sets frame properties like title, size, location, and layout.
         * Creates and adds the {@link NavigationBar} and the content container with {@link CardLayout}.
         */
        setTitle("Shapeville Learning");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650); // Slightly larger height for nav bar + content
        setLocationRelativeTo(null); // Center window
        setResizable(false);

        // Create Navigation Bar (pass reference to this MainFrame for callbacks)
        navigationBar = new NavigationBar(this, scoreManager);
        add(navigationBar, BorderLayout.NORTH);
        scoreManager.setNavigationBar(navigationBar); // Link score manager to UI display

        // Create Content Pane Container with CardLayout
        cardLayout = new CardLayout();
        contentPaneContainer = new JPanel(cardLayout);

        // Add components to the JFrame
        add(navigationBar, BorderLayout.NORTH);
        add(contentPaneContainer, BorderLayout.CENTER);
    }

    /**
     * Registers the core panels (Home and End screens) with the content pane container managed by CardLayout.
     * Task panels are registered dynamically by the TaskManager.
     */
    private void registerCorePanels() {
        /**
         * Registers the core panels (Home and End screens) with the content pane container.
         * Task panels are registered dynamically by the TaskManager.
         */
        registerPanel(Constants.HOME_PANEL_ID, new HomeScreenPanel(this));
        registerPanel(Constants.END_PANEL_ID, new EndPanel(scoreManager));
        // Task panels will be registered dynamically by the TaskManager
    }

    /**
     * Registers a panel (like a task screen or home screen) with the CardLayout.
     *
     * @param panelId A unique String identifier for the panel.
     * @param panel   The JPanel instance to register.
     */
    public void registerPanel(String panelId, JPanel panel) {
        contentPaneContainer.add(panel, panelId);
        registeredPanels.put(panelId, panel);
        System.out.println("Registered panel: " + panelId); // For debugging
    }

    /**
     * Switches the currently displayed panel in the CardLayout to the one identified by the given panel ID.
     * Optionally notifies the panel if it implements the {@link com.shapeville.ui.panel_templates.TaskPanel} interface.
     *
     * @param panelId The unique String identifier of the panel to show.
     */
    public void showPanel(String panelId) {
        System.out.println("Switching to panel: " + panelId); // For debugging
        cardLayout.show(contentPaneContainer, panelId);

        // Optional: Notify the panel it's visible (useful for starting tasks)
         JPanel panelToShow = registeredPanels.get(panelId);
         if (panelToShow instanceof TaskPanel) {
             // ((TaskPanel) panelToShow).onPanelShown(); // If TaskPanel interface has this method
             // The actual task starting logic might be better handled by TaskManager calling
             // the panel's specific startTask method after showing it.
         }
    }

    // --- Navigation Methods (Called by NavigationBar or TaskManager) ---

    /**
     * Navigates the user back to the home screen panel.
     * Resets the progress bar in the navigation bar.
     */
    public void navigateToHome() {
        // TODO: Add logic if leaving a task needs confirmation or state reset
        showPanel(Constants.HOME_PANEL_ID);
        // taskManager.resetCurrentTask(); // Tell TaskManager the flow is interrupted
        navigationBar.updateProgress(0, 0); // Reset progress bar
    }

    /**
     * Ends the current session.
     * Displays the end session panel and prompts the user to close the application.
     */
    public void endSession() {
        Object[] options = {"Confirm", "Cancel"};
        // TODO: Maybe show final score before ending?
        showPanel(Constants.END_PANEL_ID);
        // Optional: Disable navigation buttons on EndPanel?
        // Using JOptionPane to actually close after showing EndPanel might be better UX
        int choice = JOptionPane.showOptionDialog(this,
                "Session ended. Your final score: " + scoreManager.getCurrentScore() + "\nClose application?",
                "End Session",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0] // Default button;
        );
        if (choice == JOptionPane.OK_OPTION) {
             System.exit(0);
        } else {
             // If cancelled, maybe go back home?
             navigateToHome();
        }
    }

    // --- Getters for Managers (Needed by Panels/Logic) ---

    /**
     * Gets the instance of the ScoreManager.
     *
     * @return The {@link com.shapeville.logic.ScoreManager} instance.
     */
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    /**
     * Gets the instance of the TaskManager.
     *
     * @return The {@link com.shapeville.logic.TaskManager} instance.
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }

   
    /**
     * Gets the instance of the NavigationBar.
     *
     * @return The {@link com.shapeville.ui.NavigationBar} instance.
     */
    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
    
    /**
     * Sets the content panel. This method is primarily for compatibility
     * and the actual panel management is handled by registerPanel and showPanel.
     *
     * @param panel The JPanel to be set as the content panel.
     */
    public void setContentPanel(JPanel panel) {
        // No extra action needed here, as registerPanel and showPanel handle panel addition and display
        // This method is just for compatibility with TaskManager calls
        System.out.println("Setting content panel: " + panel.getClass().getSimpleName());
    }
}

