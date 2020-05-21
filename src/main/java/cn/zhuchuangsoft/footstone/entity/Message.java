package cn.zhuchuangsoft.footstone.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Message extends BaseEntity {

    private static final long serialVersionUID = 2138876294768494090L;
    @ApiModelProperty("表主键id")
    private Integer id;
    @ApiModelProperty("告警信息ID")
    private String messageID;
    @ApiModelProperty("控制器ID")
    private String controllerID;
    @ApiModelProperty("线路编号")
    private Integer lineNo;
    @ApiModelProperty("告警类型")
    private String code;
    @ApiModelProperty("告警数据")
    private Double data;
    @ApiModelProperty("告警内容")
    private String content;
    @ApiModelProperty("告警等级")
    private Integer alarmSeverity;
    @ApiModelProperty("告警时间")
    private Date addTime;

    public Message(JSONObject jsonObject) {
        this.id = jsonObject.getInteger("$id");
        this.messageID = jsonObject.getString("MessageID");
        this.controllerID = jsonObject.getString("ControllerID");
        this.lineNo = jsonObject.getInteger("LineNo");
        this.code = jsonObject.getString("Code");
        this.data = jsonObject.getDouble("Data");
        this.content = jsonObject.getString("Content");
        this.alarmSeverity = jsonObject.getInteger("AlarmSeverity");
        this.addTime = jsonObject.getDate("AddTime");
    }


}
