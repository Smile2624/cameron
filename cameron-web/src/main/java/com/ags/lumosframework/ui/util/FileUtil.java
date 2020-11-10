package com.ags.lumosframework.ui.util;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtil {

    public void deleteFile(File file) {
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String ifNoCreateDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 拷贝文件
     *
     * @param fromFile
     * @param toPath
     * @throws IOException
     */
    public void copyToDir(File fromFile, String toPath) throws IOException {
        File toFile = new File(toPath);
        if (!toFile.exists()) {
        }
        this.copyFile(fromFile, toFile);
    }

    /**
     * 拷贝文件
     *
     * @param fromFile
     * @param toFile
     * @throws IOException
     */
    public void copyFile(File fromFile, File toFile) throws IOException {
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n = 0;
        while ((n = ins.read(b)) != -1) {
            out.write(b, 0, n);
        }

        ins.close();
        out.close();
    }

    /**
     * 写日志 到文件
     *
     * @param content
     * @param file
     */
    public void writeToFile(String content, File file) {

        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            if (file.exists()) {
                writer.append(content);
            } else {
                file.createNewFile();
                writer.write(content);
            }
        } catch (IOException e) {
            //

        } finally {
            flushAndCloseWriter(writer);
        }
    }

    private void flushAndCloseWriter(FileWriter writer) {
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {

        }
    }

    /**
     * 获取e.printStackTrace() 的具体信息，赋值给String 变量，并返回
     *
     * @param e Exception
     * @return e.printStackTrace() 中 的信息
     */
    public String getStackTraceInfo(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);// 将出错的栈信息输出到printWriter中
            pw.flush();
            sw.flush();
            return (sw == null ? "" : sw.toString());
        } catch (Exception ex) {
            return "发生错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    //
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * 拷贝大文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }


}
