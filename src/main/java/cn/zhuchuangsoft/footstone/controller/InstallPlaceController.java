package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.service.IInstallPlaceService;
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
 * @date 2019-12-26
 * 设备安装
 */

@RestController
@RequestMapping("install")
public class InstallPlaceController extends BaseController {

    @Autowired
    private IInstallPlaceService installPlaceService;

    @GetMapping("getAllInstallPlace")
    @ApiOperation("获取安装地点集合")
    public JsonResult<List<InstallPlace>> getAllInstallPlace() {
        List<InstallPlace> installPlaces = installPlaceService.getAllPlace();

        return new JsonResult<>(SUCCESS, installPlaces);
    }


    @PostMapping("editorInstallPlace")
    @ApiOperation("编辑安装地点")
    public JsonResult<Void> editorInstallPlace(
            @ApiParam("安装地点编码  用于修改") @RequestParam(value = "installPlaceCode", required = false) String installPlaceCode,
            @ApiParam("地点名称") @RequestParam(value = "installPlaceName", required = false) String installPlaceName,
            @ApiParam("地点地址") @RequestParam(value = "installPlaceAddress", required = false) String installPlaceAddress,
            @ApiParam("地点维度") @RequestParam(value = "installPlaceLat", required = false) String installPlaceLat,
            @ApiParam("地点经度") @RequestParam(value = "installPlaceLon", required = false) String installPlaceLon,
            @ApiParam("操作人姓名") @RequestParam(value = "installPlaceOperater", required = false) String installPlaceOperater
    ) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isEmpty(installPlaceCode)) {
                InstallPlace installPlace = new InstallPlace(installPlaceName, installPlaceAddress, installPlaceLat, installPlaceLon, installPlaceOperater);
                installPlace.setInstallPlaceCreateTime(simpleDateFormat.format(new Date()));
                installPlace.setInstallPlaceUpdateTime(simpleDateFormat.format(new Date()));
                installPlace.setInstallPlaceCode(BaseController.getUUID());
                installPlaceService.addPlace(installPlace);
                return new JsonResult<>(SUCCESS);
            } else {
                InstallPlace installPlace = new InstallPlace(installPlaceCode, installPlaceName, installPlaceAddress, installPlaceLat, installPlaceLon, installPlaceOperater);
                installPlace.setInstallPlaceUpdateTime(simpleDateFormat.format(new Date()));
                installPlaceService.updatePlace(installPlace);
                return new JsonResult<>(SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED);
    }

    @GetMapping("getInstallPlace/{installPlaceCode}")
    @ApiOperation("获得详情")
    public JsonResult<InstallPlace> getInstallPlace(
            @ApiParam("安装地点编码") @PathVariable("installPlaceCode") String installPlaceCode
    ) {
        InstallPlace place = installPlaceService.getPlace(installPlaceCode);
        if (StringUtils.isEmpty(place)) {
            return new JsonResult<>(FAILED, "获取失败", null);
        }
        return new JsonResult<>(SUCCESS, place);
    }

    @GetMapping("deleteInstallPlace/{installPlaceCode}")
    @ApiOperation("删除集合地点")
    public JsonResult<Void> deleteInstallPlace(
            @ApiParam("安装地点编码") @PathVariable("installPlaceCode") String installPlaceCode
    ) {
        try {
            installPlaceService.deletePlace(installPlaceCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(SUCCESS);


    }
}
