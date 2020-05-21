package cn.zhuchuangsoft.footstone.utils;

import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:WarmingMsgUtils
 * Package:cn.zhuchuangsoft.footstone.utils
 * Title:
 * Dsicription:
 * 用来维护warmingMsg
 *
 * @Date:2020/5/4 15:17)
 * @Author:1012518118@qq.com
 */
@Component
public class WarmingMsgUtils {

    private static IDeviceService deviceServiceImpl;

    /**
     * 根据WarmingCode等信息，获取warmingMsg
     *
     * @param warming
     * @return
     */
    public static Warming getWarmingMsg(Warming warming) {
        if (warming == null)
            return warming;
        StringBuilder msg = new StringBuilder();
        //获取到时间
        String time = getString2WarmingMsgtime(warming.getWarmingTime());
        //获取警告code
        String warmingCode = warming.getWarmingCode();
        //获取设备code
        String deviceCode = warming.getDeviceCodes();
        if (deviceCode == null || "".equals(deviceCode))
            return warming;
        //根据deviceCode获取到设备名称
        String name = deviceServiceImpl.selectDeviceName(deviceCode);
        //查询安装地址
        String installPlace = deviceServiceImpl.selInstallPlaceByDeviceCode(warming.getDeviceCodes());
        msg.append(time)
                .append(installPlace)
                .append(name)
                .append("发生")
                .append(warmingCode);
        warming.setWarmingMsg(msg.toString());
        return warming;
    }

    static String getString2WarmingMsgtime(String date) {
        if (date == null || date == "")
            return date;
        return date2String(string2Date(date, "yyyy-MM-dd HH:mm:ss"), "yyyy年MM月dd日HH时mm分，");
    }

    public static Date string2Date(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date reDate = null;
        try {
            reDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reDate;
    }

    public static String date2String(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(date);
        return dateString;
    }

    @Autowired
    public void setIDeviceService(IDeviceService deviceServiceImpl) {
        WarmingMsgUtils.deviceServiceImpl = deviceServiceImpl;
    }
}
