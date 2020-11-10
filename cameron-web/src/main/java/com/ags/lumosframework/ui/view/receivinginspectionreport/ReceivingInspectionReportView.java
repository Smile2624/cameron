package com.ags.lumosframework.ui.view.receivinginspectionreport;

import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.Role;
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
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
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
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Menu(caption = "ReceivingInspection", captionI18NKey = "view.receivinginspection.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 5)
@SpringView(name = "ReceivingInspection", ui = CameronUI.class)
public class ReceivingInspectionReportView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = -7725333496173310504L;
    private static final int wdFormatPDF = 17;// PDF 格式
    VerticalLayout hlToolBox = new VerticalLayout();
    Panel inspectionValue = new Panel();
    VerticalLayout vlDisplay = new VerticalLayout();
    HorizontalLayout hlTemp1 = new HorizontalLayout();
    HorizontalLayout hlTemp2 = new HorizontalLayout();
    List<DimensionRuler> listInstance = null;
    @Autowired
    IDimensionInspectionService dimensionInspectionService;
    @Autowired
    IDimensionRulerService dimensionRulerService;
    StringBuilder returnMessage = new StringBuilder();
    SocketClient client = null;
    PurchasingOrderInfo purchasingOrderInfo = null;
    Hardness hardness = null;
    List<Role> role = null;
    private ComboBox<String> cbPurchasingNo = new ComboBox();
    @I18Support(caption = "Confirm", captionKey = "common.confirm")
    private Button btnConfirm = new Button();
    @I18Support(caption = "Upload PDF Attachment", captionKey = "common.uploadPdf")
    private UploadButton btnUploadPdf = new UploadButton();
    private UploadFinishEvent uploadEvent = null;
    @I18Support(caption = "View PDF Attachment", captionKey = "common.viewPdf")
    private Button btnViewPdf = new Button();
    @I18Support(caption = "Start", captionKey = "common.start")
    private Button btnStart = new Button();
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
    private AbstractComponent[] components = new AbstractComponent[]{cbPurchasingNo, btnConfirm, btnUploadPdf, btnViewPdf};// btnStart,
    @Autowired
    private IPurchasingOrderService purchasingOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private IHardnessTestReportService hardnessTestReportService;
    @Autowired
    private UserService userService;
    @Autowired
    private IReceivingInspectionService receivingInspectionService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private PDFPreviewDialog PDFPreviewDialog;
    @Autowired
    private SelectPdfDialog selectPdfDialog;
    @Autowired
    private IHardnessService hardnessService;
    private CaConfig caConfig;
    // x,y 当前数据输入位置
    // 选中的checkBox序号
    private String materialNo = "";
    private String materialRev = "";
    private List<String> inspectionItems = new ArrayList<>();
    private boolean qcChecked = false;
    private boolean qaChecked = false;
    private boolean canInspection = true;
    private String loginUserName = "";

    public ReceivingInspectionReportView() {
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
        btnStart.setEnabled(false);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);

        FileUploader fileUploader = new FileUploader(new UploadFinishedListener() {
            @Override
            public void finish(UploadFinishEvent event) {
                uploadEvent = event;
            }
        });

        btnUploadPdf.setIcon(VaadinIcons.UPLOAD);
        btnUploadPdf.setImmediateMode(true);
        btnUploadPdf.setReceiver(fileUploader);
        btnUploadPdf.addSucceededListener(fileUploader);
        btnUploadPdf.addFinishedListener(new Upload.FinishedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                try {
                    if (uploadEvent.getFileName().endsWith(".pdf") || uploadEvent.getFileName().endsWith(".PDF")) {
                        uploadPDF(uploadEvent.getUploadFile(), uploadEvent.getFileName());
                        NotificationUtils.notificationInfo("成功上传" + uploadEvent.getFileName());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("上传发生异常");
                }
            }
        });

        btnViewPdf.setIcon(VaadinIcons.FILE);
        btnViewPdf.addClickListener(this);
        cbPurchasingNo.setPlaceholder("采购单号");
//		KeyAction ka = new KeyAction(KeyCode.ENTER, new int[] {});
//		ka.addKeypressListener(new KeyActionListener() {
//
//			private static final long serialVersionUID = -3220687710828989615L;
//
//			@Override
//			public void keyPressed(KeyActionEvent keyPressEvent) {
//				// 清空当前信息
//				// gridObject.setItems(new ArrayList<>());
//				String purchasingNo = cbPurchasingNo.getValue().trim();
//				if (!Strings.isNullOrEmpty(purchasingNo)) {
//					List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
//							.getUncheckedOrder(purchasingNo,"RECEIVING");
//					if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
//						// 数据填入grid
//						gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
//					} else {
//						NotificationUtils
//								.notificationError(I18NUtility.getValue("view.receivinginspection.purchasingnotexist",
//										" PurchasingOrder: " + purchasingNo
//												+ " not exist."));
//					}
//				} else {
//					NotificationUtils.notificationError(I18NUtility
//							.getValue("view.receivinginspection.purchasingnotnull", "PurchasingNo can't be null"));
//				}
//			}
//		});
//		ka.extend(cbPurchasingNo);
        cbPurchasingNo.addValueChangeListener(new ValueChangeListener<String>() {
            private static final long serialVersionUID = -9044784139704252186L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                if (!event.getValue().equals(event.getOldValue())) {
                    String purchasingNo = cbPurchasingNo.getValue().trim();
                    if (!Strings.isNullOrEmpty(purchasingNo)) {
                        List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
                                .getUncheckedOrder(purchasingNo, "RECEIVING");
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
        btnUploadPdf.setEnabled(false);
        btnViewPdf.setEnabled(false);
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

            private static final long serialVersionUID = -454658178418921022L;

            @Override
            public void selectionChange(SelectionEvent<PurchasingOrderInfo> event) {
                qaChecked = false;
                qcChecked = false;
                btnConfirm.setEnabled(true);
                btnUploadPdf.setEnabled(true);
                btnViewPdf.setEnabled(true);
                initComponent();
                TextField tfVisualRlt = (TextField) gridLayout.getComponent(1, 1);
//                TextField tfHardnessRlt = (TextField) gridLayout.getComponent(1, 2);
//                TextField tfDimensionRlt = (TextField) gridLayout.getComponent(1, 3);
                TextField tfHardnessRlt = (TextField) (((HorizontalLayout) gridLayout.getComponent(1, 2)).getComponent(0));
                TextField tfDimensionRlt = (TextField) (((HorizontalLayout) gridLayout.getComponent(1, 3)).getComponent(0));
                TextField tfTraceabilityRlt = (TextField) gridLayout.getComponent(1, 4);
                TextField tfDocumentRlt = (TextField) (((HorizontalLayout) gridLayout.getComponent(1, 5)).getComponent(0));
                TextField tfPackingRlt = (TextField) gridLayout.getComponent(1, 6);
                TextField tfCertificationRlt = (TextField) gridLayout.getComponent(1, 7);
                TextField tfOthersRlt = (TextField) gridLayout.getComponent(1, 8);
                TextArea tfComment = (TextArea) gridLayout.getComponent(1, 9);
                if (event.getFirstSelectedItem().isPresent()) {
                    btnConfirm.setEnabled(true);
                    btnUploadPdf.setEnabled(true);
                    btnViewPdf.setEnabled(true);
                    purchasingOrderInfo = event.getFirstSelectedItem().get();
                    materialNo = purchasingOrderInfo.getMaterialNo();
                    materialRev = purchasingOrderInfo.getMaterialRev();
                    setDataToDisplayArea(purchasingOrderInfo);
                    // 在右边的gridLayout中加载改采购订单的信息，首先确认是否已经有过检验记录，因为这里qc跟qa是分开检验的
                    ReceivingInspection receivingInspection = receivingInspectionService
                            .getBySapLotNo(purchasingOrderInfo.getSapInspectionLot());
                    if (receivingInspection == null) {
                        // 此时qa不可编辑
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

                        // 硬度
//                        if (Strings.isNullOrEmpty(part.getHardnessFile()) || (hardnessService.getByHardnessFile(part.getHardnessFile()) == null)) {
//                            // 不需要硬度检验
//                            ((TextField) gridLayout.getComponent(1, 2)).setValue("NA");
//                        } else {
                        if (purchasingOrderInfo.getHardnessChecked()) {
//                            ((TextField) gridLayout.getComponent(1, 2))
//                                    .setValue(purchasingOrderInfo.getHardnessCheckedRlt());
                            ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 2)).getComponent(0))
                                    .setValue(purchasingOrderInfo.getHardnessCheckedRlt());
                        } else {
//                            ((TextField) gridLayout.getComponent(1, 2))
                            ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 2)).getComponent(0))
                                    .setValue("NA");
                            NotificationUtils.notificationInfo("没有硬度检验结果");
//                                NotificationUtils.notificationError("所选单号硬度检验还未完成，请先完成硬度检验");
//                                btnConfirm.setEnabled(false);
//                                btnViewPdf.setEnabled(false);
//                                btnUploadPdf.setEnabled(false);
//                                return;
                        }
//                        }
                        // 尺寸
//                        List<DimensionRuler> list = dimensionRulerService.getByNoRev(materialNo, materialRev);
//                        if (list == null || list.size() == 0) {
//                            // 不需要进行尺寸检验
//                            ((TextField) gridLayout.getComponent(1, 3)).setValue("NA");
//                        } else {
                        if (purchasingOrderInfo.getDimensionChecked()) {
//                            ((TextField) gridLayout.getComponent(1, 3))
                            ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 3)).getComponent(0))
                                    .setValue(purchasingOrderInfo.getDimensionCheckedRlt());
                        } else {
//                            ((TextField) gridLayout.getComponent(1, 3))
                            ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 3)).getComponent(0))
                                    .setValue("NA");
                            NotificationUtils.notificationInfo("没有尺寸检验结果");
                            // 需要尺寸检验但是还没有检，此时不可操作
//                                NotificationUtils.notificationError("所选单号尺寸检验还未完成，请先完成尺寸检验");
//                                btnConfirm.setEnabled(false);
//                                btnViewPdf.setEnabled(false);
//                                btnUploadPdf.setEnabled(false);
//                                return;
                        }
//                        }

                        if (!role.contains(roleService.getByName("qc"))) {
                            tfVisualRlt.setEnabled(false);
                            tfHardnessRlt.setEnabled(false);
                            tfDimensionRlt.setEnabled(false);
                            tfTraceabilityRlt.setEnabled(false);
                            tfDocumentRlt.setEnabled(false);
                            tfPackingRlt.setEnabled(false);
                            tfCertificationRlt.setEnabled(false);
                            tfOthersRlt.setEnabled(false);
                            tfComment.setEnabled(false);
                            btnConfirm.setEnabled(false);
                            btnViewPdf.setEnabled(false);
                            btnUploadPdf.setEnabled(false);
                        } else {
                            tfDocumentRlt.setEnabled(false);
                            tfPackingRlt.setEnabled(false);
                            tfCertificationRlt.setEnabled(false);
                            tfOthersRlt.setEnabled(false);
                            // tfComment.setEnabled(false);
                        }
                    } else {
                        qcChecked = true;
                        lblSN.setValue(receivingInspection.getSN() == null ? "" : receivingInspection.getSN());
                        lblPSL.setValue(receivingInspection.getPsl() == null ? "" : receivingInspection.getPsl());
                        tfVisualRlt.setValue(receivingInspection.getVisualResult());
//                        tfVisualRlt.setEnabled(false);
                        tfHardnessRlt.setValue(receivingInspection.getHardnessResult());
//                        tfHardnessRlt.setEnabled(false);
                        tfDimensionRlt.setValue(receivingInspection.getDimensionResult());
//                        tfDimensionRlt.setEnabled(false);
                        tfTraceabilityRlt.setValue(receivingInspection.getTraceabilityResult());
//                        tfTraceabilityRlt.setEnabled(false);
                        tfComment.setValue(receivingInspection.getComment());

                        if (!Strings.isNullOrEmpty(receivingInspection.getQaChecker())) {
                            qaChecked = true;
                            // qa未检，qc
                            tfDocumentRlt.setValue(receivingInspection.getDocumentResult());
//                            tfDocumentRlt.setEnabled(false);
                            tfPackingRlt.setValue(receivingInspection.getPackingResult());
//                            tfPackingRlt.setEnabled(false);
                            tfCertificationRlt.setValue(receivingInspection.getCertificationResult());
//                            tfCertificationRlt.setEnabled(false);
                            tfOthersRlt.setValue(receivingInspection.getOtherResult());
//                            tfOthersRlt.setEnabled(false);
//                            tfComment.setEnabled(false);
//                            btnConfirm.setEnabled(false);
//                            btnViewPdf.setEnabled(false);
//                            btnUploadPdf.setEnabled(false);
                        } //else {
                        if (!role.contains(roleService.getByName("qa"))) {
                            tfDocumentRlt.setEnabled(false);
                            tfPackingRlt.setEnabled(false);
                            tfCertificationRlt.setEnabled(false);
                            tfOthersRlt.setEnabled(false);
                            tfComment.setEnabled(false);
                            btnConfirm.setEnabled(false);
                            btnViewPdf.setEnabled(false);
                            btnUploadPdf.setEnabled(false);
                        }
//                        }
                    }
                } else {
                    btnConfirm.setEnabled(false);
                    btnViewPdf.setEnabled(false);
                    btnUploadPdf.setEnabled(false);
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
        for (int rowIndex = 1; rowIndex < 10; rowIndex++) {
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

                        private static final long serialVersionUID = 1111138042919081489L;

                        @Override
                        public void valueChange(ValueChangeEvent<String> event) {
                            if ("ok".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_GREEN);
                            } else if ("ng".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_RED);
                            } else if ("na".equalsIgnoreCase(event.getValue())) {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_ORANGE);
                            } else {
                                event.getComponent().setStyleName(CoreTheme.BACKGROUND_WHITE);
                            }
                        }
                    });
                    if (rowIndex == 9) {
                        TextArea taComment = new TextArea();
                        taComment.setSizeFull();
                        taComment.setHeight("50");
                        gridLayout.addComponent(taComment, columnIndex, rowIndex);
                    } else if (rowIndex == 2 || rowIndex == 3 || rowIndex == 5) {
                        Button btnView = new Button();
                        HorizontalLayout hlLayout = new HorizontalLayout();
                        hlLayout.setSizeFull();
                        btnView.setIcon(VaadinIcons.VIEWPORT);
                        btnView.setHeight("100%");
                        btnView.setCaption(I18NUtility.getValue("common.preview", "Preview"));
                        if (rowIndex == 2) {
                            btnView.addClickListener(new ClickListener() {

                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(ClickEvent event) {

                                    String fileName = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot();
                                    VaadinUtils.setPageLocation("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\REPORT\\MaterialInspection\\Hardness\\" + fileName + ".pdf", true);
                                }
                            });
                        }
                        if (rowIndex == 3) {
                            btnView.addClickListener(new ClickListener() {

                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(ClickEvent event) {

                                    String fileName = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot();
                                    VaadinUtils.setPageLocation("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\REPORT\\MaterialInspection\\Dimension\\" + fileName + ".pdf", true);
                                }
                            });
                        }
                        if (rowIndex == 5) {
                            btnView.addClickListener(new ClickListener() {

                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(ClickEvent event) {

                                    String fileName = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot();
                                    VaadinUtils.setPageLocation("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\MTR\\" + fileName + ".pdf", true);
                                }
                            });
                        }
                        hlLayout.addComponents(tfResult, btnView);
                        hlLayout.setComponentAlignment(btnView, Alignment.MIDDLE_RIGHT);
                        hlLayout.setExpandRatio(tfResult, 2);
                        hlLayout.setExpandRatio(btnView, 1);
                        gridLayout.addComponent(hlLayout, columnIndex, rowIndex);
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

    public void createReport(ReceivingInspection receivingInspection, String path) {
        path = path + AppConstant.MATERIAL_PREFIX + AppConstant.RECEIVING_REPORT;
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
            dataMap.put("visualRlt", receivingInspection.getVisualResult());
            dataMap.put("hardnessRlt", receivingInspection.getHardnessResult());
            dataMap.put("dimensionRlt", receivingInspection.getDimensionResult());
            dataMap.put("traceabilityRlt", receivingInspection.getTraceabilityResult());
            dataMap.put("documentRlt", receivingInspection.getDocumentResult());
            dataMap.put("packRlt", receivingInspection.getPackingResult());
            dataMap.put("certificationRlt", receivingInspection.getCertificationResult());
            dataMap.put("otherRlt", receivingInspection.getOtherResult());
            dataMap.put("comment", receivingInspection.getComment());
            dataMap.put("date", receivingInspection.getQcConfirmDate());
            dataMap.put("date1", receivingInspection.getQaConfirmDate());
            dataMap.put("date2", receivingInspection.getQaConfirmDate());
//			dataMap.put("date", new SimpleDateFormat("yyyy/MMdd").format(new Date()));

            BASE64Encoder encoder = new BASE64Encoder();
            Media qcmediaImage = caMediaService.getByTypeName("ES", receivingInspection.getQcChecker());
            if (qcmediaImage == null) {
                NotificationUtils.notificationError("当前没有配置:" + receivingInspection.getQcChecker() + "的电子签名，请首先配置该用户的电子签名");
                return;
            }
            dataMap.put("qcChecker", encoder.encode(inputStream2byte(qcmediaImage.getMediaStream())));
            BASE64Encoder encoder1 = new BASE64Encoder();
            Media qamediaImage = caMediaService.getByTypeName("ES", receivingInspection.getQaChecker());
            if (qamediaImage == null) {
                NotificationUtils.notificationError("当前没有配置:" + receivingInspection.getQaChecker() + "的电子签名，请首先配置该用户的电子签名");
                return;
            }
            dataMap.put("qaChecker", encoder1.encode(inputStream2byte(qamediaImage.getMediaStream())));

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            String jPath = AppConstant.DOC_XML_FILE_PATH;
            configuration.setDirectoryForTemplateLoading(new File(jPath));
            // 以utf-8的编码读取模板文件
            Template template = configuration.getTemplate("receiving.xml", "utf-8");

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
            fileConvertToPDF(fileName);
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
        inspectionItems.add("外观检验");
        inspectionItems.add("硬度检验");
        inspectionItems.add("尺寸检验");
        inspectionItems.add("追溯性检验");
        inspectionItems.add("文件包");
        inspectionItems.add("包装清单");
        inspectionItems.add("合格证");
        inspectionItems.add("其他");
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
            if (c instanceof HorizontalLayout) {
                TextField tfTemp = (TextField) (((HorizontalLayout) c).getComponent(0));
                tfTemp.setEnabled(true);
                tfTemp.setValue("");
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
            if (!userService.getByName(RequestInfo.current().getUserName()).getRole()
                    .contains(roleService.getByName("qa"))) {
                if (qcChecked) {
                    NotificationUtils.notificationInfo("qc检验已经完成，请勿重复提交");
                    return;
                }
                String visualRlt = ((TextField) gridLayout.getComponent(1, 1)).getValue();
                String hardnessRlt = ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 2)).getComponent(0))
                        .getValue();
                String dimensionlRlt = ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 3)).getComponent(0))
                        .getValue();
                String traceabilityRlt = ((TextField) gridLayout.getComponent(1, 4)).getValue();
                String comment = ((TextArea) gridLayout.getComponent(1, 9)).getValue();
                if (Strings.isNullOrEmpty(visualRlt) || Strings.isNullOrEmpty(hardnessRlt)
                        || Strings.isNullOrEmpty(dimensionlRlt) || Strings.isNullOrEmpty(traceabilityRlt)) {
                    ConfirmDialog.show(getUI(), "仍有需要填写信息未填写，是否保存", new DialogCallBack() {
                        @Override
                        public void done(ConfirmResult result) {
                            if (result.getResult().equals(Result.OK)) {
                                ReceivingInspection inspectionInstance = new ReceivingInspection();
                                inspectionInstance.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                                inspectionInstance.setPurchasingOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                                inspectionInstance.setPurchasingOrder(purchasingOrderInfo.getPurchasingNo());
                                inspectionInstance
                                        .setQcConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                                inspectionInstance.setQcChecker(RequestInfo.current().getUserName());
                                inspectionInstance.setVisualResult(visualRlt == null ? "" : visualRlt);
                                inspectionInstance.setHardnessResult(hardnessRlt == null ? "" : hardnessRlt);
                                inspectionInstance.setDimensionResult(dimensionlRlt == null ? "" : dimensionlRlt);
                                inspectionInstance
                                        .setTraceabilityResult(traceabilityRlt == null ? "" : traceabilityRlt);
                                inspectionInstance.setComment(comment == null ? "" : comment);
                                inspectionInstance.setPsl(lblPSL.getValue());
                                inspectionInstance.setSN(lblSN.getValue());
                                receivingInspectionService.save(inspectionInstance);
                                NotificationUtils.notificationInfo("检验信息保存成功");
                            }
                        }
                    });
                } else {
                    ReceivingInspection inspectionInstance = new ReceivingInspection();
                    inspectionInstance.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
                    inspectionInstance.setPurchasingOrderItem(purchasingOrderInfo.getPurchasingItemNo());
                    inspectionInstance.setPurchasingOrder(purchasingOrderInfo.getPurchasingNo());
                    inspectionInstance.setQcConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                    inspectionInstance.setQcChecker(RequestInfo.current().getUserName());
                    inspectionInstance.setVisualResult(visualRlt);
                    inspectionInstance.setHardnessResult(hardnessRlt);
                    inspectionInstance.setDimensionResult(dimensionlRlt);
                    inspectionInstance.setTraceabilityResult(traceabilityRlt);
                    inspectionInstance.setPsl(lblPSL.getValue());
                    inspectionInstance.setSN(lblSN.getValue());
                    inspectionInstance.setComment(comment == null ? "" : comment);
                    receivingInspectionService.save(inspectionInstance);
                    NotificationUtils.notificationInfo("检验信息保存成功");
                    purchasingOrderInfo = null;
                }
            } else if (userService.getByName(RequestInfo.current().getUserName()).getRole()
                    .contains(roleService.getByName("qa"))) {
                String visualRlt = ((TextField) gridLayout.getComponent(1, 1)).getValue();
                String hardnessRlt = ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 2)).getComponent(0))
                        .getValue();
                String dimensionlRlt = ((TextField) ((HorizontalLayout) gridLayout.getComponent(1, 3)).getComponent(0))
                        .getValue();
                String traceabilityRlt = ((TextField) gridLayout.getComponent(1, 4)).getValue();
                HorizontalLayout layout = ((HorizontalLayout) gridLayout.getComponent(1, 5));
                String documentlRlt = ((TextField) layout.getComponent(0)).getValue();
                String packingRlt = ((TextField) gridLayout.getComponent(1, 6)).getValue();
                String certificationRlt = ((TextField) gridLayout.getComponent(1, 7)).getValue();
                String otherRlt = ((TextField) gridLayout.getComponent(1, 8)).getValue();
                String comment = ((TextArea) gridLayout.getComponent(1, 9)).getValue();
                ReceivingInspection inspectionInstance = receivingInspectionService
                        .getBySapLotNo(purchasingOrderInfo.getSapInspectionLot());

                if (inspectionInstance != null) {
                    if (Strings.isNullOrEmpty(documentlRlt) || Strings.isNullOrEmpty(packingRlt)
                            || Strings.isNullOrEmpty(certificationRlt) || Strings.isNullOrEmpty(otherRlt)) {
                        ConfirmDialog.show(getUI(), "仍有需要填写信息未填写，是否保存", new DialogCallBack() {
                            @Override
                            public void done(ConfirmResult result) {
                                if (result.getResult().equals(Result.OK)) {
                                    inspectionInstance.setVisualResult(visualRlt == null ? "" : visualRlt);
                                    inspectionInstance.setHardnessResult(hardnessRlt == null ? "" : hardnessRlt);
                                    inspectionInstance.setDimensionResult(dimensionlRlt == null ? "" : dimensionlRlt);
                                    inspectionInstance
                                            .setTraceabilityResult(traceabilityRlt == null ? "" : traceabilityRlt);
                                    inspectionInstance.setDocumentResult(documentlRlt == null ? "" : documentlRlt);
                                    inspectionInstance.setPackingResult(packingRlt == null ? "" : packingRlt);
                                    inspectionInstance
                                            .setCertificationResult(certificationRlt == null ? "" : certificationRlt);
                                    inspectionInstance.setOtherResult(otherRlt == null ? "" : otherRlt);
                                    inspectionInstance.setComment(comment == null ? "" : comment);
                                    inspectionInstance.setQaChecker(RequestInfo.current().getUserName());
                                    inspectionInstance.setQaConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                                    inspectionInstance.setPsl(lblPSL.getValue());
                                    inspectionInstance.setSN(lblSN.getValue());
                                    receivingInspectionService.save(inspectionInstance);
                                }
                            }
                        });
                    } else {
                        inspectionInstance.setVisualResult(visualRlt == null ? "" : visualRlt);
                        inspectionInstance.setHardnessResult(hardnessRlt == null ? "" : hardnessRlt);
                        inspectionInstance.setDimensionResult(dimensionlRlt == null ? "" : dimensionlRlt);
                        inspectionInstance
                                .setTraceabilityResult(traceabilityRlt == null ? "" : traceabilityRlt);
                        inspectionInstance.setDocumentResult(documentlRlt);
                        inspectionInstance.setPackingResult(packingRlt);
                        inspectionInstance.setCertificationResult(certificationRlt);
                        inspectionInstance.setOtherResult(otherRlt);
                        inspectionInstance.setComment(comment == null ? "" : comment);
                        inspectionInstance.setQaChecker(RequestInfo.current().getUserName());
                        inspectionInstance.setQaConfirmDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                        inspectionInstance.setPsl(lblPSL.getValue());
                        inspectionInstance.setSN(lblSN.getValue());
                        receivingInspectionService.save(inspectionInstance);
                    }
                    // 保存完成后，生成报告文档
                    createReport(inspectionInstance, rootPath);
//                    // 将尺寸，硬度报告合并成一份
//                    mergeDocument(inspectionInstance.getSapInspectionNo());
                    mergePDF(inspectionInstance.getSapInspectionNo());
                    NotificationUtils.notificationInfo("进货检验完成");
                } else {
                    // qc还没有做检验
                    NotificationUtils.notificationError("请等待QC完成检验工作!");
                }
            } else {
                NotificationUtils.notificationError("只有QA,QC人员才可以操作，请联系管理员确认权限");
            }
            refesh();
        } else if (button.equals(btnViewPdf)) {
            selectPdfDialog.setObject(purchasingOrderInfo);
            selectPdfDialog.show(getUI(), (DialogCallBack) new DialogCallBack() {
                public void done(final ConfirmResult result) {
            }
            });
//            final File folder = new File("D:\\Attachment\\" + purchasingOrderInfo.getSapInspectionLot());
//            List<String> result = new ArrayList<>();
//            for (final File f : folder.listFiles()) {
//                if (f.isFile()) {
//                    result.add(f.getName());
//                    Page.getCurrent().open("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename="+ f.getAbsolutePath(), "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
//                }
//            }
        }
    }

//    public void mergeDocument(String sapLotNo) {
//        String rootPath = caConfig.getConfigValue();
//        String visualPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.VISUAL_REPORT;
//        String dimesionPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;
//        String hardnessPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.HARDNESS_INSPECTION_REPORT;
//        String receivingPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.RECEIVING_REPORT;
//        List<File> fileNameList = new ArrayList<>();
//
//        File fileReceiving = new File(receivingPath);
//        File[] fileListReceiving = fileReceiving.listFiles();
//        for (int i = 0; i < fileListReceiving.length; i++) {
//            if (fileListReceiving[i].isFile()) {
//                String fileName = fileListReceiving[i].getAbsolutePath();
//                if (fileName.contains(sapLotNo) && fileName.endsWith(".doc")) {
//                    fileNameList.add(fileListReceiving[i]);
//                    break;
//                }
//            }
//        }
//
//        File fileVisual = new File(visualPath);
//        File[] fileListVisual = fileVisual.listFiles();
//        for (int i = 0; i < fileListVisual.length; i++) {
//            if (fileListVisual[i].isFile()) {
//                String fileName = fileListVisual[i].getAbsolutePath();
//                if (fileName.contains(sapLotNo) && fileName.endsWith(".doc")) {
//                    fileNameList.add(fileListVisual[i]);
//                    break;
//                }
//            }
//        }
//
//        File fileDimension = new File(dimesionPath);
//        File[] fileListDimension = fileDimension.listFiles();
//        for (int i = 0; i < fileListDimension.length; i++) {
//            if (fileListDimension[i].isFile()) {
//                String fileName = fileListDimension[i].getAbsolutePath();
//                if (fileName.contains(sapLotNo) && fileName.endsWith(".doc")) {
//                    fileNameList.add(fileListDimension[i]);
//                }
//            }
//        }
//        File fileHardness = new File(hardnessPath);
//        File[] fileListHardness = fileHardness.listFiles();
//        for (int i = 0; i < fileListHardness.length; i++) {
//            if (fileListHardness[i].isFile()) {
//                String fileName = fileListHardness[i].getAbsolutePath();
//                if (fileName.contains(sapLotNo) && fileName.endsWith(".doc")) {
//                    fileNameList.add(fileListHardness[i]);
//                    break;
//                }
//            }
//        }
//
//        // 合并文档
//        int fileCount = fileNameList.size();
//        String fileName = fileNameList.get(0).getAbsolutePath();
//        Document document = new Document(fileName, FileFormat.Doc);
//
//        for (int index = 1; index < fileCount; index++) {
//            if (fileCount > 1) {
//                document.insertTextFromFile(fileNameList.get(index).getAbsolutePath(), FileFormat.Doc);
//            }
//        }
//        String filePath = rootPath + AppConstant.MATERIAL_PREFIX + purchasingOrderInfo.getPurchasingNo() + "-"
//                + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".doc";
//        document.saveToFile(filePath, FileFormat.Docx_2013);
//        fileConvertToPDF(filePath, rootPath);
//        deleteFiles(filePath);
//        //最后合成完成PDF即来料检验+供应商材料报告
//    }

    public void fileConvertToPDF(String filePath) {
        ComThread.InitSTA();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            // 转换前的文件路径
            String startFile = filePath;
            // 转换后的文件路径
            String overFile = filePath.replaceAll(".doc", ".pdf");

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


    public void mergePDF(String sapLotNo) {
        PDFMergerUtility mergePDF = new PDFMergerUtility();
        //发现文件
        List<File> fileNameList = new ArrayList<>();
        String rootPath = caConfig.getConfigValue();
        String pdfPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.RECEIVING_REPORT;//首页
        File filePDF = new File(pdfPath);
        File[] fileListPDF = filePDF.listFiles();
        for (int i = 0; i < fileListPDF.length; i++) {
            if (fileListPDF[i].isFile()) {
                String fileName = fileListPDF[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListPDF[i]);
                    break;
                }
            }
        }

        pdfPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.VISUAL_REPORT;//外观检测
        filePDF = new File(pdfPath);
        fileListPDF = filePDF.listFiles();
        for (int i = 0; i < fileListPDF.length; i++) {
            if (fileListPDF[i].isFile()) {
                String fileName = fileListPDF[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListPDF[i]);
                    break;
                }
            }
        }

        pdfPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.HARDNESS_INSPECTION_REPORT;//硬度检测
        filePDF = new File(pdfPath);
        fileListPDF = filePDF.listFiles();
        for (int i = 0; i < fileListPDF.length; i++) {
            if (fileListPDF[i].isFile()) {
                String fileName = fileListPDF[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListPDF[i]);
                    break;
                }
            }
        }

        pdfPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;//尺寸检测
        filePDF = new File(pdfPath);
        fileListPDF = filePDF.listFiles();
        for (int i = 0; i < fileListPDF.length; i++) {
            if (fileListPDF[i].isFile()) {
                String fileName = fileListPDF[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListPDF[i]);
                    break;
                }
            }
        }

        pdfPath = rootPath + AppConstant.MATERIAL_PREFIX + AppConstant.ACME;//ACME螺纹报告
        filePDF = new File(pdfPath);
        fileListPDF = filePDF.listFiles();
        for (int i = 0; i < fileListPDF.length; i++) {
            if (fileListPDF[i].isFile()) {
                String fileName = fileListPDF[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListPDF[i]);
                    break;
                }
            }
        }

        String pdfVendor = rootPath + AppConstant.VENDOR;//供应商报告
        File fileVendor = new File(pdfVendor);
        File[] fileListVendor = fileVendor.listFiles();
        for (int i = 0; i < fileListVendor.length; i++) {
            if (fileListVendor[i].isFile()) {
                String fileName = fileListVendor[i].getAbsolutePath();
                if (fileName.contains(sapLotNo) && fileName.endsWith(".pdf")) {
                    fileNameList.add(fileListVendor[i]);
                    break;
                }
            }
        }
        for (int i = 0; i < fileNameList.size(); i++) {
            mergePDF.addSource(fileNameList.get(i));
        }
        mergePDF.setDestinationFileName(rootPath + AppConstant.PDF + purchasingOrderInfo.getPurchasingNo() + "-"
                + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf");
        try {
            mergePDF.mergeDocuments();
            //首页加受控章
            String overFile = rootPath + AppConstant.PDF + purchasingOrderInfo.getPurchasingNo() + "-"
                    + purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf";
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
        } catch (COSVisitorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refesh() {
        String purchasingNo = cbPurchasingNo.getValue().trim();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService.getByPurchasingNo(purchasingNo);
            if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
                // 数据填入grid
                gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
            } else {
                NotificationUtils.notificationError(I18NUtility.getValue(
                        "view.receivinginspection.purchasingnotexist", "Purchasing No Not Exist ."));
            }
        } else {
            NotificationUtils.notificationError(
                    I18NUtility.getValue("view.receivinginspection.purchasingnotnull", "PurchasingNo can't be null"));
        }
    }

    public void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

//    public Media getMedia(String path) {
//        Media media = new Media();
//        //-----------预览获取pdf文件数据 到对象Media   "C:\\Users\\bruce_yang\\Desktop\\testpdf.pdf"
//        File file = new File(path);
//        media.setCategory("SupplierEnquiry");
//        media.setName(file.getName());
//        media.setType("pdf");
//        try {
//            media.setMediaContent(ByteStreams.toByteArray(new ByteArrayInputStream(FileUtils.readFileToByteArray(file))));
//        } catch (IOException e) {
//            throw new PlatformException(e);
//        }
//        //-----------预览获取pdf文件数据 到对象Media
//        return media;
//    }

    public List<String> getPurchasingOrder(String type) {
        return purchasingOrderService.getPurchasingNo(type);
    }

    @Override
    public void _init() {
        cbPurchasingNo.setItems(getPurchasingOrder("RECEIVING"));
    }

    public void uploadPDF(InputStream fileStream, String fileName) throws IOException {
        File targetFile = new File("D:\\Attachment\\" + purchasingOrderInfo.getSapInspectionLot() + "\\" + fileName);
        FileUtils.copyInputStreamToFile(fileStream, targetFile);
    }
}
