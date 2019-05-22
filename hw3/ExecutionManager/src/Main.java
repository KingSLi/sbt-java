public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutionManager executionManager = new MyExecutionManager();
        Runnable callback = new Task(1, false);
        Runnable[] tasks = {
                new Task(20000, false),
                new Task(20100, true),
                new Task(20200, false),
                new Task(20300, true),
                new Task(11000, false),
                new Task(12000, false),
                new Task(13000, false),
                new Task(14000, false)
        };
        Context execute = executionManager.execute(callback, tasks);
        Thread.sleep(1000);
        execute.interrupt();
        Thread.sleep(30000);
        System.out.println(execute.isFinished());
        System.out.println(execute.getFailedTaskCount());
        System.out.println(execute.getCompletedTaskCount());
        System.out.println(execute.getInterruptedTaskCount());
    }


}

class Task implements Runnable {
    private int number;
    private Boolean needException;

    public Task(int number, Boolean needException) {
        this.needException = needException;
        this.number = number;
    }

    @Override
    public void run() {
        if (needException) {
            throw new RuntimeException();
        }
        System.out.println("Start --- " + number);
        try {
            Thread.sleep(number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish --- " + number);
    }
}