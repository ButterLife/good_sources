package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.InstallPlace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-26
 */


public interface InstallPlaceMapper {

    List<InstallPlace> getAllPlace();

    void addPlace(InstallPlace installPlace);

    void updatePlace(InstallPlace installPlace);

    InstallPlace getPlace(String installPlaceCode);

    void deletePlace(String installPlaceCode);

    @Select("SELECT id,INSTALL_PLACE_CODE,INSTALL_PLACE_NAME,INSTALL_PLACE_ADDRESS \n" +
            "FROM `installplace` \n" +
            "WHERE `INSTALL_PLACE_CODE`=(SELECT `INSYALL_PLEACE_CODE` FROM `device` WHERE `DEVICE_CODE`=#{deviceCode})")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "INSTALL_PLACE_CODE", property = "installPlaceCode"),
            @Result(column = "INSTALL_PLACE_NAME", property = "installPlaceName"),
            @Result(column = "INSTALL_PLACE_ADDRESS", property = "installPlaceAddress"),
    })
    InstallPlace getInstallPlaceValueByDeviceCoce(String deviceCode);

    //判读是否是多次添加
    @Select("SELECT `warming_msg` FROM `vipdevice` WHERE `device_code`=#{deviceCode}  AND `warming_time`=#{warmingTime}")
    String selectVipDevice(@Param("deviceCode") String deviceCode, @Param("warmingTime") String warmingTime);
}
