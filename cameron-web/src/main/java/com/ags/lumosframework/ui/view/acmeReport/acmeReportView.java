package com.ags.lumosframework.ui.view.acmeReport;

import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.service.ICaMediaService;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
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
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionListener;
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

@Menu(caption = "ACMEReport", captionI18NKey = "view.ACMEReport.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 4)
@SpringView(name = "ACMEReport", ui = CameronUI.class)
public class acmeReportView extends BaseView implements Button.ClickListener {
    VerticalLayout hlToolBox = new VerticalLayout();
    HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
    Panel inspectionValue = new Panel();
    GridLayout glTemp = new GridLayout(1, 6);
    VerticalLayout vlDisplay = new VerticalLayout();
    HorizontalLayout hlTemp1 = new HorizontalLayout();
    PurchasingOrderInfo purchasingOrderInfo = null;
    private ComboBox<String> cbPurchasingNo = new ComboBox();
    @I18Support(caption = "Confirm", captionKey = "common.confirm")
    private Button btnConfirm = new Button();
    @I18Support(caption = "MaterialNo", captionKey = "view.dimensioninspection.materialno")
    private LabelWithSamleLineCaption lblMaterialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "MaterialRev", captionKey = "view.dimensioninspection.materialrev")
    private LabelWithSamleLineCaption lblMaterialRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "MaterialDesc", captionKey = "view.dimensioninspection.materialdesc")
    private LabelWithSamleLineCaption lblMaterialDesc = new LabelWithSamleLineCaption();
    private Grid<PurchasingOrderInfo> gridObject = new Grid<>();
    private AbstractComponent[] components = new AbstractComponent[]{cbPurchasingNo, btnConfirm};// btnStart,
    @Autowired
    private IPurchasingOrderService purchasingOrderService;
    private Image img1;
    private Image img2;
    private Image img3;
    private ComboBox<String> cb1 = new ComboBox<>();
    private ComboBox<String> cb2 = new ComboBox<>();
    private ComboBox<String> cb3 = new ComboBox<>();
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private ICaMediaService caMediaService;


    public acmeReportView() {
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
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnConfirm.addClickListener(this);

        cbPurchasingNo.setPlaceholder("采购单号");
        cbPurchasingNo.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = -9044784139704252186L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
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
        hlTemp1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlTemp1.setWidth("100%");
        hlTemp1.addComponent(lblMaterialNo);
        hlTemp1.addComponent(lblMaterialRev);
        hlTemp1.addComponent(lblMaterialDesc);
        btnConfirm.setEnabled(false);
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
        gridObject.addSelectionListener((SelectionListener<PurchasingOrderInfo>) event -> {

            btnConfirm.setEnabled(true);
            if (event.getFirstSelectedItem().isPresent()) {
                btnConfirm.setEnabled(true);
                purchasingOrderInfo = event.getFirstSelectedItem().get();
                setDataToDisplayArea(purchasingOrderInfo);
            } else {
                btnConfirm.setEnabled(false);
                setDataToDisplayArea(null);
                purchasingOrderInfo = null;
            }
        });
        hlSplitPanel.setFirstComponent(gridObject);
        inspectionValue.setSizeFull();
        inspectionValue.setContent(glTemp);
        hlSplitPanel.setSecondComponent(inspectionValue);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
        cb1.setItems("PASS - Meets full crest width", "FAIL – Less than full crest width");
        cb2.setItems("PASS (45° bevel away from next thread & 30° toward next thread) ",
                "FAIL (30° bevel away from next thread & 45° toward next thread)");
        cb3.setItems("PASS - Meets full crest width for >2 TPI", "PASS – Meets half crest width for <=2TPI",
                "FAIL – Less than requirement");
        cb1.setCaption("请选择检验结果：");
        cb2.setCaption("请选择检验结果：");
        cb3.setCaption("请选择检验结果：");
        cb1.setWidth("100%");
        cb2.setWidth("100%");
        cb3.setWidth("100%");
        cb1.setTextInputAllowed(false);
        cb2.setTextInputAllowed(false);
        cb3.setTextInputAllowed(false);
        img1 = new Image("", new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;
            byte[] bytes1 = null;

            @Override
            public InputStream getStream() {
                InputStream inputStream1 = null;
                try {
                    inputStream1 = new FileInputStream("D:\\CameronQualityFiles\\DOCS\\ACME\\1.png");
                    bytes1 = inputStream2byte(inputStream1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ByteArrayInputStream(bytes1);
            }
        }, ""));
        img2 = new Image("", new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;
            byte[] bytes2 = null;

            @Override
            public InputStream getStream() {
                InputStream inputStream2 = null;
                try {
                    inputStream2 = new FileInputStream("D:\\CameronQualityFiles\\DOCS\\ACME\\2.png");
                    bytes2 = inputStream2byte(inputStream2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ByteArrayInputStream(bytes2);
            }
        }, ""));
        img3 = new Image("", new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;
            byte[] bytes3 = null;

            @Override
            public InputStream getStream() {
                InputStream inputStream3 = null;
                try {
                    inputStream3 = new FileInputStream("D:\\CameronQualityFiles\\DOCS\\ACME\\3.png");
                    bytes3 = inputStream2byte(inputStream3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ByteArrayInputStream(bytes3);
            }
        }, ""));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    public void setDataToDisplayArea(PurchasingOrderInfo purchasingOrderInfo) {
        if (purchasingOrderInfo == null) {
            // 清空显示区的信息
            lblMaterialNo.clear();
            lblMaterialRev.clear();
            lblMaterialDesc.clear();
            cb1.setValue("");
            cb2.setValue("");
            cb3.setValue("");
            glTemp.removeAllComponents();
            hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        } else {
            // 填充信息
            String materialNo = purchasingOrderInfo.getMaterialNo();
            String materialRev = purchasingOrderInfo.getMaterialRev();
            String materialDesc = purchasingOrderInfo.getMaterialDesc();
            // 获取零件信息，并得到质量计划和图纸编号

            lblMaterialNo.setValue(materialNo);
            lblMaterialRev.setValue(materialRev);
            lblMaterialDesc.setValue(materialDesc);
            glTemp.removeAllComponents();
            glTemp.addComponent(img1, 0, 0);
            glTemp.addComponent(img2, 0, 2);
            glTemp.addComponent(img3, 0, 4);
            glTemp.addComponent(cb1, 0, 1);
            glTemp.addComponent(cb2, 0, 3);
            glTemp.addComponent(cb3, 0, 5);
            hlSplitPanel.setSplitPosition(0.0F, Unit.PIXELS);
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        if (button.equals(btnConfirm)) {
            if (Strings.isNullOrEmpty(cb1.getValue()) || Strings.isNullOrEmpty(cb2.getValue()) || Strings.isNullOrEmpty(cb3.getValue())) {
                NotificationUtils.notificationError("检验结果不能为空");
                return;
            }
            createReport();
            refesh();
            NotificationUtils.notificationInfo("检验报告已生成");
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

    public List<String> getPurchasingOrder(String type) {
        return purchasingOrderService.getPurchasingNo(type);
    }

    @Override
    public void _init() {
        cbPurchasingNo.setItems(getPurchasingOrder("RECEIVING"));
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

    private void createReport() {
        String filename = "D:\\CameronQualityFiles\\DOCS\\ACME\\X-001911-01.pdf";
        try {
            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (caConfig != null) {
                if (Strings.isNullOrEmpty(caConfig.getConfigValue())) {
                    NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                    return;
                }
            }
            PdfReader readerOriginalDoc = new PdfReader(filename);
            PdfWriter writeDest = new PdfWriter(caConfig.getConfigValue() + AppConstant.MATERIAL_PREFIX
                    + AppConstant.ACME + purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo()
                    + "-" + purchasingOrderInfo.getSapInspectionLot() + ".pdf");
            PdfDocument newDoc = new PdfDocument(readerOriginalDoc, writeDest);
            PdfAcroForm form = PdfAcroForm.getAcroForm(newDoc, true);
            Document document = new Document(newDoc);
            text2Formfield(document, form, "PN", purchasingOrderInfo.getMaterialNo() + " REV" + purchasingOrderInfo.getMaterialRev());
            text2Formfield(document, form, "PO", purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
            form.getField("DESC").setValue(purchasingOrderInfo.getMaterialDesc());
            form.getField("DESC").setReadOnly(true);
            if (cb1.getValue().equals("PASS - Meets full crest width")) {
                img2Formfield(document, form, "PASS1", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("FAIL1");
            } else {
                img2Formfield(document, form, "FAIL1", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("PASS1");
            }
            if (cb2.getValue().equals("PASS (45° bevel away from next thread & 30° toward next thread) ")) {
                img2Formfield(document, form, "PASS2", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("FAIL2");
            } else {
                img2Formfield(document, form, "FAIL2", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("PASS2");
            }
            if (cb3.getValue().equals("PASS - Meets full crest width for >2 TPI")) {
                img2Formfield(document, form, "PASS31", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("PASS32");
                form.removeField("FAIL3");
            } else if (cb3.getValue().equals("PASS – Meets half crest width for <=2TPI")) {
                img2Formfield(document, form, "PASS32", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("PASS31");
                form.removeField("FAIL3");
            } else {
                img2Formfield(document, form, "FAIL3", "D:\\CameronQualityFiles\\DOCS\\ACME\\check.png");
                form.removeField("PASS31");
                form.removeField("PASS32");
            }
            Media mediaImage = caMediaService.getByTypeName("ES", RequestInfo.current().getUserName());
            if (mediaImage == null) {
                NotificationUtils.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
                return;
            }
            media2Formfield(document, form, "SIGN", mediaImage);
            text2Formfield(document, form, "DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void media2Formfield(Document doc, PdfAcroForm form, String fieldName, Media media) throws IOException {
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

    public void img2Formfield(Document doc, PdfAcroForm form, String fieldName, String imgPath) throws IOException {
        PdfFormField field = form.getField(fieldName);
        ImageData imageData = ImageDataFactory.create(imgPath);
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
