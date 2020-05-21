package cn.zhuchuangsoft.footstone.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class NettyStart {
    @Resource
    private ServerHandler serverHandler;
    //第一个线程组是用于接收Client连接的
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    //第二个线程组是用于实际的业务处理操作的
    private EventLoopGroup workGroup = new NioEventLoopGroup();


    /**
     * 启动netty服务
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        //创建一个启动NIO服务的辅助启动类ServerBootstrap
        ServerBootstrap b = new ServerBootstrap();
        //绑定两个线程组
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)//需要指定使用NioServerSocketChannel这种类型的通道
                .option(ChannelOption.SO_BACKLOG, 128)//设置TCP连接的缓冲区
                .childOption(ChannelOption.TCP_NODELAY, true) //来禁用nagle算法，不等待，立即发送
                //使用childHandler 去绑定具体的事件处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Decoder());
                        //将自定义的serverHandler加入到管道中去（多个）
                        socketChannel.pipeline().addLast(serverHandler);
                    }
                });
        //  .childOption(ChannelOption.SO_KEEPALIVE, true);//保持连接
        ChannelFuture future = b.bind(8081).sync();//开启需要监听 的端口
        //ChannelFuture future1 = b.bind(9899).sync();//开启需要监听 的端口 多开端口
        if (future.isSuccess()) {
            System.out.println("启动 8081 成功");
        }

    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workGroup.shutdownGracefully().syncUninterruptibly();
        System.out.println("关闭 Netty 成功");
    }

}
