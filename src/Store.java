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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return row == node.row && col == node.col && dist == node.dist;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, dist);
        }

        @Override
        public String toString() {
            return "Node{row=" + row + ", col=" + col + ", dist=" + dist + '}';
        }
    }

    static class Segment {
        List<Node> nodes;

        Segment() { nodes = new ArrayList<>(); }
        Segment(List<Node> nodes) { this.nodes = nodes; }

        void add(Node node) { nodes.add(node); }
    }

    static class Route {
        List<Node> nodes;
        List<Segment> segments;

        Route() {
            nodes = new ArrayList<>();
            segments = new ArrayList<>();
        }

        void add(Node node) {
            nodes.add(node);
        }
        void add(List<Node> nodes) { segments.add(new Segment(nodes)); }
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

    static int dijkstra(char[][] store, int[] start, int[] end, Map<Node, Node> hist) {
        boolean[][] visited = new boolean[ROWS][COLS];
        int[][] dist = new int[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(dist[r], Integer.MAX_VALUE);
        }

        hist.put(new Node(start[0], start[1], 0), null);

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
                        hist.put(new Node(nr, nc, 0), new Node(r, c, 0));
                    }
                }
            }
        }

        return -1;
    }

    static void printMap(Map<Node, Node> hist) {
        System.out.println("-----------------------");
        for (Node n : hist.keySet()) {
            if (hist.get(n) != null)
                System.out.println(n.row + ", " + n.col + " --> " + hist.get(n).row + ", " + hist.get(n).col);
            else
                System.out.println(n.row + ", " + n.col + " --> " + "N, N");
        }
    }

    static List<Node> processMap(Map<Node, Node> history, int startRow, int startCol) {
        List<Node> nodes = new ArrayList<>();
        Node curr = new Node(startRow, startCol, 0);
        nodes.add(curr);
        while (history.get(curr) != null) {
            curr = history.get(curr);
            nodes.add(curr);
        }

        Collections.reverse(nodes);
        return nodes;
    }

    static int findShortestPathBetweenPoints(char[][] store, int startRow, int startCol, int endRow, int endCol,
                                                Map<Node, Node> history) {
        int[] start = {startRow, startCol};
        int[] end = {endRow, endCol};
        return dijkstra(store, start, end, history);
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

        Route route = new Route();

        int entranceRow = ROWS - 1;
        int entranceCol = COLS - 2;
        int totalDistance = 0;

        int currentRow = entranceRow;
        int currentCol = entranceCol;

        while (!items.isEmpty()) {
            Item closestItem = null;
            int minDistance = Integer.MAX_VALUE;
            Map<Node, Node> history = new HashMap<>();

            for (Item item : items) {
                Map<Node, Node> tempHist = new HashMap<>();
                int distance = findShortestPathBetweenPoints(store, currentRow, currentCol, item.row, item.col, tempHist);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestItem = item;
                    history = tempHist;
                    //printMap(history);
                }
            }

            System.out.println("Distance from (" + currentRow + ", " + currentCol + ") to "
                    + closestItem.id + " (" + closestItem.row + ", " + closestItem.col + "): " + minDistance);
            totalDistance += minDistance;
            currentRow = closestItem.row;
            currentCol = closestItem.col;
            items.remove(closestItem);

            List<Node> nodes = processMap(history, closestItem.row, closestItem.col);
            int start = route.nodes.size() != 0 && route.nodes.get(route.nodes.size() - 1).equals(nodes.get(0)) ? 1 : 0;
            for (int i = start; i < nodes.size(); i++) {
                route.add(nodes.get(i));
            }
            route.add(nodes);
        }

        System.out.println("Total distance: " + totalDistance);

        // Display the UI
        SwingUtilities.invokeLater(() -> new StoreUI(store, ROWS, COLS, route));
    }
}
