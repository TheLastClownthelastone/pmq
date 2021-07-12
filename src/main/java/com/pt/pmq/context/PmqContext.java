package com.pt.pmq.context;

import cn.hutool.core.io.FileUtil;
import com.pt.pmq.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author nate-pt
 * @date 2021/7/10 16:22
 * @Since 1.8
 * @Description pmq上下文
 */
@Slf4j
public class PmqContext {

    private Map<String, Queue> queueMap = new ConcurrentHashMap<>();

    private PmqContext() {
    }

    public static PmqContext getInstance() {
        return PmqContextPropdier.INSTANCE;
    }

    /**
     * 如果是有pmq.pt文件的话
     * 项目加载的时候将pmq.pt文件中的队列加载到queueMap中
     * <p>
     * 没有的则创建pmq.pt文件
     */
    public void doInit() {
        log.info("【系统初始化pmq.pt文件。。。。。。。。。。。。】");
        HavePtFile havePmqPtFile = this::havePmqPtFile;
        InputStream inputStream = havePmqPtFile.getInputStream();
        // 说明刚刚开启项目
        if (inputStream == null) {
            log.info("系统创建pmq.pt文件。。。。。。。。。。。。。。。");
            // 创建文件
            createPtFile();
        } else {
            log.info("【系统解析pmq.pt文件。。。。。。。。。。。。。。】");
            // 把有的队列进行加载
            parsePtFile(inputStream);
        }

    }


    /**
     * 判断是否存在pmq.pt文件
     *
     * @return
     */
    private InputStream havePmqPtFile() {
        InputStream resourceAsStream = PmqContext.class.getClassLoader().getResourceAsStream("pmq.pt");
        return resourceAsStream;
    }

    /**
     * 创建ptFile文件
     */
    private void createPtFile() {
        try {
            new File(SystemConstant.PMQPTFILE).createNewFile();
        } catch (IOException e) {
            log.error("【pmq.pt文件创建失败】");
        }
    }

    /**
     * 解析自定义.pt文件
     *
     * @param inputStream
     */
    private void parsePtFile(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String s = null;
            // 往内置map中设置队列名称
            while ((s = bufferedReader.readLine()) != null) {
                queueMap.put(s,new LinkedBlockingDeque());
            }

        } catch (Exception e) {
            log.error("【文件解析失败。。。。。。。。。。】");
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(inputStream);
        }

    }


    private static final class PmqContextPropdier {
        private static final PmqContext INSTANCE = new PmqContext();
    }

    @FunctionalInterface
    private interface HavePtFile {
        /**
         * 获取文件流
         *
         * @return
         */
        InputStream getInputStream();
    }


    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources");
        File file1 = new File(file.getAbsolutePath() + File.separatorChar + "1.txt");
        file1.createNewFile();
        System.out.println();
    }

}
