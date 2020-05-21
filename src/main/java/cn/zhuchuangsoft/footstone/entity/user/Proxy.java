package cn.zhuchuangsoft.footstone.entity.user;

import cn.zhuchuangsoft.footstone.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阿白
 * @date 2019-12-20
 */

@Data
@ApiModel
public class Proxy extends BaseEntity {
    private static final long serialVersionUID = 3456089054691922379L;
    @ApiModelProperty("表id")
    private int id;
    @ApiModelProperty("代理商编码")
    private String proxyCode;
    @ApiModelProperty("代理商名称")
    private String proxyName;
    @ApiModelProperty("代理商地址")
    private String proxyAddress;
    @ApiModelProperty("代理商联系电话")
    private String proxyMobile;
    @ApiModelProperty("代理商联系人姓名")
    private String liaison;
    @ApiModelProperty("代理商纬度")
    private String lat;
    @ApiModelProperty("代理商经度")
    private String lon;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("修改时间")
    private String updateTime;
    @ApiModelProperty("操作人姓名")
    private String operater;


    public Proxy() {
    }

    public Proxy(String proxyCode, String proxyName, String proxyAddress, String proxyMobile, String liaison, String lat, String lon, String operater) {
        this.proxyCode = proxyCode;
        this.proxyName = proxyName;
        this.proxyAddress = proxyAddress;
        this.proxyMobile = proxyMobile;
        this.liaison = liaison;
        this.lat = lat;
        this.lon = lon;
        this.operater = operater;
    }

    public Proxy(String proxyName, String proxyAddress, String proxyMobile, String liaison, String lat, String lon, String operater) {
        this.proxyName = proxyName;
        this.proxyAddress = proxyAddress;
        this.proxyMobile = proxyMobile;
        this.liaison = liaison;
        this.lat = lat;
        this.lon = lon;
        this.operater = operater;
    }
}
