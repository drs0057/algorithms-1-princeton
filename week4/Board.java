import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int hamming;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }

        // Hamming
        int count = 0;
        int correctNum = 1;
        for (int[] row : this.tiles) {
            for (int num : row) {
                if (num != 0 && correctNum != num) count++;
                correctNum++;
            }
        }
        this.hamming = count;

        // Manhattan
        int currentNum;
        int distances = 0;
        int properRow;
        int properCol;
        for (int r = 0; r < dimension(); r++) {
            for (int c = 0; c < dimension(); c++) {
                currentNum = this.tiles[r][c];
                if (currentNum == 0) continue;
                
                // Add row distances
                properRow = (currentNum - 1) / this.dimension();
                distances += Math.abs(r - properRow);
                // Add column distances
                properCol = (currentNum - (this.dimension() * properRow)) - 1;
                distances += Math.abs(c - properCol);
            }
        }
        this.manhattan = distances;
    }
                                           
    // string representation of this board
    public String toString() {
        String myString = "";
        myString += dimension();

        for (int i = 0; i < dimension(); i++) {
            myString += "\n";
            for (int j = 0; j < dimension(); j++) {
                myString += this.tiles[i][j] + " ";
            }
        }
        return myString;
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return this.hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (this.hamming == 0 && this.manhattan == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) return false;
        Board otherBoard = (Board) y;

        if (this.dimension() != otherBoard.dimension()) return false;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] != otherBoard.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // Find the location of the zero
        int[] zeroLocation = new int[2];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.tiles[i][j] == 0) {
                    zeroLocation[0] = i;
                    zeroLocation[1] = j;
                }
            }
        } 
        // Create the neighbors
        ArrayList<Board> neighborList = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newRow = zeroLocation[0] + dir[0];
            int newCol = zeroLocation[1] + dir[1];

            if (newRow < 0 || newRow >= this.dimension() ||
                newCol < 0 || newCol >= this.dimension()) continue;

            // Create a deep copy of tiles to make a new board
            int[][] tiles = new int[this.dimension()][this.dimension()];
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    tiles[i][j] = this.tiles[i][j];
                }
            }

            // Move the square
            int temp = tiles[newRow][newCol];
            tiles[newRow][newCol] = 0;
            tiles[zeroLocation[0]][zeroLocation[1]] = temp;

            neighborList.add(new Board(tiles));
        }
        return neighborList;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                newTiles[i][j] = this.tiles[i][j];
            }
        }

        // Swap the first two valid tiles
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension() - 1; j++) {
                if (newTiles[i][j] != 0 && newTiles[i][j + 1] != 0) {

                    int temp = newTiles[i][j];
                    newTiles[i][j] = newTiles[i][j + 1];
                    newTiles[i][j + 1] = temp;
                    return new Board(newTiles);
                }
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        Board board = new Board(tiles);
        System.out.println("Dimension: " + board.dimension());
        System.out.println(board.toString());
        System.out.println("Hamming: " + board.hamming);
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println("Goal Board?: " + board.isGoal());

        Iterable<Board> neighborsList = board.neighbors();
        for (Board neighbor : neighborsList) {
            System.out.println(neighbor.toString());
        }
        System.out.println("Twin: " + board.twin());
    }

}