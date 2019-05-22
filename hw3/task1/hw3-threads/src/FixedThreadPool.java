import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPool implements ThreadPool {
    private final LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();
    private List<MyThread> threads = new ArrayList<>();

    FixedThreadPool(int countThreads) {
        for (int i = 0; i < countThreads; ++i) {
            threads.add(new MyThread(runnables));
        }
    }

    @Override
    public void start() {
        threads.forEach(Thread::start);
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
            runnables.notify();
        }
    }
}
