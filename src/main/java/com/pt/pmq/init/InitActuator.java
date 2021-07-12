package com.pt.pmq.init;

import com.pt.pmq.context.PmqContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author nate-pt
 * @date 2021/7/10 15:57
 * @Since 1.8
 * @Description 初始化执行器
 */
@Slf4j
public class InitActuator {

    private PmqContext pmqContext = PmqContext.getInstance();

    /**
     * 执行锁，判断初始化方法有没有进行执行
     */
    private static AtomicBoolean checkExcute =  new AtomicBoolean(false);

    private InitActuator(){}



    /**
     * 获取执行器
     * @return
     */
    public static InitActuator getInstance(){
        return InitActuatorProdier.INSTANCE;
    }




    public void doInt(){
        if (checkExcute.compareAndSet(false,true)) {
            pmqContext.doInit();
        }else {
            log.error("【系统已经进行了初始化请勿重复操作。。。。。。。。。。。。】");
        }
    }




    private static  final  class InitActuatorProdier{
        private static final InitActuator INSTANCE = new InitActuator();
    }




}
