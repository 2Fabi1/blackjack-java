package Panele;

import javax.swing.*;
import java.awt.*;

public class mainMenuPanel extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;

    public mainMenuPanel(Runnable onStart,int chips) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 120, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 50, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("BLACKJACK", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);

        JButton startBtn = new JButton("Start Game");
        JButton resetBtn = new JButton("Reset Chips");
        JButton exitBtn = new JButton("Exit");

        startBtn.setFont(new Font("Arial", Font.BOLD, 20));
        resetBtn.setFont(new Font("Arial", Font.BOLD, 20));
        exitBtn.setFont(new Font("Arial", Font.BOLD, 20));

        startBtn.addActionListener(e -> {onStart.run();});

        resetBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chips reset to 100!");
        });

        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        startBtn = ustawPrzycisk(startBtn, new Color(255, 20, 255), new Color(220, 20, 220));
        add(startBtn, gbc);

        gbc.gridy = 2;
        resetBtn = ustawPrzycisk(resetBtn, new Color(20, 255, 20), new Color(20, 220, 20));
        add(resetBtn, gbc);

        gbc.gridy = 3;
        exitBtn = ustawPrzycisk(exitBtn, new Color(153, 87, 68), new Color(130, 70, 50));
        add(exitBtn, gbc);
    }

    private JButton ustawPrzycisk(JButton button, Color color, Color borderColor) {
        gamePanel.ustawPrzycisk(button, color, borderColor);
        return button;
    }
}