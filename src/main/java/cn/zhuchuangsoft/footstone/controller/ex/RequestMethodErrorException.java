package cn.zhuchuangsoft.footstone.controller.ex;

/**
 * 请求方法不正确,可能是佳岚接口换请求方式了
 */
public class RequestMethodErrorException extends ControllerException {

    private static final long serialVersionUID = -5909559510169349595L;

    public RequestMethodErrorException() {
        super();
    }

    public RequestMethodErrorException(String message) {
        super(message);
    }

    public RequestMethodErrorException(Throwable cause) {
        super(cause);
    }

    public RequestMethodErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestMethodErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
