package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import cn.zhuchuangsoft.footstone.mappers.InstallPlaceMapper;
import cn.zhuchuangsoft.footstone.service.IInstallPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-26
 */

@Service
public class InstallPlaceImpl implements IInstallPlaceService {

    @Autowired
    private InstallPlaceMapper installPlaceMapper;

    @Override
    public List<InstallPlace> getAllPlace() {
        return installPlaceMapper.getAllPlace();
    }

    @Override
    public void addPlace(InstallPlace installPlace) {
        installPlaceMapper.addPlace(installPlace);
    }

    @Override
    public void updatePlace(InstallPlace installPlace) {
        installPlaceMapper.updatePlace(installPlace);
    }

    @Override
    public InstallPlace getPlace(String installPlaceCode) {
        return installPlaceMapper.getPlace(installPlaceCode);
    }

    @Override
    public void deletePlace(String installPlaceCode) {
        installPlaceMapper.deletePlace(installPlaceCode);
    }
}
