//Changed by Cameron: 加入直接跳至子工单终检的按钮

package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.view.inspectionqc.PressureTestDialog_;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
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
import com.vaadin.ui.themes.ValoTheme;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Menu(caption = "QAInspection", captionI18NKey = "view.QAInspection.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 8)
@SpringView(name = "QAInspection", ui = {CameronUI.class})
public class QAInspectionView extends BaseView implements Button.ClickListener {
    private static final long serialVersionUID = 6370697840639947696L;
    private static final int wdFormatPDF = 17;// PDF 格式
    private static final int wdFormatHtml = 8;
    @I18Support(caption = "Sub-WO", captionKey = "view.qcinspection.subwo")
    private final Button btnSubWo = new Button();
    List<Role> role = null;
    HorizontalLayout reportPDFLayout = new HorizontalLayout();
    //    private TextField tfOrderNo = new TextField();
    private ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    //具体字段意义参考最终检验报告模板
    @I18Support(caption = "Assy.No", captionKey = "view.qcinspection.assyno")    //部件号 Assy. No
    private LabelWithSamleLineCaption tfAssyNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Confirm", captionKey = "view.qcinspection.confirm")
    private Button btnConfirm = new Button();
    //COC生成按钮
    @I18Support(caption = "COC", captionKey = "view.qainspection.coc")
    private Button btnCOC = new Button();
    @I18Support(caption = "exportPDF", captionKey = "common.Export.pdf")
    private Button btnExport = new Button();
    //    @I18Support(caption = "ProductRev", captionKey = "view.qcinspection.productrev")//版本号
//    private LabelWithSamleLineCaption tfProductRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "AssySerialNo", captionKey = "view.qcinspection.assyserialno")//装配件序列号
    private LabelWithSamleLineCaption tfAssySerialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "view.qcinspection.description")//描述
    private LabelWithSamleLineCaption tfDescription = new LabelWithSamleLineCaption();
    @I18Support(caption = "OrderNo", captionKey = "view.qcinspection.orderno")//订单号
    private LabelWithSamleLineCaption tfProductionNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Quality Plan No", captionKey = "view.qcinspection.qpno")//质量计划
    private LabelWithSamleLineCaption tfQpNo = new LabelWithSamleLineCaption();
    //    @I18Support(caption = "Quality Plan Rev", captionKey = "view.qcinspection.QpRev")//质量计划版本
//    private LabelWithSamleLineCaption tfQpRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "SalesNo", captionKey = "view.qcinspection.salesno")//销售订单号
    private LabelWithSamleLineCaption tfSalesNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Customer", captionKey = "view.qcinspection.customer")//客户
    private LabelWithSamleLineCaption tfCustomer = new LabelWithSamleLineCaption();
    private TextField tfReportNo = new TextField();//报告编号
    //    private CheckBox chbAssembling = new CheckBox();//生成最终报告时是否需要装配记录
//    private CheckBox chbPressure = new CheckBox();//生成最终报告时是否需压力测试记录
//    private CheckBox chbPaint = new CheckBox();//生成最终报告时是否需要喷漆报告
    @I18Support(caption = "FactoryCode", captionKey = "view.qcinspection.factorycode")//工厂代码
    private LabelWithSamleLineCaption tfFactoryCode = new LabelWithSamleLineCaption();
    private AbstractComponent[] components = new AbstractComponent[]{cbOrder, btnConfirm, btnCOC, btnExport, btnSubWo};//,btnPreview,btnExportQC
    private AbstractComponent[] disPlayComponents1 = new AbstractComponent[]{tfAssyNo, tfAssySerialNo, tfReportNo};//tfProductRev,tfProductionNo
    private AbstractComponent[] disPlayComponents2 = new AbstractComponent[]{tfQpNo, tfFactoryCode};//tfQpRev
    private AbstractComponent[] disPlayComponents3 = new AbstractComponent[]{tfCustomer, tfSalesNo, tfDescription};
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private HorizontalLayout hlDisplayBox = new HorizontalLayout();
    private VerticalLayout vlBox1 = new VerticalLayout();
    private VerticalLayout vlBox2 = new VerticalLayout();
    private VerticalLayout vlBox3 = new VerticalLayout();
    private Grid<FinalInspectionItems> objectGrid = new Grid<FinalInspectionItems>();
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private IOrderHistoryService orderHistoryService;
    @Autowired
    private IAssemblingService assemblingService;
    @Autowired
    private IPressureTestService pressureTestService;
    @Autowired
    private IFinalInspectionItemsService finalInspectionItemsService;
    @Autowired
    private IFinalInspectionResultService finalInspectionResultService;
    @Autowired
    private QAOrderOperationInfoDialog QAOrderOperationInfoDialog;
    @Autowired
    private QAInputPaintThicknessDialog QAInputPaintThicknessDialog;
    @Autowired
    private QAFunctionInspectionDialog QAFunctionInspectionDialog;
    @Autowired
    private QAPressureTestDialog QAPressureTestDialog;
    @Autowired
    private QADimensionInspectionDialog QADimensionInspectionDialog;
    @Autowired
    private IAppearanceInstrumentationResultService appearanceInstrumentationResultService;
    @Autowired
    private IFunctionDetectionResultService functionDetectionResultService;
    @Autowired
    private QAAssemblingInfoDialog QAAssemblingInfoDialog;
    @Autowired
    private QAHardnessTestReportDialog QAHardnessTestReportDialog;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private PDFPreviewDialog PDFPreviewDialog;
    @Autowired
    private PressureTestDialog_ pressureTestDialog;
    @Autowired
    private SelectPDFOutputDialog selectPDFOutputDialog;
    @Autowired
    private ICaMediaService mediaService;
    private String loginUserName = "";
    @Autowired
    private UserService userService;
    @Autowired
    private IRoleService roleService;
    private FinalInspectionResult finalInspectionResult;
    private List<FinalInspectionResult> QCfinalInspectionResultSavedList;
    private List<FinalInspectionResult> QAfinalInspectionResultSavedList;
    @Autowired
    private SubOrderSelectDialogQA subOrderSelectDialog;

    private List<ProductionOrder> subWo;

    public QAInspectionView() {
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

        btnCOC.setIcon(VaadinIcons.CLIPBOARD_CHECK);
        btnCOC.addClickListener(this);

        btnExport.setIcon(VaadinIcons.DOWNLOAD);
        btnExport.addClickListener(this);

        btnSubWo.setIcon(VaadinIcons.FILE_TREE_SUB);
        btnSubWo.addClickListener(this);
        btnSubWo.setVisible(false);

//		btnExportQC.setIcon(VaadinIcons.DOWNLOAD);
//		btnExportQC.addClickListener(this);
//		btnPreview.setIcon(VaadinIcons.VIEWPORT);
//		btnPreview.addClickListener(this);
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
        tfDescription.setWidth("200px");
        tfReportNo.setCaption("报告编号: ");
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
                    if (inputProductionOrder != null) {
                        //判断工单是否锁定
                        if(inputProductionOrder.getBomLockFlag()){
                            throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                                    "This Order is locked,Please contact the engineer to solve !"));
                        }
                        finalInspectionResult = finalInspectionResultService.getByOrderNo(inputProductionOrder.getProductOrderId());
                        if (finalInspectionResult == null || finalInspectionResult.getQcConfirmUser() == null) {
                            objectGrid.setItems(new ArrayList<FinalInspectionItems>());
                            NotificationUtils.notificationWarning("当前Order没有完成QC终检");
                            return;
                        }
                        QCfinalInspectionResultSavedList = finalInspectionResultService.getFinalInspectionResultByOrderNo(inputProductionOrder.getProductOrderId(), "QC");
                        QAfinalInspectionResultSavedList = finalInspectionResultService.getFinalInspectionResultByOrderNo(inputProductionOrder.getProductOrderId(), "QA");

                        //根据产品号及版本号，获取质量计划和版本
                        ProductInformation productInformation = productInformationService
                                .getByNoRev(inputProductionOrder.getProductId(), inputProductionOrder.getProductVersionId());
                        //通过工单号查找Routing信息
                        List<FinalInspectionItems> listFinalInspectionItems = finalInspectionItemsService.getAllData();
                        if (productInformation == null) {
                            NotificationUtils.notificationError("工单:" + inputProductionOrder.getProductOrderId() + " 对应的产品:" + inputProductionOrder.getProductId() + " 在系统中没有维护产品信息");
                            return;
                        }
                        tfAssyNo.setValue(inputProductionOrder.getProductId() + " REV" + inputProductionOrder.getProductVersionId());
//                        tfProductRev.setValue(inputProductionOrder.getProductVersionId());
                        tfAssySerialNo.setValue(String.format("%4d", 1).replace(" ", "0") + "-"
                                + String.format("%4d", event.getValue().getProductNumber()).replace(" ", "0"));
                        tfDescription.setValue(inputProductionOrder.getProductDesc());
                        tfDescription.setWidthUndefined();
                        tfQpNo.setValue(productInformation.getQulityPlan() + " REV" + productInformation.getQulityPlanRev());//sparePart.getQaPlanRev().split("/")[0]
//                        tfQpRev.setValue(sparePart.getQaPlanRev());//sparePart.getQaPlanRev().split("/")[1]
                        tfFactoryCode.setValue("A419");
//                        tfSalesNo.setValue(inputProductionOrder.getSalesOrder());
                        //改为PO-Item,并考虑多订单情况用/分隔
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
                        tfReportNo.setValue(finalInspectionResult.getReportNo());
                        if (listFinalInspectionItems != null && listFinalInspectionItems.size() > 0) {
                            objectGrid.setItems(listFinalInspectionItems);
                        } else {
                            objectGrid.setItems(new ArrayList<>());
                        }
                        //获取是否有子工单
                        subWo = productionOrderService.getBySuperiorOrder(inputProductionOrder.getProductOrderId());
                        if (subWo != null && subWo.size() > 0) {
                            btnSubWo.setVisible(true);
                        }
                    }
                }
            }
        });
        objectGrid.setSizeFull();
        objectGrid.addColumn(FinalInspectionItems::getInspectionItemName).setCaption("检验项目");
        objectGrid.addComponentColumn(new ValueProvider<FinalInspectionItems, HorizontalLayout>() {

            private static final long serialVersionUID = -7941433938186917381L;

            @Override
            public HorizontalLayout apply(FinalInspectionItems source) {
                HorizontalLayout layout = new HorizontalLayout();
//                Button btnView = new Button();
                TextField tfvalue = new TextField();
//                btnView.setIcon(VaadinIcons.OPEN_BOOK);
//                btnView.setCaption("查看");

                tfvalue.addValueChangeListener(new HasValue.ValueChangeListener<String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                        String textValue = tfvalue.getValue().trim();

                        tfvalue.removeStyleName(tfvalue.getStyleName());
                        if ("OK".equals(textValue)) {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_GREEN);
                        } else if ("NG".equals(textValue)) {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_RED);
                        } else {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_WHITE);
                        }
                    }
                });

//                btnView.addClickListener(new ClickListener() {
//
//                    private static final long serialVersionUID = 753594212447522087L;
//
//                    @Override
//                    public void buttonClick(ClickEvent event) {
//
//                        if (source.getInspectionItemName().equals(AppConstant.WORK_ROUTING)) {
//                            //如果是工作路线包检查项，弹出页面，展示工艺路线
//                            List<OrderRoutingConfirmInfo> orderHistory = orderHistoryService.getByOrderNo(cbOrder.getValue().getProductOrderId());
//                            QAOrderOperationInfoDialog.setObject(orderHistory);
//                            QAOrderOperationInfoDialog.refreshData();
//                            QAOrderOperationInfoDialog.show(getUI(), new DialogCallBack() {
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        } else if (source.getInspectionItemName().equals(AppConstant.ASSEMBLING_REPORT)) {
//                            //装配信息需要展示
//                            QAAssemblingInfoDialog.setData(cbOrder.getValue().getProductOrderId());
//                            QAAssemblingInfoDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        } else if (source.getInspectionItemName().equals(AppConstant.PRESSURE_TEST_REPORT)) {
//                            //压力测试需要
//                            QAPressureTestDialog.setObject(cbOrder.getValue().getProductOrderId());
//                            QAPressureTestDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
////							NotificationUtils.notificationInfo(AppConstant.PRESSURE_TEST_REPORT);
//                        } else if (source.getInspectionItemName().equals(AppConstant.DIMENSIONAL_REPORT)) {
//                            //尺寸报告显示 dimensionInspectionDialog
//                            QADimensionInspectionDialog.setData(cbOrder.getValue().getProductOrderId());
//                            QADimensionInspectionDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        } else if (source.getInspectionItemName().equals(AppConstant.HARDNESS_REPORT)) {
//                            //硬度报告显示 hardnessTestReportDialog
//                            QAHardnessTestReportDialog.setData(cbOrder.getValue().getProductOrderId());
//                            QAHardnessTestReportDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        } else if (source.getInspectionItemName().equals(AppConstant.VISUAL_EXAMINATION)) {
//                            //外观检验时判断是否需要喷漆，如果需要，弹出输入页面，需要输入漆厚度数据
////							List<AppearanceInstrumentationResult> appearanceInstrumentationResultList = appearanceInstrumentationResultService.getByNo(orderNo);
////								ProductionOrder productionOrder = productionOrderService.getByNo(orderNo);
//                            QAInputPaintThicknessDialog.setData(cbOrder.getValue().getProductOrderId());
//                            QAInputPaintThicknessDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        } else if (source.getInspectionItemName().equals(AppConstant.FUNCTION_INSPECTION)) {
//                            //功能检验需要需要输入量具编号以及扭矩测试结果，最多8个
//                            List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(cbOrder.getValue().getProductOrderId());
//                            ProductionOrder productionOrder = cbOrder.getValue();
//                            QAFunctionInspectionDialog.setData(productionOrder, functionDetectionResultList);
//                            QAFunctionInspectionDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//                        }
//                    }
//                });

                //QC 检测结果 初始化
                if (QCfinalInspectionResultSavedList != null && QCfinalInspectionResultSavedList.size() > 0) {//已保存数据
                    FinalInspectionResult finalInspectionResult1 = QCfinalInspectionResultSavedList.get(0);
                    tfvalue.setReadOnly(true);
                    switch (source.getInspectionItemName()) {
                        case AppConstant.WORK_ROUTING:
                            tfvalue.setValue(finalInspectionResult1.getRoutePackageResult() == null ? "" : finalInspectionResult1.getRoutePackageResult());
                            break;
                        case AppConstant.ASSEMBLING_REPORT:
                            tfvalue.setValue(finalInspectionResult1.getAssemblingRecorderResult() == null ? "" : finalInspectionResult1.getAssemblingRecorderResult());
                            break;
                        case AppConstant.PRESSURE_TEST_REPORT:
                            tfvalue.setValue(finalInspectionResult1.getPressureTestReportResult() == null ? "" : finalInspectionResult1.getPressureTestReportResult());
                            break;
                        case AppConstant.MTR_METRIAL_REPORT:
                            tfvalue.setValue(finalInspectionResult1.getMTRMetrialReportResult() == null ? "" : finalInspectionResult1.getMTRMetrialReportResult());
                            break;
                        case AppConstant.DIMENSIONAL_REPORT:
                            tfvalue.setValue(finalInspectionResult1.getDimensionalReportResult() == null ? "" : finalInspectionResult1.getDimensionalReportResult());
                            break;
                        case AppConstant.HARDNESS_REPORT:
                            tfvalue.setValue(finalInspectionResult1.getHardnessReportResult() == null ? "" : finalInspectionResult1.getHardnessReportResult());
                            break;
                        case AppConstant.VISUAL_EXAMINATION:
                            tfvalue.setValue(finalInspectionResult1.getVisualExaminationResult() == null ? "" : finalInspectionResult1.getVisualExaminationResult());
                            break;
                        case AppConstant.FUNCTION_INSPECTION:
                            tfvalue.setValue(finalInspectionResult1.getFunctionInspectionResult() == null ? "" : finalInspectionResult1.getFunctionInspectionResult());
                            break;
                        case AppConstant.MONOGRAMMING_STATUS:
                            tfvalue.setValue(finalInspectionResult1.getMonogrammingStatusResult() == null ? "" : finalInspectionResult1.getMonogrammingStatusResult());
                            break;

                        default:
                            tfvalue.setValue(finalInspectionResult1.getCommentsResult() == null ? "" : finalInspectionResult1.getCommentsResult());
                            break;
                    }
                } else {
                    if (Strings.isNullOrEmpty(source.getDefaultResult())) {
                        List<AppearanceInstrumentationResult> appearanceInstrumentationResultList = appearanceInstrumentationResultService.getByNo(cbOrder.getValue().getProductOrderId());
                        List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(cbOrder.getValue().getProductOrderId());
                        if (AppConstant.VISUAL_EXAMINATION.equals(source.getInspectionItemName())) {
                            if ("OK".equals(checkAppearanceInstrumentationResult(appearanceInstrumentationResultList))) {
                                tfvalue.setValue("OK");
                            } else if ("NG".equals(checkAppearanceInstrumentationResult(appearanceInstrumentationResultList))) {
                                tfvalue.setValue("NG");
                            } else {
                                tfvalue.setPlaceholder("请输入检验结果");
                            }
                        } else if (AppConstant.FUNCTION_INSPECTION.equals(source.getInspectionItemName())) {
                            if ("OK".equals(checkFunctionDetectionResult(functionDetectionResultList))) {
                                tfvalue.setValue("OK");
                            } else if ("NG".equals(checkFunctionDetectionResult(functionDetectionResultList))) {
                                tfvalue.setValue("NG");
                            } else {
                                tfvalue.setPlaceholder("请输入检验结果");
                            }
                        } else {
                            tfvalue.setPlaceholder("请输入检验结果");
                        }
                    } else {
                        tfvalue.setValue(source.getDefaultResult());
                    }
                    tfvalue.setReadOnly(true);
                }


//                layout.addComponent(btnView);
                layout.addComponent(tfvalue);
                return layout;
            }
        }).setCaption("QC检验结果");
        //QA列
        objectGrid.addComponentColumn(new ValueProvider<FinalInspectionItems, HorizontalLayout>() {

            /**
             *
             */
            private static final long serialVersionUID = 7304599177569403315L;

            @Override
            public HorizontalLayout apply(FinalInspectionItems source) {
                HorizontalLayout layout = new HorizontalLayout();
//				Button btnView = new Button();
                Button btnConfirm = new Button();
                TextField tfvalue = new TextField();
                Button btnPreview = new Button();

                tfvalue.addValueChangeListener(new HasValue.ValueChangeListener<String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                        String textValue = tfvalue.getValue().trim();

                        tfvalue.removeStyleName(tfvalue.getStyleName());
                        if ("OK".equals(textValue)) {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_GREEN);
                        } else if ("NG".equals(textValue)) {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_RED);
                        } else {
                            tfvalue.addStyleName(CoreTheme.BACKGROUND_WHITE);
                        }
                        FinalInspectionResult finalInspectionResult4 = finalInspectionResultService.getByOrderNo(cbOrder.getValue().getProductOrderId());
                        if (finalInspectionResult4 == null) {
                            finalInspectionResult4 = new FinalInspectionResult();
                            finalInspectionResult4.setOrderNo(cbOrder.getValue().getProductOrderId());
                        }
                        switch (source.getInspectionItemName()) {
                            case AppConstant.WORK_ROUTING:
                                finalInspectionResult4.setRoutePackageResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.ASSEMBLING_REPORT:
                                finalInspectionResult4.setAssemblingRecorderResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.PRESSURE_TEST_REPORT:
                                finalInspectionResult4.setPressureTestReportResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.MTR_METRIAL_REPORT:
                                finalInspectionResult4.setMTRMetrialReportResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.DIMENSIONAL_REPORT:
                                finalInspectionResult4.setDimensionalReportResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.HARDNESS_REPORT:
                                finalInspectionResult4.setHardnessReportResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.VISUAL_EXAMINATION:
                                finalInspectionResult4.setVisualExaminationResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.FUNCTION_INSPECTION:
                                finalInspectionResult4.setFunctionInspectionResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                            case AppConstant.MONOGRAMMING_STATUS:
                                finalInspectionResult4.setMonogrammingStatusResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;

                            default:
                                finalInspectionResult4.setCommentsResult(tfvalue.getValue());
                                finalInspectionResultService.save(finalInspectionResult4);
                                break;
                        }
                    }
                });


                btnConfirm.setIcon(VaadinIcons.ARROW_FORWARD);
                btnConfirm.setCaption("确认QC检验结果");
                btnConfirm.addClickListener(new ClickListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        FinalInspectionResult finalInspectionResult3 = QCfinalInspectionResultSavedList.get(0);
                        switch (source.getInspectionItemName()) {
                            case AppConstant.WORK_ROUTING:
                                tfvalue.setValue(finalInspectionResult3.getRoutePackageResult() == null ? "" : finalInspectionResult3.getRoutePackageResult());
                                break;
                            case AppConstant.ASSEMBLING_REPORT:
                                tfvalue.setValue(finalInspectionResult3.getAssemblingRecorderResult() == null ? "" : finalInspectionResult3.getAssemblingRecorderResult());
                                break;
                            case AppConstant.PRESSURE_TEST_REPORT:
                                tfvalue.setValue(finalInspectionResult3.getPressureTestReportResult() == null ? "" : finalInspectionResult3.getPressureTestReportResult());
                                break;
                            case AppConstant.MTR_METRIAL_REPORT:
                                tfvalue.setValue(finalInspectionResult3.getMTRMetrialReportResult() == null ? "" : finalInspectionResult3.getMTRMetrialReportResult());
                                break;
                            case AppConstant.DIMENSIONAL_REPORT:
                                tfvalue.setValue(finalInspectionResult3.getDimensionalReportResult() == null ? "" : finalInspectionResult3.getDimensionalReportResult());
                                break;
                            case AppConstant.HARDNESS_REPORT:
                                tfvalue.setValue(finalInspectionResult3.getHardnessReportResult() == null ? "" : finalInspectionResult3.getHardnessReportResult());
                                break;
                            case AppConstant.VISUAL_EXAMINATION:
                                tfvalue.setValue(finalInspectionResult3.getVisualExaminationResult() == null ? "" : finalInspectionResult3.getVisualExaminationResult());
                                break;
                            case AppConstant.FUNCTION_INSPECTION:
                                tfvalue.setValue(finalInspectionResult3.getFunctionInspectionResult() == null ? "" : finalInspectionResult3.getFunctionInspectionResult());
                                break;
                            case AppConstant.MONOGRAMMING_STATUS:
                                tfvalue.setValue(finalInspectionResult3.getMonogrammingStatusResult() == null ? "" : finalInspectionResult3.getMonogrammingStatusResult());
                                break;

                            default:
                                tfvalue.setValue(finalInspectionResult3.getCommentsResult() == null ? "" : finalInspectionResult3.getCommentsResult());
                                break;
                        }
                    }
                });

                btnPreview.setIcon(VaadinIcons.VIEWPORT);
                btnPreview.addStyleName(ValoTheme.BUTTON_BORDERLESS);
                btnPreview.addStyleName(ValoTheme.BUTTON_QUIET);
                btnPreview.setCaption("预览");
                btnPreview.addClickListener(new ClickListener() {

                    private static final long serialVersionUID = 753594212447522087L;

                    @Override
                    public void buttonClick(ClickEvent event) {

                        if (source.getInspectionItemName().equals(AppConstant.WORK_ROUTING)) {
//                            PDFPreviewDialog.setMedia(getMedia(path));
//                            PDFPreviewDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
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
                        } else if (source.getInspectionItemName().equals(AppConstant.ASSEMBLING_REPORT)) {
                            //装配信息需要展示
                            QAAssemblingInfoDialog.setData(cbOrder.getValue().getProductOrderId());
                            QAAssemblingInfoDialog.show(getUI(), new DialogCallBack() {

                                @Override
                                public void done(ConfirmResult result) {

                                }
                            });
                        } else if (source.getInspectionItemName().equals(AppConstant.PRESSURE_TEST_REPORT)) {
                            //压力测试需要
//                            String path = AppConstant.STANDARD_FILE_PATH + "\\test.pdf";
//                            PDFPreviewDialog.setMedia(getMedia(path));
//                            PDFPreviewDialog.show(getUI(), new DialogCallBack() {
//
//                                @Override
//                                public void done(ConfirmResult result) {
//
//                                }
//                            });
//
//                            NotificationUtils.notificationInfo(AppConstant.PRESSURE_TEST_REPORT);
                            pressureTestDialog.setObject(cbOrder.getValue());
                            pressureTestDialog.show(getUI(), new DialogCallBack() {

                                @Override
                                public void done(ConfirmResult result) {
                                }
                            });
                        } else if (source.getInspectionItemName().equals(AppConstant.VISUAL_EXAMINATION)) {
                            QAInputPaintThicknessDialog.setData(cbOrder.getValue().getProductOrderId());
                            QAInputPaintThicknessDialog.show(getUI(), new DialogCallBack() {

                                @Override
                                public void done(ConfirmResult result) {

                                }
                            });
                        } else if (source.getInspectionItemName().equals(AppConstant.FUNCTION_INSPECTION)) {
                            //功能检验需要需要输入量具编号以及扭矩测试结果，最多8个
                            List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(cbOrder.getValue().getProductOrderId());
                            ProductionOrder productionOrder = cbOrder.getValue();
                            QAFunctionInspectionDialog.setData(productionOrder, functionDetectionResultList);
                            QAFunctionInspectionDialog.show(getUI(), new DialogCallBack() {

                                @Override
                                public void done(ConfirmResult result) {

                                }
                            });
                        } else if (source.getInspectionItemName().equals(AppConstant.DIMENSIONAL_REPORT)) {//Changed by Cameron: 打开机加工工单的尺寸检验报告
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
                        }
                    }
                });


                if (QAfinalInspectionResultSavedList != null && QAfinalInspectionResultSavedList.size() > 0) {//已保存数据
                    FinalInspectionResult finalInspectionResult2 = QAfinalInspectionResultSavedList.get(0);
                    //tfvalue.setReadOnly(true);
                    switch (source.getInspectionItemName()) {
                        case AppConstant.WORK_ROUTING:
                            tfvalue.setValue(finalInspectionResult2.getRoutePackageResult() == null ? "" : finalInspectionResult2.getRoutePackageResult());
                            break;
                        case AppConstant.ASSEMBLING_REPORT:
                            tfvalue.setValue(finalInspectionResult2.getAssemblingRecorderResult() == null ? "" : finalInspectionResult2.getAssemblingRecorderResult());
                            break;
                        case AppConstant.PRESSURE_TEST_REPORT:
                            tfvalue.setValue(finalInspectionResult2.getPressureTestReportResult() == null ? "" : finalInspectionResult2.getPressureTestReportResult());
                            break;
                        case AppConstant.MTR_METRIAL_REPORT:
                            tfvalue.setValue(finalInspectionResult2.getMTRMetrialReportResult() == null ? "" : finalInspectionResult2.getMTRMetrialReportResult());
                            break;
                        case AppConstant.DIMENSIONAL_REPORT:
                            tfvalue.setValue(finalInspectionResult2.getDimensionalReportResult() == null ? "" : finalInspectionResult2.getDimensionalReportResult());
                            break;
                        case AppConstant.HARDNESS_REPORT:
                            tfvalue.setValue(finalInspectionResult2.getHardnessReportResult() == null ? "" : finalInspectionResult2.getHardnessReportResult());
                            break;
                        case AppConstant.VISUAL_EXAMINATION:
                            tfvalue.setValue(finalInspectionResult2.getVisualExaminationResult() == null ? "" : finalInspectionResult2.getVisualExaminationResult());
                            break;
                        case AppConstant.FUNCTION_INSPECTION:
                            tfvalue.setValue(finalInspectionResult2.getFunctionInspectionResult() == null ? "" : finalInspectionResult2.getFunctionInspectionResult());
                            break;
                        case AppConstant.MONOGRAMMING_STATUS:
                            tfvalue.setValue(finalInspectionResult2.getMonogrammingStatusResult() == null ? "" : finalInspectionResult2.getMonogrammingStatusResult());
                            break;

                        default:
                            tfvalue.setValue(finalInspectionResult2.getCommentsResult() == null ? "" : finalInspectionResult2.getCommentsResult());
                            break;
                    }
                } else {
                    if (Strings.isNullOrEmpty(source.getDefaultResult())) {
                        tfvalue.setPlaceholder("请输入检验结果");
                    } else {
                        tfvalue.setValue(source.getDefaultResult());
                    }
                }

                layout.addComponent(btnConfirm);
                layout.addComponent(tfvalue);
                layout.addComponent(btnPreview);//btnView

                //压力，装配，喷漆判断是否需要
//                if (AppConstant.ASSEMBLING_REPORT.equals(source.getInspectionItemName())) {
//                    layout.addComponent(chbAssembling);
//                    layout.setComponentAlignment(chbAssembling, Alignment.MIDDLE_CENTER);
//                }
//                if (AppConstant.PRESSURE_TEST_REPORT.equals(source.getInspectionItemName())) {
//                    layout.addComponent(chbPressure);
//                    layout.setComponentAlignment(chbPressure, Alignment.MIDDLE_CENTER);
//                }
//                if (AppConstant.VISUAL_EXAMINATION.equals(source.getInspectionItemName())) {
//                    layout.addComponent(chbPaint);
//                    layout.setComponentAlignment(chbPaint, Alignment.MIDDLE_CENTER);
//                }
                return layout;
            }
        }).setCaption("QA审核结果");

        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        vlRoot.addComponent(reportPDFLayout);

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
        if (role.contains(roleService.getByName("qa"))) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
//			NotificationUtils.notificationError("当前登录人员："+loginUserName+" 没有QA终检权限！");
        }

        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            cbOrder.setValue(productionOrderService.getByNo(event.getParameters()));
        }
    }

    public void initLabel() {
        tfAssyNo.setValue("");
        tfAssySerialNo.setValue("");
        tfDescription.setValue("");
        tfQpNo.setValue("");
        tfFactoryCode.setValue("");
        tfSalesNo.setValue("");
        tfCustomer.setValue("");
        tfReportNo.setValue("");
        btnSubWo.setVisible(false);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        String inputOrderNo = (cbOrder.getValue() == null ? "" : cbOrder.getValue().getProductOrderId());
        if (btnConfirm.equals(button)) {
            if (!"".equals(inputOrderNo) && productionOrderService.getByNo(inputOrderNo) != null) {
                List<FinalInspectionResult> finalInspectionResultListQC = finalInspectionResultService.getFinalInspectionResultByOrderNo(inputOrderNo, "QC");
                List<AppearanceInstrumentationResult> appearanceInstrumentationResultList = appearanceInstrumentationResultService.getByNo(inputOrderNo);
                List<FunctionDetectionResult> functionDetectionResultList = functionDetectionResultService.getByNo(inputOrderNo);
                //List<FinalInspectionResult> finalInspectionResultListQA = finalInspectionResultService.getFinalInspectionResultByOrderNo(inputOrderNo, "QA");
                // if (finalInspectionResultListQA == null || finalInspectionResultListQA.size() <= 0) {
                if ("".equals(checkSavingValueIsNull())) {
                    if (finalInspectionResultListQC != null && finalInspectionResultListQC.size() > 0) {
                        String path = "";
                        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                        if (caConfig != null) {
                            path = caConfig.getConfigValue();
                            if (Strings.isNullOrEmpty(path)) {
                                NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
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
                        try {
                            //生成报告
                            createReport(finalInspectionResultListQC.get(0), appearanceInstrumentationResultList, functionDetectionResultList, path);
                            //保存QA信息
                            saveDataToFinalInspectionResult(inputOrderNo);

                            initLabel();
                            objectGrid.setItems(new ArrayList<FinalInspectionItems>());
                            NotificationUtils.notificationInfo("工单：" + inputOrderNo + " 的审核结果保存成功！");
                        } catch (Exception e) {
                            e.printStackTrace();
                            NotificationUtils.notificationError("导出文件或保存QA信息出现异常：" + e.getMessage());
                        }
                    } else {
                        NotificationUtils.notificationError("工单：" + inputOrderNo + " 的QC检测结果为空！");
                    }
                } else {
                    NotificationUtils.notificationError(checkSavingValueIsNull());
                }
                //} else {
                //    NotificationUtils.notificationError("工单：" + inputOrderNo + " 的QA检测结果已存在！");
                //}
            } else {
                NotificationUtils.notificationError("请输入有效的工单号！");
            }
        } else if (btnExport.equals(button)) {
//            initLabel();
//            objectGrid.setItems(new ArrayList<>());
//            if ("".equals(inputOrderNo) || cbOrder.getValue() == null) {
//                NotificationUtils.notificationError("请输入有效的工单号！");
//                return;
//            }
////            orderNo = inputOrderNo;
//            System.out.println(LocalDateTime.now());
//            String[] res = getWordMergePath(inputOrderNo);
//            System.out.println(LocalDateTime.now());
//            if ("OK".equals(res[0])) {
//                wordToPDF(res[1]);
//                System.out.println("end****" + LocalDateTime.now());
//            } else {
//                NotificationUtils.notificationError("文件：" + res[1] + " 不存在，请确认！");
//            }
            if ("".equals(inputOrderNo) || cbOrder.getValue() == null) {
                NotificationUtils.notificationError("请输入有效的工单号！");
                return;
            }
            selectPDFOutputDialog.setObject(cbOrder.getValue());
            selectPDFOutputDialog.show(getUI(), new DialogCallBack() {

                @Override
                public void done(ConfirmResult result) {
                }
            });
        } else if (btnCOC.equals(button)) {
            if ("".equals(inputOrderNo) || cbOrder.getValue() == null) {
                NotificationUtils.notificationError("请输入有效的工单号！");
                return;
            }
            String rootUrl = VaadinUtils.getRootURL();
            VaadinUtils.setPageLocation(rootUrl + "Cameron#!CertificateOfConformance/" + inputOrderNo);
        } else if (btnSubWo.equals(button)) {
            subOrderSelectDialog.setObject(subWo);
            subOrderSelectDialog.show(getUI(), result -> {
            });
        }
    }

//    public String[] getWordMergePath(String inputOrderNo) {
//        String fileMergePath = "";
//        Boolean flag = true;
//        String[] res = new String[2];
////		String fileMergePath = "D:\\TestWord\\Output6.docx";
//        List<String> pathList = new ArrayList<String>();
//        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
//        if (caConfig != null) {
//            fileMergePath = caConfig.getConfigValue();
//            if (Strings.isNullOrEmpty(fileMergePath)) {
//                res[0] = "NG";
//                res[1] = "配置路径";
//                return res;
//            }
//            fileMergePath = fileMergePath + AppConstant.PDF + inputOrderNo + ".doc";
//        }
//        //客户要求此页放到第一页
//        //获取终检的文件路径
//        pathList.add(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT + inputOrderNo + ".doc");
//        //获取装配的文件路径
//        if (chbAssembling.getValue()) {
//            List<Assembling> assemblingList = assemblingService.getByOrderNo(inputOrderNo);
//            if (assemblingList != null && assemblingList.size() > 0) {
//                List<Assembling> assemblingListTemp = removeDuplicateAssemb(assemblingList);
//                for (Assembling assembling : assemblingListTemp) {
//                    pathList.add(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT + assembling.getSnBatch() + ".doc");
//                }
//
//            }
//        }
//        //获取工序Routing确认报告
//        File routeFile = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.GX_RECORD + inputOrderNo + ".doc");
//        if (routeFile.exists()) {
//            pathList.add(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.GX_RECORD + inputOrderNo + ".doc");
//        } else {
//            NotificationUtils.notificationError("工单对应工序确认报告尚未生成，请先到‘工序记录’界面生成确认报告！");
//        }
//
//        //获取压力的文件路径
//        if (chbPressure.getValue()) {
//            List<PressureTest> pressureTestList = pressureTestService.getPressureTestByOrder(inputOrderNo);
//            if (pressureTestList != null && pressureTestList.size() > 0) {
//                for (PressureTest pressureTest : pressureTestList) {
//                    pathList.add(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT + pressureTest.getProductSN() + ".doc");
//                }
//            } else {
//                res[0] = "NG";
//                res[1] = "获取工单号为：" + inputOrderNo + " 的压力测试导出文件";
//                return res;
//            }
//        }
//        //获取喷漆的文件路径
//        if (chbPaint.getValue()) {
//            pathList.add(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT + inputOrderNo + ".doc");
//        }
//
//        for (String filePath : pathList) {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                flag = false;
//                res[0] = "NG";
//                res[1] = filePath;
//                break;
//            }
//        }
//        if (flag) {
//            com.spire.doc.Document document = new com.spire.doc.Document(pathList.get(0));
//            System.out.println("0*****" + pathList.get(0));
//            for (int i = 1; i < pathList.size(); i++) {
//                System.out.println(i + "*****" + pathList.get(i));
//
//                document.insertTextFromFile(pathList.get(i), FileFormat.Docx_2013);
//            }
//            document.saveToFile(fileMergePath, FileFormat.Docx_2013);
//            res[0] = "OK";
//            res[1] = fileMergePath;
//        }
//        return res;
//    }

    /**
     * 去掉获取的重复的装配序列号
     *
     * @param assemblings
     * @return
     */
    private ArrayList<Assembling> removeDuplicateAssemb(List<Assembling> assemblings) {
        Set<Assembling> set = new TreeSet<Assembling>(new Comparator<Assembling>() {
            @Override
            public int compare(Assembling o1, Assembling o2) {
                return (o1.getSnBatch()).compareTo(o2.getSnBatch());
            }
        });
        set.addAll(assemblings);
        return new ArrayList<Assembling>(set);
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
            Dispatch.call(doc, "SaveAs", overFile, wdFormatPDF);
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

//        final Document document = new Document(filePath);
//        final String pdfFile = filePath.replace(".doc", ".pdf");
//        document.saveToFile(pdfFile, FileFormat.PDF);
        try {
            //加入受控章
            PdfReader readerOriginalDoc = new PdfReader(overFile);
            File fromFile = new File(overFile);
            PdfWriter writeDest = new PdfWriter(overFile.replace(".pdf", "-2.pdf"));
            File toFile = new File(overFile.replace(".pdf", "-2.pdf"));
            PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
            Document document2 = new Document(newDoc);
            ImageData imageData = ImageDataFactory.create("D:\\CameronQualityFiles\\DOCS\\CONTROLLED.png");
            Image image = new Image(imageData);
            image.scaleToFit(100, 100);
            image.setRotationAngle(0.5);
            image.setFixedPosition(1, 350, 400);
            document2.add(image);
            document2.close();
            fromFile.delete();
            toFile.renameTo(fromFile);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveDataToFinalInspectionResult(String inputOrderNo) {
        finalInspectionResult = finalInspectionResultService.getByOrderNo(inputOrderNo);
        if (finalInspectionResult == null) {
            finalInspectionResult = new FinalInspectionResult();
        }
        String saleOrderNo = tfSalesNo.getValue().trim();
        String consumer = tfCustomer.getValue().trim();
        String reportNo = tfReportNo.getValue().trim();
        finalInspectionResult.setOrderNo(inputOrderNo);
        finalInspectionResult.setSaleOrderNo(saleOrderNo);
        finalInspectionResult.setConsumer(consumer);
        finalInspectionResult.setQaConfirmDate(new Date());
        finalInspectionResult.setQaConfirmUser(RequestInfo.current().getUserName());
        finalInspectionResult.setReportNo(reportNo);

        finalInspectionResultService.save(finalInspectionResult);
    }

    public String checkSavingValueIsNull() {
        String notification = "";

        String saleOrderNo = tfSalesNo.getValue().trim();
        String consumer = tfCustomer.getValue().trim();
//		String orderNo = finalInspectionResult.getOrderNo();
        String routePackageResult = finalInspectionResult.getRoutePackageResult();
        String assemblingRecorderResult = finalInspectionResult.getAssemblingRecorderResult();
        String pressureTestReportResult = finalInspectionResult.getPressureTestReportResult();
        String MTRMetrialReportResult = finalInspectionResult.getMTRMetrialReportResult();
        String dimensionalReportResult = finalInspectionResult.getDimensionalReportResult();
        String hardnessReportResult = finalInspectionResult.getHardnessReportResult();
        String visualExaminationResult = finalInspectionResult.getVisualExaminationResult();
        String functionInspectionResult = finalInspectionResult.getFunctionInspectionResult();
        String monogrammingStatusResult = finalInspectionResult.getMonogrammingStatusResult();

        String[] arrValue = new String[]{routePackageResult, assemblingRecorderResult, pressureTestReportResult, MTRMetrialReportResult,
                dimensionalReportResult, hardnessReportResult, visualExaminationResult, functionInspectionResult, monogrammingStatusResult, consumer, saleOrderNo};

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

//    public boolean checkisSavedOrNot(List<FinalInspectionResult> finalInspectionResultList) {
//        boolean flag = true;
//        if (finalInspectionResultList != null && finalInspectionResultList.size() > 0) {
//            for (FinalInspectionResult finalInspectionResult : finalInspectionResultList) {
//                Date qaDate = finalInspectionResult.getQaConfirmDate();
//                String qaUser = finalInspectionResult.getQaConfirmUser();
//                if (qaDate != null || qaUser != null) {
//                    flag = false;
//                    break;
//                }
//            }
//        }
//        return flag;
//    }

//    public boolean checkisSavedOrNotQC(List<FinalInspectionResult> finalInspectionResultList) {
//        boolean flag = true;
//        if (finalInspectionResultList != null && finalInspectionResultList.size() > 0) {
//            for (FinalInspectionResult finalInspectionResult : finalInspectionResultList) {
//                Date qcDate = finalInspectionResult.getQcConfirmDate();
//                String qcUser = finalInspectionResult.getQcConfirmUser();
//                if (qcDate != null || qcUser != null) {
//                    flag = false;
//                    break;
//                }
//            }
//        }
//        return flag;
//    }

    public Media getMedia(String path) {
        Media media = new Media();
        //-----------预览获取pdf文件数据 到对象Media   "C:\\Users\\bruce_yang\\Desktop\\testpdf.pdf"
        File file = new File(path);
        media.setCategory("SupplierEnquiry");
        media.setName(file.getName());
        media.setType("pdf");
        try {
            media.setMediaContent(ByteStreams.toByteArray(new ByteArrayInputStream(FileUtils.readFileToByteArray(file))));
        } catch (IOException e) {
            throw new PlatformException(e);
        }
        //-----------预览获取pdf文件数据 到对象Media
        return media;
    }

    public void createReport(FinalInspectionResult finalInspectionResult, List<AppearanceInstrumentationResult> appearanceInstrumentationResultList,
                             List<FunctionDetectionResult> functionDetectionResultList, String path) throws Exception {
        if (finalInspectionResult != null) {
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("assyNo", tfAssyNo.getValue().split(" REV")[0].trim());//partNOAndRev sparePart.getSparePartNo()+"/"+sparePart.getSparePartRev()
            params.put("productRev", tfAssyNo.getValue().split(" REV")[1].trim());
            params.put("assySerialNo", tfAssySerialNo.getValue().trim());
            params.put("description", tfDescription.getValue().trim().replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                    .replaceAll("'", "&apos;"));
            params.put("productionOrder", cbOrder.getValue().getProductOrderId());
            params.put("qpNo", tfQpNo.getValue().split(" REV")[0].trim());//productInformation.getPaintingSpecificationFile()
            params.put("qpRev", tfQpNo.getValue().split(" REV")[1].trim());//productInformation.getPaintingSpecificationFileRev()
            params.put("salesNo", finalInspectionResult.getSaleOrderNo() == null ? "" : finalInspectionResult.getSaleOrderNo());
            params.put("customer", finalInspectionResult.getConsumer() == null ? "" : finalInspectionResult.getConsumer());
            params.put("ReportNo", tfReportNo.getValue().trim());

            params.put("routePackageResult", finalInspectionResult.getRoutePackageResult() == null ? "" : finalInspectionResult.getRoutePackageResult());//工作线路包
            params.put("assemblingRecorderResult", finalInspectionResult.getAssemblingRecorderResult() == null ? "" : finalInspectionResult.getAssemblingRecorderResult());//装配
            params.put("pressureTestReportResult", finalInspectionResult.getPressureTestReportResult() == null ? "" : finalInspectionResult.getPressureTestReportResult());//压力
            params.put("MTRMetrialReportResult", finalInspectionResult.getMTRMetrialReportResult() == null ? "" : finalInspectionResult.getMTRMetrialReportResult());//MTR
            params.put("dimensionalReportResult", finalInspectionResult.getDimensionalReportResult() == null ? "" : finalInspectionResult.getDimensionalReportResult());//尺寸报告
            params.put("hardnessReportResult", finalInspectionResult.getHardnessReportResult() == null ? "" : finalInspectionResult.getHardnessReportResult());//硬度

//	        params.put("visualExaminationResult", finalInspectionResultList.getVisualExaminationResult());//外观检验
//	        params.put("functionInspectionResult", finalInspectionResultList.getFunctionInspectionResult());//功能检验

            params.put("monogrammingStatusResult", finalInspectionResult.getMonogrammingStatusResult() == null ? "" : finalInspectionResult.getMonogrammingStatusResult());//会标是否应用且正确
            params.put("commentsResult", finalInspectionResult.getCommentsResult() == null ? "" : finalInspectionResult.getCommentsResult());//other
            params.put("AndDate", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(finalInspectionResult.getCreateTime()));
            params.put("Date", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(finalInspectionResult.getCreateTime()));
            for (int i = 0; i < 36; i++) {
                String sn = (i + 1) + "";
                if (i < 9) {
                    sn = "0" + (i + 1);
                }
                params.put("S" + sn, sn);
                if (appearanceInstrumentationResultList != null && i < appearanceInstrumentationResultList.size()) {
                    AppearanceInstrumentationResult appearanceInstrumentationResult = appearanceInstrumentationResultList.get(i);
                    String snItem = appearanceInstrumentationResult.getSn();
                    if (!Strings.isNullOrEmpty(snItem)) {
//                        String sn = snItem.substring(snItem.length() - 2);

                        params.put("V" + sn, Math.round(appearanceInstrumentationResult.getPaintingThicknessResult()));//外观检验
                    }
                } else {
                    if (i < cbOrder.getValue().getProductNumber()) {
                        params.put("V" + sn, "OK");//自动填入OK
                    } else {
                        params.put("V" + sn, "");//外观检验
                    }
                }
            }
            for (int i = 0; i < 5; i++) {
                //报告中只有5个格子       只取前5个数据
                if (functionDetectionResultList != null && i < functionDetectionResultList.size()) {
                    FunctionDetectionResult functionDetectionResult = functionDetectionResultList.get(i);
                    String snItem = functionDetectionResult.getSn();
                    if (!Strings.isNullOrEmpty(snItem)) {
                        String sn = snItem.split("_")[1];//01
                        params.put("Item" + (i + 1), sn);//功能检验
                        params.put("Value" + (i + 1), functionDetectionResult.getFunctionInspectionResult());//功能检验
                    }
                } else {
                    params.put("Item" + (i + 1), "");//功能检验
                    params.put("Value" + (i + 1), "");//功能检验
                }
            }
            params.put("FunctionalTest", finalInspectionResult.getFunctionInspectionResult() == null ? "" : finalInspectionResult.getFunctionInspectionResult());
//	    	try {
            BASE64Encoder encoder = new BASE64Encoder();
            Media iqaImageQC = null;
            Media iqaImageQA = null;
            iqaImageQC = mediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(), finalInspectionResult.getQcConfirmUser());
//				List<FinalInspectionResult> finalInspectionResultQAList = finalInspectionResultService.getFinalInspectionResultByOrderNo(orderNo,"QA");
//				if(finalInspectionResultQAList!=null && finalInspectionResultQAList.size()>0) {
//					FinalInspectionResult finalInspectionResultQA = finalInspectionResultQAList.get(0);
//					userNameQA = finalInspectionResultQA==null?"":finalInspectionResultQA.getQaConfirmUser();
//				}
            String userNameQA = RequestInfo.current().getUserName();
            iqaImageQA = Strings.isNullOrEmpty(userNameQA) ? null : mediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(), userNameQA);
            if (iqaImageQA == null && !"".equals(userNameQA)) {
                throw new PlatformException("工单：" + cbOrder.getValue().getProductOrderId() + " 的QA权限人员" + userNameQA + " 没有配置电子签名！");
            }
            if (iqaImageQC == null) {
                throw new PlatformException("工单：" + cbOrder.getValue().getProductOrderId() + " 的QC权限人员" + finalInspectionResult.getQcConfirmUser() + " 没有配置电子签名！");
            }
            params.put("operatorQA", encoder.encode(inputStream2byte(iqaImageQA.getMediaStream())));
            params.put("operatorQC", encoder.encode(inputStream2byte(iqaImageQC.getMediaStream())));
            // byte[] imageBytes = inputStream2byte(iqaImage.getMediaStream());
            // dataMap.put("imageStr", encoder.encode(imageBytes));

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            configuration.setDirectoryForTemplateLoading(new File(AppConstant.DOC_XML_FILE_PATH));//C:\\Users\\bruce_yang\\Desktop\\喀麦隆文件

            String tempName;
            if (functionDetectionResultList != null && functionDetectionResultList.size() > 0) {
                tempName = "QCInspection.xml";
            } else {
                tempName = "QCInspection-NoFT.xml";
            }
            Template template = configuration.getTemplate(tempName, "utf-8");

            //输出文件
            File outFile = new File(path + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT + finalInspectionResult.getOrderNo() + "-0.doc");//C:\\Users\\bruce_yang\\Desktop\\喀麦隆文件\\生成的word.doc

            // 鍑嗗OutputStream
            //			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                    1024 * 1024);
            template.process(params, out);
            out.flush();
            out.close();
            wordToPDF(path + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT + finalInspectionResult.getOrderNo() + "-0.doc");
            if (cbOrder.getValue().getProductNumber() > 36) {//工单件数大于36，多个首页
                int count = (int) Math.ceil((double) (cbOrder.getValue().getProductNumber()) / (double) 36);
                for (int j = 1; j < count; j++) {
                    for (int i = 0; i < 36; i++) {
                        String sn = (i + 1) + "";
                        if (i < 9) {
                            sn = "0" + (i + 1);
                        }
                        String sn2 = (i + 1 + 36 * j) + "";
                        params.replace("S" + sn, sn2);
                        if (appearanceInstrumentationResultList != null && i + 36 * j < appearanceInstrumentationResultList.size()) {
                            AppearanceInstrumentationResult appearanceInstrumentationResult = appearanceInstrumentationResultList.get(i + 36 * j);
                            String snItem = appearanceInstrumentationResult.getSn();
                            if (!Strings.isNullOrEmpty(snItem)) {
//                        String sn = snItem.substring(snItem.length() - 2);
                                params.replace("V" + sn, Math.round(appearanceInstrumentationResult.getPaintingThicknessResult()));//外观检验
                            }
                        } else {
                            if (i + 36 * j < cbOrder.getValue().getProductNumber()) {
                                params.replace("V" + sn, "OK");//自动填入OK
                            } else {
                                params.replace("V" + sn, "");//外观检验
                            }
                        }
                    }
                    outFile = new File(path + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT + finalInspectionResult.getOrderNo() + "-" + j + ".doc");
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                            1024 * 1024);
                    template.process(params, out);
                    out.flush();
                    out.close();
                    wordToPDF(path + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT + finalInspectionResult.getOrderNo() + "-" + j + ".doc");
                }
            }
            NotificationUtils.notificationInfo("QC检验导出成功！");
//			} catch (Exception e) {
//				e.printStackTrace();
//				NotificationUtils.notificationError("导出文件出现异常："+e.getMessage());
//			}
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
}