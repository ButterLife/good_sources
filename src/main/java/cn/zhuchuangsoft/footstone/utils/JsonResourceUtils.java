package cn.zhuchuangsoft.footstone.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市县三级联动工具类
 *
 * @author 张洲权
 */
public class JsonResourceUtils {
    public static JSONArray JSON_ARRAY = null;
    /**
     * 所有编码和名称的映射，key编号，value名称
     */
    public static Map<String, String> codeAndNameMap = new HashMap<>(3200);

    static {
        //读取ProvincesCitiesCounties.json文件
        try {
            ClassPathResource resource = new ClassPathResource("/static/ProvincesCitiesCounties.json");
            InputStream inputStream = resource.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[inputStream.available()];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
            String result = outputStream.toString("UTF-8");
            if (StringUtils.isNotBlank(result)) {
                JSON_ARRAY = JSON.parseArray(result);
            }

            // 初始化所有编码和名称的映射集合
            for (int i = 0; i < JSON_ARRAY.size(); i++) {
                JSONObject province = JSON_ARRAY.getJSONObject(i);
                codeAndNameMap.put(province.getString("code"), province.getString("name"));
                JSONArray cityList = province.getJSONArray("cityList");
                JSONObject city;
                for (int j = 0; j < cityList.size(); j++) {
                    city = cityList.getJSONObject(j);
                    codeAndNameMap.put(city.getString("code"), city.getString("name"));
                    JSONArray areaList = city.getJSONArray("areaList");
                    JSONObject area;
                    for (int k = 0; k < areaList.size(); k++) {
                        area = areaList.getJSONObject(k);
                        codeAndNameMap.put(area.getString("code"), area.getString("name"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JsonResourceUtils() {

    }

    /**
     * 获得省
     *
     * @return
     */
    public static List<Map<String, Object>> getProvinces() {
        List<Map<String, Object>> list = new ArrayList();
        for (int i = 0; i < JSON_ARRAY.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = JSON_ARRAY.getJSONObject(i);
            map.put("key", jsonObject.getString("code"));
            map.put("value", jsonObject.getString("name"));
            list.add(map);
        }
        return list;

    }

    /**
     * 根据省获取市
     *
     * @return
     */
    public static List<Map<String, Object>> getCities(String code) {
        List<Map<String, Object>> list = new ArrayList();
        if (StringUtils.isBlank(code) || code.equals("0")) {
            return list;
        }
        if (code.length() != 6) {
            return list;
        }
        boolean sign = false;
        for (int i = 0; i < JSON_ARRAY.size(); i++) {
            JSONObject jsonObject = JSON_ARRAY.getJSONObject(i);
            JSONArray cityList = jsonObject.getJSONArray("cityList");
            for (int j = 0; j < cityList.size(); j++) {
                JSONObject jsonObject2 = cityList.getJSONObject(j);
                if (jsonObject2.getString("code").startsWith(code.substring(0, 3))) {
                    Map<String, Object> map = new HashMap();
                    map.put("key", jsonObject2.getString("code"));
                    map.put("value", jsonObject2.getString("name"));
                    list.add(map);
                    sign = true;
                }
            }
            if (sign) {
                break;
            }
        }
        return list;
    }

    /**
     * 根据市获取县
     *
     * @return
     */
    public static List<Map<String, Object>> getCounties(String code) {
        List<Map<String, Object>> list = new ArrayList();
        if (StringUtils.isBlank(code) || code.equals("0")) {
            return list;
        }
        if (code.length() != 6) {
            return list;
        }

        boolean sign = false;
        for (int i = 0; i < JSON_ARRAY.size(); i++) {
            JSONObject jsonObject = JSON_ARRAY.getJSONObject(i);
            JSONArray cityList = jsonObject.getJSONArray("cityList");
            for (int j = 0; j < cityList.size(); j++) {
                JSONObject jsonObject2 = cityList.getJSONObject(j);
                if (jsonObject2.getString("code").startsWith(code.substring(0, 3))) {
                    JSONArray areaList = jsonObject2.getJSONArray("areaList");
                    for (int c = 0; c < areaList.size(); c++) {
                        JSONObject object = areaList.getJSONObject(c);
                        if (object.getString("code").substring(0, 4).equals(code.substring(0, 4))) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("key", object.getString("code"));
                            map.put("value", object.getString("name"));
                            list.add(map);
                            sign = true;
                        }
                    }
                    if (sign) {
                        break;
                    }
                }
            }
        }
        return list;
    }

}
