import java.util.concurrent.Callable;

public class Task<T> {
    private Callable<? extends T> task;
    private volatile Boolean isComlete = false;
    private RuntimeException exception;
    private T result;

    public Task(Callable<? extends T> callable) {
        task = callable;
    }
    public T get() throws RuntimeException {
        if (isComlete) {
            return result;
        } else if (exception != null) {
            throw exception;
        }
        synchronized (this) {
            if (isComlete) {
                return result;
            } else if (exception != null) {
                throw exception;
            }
            try {
                result = task.call();
                isComlete = true;
                return result;
            } catch (Exception e) {
                exception = new MyRuntimeException(e);
                throw exception;
            }
        }
    }
}

class MyRuntimeException extends RuntimeException {
    public MyRuntimeException(Throwable cause) {
        super(cause);
    }
}