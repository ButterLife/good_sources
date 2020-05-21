package cn.zhuchuangsoft.footstone.entity.user;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import lombok.Data;

/**
 * @author 阿白
 * @date 2019-12-13
 */

@Data
public class RoleAndUser extends BaseEntity {
    private static final long serialVersionUID = 5914080349703810726L;

    private Integer id;
    private Integer userId;
    private Integer roleId;

    public RoleAndUser() {
    }

    public RoleAndUser(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
