package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.entity.ShortMessage;
import cn.zhuchuangsoft.footstone.mappers.MessageMapper;
import cn.zhuchuangsoft.footstone.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Map<String, Object>> getMessageListByQuery(QueryParameters queryParameters) {

        return messageMapper.getMessageListByQuery(queryParameters);
    }

    @Override
    public int insertShortMessage(ShortMessage shortMessage) {
        return messageMapper.insertShortMessage(shortMessage);
    }
}
