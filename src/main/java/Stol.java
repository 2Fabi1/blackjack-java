import javax.swing.*;
import java.awt.*;

public class Stol extends JFrame {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;

    public Stol() {
        super("Blackjack");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Optional: default background (can be overridden by panels)
        getContentPane().setBackground(new Color(0, 102, 0));

        setLayout(new BorderLayout());

        setVisible(true);
    }
}