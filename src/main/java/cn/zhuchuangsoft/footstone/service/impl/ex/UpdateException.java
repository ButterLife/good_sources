package cn.zhuchuangsoft.footstone.service.impl.ex;

import javax.management.ServiceNotFoundException;
import java.rmi.ServerException;

public class UpdateException extends Exception {


    public UpdateException() {
        super();
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(Throwable cause) {
        super(String.valueOf(cause));
    }

}


