package com.shapeville.main;

import javax.swing.SwingUtilities;

/**
 * Main entry point for the Shapeville v3 application.
 * This class initializes and displays the main application window.
 */
public class MainApp {
    /**
     * Private constructor to prevent instantiation.
     */
    private MainApp() {}

    /**
     * The main method to start the Shapeville application.
     * It creates and displays the MainFrame on the Event Dispatch Thread.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
