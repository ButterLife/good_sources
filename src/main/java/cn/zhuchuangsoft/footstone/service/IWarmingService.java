package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-19
 */


public interface IWarmingService {

    /**
     * 警告列表的展示
     */
    List<Warming> getAllWarming(Warming warming);

    /**
     * 根据设备编码进行警告列表的展示
     */
    List<Warming> getWarmingByCode(String deviceCode, String startTime, String endTime);

    /**
     * 根据告警编码查看告警详情
     */
    Warming getWarmingByWarmingCode(String warmingCode);

    /**
     * 根据设备编码进行告警类型的展示
     *
     * @param deviceTypeCode
     * @return
     */
    List<WarmingType> getWarmingType(String deviceTypeCode);

    /**
     * 根据设备id&lineId更新highTemp
     *
     * @param
     * @param highTemp
     * @return >0说明修改成功
     */
    String updateHighTemp(String deviceCode, String highTemp);


    /**
     * deviceId 获取 lineNo
     *
     * @param deviceCode
     * @return
     */
    String selectHighTemp(String deviceCode);

    /**
     * 根据用户查询 未处理信息
     *
     * @param deviceCode
     * @return
     */
    List<Warming> selectWarmingByDeviceCode(String deviceCode, String isHandle);

    /**
     * 查询记录是否已存在
     *
     * @param warming
     * @return
     */
    boolean isExsits(Warming warming);

    /**
     * deviceCode 更新heightVoltage
     *
     * @param deviceCode
     * @param heightVoltage
     * @return
     */
    Integer updateHeightVoltage(String deviceCode, String heightVoltage);

    /**
     * deviceCode 更新 lowVoleage
     *
     * @param deviceCode
     * @param lowVoleage
     * @return
     */
    Integer updateLowVoleage(String deviceCode, String lowVoleage);

    /**
     * deviceCode 更新 heightPower
     *
     * @param deviceCode
     * @param heightPower
     * @return
     */
    String updateHeightPower(String deviceCode, String heightPower);

    /**
     * deviceCode 更新 heightCurrent
     *
     * @param deviceCode
     * @param heightCurrent
     * @return
     */
    Integer updateHeightCurrent(String deviceCode, String heightCurrent);

    /**
     * deviceCode 更新 heightLeakage
     *
     * @param deviceCode
     * @param heightLeakage
     * @return
     */
    Integer updateHeightLeakage(String deviceCode, String heightLeakage);

    /**
     * deviceCode 更新 ace
     *
     * @param deviceCode
     * @param ace
     * @return
     */
    String updateARC(String deviceCode, String ace);

    /**
     * 受理
     *
     * @param warming
     * @return
     */
    Integer updateIshandel(Warming warming);


    Integer insertWarming(Warming warming);


    String selectHightPower(String deviceCode);

    Device1 selectDevice1(String deviceCode);

    /**
     * 查询未处理的信息
     *
     * @param deviceCode
     * @return
     */
    List<Warming> selectWarmingIshandle(String deviceCode);


    Integer warmingIsHandle(Integer id, String handleMsg);

    String selectARC(String deviceCode);

    Integer updateEarlyTemp(String deviceCode, Integer temp);

    /**
     * 判读生成温度的的错误信息是否重复
     *
     * @param deviceCode
     * @return
     */
    Boolean getWarmingTempByDeviceCode(String deviceCode, String warmingMsg);

    /**
     * 查看对应的警告信息是否处理过
     *
     * @param deviceCode
     * @return
     */
    Boolean getWarmingByCode(String deviceCode);

    /**
     * 或取到预警值和实时的数据进行对比
     *
     * @param deviceCode
     * @return
     */
    WarmingSetting selectWarmingSetting(String deviceCode);

    int updateWarimingSetting(String deviceCode, Integer under, Integer over);
}
