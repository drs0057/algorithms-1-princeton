import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] x;
    private int runs;
    private final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        runs = trials;
        x = new double[runs];
        int numOpened = 0;

        for (int i = 0; i < runs; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }

            x[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(x);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(x);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(runs);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(runs);
    }

   // test client (see below)
   public static void main(String[] args) {

    PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    StdOut.println("Mean: " + stats.mean());
    StdOut.println("Standard Deviation: " + stats.stddev());
    StdOut.println("95% Confidence Interval: " + stats.confidenceLo() + ", " + stats.confidenceHi());
 
   }

}