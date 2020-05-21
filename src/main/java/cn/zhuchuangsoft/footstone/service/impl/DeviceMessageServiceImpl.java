package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.ThreePhase;
import cn.zhuchuangsoft.footstone.mappers.message.PoweredDeviceMapper;
import cn.zhuchuangsoft.footstone.mappers.message.TaizhouGuanMapper;
import cn.zhuchuangsoft.footstone.service.IDeviceMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceMessageServiceImpl implements IDeviceMessageService {

    @Autowired
    private PoweredDeviceMapper poweredDeviceMapper;

    @Autowired
    private TaizhouGuanMapper taizhouGuanMappermapper;

    @Async
    public void insertMessage(ThreePhase threePhase) {
        taizhouGuanMappermapper.insertMessage(threePhase);
        taizhouGuanMappermapper.updateMessage(threePhase);
        //taizhouGuanMappermapper.insertThreePases(threePhase);
    }

    @Override
    public List<PoweredDevice> getMessage(String userCode) {
        return null;
    }
}
