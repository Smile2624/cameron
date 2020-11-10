//Changed by Cameron: 允许QC修改覆盖结果，测试邮件通知功能（未使用）

package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.common.exception.PlatformException;
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
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
import com.google.common.base.Strings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.vaadin.data.HasValue;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Menu(caption = "QCInspection", captionI18NKey = "view.QCInspection.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 7)
@SpringView(name = "QCInspection", ui = {CameronUI.class})
public class QCInspectionView extends BaseView implements Button.ClickListener {


    //    private TextField tfOrderNo = new TextField();
    private final ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    //具体字段意义参考最终检验报告模板
    @I18Support(caption = "Assy.No", captionKey = "view.qcinspection.assyno")    //部件号 Assy. No
    private final LabelWithSamleLineCaption tfAssyNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Confirm", captionKey = "view.qcinspection.confirm")
    private final Button btnConfirm = new Button();
    @I18Support(caption = "Start", captionKey = "common.start")
    private final Button btnStart = new Button();
    @I18Support(caption = "LEVP", captionKey = "view.qcinspection.levp")
    private final Button btnLevp = new Button();
    @I18Support(caption = "Sub-WO", captionKey = "view.qcinspection.subwo")
    private final Button btnSubWo = new Button();
    //    @I18Support(caption = "ProductRev", captionKey = "view.qcinspection.productrev")//版本号
//    private final LabelWithSamleLineCaption tfProductRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "AssySerialNo", captionKey = "view.qcinspection.assyserialno")//装配件序列号
    private final LabelWithSamleLineCaption tfAssySerialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "view.qcinspection.description")//描述
    private final LabelWithSamleLineCaption tfDescription = new LabelWithSamleLineCaption();
    //    @I18Support(caption = "OrderNo", captionKey = "view.qcinspection.orderno")//订单号
//    private final LabelWithSamleLineCaption tfProductionNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Quality Plan No", captionKey = "view.qcinspection.qpno")//质量计划
    private final LabelWithSamleLineCaption tfQpNo = new LabelWithSamleLineCaption();
    //    @I18Support(caption = "Quality Plan Rev", captionKey = "view.qcinspection.QpRev")//质量计划版本
//    private final LabelWithSamleLineCaption tfQpRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "SalesNo", captionKey = "view.qcinspection.salesno")//销售订单号
    private final LabelWithSamleLineCaption tfSalesNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "FactoryCode", captionKey = "view.qcinspection.factorycode")//工厂代码
    private final LabelWithSamleLineCaption tfFactoryCode = new LabelWithSamleLineCaption();

    private final ComboBox<String> cbWorkshop = new ComboBox<>();//车间
    private final TextField tfReportNo = new TextField();//报告编号

    private final AbstractComponent[] components = new AbstractComponent[]{cbOrder, btnStart, btnConfirm, btnLevp, btnSubWo};
    private final AbstractComponent[] disPlayComponents1 = new AbstractComponent[]{tfAssyNo, tfAssySerialNo, cbWorkshop};//tfProductRev,tfProductionNo
    private final AbstractComponent[] disPlayComponents2 = new AbstractComponent[]{tfQpNo, tfFactoryCode, tfReportNo};//tfQpRev
    private final HorizontalLayout hlToolBox = new HorizontalLayout();
    private final HorizontalLayout hlDisplayBox = new HorizontalLayout();
    private final VerticalLayout vlBox1 = new VerticalLayout();
    private final VerticalLayout vlBox2 = new VerticalLayout();
    private final VerticalLayout vlBox3 = new VerticalLayout();
    //    private final Grid<FinalInspectionItems> objectGrid = new Grid<FinalInspectionItems>();
    private final GridLayout glLayout = new GridLayout(3, 11);
    Panel panel = new Panel();
    List<Role> role = null;
    StringBuilder returnMessage = new StringBuilder();
    SocketClient client = null;
    boolean isInputOk = false;
    String errorMessage = "";
    @I18Support(caption = "Customer", captionKey = "view.qcinspection.customer")//客户
    private LabelWithSamleLineCaption tfCustomer = new LabelWithSamleLineCaption();
    private final AbstractComponent[] disPlayComponents3 = new AbstractComponent[]{tfCustomer, tfSalesNo, tfDescription};
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IOrderHistoryService orderHistoryService;
    @Autowired
    private IFinalInspectionItemsService finalInspectionItemsService;
    @Autowired
    private IFinalInspectionResultService finalInspectionResultService;
    @Autowired
    private OrderOperationInfoDialog orderOperationInfoDialog;
    @Autowired
    private InputPaintThicknessDialog inputPaintThicknessDialog;
    @Autowired
    private FunctionInspectionDialog functionInspectionDialog;
    @Autowired
    private IAppearanceInstrumentationResultService appearanceInstrumentationResultService;
    @Autowired
    private IFunctionDetectionResultService functionDetectionResultService;
    @Autowired
    private IPressureTestService pressureTestService;
    @Autowired
    private AssemblingInfoDialog assemblingInfoDialog;
    //    private String orderNo;
    @Autowired
    private PressureTestDialog_ pressureTestDialog;
    @Autowired
    private SubOrderSelectDialog subOrderSelectDialog;
    @Autowired
    private DimensionInspectionDialog dimensionInspectionDialog;
    @Autowired
    private HardnessTestReportDialog hardnessTestReportDialog;
    @Autowired
    private UserService userService;
    @Autowired
    private IRoleService roleService;
    //	private GridLayout gridLayout = new GridLayout();
    //	private GridLayout gridLayout = new GridLayout();
    private FinalInspectionResult finalInspectionResult = new FinalInspectionResult();
    private List<FinalInspectionResult> finalInspectionResultSavedList;
    private List<String> columnsList;
    private String loginUserName = "";
    // x,y 当前数据输入位置
    private int x = 0;
    private int y = 0;
    private String ipAddress = "";
    private String prefixSend = "";// 当前发送的语音指令，用于判断返回信息并执行什么操作

    private List<ProductionOrder> subWo;

    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private ICaMediaService caMediaService;

    public QCInspectionView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        hlDisplayBox.setWidth("100%");
        hlDisplayBox.addStyleName(CoreTheme.SIDE_MENU);
        hlDisplayBox.setMargin(true);
        vlRoot.addComponent(hlDisplayBox);
        vlBox1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        vlBox2.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        vlBox3.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlDisplayBox.addComponent(vlBox1);
        hlDisplayBox.addComponent(vlBox2);
        hlDisplayBox.addComponent(vlBox3);
//        tfOrderNo.setPlaceholder("工单号");
        cbOrder.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductOrderId", "ProductOrderId"));
//        tfSalesNo.setPlaceholder("请输入销售单号");
//        tfCustomer.setPlaceholder("请输入客户代码");
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);
        btnStart.setIcon(VaadinIcons.START_COG);
        btnStart.addClickListener(this);
        btnStart.setVisible(false);
        btnLevp.setIcon(VaadinIcons.FILE_TEXT_O);
        btnLevp.addClickListener(this);
        btnLevp.setVisible(false);
        btnSubWo.setIcon(VaadinIcons.FILE_TREE_SUB);
        btnSubWo.addClickListener(this);
        btnSubWo.setVisible(false);
        cbWorkshop.setItems("#4", "#5", "#6");
        cbWorkshop.setCaption("选择车间: ");
        cbWorkshop.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> {
            if (finalInspectionResultService.getByOrderNo(cbOrder.getSelectedItem().get().getProductOrderId()) != null && finalInspectionResultService.getByOrderNo(cbOrder.getSelectedItem().get().getProductOrderId()).getReportNo() != null) {
                tfReportNo.setValue(finalInspectionResultService.getByOrderNo(cbOrder.getSelectedItem().get().getProductOrderId()).getReportNo());
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String date = LocalDate.now().format(formatter);
            if (event.getValue() != null) {
                switch (event.getValue()) {
                    case "#4":
                        if (finalInspectionResultService.getByReportNo(date).size() > 0) {
                            tfReportNo.setValue(date + "-" +
                                    String.format("%04d", Integer.parseInt(finalInspectionResultService.getByReportNo(date).get(0).getReportNo().substring(8)) + 1));
                        } else {
                            tfReportNo.setValue(date + "-0001");
                        }
                        break;
                    case "#5":
                        if (finalInspectionResultService.getByReportNo("E" + date).size() > 0) {
                            tfReportNo.setValue("E" + date + "-" +
                                    String.format("%04d", Integer.parseInt(finalInspectionResultService.getByReportNo("E" + date).get(0).getReportNo().substring(9)) + 1));
                        } else {
                            tfReportNo.setValue("E" + date + "-0001");
                        }
                        break;
                    case "#6":
                        if (finalInspectionResultService.getByReportNo("M" + date).size() > 0) {
                            tfReportNo.setValue("M" + date + "-" +
                                    String.format("%04d", Integer.parseInt(finalInspectionResultService.getByReportNo("M" + date).get(0).getReportNo().substring(9)) + 1));
                        } else {
                            tfReportNo.setValue("M" + date + "-0001");
                        }
                        break;
                }
            } else {
                tfReportNo.clear();
            }
        });
        tfReportNo.setCaption("报告编号: ");
        for (Component component : components) {
            hlTempToolBox.addComponent(component);
        }
        for (Component component : disPlayComponents1) {
            vlBox1.addComponent(component);
        }
        for (Component component : disPlayComponents2) {
            vlBox2.addComponent(component);
        }
        for (Component component : disPlayComponents3) {
            vlBox3.addComponent(component);
        }
        tfDescription.setWidthUndefined();
        cbOrder.setEmptySelectionAllowed(false);
        cbOrder.addSelectionListener(new SingleSelectionListener<ProductionOrder>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<ProductionOrder> event) {
                if (event.getValue() == null || "".equals(event.getValue())) {
                    initLabel();
                } else {
                    initLabel();
                    ProductionOrder inputProductionOrder = event.getValue();
                    finalInspectionResultSavedList = finalInspectionResultService.getFinalInspectionResultByOrderNo(event.getValue().getProductOrderId(), "QC");
                    if (inputProductionOrder != null) {
                        //判断工单是否锁定
                        if(inputProductionOrder.getBomLockFlag()){
                            throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                                    "This Order is locked,Please contact the engineer to solve !"));
                        }

                        finalInspectionResult = finalInspectionResultService.getByOrderNo(inputProductionOrder.getProductOrderId());
                        if (finalInspectionResult == null) {
                            finalInspectionResult = new FinalInspectionResult();
                        }
                        //获取是否有LEVP
                        ProductInformation productInformation = productInformationService.getByNoRev(inputProductionOrder.getProductId(), inputProductionOrder.getProductVersionId());
                        if (!(productInformation.getLevp() == null || productInformation.getLevp().equals(""))) {
                            btnLevp.setVisible(true);
                        }
                        //获取是否有子工单
                        subWo = productionOrderService.getBySuperiorOrder(inputProductionOrder.getProductOrderId());
                        if (subWo != null && subWo.size() > 0) {
                            btnSubWo.setVisible(true);
                        }
                        //根据产品号及版本号，获取质量计划和版本
                        SparePart sparePart = sparePartService
                                .getByNoRev(inputProductionOrder.getProductId(), inputProductionOrder.getProductVersionId());
                        if (sparePart == null) {
                            NotificationUtils.notificationError("工单:" + event.getValue().getProductOrderId() + " 对应的产品:" + inputProductionOrder.getProductId() + " 在系统中没有维护产品信息");
                            return;
                        }
                        tfAssyNo.setValue(inputProductionOrder.getProductId() + " REV" + inputProductionOrder.getProductVersionId());
                        tfAssySerialNo.setValue(String.format("%4d", 1).replace(" ", "0") + "-"
                                + String.format("%4d", event.getValue().getProductNumber()).replace(" ", "0"));
                        tfDescription.setValue(inputProductionOrder.getProductDesc());
                        tfQpNo.setValue(sparePart.getQaPlan() + " REV" + sparePart.getQaPlanRev());//sparePart.getQaPlanRev().split("/")[0]
                        tfFactoryCode.setValue("A419");
                        tfReportNo.setValue(finalInspectionResult == null ? "" :
                                (finalInspectionResult.getReportNo() == null ? "" : finalInspectionResult.getReportNo()));
                        String[] salesOrder = inputProductionOrder.getSalesOrder() == null ? null : inputProductionOrder.getSalesOrder().split("/");
                        String[] salesOrderItem = inputProductionOrder.getSalesOrderItem() == null ? null : inputProductionOrder.getSalesOrderItem().split("/");
                        if (salesOrder == null || salesOrderItem == null || salesOrder.length == 0 || salesOrderItem.length == 0) {
                            tfSalesNo.setValue("/");
                        } else {
                            String temp = new String();
                            for (int i = 0; i < salesOrder.length && i < salesOrderItem.length; i++) {
                                if (i > 0) {
                                    temp = temp + "/";
                                }
                                temp = temp + salesOrder[i] + "-" + salesOrderItem[i];
                            }
                            tfSalesNo.setValue(temp);
                        }
                        tfCustomer.setValue(inputProductionOrder.getCustomerCode());
                        if (inputProductionOrder.getWorkshop() != null && !inputProductionOrder.getWorkshop().equals("")) {
                            cbWorkshop.setValue(inputProductionOrder.getWorkshop());
                        }
                        //
                        List<FinalInspectionItems> listFinalInspectionItems = finalInspectionItemsService.getAllData();
                        if (listFinalInspectionItems != null && listFinalInspectionItems.size() > 0) {
//							objectGrid.setItems(listFinalInspectionItems);
                            glLayout.removeAllComponents();
                            for (int i = 0; i < listFinalInspectionItems.size(); i++) {
                                FinalInspectionItems finalInspectionItem = listFinalInspectionItems.get(i);

                                Button btnView = new Button();
                                TextField textField = new TextField();
                                //textField.setReadOnly(true);
                                Button btnOK = new Button("OK");
//                                btnOK.setStyleName(CoreTheme.BACKGROUND_GREEN);
                                Button btnNG = new Button("NG");
//                                btnNG.setStyleName(CoreTheme.BACKGROUND_RED);
                                Button btnNA = new Button("NA");

                                //添加第一列
                                glLayout.addComponent(new Label(finalInspectionItem.getInspectionItemName()), 0, i + 1);
                                //添加第二列
                                btnView.setId(finalInspectionItem.getInspectionItemName());
                                btnView.setIcon(VaadinIcons.OPEN_BOOK);
                                btnView.setCaption("查看");
                                glLayout.addComponent(btnView, 1, i + 1);
                                btnView.addClickListener(new ClickListener() {

                                    private static final long serialVersionUID = 753594212447522087L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        Button button = event.getButton();
                                        button.setEnabled(true);
                                        if (button.getId().equals(AppConstant.WORK_ROUTING)) {
                                            //如果是工作路线包检查项，弹出页面，展示工艺路线
//                                            List<OrderRoutingConfirmInfo> orderHistory = orderHistoryService.getByOrderNo(cbOrder.getValue().getProductOrderId());
//                                            orderOperationInfoDialog.setObject(orderHistory);
//                                            orderOperationInfoDialog.refreshData();
//                                            orderOperationInfoDialog.show(getUI(), new DialogCallBack() {
//                                                @Override
//                                                public void done(ConfirmResult result) {
////                                                    if (result.getResult().equals(Result.OK)) {
////                                                        textField.setValue(AppConstant.RESULT_OK);
////                                                    } else {
////                                                        textField.setValue(AppConstant.RESULT_NG);
////                                                    }
//                                                }
//                                            });
                                            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                                            if (caConfig != null) {
                                                if (Strings.isNullOrEmpty(caConfig.getConfigValue())) {
                                                    NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                                                    return;
                                                }
                                            }
                                            String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.GX_RECORD + cbOrder.getValue().getProductOrderId() + ".pdf";
                                            File gxPdfFile = new File(path);
                                            String rootUrl = VaadinUtils.getRootURL();
                                            if (gxPdfFile.isFile()) {
                                                String pdfUrl = rootUrl.replace("cameron", "CameronPDFView");
                                                Page.getCurrent().open(pdfUrl + "viewpdf?filename=" + path,
                                                        "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
                                            } else {
                                                ConfirmDialog.show(getUI(),
                                                        "工序确认PDF不存在，是否前往工序记录页面查看？",
                                                        result -> {
                                                            if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                                                try {
                                                                    VaadinUtils.setPageLocation(rootUrl + "Cameron#!OrderHistory/" + cbOrder.getValue().getProductOrderId());
                                                                } catch (PlatformException e) {
                                                                    notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                                                    return;
                                                                }
                                                            }
                                                        });
                                            }
                                        } else if (button.getId().equals(AppConstant.ASSEMBLING_REPORT)) {
                                            //装配信息需要展示
                                            assemblingInfoDialog.setData(cbOrder.getValue().getProductOrderId());
                                            assemblingInfoDialog.show(getUI(), new DialogCallBack() {

                                                @Override
                                                public void done(ConfirmResult result) {
//                                                    if (result.getResult().equals(Result.OK)) {
//                                                        textField.setValue(AppConstant.RESULT_OK);
//                                                    } else {
//                                                        textField.setValue(AppConstant.RESULT_NG);
//                                                    }
                                                }
                                            });
                                        } else if (button.getId().equals(AppConstant.PRESSURE_TEST_REPORT)) {
                                            //压力测试需要pressureTestDialog
                                            pressureTestDialog.setObject(cbOrder.getValue());
                                            pressureTestDialog.show(getUI(), new DialogCallBack() {

                                                @Override
                                                public void done(ConfirmResult result) {
                                                    if (result.getResult().equals(Result.OK)) {
                                                        textField.setValue(AppConstant.RESULT_OK);
                                                    } else {
                                                        textField.setValue(AppConstant.RESULT_NG);
                                                    }
                                                }
                                            });
                                        } else if (button.getId().equals(AppConstant.DIMENSIONAL_REPORT)) {
                                            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                                            if (caConfig != null) {
                                                if (Strings.isNullOrEmpty(caConfig.getConfigValue())) {
                                                    NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                                                    return;
                                                }
                                            }
                                            String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.DIMENSION_REPORT + cbOrder.getValue().getProductOrderId() + ".pdf";
                                            String rootUrl = VaadinUtils.getRootURL();
                                            String pdfUrl = rootUrl.replace("cameron", "CameronPDFView");
                                            Page.getCurrent().open(pdfUrl + "viewpdf?filename=" + path,
                                                    "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
                                            //尺寸报告显示 dimensionInspectionDialog
//                                            dimensionInspectionDialog.setData(cbOrder.getValue().getProductOrderId());
//                                            dimensionInspectionDialog.show(getUI(), new DialogCallBack() {
//
//                                                @Override
//                                                public void done(ConfirmResult result) {
//                                                    String res = dimensionInspectionDialog.getDimensionInspectionResult();
//                                                    if (AppConstant.RESULT_OK.equals(res)) {
//                                                        textField.setValue(AppConstant.RESULT_OK);
//                                                    } else if (AppConstant.RESULT_NG.equals(res)) {
//                                                        textField.setValue(AppConstant.RESULT_NG);
//                                                    } else if ("".equals(res)) {
//                                                        textField.setValue("");
//                                                    }
//                                                }
//                                            });
                                        } else if (button.getId().equals(AppConstant.HARDNESS_REPORT)) {
                                            //硬度报告显示 hardnessTestReportDialog
                                            hardnessTestReportDialog.setData(cbOrder.getValue().getProductOrderId());
                                            hardnessTestReportDialog.show(getUI(), new DialogCallBack() {

                                                @Override
                                                public void done(ConfirmResult result) {
                                                    String res = hardnessTestReportDialog.getHardnessTestReportItemsResult();
                                                    if (AppConstant.RESULT_OK.equals(res)) {
                                                        textField.setValue(AppConstant.RESULT_OK);
                                                    } else if (AppConstant.RESULT_NG.equals(res)) {
                                                        textField.setValue(AppConstant.RESULT_NG);
                                                    } else if ("".equals(res)) {
                                                        textField.setValue("");
                                                    }
                                                }
                                            });
                                        } else if (button.getId().equals(AppConstant.VISUAL_EXAMINATION)) {
                                            //外观检验时判断是否需要喷漆，如果需要，弹出输入页面，需要输入漆厚度数据
//											List<Assembling> assemblingList = assemblingService.getByOrderNo(orderNo);
                                            List<AppearanceInstrumentationResult> appearanceInstrumentationResultList = appearanceInstrumentationResultService.getByNo(cbOrder.getValue().getProductOrderId());
//											if(appearanceInstrumentationResult==null || appearanceInstrumentationResult.size()<=0) {
                                            ProductionOrder productionOrder = productionOrderService.getByNo(cbOrder.getValue().getProductOrderId());
                                            inputPaintThicknessDialog.setData(productionOrder, appearanceInstrumentationResultList);
                                            inputPaintThicknessDialog.show(getUI(), new DialogCallBack() {

                                                @Override
                                                public void done(ConfirmResult result) {
                                                    if (appearanceInstrumentationResultList == null || appearanceInstrumentationResultList.size() <= 0) {
                                                        if (result.getResult().equals(Result.OK)) {
                                                            if (inputPaintThicknessDialog.getVisualExaminationResult()) {//getVisualExaminationResult()
                                                                textField.setValue(AppConstant.RESULT_OK);
                                                            } else {
                                                                textField.setValue(AppConstant.RESULT_NG);
                                                            }
                                                        } else {
                                                            textField.setValue(AppConstant.RESULT_NG);
                                                        }
                                                    }
                                                }
                                            });
                                        } else if (button.getId().equals(AppConstant.FUNCTION_INSPECTION)) {
                                            //功能检验需要需要输入量具编号以及扭矩测试结果，最多8个
                                            List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(cbOrder.getValue().getProductOrderId());
//											if(functionDetectionResult==null || functionDetectionResult.size()<=0) {
                                            ProductionOrder productionOrder = cbOrder.getValue();
                                            functionInspectionDialog.setData(productionOrder, functionDetectionResultList);
                                            functionInspectionDialog.show(getUI(), new DialogCallBack() {

                                                @Override
                                                public void done(ConfirmResult result) {
                                                    if (functionDetectionResultList == null || functionDetectionResultList.size() <= 0) {
                                                        if (result.getResult().equals(Result.OK)) {
                                                            if (functionInspectionDialog.getFunctionInspectionResult()) {
                                                                textField.setValue(AppConstant.RESULT_OK);
                                                            } else {
                                                                textField.setValue(AppConstant.RESULT_NG);
                                                            }
                                                        } else {
                                                            textField.setValue(AppConstant.RESULT_NG);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                textField.setId(finalInspectionItem.getInspectionItemName());
                                textField.setPlaceholder("请输入检验结果");
                                HorizontalLayout hlLayout = new HorizontalLayout();
                                hlLayout.setSpacing(false);
                                hlLayout.setMargin(false);
                                Label label = new Label(" ");
                                label.setWidth("15px");
                                hlLayout.addComponents(textField, label, btnOK, btnNG, btnNA);
                                glLayout.addComponent(hlLayout, 2, i + 1);
                                btnOK.addClickListener((ClickListener) event1 -> {
                                    textField.setValue("OK");
                                });
                                btnNG.addClickListener((ClickListener) event1 -> {
                                    textField.setValue("NG");
                                });
                                btnNA.addClickListener((ClickListener) event1 -> {
                                    textField.setValue("NA");
                                });
                                textField.addValueChangeListener(new HasValue.ValueChangeListener<String>() {

                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                                        String textValue = textField.getValue().trim();
                                        textField.removeStyleName(textField.getStyleName());

                                        //根据结果修改文本框显示颜色
                                        if (AppConstant.RESULT_OK.equals(textValue)) {
                                            textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                                        } else if (AppConstant.RESULT_NG.equals(textValue)) {
                                            textField.addStyleName(CoreTheme.BACKGROUND_RED);
                                        } else {
                                            textField.addStyleName(CoreTheme.BACKGROUND_WHITE);
                                        }

                                        if (!Strings.isNullOrEmpty(textValue)) {
                                            //针对所有的 文本框 怎么区分？
                                            finalInspectionResult = finalInspectionResultService.getByOrderNo(cbOrder.getValue().getProductOrderId());
                                            if (finalInspectionResult == null) {
                                                finalInspectionResult = new FinalInspectionResult();
                                                finalInspectionResult.setOrderNo(cbOrder.getValue().getProductOrderId());
                                            }
                                            switch (textField.getId()) {
                                                case AppConstant.WORK_ROUTING:
                                                    finalInspectionResult.setRoutePackageResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.ASSEMBLING_REPORT:
                                                    finalInspectionResult.setAssemblingRecorderResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.PRESSURE_TEST_REPORT:
                                                    finalInspectionResult.setPressureTestReportResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.MTR_METRIAL_REPORT:
                                                    finalInspectionResult.setMTRMetrialReportResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.DIMENSIONAL_REPORT:
                                                    finalInspectionResult.setDimensionalReportResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.HARDNESS_REPORT:
                                                    finalInspectionResult.setHardnessReportResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.VISUAL_EXAMINATION:
                                                    finalInspectionResult.setVisualExaminationResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.FUNCTION_INSPECTION:
                                                    finalInspectionResult.setFunctionInspectionResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                                case AppConstant.MONOGRAMMING_STATUS:
                                                    finalInspectionResult.setMonogrammingStatusResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;

                                                default:
                                                    finalInspectionResult.setCommentsResult(textValue);
                                                    finalInspectionResultService.save(finalInspectionResult);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                textField.addBlurListener(new BlurListener() {

                                    private static final long serialVersionUID = -3007674194451711074L;

                                    @Override
                                    public void blur(BlurEvent event) {
                                        String textValue = textField.getValue().trim();
                                        if (!Strings.isNullOrEmpty(textValue)) {
                                            textField.clear();
                                            textField.setValue(textValue);
                                            if (AppConstant.RESULT_OK.equals(textValue)) {
                                                textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                                            } else if (AppConstant.RESULT_NG.equals(textValue)) {
                                                textField.addStyleName(CoreTheme.BACKGROUND_RED);
                                            }
                                        }
                                    }

                                });
                                textField.setValue(finalInspectionItem.getDefaultResult() == null ? "" : finalInspectionItem.getDefaultResult());
                                //QC检测结果
                                if (finalInspectionResultSavedList != null && finalInspectionResultSavedList.size() > 0) {
                                    FinalInspectionResult finalInspectionResult = finalInspectionResultSavedList.get(0);
                                    switch (textField.getId()) {
                                        case AppConstant.WORK_ROUTING:
                                            textField.setValue(finalInspectionResult.getRoutePackageResult() == null ? "" : finalInspectionResult.getRoutePackageResult());
                                            break;
                                        case AppConstant.ASSEMBLING_REPORT:
                                            textField.setValue(finalInspectionResult.getAssemblingRecorderResult() == null ? "" : finalInspectionResult.getAssemblingRecorderResult());
                                            break;
                                        case AppConstant.PRESSURE_TEST_REPORT:
                                            textField.setValue(finalInspectionResult.getPressureTestReportResult() == null ? "" : finalInspectionResult.getPressureTestReportResult());
                                            break;
                                        case AppConstant.MTR_METRIAL_REPORT:
                                            textField.setValue(finalInspectionResult.getMTRMetrialReportResult() == null ? "" : finalInspectionResult.getMTRMetrialReportResult());
                                            break;
                                        case AppConstant.DIMENSIONAL_REPORT:
                                            textField.setValue(finalInspectionResult.getDimensionalReportResult() == null ? "" : finalInspectionResult.getDimensionalReportResult());
                                            break;
                                        case AppConstant.HARDNESS_REPORT:
                                            textField.setValue(finalInspectionResult.getHardnessReportResult() == null ? "" : finalInspectionResult.getHardnessReportResult());
                                            break;
                                        case AppConstant.VISUAL_EXAMINATION:
                                            textField.setValue(finalInspectionResult.getVisualExaminationResult() == null ? "" : finalInspectionResult.getVisualExaminationResult());
                                            break;
                                        case AppConstant.FUNCTION_INSPECTION:
                                            textField.setValue(finalInspectionResult.getFunctionInspectionResult() == null ? "" : finalInspectionResult.getFunctionInspectionResult());
                                            break;
                                        case AppConstant.MONOGRAMMING_STATUS:
                                            textField.setValue(finalInspectionResult.getMonogrammingStatusResult() == null ? "" : finalInspectionResult.getMonogrammingStatusResult());
                                            break;

                                        default:
                                            textField.setValue(finalInspectionResult.getCommentsResult() == null ? "" : finalInspectionResult.getCommentsResult());
                                            break;
                                    }
                                } else {
                                    if (Strings.isNullOrEmpty(finalInspectionItem.getDefaultResult())) {
                                        //QC检测结果没保存   要看外观检测和功能检测有没有保存 有的话显示检测结果
                                        List<AppearanceInstrumentationResult> appearanceInstrumentationResultList = appearanceInstrumentationResultService.getByNo(cbOrder.getValue().getProductOrderId());
                                        List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(cbOrder.getValue().getProductOrderId());
                                        if (AppConstant.VISUAL_EXAMINATION.equals(textField.getId())) {
                                            if (AppConstant.RESULT_OK.equals(checkAppearanceInstrumentationResult(appearanceInstrumentationResultList))) {
                                                textField.setValue(AppConstant.RESULT_OK);
                                            } else if (AppConstant.RESULT_NG.equals(checkAppearanceInstrumentationResult(appearanceInstrumentationResultList))) {
                                                textField.setValue(AppConstant.RESULT_NG);
                                            } else {
                                                textField.setPlaceholder("请输入检验结果");
                                            }
                                        } else if (AppConstant.FUNCTION_INSPECTION.equals(textField.getId())) {
                                            if (AppConstant.RESULT_OK.equals(checkFunctionDetectionResult(functionDetectionResultList))) {
                                                textField.setValue(AppConstant.RESULT_OK);
                                            } else if (AppConstant.RESULT_NG.equals(checkFunctionDetectionResult(functionDetectionResultList))) {
                                                textField.setValue(AppConstant.RESULT_NG);
                                            } else {
                                                textField.setPlaceholder("请输入检验结果");
                                            }
                                        } else if (AppConstant.PRESSURE_TEST_REPORT.equals(textField.getId())) {
                                            List<PressureTest> pressureTest = pressureTestService.getPressureTestByOrder(cbOrder.getValue().getProductOrderId());
                                            if (pressureTest != null && pressureTest.size() > 0) {
                                                if (pressureTest.size() != cbOrder.getValue().getProductNumber()) {
                                                    textField.setValue(AppConstant.RESULT_NG);
                                                } else {
                                                    if (checkPressureTestIsOk(pressureTest) == true) {
                                                        textField.setValue(AppConstant.RESULT_OK);
                                                    } else {
                                                        textField.setValue(AppConstant.RESULT_NG);
                                                    }
                                                }
                                            } else {
                                                textField.setPlaceholder("请输入检验结果");
                                                NotificationUtils.notificationError("没有检测到生产订单号:" + cbOrder.getValue().getProductOrderId() + " 下的压力测试结果，请确认！");
                                            }
                                        } else {
                                            textField.setPlaceholder("请输入检验结果");
                                        }
                                    } else {
                                        textField.setValue(finalInspectionItem.getDefaultResult());
                                    }
                                }
                            }
                        }
                    } else {
                        NotificationUtils.notificationError("当前输入的生产订单号:" + cbOrder.getValue().getProductOrderId() + " 没有相关信息，请确认后重新输入");
//                        orderNo = "";
                        return;
                    }
                }
            }
        });

        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(new Label("检验项目"), 0, 0);
        glLayout.addComponent(new Label("记录查看"), 1, 0);
        glLayout.addComponent(new Label("检验结果"), 2, 0);

        panel.setSizeFull();
        panel.setContent(glLayout);

        vlRoot.addComponents((Component) panel);
        vlRoot.setExpandRatio((Component) panel, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<FinalInspectionItems> optional) {
        boolean enable = optional.isPresent();
    }

    @Override
    protected void init() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        //权限设置
        loginUserName = RequestInfo.current().getUserName();
        role = userService.getByName(loginUserName).getRole();
        if (role.contains(roleService.getByName("qc"))) {
            btnConfirm.setEnabled(true);
            btnStart.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
            btnStart.setEnabled(false);
//            NotificationUtils.notificationError("当前登录人员：" + loginUserName + " 没有QC检测权限！");
        }

        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            cbOrder.setValue(productionOrderService.getByNo(event.getParameters()));
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnConfirm.equals(button)) {
            String inputOrderNo = cbOrder.getValue().getProductOrderId();
            if (!"".equals(inputOrderNo) && productionOrderService.getByNo(inputOrderNo) != null) {
                for (ProductionOrder p : subWo) {
                    FinalInspectionResult finalTemp = finalInspectionResultService.getByOrderNo(p.getProductOrderId());
                    if (finalTemp == null || finalTemp.getQcConfirmUser() == null) {
                        NotificationUtils.notificationError("请先完成子工单" + p.getProductOrderId() + "的终检!");
                        return;
                    }
                }
                List<FinalInspectionResult> frTemp = finalInspectionResultService.getByReportNo(tfReportNo.getValue());
                if (frTemp != null && frTemp.size() > 0) {
                    if (!frTemp.get(0).getOrderNo().equals(cbOrder.getValue().getProductOrderId())) {
                        NotificationUtils.notificationError("终检报告编号重复，请修改！");
                        return;
                    }
                }
                List<FinalInspectionResult> finalInspectionResultList = finalInspectionResultService.getFinalInspectionResultByOrderNo(inputOrderNo);
                if (finalInspectionResultList == null || finalInspectionResultList.size() <= 0 || checkisSavedOrNot(finalInspectionResultList)) {
                    if ("".equals(checkSavingValueIsNull())) {
                        saveDataToFinalInspectionResult(inputOrderNo);
                        if (finalInspectionResult.getPressureTestReportResult().equalsIgnoreCase("OK")) {
                            addPressureQcSign();
                        }
                        initLabel();
                        glLayout.removeAllComponents();
                        NotificationUtils.notificationInfo("工单：" + inputOrderNo + " 的检测结果保存成功！");
//                        sendMail2QA();
                        cbOrder.clear();
                        tfReportNo.clear();
                    } else {
                        NotificationUtils.notificationError(checkSavingValueIsNull());
                    }
                } else {
                    ConfirmDialog.show(getUI(), "工单：" + inputOrderNo + " 的QC检测结果已存在，是否覆盖？",
                            result -> {
                                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                    saveDataToFinalInspectionResult(inputOrderNo);
                                    initLabel();
                                    glLayout.removeAllComponents();
                                    NotificationUtils.notificationInfo("工单：" + inputOrderNo + " 的检测结果保存成功！");
                                    cbOrder.clear();
                                    tfReportNo.clear();
                                }
                            });
                }
            } else {
                NotificationUtils.notificationError("请输入有效的工单号！");
            }
        } else if (btnStart.equals(button)) {
            // 检验开始,光标聚焦gridlayout(2,0)
//			TextField field = (TextField) glLayout.getComponent(2, 0);
            x = 2;
            y = 1;
            // 光标聚焦之后，连接Socket，并且发送语音播报第一条指令
            ipAddress = RequestInfo.current().getUserIpAddress();
            try {
                client = new SocketClient(ipAddress, AppConstant.PORT);

                client.setReceiveListener(message -> {
                    // 定义 OnMessage 后执行事件。
                    if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)//OC
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
                    } else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)//OR
                            && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                        // 发送语音识别请求并且返回的是语音识别结束字串
                        // 判断返回结果是否复核标准范围
                        // 获取检验项名称
                        // message去除信息头
                        String messageBody = message.split("]")[1];
                        if (!message.split("\\|")[1].equals("1")) {
                            returnMessage.append(messageBody.split("\\|")[0]);
                        }
                    } else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {//ES
                        if (returnMessage.length() == 0) {
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (x == 2 && y > 0) {
                                    TextField textField = ((TextField) glLayout.getComponent(2, y));
                                    if (textField != null) {
                                        client.sendMessage("[PT]请输入" + textField.getId() + "的检验结果");
                                    }
                                }
                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }
                            // 没有识别当前检测项的值，任然播报当前检测项的信息
                        } else {
                            getUI().accessSynchronously(new Runnable() {
                                @Override
                                public void run() {
                                    String returnMessageStr = returnMessage.toString();
                                    if (x == 2 && y > 0) {
                                        TextField component = (TextField) glLayout.getComponent(2, y);
                                        if (component != null) {
                                            if ("OK".equals(returnMessageStr) || "NG".equals(returnMessageStr)) {
                                                component.setValue(returnMessageStr.toUpperCase());
                                                isInputOk = true;
                                            } else {
                                                errorMessage = component.getId() + "的检验结果请输入OK或NG中的一个";
                                            }
                                        }
                                    }
                                    returnMessage.delete(0, returnMessage.length());
                                }
                            });
                            // 播报下一个输入项
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (isInputOk && x == 2 && y > 0 && y <= glLayout.getRows()) {
                                    isInputOk = false;
                                    y++;
                                    TextField textField = ((TextField) glLayout.getComponent(x, y));
                                    if (textField != null) {
                                        client.sendMessage("[PT]请输入" + textField.getId() + "的检验结果");
                                    } else {
                                        if (y == glLayout.getRows()) {
                                            prefixSend = AppConstant.INSPECTIONDONE;
                                            client.sendMessage("[PT]QC检验结果录入结束");
                                        }
                                    }
                                } else if (!isInputOk) {
                                    client.sendMessage("[PT]" + errorMessage);
                                    errorMessage = "";
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                prefixSend = AppConstant.PREFIXPLAYTEXT;
                client.sendMessage("[PT]现在开始QC检验结果录入");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (btnLevp.equals(button)) {
            String rootUrl = VaadinUtils.getRootURL();
            VaadinUtils.setPageLocation(rootUrl + "Cameron#!LEVP/" + cbOrder.getValue().getProductOrderId());
        } else if (btnSubWo.equals(button)) {
            subOrderSelectDialog.setObject(subWo);
            subOrderSelectDialog.show(getUI(), result -> {
            });
        }
    }

    public void saveDataToFinalInspectionResult(String inputOrderNo) {
        finalInspectionResult = finalInspectionResultService.getByOrderNo(inputOrderNo);
        if (finalInspectionResult == null) {
            finalInspectionResult = new FinalInspectionResult();
            finalInspectionResult.setOrderNo(inputOrderNo);
        }
        String saleOrderNo = tfSalesNo.getValue().trim();
        String consumer = tfCustomer.getValue().trim();
        finalInspectionResult.setSaleOrderNo(saleOrderNo);
        finalInspectionResult.setConsumer(consumer);
        finalInspectionResult.setQcConfirmDate(new Date());
        finalInspectionResult.setQcConfirmUser(RequestInfo.current().getUserName());
        finalInspectionResult.setReportNo(tfReportNo.getValue());

        finalInspectionResultService.save(finalInspectionResult);
    }

    public String checkSavingValueIsNull() {
        String notification = "";

        String saleOrderNo = tfSalesNo.getValue().trim();
        String consumer = tfCustomer.getValue().trim();
        String routePackageResult = finalInspectionResult.getRoutePackageResult();
        String assemblingRecorderResult = finalInspectionResult.getAssemblingRecorderResult();
        String pressureTestReportResult = finalInspectionResult.getPressureTestReportResult();
        String MTRMetrialReportResult = finalInspectionResult.getMTRMetrialReportResult();
        String dimensionalReportResult = finalInspectionResult.getDimensionalReportResult();
        String hardnessReportResult = finalInspectionResult.getHardnessReportResult();
        String visualExaminationResult = finalInspectionResult.getVisualExaminationResult();
        String functionInspectionResult = finalInspectionResult.getFunctionInspectionResult();
        String monogrammingStatusResult = finalInspectionResult.getMonogrammingStatusResult();
        String reportNo = tfReportNo.getValue().trim();

        String[] arrValue = new String[]{routePackageResult, assemblingRecorderResult, pressureTestReportResult, MTRMetrialReportResult,
                dimensionalReportResult, hardnessReportResult, visualExaminationResult, functionInspectionResult, monogrammingStatusResult,
                consumer, saleOrderNo, reportNo};

        int i = 0;
        for (; i < arrValue.length; i++) {
            if (Strings.isNullOrEmpty(arrValue[i])) {
                break;
            }
        }

        switch (i) {
            case 0:
                notification = "工作路线包检测结果不能为空！";
                break;
            case 1:
                notification = "装配记录检测结果不能为空！";
                break;
            case 2:
                notification = "压力试验检测结果不能为空！";
                break;
            case 3:
                notification = "MTR材料报告检测结果不能为空！";
                break;
            case 4:
                notification = "尺寸报告检测结果不能为空！";
                break;
            case 5:
                notification = "硬度报告检测结果不能为空！";
                break;
            case 6:
                notification = "外观检测检测结果不能为空！";
                break;
            case 7:
                notification = "功能检验检测结果不能为空！";
                break;
            case 8:
                notification = "会标是否应用且正确检测结果不能为空！";
                break;
            case 9:
                notification = "客户不能为空！";
                break;
            case 10:
                notification = "销售订单不能为空！";
                break;
            case 11:
                notification = "报告编号不能为空！";
                break;

            default:
                break;
        }

        return notification;
    }

    public String checkAppearanceInstrumentationResult(List<AppearanceInstrumentationResult> appearanceInstrumentationResultList) {
        String flag = "OK";
        if (appearanceInstrumentationResultList != null && appearanceInstrumentationResultList.size() > 0) {
            for (AppearanceInstrumentationResult appearanceInstrumentationResult : appearanceInstrumentationResultList) {
                if (appearanceInstrumentationResult.getPaintingThicknessResult() > 70 || appearanceInstrumentationResult.getPaintingThicknessResult() < 60) {
                    flag = "NG";
                    break;
                }
            }
        } else {
            return "";
        }
        return flag;
    }

    public String checkFunctionDetectionResult(List<FunctionDetectionResult> functionDetectionResultList) {
        String flag = "OK";
        if (functionDetectionResultList != null && functionDetectionResultList.size() > 0) {
            for (FunctionDetectionResult functionDetectionResult : functionDetectionResultList) {
                if (!"OK".equals(functionDetectionResult.getFunctionInspectionResult())) {
                    flag = "NG";
                    break;
                }
            }
        } else {
            return "";
        }
        return flag;
    }

    public boolean checkisSavedOrNot(List<FinalInspectionResult> finalInspectionResultList) {
        boolean flag = true;
        if (finalInspectionResultList != null && finalInspectionResultList.size() > 0) {
            for (FinalInspectionResult finalInspectionResult : finalInspectionResultList) {
                Date qcDate = finalInspectionResult.getQcConfirmDate();
                String qcUser = finalInspectionResult.getQcConfirmUser();
                if (qcDate != null || qcUser != null) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public boolean checkPressureTestIsOk(List<PressureTest> pressureTestList) {
        boolean flag = true;
        for (int i = 0; i < pressureTestList.size(); i++) {
            PressureTest pressureTest = pressureTestList.get(i);
            if (pressureTest != null && "NG".equals(pressureTest.getTestResult())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public void initLabel() {
        tfAssyNo.setValue("");
        tfAssySerialNo.setValue("");
        tfDescription.setValue("");
        tfQpNo.setValue("");
        tfFactoryCode.setValue("");
        tfSalesNo.setValue("");
        tfCustomer.setValue("");
        cbWorkshop.clear();
        btnLevp.setVisible(false);
        btnSubWo.setVisible(false);
    }

    //给测压报告加入QC签名
    public void addPressureQcSign() {
        List<PressureTest> listPressure = pressureTestService.getPressureTestByOrder(cbOrder.getValue().getProductOrderId());
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }
        if (listPressure != null && listPressure.size() > 0) {
            if (listPressure.get(0).getTestType().equals("井口")) {
                for (int i = 0; i < listPressure.size(); i++) {
                    String sn = listPressure.get(i).getProductSN();
                    String filename = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT + sn + ".pdf";
                    try {
                        PdfReader readerOriginalDoc = new PdfReader(filename);
                        File fromFile = new File(filename);
                        PdfWriter writeDest = new PdfWriter(filename.replace(".pdf", "-2.pdf"));
                        File toFile = new File(filename.replace(".pdf", "-2.pdf"));
                        PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
                        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(newDoc);
                        Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
                        }
                        ImageData imageData = ImageDataFactory.create(inputStream2byte(mediaImage.getMediaStream()));
                        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                        image.scaleToFit(60, 60);
                        image.setFixedPosition(1, 165, 110);
                        document.add(image);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        Paragraph par = new Paragraph("PASS    " + LocalDate.now().format(formatter));
                        par.setFontSize(9);
                        par.setFixedPosition(1, 218, 130, 200);
                        document.add(par);
                        image.setFixedPosition(2, 170, 78);
                        par.setFixedPosition(2, 215, 101, 200);
                        document.add(image);
                        document.add(par);
                        document.close();
                        fromFile.delete();
                        toFile.renameTo(fromFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (listPressure.get(0).getTestType().equals("阀门")) {
                for (int i = 0; i < listPressure.size(); i++) {
                    String sn = listPressure.get(i).getProductSN();
                    String filename = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT + sn + ".pdf";
                    try {
                        PdfReader readerOriginalDoc = new PdfReader(filename);
                        File fromFile = new File(filename);
                        PdfWriter writeDest = new PdfWriter(filename.replace(".pdf", "-2.pdf"));
                        File toFile = new File(filename.replace(".pdf", "-2.pdf"));
                        PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
                        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(newDoc);
                        Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
                        }
                        ImageData imageData = ImageDataFactory.create(inputStream2byte(mediaImage.getMediaStream()));
                        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
                        Paragraph par1 = new Paragraph("PASS");
                        par1.setFontSize(9);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        Paragraph par2 = new Paragraph(LocalDate.now().format(formatter));
                        par2.setFontSize(9);
                        par1.setFixedPosition(1, 320, 150, 200);
                        par2.setFixedPosition(1, 420, 128, 200);
                        image.scaleToFit(60, 60);
                        image.setFixedPosition(1, 187, 127);
                        document.add(image);
                        document.add(par1);
                        document.add(par2);
                        document.close();
                        fromFile.delete();
                        toFile.renameTo(fromFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    private void sendMail2QA() {
        List<User> qaUsers = userService.listByRoleId(roleService.getByName("qa").getId());

        // 发件人电子邮箱
        String from = "A419@cn0116app02.dir.slb.com";

        // 指定发送邮件的主机为 localhost
        String host = "cn0116app02.dir.slb.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties);

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            for (User u : qaUsers) {
                if (!Strings.isNullOrEmpty(u.getEmail())) {
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(u.getEmail()));
                }
            }

            // Set Subject: 头部头字段
            message.setSubject("工单" + cbOrder.getValue().getProductOrderId() + "已由QC完成终检");

            // 设置消息体
            message.setContent("<p>工单" + cbOrder.getValue().getProductOrderId() + "已由QC完成终检，点击<a href=\"http://cn0116app02:8080/cameron/Cameron#!QAInspection/" +
                    cbOrder.getValue().getProductOrderId() + "\">链接</a>前往查看</p>", "text/html;charset=gb2312");

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
