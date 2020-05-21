package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.warming.JalaLineWarming;
import cn.zhuchuangsoft.footstone.entity.warming.JalaWarming;
import org.apache.ibatis.annotations.Insert;

public interface JalaWarmingMapper {

    @Insert("INSERT INTO jala_warming(device_id, warming_time)" +
            " SELECT #{deviceId},#{warmingTime} FROM DUAL WHERE NOT EXISTS" +
            "(SELECT device_id FROM jala_warming WHERE device_id = #{deviceId});")
    int inertJalaWarming(JalaWarming jalaWarming);

    @Insert("INSERT INTO jala_line_warming ( `device_id`,`line_id`, `name`, `warming_code`, `warming_msg`) " +
            "SELECT #{deviceId}, #{lineId},#{name}, #{warmingCode}, #{warmingMsg} FROM DUAL WHERE NOT EXISTS " +
            "(SELECT line_id FROM jala_line_warming WHERE line_id = #{lineId});")
    int inertJalaLineWarming(JalaLineWarming jalaLineWarming);
}
