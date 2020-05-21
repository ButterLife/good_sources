package cn.zhuchuangsoft.footstone.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

/**
 * 自定义解码器
 */
public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("date: " + new Date() + "-----" + in.readableBytes() + "in.readInt():");
        //取到头部第一个值
        String one = Integer.toHexString(in.getByte(0) & 0xFF);
        //取到头部第二个值
        String two = Integer.toHexString(in.getByte(1) & 0xFF);
        //过滤没有用的数据
        if (one.equals("35") && two.equals("35")) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (one.equals("5a") && !two.equals("87")) {
            in.skipBytes(in.readableBytes());
            return;
        }
        //满足条件的往下传，否则等待下一个数据
        if (in.readableBytes() == 135 && (one.equals("5a") && two.equals("87"))) {
            ByteBuf byteBuf = in.retainedDuplicate();
            //输出到下一个管道
            out.add(byteBuf);
            in.skipBytes(in.readableBytes());
            return;
        }
    }


}
