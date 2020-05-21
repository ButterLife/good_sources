package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.mappers.DeviceTypeMapper;
import cn.zhuchuangsoft.footstone.mappers.ZDeviceMapper;
import cn.zhuchuangsoft.footstone.service.IZDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZDeviceServiceImpl implements IZDeviceService {
    @Autowired
    private ZDeviceMapper deviceMapper;
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<DeviceType> getAllDeviceType() {
        return deviceTypeMapper.getAllDeviceType();
    }

    @Override
    public List<Map<String, Object>> findAllDevice(QueryParameters queryParameters) {
        return deviceMapper.findAllDevice(queryParameters);
    }

    @Override
    public Map<String, Object> getDeviceById(String id) {
        return deviceMapper.getDeviceById(id);
    }

    @Override
    public int deleteDeviceByArray(String[] ids) {
        return deviceMapper.deleteDeviceByArray(ids);
    }

    @Override
    public List<Map<String, Object>> getProxy() {
        return deviceMapper.getProxy();
    }

    @Override
    public List<Map<String, Object>> getInstallPlace() {
        return deviceMapper.getInstallPlace();
    }

    @Override
    public int inertDevice(Device1 device) {
        return deviceMapper.inertDevice(device);
    }

    @Override
    public int updateDeviceById(Device1 device) {
        return deviceMapper.updateDeviceById(device);
    }

    @Override
    public Map<String, Object> getDeviceByCode(String deviceCode) {
        return deviceMapper.getDeviceByCode(deviceCode);
    }


}
