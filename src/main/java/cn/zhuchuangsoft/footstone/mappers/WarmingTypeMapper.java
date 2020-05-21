package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.warming.WarmingType;

import java.util.List;

/**
 * @date 2019-12-22
 */


public interface WarmingTypeMapper {
    /**
     * 获取WarmingType全部数据
     *
     * @return
     */
    List<WarmingType> getWarmingTypeList();

    List<WarmingType> getWarmingType(String deviceTypeCode);

    void ignoreWarming(String id);


}
