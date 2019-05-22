import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ScalableThreadPool fixedThreadPool = new ScalableThreadPool(3, 10);
        fixedThreadPool.start();
        for(int taskNumber = 1; taskNumber <= 27; taskNumber++) {
            int finalTaskNumber = taskNumber;
            fixedThreadPool.execute(
                    () -> {
                        System.out.println(Thread.currentThread().getName() + " " + finalTaskNumber);
                        try {
                            int times = new Random().nextInt(50) * 100;
                            System.out.println(times + '\t');
                            Thread.sleep(times);
                        } catch (InterruptedException ignored) {}
                    }
            );
        }
        Thread.sleep(3000);
        for(int taskNumber = 100; taskNumber <= 127; taskNumber++) {
            int finalTaskNumber = taskNumber;
            fixedThreadPool.execute(
                    () -> {
                        System.out.println(Thread.currentThread().getName() + " " + finalTaskNumber);
                        try {
                            int times = new Random().nextInt(50) * 10;
                            System.out.println(times + '\t');
                            Thread.sleep(times);
                        } catch (InterruptedException ignored) {}
                    }
            );
        }

    }
}
