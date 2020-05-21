package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.user.UserAndDevice;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-11
 */


public interface IUserAndDeviceService {

    Integer insertUserAndDevice(UserAndDevice userAndDevice);


    List<String> selectDeviceCodeByUserCode(String userCode);
}
