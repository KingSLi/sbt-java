import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MyContext implements Context {
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private AtomicInteger completedTasks = new AtomicInteger(0);
    private AtomicInteger failedTasks = new AtomicInteger(0);
    private AtomicInteger interruptedTasks = new AtomicInteger(0);
    volatile Boolean isInterrupted = false;
    private int countTasks;


    public MyContext(Runnable... tasks) {
        countTasks = tasks.length;
        for (Runnable task : tasks) {
            service.submit(() -> {
                if (isInterrupted) {
                    interruptedTasks.incrementAndGet();
                    return;
                }
                try {
                    task.run();
                    completedTasks.incrementAndGet();
                } catch (Exception e) {
                    failedTasks.incrementAndGet();
                }
            });
        }
    }

    @Override
    public int getCompletedTaskCount() {
        return completedTasks.get();
    }

    @Override
    public int getFailedTaskCount() {
        return failedTasks.get();
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedTasks.get();
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
        System.out.println("interrupt");
        service.shutdown();
    }

    @Override
    public boolean isFinished() {
        return getCompletedTaskCount() + getFailedTaskCount() + getInterruptedTaskCount() == countTasks;
    }
}
