package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.utils.JsonResourceUtils;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 省市县三级联动
 */
@RestController
public class ProvincesCitiesController extends BaseController {

    @GetMapping("/provinces")
    public JsonResult<List<Map<String, Object>>> getProvinces() {

        return new JsonResult<List<Map<String, Object>>>(SUCCESS, JsonResourceUtils.getProvinces());
    }

    @GetMapping("/cities")
    public JsonResult<List<Map<String, Object>>> getCities(String code) {

        return new JsonResult<List<Map<String, Object>>>(SUCCESS, JsonResourceUtils.getCities(code));
    }

    @GetMapping("/counties")
    public JsonResult<List<Map<String, Object>>> getCounties(String code) {
        return new JsonResult<List<Map<String, Object>>>(SUCCESS, JsonResourceUtils.getCounties(code));
    }

}
