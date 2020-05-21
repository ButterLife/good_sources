package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 阿白
 * @date 2019-12-19
 * @update 2019-12-21
 */

@RestController
@RequestMapping("warming")
public class WarmingController extends BaseController {

    @Autowired
    private IWarmingService warmingService;
    @Autowired
    private IDeviceService deviceService;

    /**
     * 警告列表展示
     */
    @PostMapping("getAllWarming")
    @ApiOperation("告警列表展示")
    public JsonResult<List<Warming>> getAllWarming(
            @ApiParam("是否被处理了") @RequestParam(name = "ishandle", required = false) String ishandle,
            @ApiParam("告警时间 如果是今日告警传today,如果是昨天告警传yesterday") @RequestParam(name = "warmingTime", required = false) String warmingTime,
            @ApiParam("当前页") @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(name = "limit", required = true, defaultValue = "5") Integer limit,
            HttpServletRequest request
    ) {
        try {
            PageHelper.startPage(page, limit);
            Warming warming = new Warming(ishandle, warmingTime);
            PageInfo<Warming> pageInfo = new PageInfo<>(warmingService.getAllWarming(warming));
        /*if(!StringUtils.isEmpty(deviceCode)){
           pageInfo=new PageInfo<>(warmingService.getWarmingByCode(deviceCode));
        }else{
            pageInfo=new PageInfo<>(warmingService.getAllWarming());
        }*/
            return new JsonResult<List<Warming>>(SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }

    /**
     * 查看单个告警详情
     */
    @GetMapping("getWarmingByWarmingCode/{warmingCode}")
    @ApiOperation("查看单个告警详情")
    public JsonResult<Map> getWarmingByWarmingCode(@ApiParam("告警编码") @PathVariable("warmingCode") String warmingCode) {


        Warming warming = warmingService.getWarmingByWarmingCode(warmingCode);
        if (StringUtils.isEmpty(warming)) {
            return new JsonResult<>(FAILED, "参数传入不正确", null);
        }
        Map maps = deviceService.getDeviceByDeviceCode(warming.getDeviceCode().getDeviceCode());
        maps.put("warming", warming);

        return new JsonResult<Map>(SUCCESS, maps);
    }

    /**
     * 查看单个设备的全部告警信息
     */
    @PostMapping("getWarmingByDeviceCode")
    @ApiOperation("查看单个设备的全部告警信息")
    public JsonResult<Map> getWarmingByDeviceCode(
            @ApiParam("deviceCode") @RequestParam(value = "deviceCode", required = true) String deviceCode,
            @ApiParam("查询起始时间 YYYY-MM-dd HH:mm") @RequestParam(value = "startTime", required = false) String startTime,
            @ApiParam("查询结束时间 YYYY-MM-dd HH:mm") @RequestParam(value = "endTime", required = false) String endTime,
            @ApiParam("当前页") @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(name = "limit", required = true, defaultValue = "5") Integer limit
    ) {
        PageHelper.startPage(page, limit);
        List<Warming> warmingList = warmingService.getWarmingByCode(deviceCode, startTime, endTime);
        PageInfo pageInfo = new PageInfo<>(warmingList);
        Map maps = deviceService.getDeviceByDeviceCode(deviceCode);
        maps.put("warming", pageInfo.getList());
        return new JsonResult<Map>(SUCCESS, maps, (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
    }

}
