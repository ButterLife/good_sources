package cn.zhuchuangsoft.footstone.entity.user;

/**
 * @author 阿白
 * @date 2019-12-11
 */

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import lombok.Data;

/**
 * 用户和设备的关联表
 */
@Data
public class UserAndDevice extends BaseEntity {
    private static final long serialVersionUID = 3718236320904799839L;
    private Integer id;
    private Integer userId;
    private String deviceId;

    private Integer proxyId;//代理商id

    public UserAndDevice() {
    }

    public UserAndDevice(Integer userId, String deviceId, Integer proxyId) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.proxyId = proxyId;
    }
}
