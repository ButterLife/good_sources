package cn.zhuchuangsoft.footstone.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
@Data
public class LoginFormParams extends BaseEntity {

    private static final long serialVersionUID = 3279235125484609412L;

    @ApiModelProperty(name = "username", value = "用户名", required = true, example = "root")
    @NotNull(message = "用户名不能为空!")
    private String username;

    @ApiModelProperty(name = "password", value = "密码", required = true, example = "abcdefg")
    @NotNull(message = "密码不能为空")
    private String password;

    @ApiModelProperty(name = "mobile", value = "手机号", required = true, example = "13788888888")
    @NotNull(message = "手机号码不能为空")
    @Size(min = 11, max = 11, message = "手机号码长度不正确")
    @Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(name = "msgId", value = "验证码回调id", required = true, example = "897887605204")
    private String msgId;
    @NotNull(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "验证码", required = true, example = "123456")
    private String code;

    @ApiModelProperty(name = "roleId", value = "角色id", required = true, example = "1")
    private Integer roleId = 3;
}
