package cn.zhuchuangsoft.footstone.controller;

/**
 * @author 阿白
 * @date 2019-12-12
 * @update 2019-12-21
 */

import cn.zhuchuangsoft.footstone.controller.ex.ParameterIsIncorrect;
import cn.zhuchuangsoft.footstone.entity.DeviceParams;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IRoleService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备管理模块
 */
//@RestController
//@RequestMapping("/deviceManage")
public class DeviceManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(DeviceManageController.class);

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IRoleService roleService;

    public static void main(String[] agrs) {
        Device1 device = new Device1();
        device.setImei(null);
        System.out.println(device);
    }

    /**
     * 设备编码类型
     */
    @GetMapping("/deviceType")
    @ApiOperation("设备编码类型")
    public JsonResult<List<DeviceType>> deviceType() {
        List<DeviceType> deviceTypes = deviceService.getAllDeviceType();
        return new JsonResult<List<DeviceType>>(SUCCESS, deviceTypes);
    }

    /**
     * 设备关系管理
     */
    @PostMapping("/add")
    @ApiOperation("添加设备")
    public JsonResult<Void> addDeviceManage(
            @ApiParam(name = "typeCode", value = "设备类型编码") String typeCode,
            @ApiParam(name = "itmet", value = "设备物联网通信编码") String itmet,
            @ApiParam(name = "insyallPleaceCode", value = "设备安装地点集合编码") String insyallPleaceCode,
            @ApiParam(name = "deviceProvince", value = "设备安装所在省") String deviceProvince,
            @ApiParam(name = "deviceCity", value = "设备安装所在市") String deviceCity,
            @ApiParam(name = "deviceCounty", value = "设备安装所在县") String deviceCounty,
            @ApiParam(name = "deviceAddress", value = "设备安装地址（也是设备告警的地址）") String deviceAddress,
            @ApiParam(name = "lat", value = "设备所在经度") String lat,
            @ApiParam(name = "lon", value = "设备所在纬度") String lon,
            @ApiParam(name = "mobile", value = "设备告警电话，多的用逗号隔开") String mobile,
            @ApiParam(name = "deviceState", value = "设备状态（正常，离线，告警）") String deviceState,
            @ApiParam(name = "operater", value = "操作人姓名") String operater,
            @ApiParam(name = "proxyCode", value = "代理商编码") String proxyCode,
            HttpServletRequest request
    ) {
        try {
            User user = (User) request.getAttribute("user");
            DeviceType deviceType = new DeviceType(typeCode);
            InstallPlace installplace = new InstallPlace(insyallPleaceCode);
            Device1 device = new Device1(deviceType, itmet, installplace, deviceProvince, deviceCity, deviceCounty, deviceAddress, lat, lon, mobile, deviceState, operater, proxyCode);
            device.setIsInstall(0);
            device.setDeviceCode(BaseController.getUUID());
            Integer flag = deviceService.increaseDevice(device);
            if (flag != 1) {
                throw new ParameterIsIncorrect("参数添加异常");
            }
            return new JsonResult<>(SUCCESS, "添加成功", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "添加失败", null);
    }

    /**
     * 设备列表展示
     */
    @PostMapping("/all")
    @ApiOperation("设备列表展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceProvince", value = "设备安装所在省", required = false, paramType = "form"),
            @ApiImplicitParam(name = "deviceCity", value = "设备安装所在市", required = false, paramType = "form"),
            @ApiImplicitParam(name = "deviceCounty", value = "设备安装所在县", required = false, paramType = "form"),
            @ApiImplicitParam(name = "deviceAddress", value = "设备安装地址（也是设备告警的地址）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "设备告警电话，多的用逗号隔开", required = false, paramType = "form"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "updateTime", value = "修改时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "deviceState", value = "设备状态（正常，离线，告警）", required = false, paramType = "form"),
    })
    public JsonResult<List<Device1>> getDevice(
            DeviceParams deviceParams,
            @ApiParam("当前页") @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(value = "limit", required = true, defaultValue = "5") Integer limit,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        PageInfo<Device1> pageInfo = null;
        try {
            List<Role> roles = roleService.findByUid(user.getId());
            Role role = roles.get(0);
            pageInfo = PageDisply(deviceParams, page, limit, role, pageInfo, user.getMobile());
            return new JsonResult<List<Device1>>(SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }

    @GetMapping("/get/{deviceCode}")
    @ApiOperation("根据设备编码获取设备")
    public JsonResult<Map> getDeviceManageByCode(
            @ApiParam("设备编码") @PathVariable String deviceCode,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        List<Role> roles = roleService.findByUid(user.getId());
        Role role = roles.get(0);
        if (role.getRoleCode().equals(USER)) {
            return new JsonResult<>(FAILED, "用户权限不足", null);
        }
        Map maps = deviceService.getDeviceByDeviceCode(deviceCode);
        return new JsonResult<>(SUCCESS, maps);

    }

    @PostMapping("/update")
    @ApiOperation("修改设备")
    public JsonResult<Void> updateDeviceManage(
            @ApiParam(name = "deviceCode", value = "设备编码", required = true) String deviceCode,
            @ApiParam(name = "typeCode", value = "设备类型编码") String typeCode,
            @ApiParam(name = "imei", value = "设备物联网通信编码") String imei,
            @ApiParam(name = "insyallPleaceCode", value = "设备安装地点集合编码") String insyallPleaceCode,
            @ApiParam(name = "deviceProvince", value = "设备安装所在省") String deviceProvince,
            @ApiParam(name = "deviceCity", value = "设备安装所在市") String deviceCity,
            @ApiParam(name = "deviceCounty", value = "设备安装所在县") String deviceCounty,
            @ApiParam(name = "deviceAddress", value = "设备安装地址（也是设备告警的地址）") String deviceAddress,
            @ApiParam(name = "lat", value = "设备所在经度") String lat,
            @ApiParam(name = "lon", value = "设备所在纬度") String lon,
            @ApiParam(name = "mobile", value = "设备告警电话，多的用逗号隔开") String mobile,
            @ApiParam(name = "deviceState", value = "设备状态（正常，离线，告警）") String deviceState,
            @ApiParam(name = "operater", value = "操作人姓名") String operater,
            @ApiParam(name = "proxyCode", value = "代理商编码") String proxyCode,
            @ApiParam(name = "isinstall", value = "是否安装") Integer isinstall,
            @ApiParam(name = "deviceId", value = "jala设备网关") String deviceId,
            @ApiParam(name = "deviceId", value = "jala设备控制id") String controllerId,
            @ApiParam(name = "deviceId", value = "jala设备sn码") String sn,
            @ApiParam(name = "deviceId", value = "jala设备lineNo") String lineNo,
            HttpServletRequest request
    ) {
        try {
            User user = (User) request.getAttribute("user");
            List<Role> roles = roleService.findByUid(user.getId());
            Role role = roles.get(0);
            if (role.getRoleCode().equals(USER)) {
                return new JsonResult<>(FAILED, "用户权限不足", null);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Device1 device = deviceService.DeviceByDeviceCode(deviceCode);
            Device1 device = new Device1();
            device.setTypeCode(new DeviceType(typeCode));
            device.setImei(imei);
            //device.setInsyallPleaceCode(new InstallPlace(insyallPleaceCode));
            device.setDeviceProvince(deviceProvince);
            device.setDeviceCity(deviceCity);
            device.setDeviceCounty(deviceCounty);
            device.setDeviceAddress(deviceAddress);
            device.setLat(lat);
            device.setLon(lon);
            device.setMobile(mobile);
            device.setDeviceState(deviceState);
            //device.setProxy(new Proxy(proxyCode));
            device.setOperater(operater);
            device.setUpdateTime(format.format(new Date()));
            device.setIsInstall(isinstall == null ? 0 : isinstall);
            System.out.println("'---------" + device);
            deviceService.updateDevice(device);
            return new JsonResult<>(SUCCESS, "修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "修改失败", null);
    }

    @PostMapping("/delete")
    @ApiOperation("删除设备")
    public JsonResult<Void> deleteManage(
            @ApiParam("设备编码") @RequestParam(value = "ids", required = true) String ids) {
        String[] idArray = ids.split(",");
        List integers = Arrays.asList(idArray);
        Integer integer = deviceService.deleteManages(integers);
        if (integer != 1) {
            return new JsonResult<>(FAILED, "删除失败", null);
        }
        return new JsonResult<>(FAILED, "删除成功", null);
    }


    /**
     * 根据不同角色设备列表展示
     */
    public PageInfo<Device1> PageDisply(DeviceParams deviceParams, Integer pageNum, Integer pageSize, Role role, PageInfo<Device1> pageInfo, String mobile) {
        PageHelper.startPage(pageNum, pageSize);
        if (role.getRoleCode().equals(ADMIN)) {
            // pageInfo=new PageInfo<>(deviceService.findAllDevice(deviceParams));
        } else if (role.getRoleCode().equals(PROXY)) {
            pageInfo = new PageInfo<>(deviceService.selectDeviceByProxy(mobile));
        } else {

            pageInfo = new PageInfo<>(deviceService.selectDeviceByUser(mobile));
        }
        return pageInfo;
    }
}
