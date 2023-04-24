import javax.swing.*;
import java.util.*;

class Store {
    static final int ROWS = 10;
    static final int COLS = 10;
    static final char EMPTY = '.';
    static final char STAND = '#';
    static final char ENTRANCE = 'E';

    static class Item {
        String id;
        int row, col;

        Item(String id, int row, int col) {
            this.id = id;
            this.row = row;
            this.col = col;
        }
    }

    static class Node {
        int row, col, dist;

        Node(int r, int c, int d) {
            row = r;
            col = c;
            dist = d;
        }
    }

    static char[][] createStore(List<Item> items) {
        char[][] store = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(store[r], EMPTY);
        }

        // stand coordinates
        int[][] standCoordinates = {
                {3, 3},
                {3, 4},
                {3, 5},
                {3, 6},
                {5, 3},
                {5, 4},
                {5, 5},
                {5, 6}
        };

        for (int[] coord : standCoordinates) {
            store[coord[0]][coord[1]] = STAND;
        }

        store[ROWS - 1][COLS - 2] = ENTRANCE;

        for (Item item : items) {
            store[item.row][item.col] = item.id.charAt(0);
        }
        return store;
    }


    static void printStore(char[][] store) {
        for (char[] row : store) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    static int dijkstra(char[][] store, int[] start, int[] end) {
        boolean[][] visited = new boolean[ROWS][COLS];
        int[][] dist = new int[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(dist[r], Integer.MAX_VALUE);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.dist));
        pq.add(new Node(start[0], start[1], 0));
        dist[start[0]][start[1]] = 0;
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            int r = curr.row;
            int c = curr.col;

            if (visited[r][c]) {
                continue;
            }
            visited[r][c] = true;

            if (r == end[0] && c == end[1]) {
                return dist[r][c];
            }

            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && !visited[nr][nc] && store[nr][nc] != STAND) {
                    int newDist = dist[r][c] + 1;
                    if (newDist < dist[nr][nc]) {
                        dist[nr][nc] = newDist;
                        pq.add(new Node(nr, nc, newDist));
                    }
                }
            }
        }

        return -1;
    }

    static int findShortestPathBetweenPoints(char[][] store, int startRow, int startCol, int endRow, int endCol) {
        int[] start = {startRow, startCol};
        int[] end = {endRow, endCol};
        return dijkstra(store, start, end);
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>(Arrays.asList(
                new Item("Apples", 6, 3),
                new Item("Bananas", 2, 3),
                new Item("Chocolates", 2, 6),
                new Item("Donuts", 6, 6),
                new Item("Flour", 0, 0)
        ));

        char[][] store = createStore(items);
        printStore(store);

        int entranceRow = ROWS - 1;
        int entranceCol = COLS - 2;
        int totalDistance = 0;

        int currentRow = entranceRow;
        int currentCol = entranceCol;

        while (!items.isEmpty()) {
            Item closestItem = null;
            int minDistance = Integer.MAX_VALUE;

            for (Item item : items) {
                int distance = findShortestPathBetweenPoints(store, currentRow, currentCol, item.row, item.col);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestItem = item;
                }
            }

            System.out.println("Distance from (" + currentRow + ", " + currentCol + ") to " + closestItem.id + ": " + minDistance);
            totalDistance += minDistance;
            currentRow = closestItem.row;
            currentCol = closestItem.col;
            items.remove(closestItem);
        }

        System.out.println("Total distance: " + totalDistance);

        // Display the UI
        SwingUtilities.invokeLater(() -> new StoreUI(store, ROWS, COLS));
    }
}
