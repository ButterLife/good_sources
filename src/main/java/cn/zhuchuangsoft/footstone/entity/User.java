package cn.zhuchuangsoft.footstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class User extends BaseEntity implements UserDetails {

    private static final long serialVersionUID = 6992496522430535313L;
    @ApiModelProperty("表id")
    private Integer id;
    @ApiModelProperty("用户编码")
    private String userCode;
    @ApiModelProperty("登录用户名")
    private String username;
    @ApiModelProperty("登录密码")
    private String password;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改时间")
    private Date updateTime;
    @ApiModelProperty("操作人编码")
    private String operater;
    @ApiModelProperty("是否删除 0:存在 1:删除")
    private Integer isDelete;
    @ApiModelProperty("权限集合")
    private List<Role> roles;

    public User() {
    }

    public User(String password, String mobile) {
        this.password = password;
        this.mobile = mobile;
    }

    public User(String username, String password, String mobile, Integer roleId) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.roles = (List<Role>) new Role(roleId);
    }

    public User(String username, String password, String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }


}
