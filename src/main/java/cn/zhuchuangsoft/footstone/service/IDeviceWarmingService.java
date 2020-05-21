package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.DeviceLine;
import cn.zhuchuangsoft.footstone.entity.Message;
import cn.zhuchuangsoft.footstone.entity.device.ThreePhase;

import java.util.List;

public interface IDeviceWarmingService<T> {


    void checkDeviceWarming(T t);


    void insertJalaWarming(List<Message> list, String ControllerId);

    int insertJalaDeviceData(List<DeviceLine> lines, String controllerId);
}
