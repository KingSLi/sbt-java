import java.util.concurrent.LinkedBlockingQueue;

class MyThread extends Thread {
    final private LinkedBlockingQueue<Runnable> runnables;

    MyThread(LinkedBlockingQueue<Runnable> runnables) {
        this.runnables = runnables;
    }

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
                if (task != null)
                    task.run();
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("Task exception");
            }
        }
    }
}