package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class DeviceMessage extends BaseEntity {

    private static final long serialVersionUID = 7884073388751018360L;
    @ApiModelProperty("设备id")
    private String deviceId;
    @ApiModelProperty("A相电压")
    private Double voltageA;
    @ApiModelProperty("B相电压")
    private Double voltageB;
    @ApiModelProperty("C相电压")
    private Double voltageC;
    @ApiModelProperty("A相电流")
    private Double currentA;
    @ApiModelProperty("B相电流")
    private Double currentB;
    @ApiModelProperty("C相电流")
    private Double currentC;
    @ApiModelProperty("A相温度")
    private Integer tempA;
    @ApiModelProperty("B相温度")
    private Integer tempB;
    @ApiModelProperty("C相温度")
    private Integer tempC;
    @ApiModelProperty("N相温度")
    private Integer tempN;
    @ApiModelProperty("上传时间")
    private Date createTime;

    public DeviceMessage(PoweredDevice device) {
        this.deviceId = device.getDeviceCode();
        this.voltageA = device.getVoltageA() / 10.0;
        this.voltageB = device.getVoltageB() / 10.0;
        this.voltageC = device.getVoltageC() / 10.0;
        this.currentA = device.getCurrentA() / 10.0;
        this.currentB = device.getCurrentB() / 10.0;
        this.currentC = device.getCurrentC() / 10.0;
        this.tempA = device.getTempA() - 40;
        this.tempB = device.getTempB() - 40;
        this.tempC = device.getTempC() - 40;
        this.tempN = device.getTempN() - 40;
        this.createTime = device.getCreateTime();
    }
}
