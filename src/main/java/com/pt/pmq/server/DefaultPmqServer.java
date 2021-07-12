package com.pt.pmq.server;

import cn.hutool.core.convert.Convert;
import com.pt.pmq.init.InitActuator;
import com.pt.pmq.util.PropertiesUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nate-pt
 * @date 2021/7/10 15:56
 * @Since 1.8
 * @Description
 */
@Slf4j
public class DefaultPmqServer implements PmqServer{



    private InitActuator initActuator = InitActuator.getInstance();

    private StartActuator startActuator = new StartActuator();

    private Integer port = Convert.convert(Integer.class,PropertiesUtils.get("pmq.port"));

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    private DefaultPmqServer(){}

    @Override
    public void start() {
        try {
            startActuator.doStart0();
        } catch (InterruptedException e) {
            log.error("【pmq启动失败】",e);
            System.exit(0);
        }
    }

    @Override
    public void preStart() {
        initActuator.doInt();
    }

    /**
     * 启动执行器，将启动逻辑代码写入私有内部类中进行保护
     */
    private class StartActuator{

        StartActuator(){
            boss = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        }


        private void doStart0() throws InterruptedException {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // pmq 协议编解码器
                            pipeline.addLast();
                        }
                    });

            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info("【pmq启动成功】,端口:{}",port);
                }
            });
            // 通道异步关闭产生阻塞
            sync.channel().closeFuture().sync();
        }

    }
}
