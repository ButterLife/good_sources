package cn.zhuchuangsoft.footstone.controller.ex;

/**
 * 用户名或密码错误
 */
public class UserNameOrPasswordErrorException extends ControllerException {

    private static final long serialVersionUID = -7780076604831671127L;

    public UserNameOrPasswordErrorException() {
        super();
    }

    public UserNameOrPasswordErrorException(String message) {
        super(message);
    }

    public UserNameOrPasswordErrorException(Throwable cause) {
        super(cause);
    }

    public UserNameOrPasswordErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameOrPasswordErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
