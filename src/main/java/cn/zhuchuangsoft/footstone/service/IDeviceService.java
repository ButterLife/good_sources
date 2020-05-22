package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.Area;
import cn.zhuchuangsoft.footstone.entity.DeviceParams;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.entity.device.VipDevice;
import cn.zhuchuangsoft.footstone.entity.user.UserAndDevice;

import java.util.List;
import java.util.Map;

public interface IDeviceService {
    /**
     * 根据设备编码查询设备类型
     *
     * @return
     */
    String selectDeviceTypeCode(String deviceCode);

    /**
     * 根据设备编码查询紧急号码
     *
     * @param deviceCode
     * @return
     */
    String selectMoblieByCode(String deviceCode);

    /**
     * 添加设备
     *
     * @param device1
     * @return
     */
    Integer increaseDevice(Device1 device1);

    /**
     * 查询所有设备信息
     *
     * @return
     */
    List<Map<String, Object>> findAllDevice(QueryParameters queryParameters);

    List<Device1> selectDeviceByProxy(String mobile);

    List<Device1> selectDeviceByUser(String mobile);

    /**
     * 根据设备编码获取某一个设备
     */
    Map getDeviceByDeviceCode(String sn);

    /**
     * 根据设备编码获取某一个设备
     */
    Device1 DeviceByDeviceCode(String sn);

    /**
     * 修改设备信息
     */
    Integer updateDevice(Device1 device1);

    /**
     * 批量删除设备
     */
    Integer deleteManages(List list);


    /**
     * 获取设备类型编码
     */
    List<DeviceType> getAllDeviceType();


    /**
     * 查询省
     */
    List<Area> selectProvince();


    /**
     * 通过deviceId&lineNo获取lineId
     *
     * @return
     */
    String selectLineId();

    /**
     * 通过DeviceID&&lineNo 获取DeviceId
     * 旗下方法sn用假数据，J191291284776 以后这个方法就用deviceCode一个参数
     *
     * @param dievceId
     * @param lineNo
     * @return
     */
    String selectDeviceCode(String dievceId, String lineNo, String sn);

    String selectDeviceName(String deviceCode);

    /**
     * 查看设备 是否存在
     *
     * @param deviceCode
     * @return
     */
    boolean existDeviceCode(String deviceCode);

    /**
     * 查找lineNo
     *
     * @param deviceCode
     * @return
     */
    String getLineNoByDeviceCode(String deviceCode);

    /**
     * 查询网关sn下的所有设备
     *
     * @param sn
     * @return
     */
    List<Device1> getDevicesBySn(String sn);

    /**
     * 查询地址用于拼接warming参数
     *
     * @param deviceCode
     * @return
     */
    String selInstallPlaceByDeviceCode(String deviceCode);

    List<String> getDevicelByDeviceId(String deviceId);

    /**
     * 获取到设置动作的值的
     *
     * @param deviceId
     * @param lineId
     * @return
     */
    Integer getErrLeakValue(String deviceId, String lineId);

    Integer updateDeviceErrLeakValue(String deviceId, String lineId, Integer errLeakValue);

    /**
     * 获取到设备的安装地址
     *
     * @param deviceId
     * @return
     */
    InstallPlace getInstallPlaceValue(String deviceId);

    /**
     * 关注线路警告
     *
     * @param vipDevice1
     * @return
     */
    Integer saveVipDevice(VipDevice vipDevice1);

    /**
     * 根据deviceCode获取安装信息
     *
     * @param deviceCode
     * @return
     */
    InstallPlace getInstallPlaceValueByDeviceCoce(String deviceCode);

    //判读该记录是否已经存再
    Boolean selectVipDevice(String deviceCode, String warmingTime);

    //获取到用户关注的警告
    List<VipDevice> selectVipDeviceByUserCode(String selectUserCode);

    /**
     * 用户取消关注
     *
     * @param selectUserCode
     * @param id
     * @return
     */
    Boolean delVipDeviceByUserCodeAndId(String selectUserCode, Integer id);
}
