package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.ThreePhase;

import java.util.List;

public interface IDeviceMessageService {

    /**
     * 添加老管三相设备详细信息
     * 留做备份
     *
     * @param threePhase
     * @return
     */
    void insertMessage(ThreePhase threePhase);

    List<PoweredDevice> getMessage(String userCode);


}
