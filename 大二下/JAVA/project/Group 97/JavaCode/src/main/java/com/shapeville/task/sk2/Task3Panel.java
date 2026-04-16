package com.shapeville.task.sk2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.shapeville.main.MainFrame;
import com.shapeville.model.Feedback;
import com.shapeville.logic.ScoreManager;
import com.shapeville.logic.TaskLogic;
import com.shapeville.ui.panel_templates.TaskPanel;
import com.shapeville.utils.Constants;

/**
 * Task 3 Panel for Area Calculation of basic shapes (Rectangle, Parallelogram, Triangle, Trapezium).
 * This panel presents the user with a shape and its dimensions, prompting them to calculate the area.
 * It includes a timer, tracks attempts, provides feedback, and shows the correct formula and calculation steps upon completion or failure.
 * Implements the {@link TaskPanel} interface to integrate with the application's task management flow.
 */
public class Task3Panel extends JPanel implements TaskPanel {
    /** Reference to the main application frame. */
    private MainFrame mainFrameRef;
    /** Buttons for selecting the shape calculation mode. */
    private JButton[] shapeButtons;
    /** Label to display the remaining time for the current problem. */
    private JLabel timeLabel;
    /** Label to display the number of attempts remaining for the current problem. */
    private JLabel attemptsLabel;
    /** Label to display the question or instructions for the current problem. */
    private JLabel questionLabel;
    /** Text field for user input of the calculated area. */
    private JTextField answerField;
    /** Button to submit the user's answer. */
    private JButton submitButton;
    /** Label to display feedback or result messages to the user. */
    private JLabel resultMessageLabel;
    /** Label to display the formula used for the current shape's area calculation. */
    private JLabel formulaLabel;
    /** Timer for the task time limit. */
    private Timer timer;
    /** Remaining time in seconds for the current problem. */
    private int timeRemaining;
    /** Number of attempts used for the current problem. */
    private int attemptsUsed;
    /** Flag indicating if a problem is currently active. */
    private boolean problemActive;
    /** The name of the shape for the current area calculation problem. */
    private String currentShape;
    /** The correct numerical answer for the current problem's area. */
    private double currentCorrectAnswer;
    /** The formula string for the current shape's area calculation. */
    private String currentFormula;
    /** Array of basic shape names supported by this task. */
    private final String[] shapes = {"Rectangle", "Parallelogram", "Triangle", "Trapezium"};
    /** Set to track which shapes have been completed in the current session. */
    private java.util.Set<String> completedShapes;
    /** Custom panel for drawing the shape and labeling dimensions. */
    private ShapeDrawingPanel shapeDrawingPanel;

    /**
     * Constructs a new Task3Panel.
     * Initializes UI components, sets up shape selection buttons, and prepares for the area calculation tasks.
     * @param mainFrame The reference to the main application frame ({@link MainFrame}).
     */
    public Task3Panel(MainFrame mainFrame) {
        this.mainFrameRef = mainFrame;
        this.completedShapes = new java.util.HashSet<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Apply high contrast background to the main panel
        setBackground(new Color(30, 30, 30));

        // Title label
        JLabel titleLabel = new JLabel("Task 3: Area Calculation of Shapes", SwingConstants.CENTER);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // High contrast text
        add(titleLabel);
        add(Box.createVerticalStrut(15));

        // Top panel: shape selection and status
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        topPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel selectLabel = new JLabel("Select shape:");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectLabel.setForeground(Color.WHITE); // High contrast text
        topPanel.add(selectLabel);

        // Create shape selection buttons
        shapeButtons = new JButton[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            final String shapeName = shapes[i];
            shapeButtons[i] = new JButton(shapeName);
            shapeButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            shapeButtons[i].setBackground(new Color(80, 80, 80)); // High contrast background
            shapeButtons[i].setForeground(Color.WHITE); // High contrast text
            topPanel.add(shapeButtons[i]);
            shapeButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (problemActive) {
                        return;
                    }
                    // Start a new problem for the selected shape
                    if (!completedShapes.contains(shapeName)) {
                        startNewShapeProblem(shapeName);
                    } else {
                        // This case should not happen since completed shapes' buttons are disabled
                        JOptionPane.showMessageDialog(Task3Panel.this,
                                "You have already completed the " + shapeName.toLowerCase() + " area task.",
                                "Task Already Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }

        timeLabel = new JLabel("Time left: --:--");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLabel.setForeground(Color.YELLOW); // High contrast text for status
        topPanel.add(timeLabel);
        attemptsLabel = new JLabel("Attempts left: " + Constants.DEFAULT_MAX_ATTEMPTS);
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        attemptsLabel.setForeground(Color.YELLOW); // High contrast text for status
        topPanel.add(attemptsLabel);

        add(topPanel);
        add(Box.createVerticalStrut(10));

        // Question label
        questionLabel = new JLabel(" ", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setAlignmentX(LEFT_ALIGNMENT);
        questionLabel.setForeground(Color.WHITE); // High contrast text
        
        add(questionLabel);
        add(Box.createVerticalStrut(10));

        // Panel for drawing the shape and labeling dimensions
        shapeDrawingPanel = new ShapeDrawingPanel();
        shapeDrawingPanel.setBackground(new Color(50, 50, 50)); // Contrast background for container
        add(shapeDrawingPanel);
        add(Box.createVerticalStrut(10));

        // Answer input panel
        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        answerPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel answerLabel = new JLabel("Your answer:");
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        answerLabel.setForeground(Color.WHITE); // High contrast text
        answerField = new JTextField(10);
        answerField.setFont(new Font("Arial", Font.PLAIN, 16));
        answerField.setBackground(new Color(60, 60, 60)); // High contrast background
        answerField.setForeground(Color.WHITE); // High contrast text
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        submitButton.setBackground(new Color(80, 80, 80)); // High contrast background
        submitButton.setForeground(Color.WHITE); // High contrast text
        answerPanel.add(answerLabel);
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        add(answerPanel);
        add(Box.createVerticalStrut(10));

        // Feedback panel for result message and formula
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        resultMessageLabel = new JLabel(" ");
        resultMessageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // Color will be set in handleSubmit()
        feedbackPanel.add(resultMessageLabel);
        
        formulaLabel = new JLabel(" ");
        formulaLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        formulaLabel.setForeground(Color.CYAN); // High contrast text for formula
        feedbackPanel.add(formulaLabel);
        add(feedbackPanel);

        // Listener for answer submission
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!problemActive) return;
                handleSubmit();
            }
        });

        // Initialize shape button states (in case some shapes are pre-completed)
        updateShapeButtons();
    }

    /**
     * Starts a new area calculation problem for the specified shape.
     * Generates random dimensions, calculates the correct answer, sets up the question text and formula,
     * starts the timer, and updates the UI.
     * @param shape The name of the shape for the new problem (e.g., "Rectangle").
     */
    private void startNewShapeProblem(String shape) {
        // Begin a new problem for the selected shape
        currentShape = shape;
        problemActive = true;
        attemptsUsed = 0;
        timeRemaining = Constants.TASK_TIME_LIMIT_SEC;  // 180 seconds
        // Reset input fields and disable all shape buttons during the problem
        for (JButton btn : shapeButtons) {
            btn.setEnabled(false);
        }
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        resultMessageLabel.setText(" ");
        formulaLabel.setText(" ");
        attemptsLabel.setText("Attempts left: " + Constants.DEFAULT_MAX_ATTEMPTS);
        timeLabel.setText("Time left: 03:00");

        // Generate random dimensions and prepare question text and solution formula
        String questionText = "";
        int v1 = 0, v2 = 0, v3 = 0;
        if (shape.equals("Rectangle")) {
            int length = 1 + (int)(Math.random() * 20);
            int width  = 1 + (int)(Math.random() * 20);
            questionText = "Calculate the area of a rectangle with length " + length + " and width " + width + ".";
            double area = length * width;
            currentCorrectAnswer = area;
            currentFormula = "Area = length × width = " + length + " × " + width + " = " + formatNumber(area);
            v1 = length;
            v2 = width;
        } else if (shape.equals("Parallelogram")) {
            int base   = 1 + (int)(Math.random() * 20);
            int height = 1 + (int)(Math.random() * 20);
            questionText = "Calculate the area of a parallelogram with base " + base + " and height " + height + ".";
            double area = base * height;
            currentCorrectAnswer = area;
            currentFormula = "Area = base × height = " + base + " × " + height + " = " + formatNumber(area);
            v1 = base;
            v2 = height;
        } else if (shape.equals("Triangle")) {
            int base   = 1 + (int)(Math.random() * 20);
            int height = 1 + (int)(Math.random() * 20);
            questionText = "Calculate the area of a triangle with base " + base + " and height " + height + ".";
            double area = 0.5 * base * height;
            currentCorrectAnswer = area;
            currentFormula = "Area = ½ × base × height = 0.5 × " + base + " × " + height + " = " + formatNumber(area);
            v1 = base;
            v2 = height;
        } else if (shape.equals("Trapezium")) {
            int a = 1 + (int)(Math.random() * 20);  // base1
            int b = 1 + (int)(Math.random() * 20);  // base2
            int h = 1 + (int)(Math.random() * 20);  // height
            questionText = "Calculate the area of a trapezium with base1 = " + a + ", base2 = " + b + ", and height " + h + ".";
            double area = 0.5 * (a + b) * h;
            currentCorrectAnswer = area;
            currentFormula = "Area = ½ × (a + b) × h = 0.5 × (" + a + " + " + b + ") × " + h + " = " + formatNumber(area);
            v1 = a;
            v2 = b;
            v3 = h;
        }
        // Set question text with HTML wrapping to prevent overflow
        questionLabel.setText("<html><div style='width:1200px;text-align:left;'>" + questionText + "</div></html>");

        // Draw the shape and label dimensions
        shapeDrawingPanel.setShapeData(currentShape, v1, v2, v3);

        // Start the countdown timer (3 minutes)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                timeLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));
                if (timeRemaining <= 0) {
                    // Time is up
                    timer.stop();
                    timeLabel.setText("Time left: 00:00");
                    problemActive = false;
                    answerField.setEnabled(false);
                    submitButton.setEnabled(false);
                    // Show correct solution and feedback
                    resultMessageLabel.setText("Time's up! The solution is shown below.");
                    formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
                    completedShapes.add(currentShape);
                    // Update progress bar
                    mainFrameRef.getNavigationBar().updateProgress(completedShapes.size(), shapes.length);
                    // After timeout, no score is awarded
                    // Check if all shapes are done
                    if (completedShapes.size() == shapes.length) {
                        // Completed all 4 shapes
                        JOptionPane.showMessageDialog(mainFrameRef,
                                "Congratulations, you have completed all 4 shapes.",
                                "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                        mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
                    } else {
                        // Enable buttons for the next shape
                        updateShapeButtons();
                    }
                }
            }
        });
        timer.start();
    }

    /**
     * Handles the submission of the user's calculated area answer.
     * Validates the input, checks if the answer is correct, and updates the UI.
     * Manages attempts left and triggers the display of the correct formula/solution upon success or failure.
     * Awards points based on the number of attempts for correct answers.
     */
    private void handleSubmit() {
        String answerText = answerField.getText().trim();
        if (answerText.isEmpty()) {
            return; // do nothing if no answer entered
        }
        double userAnswer;
        try {
            userAnswer = Double.parseDouble(answerText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            resultMessageLabel.setText("Invalid input! :("); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            return;
        }
        attemptsUsed++;
        if (Math.abs(userAnswer - currentCorrectAnswer) < 1e-6) {
            // Correct answer entered
            timer.stop();
            problemActive = false;
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            completedShapes.add(currentShape);
            // Award points based on attempts used (Basic scoring)
            ScoreManager scoreManager = mainFrameRef.getScoreManager();
            int points = scoreManager.calculatePoints(attemptsUsed, Constants.SCORE_BASIC);
            scoreManager.recordScoreAndFeedback(points);
            // Show success feedback and solution
            resultMessageLabel.setText("Correct! :)"); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(0, 255, 0)); // Green for correct
            formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
            // Update progress bar
            mainFrameRef.getNavigationBar().updateProgress(completedShapes.size(), shapes.length);
            // Check if all 4 shapes have been completed
            if (completedShapes.size() == shapes.length) {
                JOptionPane.showMessageDialog(mainFrameRef,
                        "Congratulations, you have completed all 4 shapes.",
                        "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
            } else {
                // Enable buttons for another shape
                updateShapeButtons();
            }
        } else if (attemptsUsed < Constants.DEFAULT_MAX_ATTEMPTS) {
            // Incorrect answer with attempts left
            JOptionPane.showMessageDialog(this, "Try again! :( Remaining attempts: " + (Constants.DEFAULT_MAX_ATTEMPTS - attemptsUsed),
                    "Incorrect Answer", JOptionPane.INFORMATION_MESSAGE);
            resultMessageLabel.setText("Try again! :("); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            answerField.setText("");
            answerField.requestFocus();
            attemptsLabel.setText("Attempts left: " + (Constants.DEFAULT_MAX_ATTEMPTS - attemptsUsed));
        } else {
            // No attempts left (third incorrect attempt)
            timer.stop();
            problemActive = false;
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            completedShapes.add(currentShape);
            // Show failure feedback and correct solution
            resultMessageLabel.setText("No attempts left. :( The solution is shown below."); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
            // No points are awarded since the answer was not correct within 3 attempts
            // Update progress bar (no points awarded)
            mainFrameRef.getNavigationBar().updateProgress(completedShapes.size(), shapes.length);
            if (completedShapes.size() == shapes.length) {
                JOptionPane.showMessageDialog(mainFrameRef,
                        "Congratulations, you have completed all 4 shapes.",
                        "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
            } else {
                updateShapeButtons();
            }
        }
    }

    /**
     * Updates the enabled state of the shape selection buttons.
     * Disables buttons for shapes that have already been completed in the current session.
     * Disables all shape buttons while a problem is active.
     */
    private void updateShapeButtons() {
        for (int i = 0; i < shapes.length; i++) {
            String shape = shapes[i];
            if (completedShapes.contains(shape)) {
                shapeButtons[i].setEnabled(false);
            } else {
                shapeButtons[i].setEnabled(!problemActive);
            }
        }
    }

    /**
     * Helper method to format a double value for display.
     * Formats integers without decimal places, values ending in .5 with one decimal place, and others with two decimal places.
     * @param value The double value to format.
     * @return A formatted String representation of the number.
     */
    private String formatNumber(double value) {
        if (Math.abs(value - Math.round(value)) < 1e-6) {
            return String.valueOf((long)Math.round(value));           // Integer value
        } else if (Math.abs(value * 2 - Math.round(value * 2)) < 1e-6) {
            return String.format("%.1f", value);                      // One decimal place (e.g., x.5)
        } else {
            return String.format("%.2f", value);                      // Two decimal places
        }
    }

    // TaskPanel interface methods
    @Override
    /**
     * Implemented from {@link TaskPanel}. Displays a problem on the panel.
     * Note: Problem loading is handled internally by {@code startNewShapeProblem()}.
     * @param problem The problem to display.
     */
    public void displayProblem(com.shapeville.model.Problem problem) {
        // Not used explicitly in this integrated implementation
    }

    @Override
    /**
     * Implemented from {@link TaskPanel}. Displays feedback to the user.
     * Note: Feedback is displayed directly on UI labels within {@code handleSubmit()}.
     * @param feedback The feedback to display.
     */
    public void showFeedback(Feedback feedback) {
        // Not used explicitly; feedback is shown via labels/dialogs in the UI
    }

    @Override
    /**
     * Implemented from {@link TaskPanel}. Sets the callback for task logic.
     * Currently not explicitly used for callbacks from this panel, but required by the interface.
     * @param logic The task logic instance.
     */
    public void setTaskLogicCallback(TaskLogic logic) {
        // Not used; UI and logic are combined in this panel
    }

    @Override
    /**
     * Implemented from {@link TaskPanel}. Gets the unique identifier for this panel.
     * @return The panel ID string from {@link Constants}.
     */
    public String getPanelId() {
        return Constants.AREA_CALC_PANEL_ID;
    }

    @Override
    /**
     * Implemented from {@link TaskPanel}. Resets the state of the panel.
     * Stops the timer and deactivates the current problem when the panel is left.
     */
    public void resetState() {
        // Clean up when leaving this panel (e.g., returning Home)
        if (timer != null) {
            timer.stop();
        }
        problemActive = false;
    }

    /**
     * Inner class responsible for drawing shapes and their dimensions on the panel.
     * Extends JPanel and overrides paintComponent to render the shape visualization.
     */
    private class ShapeDrawingPanel extends JPanel {
        /** The name of the shape to be drawn. */
        private String shapeName;
        /** The first parameter for the shape's dimensions (e.g., length, base1). */
        private int param1;
        /** The second parameter for the shape's dimensions (e.g., width, base2, height). */
        private int param2;
        /** The third parameter for the shape's dimensions (e.g., height for trapezium). */
        private int param3;

        /**
         * Constructs a new ShapeDrawingPanel.
         * Sets preferred size and border.
         */
        public ShapeDrawingPanel() {
            // Set a preferred size for the drawing area
            setPreferredSize(new Dimension(250, 150));
        }

        /**
         * Sets the data for the shape to be drawn.
         * Updates the shape name and dimension parameters.
         * @param shapeName The name of the shape.
         * @param p1 The first dimension parameter.
         * @param p2 The second dimension parameter.
         * @param p3 The third dimension parameter (used for Trapezium).
         */
        public void setShapeData(String shapeName, int p1, int p2, int p3) {
            this.shapeName = shapeName;
            this.param1 = p1;
            this.param2 = p2;
            this.param3 = p3;
            repaint();
        }

        @Override
        /**
         * Paints the shape and its dimensions on the panel.
         * Draws the outline, dimension lines with arrowheads, and labels for the dimensions.
         * @param g The Graphics object to paint on.
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (shapeName == null || shapeName.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            // Enable anti-aliasing for smoother lines
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Set font for labels
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            FontMetrics fm = g2.getFontMetrics();
            // Common settings
            int marginLeft = 40;
            int marginTop = 40;
            int extOffset = 15;      // offset for horizontal dimension lines
            int extOffsetX = 20;     // offset for vertical dimension lines
            int arrowSize = 8;
            // Coordinates for shape
            if (shapeName.equals("Rectangle")) {
                // Dimensions (not to scale with actual values)
                int lengthPx = 100;
                int widthPx = 60;
                // Rectangle corner coordinates
                int x_left = marginLeft;
                int x_right = marginLeft + lengthPx;
                int y_top = marginTop;
                int y_bottom = marginTop + widthPx;
                // Draw rectangle outline
                g2.setColor(Color.WHITE);
                g2.drawRect(x_left, y_top, lengthPx, widthPx);
                // Draw dimension lines and arrowheads for length (bottom side)
                int y_dim = y_bottom + extOffset;
                // Extension lines
                g2.drawLine(x_left, y_bottom, x_left, y_dim);
                g2.drawLine(x_right, y_bottom, x_right, y_dim);
                // Dimension line
                g2.drawLine(x_left, y_dim, x_right, y_dim);
                // Arrowheads on dimension line (horizontal)
                // Left end arrow
                g2.drawLine(x_left, y_dim, x_left + arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_left, y_dim, x_left + arrowSize, y_dim + arrowSize/2);
                // Right end arrow
                g2.drawLine(x_right, y_dim, x_right - arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_right, y_dim, x_right - arrowSize, y_dim + arrowSize/2);
                // Draw dimension lines and arrowheads for width (right side)
                int x_dim = x_right + extOffsetX;
                // Extension lines
                g2.drawLine(x_right, y_top, x_dim, y_top);
                g2.drawLine(x_right, y_bottom, x_dim, y_bottom);
                // Dimension line
                g2.drawLine(x_dim, y_top, x_dim, y_bottom);
                // Arrowheads on dimension line (vertical)
                // Top arrow
                g2.drawLine(x_dim, y_top, x_dim - arrowSize/2, y_top + arrowSize);
                g2.drawLine(x_dim, y_top, x_dim + arrowSize/2, y_top + arrowSize);
                // Bottom arrow
                g2.drawLine(x_dim, y_bottom, x_dim - arrowSize/2, y_bottom - arrowSize);
                g2.drawLine(x_dim, y_bottom, x_dim + arrowSize/2, y_bottom - arrowSize);
                // Labels
                String lengthLabel = "length = " + param1;
                String widthLabel = "width = " + param2;
                // Length label (above bottom dimension line, centered)
                int textWidth = fm.stringWidth(lengthLabel);
                int x_text = x_left + (lengthPx - textWidth) / 2;
                // Baseline above dimension line
                int descent = fm.getDescent();
                int textHeight = fm.getHeight();
                int baseLineY = y_dim - descent - 2;
                g2.drawString(lengthLabel, x_text, baseLineY);
                // Width label (to left of right vertical dimension line, centered)
                int midY = (y_top + y_bottom) / 2;
                textWidth = fm.stringWidth(widthLabel);
                int baseLineX = x_dim - textWidth - 5;
                // Baseline for vertical text
                baseLineY = midY + (fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(widthLabel, baseLineX, baseLineY);
            } else if (shapeName.equals("Parallelogram")) {
                // Dimensions for drawing (fixed, not to scale with actual values)
                int basePx = 100;
                int heightPx = 60;
                int offsetPx = 30; // horizontal offset for top base
                // Coordinates
                int x_bl = marginLeft;
                int y_bl = marginTop + heightPx;
                int x_br = marginLeft + basePx;
                int y_br = y_bl;
                int x_tl = marginLeft + offsetPx;
                int y_tl = marginTop;
                int x_tr = x_tl + basePx;
                int y_tr = y_tl;
                // Draw parallelogram outline
                g2.setColor(Color.WHITE);
                g2.drawLine(x_bl, y_bl, x_br, y_br); // bottom base
                g2.drawLine(x_tl, y_tl, x_tr, y_tr); // top base
                g2.drawLine(x_bl, y_bl, x_tl, y_tl); // left slanted side
                g2.drawLine(x_br, y_br, x_tr, y_tr); // right slanted side
                // Base dimension (bottom)
                int y_dim = y_bl + extOffset;
                g2.drawLine(x_bl, y_bl, x_bl, y_dim);
                g2.drawLine(x_br, y_br, x_br, y_dim);
                g2.drawLine(x_bl, y_dim, x_br, y_dim);
                // Arrowheads for base
                g2.drawLine(x_bl, y_dim, x_bl + arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_bl, y_dim, x_bl + arrowSize, y_dim + arrowSize/2);
                g2.drawLine(x_br, y_dim, x_br - arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_br, y_dim, x_br - arrowSize, y_dim + arrowSize/2);
                // Height dimension (right side vertical)
                // Determine extreme right of shape for dimension line
                int shapeRightX = Math.max(x_br, x_tr);
                int x_dim = shapeRightX + extOffsetX;
                g2.drawLine(shapeRightX, y_tl, x_dim, y_tl);
                g2.drawLine(shapeRightX, y_bl, x_dim, y_bl);
                g2.drawLine(x_dim, y_tl, x_dim, y_bl);
                // Arrowheads for height
                g2.drawLine(x_dim, y_tl, x_dim - arrowSize/2, y_tl + arrowSize);
                g2.drawLine(x_dim, y_tl, x_dim + arrowSize/2, y_tl + arrowSize);
                g2.drawLine(x_dim, y_bl, x_dim - arrowSize/2, y_bl - arrowSize);
                g2.drawLine(x_dim, y_bl, x_dim + arrowSize/2, y_bl - arrowSize);
                // Labels
                String baseLabel = "base = " + param1;
                String heightLabel = "height = " + param2;
                // Base label (above bottom dimension line)
                int textWidth = fm.stringWidth(baseLabel);
                int x_text = x_bl + (basePx - textWidth) / 2;
                int descent = fm.getDescent();
                int textHeight = fm.getHeight();
                int baseLineY = y_dim - descent - 2;
                g2.drawString(baseLabel, x_text, baseLineY);
                // Height label (to left of vertical line, centered)
                int midY = (y_tl + y_bl) / 2;
                textWidth = fm.stringWidth(heightLabel);
                int baseLineX = x_dim - textWidth - 5;
                baseLineY = midY + (fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(heightLabel, baseLineX, baseLineY);
            } else if (shapeName.equals("Triangle")) {
                // Dimensions for drawing (fixed, not to scale with actual values)
                int basePx = 100;
                int heightPx = 60;
                // Coordinates for an isosceles triangle
                int x_bl = marginLeft;
                int y_bl = marginTop + heightPx;
                int x_br = marginLeft + basePx;
                int y_br = y_bl;
                int x_apex = marginLeft + basePx / 2;
                int y_apex = marginTop;
                // Draw triangle outline
                g2.setColor(Color.WHITE);
                g2.drawLine(x_bl, y_bl, x_br, y_br);     // base
                g2.drawLine(x_bl, y_bl, x_apex, y_apex); // left side
                g2.drawLine(x_br, y_br, x_apex, y_apex); // right side
                // Base dimension (bottom)
                int y_dim = y_bl + extOffset;
                g2.drawLine(x_bl, y_bl, x_bl, y_dim);
                g2.drawLine(x_br, y_br, x_br, y_dim);
                g2.drawLine(x_bl, y_dim, x_br, y_dim);
                // Arrowheads for base
                g2.drawLine(x_bl, y_dim, x_bl + arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_bl, y_dim, x_bl + arrowSize, y_dim + arrowSize/2);
                g2.drawLine(x_br, y_dim, x_br - arrowSize, y_dim - arrowSize/2);
                g2.drawLine(x_br, y_dim, x_br - arrowSize, y_dim + arrowSize/2);
                // Height dimension (vertical drop from apex)
                int x_drop = x_apex;
                int y_drop_top = y_apex;
                int y_drop_bottom = y_bl;
                g2.drawLine(x_drop, y_drop_top, x_drop, y_drop_bottom);
                // Mark right angle at base
                g2.drawLine(x_drop, y_drop_bottom, x_drop, y_drop_bottom - 8);
                // Arrowheads for height line
                // Top arrow at apex
                g2.drawLine(x_drop, y_drop_top, x_drop - arrowSize/2, y_drop_top + arrowSize);
                g2.drawLine(x_drop, y_drop_top, x_drop + arrowSize/2, y_drop_top + arrowSize);
                // Bottom arrow at base
                g2.drawLine(x_drop, y_drop_bottom, x_drop - arrowSize/2, y_drop_bottom - arrowSize);
                g2.drawLine(x_drop, y_drop_bottom, x_drop + arrowSize/2, y_drop_bottom - arrowSize);
                // Labels
                String baseLabel = "base = " + param1;
                String heightLabel = "height = " + param2;
                // Base label (above bottom dimension line)
                int textWidth = fm.stringWidth(baseLabel);
                int x_text = x_bl + (basePx - textWidth) / 2;
                int descent = fm.getDescent();
                int baseLineY = y_dim - descent - 2;
                g2.drawString(baseLabel, x_text, baseLineY);
                // Height label (to left of drop line, centered)
                int midY = (y_apex + y_bl) / 2;
                textWidth = fm.stringWidth(heightLabel);
                int baseLineX = x_drop - textWidth - 5;
                baseLineY = midY + (fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(heightLabel, baseLineX, baseLineY);
            } else if (shapeName.equals("Trapezium")) {
                // Dimensions for drawing (fixed, not to scale with actual values)
                int base1Px = 120;  // bottom base length
                int base2Px = 80;   // top base length
                // Align top base centered relative to bottom base
                int offsetX = (base1Px - base2Px) / 2;
                // Coordinates
                int x_bl = marginLeft;
                int y_bl = marginTop + 60;
                int x_br = marginLeft + base1Px;
                int y_br = y_bl;
                int x_tl = marginLeft + offsetX;
                int y_tl = marginTop;
                int x_tr = x_tl + base2Px;
                int y_tr = y_tl;
                // Draw trapezium outline
                g2.setColor(Color.WHITE);
                g2.drawLine(x_bl, y_bl, x_br, y_br); // bottom base
                g2.drawLine(x_tl, y_tl, x_tr, y_tr); // top base
                g2.drawLine(x_bl, y_bl, x_tl, y_tl); // left side
                g2.drawLine(x_br, y_br, x_tr, y_tr); // right side
                // Bottom base dimension
                int y_dim_bottom = y_bl + extOffset;
                g2.drawLine(x_bl, y_bl, x_bl, y_dim_bottom);
                g2.drawLine(x_br, y_br, x_br, y_dim_bottom);
                g2.drawLine(x_bl, y_dim_bottom, x_br, y_dim_bottom);
                // Arrowheads for bottom base
                g2.drawLine(x_bl, y_dim_bottom, x_bl + arrowSize, y_dim_bottom - arrowSize/2);
                g2.drawLine(x_bl, y_dim_bottom, x_bl + arrowSize, y_dim_bottom + arrowSize/2);
                g2.drawLine(x_br, y_dim_bottom, x_br - arrowSize, y_dim_bottom - arrowSize/2);
                g2.drawLine(x_br, y_dim_bottom, x_br - arrowSize, y_dim_bottom + arrowSize/2);
                // Top base dimension
                int y_dim_top = y_tl - extOffset;
                g2.drawLine(x_tl, y_tl, x_tl, y_dim_top);
                g2.drawLine(x_tr, y_tr, x_tr, y_dim_top);
                g2.drawLine(x_tl, y_dim_top, x_tr, y_dim_top);
                // Arrowheads for top base
                g2.drawLine(x_tl, y_dim_top, x_tl + arrowSize, y_dim_top + arrowSize/2);
                g2.drawLine(x_tl, y_dim_top, x_tl + arrowSize, y_dim_top - arrowSize/2);
                g2.drawLine(x_tr, y_dim_top, x_tr - arrowSize, y_dim_top + arrowSize/2);
                g2.drawLine(x_tr, y_dim_top, x_tr - arrowSize, y_dim_top - arrowSize/2);
                // Height dimension (right side vertical)
                // Determine rightmost x of shape
                int shapeRightX = Math.max(x_br, x_tr);
                int x_dim = shapeRightX + extOffsetX;
                g2.drawLine(shapeRightX, y_tr, x_dim, y_tr);
                g2.drawLine(shapeRightX, y_br, x_dim, y_br);
                g2.drawLine(x_dim, y_tr, x_dim, y_br);
                // Arrowheads for height
                g2.drawLine(x_dim, y_tr, x_dim - arrowSize/2, y_tr + arrowSize);
                g2.drawLine(x_dim, y_tr, x_dim + arrowSize/2, y_tr + arrowSize);
                g2.drawLine(x_dim, y_br, x_dim - arrowSize/2, y_br - arrowSize);
                g2.drawLine(x_dim, y_br, x_dim + arrowSize/2, y_br - arrowSize);
                // Labels
                String base1Label = "base1 = " + param1;
                String base2Label = "base2 = " + param2;
                String heightLabel = "height = " + param3;
                // Bottom base label (above bottom dimension line)
                int textWidth = fm.stringWidth(base1Label);
                int x_text = x_bl + (base1Px - textWidth) / 2;
                int descent = fm.getDescent();
                int baseLineY = y_dim_bottom - descent - 2;
                g2.drawString(base1Label, x_text, baseLineY);
                // Top base label (below top dimension line)
                textWidth = fm.stringWidth(base2Label);
                x_text = x_tl + (base2Px - textWidth) / 2;
                baseLineY = y_dim_top + fm.getAscent() + 2;
                g2.drawString(base2Label, x_text, baseLineY);
                // Height label (to left of vertical line, centered)
                int midY = (y_tr + y_br) / 2;
                textWidth = fm.stringWidth(heightLabel);
                int baseLineX = x_dim - textWidth - 5;
                baseLineY = midY + (fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(heightLabel, baseLineX, baseLineY);
            }
        }
    }
}
