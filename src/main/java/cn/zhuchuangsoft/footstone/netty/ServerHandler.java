package cn.zhuchuangsoft.footstone.netty;

import cn.zhuchuangsoft.footstone.entity.device.PoweredDevice;
import cn.zhuchuangsoft.footstone.entity.device.ThreePhase;
import cn.zhuchuangsoft.footstone.service.IDeviceMessageService;
import cn.zhuchuangsoft.footstone.service.IDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;

@Component
@Scope("prototype")
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    //  将当前客户端连接 存入map   实现控制设备下发 参数
    public static Map<String, ChannelHandlerContext> map = new HashMap<String, ChannelHandlerContext>();
    @Autowired
    private IDeviceWarmingService deviceWarmingService;
    @Autowired
    private IDeviceMessageService deviceMessageService;

    public static String txfloat(int a, int b) {
        // TODO 自动生成的方法存根

        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数

        return df.format((float) a / b);

    }

    public static void main(String[] args) {
        short a = 1234;
        System.out.println((short) (a - 40));
    }

    /**
     * 获取数据
     *
     * @param ctx 上下文
     * @param msg 获取的数据
     * @throws UnsupportedEncodingException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (StringUtils.isEmpty(msg)) {
            ctx.flush();
            return;
        }
        ByteBuf readMessage = (ByteBuf) msg;
        byte[] bytes = new byte[readMessage.readableBytes()];
        readMessage.readBytes(bytes);
        //   System.out.println("date: "+new  Date()+"-----"+bytes.toString());
        //解析客户端数据
        // System.out.println("------------"+StringUtil.getHex(bytes, true));
        String str = StringUtil.judge(bytes);
        //System.out.println("-----str:"+str);
        if (str == null) {
            ctx.flush();
            return;
        }
        if (str.equals("02")) {
            ThreePhase phase = new ThreePhase(bytes);
            phase.setVoltageA(Short.parseShort(txfloat(phase.getVoltageA(), 10)));
            phase.setVoltageB(Short.parseShort(txfloat(phase.getVoltageB(), 10)));
            phase.setVoltageC(Short.parseShort(txfloat(phase.getVoltageC(), 10)));

            phase.setTempA((short) (phase.getTempA() - 40));
            phase.setTempC((short) (phase.getTempC() - 40));
            phase.setTempB((short) (phase.getTempB() - 40));
            phase.setTempN((short) (phase.getTempN() - 40));

            deviceMessageService.insertMessage(phase);

        }

        if (str.equals("10")) {
            ThreePhase phase = new ThreePhase(bytes);
            phase.setVoltageA(Short.parseShort(txfloat(phase.getVoltageA(), 10)));
            phase.setVoltageB(Short.parseShort(txfloat(phase.getVoltageB(), 10)));
            phase.setVoltageC(Short.parseShort(txfloat(phase.getVoltageC(), 10)));

            phase.setTempA((short) (phase.getTempA() - 40));
            phase.setTempC((short) (phase.getTempC() - 40));
            phase.setTempB((short) (phase.getTempB() - 40));
            phase.setTempN((short) (phase.getTempN() - 40));

            PoweredDevice poweredDevice = new PoweredDevice(bytes);
            poweredDevice.setVoltageA(Short.parseShort(txfloat(poweredDevice.getVoltageA(), 10)));
            poweredDevice.setVoltageB(Short.parseShort(txfloat(poweredDevice.getVoltageB(), 10)));
            poweredDevice.setVoltageC(Short.parseShort(txfloat(poweredDevice.getVoltageC(), 10)));

            poweredDevice.setTempA((short) (poweredDevice.getTempA() - 40));
            poweredDevice.setTempC((short) (poweredDevice.getTempC() - 40));
            poweredDevice.setTempB((short) (poweredDevice.getTempB() - 40));
            poweredDevice.setTempN((short) (poweredDevice.getTempN() - 40));

            deviceWarmingService.checkDeviceWarming(poweredDevice);
            deviceMessageService.insertMessage(phase);


        }
        //设备请求的 服务器端的地址   用作监听设备请求的那个端口
        //String servicePort=ctx.channel().localAddress().toString();
        //刷新缓存区
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().parent().close();
//        cause.printStackTrace();
//        ctx.close();
    }


}
