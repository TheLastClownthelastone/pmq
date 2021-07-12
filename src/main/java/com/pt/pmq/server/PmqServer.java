package com.pt.pmq.server;

/**
 * @author nate-pt
 * @date 2021/7/10 15:54
 * @Since 1.8
 * @Description
 */
public interface PmqServer {

    /**
     * 服务器启动
     */
    void start();

    /**
     * 启动之前准备工作
     */
    void preStart();
}
