//Changed by Cameron: 改善LEVP显示和保存逻辑

package com.ags.lumosframework.ui.view.levp;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.Levp;
import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.service.api.IUserService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.ConfirmDialog;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// import function radio button of selection Eric

@Menu(caption = "LEVP", captionI18NKey = "LEVP.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 10)
@SpringView(name = "LEVP", ui = {CameronUI.class})
public class levpView extends BaseView implements Button.ClickListener {
    // define radio button of selection Eric
    RadioButtonGroup<String> selection = new RadioButtonGroup<>();
    RadioButtonGroup<String> selection1 = new RadioButtonGroup<>();
    Panel panel;
    private ComboBox<ProductionOrder> cbOrder;
    @I18Support(caption = "Assy.No", captionKey = "view.qcinspection.assyno")
    private LabelWithSamleLineCaption tfAssyNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Confirm", captionKey = "view.levp.generatepdf")
    private Button btnConfirm;
    @I18Support(caption = "ProductRev", captionKey = "view.qcinspection.productrev")
    private LabelWithSamleLineCaption tfProductRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "AssySerialNo", captionKey = "view.qcinspection.assyserialno")
    private LabelWithSamleLineCaption tfAssySerialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "view.qcinspection.description")
    private LabelWithSamleLineCaption tfDescription = new LabelWithSamleLineCaption();
    @I18Support(caption = "LEVP")
    private LabelWithSamleLineCaption LEVPNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "LEVP Rev", captionKey = "LEVP.view.rev")
    private LabelWithSamleLineCaption LEVPRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "SalesNo", captionKey = "view.qcinspection.salesno")
    private LabelWithSamleLineCaption tfSalesNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Customer", captionKey = "view.qcinspection.customer")
    private LabelWithSamleLineCaption tfCustomer = new LabelWithSamleLineCaption();
    @I18Support(caption = "FactoryCode", captionKey = "view.qcinspection.factorycode")
    private LabelWithSamleLineCaption tfFactoryCode = new LabelWithSamleLineCaption();
    private AbstractComponent[] components;
    private AbstractComponent[] disPlayComponents1;
    private AbstractComponent[] disPlayComponents2;
    private AbstractComponent[] disPlayComponents3;
    private HorizontalLayout hlToolBox;
    private HorizontalLayout hlDisplayBox;
    private VerticalLayout vlBox1;
    private VerticalLayout vlBox2;
    private VerticalLayout vlBox3;
    private GridLayout content1;
    private Image img1;
    private Image img2;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private IUserService userService;
    private Levp levp;
    @Autowired
    private ILevpService levpService;
    private Label lblQcState = new Label();
    private Label lblWhState = new Label();

    public levpView() {
        cbOrder = (ComboBox<ProductionOrder>) new ComboBox();
        btnConfirm = new Button();
        panel = new Panel();
        components = new AbstractComponent[]{cbOrder, btnConfirm};
        disPlayComponents1 = new AbstractComponent[]{tfAssyNo, tfAssySerialNo, selection};//tfProductRev
        disPlayComponents2 = new AbstractComponent[]{LEVPNo, tfFactoryCode, selection1};//LEVPRev
        disPlayComponents3 = new AbstractComponent[]{tfCustomer, tfSalesNo, tfDescription};
        hlToolBox = new HorizontalLayout();
        hlDisplayBox = new HorizontalLayout();
        vlBox1 = new VerticalLayout();
        vlBox2 = new VerticalLayout();
        vlBox3 = new VerticalLayout();

        final VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName("card-0 card-no-padding");
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        final HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        hlDisplayBox.setWidth("100%");
        hlDisplayBox.addStyleName("side-menu");
        hlDisplayBox.setMargin(true);
        vlRoot.addComponent(hlDisplayBox);


//        content.setWidth("100%");
//        content.addStyleName("side-menu");
//        content.setMargin(true);
//        vlRoot.addComponent(content);


        vlBox1.addStyleName("input-display-inline");
        vlBox2.addStyleName("input-display-inline");
        vlBox3.addStyleName("input-display-inline");
        hlDisplayBox.addComponent(vlBox1);
        hlDisplayBox.addComponent(vlBox2);
        hlDisplayBox.addComponent(vlBox3);

        cbOrder.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductOrderId", "ProductOrderId"));
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);

        selection.setCaption("QC");
        selection.setItems("OK", "NG");
        selection.setValue("");

        selection.addValueChangeListener(event -> {
            if (!event.isUserOriginated()) {
                return;
            }
            if (levp == null) {
                levp = new Levp();
                levp.setProductionOrderNo(cbOrder.getValue().getProductOrderId());
                levp.setQcRlt(event.getValue());
                levp.setQcChecker(RequestInfo.current().getUserName());
                levp.setQcCheckedDate(new Date());
                levpService.save(levp);
                String qcState = "QC Checked by " + levp.getQcChecker();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                qcState += " on " + formatter.format(levp.getQcCheckedDate());
                lblQcState.setValue(qcState);
                vlBox1.addComponent(lblQcState);
            } else if (levp.getQcChecker() == null) {
                levp.setQcRlt(event.getValue());
                levp.setQcChecker(RequestInfo.current().getUserName());
                levp.setQcCheckedDate(new Date());
                levpService.save(levp);
                String qcState = "QC Checked by " + levp.getQcChecker();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                qcState += " on " + formatter.format(levp.getQcCheckedDate());
                lblQcState.setValue(qcState);
                vlBox1.addComponent(lblQcState);
            } else {
                ConfirmDialog.show(getUI(),
                        "该工单LEVP已有QC检验，是否覆盖？",
                        result -> {
                            if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                levp.setQcChecker(RequestInfo.current().getUserName());
                                levp.setQcRlt(event.getValue());
                                levp.setQcCheckedDate(new Date());
                                levpService.save(levp);
                                String qcState = "QC Checked by " + levp.getQcChecker();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                                qcState += " on " + formatter.format(levp.getQcCheckedDate());
                                lblQcState.setValue(qcState);
                                vlBox1.addComponent(lblQcState);
                            } else {
                                ((RadioButtonGroup) event.getComponent()).setValue(event.getOldValue());
                            }
                        });
            }
        });

        selection1.setCaption("Warehouse");
        selection1.setItems("OK", "NG");
        selection1.setValue("");
        selection1.addValueChangeListener(event -> {
            if (!event.isUserOriginated()) {
                return;
            }
            if (levp == null) {
                levp = new Levp();
                levp.setProductionOrderNo(cbOrder.getValue().getProductOrderId());
                levp.setWhRlt(event.getValue());
                levp.setWhChecker(RequestInfo.current().getUserName());
                levp.setWhCheckedDate(new Date());
                levpService.save(levp);
                String whState = "WH Checked by " + levp.getWhChecker();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                whState += " on " + formatter.format(levp.getWhCheckedDate());
                lblWhState.setValue(whState);
                vlBox2.addComponent(lblWhState);
            } else if (levp.getWhChecker() == null) {
                levp.setWhRlt(event.getValue());
                levp.setWhChecker(RequestInfo.current().getUserName());
                levp.setWhCheckedDate(new Date());
                levpService.save(levp);
                String whState = "WH Checked by " + levp.getWhChecker();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                whState += " on " + formatter.format(levp.getWhCheckedDate());
                lblWhState.setValue(whState);
                vlBox2.addComponent(lblWhState);
            } else {
                ConfirmDialog.show(getUI(),
                        "该工单LEVP已有WH检验，是否覆盖？",
                        result -> {
                            if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                levp.setWhChecker(RequestInfo.current().getUserName());
                                levp.setWhRlt(event.getValue());
                                levp.setWhCheckedDate(new Date());
                                levpService.save(levp);
                                String whState = "WH Checked by " + levp.getWhChecker();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                                whState += " on " + formatter.format(levp.getWhCheckedDate());
                                lblWhState.setValue(whState);
                                vlBox2.addComponent(lblWhState);
                            } else {
                                ((RadioButtonGroup) event.getComponent()).setValue(event.getOldValue());
                            }
                        });
            }
        });

        for (final Component component : components) {
            hlTempToolBox.addComponent(component);
        }
        for (final Component component : disPlayComponents1) {
            vlBox1.addComponent(component);
        }
        for (final Component component : disPlayComponents2) {
            vlBox2.addComponent(component);
        }
        for (final Component component : disPlayComponents3) {
            vlBox3.addComponent(component);
        }
        tfDescription.setWidthUndefined();
        cbOrder.setEmptySelectionAllowed(false);
        cbOrder.addSelectionListener(new SingleSelectionListener<ProductionOrder>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(final SingleSelectionEvent<ProductionOrder> event) {
                if (event.getValue() == null || "".equals(event.getValue())) {
                    initLabel();
                } else {
                    initLabel();
                    final ProductionOrder inputProductionOrder = event.getValue();
                    //判断该订单是否锁定
                    if(inputProductionOrder.getBomLockFlag()){
                        throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                                "This Order is locked,Please contact the engineer to solve !"));
                    }
                    levp = levpService.getByOrderNo(inputProductionOrder.getProductOrderId());
                    // levpView.finalInspectionResultSavedList = (List<FinalInspectionResult>)levpView.finalInspectionResultService.getFinalInspectionResultByOrderNo(((ProductionOrder)event.getValue()).getProductOrderId(), "QC");
                    if (inputProductionOrder == null) {
                        NotificationUtils.notificationError("当前输入的生产订单号:" + cbOrder.getValue().getProductOrderId() + " 没有相关信息，请确认后重新输入");
                        return;
                    }
                    final ProductInformation productInformation = productInformationService.getByNoRev(inputProductionOrder.getProductId(), inputProductionOrder.getProductVersionId());

                    if (productInformation == null) {
                        NotificationUtils.notificationError("工单:" + inputProductionOrder.getProductOrderId() + " 对应的产品:" + inputProductionOrder.getProductId() + " 在系统中没有维护产品信息");
                        return;
                    }
                    tfAssyNo.setValue(inputProductionOrder.getProductId() + " REV" + inputProductionOrder.getProductVersionId());
//                    tfProductRev.setValue(inputProductionOrder.getProductVersionId());
                    String assySerialNo = event.getValue().getProductOrderId();
                    if (inputProductionOrder.getProductNumber() == 1) {
                        assySerialNo += "0001";
                    } else if (inputProductionOrder.getProductNumber() > 1) {
                        assySerialNo += "0001~" + String.format("%04d", inputProductionOrder.getProductNumber());
                    }
                    tfAssySerialNo.setValue(assySerialNo);
                    tfDescription.setValue(inputProductionOrder.getProductDesc());
                    LEVPNo.setValue(productInformation.getLevp() + " REV" + productInformation.getLevpRev());
//                    LEVPRev.setValue(productInformation.getLevpRev());
                    tfFactoryCode.setValue("A419");
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
                    if (levp == null) {
                        selection.setValue("");
                        selection1.setValue("");
                    } else {
                        selection.setValue(levp.getQcRlt() == null ? "" : levp.getQcRlt());
                        selection1.setValue(levp.getWhRlt() == null ? "" : levp.getWhRlt());
                        if (!Strings.isNullOrEmpty(levp.getQcChecker())) {
                            String qcState = "QC Checked by " + levp.getQcChecker();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                            qcState += " on " + formatter.format(levp.getQcCheckedDate());
                            lblQcState.setValue(qcState);
                            vlBox1.addComponent(lblQcState);
                        }
                        if (!Strings.isNullOrEmpty(levp.getWhChecker())) {
                            String whState = "WH Checked by " + levp.getWhChecker();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                            whState += " on " + formatter.format(levp.getWhCheckedDate());
                            lblWhState.setValue(whState);
                            vlBox2.addComponent(lblWhState);
                        }
                    }
                    int levpCount = LEVPNo.getValue().split(" REV")[0].split("/").length;
                    content1 = new GridLayout(2, levpCount);
                    for (int i = 0; i < levpCount; i++) {
                        //加 LEVP content png
                        int finalI = i;
                        img1 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                            private static final long serialVersionUID = 1L;
                            byte[] bytes3 = null;

                            @Override
                            public InputStream getStream() {
                                InputStream inputStream3 = null;
                                try {
                                    inputStream3 = new FileInputStream("D:\\LEVP\\jpg\\" + LEVPNo.getValue().split(" REV")[0].split("/")[finalI] + "-QC.png");
                                    bytes3 = inputStream2byte(inputStream3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return new ByteArrayInputStream(bytes3);
                            }
                        }, ""));
                        img2 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                            private static final long serialVersionUID = 1L;
                            byte[] bytes4 = null;

                            @Override
                            public InputStream getStream() {
                                InputStream inputStream4 = null;
                                try {
                                    inputStream4 = new FileInputStream("D:\\LEVP\\jpg\\" + LEVPNo.getValue().split(" REV")[0].split("/")[finalI] + "-WH.png");
                                    bytes4 = inputStream2byte(inputStream4);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return new ByteArrayInputStream(bytes4);
                            }
                        }, ""));
                        img1.setSizeFull();
                        img1.setCaption(null);
                        img2.setSizeFull();
                        img2.setCaption(null);
                        content1.addComponent(img1, 0, i);
                        content1.addComponent(img2, 1, i);
                        content1.setWidth("100%");
                        content1.addStyleName("side-menu");
                        content1.setMargin(true);
                        panel.setContent(content1);
                    }
//
//
                }
            }
        });

        panel.setSizeFull();

//        // add radio button of selection Eric
//        content.addComponent(selection);
//        content.addComponent(selection1);


        vlRoot.addComponents(panel);
        vlRoot.setExpandRatio(panel, 1.0f);
        setSizeFull();
        setCompositionRoot(vlRoot);
    }


    @Override
    protected void init() {
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        btnConfirm.setEnabled(true);
        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        int i = 0;
        while (i < orderList.size()) {
            ProductInformation productInformation = orderList.get(i).getProductInformation();
            if (productInformation != null) {
                if (productInformation.getLevp() == null || productInformation.getLevp().equals("")) {
                    orderList.remove(i);
                } else {
                    i++;
                }
            } else {
                orderList.remove(i);
            }
        }
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            cbOrder.setValue(productionOrderService.getByNo(event.getParameters()));
        }
    }

    @Override
    public void buttonClick(final Button.ClickEvent event) {
        final Button button = event.getButton();
        button.setEnabled(true);

        if (btnConfirm.equals(button)) {
            int docCount = LEVPNo.getValue().split(" REV")[0].split("/").length;
            for (int j = 0; j < docCount; j++) {
                String filename = "D:\\LEVP\\pdf\\" + LEVPNo.getValue().split(" REV")[0].split("/")[j] + ".pdf";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                    if (caConfig != null) {
                        if (Strings.isNullOrEmpty(caConfig.getConfigValue())) {
                            NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                            return;
                        }
                    }
                    PdfReader readerOriginalDoc = new PdfReader(filename);
                    PdfWriter writeDest = new PdfWriter(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.LEVP + cbOrder.getValue().getProductOrderId() + "-" + j + ".pdf");
                    PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
                    PdfAcroForm form = PdfAcroForm.getAcroForm(newDoc, true);
                    Document document = new Document(newDoc);

                    text2Formfield(document, form, "PN", tfAssyNo.getValue().split(" REV")[0]);//PN 号 进 pdf
                    text2Formfield(document, form, "Rev", tfAssyNo.getValue().split(" REV")[1]);//PN Rev 进 pdf
                    text2Formfield(document, form, "SerialNo", tfAssySerialNo.getValue());//production order 进 pdf
                    text2Formfield(document, form, "Plant", tfFactoryCode.getValue());//Plant Code
                    if (levp.getQcChecker() != null) {
                        String qcName = userService.getByName(levp.getQcChecker()).getFirstName() + " " + userService.getByName(levp.getQcChecker()).getLastName();
                        text2Formfield(document, form, "QCName", qcName);//QC Name
                        Media mediaImage = caMediaService.getMediaByName(levp.getQcChecker());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + levp.getQcChecker() + " 签名logo未配置添加！");
                        }
                        for (int i = 1; i < 100; i++) {//添加QC签名和日期，最多99个
                            if (form.getField("QCSign" + i) == null && form.getField("QCDate" + i) == null) {
                                break;
                            }
                            if (form.getField("QCSign" + i) != null) {
                                img2Formfield(document, form, "QCSign" + i, mediaImage);//QC Sign
                            }
                            if (form.getField("QCDate" + i) != null) {
                                text2Formfield(document, form, "QCDate" + i, formatter.format(levp.getQcCheckedDate()));//QC Date
                            }
                        }
                    }
                    if (levp.getWhChecker() != null) {
                        String whName = userService.getByName(levp.getWhChecker()).getFirstName() + " " + userService.getByName(levp.getWhChecker()).getLastName();
                        text2Formfield(document, form, "WHName", whName);//WH Name
                        Media mediaImage = caMediaService.getMediaByName(levp.getWhChecker());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + levp.getWhChecker() + " 签名logo未配置添加！");
                        }
                        for (int i = 1; i < 100; i++) {//添加WH签名和日期，最多99个
                            if (form.getField("WHSign" + i) == null && form.getField("WHDate" + i) == null) {
                                break;
                            }
                            if (form.getField("WHSign" + i) != null) {
                                img2Formfield(document, form, "WHSign" + i, mediaImage);//WH Sign
                            }
                            if (form.getField("WHDate" + i) != null) {
                                text2Formfield(document, form, "WHDate" + i, formatter.format(levp.getWhCheckedDate()));//WH Date
                            }
                        }
                    }
                    document.close();
                    NotificationUtils.notificationInfo("成功生成工单" + levp.getProductionOrderNo() + "的LEVP报告!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // public void saveDataToFinalInspectionResult(final String inputOrderNo) {
    //     final String saleOrderNo = tfSalesNo.getValue().trim();
    //     final String consumer = tfCustomer.getValue().trim();
    //     finalInspectionResult.setOrderNo(inputOrderNo);
    //     finalInspectionResult.setSaleOrderNo(saleOrderNo);
    //     finalInspectionResult.setConsumer(consumer);
    //     finalInspectionResult.setQcConfirmDate(new Date());
    //     finalInspectionResult.setQcConfirmUser(RequestInfo.current().getUserName());
    // }


//    public boolean checkisSavedOrNot(final List<FinalInspectionResult> finalInspectionResultList) {
//        boolean flag = true;
//        if (finalInspectionResultList != null && finalInspectionResultList.size() > 0) {
//            for (final FinalInspectionResult finalInspectionResult : finalInspectionResultList) {
//                final Date qcDate = finalInspectionResult.getQcConfirmDate();
//                final String qcUser = finalInspectionResult.getQcConfirmUser();
//                if (qcDate != null || qcUser != null) {
//                    flag = false;
//                    break;
//                }
//            }
//        }
//        return flag;
//    }


    public void initLabel() {
        tfAssyNo.setValue("");
        tfProductRev.setValue("");
        tfAssySerialNo.setValue("");
        tfDescription.setValue("");
        LEVPNo.setValue("");
        LEVPRev.setValue("");
        tfFactoryCode.setValue("");
        tfSalesNo.setValue("");
        tfCustomer.setValue("");
        levp = null;
//        content1.removeComponent(0, 0);
//        content1.removeComponent(1, 0);
        lblQcState.setValue("");
        lblWhState.setValue("");
        vlBox1.removeComponent(lblQcState);
        vlBox2.removeComponent(lblWhState);
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

    //在PdfFormField位置添加文本，并删除PdfFormField
    public void text2Formfield(Document doc, PdfAcroForm form, String fieldName, String text) {
        PdfFormField field = form.getField(fieldName);
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(field.getFont());
        paragraph.setFontSize(field.getFontSize());
        int pageNumber = doc.getPdfDocument().getPageNumber(field.getWidgets().get(0).getPage());
        PdfArray position = field.getWidgets().get(0).getRectangle();
        float x = (float) position.getAsNumber(0).getValue();
        float y = (float) position.getAsNumber(1).getValue();
        float width = (float) (position.getAsNumber(2).getValue() - position.getAsNumber(0).getValue());
        paragraph.setFixedPosition(pageNumber, x, y, width);
        doc.add(paragraph);
        form.removeField(fieldName);
    }

    //在PdfFormField位置添加图片，并删除PdfFormField
    public void img2Formfield(Document doc, PdfAcroForm form, String fieldName, Media media) throws IOException {
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