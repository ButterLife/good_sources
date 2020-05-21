package cn.zhuchuangsoft.footstone.entity.warming;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 张洲权
 * @date
 */
@Data
public class JalaWarming {

    private Integer id;
    private String deviceId;
    private String ishandle;
    private Date handleTime;
    private String handleMsg;
    private Date warmingTime;
    private List lsit;


}
