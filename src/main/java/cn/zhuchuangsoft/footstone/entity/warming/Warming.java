package cn.zhuchuangsoft.footstone.entity.warming;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 阿白
 * @date 2019-12-18
 */

@Data
@ApiModel
@ToString
public class Warming extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4461538080515903331L;

    @ApiParam("表id")
    private Integer id;
    @ApiParam("告警编码")
    private String warmingCode;
    @ApiParam("告警消息")
    private String warmingMsg;
    @ApiParam("告警时间")
    private String warmingTime;
    @ApiParam("是否被处理了")
    private String ishandle;
    @ApiParam("处理时间")
    private String handleTime;
    @ApiParam("处理说明,两种处理方式：忽略 &&已处理")
    private String handleMsg;
    @ApiParam("设备编码对象")
    private Device1 deviceCode;

    private String voltage;
    private String current;
    private String leak;

    @ApiParam("设备临时deviceCode")
    private String deviceCodes;

    public Warming() {
    }

    public Warming(String ishandle, String warmingTime) {
        this.ishandle = ishandle;
        this.warmingTime = warmingTime;
    }
}
