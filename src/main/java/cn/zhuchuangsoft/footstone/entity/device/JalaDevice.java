package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel
public class JalaDevice extends BaseEntity {


    @ApiParam("设备编码")
    private String deviceCode;
    @ApiParam("设备电压")
    private Double voltage;
    @ApiParam("设备功率")
    private Double power;
    @ApiParam("设备电流")
    private Double current;
    @ApiParam("设备温度")
    private Double temp;
    @ApiParam("设备漏电流")
    private Double leakage;
    @ApiParam("开关状态")
    private int status;
    @ApiParam("更新时间")
    private String updateTime;
}
