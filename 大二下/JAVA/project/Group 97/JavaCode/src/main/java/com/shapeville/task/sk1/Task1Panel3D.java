package com.shapeville.task.sk1;

import com.shapeville.main.MainFrame;
import com.shapeville.model.Shape;
import com.shapeville.ui.panel_templates.TaskPanel;
import com.shapeville.utils.Constants;
import com.shapeville.logic.ScoreManager;
import com.shapeville.logic.TaskLogic;
import com.shapeville.model.Feedback;
import com.shapeville.model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Task 1 Panel for 3D Shape Recognition.
 * This panel displays 3D shapes and prompts the user to identify their names.
 * It implements the {@link com.shapeville.ui.panel_templates.TaskPanel} interface to integrate with the application's task management flow.
 */
public class Task1Panel3D extends JPanel implements TaskPanel {
    /** Reference to the main application frame. */
    private MainFrame mainFrame;
    /** Label to display the image of the 3D shape. */
    private JLabel shapeImageLabel;
    /** Text field for user input of the shape name. */
    private JTextField answerTextField;
    /** Button to submit the user's answer. */
    private JButton submitButton;
    /** Label to display feedback or results to the user. */
    private JLabel resultLabel;
    /** Label to display the current score. */
    private JLabel scoreLabel;
    /** Label to display the number of attempts remaining. */
    private JLabel attemptsLabel;
    /** Label prompting the user to enter the shape name. */
    private JLabel promptLabel;
    /** Label to display hints or type information (currently not used). */
    private JLabel typePromptLabel; // New: Shape type prompt label
    /** List of available 3D shapes for the task. */
    private List<Shape> shapes;
    /** The current shape being displayed to the user. */
    private Shape currentShape;
    /** Reference to the score manager for updating the score. */
    private ScoreManager scoreManager;
    /** Number of attempts left for the current problem. */
    private int attemptsLeft = 3;
    /** Number of attempts used for the current problem. */
    private int attemptsUsed = 1;
    /** Number of unique shapes successfully identified in the current session. */
    private int completedShapes = 0;
    /** Container panel for the shape image. */
    private JPanel imageContainer;
    /** Container panel for the input field and submit button. */
    private JPanel inputContainer;
    /** Container panel for feedback and status labels. */
    private JPanel feedbackContainer;

    /**
     * Constructs a new Task1Panel3D.
     * Initializes UI components, loads the list of 3D shapes, and displays the first random shape.
     * @param mainFrame The reference to the main application frame ({@link com.shapeville.main.MainFrame}).
     */
    public Task1Panel3D(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.scoreManager = mainFrame.getScoreManager();
        initializeUI();
        initializeShapes();
        loadRandomShape();
    }

    /**
     * Initializes the user interface components for the 3D shape recognition task.
     * Sets up the layout, adds labels, text field, buttons, and containers.
     * Configures appearance for high contrast and accessibility.
     */
    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // Apply high contrast background to the main panel
        setBackground(new Color(30, 30, 30));

        // Title area (centered)
        JLabel titleLabel = new JLabel("3D Shape Recognition");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // High contrast text
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Fixed size image container (300x300 pixels)
        imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(300, 300));
        imageContainer.setMaximumSize(new Dimension(300, 300));
        imageContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        imageContainer.setBackground(new Color(50, 50, 50)); // Contrast background for container
        
        shapeImageLabel = new JLabel();
        shapeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        shapeImageLabel.setVerticalAlignment(JLabel.CENTER);
        imageContainer.add(shapeImageLabel, BorderLayout.CENTER);
        
        gbc.gridy = 1;
        add(imageContainer, gbc);

        // Centered input area
        inputContainer = new JPanel();
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputContainer.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        promptLabel = new JLabel("Enter the correct shape name:");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        promptLabel.setForeground(Color.WHITE); // High contrast text
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputContainer.add(promptLabel);
        
        answerTextField = new JTextField(20);
        answerTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        answerTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        answerTextField.setBackground(new Color(60, 60, 60)); // High contrast background
        answerTextField.setForeground(Color.WHITE); // High contrast text
        answerTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkAnswer();
                }
            }
        });
        inputContainer.add(answerTextField);
        
        submitButton = new JButton("Submit Answer");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setBackground(new Color(80, 80, 80)); // High contrast background
        submitButton.setForeground(Color.WHITE); // High contrast text
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        inputContainer.add(submitButton);
        
        gbc.gridy = 2;
        add(inputContainer, gbc);

        // Centered feedback area
        feedbackContainer = new JPanel();
        feedbackContainer.setLayout(new BoxLayout(feedbackContainer, BoxLayout.Y_AXIS));
        feedbackContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackContainer.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Color will be set in checkAnswer()
        feedbackContainer.add(resultLabel);
        
        attemptsLabel = new JLabel("Remaining attempts: " + attemptsLeft);
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        attemptsLabel.setForeground(Color.YELLOW); // High contrast text for status
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackContainer.add(attemptsLabel);
        
        scoreLabel = new JLabel("Current Score: " + scoreManager.getCurrentScore());
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.YELLOW); // High contrast text for status
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackContainer.add(scoreLabel);
        
        gbc.gridy = 3;
        gbc.weighty = 1.0; // Allow bottom area to take up remaining space
        add(feedbackContainer, gbc);
    }

    /**
     * Initializes the list of available 3D shapes for the recognition task.
     * Each shape is created with its name, image path, and type (3D).
     */
    private void initializeShapes() {
        shapes = new ArrayList<>();
        
        // Add 3D shapes
        shapes.add(new Shape("cone", "/assets/3d/cone.png", "3D"));
        shapes.add(new Shape("cube", "/assets/3d/cube.png", "3D"));
        shapes.add(new Shape("cuboid", "/assets/3d/cuboid.png", "3D"));
        shapes.add(new Shape("cylinder", "/assets/3d/cylinder.png", "3D"));
        shapes.add(new Shape("sphere", "/assets/3d/sphere.png", "3D"));
        shapes.add(new Shape("square-based pyramid", "/assets/3d/square-based pyramid.png", "3D"));
        shapes.add(new Shape("tetrahedron", "/assets/3d/tetrahedron.png", "3D"));
        shapes.add(new Shape("triangular prism", "/assets/3d/triangular prism.png", "3D"));
    }

    /**
     * Loads a random 3D shape from the list and displays its image.
     * Resets attempt counts and UI feedback elements for the new problem.
     * Checks if the required number of shapes are completed and handles task completion.
     */
    private void loadRandomShape() {
        if (completedShapes >= Constants.SHAPES_PER_IDENTIFICATION_QUIZ) {
            Object[] options = {"Return to Home", "Try Again"};
            int choice = JOptionPane.showOptionDialog(this,
                    "You've completed " + Constants.SHAPES_PER_IDENTIFICATION_QUIZ + 
                    " shape recognitions! Your total score is: " + scoreManager.getCurrentScore() +
                    ". What would you like to do next?",
                    "Task Completed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            
            if (choice == 0) {
                mainFrame.navigateToHome();
            } else {
                resetState();
            }
            scoreManager.incrementTaskTypeCompletedCount();
            return;
        }

        // Update progress bar
        mainFrame.getNavigationBar().updateProgress(completedShapes, Constants.SHAPES_PER_IDENTIFICATION_QUIZ);

        Random random = new Random();
        currentShape = shapes.get(random.nextInt(shapes.size()));
        ImageIcon icon = new ImageIcon(getClass().getResource(currentShape.getImagePath()));
        
        shapeImageLabel.setIcon(null);
        if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            shapeImageLabel.setIcon(icon);
        } else {
            shapeImageLabel.setText("Failed to load image");
        }
        
        attemptsLeft = 3;
        attemptsUsed = 1;
        resultLabel.setText("");
        answerTextField.setText("");
        attemptsLabel.setText("Remaining attempts: " + attemptsLeft);
        submitButton.setEnabled(true);
    }

    /**
     * Checks the user's answer against the correct shape name.
     * Updates the score based on the number of attempts used.
     * Provides feedback to the user and updates the UI.
     * Handles advancing to the next shape or indicating incorrect answer after maximum attempts.
     */
    private void checkAnswer() {
        String userAnswer = answerTextField.getText().trim();
        boolean isCorrect = userAnswer.equalsIgnoreCase(currentShape.getName());
        // int points = attemptsUsed == 1 ? 3 : attemptsUsed == 2 ? 2 : 1;
        int points = scoreManager.calculatePoints(attemptsUsed,true);        
        String message;
        
        if (isCorrect) {
            scoreManager.recordScoreAndFeedback(points);
            completedShapes++;
            message = "Correct! :) +" + points + " points"; // Changed emoji to text symbol
            resultLabel.setForeground(new Color(0, 255, 0)); // Green for correct
        } else {
            attemptsLeft--;
            if (attemptsLeft <= 0) {
                message = "Incorrect! :( The correct answer is: " + currentShape.getName(); // Changed emoji to text symbol
                scoreManager.recordScoreAndFeedback(0);
                completedShapes++;
                resultLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            } else {
                message = "Try again! :( Remaining attempts: " + attemptsLeft; // Changed emoji to text symbol
                attemptsUsed++;
                resultLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            }
        }
        
        resultLabel.setText(message);
        attemptsLabel.setText("Remaining attempts: " + attemptsLeft);
        scoreLabel.setText("Current Score: " + scoreManager.getCurrentScore());
        
        Feedback feedback = new Feedback(
            isCorrect, 
            points, 
            message, 
            currentShape.getName(), 
            true, 
            completedShapes >= Constants.SHAPES_PER_IDENTIFICATION_QUIZ
        );
        showFeedback(feedback);
        
        if (isCorrect || attemptsLeft <= 0) {
            submitButton.setEnabled(false);
            Timer timer = new Timer(1500, e -> loadRandomShape());
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    /**
     * Resets the state of the panel for a new task session or when navigating away.
     * Clears the current shape, resets completed shape count, and updates UI elements to initial state.
     */
    public void resetState() {
        completedShapes = 0;
        scoreManager.resetSession();
        loadRandomShape();
    }

    @Override
    /**
     * Gets the unique identifier for this panel, as defined in {@link Constants}.
     * @return The panel ID string.
     */
    public String getPanelId() {
        return Constants.SHAPE_IDENTIFICATION_PANEL_ID;
    }

    @Override
    /**
     * Sets the callback for task logic. Currently not used in this specific panel, but required by {@link TaskPanel}.
     * @param logic The task logic instance.
     */
    public void setTaskLogicCallback(TaskLogic logic) {
        // 实现任务逻辑回调
    }

    @Override
    /**
     * Displays a problem on the panel. Currently not fully implemented as problem loading is handled internally, but required by {@link TaskPanel}.
     * @param problem The problem to display.
     */
    public void displayProblem(Problem problem) {
        // 显示问题
    }

    @Override
    /**
     * Displays feedback on the panel. Currently not fully implemented as feedback is handled internally, but required by {@link TaskPanel}.
     * @param feedback The feedback to display.
     */
    public void showFeedback(Feedback feedback) {
        resultLabel.setText(feedback.getMessage());
        scoreLabel.setText("Current Score: " + scoreManager.getCurrentScore());
    }
}

