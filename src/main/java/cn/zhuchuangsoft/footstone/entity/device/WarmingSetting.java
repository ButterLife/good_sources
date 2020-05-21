package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:WarmingSetting
 * Package:cn.zhuchuangsoft.footstone.entity.device
 * Title:
 * Dsicription:
 *
 * @Date:2020/5/11 15:38)
 * @Author:1012518118@qq.com
 */
@Data
@ApiModel
public class WarmingSetting extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8306273116165402185L;
    @ApiModelProperty("表主键id")
    private Integer id;
    @ApiModelProperty("设备编码")
    private String deviceCode;
    @ApiModelProperty("设备名字")
    private String name;
    @ApiModelProperty("设备Id")
    private String deviceId;
    @ApiModelProperty("线路No")
    private Integer lineNo;
    @ApiModelProperty("线路Id")
    private String lineId;
    @ApiModelProperty("过高电压")
    private Integer heightVoltage;
    @ApiModelProperty("过低电压")
    private Integer lowVoltage;
    @ApiModelProperty("过高功率")
    private Integer heightPower;
    @ApiModelProperty("过高电流")
    private Integer heightCurrent;
    @ApiModelProperty("漏电设置")
    private Integer heightLeakage;
    @ApiModelProperty("过高温度")
    private Integer highTemp;
    @ApiModelProperty("电弧")
    private String arc;
    //`EARLY_TEMP``EARLY_3PLOW_VOLEAGE``EARLY_1PLOW_VOLEAGE``EARLY_1PHEIGHT_VOLTAGE`
    @ApiModelProperty("温度预警值")
    private Integer earlyTemp;
    @ApiModelProperty("欠压预警值")
    private Integer earlyLowVoleage;
    @ApiModelProperty("过压预警值")
    private Integer earlyHeightVoleage;


    public WarmingSetting() {
    }

    public WarmingSetting(Integer id, String deviceCode, String name, String deviceId, Integer lineNo, String lineId, Integer heightVoltage, Integer lowVoltage, Integer heightPower, Integer heightCurrent, Integer heightLeakage, Integer highTemp, String arc) {
        this.id = id;
        this.deviceCode = deviceCode;
        this.name = name;
        this.deviceId = deviceId;
        this.lineNo = lineNo;
        this.lineId = lineId;
        this.heightVoltage = heightVoltage;
        this.lowVoltage = lowVoltage;
        this.heightPower = heightPower;
        this.heightCurrent = heightCurrent;
        this.heightLeakage = heightLeakage;
        this.highTemp = highTemp;
        this.arc = arc;
    }
}
