// StoreUI.java

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StoreUI extends JFrame {
    public StoreUI(char[][] store, int rows, int cols, Store.Route route, ArrayList<Store.Item> items) {
        setTitle("Grocery Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        StorePanel storePanel = new StorePanel(store, rows, cols, route, items);
        add(storePanel);
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

            //printNodes(route);
            //printSegments(route);
        }

        private void printNodes(Store.Route route) {
            for (int i = 0; i < route.nodes.size(); i++) {
                System.out.print("(" + route.nodes.get(i).row + ", " + route.nodes.get(i).col + ") -> ");
            }
            System.out.println();
        }

        private void printSegments(Store.Route route) {
            for (int i = 0; i < route.segments.size(); i++) {
                Store.Segment s = route.segments.get(i);
                System.out.print("Segment " + (i + 1) + ": ");
                for (int j = 0; j < s.nodes.size(); j++) {
                    System.out.print("(" + s.nodes.get(j).row + ", " + s.nodes.get(j).col + ") -> ");
                }
                System.out.println();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

            paintStore(g, cellSize);
            //paintRouteByNode(g, cellSize);
            paintRouteBySegment(g, cellSize);
            drawRoute(g, cellSize);
            displayItems(g, cellSize);
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

        private void paintRouteByNode(Graphics g, int cellSize) {
            for (int i = 0; i < route.nodes.size(); i++) {
                Store.Node node = route.nodes.get(i);

                Color color;
                switch (store[node.row][node.col]) {
                    case '.':
                        color = new Color(0, 1, 0,  (route.nodes.size() - i) * 1.0f / route.nodes.size());
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

        private void paintRouteBySegment(Graphics g, int cellSize) {
            for (int i = 0; i < route.segments.size(); i++) {
                Store.Segment segment = route.segments.get(i);
                for (int j = 0; j < segment.nodes.size(); j++) {
                    Store.Node node = segment.nodes.get(j);

                    Color color;
                    switch (store[node.row][node.col]) {
                        case '.':
                            color = new Color(0, 1, 0,  (route.segments.size() - i) * 1.0f / route.segments.size());
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

        // TODO: don't make lines overlap
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

            for (Store.Item item : items) {
                g.setColor(Color.BLACK);
                g.drawString(item.id, item.col * cellSize + padding, (rows - item.row) * cellSize - padding);
            }
        }
    }
}
