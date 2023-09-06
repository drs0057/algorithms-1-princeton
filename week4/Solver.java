import java.util.LinkedList;
import java.util.Deque;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
    private BoardNode solutionNode;

    private class BoardNode implements Comparable<BoardNode> {
        public final Board board;
        public final BoardNode prevNode;
        public final int moves;
        public final int priority;

        public BoardNode(Board board, BoardNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            this.moves = prevNode == null ? 0 : prevNode.moves + 1;
            this.priority = this.board.manhattan() + this.moves;
        }

        public void addNeighbors(MinPQ<BoardNode> pq) {
            for (Board neighbor : this.board.neighbors()) {
                if (this.prevNode != null && 
                    neighbor.equals(this.prevNode.board)) continue;
                pq.insert(new BoardNode(neighbor, this));
            }
        }

        public int compareTo(BoardNode that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<BoardNode> mainPQ = new MinPQ<BoardNode>();
        MinPQ<BoardNode> twinPQ = new MinPQ<BoardNode>();
        mainPQ.insert(new BoardNode(initial, null));
        twinPQ.insert(new BoardNode(initial.twin(), null));

        while (true) {
            BoardNode mainNode = mainPQ.delMin();
            BoardNode twinNode = twinPQ.delMin();
            mainNode.addNeighbors(mainPQ);
            twinNode.addNeighbors(twinPQ);

            if (mainNode.board.isGoal()) {
                this.solutionNode = mainNode;
                break;
            }
            else if (twinNode.board.isGoal()) { 
                this.solutionNode = null;
                break;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solutionNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.solutionNode == null ? -1 : this.solutionNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.solutionNode == null) return null;

        Deque<Board> boardPath = new LinkedList<Board>();
        BoardNode currNode = this.solutionNode;
        while (currNode != null) {
            boardPath.addFirst(currNode.board);
            currNode = currNode.prevNode;
        }
        return boardPath;
    }

    // test client (see below) 
    public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }

    // int[][] tiles = {{1, 2, 3}, {4, 0, 6}, {7, 5, 8}};
    // Board startBoard = new Board(tiles);
    // Solver ans = new Solver(startBoard);
    // System.out.println("Minimum moves: " + ans.moves());
    // System.out.println("Path: ");
    // for (Board board : ans.solution()) {
    //     System.out.println(board.toString());
    // }
    }

}