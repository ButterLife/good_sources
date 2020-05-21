package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-18
 */


public interface IDeviceHistoryService {

    /**
     * 查看设备历史记录
     */
    List<PoweredDeviceHistory> selectDeviceHistory(String deviceCode, String startTime, String endTime);


}
