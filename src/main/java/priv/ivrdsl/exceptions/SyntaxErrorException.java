package priv.ivrdsl.exceptions;

/**
 * 脚本语言的语法错误。
 * @author Guanidine Beryllium
 */
public class SyntaxErrorException extends BaseException {
    public SyntaxErrorException(String message) {
        super(message);
    }
}
