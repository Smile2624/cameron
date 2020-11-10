package com.ags.lumosframework.ui.view.assembling;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.ui.util.VoiceStringSolveUtils;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

//@Menu(
//        caption = "Assembling",
//        captionI18NKey = "view.Assembling.caption",
//        iconPath = "images/icon/text-blob.png",
//        groupName = "Production",
//        order = 2
//)
//@SpringView(
//        name = "Assembling",
//        ui = {CameronUI.class}
//)
//@Secured("Assembling")
public class AssemblingView_ extends BaseView implements Button.ClickListener, IFilterableView {
    private static final long serialVersionUID = 4854162164548450226L;
    GridLayout glLayout = new GridLayout(3, 2);
    Panel panelDetail = new Panel();
    VerticalLayout vlDetailGrid = new VerticalLayout();
    GridLayout singletonGrid;
    GridLayout batchGrid;
    GridLayout detailLayoutGrid;
    Boolean singleOrBatch = true;//用于标识当前录入按件还是批次：single->true;batch->false
    StringBuilder returnMessage = new StringBuilder();
    SocketClient client = null;
    private ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    private ComboBox<String> cbType = new ComboBox<>();
    @I18Support(
            caption = "Material",
            captionKey = "AssemblingInfo.Material"
    )
    private LabelWithSamleLineCaption tfProductNo = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "Rev",
            captionKey = "AssemblingInfo.Rev"
    )
    private LabelWithSamleLineCaption tfProductRev = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "Description",
            captionKey = "AssemblingInfo.Description"
    )
    private LabelWithSamleLineCaption tfProductDec = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "Order",
            captionKey = "AssemblingInfo.Order"
    )
    private LabelWithSamleLineCaption tfOrder = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "Qty",
            captionKey = "AssemblingInfo.Qty"
    )
    private LabelWithSamleLineCaption tfQty = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "S/N Batch",
            captionKey = "AssemblingInfo.SnBatch"
    )
    private LabelWithSamleLineCaption tfSnBatch = new LabelWithSamleLineCaption();
    @I18Support(
            caption = "S/N Batch",
            captionKey = "AssemblingInfo.SnBatch"
    )
    private ComboBox<String> cbSnBatch = new ComboBox<>();
    //    @I18Support(caption = "ConfirmOrderNumber", captionKey = "AssemblingInfo.ConfirmOrderNumber")
    private Button btnConfirm = new Button();
    @I18Support(
            caption = "TempSave",
            captionKey = "AssemblingInfo.TempSave"
    )
    private Button btnTempSave = new Button();
    @I18Support(
            caption = "SaveCommit",
            captionKey = "AssemblingInfo.SaveCommit"
    )
    private Button btnSaveCommit = new Button();
    @I18Support(
            caption = "OpenVoice",
            captionKey = "AssemblingInfo.OpenVoice"
    )
    private Button btnOpenVoice = new Button();
    private TextField tfInput = new TextField();
    private Button btnScan = new Button();
    private Button[] btns = new Button[]{btnConfirm, btnScan, btnTempSave, btnSaveCommit};//btnOpenVoice
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private int x;
    private int y;
    private int countNo = 1;
    private Boolean isValue = true;//判断接受的是 值 还是 判断是否
    private String prefixSend = "";
    private ProductionOrder order;

    private List<Assembling> singletonDetail = new ArrayList<>();

    private List<Assembling> batchDetail = new ArrayList<>();

    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IAssemblingService assemblingService;
    @Autowired
    private IAssemblingTempService assemblingTempService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private ICaConfigService caConfigService;

    public AssemblingView_() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();

        hlTempToolBox.addComponent(cbOrder);
        cbOrder.setTextInputAllowed(true);
        cbOrder.setPlaceholder(I18NUtility.getValue("AssemblingInfo.OrderNo", "OrderNo"));
        hlTempToolBox.addComponent(cbType);
        cbType.setTextInputAllowed(false);
        cbType.setEmptySelectionAllowed(false);
        cbType.setPlaceholder(I18NUtility.getValue("AssemblingInfo.RecordType", "RecordType"));
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        hlTempToolBox.addComponent(tfInput);
        tfInput.setVisible(false);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnOpenVoice.setIcon(VaadinIcons.MICROPHONE);
        btnScan.setIcon(VaadinIcons.QRCODE);
        btnScan.setCaption("开始扫码");
        btnSaveCommit.setIcon(VaadinIcons.PACKAGE);
        btnOpenVoice.setEnabled(false);
        btnScan.setEnabled(false);
        btnSaveCommit.setEnabled(false);
        btnTempSave.setEnabled(false);
        btnTempSave.setVisible(false);
        btnTempSave.setIcon(VaadinIcons.MUTE);
        hlToolBox.addComponent(hlTempToolBox);
        vlRoot.addComponent(hlToolBox);

        cbType.addSelectionListener(new SingleSelectionListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<String> event) {
                if (event.getValue() == null || "sType".equals(event.getValue().trim())) {
                    btnTempSave.setVisible(false);
                } else {
                    btnTempSave.setVisible(true);
                }

            }
        });
        Panel panel = new Panel();
        panel.setWidth("100%");
        panel.setHeightUndefined();
        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(this.tfProductNo, 0, 0);
        glLayout.addComponent(this.tfProductRev, 1, 0);
        glLayout.addComponent(this.tfProductDec, 2, 0);
        glLayout.addComponent(this.tfOrder, 0, 1);
        glLayout.addComponent(this.tfQty, 1, 1);

        panel.setContent(glLayout);
        vlRoot.addComponent(panel);

        panelDetail.setSizeFull();
        vlRoot.addComponent(panelDetail);
        vlRoot.setExpandRatio(panelDetail, 0.1f);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

        KeyAction ka = new KeyAction(219, new int[]{});//[键
        ka.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                tfInput.clear();
                //TODO:Input previous sn
            }
        });
        ka.extend(tfInput);
        KeyAction ka2 = new KeyAction(221, new int[]{});//]键
        ka2.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                tfInput.clear();
                //TODO:Input next sn
            }
        });
        ka2.extend(tfInput);
        KeyAction ka3 = new KeyAction(13, new int[]{});//回车键
        ka3.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                String input=tfInput.getValue();
                if(cbType.getValue().equals("bType")){
                    inputValueBType(input);
                } else if(cbType.getValue().equals("sType")){
                    inputValueSType(input);
                }
                tfInput.clear();
            }
        });
        ka3.extend(tfInput);
        tfInput.addBlurListener(event -> tfInput.selectAll());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnConfirm.equals(button)) {
            tfInput.setVisible(false);
//            File folder = new File("D:\\REPORT\\Production\\Assembly");
//            for (File f : folder.listFiles()) {
//                if (f.isFile() && f.getName().endsWith(".doc")) {
//                    wordToPDF(f.getPath());
//                }
//            }
            if (cbOrder.getValue() == null) {
                NotificationUtils.notificationError("请选择订单号");
            } else {
                order = cbOrder.getValue();
//                if (!this.routingService.checkRoutingStatus(this.order.getRoutingGroup(), this.order.getInnerGroupNo())) {
//                    throw new PlatformException("订单对应Routing待审核，不允许执行当前操作！");
//                }

                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    glLayout.removeComponent(2, 1);
                    glLayout.addComponent(this.cbSnBatch, 2, 1);
                    freshGrid();
                } else if (cbType.getValue().equals("bType")) {
                    glLayout.removeComponent(2, 1);
                    glLayout.addComponent(this.tfSnBatch, 2, 1);
                    freshGridBtype();
                }
            }
        } else if (btnOpenVoice.equals(button)) {
            if (Strings.isNullOrEmpty(cbType.getValue())) {
                throw new PlatformException("请先选择录入类型！");
            } else if (cbType.getValue().equals("sType")) {
                VoiceInputSingleton();//单件录入
            } else if (cbType.getValue().equals("bType")) {
                freshGridBtype();
                VoiceInputBtype();
            }
        } else if (btnScan.equals(button)) {
            if (!tfInput.isVisible()) {
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    tfInput.setVisible(true);
                    tfInput.selectAll();
                    focusOnSnSType();
                    btnScan.setCaption("停止扫码");
                    //单件录入
                } else if (cbType.getValue().equals("bType")) {
                    tfInput.setVisible(true);
                    tfInput.selectAll();
                    focusOnSnBType();
                    btnScan.setCaption("停止扫码");
                    //分组录入
                }
            } else {
                tfInput.setVisible(false);
                btnScan.setCaption("开始扫码");
            }
        } else if (btnSaveCommit.equals(button)) {
            if (cbType.getValue().equals("sType")) {
                saveCommitStype();//单件
            } else if (cbType.getValue().equals("bType")) {//分组
                //判断是否录入完成
                if (checkFulfil()) {//录完
                    saveCommitBtype(true);
                } else {
                    throw new PlatformException("装配信息未录完，请点击“暂存”按钮操作！");
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (btnTempSave.equals(button)) {
            if (checkFulfil()) {//录完
                throw new PlatformException("装配信息已录完，请点击“提交”按钮操作！");
            } else {
                saveCommitBtype(false);//分组
            }
            //停止语音
            prefixSend = AppConstant.INSPECTIONDONE;
            try {
                client.sendMessage("[PT]装配信息暂停录入");
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 分组方式检测最后一个单元格信息是否录入完成
     *
     * @return
     */
    public Boolean checkFulfil() {
        TextArea lastTa;
        if (batchDetail.size() == 0) {
            lastTa = (TextArea) detailLayoutGrid.getComponent(detailLayoutGrid.getColumns() - 1, detailLayoutGrid.getRows() - 2);
        } else {
            lastTa = (TextArea) detailLayoutGrid.getComponent(detailLayoutGrid.getColumns() - 1, detailLayoutGrid.getRows() - 1);
        }
        if (Strings.isNullOrEmpty(lastTa.getValue())) {//未录完
            return false;
        } else {
            return true;
        }
    }

    /**
     * 单件方式录入后提交保存操作
     */
    public void saveCommitStype() {
        //保存装配记录
        saveAssembReocrd();
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }
        String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT;

        //生成报告
        reportDatesStype(path);
    }

    /**
     * 分组方式录入后提交保存操作
     */
    public void saveCommitBtype(Boolean flag) {
        //TODO 保存装配记录
        saveAssembReocrdBtype(flag);
        if (flag) {//完成
            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (caConfig == null) {
                throw new PlatformException("请先配置文档报告存放路径");
            }
            String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT;
            //TODO 生成报告
            reportDatesBtype(path);
            ConfirmDialog.show(getUI(),
                    "已完成该订单装配！请前往“工序记录”页面进行装配工序的完成确认！", result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {

                        }
                    });
        } else {//暂存，不生成报告

        }

    }

    /**
     * 单件方式语音录入
     */
    public void VoiceInputSingleton() {
        try {
            String ipAddress = RequestInfo.current().getUserIpAddress();//语音服务端ip地址
            //连接语音服务
            client = new SocketClient(ipAddress, AppConstant.PORT);
            //监听信息并逻辑处理
            client.setReceiveListener(message -> {
                if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)
                        && AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
                    // [OC]语音播放结束
                    System.out.println("Receive Message -> " + message);
                    // 发送请求语音识别的指令
                    try {
                        prefixSend = AppConstant.PREFIXSTARTRECORD;
                        client.sendMessage("[BR]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)
                        && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                    // 发送语音识别请求并且返回的是语音识别结束字串
                    // message去除信息头
                    String messageBody = message.split("]")[1];
                    if (!message.split("\\|")[1].equals("1")) {
                        returnMessage.append(messageBody.split("\\|")[0]);
                    }
                } else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {//一个字段录入完，开始校验数据正确性并set值
                    if (returnMessage.length() == 0) {//录入空
                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                        try {
                            String strTitle = "";
                            String strPartNumber = "";
                            if (singleOrBatch) {
                                strTitle = ((Label) singletonGrid.getComponent(x, 0)).getValue();
                                strPartNumber = ((TextField) singletonGrid.getComponent(0, y)).getValue();
                            } else {
                                strTitle = ((Label) batchGrid.getComponent(x, 0)).getValue();
                                strPartNumber = ((TextField) batchGrid.getComponent(0, y)).getValue();
                            }
                            if (isValue) {
                                client.sendMessage("[PT]请输入零件编号" + strPartNumber + "对应的" + strTitle + "值");
                            } else {
                                client.sendMessage("[PT]该零件" + strPartNumber + "对应的值是否录入完成");
                            }
                        } catch (Exception e) {
                            NotificationUtils.notificationError("Socket连接中断");
                        }
                    } else {//录入有值
                        //set值
                        getUI().accessSynchronously(new Runnable() {
                            @Override
                            public void run() {
                                TextArea tf;
                                String strQty;
                                if (singleOrBatch) {
                                    tf = ((TextArea) singletonGrid.getComponent(x, y));
                                    strQty = ((TextField) singletonGrid.getComponent(4, y)).getValue();
                                } else {
                                    tf = ((TextArea) batchGrid.getComponent(x, y));
                                    strQty = ((TextField) batchGrid.getComponent(4, y)).getValue();
                                }
                                if (isValue) {//录入值
                                    String inputString = VoiceStringSolveUtils.solveString(returnMessage.toString().trim());
                                    if (countNo == 1) {//
                                        tf.setValue(inputString.toUpperCase());
                                    } else {
                                        tf.setValue(tf.getValue() + "\n" + inputString.toUpperCase());
                                    }

                                    if (x == 6) {
                                        int qty = Integer.parseInt(strQty);
                                        if (qty == 1) {
                                            countNo = 1;
                                            isValue = true;
                                            if (singleOrBatch) {
                                                if (y < singletonDetail.size()) {
                                                    x = 4;
                                                }
                                            } else {
                                                if (y < batchDetail.size()) {
                                                    x = 4;
                                                }
                                            }
                                            y += 1;
                                        } else if (qty == countNo) {
                                            countNo = 1;
                                            isValue = true;
                                            x = 4;
                                            y += 1;
                                        } else {
                                            isValue = false;
                                        }
                                    }
                                } else {//录入是否
                                    if (returnMessage.toString().trim().contains("是")) {
                                        countNo = 1;
                                        isValue = true;
                                        if (singleOrBatch) {
                                            if (y < singletonDetail.size()) {
                                                x = 4;
                                            }
                                        } else {
                                            if (y < batchDetail.size()) {
                                                x = 4;
                                            }
                                        }
                                        y += 1;
                                    } else {//该零件需要继续录入一条序列号和熔炼炉号
                                        countNo += 1;
                                        isValue = true;
                                        x = 4;
                                    }
                                }
                                returnMessage.delete(0, returnMessage.length());
                            }
                        });
                        //开始下一个字段
                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                        if (singleOrBatch) {//单件grid
                            if (x < 6) {
                                x += 1;
                            } else {
                                if (y >= singletonDetail.size() && isValue) {//单件完成，重置（x,y）定位
                                    x = 5;
                                    y = 1;
                                    singleOrBatch = false;
                                }
                            }
                            try {
                                String strTitle;
                                String strPartN;
                                if (singleOrBatch) {
                                    strTitle = ((Label) singletonGrid.getComponent(x, 0)).getValue();
                                    strPartN = ((TextField) singletonGrid.getComponent(0, y)).getValue();
                                } else {
                                    if (batchDetail == null || batchDetail.size() == 0) {
                                        //TODO 全部录入完成
                                        prefixSend = AppConstant.INSPECTIONDONE;
                                        client.sendMessage("[PT]装配信息录入结束完成");
                                        client.close();
                                    }
                                    strTitle = ((Label) batchGrid.getComponent(x, 0)).getValue();
                                    strPartN = ((TextField) batchGrid.getComponent(0, y)).getValue();
                                }
                                if (isValue) {
                                    client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
                                    isValue = true;
                                } else {
                                    client.sendMessage("[PT]该零件对应的值是否录入完成");
                                    isValue = false;
                                }

                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }

                        } else {//批次grid
                            try {
                                if (x < 6) {
                                    x += 1;
                                } else {
                                    if (y >= batchDetail.size() && isValue) {//批次完成
                                        x = 5;
                                        y = 1;
                                        singleOrBatch = true;
                                    }
                                }

                                if (singleOrBatch) {
                                    //TODO 全部录入完成
                                    prefixSend = AppConstant.INSPECTIONDONE;
                                    client.sendMessage("[PT]装配信息录入结束完成");
                                    client.close();
                                } else {
                                    String strTitle = ((Label) batchGrid.getComponent(x, 0)).getValue();
                                    String strPartN = ((TextField) batchGrid.getComponent(0, y)).getValue();
                                    if (isValue) {
                                        client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
                                        isValue = true;
                                    } else {
                                        client.sendMessage("[PT]该零件对应的值是否录入完成");
                                        isValue = false;
                                    }
                                }
                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }
                        }
                    }
                } else if (message.startsWith(AppConstant.PALYEXCEPTION)) {
                    System.out.println("语音服务网络断开，正尝试重新连接");
                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                    try {
                        String strTitle = "";
                        String strPartN = "";
                        if (singleOrBatch) {
                            strTitle = ((Label) singletonGrid.getComponent(x, 0)).getValue();
                            strPartN = ((TextField) singletonGrid.getComponent(0, y)).getValue();
                        } else {
                            strTitle = ((Label) batchGrid.getComponent(x, 0)).getValue();
                            strPartN = ((TextField) batchGrid.getComponent(0, y)).getValue();
                        }
                        if (isValue) {
                            client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
                        } else {
                            client.sendMessage("[PT]该零件" + strPartN + "对应的值是否录入完成");
                        }
                    } catch (Exception e) {
                        NotificationUtils.notificationError("Socket连接中断");
                    }
                }
            });
            //首次第一个输入值
            prefixSend = AppConstant.PREFIXPLAYTEXT;
            String strTitle = "";
            String strPartN = "";
            if (singleOrBatch) {
                strTitle = ((Label) singletonGrid.getComponent(x, 0)).getValue();
                strPartN = ((TextField) singletonGrid.getComponent(0, y)).getValue();
            } else {
                strTitle = ((Label) batchGrid.getComponent(x, 0)).getValue();
                strPartN = ((TextField) batchGrid.getComponent(0, y)).getValue();
            }
            client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 单件保存装配记录
     */
    public void saveAssembReocrd() {
        //****单件
        for (int s = 0; s < singletonDetail.size(); s++) {

            TextField tfPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            String tempValue0 = tfPartNo.getValue();

            Assembling singleAssemb = assemblingService.getBySnAndPartNo(cbSnBatch.getValue(), tempValue0, Integer.toString(s));
            if (singleAssemb == null) {
                singleAssemb = new Assembling();
            }
            singleAssemb.setOrderNo(order.getProductOrderId());
            singleAssemb.setRetrospectType("singleton");
            singleAssemb.setSnBatch(cbSnBatch.getValue());

            singleAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) singletonGrid.getComponent(1, s + 1);
            String tempValue1 = tfPartDesc.getValue();
            singleAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

            TextField tfPlmRev = (TextField) singletonGrid.getComponent(2, s + 1);
            String tempValue2 = tfPlmRev.getValue();
            singleAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) singletonGrid.getComponent(3, s + 1);
            String tempValue3 = tfPartRev.getValue();
            singleAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

            TextArea tfSerialNo = (TextArea) singletonGrid.getComponent(5, s + 1);
            String tempValue4 = tfSerialNo.getValue();
            singleAssemb.setSerialNo(tempValue4 == null ? "" : tempValue4);

            TextArea tfHeatNoLot = (TextArea) singletonGrid.getComponent(6, s + 1);
            String tempValue5 = tfHeatNoLot.getValue();
            singleAssemb.setHeatNoLot(tempValue5 == null ? "" : tempValue5);

            singleAssemb.setHardness("/");
            singleAssemb.setMatType("/");
            singleAssemb.setQcCheck("/");
            singleAssemb.setDescription(Integer.toString(s));

            assemblingService.save(singleAssemb);
        }
        //*******批次
        for (int b = 0; b < batchDetail.size(); b++) {
            TextField tfPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            String tempValue0 = tfPartNo.getValue();

            Assembling bAssemb = assemblingService.getBySnAndPartNo(cbSnBatch.getValue(), tempValue0, Integer.toString(b));
            if (bAssemb == null) {
                bAssemb = new Assembling();
            }
            bAssemb.setOrderNo(order.getProductOrderId());
            bAssemb.setRetrospectType("batch");
            bAssemb.setSnBatch(cbSnBatch.getValue());

            bAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) batchGrid.getComponent(1, b + 1);
            String tempValue1 = tfPartDesc.getValue();
            bAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

            TextField tfPlmRev = (TextField) batchGrid.getComponent(2, b + 1);
            String tempValue2 = tfPlmRev.getValue();
            bAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) batchGrid.getComponent(3, b + 1);
            String tempValue3 = tfPartRev.getValue();
            bAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

            TextField tfBatchQty = (TextField) batchGrid.getComponent(4, b + 1);
            String tempValue4 = tfBatchQty.getValue();
            bAssemb.setBatchQty(new Integer(tempValue4));

            TextArea tfBatch = (TextArea) batchGrid.getComponent(5, b + 1);
            String tempValue5 = tfBatch.getValue();
            bAssemb.setBatch(tempValue5 == null ? "" : tempValue5);

            TextArea tfHeatNoLot = (TextArea) batchGrid.getComponent(6, b + 1);
            String tempValue6 = tfHeatNoLot.getValue();
            bAssemb.setHeatNoLot(tempValue6 == null ? "" : tempValue6);
            bAssemb.setHardness("/");
            bAssemb.setCOrD("/");
            bAssemb.setEOrD("/");
            bAssemb.setQcCheck("/");
            bAssemb.setDescription(Integer.toString(b));

            assemblingService.save(bAssemb);
        }

    }

    /**
     * 单件方式装配报告数据
     *
     * @param path
     */
    public void reportDatesStype(String path) {
        List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
        //****单件
        for (int s = 0; s < singletonDetail.size(); s++) {
            Map<String, String> sMap = new HashMap<>();
            TextField tfPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            String tempValue0 = tfPartNo.getValue();
            sMap.put("sPartNo", tempValue0 == null ? "" : tempValue0);
            TextArea tfPartDesc = (TextArea) singletonGrid.getComponent(1, s + 1);
            String tempValue1 = tfPartDesc.getValue();
            sMap.put("sPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

            TextField tfPlmRev = (TextField) singletonGrid.getComponent(2, s + 1);
            String tempValue2 = tfPlmRev.getValue();
            sMap.put("sPlmRev", tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) singletonGrid.getComponent(3, s + 1);
            String tempValue3 = tfPartRev.getValue();
            sMap.put("sPartRev", tempValue3 == null ? "" : tempValue3);

            TextArea tfSerialNo = (TextArea) singletonGrid.getComponent(5, s + 1);
            String tempValue4 = tfSerialNo.getValue();
            sMap.put("sSN", tempValue4 == null ? "" : tempValue4);

            TextArea tfHeatNoLot = (TextArea) singletonGrid.getComponent(6, s + 1);
            String tempValue5 = tfHeatNoLot.getValue();
            sMap.put("sHeat", tempValue5 == null ? "" : tempValue5);

            sMap.put("sHardness", "/");
            sMap.put("sMat", "/");
            sMap.put("sQC", "/");
            sList.add(sMap);
        }
        //****批次
        List<Map<String, String>> bList = new ArrayList<Map<String, String>>();
        for (int b = 0; b < batchDetail.size(); b++) {
            Map<String, String> bMap = new HashMap<>();

            TextField tfPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            String tempValue0 = tfPartNo.getValue();
            bMap.put("bPartNo", tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) batchGrid.getComponent(1, b + 1);
            String tempValue1 = tfPartDesc.getValue();
            bMap.put("bPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

            TextField tfPlmRev = (TextField) batchGrid.getComponent(2, b + 1);
            String tempValue2 = tfPlmRev.getValue();
            bMap.put("bPlmRev", tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) batchGrid.getComponent(3, b + 1);
            String tempValue3 = tfPartRev.getValue();
            bMap.put("bPartRev", tempValue3 == null ? "" : tempValue3);

            TextField tfBatchQty = (TextField) batchGrid.getComponent(4, b + 1);
            String tempValue4 = tfBatchQty.getValue();
            int bQty = new Integer(tempValue4);
            bMap.put("bQty", bQty < 0 ? "0" : bQty + "");

            TextArea tfBatch = (TextArea) batchGrid.getComponent(5, b + 1);
            String tempValue5 = tfBatch.getValue();
            bMap.put("bBatch", tempValue5 == null ? "" : tempValue5);

            TextArea tfHeatNoLot = (TextArea) batchGrid.getComponent(6, b + 1);
            String tempValue6 = tfHeatNoLot.getValue();
            bMap.put("bHeat", tempValue6 == null ? "" : tempValue6);
            bMap.put("bHardness", "/");
            bMap.put("bCD", "/");
            bMap.put("bED", "/");
            bMap.put("bQC", "/");
            bList.add(bMap);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tSnBatch", cbSnBatch.getValue());
        dataMap.put("tQty", tfQty.getValue());
        dataMap.put("slist", sList);
        dataMap.put("blist", bList);
        String pathFileName = path + cbSnBatch.getValue() + ".doc";
        try {
            //生成报告
            createReport(pathFileName, dataMap);
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }

    /**
     * 分组方式保存装配记录
     */
    public void saveAssembReocrdBtype(Boolean flag) {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();
        Boolean xyValue = true;

        for (int i = 0; i < order.getProductNumber(); i++) {
            String snBatch = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            //****单件
            for (int s = 0; s < sSize; s++) {
                TextField tfPartNo = (TextField) detailLayoutGrid.getComponent(0, s + 1);
                String partNo = tfPartNo.getValue();

                Assembling singleAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(s));
                if (singleAssemb == null) {
                    singleAssemb = new Assembling();
                }
                singleAssemb.setOrderNo(order.getProductOrderId());
                singleAssemb.setRetrospectType("singleton");
                singleAssemb.setSnBatch(snBatch);

                String tempValue0 = tfPartNo.getValue();
                singleAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

                TextArea tfPartDesc = (TextArea) detailLayoutGrid.getComponent(1, s + 1);
                String tempValue1 = tfPartDesc.getValue();
                singleAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(2, s + 1);
                String tempValue2 = tfPlmRev.getValue();
                singleAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3, s + 1);
                String tempValue3 = tfPartRev.getValue();
                singleAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

                TextArea tfSerialNo = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 5, s + 1);
                String tempValue4 = tfSerialNo.getValue();
                singleAssemb.setSerialNo(tempValue4 == null ? "" : tempValue4);
                if (xyValue) {
                    if (Strings.isNullOrEmpty(tempValue4)) {
                        x = 2 * (i) + 5;
                        y = s + 1;
                        xyValue = false;
                    }
                }

                TextArea tfHeatNoLot = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 6, s + 1);
                String tempValue5 = tfHeatNoLot.getValue();
                singleAssemb.setHeatNoLot(tempValue5 == null ? "" : tempValue5);
                if (xyValue) {
                    if (Strings.isNullOrEmpty(tempValue5)) {
                        x = 2 * (i) + 6;
                        y = s + 1;
                        xyValue = false;
                    }
                }

                singleAssemb.setHardness("/");
                singleAssemb.setMatType("/");
                singleAssemb.setQcCheck("/");
                singleAssemb.setDescription(Integer.toString(s));

                assemblingService.save(singleAssemb);
            }
            //*******批次
            for (int b = 0; b < bSize; b++) {

                TextField tfPartNo = (TextField) detailLayoutGrid.getComponent(0, b + sSize + 2);
                String partNo = tfPartNo.getValue();

                Assembling bAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(b));
                if (bAssemb == null) {
                    bAssemb = new Assembling();
                }
                bAssemb.setOrderNo(order.getProductOrderId());
                bAssemb.setRetrospectType("batch");
                bAssemb.setSnBatch(snBatch);

                String tempValue0 = tfPartNo.getValue();
                bAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

                TextArea tfPartDesc = (TextArea) detailLayoutGrid.getComponent(1, b + sSize + 2);
                String tempValue1 = tfPartDesc.getValue();
                bAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(2, b + sSize + 2);
                String tempValue2 = tfPlmRev.getValue();
                bAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3, b + sSize + 2);
                String tempValue3 = tfPartRev.getValue();
                bAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

                TextField tfBatchQty = (TextField) detailLayoutGrid.getComponent(4, b + sSize + 2);
                String tempValue4 = tfBatchQty.getValue();
                bAssemb.setBatchQty(new Integer(tempValue4));

                TextArea tfBatch = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 5, b + sSize + 2);
                String tempValue5 = tfBatch.getValue();
                bAssemb.setBatch(tempValue5 == null ? "" : tempValue5);
                if (xyValue) {
                    if (Strings.isNullOrEmpty(tempValue5)) {
                        x = 2 * (i) + 5;
                        y = b + sSize + 2;
                        xyValue = false;
                    }
                }

                TextArea tfHeatNoLot = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 6, b + sSize + 2);
                String tempValue6 = tfHeatNoLot.getValue();
                bAssemb.setHeatNoLot(tempValue6 == null ? "" : tempValue6);
                if (xyValue) {
                    if (Strings.isNullOrEmpty(tempValue6)) {
                        x = 2 * (i) + 6;
                        y = b + sSize + 2;
                        xyValue = false;
                    }
                }

                bAssemb.setHardness("/");
                bAssemb.setCOrD("/");
                bAssemb.setEOrD("/");
                bAssemb.setQcCheck("/");
                bAssemb.setDescription(Integer.toString(b));
                assemblingService.save(bAssemb);
            }
        }

        if (flag) {//完成，删除工单对应暂存记录
            AssemblingTemp assemblingTemp = assemblingTempService.getByOrderNo(order.getProductOrderId());
            if (assemblingTemp != null) {
                assemblingTempService.delete(assemblingTemp);
            }

        } else {//暂存：添加或更新工单对应暂存记录
            AssemblingTemp assemblingTemp = assemblingTempService.getByOrderNo(order.getProductOrderId());
            if (assemblingTemp == null) {
                assemblingTemp = new AssemblingTemp();
            }
            assemblingTemp.setOrderNo(order.getProductOrderId());
            assemblingTemp.setxValue(x);
            assemblingTemp.setyValue(y);
            assemblingTemp.setCountNo(countNo);
            assemblingTemp.setCheckType("分组");
            assemblingTempService.save(assemblingTemp);
        }

    }

    /**
     * 分组方式装配报告数据
     *
     * @param path
     */
    public void reportDatesBtype(String path) {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();
        for (int i = 0; i < order.getProductNumber(); i++) {
            //****单件
            List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
            for (int s = 0; s < sSize; s++) {
                TextField tfPartNo = (TextField) detailLayoutGrid.getComponent(0, s + 1);
                String tempValue0 = tfPartNo.getValue();

                TextArea tfPartDesc = (TextArea) detailLayoutGrid.getComponent(1, s + 1);
                String tempValue1 = tfPartDesc.getValue();

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(2, s + 1);
                String tempValue2 = tfPlmRev.getValue();

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3, s + 1);
                String tempValue3 = tfPartRev.getValue();

                TextArea tfSerialNo = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 5, s + 1);
                String tempValue4 = tfSerialNo.getValue();

                TextArea tfHeatNoLot = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 6, s + 1);
                String tempValue5 = tfHeatNoLot.getValue();

                Map<String, String> sMap = new HashMap<>();
                sMap.put("sPartNo", tempValue0 == null ? "" : tempValue0);
                sMap.put("sPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                sMap.put("sPlmRev", tempValue2 == null ? "" : tempValue2);
                sMap.put("sPartRev", tempValue3 == null ? "" : tempValue3);
                sMap.put("sSN", tempValue4 == null ? "" : tempValue4);
                sMap.put("sHeat", tempValue5 == null ? "" : tempValue5);
                sMap.put("sHardness", "/");
                sMap.put("sMat", "/");
                sMap.put("sQC", "/");
                sList.add(sMap);
            }
            //批次
            List<Map<String, String>> bList = new ArrayList<Map<String, String>>();
            for (int b = 0; b < bSize; b++) {
                TextField tfPartNo = (TextField) detailLayoutGrid.getComponent(0, b + sSize + 2);
                String tempValue0 = tfPartNo.getValue();

                TextArea tfPartDesc = (TextArea) detailLayoutGrid.getComponent(1, b + sSize + 2);
                String tempValue1 = tfPartDesc.getValue();

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(2, b + sSize + 2);
                String tempValue2 = tfPlmRev.getValue();

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3, b + sSize + 2);
                String tempValue3 = tfPartRev.getValue();

                TextField tfBatchQty = (TextField) detailLayoutGrid.getComponent(4, b + sSize + 2);
                String tempValue4 = tfBatchQty.getValue();

                TextArea tfBatch = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 5, b + sSize + 2);
                String tempValue5 = tfBatch.getValue();

                TextArea tfHeatNoLot = (TextArea) detailLayoutGrid.getComponent(2 * (i) + 6, b + sSize + 2);
                String tempValue6 = tfHeatNoLot.getValue();

                Map<String, String> bMap = new HashMap<>();
                bMap.put("bPartNo", tempValue0 == null ? "" : tempValue0);
                bMap.put("bPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                bMap.put("bPlmRev", tempValue2 == null ? "" : tempValue2);
                bMap.put("bPartRev", tempValue3 == null ? "" : tempValue3);
                bMap.put("bQty", new Integer(tempValue4) < 1 ? "1" : new Integer(tempValue4) + "");
                bMap.put("bBatch", tempValue5 == null ? "" : tempValue5);
                bMap.put("bHeat", tempValue6 == null ? "" : tempValue6);
                bMap.put("bHardness", "/");
                bMap.put("bCD", "/");
                bMap.put("bED", "/");
                bMap.put("bQC", "/");
                bList.add(bMap);
            }
            Map<String, Object> dataMap = new HashMap<>();
            String strSnBatch = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            dataMap.put("tSnBatch", strSnBatch);
            dataMap.put("tQty", i + 1 + " of " + order.getProductNumber());
            dataMap.put("slist", sList);
            dataMap.put("blist", bList);
            String pathFileName = path + strSnBatch + ".doc";
            try {
                //生成报告
                createReport(pathFileName, dataMap);
            } catch (Exception e) {
                throw new PlatformException(e.getMessage());
            }
        }

    }

    /**
     * 生成报告
     */
    public void createReport(String pathFileName, Map<String, Object> dataMap) throws Exception {

        // 准备填充公共数据
        dataMap.put("tMaterial", tfProductNo.getValue());
        dataMap.put("tRev", tfProductRev.getValue());
        dataMap.put("tDesc", tfProductDec.getValue().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        dataMap.put("tOrder", tfOrder.getValue());

        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy.MM.dd");
        dataMap.put("RDate", myFmt.format(new Date()));
        dataMap.put("tDate", myFmt.format(new Date()));

        // 将Media的InputStream转换为byte[] 然后进行BASE64编码
        BASE64Encoder encoder = new BASE64Encoder();

        Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
        if (mediaImage == null) {
            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
        }
        dataMap.put("RecordByImage", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));

        // Configuration用于读取模板文件(XML、FTL文件或迅是我们这里用的StringTemplateLoader)
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        // 指定模板文件所在目录的路径,而不是文件的路径
        String jPath = AppConstant.DOC_XML_FILE_PATH;
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        // 以utf-8的编码读取模板文件
        Template template = configuration.getTemplate("assembling.xml", "utf-8");

        //输出文件
        File outFile = new File(pathFileName);

        //将模板和数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                1024 * 1024);
        template.process(dataMap, out);
        out.flush();
        out.close();
        wordToPDF(pathFileName);
        System.out.println("*********成功生成装配报告-" + pathFileName + "***********");
        NotificationUtils.notificationInfo("成功生成装配报告-" + pathFileName);
    }

    private byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        return outputStream.toByteArray();
    }

    /**
     * 单件type标题数据
     */
    public void freshLab() {
        tfOrder.setValue(order.getProductOrderId());
        List<String> strList = new ArrayList<>();
        for (int i = 1; i <= order.getProductNumber(); i++) {
            strList.add(order.getProductOrderId() + String.format("%4d", i).replace(" ", "0"));
        }
        cbSnBatch.clear();
        cbSnBatch.setItems(strList);
        cbSnBatch.setEmptySelectionAllowed(false);
        cbSnBatch.setTextInputAllowed(false);
        cbSnBatch.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 6L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                if (Strings.isNullOrEmpty(event.getValue())) {
                    tfQty.setValue("");
                    btnOpenVoice.setEnabled(false);
                    btnSaveCommit.setEnabled(false);
                } else {
                    String strSeq = event.getValue().substring(event.getValue().length() - 2);
                    tfQty.setValue(Integer.parseInt(strSeq) + " of " + order.getProductNumber());

                    //动态生成gridlayout
                    autoSetDetailGrid();
                    //set已存值
                    setStypeValue(event.getValue());
                    btnOpenVoice.setEnabled(true);
                    btnSaveCommit.setEnabled(true);
                }

            }
        });
        tfProductNo.setValue(order.getProductId());
        tfProductRev.setValue(order.getProductVersionId());
        tfProductDec.setValue(order.getProductInformation().getProductDesc() == null ? "" : order.getProductInformation().getProductDesc());
    }

    public void setStypeValue(String snBatch) {
        //单件明细
        for (int s = 0; s < singletonDetail.size(); s++) {
            TextField txPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            Assembling assembling = assemblingService.getBySnAndPartNo(snBatch, txPartNo.getValue().trim(), Integer.toString(s));
            TextArea taSN = (TextArea) singletonGrid.getComponent(5, s + 1);
            TextArea taHeat = (TextArea) singletonGrid.getComponent(6, s + 1);
            if (assembling != null) {
                taSN.setValue(assembling.getSerialNo() == null ? "" : assembling.getSerialNo());
                taHeat.setValue(assembling.getHeatNoLot() == null ? "" : assembling.getHeatNoLot());
            } else {
                taSN.clear();
                taHeat.clear();
            }
        }
        //批次明细
        for (int b = 0; b < batchDetail.size(); b++) {
            TextField txPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            Assembling assembling = assemblingService.getBySnAndPartNo(snBatch, txPartNo.getValue().trim(), Integer.toString(b));
            TextArea taSN = (TextArea) batchGrid.getComponent(5, b + 1);
            TextArea taHeat = (TextArea) batchGrid.getComponent(6, b + 1);
            if (assembling != null) {
                taSN.setValue(assembling.getBatch() == null ? "" : assembling.getBatch());
                taHeat.setValue(assembling.getHeatNoLot() == null ? "" : assembling.getHeatNoLot());
            } else {
                taSN.clear();
                taHeat.clear();
            }
        }

    }

    /**
     * 分组type标题数据
     */
    public void freshLabBtype() {
        tfOrder.setValue(order.getProductOrderId());
        tfQty.setValue(order.getProductNumber() + "");
        tfSnBatch.setValue(order.getProductOrderId() + String.format("%4d", 1).replace(" ", "0") +
                " -- " + order.getProductOrderId() + String.format("%4d", order.getProductNumber()).replace(" ", "0"));
        tfProductNo.setValue(order.getProductId());
        tfProductRev.setValue(order.getProductVersionId());
        tfProductDec.setValue(order.getProductInformation().getProductDesc() == null ? "" : order.getProductInformation().getProductDesc());
    }

    /**
     * 单件type追溯零件
     */
    public void freshGrid() {
        singletonDetail = new ArrayList<>();
        batchDetail = new ArrayList<>();

        List<Bom> singletonBomList = order.getSingletonBoms();
        for (Bom bom : singletonBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling single = bomToAssemb(bom, "singleton");
            for (int i = 0; i < single.getBatchQty(); i++) {
                singletonDetail.add(single);
            }
        }

        List<Bom> batchBomList = order.geBatchBoms();
        for (Bom bom : batchBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling batch = bomToAssemb(bom, "batch");
            batchDetail.add(batch);
        }
        //加载页面显示数据
        freshLab();
        autoSetDetailGrid();
    }

    public void autoSetDetailGrid() {
        vlDetailGrid.setMargin(true);
//        vlDetailGrid.setSizeFull();
        vlDetailGrid.setSpacing(true);
        panelDetail.setContent(vlDetailGrid);

        //***********单件
        singletonGrid = new GridLayout(7, singletonDetail.size() + 1);
        singletonGrid.setSpacing(true);
        singletonGrid.setWidth("100%");
        singletonGrid.setMargin(true);
        singletonGrid.setHeightUndefined();
        if (singletonDetail.size() > 0) {
            //第一行
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）")), 0, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No")), 5, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot")), 6, 0);

            //明细
            for (int s = 0; s < singletonDetail.size(); s++) {
                Assembling singleAssembling = singletonDetail.get(s);
                singletonGrid.addComponent(getTextField(singleAssembling.getPartNo(), false, "125px"), 0, s + 1);
                singletonGrid.addComponent(getTextArea(singleAssembling.getPartDesc(), false, "200px"), 1, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getPlmRev(), true, "50px"), 2, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getPartRev(), true, "50px"), 3, s + 1);
                TextField tfQty = new TextField();
                tfQty.setWidth("1px");
                Integer value = singleAssembling.getBatchQty();
                tfQty.setValue(value == null ? "1" : value.toString());
                tfQty.setVisible(false);
                singletonGrid.addComponent(tfQty, 4, s + 1);
                singletonGrid.addComponent(getTextArea(singleAssembling.getSerialNo(), true, "200px"), 5, s + 1);
                singletonGrid.addComponent(getTextArea(singleAssembling.getHeatNoLot(), true, "120px"), 6, s + 1);
            }
        }

        //**********批次
        batchGrid = new GridLayout(7, batchDetail.size() + 1);
        batchGrid.setSpacing(true);
        batchGrid.setMargin(true);
        batchGrid.setWidth("100%");
        batchGrid.setHeightUndefined();
        if (batchDetail.size() > 0) {
            //第一行
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）")), 0, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Qty", "Qty")), 4, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Batch")), 5, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot")), 6, 0);
            //明细
            for (int b = 0; b < batchDetail.size(); b++) {
                Assembling bAssembling = batchDetail.get(b);
                batchGrid.addComponent(getTextField(bAssembling.getPartNo(), false, "125px"), 0, b + 1);
                batchGrid.addComponent(getTextArea(bAssembling.getPartDesc(), false, "200px"), 1, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getPlmRev(), true, "50px"), 2, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getPartRev(), true, "50px"), 3, b + 1);
                TextField tfQty = new TextField();
                tfQty.setWidth("30px");
                Integer value = bAssembling.getBatchQty();
                tfQty.setValue(value == null ? "1" : value.toString());
                tfQty.addValueChangeListener(event -> {
                    String newValue = event.getValue().trim();
                    if (!Strings.isNullOrEmpty(newValue)) {
                        boolean isNumber = RegExpValidatorUtils.isIsPositive(newValue);
                        if (!isNumber) {
                            NotificationUtils.notificationError(I18NUtility.getValue("Common.OnlyNumberAllowed", "please input greater than zero number!"));
                        }
                    }

                });
                batchGrid.addComponent(tfQty, 4, b + 1);
                batchGrid.addComponent(getTextArea(bAssembling.getBatch(), true, "200px"), 5, b + 1);
                batchGrid.addComponent(getTextArea(bAssembling.getHeatNoLot(), true, "120px"), 6, b + 1);
            }
        }
        vlDetailGrid.removeAllComponents();
        vlDetailGrid.addComponents(singletonGrid, batchGrid);

        if (singletonDetail.size() == 0) {
            singleOrBatch = false;
        } else {
            singleOrBatch = true;
        }
        x = 5;
        y = 1;
    }

    /**
     * 分组type追溯零件
     */
    public void freshGridBtype() {
        singletonDetail = new ArrayList<>();
        batchDetail = new ArrayList<>();
        List<Assembling> assemblings = assemblingService.getByOrderNo(order.getProductOrderId());
        AssemblingTemp assemblingTemp = assemblingTempService.getByOrderNo(order.getProductOrderId());
        if (assemblings != null && assemblings.size() > 0 && assemblingTemp == null) {
            NotificationUtils.notificationInfo("该订单装配已完成");
        } else {
            List<Bom> singletonBomList = order.getSingletonBoms();
            for (Bom bom : singletonBomList) {
                if (bom.getSparePart() == null) {
                    continue;
                }
                Assembling single = bomToAssembBtype(bom, "singleton");
                for (int i = 0; i < single.getBatchQty(); i++) {
                    singletonDetail.add(single);
                }
            }
            List<Bom> batchBomList = order.geBatchBoms();
            for (Bom bom : batchBomList) {
                if (bom.getSparePart() == null) {
                    continue;
                }
                Assembling batch = bomToAssembBtype(bom, "batch");
                batchDetail.add(batch);
            }
            //动态生成gridlayout
            autoSetDetailGridBtype();
            freshLabBtype();
            //是否暂存过
            if (assemblingTemp != null) {
                x = assemblingTemp.getxValue();
                y = assemblingTemp.getyValue();
                countNo = assemblingTemp.getCountNo();
                System.out.println("*temp* x=" + x + ",y=" + y + ",countNo=" + countNo);
                Boolean sFlag = true;
                //TODO set暂存值
                for (int i = 1; i <= y; i++) {
                    if (i == singletonDetail.size() + 1) {
                        sFlag = false;
                        continue;
                    }
                    String partNo = ((TextField) detailLayoutGrid.getComponent(0, i)).getValue();
                    for (int j = 5, n = 1; j <= detailLayoutGrid.getColumns() - 1; j += 2, n++) {
//                        if (i >= y && j >= x) {
//                            break;
//                        }
                        String snBatch = order.getProductOrderId() + String.format("%4d", n).replace(" ", "0");
                        Assembling assembling;
                        if (sFlag) {
                            assembling = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(i - 1));
                        } else {
                            assembling = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(i - 1 - singletonDetail.size()));
                        }
                        if (assembling != null) {
                            TextArea taSN = (TextArea) detailLayoutGrid.getComponent(j, i);
                            TextArea taHeat = (TextArea) detailLayoutGrid.getComponent(j + 1, i);
                            if (sFlag) {//单件
                                taSN.setValue(assembling.getSerialNo() == null ? "" : assembling.getSerialNo());
                            } else {//批次
                                taSN.setValue(assembling.getBatch() == null ? "" : assembling.getBatch());
                            }

                            taHeat.setValue(assembling.getHeatNoLot() == null ? "" : assembling.getHeatNoLot());
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        btnOpenVoice.setEnabled(true);
        btnScan.setEnabled(true);
        btnTempSave.setEnabled(true);
        btnSaveCommit.setEnabled(true);
    }

    public void autoSetDetailGridBtype() {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();
        int columns = order.getProductNumber() * 2 + 5;
        int rows = sSize + bSize + 2;
        detailLayoutGrid = new GridLayout(columns, rows);
        detailLayoutGrid.setSpacing(true);
        detailLayoutGrid.setMargin(true);
        panelDetail.setContent(detailLayoutGrid);

        //singleton第一行
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）")), 0, 0);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, 0);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, 0);
        for (int n = 5, i = 1; n < columns; n += 2, i++) {
            detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No") + "(" + i + ")"), n, 0);
            detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot") + "(" + i + ")"), n + 1, 0);
        }
        //明细
        for (int s = 0; s < sSize; s++) {
            Assembling singleAssembling = singletonDetail.get(s);
            detailLayoutGrid.addComponent(getTextField(singleAssembling.getPartNo(), false, "125px"), 0, s + 1);
            detailLayoutGrid.addComponent(getTextArea(singleAssembling.getPartDesc(), false, "200px"), 1, s + 1);
            detailLayoutGrid.addComponent(getTextField(singleAssembling.getPlmRev(), true, "50px"), 2, s + 1);
            detailLayoutGrid.addComponent(getTextField(singleAssembling.getPartRev(), true, "50px"), 3, s + 1);
            TextField tfQty = new TextField();
            tfQty.setWidth("1px");
            Integer value = singleAssembling.getBatchQty();
            tfQty.setValue(value == null ? "1" : value.toString());
            tfQty.setVisible(false);
            detailLayoutGrid.addComponent(tfQty, 4, s + 1);
            for (int n = 5; n < columns; n += 2) {
                detailLayoutGrid.addComponent(getTextArea(singleAssembling.getSerialNo(), true, "200px"), n, s + 1);
                detailLayoutGrid.addComponent(getTextArea(singleAssembling.getHeatNoLot(), true, "120px"), n + 1, s + 1);
            }
        }

        //batch第一行
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）")), 0, sSize + 1);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, sSize + 1);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, sSize + 1);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, sSize + 1);
        detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Qty", "Qty")), 4, sSize + 1);
        for (int n = 5, i = 1; n < columns; n += 2, i++) {
            detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Batch") + "(" + i + ")"), n, sSize + 1);
            detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot") + "(" + i + ")"), n + 1, sSize + 1);
        }
        //明细
        for (int b = 0; b < batchDetail.size(); b++) {
            Assembling bAssembling = batchDetail.get(b);
            detailLayoutGrid.addComponent(getTextField(bAssembling.getPartNo(), false, "125px"), 0, sSize + 1 + b + 1);
            detailLayoutGrid.addComponent(getTextArea(bAssembling.getPartDesc(), false, "200px"), 1, sSize + 1 + b + 1);
            detailLayoutGrid.addComponent(getTextField(bAssembling.getPlmRev(), true, "50px"), 2, sSize + 1 + b + 1);
            detailLayoutGrid.addComponent(getTextField(bAssembling.getPartRev(), true, "50px"), 3, sSize + 1 + b + 1);
            TextField tfQty = new TextField();
            tfQty.setWidth("30px");
            Integer value = bAssembling.getBatchQty();
            tfQty.setValue(value == null ? "1" : value.toString());
            tfQty.addValueChangeListener(event -> {
                String newValue = event.getValue().trim();
                if (!Strings.isNullOrEmpty(newValue)) {
                    boolean isNumber = RegExpValidatorUtils.isIsPositive(newValue);
                    if (!isNumber) {
                        NotificationUtils.notificationError(I18NUtility.getValue("Common.OnlyNumberAllowed", "please input greater than zero number!"));
                    }
                }

            });
            detailLayoutGrid.addComponent(tfQty, 4, sSize + 1 + b + 1);
            for (int n = 5; n < columns; n += 2) {
                detailLayoutGrid.addComponent(getTextArea(bAssembling.getBatch(), true, "200px"), n, sSize + 1 + b + 1);
                detailLayoutGrid.addComponent(getTextArea(bAssembling.getHeatNoLot(), true, "120px"), n + 1, sSize + 1 + b + 1);
            }
        }
        System.out.println(detailLayoutGrid.getColumns() + "------------" + detailLayoutGrid.getRows());
        if (singletonDetail.size() == 0) {
            x = 5;
            y = 2;
        } else {
            x = 5;
            y = 1;
        }

    }

    /**
     * 分组方式语音录入
     */
    public void VoiceInputBtype() {
        try {
            String ipAddress = RequestInfo.current().getUserIpAddress();//语音服务端ip地址
            //连接语音服务
            client = new SocketClient(ipAddress, AppConstant.PORT);
            //监听信息并逻辑处理
            client.setReceiveListener(message -> {
                if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)
                        && AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
                    // [OC]语音播放结束
                    System.out.println("Receive Message -> " + message);
                    // 发送请求语音识别的指令
                    try {
                        prefixSend = AppConstant.PREFIXSTARTRECORD;
                        client.sendMessage("[BR]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)
                        && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                    // 发送语音识别请求并且返回的是语音识别结束字串
                    // message去除信息头
                    String messageBody = message.split("]")[1];
                    if (!message.split("\\|")[1].equals("1")) {
                        returnMessage.append(messageBody.split("\\|")[0]);
                    }
                } else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {//一个字段录入完，开始校验数据正确性并set值
                    if (returnMessage.length() == 0) {//录入空
                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                        try {
                            String strTitle = "";
                            String strPartNumber = "";
                            strTitle = ((Label) detailLayoutGrid.getComponent(x, 0)).getValue();
                            strPartNumber = ((TextField) detailLayoutGrid.getComponent(0, y)).getValue();

                            if (isValue) {
                                client.sendMessage("[PT]请输入零件编号" + strPartNumber + "对应的" + strTitle + "值");
                            } else {
                                client.sendMessage("[PT]该零件" + strPartNumber + "对应的值是否录入完成");
                            }
                        } catch (Exception e) {
                            NotificationUtils.notificationError("Socket连接中断");
                        }
                    } else {//录入有值
                        //set值
                        getUI().accessSynchronously(new Runnable() {
                            @Override
                            public void run() {
                                TextArea tf;
                                String strQty;

                                tf = ((TextArea) detailLayoutGrid.getComponent(x, y));
                                strQty = ((TextField) detailLayoutGrid.getComponent(4, y)).getValue();

                                if (isValue) {//录入值
                                    String inputString = VoiceStringSolveUtils.solveString(returnMessage.toString().trim());
                                    if (countNo == 1) {//
                                        tf.setValue(inputString.toUpperCase());
                                    } else {
                                        tf.setValue(tf.getValue() + "\r\n" + inputString.toUpperCase());
                                    }

                                    if (x % 2 == 0) {
                                        int qty = Integer.parseInt(strQty);
                                        if (qty == 1) {
                                            countNo = 1;
                                            isValue = true;
                                        } else if (qty == countNo) {
                                            countNo = 1;
                                            isValue = true;
                                        } else {
                                            isValue = false;
                                            x -= 1;
                                        }
                                    }
                                } else {//录入是否
                                    if (returnMessage.toString().trim().contains("是")) {
                                        countNo = 1;
                                        isValue = true;
                                    } else {//该零件需要继续录入一条序列号和熔炼炉号
                                        countNo += 1;
                                        isValue = true;
                                        x -= 2;
                                    }
                                }
                                returnMessage.delete(0, returnMessage.length());
                            }
                        });
                        //开始下一个字段
                        prefixSend = AppConstant.PREFIXPLAYTEXT;

                        if (x < detailLayoutGrid.getColumns() - 1) {
                            x += 1;
                        } else {
                            x = 5;
                            y += 1;
                            if (y == singletonDetail.size() + 1) {//单件完成，重置（x,y）定位
                                y += 1;
                            }
                        }

                        try {
                            System.out.println(x + "------" + y + "*****" + detailLayoutGrid.getRows());
                            if (y >= detailLayoutGrid.getRows()) {
                                //全部录入完成
                                prefixSend = AppConstant.INSPECTIONDONE;
                                client.sendMessage("[PT]装配信息录入结束完成");
                                client.close();
                            }
                            String strTitle = ((Label) detailLayoutGrid.getComponent(x, 0)).getValue();
                            String strPartN = ((TextField) detailLayoutGrid.getComponent(0, y)).getValue();
                            if (isValue) {
                                client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
                            } else {
                                client.sendMessage("[PT]该零件对应的值是否录入完成");
                            }
                        } catch (Exception e) {
                            NotificationUtils.notificationError("Socket连接中断");
                        }
                    }
                } else if (message.startsWith(AppConstant.PALYEXCEPTION)) {
                    System.out.println("语音服务网络断开，正尝试重新连接");
                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                    try {
                        String strTitle = ((Label) detailLayoutGrid.getComponent(x, 0)).getValue();
                        String strPartN = ((TextField) detailLayoutGrid.getComponent(0, y)).getValue();
                        if (isValue) {
                            client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
                        } else {
                            client.sendMessage("[PT]该零件" + strPartN + "对应的值是否录入完成");
                        }
                    } catch (Exception e) {
                        NotificationUtils.notificationError("Socket连接中断");
                    }
                }
            });
            //首次第一个输入值
            prefixSend = AppConstant.PREFIXPLAYTEXT;
            String strTitle = "";
            String strPartN = "";
            strTitle = ((Label) detailLayoutGrid.getComponent(x, 0)).getValue();
            strPartN = ((TextField) detailLayoutGrid.getComponent(0, y)).getValue();
            client.sendMessage("[PT]请输入零件编号" + strPartN + "对应的" + strTitle + "值");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Assembling bomToAssemb(Bom bom, String type) {
        Assembling assemb = new Assembling();
        assemb.setOrderNo(order.getProductOrderId());
        assemb.setRetrospectType(type);
        assemb.setPartNo(bom.getSparePart().getSparePartNo() == null ? "" : bom.getSparePart().getSparePartNo());
        assemb.setPartDesc(bom.getSparePart().getSparePartDec() == null ? "" : bom.getSparePart().getSparePartDec());
        assemb.setPlmRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setPartRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setHardness(bom.getSparePart().getHardnessFile() == null ? "" : bom.getSparePart().getHardnessFile());
        assemb.setBatchQty(bom.getPartQuantity() < 1 ? 1 : bom.getPartQuantity());
        return assemb;
    }

    public Assembling bomToAssembBtype(Bom bom, String type) {
        Assembling assemb = new Assembling();
        assemb.setOrderNo(order.getProductOrderId());
        assemb.setRetrospectType(type);
        assemb.setPartNo(bom.getSparePart().getSparePartNo() == null ? "" : bom.getSparePart().getSparePartNo());
        assemb.setPartDesc(bom.getSparePart().getSparePartDec() == null ? "" : bom.getSparePart().getSparePartDec());
        assemb.setPlmRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setPartRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setHardness(bom.getSparePart().getHardnessFile() == null ? "" : bom.getSparePart().getHardnessFile());
        assemb.setBatchQty(bom.getPartQuantity() < 1 ? 1 : bom.getPartQuantity());
        return assemb;
    }

    public TextField getTextField(String tempValue, Boolean isAble, String strWidth) {
        TextField tf = new TextField();
        tf.setWidth(strWidth);
        tf.setHeight("50px");
        if (!isAble) {
            tf.setReadOnly(true);
            tf.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        }

        tf.setValue(tempValue == null ? "" : tempValue);
        return tf;
    }

    public TextArea getTextArea(String tempValue, Boolean isAble, String strWidth) {
        TextArea ta = new TextArea();
        ta.setWidth(strWidth);
        ta.setHeight("50px");
        if (!isAble) {
            ta.setReadOnly(true);
            ta.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        }

        ta.setValue(tempValue == null ? "" : tempValue);
        return ta;
    }

    @Override
    protected void init() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        cbType.setItems("sType", "bType");
        cbType.setItemCaptionGenerator(select -> select.equals("sType") ? "单件" : "分组");
    }


    @Override
    public void updateAfterFilterApply() {
        // TODO Auto-generated method stub

    }

    public void wordToPDF(String filePath) {
//        ComThread.InitSTA();
//        ActiveXComponent app = null;
//        Dispatch doc = null;
//        //转换前的文件路径
//        String startFile = filePath;// "D:\\TestWord\\test" + ".docx";
//        //转换后的文件路劲
//        String overFile = "";
//        try {
//            app = new ActiveXComponent("Word.Application");
//            app.setProperty("Visible", new Variant(false));
//            Dispatch docs = app.getProperty("Documents").toDispatch();
//
//            overFile = startFile.replace(".doc", ".pdf");
//
//            doc = Dispatch.call(docs, "Open", startFile).toDispatch();
//            File tofile = new File(overFile);
//            if (tofile.exists()) {
//                tofile.delete();
//            }
//            Dispatch.call(doc, "SaveAs", overFile, 17);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            Dispatch.call(doc, "Close", false);
//            if (app != null)
//                app.invoke("Quit", new Variant[]{});
//        }
//        //结束后关闭进程
//        System.out.println(LocalDateTime.now());
//        ComThread.Release();

        final Document document = new Document(filePath);
        final String overFile = filePath.replace(".doc", ".pdf");
        document.saveToFile(overFile, FileFormat.PDF);
    }

    public void inputValueBType(String input){
        //TODO:Input for BType
    }

    public void inputValueSType(String input){
        //TODO:Input for BType
    }

    public void focusOnSnBType(){
        //TODO:Highlight current sn BType
    }

    public void focusOnSnSType(){
        //TODO:Highlight current sn SType
    }
}
