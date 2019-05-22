import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool implements ThreadPool {
    int minThreads, maxThreads;
    LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();
    Set<MyThread> threads = new HashSet<>();
    AtomicInteger countBusyThreads = new AtomicInteger(0);

    public ScalableThreadPool(int min, int max) {
        minThreads = min;
        maxThreads = max;
        for(int i = 0; i < min; ++i) {
            threads.add(new MyThread());
        }
    }

    @Override
    public void start() {
        threads.forEach(MyThread::start);
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (runnables) {
            if (!runnables.isEmpty() && threads.size() == countBusyThreads.get()) {
                incrementThreadCount();
            }
            runnables.add(runnable);
            runnables.notify();
        }
    }

    private void incrementThreadCount() {
        if (threads.size() < maxThreads) {
            MyThread thread = new MyThread();
            threads.add(thread);
            thread.start();
        }
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable task = null;
                synchronized (runnables) {
                    try {
                        while (runnables.isEmpty()) {
                            runnables.wait();
                        }
                        task = runnables.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Thread interrupt");
                    }
                }
                try {
                    if (task != null) {
                        countBusyThreads.incrementAndGet();
                        task.run();
                        countBusyThreads.decrementAndGet();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    System.out.println("Task exception");
                }
                synchronized (runnables) {
                    if (runnables.isEmpty()) {
                        if (threads.size() > minThreads) {
                            threads.remove(this);
                            return;
                        }
                    }
                }
            }
        }
    }
}
