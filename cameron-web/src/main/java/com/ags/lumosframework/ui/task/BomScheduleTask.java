package com.ags.lumosframework.ui.task;

import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.TextBlob;
import com.ags.lumosframework.sdk.service.api.ITextBlobService;
import com.ags.lumosframework.service.IBomService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.service.ISparePartService;
import com.ags.lumosframework.ui.util.FileUtil;
import com.ags.lumosframework.ui.util.ReadArithmetic;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class BomScheduleTask implements SchedulingConfigurer {

    @Autowired
    private ITextBlobService textBlobService;
    @Autowired
    private IBomService bomService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IProductionOrderService orderService;

    @Value("${bom.schedule.cron}")
    private String cron;

    @Value("${bom.schedule.root}")
    private String path;

    @Value("${bom.schedule.hold}")
    private String holdPath;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //1.添加任务内容(Runnable)
        scheduledTaskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    System.out.println("<------BOM schedule start-------->" + LocalDateTime.now().toLocalTime());
                    RequestInfo current = RequestInfo.current();
                    boolean reset = false;
                    if (current == null) {
                        reset = true;
                        RequestInfo tmpRequestInfo = new RequestInfo();
                        tmpRequestInfo.setUserId(-1);
                        tmpRequestInfo.setUserFirstName("admin");
                        tmpRequestInfo.setUserIpAddress("0:0:0:0");
                        tmpRequestInfo.setUserLastName("");
                        tmpRequestInfo.setUserZoneId(ZoneId.systemDefault());
                        tmpRequestInfo.setRequestZonedDateTime(ZonedDateTime.now(ZoneId.of("Asia/Shanghai")));
                        tmpRequestInfo.setUserName("");
                        tmpRequestInfo.setUserLocal(Locale.CHINA);
                        RequestInfo.set(tmpRequestInfo);
                    }
                    try {
                        bomSchedule();
                    } finally {
                        if (reset) {
                            RequestInfo.set(null);
                        }
                    }

                    System.out.println("<------schedule end-------->" + LocalDateTime.now().toLocalTime());
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期 //2.2 合法性校验.
                    TextBlob cronTextBlob = textBlobService.getByName("Bom_Timer_Cron");
                    if (cronTextBlob != null && cronTextBlob.getValue() != null) {
                        cron = CronExpression.isValidExpression(cronTextBlob.getValue()) ? cronTextBlob.getValue() : cron;
                    }
                    System.out.println("<---------cron----------> = " + cron);
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    public void bomSchedule() {
        TextBlob pathTextBlob = textBlobService.getByName("Bom_File_Path");
        TextBlob holdPathTextBlob = textBlobService.getByName("Bom_File_HoldPath");
        if (pathTextBlob != null) {
            path = pathTextBlob.getValue() != null ? pathTextBlob.getValue() : path;
        }
        if (holdPathTextBlob != null) {
            holdPath = holdPathTextBlob.getValue() != null ? holdPathTextBlob.getValue() : holdPath;
        }
        File root = new File(path);
        List<File> files = new ArrayList<>();
        List<File> rubbishFiles = new ArrayList<>();
        //文件分类
        if (!root.isDirectory()) {
            String fileName = root.getName();
            if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX") || fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                files.add(root);
            } else {
                rubbishFiles.add(root);
            }
        } else {
            File[] subFiles = root.listFiles();
            for (File f : subFiles) {
                if (!f.isDirectory()) {
                    String fileName = f.getName();
                    if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX") || fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                        files.add(f);
                    } else {
                        rubbishFiles.add(f);
                    }
                }
            }
        }

        //文件解析保存，文件转移
        ReadArithmetic readArithmetic = new ReadArithmetic();
        FileUtil fileUtil = new FileUtil();
        for (File f : files) {
            InputStream in = null;
            try {
                String fileName = f.getName();
                in = new FileInputStream(f);
                if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
                    importXlsx(in);
                } else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                    importXls(in);
                }
                readArithmetic.pigeonHoleFile(f, ReadArithmetic.PASS_PATH, holdPath, fileUtil);
            } catch (IOException e) {
                readArithmetic.pigeonHoleFile(f, ReadArithmetic.ERROR_PATH, holdPath, fileUtil);
                readArithmetic.logError(holdPath, fileUtil, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        //文件转移
        for (File f : rubbishFiles) {
            readArithmetic.pigeonHoleFile(f, ReadArithmetic.RUBBISH_PATH, holdPath, fileUtil);
        }
    }

    /**
     * 导入bom信息（.xlsx）
     *
     * @param is
     * @throws IOException
     */
    public void importXlsx(InputStream is) throws IOException {
        // *****.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        Boolean isSaveFlag = false;// 控制零件开始结束
        Boolean startEndFlag = false;// 控制零件开始结束
        Boolean phantomItemXFlag = false;
        Boolean RetrospectFlag = true;//追溯标识

        String productNo = "";
        String productRev = "";
        String itemCategory = "";
        String explosionLvl = "";
        String sortString = "";
        String itemNo = "";
        String partNo = "";
        int partQty = 0;
        String baseUnit = "";
        String partRev = "";
        String legacyRev = "";
        String partDes = "";
        String longText = "";
        String phantomItem = "";
        Boolean isRetrospect = false;
        String retrospectType = "";

        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell aaItemCategory = xssfRow.getCell(0);//Item category
                XSSFCell bbExplosionLvl = xssfRow.getCell(1);//Explosion level
                XSSFCell ccSortString = xssfRow.getCell(2);//Sort String
                XSSFCell ddItemNumber = xssfRow.getCell(3);//Item Number
                XSSFCell eeComponent = xssfRow.getCell(4);//Component number
                XSSFCell ffObjectDes = xssfRow.getCell(5);//Object Description
                XSSFCell ggRevisionLvl = xssfRow.getCell(6);//Revision Level
                XSSFCell hhLegacyRev = xssfRow.getCell(7);//Legacy rev
                XSSFCell iiCompQty = xssfRow.getCell(8);//Comp. Qty (BUn)
                XSSFCell jjBaseUnit = xssfRow.getCell(9);//Base Unit of Measure
                XSSFCell kkDocumentType = xssfRow.getCell(10);//Document Type
                XSSFCell llDocument = xssfRow.getCell(11);//Document
                XSSFCell mmDocumentVer = xssfRow.getCell(12);//Document version
                XSSFCell nnDescription = xssfRow.getCell(13);//Description
                XSSFCell ooLongText = xssfRow.getCell(14);//Long Text
                XSSFCell ppPhantomItem = xssfRow.getCell(15);//Phantom item

                //0 !null
                if ((bbExplosionLvl != null && "0".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    isSaveFlag = true;
                    startEndFlag = true;

                    productNo = eeComponent.getStringCellValue();
                    productRev = ggRevisionLvl.getStringCellValue();

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "0";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                    if (sparePart == null || sparePart.isRetrospect() == false || (!"A".equalsIgnoreCase(itemCategory) && !"L".equalsIgnoreCase(itemCategory))) {
                        RetrospectFlag = false;
                    } else {
                        RetrospectFlag = true;
                    }
                    if (RetrospectFlag) {
                        isRetrospect = sparePart.isRetrospect();
                        retrospectType = sparePart.getRetrospectType();
                    }
                    //更新产品订单中最新版本号用于bom管控
                    List<ProductionOrder> orderList = orderService.getByPartNoPartNoRev(productNo, "");
                    for (ProductionOrder order : orderList) {
                        order.setNewestBomVersion(productRev);
                        if (!productRev.equals(order.getProductVersionId())) {//新版本则锁定工单
                            order.setBomLockFlag(true);
                            order.setBomCheckFlag(true);
                        }
                        orderService.save(order);
                    }
                }

                if (aaItemCategory == null || "".equals(aaItemCategory.toString().trim())) {
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                }
                //.1 null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null || "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = "";
                    explosionLvl = "";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = kkDocumentType.getStringCellValue();
                    partNo = llDocument.getStringCellValue();
                    partQty = 0;
                    baseUnit = "";
                    partRev = mmDocumentVer.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = nnDescription.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    isRetrospect = false;
                    retrospectType = "";
                }
                //.1 !null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "1";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if ("x".equalsIgnoreCase(phantomItem)) {
                        phantomItemXFlag = true;
                        isRetrospect = false;
                        retrospectType = "";
                    } else {
                        phantomItemXFlag = false;
                        if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                            SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                            if (sparePart != null) {
                                isRetrospect = sparePart.isRetrospect();
                                retrospectType = sparePart.getRetrospectType();
                            } else {
                                isRetrospect = false;
                                retrospectType = "";
                            }
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    }
                }
                //..2 null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;

                    }

                }
                //..2 !null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim())) && phantomItemXFlag) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "2";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                        SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                        if (sparePart != null) {
                            isRetrospect = sparePart.isRetrospect();
                            retrospectType = sparePart.getRetrospectType();
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    } else {
                        isRetrospect = false;
                        retrospectType = "";
                    }

                }
                //...3 null
                if ((bbExplosionLvl != null && "...3".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                    }
                }

                if (rowNum == xssfSheet.getLastRowNum()) {
                    System.out.println("$$$$$$$$$" + (rowNum + 1) + "$$$$$$$$$");
                    saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                            partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);

                }
            }
        }
    }

    public void saveDataToBom(String productNo, String productRev, String itemCategory, String explosionLvl,
                              String sortString, String itemNo, String partNo, int partQty, String baseUnit,
                              String partRev, String legacyRev, String partDes, String longText,
                              String phantomItem, boolean isRetrospect, String retrospectType) {
        Bom bomSaved = bomService.getByNoRevItemPartNoPartRev(productNo, productRev, itemNo, partNo, partRev);
        if (bomSaved == null) {
            bomSaved = new Bom();
            bomSaved.setProductNo(productNo);
            bomSaved.setProductRev(productRev);
            bomSaved.setItemNo(itemNo);
            bomSaved.setPartNo(partNo);
            bomSaved.setPartRev(partRev);
        }
        bomSaved.setItemCategory(itemCategory);
        bomSaved.setExplosionLevel(explosionLvl);
        bomSaved.setSortString(sortString);
        bomSaved.setBaseUnit(baseUnit);
        bomSaved.setLegacyRev(legacyRev);
        bomSaved.setPhantomItem(phantomItem);
        bomSaved.setPartDes(partDes);
        bomSaved.setLongText(longText);
        bomSaved.setPartQuantity(partQty);
        bomSaved.setRetrospect(isRetrospect);
        if (isRetrospect) {
            bomSaved.setRetrospectType(retrospectType);
        }
        bomService.save(bomSaved);
    }

    /**
     * 导入BOM信息（.xls）
     *
     * @param is
     * @throws IOException
     */
    public void importXls(InputStream is) throws IOException {
        // *****.xlsx
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        Boolean isSaveFlag = false;// 控制零件开始结束
        Boolean startEndFlag = false;// 控制零件开始结束
        Boolean phantomItemXFlag = false;
        Boolean RetrospectFlag = true;//追溯标识

        String productNo = "";
        String productRev = "";
        String itemCategory = "";
        String explosionLvl = "";
        String sortString = "";
        String itemNo = "";
        String partNo = "";
        int partQty = 0;
        String baseUnit = "";
        String partRev = "";
        String legacyRev = "";
        String partDes = "";
        String longText = "";
        String phantomItem = "";
        Boolean isRetrospect = false;
        String retrospectType = "";

        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                HSSFCell aaItemCategory = hssfRow.getCell(0);//Item category
                HSSFCell bbExplosionLvl = hssfRow.getCell(1);//Explosion level
                HSSFCell ccSortString = hssfRow.getCell(2);//Sort String
                HSSFCell ddItemNumber = hssfRow.getCell(3);//Item Number
                HSSFCell eeComponent = hssfRow.getCell(4);//Component number
                HSSFCell ffObjectDes = hssfRow.getCell(5);//Object Description
                HSSFCell ggRevisionLvl = hssfRow.getCell(6);//Revision Level
                HSSFCell hhLegacyRev = hssfRow.getCell(7);//Legacy rev
                HSSFCell iiCompQty = hssfRow.getCell(8);//Comp. Qty (BUn)
                HSSFCell jjBaseUnit = hssfRow.getCell(9);//Base Unit of Measure
                HSSFCell kkDocumentType = hssfRow.getCell(10);//Document Type
                HSSFCell llDocument = hssfRow.getCell(11);//Document
                HSSFCell mmDocumentVer = hssfRow.getCell(12);//Document version
                HSSFCell nnDescription = hssfRow.getCell(13);//Description
                HSSFCell ooLongText = hssfRow.getCell(14);//Long Text
                HSSFCell ppPhantomItem = hssfRow.getCell(15);//Phantom item

                //0 !null
                if ((bbExplosionLvl != null && "0".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    isSaveFlag = true;
                    startEndFlag = true;

                    productNo = eeComponent.getStringCellValue();
                    productRev = ggRevisionLvl.getStringCellValue();

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "0";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                    if (sparePart == null || sparePart.isRetrospect() == false || (!"A".equalsIgnoreCase(itemCategory) && !"L".equalsIgnoreCase(itemCategory))) {
                        RetrospectFlag = false;
                    } else {
                        RetrospectFlag = true;
                    }
                    if (RetrospectFlag) {
                        isRetrospect = sparePart.isRetrospect();
                        retrospectType = sparePart.getRetrospectType();
                    }
                    //更新产品订单中最新版本号用于bom管控
                    List<ProductionOrder> orderList = orderService.getByPartNoPartNoRev(productNo, "");
                    for (ProductionOrder order : orderList) {
                        order.setNewestBomVersion(productRev);
                        if (!productRev.equals(order.getProductVersionId())) {//新版本则锁定工单
                            order.setBomLockFlag(true);
                            order.setBomCheckFlag(true);
                        }
                        orderService.save(order);
                    }
                }

                if (aaItemCategory == null || "".equals(aaItemCategory.toString().trim())) {
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                }
                //.1 null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null || "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = "";
                    explosionLvl = "";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = kkDocumentType.getStringCellValue();
                    partNo = llDocument.getStringCellValue();
                    partQty = 0;
                    baseUnit = "";
                    partRev = mmDocumentVer.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = nnDescription.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    isRetrospect = false;
                    retrospectType = "";
                }
                //.1 !null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "1";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if ("x".equalsIgnoreCase(phantomItem)) {
                        phantomItemXFlag = true;
                        isRetrospect = false;
                        retrospectType = "";
                    } else {
                        phantomItemXFlag = false;
                        if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                            SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                            if (sparePart != null) {
                                isRetrospect = sparePart.isRetrospect();
                                retrospectType = sparePart.getRetrospectType();
                            } else {
                                isRetrospect = false;
                                retrospectType = "";
                            }
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    }
                }
                //..2 null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;

                    }

                }
                //..2 !null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim())) && phantomItemXFlag) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";

                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "2";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                        SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                        if (sparePart != null) {
                            isRetrospect = sparePart.isRetrospect();
                            retrospectType = sparePart.getRetrospectType();
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    } else {
                        isRetrospect = false;
                        retrospectType = "";
                    }

                }
                //...3 null
                if ((bbExplosionLvl != null && "...3".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                    }
                }

                if (rowNum == hssfSheet.getLastRowNum()) {
                    System.out.println("$$$$$$$$$" + (rowNum + 1) + "$$$$$$$$$");
                    saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                            partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);

                }
            }
        }

    }
}
