package com.ags.lumosframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@Configuration
@EnableScheduling
public class CameronApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CameronApp.class, args);
    }

    public static File getLastModified(String directoryFilePath) {
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CameronApp.class);
    }

    //@Scheduled(cron = "0 5 * * * *")
    public void inslotImport() {
        File inslotFile = getLastModified("\\\\xprthq1.cc//c.coopcam.com\\WHMAIL\\A419\\Inspection Lot");
        try {
            BufferedReader buf = new BufferedReader(new FileReader(inslotFile.getAbsolutePath()));
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/amax_cameron?useSSL=false", "root", "123456");
            String lineJustFetched = null;
            String[] wordsArray = null;
            int count = 0;
            lineJustFetched = buf.readLine();
            while (true) {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null) {
                    break;
                } else {
                    wordsArray = lineJustFetched.split("\t");
                    ResultSet rs = con.createStatement().executeQuery("select * from purchasing_order where SAP_INSPECTION_LOT='" + wordsArray[0].replaceAll("^0+", "") + "'");
                    rs.last();
                    if (rs.getRow() == 0) {
                        rs = con.createStatement().executeQuery("select ID from purchasing_order");
                        long newId = 0L;
                        while (rs.next()) {
                            newId = rs.getLong("id");
                        }
                        newId = newId + 1;
                        String rev = wordsArray[5];
                        if (wordsArray[5].equals("")) {
                            rs = con.createStatement().executeQuery("select SPARE_PART_REV from spare_part where SPARE_PART_NO='" + wordsArray[1] + "' order by SPARE_PART_REV desc");
                            rs.first();
                            if (rs.getRow() > 0) {
                                rev = rs.getString("SPARE_PART_REV");
                            }
                        }
                        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String sqlUpdate = "INSERT INTO purchasing_order (ID, COMPANY_ID, " +
                                "CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, " +
                                "DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, " +
                                "DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, SAP_INSPECTION_LOT, MATERIAL_NO, " +
                                "MATERIAL_QUANTITY, MATERIAL_UNIT, MATERIAL_DESC, MATERIAL_REV, PURCHASING_NO, " +
                                "PURCHASING_ITEM_NO, VENDOR_NAME, DIMENSION_CHECKED, HARDNESS_CHECKED, INSPECTION_QUANTITY, " +
                                "VISUAL_CHECKED) VALUES ('" + newId + "', '0', '" + currentTime + "', '1', 'admin', 'Admin', '127.0.0.1', " +
                                "'-1', '" + currentTime + "', '1', '127.0.0.1', 'admin', 'Admin', '-1', '0', false, '" + wordsArray[0].replaceAll("^0+", "")
                                + "', '" + wordsArray[1] + "', '" + wordsArray[2].substring(0, wordsArray[2].length() - 4) + "', '" + wordsArray[3] + "', " +
                                "'" + wordsArray[4].replaceAll("'", "''") + "', '" + rev + "', '" + wordsArray[6] + "', '" + wordsArray[7]//.replaceAll("^0+", "")
                                + "', '" + wordsArray[8] + "', false, false, '0', false);";
                        con.createStatement().executeUpdate(sqlUpdate);
                        System.out.println(sqlUpdate);
                        count = count + 1;
                    }
//                    stmt.executeUpdate("INSERT INTO product_order (ID, COMPANY_ID, CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, PRODUCT_ORDER_ID, PRODUCT_ID, PRODUCT_VERSION_ID, PRODUCT_DESC, PRODUCT_QTY, ROUTING_GROUP, INNER_GROUP_NO) VALUES (" + newId + ", 0, '2020-03-08 12:00:00', 1, 'admin', 'Admin', '127.0.0.1', -1, '2020-03-08 12:00:00', 1, '127.0.0.1', 'admin', 'Admin', -1, 0, false, " + time + ", 1000010000, 1, 'parttest', 1, 50000, 1)");
                }
            }
            System.out.println(count + " lines of data for inslot inserted!");
            con.close();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Scheduled(cron = "0 35 * * * *")
    public void productionorderImport() {
        File poFile = getLastModified("\\\\xprthq1.ccc.coopcam.com\\WHMAIL\\A419\\Production Order");
        try {
            BufferedReader buf = new BufferedReader(new FileReader(poFile.getAbsolutePath()));
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/amax_cameron?useSSL=false", "root", "123456");
            String lineJustFetched = null;
            String[] wordsArray = null;
            int count = 0;
            lineJustFetched = buf.readLine();
            while (true) {
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null) {
                    break;
                } else {
                    wordsArray = lineJustFetched.split("\t");
                    if (wordsArray[0].replaceAll("^0+", "").startsWith("12")) {
                        ResultSet rs = con.createStatement().executeQuery("select * from product_order where PRODUCT_ORDER_ID='" + wordsArray[0].replaceAll("^0+", "") + "'");
                        rs.last();
                        if (rs.getRow() == 0) {
                            rs = con.createStatement().executeQuery("select ID from product_order");
                            long newId = 0L;
                            while (rs.next()) {
                                newId = rs.getLong("id");
                            }
                            newId = newId + 1;
                            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            String sqlUpdate = "INSERT INTO product_order (ID, COMPANY_ID, " +
                                    "CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, " +
                                    "DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, " +
                                    "DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, PRODUCT_ORDER_ID, PRODUCT_ID, " +
                                    "PRODUCT_VERSION_ID, PRODUCT_DESC, PRODUCT_QTY, ROUTING_GROUP, INNER_GROUP_NO, " +
                                    "CUSTOMER_CODE, SALES_ORDER, SALES_ORDER_ITEM,PAINT_SPECIFICATION) " +
                                    "VALUES ('" + newId + "', '0', '" + currentTime + "', '1', 'admin', 'Admin', '127.0.0.1', " +
                                    "'-1', '" + currentTime + "', '1', '127.0.0.1', 'admin', 'Admin', '-1', '0', false, '" + wordsArray[0].replaceAll("^0+", "")
                                    + "', '" + wordsArray[1] + "', '" + wordsArray[2] + "', '" + wordsArray[3].replaceAll("'", "''") + "', " +
                                    "'" + wordsArray[4].substring(0, wordsArray[4].length() - 4) + "', '" + wordsArray[6] + "', '" + wordsArray[7].replaceAll("^0+", "") +
                                    "', '/', '/', '/', '/');";
                            con.createStatement().executeUpdate(sqlUpdate);
                            System.out.println(sqlUpdate);
                            count = count + 1;
                        }
                    }
//                    stmt.executeUpdate("INSERT INTO product_order (ID, COMPANY_ID, CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, PRODUCT_ORDER_ID, PRODUCT_ID, PRODUCT_VERSION_ID, PRODUCT_DESC, PRODUCT_QTY, ROUTING_GROUP, INNER_GROUP_NO) VALUES (" + newId + ", 0, '2020-03-08 12:00:00', 1, 'admin', 'Admin', '127.0.0.1', -1, '2020-03-08 12:00:00', 1, '127.0.0.1', 'admin', 'Admin', -1, 0, false, " + time + ", 1000010000, 1, 'parttest', 1, 50000, 1)");
                }
            }
            System.out.println(count + " lines of data for po inserted!");
            con.close();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "0 * * * * *")
//    public void routingImport() {
//        File rtgFile = getLastModified("\\\\xprthq1.ccc.coopcam.com\\WHMAIL\\A419\\Routing");
//        try {
//            BufferedReader buf = new BufferedReader(new FileReader(rtgFile.getAbsolutePath()));
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/amax_cameron?useSSL=false", "root", "123456");
//            String lineJustFetched = null;
//            String[] wordsArray = null;
//            int count = 0;
//            lineJustFetched = buf.readLine();
//            while (true) {
//                lineJustFetched = buf.readLine();
//                if (lineJustFetched == null) {
//                    break;
//                } else {
//                    wordsArray = lineJustFetched.split("\t");
//                    if (wordsArray[0].replaceAll("^0+", "").startsWith("12")) {
//                        ResultSet rs = con.createStatement().executeQuery("select * from product_order where PRODUCT_ORDER_ID='" + wordsArray[0].replaceAll("^0+", "") + "'");
//                        rs.last();
//                        if (rs.getRow() == 0) {
//                            rs = con.createStatement().executeQuery("select ID from product_order");
//                            long newId = 0L;
//                            while (rs.next()) {
//                                newId = rs.getLong("id");
//                            }
//                            newId = newId + 1;
//                            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                            String sqlUpdate = "INSERT INTO product_order (ID, COMPANY_ID, " +
//                                    "CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, " +
//                                    "DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, " +
//                                    "DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, PRODUCT_ORDER_ID, PRODUCT_ID, " +
//                                    "PRODUCT_VERSION_ID, PRODUCT_DESC, PRODUCT_QTY, ROUTING_GROUP, INNER_GROUP_NO) " +
//                                    "VALUES ('" + newId + "', '0', '" + currentTime + "', '1', 'admin', 'Admin', '127.0.0.1', " +
//                                    "'-1', '" + currentTime + "', '1', '127.0.0.1', 'admin', 'Admin', '-1', '0', false, '" + wordsArray[0].replaceAll("^0+", "")
//                                    + "', '" + wordsArray[1] + "', '" + wordsArray[2] + "', '" + wordsArray[3].replaceAll("'", "''") + "', " +
//                                    "'" + wordsArray[4].substring(0, wordsArray[4].length() - 4) + "', '" + wordsArray[6] + "', '" + wordsArray[7].replaceAll("^0+", "") + "');";
////                            con.createStatement().executeUpdate(sqlUpdate);
//                            System.out.println(sqlUpdate);
//                            count = count + 1;
//                        }
//                    }
////                    stmt.executeUpdate("INSERT INTO product_order (ID, COMPANY_ID, CREATE_TIME, CREATE_USER_ID, CREATE_USER_NAME, CREATE_USER_FULL_NAME, CREATE_IP, DTS_CREATION_BID, LM_TIME, LM_USER_ID, LM_IP, LM_USER_NAME, LM_USER_FULL_NAME, DTS_MODIFIED_BID, DELETE_USER_ID, DELETED, PRODUCT_ORDER_ID, PRODUCT_ID, PRODUCT_VERSION_ID, PRODUCT_DESC, PRODUCT_QTY, ROUTING_GROUP, INNER_GROUP_NO) VALUES (" + newId + ", 0, '2020-03-08 12:00:00', 1, 'admin', 'Admin', '127.0.0.1', -1, '2020-03-08 12:00:00', 1, '127.0.0.1', 'admin', 'Admin', -1, 0, false, " + time + ", 1000010000, 1, 'parttest', 1, 50000, 1)");
//                }
//            }
//            System.out.println(count + " lines of data for po inserted!");
//            con.close();
//            buf.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
