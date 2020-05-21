package cn.zhuchuangsoft.footstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class Role extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = -443879735659873383L;
    private Integer id;
    private String roleName;
    private String roleDesc;

    public Role(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
