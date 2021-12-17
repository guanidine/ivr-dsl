package priv.ivrdsl.exceptions;

/**
 * 自定义异常处理的“根异常”类
 *
 * @author Guanidine Beryllium
 */
public class BaseException extends RuntimeException {
    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}