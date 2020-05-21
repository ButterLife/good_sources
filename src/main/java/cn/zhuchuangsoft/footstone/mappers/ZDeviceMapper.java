package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZDeviceMapper {
    List<Map<String, Object>> findAllDevice(QueryParameters queryParameters);

    Map<String, Object> getDeviceById(String id);

    int deleteDeviceByArray(String[] array);

    List<Map<String, Object>> getProxy();

    List<Map<String, Object>> getInstallPlace();

    int inertDevice(Device1 device);

    int updateDeviceById(Device1 device);

    Map<String, Object> getDeviceByCode(String deviceCode);
}
