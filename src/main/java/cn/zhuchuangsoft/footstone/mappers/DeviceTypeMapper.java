package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.device.DeviceType;

import java.util.List;
import java.util.Map;

/**
 * @author 阿白
 * @date 2019-12-18
 */


public interface DeviceTypeMapper {

    /**
     * 获取设备类型编码
     */
    List<DeviceType> getAllDeviceType();
}
