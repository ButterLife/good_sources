package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-18
 */


public interface DeviceHistoryMapper {

    /**
     * 查看设备历史记录
     */
    List<PoweredDeviceHistory> selectDeviceHistory(@Param("deviceCode") String deviceCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据设备编码获取设备实时信息
     */
    PoweredDevice getPoweredDevice(@Param("deviceCode") String deviceCode);


}
