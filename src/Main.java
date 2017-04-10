import static java.lang.Thread.currentThread;

/**
 * Created by Galinka on 03.04.2017.
 */
public class Main {

    public static void main(String[] args) {
        final Semaphore s = new Semaphore(4);

        new Thread(new TestRunnable(s)).start();
        new Thread(new TestRunnableMultiplePermits(s,3)).start();
        new Thread(new TestRunnable(s)).start();
        new Thread(new TestRunnable(s)).start();
        new Thread(new TestRunnable(s)).start();
        new Thread(new TestRunnableMultiplePermits(s, 2)).start();
        new Thread(new TestRunnable(s)).start();
        new Thread(new TestRunnable(s)).start();
    }
}
