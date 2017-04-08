import java.util.Random;

import static java.lang.Thread.currentThread;

/**
 * Created by Galinka on 03.04.2017.
 */
public class TestRunnable implements Runnable{
    private final Semaphore semaphore;

    public TestRunnable(Semaphore semaphore) {

        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        semaphore.acquire();
        System.out.println(Thread.currentThread().getName()+" start work");
        int time = (new Random().nextInt(1000))+1000;
        try {
            currentThread().sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" end work");
        semaphore.release();
    }
}
