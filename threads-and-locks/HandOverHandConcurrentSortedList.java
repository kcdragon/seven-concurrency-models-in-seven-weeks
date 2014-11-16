// Write a version of ConcurrentSortedList that uses a single lock instead of hand-over-hand locking. Benchmark it against the other version. Does hand-over-hand locking provide any performance advantage? When might it be a good choice? When might it not?

import java.util.concurrent.locks.ReentrantLock;

public class HandOverHandConcurrentSortedList implements ConcurrentSortedList {

    private class Node {
        int value;
        Node previous;
        Node next;
        ReentrantLock lock = new ReentrantLock();

        Node() {}

        Node(int value, Node previous, Node next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
    }

    private final Node head;
    private final Node tail;

    public HandOverHandConcurrentSortedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.previous = head;
    }

    public void insert(int value) {
        Node current = head;
        current.lock.lock();
        Node next = current.next;

        try {
            while (true) {
                next.lock.lock();
                try {
                    if (next == tail || next.value < value) {
                        Node node = new Node(value, current, next);
                        next.previous = node;
                        current.next = node;
                        return;
                    }
                }
                finally {
                    current.lock.unlock();
                }
                current = next;
                next = current.next;
            }
        }
        finally {
            next.lock.unlock();
        }
    }

    public int size() {
        Node current = tail;
        int count = 0;

        while (current.previous != head) {
            ReentrantLock lock = current.lock;
            lock.lock();
            try {
                ++count;
                current = current.previous;
            }
            finally {
                lock.unlock();
            }
        }

        return count;
    }
}
