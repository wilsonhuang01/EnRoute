// StoreUI.java

import javax.swing.*;
import java.awt.*;

public class StoreUI extends JFrame {
    public StoreUI(char[][] store, int rows, int cols) {
        setTitle("Grocery Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        StorePanel storePanel = new StorePanel(store, rows, cols);
        add(storePanel);
        setVisible(true);
    }

    static class StorePanel extends JPanel {
        char[][] store;
        int rows, cols;

        StorePanel(char[][] store, int rows, int cols) {
            this.store = store;
            this.rows = rows;
            this.cols = cols;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

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
    }
}
