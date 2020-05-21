package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.user.User;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;

public interface UserMapper {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */

    Integer insertUser(User user);

    /**
     * 根据用户名查询用户数据
     *
     * @param username
     * @return
     */
    //@Cacheable(cacheNames = "username")
    User selectUserByUserName(String username);

    /**
     * 根据手机号查询用户数据
     *
     * @param mobile
     * @return
     */
    @Cacheable(cacheNames = "mobile")
    User selectUserByMobile(String mobile);

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    Integer modifyPassword(User user);

    /**
     * 根据用户名判断用户是否重复
     *
     * @param username
     * @return
     */
    boolean judgeByUsername(String username);

    boolean judgeByMobile(String mobile);


    /**
     * 更新用户的个人资料
     *
     * @param user
     * @return 受影响的行数
     */
    Integer updateInfoByUid(User user);


    /**
     * 删除地址
     *
     * @param aid 地址的id
     * @return 受影响的行数
     */
    Integer deleteByUid(Integer aid);


    /**
     * @param userName
     * @return
     */
    @Select("SELECT user_code From user WHERE user_name = #{userName}")
    String selectUserCodeByUserName(@Param("userName") String userName);

    @Select("SELECT user_name From user WHERE user_code = #{userCode}")
    String selectUserNameByUserCode(String userCode);
}
