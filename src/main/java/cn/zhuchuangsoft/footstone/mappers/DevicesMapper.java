package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.Device;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface DevicesMapper {

    /**
     * 根据设备列表,添加设备
     *
     * @param data
     * @return
     */
    @Insert({"<script>INSERT INTO devices (" +
            "device_id,controller_id,name," +
            "category_id,icon,lan," +
            "sn,connect" +
            ") VALUES " +
            "<foreach collection=\"data\" separator=\"),(\" open=\"(\" close=\")\" item=\"device\">" +
            "#{device.deviceId},#{device.controllerId}," +
            "#{device.categoryId},#{device.icon},#{device.lan},#{device.sn}," +
            "#{device.connect}" +
            "</foreach></script>"})
    Integer addDevice(@Param("list") List<Device> data);

    /**
     * 获取所有设备的设备相关信息
     *
     * @return
     */
    @Select("SELECT" +
            "device_id,controller_id,name," +
            "category_id,icon,lan," +
            "sn,connect" +
            "FROM" +
            "devices" +
            "WHERE" +
            "is_delete!=1")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "device_id", property = "deviceId"),
            @Result(column = "controller_id", property = "controllerId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "is_delete", property = "isDelete"),
    })
    List<Device> getDeviceAll();

    /**
     * 添加设备和用户关系
     */

    @Insert(
            {"insert into device(" +
                    "device_id,name,category_id,icon,lan,sn)" +
                    "values" +
                    "(#{deviceId},#{name},#{categoryId}," +
                    "#{icon},#{lan},#{sn})"}
    )
    Integer addDevices(Device1 device1);

    @Select("SELECT LINE_NO,DEVICE_CODE,MOBILE FROM device WHERE Controller_ID =#{controllerId}")
    List<Map<String, Object>> getDeviceByControllerId(String controllerId);

    @Select("SELECT mobile FROM user_device WHERE device_code =#{code}")
    List<String> getUserListByCode(String code);
}
