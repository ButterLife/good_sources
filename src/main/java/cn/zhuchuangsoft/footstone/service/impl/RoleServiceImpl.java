package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.user.Proxy;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.mappers.ProxyMapper;
import cn.zhuchuangsoft.footstone.mappers.RoleMapper;
import cn.zhuchuangsoft.footstone.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-13
 */

@Service
public class RoleServiceImpl implements IRoleService {


    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ProxyMapper proxyMapper;

    @Override
    public Role findById(Integer id) {
        return roleMapper.findById(id);
    }

    @Override
    public List<Role> findByUid(Integer uid) {
        return roleMapper.findByUid(uid);
    }

    @Override
    public List<Proxy> proxyList() {
        return proxyMapper.proxyList();
    }

    @Override
    public void addProxy(Proxy proxy) {
        proxyMapper.addProxy(proxy);
    }

    @Override
    public Proxy getProxyByCode(String proxyCode) {
        return proxyMapper.getProxyByCode(proxyCode);
    }

    @Override
    public void updateProxy(Proxy proxy) {
        proxyMapper.updateProxy(proxy);
    }

    @Override
    public void deleteProxy(String proxyCode) {
        proxyMapper.deleteProxy(proxyCode);
    }
}
