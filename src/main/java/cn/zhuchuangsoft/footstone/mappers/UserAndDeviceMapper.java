package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.Device;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.entity.user.UserAndDevice;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-11
 */


public interface UserAndDeviceMapper {


    @Insert({"INSERT INTO user_device(" +
            "device_id,user_id,proxy_id)" +
            "values" +
            "(#{UserAndDevice.deviceId},#{UserAndDevice.userId},#{UserAndDevice.proxyId})"})
    Integer insertUserDevice(@Param("UserAndDevice") UserAndDevice userAndDevice);

    //    @Cacheable()
    @Select("SELECT user_code from user_device where device_code = #{deviceCode}")
    List<String> selectUserDevice(@Param("deviceCode") String deviceCode);

    @Select("SELECT device_code from user_device where user_code = #{userCode}")
    List<String> selectDeviceByUserCode(@Param("userCode") String userCode);
}
