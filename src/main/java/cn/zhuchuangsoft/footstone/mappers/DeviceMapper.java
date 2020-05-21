package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Map;


public interface DeviceMapper {

    /**
     * 获取所有设备的设备相关信息
     *
     * @return
     */

    List<Map<String, Object>> findAllDevice(QueryParameters queryParameters);

    /**
     * 根据代理用户id查询设备
     */
    List<Device1> selectDeviceByProxy(String mobile);

    /**
     * 根据普通用户id查询设备
     */
    List<Device1> selectDeviceByUser(String mobile);

    /**
     * 添加设备和用户关系
     */

//    @CacheEvict(key = "'device:'+a0.sn")
    Integer addDevices(Device1 device1);

    /**
     * 根据设备编码获取某一个设备
     */
    Device1 getDeviceByDeviceCode(String deviceCode);


    /**
     * 修改设备信息
     */

    Integer updateDevice(Device1 device1);

    /**
     * 批量删除设备
     */
    Integer deleteManages(List list);

    @Select("SELECT TYPE_CODE FROM device WHERE DEVICE_CODE =#{deviceCode}")
    String selectDeviceTypeCode(String deviceCode);

    @Select("SELECT MOBILE FROM device WHERE DEVICE_CODE =#{deviceCode}")
    String selectMoblieByCode(String deviceCode);

    /**
     * 根据DeviceId && LineNo 查询DeviceCode
     *
     * @param deviceId
     * @param lineNo
     * @return
     */

    @Select("SELECT DEVICE_CODE FROM device WHERE Device_ID = #{deviceId} AND LINE_NO = #{lineNo}AND LINE_NO = #{lineNo}AND sn = #{sn};")
    String selectDeviceCode(@Param("deviceId") String deviceId, @Param("lineNo") String lineNo, @Param("sn") String sn);


    //    @Caching(
//            put = {@CachePut(value = "device", key = "'device:deviceCode'+#device1.deviceCode"),
//                    @CachePut(value = "device", key = "'deviceName:'+#device1.deviceName")
//            }
//    )
    @Insert({"INSERT INTO device" +
            "(id,DEVICE_CODE,DEVICE_NAME,Device_ID,Controller_ID,SN," +
            "LINE_NO,TYPE_CODE,IMEI,INSYALL_PLEACE_CODE,DEVICE_PROVINCE," +
            "DEVICE_CITY,DEVICE_COUNTY,DEVICE_ADDRESS,LAT,LON," +
            "MOBILE,DEVICE_STATE,CREATE_TIME,UPDATE_TIME,PROXY_CODE," +
            "OPERATER,IS_DELETE,IS_INSTALL)VALUES" +
            "(#{device.id},#{device.deviceCode},#{device.deviceName},#{device.deviceId},#{device.controllerId},#{device.sn}," +
            "#{device.lineNo},#{device.typeCode},#{device.imei},#{device.insyallPleaceCode},#{device.deviceProvince}," +
            "#{device.deviceCity},#{device.deviceCounty},#{device.deviceAddress},#{device.lat},#{device.lon}," +
            "#{device.mobile},#{device.deviceState},#{device.createTime},#{device.updateTime},#{device.proxyCode}," +
            "#{device.operater},#{device.isDelete},#{device.isInstall})"
    })
    Integer insertDevice(@Param("device1") Device1 device1);

    //    @Cacheable(value = "device", key = "'deviceName:'+#a0")
    @Select("SELECT DEVICE_NAME FROM device WHERE DEVICE_CODE = #{deviceCode}")
    String selectDeviceName(String deviceCode);

//    @Cacheable(value = "device" , key = "")

    /**
     * 验证设备存不存在
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT DEVICE_CODE FROM device WHERE DEVICE_CODE = #{deviceCode}")
    List<String> selectId(String deviceCode);

    @Select({"SELECT id,DEVICE_CODE,DEVICE_NAME,Device_ID,Controller_ID," +
            "SN,LINE_NO,TYPE_CODE,IMEI,INSYALL_PLEACE_CODE," +
            "DEVICE_PROVINCE,DEVICE_CITY,DEVICE_COUNTY,DEVICE_ADDRESS,LAT,LON," +
            "MOBILE,DEVICE_STATE,CREATE_TIME,UPDATE_TIME,PROXY_CODE," +
            "OPERATER,IS_DELETE,IS_INSTALL " +
            "FROM device WHERE sn =#{sn};"})
    List<Device1> getDevciesBySn(String sn);

    @Select("SELECT DEVICE_CODE FROM device WHERE Device_ID = #{deviceId} AND LINE_ID = #{lineId};")
    String selectDeviceCodeByLineID(@Param("deviceId") String deviceId, @Param("lineId") String lineId);

    @Select("SELECT INSYALL_PLEACE_CODE FROM device WHERE Device_Code = #{deviceCode}")
    String selInstallPlaceCodeByDeviceCode(String deviceCode);

    @Select("select DEVICE_CODE from device where Device_ID=#{deviceId}")
    List<String> getDevicelByDeviceId(@Param("deviceId") String deviceId);

    @Select("select Err_LeakValue from device where DEVICE_CODE =#{devicelByDeviceId}")
    Integer getErrLeakValueMapper(String devicelByDeviceId);

    @Update("update device set Err_LeakValue = #{errLeakValue} where DEVICE_CODE=#{devicelByDeviceId}")
    Integer updateErrLeakValueMapper(@Param("devicelByDeviceId") String devicelByDeviceId, @Param("errLeakValue") Integer errLeakValue);

    //根据deviceId获取到设备安装地址
    @Select("SELECT id,INSTALL_PLACE_CODE,INSTALL_PLACE_NAME,INSTALL_PLACE_ADDRESS FROM `installplace`WHERE `INSTALL_PLACE_CODE`=(SELECT `INSYALL_PLEACE_CODE` FROM `device` WHERE `Device_ID` =#{deviceId} LIMIT 1)")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "INSTALL_PLACE_CODE", property = "installPlaceCode"),
            @Result(column = "INSTALL_PLACE_NAME", property = "installPlaceName"),
            @Result(column = "INSTALL_PLACE_ADDRESS", property = "installPlaceAddress"),
    })
    InstallPlace getInstallPlaceValue(String deviceId);
}
