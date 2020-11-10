package com.ags.lumosframework.ui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadArithmetic {

    public Logger log = LoggerFactory.getLogger(ReadArithmetic.class);
    public static final String RESULT_DATA = "OK";

    public static final int SLEEP_TIME = 500;
    /**
     * 额外的路径文件夹
     */
    public final static String EXTRA_PATH = "EXTRA";

    public final static String PASS_PATH = "PASS";

    public final static String ERROR_PATH = "ERROR";

    public final static String RUBBISH_PATH = "RUBBISH";

    private SimpleDateFormat simpleDateFormat;

    public final static String YYYYMMDD = "yyyyMMdd";

    /**
     * 文件归档 归档完成 删除源文件
     *
     * @param file
     *            源文件
     * @param
     * @param analysisType
     *            归档类型
     */
    public void pigeonHoleFile(File file, String analysisType, String holePath, FileUtil fileUtil) {
        /**
         * 按照天生成文件夹
         */
        simpleDateFormat = new SimpleDateFormat(YYYYMMDD);
        holePath = fileUtil.ifNoCreateDir(holePath + File.separator + EXTRA_PATH);// 第1目录是否存在 这个是为了分离监控目录以及归档目录一样的情况
        holePath = fileUtil.ifNoCreateDir(holePath + File.separator + analysisType);// 第2目录是否存在
        /**
         * 按照天分类的路径是否存在
         */
        String path = fileUtil.ifNoCreateDir(holePath + File.separator + simpleDateFormat.format(new Date()));
        path = path + File.separator + file.getName();
        File dest = new File(path);
        try {
            // (1)判断是否可以归档（是否被占用）
            this.isOccupied(file, dest, fileUtil);
        } catch (Exception e) {
            log.error("归档异常：" + e);
        }
        simpleDateFormat = null;
    }

    /**
     * 判断文件是否被占用
     *
     * @param file
     */
    private void isOccupied(File file, File dest, FileUtil fileUtil) {

        new Thread(new Runnable() {

            private static final int Limit = 10;
            private int count = 1;

            @Override
            public void run() {
                boolean canMove = false;
                try {
                    while (!canMove) {
                        if (count > Limit) {
                            break;
                        }
                        try {
                            fileUtil.copyFileUsingFileChannels(file, dest);
                            /**
                             * 删除源文件
                             */
                            fileUtil.deleteFile(file);
                            canMove = true;
                        } catch (Exception e) {
                            canMove = false;
                            log.warn("move hold file  err");
                        }
                        ++count;
                        Thread.sleep(SLEEP_TIME);
                    }
                } catch (Exception e) {
                    log.warn("判断占用异常  isOccupied  has  failed");
                }
            }
        }).start();
    }

//    /**
//     * 记录错误的日志
//     *
//     * @param type
//     * @param analysisType
//     * @param infos
//     * @param options
//     */
//    public void insertErrorLog(EquipmentTypes type, String infos, String options) {
//        if (tbErrorLogService != null) {
//            tbErrorLogService.createErrorLog(type.getName(), TBFileUtil.getHostName(), infos, options);
//        }
//    }

    public void logError(String holePath, FileUtil fileUtil, Exception e) {
        simpleDateFormat = new SimpleDateFormat(YYYYMMDD);
        String fileName = "log_" + simpleDateFormat.format(new Date()) + ".txt";
        String content = "" + new Date() + " "  + fileUtil.getStackTraceInfo(e);
        fileUtil.writeToFile(content, new File(holePath + File.separator + fileName));
    }


}
