package com.ags.lumosframework.ui.view.dimensioninspectionruler;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.DimensionRuler;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.service.IDimensionRulerService;
import com.ags.lumosframework.service.ISparePartService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.PermissionConstants;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.view.sparepart.SparePartConditions;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Menu(caption = "DimensionRuler", captionI18NKey = "view.dimensionruler.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 7)
@SpringView(name = "DimensionRuler", ui = CameronUI.class)
@Secured("DimensionRuler")
public class DimensionRulerView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 4854162164548450226L;

    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    @Secured(PermissionConstants.INSPECTIONTYPE_EDIT)
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();


    @Secured("administration:manage")
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    @I18Support(caption = "Delete", captionKey = "common.deletePn")
    private Button btnDelete = new Button();

    //*****************Eric edit new line delete button*******************************
    @I18Support(caption = "Delete Line", captionKey = "line.delete")
    private Button btnDelete1 = new Button();

    @I18Support(caption = "Import Excel", captionKey = "common.import")
    private UploadButton upload = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    //*****************Eric edit new line delete button*******************************
    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnDelete1, btnRefresh};//btnAdd,btnDelete,

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private Grid<DimensionRuler> objectGrid = new Grid<>();
    private Grid<DimensionRuler> itemsGrid = new Grid<DimensionRuler>();

    @Autowired
    private IDimensionRulerService dimensionRulerService;


    @Autowired
    private AddDimensionRulerDialog addDimensionRulerDialog;

    String preInspectionName = "";

    private int num = 0;

    @Autowired
    private ISparePartService partService;

    private TextField tfNo = new TextField();
    private TextField tfRev = new TextField();
    private Button btnSearch = new Button();

    public DimensionRulerView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        //YJ: 加入搜索框
        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);

        hlTemp.addComponents(tfNo, tfRev, btnSearch);
        tfNo.setPlaceholder(I18NUtility.getValue("view.DimensionRuler.MaterialNo", "MaterialNo"));
        tfRev.setPlaceholder(I18NUtility.getValue("view.DimensionRuler.MaterialRev", "MaterialRev"));
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("primary");
        btnSearch.addClickListener(this);

        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        //*****************Eric edit new line delete button*******************************
        btnDelete1.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);

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
                try {
                    String fileName = event.getFilename();
                    if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
                        num = importsxlsx(uploadEvent.getUploadFileInByte(), true);
                    } else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                        num = importsxls(uploadEvent.getUploadFileInByte(), true);
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认导入文件是否正确！");
                }
                refreshGrid();
                NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                num = 0;
            }
        });
        hlTempToolBox.addComponent(upload);

        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);
        objectGrid.setSizeFull();
        objectGrid.addColumn(DimensionRuler::getMaterialNo).setCaption(I18NUtility.getValue("view.DimensionRuler.MaterialNo", "MaterialNo"));
        objectGrid.addColumn(DimensionRuler::getMaterialRev).setCaption(I18NUtility.getValue("view.DimensionRuler.MaterialRev", "MaterialRev"));
        objectGrid.addSelectionListener(event -> {
            Optional<DimensionRuler> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                setDateToItem(optional.get());
            } else {
                setDateToItem(null);
            }
        });
        hlSplitPanel.setFirstComponent(objectGrid);
        itemsGrid.setSizeFull();
        itemsGrid.addColumn(DimensionRuler::getInspectionItemName).setCaption(I18NUtility.getValue("view.DimensionRuler.InspectionItemName", "InspectionItemName"));
        itemsGrid.addColumn(source -> {
            double price = source.getMaxValue();
            if (price == 0) {
                return "NA";
            } else {
                return NumberFormat.getInstance().format(price);
            }
        }).setCaption(I18NUtility.getValue("view.DimensionRuler.maxvalue", "maxvalue"));
        itemsGrid.addColumn(source -> {
            double price = source.getMinValue();
            if (price == 0) {
                return "NA";
            } else {
                return NumberFormat.getInstance().format(price);
            }
        }).setCaption(I18NUtility.getValue("view.DimensionRuler.minvalue", "minvalue"));
        itemsGrid.addSelectionListener(event -> {
            Optional<DimensionRuler> optional = event.getFirstSelectedItem();
            setButtonStatus(optional);

        });
        hlSplitPanel.setSecondComponent(itemsGrid);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<DimensionRuler> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnRefresh.setEnabled(enable);
//		btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());

        refreshGrid();
    }

    private void refreshGrid() {

        List<DimensionRuler> productRoutingList = dimensionRulerService.getByNoRev(tfNo.getValue(), tfRev.getValue());

        objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicateBom(productRoutingList)));

//        objectGrid.setData(removeDuplicateBom(productRoutingList));


    }

    //去重（no+rev）
    private ArrayList<DimensionRuler> removeDuplicateBom(List<DimensionRuler> users) {
        Set<DimensionRuler> set = new TreeSet<DimensionRuler>(new Comparator<DimensionRuler>() {
            @Override
            public int compare(DimensionRuler o1, DimensionRuler o2) {
                return (o1.getMaterialNo() + o1.getMaterialRev()).compareTo(o2.getMaterialNo() + o2.getMaterialRev());
            }
        });
        set.addAll(users);
        return new ArrayList<DimensionRuler>(set);
    }

    private void setDateToItem(DimensionRuler dimensionRuler) {
        if (dimensionRuler != null) {
            itemsGrid.setItems(dimensionRulerService.getByNoRev(dimensionRuler.getMaterialNo(), dimensionRuler.getMaterialRev()));
        } else {
            itemsGrid.setItems(new ArrayList());
        }
    }

    @Override
    public void updateAfterFilterApply() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);

        if (btnEdit.equals(button)) {
//			DimensionRuler dimensionRuler = (DimensionRuler) objectGrid.getSelectedObject();
            DimensionRuler dimensionRuler = itemsGrid.asSingleSelect().getValue();
            addDimensionRulerDialog.setObject(dimensionRuler);
            addDimensionRulerDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
//					DimensionRuler temp = (DimensionRuler) result.getObj();
                    setDateToItem(dimensionRuler);
                }
            });
        } else if (btnAdd.equals(button)) {
//			DimensionRuler dimensionRuler = (DimensionRuler) objectGrid.getSelectedObject();
//			DimensionRuler dimensionRuler =itemsGrid.asSingleSelect().getValue();
            addDimensionRulerDialog.setObject(null);
            addDimensionRulerDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
//					DimensionRuler temp = (DimensionRuler) result.getObj();
//					setDateToItem(dimensionRuler);
                    refreshGrid();
                }
            });
        } else if (btnRefresh.equals(button)) {
            refreshGrid();
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                //****************ERIC edit portion ***********************
                                dimensionRulerService.deleteRulerList(objectGrid.asSingleSelect().getValue().getMaterialNo(), objectGrid.asSingleSelect().getValue().getMaterialRev());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
//                            setDateToItem(objectGrid.asSingleSelect().getValue());
                            refreshGrid();
                        }
                    });
            //*************************************Eric newly add delete button for each item line***********************************
        }  else if (btnDelete1.equals(button)) {
                ConfirmDialog.show(getUI(),
                        I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                            if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                try {
                                    dimensionRulerService.deleteRulerListdetail(objectGrid.asSingleSelect().getValue().getMaterialNo(),itemsGrid.asSingleSelect().getValue().getInspectionItemName());

                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
//                            setDateToItem(objectGrid.asSingleSelect().getValue());
                            refreshGrid();
                        }
                    });
        } else if (btnSearch.equals(button)){
            refreshGrid();
        }
    }


    public int importsxlsx(byte[] bytes, boolean flag) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String materialNo = "";
        String materialRev = "";
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        //获取第二行 合并单元格中的值：
        for (int i = 0; i < xssfSheet.getRow(1).getLastCellNum(); i++) {
            if (isMergedRegion(xssfSheet, 1, i)) {
                String value = getMergedRegionValue(xssfSheet, 1, i);
                if ("".equals(materialNo)) {
                    materialNo = value;
                    System.out.println(materialNo);
                } else {
                    if (!materialNo.equals(value)) {
                        materialRev = value;
                        System.out.println(materialRev);
                        break;
                    }
                }
            }
        }
        if (isMergedRegion(xssfSheet, 5, 8)) {
            String value = getMergedRegionValue(xssfSheet, 5, 8);
            String tempDrawingInfo = getCharacter(value);
            if (tempDrawingInfo.contains("REV")) {
                tempDrawingInfo = tempDrawingInfo.replace("REV", "/");
            } else {
                tempDrawingInfo = tempDrawingInfo.replace("rev", "/");
            }
            String mNo = getCharacter(materialNo);
            String mRev = getCharacter(materialRev);
            SparePart part = partService.getByNoRev(mNo, mRev);
            if (part != null) {
                part.setDrawNo(tempDrawingInfo);
                partService.save(part);
            }
        }
        //判断应该获取第8行的  0列  或 1
        int cellNum = lastColumnOfMergedRegion(xssfSheet, 7, 0);

        // 循环行Row 
        for (int rowNum = 8; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                String inspectionName = "";
                if (isMergedRegion(xssfSheet, rowNum, cellNum)) {
                    inspectionName = getMergedRegionValue(xssfSheet, rowNum, cellNum);
                } else {
                    XSSFCell inspectionNameCell = xssfRow.getCell(cellNum);
                    if (inspectionNameCell != null) {
                        if (inspectionNameCell.getCellType().equals(CellType.NUMERIC)) {
                            inspectionName = String.valueOf(inspectionNameCell.getNumericCellValue());
                        } else {
                            inspectionName = inspectionNameCell.getStringCellValue();
                        }

                    }
                }
                //当有上下纵行合并时  只取一行的值就行
                DimensionRuler dimensionRulerSaved = null;
                if (!Strings.isNullOrEmpty(getCharacter(materialNo)) && !Strings.isNullOrEmpty(getCharacter(materialRev)) && !Strings.isNullOrEmpty(inspectionName)) {
                    dimensionRulerSaved = dimensionRulerService.getByNoRevItemName(getCharacter(materialNo),
                            getCharacter(materialRev), inspectionName);
                }

                // 删除已有的数据
                if (dimensionRulerSaved != null) {
                    // 保存数据
                    if (saveDataToDimensionRuler(dimensionRulerSaved, inspectionName)) {
                    }
                } else {
                    dimensionRulerSaved = new DimensionRuler();
                    dimensionRulerSaved.setMaterialNo("零件号".equals(getChineseCharacter(materialNo)) ? getCharacter(materialNo) : "");
                    dimensionRulerSaved.setMaterialRev("版本".equals(getChineseCharacter(materialRev)) ? getCharacter(materialRev) : "");
                    if (saveDataToDimensionRuler(dimensionRulerSaved, inspectionName)) {
                        coutNum++;
                    }
                }

            }
        }
        xssfWorkbook.close();
        preInspectionName = "";
        return coutNum;
    }

    public int importsxls(byte[] bytes, boolean flag) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String materialNo = "";
        String materialRev = "";
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        //获取第二行 合并单元格中的值：
        for (int i = 0; i < hssfSheet.getRow(1).getLastCellNum(); i++) {
            if (isMergedRegion(hssfSheet, 1, i)) {
                String value = getMergedRegionValue(hssfSheet, 1, i);
                if ("".equals(materialNo)) {
                    materialNo = value;
                } else {
                    if (!materialNo.equals(value)) {
                        materialRev = value;
                        break;
                    }
                }
            }
        }
        if (isMergedRegion(hssfSheet, 5, 8)) {
            String value = getMergedRegionValue(hssfSheet, 5, 8);
            String tempDrawingInfo = getCharacter(value);
            if (tempDrawingInfo.contains("REV")) {
                tempDrawingInfo = tempDrawingInfo.replace("REV", "/");
            } else {
                tempDrawingInfo = tempDrawingInfo.replace("rev", "/");
            }
            String mNo = getCharacter(materialNo);
            String mRev = getCharacter(materialRev);
            SparePart part = partService.getByNoRev(mNo, mRev);
            if (part != null) {
                part.setDrawNo(tempDrawingInfo);
                partService.save(part);
            }
        }
        //判断应该获取第8行的  0列  或 1
        int cellNum = lastColumnOfMergedRegion(hssfSheet, 7, 0);

        // 循环行Row 
        for (int rowNum = 8; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                String inspectionName = "";
                if (isMergedRegion(hssfSheet, rowNum, cellNum)) {
                    inspectionName = getMergedRegionValue(hssfSheet, rowNum, cellNum);
                } else {
                    HSSFCell inspectionNameCell = hssfRow.getCell(cellNum);
                    if (inspectionNameCell != null) {
                        if (inspectionNameCell.getCellType().equals(CellType.NUMERIC)) {
                            inspectionName = String.valueOf(inspectionNameCell.getNumericCellValue());
                        } else {
                            inspectionName = inspectionNameCell.getStringCellValue();
                        }
                    }
                }
                DimensionRuler dimensionRulerSaved = null;
                if (!Strings.isNullOrEmpty(getCharacter(materialNo))
                        && !Strings.isNullOrEmpty(getCharacter(materialRev))
                        && !Strings.isNullOrEmpty(inspectionName)) {
                    dimensionRulerSaved = dimensionRulerService.getByNoRevItemName(getCharacter(materialNo),
                            getCharacter(materialRev), inspectionName);
                }
                if (dimensionRulerSaved != null) {
                    if (saveDataToDimensionRuler(dimensionRulerSaved, inspectionName)) {
                    }
                } else {
                    dimensionRulerSaved = new DimensionRuler();
                    if (saveDataToDimensionRuler(dimensionRulerSaved, inspectionName)) {
                        coutNum++;
                    }
                }
            }
        }
        hssfWorkbook.close();
        preInspectionName = "";
        return coutNum;
    }

    //判断是否有合并单元格
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

    //所在的合并单元格最后一列  不是合并单元格就是当前列
    private int lastColumnOfMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return lastColumn;
                }
            }
        }
        return 0;
    }

    //所在的合并单元格中的行的    行数差
    private int rowsOfMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return lastRow - firstRow;
                }
            }
        }
        return 0;
    }

    public boolean saveDataToDimensionRuler(DimensionRuler dimensionRuler, String inspectionName) {
        boolean flag = false;
        String inspectionItemType = "";//NUMBER  BOOLEAN
        if (!"".equals(inspectionName)) {
            System.out.println(inspectionName);
            String baseNumber = "";
            String floatNumber = "";
            String minValue = "";
            String maxValue = "";
            double a = 0;
            int indexDiameter = inspectionName.indexOf("Φ");
            int indexDiameterAlso = inspectionName.indexOf("Ø");
            int indexsuffix = inspectionName.indexOf("ϕ");
            int indexAddSubtract = inspectionName.indexOf("±");
            int indexAdd = inspectionName.indexOf("+");

            int indexSubtract = inspectionName.indexOf("-");
            int indexTilde = inspectionName.indexOf("~");
            int indexSlash = inspectionName.indexOf("/");

            if (indexAddSubtract != -1) {//±  Φ3.995±0.002   5.141±0.015 0.210°±0.015°
                if (indexDiameter != -1) {
                    baseNumber = inspectionName.substring(indexDiameter + 1, indexAddSubtract).trim();
                    floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length()).trim();
                } else if (indexDiameterAlso != -1) {
                    baseNumber = inspectionName.substring(indexDiameterAlso + 1, indexAddSubtract).trim();
                    floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length()).trim();
                } else if (indexsuffix != -1) {
                    baseNumber = inspectionName.substring(indexsuffix + 1, indexAddSubtract).trim();
                    floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length()).trim();
                } else if (inspectionName.indexOf("Ф") != -1) {
                    baseNumber = inspectionName.substring(inspectionName.indexOf("Ф") + 1, indexAddSubtract).trim();
                    floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length()).trim();
                } else {
                    if (inspectionName.indexOf("°") > -1) {
                        baseNumber = inspectionName.substring(0, inspectionName.indexOf("°")).trim();
                        floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length() - 1).trim();
                    } else {
                        baseNumber = inspectionName.substring(0, indexAddSubtract).trim();
                        floatNumber = inspectionName.substring(indexAddSubtract + 1, inspectionName.length()).trim();
                    }

                }

                BigDecimal bn = new BigDecimal(baseNumber);
                BigDecimal fn = new BigDecimal(floatNumber);
                dimensionRuler.setMaxValue((bn.add(fn)).doubleValue());//相加后 转换为double
                dimensionRuler.setMinValue((bn.subtract(fn)).doubleValue());//相减后 转换为double
                inspectionItemType = "NUMBRIC";
            } else if (indexDiameterAlso != -1 && indexTilde != -1) {//  Ø1.235~1.240
                minValue = inspectionName.substring(1, indexTilde).trim();
                maxValue = inspectionName.substring(indexTilde + 1, inspectionName.length()).trim();
                dimensionRuler.setMaxValue(Double.valueOf(maxValue) > Double.valueOf(minValue) ? Double.valueOf(maxValue) : Double.valueOf(minValue));
                dimensionRuler.setMinValue(Double.valueOf(maxValue) < Double.valueOf(minValue) ? Double.valueOf(maxValue) : Double.valueOf(minValue));
                inspectionItemType = "NUMBRIC";
            } else if (indexSubtract != -1 && indexSlash == -1) {//Φ2.997-0.007  2.997-0.007
                if (indexDiameter != -1) {
                    baseNumber = inspectionName.substring(1, indexSubtract).trim();
                } else {
                    baseNumber = inspectionName.substring(0, indexSubtract).trim();
                }
                floatNumber = inspectionName.substring(indexSubtract + 1, inspectionName.length()).trim();
                if (valueIsNumber(baseNumber) && valueIsNumber(floatNumber)) {
                    BigDecimal bn = new BigDecimal(baseNumber);
                    BigDecimal fn = new BigDecimal(floatNumber);
                    dimensionRuler.setMaxValue(Double.valueOf(baseNumber));//转换为double
                    dimensionRuler.setMinValue((bn.subtract(fn)).doubleValue());//相减  转换为double
                    inspectionItemType = "NUMBRIC";
                } else {
                    dimensionRuler.setMaxValue(a);//相加后 转换为double
                    dimensionRuler.setMinValue(a);// 转换为double
                    inspectionItemType = "BOOLEAN";
                }
            } else if (indexAdd != -1 && indexSlash == -1) {// Φ2.062+0.005  0.150+0.005
                if (indexDiameter != -1) {
                    baseNumber = inspectionName.substring(1, indexAdd).trim();
                } else {
                    baseNumber = inspectionName.substring(0, indexAdd).trim();
                }
                floatNumber = inspectionName.substring(indexAdd + 1, inspectionName.length()).trim();
                if (valueIsNumber(baseNumber) && valueIsNumber(floatNumber)) {
                    BigDecimal bn = new BigDecimal(baseNumber);
                    BigDecimal fn = new BigDecimal(floatNumber);
                    dimensionRuler.setMaxValue((bn.add(fn)).doubleValue());//相加后 转换为double
                    dimensionRuler.setMinValue(Double.valueOf(baseNumber));// 转换为double
                    inspectionItemType = "NUMBERIC";
                } else {
                    dimensionRuler.setMaxValue(a);
                    dimensionRuler.setMinValue(a);
                    inspectionItemType = "BOOLEAN";
                }
            } else if (indexAdd != -1 && indexSubtract != -1 && indexSlash != -1) {// Φ2.075+0.015/-0.000   3.38+0.188/-0.063  Φ0.676-0.005/+0.000
                String floatNumberAdd = "";
                String floatNumberSubtract = "";
                if (indexDiameter != -1) {
                    baseNumber = inspectionName.substring(1, indexAdd < indexSubtract ? indexAdd : indexSubtract).trim();
                } else {
                    baseNumber = inspectionName.substring(0, indexAdd < indexSubtract ? indexAdd : indexSubtract).trim();
                }
                if (indexAdd < indexSubtract) {
                    floatNumberAdd = inspectionName.substring(indexAdd + 1, indexSlash);
                    floatNumberSubtract = inspectionName.substring(indexSubtract + 1, inspectionName.length()).trim();
                } else {
                    floatNumberSubtract = inspectionName.substring(indexSubtract + 1, indexSlash).trim();
                    floatNumberAdd = inspectionName.substring(indexAdd + 1, inspectionName.length()).trim();
                }
                if (valueIsNumber(baseNumber) && valueIsNumber(floatNumberAdd) && valueIsNumber(floatNumberSubtract)) {
                    BigDecimal bn = new BigDecimal(baseNumber);
                    BigDecimal fnAdd = new BigDecimal(floatNumberAdd);
                    BigDecimal fnSubtract = new BigDecimal(floatNumberSubtract);
                    dimensionRuler.setMaxValue((bn.add(fnAdd)).doubleValue());//相加后 转换为double
                    dimensionRuler.setMinValue((bn.subtract(fnSubtract)).doubleValue());//相减后  转换为double
                    inspectionItemType = "NUMBERIC";
                } else {
                    dimensionRuler.setMaxValue(a);
                    dimensionRuler.setMinValue(a);
                    inspectionItemType = "BOOLEAN";
                }
            } else {
                if (valueIsNumber(inspectionName)) {
                    dimensionRuler.setMaxValue(Double.parseDouble(inspectionName));
                    dimensionRuler.setMinValue(Double.parseDouble(inspectionName));
                    inspectionItemType = "NUMBRIC";
                } else {
                    dimensionRuler.setMaxValue(a);
                    dimensionRuler.setMinValue(a);
                    inspectionItemType = "BOOLEAN";
                }
            }

            dimensionRuler.setInspectionItemName(inspectionName);
            dimensionRuler.setInspectionItemType(inspectionItemType);
            dimensionRulerService.save(dimensionRuler);
            flag = true;
        }
        return flag;
    }

    public boolean valueIsNumber(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            boolean isNumber = RegExpValidatorUtils.isIsNumber(value.trim());
            if (!isNumber) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //获取合并单元格的值
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
        return null;
    }

    public String getCellValue(Cell cell) {
        if (cell == null) return "";
        return cell.getStringCellValue();
    }

    public static String getChineseCharacter(String a) {
        String str = "";
        if ((!(a.length() == a.getBytes().length))) {
            String reg1 = "[^\\u4e00-\\u9fa5]";
            str = a.replaceAll(reg1, "");
        }
        return str;
    }

    public static String getCharacter(String a) {
        String str = "";
        if ((!(a.length() == a.getBytes().length))) {
            String reg1 = "[ \\u4e00-\\u9fa5]";
            str = a.replaceAll(reg1, "");
        }
        return str;
    }
}
