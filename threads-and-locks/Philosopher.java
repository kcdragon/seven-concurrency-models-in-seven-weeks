import java.util.Random;

public class Philosopher extends Thread {

    private static final int TIME = 1000; // decreasing TIME will increase the likelihood of deadlock

    private String name;
    private Chopstick left, right;
    private Random random;

    public Philosopher(String name, Chopstick left, Chopstick right) {
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
                    System.out.println(name + " is picking up left chopstick.");

                    synchronized(right) {
                        System.out.println(name + " is picking up right chopstick.");

                        Thread.sleep(random.nextInt(TIME));
                    }
                }
            }
        }
        catch(InterruptedException e) {}
    }

    public static void main(String... args) {
        System.out.println("Hello, world.");

        Chopstick[] chopsticks = new Chopstick[] {
            new Chopstick(),
            new Chopstick(),
            new Chopstick()
        };

        Philosopher one = new Philosopher("one", chopsticks[0], chopsticks[1]);
        Philosopher two = new Philosopher("two", chopsticks[1], chopsticks[2]);
        Philosopher three = new Philosopher("three", chopsticks[2], chopsticks[0]);

        one.start();
        two.start();
        three.start();
    }
}

class Chopstick {
}
