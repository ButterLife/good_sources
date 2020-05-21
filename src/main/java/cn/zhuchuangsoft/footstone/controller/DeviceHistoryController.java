package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import cn.zhuchuangsoft.footstone.service.IDeviceHistoryService;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 阿白
 * @date 2019-12-18
 * @update 2019-12-20
 * 设备历史记录模块
 */

@RestController
@RequestMapping("deviceHistory")
public class DeviceHistoryController extends BaseController {

    @Autowired
    private IDeviceHistoryService deviceHistoryService;

    @Autowired
    private IWarmingService warmingService;
    @Autowired
    private IDeviceService deviceService;

    /**
     * 查看设备历史记录
     */
    @PostMapping("selectDeviceHistory")
    @ApiOperation("查看设备历史记录")
    public JsonResult<Map> selectOneDeviceHistory(
            @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = true) String deviceCode,
            @ApiParam("查询起始时间 YYYY-MM-dd HH:mm") @RequestParam(value = "startTime", required = false) String startTime,
            @ApiParam("查询结束时间 YYYY-MM-dd HH:mm") @RequestParam(value = "endTime", required = false) String endTime,
            @ApiParam("当前页") @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(value = "limit", required = true, defaultValue = "5") Integer limit
    ) {
        try {
            PageHelper.startPage(page, limit);
            PageInfo<PoweredDeviceHistory> pageInfo = new PageInfo<PoweredDeviceHistory>(deviceHistoryService.selectDeviceHistory(deviceCode, startTime, endTime));
            Device1 device = deviceService.DeviceByDeviceCode(deviceCode);
            List<WarmingType> warmings = warmingService.getWarmingType(device.getDeviceTypeCode());
            Map map = new HashMap();
            map.put("device", device);
            map.put("warmings", warmings);
            map.put("history", pageInfo.getList());
            return new JsonResult<>(SUCCESS, map, (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询错误", null);
    }


}
