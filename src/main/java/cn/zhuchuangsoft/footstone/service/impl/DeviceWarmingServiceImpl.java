package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.constants.Constants;
import cn.zhuchuangsoft.footstone.entity.DeviceLine;
import cn.zhuchuangsoft.footstone.entity.Message;
import cn.zhuchuangsoft.footstone.entity.ShortMessage;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import cn.zhuchuangsoft.footstone.mappers.*;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IDeviceWarmingService;
import cn.zhuchuangsoft.footstone.service.IJalaWarmingService;
import cn.zhuchuangsoft.footstone.service.IZDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.FastJsonUtils;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import cn.zhuchuangsoft.footstone.utils.RedisUtils;
import cn.zhuchuangsoft.footstone.websocket.WebSocketServer;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DeviceWarmingServiceImpl implements IDeviceWarmingService {

    @Autowired
    private IZDeviceWarmingService deviceWarmingService;
    @Autowired
    private DevicesMapper devicesMapper;
    @Autowired
    private ZDeviceWarmingMapper warmingMapper;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IJalaWarmingService jalaWarmingService;
    @Autowired
    private JalaDeviceMapper jalaDeviceMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void checkDeviceWarming(Object o) {
        List typeCodeList = new ArrayList();
        List typeNameList = new ArrayList();
        //验证对象
        if (o instanceof PoweredDevice) {
            PoweredDevice threePhase = (PoweredDevice) o;
            String deviceCode = threePhase.getDeviceCode();
            String deviceTypeCode = deviceService.selectDeviceTypeCode("1-" + deviceCode);
            List<WarmingType> warmingTypeByCode = deviceWarmingService.getWarmingTypeAll(deviceTypeCode);
            if (!warmingTypeByCode.isEmpty()) {
                for (WarmingType w : warmingTypeByCode) {
                    //voltageA
                    if ("A_Voltage".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getVoltageA().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }

                    }
                    if ("B_Voltage".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getVoltageB().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("C_Voltage".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getVoltageC().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("A_Current".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getCurrentA().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("B_Current".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getCurrentB().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("C_Current".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getCurrentC().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("A_Temp".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getTempA().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("B_Temp".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getTempB().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("C_Temp".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getTempC().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }

                    }
                    if ("N_Temp".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getTempN().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }

                    }
                    if ("A_Power".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getPowerA().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }

                    }
                    if ("B_Power".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getPowerB().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("C_Power".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getPowerC().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                    if ("GPRS".equals(w.getParamentName())) {
                        if (Integer.parseInt(w.getVal()) < Integer.parseInt(threePhase.getGPRS().toString())) {
                            typeCodeList.add(w.getWarmingTypeCode());
                            typeNameList.add(w.getWarmingTypeName());
                        }
                    }
                }
            }
            if (!typeCodeList.isEmpty()) {
                Warming warming = new Warming();
                Device1 device = new Device1();
                device.setDeviceCode("1-" + deviceCode);
                warming.setDeviceCode(device);
                warming.setWarmingCode(StringUtils.join(typeCodeList, ","));
                warming.setWarmingMsg(StringUtils.join(typeNameList, ","));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                warming.setWarmingTime(format.format(new Date()));
                warming.setIshandle("0");
                warming.setCurrent(threePhase.getCurrentA() + "");
                warming.setLeak("");
                warming.setVoltage(threePhase.getVoltageA() + "");
                System.out.println("warming:" + warming);

                //发送报警短信
                String moblies = deviceService.selectMoblieByCode("1-" + deviceCode);
                //List<String> d=devicesMapper.getUserListByCode("1-"+deviceCode);
                String[] moblie = moblies.split(",");
                //将错误信息保存到数据库
                int i = warmingMapper.insertWaring(warming);
                for (String m : moblie) {
                    //String msgId = JiGuangApiMethod.sendCode(m);
                    Map<String, Object> map = messageMapper.selectDevicePlaceByCode("1-" + deviceCode);
                    ShortMessage message = new ShortMessage();
                    message.setMobile(m);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    message.setSendTime(format1.format(new Date()));
                    message.setSendType("设备报警提醒");
                    message.setWarmCode(StringUtils.join(typeCodeList, ","));
                    message.setPlace(map.get("INSTALL_PLACE_NAME").toString());
                    messageMapper.insertShortMessage(message);

                    System.out.println(message.getId());
                    //发送WebSocket信息给前端
                    try {
                        Map<String, Object> mapJson = new HashMap<String, Object>();
                        mapJson.put("id", warming.getId());
                        mapJson.put("isWarm", true);
                        String s = JSON.toJSONString(mapJson);
                        WebSocketServer.sendInfo(s, m);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("发送报警信息" + typeNameList.toArray().length);
                try {

                    WebSocketServer.sendInfo(warming.getId() + "", Constants.ROOT_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return;

        }

    }

    @Override
    public int insertJalaDeviceData(List list, String controllerId) {
        List<DeviceLine> deviceLineList = list;
        for (DeviceLine line : deviceLineList) {
            //线路在线状态 1 在线  0 离线
            //if(line.getLineStatus().equals(1)){
            line.setLineID("2-" + line.getLineID());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            line.setUpdateTime(simpleDateFormat.format(new Date()));
            //插入临时数据
            jalaDeviceMapper.insertJalaDeviceData(line);
            //插入历史数据
            jalaDeviceMapper.insertJalaDeviceHistoryData(line);
            // }
        }
        return 0;
    }


    /**
     * @param list
     * @param controllerId
     */
    @Override
    public void insertJalaWarming(List list, String controllerId) {
        List<Map<String, Object>> deviceByControllerId = devicesMapper.getDeviceByControllerId(controllerId);
        List<Message> messageList = list;
        List<String> warmingCodeList = new ArrayList<>();
        for (Message m : messageList) {
            if ("OFFLINE".equals(m.getCode())) {
                Object mobiles = "";
                Object deviceCode = null;
                Warming warming = null;
                for (Map<String, Object> map : deviceByControllerId) {
                    deviceCode = map.get("DEVICE_CODE");
                    mobiles = map.get("MOBILE");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Map<String, Object> time = warmingMapper.selectWarmingByCode(deviceCode.toString());
                    if (time == null) {
                        Warming warming2 = new Warming();
                        Device1 device = new Device1();
                        device.setDeviceCode(deviceCode.toString());
                        warming2.setDeviceCode(device);
                        warming2.setWarmingCode(m.getCode());
                        warming2.setWarmingMsg(m.getContent());
                        warming2.setWarmingTime(simpleDateFormat.format(new Date()));
                        warming2.setIshandle("0");
                        warmingCodeList.add(m.getCode());

                        String surface = deviceWarmingService.selectSurfaceByCode(deviceCode.toString());
                        List<Map<String, Object>> stringObjectMap = deviceWarmingService.selectDeviceDataByCode(surface, deviceCode.toString());
                        for (Map<String, Object> mapObj : stringObjectMap) {
                            if (mapObj.get("enKey").toString().contains("Voltage")) {
                                warming2.setVoltage(mapObj.get("enKey").toString());
                            }
                            if (mapObj.get("enKey").toString().contains("Current")) {
                                warming2.setCurrent(mapObj.get("enKey").toString());
                            }
                            if (mapObj.get("enKey").toString().contains("Leakage")) {
                                warming2.setLeak(mapObj.get("enKey").toString());
                            }
                        }
                        warmingMapper.insertWaring(warming);
                    } else {
                        try {
                            long warmingTime = simpleDateFormat.parse(time.get("warming_time").toString()).getTime();
                            long currentTime = System.currentTimeMillis();
                            long diff = (currentTime - warmingTime) / 1000 / 60;
                            if (diff >= 3) {
                                warming = new Warming();
                                Device1 device = new Device1();
                                device.setDeviceCode(deviceCode.toString());
                                warming.setDeviceCode(device);
                                warming.setWarmingCode(m.getCode());
                                warming.setWarmingMsg(m.getContent());
                                //SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                warming.setWarmingTime(simpleDateFormat.format(new Date()));
                                warming.setIshandle("0");
                                warmingCodeList.add(m.getCode());

                                String surface = deviceWarmingService.selectSurfaceByCode(deviceCode.toString());
                                List<Map<String, Object>> stringObjectMap = deviceWarmingService.selectDeviceDataByCode(surface, deviceCode.toString());
                                for (Map<String, Object> mapObj : stringObjectMap) {
                                    if (mapObj.get("enKey").toString().contains("Voltage")) {
                                        warming.setVoltage(mapObj.get("enKey").toString());
                                    }
                                    if (mapObj.get("enKey").toString().contains("Current")) {
                                        warming.setCurrent(mapObj.get("enKey").toString());
                                    }
                                    if (mapObj.get("enKey").toString().contains("Leakage")) {
                                        warming.setLeak(mapObj.get("enKey").toString());
                                    }
                                }

                                int i = warmingMapper.insertWaring(warming);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                System.out.println(warming);
                String[] moblie = mobiles.toString().split(",");
                for (String m2 : moblie) {
                    Map<String, Object> place = messageMapper.selectDevicePlaceByCode(deviceCode.toString());
                    ShortMessage message = new ShortMessage();
                    message.setMobile(m2);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    message.setSendTime(format.format(new Date()));
                    message.setSendType("设备报警提醒");
                    message.setWarmCode(StringUtils.join(warmingCodeList, ","));
                    message.setPlace(place.get("INSTALL_PLACE_NAME").toString());
                    messageMapper.insertShortMessage(message);
                    try {
                        Map<String, Object> mapJson = new HashMap<String, Object>();
                        mapJson.put("id", warming.getId());
                        mapJson.put("isWarm", true);
                        String s = JSON.toJSONString(mapJson);
                        WebSocketServer.sendInfo(s, m2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                try {

                    WebSocketServer.sendInfo("设备异常", Constants.ROOT_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }


            for (Map<String, Object> map : deviceByControllerId) {
                Object deviceCode = map.get("DEVICE_CODE");
                Object lineNo = map.get("LINE_NO");
                Object controllerID = map.get("Controller_ID");
                Object mobiles = map.get("MOBILE");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (lineNo.equals(m.getLineNo().toString())) {
                    Map<String, Object> time = warmingMapper.selectWarmingByCode(deviceCode.toString());
                    if (time == null) {
                        System.out.println("---lineNo:" + lineNo + "---m.getLineNo()--" + m.getLineNo());
                        Warming warming = new Warming();
                        warming.setWarmingCode(m.getCode());
                        warming.setWarmingMsg(m.getContent());
                        Device1 device = new Device1();
                        device.setDeviceCode(deviceCode.toString());
                        warming.setDeviceCode(device);
                        warming.setIshandle("0");
                        warming.setWarmingTime(simpleDateFormat.format(new Date()));

                        String surface = deviceWarmingService.selectSurfaceByCode(deviceCode.toString());
                        List<Map<String, Object>> stringObjectMap = deviceWarmingService.selectDeviceDataByCode(surface, deviceCode.toString());
                        for (Map<String, Object> mapObj : stringObjectMap) {
                            if (mapObj.get("enKey").toString().contains("Voltage")) {
                                warming.setVoltage(mapObj.get("enKey").toString());
                            }
                            if (mapObj.get("enKey").toString().contains("Current")) {
                                warming.setCurrent(mapObj.get("enKey").toString());
                            }
                            if (mapObj.get("enKey").toString().contains("Leakage")) {
                                warming.setLeak(mapObj.get("enKey").toString());
                            }
                        }

                        String[] moblie = mobiles.toString().split(",");
                        for (String m2 : moblie) {
                            System.out.println("---------发送报警信息" + m2);
                            Map<String, Object> place = messageMapper.selectDevicePlaceByCode(deviceCode.toString());
                            ShortMessage message = new ShortMessage();
                            message.setMobile(m2);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            message.setSendTime(format.format(new Date()));
                            message.setSendType("设备报警提醒");
                            message.setPlace(place.get("INSTALL_PLACE_NAME").toString());
                            messageMapper.insertShortMessage(message);
                        }
                        try {
                            WebSocketServer.sendInfo("设备异常", Constants.ROOT_CODE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int i = warmingMapper.insertWaring(warming);
                    } else {
                        try {
                            long warmingTime = simpleDateFormat.parse(time.get("warming_time").toString()).getTime();
                            long currentTime = System.currentTimeMillis();
                            long diff = (currentTime - warmingTime) / 1000 / 60;
                            //相隔时间大于3分钟再存入
                            if (diff >= 3) {
                                System.out.println("---lineNo:" + lineNo + "---m.getLineNo()--" + m.getLineNo());
                                Warming warming = new Warming();
                                warming.setWarmingCode(m.getCode());
                                warming.setWarmingMsg(m.getContent());
                                Device1 device = new Device1();
                                device.setDeviceCode(deviceCode.toString());
                                warming.setDeviceCode(device);
                                warming.setIshandle("0");
                                warming.setWarmingTime(simpleDateFormat.format(new Date()));

                                String surface = deviceWarmingService.selectSurfaceByCode(deviceCode.toString());
                                List<Map<String, Object>> stringObjectMap = deviceWarmingService.selectDeviceDataByCode(surface, deviceCode.toString());
                                for (Map<String, Object> mapObj : stringObjectMap) {
                                    if (mapObj.get("enKey").toString().contains("Voltage")) {
                                        warming.setVoltage(mapObj.get("enKey").toString());
                                    }
                                    if (mapObj.get("enKey").toString().contains("Current")) {
                                        warming.setCurrent(mapObj.get("enKey").toString());
                                    }
                                    if (mapObj.get("enKey").toString().contains("Leakage")) {
                                        warming.setLeak(mapObj.get("enKey").toString());
                                    }
                                }

                                String[] moblie = mobiles.toString().split(",");
                                for (String m2 : moblie) {
                                    System.out.println("---------发送报警信息" + m2);
                                    Map<String, Object> place = messageMapper.selectDevicePlaceByCode(deviceCode.toString());
                                    ShortMessage message = new ShortMessage();
                                    message.setMobile(m2);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    message.setSendTime(format.format(new Date()));
                                    message.setSendType("设备报警提醒");
                                    message.setPlace(place.get("INSTALL_PLACE_NAME").toString());
                                    messageMapper.insertShortMessage(message);
                                    try {
                                        Map<String, Object> mapJson = new HashMap<String, Object>();
                                        mapJson.put("id", warming.getId());
                                        mapJson.put("isWarm", true);
                                        String s = JSON.toJSONString(mapJson);
                                        WebSocketServer.sendInfo(s, m2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    WebSocketServer.sendInfo("设备异常", Constants.ROOT_CODE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int i = warmingMapper.insertWaring(warming);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }

        }

    }


}
