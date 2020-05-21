package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.DeviceLine;
import org.apache.ibatis.annotations.Insert;

public interface JalaDeviceMapper {

    @Insert("INSERT INTO `t_jala` (`Device_Code`, `Update_Time`, `Voltage`, `Power`, `Current`, `Leakage`, `Temp`, `Status`)" +
            " VALUES ( #{lineID}, #{updateTime}, #{voltage}, #{power}, #{current}, #{leakAge}, #{temp}, #{status})" +
            " ON DUPLICATE KEY UPDATE Update_Time = VALUES(Update_Time),Voltage = VALUES(Voltage)," +
            " Power = VALUES(Power),Current = VALUES(Current),Leakage = VALUES(Leakage)," +
            " Temp = VALUES(Temp),Status = VALUES(Status)")
    int insertJalaDeviceData(DeviceLine line);

    @Insert("INSERT INTO `t_jala_history` (`Device_Code`, `Update_Time`, `Voltage`, `Power`, `Current`, `Leakage`, `Temp`, `Status`)" +
            " VALUES ( #{lineID}, #{updateTime}, #{voltage}, #{power}, #{current}, #{leakAge}, #{temp}, #{status})")
    int insertJalaDeviceHistoryData(DeviceLine line);
}
