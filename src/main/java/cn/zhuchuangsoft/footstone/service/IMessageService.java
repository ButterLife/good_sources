package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.ShortMessage;

import java.util.List;
import java.util.Map;

public interface IMessageService {
    /**
     * 根据条件查询信息
     *
     * @param queryParameters
     * @return
     */
    List<Map<String, Object>> getMessageListByQuery(QueryParameters queryParameters);

    int insertShortMessage(ShortMessage shortMessage);
}
