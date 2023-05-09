import javax.swing.*;
import java.awt.*;

public class StoreSelectionPage extends JFrame {

    private Store store;

    public StoreSelectionPage(Store store) {
        this.store = store;

        setTitle("Choose a store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(new LandingPage.GradientBackground());
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Choose a store", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        titleLabel.setForeground(Color.WHITE);

        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton targetButton = new JButton("Target");
        targetButton.setFont(new Font("Serif", Font.PLAIN, 30));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(targetButton);
        buttonPanel.add(Box.createHorizontalStrut(50));

        JButton walmartButton = new JButton("Walmart");
        walmartButton.setFont(new Font("Serif", Font.PLAIN, 30));
        buttonPanel.add(walmartButton);
        buttonPanel.add(Box.createHorizontalStrut(50));

        JButton safeway = new JButton("Safeway");
        safeway.setFont(new Font("Serif", Font.PLAIN, 30));
        buttonPanel.add(safeway);
        buttonPanel.add(Box.createHorizontalGlue());

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setOpaque(false);
        outerPanel.add(buttonPanel);

        add(outerPanel, BorderLayout.CENTER);

        targetButton.addActionListener(e -> {
            new ItemSelectionPage(this.store);
            this.dispose();
        });
        walmartButton.addActionListener(e -> {
            new ItemSelectionPage(this.store);
            this.dispose();
        });
        safeway.addActionListener(e -> {
            new ItemSelectionPage(this.store);
            this.dispose();
        });

        setVisible(true);
    }
}
