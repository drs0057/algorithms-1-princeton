import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private int size = 0;
    private Node root = null;
    private Point2D nearestPoint = null;

    private class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private boolean isVertical;
        private RectHV rect;

        public Node(Point2D point, Node parent) {
            this.point = point;
            if (parent == null) this.isVertical = true;
            else this.isVertical = !parent.isVertical;

            // rect describing the access of the node, limited by ancestors
            if (parent == null) {
                this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            }
            else {
                double xmin = parent.rect.xmin();
                double ymin = parent.rect.ymin();
                double xmax = parent.rect.xmax();
                double ymax = parent.rect.ymax();

                // vertical division of the rect
                if (parent.isVertical) {
                    // point will go to the left of the parent
                    if (parent.compareTo(point) > 0) xmax = parent.point.x();
                    else xmin = parent.point.x();
                }
                else {
                    if (parent.compareTo(point) > 0) ymax = parent.point.y();
                    else ymin = parent.point.y();
                }

                this.rect = new RectHV(xmin, ymin, xmax, ymax);
            }
        }

        public int compareTo(Point2D that) {
            if (this.isVertical) {
                return Double.compare(this.point.x(), that.x());
            }
            else return Double.compare(this.point.y(), that.y());
        }

        public void draw() {
            StdDraw.setPenRadius(0.005);

            if (this.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this.point.x(), this.rect.ymin(), 
                             this.point.x(), this.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this.rect.xmin(), this.point.y(), 
                             this.rect.xmax(), this.point.y());                
            }

            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            this.point.draw();

            if (this.left != null) this.left.draw();
            if (this.right != null) this.right.draw();
        }
    }

    // construct an empty set of points 
    public KdTree() {
    }

    // is the set empty?                               
    public boolean isEmpty() {
        return this.size == 0;
    }   

    // number of points in the set                    
    public int size() {
        return this.size;
    }       

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.contains(p)) return;
        put(p, this.root);
        this.size++;
    }

    // helper function for insert
    private void put(Point2D p, Node node) {
        if (node == null) {
            this.root = new Node(p, null);
            return;
        }

        if (node.compareTo(p) > 0) {
            if (node.left == null) {
                node.left = new Node(p, node);
                return;
            }
            else put(p, node.left);
        }
        else {
            if (node.right == null) {
                node.right = new Node(p, node);
                return;
            }
            else put(p, node.right);
        }
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(p, this.root);
    }

    // helper function for contains
    private boolean get(Point2D p, Node node) {
        if (node == null) return false;
        if (node.point.distanceSquaredTo(p) == 0) return true;
    
        if (node.compareTo(p) > 0) return get(p, node.left);
        else return get(p, node.right);
    }

    // draw all points to standard draw 
    public void draw() {
        if (this.root == null) return;
        this.root.draw();
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return pointsInRect(this.root, rect, new LinkedList<Point2D>());
    }         

    // helper function for range
    private List<Point2D> pointsInRect(Node node, RectHV rect, List<Point2D> points) {
        if (node == null) return points;
        if (!node.rect.intersects(rect)) return points;

        if (rect.contains(node.point)) {
            points.add(node.point);
        }
        pointsInRect(node.left, rect, points);
        pointsInRect(node.right, rect, points);
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.isEmpty()) return null;
        
        this.nearestPoint = null;
        findNearest(p, this.root);
        return this.nearestPoint;
    }

    // helper function for nearest
    private void findNearest(Point2D p, Node node) {
        if (node == null) return;
        if (this.nearestPoint == null) this.nearestPoint = node.point;
        if (this.nearestPoint.distanceSquaredTo(p) < node.rect.distanceSquaredTo(p)) return;

        if (node.point.distanceSquaredTo(p) < this.nearestPoint.distanceSquaredTo(p)) {
            this.nearestPoint = node.point;
        }
        if (node.compareTo(p) > 0) {
            findNearest(p, node.left);
            findNearest(p, node.right);
        }
        else {
            findNearest(p, node.right);
            findNearest(p, node.left);
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.1, 0.1));
        tree.insert(new Point2D(0.2, 0.1));
        tree.insert(new Point2D(0.2, 0.7));
        tree.insert(new Point2D(0.8, 0.3));
        System.out.println(tree.size());
        System.out.println(tree.isEmpty());
        System.out.println(tree.root.point);
        System.out.println(tree.nearest(new Point2D(0.1, 0.11)));
        tree.draw();
    }
}