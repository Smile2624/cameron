package com.ags.lumosframework.ui.view.hardnesstestreport;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.TextBlob;
import com.ags.lumosframework.sdk.service.TextBlobService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Menu(caption = "HardnessTestReport", captionI18NKey = "view.hardnessTestReport.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 2)
@SpringView(name = "HardnessTestReport", ui = CameronUI.class)
public class HardnessTestReportView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = 5259668366342485914L;
    VerticalLayout hlToolBox = new VerticalLayout();
    VerticalLayout vlDisplay = new VerticalLayout();
    GridLayout glLayout = new GridLayout();
    Panel inspectionValue = new Panel();
    HorizontalLayout hlTemp1 = new HorizontalLayout();
    HorizontalLayout hlTemp2 = new HorizontalLayout();
    HorizontalLayout hlTemp3 = new HorizontalLayout();
    HorizontalLayout hlTemp4 = new HorizontalLayout();
    // 保存检验记录
    List<HardnessTestReportItems> hardnessTestReportItemsList = new ArrayList<>();
    SparePart part = null;
    String acceptRange = "";
    StringBuilder ifSave = new StringBuilder();
    private ComboBox<String> cbPurchaseOrder = new ComboBox();
    @I18Support(caption = "Confirm", captionKey = "view.hardnessTestReport.confirm")
    private Button btnConfirm = new Button();
    @I18Support(caption = "Start", captionKey = "common.start")
    private Button btnStart = new Button();
    @I18Support(caption = "Skip", captionKey = "common.skip")
    private Button btnSkip = new Button();
    @I18Support(caption = "AcceptableRange", captionKey = "view.hardnessTestReport.AcceptableRange")
    private LabelWithSamleLineCaption lblAcceptableRange = new LabelWithSamleLineCaption();
    @I18Support(caption = "SerialNo", captionKey = "view.hardnessTestReport.SerialNo") // 销售订单号
    private TextField tfSerialNo = new TextField();
    @I18Support(caption = "Temp", captionKey = "view.hardnessTestReport.Temp") // 客户
    private TextField tfTemp = new TextField();
    @I18Support(caption = "MaterialMS", captionKey = "view.hardnessTestReport.MaterialMS")
    private LabelWithSamleLineCaption tfMaterialMS = new LabelWithSamleLineCaption();
    @I18Support(caption = "PartNo/Rev", captionKey = "view.hardnessTestReport.PartNo/Rev")
    private LabelWithSamleLineCaption lblPartNoRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "PartDesc", captionKey = "view.hardnessTestReport.PartDesc")
    private LabelWithSamleLineCaption lblPartDesc = new LabelWithSamleLineCaption();
    @I18Support(caption = "Quantity", captionKey = "view.hardnessTestReport.Quantity")
    private LabelWithSamleLineCaption lblQuantity = new LabelWithSamleLineCaption();
    @I18Support(caption = "QP/Rev", captionKey = "view.hardnessTestReport.QP/Rev")
    private LabelWithSamleLineCaption lblQPRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "ProductOrder", captionKey = "view.hardnessTestReport.ProductOrder")
    private LabelWithSamleLineCaption lblProductOrder = new LabelWithSamleLineCaption();
    //	@I18Support(caption = "TestSpecification", captionKey = "view.hardnessTestReport.TestSpecification")
    private ComboBox<String> cbTestSpecification = new ComboBox();
    //	@I18Support(caption = "MeasuringType", captionKey = "view.hardnessTestReport.MeasuringType")
    private ComboBox<String> cbMeasuringType = new ComboBox();
    private ComboBox<String> cbInspectionType = new ComboBox<>();// 抽检;全检
    private TextField tfInspectionQuantity = new TextField();
    private Grid<PurchasingOrderInfo> gridObject = new Grid<>();
    private String purchasingOrder;
    private AbstractComponent[] components = new AbstractComponent[]{cbPurchaseOrder,
            tfInspectionQuantity, btnStart, btnConfirm, btnSkip};// ,cbInspectionType
    private LabelWithSamleLineCaption[] labelComponents = new LabelWithSamleLineCaption[]{lblPartNoRev, lblPartDesc,
            lblQuantity, lblQPRev, lblProductOrder, lblAcceptableRange};
    private PurchasingOrderInfo purchasingInfo;
    private String SAPBatchNo = "";
    private String purchaseOrderSubitem = "";
    // 选中一条采购单号记录后，记录当前单号的信息，零件号，零件版本，两件数量
    private String materialNo = "";
    private String materialRev = "";
    private int materialQuantity = 0;
    // 用户于Socket连接--本机IP
    private String ipAddress = "";
    private SocketClient client = null;
    private String prefixSend = "";// 用于表示当前发送的语音命令
    private StringBuilder returnMessage = new StringBuilder();// 用于存储语音识别的字串
    private List<String> columnsList;
    // 记录开始位置
    private int x = 0;
    private int y = 0;
    private float minValue = 0;
    private float maxValue = 0;
    private int acceptQuantity = 0;
    private int rejectedQuantity = 0;
    @Autowired
    private IPurchasingOrderService purchasingOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IHardnessService hardnessService;
    @Autowired
    private IHardnessTestReportService hardnessTestReportService;
    @Autowired
    private IHardnessTestReportItemsService hardnessTestReportItemsService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private ICaConfigService caConfigService;
    private CaConfig con;
    @Autowired
    private IUniqueTraceabilityService traceabilityService;
    private int inspetionQuantity = 0;
    private boolean ensureMessage = false;
    // 表示当前检验的采购单是否通过检验
    private boolean pass = true;

    public HardnessTestReportView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        // 1、采购订单输入框和确认保存按钮
        for (Component component : components) {
            hlTempToolBox.addComponent(component);
        }
        btnStart.addClickListener(this);
        btnStart.setIcon(VaadinIcons.START_COG);
        btnConfirm.addClickListener(this);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.setEnabled(false);
        btnSkip.addClickListener(this);
        btnSkip.setIcon(VaadinIcons.FAST_FORWARD);
        btnSkip.setEnabled(true);
        tfInspectionQuantity.setPlaceholder("检验数量");
        tfInspectionQuantity.setEnabled(false);
        cbPurchaseOrder.setPlaceholder(
                I18NUtility.getValue("view.HardnessTestReportItems.purchasingOrder", "purchasingOrder"));// "采购单号"
        tfInspectionQuantity.addValueChangeListener(new ValueChangeListener<String>() {

            private static final long serialVersionUID = -6608103730391353937L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                glLayout.removeAllComponents();
                String inputValue = event.getValue();
                if (!Strings.isNullOrEmpty(inputValue)) {
                    if (RegExpValidatorUtils.isIsPositive(inputValue)) {
                        int inspectionQuantity = Integer.valueOf(inputValue);// 检验数量
                        // 如果输入的检验数量大于实际订单数量
                        if (inspectionQuantity > materialQuantity) {
                            btnConfirm.setEnabled(false);
                            tfInspectionQuantity.setValue("");
                            NotificationUtils.notificationError("检验数量不能大于当前的采购订单的数量");
                            return;
                        }
                        if (inspectionQuantity == 0) {
                            tfInspectionQuantity.setValue("");
                            btnConfirm.setEnabled(false);
                            NotificationUtils.notificationError("检验数量需要输入大于0的数量");
                            return;
                        }
                        int rowCount = inspectionQuantity + 1;
                        glLayout.setColumns(5);
                        glLayout.setRows(inspectionQuantity + 1);
                        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                            for (int columnIndex = 0; columnIndex < 5; columnIndex++) {
                                // 第一行控件中加入Label显示列名
                                if (rowIndex == 0) {
                                    Label lblItem = new Label(columnsList.get(columnIndex));
                                    lblItem.addStyleName(CoreTheme.FONT_PRIMARY);
                                    glLayout.addComponent(lblItem, columnIndex, rowIndex);
                                } else {
                                    glLayout.addComponent(new TextField(), columnIndex, rowIndex);
                                }
                            }
                        }
                        btnConfirm.setEnabled(true);
                    } else {
                        tfInspectionQuantity.setValue("");
                        NotificationUtils.notificationError("请输入整数数字");
                        btnConfirm.setEnabled(false);
                    }
                }
            }
        });
        cbPurchaseOrder.addValueChangeListener(new ValueChangeListener<String>() {
            private static final long serialVersionUID = 5116447602774686272L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                columnsList = getColumns();
                if (!event.getValue().equals(event.getOldValue())) {
                    String purchasingNo = cbPurchaseOrder.getValue().trim();
                    if (!Strings.isNullOrEmpty(purchasingNo)) {
                        List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
                                .getUncheckedOrder(purchasingNo, "HARDNESS");
                        if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                            // 数据填入grid
                            gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
                            setDataToDisplayArea(null);
                        } else {
                            NotificationUtils
                                    .notificationError(I18NUtility.getValue("view.hardnessinspection.purchasingnotexist",
                                            "Purchasing No Not Exist or This Order Has Finished Hardness Inspection."));
                        }
                    }
                }
            }
        });
        // 2、采购订单子项带出的信息
        vlRoot.addComponent(vlDisplay);
        vlDisplay.setWidth("100%");
        vlDisplay.setMargin(true);
        vlDisplay.addComponent(hlTemp1);
        vlDisplay.addComponent(hlTemp2);
        vlDisplay.addComponent(hlTemp4);
        hlTemp1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp2.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp4.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp1.setWidth("100%");
        hlTemp2.setWidth("100%");
        hlTemp4.setWidth("75%");
        hlTemp1.addComponent(lblPartNoRev);
        hlTemp1.addComponent(lblQPRev);
        hlTemp1.addComponent(tfMaterialMS);
        hlTemp1.addComponent(lblAcceptableRange);
        hlTemp2.addComponent(cbMeasuringType);
        cbMeasuringType.setPlaceholder("压痕设备类型");
        hlTemp2.addComponent(cbTestSpecification);
        cbTestSpecification.setPlaceholder("测试程序");
        hlTemp2.addComponent(tfSerialNo);
        hlTemp2.addComponent(tfTemp);
        hlTemp4.addComponent(lblPartDesc);
        TextBlobService textBlobService = BeanManager.getService(TextBlobService.class);
        TextBlob textBlobSpecification = textBlobService.getByName("Test_Specification");
        if (textBlobSpecification == null) {
            NotificationUtils.notificationError("请配置试验程序");
        } else {
            String value = textBlobSpecification.getValue();
            cbTestSpecification.setItems(value.split(","));
        }
        TextBlob textBlobMeasuring = textBlobService.getByName("Measuring_Device_Type");
        if (textBlobMeasuring == null) {
            NotificationUtils.notificationError("请配置试验程序");
        } else {
            String value = textBlobMeasuring.getValue();
            cbMeasuringType.setItems(value.split(","));
        }
        tfTemp.addBlurListener(new BlurListener() {
            /**
             *
             */
            private static final long serialVersionUID = -6922056973844720398L;

            @Override
            public void blur(BlurEvent event) {
                TextField textField = (TextField) event.getSource();
                String textValue = textField.getValue().trim();
                if (!Strings.isNullOrEmpty(textValue) && !valueIsTemp(textValue)) {
                    textField.setValue("");
                    NotificationUtils.notificationError(textField.getCaption() + ",请填入数字，可正负，可保留小数，如30、-30.5！");
                }
            }
        });

        // 3、表格显示信息：采购订单子项、子项中的零件测试结果
        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(320.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);
        gridObject.setSizeFull();
        gridObject.addColumn(PurchasingOrderInfo::getPurchasingItemNo)
                .setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"))
                .setWidth(120.0);
        gridObject.addColumn(PurchasingOrderInfo::getSapInspectionLot)
                .setCaption(I18NUtility.getValue("view.materialinspection.sapinspectionlot", "SapInspectionLot"))
                .setWidth(120.0);
        gridObject.addColumn(PurchasingOrderInfo::getMaterialQuantity)
                .setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "Quantity"))
                .setWidth(80.0);
        gridObject.addSelectionListener(new SelectionListener<PurchasingOrderInfo>() {

            private static final long serialVersionUID = -454658178418921022L;

            @Override
            public void selectionChange(SelectionEvent<PurchasingOrderInfo> event) {
                glLayout.removeAllComponents();
                cbInspectionType.setSelectedItem(null);
                tfInspectionQuantity.setValue("");
                minValue = 0;
                maxValue = 0;
                if (event.getFirstSelectedItem().isPresent()) {
                    // 是否需要进行硬度检验
                    purchasingInfo = event.getFirstSelectedItem().get();
                    part = sparePartService.getByNoRev(purchasingInfo.getMaterialNo(), purchasingInfo.getMaterialRev());

                    if (part == null) {
                        setDataToDisplayArea(null);
                        NotificationUtils.notificationError("系统没有维护零件" + purchasingInfo.getMaterialNo() + "信息");
                        return;
                    } else {
                        String hardnessDoc = part.getHardnessFile();
                        if (Strings.isNullOrEmpty(hardnessDoc)) {
                            NotificationUtils.notificationError("当前零件" + purchasingInfo.getMaterialNo() + "对硬度没有检验要求");
                            return;
                        } else {
                            Hardness hardness = hardnessService.getByRuleName(part.getHardnessFile().split("/")[0]);
                            if (hardness == null) {
                                NotificationUtils
                                        .notificationError("当前零件" + purchasingInfo.getMaterialNo() + "对硬度没有检验要求");
                                return;
                            } else {
                                inspetionQuantity = purchasingInfo.getInspectionQuantity();
                                if (inspetionQuantity == 0) {
                                    NotificationUtils
                                            .notificationError("请联系管理员配置该订单需要检验的数量");
                                    return;
                                } else {
                                    minValue = hardness.getHardnessDownLimit();
                                    maxValue = hardness.getHardnessUpLimit();
                                    acceptRange = (int) minValue + "-" + (int) maxValue;
                                }

                            }
                            setDataToDisplayArea(purchasingInfo);
                        }
                    }
//					cbInspectionType.setEnabled(true);
                    purchasingOrder = purchasingInfo.getPurchasingNo();
                    SAPBatchNo = purchasingInfo.getSapInspectionLot();
                    purchaseOrderSubitem = purchasingInfo.getPurchasingItemNo();
                    inspetionQuantity = purchasingInfo.getInspectionQuantity();
                    tfInspectionQuantity.setValue(String.valueOf(inspetionQuantity));
                } else {
                    purchasingOrder = "";
                    part = null;
                    purchasingInfo = null;
                    materialQuantity = 0;
                    SAPBatchNo = "";
                    purchaseOrderSubitem = "";
                    cbInspectionType.setEnabled(false);
                    setDataToDisplayArea(null);
                }
            }
        });
        hlSplitPanel.setFirstComponent((Component) gridObject);

        inspectionValue.setSizeFull();
        inspectionValue.setContent(glLayout);

        hlSplitPanel.setSecondComponent(inspectionValue);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    // 匹配浮点数 包括正负
    public static boolean isIsTemp(String str) {
        String regex = "^(-?\\d+)(\\.\\d+)?$";
        return str.matches(regex);// match(regex, str)
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        x = 0;
        y = 1;
        if (btnSkip.equals(button)) {
            ConfirmDialog.show(getUI(), "确定要跳过硬度检验吗？", new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    NotificationUtils.notificationInfo("硬度检验已跳过");
                    purchasingInfo.setHardnessChecked(true);
                    purchasingInfo.setHardnessCheckedRlt("NA");
                    purchasingOrderService.save(purchasingInfo);
                    rejectedQuantity = 0;
                    pass = true;
                    // 刷新数据，将已经检验的数据去掉
                    refresh();
                }
            });
        } else if (btnStart.equals(button)) {
            // 开始语音检测
            ipAddress = RequestInfo.current().getUserIpAddress();
            // 获取Socket连接
            try {
                client = new SocketClient(ipAddress, AppConstant.PORT);
                btnStart.setEnabled(false);
                client.setReceiveListener(message -> {
                    // 定义 OnMessage 后执行事件。
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
                        // 判断返回结果是否复核标准范围
                        // 获取检验项名称
                        // message去除信息头
                        String messageBody = message.split("]")[1];
                        if (ensureMessage) {
                            if (!message.split("\\|")[1].equals("1")) {
                                ifSave.append(messageBody.split("\\|")[0]);
                            }
                        } else {
                            if (!message.split("\\|")[1].equals("1")) {
                                returnMessage.append(messageBody.split("\\|")[0]);
                            }
                        }
                    } else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {
                        // 没有识别当前检测项的值，任然播报当前检测项的信息
                        if (returnMessage.length() == 0) {
                            // 没有识别到语音，播报当前检测内容
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (x == 0) {
                                    // 第一列，播报输入序列号语音
                                    client.sendMessage("[PT]请输入检测的第" + y + "个SN序列号");
                                } else {
                                    client.sendMessage("[PT]请输入" + columnsList.get(x) + "的值");
                                }
                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }

                        } else {
                            if (x == 3) {
                                // 第四列，硬度值，只能是数字
                                if (RegExpValidatorUtils.isIsNumber(returnMessage.toString())) {
                                    // 是数字的情况判断是否在标准范围内
                                    int hardnessValue = Integer.parseInt(returnMessage.toString());

                                    if (hardnessValue >= minValue && hardnessValue <= maxValue) {
                                        // 识别到语音，将值写到页面
                                        getUI().accessSynchronously(new Runnable() {
                                            @Override
                                            public void run() {
                                                TextField component = (TextField) glLayout.getComponent(x, y);
                                                component.setValue(returnMessage.toString());
                                                returnMessage.delete(0, returnMessage.length());
                                                TextField componentNext = (TextField) glLayout.getComponent(x + 1, y);
                                                componentNext.setValue("OK");
                                                acceptQuantity++;
                                                // 保存数据
                                                saveData(y);
                                            }
                                        });
                                        // 播报下一个需要输入的检验内容
                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                        try {
                                            if (y < glLayout.getRows() - 1) {
                                                x = 0;
                                                y++;
                                                client.sendMessage("[PT]请输入检测的第" + y + "个SN序列号");
                                            } else {
                                                prefixSend = AppConstant.INSPECTIONDONE;
                                                client.sendMessage("[PT]检验完成");
                                            }
                                        } catch (Exception e) {
                                            NotificationUtils.notificationError("Socket连接中断");
                                        }
                                    } else {
                                        if (ensureMessage) {
                                            if (ifSave != null && ifSave.length() > 0
                                                    && "是".equals(ifSave.toString())) {
                                                getUI().accessSynchronously(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        TextField component = (TextField) glLayout.getComponent(x, y);
                                                        component.setValue(returnMessage.toString());
                                                        returnMessage.delete(0, returnMessage.length());
                                                        TextField componentNext = (TextField) glLayout
                                                                .getComponent(x + 1, y);
                                                        componentNext.setValue("NG");
                                                        ifSave.delete(0, ifSave.length());
                                                        pass = false;
                                                        ensureMessage = false;
                                                        rejectedQuantity++;
                                                        saveData(y);
                                                    }
                                                });
                                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                try {
                                                    if (y < glLayout.getRows() - 1) {
                                                        x = 0;
                                                        y++;
                                                        client.sendMessage("[PT]请输入检测的第" + y + "个SN序列号");
                                                    } else {
                                                        prefixSend = AppConstant.INSPECTIONDONE;
                                                        client.sendMessage("[PT]检验完成");
                                                    }
                                                } catch (Exception e) {
                                                    NotificationUtils.notificationError("Socket连接中断");
                                                }
                                            } else {
                                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                try {
                                                    client.sendMessage("[PT]重新输入检验项检测的硬度值的值");
                                                    returnMessage.delete(0, returnMessage.length());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                returnMessage.delete(0, returnMessage.length());
                                                ifSave.delete(0, ifSave.length());
                                                ensureMessage = false;
                                            }
                                        } else {
                                            // 值不在范围内
                                            ensureMessage = true;
                                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                                            try {
                                                client.sendMessage("[PT]输入的值不在标准范围，是否要录入");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                    try {
                                        client.sendMessage("[PT]录入不是数字请重新录入检测的硬度值");
                                        returnMessage.delete(0, returnMessage.length());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (x == 0) {
                                    String SN = returnMessage.toString();
                                    String serialNo = purchasingInfo.getPurchasingNo() + "-" + formatString(purchasingInfo.getPurchasingItemNo(), 3) + "-" + formatString(SN, 3);
                                    UniqueTraceability instance = traceabilityService.getByItem(serialNo);
                                    System.out.println(serialNo);
                                    if (instance == null) {
                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                        try {
                                            returnMessage.delete(0, returnMessage.length());
                                            client.sendMessage("[PT]当前输入的序列号" + SN + "没有找到供应商信息,请重新输入检测的第" + y + "个SN序列号");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        getUI().accessSynchronously(new Runnable() {
                                            @Override
                                            public void run() {
                                                TextField component = (TextField) glLayout.getComponent(x, y);
                                                component.setValue(returnMessage.toString());
                                                returnMessage.delete(0, returnMessage.length());
                                                ((TextField) glLayout.getComponent(x + 1, y)).setValue(instance.getHeatNo());
                                                ((TextField) glLayout.getComponent(x + 2, y))
                                                        .setValue(instance.getHlLotNo());
                                                //保存信息

                                            }
                                        });
                                        // 播报下一个需要输入的检验内容
                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                        try {
                                            x = 3;
                                            client.sendMessage("[PT]请输入实际的硬度测量值");
                                        } catch (Exception e) {
                                            NotificationUtils.notificationError("Socket连接中断");
                                        }
                                    }

                                }
                            }

                        }
                    } else if (message.startsWith(AppConstant.PALYEXCEPTION)) {
                        System.out.println("语音服务网络断开，正尝试重新连接");
                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                        try {
                            if (x == 0) {
                                // 第一列，播报输入序列号语音
                                client.sendMessage("[PT]请输入检测的第" + y + "个SN序列号");
                            } else {
                                client.sendMessage("[PT]请输入" + columnsList.get(x) + "的值");
                            }
                        } catch (Exception e) {
                            NotificationUtils.notificationError("Socket连接中断");
                        }
                    }
                });
                prefixSend = AppConstant.PREFIXPLAYTEXT;
                client.sendMessage("[PT]请输入需要检测的第一个SN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 处理生成一个word格式的硬度测试报告
            con = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (con == null) {
                NotificationUtils.notificationError("请先配置报告存放的根目录!");
                return;
            }
            if (Strings.isNullOrEmpty(tfTemp.getValue().trim())) {
                NotificationUtils.notificationError("请输入温度值");
                return;
            }
            if (Strings.isNullOrEmpty(tfSerialNo.getValue().trim())) {
                NotificationUtils.notificationError("请输入序列号值");
                return;
            }
            Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
            if (mediaImage == null) {
                NotificationUtils.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
                return;
            }
            String rootPath = con.getConfigValue();
            if (!saveAll()) {
                return;
            }

            createReport(rootPath);
            // 重新报讯页面数据，避免工人在语音输入之后需要更改保存数据
            // 更新该采购订单的检验信息
            NotificationUtils.notificationInfo("采购单:" + purchasingInfo.getPurchasingNo() + "-" + purchasingInfo.getPurchasingItemNo() + "硬度检验完成");
            purchasingInfo.setHardnessChecked(true);
            purchasingInfo.setHardnessCheckedRlt(pass ? "OK" : "NG");
            purchasingOrderService.save(purchasingInfo);
            rejectedQuantity = 0;
            pass = true;
            // 刷新数据，将已经检验的数据去掉
            refresh();
            //关闭socket
            try {
                if (client != null) {
                    client.close();
                    btnStart.setEnabled(true);
                }
            } catch (Exception e) {
            }
        }

    }

    public void setDataToDisplayArea(PurchasingOrderInfo purchasingOrderInfo) {
        if (purchasingOrderInfo == null) {
            // 清空显示区的信息
            for (LabelWithSamleLineCaption label : labelComponents) {
                label.clear();
            }
            tfSerialNo.clear();
            tfMaterialMS.clear();
            tfTemp.clear();
        } else {
            // 填充信息
            materialNo = purchasingOrderInfo.getMaterialNo();
            materialRev = purchasingOrderInfo.getMaterialRev();
            materialQuantity = purchasingOrderInfo.getMaterialQuantity();
            String materialDesc = purchasingOrderInfo.getMaterialDesc();
            String quality = "";

            String serialNo = "";
            String materialMS = part.getHardnessFile() + " REV" + part.getHardnessRev();
            if (!Strings.isNullOrEmpty(part.getQaPlan())) {
                quality = part.getQaPlan() + " / " + part.getQaPlanRev();
            }
            tfSerialNo.setReadOnly(false);
            tfMaterialMS.setReadOnly(false);
            tfTemp.setReadOnly(false);

            lblPartNoRev.setValue(materialNo + " / " + materialRev);
            lblPartDesc.setValue(materialDesc);
            lblQuantity.setValue(String.valueOf(materialQuantity));
            lblQPRev.setValue(quality);
            lblProductOrder.setValue("///");
//			lblMeasuringType.setValue(AppConstant.MEASURING_TYPE);
//			lblTestSpecification.setValue(AppConstant.HARDNESS_TEST_SPECIFICATION);
            lblAcceptableRange.setValue(acceptRange);

            tfSerialNo.setValue(serialNo);
            tfMaterialMS.setValue(materialMS == null ? "" : materialMS);

        }
    }

    public boolean valueIsTemp(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            if (isIsTemp(value)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<String> getColumns() {
        List<String> list = new ArrayList<>();
        list.add("序列号");
        list.add("熔炼炉号");
        list.add("热处理炉号");
        list.add("实际测量值");
        list.add("检验结果");
        return list;
    }

    public void createReport(String path) {
        path = path + AppConstant.MATERIAL_PREFIX + AppConstant.HARDNESS_INSPECTION_REPORT;
        try {
            rejectedQuantity = 0;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            int count = Integer.parseInt(tfInspectionQuantity.getValue());
            int pageNum = 1;
            if (count > 21) {
                int temp = count - 21;
                if (temp > 33)
                    pageNum = pageNum + temp % 38 + 1;
            }
            dataMap.put("pageNum", pageNum);
            dataMap.put("partNo", purchasingInfo.getMaterialNo() + " / " + purchasingInfo.getMaterialRev());
            dataMap.put("partDesc", purchasingInfo.getMaterialDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
            dataMap.put("serialNos", tfSerialNo.getValue().trim());
            String strQty = String.valueOf(purchasingInfo.getMaterialQuantity());
            dataMap.put("qty", String.format("%1$-4s", strQty));
            dataMap.put("purchasingOrder",
                    purchasingInfo.getPurchasingNo() + "-" + purchasingInfo.getPurchasingItemNo());
            dataMap.put("qpRev", lblQPRev.getValue());
            dataMap.put("productionOrder", "\\\\");
            dataMap.put("testSpecification", cbTestSpecification.getValue() == null ? "" : cbTestSpecification.getValue());
            dataMap.put("measuringType", cbMeasuringType.getValue() == null ? "" : cbMeasuringType.getValue());
            dataMap.put("temp", tfTemp.getValue());

            dataMap.put("acceptRange", lblAcceptableRange.getValue());
            dataMap.put("materialMS", tfMaterialMS.getValue());

//			dataMap.put("inspector", RequestInfo.current().getUserName());
            dataMap.put("Date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

            //

            List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
            for (int rowIndex = 1; rowIndex < glLayout.getRows(); rowIndex++) {
                Map<String, String> sMap = new HashMap<>();
                TextField tfSNValue = (TextField) glLayout.getComponent(0, rowIndex);
                TextField tfHeatValue = (TextField) glLayout.getComponent(1, rowIndex);
                TextField tfHtNoValue = (TextField) glLayout.getComponent(2, rowIndex);
                TextField tfHardnessValue = (TextField) glLayout.getComponent(3, rowIndex);
                TextField tfSComment = (TextField) glLayout.getComponent(4, rowIndex);
                sMap.put("serialNo", tfSNValue.getValue() == null ? "" : tfSNValue.getValue());
                sMap.put("heatNo", tfHeatValue.getValue() == null ? "" : tfHeatValue.getValue());
                sMap.put("htLotNo", tfHtNoValue.getValue() == null ? "" : tfHtNoValue.getValue());
                sMap.put("value", tfHardnessValue.getValue() == null ? "" : tfHardnessValue.getValue());
                float actualValue = Float.parseFloat(tfHardnessValue.getValue());
                if (actualValue >= minValue && actualValue <= maxValue) {
                    sMap.put("comment", "OK");
                } else {
                    sMap.put("comment", "NG");
                    rejectedQuantity++;
                }
                sList.add(sMap);

            }
            dataMap.put("slist", sList);
            dataMap.put("accQty", String.format("%1$-5s", String.valueOf(materialQuantity - rejectedQuantity)));
            dataMap.put("rejQty", rejectedQuantity);
            BASE64Encoder encoder = new BASE64Encoder();
            Media mediaImage = caMediaService.getByTypeName("ES", RequestInfo.current().getUserName());
            if (mediaImage == null) {
                NotificationUtils.notificationError("当前用户:" + RequestInfo.current().getUserName() + "没有配置电子签名,请首先配置电子签名");
                return;
            }
            dataMap.put("signature", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            String jPath = AppConstant.DOC_XML_FILE_PATH;
            configuration.setDirectoryForTemplateLoading(new File(jPath));
            // 以utf-8的编码读取模板文件
            Template template = configuration.getTemplate("hardness.xml", "utf-8");

            // 输出文件
            String fileName = path + purchasingInfo.getPurchasingNo() + "-" + purchasingInfo.getPurchasingItemNo() + "-" + purchasingInfo.getSapInspectionLot() + ".doc";
            File outFile = new File(fileName);

            // 将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")), 1024 * 1024);
            template.process(dataMap, out);
            out.flush();
            out.close();
            wordToPDF(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData(int rowNum) {
        TextField tfSN = (TextField) glLayout.getComponent(0, rowNum);
        TextField tfHeat = (TextField) glLayout.getComponent(1, rowNum);
        TextField tfHtLot = (TextField) glLayout.getComponent(2, rowNum);
        TextField tfValue = (TextField) glLayout.getComponent(3, rowNum);
        TextField tfComment = (TextField) glLayout.getComponent(4, rowNum);
        HardnessTestReportItems instance = new HardnessTestReportItems();
        String serialNo = purchasingInfo.getPurchasingNo() + "-" + purchasingInfo.getPurchasingItemNo() + "-" + tfSN.getValue();
        instance.setSerialNo(serialNo);
        instance.setHeatNo(tfHeat.getValue());
        instance.setHTLotNo(tfHtLot.getValue());
        instance.setPartNo(purchasingInfo.getMaterialNo());
        instance.setPartNoRev(purchasingInfo.getMaterialRev());
        instance.setActualHardnessValue(Float.parseFloat(tfValue.getValue()));
        instance.setResult(tfComment.getValue());
        instance.setPurchaseOrder(purchasingInfo.getPurchasingNo());
        instance.setPurchaseOrderSubitem(purchaseOrderSubitem);
        instance.setSAPBatchNo(SAPBatchNo);
        hardnessTestReportItemsService.save(instance);
    }

    public boolean saveAll() {
        rejectedQuantity = 0;
        deleteRecords(purchasingInfo.getSapInspectionLot());
        List<HardnessTestReportItems> list = new ArrayList<>();
        for (int rowIndex = 1; rowIndex < glLayout.getRows(); rowIndex++) {
            HardnessTestReportItems instance = new HardnessTestReportItems();
            TextField tfSN = (TextField) glLayout.getComponent(0, rowIndex);
            TextField tfHeat = (TextField) glLayout.getComponent(1, rowIndex);
            TextField tfHtLot = (TextField) glLayout.getComponent(2, rowIndex);
            TextField tfValue = (TextField) glLayout.getComponent(3, rowIndex);
            TextField tfResult = (TextField) glLayout.getComponent(4, rowIndex);
            if (Strings.isNullOrEmpty(tfSN.getValue()) || Strings.isNullOrEmpty(tfValue.getValue())) {
                NotificationUtils.notificationError("检验项中SN或者硬度值不能为空");
                return false;
            }
            float value = Float.parseFloat(tfValue.getValue());
            if (value >= minValue && value <= maxValue) {
                instance.setResult("OK");
            } else {
                pass = false;
                instance.setResult("NG");
                rejectedQuantity++;
            }
            String serialNo = purchasingInfo.getPurchasingNo() + "-" + purchasingInfo.getPurchasingItemNo() + "-" + tfSN.getValue();
            instance.setSerialNo(serialNo);
            instance.setHeatNo(tfHeat.getValue());
            instance.setHTLotNo(tfHtLot.getValue());
            instance.setPartNo(purchasingInfo.getMaterialNo());
            instance.setPartNoRev(purchasingInfo.getMaterialRev());
            if (!RegExpValidatorUtils.isIsNumber(tfValue.getValue())) {
                NotificationUtils.notificationError("检验的硬度值必须是数字型");
                return false;
            }
            instance.setActualHardnessValue(Float.parseFloat(tfValue.getValue()));
            instance.setPurchaseOrder(purchasingInfo.getPurchasingNo());
            instance.setPurchaseOrderSubitem(purchaseOrderSubitem);
            instance.setSAPBatchNo(SAPBatchNo);
            list.add(instance);
        }
        hardnessTestReportItemsService.saveAll(list);
        //
        HardnessTestReport testreport = new HardnessTestReport();
        testreport.setPurchaseOrder(purchasingOrder);
        testreport.setSAPBatchNo(SAPBatchNo);
        testreport.setPurchaseOrderSubitem(purchaseOrderSubitem);
        testreport.setSerialNumbers(tfSerialNo.getValue().trim());
        testreport.setTemp(tfTemp.getValue().trim());
        testreport.setMaterialMS(tfMaterialMS.getValue());
        testreport.setPartQuantity(materialQuantity);
        testreport.setQuantityAccepted(materialQuantity - rejectedQuantity);
        testreport.setQuantityRejected(rejectedQuantity);
        testreport.setTestedFinished(true);
        hardnessTestReportService.save(testreport);
        return true;
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

    public void deleteRecords(String sapLotNo) {
        hardnessTestReportItemsService.deleteRecords(sapLotNo);
        hardnessTestReportService.deleteBysapLot(sapLotNo);
    }

    public void refresh() {
        String purchasingNo = cbPurchaseOrder.getValue().trim();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService.getUncheckedOrder(purchasingNo,
                    "HARDNESS");
            cbMeasuringType.setSelectedItem(null);
            cbTestSpecification.setSelectedItem(null);
            if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                // 数据填入grid
                gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
                // 初始化 itemgrid
                setDataToDisplayArea(null);
            } else {
                gridObject.setDataProvider(DataProvider.ofCollection(new ArrayList<PurchasingOrderInfo>()));
                NotificationUtils
                        .notificationInfo(I18NUtility.getValue("view.hardnessinspection.purchasingnotexist",
                                "Purchasing No Not Exist or This Order Has Finished Hardness Inspection."));
            }
        }
    }

    public List<String> getPurchasingOrder(String type) {
        return purchasingOrderService.getPurchasingNo(type);
    }

    @Override
    public void _init() {
        cbPurchaseOrder.setItems(getPurchasingOrder("HARDNESS"));
    }

    public String formatString(String str, int length) {
        //这里str默认传入两位字符
        if (str.length() < length) {
            str = "0".concat(str);
        }
        return str;
    }

    public void wordToPDF(String filePath) {
        ComThread.InitSTA();
        ActiveXComponent app = null;
        Dispatch doc = null;
        //转换前的文件路径
        String startFile = filePath;// "D:\\TestWord\\test" + ".docx";
        //转换后的文件路劲
        String overFile = "";
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            overFile = startFile.replace(".doc", ".pdf");

            doc = Dispatch.call(docs, "Open", startFile).toDispatch();
            File tofile = new File(overFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", overFile, 17);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        //结束后关闭进程
        System.out.println(LocalDateTime.now());
        ComThread.Release();

        //加入受控章
        try {
            PdfReader readerOriginalDoc = new PdfReader(overFile);
            File fromFile = new File(overFile);
            PdfWriter writeDest = new PdfWriter(overFile.replace(".pdf", "-2.pdf"));
            File toFile = new File(overFile.replace(".pdf", "-2.pdf"));
            PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
            com.itextpdf.layout.Document document2 = new com.itextpdf.layout.Document(newDoc);
            ImageData imageData = ImageDataFactory.create("D:\\CameronQualityFiles\\DOCS\\CONTROLLED.png");
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
            image.scaleToFit(100, 100);
            image.setRotationAngle(0.5);
            image.setFixedPosition(1, 470, 180);
            document2.add(image);
            document2.close();
            fromFile.delete();
            toFile.renameTo(fromFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
