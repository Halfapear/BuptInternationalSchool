package com.shapeville.task.bonus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
/**
 * Bonus 1: Compound Shape Area Calculation
 * Allows users to select a compound shape and calculate its area.
 */
public class Compound extends JPanel {
    private JTextField answerField;
    private int attempt = 0;
    private int currentShapeIndex = 0;
    private JLabel shapeImageLabel;
    private JLabel questionLabel;
    private JLabel timerLabel;
    private List<CompoundShape> shapes;
    private int practisedShapes = 0;
    private JPanel content;
    private Timer timer;
    private int timeRemaining = 300; // 5 minutes (300 seconds)
    private boolean timerRunning = false;
    private JLabel shapeDescriptionLabel;

    /**
     * Constructor
     */
    public Compound() {
        setLayout(new BorderLayout());
        // Apply high contrast background to the main panel
        setBackground(new Color(30, 30, 30));

        content = new JPanel();
        content.setBackground(new Color(30, 30, 30)); // Match main panel background
        add(content, BorderLayout.CENTER);
        initializeShapes();
        setupUI();
    }

    /**
     * Initializes all compound shapes with their descriptions, questions, correct answers, and solution hints.
     */
    private void initializeShapes() {
        shapes = new ArrayList<>();
        
        // Add 6 compound shapes from Figure 10
        // Shape 1: L-shaped (two rectangles)
        shapes.add(new CompoundShape(
            "Shape 1: L-shaped Compound Rectangle",
            "Calculate the total area of the compound shape (unit: square centimeters)",
            310, // 20*10 + 11*10 = 200 + 110 = 310
            new String[]{"Hint: Break down the shape into two rectangles", "Rectangle area = length × width", "Calculate the area of each part and add them together"},
            createShapeImage(1)
        ));
        
        // Shape 2: Rectangle
        shapes.add(new CompoundShape(
            "Shape 2: L-shaped Compound Rectangle",
            "Calculate the total area of the compound shape (unit: square centimeters)",
            598, // 16*16 + 19*18 = 256 + 324 = 598
            new String[]{"Rectangle area = length × width"},
            createShapeImage(2)
        ));
        
        // Shape 3: L-shaped (two rectangles)
        shapes.add(new CompoundShape(
            "Shape 3: Convex-shaped Compound Rectangle",
            "Calculate the total area of the compound shape (unit: square meters)",
            288, // 24*6 + 12*12 = 288
            new String[]{"Hint: Break down the shape into two rectangles", "Rectangle area = length × width", "Calculate the area of each part and add them together"},
            createShapeImage(3)
        ));
        
        // Shape 4: Trapezoid
        shapes.add(new CompoundShape(
            "Shape 4: Trapezoid",
            "Calculate the area of the trapezoid (unit: square meters)",
            18, // (3+6)*4/2 = 18
            new String[]{"Trapezoid area = (top base + bottom base) × height ÷ 2"},
            createShapeImage(4)
        ));
        
        // Shape 5: L-shaped (two rectangles)
        shapes.add(new CompoundShape(
            "Shape 5: Convex-shaped Compound Rectangle",
            "Calculate the total area of the compound shape (unit: square meters)",
            3456, // 36*36 + 60*36 = 3456
            new String[]{"Hint: Break down the shape into two rectangles", "Rectangle area = length × width", "Calculate the area of each part and add them together"},
            createShapeImage(5)
        ));
        
        // Shape 6: L-shaped (two rectangles)
        shapes.add(new CompoundShape(
            "Shape 6: L-shaped Compound Rectangle",
            "Calculate the total area of the compound shape (unit: square meters)",
            174, // 10*11 + 8*8 = 110 + 64 = 174 (or approximate value)
            new String[]{"Hint: Break down the shape into two rectangles", "Rectangle area = length × width", "Calculate the area of each part and add them together"},
            createShapeImage(6)
        ));
    }

    /**
     * Creates a shape image.
     * @param shapeNumber The shape number.
     * @return The shape image.
     */
    private ImageIcon createShapeImage(int shapeNumber) {
        // 修改为使用类路径加载资源
        String imagePath = "/assets/compound/compound" + shapeNumber + ".png";
        return com.shapeville.utils.ImageLoader.loadImageAndScale(imagePath, 400, 300);
    }


    /**
     * Sets up the user interface components.
     */
    private void setupUI() {
        content.setLayout(new BorderLayout(10, 10));
        
        // North area for title, timer and attempts
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        // 创建顶部选择面板 - 模仿Sector.java的界面
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectionPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel selectLabel = new JLabel("Select shape:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectLabel.setForeground(Color.WHITE); // High contrast text
        selectionPanel.add(selectLabel);
        
        // 添加复合形状选择按钮
        JButton shapeButton = new JButton("Compound Shape");
        shapeButton.setBackground(new Color(80, 80, 80)); // High contrast background
        shapeButton.setForeground(Color.WHITE); // High contrast text
        shapeButton.addActionListener(e -> {
        // 显示形状选择对话框
        String[] options = new String[shapes.size()];
        for (int i = 0; i < shapes.size(); i++) {
            options[i] = "Shape " + (i + 1);
        }
        
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select one of the 6 compound shapes:",
            "Select Shape",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice >= 0) {
            currentShapeIndex = choice;
            resetTimer(); // 重置计时器
            attempt = 0;  // 重置尝试次数
            showCurrentShape();
        }
        });
        selectionPanel.add(shapeButton);
        
        northPanel.add(selectionPanel, BorderLayout.NORTH);
        
        // 添加时间面板
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        timerLabel = new JLabel("Time left: 05:00");
        timerLabel.setForeground(Color.YELLOW); // High contrast text for status
        infoPanel.add(timerLabel);
        northPanel.add(infoPanel, BorderLayout.CENTER);
        
        content.add(northPanel, BorderLayout.NORTH);
        
        // Center area for problem description and shape image
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(30, 30, 30)); // Match main panel background

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE); // High contrast text
        centerPanel.add(questionLabel, BorderLayout.NORTH);
        
        shapeImageLabel = new JLabel("", SwingConstants.CENTER);
        shapeImageLabel.setPreferredSize(new Dimension(400, 300));
        shapeImageLabel.setBackground(new Color(50, 50, 50)); // Contrast background for container
        centerPanel.add(shapeImageLabel, BorderLayout.CENTER);
        
        // 新增：形状描述标签
        shapeDescriptionLabel = new JLabel("", SwingConstants.CENTER);
        shapeDescriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        shapeDescriptionLabel.setForeground(Color.WHITE); // High contrast text
        centerPanel.add(shapeDescriptionLabel, BorderLayout.SOUTH);
        
        content.add(centerPanel, BorderLayout.CENTER);
        
        // South area for input and buttons
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        JLabel answerLabel = new JLabel("Enter Area:");
        answerLabel.setForeground(Color.WHITE); // High contrast text
        southPanel.add(answerLabel);
        
        answerField = new JTextField(10);
        answerField.setBackground(new Color(60, 60, 60)); // High contrast background
        answerField.setForeground(Color.WHITE); // High contrast text
        southPanel.add(answerField);
        
        JButton submitButton = new JButton("Submit Answer");
        submitButton.setBackground(new Color(80, 80, 80)); // High contrast background
        submitButton.setForeground(Color.WHITE); // High contrast text
        submitButton.addActionListener(e -> checkAnswer(answerField.getText()));
        southPanel.add(submitButton);
        
         content.add(southPanel, BorderLayout.SOUTH);
      
        
        // Display the first shape
        showCurrentShape();
        startTimer();
    }
    
    /**
     * Displays the current shape information.
     */
    private void showCurrentShape() {
        CompoundShape currentShape = shapes.get(currentShapeIndex);
        questionLabel.setText("<html><body style='width: 400px'>" + 
                              currentShape.getDescription() + "<br>" + 
                              currentShape.getQuestion() + "</body></html>");
        
        // Display shape image
        if (currentShape.getImage() != null) {
            shapeImageLabel.setIcon(currentShape.getImage());
            shapeImageLabel.setText("");
        } else {
            // If no image, display shape description in image label (as fallback)
            shapeImageLabel.setIcon(null);
            shapeImageLabel.setText("Shape " + (currentShapeIndex + 1) + " / " + shapes.size());
        }
        
        // 更新形状描述标签
        shapeDescriptionLabel.setText(currentShape.getDescription());
        
        // Reset attempt count
        attempt = 0;
    }
    
    /**
     * Starts the countdown timer.
     */
    private void startTimer() {
        if (timerRunning) {
            return;
        }
        
        timerRunning = true;
        timeRemaining = 300; // 5 minutes
        updateTimerLabel();
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    SwingUtilities.invokeLater(() -> updateTimerLabel());
                } else {
                    // Time's up
                    timer.cancel();
                    timerRunning = false;
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Compound.this, 
                                "Time's up! Let's see the solution.");
                        showSolution();
                    });
                }
            }
        }, 1000, 1000);
    }
    
    /**
     * Resets the timer.
     */
    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timerRunning = false;
        startTimer();
    }
    
    /**
     * Updates the timer label.
     */
    private void updateTimerLabel() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("Time Remaining: %d:%02d", minutes, seconds));
    }

    /**
     * Checks if the user's answer is correct.
     * @param input The user's input answer.
     */
    private void checkAnswer(String input) {
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an answer! :("); // Added symbol
            return;
        }
        
        try {
            double userAnswer = Double.parseDouble(input);
            CompoundShape currentShape = shapes.get(currentShapeIndex);
            
            // Check if the answer is correct (allowing a 5% error margin)
            if (Math.abs(userAnswer - currentShape.getCorrectAnswer()) / currentShape.getCorrectAnswer() <= 0.05) {
                // Stop the timer
                if (timer != null) {
                    timer.cancel();
                    timerRunning = false;
                }
        
                  // 记录分数 - 根据尝试次数计算得分
                recordScore(attempt + 1);
                
                // Improvement: Add text and symbol feedback
                JOptionPane.showMessageDialog(this, "Correct! :)"); // Updated text and symbol
                practisedShapes++;

                // 更新进度条 - 确保总数为shapes.size()
                updateProgress(practisedShapes, shapes.size());
                
                // Mark this shape as completed
                currentShape.setCompleted(true);
                
                // Check if all shapes are completed
                if (practisedShapes >= shapes.size()) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You have completed all compound shape exercises! :)"); // Added symbol
                    // 修复：完成所有形状后返回主界面
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    if (topFrame instanceof com.shapeville.main.MainFrame) {
                        com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
                        mainFrame.getTaskManager().currentTaskTypeCompleted(new com.shapeville.logic.TaskLogic(){});
                        mainFrame.navigateToHome();
                    }
                } else {
                    // Move to the next incomplete shape
                    moveToNextShape();
                    // 修复：重新开始计时器
                    resetTimer();
                }
            } else{
                attempt++;
                if (attempt >= 3) {
                    // Stop the timer
                    if (timer != null) {
                        timer.cancel();
                        timerRunning = false;
                    }
                    
                    showSolution();
                    
                    // Mark this shape as completed
                    currentShape.setCompleted(true);
                    practisedShapes++;
                    
                    // 更新进度条 - 无论答对答错都更新进度
                    updateProgress(practisedShapes, shapes.size());
                    
                    // Check if all shapes are completed
                    if (practisedShapes >= shapes.size()) {
                        JOptionPane.showMessageDialog(this, "You have completed all compound shape exercises! :)"); // Added symbol
                        // Return to home page or proceed to the next task
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        if (topFrame instanceof com.shapeville.main.MainFrame) {
                            com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
                            mainFrame.getTaskManager().currentTaskTypeCompleted(new com.shapeville.logic.TaskLogic(){});
                            mainFrame.navigateToHome();
                        }
                    } else {
                        // Move to the next incomplete shape
                        moveToNextShape();
                        // 修复：重新开始计时器
                        resetTimer();
                    }
                } else {
                    // Improvement: Add text and symbol feedback
                    JOptionPane.showMessageDialog(this, "Try again! :(\nYou have " + (3 - attempt) + " attempts remaining."); // Updated text and symbol
                }
            }
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number! :("); // Added symbol
        }
    }
    

    /**
     * Displays the solution for the current shape.
     */
    private void showSolution() {
        CompoundShape currentShape = shapes.get(currentShapeIndex);
        
        StringBuilder solution = new StringBuilder();
        solution.append("The correct answer is: ").append(currentShape.getCorrectAnswer()).append("\n\n");
        solution.append("Solution:\n");
        for (String hint : currentShape.getHints()) {
            solution.append("- ").append(hint).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, solution.toString(), 
                "Solution for Shape " + (currentShapeIndex + 1), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Moves to the next incomplete shape.
     */
    private void moveToNextShape() {
        // Find the next incomplete shape
        int nextIndex = (currentShapeIndex + 1) % shapes.size();
        while (shapes.get(nextIndex).isCompleted() && nextIndex != currentShapeIndex) {
            nextIndex = (nextIndex + 1) % shapes.size();
        }
        
        currentShapeIndex = nextIndex;
        answerField.setText("");
        attempt = 0; // 修复：重置尝试次数
        showCurrentShape();
    }
    
    // 添加记录分数的方法
/**
 * 记录用户得分
 * @param attemptsUsed 用户使用的尝试次数
 */
private void recordScore(int attemptsUsed) {
    // 获取MainFrame实例
    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    if (topFrame instanceof com.shapeville.main.MainFrame) {
        com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
        // 获取ScoreManager并记录分数
        com.shapeville.logic.ScoreManager scoreManager = mainFrame.getScoreManager();
        if (scoreManager != null) {
            // 复合形状计算是高级任务，使用高级评分
            int points = scoreManager.calculatePoints(attemptsUsed, true);
            scoreManager.recordScoreAndFeedback(points);
        }
    }
}
    /**
     * Inner class representing a compound shape.
     */
    private class CompoundShape {
        private String description;
        private String question;
        private double correctAnswer;
        private String[] hints;
        private ImageIcon image;
        private boolean completed;
        
        /**
         * Constructor for CompoundShape.
         * @param description The description of the shape.
         * @param question The question about the shape.
         * @param correctAnswer The correct area value.
         * @param hints The solution hints.
         * @param image The shape image.
         */
        public CompoundShape(String description, String question, double correctAnswer, String[] hints, ImageIcon image) {
            this.description = description;
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.hints = hints;
            this.image = image;
            this.completed = false;
        }
        
        /**
         * Gets the description of the shape.
         * @return The description.
         */
        public String getDescription() {
            return description;
        }
        
        /**
         * Gets the question about the shape.
         * @return The question.
         */
        public String getQuestion() {
            return question;
        }
        
        /**
         * Gets the correct answer (area).
         * @return The correct area value.
         */
        public double getCorrectAnswer() {
            return correctAnswer;
        }
        
        /**
         * Gets the solution hints.
         * @return The hints as an array of strings.
         */
        public String[] getHints() {
            return hints;
        }
        
        /**
         * Gets the shape image.
         * @return The image.
         */
        public ImageIcon getImage() {
            return image;
        }
        
        /**
         * Checks if this shape is completed.
         * @return True if completed, otherwise false.
         */
        public boolean isCompleted() {
            return completed;
        }
        
        /**
         * Sets the completion status of this shape.
         * @param completed The completion status.
         */
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    /**
 * 更新进度条
 * @param current 当前完成的形状数量
 * @param total 总形状数量
 */
private void updateProgress(int current, int total) {
    // 获取MainFrame实例
    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    if (topFrame instanceof com.shapeville.main.MainFrame) {
        com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
        // 获取NavigationBar并更新进度
        // 修复：确保current不会超过total
        int displayCurrent = Math.min(current, total);
        mainFrame.getNavigationBar().updateProgress(displayCurrent, total);
    }
}

}
