package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-12
 */

@Data
@ApiModel
public class Device1 extends BaseEntity {
    private static final long serialVersionUID = -8306273116165402185L;
    @ApiModelProperty("表主键id")
    private Integer id;
    @ApiModelProperty("设备编码,格式typecode+“-”+model+“-”+LineId")
    private String deviceCode;
    @ApiModelProperty("设备名字")
    private String deviceName;
    @ApiModelProperty("jala设备编码")
    private String deviceId;
    @ApiModelProperty("jala设备控制编码")
    private String controllerId;
    @ApiModelProperty("jala设备SN编码")
    private String sn;
    @ApiModelProperty("jala设备线路")
    private String lineNo;
    @ApiModelProperty("设备物联网通信编码")
    private String imei;
    @ApiModelProperty("设备安装地点集合编码")
    private String insyallPleaceCode;
    @ApiModelProperty("设备安装所在省")
    private String deviceProvince;
    @ApiModelProperty("设备安装所在市")
    private String deviceCity;
    @ApiModelProperty("设备安装所在县")
    private String deviceCounty;
    @ApiModelProperty("设备安装地址（也是设备告警的地址）")
    private String deviceAddress;
    @ApiModelProperty("设备所在纬度")
    private String lat;
    @ApiModelProperty("设备所在经度")
    private String lon;
    @ApiModelProperty("设备告警电话，多的用逗号隔开")
    private String mobile;
    @ApiModelProperty("设备状态（正常，离线，告警）")
    private String deviceState;
    @ApiModelProperty("设备类型编码")
    private DeviceType typeCode;
    @Transient
    private String proxyCode;
    @Transient
    private String deviceTypeCode;
    @Transient
    private String deviceTypeName;

    @ApiModelProperty("是否安装")
    private Integer isInstall;
    @ApiModelProperty("设备实时信息")
    private PoweredDevice poweredDevice;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("修改时间")
    private String updateTime;
    @ApiModelProperty("操作人姓名")
    private String operater;
    @ApiModelProperty("设备是否删除 1为删除")
    private Integer isDelete;
    @ApiModelProperty("代理商编码")
    private Proxy proxy;
    @ApiModelProperty("漏电动作值")
    private Integer errLeakValue;

    private String adminName;
    private String adminTel;

/*    @Transient
    private Short voltageA;
    @Transient
    protected Short voltageB;
    @Transient
    protected Short voltageC;
    @Transient
    protected Short currentA;
    @Transient
    protected Short currentB;
    @Transient
    protected Short currentC;
    @Transient
    protected Short tempA;
    @Transient
    protected Short tempB;
    @Transient
    protected Short tempC;
    @Transient
    protected Short tempN;*/


    public Device1() {
    }

    public Device1(DeviceType typeCode, String imei, String insyallPleaceCode, String deviceProvince, String deviceCity, String deviceCounty, String deviceAddress, String lat, String lon, String mobile, String deviceState, String operater, String proxyCode) {
        this.typeCode = typeCode;
        this.imei = imei;
        this.insyallPleaceCode = insyallPleaceCode;
        this.deviceProvince = deviceProvince;
        this.deviceCity = deviceCity;
        this.deviceCounty = deviceCounty;
        this.deviceAddress = deviceAddress;
        this.lat = lat;
        this.lon = lon;
        this.mobile = mobile;
        this.deviceState = deviceState;
        this.operater = operater;
        this.proxyCode = proxyCode;
    }

    public Device1(DeviceType deviceType, String itmet, InstallPlace installplace, String deviceProvince, String deviceCity, String deviceCounty, String deviceAddress, String lat, String lon, String mobile, String deviceState, String operater, String proxyCode) {

    }

}
