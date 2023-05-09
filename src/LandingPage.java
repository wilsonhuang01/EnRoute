import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    static class GradientBackground extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = Color.BLUE;
            Color color2 = Color.CYAN;
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public LandingPage(Store store) {
        setTitle("En-Route");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(new GradientBackground()); // Set the gradient background
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("En-Route", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0)); // Add some space above the title
        titleLabel.setForeground(Color.WHITE); // Set the text color to white

        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the panel to center

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Serif", Font.PLAIN, 30));
        startButton.setMaximumSize(new Dimension(200, 50)); // Set maximum size
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the button to center
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(50)); // Add spacing between the buttons

        JButton aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Serif", Font.PLAIN, 30));
        aboutButton.setMaximumSize(new Dimension(200, 50)); // Set maximum size
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the button to center
        buttonPanel.add(aboutButton);
        buttonPanel.add(Box.createVerticalStrut(50)); // Add spacing between the buttons

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Serif", Font.PLAIN, 30));
        exitButton.setMaximumSize(new Dimension(200, 50)); // Set maximum size
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the button to center
        buttonPanel.add(exitButton);

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setOpaque(false); // Make this panel transparent so the gradient shows through
        outerPanel.add(buttonPanel);
        add(outerPanel, BorderLayout.CENTER);

        // Button actions
        startButton.addActionListener(e -> {
            new StoreSelectionPage(store);
            this.dispose();
        });
        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "En-Route is a grocery store route optimization application.");
        });
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}
