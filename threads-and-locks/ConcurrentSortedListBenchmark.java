import java.util.*;

public class ConcurrentSortedListBenchmark {

    public static void main(String... args) {
        verify(new HandOverHandConcurrentSortedList());
        verify(new SingleLockConcurrentSortedList());

        System.out.println();

        long time = benchmark(new HandOverHandConcurrentSortedList());
        System.out.println("Hand Over Hand");
        System.out.println("Total execution time: " + time + "ms");
        System.out.println();

        time = benchmark(new SingleLockConcurrentSortedList());
        System.out.println("Single Lock");
        System.out.println("Total execution time: " + time + "ms");
        System.out.println();

        time = benchmark(new HandOverHandConcurrentSortedList());
        System.out.println("Hand Over Hand");
        System.out.println("Total execution time: " + time + "ms");
        System.out.println();

        time = benchmark(new SingleLockConcurrentSortedList());
        System.out.println("Single Lock");
        System.out.println("Total execution time: " + time + "ms");
        System.out.println();
    }

    static class BenchmarkThread extends Thread {
        private final ConcurrentSortedList list;

        public BenchmarkThread(ConcurrentSortedList list) {
            this.list = list;
        }

        public void run() {
            for (int i = 0; i < 1000; i++) {
                list.insert(1);
                list.size();
            }
        }
    }

    private static long benchmark(ConcurrentSortedList list) {
        long startTime = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new BenchmarkThread(list);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            }
            catch (InterruptedException e) {
            }
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static void verify(ConcurrentSortedList list) {
        System.out.println("list.size() == 0");
        System.out.println(list.size() == 0);
        System.out.println();

        System.out.println("Insert element into list");
        list.insert(1);
        System.out.println("list.size() == 1");
        System.out.println(list.size() == 1);
        System.out.println();

        System.out.println("Insert element into list");
        list.insert(1);
        System.out.println("list.size() == 2");
        System.out.println(list.size() == 2);
        System.out.println();

        System.out.println("Insert element into list");
        list.insert(1);
        System.out.println("list.size() == 3");
        System.out.println(list.size() == 3);
        System.out.println();
    }
}
