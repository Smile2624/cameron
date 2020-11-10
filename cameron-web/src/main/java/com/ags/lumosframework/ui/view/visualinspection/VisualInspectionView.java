package com.ags.lumosframework.ui.view.visualinspection;

import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.domain.User;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult.Result;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
import java.util.*;

@Menu(caption = "VisualInspection", captionI18NKey = "view.visualinspection.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 0)
@SpringView(name = "VisualInspection", ui = CameronUI.class)
public class VisualInspectionView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    private ComboBox<String> cbPurchasingNo = new ComboBox();

    @I18Support(caption = "Confirm", captionKey = "common.confirm")
    private Button btnConfirm = new Button();

    private ComboBox<User> cbWitness = new ComboBox<>();

    @I18Support(caption = "MaterialNo", captionKey = "view.dimensioninspection.materialno")
    private LabelWithSamleLineCaption lblMaterialNo = new LabelWithSamleLineCaption();

    @I18Support(caption = "MaterialRev", captionKey = "view.dimensioninspection.materialrev")
    private LabelWithSamleLineCaption lblMaterialRev = new LabelWithSamleLineCaption();

    @I18Support(caption = "SN", captionKey = "view.receivinginspection.sn")
    private TextField lblSN = new TextField();

    @I18Support(caption = "MaterialDesc", captionKey = "view.dimensioninspection.materialdesc")
    private LabelWithSamleLineCaption lblMaterialDesc = new LabelWithSamleLineCaption();

    @I18Support(caption = "QualityPlan", captionKey = "view.dimensioninspection.qualityplan")
    private LabelWithSamleLineCaption lblQualityPlan = new LabelWithSamleLineCaption();

    @I18Support(caption = "PSL", captionKey = "view.receivinginspection.psl")
    private TextField lblPSL = new TextField();

    private Grid<PurchasingOrderInfo> gridObject = new Grid<>();

    private GridLayout gridLayout = new GridLayout();

    VerticalLayout hlToolBox = new VerticalLayout();

    Panel inspectionValue = new Panel();

    VerticalLayout vlDisplay = new VerticalLayout();

    HorizontalLayout hlTemp1 = new HorizontalLayout();
    HorizontalLayout hlTemp2 = new HorizontalLayout();

    private AbstractComponent[] components = new AbstractComponent[]{cbPurchasingNo, cbWitness, btnConfirm};

    List<DimensionRuler> listInstance = null;

    @Autowired
    private IPurchasingOrderService purchasingOrderService;

    @Autowired
    private ISparePartService sparePartService;

    @Autowired
    private ICaMediaService caMediaService;

    @Autowired
    private UserService userService;

    @Autowired
    private IVisualInspectionService visualInspectionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICaConfigService caConfigService;


    private CaConfig caConfig;
    // x,y 当前数据输入位置
    // 选中的checkBox序号
    private String materialNo = "";
    private String materialRev = "";

    StringBuilder returnMessage = new StringBuilder();
    SocketClient client = null;

    PurchasingOrderInfo purchasingOrderInfo = null;

    private List<String> inspectionItems = new ArrayList<>();

    private static final int wdFormatPDF = 17;// PDF 格式

    private boolean isChecked = false;

    private boolean canInspection = true;

    private String loginUserName = "";
    List<Role> role = null;

    public VisualInspectionView() {
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
//        btnStart.setIcon(VaadinIcons.START_COG);
//        btnStart.setEnabled(false);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);
        cbPurchasingNo.setPlaceholder("采购单号");
        cbWitness.setPlaceholder("见证人");
        cbPurchasingNo.addValueChangeListener(new ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                if (!event.getValue().equals(event.getOldValue())) {
                    String purchasingNo = cbPurchasingNo.getValue().trim();
                    if (!Strings.isNullOrEmpty(purchasingNo)) {
                        List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
                                .getUncheckedOrder(purchasingNo, "VISUAL");
                        if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                            // 数据填入grid
                            gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
                        } else {
                            NotificationUtils
                                    .notificationError(I18NUtility.getValue("view.receivinginspection.purchasingnotexist",
                                            " PurchasingOrder: " + purchasingNo
                                                    + " not exist."));
                        }
                    }
                }
            }
        });
        vlRoot.addComponent(vlDisplay);
        vlDisplay.setWidth("100%");
        vlDisplay.setMargin(true);
        vlDisplay.addComponent(hlTemp1);
        vlDisplay.addComponent(hlTemp2);
        hlTemp1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp2.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp1.setWidth("100%");
        hlTemp2.setWidth("100%");
        hlTemp1.addComponent(lblMaterialNo);
        hlTemp1.addComponent(lblMaterialRev);
        hlTemp1.addComponent(lblMaterialDesc);
        hlTemp2.addComponent(lblSN);
        hlTemp2.addComponent(lblQualityPlan);
        hlTemp2.addComponent(lblPSL);
        initList();
        btnConfirm.setEnabled(false);
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
                .setWidth(130.0);
        gridObject.addColumn(PurchasingOrderInfo::getMaterialQuantity)
                .setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "Quantity"))
                .setWidth(80.0);
        gridObject.addSelectionListener(new SelectionListener<PurchasingOrderInfo>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SelectionEvent<PurchasingOrderInfo> event) {
                isChecked = false;
                btnConfirm.setEnabled(true);
                initComponent();
                TextField tfPackingRlt = (TextField) gridLayout.getComponent(1, 1);
                TextField tfRawMaterialRlt = (TextField) gridLayout.getComponent(1, 2);
                TextField tfMachinedSurfaceRlt = (TextField) gridLayout.getComponent(1, 3);
                TextField tfNonmetalRlt = (TextField) gridLayout.getComponent(1, 4);
                TextField tfCoatingRlt = (TextField) gridLayout.getComponent(1, 5);
                TextField tfOthersRlt = (TextField) gridLayout.getComponent(1, 6);
                TextArea tfComment = (TextArea) gridLayout.getComponent(1, 7);
                if (event.getFirstSelectedItem().isPresent()) {
                    btnConfirm.setEnabled(true);
                    purchasingOrderInfo = event.getFirstSelectedItem().get();
                    materialNo = purchasingOrderInfo.getMaterialNo();
                    materialRev = purchasingOrderInfo.getMaterialRev();
                    setDataToDisplayArea(purchasingOrderInfo);
                    VisualInspection visualInspection = visualInspectionService
                            .getBySapLotNo(purchasingOrderInfo.getSapInspectionLot());
                    if (visualInspection == null) {
                        SparePart part = sparePartService.getByNoRev(materialNo, materialRev);
                        if (part == null) {
                            NotificationUtils.notificationError("系统没有维护零件:" + materialNo + "的相关信息，请先维护零件信息");
                            canInspection = false;
                            return;
                        }
                        lblQualityPlan.setValue(part.getQaPlan() + "/" + part.getQaPlanRev());
                        canInspection = true;
                        lblSN.setValue("");
                        lblPSL.setValue("");
                    } else {
                        isChecked = true;
                        lblSN.setValue(visualInspection.getSN() == null ? "" : visualInspection.getSN());
                        lblPSL.setValue(visualInspection.getPsl() == null ? "" : visualInspection.getPsl());
                        tfPackingRlt.setValue(visualInspection.getPackingResult());
                        tfPackingRlt.setEnabled(false);
                        tfRawMaterialRlt.setValue(visualInspection.getRawMaterialResult());
                        tfRawMaterialRlt.setEnabled(false);
                        tfMachinedSurfaceRlt.setValue(visualInspection.getMachinedSurfaceResult());
                        tfMachinedSurfaceRlt.setEnabled(false);
                        tfNonmetalRlt.setValue(visualInspection.getNonmetalPartsResult());
                        tfNonmetalRlt.setEnabled(false);
                        tfCoatingRlt.setValue(visualInspection.getCoatingResult());
                        tfCoatingRlt.setEnabled(false);
                        tfOthersRlt.setValue(visualInspection.getOtherResult());
                        tfCoatingRlt.setEnabled(false);
                        tfComment.setValue(visualInspection.getComment());
                    }
                } else {
                    btnConfirm.setEnabled(false);
                    setDataToDisplayArea(null);
                    purchasingOrderInfo = null;
                    materialNo = "";
                    materialRev = "";
                }
            }

        });
        hlSplitPanel.setFirstComponent((Component) gridObject);
        inspectionValue.setSizeFull();
        inspectionValue.setContent(gridLayout);
        gridLayout.setWidth("100%");
        gridLayout.setHeight("100%");
        gridLayout.setRows(10);
        gridLayout.setColumns(3);
        gridLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        // 初始化第一行，标题
        gridLayout.addComponent(new Label("检验项目"), 0, 0);
        gridLayout.addComponent(new Label("检验结果"), 1, 0);
        for (int rowIndex = 1; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
                if (columnIndex == 0) {
                    Label lblItem = new Label();
                    lblItem.setValue(inspectionItems.get(rowIndex - 1));
                    lblItem.addStyleNames(CoreTheme.FONT_PRIMARY, CoreTheme.JASPER_BORDER);
                    gridLayout.addComponent(lblItem, columnIndex, rowIndex);
                    gridLayout.setComponentAlignment(lblItem, Alignment.MIDDLE_LEFT);
                } else {
                    TextField tfResult = new TextField();
                    tfResult.setSizeFull();
                    tfResult.addValueChangeListener(new ValueChangeListener<String>() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public void valueChange(ValueChangeEvent<String> event) {
                            if ("ok".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_GREEN);
                                tfResult.setValue("OK");
                            } else if ("na".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_ORANGE);
                                tfResult.setValue("NA");
                            } else if ("".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_WHITE);
                            } else {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_RED);
                            }
                        }
                    });
                    if (rowIndex == 7) {
                        TextArea taComment = new TextArea();
                        taComment.setSizeFull();
                        taComment.setHeight("50");
                        gridLayout.addComponent(taComment, columnIndex, rowIndex);
                    } else {
                        gridLayout.addComponent(tfResult, columnIndex, rowIndex);
                    }

                }
            }
        }

        hlSplitPanel.setSecondComponent((Component) inspectionValue);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        loginUserName = RequestInfo.current().getUserName();
        role = userService.getByName(loginUserName).getRole();
        List<User> users = userService.getByNameLazzy("");
        cbWitness.setItems(users);
        cbWitness.setItemCaptionGenerator(user -> user.getLastName() + " " + user.getFirstName());
    }

    public void setDataToDisplayArea(PurchasingOrderInfo purchasingOrderInfo) {
        if (purchasingOrderInfo == null) {
            // 清空显示区的信息
            lblMaterialNo.clear();
            lblMaterialRev.clear();
            lblMaterialDesc.clear();
            lblSN.clear();
            lblQualityPlan.clear();
            lblPSL.clear();
        } else {
            // 填充信息
            String materialNo = purchasingOrderInfo.getMaterialNo();
            String materialRev = purchasingOrderInfo.getMaterialRev();
            String materialDesc = purchasingOrderInfo.getMaterialDesc();
            String quality = "";
            // 获取零件信息，并得到质量计划和图纸编号

            lblMaterialNo.setValue(materialNo);
            lblMaterialRev.setValue(materialRev);
            lblMaterialDesc.setValue(materialDesc);
            lblQualityPlan.setValue(quality);
        }
    }

    public void createReport(VisualInspection visualInspection, String path) {
        path = path + AppConstant.MATERIAL_PREFIX + AppConstant.VISUAL_REPORT;
        List<File> fileList = new ArrayList<>();
        try {
            SparePart part = sparePartService.getByNoRev(materialNo, materialRev);
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("partNo", purchasingOrderInfo.getMaterialNo());
            dataMap.put("rev", purchasingOrderInfo.getMaterialRev());
            dataMap.put("poNo",
                    purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
            dataMap.put("desc", purchasingOrderInfo.getMaterialDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
            dataMap.put("qpRev", part.getQaPlan() + "/" + part.getQaPlanRev());
            dataMap.put("qty", purchasingOrderInfo.getMaterialQuantity());
            dataMap.put("api", "6A");
            dataMap.put("psl", lblPSL.getValue());
            dataMap.put("sn", lblSN.getValue());
            dataMap.put("packingRlt", visualInspection.getPackingResult());
            dataMap.put("rawmaterialRlt", visualInspection.getRawMaterialResult());
            dataMap.put("machinedRlt", visualInspection.getMachinedSurfaceResult());
            dataMap.put("nonmetalRlt", visualInspection.getNonmetalPartsResult());
            dataMap.put("coatingRlt", visualInspection.getCoatingResult());
            dataMap.put("otherRlt", visualInspection.getOtherResult());
            dataMap.put("comment", visualInspection.getComment());
            dataMap.put("date", visualInspection.getQcConfirmDate());
//			dataMap.put("date", new SimpleDateFormat("yyyy/MMdd").format(new Date()));

            BASE64Encoder encoder = new BASE64Encoder();
            Media qcmediaImage = caMediaService.getByTypeName("ES", visualInspection.getQcChecker());
            if (qcmediaImage == null) {
                NotificationUtils.notificationError("当前没有配置:" + visualInspection.getQcChecker() + "的电子签名，请首先配置该用户的电子签名");
                return;
            }
            dataMap.put("qcChecker", encoder.encode(inputStream2byte(qcmediaImage.getMediaStream())));

            Media qcWitnessMediaImage = caMediaService.getByTypeName("ES", visualInspection.getQcWitness());
            if (qcWitnessMediaImage == null) {
                NotificationUtils.notificationError("当前没有配置:" + visualInspection.getQcWitness() + "的电子签名，请首先配置该用户的电子签名");
                return;
            }
            dataMap.put("qcWitness", encoder.encode(inputStream2byte(qcWitnessMediaImage.getMediaStream())));


            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            String jPath = AppConstant.DOC_XML_FILE_PATH;
            configuration.setDirectoryForTemplateLoading(new File(jPath));
            // 以utf-8的编码读取模板文件
            Template template = configuration.getTemplate("visual.xml", "utf-8");

            // 输出文件
            String fileName = path + purchasingOrderInfo.getPurchasingNo() + "-"
                    + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot()
                    + ".doc";
            File outFile = new File(fileName);
            // 将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")), 1024 * 1024);
            template.process(dataMap, out);
            out.flush();
            out.close();
            fileList.add(outFile);
            String overFile = path + purchasingOrderInfo.getPurchasingNo() + "-"
                    + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf";
            //word2Pdf(fileName, overFile);
            fileConvertToPDF(fileName, path);
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

    public void initList() {
        inspectionItems.add("包装与防护");
        inspectionItems.add("原材料外观检验");
        inspectionItems.add("机加工外观检验");
        inspectionItems.add("非金属件外观检验");
        inspectionItems.add("涂层外观检验");
        inspectionItems.add("其他外观检验");
        inspectionItems.add("备注");
    }

    public void initComponent() {
        Iterator<Component> iter = gridLayout.getComponentIterator();
        while (iter.hasNext()) {
            Component c = iter.next();
            if (c instanceof TextField) {
                ((TextField) c).setEnabled(true);
                ((TextField) c).setValue("");
            }
            if (c instanceof TextArea) {
                ((TextArea) c).setEnabled(true);
                ((TextArea) c).setValue("");
            }
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        if (button.equals(btnConfirm)) {
            caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (caConfig == null) {
                NotificationUtils.notificationError("请先配置报告存放的根目录!");
                return;
            }
            String rootPath = caConfig.getConfigValue();
            if (!canInspection) {
                NotificationUtils.notificationError("当前零件不可检验，请确认是否维护相关信息");
                canInspection = true;
                return;
            }
            Media mediaImage = caMediaService.getByTypeName("ES", RequestInfo.current().getUserName());
            if (mediaImage == null) {
                NotificationUtils.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
                return;
            }
            if (isChecked) {
                NotificationUtils.notificationInfo("检验已经完成，请勿重复提交");
                return;
            }
            String packingRlt = ((TextField) gridLayout.getComponent(1, 1)).getValue();
            String rawmaterialRlt = ((TextField) gridLayout.getComponent(1, 2)).getValue();
            String machinedRlt = ((TextField) gridLayout.getComponent(1, 3)).getValue();
            String nonmetalRlt = ((TextField) gridLayout.getComponent(1, 4)).getValue();
            String coatingRlt = ((TextField) gridLayout.getComponent(1, 5)).getValue();
            String othersRlt = ((TextField) gridLayout.getComponent(1, 6)).getValue();
            String comment = ((TextArea) gridLayout.getComponent(1, 7)).getValue();
            if (Strings.isNullOrEmpty(packingRlt) || Strings.isNullOrEmpty(rawmaterialRlt)
                    || Strings.isNullOrEmpty(machinedRlt) || Strings.isNullOrEmpty(nonmetalRlt)
                    || Strings.isNullOrEmpty(coatingRlt) || Strings.isNullOrEmpty(othersRlt)) {
                ConfirmDialog.show(getUI(), "仍有需要填写信息未填写，是否暂时保存？", new DialogCallBack() {
                    @Override
                    public void done(ConfirmResult result) {
                        if (result.getResult().equals(Result.OK)) {
                            VisualInspection inspectionInstance = new VisualInspection();
                            inspectionInstance.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                            inspectionInstance.setPurchasingOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                            inspectionInstance.setPurchasingOrder(purchasingOrderInfo.getPurchasingNo());
                            inspectionInstance
                                    .setQcConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                            inspectionInstance.setQcChecker(RequestInfo.current().getUserName());
                            inspectionInstance.setPackingResult(packingRlt == null ? "" : packingRlt);
                            inspectionInstance.setRawMaterialResult(rawmaterialRlt == null ? "" : rawmaterialRlt);
                            inspectionInstance.setMachinedSurfaceResult(machinedRlt == null ? "" : machinedRlt);
                            inspectionInstance
                                    .setNonmetalPartsResult(nonmetalRlt == null ? "" : nonmetalRlt);
                            inspectionInstance.setCoatingResult(coatingRlt == null ? "" : coatingRlt);
                            inspectionInstance.setOtherResult(othersRlt == null ? "" : othersRlt);
                            inspectionInstance.setComment(comment == null ? "" : comment);
                            inspectionInstance.setPsl(lblPSL.getValue());
                            inspectionInstance.setSN(lblSN.getValue());
                            visualInspectionService.save(inspectionInstance);
                        }
                    }
                });
            } else {
                if (cbWitness.isEmpty()) {
                    NotificationUtils.notificationError("请选择见证人");
                    return;
                }
                ConfirmDialog.show(getUI(), "我们证明上述描述的项目已经被检验并符合订单条款", new DialogCallBack() {
                    @Override
                    public void done(ConfirmResult result) {
                        if (result.getResult().equals(Result.OK)) {
                            VisualInspection inspectionInstance = new VisualInspection();
                            inspectionInstance.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                            inspectionInstance.setPurchasingOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                            inspectionInstance.setPurchasingOrder(purchasingOrderInfo.getPurchasingNo());
                            inspectionInstance
                                    .setQcConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                            inspectionInstance.setQcChecker(RequestInfo.current().getUserName());
                            inspectionInstance.setPackingResult(packingRlt == null ? "" : packingRlt);
                            inspectionInstance.setRawMaterialResult(rawmaterialRlt == null ? "" : rawmaterialRlt);
                            inspectionInstance.setMachinedSurfaceResult(machinedRlt == null ? "" : machinedRlt);
                            inspectionInstance
                                    .setNonmetalPartsResult(nonmetalRlt == null ? "" : nonmetalRlt);
                            inspectionInstance.setCoatingResult(coatingRlt == null ? "" : coatingRlt);
                            inspectionInstance.setOtherResult(othersRlt == null ? "" : othersRlt);
                            inspectionInstance.setComment(comment == null ? "" : comment);
                            inspectionInstance.setPsl(lblPSL.getValue());
                            inspectionInstance.setSN(lblSN.getValue());
                            inspectionInstance.setQcWitness(cbWitness.getValue().getName());
                            visualInspectionService.save(inspectionInstance);
                            createReport(inspectionInstance, rootPath);
                            purchasingOrderInfo.setVisualChecked(true);
                            if (("OK".equalsIgnoreCase(inspectionInstance.getPackingResult()) || "NA".equalsIgnoreCase(inspectionInstance.getPackingResult()))
                                    && ("OK".equalsIgnoreCase(inspectionInstance.getRawMaterialResult()) || "NA".equalsIgnoreCase(inspectionInstance.getRawMaterialResult()))
                                    && ("OK".equalsIgnoreCase(inspectionInstance.getMachinedSurfaceResult()) || "NA".equalsIgnoreCase(inspectionInstance.getMachinedSurfaceResult()))
                                    && ("OK".equalsIgnoreCase(inspectionInstance.getNonmetalPartsResult()) || "NA".equalsIgnoreCase(inspectionInstance.getNonmetalPartsResult()))
                                    && ("OK".equalsIgnoreCase(inspectionInstance.getCoatingResult()) || "NA".equalsIgnoreCase(inspectionInstance.getCoatingResult()))
                                    && ("OK".equalsIgnoreCase(inspectionInstance.getOtherResult()) || "NA".equalsIgnoreCase(inspectionInstance.getOtherResult()))) {
                                purchasingOrderInfo.setVisualCheckedRlt("OK");
                            } else {
                                purchasingOrderInfo.setVisualCheckedRlt("NG");
                            }
                            purchasingOrderService.save(purchasingOrderInfo);
                            purchasingOrderInfo = null;
                            cbWitness.clear();
                        }
                        NotificationUtils.notificationInfo("成功生成检验报告");
                        refesh();
                    }
                });
            }
        }
    }

    public void fileConvertToPDF(String filePath, String path) {
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            // 转换前的文件路径
            String startFile = filePath;
            // 转换后的文件路径
            String overFile = path + purchasingOrderInfo.getPurchasingNo() + "-"
                    + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf";

            doc = Dispatch.call(docs, "Open", startFile).toDispatch();
            File tofile = new File(overFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", overFile, wdFormatPDF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        // 结束后关闭进程
        ComThread.Release();
    }

    public void refesh() {
        cbPurchasingNo.setItems(getPurchasingOrder("VISUAL"));
        String purchasingNo = cbPurchasingNo.getValue().trim();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService.getUncheckedOrder(purchasingNo, "VISUAL");
            if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                // 数据填入grid
                gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
            } else {
                cbPurchasingNo.setValue("");
                listPurchasingOrderInfo.clear();
                gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
//                NotificationUtils.notificationError(I18NUtility.getValue(
//                        "view.receivinginspection.purchasingnotexist", "Purchasing No Not Exist ."));
            }
        } else {
            NotificationUtils.notificationError(
                    I18NUtility.getValue("view.receivinginspection.purchasingnotnull", "PurchasingNo can't be null"));
        }
    }

    public List<String> getPurchasingOrder(String type) {
        return purchasingOrderService.getPurchasingNo(type);
    }

    @Override
    public void _init() {
        cbPurchasingNo.setItems(getPurchasingOrder("VISUAL"));
    }
}
