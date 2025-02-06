package rpg.ai;

import java.util.*;

public class Pathfinder {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    public static class Grid {
        private final boolean[][] walkable;
        private final int width;
        private final int height;

        public Grid(int width, int height) {
            this.width = width;
            this.height = height;
            this.walkable = new boolean[width][height];
            initializeAllWalkable();
        }

        private void initializeAllWalkable() {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    walkable[x][y] = true;
                }
            }
        }

        public boolean isWalkable(int x, int y) {
            return x >= 0 && x < width && y >= 0 && y < height && walkable[x][y];
        }

        public void setWalkable(int x, int y, boolean walkable) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                this.walkable[x][y] = walkable;
            }
        }
    }

    private static class Node implements Comparable<Node> {
        final int x;
        final int y;
        Node parent;
        double gCost; // Cost from start to this node
        double hCost; // Heuristic cost to target
        double fCost; // Total cost (g + h)

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fCost, other.fCost);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public List<int[]> findPath(int startX, int startY, int targetX, int targetY, Grid grid) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY);
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startX, startY, targetX, targetY);
        startNode.fCost = startNode.gCost + startNode.hCost;
        openSet.add(startNode);
        allNodes.put(key(startX, startY), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            for (int[] direction : DIRECTIONS) {
                int neighborX = current.x + direction[0];
                int neighborY = current.y + direction[1];

                if (!grid.isWalkable(neighborX, neighborY)) continue;

                String neighborKey = key(neighborX, neighborY);
                Node neighbor = allNodes.getOrDefault(neighborKey, new Node(neighborX, neighborY));
                
                double tentativeGCost = current.gCost + 1; // Assuming uniform movement cost

                if (tentativeGCost < neighbor.gCost) {
                    neighbor.parent = current;
                    neighbor.gCost = tentativeGCost;
                    neighbor.hCost = calculateHeuristic(neighborX, neighborY, targetX, targetY);
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;

                    if (!allNodes.containsKey(neighborKey)) {
                        openSet.add(neighbor);
                        allNodes.put(neighborKey, neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<int[]> reconstructPath(Node endNode) {
        List<int[]> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(new int[]{current.x, current.y});
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }

    private double calculateHeuristic(int x1, int y1, int x2, int y2) {
        // Manhattan distance for grid-based movement
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private String key(int x, int y) {
        return x + "," + y;
    }

    // Example usage
    public static void main(String[] args) {
        // Create a 10x10 grid
        Grid grid = new Grid(10, 10);
        
        // Add some obstacles
        grid.setWalkable(3, 3, false);
        grid.setWalkable(3, 4, false);
        grid.setWalkable(3, 5, false);

        Pathfinder pathfinder = new Pathfinder();
        List<int[]> path = pathfinder.findPath(1, 1, 8, 8, grid);

        System.out.println("Path found:");
        for (int[] coord : path) {
            System.out.println("(" + coord[0] + ", " + coord[1] + ")");
        }
    }
}
