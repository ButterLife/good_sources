package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;
import cn.zhuchuangsoft.footstone.mappers.WarmingTypeMapper;
import cn.zhuchuangsoft.footstone.mappers.ZDeviceWarmingMapper;
import cn.zhuchuangsoft.footstone.service.IZDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.FastJsonUtils;
import cn.zhuchuangsoft.footstone.utils.RedisUtils;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZDeviceWarmingServiceImpl implements IZDeviceWarmingService {
    public String WARMING_TYPE_LIST = "WARMING_TYPE_LIST";
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private ZDeviceWarmingMapper deviceWarmingMapper;
    @Autowired
    private WarmingTypeMapper warmingTypeMapper;

    @Override
    public List<String> selectDeviceByMobile(String mobile) {

        return deviceWarmingMapper.selectDeviceByMobile(mobile);
    }

    @Override
    public List<Map<String, Object>> selectDeviceWarmingByCode(String deviceCode, QueryParameters queryParameters) {


        return deviceWarmingMapper.selectDeviceWarmingByCode(deviceCode, queryParameters);
    }

    @Override
    public int updateWarmingById(Warming warming) {

        return deviceWarmingMapper.updateWarmingById(warming);
    }

    @Override
    public List<Map<String, Object>> selectDeviceWarmingByCodes(@Param("array") Object[] arrys, @Param("query") QueryParameters queryParameters) {


        return deviceWarmingMapper.selectDeviceWarmingByCodes(arrys, queryParameters);
    }

    @Override
    public List<Map<String, Object>> selectDeviceWarmingAll(QueryParameters queryParameters) {
        return deviceWarmingMapper.selectDeviceWarmingAll(queryParameters);
    }

    @Override
    public List<Map<String, Object>> selectDeviceDataByCode(String surface, String deviceCode) {
        Map<String, Object> map = deviceWarmingMapper.selectDeviceDataByCode(surface, deviceCode);
        List<WarmingType> warmingTypeAll = getWarmingTypeAll(map.get("TYPE_CODE").toString());
        List<Map<String, Object>> list = new ArrayList();
        for (WarmingType warmingType : warmingTypeAll) {
            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (warmingType.getParamentName().equalsIgnoreCase(m.getKey())) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("zhKey", warmingType.getParament());
                    map2.put("enKey", m.getKey());
                    map2.put("value", m.getValue());
                    list.add(map2);
                    break;
                }
            }
        }

        return list;
    }

    @Override
    public String selectSurfaceByCode(String deviceCode) {
        return deviceWarmingMapper.selectSurfaceByCode(deviceCode);
    }

    @Override
    public Map<String, Object> selectWarmingById(String id) {
        return deviceWarmingMapper.selectWarmingById(id);
    }

    @Override
    public List<WarmingType> getWarmingTypeAll(String deviceTypeCode) {
        List<WarmingType> warmingTypeList = null;
        List<WarmingType> list = new ArrayList<>();
        //查询缓存
        try {

            warmingTypeList = warmingTypeMapper.getWarmingTypeList();
            /*//如果缓存中有直接响应结果
            Object obj = redisUtil.get(WARMING_TYPE_LIST);
            if (obj != null && obj.toString().length() != 0) {
                warmingTypeList = FastJsonUtils.toList(obj.toString(), WarmingType.class);
            } else {
                //从数据库查询
                warmingTypeList = warmingTypeMapper.getWarmingTypeList();
                //存入redis
                redisUtil.set(WARMING_TYPE_LIST, FastJsonUtils.toString(warmingTypeList));
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (WarmingType warmingType : warmingTypeList) {
            //getTypeCode() getDeviceTypeCode
            if (deviceTypeCode.equals(warmingType.getTypeCode())) {
                list.add(warmingType);
            }
        }


        return list;
    }

    @Override
    public void ignoreWarming(String id) {
        warmingTypeMapper.ignoreWarming(id);
    }


    @Override
    public List<Map<String, Object>> selectWarmingByCodes(Object[] code) {


        return deviceWarmingMapper.selectWarmingByCodes(code);
    }

}
