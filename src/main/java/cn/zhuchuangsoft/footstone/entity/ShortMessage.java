package cn.zhuchuangsoft.footstone.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class ShortMessage {
    @ApiModelProperty("表主键id")
    private Integer id;
    private String sendTime;
    private String sendType;
    private String warmCode;
    private String mobile;
    private String place;

}
