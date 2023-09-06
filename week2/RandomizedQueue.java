import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private int n;
    private Node head, tail;

    private class Node {
        Item item;
        Node next;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        head = null;
        tail = null;
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (n == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node temp = tail;
        tail = new Node();
        tail.item = item;
        tail.next = null;
        if (temp != null) temp.next = tail;
        if (head == null) head = tail;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int location = StdRandom.uniformInt(n);
        Node current = head;
        Node prev = null;
        for (int i = 0; i < location; i++) {
            prev = current;
            current = current.next;
        }
        if (prev != null) prev.next = current.next;
        if (current == head) head = current.next;
        if (current == tail) tail = prev;
        n--;
        return current.item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int location = StdRandom.uniformInt(n);
        Node current = head;
        for (int i = 0; i < location; i++) {
            current = current.next;
        }
        Item item = current.item;
        return item;        
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Object[] array;
        private int current = 0;

        public ListIterator() {
            array = new Object[n];
            Node node = head;
            for (int i = 0; i < n; i++) {
                array[i] = node;
                node = node.next;
            }
            StdRandom.shuffle(array);
        }

        public boolean hasNext() {
            return !(current == array.length);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return ((Node) array[current++]).item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> RQ = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            RQ.enqueue(i);
        }
        for (int i : RQ) System.out.println(i);
        System.out.println("Size: " +  RQ.size());
        System.out.println("Random item removed: " + RQ.dequeue());
        System.out.println("Size: " + RQ.size());
        System.out.println("Random item: " + RQ.sample());
        System.out.println("Is empty: " + RQ.isEmpty());
    }
}

