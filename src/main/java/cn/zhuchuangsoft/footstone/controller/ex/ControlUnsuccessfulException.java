package cn.zhuchuangsoft.footstone.controller.ex;

/**
 * 控制开关失败异常
 */
public class ControlUnsuccessfulException extends ControllerException {

    private static final long serialVersionUID = -3240880828206713167L;

    public ControlUnsuccessfulException() {
        super();
    }

    public ControlUnsuccessfulException(String message) {
        super(message);
    }

    public ControlUnsuccessfulException(Throwable cause) {
        super(cause);
    }

    public ControlUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControlUnsuccessfulException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
