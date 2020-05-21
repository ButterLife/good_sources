package cn.zhuchuangsoft.footstone.controller.ex;

/**
 * 参数不正确或缺失异常
 */
public class ParameterIsIncorrect extends ControllerException {

    private static final long serialVersionUID = 8097715888020069738L;

    public ParameterIsIncorrect() {
        super();
    }

    public ParameterIsIncorrect(String message) {
        super(message);
    }

    public ParameterIsIncorrect(Throwable cause) {
        super(cause);
    }

    public ParameterIsIncorrect(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterIsIncorrect(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
