package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import cn.zhuchuangsoft.footstone.utils.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 写入数据库封装实体类
 */
@Data
@ApiModel
public class PoweredDevice extends BaseEntity {

    private static final long serialVersionUID = -7425714880321664214L;
    @ApiModelProperty("设备编码")
    protected String deviceCode;
    @ApiModelProperty("A相电压")
    protected Short voltageA;
    @ApiModelProperty("B相电压")
    protected Short voltageB;
    @ApiModelProperty("C相电压")
    protected Short voltageC;
    @ApiModelProperty("A相电流")
    protected Short currentA;
    @ApiModelProperty("B相电流")
    protected Short currentB;
    @ApiModelProperty("C相电流")
    protected Short currentC;
    @ApiModelProperty("A相温度 偏移量40 -40")
    protected Short tempA;
    @ApiModelProperty("B相温度")
    protected Short tempB;
    @ApiModelProperty("C相温度")
    protected Short tempC;
    @ApiModelProperty("N相温度")
    protected Short tempN;
    @ApiModelProperty("A相功率")
    protected Short powerA;
    @ApiModelProperty("B相功率")
    protected Short powerB;
    @ApiModelProperty("C相功率")
    protected Short powerC;
    @ApiModelProperty("信号强度")
    protected Short GPRS;
    @ApiModelProperty("上传时间")
    protected Date createTime;
    @ApiModelProperty("表id")
    private Integer id;

    public PoweredDevice(byte[] bytes) {
        this.deviceCode = String.valueOf(StringUtil.byteFour(bytes[4], bytes[5], bytes[6], bytes[7]));
        this.voltageA = StringUtil.byteTwo(bytes[8], bytes[9]);
        this.voltageB = StringUtil.byteTwo(bytes[10], bytes[11]);
        this.voltageC = StringUtil.byteTwo(bytes[12], bytes[13]);
        this.currentA = StringUtil.byteTwo(bytes[18], bytes[19]);
        this.currentB = StringUtil.byteTwo(bytes[20], bytes[21]);
        this.currentC = StringUtil.byteTwo(bytes[22], bytes[23]);
        this.tempA = StringUtil.byteTwo(bytes[30], bytes[31]);
        this.tempB = StringUtil.byteTwo(bytes[32], bytes[33]);
        this.tempC = StringUtil.byteTwo(bytes[34], bytes[35]);
        this.tempN = StringUtil.byteTwo(bytes[36], bytes[37]);
        this.powerA = StringUtil.byteTwo(bytes[98], bytes[99]);
        this.powerB = StringUtil.byteTwo(bytes[100], bytes[101]);
        this.powerC = StringUtil.byteTwo(bytes[102], bytes[103]);
        this.GPRS = Short.parseShort(bytes[128] + "");
        this.createTime = new Date();
    }

    public PoweredDevice() {
    }
}
