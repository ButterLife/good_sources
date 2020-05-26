package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.Device;
import cn.zhuchuangsoft.footstone.entity.DeviceLine;
import cn.zhuchuangsoft.footstone.entity.Lines;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IDeviceWarmingService;
import cn.zhuchuangsoft.footstone.service.IUserService;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonUtils;
import cn.zhuchuangsoft.footstone.utils.WarmingMsgUtils;
import cn.zhuchuangsoft.footstone.websocket.WebSocketServer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 拉取Jala设备信息和报警信息
 */
@Component
@Slf4j

public class JalaWarmingController extends BaseController {
    @Autowired
    IUserService userServiceImpl;
    @Autowired
    IDeviceService deviceServiceImpl;
    @Autowired
    IWarmingService warmingServiceImpl;
    @Autowired
    private IDeviceWarmingService deviceWarmingService;
    //预警前判读是否已经有告警生成
    private Map<String, Object> warmingMap = new HashMap<>();
    //用来确定一个时间段内是否要插入数据
    // private Boolean flageDoom = true;

    //3分钟开一次门
    //@Scheduled(fixedDelay = 3 * 60 * 1000)
    @Async
    public void isFlageDoom() {
        //flageDoom = true;
    }

    /**
     * 拉取jala设备的时时数据
     * 0/3 * * * * ?  10s拉取一次
     */
    // @Scheduled(cron = "0/50 * * * * ?")
    // @Scheduled(fixedDelay = 10000)//上一次执行完毕时间点3秒再次执行
    @Async
    public void getPullDevice() {
        String authorization = getAuthorization(null);
        try {
            //就是设备上面的id
            String url = "http://ex-api.jalasmart.com/api/v2/devices/" + ID;
            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            //存放device的集合
            List<Device> datas = new ArrayList<>();
            Integer count = data.size();
            for (int i = 0; i < count; i++) {
                JSONObject js = data.getJSONObject(i);
                Device device = new Device(js);
                datas.add(device);
            }
            if (!datas.isEmpty()) {
                for (Device d : datas) {
                    List<String> parameter = new ArrayList<String>();
                    //拿到网关
                    String sn = new String(d.getSn());
                    parameter.add("SN=" + sn);
                    //拿到了数据签名
                    String authorization2 = getAuthorization(parameter);
                    //根据网关获取到设备信息
                    String url2 = "http://ex-api.jalasmart.com/api/v2/devices/" + sn;
                    HttpRequestBase httpRequestBase2 = getRequestByUrl(url2, authorization2, 1);
                    String json2 = getJson(httpRequestBase2);
                    JSONObject jsonObject2 = JSON.parseObject(json2);
                    //根据网关来获取到温度的信息
                    JSONObject data2 = jsonObject2.getJSONObject("Data");
                    Device device = new Device(data2);
                    //获取到jala那边的实时的的数据，这里我们只需要温度的值
                    List<DeviceLine> lines = device.getLines();
                    List<String> massages = new ArrayList<>();
                    for (DeviceLine line : lines) {
                        //拼接deviceCode
                        String deviceCode = 2 + "-" + line.getModel() + "-" + line.getLineID() + "-" + sn;
                       /* if (!deviceServiceImpl.existDeviceCode(deviceCode)) {
                            Device1 device1 = new Device1();
                            device1.setDeviceCode(deviceCode);
                            device1.setDeviceId(d.getDeviceId());
                            device1.setControllerId(d.getControllerId());
                            device1.setSn(sn);
                            device1.setLineNo(line.getLineNo().toString());
                            device1.setDeviceName(line.getName());
                            // devicetype属性
                            DeviceType typeCode = new DeviceType();
                            typeCode.setId(1);
                            typeCode.setDeviceTypeCode("2");
                            typeCode.setDeviceTypeName("集成用电设备");
                            typeCode.setDeviceTypeHistoryTableName("t_Jala_history");
                            typeCode.setDeviceTypeTableName("t_Jala");
                            device1.setTypeCode(typeCode);

                            device1.setMobile("15070304182");
                            device1.setDeviceCity("温州市");
                            device1.setDeviceProvince("浙江省");
                            device1.setDeviceCounty("平阳县");
                            device1.setDeviceAddress("浙江越创电子科技有限公司");
                            device1.setLat("120.56154");
                            device1.setLon("27.613918");
                            device1.setDeviceState("正常");

                            // proxy 代理商编码
                            Proxy proxy = new Proxy();
                            proxy.setId(2);
                            proxy.setProxyName("代理商2");
                            proxy.setProxyMobile("45647645");
                            device1.setProxy(proxy);
                            device1.setOperater("admin");
                            device1.setIsDelete(0);
                            deviceServiceImpl.increaseDevice(device1);
                        }*/
                        String warmingCode2temp = "";
                        // 去自己的数据中拿取到温度的数据
                        String highTemp = warmingServiceImpl.selectHighTemp(deviceCode);
                        if (null != highTemp && !"".equals(highTemp)) {
                            int highTempNum = Integer.parseInt(highTemp);
                            if (line.getModel().startsWith("3P")) {
                                boolean flag = false;
                                // 三相的判断方式推送过温消息
                                if (highTempNum <= line.getTempA()) {
                                    warmingCode2temp += "A相";
                                    flag = true;
                                }
                                if (highTempNum <= line.getTempB()) {
                                    warmingCode2temp += "B相";
                                    flag = true;
                                }
                                if (highTempNum <= line.getTempC()) {
                                    warmingCode2temp += "C相";
                                    flag = true;
                                }
                                if (highTempNum <= line.getTempN()) {
                                    warmingCode2temp += "D相";
                                    flag = true;
                                }
                                if (flag == true) {
                                    warmingCode2temp += "过温";
                                }

                            } else if (line != null && line.getModel().startsWith("1P")) {
                                Double temp = line.getTemp();
                                // 单相的判断方式推送过温消息
                                if (highTempNum <= temp) {
                                    warmingCode2temp = "过温";
                                }
                            } else {
                                // 其他型号。
                            }
                            // 过温信息不为空
                            if (!"".equals(warmingCode2temp)) {
                                handingWarming(deviceCode, warmingCode2temp);
                                // 过温关掉开关
                                //turnOffSwitch(deviceCode);
                            }
                        }
                        // 去自己的数据库中获取到功率对比
                        String highPower = warmingServiceImpl.selectHightPower(deviceCode);
                        if (null != highPower && !"".equals(highPower)) {
                            Double highPowerNum = Double.parseDouble(highPower);
                            Double power = line.getPower();
                            // 判断是否正常
                            if (highPowerNum <= power) {
                                handingWarming(deviceCode, "功率过高");
                                // 功率过高
                                turnOffSwitch(deviceCode);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(fixedDelay = 3 * 60 * 1000)//一分钟清理map
    @Async
    public void clearMap() {
        warmingMap.clear();
    }
    /**
     * 实时拉取去jala的数据，判读是否要生成预警信息
     *
     */
    @Scheduled(fixedDelay = 1800)
    @Async
    public void getPullDeviceMes() throws IOException {
        HttpRequestBase httpRequestBase = null;
        List<Device> datas = getDeviceList(httpRequestBase);
        //拼接device_code
        if (datas.size() > 0) {
            for (Device deviceSn : datas) {
                List<String> parameter = new ArrayList<String>();
                //拿到网关
                String sn = new String(deviceSn.getSn());
                parameter.add("SN=" + sn);
                //拿到了数据签名
                String authorization2 = getAuthorization(parameter);
                //根据网关获取到设备信息
                String url2 = "http://ex-api.jalasmart.com/api/v2/devices/" + sn;
                HttpRequestBase httpRequestBase2 = getRequestByUrl(url2, authorization2, 1);
                String json2 = getJson(httpRequestBase2);
                JSONObject jsonObject2 = JSON.parseObject(json2);
                //获取到该网关下的所有设备
                JSONObject deviceBySn = jsonObject2.getJSONObject("Data");
                Device device = new Device(deviceBySn);
                //获取到jala那边的实时的的数据，这里我们只需要温度的值
                List<DeviceLine> lines = device.getLines();
                for (DeviceLine line : lines) {
                    //拼接device_code查找设备的各个预警值
                    String deviceCode = 2 + "-" + line.getModel() + "-" + line.getLineID() + "-" + sn;
                    if (line.getVoltage() != null) {
                        // log.info("获取生成预警信息当前电压："+line.getVoltage()+"线路ID："+line.getLineID()+"线路名称："+line.getName());
                        //实时电压的数据
                        Double voltage = line.getVoltage();
                        //获取预警的值 里面是已经分了类型了
                        WarmingSetting warmingSetting = warmingServiceImpl.selectWarmingSetting(deviceCode);

                        //判读是是否是带H 的电压
                        String[] modelSplit = line.getModel().split("_");
                        //判读是否是3相的
                        if (line.getModel().contains("3P")) {
                            voltage = line.getVoltageAB();
                        }
                        //带H的电压预警判断
                        if (modelSplit.length >= 2 && modelSplit[1].equals("H")) {
                            // 带H功率的预警
                            Double power = line.getPower();
                            if (power > warmingSetting.getHeightPower()) {
                                //功率过大  生成预警信息
                                voltageWarming(deviceCode, power, "功率预警");
                                continue;
                            }
                            //电压预警
                            if (voltage > 220) {
                                //就获取过压值来比较
                                if (voltage >= warmingSetting.getEarlyHeightVoleage()) {
                                    if (!warmingMap.containsKey(deviceCode)) {
                                        voltageWarming(deviceCode, voltage, "过压预警");
                                        continue;
                                    }
                                }
                            } else {
                                //否则就是带H取欠压预警值比较     //说明没有告警信息生成  2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776 三相欠压预警没有处理
                                if (voltage <= warmingSetting.getEarlyLowVoleage()) {
                                    //说明没有告警信息生成
                                    voltageWarming(deviceCode, voltage, "欠压预警");
                                    continue;
                                }
                            }

                        } else {
                            // 不带H功率的预警
                            if (line.getPower() > warmingSetting.getHeightPower()) {
                                //功率过大，生成功率预警
                                voltageWarming(deviceCode, line.getPower(), "功率预警");
                                continue;
                            }


                            //不带H的值
                            if (voltage > 220) {

                                //就获取过压值来比较
                                if (voltage >= warmingSetting.getEarlyHeightVoleage()) {
                                    //说明没有告警信息生成
                                    if (!warmingMap.containsKey(deviceCode)) {
                                        voltageWarming(deviceCode, voltage, "过压预警");
                                        continue;
                                    }

                                }
                            } else {
                                //否则就取欠压预警值比较
                                if (voltage <= warmingSetting.getEarlyLowVoleage()) {
                                    //说明没有告警信息生成
                                    if (!warmingMap.containsKey(deviceCode)) {
                                        voltageWarming(deviceCode, voltage, "欠压预警");
                                        continue;
                                    }
                                }
                            }
                        }

                    } else {
                        //log.info("预警的没有获取到电压值");
                    }
                }
            }
        }


    }

    /**
     * 电压预警发送消息
     *
     * @param deviceCode
     * @param voltage
     */
    private void voltageWarming(String deviceCode, Double voltage, String warmingCode) {
        Warming warming = new Warming();
        warming.setWarmingCode(warmingCode);
        warming.setDeviceCodes(deviceCode);
        warming.setHandleMsg(voltage.toString());
        if ("功率预警".equals(warmingCode)) {
            warming.setHandleMsg(warming.getHandleMsg() + "瓦");
            log.info("当前功率：" + warming.getHandleMsg());
        }

        warming.setWarmingTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //2020年05月21日18时43分，在鳌江新客运中心线路1发生电弧预警
        //设置电压预警的信息   时间+地点+预警信息+现在的电压
        warming = WarmingMsgUtils.getWarmingMsg(warming);
        // 判断是否需要插入
        if (warmingServiceImpl.isExsits(warming)) {
            return;
        }
        warmingServiceImpl.insertWarming(warming);
    }

    /**
     * 处理产生的warming
     *
     * @return
     */
    private void handingWarming(String deviceCode, String warmingCode) {
        Warming warming = new Warming();
        warming.setDeviceCodes(deviceCode);
        warming.setWarmingTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        warming.setWarmingCode(warmingCode);
        //产生一条数据
        warming = WarmingMsgUtils.getWarmingMsg(warming);
        log.info(warming.getWarmingMsg());
        String war = warming.getWarmingMsg();
        Boolean flag = warmingServiceImpl.getWarmingTempByDeviceCode(deviceCode, war);
        if (!flag) {
            warmingServiceImpl.insertWarming(warming);
        } else {
        }


    }

    /**
     * 关闭开关
     *
     * @param deviceCode
     */
    private void turnOffSwitch(String deviceCode) {
        if (deviceCode == null && "".equals(deviceCode)) {
            return;
        }
        Device1 d = warmingServiceImpl.selectDevice1(deviceCode);

        String lineNo = deviceServiceImpl.getLineNoByDeviceCode(deviceCode);
//        String lineNo = deviceCode.split("-")[1];
        int lineNoNum = Integer.parseInt(lineNo);
        List<String> switchParameter = new ArrayList<String>();
        switchParameter.add("ControllerID=" + d.getControllerId());
        Lines switchline = new Lines(lineNoNum, 0);
        List<Lines> lineList = new ArrayList<>();
        lineList.add(switchline);
        switchParameter.add("Lines=" + JSONArray.toJSONString(lineList));
        String switchAuthorization = getAuthorization(switchParameter);
        String setSwitch = "http://ex-api.jalasmart.com/api/v2/status/" + d.getControllerId();
        HttpPut put = null;
        try {
            put = (HttpPut) getRequestByUrl(setSwitch, switchAuthorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("ControllerID", d.getControllerId());
            JSONObject bodyJson2 = new JSONObject(true);
            bodyJson2.put("LineNo", lineNoNum);
            bodyJson2.put("Status", 0);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(bodyJson2);
            bodyJson.put("Lines", jsonArray);
            put.setEntity(new StringEntity(bodyJson.toJSONString()));
            getJson(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取jala报警信息
     * 定时异步
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(fixedDelay = 1500)//上一次执行完毕时间点1秒再次执行
    @Async
    public void getPullWarming() {
        try {
            //时间的格式化
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 获取对应的网关下的所有的设备告警信息
            List<String> parameter = new ArrayList<String>();
            // parameter.add("ControllerID=" + d.getControllerId());
            parameter.add("ControllerID=" + "5e0955c013d81a2d60137ea5");
            parameter.add("Page=" + 0);
            parameter.add("Limit=" + 10);
            String authorization2 = getAuthorization(parameter);
            String url2 = "http://ex-api.jalasmart.com/api/v2/messages/5e0955c013d81a2d60137ea5/" + 0 + "/" + 10;
            //String url2 = "http://ex-api.jalasmart.com/api/v2/messages/" + d.getControllerId() + "/" + 0;
            HttpRequestBase httpRequestBase2 = getRequestByUrl(url2, authorization2, 1);
            String json2 = getJson(httpRequestBase2);
            JSONObject jsonObject2 = JSON.parseObject(json2);
            //JSONArray data2 = jsonObject2.getJSONArray("Data");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("Data");
            JSONArray data2 = jsonObject3.getJSONArray("Messages");
            if (data2.size() > 0) {
                for (int i = 0; i < data2.size(); i++) {
                    // 用warming封装
                    // String deviceId = d.getDeviceId();
                    String deviceId = "5e1e9c8d4c1d6d2650e9aceb";
                    Integer lineNo = data2.getJSONObject(i).getInteger("LineNo");
                    long createTime = data2.getJSONObject(i).getDate("AddTime").getTime();
                    //code是判读是哪种设备故障
                    String code = purseCode(data2.getJSONObject(i).getString("Code"));
                    // code为空 是我们处理的警告。不需要拉
                    if ("".equals(code))
                        continue;
                    // 离线的警报 暂定为sn下所有的设备都创建一条报警信息。
                    if ("离线".equals(code)) {
//                            outLineWarming(d.getSn(),createTime);
                        if (warmingTimeAndNowDate(dateFormat.format(createTime), dateFormat)) {
                            continue;
                        }
                        outLineWarming("J191291284776", createTime);
                        continue;
                    }

                    String deviceCode = "";
                    Warming warming = new Warming();
                    if (lineNo != null) {
//                            String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo.toString(),d.getSn());
                        deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo.toString(), "J191291284776");
                        warming.setDeviceCodes(deviceCode);
                    }

                    // 电弧预警的拦截，
                    if ("电弧预警".equals(code) && !"".equals(deviceCode)) {
                        String arc = warmingServiceImpl.selectARC(deviceCode);
                        if (!"1".equals(arc))
                            continue;
                    }

                    //设置创建的时间
                    warming.setWarmingTime(dateFormat.format(new Date(createTime)));
                    warming.setWarmingCode(code);

                    warming = WarmingMsgUtils.getWarmingMsg(warming);
                    //判读创建的警告是否是在在当前时间的前半个小时
                    if (warmingTimeAndNowDate(warming.getWarmingTime(), dateFormat)) {
                        continue;
                    }
                    // 判断是否需要插入
                    if (warmingServiceImpl.isExsits(warming)) {
                        continue;
                    }
                    warmingMap.put(warming.getDeviceCodes(), warming);
                    warmingServiceImpl.insertWarming(warming);
                }
            }
//            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 对时间是否将过去的生成的的警告推送给前端并插入数据库中
     *
     * @param warmingTime
     * @param dateFormat
     * @return
     */
    private boolean warmingTimeAndNowDate(String warmingTime, SimpleDateFormat dateFormat) {
        //获取到创建的时间和现在的时间进行对比 ，看是否要插入数据库中
        //1.获取当前系统时间-30分钟的的时间戳
        long nowDate = new Date().getTime() - 30 * 60 * 1000;
        try {
            Date parse = dateFormat.parse(warmingTime);
            //2.对比时间
            if (nowDate > parse.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理离线警告
     *
     * @param sn
     * @param createTime
     */
    private void outLineWarming(String sn, long createTime) {
        List<Device1> devices = deviceServiceImpl.getDevicesBySn(sn);
        for (Device1 device : devices) {
            // 使用离线的数据
            Warming warming = new Warming();
            warming.setDeviceCodes(device.getDeviceCode());
            warming.setWarmingCode("离线");
            warming.setWarmingTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(createTime)));
            StringBuilder msg = new StringBuilder();

            String installPlace = deviceServiceImpl.selInstallPlaceByDeviceCode(device.getDeviceCode());
            msg.append(new SimpleDateFormat("yyyy年MM月dd日HH时mm分，").format(createTime))

//                    .append("发生<span style=font-size:16px;color:red;font-weight:bold>")
//                    .append(device.getDeviceName())
//                    .append("</span>")
                    .append(installPlace)
                    .append(device.getDeviceName())
                    .append("发生")
//                    .append("发生<span style=font-size:16px;color:red;font-weight:bold>")
                    .append("离线");
//                    .append("</span>");

            warming.setWarmingMsg(msg.toString());
            if (warmingServiceImpl.isExsits(warming))
                continue;
            warmingServiceImpl.insertWarming(warming);
        }
    }

    /**
     * 处理code对应警告
     *
     * @param code
     * @return
     */
    String purseCode(String code) {
        String purseCode = "";
        if ("HV".equals(code)) {
            purseCode = "过压告警";
        }
        if ("LV".equals(code)) {
            purseCode = "欠压告警";
        }
        if ("HC".equals(code)) {
            purseCode = "过流告警";
        }
        if ("LEAK".equals(code)) {
            purseCode = "漏电告警";
        }
        if ("Err_HV".equals(code)) {
            purseCode = "过压故障";
        }
        if ("Err_LV".equals(code)) {
            purseCode = "欠压故障";
        }
        if ("Err_HC".equals(code)) {
            purseCode = "过流故障";
        }
        if ("Err_LEAK".equals(code)) {
            purseCode = "漏电故障";
        }
        if ("OFFLINE".equals(code)) {
            purseCode = "离线";
        }
        if ("TEST".equals(code)) {
            purseCode = "漏电自检";
        }
        if ("Err_ARC".equals(code)) {
            purseCode = "电弧故障";
        }
        if ("Test_ARC".equals(code)) {
            purseCode = "电弧探测器自检";
        }
        if ("Key_TEST".equals(code)) {
            purseCode = "三相按键测试";
        }
        if ("TRANS".equals(code)) {
            purseCode = "短路故障";
        }
        if ("OPEND".equals(code)) {
            purseCode = "缺相故障";
        }
        if ("HIGH_ARC".equals(code)) {
            purseCode = "电弧预警";
        }
        if ("Err_HT".equals(code)) {
            purseCode = "温度过高";
        }
        return purseCode;
    }

    /**
     * 获取实时数的公共部分
     *
     * @param httpRequestBase 请求类
     * @return
     */
    public List<Device> getDeviceList(HttpRequestBase httpRequestBase) {
        String authorization = getAuthorization(null);
        //就是设备上面的id
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + ID;
        List<Device> datas = null;
        try {
            httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            //存放device的集合
            datas = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JSONObject deviceJs = data.getJSONObject(i);
                //解析json数据
                Device device = new Device(deviceJs);
                datas.add(device);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
    }

}
