package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.controller.InstallPlaceController;
import cn.zhuchuangsoft.footstone.entity.InstallPlace;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-26
 */


public interface IInstallPlaceService {


    List<InstallPlace> getAllPlace();

    void addPlace(InstallPlace installPlace);

    void updatePlace(InstallPlace installPlace);

    InstallPlace getPlace(String installPlaceCode);

    void deletePlace(String installPlaceCode);
}
