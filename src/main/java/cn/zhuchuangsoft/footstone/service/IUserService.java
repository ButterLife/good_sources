package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.user.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    Integer increaseUser(User user);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User selectUsername(String username);

    /**
     * 根据手机号查询用户数据
     *
     * @param mobile
     * @return
     */
    User selectMobile(String mobile);

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    Integer updatePassword(User user);

    boolean verifyPassword(String oldPassword, User user);

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    boolean judgeUsername(String username);

    /**
     * 判断手机号是否存在
     *
     * @param mobile
     * @return
     */
    boolean judgeMobile(String mobile);


    /**
     * 修改用户个人资料
     *
     * @param username 用户名
     * @param user     封装了用户新资料的对象
     * @return
     */
    void changeInfo(String username, User user);

    /**
     * 删除用户
     *
     * @param username 用户名
     */
    void deleteUser(String username);

    String selectUserCode(String userName);
}
