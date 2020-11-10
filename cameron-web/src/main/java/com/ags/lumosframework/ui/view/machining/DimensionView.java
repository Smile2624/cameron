//Changed by Cameron: 加入机加工尺寸检验界面，逻辑类似来料尺寸检验，但数据来源为工单而非采购订单

package com.ags.lumosframework.ui.view.machining;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
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
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Menu(caption = "DimensionView", captionI18NKey = "Cameron.DimensionView", iconPath = "images/icon/text-blob.png", groupName = "Production", order = 5)
@SpringView(name = "Dimension", ui = CameronUI.class)
public class DimensionView extends BaseView implements Button.ClickListener {
    private final ComboBox<ProductionOrder> cbProductOrder = new ComboBox<>();
    @I18Support(caption = "Confirm", captionKey = "common.confirm")
    private final Button btnConfirm = new Button();
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
    private final GridLayout gridLayout = new GridLayout();
    private final AbstractComponent[] components = new AbstractComponent[]{cbProductOrder, tfInspectionQuantity, btnConfirm};
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
    ProductionOrder productionOrder = null;
    CaConfig con = null;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private ICaMediaService caMediaService;
    // 选中的checkBox序号
    private String materialNo = "";
    private String materialRev = "";
    private int inspectionQuantitySetted = 0;
    // 抽检数量
    private int inspectionQuantity = 0;
    // 表示当前检验的采购单是否通过检验
    private boolean pass = true;

    public DimensionView() {
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
        btnConfirm.setEnabled(false);
        btnCopy.setCaption("整行复制");
        btnCopy.addClickListener(this);
        tfInspectionQuantity.setPlaceholder("检验数量");
        tfInspectionQuantity.setEnabled(false);
        tfInspectionQuantity.addValueChangeListener(new ValueChangeListener<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                String inputValue = event.getValue();
                displayGrid(inputValue);
                if (Strings.isNullOrEmpty(event.getValue())) {
                    btnConfirm.setEnabled(false);
                } else {
                    btnConfirm.setEnabled(true);
                }
            }
        });
        cbProductOrder.setPlaceholder("工单号");
        cbProductOrder.setItemCaptionGenerator(ProductionOrder::getProductOrderId);

        cbProductOrder.addValueChangeListener(new ValueChangeListener<ProductionOrder>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent<ProductionOrder> event) {
                if (event.getValue() != null) {
                    productionOrder = event.getValue();
                    materialNo = productionOrder.getProductId();
                    materialRev = productionOrder.getProductVersionId();
                    inspectionQuantitySetted = productionOrder.getProductNumber();
                    tfInspectionQuantity.clear();
                    //判断该订单是否锁定
                    if(productionOrder.getBomLockFlag()){
                        throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                                "This Order is locked,Please contact the engineer to solve !"));
                    }
                    // 判断该订单是否需要进行尺寸检验
                    setDataToDisplayArea(productionOrder);
                    List<DimensionRuler> list = dimensionRulerService.getByNoRev(materialNo, materialRev);
                    if (list == null || list.size() == 0) {
                        list = dimensionRulerService.getByNoRev(materialNo, null);
                        if (list.size() > 0) {
                            String lastRev = list.get(list.size() - 1).getMaterialRev();
                            list = dimensionRulerService.getByNoRev(materialNo, lastRev);
                            ConfirmDialog.show(getUI(), "当前版本" + materialRev + "没有维护尺寸模板，是否复制版本" + lastRev + "的尺寸模板",
                                    result -> {
                                        if (Result.OK.equals(result.getResult())) {
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
                                            tfInspectionQuantity.setValue(String.valueOf(productionOrder.getProductNumber()));
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
                        tfInspectionQuantity.setValue(String.valueOf(productionOrder.getProductNumber()));
                    }
                } else {
                    setDataToDisplayArea(null);
                    productionOrder = null;
                    materialNo = "";
                    materialRev = "";
                    tfInspectionQuantity.clear();
                    gridLayout.removeAllComponents();
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
        Panel hlSplitPanel = new Panel();
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);
        inspectionValue.setSizeFull();
        inspectionValue.setContent(gridLayout);
        hlSplitPanel.setContent(inspectionValue);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

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
                        // 保存数据
                        int saveItems = saveDataGroup();
                        //是否检验已经完成，如果已经完成，需要更改采购单的信息以及生成检验报告
                        if (inspectionDone()) {
                            createReportPDF(rootPath);
                        }

                        //将变量初始化
                        gridLayout.removeAllComponents();
                        pass = true;
                        tfSerialNos.clear();
                        NotificationUtils.notificationInfo("保存(更新)信息:" + saveItems + "条");
                    }
                }
            });
        }
//        else if (btnGetData.equals(button)) {
//            String inspectionType = productionOrder.getDescription();
//            if (!Strings.isNullOrEmpty(inspectionType)) {
//                String sapLot = productionOrder.getSapInspectionLot();
//                String snPrefix = productionOrder.getPurchasingNo() + "-"
//                        + productionOrder.getPurchasingItemNo();
//                String snChecked = productionOrder.getCheckedSn();
//                String[] arraySn = snChecked.split(",");
//                for (int k = 0; k < arraySn.length; k++) {
//                    ((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
//                }
//                if ("分组".equals(inspectionType)) {
//                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
//                        for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
//                            // 获取该零件SN对应的行的检验项的检验值
//                            String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
//                            String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
//                                    snPrefix + "-" + arraySn[colmnIndex]);
//                            if (!Strings.isNullOrEmpty(value)) {
//                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
//                            } else {
//                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
//                                break;
//                            }
//                        }
//                    }
//                    // 最后加载量具信息
//                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
//                        String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
//                        String gageNo = dimensionInspectionService.getGageNo(sapLot, inspectionName);
//                        ((TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex)).setValue(gageNo);
//                    }
//                } else {
//                    for (int k = 0; k < arraySn.length; k++) {
//                        ((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
//                    }
//                    for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
//                        for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
//                            String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
//                            String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
//                                    snPrefix + "-" + arraySn[colmnIndex]);
//                            if (!Strings.isNullOrEmpty(value)) {
//                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
//                            } else {
//                                ((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
//                            }
//                        }
//                    }
//                    // 最后加载量具信息
//                    for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
//                        String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
//                        String gageNo = dimensionInspectionService.getGageNo(sapLot, inspectionName);
//                        ((TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex)).setValue(gageNo);
//                    }
//                }
//            }
//        }
        else if (btnCopy.equals(button)) {
            for (int j = 1; j < gridLayout.getRows(); j++) {
                for (int i = 2; i < gridLayout.getColumns() - 2; i++) {
                    if (!Strings.isNullOrEmpty(((TextField) gridLayout.getComponent(i, j)).getValue())) {
                        String temp = ((TextField) gridLayout.getComponent(i, j)).getValue();
                        ((TextField) gridLayout.getComponent(i + 1, j)).setValue(temp);
                    }
                }
            }
        }
    }

    public void setDataToDisplayArea(ProductionOrder productionOrder) {
        if (productionOrder == null) {
            // 清空显示区的信息
            lblMaterialNo.clear();
            lblMaterialRev.clear();
            lblMaterialDesc.clear();
            lblVendor.clear();
            lblQualityPlan.clear();
            lblDrawingNo.clear();
        } else {
            // 填充信息
            String materialNo = productionOrder.getProductId();
            String materialRev = productionOrder.getProductVersionId();
            String materialDesc = productionOrder.getProductDesc();
            String vendor = "";
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

    public void createReportPDF(String path) {
        // 由于尺寸检验的检验数量个检验项都是不可固定的，都有可能超出一页纸范围的可能，这里采用固定列(SN数量)的方式，来动态加载检验项，超过一页的继续往下一页加载，如果检验数量超过一页的范围，则需要在生成一份word保存检验结果
        int dataColumn = gridLayout.getColumns() - 3;// 实际检验数量
        int pageNum = (int) Math.ceil((float) dataColumn / 10);// 实际的检验数量需要几页纸可以加载数据
        path = path + AppConstant.PRODUCTION_PREFIX + AppConstant.DIMENSION_REPORT;
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
                    text2Formfield(document, form, "PN", productionOrder.getProductId());
                    text2Formfield(document, form, "REV", productionOrder.getProductVersionId());
                    text2Formfield(document, form, "PO",
                            productionOrder.getProductOrderId());
                    text2Formfield(document, form, "VENDOR", "");
                    text2Formfield(document, form, "DESC", productionOrder.getProductDesc());
                    text2Formfield(document, form, "IP", lblQualityPlan.getValue());
                    text2Formfield(document, form, "QTY", Integer.toString(productionOrder.getProductNumber()));
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
                        String userName = dimensionInspectionService.getInspector(productionOrder.getProductOrderId(),
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
            PdfWriter writer = new PdfWriter(path + productionOrder.getProductOrderId() + ".pdf");

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

    public int saveDataGroup() {
        pass = true;
        List<DimensionInspectionResult> list = new ArrayList<>();
        int rowCount = gridLayout.getRows();
        int columnCount = gridLayout.getColumns();
        String sapLot = productionOrder.getProductOrderId();
        String snPrefix = productionOrder.getProductOrderId();
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
                dimensionInspectionResult.setOrderItem("NA");
                dimensionInspectionResult.setPurchasingNo(productionOrder.getProductOrderId());
                dimensionInspectionResult.setSapInspectionNo(productionOrder.getProductOrderId());
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

    @Override
    public void _init() {
        cbProductOrder.setItems(productionOrderService.getAllOrder());
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
                productionOrder = cbProductOrder.getValue();
                listInstance = dimensionRulerService.getByNoRev(productionOrder.getProductId(),
                        productionOrder.getProductVersionId());
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
