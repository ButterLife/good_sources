package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.user.UserAndDevice;
import cn.zhuchuangsoft.footstone.mappers.UserAndDeviceMapper;
import cn.zhuchuangsoft.footstone.mappers.UserMapper;
import cn.zhuchuangsoft.footstone.service.IUserAndDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-11
 */

@Service
public class UserAndDeviceServiceImpl implements IUserAndDeviceService {
    @Autowired
    private UserAndDeviceMapper userAndDeviceMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer insertUserAndDevice(UserAndDevice userAndDevice) {
        return userAndDeviceMapper.insertUserDevice(userAndDevice);
    }

    @Override
    public List<String> selectDeviceCodeByUserCode(String userCode) {
        return userAndDeviceMapper.selectDeviceByUserCode(userCode);
    }
}
