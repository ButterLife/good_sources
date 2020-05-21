package cn.zhuchuangsoft.footstone.service.impl.ex;

import javax.management.ServiceNotFoundException;
import java.rmi.ServerException;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super();
    }


    public UserNotFoundException(String message) {
        super(message);
    }


    public UserNotFoundException(Throwable cause) {
        super(String.valueOf(cause));
    }

}
