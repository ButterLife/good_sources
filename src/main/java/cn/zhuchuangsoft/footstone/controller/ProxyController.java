package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import cn.zhuchuangsoft.footstone.service.IRoleService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-24
 */

@RestController
@RequestMapping("proxy")
public class ProxyController extends BaseController {

    @Autowired
    private IRoleService roleService;

    /**
     * 代理商类型
     */
    @GetMapping("proxyList")
    @ApiOperation("代理商下拉框")
    public JsonResult<List<Proxy>> proxyList() {
        List<Proxy> proxyList = roleService.proxyList();
        return new JsonResult<List<Proxy>>(SUCCESS, proxyList);
    }


    /**
     * 添加代理商
     */
    @PostMapping("editorProxy")
    @ApiOperation("编辑代理商")
    public JsonResult<Void> editorProxy(
            @ApiParam(name = "proxyCode", value = "代理商编码") String proxyCode,
            @ApiParam(name = "proxyName", value = "代理商名称") String proxyName,
            @ApiParam(name = "proxyAddress", value = "代理商地址") String proxyAddress,
            @ApiParam(name = "proxyMobile", value = "代理商联系电话") String proxyMobile,
            @ApiParam(name = "liaison", value = "代理商联系人姓名") String liaison,
            @ApiParam(name = "lat", value = "代理商纬度") String lat,
            @ApiParam(name = "lon", value = "代理商经度") String lon,
            @ApiParam(name = "operater", value = "代理商名称") String operater
    ) {
        try {
            Proxy proxy = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (!StringUtils.isEmpty(proxyCode)) {
                proxy = new Proxy(proxyCode, proxyName, proxyAddress, proxyMobile, liaison, lat, lon, operater);
                proxy.setUpdateTime(format.format(new Date()));
                roleService.updateProxy(proxy);
            } else {
                proxy = new Proxy(proxyName, proxyAddress, proxyMobile, liaison, lat, lon, operater);
                proxy.setProxyCode(BaseController.getUUID());

                proxy.setCreateTime(format.format(new Date()));
                proxy.setUpdateTime(format.format(new Date()));
                roleService.addProxy(proxy);
            }

            return new JsonResult<>(SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<Void>(FAILED, null);
    }

    /**
     * 获取代理商
     */
    @GetMapping("getProxyByCode/{proxyCode}")
    @ApiOperation("获取代理商")
    public JsonResult<Proxy> getProxyByCode(
            @ApiParam("proxyCode") @PathVariable("proxyCode") String proxyCode
    ) {
        Proxy proxy = roleService.getProxyByCode(proxyCode);
        return new JsonResult<>(SUCCESS, proxy);
    }

    /**
     * 删除代理商
     */
    @GetMapping("deleteProxy/{proxyCode}")
    @ApiOperation("删除代理商")
    public JsonResult<Void> deleteProxy(
            @ApiParam("proxyCode") @PathVariable("proxyCode") String proxyCode
    ) {
        roleService.deleteProxy(proxyCode);
        return new JsonResult<>(SUCCESS, null);
    }
}
