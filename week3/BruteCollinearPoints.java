import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;

   public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
    if (points == null) {
        throw new IllegalArgumentException();
    }

    // Make a copy of points and check for null nodes
    Point[] copyPoints = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
        if (points[i] == null) throw new IllegalArgumentException();
        Point p = points[i];
        copyPoints[i] = p;
    }

    Arrays.sort(copyPoints);
    List<LineSegment> lines = new LinkedList<LineSegment>();
    Point previousPoint = null;
    
    // Check for duplicates
    for (int i = 0; i < copyPoints.length; i++) {
        if (previousPoint != null && copyPoints[i].compareTo(previousPoint) == 0) { 
            throw new IllegalArgumentException();
        }
        previousPoint = copyPoints[i];
    


        for (int j = i + 1; j < copyPoints.length; j++) {
            for (int k = j + 1; k < copyPoints.length; k++) {
                for (int m = k + 1; m < copyPoints.length; m++) {
                    Point p = copyPoints[i];
                    Point q = copyPoints[j];
                    Point r = copyPoints[k];
                    Point s = copyPoints[m];

                    if (Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0 && 
                        Double.compare(p.slopeTo(r),  p.slopeTo(s)) == 0) {
                        LineSegment segment = new LineSegment(p, s);
                        lines.add(segment);
                    }
                }
            }
        }   
    }
    
    segments = lines.toArray(new LineSegment[lines.size()]);
}




   public int numberOfSegments() {
    return segments.length;
   }

   public LineSegment[] segments() {
    return segments.clone();
   }

   public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}