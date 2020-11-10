package com.ags.lumosframework.ui.view.uniquetraceabilityrecord;

import com.ags.lumosframework.pojo.UniqueTraceability;
import com.ags.lumosframework.service.IUniqueTraceabilityService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.google.common.base.Strings;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Menu(caption = "UniqueTraceabilityRecord", captionI18NKey = "view.uniquetraceabilityrecord.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 15)
@SpringView(name = "UniqueTraceabilityRecord", ui = CameronUI.class)
@Secured("administration:manage")
public class UniqueTraceabilityRecordView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = -4142494613253628485L;

    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();

    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();

    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    @I18Support(caption = "Import", captionKey = "common.import")
    private UploadButton upload = new UploadButton();

    @I18Support(caption = "MaterialNo", captionKey = "view.uniquedimensioninspectionview.materialno")
    private LabelWithSamleLineCaption lblMaterialNo = new LabelWithSamleLineCaption();

    @I18Support(caption = "MaterialRev", captionKey = "view.uniquedimensioninspectionview.materialrev")
    private LabelWithSamleLineCaption lblMaterialRev = new LabelWithSamleLineCaption();
    private TextField tfPurchasingNo = new TextField();

    private Button btnSearch = new Button();

    private UploadFinishEvent uploadEvent = null;

    private Button[] btns = new Button[]{btnRefresh};// btnAdd ,btnEdit, btnDelete,

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private Grid<UniqueTraceability> objectGrid = new Grid<>();

    private Grid<UniqueTraceability> objectItemGrid = new Grid<>();

    @Autowired
    IUniqueTraceabilityService uniqueTraceabilityService;

    List<String> inspectionItemsList = new ArrayList<>();// 保存每一个sheet的检验项。sheet中的第四行第1列开始

    private UniqueTraceability selectedInstance = null;

    List<UniqueTraceability> uniqueTraceabilityList = new ArrayList<>();

    public UniqueTraceabilityRecordView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);
        hlTemp.addComponents(tfPurchasingNo, btnSearch);
        tfPurchasingNo.setPlaceholder(I18NUtility.getValue("view.materialinspection.purchasingno", "PurchasingNo"));
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("primary");
        btnSearch.addClickListener(this);
        hlTempToolBox.addComponent(upload);
        FileUploader fileUploader = new FileUploader(new UploadFinishedListener() {
            @Override
            public void finish(UploadFinishEvent event) {
                uploadEvent = event;
            }
        });

        upload.setIcon(VaadinIcons.UPLOAD);
        upload.setImmediateMode(true);
        upload.setReceiver(fileUploader);
        upload.addSucceededListener(fileUploader);
        upload.addFinishedListener(new Upload.FinishedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                int num = 0;
                try {
                    String fileName = event.getFilename();
                    if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
                        num = importsxlsx(uploadEvent.getUploadFileInByte());
                    } else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                        num = importsxls(uploadEvent.getUploadFileInByte());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                    NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认文件格式是否正确");
                }
            }

        });
        // 显示零件信息
        HorizontalLayout hlDisplay = new HorizontalLayout();
        hlDisplay.addComponent(lblMaterialNo);
        hlDisplay.addComponent(lblMaterialRev);
        hlDisplay.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        vlRoot.addComponent(hlDisplay);
        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        objectGrid.setSizeFull();
        objectGrid.addColumn(new ValueProvider<UniqueTraceability, String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String apply(UniqueTraceability source) {
                return source.getPurchasingNo().substring(0, source.getPurchasingNo().lastIndexOf("-"));

            }
        }).setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.po", "PurchasingOrder"));
        objectGrid.addSelectionListener(event -> {
            Optional<UniqueTraceability> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                selectedInstance = optional.get();
                lblMaterialNo.setValue(selectedInstance.getMaterialNo());
                lblMaterialRev.setValue(selectedInstance.getMaterialRev());
                setDateToItem(optional.get());
            } else {
                selectedInstance = null;
                lblMaterialNo.setValue(null);
                lblMaterialRev.setValue(null);
                setDateToItem(null);
            }
        });
        hlSplitPanel.setFirstComponent((Component) objectGrid);

        objectItemGrid.setSizeFull();
        objectItemGrid.addColumn(UniqueTraceability::getHeatNo)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.heatno", "HeatNo")).setWidth(120);
        objectItemGrid.addColumn(UniqueTraceability::getHlLotNo)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.hllotno", "HlLotNo")).setWidth(120);
        objectItemGrid.addColumn(UniqueTraceability::getPurchasingNo)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.posn", "PurchasingNo")).setWidth(200);
        objectItemGrid.addColumn(UniqueTraceability::getQuantity)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.quantity", "Quantity")).setWidth(60);
        objectItemGrid.addColumn(UniqueTraceability::getIfQCN)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.ifqcn", "IfQCN")).setWidth(80);
        objectItemGrid.addColumn(UniqueTraceability::getComment)
                .setCaption(I18NUtility.getValue("view.uniquetraceabilityrecord.comment", "Comment")).setWidth(200);
        objectItemGrid.addSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });

        hlSplitPanel.setSecondComponent((Component) objectItemGrid);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<UniqueTraceability> firstSelectedItem) {
        if (firstSelectedItem.isPresent()) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
        } else {
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void setDateToItem(UniqueTraceability uniqueTraceability) {
        if (uniqueTraceability == null) {
            objectItemGrid.setItems(new ArrayList<UniqueTraceability>());
        } else {
            objectItemGrid
                    .setDataProvider(DataProvider.ofCollection(uniqueTraceabilityService.getByPoSn(uniqueTraceability
                            .getPurchasingNo().substring(0, uniqueTraceability.getPurchasingNo().lastIndexOf("-")))));
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (button.equals(btnEdit)) {

        } else if (button.equals(btnDelete)) {

        } else if (button.equals(btnRefresh)) {
            tfPurchasingNo.clear();
            refreshGrid(tfPurchasingNo.getValue().trim());
        } else if (button.equals(btnSearch)) {
            refreshGrid(tfPurchasingNo.getValue().trim());
        }
    }

    /**
     * @param bytes
     * @return
     * @throws IOException 导入。xlsx格式的文件
     */
    public int importsxlsx(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String materialNo = "";
        String materialRev = "";
        String cusOrder = "";
        String material_Desc = "";
        String heatNo = "";
        String htLotNo = "";
        String custPoSn = "";
        String purchasingSN = "";
        String quantity = "";
        String ifQCN = "";
        String comment = "";
        String warehouseConfirmer = "";
        String qaConfirmer = "";
        String qcConfirmer = "";
        String whDate = "";
        String qaDate = "";
        String qcDate = "";
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<UniqueTraceability> list = new ArrayList<>();
        int sheetCount = xssfWorkbook.getNumberOfSheets();// 文件中含有多个sheet,需要便利每个sheet，知道出现空值的时候结束遍历
        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
            // 获取每一个sheet的公共属性信息
            if (sheetIndex == 0) {
                // 零件信息
                String materialInfo = getMergedRegionValue(xssfSheet, 4, 3);
                materialNo = materialInfo.split("REV.")[0];
                materialRev = materialInfo.split("REV.")[1];
                XSSFCell orderCell = xssfSheet.getRow(4).getCell(7);
                orderCell.setCellType(CellType.STRING);
                if (uniqueTraceabilityService.countByCustOrder(cusOrder) > 0) {
                    NotificationUtils.notificationError("当前导入的追溯信息已经存在,请勿重复导入");
                    break;
                }
                material_Desc = getMergedRegionValue(xssfSheet, 5, 2);
            }
            if (Strings.isNullOrEmpty(xssfSheet.getRow(9).getCell(4).getStringCellValue())) {
                break;
            }
            // 检验员信息，每个sheet获取一次，可能会有不同
//            String warehouseConfirmerInfo = getMergedRegionValue(xssfSheet, 24, 2);// 仓库检验员
//            if (!Strings.isNullOrEmpty(warehouseConfirmerInfo)) {
//                warehouseConfirmer = warehouseConfirmerInfo.split(" ")[0];
//                whDate = warehouseConfirmerInfo.split(" ")[1];
//            } else {
//                warehouseConfirmerInfo = getMergedRegionValue(xssfSheet, 25, 2);// 仓库检验员
//                warehouseConfirmer = warehouseConfirmerInfo.split(" ")[0];
//                whDate = warehouseConfirmerInfo.split(" ")[1];
//            }
//
//            // qa.qc检验员信息
//
//            qaConfirmer = getMergedRegionValue(xssfSheet, 25, 4).split(" ")[0];
//            qaDate = getMergedRegionValue(xssfSheet, 25, 4).split(" ")[1];
//            qcConfirmer = getMergedRegionValue(xssfSheet, 25, 6).split(" ")[0];
//            qcDate = getMergedRegionValue(xssfSheet, 25, 4).split(" ")[1];
            for (int rowIndex = 0; rowIndex < xssfSheet.getLastRowNum(); rowIndex++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                if (rowIndex >= 9 && rowIndex <= 23) {
                    // 保存物料信息
                    XSSFCell cellHeatNo = xssfRow.getCell(1);
                    XSSFCell cellHTLotNo = xssfRow.getCell(2);
                    XSSFCell cellCusPoSn = xssfRow.getCell(3);
                    XSSFCell cellPurchasingSN = xssfRow.getCell(4);
                    XSSFCell cellQuantity = xssfRow.getCell(5);
                    XSSFCell cellIfQCN = xssfRow.getCell(6);
                    XSSFCell cellComment = xssfRow.getCell(7);
                    cellQuantity.setCellType(CellType.STRING);
                    cellHeatNo.setCellType(CellType.STRING);
                    heatNo = cellHeatNo.getStringCellValue() == null ? "" : cellHeatNo.getStringCellValue();
                    if (Strings.isNullOrEmpty(heatNo)) {
                        break;
                    }
                    htLotNo = cellHTLotNo.getStringCellValue() == null ? "" : cellHTLotNo.getStringCellValue();
                    custPoSn = cellCusPoSn.getStringCellValue() == null ? "" : cellCusPoSn.getStringCellValue();
                    purchasingSN = cellPurchasingSN.getStringCellValue() == null ? ""
                            : cellPurchasingSN.getStringCellValue();
                    quantity = cellQuantity.getStringCellValue() == null ? "" : cellQuantity.getStringCellValue();
                    ifQCN = cellIfQCN.getStringCellValue() == null ? "" : cellIfQCN.getStringCellValue();
                    comment = cellComment.getStringCellValue() == null ? "" : cellComment.getStringCellValue();

                    UniqueTraceability instance = new UniqueTraceability();
                    instance.setMaterialNo(materialNo);
                    instance.setMaterialRev(materialRev);
                    instance.setMaterialDesc(material_Desc);
                    instance.setPurchasingNo(purchasingSN);
                    instance.setCustOrder(cusOrder);
                    instance.setCustNo(custPoSn);
                    instance.setHeatNo(heatNo);
                    instance.setHlLotNo(htLotNo);
                    instance.setQuantity(Strings.isNullOrEmpty(quantity) ? 1 : Integer.parseInt(quantity));
                    instance.setWarehouseConfirmer(warehouseConfirmer);
                    instance.setQaConfirmer(qaConfirmer);
                    instance.setQcConfirmer(qcConfirmer);
                    instance.setIfQCN(ifQCN);
                    instance.setComment(comment);
                    instance.setWhDate(whDate);
                    instance.setQaDate(qaDate);
                    instance.setQcDate(qcDate);

                    list.add(instance);
                }
            }
            uniqueTraceabilityService.saveAll(list);
        }
        xssfWorkbook.close();
        return coutNum;
    }

    public int importsxls(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String materialNo = "";
        String materialRev = "";
        String cusOrder = "";
        String material_Desc = "";
        String heatNo = "";
        String htLotNo = "";
        String custPoSn = "";
        String purchasingSN = "";
        String quantity = "";
        String ifQCN = "";
        String comment = "";
        String warehouseConfirmer = "";
        String qaConfirmer = "";
        String qcConfirmer = "";
        String whDate = "";
        String qaDate = "";
        String qcDate = "";
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<UniqueTraceability> list = new ArrayList<>();
        int sheetCount = hssfWorkbook.getNumberOfSheets();// 文件中含有多个sheet,需要便利每个sheet，知道出现空值的时候结束遍历
        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetIndex);
            // 获取每一个sheet的公共属性信息
            if (sheetIndex == 0) {
                // 零件信息
                String materialInfo = getMergedRegionValue(hssfSheet, 4, 3);
                materialNo = materialInfo.split("REV.")[0];
                materialRev = materialInfo.split("REV.")[1];
                HSSFCell orderCell = hssfSheet.getRow(4).getCell(7);
                orderCell.setCellType(CellType.STRING);
                cusOrder = hssfSheet.getRow(4).getCell(7).getStringCellValue();
                if (uniqueTraceabilityService.countByCustOrder(cusOrder) > 0) {
                    NotificationUtils.notificationError("当前追溯记录已经导入，请勿重复导入");
                    break;
                }
                material_Desc = getMergedRegionValue(hssfSheet, 5, 2);
            }
            if (Strings.isNullOrEmpty(hssfSheet.getRow(9).getCell(4).getStringCellValue())) {
                break;
            }
            // 检验员信息，每个sheet获取一次，可能会有不同
//            String warehouseConfirmerInfo = getMergedRegionValue(hssfSheet, 24, 2);// 仓库检验员
//            if (!Strings.isNullOrEmpty(warehouseConfirmerInfo)) {
//                warehouseConfirmer = warehouseConfirmerInfo.split(" ")[0];
//                whDate = warehouseConfirmerInfo.split(" ")[1];
//            } else {
//                warehouseConfirmerInfo = getMergedRegionValue(hssfSheet, 25, 2);// 仓库检验员
//                warehouseConfirmer = warehouseConfirmerInfo.split(" ")[0];
//                whDate = warehouseConfirmerInfo.split(" ")[1];
//            }
//
//            // qa.qc检验员信息
//
//            qaConfirmer = getMergedRegionValue(hssfSheet, 25, 4).split(" ")[0];
//            qaDate = getMergedRegionValue(hssfSheet, 25, 4).split(" ")[1];
//            qcConfirmer = getMergedRegionValue(hssfSheet, 25, 6).split(" ")[0];
//            qcDate = getMergedRegionValue(hssfSheet, 25, 4).split(" ")[1];
            for (int rowIndex = 0; rowIndex < hssfSheet.getLastRowNum(); rowIndex++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                if (rowIndex >= 9 && rowIndex <= 23) {
                    // 保存物料信息
                    HSSFCell cellHeatNo = hssfRow.getCell(1);
                    HSSFCell cellHTLotNo = hssfRow.getCell(2);
                    HSSFCell cellCusPoSn = hssfRow.getCell(3);
                    HSSFCell cellPurchasingSN = hssfRow.getCell(4);
                    HSSFCell cellQuantity = hssfRow.getCell(5);
                    HSSFCell cellIfQCN = hssfRow.getCell(6);
                    HSSFCell cellComment = hssfRow.getCell(7);
                    cellQuantity.setCellType(CellType.STRING);
                    cellHeatNo.setCellType(CellType.STRING);
                    heatNo = cellHeatNo.getStringCellValue() == null ? "" : cellHeatNo.getStringCellValue();
                    if (Strings.isNullOrEmpty(heatNo)) {
                        break;
                    }
                    htLotNo = cellHTLotNo.getStringCellValue() == null ? "" : cellHTLotNo.getStringCellValue();
                    custPoSn = cellCusPoSn.getStringCellValue() == null ? "" : cellCusPoSn.getStringCellValue();
                    purchasingSN = cellPurchasingSN.getStringCellValue() == null ? ""
                            : cellPurchasingSN.getStringCellValue();
                    quantity = cellQuantity.getStringCellValue() == null ? "" : cellQuantity.getStringCellValue();
                    ifQCN = cellIfQCN.getStringCellValue() == null ? "" : cellIfQCN.getStringCellValue();
                    comment = cellComment.getStringCellValue() == null ? "" : cellComment.getStringCellValue();

                    UniqueTraceability instance = new UniqueTraceability();
                    instance.setMaterialNo(materialNo);
                    instance.setMaterialRev(materialRev);
                    instance.setMaterialDesc(material_Desc);
                    instance.setPurchasingNo(purchasingSN);
                    instance.setPurchasingOrder(purchasingSN.split("-")[0]);
                    instance.setCustOrder(cusOrder);
                    instance.setCustNo(custPoSn);
                    instance.setHeatNo(heatNo);
                    instance.setHlLotNo(htLotNo);
                    instance.setQuantity(Strings.isNullOrEmpty(quantity) ? 1 : Integer.parseInt(quantity));
                    instance.setWarehouseConfirmer(warehouseConfirmer);
                    instance.setQaConfirmer(qaConfirmer);
                    instance.setQcConfirmer(qcConfirmer);
                    instance.setIfQCN(ifQCN);
                    instance.setComment(comment);
                    instance.setWhDate(whDate);
                    instance.setQaDate(qaDate);
                    instance.setQcDate(qcDate);
                    list.add(instance);
                }
            }
            uniqueTraceabilityService.saveAll(list);
        }
        hssfWorkbook.close();
        return list.size();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        refreshGrid(tfPurchasingNo.getValue().trim());
    }

    private void refreshGrid(String po) {
        uniqueTraceabilityList = uniqueTraceabilityService.getByPoSn(po);
        objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicate(uniqueTraceabilityList)));
    }

    private ArrayList<UniqueTraceability> removeDuplicate(List<UniqueTraceability> list) {
        Set<UniqueTraceability> set = new TreeSet<UniqueTraceability>(new Comparator<UniqueTraceability>() {
            @Override
            public int compare(UniqueTraceability o1, UniqueTraceability o2) {
                return (o1.getPurchasingNo().substring(0, o1.getPurchasingNo().lastIndexOf("-")))
                        .compareTo((o2.getPurchasingNo().substring(0, o2.getPurchasingNo().lastIndexOf("-"))));
            }
        });
        set.addAll(list);
        return new ArrayList<UniqueTraceability>(set);
    }

    // 判断是否有合并单元格
    private boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    // 获取合并单元格的值
    public String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return "";
    }

    public String getCellValue(Cell cell) {

        if (cell == null)
            return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return cell.getCellFormula();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }
}
