import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int length;
    private WeightedQuickUnionUF data;
    private boolean[][] opened;
    private int top;
    private int bottom;
    private int numOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        length = n;
        opened = new boolean[n][n];
        data = new WeightedQuickUnionUF(n * n + 2);
        top = n * n;
        bottom = n * n + 1;
        numOpen = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (checkIndex(row, col)) {
            if (!isOpen(row, col)) {
                opened[row - 1][col - 1] = true;
                numOpen++;
            }
            if (row == 1) {
                data.union((row - 1) * length + col - 1, top);
            }
            if (row == length) {
                data.union((row - 1) * length + col - 1, bottom);
            }
            // Check up
            if (row > 1 && isOpen(row - 1, col)) {
                data.union((row - 1) * length + col - 1, (row - 2) * length + col - 1);
            }
            // Check down
            if (row < length && isOpen(row + 1, col)) {
                data.union((row - 1) * length + col - 1, row * length + col-1);
            }
            // Check left
            if (col > 1 && isOpen(row, col - 1)) {
                data.union((row - 1) * length + col - 1, (row - 1) * length + col - 2);
            }
            // Check right
            if (col < length && isOpen(row, col + 1)) {
                data.union((row - 1) * length + col - 1, (row - 1) * length + col);
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (checkIndex(row, col)) {
            return opened[row - 1][col - 1];
        }
        throw new IllegalArgumentException();
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (checkIndex(row, col)) {
            return data.find((row - 1) * length + col - 1) == data.find(top);
        }
        throw new IllegalArgumentException();
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return data.find(top) == data.find(bottom);
    }

    private boolean checkIndex(int row, int col) {
        if (row < 1 || col < 1 || row > length || col > length) return false;
        return true;
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
        perc.open(1, 1);
        System.out.println(perc.isFull(1, 1));
        System.out.println(perc.percolates());
    }
}

