package com.ags.lumosframework.ui.task;

import com.ags.lumosframework.pojo.OperationInstruction;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Department;
import com.ags.lumosframework.sdk.domain.TextBlob;
import com.ags.lumosframework.sdk.service.api.IDepartmentService;
import com.ags.lumosframework.sdk.service.api.ITextBlobService;
import com.ags.lumosframework.service.IOperationInstructionService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author peyton_zhao
 */
@Component
public class OperationInstructionTocTask implements SchedulingConfigurer {


    @Value("${Toc.schedule.cron}")
    private String cron;

    @Value("${Toc.schedule.path}")
    private String path;

    private final String tocWorkInstructions = "WorkInstructions";
    private final String tocFacilityForms = "FacilityForms";

    private static final int wdFormatPDF = 17;// PDF 格式

    @Autowired
    private ITextBlobService textBlobService;
    @Autowired
    private IOperationInstructionService opService;
    @Autowired
    private IDepartmentService departmentService;

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
                        TocSchedule();
                    } catch (Exception e) {
                        e.printStackTrace();
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
                    TextBlob cronTextBlob = textBlobService.getByName("Operation_Instruction_Timer_Cron");
                    if (cronTextBlob != null && cronTextBlob.getValue() != null) {
                        cron = CronExpression.isValidExpression(cronTextBlob.getValue()) ? cronTextBlob.getValue() : cron;
                    }
                    System.out.println("<---------cron----------> = " + cron);
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    private void TocSchedule() throws Exception {
        TextBlob documentPath = textBlobService.getByName("Operation_Instruction_File_Path");
        if (documentPath != null) {
            path = documentPath.getValue() != null ? documentPath.getValue() : path;
        }
        //TOC-WorkInstructions
        tocWorkInstructionsSchedule();
        //TOC-FacilityForms
        tocFacilityFormsSchedule();
    }

    /**
     * 获取TOC-WorkInstructions文件列表数据
     */
    private void tocWorkInstructionsSchedule() throws Exception {
        OperationInstruction opWorkInstruction = opService.getSuperByChildren(tocWorkInstructions);
        //判断是否有更新
        String localDateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusDays(-1));
        if (localDateNow.equals(opWorkInstruction.getModifiedDate())) {//是否有更新
            List<Department> departments = departmentService.listDepartmentName();
            List<String> departmentNames = new ArrayList<>();
            departmentNames.add("-");
            departments.forEach(department -> departmentNames.add(department.getName()));

            Map<String, Object> dataMap;
            List<String> pathFileList = new ArrayList<>();
            int tem = 1;
            for (String departmentName : departmentNames) {
                List<OperationInstruction> opWIList = opService.getByTypeAndDepartment(opWorkInstruction.getInstructionName(), departmentName);
                if (opWIList.size() > 0) {
                    dataMap = new HashMap<>();
                    List<Map<String, String>> tocList = new ArrayList<>();
                    int count = 1;
                    for (OperationInstruction op : opWIList) {
                        //非前一天删除的不作处理，跳过
                        if (op.getDeleteFlag() && !DateTimeFormatter.ofPattern("yyyy-MM-dd").format(op.getLastModifyTime()).equals(localDateNow)) {
                            continue;
                        }
                        Map<String, String> map = new HashMap<>(6);
                        map.put("section", count + "");
                        map.put("workIn", op.getInstructionName());
                        map.put("rev", op.getInstructionRev());
                        map.put("title", op.getInstructionDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                        if (op.getDeleteFlag()) {
                            map.put("TF", "T1");
                        } else {
                            map.put("TF", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(op.getLastModifyTime()).equals(localDateNow) ? "T0" : "T1");
                        }
                        map.put("TD", op.getDeleteFlag() ? "YD" : "ND");
                        tocList.add(map);
                        count++;
                    }

                    //填充公共数据
                    dataMap.put("rev0", Integer.parseInt(opWorkInstruction.getInstructionRev()) + 1 + "");
                    dataMap.put("date0", opWorkInstruction.getModifiedDate());
                    dataMap.put("department", departmentName);
                    //填充列表数据
                    dataMap.put("toclist", tocList);
                    //生成临时 doc-i 文件
                    createDoc(dataMap, path + "\\TOC-WorkIn-" + tem + ".doc", "TocWorkInstructions.xml");
                    pathFileList.add(path + "\\TOC-WorkIn-" + tem + ".doc");
                    tem++;
                }

            }
            //合成TOC-WorkInstructions.doc
            com.spire.doc.Document document = new com.spire.doc.Document(pathFileList.get(0));
            System.out.println("0*****" + pathFileList.get(0));
            for (int i = 1; i < pathFileList.size(); i++) {
                System.out.println(i + "*****" + pathFileList.get(i));

                document.insertTextFromFile(pathFileList.get(i), FileFormat.Docx_2013);
            }
            document.saveToFile(path + "\\TOC-WorkInstructions.doc", FileFormat.Docx_2013);
            //删除临时 doc-i 文件
            pathFileList.forEach(file -> new File(file).delete());
            //生成pdf,删除doc
            wordToPDF(path + "\\TOC-WorkInstructions.doc");

            //版本号+1
            opWorkInstruction.setInstructionRev(Integer.parseInt(opWorkInstruction.getInstructionRev()) + 1 + "");
            opService.save(opWorkInstruction);
        }


    }

    /**
     * 获取TOC-FacilityForms文件列表数据
     */
    private void tocFacilityFormsSchedule() throws Exception {
        OperationInstruction opFacilityForm = opService.getSuperByChildren(tocFacilityForms);
        //判断是否有更新
        String localDateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusDays(-1));
        if (localDateNow.equals(opFacilityForm.getModifiedDate())) {
            Map<String, Object> dataMap = new HashMap<>();

            List<Map<String, String>> tocList = new ArrayList<>();
            List<OperationInstruction> opFFList = opFacilityForm.getChild("", false);
            int count = 1;
            for (OperationInstruction op : opFFList) {
                //非前一天删除的不作处理，跳过
                if (op.getDeleteFlag() && !DateTimeFormatter.ofPattern("yyyy-MM-dd").format(op.getLastModifyTime()).equals(localDateNow)) {
                    continue;
                }
                Map<String, String> map = new HashMap<>();
                map.put("section", count + "");
                map.put("recordFo", op.getInstructionName());
                map.put("rev", op.getInstructionRev());
                map.put("title", op.getInstructionDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                if (op.getDeleteFlag()) {
                    map.put("TF", "T1");
                } else {
                    map.put("TF", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(op.getLastModifyTime()).equals(localDateNow) ? "T0" : "T1");
                }
                map.put("TD", op.getDeleteFlag() ? "YD" : "ND");

                tocList.add(map);
                count++;
            }

            //填充公共数据
            dataMap.put("rev0", Integer.parseInt(opFacilityForm.getInstructionRev()) + 1 + "");
            dataMap.put("date0", opFacilityForm.getModifiedDate());
            //填充列表数据
            dataMap.put("toclist", tocList);

            //生成临时doc文件
            createDoc(dataMap, path + "\\TOC-FacilityForms.doc", "TocFacilityForms.xml");
            //生成pdf,删除doc
            wordToPDF(path + "\\TOC-FacilityForms.doc");

            //版本号+1
            opFacilityForm.setInstructionRev(Integer.parseInt(opFacilityForm.getInstructionRev()) + 1 + "");
            opService.save(opFacilityForm);
        }
    }

    /**
     * 生成word
     *
     * @param dataMap  列表数据
     * @param pathName 文件路径名称 (path// + WorkInstructions.doc ; FacilityForms.doc)
     * @param xmlName  模板文件名 （TocWorkInstructions.xml ；TocFacilityForms.xml ）
     * @throws Exception
     */
    private void createDoc(Map<String, Object> dataMap, String pathName, String xmlName) throws Exception {

        // Configuration用于读取模板文件(XML、FTL文件或迅是我们这里用的StringTemplateLoader)
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        // 指定模板文件所在目录的路径,而不是文件的路径
        String jPath = AppConstant.DOC_XML_FILE_PATH;
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        // 以utf-8的编码读取模板文件
        Template template = configuration.getTemplate(xmlName, "utf-8");
        //输出文件
        File outFile = new File(pathName);

        //将模板和数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                1024 * 1024);
        template.process(dataMap, out);
        out.flush();
        out.close();
        System.out.println("*********Create Doc Success ***********");

    }

    /**
     * @param filePathName
     */
    public void wordToPDF(String filePathName) {
        ComThread.InitSTA();
        ActiveXComponent app = null;
        Dispatch doc = null;
        String overFile;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            overFile = filePathName.replace(".doc", ".pdf");

            doc = Dispatch.call(docs, "Open", filePathName).toDispatch();
            File tofile = new File(overFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", overFile, wdFormatPDF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            if (app != null) {
                app.invoke("Quit", new Variant[]{});
            }
        }
        //结束后关闭进程
        System.out.println(LocalDateTime.now());
        ComThread.Release();
        File file = new File(filePathName);
        file.delete();
    }

}
