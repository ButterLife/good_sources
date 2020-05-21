package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ZDeviceWarmingMapper {


    List<String> selectDeviceByMobile(String mobile);

    List<Map<String, Object>> selectDeviceWarmingByCode(@Param("deviceCode") String deviceCode, @Param("query") QueryParameters queryParameters);

    int updateWarmingById(Warming warming);

    List<Map<String, Object>> selectDeviceWarmingByCodes(@Param("array") Object[] arrys, @Param("query") QueryParameters queryParameters);

    /**
     * 添加Warming对象
     *
     * @param warming
     * @return
     */
    int insertWaring(Warming warming);


    Map<String, Object> selectWarmingById(String deviceCode);

    Map<String, Object> selectWarmingByCode(String id);

    List<Map<String, Object>> selectDeviceWarmingAll(@Param("query") QueryParameters queryParameters);

    @Select("SELECT t.*,d.TYPE_CODE as TYPE_CODE FROM ${surface} t  INNER JOIN device d on t.Device_Code=d.DEVICE_CODE  WHERE t.Device_Code = #{deviceCode}")
    Map<String, Object> selectDeviceDataByCode(@Param("surface") String surface, @Param("deviceCode") String deviceCode);

    String selectSurfaceByCode(String deviceCode);

    List<Map<String, Object>> selectWarmingByCodes(@Param("array") Object[] code);
}
