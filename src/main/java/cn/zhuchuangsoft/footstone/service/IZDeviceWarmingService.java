package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;

import java.util.List;
import java.util.Map;

public interface IZDeviceWarmingService {
    /**
     * 根据号码查询当前所有设备code
     *
     * @param mobile
     * @return
     */
    List<String> selectDeviceByMobile(String mobile);

    /**
     * 根据deviceCode查询所有报警信息
     *
     * @param deviceCode
     * @return
     */
    List<Map<String, Object>> selectDeviceWarmingByCode(String deviceCode, QueryParameters queryParameters);

    /**
     * 更新Warming对象
     *
     * @param warming
     * @return
     */
    int updateWarmingById(Warming warming);

    /**
     * @param objects
     * @param queryParameters
     * @return
     */
    List<Map<String, Object>> selectDeviceWarmingByCodes(Object[] objects, QueryParameters queryParameters);

    /**
     * @param queryParameters
     * @return
     */
    List<Map<String, Object>> selectDeviceWarmingAll(QueryParameters queryParameters);

    /**
     * @param deviceCode
     * @return
     */
    List<Map<String, Object>> selectDeviceDataByCode(String surface, String deviceCode);

    /**
     * @param deviceCode
     * @return
     */
    String selectSurfaceByCode(String deviceCode);

    /**
     * 根据报警id查询详细报警信息
     *
     * @param id
     * @return
     */
    Map<String, Object> selectWarmingById(String id);

    List<WarmingType> getWarmingTypeAll(String deviceTypeCode);

    void ignoreWarming(String id);

    List<Map<String, Object>> selectWarmingByCodes(Object[] code);
}
