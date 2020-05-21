package cn.zhuchuangsoft.footstone.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceLine extends BaseEntity {

    private static final long serialVersionUID = 6127275435695044483L;
    @ApiModelProperty("线路表ID")
    private String id;
    @ApiModelProperty("线路ID")
    private String lineID;
    @ApiModelProperty("线路编号")
    private Integer lineNo;
    @ApiModelProperty("线路版本")
    private Integer lineVersion;
    @ApiModelProperty("线路在线状态 1:在线 0:离线")
    private Integer lineStatus;
    @ApiModelProperty("线路名称")
    private String name;
    @ApiModelProperty("产品型号")
    private String model;
    @ApiModelProperty("漏电预警值 单位mA")
    private Integer leakValue;
    @ApiModelProperty("漏电故障值 单位mA")
    private Integer errLeakValue;
    @ApiModelProperty("当前漏电值(剩余电流)")
    private Double leakAge;
    @ApiModelProperty("区分产品是否带漏电保护功能" +
            "0:不显示漏电值,没有漏电保护功能" +
            "1:显示漏电值,有漏电保护功能,可以远程漏电自检" +
            "2:只显示漏电值,没有漏电保护功能,可以修改漏电预警值")
    private Integer isLeakAge;
    @ApiModelProperty("线路功率")
    private Double power;
    @ApiModelProperty("线路电压（单相）")
    private Double voltage;
    @ApiModelProperty("开关状态 0:关闭 1:开启")
    private Integer status;
    @ApiModelProperty("线路最大电流")
    private Double max;
    @ApiModelProperty("线路当前电流（单相）")
    private Double current;
    @ApiModelProperty("最大月用电量")
    private Double limit;
    @ApiModelProperty("当前温度（单相）")
    private Double temp;
    @ApiModelProperty("欠压值")
    private Integer under;
    @ApiModelProperty("过压值")
    private Integer over;
    @ApiModelProperty("锁定物理开关 0:不锁定 1:锁定")
    private Integer enabled;
    @ApiModelProperty("过流持续时间 持续多久后断开")
    private Integer duration;
    @ApiModelProperty("AB电压（三相）")
    private Double voltageAB;
    @ApiModelProperty("BC电压（三相）")
    private Double voltageBC;
    @ApiModelProperty("CA电压（三相）")
    private Double voltageCA;
    @ApiModelProperty("A电流（三相)")
    private Double currentA;
    @ApiModelProperty("B电流(三相）")
    private Double currentB;
    @ApiModelProperty("C电流（三相）")
    private Double currentC;
    @ApiModelProperty("A温度（三相）")
    private Double tempA;
    @ApiModelProperty("B温度（三相）")
    private Double tempB;
    @ApiModelProperty("C温度（三相）")
    private Double tempC;
    @ApiModelProperty("N温度（三相）")
    private Double tempN;
    @ApiModelProperty("修改时间")
    private String updateTime;
    @ApiModelProperty("电弧")
    private String arc;

    public DeviceLine() {

    }

    public DeviceLine(JSONObject jsonObject) {
        this.id = jsonObject.getString("$id");
        this.lineID = jsonObject.getString("LineID");
        this.lineNo = jsonObject.getInteger("LineNo");
        this.lineVersion = jsonObject.getInteger("Line_Version");
        this.lineStatus = jsonObject.getInteger("Line_Status");
        this.name = jsonObject.getString("Name");
        this.model = jsonObject.getString("Model");
        this.leakValue = jsonObject.getInteger("LeakValue");
        this.errLeakValue = jsonObject.getInteger("Err_LeakValue");
        this.leakAge = jsonObject.getDouble("Leakage");
        this.isLeakAge = jsonObject.getInteger("isLeakage");
        this.power = jsonObject.getDouble("Power");
        this.voltage = jsonObject.getDouble("Voltage");
        this.status = jsonObject.getInteger("Status");
        this.max = jsonObject.getDouble("Max");
        this.current = jsonObject.getDouble("Current");
        this.limit = jsonObject.getDouble("Limit");
        this.temp = jsonObject.getDouble("Temp");
        this.under = jsonObject.getInteger("Under");
        this.over = jsonObject.getInteger("Over");
        this.enabled = jsonObject.getInteger("Enabled");
        this.duration = jsonObject.getInteger("Duration");
        this.voltageAB = jsonObject.getDouble("VoltageAB");
        this.voltageBC = jsonObject.getDouble("VoltageBC");
        this.voltageCA = jsonObject.getDouble("VoltageCA");
        this.currentA = jsonObject.getDouble("CurrentA");
        this.currentB = jsonObject.getDouble("CurrentB");
        this.currentC = jsonObject.getDouble("CurrentC");
        this.tempA = jsonObject.getDouble("TempA");
        this.tempB = jsonObject.getDouble("TempB");
        this.tempC = jsonObject.getDouble("TempC");
        this.tempN = jsonObject.getDouble("TempN");
    }

}
