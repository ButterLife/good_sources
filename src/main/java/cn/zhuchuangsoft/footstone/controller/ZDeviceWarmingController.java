package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IZDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ZDeviceWarmingController extends BaseController {
    @Autowired
    private IZDeviceWarmingService deviceWarmingService;

    /**
     * 用户下所有设备报警信息
     *
     * @param queryParameters 查询条件对象
     * @param request
     * @return
     */
    @ApiOperation("该用户告使用设备警列表展示")
    @GetMapping("/warming/all")
    public JsonResult<List<Map<String, Object>>> getWarmning(QueryParameters queryParameters, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<Map<String, Object>> list2 = null;
        PageHelper.startPage(queryParameters.getPage(), queryParameters.getLimit());
        if ("root".equals(user.getUsername())) {
            list2 = deviceWarmingService.selectDeviceWarmingAll(queryParameters);

        } else {
            List<String> list = deviceWarmingService.selectDeviceByMobile(user.getMobile());
            //List<String> list=deviceWarmingService.selectDeviceByMobile("13757746583");
            list2 = deviceWarmingService.selectDeviceWarmingByCodes(list.toArray(), queryParameters);
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list2);
        List<Map<String, Object>> list = pageInfo.getList();
        if (!list2.isEmpty()) {
            return new JsonResult<List<Map<String, Object>>>(SUCCESS, list, (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        }
        return new JsonResult<>(FAILED, "没有错误信息", null);
    }

    @GetMapping("/warming/plus")
    public JsonResult<List<Map<String, Object>>> getWarmninga(QueryParameters queryParameters, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        PageHelper.startPage(queryParameters.getPage(), queryParameters.getLimit());
        List<String> deviceList = deviceWarmingService.selectDeviceByMobile(user.getMobile());
        if (!deviceList.isEmpty()) {
            List<Map<String, Object>> list2 = deviceWarmingService.selectWarmingByCodes(deviceList.toArray());
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list2);
            List<Map<String, Object>> list = pageInfo.getList();
            if (!list2.isEmpty()) {

                return new JsonResult<List<Map<String, Object>>>(SUCCESS, list, (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
            }
        }

        return new JsonResult<>(FAILED, "没有报警信息", null);
    }

    /**
     * @param queryParameters
     * @return
     */
    @ApiOperation("查询设备告警列表")
    @GetMapping("/warming/query")
    public JsonResult<List<Map<String, Object>>> query(String deviceCode, QueryParameters queryParameters) {
        System.out.println(queryParameters);
        try {
            List<Map<String, Object>> maps = deviceWarmingService.selectDeviceWarmingByCode(deviceCode, queryParameters);
            System.out.println(maps);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(deviceWarmingService.selectDeviceWarmingByCode(deviceCode, queryParameters));
            return new JsonResult<>(SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }

    @GetMapping("/warming/get/{id}")
    public JsonResult<Map<String, Object>> select(@PathVariable String id) {
        try {
            Map<String, Object> a = deviceWarmingService.selectWarmingById(id);
            return new JsonResult<>(SUCCESS, "", a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }


    /**
     * 更新 warming 对象
     *
     * @param handleMsg
     * @return
     */
    @ApiOperation("修改设备警列表")
    @PostMapping("/warming/processing")
    public JsonResult<Void> Processing(String id, String handleMsg) {
        Warming warming = new Warming();
        warming.setHandleMsg(handleMsg);
        warming.setId(Integer.parseInt(id));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        warming.setHandleTime(simpleDateFormat.format(new Date()));
        try {
            deviceWarmingService.updateWarmingById(warming);
//            deviceWarmingService.updateWarmingById(id,handleMsg,handleMsg);
            return new JsonResult<>(SUCCESS, "修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new JsonResult<>(FAILED, "修改失败", null);
    }


    /* *
     * @Description 批量忽略信息
     * @Date  2020/3/19
     * @Param [id, handleMsg]
     * @return cn.zhuchuangsoft.footstone.utils.JsonResult<java.lang.Void>
     **/
    @ApiOperation("批量忽略信息")
    @PostMapping("/warming/ignore")
    public JsonResult<Void> ignoreWarming(String ids) {
        try {
            String[] idsSplit = ids.split(",");
            for (String id : idsSplit) {
                deviceWarmingService.ignoreWarming(id);
            }
            return new JsonResult<>(SUCCESS, "修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "修改失败", null);
    }
}
