package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.mappers.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-13
 */


public interface IRoleService {

    Role findById(Integer id);

    List<Role> findByUid(Integer uid);

    List<Proxy> proxyList();

    void addProxy(Proxy proxy);

    Proxy getProxyByCode(String proxyCode);

    void updateProxy(Proxy proxy);

    void deleteProxy(String proxyCode);
}
