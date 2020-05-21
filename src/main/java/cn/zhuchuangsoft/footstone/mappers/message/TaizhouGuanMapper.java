package cn.zhuchuangsoft.footstone.mappers.message;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.PoweredDeviceHistory;
import cn.zhuchuangsoft.footstone.entity.device.ThreePhase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaizhouGuanMapper {

    @Insert("INSERT INTO taizhou_guan (" +
            "device_id,voltage_a,voltage_b,voltage_c," +
            "err_under,err_over,current_a,current_b,current_c," +
            "err_current,leakage,err_leak_value,temp_a," +
            "temp_b,temp_c,temp_n,err_temp_value," +
            "arc_a,arc_b,arc_c,err_arc_value," +
            "voltage_balance,err_voltage_balance," +
            "current_balance,err_current_balance," +
            "hz_a,hz_b,hz_c,voltage_phase_a,voltage_phase_b," +
            "voltage_phase_c,current_phase_a,current_phase_b," +
            "current_phase_c,voltage_harmonic_a,voltage_harmonic_b," +
            "voltage_harmonic_c,current_harmonic_a,current_harmonic_b," +
            "current_harmonic_c,factor_a,factor_b,factor_c," +
            "no_power_a,no_power_b,no_power_c,power_a,power_b," +
            "power_c,apparent_a,apparent_b,apparent_c," +
            "power_all,energy_a,energy_b,energy_c,energy," +
            "signal_g,failure,failure_a,failure_b,failure_c," +
            "create_time" +
            ") VALUES (" +
            "#{deviceId},#{voltageA},#{voltageB},#{voltageC}," +
            "#{errUnder},#{errOver},#{currentA},#{currentB},#{currentC}," +
            "#{errCurrent},#{leakage},#{errLeakValue},#{tempA}," +
            "#{tempB},#{tempC},#{tempN},#{errTempValue}," +
            "#{arcA},#{arcB},#{arcC},#{errArcValue}," +
            "#{voltageBalance},#{errVoltageBalance}," +
            "#{currentBalance},#{errCurrentBalance}," +
            "#{hzA},#{hzB},#{hzC},#{voltagePhaseA},#{voltagePhaseB}," +
            "#{voltagePhaseC},#{currentPhaseA},#{currentPhaseB}," +
            "#{currentPhaseC},#{voltageHarmonicA},#{voltageHarmonicB}," +
            "#{voltageHarmonicC},#{currentHarmonicA},#{currentHarmonicB}," +
            "#{currentHarmonicC},#{factorA},#{factorB},#{factorC}," +
            "#{noPowerA},#{noPowerB},#{noPowerC},#{powerA},#{powerB}," +
            "#{powerC},#{apparentA},#{apparentB},#{apparentC}," +
            "#{powerAll},#{energyA},#{energyB},#{energyC},#{energy}," +
            "#{signalG},#{failure},#{failureA},#{failureB},#{failureC}," +
            "#{createTime}" +
            ")")
    Integer insertThreePases(ThreePhase threePhase);

    //在原有的基础上修改信息
    @Insert("INSERT INTO `good_source`.`t_taizhou` " +
            "(`Device_Code`, `Create_Time`, `A_Voltage`, `B_Voltage`, " +
            "`C_Voltage`, `A_Current`, `B_Current`, `C_Current`, `A_Temp`, `B_Temp`," +
            " `C_Temp`, `N_Temp`, `A_Power`, `B_Power`, `C_Power`, `GPRS`) " +
            "VALUES (#{deviceCode}, #{createTime}, #{voltageA}, #{voltageB}, #{voltageC}," +
            " #{currentA}, #{currentB}, #{currentC}, #{tempA}, #{tempB}, #{tempC}, " +
            "#{tempN}, #{powerA}, #{powerB}, #{powerC}, #{GPRS}) " +
            "ON DUPLICATE KEY UPDATE " +
            "Device_Code = VALUES(Device_Code)," +
            "A_Voltage = VALUES(A_Voltage)," +
            "B_Voltage = VALUES(B_Voltage)," +
            "C_Voltage = VALUES(C_Voltage)," +
            "A_Current = VALUES(A_Current)," +
            "B_Current = VALUES(B_Current)," +
            "C_Current = VALUES(C_Current)," +
            "A_Temp = VALUES(A_Temp)," +
            "B_Temp = VALUES(B_Temp)," +
            "C_Temp = VALUES(C_Temp)," +
            "N_Temp = VALUES(N_Temp)," +
            "A_Power = VALUES(A_Power)," +
            "B_Power = VALUES(B_Power)," +
            "C_Power = VALUES(C_Power)," +
            "GPRS = VALUES(GPRS)," +
            "Create_Time = VALUES(Create_Time);")
    Integer updateMessage(PoweredDevice poweredDevice);

    @Select("")
    PoweredDeviceHistory selectMessage(List<String> deviceIds);

    @Insert("INSERT INTO `good_source`.`t_taizhou_history` " +
            "(`Device_Code`, `Create_Time`, `A_Voltage`, `B_Voltage`, " +
            "`C_Voltage`, `A_Current`, `B_Current`, `C_Current`, `A_Temp`, `B_Temp`," +
            " `C_Temp`, `N_Temp`, `A_Power`, `B_Power`, `C_Power`, `GPRS`) " +
            "VALUES (#{deviceCode}, #{createTime}, #{voltageA}, #{voltageB}, #{voltageC}," +
            " #{currentA}, #{currentB}, #{currentC}, #{tempA}, #{tempB}, #{tempC}, " +
            "#{tempN}, #{powerA}, #{powerB}, #{powerC}, #{GPRS});")
    Integer insertMessage(PoweredDevice poweredDevice);

}
