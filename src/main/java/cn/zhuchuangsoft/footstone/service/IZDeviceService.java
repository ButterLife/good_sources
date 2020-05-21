package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;

import java.util.List;
import java.util.Map;

public interface IZDeviceService {
    /**
     * 获取全部设备类型
     *
     * @return
     */
    List<DeviceType> getAllDeviceType();

    /**
     * 获取设备
     *
     * @param queryParameters
     * @return
     */
    List<Map<String, Object>> findAllDevice(QueryParameters queryParameters);

    /**
     * 根据id 获得设备信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getDeviceById(String id);

    /**
     * 删除设备
     *
     * @param split
     * @return
     */
    int deleteDeviceByArray(String[] split);

    /**
     * 获得代理商Lisr
     *
     * @return
     */
    List<Map<String, Object>> getProxy();

    /**
     * 获得安装地点Lisr
     *
     * @return
     */
    List<Map<String, Object>> getInstallPlace();

    /**
     * 添加设备
     *
     * @param device
     * @return
     */
    int inertDevice(Device1 device);

    /**
     * 更新设备
     *
     * @param device
     * @return
     */
    int updateDeviceById(Device1 device);

    Map<String, Object> getDeviceByCode(String deviceCode);
}
