package com.ags.lumosframework.ui.task;

import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.TextBlob;
import com.ags.lumosframework.sdk.service.api.ITextBlobService;
import com.ags.lumosframework.service.IProductInformationService;
import com.ags.lumosframework.service.ISparePartService;
import com.ags.lumosframework.ui.util.FileUtil;
import com.ags.lumosframework.ui.util.ReadArithmetic;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class SparePartScheduleTask implements SchedulingConfigurer {

    @Autowired
    private ISparePartService sparePartService;

    @Autowired
    private IProductInformationService productInformationService;

    @Autowired
    private ITextBlobService textBlobService;

    @Value("${part.schedule.cron}")
    private String cron;

    @Value("${part.schedule.root}")
    private String path;

    @Value("${part.schedule.hold}")
    private String holdPath;

    boolean isFirstTime = true;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    System.out.println("<------schedule start-------->" + LocalDateTime.now().toLocalTime());
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
                        SparePartSchedule();
                    } finally {
                        if (reset) {
                            RequestInfo.set(null);
                        }
                    }

                    System.out.println("<------schedule end-------->" + LocalDateTime.now().toLocalTime());
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    TextBlob cronTextBlob = textBlobService.getByName("Spare_Part_Timer_Cron");
                    if (cronTextBlob != null) {
                        cron = cronTextBlob.getValue() != null ? cronTextBlob.getValue() : cron;
                    }
                    System.out.println("<---------cron----------> = " + cron);
                    //2.2 合法性校验.
                    //todo
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    public void SparePartSchedule() {
        TextBlob pathTextBlob = textBlobService.getByName("Spare_Part_File_Path");
        TextBlob holdPathTextBlob = textBlobService.getByName("Spare_Part_File_HoldPath");
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
                    importsxlsx(in);
                } else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                    importsxls(in);
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

    public void importsxlsx(InputStream is) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        List<SparePart> partList = new ArrayList<>();
        List<ProductInformation> productList = new ArrayList<>();
        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell cellMaterialNo = xssfRow.getCell(0);//料号
                XSSFCell cellMaterialRev = xssfRow.getCell(1);//版本
                XSSFCell cellMaterialDesc = xssfRow.getCell(2);//料号描述
                XSSFCell cellTemperature = xssfRow.getCell(3);//温度标注
                XSSFCell cellMaterialRating = xssfRow.getCell(4);//材料标准
                XSSFCell cellPSLRating = xssfRow.getCell(5);//psl等级
                XSSFCell cellPressure = xssfRow.getCell(6);//压力标准
                XSSFCell cellPressureRev = xssfRow.getCell(7);//压力标准版本
                XSSFCell cellPaint = xssfRow.getCell(8);//喷漆标准
                XSSFCell cellPaintRev = xssfRow.getCell(9);//喷漆标准版本
                XSSFCell cellQulity = xssfRow.getCell(10);//质量计划
                XSSFCell cellQulityRev = xssfRow.getCell(11);//质量计划版本
                XSSFCell cellTorQue = xssfRow.getCell(12);//带压开启扭矩
                XSSFCell cellGasTest = xssfRow.getCell(13);//气压测试
                XSSFCell cellGasTestRev = xssfRow.getCell(14);//气压测试版本
                XSSFCell cellLevp = xssfRow.getCell(15);//levp
                XSSFCell cellLevpRev = xssfRow.getCell(16);//levp版本
                //--------------->产品信息结束，零件信息开始<-----------------
                XSSFCell cellLongtext = xssfRow.getCell(17);//长文本
                XSSFCell cellIsretrospect = xssfRow.getCell(18);//是否追溯
                XSSFCell cellRetrospectType = xssfRow.getCell(19);//追溯类型
                XSSFCell cellAPIStand = xssfRow.getCell(20);//api标准
                XSSFCell cellDrawNo = xssfRow.getCell(21);//图纸
                XSSFCell cellDrawRev = xssfRow.getCell(22);//图纸版本
                XSSFCell cellHardness = xssfRow.getCell(23);//硬度
                XSSFCell cellHardRev = xssfRow.getCell(24);//硬度版本
                XSSFCell cellDNote = xssfRow.getCell(25);//DNote
                XSSFCell cellDNoteRev = xssfRow.getCell(26);//Dnote版本
                XSSFCell cellCoating = xssfRow.getCell(27);//Coating
                XSSFCell cellCoatingRev = xssfRow.getCell(28);//Coating版本
                XSSFCell cellWelding = xssfRow.getCell(29);//Welding
                XSSFCell cellWeldingRev = xssfRow.getCell(30);//Welding版本
                XSSFCell cellIsReviewed = xssfRow.getCell(31);//。。

                String materialNo = cellMaterialNo.getStringCellValue();
                cellMaterialRev.setCellType(CellType.STRING);
                String MaterialRev = cellMaterialRev.getStringCellValue();
                String materialDesc = cellMaterialDesc.getStringCellValue();
                String temperature = cellTemperature.getStringCellValue();
                String materilRating = cellMaterialRating.getStringCellValue();
                cellPSLRating.setCellType(CellType.STRING);
                String pslRating = cellPSLRating.getStringCellValue();
                String pressure = cellPressure.getStringCellValue();
                cellPressureRev.setCellType(CellType.STRING);
                String pressureRev = cellPressureRev.getStringCellValue();
                String paint = cellPaint.getStringCellValue();
                cellPaintRev.setCellType(CellType.STRING);
                String paintRev = cellPaintRev.getStringCellValue();
                String qulity = cellQulity.getStringCellValue();
                cellQulityRev.setCellType(CellType.STRING);
                String qulityRev = cellQulityRev.getStringCellValue();
                String torQue = cellTorQue.getStringCellValue();
                String gas = cellGasTest.getStringCellValue();
                cellGasTestRev.setCellType(CellType.STRING);
                String gasRev = cellGasTestRev.getStringCellValue();
                String levp = cellLevp.getStringCellValue();
                cellLevpRev.setCellType(CellType.STRING);
                String levpRev = cellLevpRev.getStringCellValue();
                //--------------->产品信息结束，零件信息开始<-----------------
                String longText = cellLongtext.getStringCellValue();
                cellIsretrospect.setCellType(CellType.STRING);
                String isretrospect = cellIsretrospect.getStringCellValue();
                String retrospectType = cellRetrospectType.getStringCellValue();
                String apiStand = cellAPIStand.getStringCellValue();
                String drawNo = cellDrawNo.getStringCellValue();
                cellDrawRev.setCellType(CellType.STRING);
                String drawRev = cellDrawRev.getStringCellValue();
                String hardness = cellHardness.getStringCellValue();
                cellHardRev.setCellType(CellType.STRING);
                String hardnessRev = cellHardRev.getStringCellValue();
                String dNote = cellDNote.getStringCellValue();
                cellDNoteRev.setCellType(CellType.STRING);
                String dNoteRev = cellDNoteRev.getStringCellValue();
                String coating = cellCoating.getStringCellValue();
                cellCoatingRev.setCellType(CellType.STRING);
                String coatingRev = cellCoatingRev.getStringCellValue();
                String welding = cellWelding.getStringCellValue();
                cellWeldingRev.setCellType(CellType.STRING);
                String weldingRev = cellWeldingRev.getStringCellValue();
                cellIsReviewed.setCellType(CellType.STRING);
                String isReviewed = cellIsReviewed.getStringCellValue();
                //零件信息一定会有，但是产品信息不一定会有，即产品一定是零件，零件不一定是产品
                //判断当前行的零件信息是否已经存在
                SparePart part;
                SparePart byNoRev = sparePartService.getByNoRev(materialNo, MaterialRev);
                if (byNoRev != null) {
                    part = byNoRev;
                } else {
                    part = new SparePart();
                    part.setSparePartNo(materialNo);
                    part.setSparePartRev(MaterialRev);
                    part.setSparePartDec(materialDesc);
                }
                part.setQaPlan(qulity);
                part.setQaPlanRev(qulityRev);
                part.setDrawNo(drawNo);
                part.setDrawRev(drawRev);
                part.setWelding(welding);
                part.setWeldingRev(weldingRev);
                part.setCoating(coating);
                part.setCoatingRev(coatingRev);
                part.setReviewed(false);
                part.setDNote(dNote);
                part.setDNoteRev(dNoteRev);
                part.setHardnessFile(hardness);
                part.setHardnessRev(hardnessRev);
                part.setApiStand(apiStand);
                part.setLongText(longText);
                part.setRetrospect("1".equals(isretrospect) ? true : false);
                part.setRetrospectType(retrospectType);
                part.setPslLevelStand(pslRating);
                partList.add(part);
                //产品信息全部为空，才不保存产品信息
                if (StringUtils.isNotBlank(temperature) || StringUtils.isNotBlank(materilRating)
                        || StringUtils.isNotBlank(pressure) || StringUtils.isNotBlank(paint) || StringUtils.isNotBlank(qulity)
                        || StringUtils.isNotBlank(torQue) || StringUtils.isNotBlank(gas) || StringUtils.isNotBlank(levp)) {
                    ProductInformation product = new ProductInformation();
                    ProductInformation productByNoRev = productInformationService.getByNoRev(materialNo, MaterialRev);
                    if (productByNoRev != null) {
                        product = productByNoRev;
                    } else {
                        product = new ProductInformation();
                    }
                    product.setProductId(materialNo);
                    product.setProductVersionId(MaterialRev);
                    product.setProductDesc(materialDesc);
                    product.setMaterialRating(materilRating);
                    product.setPressureInspectionProcedure(pressure);
                    product.setPressureInspectionProcedureVersion(pressureRev);
                    product.setPSLRating(pslRating);
                    product.setQualityPlan(qulity);
                    product.setQulityPlanRev(qulityRev);
                    product.setGasTest(gas);
                    product.setGasTestRev(gasRev);
                    product.setTemperatureRating(temperature);
                    product.setBlowdownTorque(torQue);
                    product.setLevp(levp);
                    product.setLevpRev(levpRev);
                    product.setPaintingSpecificationFile(paint);
                    product.setPaintingSpecificationFileRev(paintRev);
                    product.setReviewed(false);
                    productList.add(product);
                }
            }
        }
        sparePartService.saveAll(partList);
        productInformationService.saveAll(productList);
        xssfWorkbook.close();
    }

    public void importsxls(InputStream is) throws IOException {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        List<SparePart> partList = new ArrayList<>();
        List<ProductInformation> productList = new ArrayList<>();

        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);

            if (hssfRow != null) {
                HSSFCell cellMaterialNo = hssfRow.getCell(0);//料号
                HSSFCell cellMaterialRev = hssfRow.getCell(1);//版本
                HSSFCell cellMaterialDesc = hssfRow.getCell(2);//料号描述
                HSSFCell cellTemperature = hssfRow.getCell(3);//温度标注
                HSSFCell cellMaterialRating = hssfRow.getCell(4);//材料标准
                HSSFCell cellPSLRating = hssfRow.getCell(5);//psl等级
                HSSFCell cellPressure = hssfRow.getCell(6);//压力标准
                HSSFCell cellPressureRev = hssfRow.getCell(7);//压力标准版本
                HSSFCell cellPaint = hssfRow.getCell(8);//喷漆标准
                HSSFCell cellPaintRev = hssfRow.getCell(9);//喷漆标准版本
                HSSFCell cellQulity = hssfRow.getCell(10);//质量计划
                HSSFCell cellQulityRev = hssfRow.getCell(11);//质量计划版本
                HSSFCell cellTorQue = hssfRow.getCell(12);//带压开启扭矩
                HSSFCell cellGasTest = hssfRow.getCell(13);//气压测试
                HSSFCell cellGasTestRev = hssfRow.getCell(14);//气压测试版本
                HSSFCell cellLevp = hssfRow.getCell(15);//levp
                HSSFCell cellLevpRev = hssfRow.getCell(16);//levp版本
                //--------------->产品信息结束，零件信息开始<-----------------
                HSSFCell cellLongtext = hssfRow.getCell(17);//长文本
                HSSFCell cellIsretrospect = hssfRow.getCell(18);//是否追溯
                HSSFCell cellRetrospectType = hssfRow.getCell(19);//追溯类型
                HSSFCell cellAPIStand = hssfRow.getCell(20);//api标准
                HSSFCell cellDrawNo = hssfRow.getCell(21);//图纸
                HSSFCell cellDrawRev = hssfRow.getCell(22);//图纸版本
                HSSFCell cellHardness = hssfRow.getCell(23);//硬度
                HSSFCell cellHardRev = hssfRow.getCell(24);//硬度版本
                HSSFCell cellDNote = hssfRow.getCell(25);//DNote
                HSSFCell cellDNoteRev = hssfRow.getCell(26);//Dnote版本
                HSSFCell cellCoating = hssfRow.getCell(27);//Coating
                HSSFCell cellCoatingRev = hssfRow.getCell(28);//Coating版本
                HSSFCell cellWelding = hssfRow.getCell(29);//Welding
                HSSFCell cellWeldingRev = hssfRow.getCell(30);//Welding版本
                HSSFCell cellIsReviewed = hssfRow.getCell(31);//。。

                String materialNo = cellMaterialNo.getStringCellValue();
                cellMaterialRev.setCellType(CellType.STRING);
                String MaterialRev = cellMaterialRev.getStringCellValue();
                String materialDesc = cellMaterialDesc.getStringCellValue();
                String temperature = cellTemperature.getStringCellValue();
                String materilRating = cellMaterialRating.getStringCellValue();
                cellPSLRating.setCellType(CellType.STRING);
                String pslRating = cellPSLRating.getStringCellValue();
                String pressure = cellPressure.getStringCellValue();
                cellPressureRev.setCellType(CellType.STRING);
                String pressureRev = cellPressureRev.getStringCellValue();
                String paint = cellPaint.getStringCellValue();
                cellPaintRev.setCellType(CellType.STRING);
                String paintRev = cellPaintRev.getStringCellValue();
                String qulity = cellQulity.getStringCellValue();
                cellQulityRev.setCellType(CellType.STRING);
                String qulityRev = cellQulityRev.getStringCellValue();
                String torQue = cellTorQue.getStringCellValue();
                String gas = cellGasTest.getStringCellValue();
                cellGasTestRev.setCellType(CellType.STRING);
                String gasRev = cellGasTestRev.getStringCellValue();
                String levp = cellLevp.getStringCellValue();
                cellLevpRev.setCellType(CellType.STRING);
                String levpRev = cellLevpRev.getStringCellValue();
                //--------------->产品信息结束，零件信息开始<-----------------
                String longText = cellLongtext.getStringCellValue();
                cellIsretrospect.setCellType(CellType.STRING);
                String isretrospect = cellIsretrospect.getStringCellValue();
                String retrospectType = cellRetrospectType.getStringCellValue();
                String apiStand = cellAPIStand.getStringCellValue();
                String drawNo = cellDrawNo.getStringCellValue();
                cellDrawRev.setCellType(CellType.STRING);
                String drawRev = cellDrawRev.getStringCellValue();
                String hardness = cellHardness.getStringCellValue();
                cellHardRev.setCellType(CellType.STRING);
                String hardnessRev = cellHardRev.getStringCellValue();
                String dNote = cellDNote.getStringCellValue();
                cellDNoteRev.setCellType(CellType.STRING);
                String dNoteRev = cellDNoteRev.getStringCellValue();
                String coating = cellCoating.getStringCellValue();
                cellCoatingRev.setCellType(CellType.STRING);
                String coatingRev = cellCoatingRev.getStringCellValue();
                String welding = cellWelding.getStringCellValue();
                cellWeldingRev.setCellType(CellType.STRING);
                String weldingRev = cellWeldingRev.getStringCellValue();
                cellIsReviewed.setCellType(CellType.STRING);
                String isReviewed = cellIsReviewed.getStringCellValue();
                //零件信息一定会有，但是产品信息不一定会有，即产品一定是零件，零件不一定是产品
                //判断当前行的零件信息是否已经存在
                SparePart part;
                SparePart byNoRev = sparePartService.getByNoRev(materialNo, MaterialRev);
                if (byNoRev != null) {
                    part = byNoRev;
                } else {
                    part = new SparePart();
                    part.setSparePartNo(materialNo);
                    part.setSparePartRev(MaterialRev);
                    part.setSparePartDec(materialDesc);
                }
                part.setQaPlan(qulity);
                part.setQaPlanRev(qulityRev);
                part.setDrawNo(drawNo);
                part.setDrawRev(drawRev);
                part.setWelding(welding);
                part.setWeldingRev(weldingRev);
                part.setCoating(coating);
                part.setCoatingRev(coatingRev);
                part.setReviewed(false);
                part.setDNote(dNote);
                part.setDNoteRev(dNoteRev);
                part.setHardnessFile(hardness);
                part.setHardnessRev(hardnessRev);
                part.setApiStand(apiStand);
                part.setLongText(longText);
                part.setRetrospect("1".equals(isretrospect) ? true : false);
                part.setRetrospectType(retrospectType);
                part.setPslLevelStand(pslRating);
                partList.add(part);
                //产品信息全部为空，才不保存产品信息
                if (StringUtils.isNotBlank(temperature) || StringUtils.isNotBlank(materilRating)
                        || StringUtils.isNotBlank(pressure) || StringUtils.isNotBlank(paint) || StringUtils.isNotBlank(qulity)
                        || StringUtils.isNotBlank(torQue) || StringUtils.isNotBlank(gas) || StringUtils.isNotBlank(levp)) {
                    ProductInformation product = new ProductInformation();
                    ProductInformation productByNoRev = productInformationService.getByNoRev(materialNo, MaterialRev);
                    if (productByNoRev != null) {
                        product = productByNoRev;
                    } else {
                        product = new ProductInformation();
                    }
                    product.setProductId(materialNo);
                    product.setProductVersionId(MaterialRev);
                    product.setProductDesc(materialDesc);
                    product.setMaterialRating(materilRating);
                    product.setPressureInspectionProcedure(pressure);
                    product.setPressureInspectionProcedureVersion(pressureRev);
                    product.setPSLRating(pslRating);
                    product.setQualityPlan(qulity);
                    product.setQulityPlanRev(qulityRev);
                    product.setGasTest(gas);
                    product.setGasTestRev(gasRev);
                    product.setTemperatureRating(temperature);
                    product.setBlowdownTorque(torQue);
                    product.setLevp(levp);
                    product.setLevpRev(levpRev);
                    product.setPaintingSpecificationFile(paint);
                    product.setPaintingSpecificationFileRev(paintRev);
                    product.setReviewed(false);
                    productList.add(product);
                }
            }
        }
        sparePartService.saveAll(partList);
        productInformationService.saveAll(productList);
        hssfWorkbook.close();
    }

    public boolean checkIsAdded(List<SparePart> spList, SparePart sparePart) {
        boolean flag = false;
        String sparePartNo = sparePart.getSparePartNo();
        for (SparePart sparePartObj : spList) {
            String sparePartAdded = sparePartObj.getSparePartNo();
            if (!Strings.isNullOrEmpty(sparePartNo) && sparePartNo.equals(sparePartAdded)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //初始化request信息，这里没有请求，无法保存数据

}
