package com.shapeville.ui.panel_templates;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.shapeville.logic.ScoreManager;

/**
 * The EndPanel class represents the screen displayed at the end of a user session.
 * It shows a thank you message and the user's final score for the session.
 */
public class EndPanel extends JPanel {

    /**
     * Constructs a new EndPanel.
     * Retrieves the final score from the {@link ScoreManager} and displays it along with a thank you message.
     * @param scoreManager The {@link ScoreManager} containing the final score.
     */
    public EndPanel(ScoreManager scoreManager) {
        int finalScore = scoreManager.getCurrentScore(); // 调用 getCurrentScore()
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        JLabel label = new JLabel("Thank you for playing Shapeville!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Display final score
        JLabel scoreLabel = new JLabel("Your final score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        add(scoreLabel, BorderLayout.SOUTH);
    }
}
