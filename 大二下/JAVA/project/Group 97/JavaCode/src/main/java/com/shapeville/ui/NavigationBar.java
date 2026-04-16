package com.shapeville.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.shapeville.logic.ScoreManager;
import com.shapeville.main.MainFrame;

/**
 * The NavigationBar class represents the top navigation bar of the Shapeville application.
 * It displays the user's current score and task progress.
 * It also contains buttons to navigate back to the home screen and to end the current session.
 * This bar interacts with the {@link MainFrame} for navigation and {@link ScoreManager} for score display.
 */
public class NavigationBar extends JPanel {
    /** Label to display the current score. */
    private JLabel scoreLabel;
    /** Button to navigate back to the home screen. */
    private JButton homeButton;
    /** Button to end the current session. */
    private JButton endSessionButton;
    /** Progress bar to show task completion status. */
    private JProgressBar progressBar;
    /** Reference to the MainFrame for triggering navigation actions. */
    private MainFrame mainFrameRef;
    /** Reference to the ScoreManager for accessing score data. */
    private ScoreManager scoreManager;

    /**
     * Constructs a new NavigationBar.
     * Initializes the UI components and sets up action listeners for the buttons.
     *
     * @param mainFrame    A reference to the main application frame ({@link MainFrame}).
     * @param scoreManager A reference to the score manager ({@link ScoreManager}) to display the score.
     */
    public NavigationBar(MainFrame mainFrame, ScoreManager scoreManager) {
        this.mainFrameRef = mainFrame;
        this.scoreManager = scoreManager; // Initialize ScoreManager
        initializeUI();
    }

    /**
     * Initializes the user interface components of the navigation bar.
     * Sets up the layout, preferred size, background color, labels, progress bar, and buttons.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 50));
        setBackground(new Color(200, 200, 200)); // 灰色背景

        // Left side: Score display
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(scoreLabel, BorderLayout.WEST);

        // Center: Progress bar
        progressBar = new JProgressBar(0, 100); // Default max value is 100
        progressBar.setStringPainted(true); // Show percentage
        add(progressBar, BorderLayout.CENTER);

        // Right side: Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        homeButton = new JButton("Home");
        endSessionButton = new JButton("End Session");

        // Add button listeners
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrameRef.navigateToHome();
            }
        });

        endSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrameRef.endSession();
            }
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(endSessionButton);
        add(buttonPanel, BorderLayout.EAST);
    }

    /**
     * Updates the displayed score on the navigation bar.
     * Retrieves the current score from the {@link ScoreManager} and updates the score label.
     */
    public void updateScore() {
        int score = scoreManager.getCurrentScore(); 
        scoreLabel.setText("Score: " + score);
        //scoreLabel.setText("Score: " + 1);
    }

    /**
     * Updates the progress bar to show the current task completion status.
     * The progress is displayed as a percentage and a fraction (e.g., "3/5").
     *
     * @param current The current number of tasks completed in the sequence.
     * @param total   The total number of tasks in the current sequence.
     */
    public void updateProgress(int current, int total) {
        if (total > 0) {
            int progress = (int) ((current / (double) total) * 100);
            progressBar.setValue(progress);
            progressBar.setString(current + "/" + total);
        } else {
            progressBar.setValue(0);
            progressBar.setString("0/0");
        }
    }
}