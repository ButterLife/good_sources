package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.Area;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.VipDevice;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import cn.zhuchuangsoft.footstone.mappers.*;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private UserAndDeviceMapper userAndDeviceMapper;
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;
    @Autowired
    private WarmingTypeMapper warmingTypeMapper;
    @Autowired
    private DeviceHistoryMapper deviceHistoryMapper;

    @Autowired
    private InstallPlaceMapper installPlaceMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public String selectDeviceTypeCode(String deviceCode) {
        return deviceMapper.selectDeviceTypeCode(deviceCode);
    }

    @Override
    public String selectMoblieByCode(String deviceCode) {
        return deviceMapper.selectMoblieByCode(deviceCode);
    }

    @Override
    public Integer increaseDevice(Device1 device1) {

        String flag = addDevice(device1);
        if (flag != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> findAllDevice(QueryParameters queryParameters) {
        return deviceMapper.findAllDevice(queryParameters);
    }


    @Override
    public List<Device1> selectDeviceByProxy(String mobile) {
        return deviceMapper.selectDeviceByProxy(mobile);
    }

    @Override
    public List<Device1> selectDeviceByUser(String mobile) {
        return deviceMapper.selectDeviceByUser(mobile);
    }

    @Override
    public Map<Object, Object> getDeviceByDeviceCode(String deviceCode) {
        Device1 device = deviceMapper.getDeviceByDeviceCode(deviceCode);
        Map<Object, Object> maps = new HashMap<Object, Object>();
        if (!StringUtils.isEmpty(device)) {
            PoweredDevice poweredDevice = deviceHistoryMapper.getPoweredDevice(device.getDeviceCode());
            List<WarmingType> warmingTypes = warmingTypeMapper.getWarmingType(device.getDeviceTypeCode());
            if (StringUtils.isEmpty(poweredDevice)) {
                maps.put("device", device);
                maps.put("powered", "");
                maps.put("warmingType", "");
            } else {
                maps.put("device", device);
                maps.put("powered", poweredDevice);
                maps.put("warmingType", warmingTypes);
            }
        } else {
            maps.put("device", "");
            maps.put("powered", "");
            maps.put("warmingType", "");
            maps.put("msg", "设备类型异常");
        }
        return maps;
    }

    @Override
    public Device1 DeviceByDeviceCode(String sn) {
        return deviceMapper.getDeviceByDeviceCode(sn);
    }

    @Override
    public Integer updateDevice(Device1 device1) {
        return deviceMapper.updateDevice(device1);
    }

    @Override
    public Integer deleteManages(List list) {
        return deviceMapper.deleteManages(list);
    }

    @Override
    public List<DeviceType> getAllDeviceType() {
        return deviceTypeMapper.getAllDeviceType();
    }

    @Override
    public List<Area> selectProvince() {
        return areaMapper.selectProvince();
    }

    /**
     * 通过deviceId&lineNo获取lineId
     *
     * @return
     */
    @Override
    public String selectLineId() {
        return null;
    }

    /**
     * 分割线
     */
 /*   private void addDevice(List<Device> data){
        Integer rows = deviceMapper.addDevice(data);
        if(rows!=data.size()){
            throw new InsertException("添加设备条数异常");
        }
    }*/
//    @Cache
    @CachePut(value = "Device1", key = "'deviceCode:'+#a0.lineNo+#a0.sn")
    public String addDevice(Device1 device1) {
        device1.setCreateTime(new SimpleDateFormat("YYYY-MM-dd HH:mm").format(new Date()));
        device1.setUpdateTime(new SimpleDateFormat("YYYY-MM-dd HH:mm").format(new Date()));
        Integer rows = deviceMapper.addDevices(device1);
        if (rows != 1) {
            throw new RuntimeException("添加设备异常");
        }
        return device1.getDeviceCode();
    }

    /**
     * 通过Deviceid&lineNo&sn 获取deivceCode
     *
     * @param deviceId
     * @param lineNo
     * @return
     */
    // @Cacheable(value = "Device1", key = "'deviceCode:'+#a1+#a2")
    @Override
    public String selectDeviceCode(String deviceId, String lineNo, String sn) {
        String deviceCode = deviceMapper.selectDeviceCode(deviceId, lineNo, sn);
        if (deviceCode != null || "".equals(deviceCode))
            return deviceCode;
        return null;
    }

    @Override
    public String selectDeviceName(String deviceCode) {
        if (deviceCode == null || deviceCode.equals(""))
            return null;
        String devcieName = deviceMapper.selectDeviceName(deviceCode);
        return devcieName;
    }

    /**
     * 查看设备是否存在
     *
     * @param deviceCode
     * @return
     */
    @Override
    public boolean existDeviceCode(String deviceCode) {

        return deviceMapper.selectId(deviceCode).size() != 0;
    }

    /**
     * 查找lineNo
     *
     * @param deviceCode
     * @return
     */
    @Override
    public String getLineNoByDeviceCode(String deviceCode) {
        return deviceMapper.getDeviceByDeviceCode(deviceCode).getLineNo();

    }

    /**
     * 查询网关sn下的所有设备
     *
     * @param sn
     * @return
     */
//    @Cacheable(key = "'DevicesSN:'+#p0")
    @Override
    public List<Device1> getDevicesBySn(String sn) {
        return deviceMapper.getDevciesBySn(sn);
    }

    /**
     * 查询地址用于拼接warming参数
     *
     * @param deviceCode
     * @return
     */
    @Override
//    @Cacheable(key = "'InstallPlace:'+#a0")
    public String selInstallPlaceByDeviceCode(String deviceCode) {
        if (deviceCode == null || "".equals(deviceCode))
            return null;
        String installPalceCode = deviceMapper.selInstallPlaceCodeByDeviceCode(deviceCode);

        if (installPalceCode == null || "".equals(installPalceCode))
            return null;
        InstallPlace installPlace = installPlaceMapper.getPlace(installPalceCode);
        StringBuilder msg = new StringBuilder();
        msg.append("在")
                .append(installPlace.getInstallPlaceName());
        return msg.toString();
    }

    @Override
    public List<String> getDevicelByDeviceId(String deviceId) {
        return deviceMapper.getDevicelByDeviceId(deviceId);
    }

    @Override
    public Integer getErrLeakValue(String deviceId, String lineId) {
        Integer errLeakValue = 100;
        List<String> devicelByDeviceIds = getDevicelByDeviceId(deviceId);
        for (String devicelByDeviceId : devicelByDeviceIds) {
            //2-1PN_R-5d8dc3404c1d6e1adc048766-J191291284776
            String[] deviceCodesplit = devicelByDeviceId.split("-");
            if (deviceCodesplit[2].equals(lineId)) {
                //根据device的编号获取到ErrLeakValue
                errLeakValue = deviceMapper.getErrLeakValueMapper(devicelByDeviceId);
            }

        }
        return errLeakValue;
    }

    /**
     * 更改数据库中电流动作值
     *
     * @param deviceId
     * @param lineId
     * @return
     */
    @Override
    public Integer updateDeviceErrLeakValue(String deviceId, String lineId, Integer errLeakValue) {
        Integer flag = 0;
        List<String> devicelByDeviceIds = getDevicelByDeviceId(deviceId);
        for (String devicelByDeviceId : devicelByDeviceIds) {
            //2-1PN_R-5d8dc3404c1d6e1adc048766-J191291284776
            String[] deviceCodesplit = devicelByDeviceId.split("-");
            if (deviceCodesplit[2].equals(lineId)) {
                //根据device的编号获取到ErrLeakValue
                flag = deviceMapper.updateErrLeakValueMapper(devicelByDeviceId, errLeakValue);
            }
        }
        return flag;
    }

    /**
     * 使用设备的ID获取到该设备的安装地址
     *
     * @param deviceId
     * @return
     */
    @Override
    public InstallPlace getInstallPlaceValue(String deviceId) {
        return deviceMapper.getInstallPlaceValue(deviceId);
    }

    @Override
    public Integer saveVipDevice(VipDevice vipDevice1) {
        return deviceMapper.saveVipDevice(vipDevice1);
    }

    @Override
    public InstallPlace getInstallPlaceValueByDeviceCoce(String deviceCode) {
        return installPlaceMapper.getInstallPlaceValueByDeviceCoce(deviceCode);
    }

    @Override
    public Boolean selectVipDevice(String deviceCode, String warmingTime) {
        String msg = installPlaceMapper.selectVipDevice(deviceCode, warmingTime);
        return msg != null;
    }

    /**
     * 获取到用户关注的警告信息
     *
     * @param selectUserCode
     * @return
     */
    @Override
    public List<VipDevice> selectVipDeviceByUserCode(String selectUserCode) {
        List<VipDevice> vipDeviceList = deviceMapper.selectVipDeviceByUserCode(selectUserCode);
        return vipDeviceList;
    }

    /**
     * 用户取关
     *
     * @param selectUserCode
     * @param id
     * @return
     */
    @Override
    public Boolean delVipDeviceByUserCodeAndId(String selectUserCode, Integer id) {
        Integer del = deviceMapper.delVipDevice(selectUserCode, id);
        log.info("删除成功" + del.toString());
        return del > 0;
    }
}
