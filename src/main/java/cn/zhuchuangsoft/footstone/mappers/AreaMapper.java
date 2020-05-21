package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿白
 * @date 2019-12-20
 */


public interface AreaMapper {

    public void insertArea(@Param("list") List<Area> list);

    List<Area> selectProvince();
}
