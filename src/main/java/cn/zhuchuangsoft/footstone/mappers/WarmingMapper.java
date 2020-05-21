package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface WarmingMapper {

    /**
     * 警告列表展示
     */
    List<Warming> getAllWarming(Warming warming);

    /**
     * 根据设备编码进行警告列表的展示
     */
    List<Warming> getWarmingByCode(@Param("deviceCode") String deviceCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    Warming getWarmingByWarmingCode(String warmingCode);


    /**
     * @param deviceCode
     * @return
     */
    Map<String, Object> selectWarmingByCode(String deviceCode);

    //    @CachePut(value = "device", key = "'isExsits:'+#a0.deviceCode+'Msg:'+#a0.warmingMsg")
    @Insert({"INSERT INTO warming" +
            "(warming_code,device_code,warming_msg,ishandle,handle_time,handle_msg,warming_time) VALUES " +
            "(#{warming.warmingCode},#{warming.deviceCodes},#{warming.warmingMsg},#{warming.ishandle},#{warming.handleTime},#{warming.handleMsg},#{warming.warmingTime})"})
    Integer insertWarming(@Param("warming") Warming warming);

    // @Select("SELECT id,warming_code,warming_msg,ishandle,handle_time,handle_msg,warming_time,device_code FROM warming WHERE device_code = #{deviceCode}")
    @Select({"<script>SELECT id,warming_code,warming_msg,ishandle,handle_time,handle_msg,warming_time,device_code from warming <where>" +
            "<if test='deviceCode!=null'> AND device_code = #{deviceCode}</if>" +
            " <if test='isHandle==1'>AND ishandle = #{isHandle}</if>" +
            " <if test='isHandle==0'>AND ISNULL(ishandle)</if>" +
            "</where></script>"
    })
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "warming_code", property = "warmingCode"),
            @Result(column = "device_code", property = "deviceCodes"),
            @Result(column = "warming_msg", property = "warmingMsg"),
            @Result(column = "ishandle", property = "ishandle"),
            @Result(column = "handle_time", property = "handleTime"),
            @Result(column = "handle_msg", property = "handleMsg"),
            @Result(column = "warming_time", property = "warmingTime"),
    })
    List<Warming> selectWarmingByUserCode(@Param("deviceCode") String deviceCode, @Param("isHandle") String isHandle);

    //    @Select("SELECT id,warming_code,warming_msg,ishandle,handle_time,handle_msg,warming_time,device_code FROM warming WHERE device_code = #{deviceCode} AND (ishandle = '0' OR ISNULL(ishandle));")
    @Select("SELECT id,warming_code,warming_msg,ishandle,handle_time,handle_msg,DATE_ADD(`warming_time`,INTERVAL 30 MINUTE) warming_time,device_code FROM warming WHERE device_code = #{deviceCode} AND (ishandle = '0' OR ISNULL(ishandle));")
    List<Warming> selectWarmingIshandel(@Param("deviceCode") String deviceCode);

    //    @Cacheable(value = "device", key = "'isExsits:'+#a0.deviceCode+'Msg:'+#a0.warmingMsg")
    @Select({"<script>SELECT id from warming <where>" +
            " <if test='warming.warmingCode!=null'> AND  warming_code = #{warming.warmingCode}</if>" +
            "<if test='warming.deviceCode!=null'> AND device_code = #{warming.deviceCode}</if>" +
            " <if test='warming.warmingMsg!=null'>AND warming_msg = #{warming.warmingMsg}</if>" +
            "</where></script>"
    })
//<if test='warming.warmingTime!=null'>AND warming_time =#{warming.warmingTime}</if>
    List<Integer> isExsits(@Param("warming") Warming warming);

    @Update({"UPDATE warming SET " +
            "warming_code = #{warming.warmingCode},device_code =#{warming.deviceCodes}," +
            "warming_msg=#{warming.warmingMsg},ishandle=#{warming.ishandle},handle_time=#{warming.handleTime}," +
            "handle_msg=#{warming.handleMsg},warming_time=#{warming.warmingTime} " +
            "WHERE id = #{warming.id};"})
    Integer updateWarming(Warming warming);

    /**
     * 通过 id设置是否已处理
     *
     * @param id
     * @return
     */
    @Update({"UPDATE warming SET " +
            "ishandle= '1' ,handle_msg = #{handleMsg},handle_time = #{handleTime}" +
            "WHERE id = #{id};"})
    Integer setIsHandle(@Param("id") Integer id, @Param("handleMsg") String handleMsg, @Param("handleTime") String handleTime);

    @Select("SELECT * FROM warming WHERE device_code = #{deviceCode};")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "warming_code", property = "warmingCode"),
            @Result(column = "device_code", property = "deviceCodes"),
            @Result(column = "warming_msg", property = "warmingMsg"),
            @Result(column = "ishandle", property = "ishandle"),
            @Result(column = "handle_time", property = "handleTime"),
            @Result(column = "handle_msg", property = "handleMsg"),
            @Result(column = "warming_time", property = "warmingTime"),
    })
    List<Warming> getWarmingTempByCode(String deviceCode);

    @Select("SELECT device_code FROM `warming` WHERE `warming_code`='电弧预警' AND `device_code`=#{deviceCode} AND ISNULL(ishandle)")
    List<String> getWarmingByCodeType(String deviceCode);


}
