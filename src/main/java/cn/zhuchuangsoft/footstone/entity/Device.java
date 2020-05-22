package cn.zhuchuangsoft.footstone.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端展示用的。
 */
@Data
@ApiModel
public class Device extends BaseEntity {

    private static final long serialVersionUID = 754841082964042657L;
    @ApiModelProperty("表主键id")
    public Integer id;
    @ApiModelProperty("设备id")
    public String deviceId;
    @ApiModelProperty("控制器id")
    public String controllerId;
    @ApiModelProperty("设备名称")
    public String name;
    @ApiModelProperty("设备类型")
    public String categoryId;
    @ApiModelProperty("设备图标 如果为空则为null")
    public String icon;
    @ApiModelProperty("上网方式 例:WIFI")
    public String lan;
    @ApiModelProperty("设备SN码")
    public String sn;
    @ApiModelProperty("设备是否在线")
    public boolean connect;
    @ApiModelProperty("设备线路集合")
    public List<DeviceLine> lines;
    @ApiModelProperty("设备是否删除 1为删除")
    public Integer isDelete;
    @ApiModelProperty("查询设备的安装地点名称")
    public String installPlaceName;
    @ApiModelProperty("查询设备的安装地址")
    public String installPlaceAddres;

    //解析json的数据
    public Device(JSONObject jsonObject) {
        this.id = jsonObject.getInteger("$id");
        this.deviceId = jsonObject.getString("DeviceID");
        this.controllerId = jsonObject.getString("ControllerID");
        this.name = jsonObject.getString("Name");
        this.categoryId = jsonObject.getString("CategoryID");
        this.icon = jsonObject.getString("Icon");
        this.lan = jsonObject.getString("Lan");
        this.sn = jsonObject.getString("SN");
        this.connect = jsonObject.getBoolean("Connect");
        if (!jsonObject.containsKey("Lines")) {
            return;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("Lines");
        List<DeviceLine> list = new ArrayList<DeviceLine>();
        for (int i = 0; i < jsonArray.size(); i++) {
            DeviceLine deviceLine = new DeviceLine(jsonArray.getJSONObject(i));
            list.add(deviceLine);
        }
        this.lines = list;
    }

}
