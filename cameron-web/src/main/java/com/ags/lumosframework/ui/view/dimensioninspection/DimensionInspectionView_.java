//Changed by Cameron: 报告直接用iTextPdf在pdf模板中用AcroForm生成，改善保存和生成报告的速度

package com.ags.lumosframework.ui.view.dimensioninspection;

import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult.Result;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.DataProvider;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Menu(caption = "DimensionInspection", captionI18NKey = "view.dimensioninspection.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 3)
@SpringView(name = "DimensionInspection", ui = CameronUI.class)
public class DimensionInspectionView_ extends BaseView implements Button.ClickListener {


    private static final long serialVersionUID = -5881921658681194159L;
    private final ComboBox<String> cbPurchasingNo = new ComboBox<String>();
    @I18Support(caption = "Confirm", captionKey = "common.confirm")
    private final Button btnConfirm = new Button();
    @I18Support(caption = "Start", captionKey = "common.start")
    private final Button btnStart = new Button();
    @I18Support(caption = "Skip", captionKey = "common.skip")
    private final Button btnSkip = new Button();
    private final Button btnGetData = new Button();
    private final Button send = new Button();
    private final Button btnCopy = new Button();
    @I18Support(caption = "MaterialNo", captionKey = "view.dimensioninspection.materialno")
    private final LabelWithSamleLineCaption lblMaterialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "MaterialRev", captionKey = "view.dimensioninspection.materialrev")
    private final LabelWithSamleLineCaption lblMaterialRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "Vendor", captionKey = "view.dimensioninspection.vendor")
    private final LabelWithSamleLineCaption lblVendor = new LabelWithSamleLineCaption();
    @I18Support(caption = "MaterialDesc", captionKey = "view.dimensioninspection.materialdesc")
    private final LabelWithSamleLineCaption lblMaterialDesc = new LabelWithSamleLineCaption();
    @I18Support(caption = "QualityPlan", captionKey = "view.dimensioninspection.qualityplan")
    private final LabelWithSamleLineCaption lblQualityPlan = new LabelWithSamleLineCaption();
    @I18Support(caption = "DrawingNo", captionKey = "view.dimensioninspection.drawingno")
    private final LabelWithSamleLineCaption lblDrawingNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "SerialNo", captionKey = "view.dimensioninspection.serialno")
    private final TextField tfSerialNos = new TextField();
    private final TextField tfInspectionQuantity = new TextField();
    private final Grid<PurchasingOrderInfo> gridObject = new Grid<>();
    private final ComboBox<String> cbInspectionType = new ComboBox<>();// 检验类型 单件，分组，无SN
    private final GridLayout gridLayout = new GridLayout();
    private final AbstractComponent[] components = new AbstractComponent[]{cbPurchasingNo, tfInspectionQuantity,
            cbInspectionType, btnGetData, btnStart, btnConfirm, btnSkip};
    private final String inspectionItemName = "";// 检验项的名称
    VerticalLayout hlToolBox = new VerticalLayout();
    Panel inspectionValue = new Panel();
    VerticalLayout vlDisplay = new VerticalLayout();
    HorizontalLayout hlTemp1 = new HorizontalLayout();
    HorizontalLayout hlTemp2 = new HorizontalLayout();
    HorizontalLayout hlTemp3 = new HorizontalLayout();
    List<DimensionRuler> listInstance = null;
    @Autowired
    IDimensionInspectionService dimensionInspectionService;
    @Autowired
    IDimensionRulerService dimensionRulerService;
    @Autowired
    ICaConfigService caConfigService;
    StringBuilder returnMessage = new StringBuilder();//当信息为普通提示信息时使用
    SocketClient client = null;
    PurchasingOrderInfo purchasingOrderInfo = null;
    CaConfig con = null;
    StringBuilder ifSave = new StringBuilder();//当信息为确认信息时使用
    boolean isSelectItemMessage = false;
    StringBuilder itemRowNum = new StringBuilder();
    boolean isFirst = true;
    @Autowired
    private IPurchasingOrderService purchasingOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private ICaMediaService caMediaService;
    // 选中的checkBox序号
    private String ipAddress = "";
    private String prefixSend = "";// 当前发送的语音指令，用于判断返回信息并执行什么操作
    private String materialNo = "";
    private String materialRev = "";
    private int inspectionQuantitySetted = 0;
    // 抽检数量
    private int inspectionQuantity = 0;
    private boolean ensureMessage = false;//表示当前播放语音信息是确认信息还是普通的提示信息
    // 表示当前检验的采购单是否通过检验
    private boolean pass = true;
    private int x = 0;
    private int y = 0;

    public DimensionInspectionView_() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        for (Component component : components) {
            hlTempToolBox.addComponent(component);
        }
        btnStart.setIcon(VaadinIcons.START_COG);
        btnStart.addClickListener(this);
        btnStart.setEnabled(false);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);
        btnConfirm.setEnabled(false);
        btnSkip.setIcon(VaadinIcons.FAST_FORWARD);
        btnSkip.addClickListener(this);
        btnSkip.setEnabled(true);
        send.addClickListener(this);
        btnGetData.addClickListener(this);
        btnGetData.setIcon(VaadinIcons.GAVEL);
        btnCopy.setCaption("整行复制");
        btnCopy.addClickListener(this);
        tfInspectionQuantity.setPlaceholder("检验数量");
        tfInspectionQuantity.setEnabled(false);
        tfInspectionQuantity.addValueChangeListener(new ValueChangeListener<String>() {

            private static final long serialVersionUID = -547168792572880770L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                String inputValue = event.getValue();
                displayGrid(inputValue);
            }
        });
        cbInspectionType
                .setPlaceholder(I18NUtility.getValue("view.dimensioninspection.inspectiontype", "InspectionType"));
        cbInspectionType.setItems("单件", "分组");// , "无SN"
        cbInspectionType.setEnabled(false);// 单件:每一个件需要检验完所有的检验项才开始下一个件的检验;分组:检验完一组中所有的SN的一个检验项才会检查下一个检验项;无SN:只有结果，没有SN
        cbInspectionType.addValueChangeListener(new ValueChangeListener<String>() {
            private static final long serialVersionUID = 8850451531559355360L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {

                if (Strings.isNullOrEmpty(event.getValue())) {
                    btnConfirm.setEnabled(false);
                    btnStart.setEnabled(false);
                } else {
                    btnConfirm.setEnabled(true);
                    btnStart.setEnabled(true);
                }

            }
        });
        cbPurchasingNo.setPlaceholder("采购单号");

        cbPurchasingNo.addValueChangeListener(new ValueChangeListener<String>() {

            private static final long serialVersionUID = -6254353796216761054L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                if (!event.getValue().equals(event.getOldValue())) {
                    String purchasingNo = cbPurchasingNo.getValue().trim();
                    if (!Strings.isNullOrEmpty(purchasingNo)) {
                        List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
                                .getUncheckedOrder(purchasingNo, "DIMENSION");
                        if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                            // 数据填入grid
                            gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
                        } else {
                            NotificationUtils.notificationError(I18NUtility.getValue(
                                    "view.dimensioninspection.purchasingnotexist",
                                    "Purchasing No Not Exist or This Order Has Finished Dimension Inspection."));
                        }
                    } else {
                        NotificationUtils.notificationError(I18NUtility
                                .getValue("view.dimensioninspection.purchasingnotnull", "PurchasingNo can't be null"));
                    }
                }
            }
        });
        vlRoot.addComponent(vlDisplay);
        vlDisplay.setWidth("100%");
        vlDisplay.setMargin(true);
        vlDisplay.addComponent(hlTemp1);
        vlDisplay.addComponent(hlTemp2);
        vlDisplay.addComponent(hlTemp3);
        hlTemp1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp2.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp3.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp1.setWidth("100%");
        hlTemp2.setWidth("100%");
        hlTemp1.addComponent(lblMaterialNo);
        hlTemp1.addComponent(lblMaterialRev);
        hlTemp1.addComponent(tfSerialNos);
        hlTemp2.addComponent(lblVendor);
        hlTemp2.addComponent(lblQualityPlan);
        hlTemp2.addComponent(lblDrawingNo);
        hlTemp3.addComponent(lblMaterialDesc);
        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);
        gridObject.setSizeFull();
        gridObject.addColumn(PurchasingOrderInfo::getPurchasingItemNo)
                .setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"))
                .setWidth(100.0);
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
                isFirst = true;
                if (event.getFirstSelectedItem().isPresent()) {
                    purchasingOrderInfo = event.getFirstSelectedItem().get();
                    materialNo = purchasingOrderInfo.getMaterialNo();
                    materialRev = purchasingOrderInfo.getMaterialRev();
                    inspectionQuantitySetted = purchasingOrderInfo.getInspectionQuantity();
                    tfInspectionQuantity.setValue("");
                    // 判断该订单是否需要进行尺寸检验
                    setDataToDisplayArea(purchasingOrderInfo);
                    List<DimensionRuler> list = dimensionRulerService.getByNoRev(materialNo, materialRev);
                    if (list == null || list.size() == 0) {
                        list = dimensionRulerService.getByNoRev(materialNo, null);
                        if (list.size() > 0) {
                            String lastRev = list.get(list.size() - 1).getMaterialRev();
                            list = dimensionRulerService.getByNoRev(materialNo, lastRev);
                            ConfirmDialog.show(getUI(), "当前版本" + materialRev + "没有维护尺寸模板，是否复制版本" + lastRev + "的尺寸模板",
                                    result -> {
                                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                            final List<DimensionRuler> list2 = dimensionRulerService.getByNoRev(materialNo, lastRev);
                                            for (DimensionRuler dimensionRuler : list2) {
                                                DimensionRuler dimensionRuler1 = new DimensionRuler();
                                                dimensionRuler1.setMaterialRev(materialRev);
                                                dimensionRuler1.setMaterialNo(dimensionRuler.getMaterialNo());
                                                dimensionRuler1.setInspectionItemName(dimensionRuler.getInspectionItemName());
                                                dimensionRuler1.setInspectionItemType(dimensionRuler.getInspectionItemType());
                                                dimensionRuler1.setMaxValue(dimensionRuler.getMaxValue());
                                                dimensionRuler1.setMinValue(dimensionRuler.getMinValue());
                                                dimensionRulerService.save(dimensionRuler1);
                                            }
                                            tfInspectionQuantity.setValue(String.valueOf(purchasingOrderInfo.getInspectionQuantity()));
                                            cbInspectionType.setEnabled(true);
                                            cbInspectionType.setSelectedItem(null);
                                        }
                                    });
                            if (list == null || list.size() == 0) {
                                return;
                            }
                        } else {
                            NotificationUtils.notificationError("当前零件没有维护尺寸检验项或者不需要尺寸检验，请确认");
                            gridLayout.removeAllComponents();
                            return;
                        }
                    } else {
                        if (inspectionQuantitySetted == 0) {
                            NotificationUtils.notificationError("请联系管理员配置该订单需要检验的数量");
                            gridLayout.removeAllComponents();
                            return;
                        }
                        tfInspectionQuantity.setValue(String.valueOf(purchasingOrderInfo.getInspectionQuantity()));
                        cbInspectionType.setEnabled(true);
                        cbInspectionType.setSelectedItem(null);
                    }
                } else {
                    setDataToDisplayArea(null);
                    purchasingOrderInfo = null;
                    materialNo = "";
                    materialRev = "";
                    cbInspectionType.setEnabled(false);
                    cbInspectionType.setSelectedItem(null);
                    gridLayout.removeAllComponents();
                }
            }
        });
        hlSplitPanel.setFirstComponent(gridObject);
        inspectionValue.setSizeFull();
        inspectionValue.setContent(gridLayout);
        hlSplitPanel.setSecondComponent(inspectionValue);
        this.

                setSizeFull();
        this.

                setCompositionRoot(vlRoot);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        if (btnConfirm.equals(button)) {
            con = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (con == null) {
                NotificationUtils.notificationError("请先配置报告存放的根目录!");
                return;
            }
            Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
            if (mediaImage == null) {
                NotificationUtils
                        .notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
                return;
            }
            if (Strings.isNullOrEmpty(tfSerialNos.getValue())) {
                NotificationUtils.notificationError("请输入检验的序列号!");
                return;
            }
            String rootPath = con.getConfigValue();
            ConfirmDialog.show(getUI(), "确定要保存当前检验信息吗", new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if (result.getResult().equals(Result.OK)) {
                        int saveItems = 0;
                        // 保存数据
                        String dimensionRlt = purchasingOrderInfo.getDimensionCheckedRlt();
                        if ("单件".equals(cbInspectionType.getValue())) {
                            saveItems = saveDataSingle();
                            purchasingOrderInfo.setCheckedSn(checkedSn());
                            purchasingOrderInfo.setDescription("单件");
                        } else {
                            saveItems = saveDataGroup();
                            purchasingOrderInfo.setCheckedSn(checkedSn());
                            purchasingOrderInfo.setDescription("分组");
                            isSelectItemMessage = false;
                        }
                        if (Strings.isNullOrEmpty(dimensionRlt)) {
                            purchasingOrderInfo.setDimensionCheckedRlt(pass ? "OK" : "NG");
                        } else {
                            if ("OK".equals(dimensionRlt) && pass) {
                                purchasingOrderInfo.setDimensionCheckedRlt("OK");
                            } else {
                                purchasingOrderInfo.setDimensionCheckedRlt("NG");
                            }
                        }
                        //是否检验已经完成，如果已经完成，需要更改采购单的信息以及生成检验报告
                        if (inspectionDone()) {
                            purchasingOrderInfo.setDimensionChecked(true);
//                            createReport(rootPath);
                            createReportPDF(rootPath);
                        }
                        purchasingOrderService.save(purchasingOrderInfo);

                        //将变量初始化
                        gridLayout.removeAllComponents();
                        gridObject.setDataProvider(DataProvider.ofCollection(purchasingOrderService
                                .getUncheckedOrder(cbPurchasingNo.getValue().trim(), "DIMENSION")));
                        pass = true;
                        isFirst = true;
                        tfSerialNos.clear();
                        NotificationUtils.notificationInfo("保存(更新)信息:" + saveItems + "条");
                    }
                }
            });
            try {
                if (client != null) {
                    client.close();
                    btnStart.setEnabled(true);
                }
            } catch (Exception e) {
            }
            /////////////////////////////////////////////////////////////////////////////测试用代码
//            con = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
//            String rootPath = con.getConfigValue();
//            createReportPDF(rootPath);
            /////////////////////////////////////////////////////////////////////////////测试用代码
        } else if (btnGetData.equals(button)) {
            String inspectionType = purchasingOrderInfo.getDescription();
            if (!Strings.isNullOrEmpty(inspectionType)) {
                cbInspectionType.setSelectedItem(inspectionType);
                String sapLot = purchasingOrderInfo.getSapInspectionLot();
                String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-"
                        + purchasingOrderInfo.getPurchasingItemNo();
                String snChecked = purchasingOrderInfo.getCheckedSn();
                String[] arraySn = snChecked.split(",");
                if (arraySn.length > 0) {
                    isFirst = false;
                }
                for (int k = 0; k < arraySn.length; k++) {
                    ((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
                }
                if ("分组".equals(inspectionType)) {
                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
                        for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
                            // 获取该零件SN对应的行的检验项的检验值
                            String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
                            String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
                                    snPrefix + "-" + arraySn[colmnIndex]);
                            if (!Strings.isNullOrEmpty(value)) {
                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
                                isFirst = false;
                            } else {
                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
                                break;
                            }
                        }
                    }
                    // 最后加载量具信息
                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
                        String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
                        String gageNo = dimensionInspectionService.getGageNo(sapLot, inspectionName);
                        ((TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex)).setValue(gageNo);
                    }
                } else {
                    for (int k = 0; k < arraySn.length; k++) {
                        ((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
                    }
                    for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
                        for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
                            String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
                            String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
                                    snPrefix + "-" + arraySn[colmnIndex]);
                            if (!Strings.isNullOrEmpty(value)) {
                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
                                isFirst = false;
                            } else {
                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
                            }
                        }
                    }
                    // 最后加载量具信息
                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
                        String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
                        String gageNo = dimensionInspectionService.getGageNo(sapLot, inspectionName);
                        ((TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex)).setValue(gageNo);
                    }
                }
            }
        } else if (btnSkip.equals(button)) {
            ConfirmDialog.show(getUI(), "确定要跳过尺寸检验吗？", new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if (result.getResult().equals(Result.OK)) {
                        purchasingOrderInfo.setDimensionCheckedRlt("NA");
                        purchasingOrderInfo.setDimensionChecked(true);
                    }
                    purchasingOrderService.save(purchasingOrderInfo);

                    //将变量初始化
                    gridLayout.removeAllComponents();
                    gridObject.setDataProvider(DataProvider.ofCollection(purchasingOrderService
                            .getUncheckedOrder(cbPurchasingNo.getValue().trim(), "DIMENSION")));
                    pass = true;
                    isFirst = true;
                    tfSerialNos.clear();
                    NotificationUtils.notificationInfo("尺寸检验已跳过");
                }
            });
        } else if (btnCopy.equals(button)) {
            for (int j = 1; j < gridLayout.getRows(); j++)
                for (int i = 2; i < gridLayout.getColumns() - 2; i++) {
                    if (!Strings.isNullOrEmpty(((TextField) gridLayout.getComponent(i, j)).getValue())) {
                        String temp = ((TextField) gridLayout.getComponent(i, j)).getValue();
                        ((TextField) gridLayout.getComponent(i + 1, j)).setValue(temp);
                    }
                }
        } else {
            // 开始语音，这里判断一下
            if ("单件".equals(cbInspectionType.getValue())) {
                inspectionBySingle();
            } else {
                inspectionByGroup();
            }
        }
    }

    /*
     * 此方法用于用户单件检验，单件检验时，用户不需要选择检验项，按照页面顺序依次检验
     * 检验过程中，随时用户可以点击确认按钮保存信息，下次登录后，用户可以获取已经完成检验的数据，并继续未完成的检验
     * **/
    private void inspectionBySingle() {
        // 根据页面的数据，获取语音输入的开始位置
        int[] startPosition = getStartXYSingle();
        x = startPosition[0];
        y = startPosition[1];
        ipAddress = RequestInfo.current().getUserIpAddress();
        System.out.println(x + "," + y);
        try {
            client = new SocketClient(ipAddress, AppConstant.PORT);
            btnStart.setEnabled(false);
            client.setReceiveListener(serverMessage -> {
                if (serverMessage.startsWith(AppConstant.PREFIXPLATTEXTEND) && AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
                    //表示服务端接受播放语音消息并返回结果
                    try {
                        prefixSend = AppConstant.PREFIXSTARTRECORD;
                        client.sendMessage("[BR]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (serverMessage.startsWith(AppConstant.PREFIXRECORDRESULT)
                        && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                    //表示服务端返回的从客户端识别到的语音消息需要将信息进行处理
                    String messageBody = serverMessage.split("]")[1];
                    if (ensureMessage) {
                        if (!serverMessage.split("\\|")[1].equals("1")) {
                            ifSave.append(messageBody.split("\\|")[0]);
                        }
                    } else {
                        if (!serverMessage.split("\\|")[1].equals("1")) {
                            returnMessage.append(messageBody.split("\\|")[0]);
                        }
                    }
                } else if (serverMessage.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {
                    //表示客户端语音录入结束，服务端返回消息头，这时候可以来根据服务端识别的消息进行逻辑处理
                    if (returnMessage.length() == 0) {
                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                        try {
                            if (y == 0) {
                                // sn序列号
                                client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
                            } else {
                                // 检验项
                                String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                if (x == gridLayout.getColumns() - 1) {
                                    client.sendMessage("[PT]请输入检验项" + item + "的量具编号");
                                } else {
                                    client.sendMessage("[PT]请输入检验项" + item + "的值");
                                }
                            }
                        } catch (Exception e) {
                            NotificationUtils.notificationError("Socket连接中断");
                        }
                    } else {
                        String displayMessage = returnMessage.toString().trim().replace("酒吧", "98")
                                .replace(":", ".").replace("吧", "8").toUpperCase();
                        if (x == gridLayout.getColumns() - 1 || y == 0) {
                            //当前录入的是序列号或者量具编号
                            getUI().accessSynchronously(new Runnable() {
                                @Override
                                public void run() {
                                    TextField component = (TextField) gridLayout.getComponent(x, y);
                                    System.out.println(displayMessage);
                                    if (x == gridLayout.getColumns() - 1) {// 量具信息
                                        if (displayMessage.startsWith("SJ")) {
                                            component.setValue("SJQ-" + displayMessage.substring(3));
                                        } else {
                                            component.setValue("CSI-" + displayMessage);
                                        }
                                    } else {
                                        component.setValue(displayMessage);
                                    }
                                    returnMessage.delete(0, returnMessage.length());
                                }
                            });
                            //下一个检验项
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (y == 0) {
                                    y++;
                                    String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                    client.sendMessage("[PT]请输入 检验项" + item + "的值");
                                } else {
                                    if (y == gridLayout.getRows() - 1) {
                                        prefixSend = AppConstant.INSPECTIONDONE;
                                        client.sendMessage("[PT]检验完成");
                                    } else {
                                        y++;
                                        String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                        client.sendMessage("[PT]请输入检验项" + item + "量具编号");
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                            DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev,
                                    item);
                            if ("NUMBRIC".equals(ruler.getInspectionItemType())) {
                                double minValue = ruler.getMinValue();
                                double maxValue = ruler.getMaxValue();
                                if (RegExpValidatorUtils.isNumber(displayMessage)) {
                                    if (Double.parseDouble(displayMessage) >= minValue && Double.parseDouble(displayMessage) <= maxValue) {
                                        //检验项为数字类型并且输入的值再标准范围，此时直接录入值
                                        getUI().accessSynchronously(new Runnable() {
                                            @Override
                                            public void run() {
                                                TextField component = (TextField) gridLayout.getComponent(x, y);
                                                component.setValue(displayMessage);
                                                returnMessage.delete(0, returnMessage.length());
                                            }
                                        });
                                        //下一个检验项播报
                                        try {
                                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                                            if (y < gridLayout.getRows() - 1) {
                                                //继续当前列
                                                y++;
                                                item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                client.sendMessage("[PT]请输入 检验项" + item + "的值");
                                            } else {
                                                //列++
                                                x++;
                                                if (x == gridLayout.getColumns() - 1) {
                                                    y = 1;
                                                    item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                    client.sendMessage("[PT]请输入检验项" + item + "的量具编号");
                                                } else {
                                                    y = 0;
                                                    client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个零件的序列号");
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        //检验项时数字类型，但是录入的值不在标准范围，此时需要用户确认一下，是否输入
                                        try {
                                            if (ensureMessage) {
                                                //表示当前的信息是确认信息，是否录入不在标准范围内的值
                                                if (ifSave != null && ifSave.length() > 0 && "是".equals(ifSave.toString())) {
                                                    getUI().accessSynchronously(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            TextField component = (TextField) gridLayout.getComponent(x,
                                                                    y);
                                                            component.setValue(displayMessage);
                                                            component.setStyleName(CoreTheme.BACKGROUND_RED);
                                                            returnMessage.delete(0, returnMessage.length());
                                                            ifSave.delete(0, ifSave.length());
//															saveInspectionItem(x, y, false, purchasingOrderInfo);
                                                            ensureMessage = false;
                                                        }
                                                    });
                                                    //下一个检验项
                                                    try {
                                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                        if (y < gridLayout.getRows() - 1) {
                                                            //继续当前列
                                                            y++;
                                                            item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                            client.sendMessage("[PT]请输入 检验项" + item + "的值");
                                                        } else {
                                                            //列++
                                                            x++;
                                                            if (x == gridLayout.getColumns() - 1) {
                                                                y = 1;
                                                                item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                                client.sendMessage("[PT]请输入检验项" + item + "的量具编号");
                                                            } else {
                                                                y = 0;
                                                                client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个零件的序列号");
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                } else {
                                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                    client.sendMessage("[PT]请重新输入 检验项" + item + "的值");
                                                    returnMessage.delete(0, returnMessage.length());
                                                    ifSave.delete(0, ifSave.length());
                                                    ensureMessage = false;
                                                }
                                            } else {
                                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                ensureMessage = true;
                                                client.sendMessage("[PT]当前录入值" + displayMessage + "不在标准范围，是否录入");
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } else {
                                    //检验项时数字类型，录入数据不是数字类型，提示重新录入
                                    try {
                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                        client.sendMessage("[PT]输入数据" + returnMessage.toString() + "不是数字类型，请重新录入");
                                        returnMessage.delete(0, returnMessage.length());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                getUI().accessSynchronously(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextField component = (TextField) gridLayout.getComponent(x, y);
                                        System.out.println(displayMessage);
                                        getUI().accessSynchronously(new Runnable() {
                                            @Override
                                            public void run() {
                                                if ("OK".equals(displayMessage)) {
                                                    component.setValue(displayMessage);
                                                } else {
                                                    component.setValue("NG");
                                                    component.setStyleName(CoreTheme.BACKGROUND_RED);
                                                }
//												saveInspectionItem(x, y, false, purchasingOrderInfo);
                                            }
                                        });
                                        returnMessage.delete(0, returnMessage.length());
                                    }
                                });
                                //下一个检验项
                                try {
                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                    if (y < gridLayout.getRows() - 1) {
                                        //继续当前列
                                        y++;
                                        item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                        client.sendMessage("[PT]请输入 检验项" + item + "的值");
                                    } else {
                                        //列++
                                        x++;
                                        y = 0;
                                        client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个零件的序列号");
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                } else if (serverMessage.startsWith(AppConstant.PALYEXCEPTION)) {
                    System.out.println("语音服务网络断开，正尝试重新连接");
                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                    try {
                        if (y == 0) {
                            // sn序列号
                            client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
                        } else {
                            // 检验项
                            String itemName = ((Label) gridLayout.getComponent(1, y)).getValue();
                            if (x == gridLayout.getColumns() - 1) {
                                client.sendMessage("[PT]请输入检验项" + itemName + "的量具编号");
                            } else {
                                client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
                            }
                        }
                    } catch (Exception e) {
                        NotificationUtils.notificationError("Socket连接中断");
                    }
                }
            });
            //发送第一条语音消息
            prefixSend = AppConstant.PREFIXPLAYTEXT;
            if (y == 0) {
                //如果其实位置在第一行(零件序列号)
                client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个零件的序列号");
            } else {
                //如果是最后一列，需要输入量具编号
                String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                if (x == gridLayout.getColumns() - 1) {
                    client.sendMessage("[PT]请输入检验项" + item + "量具编号");
                } else {
                    String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
                    client.sendMessage("[PT]请输入SN" + sn + "的检验项" + item + "的值");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 此方法用于分组语音检验，用户可以根据需要自己选择当前需要检验的检验项
     * 如果是第一次检验，则在输入完所有的sn之后，系统提示用户选择需要检验的检验项，用户只需要报检验项的行号
     * 如果是之前已经检验过但是没有完成检验，在数据加载完成之后，由用户选择需要检验的检验项
     * */
    //分组语音检验
    private void inspectionByGroup() {
        int[] startXY = getStartXYGroup();
        x = startXY[0];
        y = startXY[1];
        ipAddress = RequestInfo.current().getUserIpAddress();
        try {
            client = new SocketClient(ipAddress, AppConstant.PORT);
            btnStart.setEnabled(false);
            client.setReceiveListener(serverMessage -> {
                if (serverMessage.startsWith(AppConstant.PREFIXPLATTEXTEND) && AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
                    //表示服务端接受播放语音消息并返回结果
                    try {
                        prefixSend = AppConstant.PREFIXSTARTRECORD;
                        client.sendMessage("[BR]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (serverMessage.startsWith(AppConstant.PREFIXRECORDRESULT)
                        && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                    //表示服务端返回的从客户端识别到的语音消息需要将信息进行处理
                    String messageBody = serverMessage.split("]")[1];
                    if (ensureMessage) {
                        //当前确认信息
                        if (!serverMessage.split("\\|")[1].equals("1")) {
                            ifSave.append(messageBody.split("\\|")[0]);
                        }
                    } else if (isSelectItemMessage) {
                        //当前选择检验项信息
                        if (!serverMessage.split("\\|")[1].equals("1")) {
                            itemRowNum.append(messageBody.split("\\|")[0]);
                        }
                    } else {
                        //当前录入检验信息
                        if (!serverMessage.split("\\|")[1].equals("1")) {
                            returnMessage.append(messageBody.split("\\|")[0]);
                        }
                    }
                } else if (serverMessage.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {
                    //表示客户端语音录入结束，服务端返回消息头，这时候可以来根据服务端识别的消息进行逻辑处理
                    if (isSelectItemMessage) {
                        try {
                            if (itemRowNum.length() == 0) {
                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                client.sendMessage("[PT]请输入需要检验的检验项的行号");
                            } else {
                                String itemRowIndex = itemRowNum.toString().trim().replace("酒吧", "98")
                                        .replace(":", ".").replace("吧", "8").toUpperCase();
                                if (RegExpValidatorUtils.isPositive(itemRowIndex)) {
                                    x = 2;
                                    y = Integer.parseInt(itemRowIndex);
                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                    Object[] result = ifValidateRowIndex(y);
                                    if ((boolean) result[0]) {
                                        String itemName = ((Label) gridLayout.getComponent(1, y)).getValue();
                                        String sn = ((TextField) gridLayout.getComponent(2, 0)).getValue();
                                        client.sendMessage("[PT]请输入SN" + sn + "的检验项" + itemName + "的值");
                                        isSelectItemMessage = false;
                                        itemRowNum.delete(0, itemRowNum.length());
                                    } else {
                                        String message = (String) result[1];
                                        itemRowNum.delete(0, itemRowNum.length());
                                        client.sendMessage("[PT]" + message);
                                    }
                                } else {
                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                    client.sendMessage("[PT]输的值不是数字类型，请重新输入需要检验的检验项的行号");
                                    itemRowNum.delete(0, itemRowNum.length());
                                }
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        if (returnMessage.length() == 0) {
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (y == 0) {
                                    // sn序列号
                                    client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
                                } else {
                                    // 检验项
                                    String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                    if (x == gridLayout.getColumns() - 1) {
                                        client.sendMessage("[PT]请输入检验项" + item + "的量具编号");
                                    } else {
                                        client.sendMessage("[PT]请输入检验项" + item + "的值");
                                    }
                                }
                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }
                        } else {
                            String displayMessage = returnMessage.toString().trim().replace("酒吧", "98")
                                    .replace(":", ".").replace("吧", "8").toUpperCase();
                            if (x == gridLayout.getColumns() - 1 || y == 0) {
                                //当前录入的是序列号或者量具编号
                                getUI().accessSynchronously(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextField component = (TextField) gridLayout.getComponent(x, y);
                                        System.out.println(displayMessage);
                                        if (x == gridLayout.getColumns() - 1) {// 量具信息
                                            if (displayMessage.startsWith("SJ")) {
                                                component.setValue("SJQ-" + displayMessage.substring(3));
                                            } else {
                                                component.setValue("CSI-" + displayMessage);
                                            }
                                        } else {
                                            component.setValue(displayMessage);
                                        }
                                        returnMessage.delete(0, returnMessage.length());
                                    }
                                });
                                //下一个检验项
                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                try {
                                    if (y == 0) {
                                        if (x == gridLayout.getColumns() - 2) {
                                            //序列号输入完成，需要确认输入检验项
                                            client.sendMessage("[PT]请输入需要检验的检验项的行号");
                                            isSelectItemMessage = true;
                                        } else {
                                            x++;
                                            client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个零件的序列号");
                                        }
                                    } else {
                                        //当前在最后一列，需要用户确认下一个检验项
                                        if (inspectionDone()) {
                                            prefixSend = AppConstant.INSPECTIONDONE;
                                            client.sendMessage("[PT]检验完成");
                                        } else {
                                            client.sendMessage("[PT]请输入需要检验的检验项的行号");
                                            isSelectItemMessage = true;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                String item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev,
                                        item);
                                if ("NUMBRIC".equals(ruler.getInspectionItemType())) {
                                    double minValue = ruler.getMinValue();
                                    double maxValue = ruler.getMaxValue();
                                    if (RegExpValidatorUtils.isNumber(displayMessage)) {
                                        if (Double.parseDouble(displayMessage) >= minValue && Double.parseDouble(displayMessage) <= maxValue) {
                                            //检验项为数字类型并且输入的值再标准范围，此时直接录入值
                                            getUI().accessSynchronously(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TextField component = (TextField) gridLayout.getComponent(x, y);
                                                    component.setValue(displayMessage);
                                                    returnMessage.delete(0, returnMessage.length());
                                                }
                                            });
                                            //下一个检验项播报
                                            try {
                                                prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                x++;
                                                if (x == gridLayout.getColumns() - 1) {
                                                    client.sendMessage("[PT]请输入 检验项" + item + "的量具编号");
                                                } else {
                                                    String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
                                                    client.sendMessage("[PT]请输入SN" + sn + "的检验值");
                                                }
                                            } catch (Exception e) {
                                            }
                                        } else {
                                            //检验项时数字类型，但是录入的值不在标准范围，此时需要用户确认一下，是否输入
                                            try {
                                                if (ensureMessage) {
                                                    //表示当前的信息是确认信息，是否录入不在标准范围内的值
                                                    if (ifSave != null && ifSave.length() > 0 && "是".equals(ifSave.toString())) {
                                                        getUI().accessSynchronously(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                TextField component = (TextField) gridLayout.getComponent(x,
                                                                        y);
                                                                component.setValue(displayMessage);
                                                                component.setStyleName(CoreTheme.BACKGROUND_RED);
                                                                returnMessage.delete(0, returnMessage.length());
                                                                ifSave.delete(0, ifSave.length());
//																saveInspectionItem(x, y, false, purchasingOrderInfo);
                                                                ensureMessage = false;
                                                            }
                                                        });
                                                        //下一个检验项播报
                                                        try {
                                                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                            item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                                            x++;
                                                            if (x == gridLayout.getColumns() - 1) {
                                                                client.sendMessage("[PT]请输入 检验项" + item + "的量具编号");
                                                            } else {
                                                                String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
                                                                client.sendMessage("[PT]请输入SN" + sn + "的检验值");
                                                            }
                                                        } catch (Exception e) {
                                                        }
                                                    } else {
                                                        prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                        client.sendMessage("[PT]请重新输入 检验项" + item + "的值");
                                                        returnMessage.delete(0, returnMessage.length());
                                                        ifSave.delete(0, ifSave.length());
                                                        ensureMessage = false;
                                                    }
                                                } else {
                                                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                                                    ensureMessage = true;
                                                    client.sendMessage("[PT]当前录入值" + displayMessage + "不在标准范围，是否录入");
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    } else {
                                        //检验项时数字类型，录入数据不是数字类型，提示重新录入
                                        try {
                                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                                            client.sendMessage("[PT]输入数据" + returnMessage.toString() + "不是数字类型，请重新录入");
                                            returnMessage.delete(0, returnMessage.length());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    getUI().accessSynchronously(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextField component = (TextField) gridLayout.getComponent(x, y);
                                            System.out.println(displayMessage);
                                            getUI().accessSynchronously(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if ("OK".equals(displayMessage)) {
                                                        component.setValue(displayMessage);
                                                    } else {
                                                        component.setValue("NG");
                                                        component.setStyleName(CoreTheme.BACKGROUND_RED);
                                                    }
//													saveInspectionItem(x, y, false, purchasingOrderInfo);
                                                }
                                            });
                                            returnMessage.delete(0, returnMessage.length());
                                        }
                                    });
                                    //下一个检验项
                                    try {
                                        //下一个检验项播报
                                        try {
                                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                                            item = ((Label) gridLayout.getComponent(1, y)).getValue();
                                            x++;

                                            if (x == gridLayout.getColumns() - 1) {
                                                client.sendMessage("[PT]请输入 检验项" + item + "的量具编号");
                                            } else {
                                                String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
                                                client.sendMessage("[PT]请输入SN" + sn + "的检验值");
                                            }
                                        } catch (Exception e) {
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                        }
                    }

                } else if (serverMessage.startsWith(AppConstant.PALYEXCEPTION)) {
                    System.out.println("语音服务网络断开，正尝试重新连接");
                    prefixSend = AppConstant.PREFIXPLAYTEXT;
                    try {
                        if (y == 0) {
                            // sn序列号
                            client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
                        } else {
                            // 检验项
                            String itemName = ((Label) gridLayout.getComponent(1, y)).getValue();
                            if (x == gridLayout.getColumns() - 1) {
                                client.sendMessage("[PT]请输入检验项" + itemName + "的量具编号");
                            } else {
                                client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
                            }
                        }
                    } catch (Exception e) {
                        NotificationUtils.notificationError("Socket连接中断");
                    }
                }
            });
            //发送第一条语音消息
            prefixSend = AppConstant.PREFIXPLAYTEXT;
            if (y == 0 && x == 0) {
                //表示当前页面每行数据都已经输入完成，需要用户确认需要检验的项所在的行号
                client.sendMessage("[PT]请输入需要检验的检验项的行号");
                isSelectItemMessage = true;
            } else {
                //如果是第一次
                if (isFirst) {
                    x = 2;
                    y = 0;
                    client.sendMessage("[PT]请输入检验的第一个零件的序列号");
                } else {
                    if (y == 0) {
                        //需要输入零件序列号
                        client.sendMessage("[PT]请输入检验的第" + (x - 1) + "个零件的序列号");
                    } else {
                        String itemName = ((Label) gridLayout.getComponent(1, y)).getValue();
                        if (x == gridLayout.getColumns() - 1) {
                            client.sendMessage("[PT]请输入检验项" + itemName + "的量具编号");
                        } else {
                            String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
                            client.sendMessage("[PT]请输入SN" + sn + "的检验项" + itemName + "的值");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDataToDisplayArea(PurchasingOrderInfo purchasingOrderInfo) {
        if (purchasingOrderInfo == null) {
            // 清空显示区的信息
            lblMaterialNo.clear();
            lblMaterialRev.clear();
            lblMaterialDesc.clear();
            lblVendor.clear();
            lblQualityPlan.clear();
            lblDrawingNo.clear();
        } else {
            // 填充信息
            String materialNo = purchasingOrderInfo.getMaterialNo();
            String materialRev = purchasingOrderInfo.getMaterialRev();
            String materialDesc = purchasingOrderInfo.getMaterialDesc();
            String vendor = purchasingOrderInfo.getVendorName();
            String quality = "";
            String drawingNo = "";
            // 获取零件信息，并得到质量计划和图纸编号
            SparePart part = sparePartService.getByNoRev(materialNo, materialRev);
            if (part != null) {
                quality = part.getQaPlan() + " REV" + part.getQaPlanRev();
                drawingNo = part.getDrawNo() + " REV" + part.getDrawRev();
            }

            lblMaterialNo.setValue(materialNo);
            lblMaterialRev.setValue(materialRev);
            lblMaterialDesc.setValue(materialDesc);
            lblVendor.setValue(vendor);
            lblQualityPlan.setValue(quality);
            lblDrawingNo.setValue(drawingNo);
        }
    }


    // 在多人检验的情况下，获取检验项index
//    public List<Integer> getCheckedItemsIndex() {
//        int rows = gridLayout.getRows();
//        List<Integer> indexList = new ArrayList<>();
//        for (int index = 1; index < rows; index++) {
//            CheckBox cbInstance = (CheckBox) gridLayout.getComponent(0, index);
//            if (cbInstance.getValue()) {
//                indexList.add(index);
//            }
//        }
//        return indexList;
//    }

//    public void saveInspectionItem(int columnIndex, int rowIndex, boolean pass, PurchasingOrderInfo orderInfo) {
//
//        String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
//        String itemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
//        String itemValue = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
//        int columnCount = gridLayout.getColumns();
//        String gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
//        DimensionInspectionResult result = new DimensionInspectionResult();
//
//        result.setGageInfo(gageInfo);
//        result.setInspectionValue(itemValue);
//        result.setMaterialNo(orderInfo.getMaterialNo());
//        result.setMaterialRev(orderInfo.getMaterialRev());
//        result.setOrderItem(orderInfo.getPurchasingItemNo());
//        result.setPurchasingNo(orderInfo.getPurchasingNo());
//        result.setSapInspectionNo(orderInfo.getSapInspectionLot());
//        String serialNo = orderInfo.getPurchasingNo() + "-" + orderInfo.getPurchasingItemNo() + "-" + sn;
//        result.setMaterialSN(serialNo);
//        result.setInspectionName(itemName);
//        result.setIsPass(pass);
//
//        dimensionInspectionService.save(result);
//    }


    public void createReport(String path) {
        // 由于尺寸检验的检验数量个检验项都是不可固定的，都有可能超出一页纸范围的可能，这里采用固定列(SN数量)的方式，来动态加载检验项，超过一页的继续往下一页加载，如果检验数量超过一页的范围，则需要在生成一份word保存检验结果
        int dataColumn = gridLayout.getColumns() - 3;// 实际检验数量
        int pageNum = (int) Math.ceil((float) dataColumn / 10);// 实际的检验数量需要几页纸可以加载数据
        path = path + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;
        List<File> fileList = new ArrayList<>();
        for (int index = 0; index < pageNum; index++) {
            try {
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("partNo", purchasingOrderInfo.getMaterialNo());
                dataMap.put("Rev", purchasingOrderInfo.getMaterialRev());
                dataMap.put("purchasingOrder",
                        purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
                dataMap.put("vendor", purchasingOrderInfo.getVendorName());
                dataMap.put("description", purchasingOrderInfo.getMaterialDesc().replaceAll("&", "&amp;")
                        .replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                dataMap.put("qpRev", lblQualityPlan.getValue());
                dataMap.put("qty", purchasingOrderInfo.getMaterialQuantity());
                dataMap.put("serialNos", tfSerialNos.getValue());
                dataMap.put("drawingNo", lblDrawingNo.getValue());
                // 循环写入SN
                // List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
                for (int snIndex = 2; snIndex < 12; snIndex++) {
                    // 加载SN
                    int colIndex = 10 * index + snIndex;
                    if (colIndex < gridLayout.getColumns() - 1) {
                        TextField tfSn = (TextField) gridLayout.getComponent(10 * index + snIndex, 0);
                        String snValue = tfSn.getValue() == null ? "" : tfSn.getValue();
                        dataMap.put("SN" + (snIndex - 1), snValue);
                    } else {
                        dataMap.put("SN" + (snIndex - 1), "");
                    }
                }
                // 循环写入值，以行为单位
                List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
                for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
                    Map<String, String> sMap = new HashMap<>();
                    Label lbl = (Label) gridLayout.getComponent(1, rowIndex);
                    String item = lbl.getValue();
                    sMap.put("item", item);
                    // 获取当前检验项的检验人员信息
                    String userName = dimensionInspectionService.getInspector(purchasingOrderInfo.getSapInspectionLot(),
                            item);
                    // 循环当前行的每一列
                    for (int valueIndex = 2; valueIndex < 12; valueIndex++) {
                        int colIndex = 10 * index + valueIndex;
                        if (colIndex < gridLayout.getColumns() - 1) {
                            TextField tfValue = (TextField) gridLayout.getComponent(10 * index + valueIndex, rowIndex);
                            String value = tfValue.getValue() == null ? "" : tfValue.getValue();
                            sMap.put("value" + (valueIndex - 1), value);
                        } else {
                            sMap.put("value" + (valueIndex - 1), "");
                        }
                    }
                    // 量具信息
                    TextField tfGauge = (TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex);
                    String gaugeValue = tfGauge.getValue() == null ? "" : tfGauge.getValue();
                    sMap.put("gague", gaugeValue);
                    BASE64Encoder encoder = new BASE64Encoder();
                    Media mediaImage = caMediaService.getByTypeName("ES", userName);
                    sMap.put("signature", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));
                    sMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                    sList.add(sMap);
                }
                dataMap.put("slist", sList);
//				dataMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

                Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
                configuration.setDefaultEncoding("utf-8");
                configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

                String jPath = AppConstant.DOC_XML_FILE_PATH;
                configuration.setDirectoryForTemplateLoading(new File(jPath));
                // 以utf-8的编码读取模板文件
                Template template = configuration.getTemplate("dimension.xml", "utf-8");

                // 输出文件
                String fileName = path + (index + 1) + ".doc";
                File outFile = new File(fileName);
                // 将模板和数据模型合并生成文件
                Writer out = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8), 1024 * 1024);
                template.process(dataMap, out);
                out.flush();
                out.close();
                fileList.add(outFile);
                System.out.println("*********成功生成尺寸检验报告***********");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 合并文档
        int fileCount = fileList.size();
        String fileName = fileList.get(0).getAbsolutePath();
        Document document = new Document(fileName, FileFormat.Doc);

        for (int index = 1; index < fileCount; index++) {
            if (fileCount > 1) {
                // document.addSection().addParagraph();
                document.insertTextFromFile(fileList.get(index).getAbsolutePath(), FileFormat.Doc);
                // fileList.get(index).delete();
            }
        }
        document.saveToFile(path + purchasingOrderInfo.getPurchasingNo() + "-"
                        + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".doc",
                FileFormat.Doc);
        deleteFiles(fileList);
        wordToPDF(path + purchasingOrderInfo.getPurchasingNo() + "-"
                + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".doc");
    }

    public void createReportPDF(String path) {
        // 由于尺寸检验的检验数量个检验项都是不可固定的，都有可能超出一页纸范围的可能，这里采用固定列(SN数量)的方式，来动态加载检验项，超过一页的继续往下一页加载，如果检验数量超过一页的范围，则需要在生成一份word保存检验结果
        int dataColumn = gridLayout.getColumns() - 3;// 实际检验数量
        int pageNum = (int) Math.ceil((float) dataColumn / 10);// 实际的检验数量需要几页纸可以加载数据
        path = path + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;
        String filename = AppConstant.DOC_XML_FILE_PATH + "\\dimension.pdf";
        List<File> fileList = new ArrayList<>();
        int pageCount = 0;
        int totalPage = pageNum * ((int) Math.ceil((float) gridLayout.getRows() / 14));
        for (int index = 0; index < pageNum; index++) {
            for (int index2 = 0; index2 < ((int) Math.ceil((float) gridLayout.getRows() / 14)); index2++) {//尺寸多于14个需要分页
                pageCount++;
                try {
                    PdfReader readerOriginalDoc = new PdfReader(filename);
                    PdfWriter writeDest = new PdfWriter(path + index + index2 + ".pdf");
                    PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
                    PdfAcroForm form = PdfAcroForm.getAcroForm(newDoc, true);
                    com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(newDoc);
                    text2Formfield(document, form, "PN", purchasingOrderInfo.getMaterialNo());
                    text2Formfield(document, form, "REV", purchasingOrderInfo.getMaterialRev());
                    text2Formfield(document, form, "PO",
                            purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
                    text2Formfield(document, form, "VENDOR", purchasingOrderInfo.getVendorName());
                    text2Formfield(document, form, "DESC", purchasingOrderInfo.getMaterialDesc());
                    text2Formfield(document, form, "IP", lblQualityPlan.getValue());
                    text2Formfield(document, form, "QTY", Integer.toString(purchasingOrderInfo.getMaterialQuantity()));
                    text2Formfield(document, form, "SN", tfSerialNos.getValue());
                    text2Formfield(document, form, "DWG", lblDrawingNo.getValue());
                    // 循环写入SN
                    // List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
                    for (int snIndex = 2; snIndex < 12; snIndex++) {
                        // 加载SN
                        int colIndex = 10 * index + snIndex;
                        if (colIndex < gridLayout.getColumns() - 1) {
                            TextField tfSn = (TextField) gridLayout.getComponent(10 * index + snIndex, 0);
                            String snValue = tfSn.getValue() == null ? "" : tfSn.getValue();
                            text2Formfield(document, form, "SN" + (snIndex - 1), snValue);
                        } else {
                            text2Formfield(document, form, "SN" + (snIndex - 1), "");
                        }
                    }
                    // 循环写入值，以行为单位
                    for (int rowIndex = 1; (rowIndex < gridLayout.getRows() - index2 * 14) && (rowIndex <= 14); rowIndex++) {
                        Label lbl = (Label) gridLayout.getComponent(1, rowIndex + index2 * 14);
                        String item = lbl.getValue();
                        text2Formfield(document, form, "DIM" + rowIndex, item);
                        // 获取当前检验项的检验人员信息
                        String userName = dimensionInspectionService.getInspector(purchasingOrderInfo.getSapInspectionLot(),
                                item);
                        // 循环当前行的每一列
                        for (int valueIndex = 2; valueIndex < 12; valueIndex++) {
                            int colIndex = 10 * index + valueIndex;
                            if (colIndex < gridLayout.getColumns() - 1) {
                                TextField tfValue = (TextField) gridLayout.getComponent(10 * index + valueIndex, rowIndex + index2 * 14);
                                String value = tfValue.getValue() == null ? "" : tfValue.getValue();
                                text2Formfield(document, form, "RLT" + ((valueIndex - 1) + 10 * (rowIndex - 1)), value);
                            } else {
                                text2Formfield(document, form, "RLT" + ((valueIndex - 1) + (10 * (rowIndex - 1))), "");
                            }
                        }
                        // 量具信息
                        TextField tfGauge = (TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex + index2 * 14);
                        String gaugeValue = tfGauge.getValue() == null ? "" : tfGauge.getValue();
                        text2Formfield(document, form, "GAGE" + rowIndex, gaugeValue);
                        Media mediaImage = caMediaService.getByTypeName("ES", userName);
                        img2Formfield(document, form, "SIGN" + rowIndex, mediaImage);
                        text2Formfield(document, form, "DATE" + rowIndex, new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                    }
                    if (gridLayout.getRows() - index2 * 14 < 14) {
                        for (int rowIndex2 = gridLayout.getRows() - index2 * 14; rowIndex2 <= 14; rowIndex2++) {
                            text2Formfield(document, form, "DIM" + rowIndex2, "");
                            for (int valueIndex = 2; valueIndex < 12; valueIndex++) {
                                text2Formfield(document, form, "RLT" + ((valueIndex - 1) + 10 * (rowIndex2 - 1)), "");
                            }
                            text2Formfield(document, form, "GAGE" + rowIndex2, "");
                            text2Formfield(document, form, "SIGN" + rowIndex2, "");
                            text2Formfield(document, form, "DATE" + rowIndex2, "");
                        }
                    }
                    text2Formfield(document, form, "PAGE", "Page " + pageCount + " of " + totalPage);

                    //加入受控章
                    ImageData imageData = ImageDataFactory.create("D:\\CameronQualityFiles\\DOCS\\CONTROLLED.png");
                    com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                    image.scaleToFit(100, 100);
                    image.setRotationAngle(0.5);
                    image.setFixedPosition(1, 470, 180);
                    document.add(image);

                    File outFile = new File(path + index + index2 + ".pdf");
                    fileList.add(outFile);
                    document.close();
                    System.out.println("*********成功生成尺寸检验报告***********");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 合并文档
        try {
            PdfWriter writer = new PdfWriter(path + purchasingOrderInfo.getPurchasingNo() + "-"
                    + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf");

            // In smart mode when resources (such as fonts, images,...) are encountered,
            // a reference to these resources is saved in a cache and can be reused.
            // This mode reduces the file size of the resulting PDF document.
            writer.setSmartMode(true);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // This method initializes an outline tree of the document and sets outline mode to true.
            pdfDoc.initializeOutlines();


            for (int i = 0; i < fileList.size(); i++) {
                String f = fileList.get(i).getAbsolutePath();
                PdfDocument addedDoc = new PdfDocument(new PdfReader(f));
                addedDoc.copyPagesTo(1, addedDoc.getNumberOfPages(), pdfDoc);
                addedDoc.close();
            }
            pdfDoc.close();
            deleteFiles(fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // 点击确认按钮,保存数据
    public int saveDataSingle() {
        // 将pass置为true
        pass = true;
        // 删除已有数据
        List<DimensionInspectionResult> list = new ArrayList<>();
        boolean flag = true;
        int rowCount = gridLayout.getRows();
        int columnCount = gridLayout.getColumns();
        String sapLot = purchasingOrderInfo.getSapInspectionLot();
        String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo();
        List<DimensionInspectionResult> oldResults = dimensionInspectionService.getBySapLotNo(sapLot);
        for (DimensionInspectionResult r : oldResults) {
            dimensionInspectionService.delete(r);
        }
        // 淇濆瓨鏁版嵁
        for (int columnIndex = 2; columnIndex < columnCount - 1; columnIndex++) {
            String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
            if (Strings.isNullOrEmpty(sn) || !flag) {
                break;
            }
            for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
                String itemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
                String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
                String gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
                if (Strings.isNullOrEmpty(value)) {
                    flag = false;
                    break;
                }
                DimensionInspectionResult dimensionInspectionResult = new DimensionInspectionResult();
                dimensionInspectionResult.setInspectionValue(value);
                dimensionInspectionResult.setMaterialNo(materialNo);
                dimensionInspectionResult.setMaterialRev(materialRev);
                dimensionInspectionResult.setOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                dimensionInspectionResult.setPurchasingNo(purchasingOrderInfo.getPurchasingNo());
                dimensionInspectionResult.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                dimensionInspectionResult.setMaterialSN(snPrefix + "-" + sn);
                dimensionInspectionResult.setInspectionName(itemName);
                dimensionInspectionResult.setIsPass(checkItemPass(materialNo, materialRev, itemName, value));
                dimensionInspectionResult.setGageInfo(gageInfo);
                list.add(dimensionInspectionResult);

            }
        }
        dimensionInspectionService.saveAll(list);
        return list.size();
    }

    public int saveDataGroup() {
        pass = true;
        List<DimensionInspectionResult> list = new ArrayList<>();
        int rowCount = gridLayout.getRows();
        int columnCount = gridLayout.getColumns();
        String sapLot = purchasingOrderInfo.getSapInspectionLot();
        String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo();
        System.out.println("1======" + LocalDateTime.now().toString());
        List<DimensionInspectionResult> oldResults = dimensionInspectionService.getBySapLotNo(sapLot);
        for (DimensionInspectionResult r : oldResults) {
            dimensionInspectionService.delete(r);
        }
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            String firstInspectionValue = ((TextField) gridLayout.getComponent(2, rowIndex)).getValue();//每行的第一个检验项的值，如果为空，则跳过此行
            if (Strings.isNullOrEmpty(firstInspectionValue)) {
                continue;
            }
            String itemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
            String gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
            for (int columnIndex = 2; columnIndex < columnCount - 1; columnIndex++) {
                System.out.println("ROW:" + rowIndex + " COLUMN:" + columnIndex + "=====" + LocalDateTime.now().toString());
                String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
                if (Strings.isNullOrEmpty(value)) {
                    break;
                }
                String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
                DimensionInspectionResult dimensionInspectionResult = new DimensionInspectionResult();
                dimensionInspectionResult.setInspectionValue(value);
                dimensionInspectionResult.setMaterialNo(materialNo);
                dimensionInspectionResult.setMaterialRev(materialRev);
                dimensionInspectionResult.setOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                dimensionInspectionResult.setPurchasingNo(purchasingOrderInfo.getPurchasingNo());
                dimensionInspectionResult.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                dimensionInspectionResult.setMaterialSN(snPrefix + "-" + sn);
                dimensionInspectionResult.setInspectionName(itemName);
                dimensionInspectionResult.setIsPass(checkItemPass(materialNo, materialRev, itemName, value));
                dimensionInspectionResult.setGageInfo(gageInfo);
                list.add(dimensionInspectionResult);
            }
        }
        System.out.println("2======" + LocalDateTime.now().toString());
        dimensionInspectionService.saveAll(list);
        System.out.println("3======" + LocalDateTime.now().toString());
        return list.size();
    }


    public boolean checkItemPass(String materialNo, String materialRev, String itemName, String value) {
        DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev, itemName);
        String itemType = ruler.getInspectionItemType();
        if ("NUMBRIC".equals(itemType)) {
            double floatValue = Double.parseDouble(value);
            double maxValue = ruler.getMaxValue();
            double minValue = ruler.getMinValue();
            if (floatValue >= minValue && floatValue <= maxValue) {
                {
                    return true;
                }
            } else {
                pass = false;
                return false;
            }
        } else {
            if ("OK".equalsIgnoreCase(value)) {
                return true;
            } else {
                pass = false;
                return false;
            }
        }
    }

    public void deleteFiles(List<File> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).delete();
        }
    }

    public List<String> getPurchasingOrder(String type) {
        return purchasingOrderService.getPurchasingNo(type);
    }

    @Override
    public void _init() {
        cbPurchasingNo.setItems(getPurchasingOrder("DIMENSION"));
    }

    //判断用户输入的行号是否为有效的数字
    public Object[] ifValidateRowIndex(int index) {
        int count = gridLayout.getRows() - 1;
        if (index == 0 || index > count) {
            return new Object[]{false, "输入数字" + index + "不在1到" + count + "之间,请重新输入行号"};
        }
        String value = ((TextField) gridLayout.getComponent(2, index)).getValue();
        if (!Strings.isNullOrEmpty(value)) {
            return new Object[]{false, "第" + index + "行检验数据已经存在，请重新输入行号"};
        }
        return new Object[]{true, ""};
    }

    //当检验类型为分单件时，获取检验开始时候的起始左边X,Y
    public int[] getStartXYSingle() {
        for (int columnIndex = 2; columnIndex < gridLayout.getColumns(); columnIndex++) {
            for (int rowIndex = 0; rowIndex < gridLayout.getRows(); rowIndex++) {
                Component c = gridLayout.getComponent(columnIndex, rowIndex);
                if (c instanceof TextField) {
                    String value = ((TextField) c).getValue();
                    if (Strings.isNullOrEmpty(value)) {
                        return new int[]{columnIndex, rowIndex};
                    }
                }
            }
        }
        return new int[]{0, 0};
    }

    //当检验类型为分组时，获取检验开始时候的起始左边X,Y
    public int[] getStartXYGroup() {
        /**
         * 这里只需要判断某一行的数据是否全部输入完成，如果有，则默认从此处开始，如果没有，则有用户决定从哪里开始检验
         * 1.SN是否输入完成
         * 2.SN全部输入完成，判断检验项的输入情况
         * */
        int columnCount = gridLayout.getColumns();
        int rowCount = gridLayout.getRows();
        for (int columnIndex = 2; columnIndex < columnCount - 1; columnIndex++) {
            TextField tfsn = (TextField) gridLayout.getComponent(columnIndex, 0);
            String sn = tfsn.getValue();
            if (Strings.isNullOrEmpty(sn)) {
                return new int[]{columnIndex, 0};
            }
        }
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            for (int columnIndex = 2; columnIndex < columnCount; columnIndex++) {
                String firstValue = ((TextField) gridLayout.getComponent(2, rowIndex)).getValue();
                if (Strings.isNullOrEmpty(firstValue)) {
                    break;
                }
                TextField tfvalue = (TextField) gridLayout.getComponent(columnIndex, rowIndex);
                String value = tfvalue.getValue();
                if (Strings.isNullOrEmpty(value)) {
                    return new int[]{columnIndex, rowIndex};
                }
            }
        }
        return new int[]{0, 0};
    }

    //获取所有输入的SN，由于在分组检验的情况下，有可能用户在输入左右的需要检验的SN之后并没有输入检验结果，此时在检验结果的表中并不会记录数据，需要把SN保存

    public String checkedSn() {
        String checkedSn = "";
        for (int columnIndex = 2; columnIndex < gridLayout.getColumns() - 1; columnIndex++) {
            TextField tfsn = (TextField) gridLayout.getComponent(columnIndex, 0);
            String sn = tfsn.getValue();
            if (!Strings.isNullOrEmpty(sn)) {
                checkedSn = checkedSn + sn + ",";
            } else {
                break;
            }
        }
        if (checkedSn.length() > 0) {
            return checkedSn.substring(0, checkedSn.length() - 1);
        } else {
            return checkedSn;
        }
    }

    //如果所有的输入框都已经写入数据，表示此时检验已经完成，否则认为没有检验完成
    public boolean inspectionDone() {
        boolean flag = true;
        for (int k = 1; k < gridLayout.getRows(); k++) {
            for (int j = 2; j < gridLayout.getColumns(); j++) {
                Component c = gridLayout.getComponent(j, k);
                if (c instanceof TextField) {
                    if (Strings.isNullOrEmpty(((TextField) c).getValue())) {
                        flag = false;
                        break;
                    }
                }
            }
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    public void displayGrid(String qty) {
        if (!Strings.isNullOrEmpty(qty)) {
            if (RegExpValidatorUtils.isIsPositive(qty)) {
                gridLayout.removeAllComponents();
                inspectionQuantity = Integer.valueOf(qty);
                inspectionValue.setSizeFull();
                inspectionValue.setContent(gridLayout);
                purchasingOrderInfo = gridObject.asSingleSelect().getValue();
                listInstance = dimensionRulerService.getByNoRev(purchasingOrderInfo.getMaterialNo(),
                        purchasingOrderInfo.getMaterialRev());
                int inspectionItems = listInstance.size();// rows
                gridLayout.setRows(inspectionItems + 1);
                gridLayout.setColumns(inspectionQuantity + 3);
                gridLayout.setMargin(true);
//                Label lblDrawing = new Label("图纸尺寸");
//                lblDrawing.addStyleName(CoreTheme.FONT_PRIMARY);
//                gridLayout.addComponent(lblDrawing, 1, 0);
                gridLayout.addComponent(btnCopy, 1, 0);
                // gridLayout.addComponent(btnCopy, 0, 0);
//						gridLayout.addComponent(cbCheckAll, 0, 0);
                // 初始化第一列
                for (int i = 1; i < inspectionItems + 1; i++) {
                    Label labelOrder = new Label();
                    gridLayout.addComponent(labelOrder, 0, i);
                    labelOrder.addStyleNames(CoreTheme.MARGIN_RIGHT, CoreTheme.BACKGROUND_YELLOW);
                    labelOrder.setValue(String.valueOf(i));

                }
                // 初始化gridlayout第二列，检验项
                for (int i = 1; i < inspectionItems + 1; i++) {
                    Label label = new Label();
                    label.addStyleName(CoreTheme.FONT_PRIMARY);
                    String inspectionName = listInstance.get(i - 1).getInspectionItemName();
                    label.setValue(inspectionName);
                    gridLayout.addComponent(label, 1, i);
                }
                // 初始化GridLayout第一行,SN序列号
                for (int i = 2; i < inspectionQuantity + 2; i++) {
                    TextField tfSN = new TextField();
                    tfSN.setWidth("100px");
                    tfSN.setPlaceholder("零件SN");
                    tfSN.setValueChangeTimeout(1000);
                    gridLayout.addComponent(tfSN, i, 0);
                }
                Label lblGageInfo = new Label("量具信息");
                lblGageInfo.addStyleName(CoreTheme.FONT_PRIMARY);
                gridLayout.addComponent(lblGageInfo, inspectionQuantity + 2, 0);// 量具信息
                gridLayout.setComponentAlignment(lblGageInfo, Alignment.MIDDLE_CENTER);
                for (int i = 2; i < inspectionQuantity + 3; i++) {
                    for (int j = 1; j < inspectionItems + 1; j++) {
                        TextField tfInspectionValue = new TextField();
                        tfInspectionValue.setWidth("100px");
                        if (i < inspectionQuantity + 2) {
                            final String item = ((Label) gridLayout.getComponent(1, j)).getValue();
                            tfInspectionValue.addValueChangeListener(new ValueChangeListener<String>() {

                                @Override
                                public void valueChange(ValueChangeEvent<String> event) {
                                    DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev, item);
                                    if ("NUMBRIC".equals(ruler.getInspectionItemType())) {
                                        double minValue = ruler.getMinValue();
                                        double maxValue = ruler.getMaxValue();
                                        if (!RegExpValidatorUtils.isNumber(event.getValue())) {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_RED);
                                        } else if (Double.parseDouble(event.getValue()) >= minValue && Double.parseDouble(event.getValue()) <= maxValue) {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_GREEN);
                                        } else if ("".equalsIgnoreCase(event.getValue())) {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_WHITE);
                                        } else {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_RED);
                                        }
                                    } else {
                                        if ("ok".equalsIgnoreCase(event.getValue())) {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_GREEN);
                                        } else if ("".equalsIgnoreCase(event.getValue())) {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_WHITE);
                                        } else {
                                            event.getComponent().setStyleName(CoreTheme.BACKGROUND_RED);
                                        }
                                    }
                                }
                            });
                        }
                        tfInspectionValue.setValueChangeTimeout(1000);
                        gridLayout.addComponent(tfInspectionValue, i, j);
                    }
                }
            }
        }
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
    }

    //在PdfFormField位置添加文本，并删除PdfFormField
    public void text2Formfield(com.itextpdf.layout.Document doc, PdfAcroForm form, String fieldName, String text) {
        System.out.println("----------------writing field:" + fieldName);
        PdfFormField field = form.getField(fieldName);
        Paragraph paragraph = new Paragraph(text);
        PdfFont font = field.getFont();
        paragraph.setFont(font);
        float fontsize = 10.0F;
        int pageNumber = doc.getPdfDocument().getPageNumber(field.getWidgets().get(0).getPage());
        PdfArray position = field.getWidgets().get(0).getRectangle();
        float x = (float) position.getAsNumber(0).getValue();
        float y = (float) position.getAsNumber(1).getValue();
        float width = (float) (position.getAsNumber(2).getValue() - position.getAsNumber(0).getValue());
        for (; fontsize >= 1.0F; fontsize--) {
            if (font.getWidth(text, fontsize) <= width) {
                break;
            }
        }
        paragraph.setFontSize(fontsize);
        paragraph.setFixedPosition(pageNumber, x, y, width);
        doc.add(paragraph);
        form.removeField(fieldName);
//        PdfFormField field = form.getField(fieldName);
//        field.setValue(text);
//        field.setReadOnly(true);
    }

    //在PdfFormField位置添加图片，并删除PdfFormField
    public void img2Formfield(com.itextpdf.layout.Document doc, PdfAcroForm form, String fieldName, Media media) throws IOException {
        PdfFormField field = form.getField(fieldName);
        ImageData imageData = ImageDataFactory.create(inputStream2byte(media.getMediaStream()));
        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
        int pageNumber = doc.getPdfDocument().getPageNumber(field.getWidgets().get(0).getPage());
        PdfArray position = field.getWidgets().get(0).getRectangle();
        float x = (float) position.getAsNumber(0).getValue();
        float y = (float) position.getAsNumber(1).getValue();
        float width = (float) (position.getAsNumber(2).getValue() - position.getAsNumber(0).getValue());
        float height = (float) (position.getAsNumber(3).getValue() - position.getAsNumber(1).getValue());
        image.scaleToFit(width, height);
        image.setFixedPosition(pageNumber, x, y);
        doc.add(image);
        form.removeField(fieldName);
    }
}
