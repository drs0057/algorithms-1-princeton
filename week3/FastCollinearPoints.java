import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] segmentsArray;

   public FastCollinearPoints(Point[] points) {
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

    
    // Calculate segments
    Arrays.sort(copyPoints);
    Point previousPoint = null;
    List<LineSegment> segmentsLL = new LinkedList<LineSegment>();
    for (int i = 0; i < copyPoints.length; i++) {

        // Check for duplicates
        if (previousPoint != null && copyPoints[i].compareTo(previousPoint) == 0) { 
            throw new IllegalArgumentException();
        }
        previousPoint = copyPoints[i];
    
        // Sort points by their slopes with p
        Point[] pointsBySlope = copyPoints.clone();
        Point p = pointsBySlope[i];
        Arrays.sort(pointsBySlope, p.slopeOrder());

        // Try to find 3 equal slopes 
        double lastSlope = Double.NEGATIVE_INFINITY;
        int startingIndex = 1;
        for (int k = 1; k < pointsBySlope.length; k++) {

            if (p.slopeTo(pointsBySlope[k]) != lastSlope) {
                if (k - startingIndex >= 3) {
                    LineSegment line = new LineSegment(p, pointsBySlope[k - 1]);
                    segmentsLL.add(line);
                }
                startingIndex = k;
            }
            lastSlope = p.slopeTo(pointsBySlope[k]);

            // Same logic but double checking the last point
            if (k == pointsBySlope.length - 1) {
                if (k - startingIndex >= 2) {
                    LineSegment line = new LineSegment(p, pointsBySlope[k]);
                    segmentsLL.add(line);
                }
            }
        }
    }

    segmentsArray = segmentsLL.toArray(new LineSegment[segmentsLL.size()]);
   }


   public int numberOfSegments() {
    return segmentsArray.length;
   }


   public LineSegment[] segments() {
    return segmentsArray.clone();
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}