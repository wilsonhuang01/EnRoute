import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StoreUI extends JFrame {

    private JButton exitButton;
    private ArrayList<Store.Item> shoppingCart;
    private Store store;

    public StoreUI(Store store, char[][] storeLayout, int rows, int cols, Store.Route route, ArrayList<Store.Item> items) {
        this.store = store; // Add this line
        this.shoppingCart = items;

        setTitle("Grocery Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            shoppingCart.clear(); // Clear the shopping cart
            new LandingPage(this.store); // Pass the store instance
            this.dispose();
        });

        mainPanel.add(exitButton, BorderLayout.NORTH);

        StorePanel storePanel = new StorePanel(storeLayout, rows, cols, route, items);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        storePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding to the storePanel
        mainPanel.add(storePanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }


    static class StorePanel extends JPanel {
        char[][] store;
        int rows, cols;
        Store.Route route;
        ArrayList<Store.Item> items;

        StorePanel(char[][] store, int rows, int cols, Store.Route route, ArrayList<Store.Item> items) {
            this.store = store;
            this.rows = rows;
            this.cols = cols;
            this.route = route;
            this.items = items;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Calculate the size of each cell
            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

            // Calculate the total width and height of the grid
            int totalWidth = cols * cellSize;
            int totalHeight = rows * cellSize;

            // Calculate the offset needed to center the grid
            int xOffset = (getWidth() - totalWidth) / 2;
            int yOffset = (getHeight() - totalHeight) / 2;

            // Translate the graphics context to the offset position
            g.translate(xOffset, yOffset);

            // Call the methods to paint the store, the route and items with adjusted coordinates
            paintStore(g, cellSize);
            paintRouteBySegment(g, cellSize);
            drawRoute(g, cellSize);
            displayItems(g, cellSize);

            // Translate the graphics context back to the original position
            g.translate(-xOffset, -yOffset);
        }


        private void paintStore(Graphics g, int cellSize) {
            for (int r = rows - 1; r >= 0; r--) { // Reverse the row iteration order
                for (int c = 0; c < cols; c++) {
                    Color color;
                    switch (store[r][c]) {
                        case '.':
                            color = Color.WHITE;
                            break;
                        case '#':
                            color = Color.BLACK;
                            break;
                        case 'E':
                            color = Color.BLUE;
                            break;
                        default:
                            color = Color.RED;
                    }

                    g.setColor(color);
                    g.fillRect(c * cellSize, (rows - r - 1) * cellSize, cellSize, cellSize); // Reverse the row coordinate

                    g.setColor(Color.BLACK);
                    g.drawRect(c * cellSize, (rows - r - 1) * cellSize, cellSize, cellSize); // Reverse the row coordinate
                }
            }
        }

        private void paintRouteBySegment(Graphics g, int cellSize) {
            for (int i = 0; i < route.segments.size(); i++) {
                Store.Segment segment = route.segments.get(i);
                for (int j = 0; j < segment.nodes.size(); j++) {
                    Store.Node node = segment.nodes.get(j);

                    Color color;
                    switch (store[node.row][node.col]) {
                        case '.':
                            color = new Color(0, 1, 0, (route.segments.size() - i) * 1.0f / route.segments.size());
                            break;
                        case 'E':
                            color = Color.BLUE;
                            break;
                        default:
                            color = Color.RED;
                            break;
                    }

                    g.setColor(color);
                    g.fillRect(node.col * cellSize, (rows - node.row - 1) * cellSize, cellSize, cellSize);

                    g.setColor(Color.BLACK);
                    g.drawRect(node.col * cellSize, (rows - node.row - 1) * cellSize, cellSize, cellSize);
                }
            }
        }

        private void drawRoute(Graphics g, int cellSize) {
            if (route.nodes.size() == 0) return;

            Store.Node prev = route.nodes.get(0);
            Store.Node curr;

            for (int i = 1; i < route.nodes.size(); i++) {
                curr = route.nodes.get(i);

                g.setColor(Color.BLACK);
                g.drawLine((int) ((prev.col + 0.5) * cellSize), (int) ((rows - prev.row - 0.5) * cellSize),
                        (int) ((curr.col + 0.5) * cellSize), (int) ((rows - curr.row - 0.5) * cellSize));

                prev = curr;
            }
        }

        private void displayItems(Graphics g, int cellSize) {
            int padding = 10;

            g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); // Increase the font size

            for (Store.Item item : items) {
                g.setColor(Color.BLACK);
                int xPos = item.col * cellSize + padding;
                int yPos = (rows - item.row) * cellSize - padding;
                //System.out.println("Drawing " + item.id + " at " + xPos + ", " + yPos);  // Output item position to the console
                g.drawString(item.id, xPos, yPos);
            }
        }
    }
}
