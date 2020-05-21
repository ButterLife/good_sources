package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.ShortMessage;

import java.util.List;
import java.util.Map;

public interface MessageMapper {

    List<Map<String, Object>> getMessageListByQuery(QueryParameters queryParameters);

    Map<String, Object> selectDevicePlaceByCode(String deviceCode);

    int insertShortMessage(ShortMessage message);
}
