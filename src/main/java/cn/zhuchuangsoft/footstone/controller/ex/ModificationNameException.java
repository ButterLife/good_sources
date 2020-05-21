package cn.zhuchuangsoft.footstone.controller.ex;

/**
 * 修改名称异常
 */
public class ModificationNameException extends ControllerException {
    public ModificationNameException() {
        super();
    }

    public ModificationNameException(String message) {
        super(message);
    }

    public ModificationNameException(Throwable cause) {
        super(cause);
    }

    public ModificationNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModificationNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
