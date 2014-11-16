import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher {

    private static final Map<String, Thread[]> PHILOSOPHER_TYPES = new HashMap<String, Thread[]>();
    static {
        PHILOSOPHER_TYPES.put("Intrinsic", buildIntrinsicLockPhilosophers());
        PHILOSOPHER_TYPES.put("Reentrant", buildReentrantLockPhilosophers());
    }

    public static void main(String... args) {
        if (args.length == 0) {
            System.err.println("Please specify a type of Philosopher problem to run.");
            return;
        }

        String type = args[0];

        if (!PHILOSOPHER_TYPES.containsKey(type)) {
            System.err.println(type + " is not a valid Philosopher type.");
            return;
        }

        Thread[] philosophers = PHILOSOPHER_TYPES.get(type);
        for (Thread philosopher : philosophers) {
            philosopher.start();
        }
    }

    private static Thread[] buildIntrinsicLockPhilosophers() {
        Chopstick[] chopsticks = new Chopstick[] {
            new Chopstick(),
            new Chopstick(),
            new Chopstick()
        };

        return new Thread[] {
            new IntrinsicLockPhilosopher("one", chopsticks[0], chopsticks[1]),
            new IntrinsicLockPhilosopher("two", chopsticks[1], chopsticks[2]),
            new IntrinsicLockPhilosopher("three", chopsticks[2], chopsticks[0])
        };
    }

    private static Thread[] buildReentrantLockPhilosophers() {
        ReentrantLock table = new ReentrantLock();

        ReentrantLockPhilosopher one = new ReentrantLockPhilosopher("one", table);
        ReentrantLockPhilosopher two = new ReentrantLockPhilosopher("two", table);
        ReentrantLockPhilosopher three = new ReentrantLockPhilosopher("three", table);

        one.setLeft(three);
        one.setRight(two);

        two.setLeft(one);
        two.setRight(three);

        three.setLeft(two);
        three.setRight(one);

        return new Thread[] {
            one, two, three
        };
    }
}
