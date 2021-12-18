package priv.ivrdsl.exception;

/**
 * 没有相关的数据库配置。
 *
 * @author Guanidine Beryllium
 */
public class MissingPropsException extends BaseException {
    public MissingPropsException(String message) {
        super(message);
    }
}
