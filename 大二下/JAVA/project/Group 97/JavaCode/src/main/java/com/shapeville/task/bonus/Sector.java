package com.shapeville.task.bonus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Bonus 2: Sector Area Calculation
 * Allows users to choose a sector and calculate its area.
 */
public class Sector extends JPanel {
    private JTextField answerField;
    private int attempt = 0;
    private int currentSectorIndex = 0;
    private JLabel sectorImageLabel;
    private JLabel questionLabel;
    private JLabel timerLabel;
    private List<SectorShape> sectors;
    private int practicedSectors = 0;
    private JPanel content;
    private Timer timer;
    private int timeRemaining = 300; // 5 minutes (300 seconds)
    private boolean timerRunning = false;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private JLabel sectorValuesLabel;

    /**
     * Constructor
     */
    public Sector() {
        setLayout(new BorderLayout());
        // Apply high contrast background to the main panel
        setBackground(new Color(30, 30, 30));

        content = new JPanel();
        content.setBackground(new Color(30, 30, 30)); // Match main panel background
        add(content, BorderLayout.CENTER);
        initializeSectors();
        setupUI();
    }

    /**
     * Initializes all sectors with their descriptions, questions, correct answers, and solution hints.
     */
    private void initializeSectors() {
        sectors = new ArrayList<>();
        
        // Add 8 sectors from Figure 13
        // Sector 1: 90-degree sector, radius 8 cm
        sectors.add(new SectorShape(
            "Sector 1",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            50.24, // (90/360) * π * 8² 
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (90/360) × 3.14 × 8² = 50.24 square centimeters"},
            createSectorImage(1),
            "Radius = 8 cm, Central angle = 90°"
        ));
        
        // Sector 2: 100-degree sector, radius 35 meters
        sectors.add(new SectorShape(
            "Sector 2",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            1068.47, // (100/360) * π * 35²=1068.47 square meters
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (100/360) × 3.14 × 35² = 1068.47 square meters"},
            createSectorImage(2),
            "Radius = 35 meters, Central angle = 100°"
        ));
        
        // Sector 3: 110-degree sector, radius 22 feet
        sectors.add(new SectorShape(
            "Sector 3",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            464.37, // (110/360) * π * 22² 
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (110/360) × 3.14 × 22² = 464.37 square feet"},
            createSectorImage(3),
            "Radius = 22 feet, Central angle = 110°"
        ));
        
        // Sector 4: 130-degree sector, radius 18 feet
        sectors.add(new SectorShape(
            "Sector 4",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            367.38, // [(130/360) * π * 18²] 
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (130/360) × 3.14 × 18² = 367.38 square feet"},
            createSectorImage(4),
            "Radius = 18 feet, Central angle = 130°"
        ));
        
        // Sector 5: 240-degree sector, radius 14 cm
        sectors.add(new SectorShape(
            "Sector 5",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            410.29, // (240/360) * π * 14²
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (240/360) × 3.14 × 14² = 410.29 square centimeters"},
            createSectorImage(5),
            "Radius = 14 cm, Central angle = 240°"
        ));
        
        // Sector 6: 250-degree sector, radius 15 mm
        sectors.add(new SectorShape(
            "Sector 6",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            490.63, // (250/360) * π * 15²
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (250/360) × 3.14 × 15² = 490.63 square millimeters"},
            createSectorImage(6),
            "Radius = 15 mm, Central angle = 250°"
        ));
        
        // Sector 7: 270-degree sector, radius 8 inches
        sectors.add(new SectorShape(
            "Sector 7",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            150.72, // (270/360) * π * 8² 
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (270/360) × 3.14 × 8² = 410.29 square inches"},
            createSectorImage(7),
            "Radius = 8 inches, Central angle = 270°"
        ));
        
        // Sector 8: 280-degree sector, radius 12 yards
        sectors.add(new SectorShape(
            "Sector 8",
            "Calculate the area of the shaded region. Round to two decimal places. (Use π = 3.14)",
            351.68, // (280/360) * π * 12²
            new String[]{"Sector area = (central angle/360°) × π × radius²", "= (280/360) × 3.14 × 12² = 351.68 square yards"},
            createSectorImage(8),
            "Radius = 12 yards, Central angle = 280°"
        ));
    }

    /**
     * Creates an image for the sector.
     * @param sectorNumber The number of the sector.
     * @return The sector image.
     */
    private ImageIcon createSectorImage(int sectorNumber) {
        // Load image from resources/assets/sector directory
        String imagePath = "/assets/sectors/sector" + sectorNumber + ".png";
        return com.shapeville.utils.ImageLoader.loadImageAndScale(imagePath, 300, 300);
    }

    /**
     * Sets up the user interface components.
     */
    private void setupUI() {
        content.setLayout(new BorderLayout(10, 10));
        
        // North area for title, timer and attempts
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        // 创建顶部选择面板 - 模仿图片中的界面
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectionPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        JLabel selectLabel = new JLabel("Select shape:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectLabel.setForeground(Color.WHITE); // High contrast text
        selectionPanel.add(selectLabel);
        
        // 添加扇形选择按钮 - 使用单个按钮"Sector"
        JButton sectorButton = new JButton("Sector");
        sectorButton.setBackground(new Color(80, 80, 80)); // High contrast background
        sectorButton.setForeground(Color.WHITE); // High contrast text
        sectorButton.addActionListener(e -> {
            // 显示扇形选择对话框
            String[] options = new String[sectors.size()];
            for (int i = 0; i < sectors.size(); i++) {
                options[i] = "Sector " + (i + 1);
            }
            
            int choice = JOptionPane.showOptionDialog(
                this,
                "Select one of the 8 options of a sector of a circle:",
                "Select Sector",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice >= 0) {
                currentSectorIndex = choice;
                resetTimer(); // 重置计时器
                attempt = 0;  // 重置尝试次数
                showCurrentSector();
            }
        });
        selectionPanel.add(sectorButton);
        
        northPanel.add(selectionPanel, BorderLayout.NORTH);
        
        // 添加时间和尝试次数面板
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        timerLabel = new JLabel("Time left: 05:00");
        timerLabel.setForeground(Color.YELLOW); // High contrast text for status
        JLabel attemptsLabel = new JLabel("Attempts left: 3");
        attemptsLabel.setForeground(Color.YELLOW); // High contrast text for status
        infoPanel.add(timerLabel);
        infoPanel.add(attemptsLabel);
        northPanel.add(infoPanel, BorderLayout.CENTER);
        
        content.add(northPanel, BorderLayout.NORTH);
        
        // Center area for problem description and sector image
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        JPanel infoPanel2 = new JPanel(new BorderLayout());
        infoPanel2.setBackground(new Color(30, 30, 30)); // Match main panel background
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setForeground(Color.WHITE); // High contrast text
        infoPanel2.add(questionLabel, BorderLayout.NORTH);
        
        JLabel formulaLabel = new JLabel("<html><body style='width: 300px; text-align: center;'>" +
                "Sector area = (central angle/360°) × π × radius²</body></html>", SwingConstants.CENTER);
        formulaLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        formulaLabel.setForeground(Color.CYAN); // High contrast text for formula
        infoPanel2.add(formulaLabel, BorderLayout.CENTER);
        
        centerPanel.add(infoPanel2, BorderLayout.NORTH);
        
        sectorImageLabel = new JLabel("", SwingConstants.CENTER);
        sectorImageLabel.setPreferredSize(new Dimension(300, 300));
        sectorImageLabel.setBackground(new Color(50, 50, 50)); // Contrast background for container
        centerPanel.add(sectorImageLabel, BorderLayout.CENTER);

        // 新增：扇形数值信息标签
        sectorValuesLabel = new JLabel("", SwingConstants.CENTER);
        sectorValuesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sectorValuesLabel.setForeground(Color.WHITE); // High contrast text
        centerPanel.add(sectorValuesLabel, BorderLayout.SOUTH);
        
        content.add(centerPanel, BorderLayout.CENTER);
        
        // South area for input and buttons
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.setBackground(new Color(30, 30, 30)); // Match main panel background
        
        JLabel answerLabel = new JLabel("Enter area:");
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
        
        // Display the first sector
        showCurrentSector();
        startTimer();
        
        // 更新尝试次数标签
        attempt = 0;
        attemptsLabel.setText("Attempts left: " + (3 - attempt));
    }
    
    /**
     * Displays the current sector information.
     */
    private void showCurrentSector() {
        SectorShape currentSector = sectors.get(currentSectorIndex);
        questionLabel.setText("<html><body style='width: 400px; text-align: center;'>" +
                              currentSector.getDescription() + "<br>" +
                              currentSector.getQuestion() + "</body></html>");
        
        // Display the sector image
        if (currentSector.getImage() != null) {
            sectorImageLabel.setIcon(currentSector.getImage());
            sectorImageLabel.setText("");
        } else {
            // If no image, display the sector description
            sectorImageLabel.setIcon(null);
            sectorImageLabel.setText("Sector " + (currentSectorIndex + 1) + " / " + sectors.size());
        }
        
        // 更新扇形数值信息标签
        sectorValuesLabel.setText(currentSector.getValues());
        
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
                        JOptionPane.showMessageDialog(Sector.this, 
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
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter an answer! :("); // Updated message and symbol
            return;
        }
        
        try {
            double userAnswer = Double.parseDouble(input);
            SectorShape currentSector = sectors.get(currentSectorIndex);
            
            // Check if the answer is correct (allowing a 5% error margin)
            if (Math.abs(userAnswer - currentSector.getCorrectAnswer()) / currentSector.getCorrectAnswer() <= 0.05) {
                // Stop the timer
                if (timer != null) {
                    timer.cancel();
                    timerRunning = false;
                }
                // 记录分数 - 根据尝试次数计算得分
                recordScore(attempt + 1);
                
                // Mark this sector as completed
                currentSector.setCompleted(true);
                practicedSectors++;
                
                // 修复问题1：先增加完成数量，再更新进度条
                updateProgress(practicedSectors, sectors.size());
                
                // Improvement: Add text and symbol feedback
                JOptionPane.showMessageDialog(this, "Correct! :)"); // Updated message and symbol
                
                // Check if all sectors are completed
                if (practicedSectors >= sectors.size()) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You have completed all sector area calculation exercises! :)"); // Updated message and symbol
                    // Return to home page or proceed to the next task
                    javax.swing.JFrame topFrame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame instanceof com.shapeville.main.MainFrame) {
                    ((com.shapeville.main.MainFrame) topFrame).navigateToHome();
                }
                } else {
                    // Move to the next incomplete sector
                    moveToNextSector();
                }
            } else {
                attempt++;
                if (attempt >= 3) {
                    // Stop the timer
                    if (timer != null) {
                        timer.cancel();
                        timerRunning = false;
                    }
                    
                    showSolution();
                    
                    // Mark this sector as completed
                    currentSector.setCompleted(true);
                    practicedSectors++;
                    
                    // 修复问题1：先增加完成数量，再更新进度条
                    updateProgress(practicedSectors, sectors.size());
                    
                    // Check if all sectors are completed
                    if (practicedSectors >= sectors.size()) {
                        JOptionPane.showMessageDialog(this, "You have completed all sector area calculation exercises! :)"); // Updated message and symbol
                        // Return to home page or proceed to the next task
                        javax.swing.JFrame topFrame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);
                    if (topFrame instanceof com.shapeville.main.MainFrame) {
                        ((com.shapeville.main.MainFrame) topFrame).navigateToHome();
                    }
                    } else {
                        // Move to the next incomplete sector
                        moveToNextSector();
                    }
                } else {
                    // Improvement: Add text and symbol feedback
                    JOptionPane.showMessageDialog(this, "Try again! :(\nYou have " + (3 - attempt) + " attempts remaining."); // Updated message and symbol
                }
            }
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number! :("); // Updated message and symbol
        }
    }
    
    /**
     * Displays the solution for the current sector.
     */
    private void showSolution() {
        SectorShape currentSector = sectors.get(currentSectorIndex);
        
        StringBuilder solution = new StringBuilder();
        solution.append("The correct answer is: ").append(df.format(currentSector.getCorrectAnswer())).append("\n\n");
        solution.append("Solution:\n");
        for (String hint : currentSector.getHints()) {
            solution.append("- ").append(hint).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, solution.toString(), 
                "Solution for Sector " + (currentSectorIndex + 1), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Moves to the next incomplete sector.
     */
    private void moveToNextSector() {
        // Find the next incomplete sector
        int nextIndex = (currentSectorIndex + 1) % sectors.size();
        while (sectors.get(nextIndex).isCompleted() && nextIndex != currentSectorIndex) {
            nextIndex = (nextIndex + 1) % sectors.size();
        }
        
        currentSectorIndex = nextIndex;
        answerField.setText("");
        attempt = 0; // 重置尝试次数
        
        // 修复问题2：确保在这里重置计时器
        resetTimer();
        
        showCurrentSector();
    }
    
    /**
     * Inner class representing a circular sector.
     */
    private class SectorShape {
        private String description;
        private String question;
        private double correctAnswer;
        private String[] hints;
        private ImageIcon image;
        private boolean completed;
        private String values;
        
        /**
         * Constructor for SectorShape.
         * @param description The description of the sector.
         * @param question The question about the sector.
         * @param correctAnswer The correct area value.
         * @param hints The solution hints.
         * @param image The sector image.
         * @param values The radius and angle values of the sector.
         */
        public SectorShape(String description, String question, double correctAnswer, String[] hints, ImageIcon image, String values) {
            this.description = description;
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.hints = hints;
            this.image = image;
            this.completed = false;
            this.values = values;
        }
        
        /**
         * Gets the description of the sector.
         * @return The description.
         */
        public String getDescription() {
            return description;
        }
        
        /**
         * Gets the question about the sector.
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
         * Gets the sector image.
         * @return The image.
         */
        public ImageIcon getImage() {
            return image;
        }
        
        /**
         * Checks if this sector is completed.
         * @return True if completed, otherwise false.
         */
        public boolean isCompleted() {
            return completed;
        }
        
        /**
         * Sets the completion status of this sector.
         * @param completed The completion status.
         */
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
        
        /**
         * Gets the radius and angle values of the sector.
         * @return The values.
         */
        public String getValues() {
            return values;
        }
    }

    /**
     * 更新进度条
     * @param current 当前完成的扇形数量
     * @param total 总扇形数量
     */
    private void updateProgress(int current, int total) {
        // 获取MainFrame实例
        javax.swing.JFrame topFrame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof com.shapeville.main.MainFrame) {
            com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
            // 获取NavigationBar并更新进度
            mainFrame.getNavigationBar().updateProgress(current, total);
        }
    }

    /**
     * 记录用户得分
     * @param attemptsUsed 用户使用的尝试次数
     */
    private void recordScore(int attemptsUsed) {
        // 获取MainFrame实例
        javax.swing.JFrame topFrame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof com.shapeville.main.MainFrame) {
            com.shapeville.main.MainFrame mainFrame = (com.shapeville.main.MainFrame) topFrame;
            // 获取ScoreManager并记录分数
            com.shapeville.logic.ScoreManager scoreManager = mainFrame.getScoreManager();
            if (scoreManager != null) {
                // 扇形区域计算是高级任务，使用高级评分
                int points = scoreManager.calculatePoints(attemptsUsed, true);
                scoreManager.recordScoreAndFeedback(points);
            }
        }
    }
}

