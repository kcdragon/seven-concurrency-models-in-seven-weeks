import java.util.Random;

public class IntrinsicLockPhilosopher extends Thread {

    private static final int TIME = 1000; // decreasing TIME will increase the likelihood of deadlock

    private String name;
    private Chopstick left, right;
    private Random random;

    public IntrinsicLockPhilosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
        random = new Random();
    }

    public void run() {
        System.out.println(name + " is starting.");

        try {
            while(true) {
                Thread.sleep(random.nextInt(TIME));

                synchronized(left) {

                    while(!left.available) {
                        left.wait();
                    }
                    left.available = false;
                    System.out.println(name + " is picking up left chopstick.");

                    synchronized(right) {

                        while(!right.available) {
                            right.wait();
                        }
                        right.available = false;
                        System.out.println(name + " is picking up right chopstick.");

                        Thread.sleep(random.nextInt(TIME));

                        right.available = true;
                        right.notify();
                    }

                    left.available = true;
                    left.notify();
                }
            }
        }
        catch(InterruptedException e) {}
    }
}
