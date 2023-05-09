import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemSelectionPage extends JFrame {
    private Store store;
    private Map<JCheckBox, Store.Item> checkBoxItemMap;
    private ArrayList<Store.Item> shoppingCart;

    public ItemSelectionPage(Store store) {
        this.store = store;
        this.checkBoxItemMap = new HashMap<>();
        this.shoppingCart = new ArrayList<>();

        setTitle("Item Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Select items you want to add to your shopping cart:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        add(titleLabel, BorderLayout.NORTH);

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel column1 = new JPanel();
        column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
        JPanel column2 = new JPanel();
        column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));

        int i = 0;
        for (Store.Item item : store.getAllItems().values()) {
            JCheckBox checkBox = new JCheckBox(item.id);
            checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (i % 2 == 0) {
                column1.add(checkBox);
            } else {
                column2.add(checkBox);
            }
            checkBoxItemMap.put(checkBox, item);
            i++;
        }

        itemPanel.add(Box.createHorizontalGlue());
        itemPanel.add(column1);
        itemPanel.add(Box.createHorizontalStrut(50));  // This creates a space between the two columns
        itemPanel.add(column2);
        itemPanel.add(Box.createHorizontalGlue());

        add(itemPanel, BorderLayout.CENTER);

        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Serif", Font.PLAIN, 30));
        nextButton.addActionListener(e -> {
            for (JCheckBox checkBox : checkBoxItemMap.keySet()) {
                if (checkBox.isSelected()) {
                    shoppingCart.add(checkBoxItemMap.get(checkBox));
                }
            }

            ArrayList<Store.Item> itemsCopy = new ArrayList<>();
            itemsCopy.addAll(shoppingCart);

            char[][] storeLayout = Store.createStore(shoppingCart);
            Store.Route route = store.computeRoute(storeLayout, shoppingCart);

            // Pass 'store' as the first argument to StoreUI
            new StoreUI(store, storeLayout, Store.ROWS, Store.COLS, route, itemsCopy);
            this.dispose();
        });

        add(nextButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
