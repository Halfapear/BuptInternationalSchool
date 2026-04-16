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
 * Task 4 Panel for Circle Area and Circumference calculations.
 * This panel allows the user to practice calculating the area and circumference of circles.
 * It supports two modes (Area and Circumference) and provides either the radius or diameter.
 * Features include a timer, attempt tracking, feedback, and displaying the formula with substituted values.
 * Implements the {@link com.shapeville.ui.panel_templates.TaskPanel} interface for integration with the task management flow.
 */
public class Task4Panel extends JPanel implements TaskPanel {
    /** Reference to the main application frame. */
    private MainFrame mainFrameRef;
    /** Button to select the Area calculation mode. */
    private JButton areaButton;
    /** Button to select the Circumference calculation mode. */
    private JButton circumferenceButton;
    /** Label to display the remaining time. */
    private JLabel timeLabel;
    /** Label to display the number of attempts remaining. */
    private JLabel attemptsLabel;
    /** Label to display the question or instructions for the current problem. */
    private JLabel questionLabel;
    /** Text field for user input of the calculated value. */
    private JTextField answerField;
    /** Button to submit the user's answer. */
    private JButton submitButton;
    /** Label to display feedback or result messages. */
    private JLabel resultMessageLabel;
    /** Label to display the formula for the current circle calculation. */
    private JLabel formulaLabel;
    /** Timer for the task time limit. */
    private Timer timer;
    /** Remaining time in seconds for the current problem. */
    private int timeRemaining;
    /** Number of attempts used for the current problem. */
    private int attemptsUsed;
    /** Flag indicating if a problem is currently active. */
    private boolean problemActive;
    /** Flag indicating if the Area calculation with Radius scenario is completed. */
    private boolean doneAreaRadius = false;
    /** Flag indicating if the Area calculation with Diameter scenario is completed. */
    private boolean doneAreaDiameter = false;
    /** Flag indicating if the Circumference calculation with Radius scenario is completed. */
    private boolean doneCircRadius = false;
    /** Flag indicating if the Circumference calculation with Diameter scenario is completed. */
    private boolean doneCircDiameter = false;
    // Current problem context
    /** Flag indicating if the current problem is Area calculation (true) or Circumference (false). */
    private boolean currentIsArea;
    /** Flag indicating if the given value for the current problem is Radius (true) or Diameter (false). */
    private boolean givenIsRadius;
    /** The correct numerical answer for the current problem. */
    private double currentCorrectAnswer;
    /** The formula string for the current circle calculation problem. */
    private String currentFormula;
    // Panel for drawing the circle and labeling given dimensions
    /** Custom panel for drawing the circle and labeling the given dimension. */
    private CircleDrawingPanel circleDrawingPanel;

    /**
     * Constructs a new Task4Panel.
     * Initializes UI components, sets up mode selection buttons, and prepares for circle calculation tasks.
     * @param mainFrame The reference to the main application frame ({@link com.shapeville.main.MainFrame}).
     */
    public Task4Panel(MainFrame mainFrame) {
        this.mainFrameRef = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Apply high contrast background to the main panel
        setBackground(new Color(30, 30, 30));

        // Title label
        JLabel titleLabel = new JLabel("Task 4: Circle Area and Circumference", SwingConstants.CENTER);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // High contrast text
        add(titleLabel);
        add(Box.createVerticalStrut(15));

        // Top panel: mode selection and status
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        topPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel modeLabel = new JLabel("Select mode:");
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        modeLabel.setForeground(Color.WHITE); // High contrast text
        topPanel.add(modeLabel);
        areaButton = new JButton("Area");
        areaButton.setFont(new Font("Arial", Font.PLAIN, 16));
        areaButton.setBackground(new Color(80, 80, 80)); // High contrast background
        areaButton.setForeground(Color.WHITE); // High contrast text
        circumferenceButton = new JButton("Circumference");
        circumferenceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        circumferenceButton.setBackground(new Color(80, 80, 80)); // High contrast background
        circumferenceButton.setForeground(Color.WHITE); // High contrast text
        topPanel.add(areaButton);
        topPanel.add(circumferenceButton);
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

        // Panel for drawing the circle and labeling dimensions
        circleDrawingPanel = new CircleDrawingPanel();
        circleDrawingPanel.setBackground(new Color(50, 50, 50)); // Contrast background for container
        add(circleDrawingPanel);
        add(Box.createVerticalStrut(10));

        // Answer input panel
        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        answerPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel answerLabel = new JLabel("Your answer(integer part only):");
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

        // Mode selection button listeners
        areaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (problemActive) return;
                startNewCircleProblem(true);  // true = Area mode
            }
        });
        circumferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (problemActive) return;
                startNewCircleProblem(false); // false = Circumference mode
            }
        });

        // Answer submission listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!problemActive) return;
                handleSubmit();
            }
        });
    }

    /**
     * Starts a new circle calculation problem (Area or Circumference).
     * Selects a random value (radius or diameter) and generates the question.
     * Sets up the correct answer, formula, and starts the timer.
     * Disables mode selection buttons while a problem is active.
     * Ensures each of the four scenarios (Area/Radius, Area/Diameter, Circ/Radius, Circ/Diameter) is presented once.
     * @param isAreaMode True to start an Area calculation problem, false for Circumference.
     */
    private void startNewCircleProblem(boolean isAreaMode) {
        // Ensure the selected mode still has an unseen scenario
        if (isAreaMode && doneAreaRadius && doneAreaDiameter) {
            JOptionPane.showMessageDialog(this,
                    "You have completed all Area calculations for the circle.",
                    "Mode Complete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!isAreaMode && doneCircRadius && doneCircDiameter) {
            JOptionPane.showMessageDialog(this,
                    "You have completed all Circumference calculations for the circle.",
                    "Mode Complete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        currentIsArea = isAreaMode;
        // Decide whether to use radius or diameter for this problem
        if (isAreaMode) {
            if (!doneAreaRadius && !doneAreaDiameter) {
                givenIsRadius = (Math.random() < 0.5);
            } else if (!doneAreaRadius) {
                givenIsRadius = true;
            } else if (!doneAreaDiameter) {
                givenIsRadius = false;
            } else {
                return;  // no scenario left (should not reach here)
            }
        } else {
            if (!doneCircRadius && !doneCircDiameter) {
                givenIsRadius = (Math.random() < 0.5);
            } else if (!doneCircRadius) {
                givenIsRadius = true;
            } else if (!doneCircDiameter) {
                givenIsRadius = false;
            } else {
                return;
            }
        }
        problemActive = true;
        attemptsUsed = 0;
        timeRemaining = Constants.TASK_TIME_LIMIT_SEC;
        // Disable mode buttons and reset UI for new problem
        areaButton.setEnabled(false);
        circumferenceButton.setEnabled(false);
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        resultMessageLabel.setText(" ");
        formulaLabel.setText(" ");
        attemptsLabel.setText("Attempts left: " + Constants.DEFAULT_MAX_ATTEMPTS);
        timeLabel.setText("Time left: 03:00");

        // Generate a random value (1–20) for radius or diameter
        int value = 1 + (int)(Math.random() * 20);
        String valueDescription = (givenIsRadius ? "radius " + value : "diameter " + value);
        if (currentIsArea) {
            // Area calculation scenario
            questionLabel.setText("<html><div style='width:400px;'>Calculate the area of a circle with " + valueDescription + ".</div></html>");
            double r = givenIsRadius ? value : (value / 2.0);
            double area = Math.PI * r * r;
            currentCorrectAnswer = area;
            if (givenIsRadius) {
                currentFormula = "Area = π × r² = π × " + value + "² = " + formatNumber(area);
            } else {
                currentFormula = "Area = π × (d/2)² = π × (" + value + "/2)² = " + formatNumber(area);
            }
        } else {
            // Circumference calculation scenario
            questionLabel.setText("<html><div style='width:400px;'>Calculate the circumference of a circle with " + valueDescription + ".</div></html>");
            if (givenIsRadius) {
                double circumference = 2 * Math.PI * value;
                currentCorrectAnswer = circumference;
                currentFormula = "Circumference = 2πr = 2 × π × " + value + " = " + formatNumber(circumference);
            } else {
                double circumference = Math.PI * value;
                currentCorrectAnswer = circumference;
                currentFormula = "Circumference = π × d = π × " + value + " = " + formatNumber(circumference);
            }
        }
        // Draw the circle and label the given dimension
        circleDrawingPanel.setCircleData(value, givenIsRadius);

        // Start the 3-minute timer
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
                    // Show solution and mark scenario as done
                    resultMessageLabel.setText("Time's up! The solution is shown below.");
                    formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
                    markCurrentScenarioDone();
                    // Check if all four scenarios are completed
                    if (allScenariosDone()) {
                        JOptionPane.showMessageDialog(mainFrameRef,
                                "Congratulations, you have completed all circle calculations.",
                                "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                        mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
                    } else {
                        updateModeButtons();
                    }
                }
            }
        });
        timer.start();
    }

    private void handleSubmit() {
        String answerText = answerField.getText().trim();
        if (answerText.isEmpty()) {
            return;
        }
        double userAnswer;
        try {
            userAnswer = Double.parseDouble(answerText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            resultMessageLabel.setText("Invalid input! :("); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
            return;
        }
        attemptsUsed++;
        // Use a tolerance for correctness because of π (accept answers within ~1.0 of true value)
        double tolerance = 1.0;
        if (Math.abs(userAnswer - currentCorrectAnswer) < tolerance) {
            // Correct answer
            timer.stop();
            problemActive = false;
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            markCurrentScenarioDone();
            // Award points (Basic scoring)
            ScoreManager scoreManager = mainFrameRef.getScoreManager();
            int points = scoreManager.calculatePoints(attemptsUsed, Constants.SCORE_BASIC);
            scoreManager.recordScoreAndFeedback(points);
            // Show success feedback and formula
            resultMessageLabel.setText("Correct! :)"); // Changed emoji to text symbol
            resultMessageLabel.setForeground(new Color(0, 255, 0)); // Green for correct
            formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
            if (allScenariosDone()) {
                JOptionPane.showMessageDialog(mainFrameRef,
                        "Congratulations, you have completed all circle calculations.",
                        "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
            } else {
                updateModeButtons();
            }
        } else {
            // Incorrect answer
            if (attemptsUsed < Constants.DEFAULT_MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this, "Try again! :( Remaining attempts: " + (Constants.DEFAULT_MAX_ATTEMPTS - attemptsUsed),
                        "Incorrect Answer", JOptionPane.INFORMATION_MESSAGE);
                resultMessageLabel.setText("Try again! :("); // Changed emoji to text symbol
                resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
                answerField.setText("");
                answerField.requestFocus();
                attemptsLabel.setText("Attempts left: " + (Constants.DEFAULT_MAX_ATTEMPTS - attemptsUsed));
            } else {
                // Third attempt incorrect – no more attempts
                timer.stop();
                problemActive = false;
                answerField.setEnabled(false);
                submitButton.setEnabled(false);
                markCurrentScenarioDone();
                resultMessageLabel.setText("No attempts left. :( The solution is shown below."); // Changed emoji to text symbol
                resultMessageLabel.setForeground(new Color(255, 0, 0)); // Red for incorrect
                formulaLabel.setText("<html><div style='width:400px;'>" + currentFormula + "</div></html>");
                if (allScenariosDone()) {
                    JOptionPane.showMessageDialog(mainFrameRef,
                            "Congratulations, you have completed all circle calculations.",
                            "Task Complete", JOptionPane.INFORMATION_MESSAGE);
                    mainFrameRef.getTaskManager().currentTaskTypeCompleted(new TaskLogic(){});
                } else {
                    updateModeButtons();
                }
            }
        }
    }

    // Mark the current scenario (mode and input type) as completed and update progress
    private void markCurrentScenarioDone() {
        if (currentIsArea) {
            if (givenIsRadius) {
                doneAreaRadius = true;
            } else {
                doneAreaDiameter = true;
            }
        } else {
            if (givenIsRadius) {
                doneCircRadius = true;
            } else {
                doneCircDiameter = true;
            }
        }
        // Update progress bar
        int completedCount = 0;
        if (doneAreaRadius) completedCount++;
        if (doneAreaDiameter) completedCount++;
        if (doneCircRadius) completedCount++;
        if (doneCircDiameter) completedCount++;
        mainFrameRef.getNavigationBar().updateProgress(completedCount, 4);
    }

    // Check if all four scenario combinations have been completed
    private boolean allScenariosDone() {
        return doneAreaRadius && doneAreaDiameter && doneCircRadius && doneCircDiameter;
    }

    // Enable or disable the mode buttons based on remaining tasks
    private void updateModeButtons() {
        if (!doneAreaRadius || !doneAreaDiameter) {
            areaButton.setEnabled(true);
        } else {
            areaButton.setEnabled(false);
        }
        if (!doneCircRadius || !doneCircDiameter) {
            circumferenceButton.setEnabled(true);
        } else {
            circumferenceButton.setEnabled(false);
        }
    }

    // Format numbers to two decimal places for display of π calculations
    private String formatNumber(double value) {
        return String.format("%.2f", value);
    }

    // TaskPanel interface methods
    @Override
    public void displayProblem(com.shapeville.model.Problem problem) {
        // Not used in this implementation
    }

    @Override
    public void showFeedback(Feedback feedback) {
        // Not used (feedback is shown via UI components directly)
    }

    @Override
    public void setTaskLogicCallback(TaskLogic logic) {
        // Not used
    }

    @Override
    public String getPanelId() {
        return Constants.CIRCLE_CALC_PANEL_ID;
    }

    @Override
    public void resetState() {
        if (timer != null) {
            timer.stop();
        }
        problemActive = false;
    }

    /**
     * Inner panel class for drawing the circle and annotating radius/diameter.
     */
    private class CircleDrawingPanel extends JPanel {
        private int givenValue;
        private boolean givenIsRadius;

        public CircleDrawingPanel() {
            setPreferredSize(new Dimension(250, 200));
            this.givenValue = 0;
            this.givenIsRadius = true;
        }

        public void setCircleData(int value, boolean isRadius) {
            this.givenValue = value;
            this.givenIsRadius = isRadius;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (givenValue <= 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            FontMetrics fm = g2.getFontMetrics();
            // Determine center of panel and fixed radius for drawing
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radiusPx = 50;
            int arrowSize = 8;
            // Draw circle outline and center point
            g2.setColor(Color.WHITE);
            g2.drawOval(cx - radiusPx, cy - radiusPx, 2 * radiusPx, 2 * radiusPx);
            g2.fillOval(cx - 3, cy - 3, 6, 6);
            if (givenIsRadius) {
                // Draw radius line and arrow
                g2.drawLine(cx, cy, cx + radiusPx, cy);
                // Arrow at circumference end of radius
                g2.drawLine(cx + radiusPx, cy, cx + radiusPx - arrowSize, cy - arrowSize / 2);
                g2.drawLine(cx + radiusPx, cy, cx + radiusPx - arrowSize, cy + arrowSize / 2);
                // Label "radius = value"
                String radiusLabel = "radius = " + givenValue;
                int textWidth = fm.stringWidth(radiusLabel);
                int x_text = cx + radiusPx/2 - textWidth / 2;
                int descent = fm.getDescent();
                int baseLineY = cy - descent - 2;
                g2.drawString(radiusLabel, x_text, baseLineY);
            } else {
                // Draw diameter line through center
                g2.drawLine(cx - radiusPx, cy, cx + radiusPx, cy);
                // Arrowheads at both ends of diameter
                g2.drawLine(cx - radiusPx, cy, cx - radiusPx + arrowSize, cy - arrowSize / 2);
                g2.drawLine(cx - radiusPx, cy, cx - radiusPx + arrowSize, cy + arrowSize / 2);
                g2.drawLine(cx + radiusPx, cy, cx + radiusPx - arrowSize, cy - arrowSize / 2);
                g2.drawLine(cx + radiusPx, cy, cx + radiusPx - arrowSize, cy + arrowSize / 2);
                // Label "diameter = value"
                String diameterLabel = "diameter = " + givenValue;
                int textWidth = fm.stringWidth(diameterLabel);
                int x_text = cx - textWidth / 2;
                int descent = fm.getDescent();
                int baseLineY = cy - descent - 2;
                g2.drawString(diameterLabel, x_text, baseLineY);
            }
        }
    }
}
