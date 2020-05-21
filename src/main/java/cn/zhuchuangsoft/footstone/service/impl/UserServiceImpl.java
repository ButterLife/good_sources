package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.controller.BaseController;
import cn.zhuchuangsoft.footstone.controller.ex.ControlUnsuccessfulException;
import cn.zhuchuangsoft.footstone.controller.ex.ControllerException;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.entity.user.RoleAndUser;

import cn.zhuchuangsoft.footstone.entity.user.User;

import cn.zhuchuangsoft.footstone.mappers.RoleMapper;
import cn.zhuchuangsoft.footstone.mappers.UserMapper;
import cn.zhuchuangsoft.footstone.service.IUserService;

import cn.zhuchuangsoft.footstone.service.impl.ex.UpdateException;
import cn.zhuchuangsoft.footstone.service.impl.ex.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Integer increaseUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String code = BaseController.getUUID();
        user.setUserCode(code);
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setOperater(code);
        userMapper.insertUser(user);

        RoleAndUser roleAndUser = new RoleAndUser(user.getId(), user.getRoles().get(0).getId());

        return roleMapper.saveRoleAndUser(roleAndUser);
    }

    @Override
    public User selectUsername(String username) {
        return userMapper.selectUserByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserByUserName(username);
        List<Role> roles = roleMapper.findByUid(user.getId());
        user.setRoles(roles);
        System.out.println(user.toString());
        return user;
    }

    @Override
    public User selectMobile(String mobile) {
        return userMapper.selectUserByMobile(mobile);
    }

    @CacheEvict(cacheNames = {"username", "mobile"}, allEntries = true)
    @Override
    public Integer updatePassword(User user) {
        System.out.println(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.modifyPassword(user);
    }

    @Override
    public boolean verifyPassword(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public boolean judgeUsername(String username) {
        return userMapper.judgeByUsername(username);
    }

    @Override
    public boolean judgeMobile(String mobile) {
        return userMapper.judgeByMobile(mobile);
    }


    @Override

    public void changeInfo(String username, User user) {
        // 日志
        System.err.println("UserServiceImpl.changeInfo()");

        // 调用userMapper.selectUserByUserName()查询用户数据
        User result = userMapper.selectUserByUserName(username);
        if (result == null) {
            throw new ControlUnsuccessfulException("修改用户资料失败，尝试访问的用户数据不存在！");
        }
        // 判断查询结果(result)中的isDelete属性是否为1
        if (result.getIsDelete() == 1) {
            // 是：抛出UserNotFoundException

            throw new ControlUnsuccessfulException("修改用户资料失败，用户数据已被删除！");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String code = BaseController.getUUID();
        user.setUserCode(code);
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setOperater(code);
        userMapper.insertUser(user);
        RoleAndUser roleAndUser = new RoleAndUser(user.getId(), user.getRoles().get(0).getId());

        Integer rows = userMapper.updateInfoByUid(user);
        // 判断返回值是否不为1
        if (rows != 1) {
            // 是：抛出UpdateException
            throw new ControllerException("修改用户资料失败，更新用户资料时出现未知错误，请联系系统管理员！");
        }

    }

    @Override
    public void deleteUser(String username) {
        // 调用userMapper.selectUserByUserName()查询用户数据
        User result = userMapper.selectUserByUserName(username);
        if (result == null) {
            throw new ControlUnsuccessfulException("修改用户资料失败，尝试访问的用户数据不存在！");
        }
        // 判断查询结果(result)中的isDelete属性是否为1
        if (result.getIsDelete() == 1) {
            // 是：抛出UserNotFoundException

            throw new ControlUnsuccessfulException("修改用户资料失败，用户数据已被删除！");
        }

    }

    @Override
    public String selectUserCode(String userName) {
        return userMapper.selectUserCodeByUserName(userName);
    }


}
