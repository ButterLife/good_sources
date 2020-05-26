package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.controller.ex.ControlUnsuccessfulException;
import cn.zhuchuangsoft.footstone.controller.ex.ModificationNameException;
import cn.zhuchuangsoft.footstone.entity.Device;
import cn.zhuchuangsoft.footstone.entity.DeviceLine;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.Lines;
import cn.zhuchuangsoft.footstone.entity.device.VipDevice;
import cn.zhuchuangsoft.footstone.entity.device.WarmingSetting;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IUserAndDeviceService;
import cn.zhuchuangsoft.footstone.service.IUserService;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("device")
@Slf4j
public class DeviceController extends BaseController {

    @Autowired
    IDeviceService deviceServiceImpl;
    @Autowired
    IWarmingService warmingServiceImpl;
    @Autowired
    IUserService userServiceImpl;
    @Autowired
    IUserAndDeviceService userAndDeviceServiceImpl;
    //    @GetMapping("get-line-Power")
//    @ApiOperation("获取线路power")
//    public JsonResult<List<Device>> getLinePower(@ApiParam("设备ID") @PathVariable("DeviceID") String deviceId) {
//        List<String> parameter = new ArrayList<String>();
//        parameter.add("DeviceID=" + deviceId);
//        parameter.add("Time=" + new);
//        String authorization = getAuthorization(parameter);
//        String url = "";
//        try {
//            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
//            String json = getJson(httpRequestBase);
//            JSONObject jsonObject = JSON.parseObject(json);
//            JSONArray data = jsonObject.getJSONArray("Data");
//            Set<String> sns = new HashSet<>();
//            Integer count = data.size();
//            for (int i = 0; i < count; i++) {
//                JSONObject js = data.getJSONObject(i);
//                Device device = new Device(js);
//                sns.add(device.getSn());
//            }
//            // 获取每个网关
//            List<Device> deviceLines = new ArrayList<>();
//            for (String sn : sns) {
//                List<String> parameter = new ArrayList<String>();
//                parameter.add("SN=" + sn);
//                authorization = getAuthorization(parameter);
//                url = "http://ex-api.jalasmart.com/api/v2/devices/" + sn;
//                httpRequestBase = getRequestByUrl(url, authorization, 1);
//                json = getJson(httpRequestBase);
//                jsonObject = JSON.parseObject(json);
//                JSONObject devices = jsonObject.getJSONObject("Data");
//                Device device = new Device(devices);
//                deviceLines.add(device);
//            }
//            return new JsonResult<List<Device>>(SUCCESS, deviceLines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new JsonResult<>(FAILED);
//    }
    List<Device> devices = null;

    public static void main(String[] args) {
        String module = "1PN_H";
        System.out.println(module.contains("3P"));
    }

    @PostMapping("set-switch")
    @ApiOperation("控制产品开关")
    public JsonResult<String> setSwitch(@ApiParam("控制器ID") String controllerId, @ApiParam("线路编号") Integer lineNo, @ApiParam("0:关闭 1:开启") Integer status) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("ControllerID=" + controllerId);
        Lines lines = new Lines(lineNo, status);
        List<Lines> lineList = new ArrayList<>();
        lineList.add(lines);
        parameter.add("Lines=" + JSONArray.toJSONString(lineList));
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/status/" + controllerId;
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("ControllerID", controllerId);
            JSONObject bodyJson2 = new JSONObject(true);
            bodyJson2.put("LineNo", lineNo);
            bodyJson2.put("Status", status);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(bodyJson2);
            bodyJson.put("Lines", jsonArray);
            put.setEntity(new StringEntity(bodyJson.toJSONString()));
            String json = getJson(put);
            JSONObject jsonObject = JSON.parseObject(json);
            Integer code = jsonObject.getInteger("Code");
            if (code != 1) {
                throw new ControlUnsuccessfulException("控制失败");
            }
            return new JsonResult<String>(SUCCESS);
        } catch (IOException e) {
            return new JsonResult<String>(FAILED, "控制失败");
        }

    }

    /**
     * 获取line阀值设置的接口
     *
     * @param deviceId
     * @param lineId
     * @return
     */
    @GetMapping("get/line/set")
    @ApiOperation("获取设置")
    public JsonResult<WarmingSetting> setThresholdValue(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId) {

        // 获取设备，取出阀值
        List<String> parameter = new ArrayList<String>();
        parameter.add("SN=" + "J191291284776");
        String authorization = getAuthorization(parameter);
        String url = "";
        url = "http://ex-api.jalasmart.com/api/v2/devices/J191291284776";
        try {
            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject data = jsonObject.getJSONObject("Data");
            JSONArray lines = data.getJSONArray("Lines");
            if (lines.size() == 0)
                return new JsonResult<>(FAILED);

            for (int i = 0; i < lines.size(); i++) {
                JSONObject line = lines.getJSONObject(i);
                if (deviceId != null && lineId != null && deviceId.equals(data.getString("DeviceID")) && lineId.equals(line.getString("LineID"))) {
                    String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, line.getInteger("LineNo").toString(), "J191291284776");
                    WarmingSetting warmingSetting = new WarmingSetting();
                    warmingSetting.setDeviceCode(deviceCode);
                    warmingSetting.setDeviceId(deviceId);
                    warmingSetting.setHeightCurrent(line.getDouble("Max").intValue());
//                    warmingSetting.setHeightVoltage(line.getInteger("Over"));
//                    warmingSetting.setLowVoltage(line.getInteger("Under"));
                    String highTemp = warmingServiceImpl.selectHighTemp(deviceCode);
                    if (highTemp == null) {
                        highTemp = "0";
                    }
                    warmingSetting.setHighTemp(Integer.parseInt(highTemp));
                    warmingSetting.setLineNo(line.getInteger("LineNo"));
                    // warmingSetting.setHeightLeakage(line.getInteger("Err_LeakValue"));
                    //jala那边的接口可能发生的错误，所以我改变了表的结构来满足我们的需求
                    Integer err_LeakValue = deviceServiceImpl.getErrLeakValue(deviceId, lineId);
                    warmingSetting.setHeightLeakage(err_LeakValue);
                    String arc = warmingServiceImpl.selectARC(deviceCode);
                    warmingSetting.setArc(arc);
                    warmingSetting.setLineId(lineId);
                    warmingSetting.setName(line.getString("Name"));
                    // String highPower = warmingServiceImpl.selectHightPower(deviceCode);
                    WarmingSetting selectWwarmingSetting = warmingServiceImpl.selectWarmingSetting(deviceCode);
                    //自己取数据查看电压预警值
                    warmingSetting.setHeightVoltage(selectWwarmingSetting.getEarlyHeightVoleage());
                    warmingSetting.setLowVoltage(selectWwarmingSetting.getEarlyLowVoleage());
                    //获取到自己设置的功率
                    if (selectWwarmingSetting.getHeightPower() != null && !"".equals(selectWwarmingSetting.getHeightPower()))
                        warmingSetting.setHeightPower(selectWwarmingSetting.getHeightPower());

                    return new JsonResult<WarmingSetting>(SUCCESS, warmingSetting);
                }
            }


            return new JsonResult<WarmingSetting>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }

    @PostMapping("set/device/name")
    @ApiOperation("设置设备名称")
    public JsonResult<String> setDeviceName(@ApiParam("设备ID") String deviceId, @ApiParam("设备名称") String name) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("Name=" + name);
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/name";
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("DeviceID", deviceId);
            bodyJson.put("Name", name);
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            JSONObject jsonObject = JSON.parseObject(getJson(put));
            if (jsonObject.getInteger("Code") != 1) {
                throw new ModificationNameException("修改设备名称失败 ");
            }
            return new JsonResult<String>(SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<String>(FAILED, "修改设备名称失败");
    }

    @ApiOperation("获取设备警告信息")
    @GetMapping("get-warning/{userName}/page/{page}")
    public JsonResult<List<Warming>> getWarning(@PathVariable("userName") @ApiParam("用户名字") String userName, @PathVariable("page") @ApiParam("分页，每页最大100条数据，从0开始") Integer page, @ApiParam("是否处理过警告") String isHandle) {
        String userCode = userServiceImpl.selectUserCode(userName);
        if (("全部").equals(isHandle)) {
            isHandle = null;
        }
        if (userCode != null) {
            List<String> deivceCodes = userAndDeviceServiceImpl.selectDeviceCodeByUserCode(userCode);
            if (deivceCodes.size() > 0) {
                List<Warming> warmings = new ArrayList<>();
                for (String deviceCode : deivceCodes) {
                    List<Warming> warmings1 = warmingServiceImpl.selectWarmingByDeviceCode(deviceCode, isHandle);
                    if (warmings1.size() > 0)
                        warmings.addAll(warmings1);
                }
                return new JsonResult<List<Warming>>(SUCCESS, warmings);
            }
        }
        return new JsonResult<>(FAILED);
    }

    @GetMapping("set/lines/name")
    @ApiOperation("修改线路名称")
    public JsonResult<String> setLineName(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("线路名称") String name) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("LineID=" + lineId);
        parameter.add("Name=" + name);
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/name";
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("DeviceID", deviceId);
            bodyJson.put("LineID", lineId);
            bodyJson.put("Name", name);
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            JSONObject jsonObject = JSON.parseObject(getJson(put));
            if (jsonObject.getInteger("Code") != 1) {
                throw new ModificationNameException("修改线路名称失败");
            }
            return new JsonResult<>(SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "修改线路名称失败");
    }

    @PostMapping("set/line/max-current")
    @ApiOperation("设置最大电流")
    public JsonResult<String> setMaxCurrent(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("最大电流,大于6小于63") int max, @ApiParam("超过最大电流持续多久断开，0-60秒") int duration) {
        //查设备Code信息数据  数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
        List<String> devicelByDeviceIds = deviceServiceImpl.getDevicelByDeviceId(deviceId);
        // System.out.println("我已经得到数据了：" + devicelByDeviceIds.size());
        if (devicelByDeviceIds.size() > 0) {
            String deviceCode = null;
            //数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
            for (String devicelByDeviceId : devicelByDeviceIds) {
                String[] devicelByDeviceIdSplit = devicelByDeviceId.split("-");
                //获取到当前的这个设备型号
                if (devicelByDeviceIdSplit[2].equals(lineId)) {
                    String[] typeSplit = devicelByDeviceIdSplit[1].split("_");
                    if (typeSplit.length > 1 && "H".equals(typeSplit[1])) {
                        if (max > 63 || max < 6) {
                            return new JsonResult<String>(FAILED, "该版本型号电流设置范围为：6--63");
                        }
                        break;
                    } else {
                        if (max > 40 || max < 1) {
                            return new JsonResult<String>(FAILED, "该版本型号电流设置范围为：1--40");
                        }
                        break;
                    }
                }
            }
            List<String> parameter = new ArrayList<String>();
            parameter.add("DeviceID=" + deviceId);
            parameter.add("LineID=" + lineId);
            parameter.add("Max=" + max);
            parameter.add("Duration=" + duration);
            String authorization = getAuthorization(parameter);
            String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/max";
            try {
                HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
                JSONObject bodyJson = new JSONObject(true);
                bodyJson.put("DeviceID", deviceId);
                bodyJson.put("LineID", lineId);
                bodyJson.put("Max", max);
                bodyJson.put("Duration", duration);
                put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
                JSONObject jsonObject = JSON.parseObject(getJson(put));
                //这里需要修改，但是需要进行类型判读
                if (jsonObject.getInteger("Code") != 1) {
                    //throw new ModificationNameException("修改最大电流失败");
                    return new JsonResult<String>(SUCCESS, "设备电流修改成功");
                }
                return new JsonResult<String>(SUCCESS, "设备电流修改成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JsonResult<String>(FAILED, "没有该设备，设备ID为：" + deviceId);
    }

    @PostMapping("set/line/voltage")
    @ApiOperation("设置过压和欠压值")
    public JsonResult<String> setVoltage(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("欠压值 范围175-205") Integer under, @ApiParam("过压值 范围235-265") Integer over) {
        //判读是否超过电压值
        String[] deviceIdSplit = deviceId.split("_");

        String deviceCode = null;
        //获取到deviceCode
        List<String> devicelByDeviceIds = deviceServiceImpl.getDevicelByDeviceId(deviceIdSplit[0]);
        if (devicelByDeviceIds.size() > 0) {
            String sa = "5e1e9c8d4c1d6d2650e9aceb";
            for (String devicelByDeviceIdGetDeviceCode : devicelByDeviceIds) {
                deviceCode = devicelByDeviceIdGetDeviceCode;
                //数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
                String[] deviceCodeSplit = devicelByDeviceIdGetDeviceCode.split("-");
                if (deviceCodeSplit.length >= 2 && lineId.equals(deviceCodeSplit[2])) {
                    String[] typeSplit = deviceCodeSplit[1].split("_");
                    if (typeSplit.length >= 2 && "H".equals(typeSplit[1])) {

                        if (deviceIdSplit.length >= 2 && "O".equals(deviceIdSplit[1])) {
                            if (over > 280 || over < 270) {
                                return new JsonResult<String>(FAILED, "-过压范围：255--270");
                            }
                        } else {
                            if (under < 160 || under > 170) {
                                return new JsonResult<String>(FAILED, "-欠压范围：175--190");
                            }
                        }

                    } else {
                        //不带H的
                        //return new JsonResult<String>(FAILED, "该版本型号电压欠压值范围：175--205，过压值范围：235--265");
                        //sb.append("该版本型号电压欠压值范围：175--205，过压值范围：235--265")
                        //2-1P-5dfc8e804c1d6e27a4f28a91-J191291284776
                        if (deviceIdSplit.length >= 2 && "O".equals(deviceIdSplit[1])) {
                            if (over > 250 || over < 220) {
                                return new JsonResult<String>(FAILED, "-过压压范围：220--250");

                            }
                        } else {

                            if (under < 180 || under > 220) {
                                return new JsonResult<String>(FAILED, "-欠压范围：180--220");
                            }
                        }
                    }
                    break;
                }
            }
            //修改失败时，就重试5次
            for (int i = 0; i < 5; i++) {
                //设置jala那边的电压值
                if (setJalaVoltage(lineId, under, over, deviceIdSplit[0])) {
                    continue;
                } else {
                    break;
                }
            }
            if (deviceCode != null) {
                //更改数据库中的预警值
                int update = warmingServiceImpl.updateWarimingSetting(deviceCode, under, over);
                if (update > 0) {
                    return new JsonResult<String>(SUCCESS, "电压设置成功");
                } else {
                    return new JsonResult<String>(FAILED, "电压设置失败");
                }
            }

           /* } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
        return new JsonResult<String>(FAILED, "没有该设备：设备ID为：" + deviceId);
    }

    /**
     * 设置jala那边的电压数据
     *
     * @param lineId
     * @param under
     * @param over
     * @param s
     * @return
     */
    private boolean setJalaVoltage(String lineId, Integer under, Integer over, String s) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + s);
        parameter.add("LineID=" + lineId);
        parameter.add("Under=" + under);
        parameter.add("Over=" + over);
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + s + "/lines/" + lineId + "/under";

        JSONObject jsonObject = null;
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("DeviceID", s);
            bodyJson.put("LineID", lineId);
            bodyJson.put("Under", under);
            bodyJson.put("Over", over);
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            jsonObject = JSON.parseObject(getJson(put));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        //跟新或者修改数据

        if (jsonObject.getInteger("Code") != 1) {
            return true;
        }
        return false;
    }

    @PostMapping("set/line/enabled")
    @ApiOperation("锁定手动，只有单相的需要使用手柄锁定。")
    public JsonResult<Void> setEnabled(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("锁定手动是否开启 0:关闭 1:开启") Integer enabled) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("LineID=" + lineId);
        parameter.add("Enabled=" + enabled);
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/enabled";
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("DeviceID", deviceId);
            bodyJson.put("LineID", lineId);
            bodyJson.put("Enabled", enabled);
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            JSONObject jsonObject = JSON.parseObject(getJson(put));
            if (jsonObject.getInteger("Code") != 1) {
                throw new ModificationNameException("设置手动开关失败");
            }
            return new JsonResult<>(SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }

    @PostMapping("set/line/leak")
    @ApiOperation("设置漏电预警值，需要根据isLeakage字段判断是否可以设置该值")
    public JsonResult<String> setLeak(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("漏电预警值 单位mA") Integer leakValue) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("LineID=" + lineId);
        parameter.add("LeakValue=" + leakValue);
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/leak";
        try {
            HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("DeviceID", deviceId);
            bodyJson.put("LineID", lineId);
            bodyJson.put("LeakValue", leakValue);
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            JSONObject jsonObject = JSON.parseObject(getJson(put));
            if (jsonObject.getInteger("Code") != 1) {
                return new JsonResult<String>(FAILED, jsonObject.getString("Message") + ",设置范围：0<预警值<动作值");
            }
            return new JsonResult<>(SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);


        //return new JsonResult<>(SUCCESS);
    }

    @PostMapping("set/line/err-leak")
    @ApiOperation("设置漏电动作值，需要根据isLeakage字段判断是否可以设置该值,等于2可以设置")
    public JsonResult<String> setErrLeak(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId, @ApiParam("漏电动作值 单位mA") Integer errLeakValue) throws IOException {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("LineID=" + lineId);
        String authorization = getAuthorization(parameter);
        String url = "";
        String mes = "";
        // 根据型号进行区分，（将来会进行将包装类加入 deviceCode ,以后只传递deviceCode）
        url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId;
        HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
        String json = getJson(httpRequestBase);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject("Data");
        DeviceLine deviceLine = new DeviceLine(data);
        if (deviceLine.getLeakValue() > errLeakValue || errLeakValue > 300) {
            mes = "动作值小于预警值，范围：" + deviceLine.getLeakValue() + "---300";
            return new JsonResult<String>(FAILED, mes);
        }
        //更改数据库中的数据
        Integer flag = deviceServiceImpl.updateDeviceErrLeakValue(deviceId, lineId, errLeakValue);
        if (flag > 0) {
            mes = "设置成功";
        }
        return new JsonResult<String>(SUCCESS, mes);
    }


    public JsonResult<String> setTemp(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineNo, @ApiParam("过温动作值 单位℃(暂定限制<=200)") Integer temp) {

        String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo, "J191291284776");
        Integer updataTemp = warmingServiceImpl.updateEarlyTemp(deviceCode, temp);
        if (temp.equals(updataTemp)) {
            return new JsonResult<String>(SUCCESS, "修改成功");
        }
        return new JsonResult<String>(FAILED, "预警值设置失败，范围：小于温度警告值");
    }

    /**
     * 更新：修改LineId为LineNo字段 原因：数据库缺少LineId字段
     *
     * @param deviceId 设备编号
     * @param lineNo   线路编号
     * @param highTemp 过温动作值
     * @return
     */
    @PostMapping("set/line/err-temp")
    @ApiOperation("设置过温动作值，暂定85℃")
    public JsonResult<String> setErrTemp(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineNo, @ApiParam("过温动作值 单位℃(暂定限制<=200)") Integer highTemp) {

        //判断温度是否设置过高，暂定标准<=200
        if (highTemp > 85 || highTemp <= -273.1) {
            return new JsonResult<String>(FAILED, "设置温度过高，请设置温度小于85℃");
        }
        // 修改成功与否反馈
        String highTempStr = String.valueOf(highTemp);
        String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo, "J191291284776");
        String flag = warmingServiceImpl.updateHighTemp(deviceCode, highTempStr);
        if (flag != highTempStr)
            return new JsonResult<String>(FAILED, "修改失败，请设置温度小于85℃");
        return new JsonResult<String>(SUCCESS, "修改成功");
    }

    @PostMapping("set/line/err-power")
    @ApiOperation("设置过高功率")
    public JsonResult<String> setHeightPower(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineNo, @ApiParam("最大功率（power）") Integer highPower) {
        //判断设置功率范围。
        if (highPower == null || highPower <= 0) {
            return new JsonResult(FAILED, "范围不能小于等于0");
        }
        String highPowerStr = highPower.toString();
        // 修改成功与否反馈
        String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo, "J191291284776");
        String flag = warmingServiceImpl.updateHeightPower(deviceCode, highPowerStr);
        if (highPowerStr.equals(flag)) {
            return new JsonResult<String>(SUCCESS, "修改成功");
        }

        return new JsonResult<String>(FAILED);
    }

    /**
     * 设置监测不监测电弧。
     *
     * @param deviceCode
     * @param arc
     * @return
     */
    @PostMapping("set/line/ARC")
    public JsonResult<String> setArc(@ApiParam("deviceCode") String deviceCode, @ApiParam("arc") String arc) {
        if (deviceCode == null && arc == null && "off".equals(arc) && "on".equals(arc))
            return new JsonResult<>(FAILED, "请检查数据");
        String flag = warmingServiceImpl.updateARC(deviceCode, arc);

        if (flag == null || "".equals(flag))
            return new JsonResult<String>(FAILED, "更改失败");
        return new JsonResult<>(SUCCESS, "修改成功");
    }

    /**
     * 受理前端warming消息
     *
     * @param id
     * @param handleMsg
     * @return
     */
    @PostMapping("set/deal-warming")
    @ApiOperation("处理报警信息")
    public JsonResult<Void> dealWarming(@ApiParam("id") String id, @ApiParam("isHandle") String handleMsg) {
//    public JsonResult<Void> dealWarming() {
        if (id == null)
            return new JsonResult<>(FAILED);
        if (null == handleMsg || "".equals(handleMsg)) {
            handleMsg = "未处理";
        }
        int flag = warmingServiceImpl.warmingIsHandle(Integer.parseInt(id), handleMsg);
//        int flag =0;
        if (flag <= 0)
            return new JsonResult<>(FAILED);
        return new JsonResult<>(SUCCESS);
    }

    /**
     * 设置阀值总接口。
     *
     * @param deviceId
     * @param lineId
     * @param lineNo
     * @param model
     * @param max
     * @param under
     * @param over
     * @param errLeakValue
     * @param highTemp
     * @return
     */
    @GetMapping("set/line/all")
    @ApiOperation("设置阀值")
    public JsonResult<String> setThresholdValue(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String lineId,
                                                @ApiParam("漏电动作值 单位mA") Integer lineNo,
                                                @ApiParam("产品型号") String model, @ApiParam("最大电流") Integer max,
                                                @ApiParam("欠压值 ") Integer under, @ApiParam("过压值") Integer over,
                                                @ApiParam("漏电动作值 单位mA") Integer errLeakValue, @ApiParam("温度 单位℃") Integer highTemp, @ApiParam("电弧") String arc) {
        try {
            // 判断设置是否成功
            boolean flag = true;
            String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, lineNo.toString(), "J191291284776");
            StringBuilder sb = new StringBuilder();
            Map<String, Object> lineDetailParameters = new HashMap<>();
            lineDetailParameters.put("DeviceID", deviceId);
            lineDetailParameters.put("LineID", lineId);
            String url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId;
            // 根据产品型号判断线路产品是否为三相（否则为单相） TODO
            if (model.endsWith("H")) {
                url = url + "/threephase";
            }
            String json = getResponseJson(lineDetailParameters, url, 1);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject data = jsonObject.getJSONObject("Data");
            DeviceLine oldInfo = new DeviceLine(data);
            // 1.判断是否设置过载
            if (max < oldInfo.getMax() || max > oldInfo.getMax()) {
                //查设备Code信息数据  数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
                List<String> devicelByDeviceIds = deviceServiceImpl.getDevicelByDeviceId(deviceId);
                if (devicelByDeviceIds.size() > 0) {
                    //数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
                    for (String devicelByDeviceId : devicelByDeviceIds) {
                        String[] devicelByDeviceIdSplit = devicelByDeviceId.split("-");
                        //获取到当前的这个设备型号
                        if (devicelByDeviceIdSplit[2].equals(lineId)) {
                            String[] typeSplit = devicelByDeviceIdSplit[1].split("_");
                            if ("H".equals(typeSplit[1])) {
                                if (max > 63 || max < 6) {
                                    sb.append("-过载：该版本型号电流设置范围为：6--63");
                                    flag = false;
                                }
                            } else if ("R".equals(typeSplit[1])) {
                                if (max > 40 || max < 1) {
                                    sb.append("-过载：该版本型号电流设置范围为：1--40");
                                    flag = false;
                                }
                            }
                        }
                    }
                    List<String> parameter = new ArrayList<String>();
                    parameter.add("DeviceID=" + deviceId);
                    parameter.add("LineID=" + lineId);
                    parameter.add("Max=" + max);
                    parameter.add("Duration=" + 0);
                    String authorization = getAuthorization(parameter);
                    url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/max";

                    HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
                    JSONObject bodyJson = new JSONObject(true);
                    bodyJson.put("DeviceID", deviceId);
                    bodyJson.put("LineID", lineId);
                    bodyJson.put("Max", max);
                    bodyJson.put("Duration", 0);
                    put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
                    JSONObject nowMax = JSON.parseObject(getJson(put));
                    //这里需要修改，但是需要进行类型判读
                    if (nowMax.getInteger("Code") != 1) {
                        flag = false;
                        sb.append("-过载：修改电流失败");
                    }
                }

                // 2.判断是否设置过压和欠压
                if (!under.equals(oldInfo.getUnder()) || !over.equals(oldInfo.getOver())) {
                    List<String> parameter = new ArrayList<String>();

                    if (devicelByDeviceIds.size() > 0) {
                        for (String devicelByDeviceIdGetDeviceCode : devicelByDeviceIds) {
                            //数据库中的数据结构为：2-3PN_H-5DD2476F4C1D6E0EC8AE8622-J191291284776
                            String[] deviceCodeSplit = devicelByDeviceIdGetDeviceCode.split("-");
                            if (lineId.equals(deviceCodeSplit[2])) {
                                String[] typeSplit = deviceCodeSplit[1].split("_");
                                if ("H".equals(typeSplit[1])) {
                                    if (under > 170 || under < 160) {
                                        sb.append("-欠压范围：160--170");
                                        flag = false;
                                    }
                                    if (over > 280 || over < 270) {
                                        sb.append("-过压范围：270--280");
                                        flag = false;
                                    }

                                } else {
                                    //不带H的
//                                    return new JsonResult<String>(FAILED, "该版本型号电压欠压值范围：175--205，过压值范围：235--265");
//                                    sb.append("该版本型号电压欠压值范围：175--205，过压值范围：235--265")
                                    if (under > 170 || under < 160) {
                                        sb.append("-欠压范围：175--205");
                                        flag = false;
                                    }
                                    if (over > 280 || over < 270) {
                                        sb.append("-过压范围：235--265");
                                        flag = false;
                                    }
                                }
                            }
                        }
                        parameter.add("DeviceID=" + deviceId);
                        parameter.add("LineID=" + lineId);
                        parameter.add("Under=" + under);
                        parameter.add("Over=" + over);
                        String authorization = getAuthorization(parameter);
                        url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/under";

                        HttpPut put = (HttpPut) getRequestByUrl(url, authorization, 3);
                        JSONObject bodyJson = new JSONObject(true);
                        bodyJson.put("DeviceID", deviceId);
                        bodyJson.put("LineID", lineId);
                        bodyJson.put("Under", under);
                        bodyJson.put("Over", over);
                        put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
                        JSONObject nowOverAndUnder = JSON.parseObject(getJson(put));
                        if (nowOverAndUnder.getInteger("Code") != 1) {

                            // 通过获取nowOverAndUnder get String Message  判断情况

                            flag = false;
                        }
                    }
                }

                // 3.判断是否设置漏电动作值
                if (oldInfo != null && oldInfo.getErrLeakValue() != null && oldInfo.getIsLeakAge() != null && oldInfo.getIsLeakAge() == 2 && !errLeakValue.equals(oldInfo.getErrLeakValue())) {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("DeviceID", deviceId);
                    parameters.put("LineID", lineId);
                    parameters.put("ErrLeakValue", errLeakValue);
                    url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/err_leak";
                    String responseJsonStr = getResponseJson(parameters, url, 3);
                    jsonObject = JSON.parseObject(responseJsonStr);
                    // 处理返回json数据，并进行判断，返回前端结果
                    if (jsonObject.getInteger("Code") != 1) {
                        // jsonObject get String Message  判断情况
                        flag = false;
                    }
                }

                // 4.设置温度
                //判断温度是否设置过高，暂定标准<=200
                if (highTemp > 85 || highTemp <= -273.1) {
                    sb.append("设置错误，温度范围：-273.1<温度<85");
                    flag = false;
                } else {
                    // 修改成功与否反馈
                    String highTempStr = String.valueOf(highTemp);

                    String nowHighTemp = warmingServiceImpl.updateHighTemp(deviceCode, highTempStr);

                    if (!nowHighTemp.equals(highTempStr)) {
                        sb.append("-温度：设置温度失败。");
                        flag = false;
                    }
                }
                // 电弧
                if (deviceCode == null && arc == null && "off".equals(arc) && "on".equals(arc)) {
                    sb.append("-电弧：请输入有效数据");
                    flag = false;
                }

                String nowArc = warmingServiceImpl.updateARC(deviceCode, arc);

                if (nowArc == null || "".equals(nowArc)) {
                    sb.append("-电弧：请输入有效数据");
                    flag = false;
                }
                //设置不成功，抛出异常
                if (!flag) {
                    return new JsonResult<String>(FAILED, sb.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<String>(SUCCESS, "设置成功");
    }

    /**
     * 设备实时监测数据
     *
     * @param deviceId 设备id
     * @param lineId   线路id
     * @param model    线路型号
     * @return
     */
    @GetMapping("get/lines/model")
    @ApiOperation("实时监控数据")
    public JsonResult<DeviceLine> setThresholdValue(@ApiParam("设备ID") String deviceId, @ApiParam("线路ID") String
            lineId, @ApiParam("型号") String model) {
        List<String> parameter = new ArrayList<String>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("LineID=" + lineId);
        String authorization = getAuthorization(parameter);
        String url = "";
        // 根据型号进行区分，（将来会进行将包装类加入 deviceCode ,以后只传递deviceCode）
        if (model.startsWith("3P")) {
            url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId + "/threephase";
        } else {
            url = "http://ex-api.jalasmart.com/api/v2/devices/" + deviceId + "/lines/" + lineId;
        }
        try {
            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject data = jsonObject.getJSONObject("Data");
            DeviceLine deviceLine = new DeviceLine(data);
            String deviceCode = deviceServiceImpl.selectDeviceCode(deviceId, deviceLine.getLineNo().toString(), "J191291284776");
            String arc = warmingServiceImpl.selectARC(deviceCode);
            if ("1".equals(arc)) {
                //去数据库查看这条数据是否是已经处理过
                Boolean flag = warmingServiceImpl.getWarmingByCode(deviceCode);
                if (flag) {
                    arc = "2";
                }
            }
            deviceLine.setArc(arc);
            return new JsonResult<DeviceLine>(SUCCESS, deviceLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }

    @GetMapping("get/lines/energy")
    @ApiOperation("实时用电量数据")
    public JsonResult<String> getEnergy(@ApiParam("设备ID") String deviceId) {
        Double energy = 0.0;
        DeviceLine deviceLine = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String formatTime = simpleDateFormat.format(new Date());
        List<String> parameter = new ArrayList<>();
        parameter.add("DeviceID=" + deviceId);
        parameter.add("Date=2020-05-23");
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/energy/" + deviceId + "/2020-05-23";
        HttpRequestBase httpRequestBase = null;
        try {
            httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject dataJSONObject = data.getJSONObject(i);
                JSONArray lines = dataJSONObject.getJSONArray("Lines");
                for (int j = 0; j < lines.size(); j++) {
                    JSONObject jsonObjectEnergy = lines.getJSONObject(j);
                    energy += jsonObjectEnergy.getDouble("Energy");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (energy != null) {
            DecimalFormat df = new DecimalFormat("#0.00");
            return new JsonResult<String>(SUCCESS, "获取成功", df.format(energy));
        }
        return new JsonResult<String>(FAILED, "0.0");

    }

    /**
     * 获取用户（现为默认用户）网关下的所有线路
     * 更新：更改传输对象为Device对象吧。理由：获取线路详情需要DeviceId 更新时间 ：2020年4月26日11:06:29
     *
     * @return
     */
/*
    @GetMapping("get-line-all")
    @ApiOperation("获取所有线路")
    public JsonResult<List<Device>> getDeviceLineAll() {

        //以下获取整个网关
        String authorization = getAuthorization(null);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + ID;
        try {
            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            Set<String> sns = new HashSet<>();
            Integer count = data.size();
            for (int i = 0; i < count; i++) {
                JSONObject js = data.getJSONObject(i);
                Device device = new Device(js);
                sns.add(device.getSn());
            }
            // 获取每个网关
            List<Device> devices = new ArrayList<>();
            for (String sn : sns) {
                List<String> parameter = new ArrayList<String>();
                parameter.add("SN=" + sn);
                authorization = getAuthorization(parameter);
                url = "http://ex-api.jalasmart.com/api/v2/devices/" + sn;
                httpRequestBase = getRequestByUrl(url, authorization, 1);
                json = getJson(httpRequestBase);
                jsonObject = JSON.parseObject(json);
                JSONObject devices1 = jsonObject.getJSONObject("Data");
                Device device = new Device(devices1);
                devices.add(device);
            }
            return new JsonResult<List<Device>>(SUCCESS, devices);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }
*/
    @GetMapping("get-line-all")
    @ApiOperation("获取所有线路")
    public JsonResult<List<Device>> getDeviceLineAll(@ApiParam("过滤条件1") String filterAll, @ApiParam("状态条件") String
            statusFilter, @ApiParam("型号") String model) {
        if ("".equals(filterAll)) {
            filterAll = null;
        }
        if ("".equals(statusFilter)) {
            statusFilter = null;
        }
        if ("".equals(model)) {
            model = null;
        }
        //以下获取整个网关
        String authorization = getAuthorization(null);
        String url = "http://ex-api.jalasmart.com/api/v2/devices/" + ID;
        try {
            HttpRequestBase httpRequestBase = getRequestByUrl(url, authorization, 1);
            String json = getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            Set<String> sns = new HashSet<>();
            Integer count = data.size();
            for (int i = 0; i < count; i++) {
                JSONObject js = data.getJSONObject(i);
                Device device = new Device(js);
                sns.add(device.getSn());
            }
            // 获取每个网关
            List<Device> devices = new ArrayList<>();
            for (String sn : sns) {
                List<String> parameter = new ArrayList<String>();
                parameter.add("SN=" + sn);
                authorization = getAuthorization(parameter);
                url = "http://ex-api.jalasmart.com/api/v2/devices/" + sn;
                httpRequestBase = getRequestByUrl(url, authorization, 1);
                json = getJson(httpRequestBase);
                jsonObject = JSON.parseObject(json);
                JSONObject devices1 = jsonObject.getJSONObject("Data");
                Device device = new Device(devices1);
                //使用deviceid来获取到安装地址
                InstallPlace installPlace = deviceServiceImpl.getInstallPlaceValue(device.getDeviceId());
                device.setInstallPlaceName(installPlace.getInstallPlaceName());
                device.setInstallPlaceAddres(installPlace.getInstallPlaceAddress());
                devices.add(device);
            }
            //条件查询
            if (statusFilter != null) {
                devices = filterFilterStatusDevice(devices, statusFilter);
            }
            if (model != null) {
                devices = getDevicesByModel(devices, model);
            }
            if (filterAll != null) {
                devices = filterDevice(devices, filterAll);
            }
            //filterDevice(devices, filterAll, statusFilter, model);
            return new JsonResult<List<Device>>(SUCCESS, devices);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }

    /**
     * 添加关注的警告
     *
     * @return List<Warming>
     */
    @PostMapping("save/line/warming")
    @ApiOperation("保存关注信息")
    public JsonResult<String> getVIPDevice(@ApiParam("设备ID") String deviceCode, @ApiParam("用户名称") String userName, @ApiParam("警告产生的时间") String warmingTime, @ApiParam("警告内容") String warmingMsg, @ApiParam("告警编码") String warmingCode) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = simpleDateFormat.parse(warmingTime);
        warmingTime = simpleDateFormat.format(parse);
        InstallPlace installPlace = deviceServiceImpl.getInstallPlaceValueByDeviceCoce(deviceCode);
        //判读该关注是否已经存在
        Boolean flag = deviceServiceImpl.selectVipDevice(deviceCode, warmingTime);
        if (flag) {
            return new JsonResult<String>(2, "该关注已经存在");
        }
        //获取到usercode
        String selectUserCode = userServiceImpl.selectUserCode(userName);
        VipDevice vipDevice1 = new VipDevice(0, selectUserCode, userName, deviceCode, installPlace.getInstallPlaceName(), installPlace.getInstallPlaceAddress(), warmingMsg, warmingTime, warmingCode);
        Integer save = deviceServiceImpl.saveVipDevice(vipDevice1);
        if (save > 0) {
            return new JsonResult<String>(SUCCESS, "关注成功");
        } else {
            return new JsonResult<String>(FAILED, "关注失败");
        }
    }

    ;

    /**
     * 获取用户的关注的警告信息
     *
     * @param userName 用户名称
     * @return JsonResult<VipDevice>
     */
    @GetMapping("get/line/vipwarming")
    @ApiOperation("获取用户关注的信息")
    public JsonResult<List<VipDevice>> getVipDevice(@ApiParam("用户名称") String userName) {
        //根据userName来获取到关注的设备
        //获取到usercode
        String selectUserCode = userServiceImpl.selectUserCode(userName);
        List<VipDevice> vipDevicesList = deviceServiceImpl.selectVipDeviceByUserCode(selectUserCode);
        if (vipDevicesList != null || vipDevicesList.size() > 0) {
            return new JsonResult<List<VipDevice>>(SUCCESS, vipDevicesList);
        }

        return new JsonResult<List<VipDevice>>(FAILED);
    }

    @PostMapping("del/line/vipwarming")
    @ApiOperation("用户取消关注")
    public JsonResult<String> delVipDevice(String userName, Integer id) {
        String selectUserCode = userServiceImpl.selectUserCode(userName);
        Boolean flag = deviceServiceImpl.delVipDeviceByUserCodeAndId(selectUserCode, id);
        if (flag) {
            return new JsonResult<String>(SUCCESS, "取消关注成功");
        }
        return new JsonResult<String>(FAILED, "取消关注失败");
    }


    /**
     * 添加条件查询时的方法
     *
     * @return
     */
    private List<Device> filterDevice(List<Device> deviceLists, String filterAll) {

        if (filterAll == null) {
            //没有过滤条件就直接返回
            return deviceLists;
        }
        //查询过滤很多条件的
        if (filterAll != null) {
            if (filterAll.equals("三相")) {
                filterAll = "3P";
            }

            devices = new ArrayList<>();
            //获取到设备的集合
            List<DeviceLine> lines = null;
            for (Device device : deviceLists) {
                lines = new ArrayList<>();
                //1.按照按照地名
                if (device.getInstallPlaceAddres().contains(filterAll) || device.getInstallPlaceName().contains(filterAll)) {
                    devices.add(device);
                    continue;
                }
                //2.按照线路名称查询
                for (DeviceLine deviceLine : device.getLines()) {

                    if (deviceLine.getName().equals(filterAll)) {
                        lines.add(deviceLine);
                        //设置当前的线路
                        device.setLines(lines);
                        devices.add(device);
                    }
                }
                //3.线路类型
                for (DeviceLine deviceLine : device.getLines()) {
                    //单项的设计
                    if (filterAll.equals("单相")) {
                        filterAll = "1P";
                    }
                    if (deviceLine.getModel().contains(filterAll)) {
                        lines.add(deviceLine);
                        //设置当前的线路
                        device.setLines(lines);
                        if (filterAll.contains("3P")) {
                            devices.add(device);
                        }
                        if ("1P".contains(filterAll)) {
                            filterAll = "单相";
                        }
                    }
                }
                if (lines.size() > 0 && filterAll.equals("单相")) {
                    devices.add(device);
                }
                //4.设备状态
                if (filterAll.contains("在线") && device.connect) {
                    devices.add(device);
                    continue;
                } else if (filterAll.contains("离线") && !device.connect) {
                    devices.add(device);
                    continue;
                }
            }
        }
        // devices.size()>0条件成立了，说明线路匹配成功了 方法结束
        if (devices != null && devices.size() > 0) {
            return devices;
        }
        //多条件查询最后的判断，是否有真正的数据过滤出来
        if (devices != null && devices.size() > 0) {
            deviceLists = devices;
        }
        return deviceLists;
    }

    /**
     * 根据类型来获取数据
     *
     * @param deviceLists
     * @param filterModel
     * @return
     */
    private List<Device> getDevicesByModel(List<Device> deviceLists, String filterModel) {
        //类型判断
        if (filterModel != null) {
            //根据类型来查看
            for (Device deList : deviceLists) {
                //用来存储新的数据
                List<DeviceLine> newDeviceLine = new ArrayList<DeviceLine>();
                //获取到设备的集合
                List<DeviceLine> lines = deList.getLines();
                //判读线路的状态
                for (DeviceLine line : lines) {
                    //判读是要过滤的值
                    if (line.getModel().contains(filterModel)) {
                        newDeviceLine.add(line);
                    }
                }
                //重新设置过滤后的设备详情
                deList.setLines(newDeviceLine);
            }
            //重新覆盖原来的数据
            deviceLists = deviceLists;
        }
        return deviceLists;
    }

    /**
     * 通过状态获取数据
     *
     * @param deviceLists
     * @param filterStatus
     * @return
     */
    private List<Device> filterFilterStatusDevice(List<Device> deviceLists, String filterStatus) {
        //状态判断
        if (filterStatus != null) {
            devices = new ArrayList<>();
            //查询状态的数据
            for (Device device : deviceLists) {
                //在线判断
                if ("0".equals(filterStatus) && device.connect) {
                    devices.add(device);
                } else if ("1".equals(filterStatus) && !device.connect) {
                    //获取到离线数据推送给前端
                    devices.add(device);
                }
            }
            deviceLists = devices;
        }
        return deviceLists;
    }


}
