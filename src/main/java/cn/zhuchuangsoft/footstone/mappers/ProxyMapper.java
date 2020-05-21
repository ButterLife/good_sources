package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.user.Proxy;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-20
 */


public interface ProxyMapper {

    List<Proxy> proxyList();

    void addProxy(Proxy proxy);

    Proxy getProxyByCode(String proxyCode);

    void updateProxy(Proxy proxy);

    void deleteProxy(String proxyCode);
}
