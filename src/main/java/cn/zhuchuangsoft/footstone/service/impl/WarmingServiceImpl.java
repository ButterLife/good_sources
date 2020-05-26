package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import cn.zhuchuangsoft.footstone.mappers.*;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonUtils;
import cn.zhuchuangsoft.footstone.utils.WarmingMsgUtils;
import cn.zhuchuangsoft.footstone.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-19
 */

@Service
@Slf4j
public class WarmingServiceImpl implements IWarmingService {

    @Autowired
    UserAndDeviceMapper userAndDeviceMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private WarmingMapper warmingMapper;
    @Autowired
    private WarmingTypeMapper warmingTypeMapper;
    @Autowired
    private WarmingSettingMapper warmingSettingMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public List<Warming> getAllWarming(Warming warming) {
        return warmingMapper.getAllWarming(warming);
    }

    @Override
    public List<Warming> getWarmingByCode(String deviceCode, String startTime, String endTime) {
        return warmingMapper.getWarmingByCode(deviceCode, startTime, endTime);
    }

    @Override
    public Warming getWarmingByWarmingCode(String warmingCode) {
        return warmingMapper.getWarmingByWarmingCode(warmingCode);
    }

    @Override
    public List<WarmingType> getWarmingType(String deviceTypeCode) {
        return warmingTypeMapper.getWarmingType(deviceTypeCode);
    }

    /**
     * 根据设备deviceCode更新highTemp
     *
     * @param deviceCode
     * @param highTemp
     * @return >0说明修改成功
     */
    @CachePut(value = "warmingSetting", key = "'HighTemp:'+#a0")
    @Override
    public String updateHighTemp(String deviceCode, String highTemp) {
        String hingTempOld = warmingSettingMapper.selectHighTemp(deviceCode);
        //判断设备是否存在。
        if (deviceCode == null || "".equals(deviceCode))
            return hingTempOld;
        Integer flag = warmingSettingMapper.updateHighTemp(deviceCode, highTemp);
        if (flag != 1)
            return hingTempOld;
        return highTemp;

    }

    /**
     * deviceId 获取 lineNo
     *
     * @param deviceCode
     * @return
     */
    // @Cacheable(value = "warmingSetting", key = "'HighTemp:'+#a0")
    @Override
    public String selectHighTemp(String deviceCode) {
        String highTemp = warmingSettingMapper.selectHighTemp(deviceCode);
        return highTemp;
    }

    /**
     * 根据用户查询 未处理信息
     *
     * @param deviceCode
     * @return
     */
    @Override
    public List<Warming> selectWarmingByDeviceCode(String deviceCode, String isHandle) {
        return warmingMapper.selectWarmingByUserCode(deviceCode, isHandle);
    }

    /**
     * 查询未处理的信息
     *
     * @param deviceCode
     * @return
     */
    @Override
    public List<Warming> selectWarmingIshandle(String deviceCode) {
        //需要对时间进行对比，看需不需展示警告数据
        List<Warming> warmingList = new ArrayList<>();
        List<Warming> warmings = warmingMapper.selectWarmingIshandel(deviceCode);
        String str = "yyy-MM-dd HH:mm:ss";
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        try {
            for (Warming warming : warmings) {
                time = warming.getWarmingTime();
                Date dates = sdf.parse(time);
                if (dates.getTime() >= new Date().getTime()) {
                    //如果警告创建的时间+30分钟比当前系统时间还要大的就添加数据到集合中
                    warmingList.add(warming);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //最终返回数据
        return warmingList;
    }

    @Override
    public Integer warmingIsHandle(Integer id, String handleMsg) {

        // 创建处理时间。
        String handleTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());

        return warmingMapper.setIsHandle(id, handleMsg, handleTime);
    }

    // @Cacheable(value = "warmingSetting", key = "'ARC:'+#a0")
    @Override
    public String selectARC(String deviceCode) {
        return warmingSettingMapper.selectARC(deviceCode);
    }

    @Override
    public Integer updateEarlyTemp(String deviceCode, Integer temp) {
        Integer tempOld = warmingSettingMapper.selectEarlyTemp(deviceCode);
        Integer flag = warmingSettingMapper.updateEarlyTemp(deviceCode, temp);
        if (flag == null)
            return tempOld;
        return temp;
    }

    /**
     * 判断过温还是功率过大
     *
     * @param deviceCode
     * @param warmingMsg
     * @return Boolean
     */
    @Override
    public Boolean getWarmingTempByDeviceCode(String deviceCode, String warmingMsg) {

        List<Warming> warmings = warmingMapper.getWarmingTempByCode(deviceCode);
        System.out.println(warmings.size());
        for (Warming warming : warmings) {
            //如果这些条件都成立了，说明已经有数据库中有数据了
            System.out.println(warming.getDeviceCodes());
            if (warming.getWarmingCode().equals("过温") && warming.getDeviceCodes().equals(deviceCode) && warming.getWarmingMsg().equals(warmingMsg)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean getWarmingByCode(String deviceCode) {
        List<String> warmings = warmingMapper.getWarmingByCodeType(deviceCode);
        if (warmings.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 或到预警信息的值和实时的数据对比
     *
     * @param deviceCode
     * @return
     */
    @Override
    public WarmingSetting selectWarmingSetting(String deviceCode) {
        return warmingSettingMapper.getWarmingSetting(deviceCode);

    }

    /**
     * 更改数据中的电压预警值
     *
     * @param deviceCode 设备的唯一标识
     * @param under      上压阈值
     * @param over       下压阈值
     * @return
     */
    @Override
    public int updateWarimingSetting(String deviceCode, Integer under, Integer over) {
        return warmingSettingMapper.updateVoltageByDeviceCode(deviceCode, under, over);
    }

    /**
     * 查询记录是否已存在
     * true 存在
     *
     * @param warming
     * @return true 存在
     */
    @Override
    public boolean isExsits(Warming warming) {
        List<Integer> s = warmingMapper.isExsits(warming);
        return s.size() >= 1;
    }

    /**
     * deviceCode 更新heightVoltage
     *
     * @param deviceCode
     * @param heightVoltage
     * @return
     */
    @Override
    public Integer updateHeightVoltage(String deviceCode, String heightVoltage) {

        return null;
    }

    /**
     * deviceCode 更新 lowVoleage
     *
     * @param deviceCode
     * @param lowVoleage
     * @return
     */
    @Override
    public Integer updateLowVoleage(String deviceCode, String lowVoleage) {
        return null;
    }

    /**
     * deviceCode 更新 heightPower
     * 更新失败返回null
     *
     * @param deviceCode
     * @param heightPower
     * @return
     */
    //  @CachePut(value = "warmingSetting", key = "'HighPower:'+#a0")
    @Override
    public String updateHeightPower(String deviceCode, String heightPower) {
        String heightPowerOld = warmingSettingMapper.selectHeightPower(deviceCode);
        if (deviceCode == null || "".equals(deviceCode))
            return heightPowerOld;
        // 检查范围是否合理
        if ("".equals(heightPower)) {
            return heightPowerOld;
        }
        int heightPowerNum = Integer.parseInt(heightPower);
        if (heightPowerNum < 0)
            return heightPowerOld;
        int flag = warmingSettingMapper.updateHeightPower(deviceCode, heightPower);
        if (flag > 0) {
            return heightPower;
        }
        return heightPowerOld;
    }

    /**
     * deviceCode 更新 heightCurrent
     *
     * @param deviceCode
     * @param heightCurrent
     * @return
     */
    @Override
    public Integer updateHeightCurrent(String deviceCode, String heightCurrent) {
        if (deviceCode == null || heightCurrent == null)
            return -1;
        // 获取型号 根据型号来判断是范围,（暂时根据那边api为准，只有1PN_R为1-40A）
        String model = deviceCode.split("-")[1];
        if ("1PN_R".equals(model)) {
            if (Integer.parseInt(heightCurrent) < 1 || Integer.parseInt(heightCurrent) > 40) {
                return -1;
            }
        } else {
            if (Integer.parseInt(heightCurrent) < 1 || Integer.parseInt(heightCurrent) > 63)
                return -1;
        }
        return warmingSettingMapper.updateHeightGurrent(deviceCode, heightCurrent);
    }

    /**
     * deviceCode 更新 heightLeakage ,若没有deviceCode则自动添加
     *
     * @param deviceCode
     * @param heightLeakage
     * @return
     */
    @Override
    public Integer updateHeightLeakage(String deviceCode, String heightLeakage) {
        // 验证heightLeakage范围
        if (deviceCode == null || heightLeakage == null)
            return -1;
        int heightLeakageNum = Integer.parseInt(heightLeakage);
        if (warmingSettingMapper.selectExists(deviceCode) == null)
            warmingSettingMapper.insertWarmingSetting(deviceCode);
        if (heightLeakageNum >= 0)
            return -1;
        return warmingSettingMapper.updateHeightLeakage(deviceCode, heightLeakage);
    }

    /**
     * deviceCode 更新 ace
     *
     * @param deviceCode
     * @param arc
     * @return
     */
    //  @CachePut(value = "warmingSetting", key = "'ARC:'+#a0")
    @Override
    public String updateARC(String deviceCode, String arc) {
        String arcOld = warmingSettingMapper.selectARC(deviceCode);
        if (deviceCode == null || "".equals(deviceCode))
            return arcOld;
        if (arc == null) {
            arc = "off";
        }
        if (warmingSettingMapper.selectExists(deviceCode) == null) {

            if ("on".equals(arc)) {
                arc = "1";
            } else if ("off".equals(arc)) {
                arc = "0";
            } else {
                return arcOld;
            }
            //不存在就插入一条数据
            warmingSettingMapper.insertWarmingSetting(deviceCode);
        } else {
            if ("on".equals(arc)) {
                arc = "1";
            } else if ("off".equals(arc)) {
                arc = "0";
            }
            //存在就修改数据
            warmingSettingMapper.updateACE(deviceCode, arc);
        }
        return arc;
    }

    /**
     * 受理
     *
     * @param warming
     * @return
     */
    @Override
    public Integer updateIshandel(Warming warming) {
        warming.getHandleMsg();
        if (warming.getHandleMsg() == null || warming.getHandleMsg() == null)
            return -1;
        warming.setIshandle("1");
        warming.setWarmingTime(WarmingMsgUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return warmingMapper.updateWarming(warming);
    }

    @Override
    public Integer insertWarming(Warming warming) {
        String voltgte = null;
        if (warming.getHandleMsg() != null) {
            voltgte = warming.getHandleMsg();
            warming.setHandleMsg(null);
        }
        int flag = warmingMapper.insertWarming(warming);
//        int flag=1;
        if (flag >= 1) {

            // 根据设备获取的用户名进行推送
            List<String> userCodes = userAndDeviceMapper.selectUserDevice(warming.getDeviceCodes());
            if (userCodes.size() > 0) {
                try {
                    for (String userCode : userCodes) {
                        String userName = userMapper.selectUserNameByUserCode(userCode);
                        String warmingMsg = warming.getWarmingMsg();
                        //处理强调前端的数据
                        String code = warming.getWarmingCode();
                        String deviceName = deviceMapper.selectDeviceName(warming.getDeviceCodes());

                        warmingMsg = warmingMsg.replace(deviceName, "<span style=font-size:16px;color:red;font-weight:bold>" + deviceName + "</span>");

                        warmingMsg = warmingMsg.replace(code, "<span style=font-size:16px;color:red;font-weight:bold>" + code + "</span>");

                        //将这个warming的数据推送给前端
                        warming.setWarmingMsg(warmingMsg);
                        //插入数据完成之后就给前端推送最新的数据
                        if (voltgte != null) {
                            if (voltgte.contains("瓦")) {
                                StringBuffer sb = new StringBuffer(warming.getWarmingMsg());
                                sb.append("当前功率：" + voltgte);
                                warming.setWarmingMsg(sb.toString());
                            } else {
                                StringBuffer sb = new StringBuffer(warming.getWarmingMsg());
                                sb.append("当前电压：" + voltgte);
                                warming.setWarmingMsg(sb.toString());
                            }
                        }
                        WebSocketServer.sendInfo(JsonUtils.toString(warming), userName);
                        Thread.currentThread().sleep(10);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


    //@Cacheable(value = "warmingSetting", key = "'HighPower:'+#a0")
    @Override
    public String selectHightPower(String deviceCode) {
        return warmingSettingMapper.selectHeightPower(deviceCode);
    }

    // @Cacheable(value = "devcie1", key = "'device:'+#a0")
    @Override
    public Device1 selectDevice1(String deviceCode) {
        if (deviceCode == null || "".equals(deviceCode))
            return null;
        return deviceMapper.getDeviceByDeviceCode(deviceCode);
    }
}
