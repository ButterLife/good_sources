package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.DeviceType;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.service.IRoleService;
import cn.zhuchuangsoft.footstone.service.IZDeviceService;
import cn.zhuchuangsoft.footstone.service.IZDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ZDeviceManageController extends BaseController {
    @Autowired
    private IZDeviceService deviceService;
    @Autowired
    private IZDeviceWarmingService deviceWarmingService;
    @Autowired
    private IRoleService roleService;

    /**
     * 设备编码类型
     */
    @GetMapping("/deviceManage/type")
    @ApiOperation("设备编码类型")
    public JsonResult<List<DeviceType>> getDeviceType() {
        List<DeviceType> deviceTypes = deviceService.getAllDeviceType();
        return new JsonResult<>(SUCCESS, deviceTypes);
    }

    @GetMapping("/deviceManage/place")
    @ApiOperation("设备安装地点集合")
    public JsonResult<List<Map<String, Object>>> getInstallPlace() {
        List<Map<String, Object>> deviceTypes = deviceService.getInstallPlace();
        return new JsonResult<>(SUCCESS, deviceTypes);
    }

    @GetMapping("/deviceManage/proxy")
    @ApiOperation("设备代理类型")
    public JsonResult<List<Map<String, Object>>> getProxy() {
        List<Map<String, Object>> deviceTypes = deviceService.getProxy();
        return new JsonResult<>(SUCCESS, deviceTypes);
    }

    /**
     * 设备列表展示
     */
    @GetMapping("/deviceManage/all")
    @ApiOperation("设备列表展示")
    public JsonResult<List<Map<String, Object>>> getDevice(QueryParameters queryParameters, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        try {
            List<Role> roles = roleService.findByUid(user.getId());
            Role role = roles.get(0);
            PageHelper.startPage(queryParameters.getPage(), queryParameters.getLimit());
            PageInfo<Map<String, Object>> pageInfo = null;
            if (role.getRoleCode().equals(ADMIN)) {
                pageInfo = new PageInfo<>(deviceService.findAllDevice(queryParameters));
            }
            return new JsonResult<>(SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }

    @GetMapping("/deviceManage/get/{id}")
    @ApiOperation("根据id获取设备")
    public JsonResult<Map<String, Object>> getDeviceManageById(@PathVariable String id) {
        try {
            return new JsonResult<>(SUCCESS, deviceService.getDeviceById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }

    @PostMapping("/deviceManage/add")
    @ApiOperation("添加设备")
    public JsonResult insertDevice(Device1 device, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        device.setCreateTime(simpleDateFormat.format(new Date()));
        device.setUpdateTime(simpleDateFormat.format(new Date()));
        if (StringUtils.isEmpty(device.getDeviceState())) {
            device.setDeviceState("0");// 0正常 1离线 2 报警
        }
        device.setOperater(user.getUsername());
        device.setDeviceCode(device.getDeviceTypeCode() + "-" + device.getDeviceCode());
        int i = deviceService.inertDevice(device);
        return i > 0 ? new JsonResult(SUCCESS, "添加成功", null) : new JsonResult<>(FAILED, "添加失败", null);
    }

    @PostMapping("/deviceManage/update")
    @ApiOperation("跟新设备")
    public JsonResult updateDeviceManage(Device1 device, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        device.setUpdateTime(simpleDateFormat.format(new Date()));
        device.setOperater(user.getUsername());
        device.setDeviceCode(device.getDeviceTypeCode() + "-" + device.getDeviceCode());
        int i = deviceService.updateDeviceById(device);
        return i > 0 ? new JsonResult(SUCCESS, "更新成功", null) : new JsonResult<>(FAILED, "更新失败", null);
    }

    /**
     * @param deviceCode
     * @return
     */
    @GetMapping("/deviceData/{deviceCode}")
    public Map<String, Object> getDeviceData(@PathVariable String deviceCode) {
        Map<String, Object> map = null;
        try {
            String surface = deviceWarmingService.selectSurfaceByCode(deviceCode);
            List<Map<String, Object>> stringObjectMap = deviceWarmingService.selectDeviceDataByCode(surface, deviceCode);
            map = deviceService.getDeviceByCode(deviceCode);
            map.put("date", stringObjectMap);
            map.put("code", SUCCESS);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("code", FAILED);
        map.put("msg", "查询失败");
        return map;
    }

    /**
     * 删除设备
     *
     * @param ids
     * @return
     */
    @PostMapping("/deviceManage/delete")
    @ApiOperation("删除设备")
    public JsonResult<Void> deleteManage(@ApiParam("设备id") String ids) {
        int i = deviceService.deleteDeviceByArray(ids.split(","));
        return i > 0 ? new JsonResult(SUCCESS, "删除成功", null) : new JsonResult<>(FAILED, "删除失败", null);
    }
}
