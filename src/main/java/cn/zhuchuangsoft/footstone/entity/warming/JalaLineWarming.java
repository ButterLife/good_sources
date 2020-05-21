package cn.zhuchuangsoft.footstone.entity.warming;

import lombok.Data;

@Data
public class JalaLineWarming {
    private Integer id;
    private String deviceId;
    private String lineId;
    private String warmingCode;
    private String warmingMsg;
    private String name;
}
