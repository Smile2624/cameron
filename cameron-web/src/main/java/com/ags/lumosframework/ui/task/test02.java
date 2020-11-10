package com.ags.lumosframework.ui.task;

import java.io.*;

import jcifs.smb.*;


public class test02 {

//    public static void main(String[] args) throws Exception {
//
//        String filePath = "\\\\" + "119.119.118.135"   + "\\Users\\spring_wang\\" + "111.txt";
//        File csv = new File(filePath);
//        System.out.println(new FileInputStream(csv));
//        BufferedReader br = new BufferedReader(new FileReader(csv));//构造一个BufferedReader
//        StringBuilder result = new StringBuilder();
//        String s = null;
//        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//            result.append(System.lineSeparator()+s);
//        }
//        br.close();
//        System.out.println(result.toString());
//    }

    //    从共享目录下载文件
    @SuppressWarnings("unused")
    public static void smbGet(String remoteUrl,String localDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl);
            if(remoteFile==null){
                System.out.println("共享文件不存在");
                return;
            }
            String fileName = remoteFile.getName();
            File localFile = new File(localDir+File.separator+fileName);
            jcifs.Config.setProperty( "jcifs.smb.lmCompatibility", "2");
            jcifs.Config.setProperty("jcifs.smb.client.responseTimeout", "120000");
            jcifs.Config.setProperty("jcifs.smb.client.soTimeout", "120000");
            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));

            out = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[1024];
            while(in.read(buffer)!=-1){
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //向共享目录上传文件
    public static void smbPut(String remoteUrl,String localFilePath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File localFile = new File(localFilePath);

            String fileName = localFile.getName();
            SmbFile remoteFile = new SmbFile(remoteUrl+"/"+fileName);
            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
            byte[] buffer = new byte[1024];
            while(in.read(buffer)!=-1){
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}