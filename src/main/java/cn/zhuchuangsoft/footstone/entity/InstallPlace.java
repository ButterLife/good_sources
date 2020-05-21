package cn.zhuchuangsoft.footstone.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阿白
 * @date 2019-12-26
 */
@Data
@ApiModel
public class InstallPlace extends BaseEntity {

    private static final long serialVersionUID = 6127275435695044484L;

    @ApiModelProperty("表id")
    private Integer id;
    @ApiModelProperty("地点编码")
    private String installPlaceCode;
    @ApiModelProperty("地点名称")
    private String installPlaceName;
    @ApiModelProperty("地点地址")
    private String installPlaceAddress;
    @ApiModelProperty("地点维度")
    private String installPlaceLat;
    @ApiModelProperty("地点经度")
    private String installPlaceLon;
    @ApiModelProperty("创建时间")
    private String installPlaceCreateTime;
    @ApiModelProperty("更新时间")
    private String installPlaceUpdateTime;
    @ApiModelProperty("操作人姓名")
    private String installPlaceOperater;

    public InstallPlace() {
    }

    public InstallPlace(String installPlaceCode) {
        this.installPlaceCode = installPlaceCode;
    }

    public InstallPlace(String installPlaceName, String installPlaceAddress, String installPlaceLat, String installPlaceLon, String installPlaceOperater) {
        this.installPlaceName = installPlaceName;
        this.installPlaceAddress = installPlaceAddress;
        this.installPlaceLat = installPlaceLat;
        this.installPlaceLon = installPlaceLon;
        this.installPlaceOperater = installPlaceOperater;
    }

    public InstallPlace(String installPlaceCode, String installPlaceName, String installPlaceAddress, String installPlaceLat, String installPlaceLon, String installPlaceOperater) {
        this.installPlaceCode = installPlaceCode;
        this.installPlaceName = installPlaceName;
        this.installPlaceAddress = installPlaceAddress;
        this.installPlaceLat = installPlaceLat;
        this.installPlaceLon = installPlaceLon;
        this.installPlaceOperater = installPlaceOperater;
    }
}
