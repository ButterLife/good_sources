package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * ClassName:WarmingSettingMapper
 * Package:cn.zhuchuangsoft.footstone.mappers
 * Title:
 * Dsicription:
 *
 * @Date:2020/4/24 22:37)
 * @Author:1012518118@qq.com
 */
public interface WarmingSettingMapper {
    /**
     * 查看是否存在此deviceCode.
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT DEVICE_CODE from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectExists(String deviceCode);

    @Select("SELECT EARLY_TEMP from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    Integer selectEarlyTemp(String deviceCode);

    /**
     * 根据deviceCode 获取过高电压
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT HEIGHT_VOLTAGE from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHighVoleage(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取过低电压
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT LOW_VOLEAGE from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectLowVoleage(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取过高功率
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT HEIGHT_POWER from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHighPower(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取过高电流
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT HEIGHT_CURRENT from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHighCurrent(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取过高漏电流
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT HEIGHT_LEAKAGE from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHighLeakage(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取过温最高值
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT HEIGHT_TEMP from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHighTemp(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 获取是否支持电弧
     *
     * @param deviceCode
     * @return
     */
    @Select("SELECT ARC from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectARC(@Param("deviceCode") String deviceCode);

    /**
     * 无此线路设置时插入此deviceCode的设置
     *
     * @param deviceCode
     * @return
     */
    @Insert("INSERT INTO t_warmingsetting (DEVICE_CODE) VALUE(#deviceCode)")
    Integer insertWarmingSetting(@Param("deviceCode") String deviceCode);

    /**
     * 根据deviceCode 更新过压值
     *
     * @param heightVoltage
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET HEIGHT_VOLTAGE = #{heightVoltage} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateHeightVoltage(String deviceCode, String heightVoltage);

    /**
     * 根据deviceCode 更新欠压值
     *
     * @param lowVoltage
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET LOW_VOLEAGE = #{lowVoltage} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateLowVoltage(String deviceCode, String lowVoltage);

    /**
     * 根据deviceCode 更新过高功率值
     *
     * @param heightPower
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET HEIGHT_POWER = #{heightPower} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateHeightPower(@Param("deviceCode") String deviceCode, @Param("heightPower") String heightPower);

    @Select("SELECT HEIGHT_POWER from t_warmingsetting WHERE DEVICE_CODE = #{deviceCode};")
    String selectHeightPower(String DeviceCode);

    /**
     * 根据deviceCode 更新过高电流流值
     *
     * @param heightCurrent
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET HEIGHT_CURRENT = #{heightCurrent} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateHeightGurrent(String deviceCode, String heightCurrent);

    /**
     * 根据deviceCode 更新过高漏电值
     *
     * @param heightLeakage
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET HEIGHT_LEAKAGE = #{heightLeakage} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateHeightLeakage(String deviceCode, String heightLeakage);

    /**
     * 根据deviceCode 更新电弧   暂定"0"为不警报，"1"为警报
     *
     * @param arc
     * @param deviceCode
     * @return
     */
    @Update("UPDATE t_warmingsetting SET ARC = #{arc} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateACE(@Param("deviceCode") String deviceCode, @Param("arc") String arc);

    /**
     * 改过温动作值(注意点：数据库过温最高值是String)
     *
     * @param deviceCode
     * @param highTemp
     * @return
     */
    @Update("UPDATE t_warmingsetting SET HEIGHT_TEMP = #{highTemp} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateHighTemp(@Param("deviceCode") String deviceCode, @Param("highTemp") String highTemp);

    @Update("UPDATE t_warmingsetting SET EARLY_TEMP = #{temp} WHERE DEVICE_CODE = #{deviceCode};")
    Integer updateEarlyTemp(String deviceCode, Integer temp);

    //查找预警值
    @Select("SELECT `id`,`DEVICE_CODE`,`HEIGHT_VOLTAGE`,`LOW_VOLEAGE`,`HEIGHT_POWER`,`HEIGHT_CURRENT`,`HEIGHT_LEAKAGE`,`HEIGHT_TEMP`,`ARC`,`EARLY_TEMP`,EARLY_LOW_VOLEAGE,EARLY_HEIGHT_VOLTAGE FROM `t_warmingsetting` WHERE `DEVICE_CODE`=#{deviceCode}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "DEVICE_CODE", property = "deviceCode"),
            @Result(column = "HEIGHT_VOLTAGE", property = "heightVoltage"),
            @Result(column = "LOW_VOLEAGE", property = "lowVoltage"),
            @Result(column = "HEIGHT_POWER", property = "heightPower"),
            @Result(column = "HEIGHT_CURRENT", property = "heightCurrent"),
            @Result(column = "HEIGHT_LEAKAGE", property = "heightLeakage"),
            @Result(column = "HEIGHT_TEMP", property = "highTemp"),
            @Result(column = "ARC", property = "arc"),
            @Result(column = "EARLY_TEMP", property = "earlyTemp"),
            @Result(column = "EARLY_LOW_VOLEAGE", property = "earlyLowVoleage"),
            @Result(column = "EARLY_HEIGHT_VOLTAGE", property = "earlyHeightVoleage"),

    })
    WarmingSetting getWarmingSetting(String deviceCode);

    //修改数据库中的值
    @Update("UPDATE t_warmingsetting SET EARLY_HEIGHT_VOLTAGE = #{over},EARLY_LOW_VOLEAGE = #{under} WHERE DEVICE_CODE = #{deviceCode};")
    int updateVoltageByDeviceCode(String deviceCode, Integer under, Integer over);
}
