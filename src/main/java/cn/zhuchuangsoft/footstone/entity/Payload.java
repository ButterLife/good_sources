package cn.zhuchuangsoft.footstone.entity;


import lombok.Data;

import java.util.Date;

/**
 * token载荷信息封装对象
 *
 * @param <T>
 */
@Data
public class Payload<T> {

    private String id;
    private T userInfo;
    private Date expiration;


}
