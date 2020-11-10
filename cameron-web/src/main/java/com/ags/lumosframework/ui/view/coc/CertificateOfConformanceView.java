package com.ags.lumosframework.ui.view.coc;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.CertificateOfConformance;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Menu(caption = "CertificateOfConformance", captionI18NKey = "CertificateOfConformance.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 9)
@SpringView(name = "CertificateOfConformance", ui = CameronUI.class)
public class CertificateOfConformanceView extends BaseView implements Button.ClickListener, IFilterableView {

    @I18Support(caption = "LicenseType", captionKey = "CertificateOfConformance.LicenseType")
    ComboBox<String> cbLicenseType = new ComboBox<String>();// 合格证类型
    GridLayout glLayout = new GridLayout(3, 3);
    GridLayout glLayoutText = new GridLayout(5, 3);
    SimpleDateFormat dfBase = new SimpleDateFormat("yyyy-MM");// 设置日期格式
    @I18Support(caption = "Save", captionKey = "PaintingInformation.Save")
    private Button btnSave = new Button();
    @I18Support(caption = "Export", captionKey = "common.Export.word")
    private Button btnExport = new Button();
    //    private TextField tfproductOrderId = new TextField();
    private ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    private Button btnConfirm = new Button();
    @I18Support(caption = "PartNO", captionKey = "CertificateOfConformance.PartNO")
    private LabelWithSamleLineCaption tfPartNO = new LabelWithSamleLineCaption();// 零件号
    @I18Support(caption = "PartNORev", captionKey = "CertificateOfConformance.PartNORev")
    private LabelWithSamleLineCaption tfPartNORev = new LabelWithSamleLineCaption();// 零件号版本
    @I18Support(caption = "Quantity", captionKey = "CertificateOfConformance.Quantity")
    private LabelWithSamleLineCaption tfQuantity = new LabelWithSamleLineCaption();// 工单中的产品数量
    @I18Support(caption = "SerialNumber", captionKey = "CertificateOfConformance.SerialNumber")
    private LabelWithSamleLineCaption tfSerialNumber = new LabelWithSamleLineCaption();// 序列号
    @I18Support(caption = "NotesOfCertification", captionKey = "CertificateOfConformance.NotesOfCertification")
    private LabelWithSamleLineCaption tfNotesOfCertification = new LabelWithSamleLineCaption();// 计划及版本
    //Changed by Cameron: 将tfDescription改为TextArea，并允许编辑
    @I18Support(caption = "Description", captionKey = "CertificateOfConformance.Description")
    private TextArea tfDescription = new TextArea();// 描述(bom execl 中导入零件的长文本)
    @I18Support(caption = "CertificateNumber", captionKey = "CertificateOfConformance.CertificateNumber")
    private TextField tfCertificateNumber = new TextField();// 合格证号
    @I18Support(caption = "Date", captionKey = "CertificateOfConformance.Date")
    private DateField tfDate = new DateField();// 日期
    @I18Support(caption = "Customer", captionKey = "CertificateOfConformance.Customer")
    private TextField tfCustomer = new TextField();// 客户
    @I18Support(caption = "CustomerPONumber", captionKey = "CertificateOfConformance.CustomerPONumber")
    private TextField tfCustomerPONumber = new TextField();// 客户订单
    @I18Support(caption = "SalesOrderNumber", captionKey = "CertificateOfConformance.SalesOrderNumber")
    private TextField tfSalesOrderNumber = new TextField();// 销售订单
    @I18Support(caption = "InternalTrackingNumber", captionKey = "CertificateOfConformance.InternalTrackingNumber")
    private TextField tfInternalTrackingNumber = new TextField();// 内部跟踪号
    @I18Support(caption = "Item", captionKey = "CertificateOfConformance.Item")
    private TextField tfItem = new TextField();// 项目
    @I18Support(caption = "Notes", captionKey = "CertificateOfConformance.Notes")
    private TextField tfNotes = new TextField();// 备注
    @I18Support(caption = "LicenseNumber", captionKey = "CertificateOfConformance.LicenseNumber")
    private TextField tfLicenseNumber = new TextField();// 会标编号
    @I18Support(caption = "QualityRepresentative", captionKey = "CertificateOfConformance.QualityRepresentative")
    private TextField tfQualityRepresentative = new TextField();// 质量代表
    private Button[] btns = new Button[]{btnConfirm, btnSave};// ,btnStart, btnExport
    private TextField[] textFieldAll = new TextField[]{tfCertificateNumber, tfCustomer, tfCustomerPONumber,
            tfSalesOrderNumber, tfInternalTrackingNumber, tfItem, tfNotes, tfLicenseNumber, tfQualityRepresentative};
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private ProductionOrder order;
    private SparePart sparePart;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private ICertificateOfConformanceService certificateOfConformanceService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private ICaMediaService caMediaService;


    public CertificateOfConformanceView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        cbOrder.setPlaceholder(I18NUtility.getValue("PaintingInformation.productOrderId", "productOrderId"));

        hlTempToolBox.addComponent(cbOrder);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnSave.setIcon(VaadinIcons.PACKAGE);
        btnExport.setIcon(VaadinIcons.DOWNLOAD);

        Panel panel = new Panel();
        panel.setWidth("100%");
        panel.setHeightUndefined();
        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(this.tfPartNO, 0, 0);
        glLayout.addComponent(this.tfPartNORev, 1, 0);
        glLayout.addComponent(this.tfNotesOfCertification, 2, 0);
        glLayout.addComponent(this.tfSerialNumber, 0, 1);
        glLayout.addComponent(this.tfQuantity, 1, 1);
        // glLayout.addComponent(this.tfDescription, 0, 2);
        glLayout.addComponent(this.tfDescription, 0, 2, 2, 2);// 长文本
        tfDescription.setWidth("100%");//Changed by Cameron: tfDescription改为TextArea
        panel.setContent(glLayout);
        vlRoot.addComponent(panel);

        Panel panelText = new Panel();
        panelText.setWidth("100%");
        panelText.setSizeFull();

        // inputdisplayinline
        glLayoutText.setSpacing(true);
        glLayoutText.setMargin(true);
        glLayoutText.setWidth("100%");
        glLayoutText.setHeightUndefined();

        // 第一行
        glLayoutText.addComponent(this.cbLicenseType, 0, 0);
        cbLicenseType.setWidth("100%");
        // glLayoutText.addComponent(this.cbIsProtect, 1, 0);

        // 第二行
        glLayoutText.addComponent(this.tfCertificateNumber, 0, 1);
        glLayoutText.addComponent(this.tfDate, 1, 1);
        glLayoutText.addComponent(this.tfCustomer, 2, 1);
        glLayoutText.addComponent(this.tfCustomerPONumber, 3, 1);
        glLayoutText.addComponent(this.tfSalesOrderNumber, 4, 1);
        // 第三行
        glLayoutText.addComponent(this.tfInternalTrackingNumber, 0, 2);
        glLayoutText.addComponent(this.tfItem, 1, 2);
        glLayoutText.addComponent(this.tfNotes, 2, 2);
        glLayoutText.addComponent(this.tfLicenseNumber, 3, 2);
        glLayoutText.addComponent(this.tfQualityRepresentative, 4, 2);

//        tfCustomer.setReadOnly(true);
//        tfCustomerPONumber.setReadOnly(true);
//        tfItem.setReadOnly(true);
        tfDate.setDateFormat("yyyy-MM-dd");
        tfDate.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                return;
            }
            String prefix;
            if (event.getValue().getMonthValue() < 10) {
                prefix = event.getValue().getYear() + "-0" + event.getValue().getMonthValue();
            } else {
                prefix = event.getValue().getYear() + "-" + event.getValue().getMonthValue();
            }
            // 自动设置合格证号
            int num = certificateOfConformanceService.getCertificateNumberNext(prefix);
            tfCertificateNumber.setValue(prefix + "-" + initNum(num));
        });
        cbLicenseType.setItems(AppConstant.COC_HAVE_LOGO, AppConstant.COC_NO_LOGO);
        cbLicenseType.setItemCaptionGenerator(new ItemCaptionGenerator<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(String item) {
                return AppConstant.COC_HAVE_LOGO.equals(item) ? "产品合格证(新制造的产品)" : AppConstant.COC_NO_LOGO.equals(item) ? "产品合格证(无会标的API 6A 20版的产品和备件)" : "";
            }
        });
        cbLicenseType.addValueChangeListener(new ValueChangeListener<String>() {

            private static final long serialVersionUID = 5345896831051606921L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                Optional<String> optional = cbLicenseType.getSelectedItem();
                if (optional.isPresent()) {
                    String logo = optional.get();
                    if (!Strings.isNullOrEmpty(logo)) {
                        btnSave.setEnabled(true);
                    } else {
                        btnSave.setEnabled(false);
                    }
                } else {
                    btnSave.setEnabled(false);
                }
            }

        });

        panelText.setContent(glLayoutText);
        vlRoot.addComponent(panelText);
        vlRoot.setExpandRatio(panelText, 0.1f);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnSave.equals(button)) {
            String path = "";
            CertificateOfConformance cocTemp = certificateOfConformanceService.getByCertificateNo(tfCertificateNumber.getValue());
            if(!sparePart.getLongText().equals(tfDescription.getValue())){//Changed by Cameron: 将tfDescription中的LongText保存至SparePart表中
                sparePart.setLongText(tfDescription.getValue());
                sparePartService.save(sparePart);
            }
            if (cocTemp != null && !cocTemp.getOrderNo().equals(cbOrder.getValue().getProductOrderId())) {
                NotificationUtils.notificationError("COC编号重复，请修改！");
                return;
            }
            if (cbOrder.getValue() != null) {
                //CertificateOfConformance certificateOfConformance = certificateOfConformanceService
                //      .getByOrder(cbOrder.getValue().getProductOrderId());
                //if (certificateOfConformance != null) {
                //NotificationUtils.notificationError("此工单号已保存到COC信息，请不要重复操作！");
                //} else {
                CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                if (caConfig != null) {
                    path = caConfig.getConfigValue();
                    if (Strings.isNullOrEmpty(path)) {
                        NotificationUtils.notificationError("当前没有配置报告输出路径,请首先配置输入路径");
                        return;
                    }
                }
                Media mediaImage = caMediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(), RequestInfo.current().getUserName());
                if (mediaImage == null) {
                    NotificationUtils.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
                    return;
                }
                //检查QA公章是否添加
                Media qaSeal = caMediaService.getByTypeName(ElectronicSignatureLoGoType.SEAL.getKey(), RequestInfo.current().getUserName());
                if (qaSeal == null) {
                    NotificationUtils.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的QA专用章,请首先配置该用户的QA专用章");
                    return;
                }
                CertificateOfConformance certificateOfConformanceIsSaveing = setValueToPaintingInformation();
                // 保存喷漆信息
                if (certificateOfConformanceIsSaveing != null) {
                    certificateOfConformanceService.save(certificateOfConformanceIsSaveing);
                    //信息保存成功之后，生成Word文档并保存
                    createReport(path);
                    initTextField();
                    initLab(null);
                    NotificationUtils.notificationInfo("工单号：" + cbOrder.getValue().getProductOrderId() + " 的COC信息保存成功！");
                    cbOrder.clear();
                } else {
                    NotificationUtils.notificationError("工单信息获取失败！");
                }
                //}
            } else {
                NotificationUtils.notificationError("请输入有效的工单号！");
            }
        } else if (btnExport.equals(button)) {
            String orderId = cbOrder.getValue().getProductOrderId();
            if (cbOrder.getValue() != null) {
                if (orderId.equals(order.getProductOrderId())) {
                    if (sparePart != null) {// && productInformation!=null
                        String path = "";
                        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                        if (caConfig != null) {
                            path = caConfig.getConfigValue();
                            if (Strings.isNullOrEmpty(path)) {
                                NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                                return;
                            }
                        }
                        createReport(path);
                    } else {
                        NotificationUtils.notificationError("工单号对应的零件信息和产品信息为空，请到零件界面或产品信息界面维护！");
                    }
                } else {
                    NotificationUtils.notificationError("输入的工单号和查询的内容不匹配，请重新点击查询按钮！");
                }
            } else {
                NotificationUtils.notificationError("请输入有效的工单号并进行查询！");
            }
        } else if (btnConfirm.equals(button)) {
            if (cbOrder.getValue() == null) {
                NotificationUtils.notificationError("请先选择工单号！");
            } else {
                order = cbOrder.getValue();
                //判断工单是否锁定
                if(order.getBomLockFlag()){
                    throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                            "This Order is locked,Please contact the engineer to solve !"));
                }
                String productId = order.getProductId();
                String productVersionId = order.getProductVersionId();
                if (!Strings.isNullOrEmpty(productId) && !Strings.isNullOrEmpty(productVersionId)) {
                    sparePart = sparePartService.getByNoRev(productId, productVersionId);
                }
//                // 自动设置合格证号
//                int num = certificateOfConformanceService.getCertificateNumberNext(dfBase.format(new Date()));
//                tfCertificateNumber.setValue(dfBase.format(new Date()) + "-" + initNum(num));
                tfDate.setValue(LocalDate.now());
                initLab(order);
                freshLab();

            }
        }
    }

    public void initLab(ProductionOrder order) {
        if (order != null) {
            tfCustomer.setValue(Strings.isNullOrEmpty(order.getCustomerCode()) ? "" : order.getCustomerCode());
            tfCustomerPONumber.setValue(Strings.isNullOrEmpty(order.getSalesOrder()) ? "" : order.getSalesOrder());
            tfItem.setValue(Strings.isNullOrEmpty(order.getSalesOrderItem()) ? "" : order.getSalesOrderItem());
            tfQualityRepresentative.setValue(RequestInfo.current().getUserFirstName() + " " + RequestInfo.current().getUserLastName());
            tfSalesOrderNumber.setValue("/");
            tfInternalTrackingNumber.setValue("/");
        } else {
            tfCustomer.setValue("");
            tfCustomerPONumber.setValue("");
            tfItem.setValue("");
            tfQualityRepresentative.setValue("");
            tfSalesOrderNumber.setValue("");
            tfInternalTrackingNumber.setValue("");
        }
        tfPartNO.setValue("");
        tfPartNORev.setValue("");
        tfQuantity.setValue("");
        tfSerialNumber.setValue("");
        tfNotesOfCertification.setValue("");
        tfDescription.setValue("");
    }

    public void initTextField() {
        for (TextField textField : textFieldAll) {
            textField.clear();
        }
        cbLicenseType.setSelectedItem("");
        tfDate.clear();
    }

    public void freshLab() {
        if (sparePart != null) {
            int productNumber = order.getProductNumber();
            String orderNo = order.getProductOrderId();
            if (!Strings.isNullOrEmpty(orderNo) && productNumber > 0) {
                if (productNumber > 1) {
                    tfSerialNumber.setValue(order.getProductOrderId() + "0001~" + String.format("%04d", productNumber));
                } else {
                    tfSerialNumber.setValue(order.getProductOrderId() + "0001");
                }
                tfQuantity.setValue(String.valueOf(productNumber));
            }

            tfPartNO.setValue(sparePart.getSparePartNo());
            tfPartNORev.setValue(sparePart.getSparePartRev());
            tfDescription.setValue(sparePart.getLongText());
            tfNotesOfCertification.setValue(sparePart.getQaPlan() + " Rev." + sparePart.getQaPlanRev());
            CertificateOfConformance coc = certificateOfConformanceService.getByOrder(orderNo);
            if (coc != null) {
                cbLicenseType.setValue(coc.getLogo().equals("HAVELOGO") ? AppConstant.COC_HAVE_LOGO : AppConstant.COC_NO_LOGO);
                tfNotes.setValue(coc.getNotes());
                tfLicenseNumber.setValue(coc.getLicenseNumber());
                tfCertificateNumber.setValue(coc.getCertificateNumber());
            }
        }
    }

    public CertificateOfConformance setValueToPaintingInformation() {
        CertificateOfConformance certificateOfConformance = certificateOfConformanceService
                .getByOrder(cbOrder.getValue().getProductOrderId());
        if (certificateOfConformance == null) {
            certificateOfConformance = new CertificateOfConformance();
        }
        certificateOfConformance.setOrderNo(cbOrder.getValue().getProductOrderId());

        Optional<String> optional = cbLicenseType.getSelectedItem();
        if (optional.isPresent()) {
            certificateOfConformance.setLogo(optional.get());
        }

        certificateOfConformance.setCertificateNumber(tfCertificateNumber.getValue().trim());
        certificateOfConformance.setDate(localDateToDate(tfDate.getValue()));
        certificateOfConformance.setCustomer(tfCustomer.getValue().trim());
        certificateOfConformance.setCustomerPONumber(tfCustomerPONumber.getValue().trim());
        certificateOfConformance.setSalesOrderNumber(tfSalesOrderNumber.getValue().trim());
        certificateOfConformance.setInternalTrackingNumber(tfInternalTrackingNumber.getValue().trim());
        certificateOfConformance.setItem(tfItem.getValue().trim());
        certificateOfConformance.setNotes(tfNotes.getValue().trim());
        certificateOfConformance.setLicenseNumber(tfLicenseNumber.getValue().trim());
        certificateOfConformance.setQualityRepresentative(tfQualityRepresentative.getValue().trim());

        return certificateOfConformance;
    }

    public Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public String initNum(int num) {
        String numStr = num + "";
        int length = numStr.length();
        if (length == 2) {
            numStr = "0" + numStr;
        } else if (length == 1) {
            numStr = "00" + numStr;
        }
        return numStr;
    }

    public Date initDateByMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    @Override
    public void updateAfterFilterApply() {

    }

    @Override
    protected void init() {
        btnSave.setEnabled(false);
//        tfCertificateNumber.setReadOnly(true);
        tfDate.setTextFieldEnabled(false);
        initLab(null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            cbOrder.setValue(productionOrderService.getByNo(event.getParameters()));
            btnConfirm.click();
        }
    }

    public void createReport(String path) {

        String productOrderId = cbOrder.getValue().getProductOrderId();
        CertificateOfConformance certificateOfConformance = certificateOfConformanceService.getByOrder(productOrderId);
        if (certificateOfConformance != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String haveLogo = certificateOfConformance.getLogo();
            params.put("certificateNumber", certificateOfConformance.getCertificateNumber());
            params.put("date", sdf.format(certificateOfConformance.getDate()));
            params.put("customer", certificateOfConformance.getCustomer());
            params.put("salesOrderNumber", certificateOfConformance.getSalesOrderNumber());
            params.put("customerPONumber", certificateOfConformance.getCustomerPONumber());
            if (AppConstant.COC_HAVE_LOGO.equals(haveLogo)) {//"HAVELOGO", "NOLOGO"
                params.put("internalTrackingNumber", Strings.isNullOrEmpty(certificateOfConformance.getInternalTrackingNumber()) ? "/" : certificateOfConformance.getInternalTrackingNumber());
            }

            params.put("item", certificateOfConformance.getItem());
            params.put("partNO", tfPartNO.getValue());
            params.put("partNORev", tfPartNORev.getValue());
            String description = tfDescription.getValue();
            description = description.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            params.put("description", description);
            params.put("quantity", tfQuantity.getValue());
            params.put("serialNumber", tfSerialNumber.getValue());
            params.put("notes", Strings.isNullOrEmpty(certificateOfConformance.getNotes()) ? "NA" : certificateOfConformance.getNotes());

            params.put("licenseNumber", certificateOfConformance.getLicenseNumber());
            params.put("notesOfCertification", tfNotesOfCertification.getValue());
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                Media mediaSignature = caMediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(), RequestInfo.current().getUserName());
                Media mediaStamp = caMediaService.getByTypeName(ElectronicSignatureLoGoType.SEAL.getKey(), RequestInfo.current().getUserName());
                params.put("representative", tfQualityRepresentative.getValue());
                params.put("signature", encoder.encode(inputStream2byte(mediaSignature.getMediaStream())));
                params.put("stamp", encoder.encode(inputStream2byte(mediaStamp.getMediaStream())));


                Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
                configuration.setDefaultEncoding("utf-8");
                configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

                configuration.setDirectoryForTemplateLoading(new File(AppConstant.DOC_XML_FILE_PATH));
                if (AppConstant.COC_HAVE_LOGO.equals(haveLogo)) {
                    Template template = configuration.getTemplate("cocHaveLogo.xml", "utf-8");
                    // 输出文件
                    File outFile = new File(path + AppConstant.PRODUCTION_PREFIX + AppConstant.COC + productOrderId + ".doc");


                    // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Writer out = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")), 1024 * 1024);
                    template.process(params, out);
                    out.flush();
                    out.close();

                    docToPdf(path + AppConstant.PRODUCTION_PREFIX + AppConstant.COC + productOrderId + ".doc");

                    NotificationUtils.notificationInfo("COC文件导出成功！");
                } else if (AppConstant.COC_NO_LOGO.equals(haveLogo)) {
                    Template template = configuration.getTemplate("cocNoLogo.xml", "utf-8");
                    // 输出文件
                    File outFile = new File(path + AppConstant.PRODUCTION_PREFIX + AppConstant.COC + productOrderId + ".doc");
                    Writer out = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")), 1024 * 1024);
                    template.process(params, out);
                    out.flush();
                    out.close();

                    docToPdf(path + AppConstant.PRODUCTION_PREFIX + AppConstant.COC + productOrderId + ".doc");

                    NotificationUtils.notificationInfo("COC文件导出成功！");
                } else {
                    throw new PlatformException("工单为 " + productOrderId + " 的COC保存信没有选择文件类型！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                NotificationUtils.notificationError("导出文件出现异常：" + e.getMessage());
            }
        } else {
            NotificationUtils.notificationError("此工单：" + productOrderId + " 的COC数据未保存！");
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

    private void docToPdf(String filePath) {
        //另存为PDF（MS Office）
        ComThread.InitSTA();
        ActiveXComponent app = null;
        Dispatch doc = null;
        //转换前的文件路径
        String startFile = filePath;
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
        ComThread.Release();
    }
}
