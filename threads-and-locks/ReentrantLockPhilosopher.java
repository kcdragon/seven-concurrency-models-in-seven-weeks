import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockPhilosopher extends Thread {

    private static final int TIME = 1000;

    private String name;
    private boolean eating;
    private ReentrantLockPhilosopher left;
    private ReentrantLockPhilosopher right;
    private ReentrantLock table;
    private Condition condition;
    private Random random;

    public ReentrantLockPhilosopher(String name, ReentrantLock table) {
        this.name = name;
        eating = false;
        this.table = table;
        condition = table.newCondition();
        random = new Random();
    }

    public void setLeft(ReentrantLockPhilosopher left) {
        this.left = left;
    }

    public void setRight(ReentrantLockPhilosopher right) {
        this.right = right;
    }

    public void run() {
        try {
            while(true) {
                think();
                eat();
            }
        }
        catch (InterruptedException e) {
        }
    }

    private void think() throws InterruptedException {
        table.lock();
        try {
            System.out.println(name + " is thinking.");
            eating = false;
            left.condition.signal();
            right.condition.signal();
        }
        finally {
            table.unlock();
        }
        Thread.sleep(TIME);
    }

    private void eat() throws InterruptedException {
        table.lock();
        try {
            while (left.eating || right.eating) {
                condition.await();
            }
            System.out.println(name + " is eating.");
            eating = true;
        }
        finally {
            table.unlock();
        }
        Thread.sleep(TIME);
    }
}
