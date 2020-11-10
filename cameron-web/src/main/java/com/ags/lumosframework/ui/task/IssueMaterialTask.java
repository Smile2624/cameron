package com.ags.lumosframework.ui.task;


import com.ags.lumosframework.pojo.IssueMaterialList;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.TextBlob;
import com.ags.lumosframework.sdk.service.api.ITextBlobService;
import com.ags.lumosframework.service.IIssueMaterialService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.FileUtil;
import com.ags.lumosframework.ui.util.ReadArithmetic;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class IssueMaterialTask implements SchedulingConfigurer {


    @Autowired
    private ITextBlobService textBlobService;

    @Autowired
    IIssueMaterialService issueMaterialService;

    @Value("${material.schedule.root}")
    private String path;
    @Value("${material.schedule.hold}")
    private String holdPath;
    @Value("${material.schedule.cron}")
    private String cron;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    System.out.println("<--------issue-material-schedule-start--------->" + LocalDateTime.now().toLocalTime());
                    importData();
                    System.out.println("<--------issue-material-schedule-end--------->" + LocalDateTime.now().toLocalTime());
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期 //2.2 合法性校验.
                    TextBlob cronTextBlob = textBlobService.getByName("Issue_Material_Schedule_cron");
                    if (cronTextBlob != null && cronTextBlob.getValue() != null) {
                        cron = CronExpression.isValidExpression(cronTextBlob.getValue()) ? cronTextBlob.getValue() : cron;
                    }
                    System.out.println("<---------cron----------> = " + cron);
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    public void importData() {
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
            TextBlob pathTextBlob = textBlobService.getByName("Issue_Material_File_Path");
            TextBlob holdTextBlob = textBlobService.getByName("Issue_Material_File_HoldPath");
            if (pathTextBlob != null) {
                path = pathTextBlob.getValue() != null ? pathTextBlob.getValue() : path;
            }
            if (holdTextBlob != null) {
                holdPath = holdTextBlob.getValue() != null ? holdTextBlob.getValue() : holdPath;
            }
            File file = new File(path);
            if (file.isDirectory()) {
                ReadArithmetic readArithmetic = new ReadArithmetic();
                FileUtil fileUtil = new FileUtil();
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        readData(files[i]);
                        readArithmetic.pigeonHoleFile(files[i], ReadArithmetic.PASS_PATH, holdPath, fileUtil);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        readArithmetic.pigeonHoleFile(files[i], ReadArithmetic.ERROR_PATH, holdPath, fileUtil);
                        readArithmetic.logError(holdPath, fileUtil, e);
                    }
                }
            }
        } finally {
            if (reset) {
                RequestInfo.set(null);
            }
        }

    }

    private void readData(File file) throws UnsupportedEncodingException, FileNotFoundException {
        List<IssueMaterialList> list = new ArrayList<>();

        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), "utf8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        int i = 0;
        try {
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] datas = lineTxt.split("\t");
                //data长度默认应该是7，但是最后伟有个能是为空，所以长度有可能是6
                if (i > 0) {
                    //i=0 时为信息头，不需要处理
                    IssueMaterialList object = issueMaterialService.getByOrderNoAndMatNo(datas[0].substring(3), datas[1]);
                    if (object == null) {
                        object = new IssueMaterialList();
                    } else {//存在跳过
                        continue;
                    }
                    object.setOrderNo(datas[0].substring(3));
                    object.setMaterialNo(datas[1]);
                    object.setMaterialDesc(datas[2]);
                    object.setRequirementQuantity(Double.parseDouble(datas[3].replaceAll("\"", "").replaceAll(",", "")));
                    object.setQuantityWithdrawn(Double.parseDouble(datas[4].replaceAll("\"", "").replaceAll(",", "")));
                    object.setShortage(Double.parseDouble(datas[5].replaceAll("\"", "").replaceAll(",", "")));
                    if (datas.length > 6) {
                        object.setProdStorage(datas[6]);
                    } else {
                        object.setProdStorage(null);
                    }
                    object.setStatus(AppConstant.STATUS_N);
                    list.add(object);
                }
                i++;
            }
            //保存数据
            issueMaterialService.saveAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
