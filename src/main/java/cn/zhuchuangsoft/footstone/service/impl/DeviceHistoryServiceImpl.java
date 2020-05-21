package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import cn.zhuchuangsoft.footstone.mappers.DeviceHistoryMapper;
import cn.zhuchuangsoft.footstone.service.IDeviceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-18
 */

@Service
public class DeviceHistoryServiceImpl implements IDeviceHistoryService {

    @Autowired
    private DeviceHistoryMapper deviceHistoryMapper;

    @Override
    public List<PoweredDeviceHistory> selectDeviceHistory(String deviceCode, String startTime, String endTime) {
        return deviceHistoryMapper.selectDeviceHistory(deviceCode, startTime, endTime);
    }


}
