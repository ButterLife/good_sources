package cn.zhuchuangsoft.footstone.entity.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 添加关注类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VipDevice {
    private Integer id;
    private String userCode;
    private String userName;
    private String deviceCode;
    private String implaceName;
    private String implaceAddr;
    private String warmingMsg;
    private String warmingTime;
    private String warmingCode;
}
