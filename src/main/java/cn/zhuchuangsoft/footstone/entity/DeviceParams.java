package cn.zhuchuangsoft.footstone.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阿白
 * @date 2019-12-20
 * 参数表
 * ps:前端传过来的设备参数
 */
@ApiModel
@Data
public class DeviceParams extends BaseEntity {
    private static final long serialVersionUID = -1090508609663656840L;

    @ApiModelProperty(name = "deviceProvince", value = "设备安装所在省", required = false)
    private String deviceProvince;
    @ApiModelProperty(name = "deviceCity", value = "设备安装所在市", required = false)
    private String deviceCity;
    @ApiModelProperty(name = "deviceCounty", value = "设备安装所在县", required = false)
    private String deviceCounty;
    @ApiModelProperty(name = "deviceAddress", value = "设备安装地址（也是设备告警的地址）", required = false)
    private String deviceAddress;
    @ApiModelProperty(name = "mobile", value = "设备告警电话，多的用逗号隔开", required = false)
    private String mobile;
    @ApiModelProperty(name = "createTime", value = "创建时间", required = false)
    private String createTime;
    @ApiModelProperty(name = "updateTime", value = "修改时间", required = false)
    private String updateTime;
    @ApiModelProperty(name = "deviceState", value = "设备状态（正常，离线，告警）", required = false)
    private String deviceState;

    private String startTime;
    private String endTime;
    private Integer page = 1;
    private Integer limit = 20;
    private String key;


}
