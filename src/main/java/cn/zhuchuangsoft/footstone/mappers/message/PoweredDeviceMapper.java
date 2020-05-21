package cn.zhuchuangsoft.footstone.mappers.message;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PoweredDeviceMapper {


    //在原有的基础上修改信息
    @Insert("INSERT INTO powered_device (" +
            "device_code,voltage_a,voltage_b,voltage_c," +
            "current_a,current_b,current_c," +
            "temp_a,temp_b,temp_c,temp_n," +
            "create_time" +
            ") VALUES (" +
            "#{deviceCode},#{voltageA},#{voltageB},#{voltageC}," +
            "#{currentA},#{currentB},#{currentC}," +
            "#{tempA},#{tempB},#{tempC},#{tempN}," +
            "#{createTime}" +
            ")" +
            "ON DUPLICATE KEY UPDATE " +
            "voltage_a = VALUES(voltage_a)," +
            "voltage_b = VALUES(voltage_b)," +
            "voltage_c = VALUES(voltage_c)," +
            "current_a = VALUES(current_a)," +
            "current_b = VALUES(current_b)," +
            "current_c = VALUES(current_c)," +
            "temp_a = VALUES(temp_a)," +
            "temp_b = VALUES(temp_b)," +
            "temp_c = VALUES(temp_c)," +
            "temp_n = VALUES(temp_n)," +
            "create_time = VALUES(create_time);")
    Integer updateMessage(PoweredDevice poweredDevice);

    @Select("")
    PoweredDeviceHistory selectMessage(List<String> deviceIds);

    @Insert("INSERT INTO powered_device_history (" +
            "device_code,voltage_a,voltage_b,voltage_c," +
            "current_a,current_b,current_c," +
            "temp_a,temp_b,temp_c,temp_n," +
            "create_time" +
            ") VALUES (" +
            "#{deviceCode},#{voltageA},#{voltageB},#{voltageC}," +
            "#{currentA},#{currentB},#{currentC}," +
            "#{tempA},#{tempB},#{tempC},#{tempN}," +
            "#{createTime}" +
            ");")
    Integer insertMessage(PoweredDevice poweredDevice);


}
