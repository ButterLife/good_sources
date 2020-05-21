package cn.zhuchuangsoft.footstone.entity.device;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-18
 */

@Data
@ApiModel
public class DeviceType extends BaseEntity {
    private static final long serialVersionUID = -5923966956013371929L;

    @ApiParam("表id")
    private Integer id;
    @ApiParam("设备类型编码")
    private String deviceTypeCode;
    @ApiParam("设备类型名称")
    private String deviceTypeName;
    @ApiParam("设备记录数据子表的表名")
    private String deviceTypeTableName;
    @ApiParam("设备记录数据历史记录的子表的表名")
    private String deviceTypeHistoryTableName;
    @ApiParam("不同设备报警类型")
    private List<WarmingType> warmingTypes;


    public DeviceType() {
    }

    public DeviceType(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }
}
