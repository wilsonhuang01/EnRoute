import javax.swing.*;
import java.util.*;

class Store {
    static final int ROWS = 11;
    static final int COLS = 11;
    static final char EMPTY = '.';
    static final char STAND = '#';
    static final char ENTRANCE = 'E';
    static Map<Integer, Item> allItems;

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

        Segment(List<Node> nodes) {
            this.nodes = nodes;
        }

        void add(Node node) {
            nodes.add(node);
        }
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

        void add(List<Node> nodes) {
            segments.add(new Segment(nodes));
        }
    }

    public Map<Integer, Item> getAllItems() {
        return allItems;
    }

    static char[][] createStore(List<Item> items) {
        char[][] store = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(store[r], EMPTY);
        }

        // stand coordinates
        int[][] standCoordinates = {
                {0, 2},
                {0, 3},
                {0, 4},
                {0, 5},
                {0, 6},
                {0, 7},
                {3, 2},
                {3, 3},
                {3, 4},
                {3, 5},
                {3, 6},
                {3, 7},
                {6, 2},
                {6, 3},
                {6, 4},
                {6, 5},
                {6, 6},
                {6, 7},
                {9, 2},
                {9, 3},
                {9, 4},
                {9, 5},
                {9, 6},
                {9, 7}
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

    public static void initializeItems() {
        allItems = new HashMap<>();
        allItems.put(1, new Item("Apples", 8, 2));
        allItems.put(2, new Item("Bananas", 8, 3));
        allItems.put(3, new Item("Pears", 8, 5));
        allItems.put(4, new Item("Limes", 8, 6));
        allItems.put(5, new Item("Kiwi", 7, 4));
        allItems.put(6, new Item("Lay's", 5, 2));
        allItems.put(7, new Item("Doritos", 5, 3));
        allItems.put(8, new Item("M&Ms", 5, 5));
        allItems.put(9, new Item("Oreos", 5, 6));
        allItems.put(10, new Item("Cheetos", 4, 4));
        allItems.put(11, new Item("Towels", 2, 2));
        allItems.put(12, new Item("Soap", 2, 3));
        allItems.put(13, new Item("Napkins", 2, 5));
        allItems.put(14, new Item("Cutlery", 2, 6));
        allItems.put(15, new Item("Pepsi", 1, 4));
    }

    public Route computeRoute(char[][] store, List<Item> shoppingCart) {
        Route route = new Route();

        int entranceRow = ROWS - 1;
        int entranceCol = COLS - 2;
        int totalDistance = 0;

        int currentRow = entranceRow;
        int currentCol = entranceCol;

        while (!shoppingCart.isEmpty()) {
            Item closestItem = null;
            int minDistance = Integer.MAX_VALUE;
            Map<Node, Node> history = new HashMap<>();

            for (Item item : shoppingCart) {
                Map<Node, Node> tempHist = new HashMap<>();
                int distance = findShortestPathBetweenPoints(store, currentRow, currentCol, item.row, item.col, tempHist);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestItem = item;
                    history = tempHist;
                }
            }

            System.out.println("Distance from (" + currentRow + ", " + currentCol + ") to "
                    + closestItem.id + " (" + closestItem.row + ", " + closestItem.col + "): " + minDistance);
            totalDistance += minDistance;
            currentRow = closestItem.row;
            currentCol = closestItem.col;
            shoppingCart.remove(closestItem);

            List<Node> nodes = processMap(history, closestItem.row, closestItem.col);
            int start = route.nodes.size() != 0 && route.nodes.get(route.nodes.size() - 1).equals(nodes.get(0)) ? 1 : 0;
            for (int i = start; i < nodes.size(); i++) {
                route.add(nodes.get(i));
            }
            route.add(nodes);
        }

        System.out.println("Total distance: " + totalDistance);

        return route;
    }


    static int findShortestPathBetweenPoints(char[][] store, int startRow, int startCol, int endRow, int endCol,
                                             Map<Node, Node> history) {
        int[] start = {startRow, startCol};
        int[] end = {endRow, endCol};
        return dijkstra(store, start, end, history);
    }

    public static void main(String[] args) {
        // Initialize the store with all items
        Store store = new Store();
        initializeItems();

        // Launch the landing page
        SwingUtilities.invokeLater(() -> new LandingPage(store));
    }

}
