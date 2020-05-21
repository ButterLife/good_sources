package cn.zhuchuangsoft.footstone.entity.warming;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author 阿白
 * @date 2019-12-18
 */

@Data
@ApiModel
public class WarmingType extends BaseEntity {

    private static final long serialVersionUID = 3552728759955469169L;
    @ApiParam("表主键")
    private Integer id;
    @ApiParam("告警类型编码")
    private String warmingTypeCode;
    @ApiParam("告警消息名字")
    private String warmingTypeName;
    @ApiParam("这个告警上传的参数名字")
    private String paramentName;
    @ApiParam("告警阈值")
    private String val;
    @ApiParam("告警阈值判断标准（大于、小于、等于）")
    private String standart;
    @ApiParam("参数")
    private String parament;
    @ApiParam("单位")
    private String unit;

    @ApiParam("设备类型编码")
    private DeviceType deviceTypeCode;

    @ApiParam("设备类型临时编码存放")
    private String typeCode;

}
