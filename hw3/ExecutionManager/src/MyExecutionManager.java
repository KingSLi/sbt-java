

public class MyExecutionManager implements ExecutionManager {
    private MyContext context;

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        context = new MyContext(tasks);
        new Thread(() -> {
            while (!context.isFinished()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            callback.run();
        }).start();
        return context;
    }
}
