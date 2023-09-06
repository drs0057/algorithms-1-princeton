import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main (String[] args){
        int count = Integer.parseInt(args[0]);
        RandomizedQueue<String> RQ = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String line = StdIn.readString();
            RQ.enqueue(line);
        }

        while (count-- > 0) {
            System.out.println(RQ.dequeue());
        }

        // Iterator<String> iterator = RQ.iterator();
    }
}

