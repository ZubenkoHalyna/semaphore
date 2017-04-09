import java.util.LinkedList;
import java.util.Objects;

import static java.lang.Thread.currentThread;

/**
 * Created by Galinka on 03.04.2017.
 */
public class Semaphore {
    private final int maxNumberOfPermits;
    private volatile int numberOfPermits;
    private volatile LinkedList<Thread> threadQueue = new LinkedList<>();

    public Semaphore(int maxNumberOfPermits) {
        numberOfPermits = maxNumberOfPermits;
        this.maxNumberOfPermits = maxNumberOfPermits;
    }

    private synchronized boolean getPermits(int number){
        System.out.println(Thread.currentThread().getName()+" getPermits start");

        if (number > maxNumberOfPermits){
            throw new RuntimeException("Number of requested permits more then max available for the Semaphore");
        }

        if (numberOfPermits >= number) {
            numberOfPermits-=number;
            System.out.println(Thread.currentThread().getName()+" getPermits end");
            return true;
        }
        System.out.println(Thread.currentThread().getName()+" getPermits end");
        return false;
    }

    private void waitThread(Thread thread, int permits) {
        System.out.println(Thread.currentThread().getName()+" waitThread start");
        synchronized (thread) {
            try {
                thread.wait();
                acquire(permits);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName()+" waitThread end");
    }

    private synchronized void notifyThread(Thread thread){
        System.out.println(Thread.currentThread().getName()+" notifyThread start");
        synchronized (thread) {
            threadQueue.remove(thread);
            thread.notify();
        }
        System.out.println(Thread.currentThread().getName()+" notifyThread end");
    }

    public void acquire() {
        System.out.println(Thread.currentThread().getName()+" acquire start");
        if (! getPermits(1)) {
            AddThread(Thread.currentThread());
            waitThread(Thread.currentThread(),1);
        }
        System.out.println(Thread.currentThread().getName()+" acquire end");
    }

    private synchronized void AddThread(Thread t){
        threadQueue.add(t);
    }

    public void acquire(int permits) {
        System.out.println(Thread.currentThread().getName()+" acquire("+permits+") start");
        if (! getPermits(permits)){
            AddThread(Thread.currentThread());
            waitThread(Thread.currentThread(), permits);
        }
        System.out.println(Thread.currentThread().getName()+" acquire("+permits+") end");
    }

    public synchronized void release() {
        System.out.println(Thread.currentThread().getName()+" release start");
        numberOfPermits++;
        checkMaxNumberOfPermits();

        if (threadQueue.size()>0){
            notifyThread(threadQueue.get(0));
        }
        System.out.println(Thread.currentThread().getName()+" release end");
    }

    public synchronized void release(int permits) {
        System.out.println(Thread.currentThread().getName()+" release ("+permits+") start");
        numberOfPermits += permits;
        checkMaxNumberOfPermits();

        int numberOfThreads = Math.min(threadQueue.size(), permits);

        if (threadQueue.size()>0){
            for(int i=0; i<numberOfThreads; i++) {
                notifyThread(threadQueue.get(0));
            }
        }
        System.out.println(Thread.currentThread().getName()+" release ("+permits+") end");
    }

    private synchronized void checkMaxNumberOfPermits() {
        if (numberOfPermits>maxNumberOfPermits){
            throw new RuntimeException("Number of permits become more then max available for the Semaphore");
        }
    }

    public int getAvailablePermits() {
        return numberOfPermits;
    }
}

