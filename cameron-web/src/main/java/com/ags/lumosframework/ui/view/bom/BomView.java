package com.ags.lumosframework.ui.view.bom;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.RetrospectType;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.service.IBomService;
import com.ags.lumosframework.service.IProductInformationService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.service.ISparePartService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.util.MyObjectListGrid;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectClickListener;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

@Menu(caption = "Bom", captionI18NKey = "Cameron.Bom", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 3)
@SpringView(name = "Bom", ui = CameronUI.class)
@Secured("Bom")
public class BomView extends BaseView implements Button.ClickListener {

    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();

    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();

    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    private TextField tfNo = new TextField();
    private TextField tfRev = new TextField();
    private Button btnSearch = new Button();

    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh};

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private Grid<Bom> objectGrid = new Grid<>();

    //private IDomainObjectGrid<Bom> objectItemGrid = new PaginationDomainObjectList<>();
    private IDomainObjectGrid<Bom> objectItemGrid = new MyObjectListGrid<>(false);

    @I18Support(caption = "ImportExcel", captionKey = "cameron.ImportExcel")
    private UploadButton upload = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    @Autowired
    private AddBomDialog addBomDialog;
    @Autowired
    private IBomService bomService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IProductionOrderService orderService;

    @Autowired
    private BomLongTextShowDialog longTextDialog;
    private int num = 0;

    public BomView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleNames(CoreTheme.TOOLBOX, CoreTheme.INPUT_DISPLAY_INLINE);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);

        hlTemp.addComponents(tfNo, tfRev, btnSearch);
        tfNo.setPlaceholder(I18NUtility.getValue("Bom.ProductNo", "ProductNo"));
        tfRev.setPlaceholder(I18NUtility.getValue("Bom.ProductRev", "ProductRev"));
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
                    if (uploadEvent.getFileName().endsWith(".xls") || uploadEvent.getFileName().endsWith(".XLS")) {
                        num = importXls(uploadEvent.getUploadFileInByte());
                    } else if (uploadEvent.getFileName().endsWith(".xlsx") || uploadEvent.getFileName().endsWith(".XLSX")) {
                        num = importXlsx(uploadEvent.getUploadFileInByte());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }

                    NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                    num = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认模板是否正确");
                }
            }
        });
        hlTempToolBox.addComponent(upload);

        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        objectGrid.setSizeFull();
        objectGrid.addColumn(Bom::getProductNo).setCaption(I18NUtility.getValue("Bom.ProductNo", "ProductNo"));
        objectGrid.addColumn(Bom::getProductRev).setCaption(I18NUtility.getValue("Bom.ProductRev", "ProductRev"));
        objectGrid.addSelectionListener(event -> {
            Optional<Bom> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                setDateToItem(optional.get());
            } else {
                setDateToItem(null);
            }
        });
        hlSplitPanel.setFirstComponent((Component) objectGrid);

//		objectItemGrid.setSizeFull();
        objectItemGrid.addColumn(Bom::getItemCategory).setCaption(I18NUtility.getValue("Bom.ItemCategory", "ItemCategory"));
        objectItemGrid.addColumn(Bom::getExplosionLevel).setCaption(I18NUtility.getValue("Bom.ExplosionLevel", "Level"));
        objectItemGrid.addColumn(Bom::getSortString).setCaption(I18NUtility.getValue("Bom.SortString", "SortString"));
        objectItemGrid.addColumn(Bom::getItemNo).setCaption(I18NUtility.getValue("Bom.ItemNo", "ItemNo"));
        objectItemGrid.addColumn(Bom::getPartNo).setCaption(I18NUtility.getValue("SparePart.SparePartNo", "SparePartNo"));
        objectItemGrid.addColumn(Bom::getPartRev).setCaption(I18NUtility.getValue("SparePart.SparePartRev", "SparePartRev"));
        objectItemGrid.addColumn(source ->
                Strings.isNullOrEmpty(source.getPartRev()) ? source.getLegacyRev() : ""
        ).setCaption(I18NUtility.getValue("SparePart.LegacyRev", "LegacyRev"));
        objectItemGrid.addColumn(Bom::getPartQuantity).setCaption(I18NUtility.getValue("Bom.PartQuantity", "PartQuantity"));
        objectItemGrid.addColumn(Bom::getBaseUnit).setCaption(I18NUtility.getValue("Bom.BaseUnit", "Unit"));
        objectItemGrid.addColumn(Bom::getPartDes).setCaption(I18NUtility.getValue("SparePart.SparePartDec", "SparePartDec"));
        objectItemGrid.addColumn(source ->
                source.isRetrospect() == true ? "是" : "否"
        ).setCaption(I18NUtility.getValue("Bom.IsRetrospect", "IsRetrospect")).setHidden(true);
        objectItemGrid.addColumn(source -> {
            if (source.getRetrospectType() == null || "".equals(source.getRetrospectType())) {
                return "";
            } else {
                return RetrospectType.SINGLE.getType().equals(source.getRetrospectType()) ? "单件" : "批次";
            }
        }).setCaption(I18NUtility.getValue("Bom.RetrospectType", "RetrospectType")).setHidden(true);
        objectItemGrid.addColumn(Bom::getLongText).setCaption(I18NUtility.getValue("Bom.LongText", "LongText"));
//        objectItemGrid.addComponentColumn((ValueProvider<Bom, VerticalLayout>) source -> {
//            VerticalLayout vlLongtext=new VerticalLayout();
//            TextArea taLongText=new TextArea();
//            taLongText.setValue(source.getLongText());
//            vlLongtext.addComponent(taLongText);
//            return vlLongtext;
//        }).setCaption(I18NUtility.getValue("Bom.LongText", "LongText"));
        objectItemGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });
        objectItemGrid.setObjectClickListener((IObjectClickListener<Bom>) event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Bom bom = event.getItem();
                longTextDialog.setObject(bom);
                longTextDialog.show(getUI(), result -> {

                });
            }

        });
        hlSplitPanel.setSecondComponent((Component) objectItemGrid);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<Bom> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            String[] parameters = event.getParameters().split("/");
            tfNo.setValue(parameters[0]);
            tfRev.setValue(parameters[1]);
        }
        refreshGrid();
        if (bomService.getBomsByNoRev(tfNo.getValue(), tfRev.getValue()) != null) {
            objectGrid.select(bomService.getBomsByNoRev(tfNo.getValue(), tfRev.getValue()).get(0));
        }
    }

    private void refreshGrid() {
        List<Bom> bomList = bomService.getBomsByNoRev(tfNo.getValue(), tfRev.getValue());

        objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicateBom(bomList)));
    }

    // 去重（no+rev）
    private ArrayList<Bom> removeDuplicateBom(List<Bom> boms) {
        Set<Bom> set = new TreeSet<>(Comparator.comparing(o -> (o.getProductNo() + o.getProductRev())));
        set.addAll(boms);
        return new ArrayList<>(set);
    }

    private void setDateToItem(Bom bom) {
        if (bom != null) {
            objectItemGrid.setData(bomService.getBomsByNoRev(bom.getProductNo(), bom.getProductRev()));
        } else {
            objectItemGrid.setData(new ArrayList());
        }
        objectItemGrid.refresh();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnAdd.equals(button)) {
            addBomDialog.setObject(null);
            addBomDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    refreshGrid();
                }
            });
        } else if (btnEdit.equals(button)) {
            Bom bom = objectItemGrid.getSelectedObject();
            addBomDialog.setObject(bom);
            addBomDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    setDateToItem(bom);
                    // refreshGrid();
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                Bom bom = objectGrid.asSingleSelect().getValue();
                                if (bom != null) {
                                    bomService.deleteBomList(bom.getProductNo(), bom.getProductRev());
                                }
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            refreshGrid();
                            setDateToItem(objectGrid.asSingleSelect().getValue());
                        }
                    });
        } else if (btnRefresh.equals(button)) {
            tfNo.clear();
            tfRev.clear();
            refreshGrid();
        } else if (btnSearch.equals(button)) {
            refreshGrid();
        }
    }

    /**
     * 导入零件信息（.xlsx）
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public int importXlsx(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        Bom bomReturn = null;
        // *****.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        Boolean isSaveFlag = false;// 控制零件开始结束
        Boolean startEndFlag = false;// 控制零件开始结束
        Boolean phantomItemXFlag = false;
        Boolean RetrospectFlag = true;//追溯标识

        String productNo = "";
        String productRev = "";
        String itemCategory = "";
        String explosionLvl = "";
        String sortString = "";
        String itemNo = "";
        String partNo = "";
        int partQty = 0;
        String baseUnit = "";
        String partRev = "";
        String legacyRev = "";
        String partDes = "";
        String longText = "";
        String phantomItem = "";
        Boolean isRetrospect = false;
        String retrospectType = "";

        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell aaItemCategory = xssfRow.getCell(0);//Item category
                XSSFCell bbExplosionLvl = xssfRow.getCell(1);//Explosion level
                XSSFCell ccSortString = xssfRow.getCell(2);//Sort String
                XSSFCell ddItemNumber = xssfRow.getCell(3);//Item Number
                XSSFCell eeComponent = xssfRow.getCell(4);//Component number
                XSSFCell ffObjectDes = xssfRow.getCell(5);//Object Description
                XSSFCell ggRevisionLvl = xssfRow.getCell(6);//Revision Level
                XSSFCell hhLegacyRev = xssfRow.getCell(7);//Legacy rev
                XSSFCell iiCompQty = xssfRow.getCell(8);//Comp. Qty (BUn)
                XSSFCell jjBaseUnit = xssfRow.getCell(9);//Base Unit of Measure
                XSSFCell kkDocumentType = xssfRow.getCell(10);//Document Type
                XSSFCell llDocument = xssfRow.getCell(11);//Document
                XSSFCell mmDocumentVer = xssfRow.getCell(12);//Document version
                XSSFCell nnDescription = xssfRow.getCell(13);//Description
                XSSFCell ooLongText = xssfRow.getCell(14);//Long Text
                XSSFCell ppPhantomItem = xssfRow.getCell(15);//Phantom item

                //0 !null
                if ((bbExplosionLvl != null && "0".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    isSaveFlag = true;
                    startEndFlag = true;

                    productNo = eeComponent.getStringCellValue();
                    productRev = ggRevisionLvl.getStringCellValue();

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "0";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                    if (sparePart == null || sparePart.isRetrospect() == false || (!"A".equalsIgnoreCase(itemCategory) && !"L".equalsIgnoreCase(itemCategory))) {
                        RetrospectFlag = false;
                    } else {
                        RetrospectFlag = true;
                    }
                    if (RetrospectFlag) {
                        isRetrospect = sparePart.isRetrospect();
                        retrospectType = sparePart.getRetrospectType();
                    }
                    //更新产品订单中最新版本号用于bom管控
                    List<ProductionOrder> orderList = orderService.getByPartNoPartNoRev(productNo, "");
                    for (ProductionOrder order : orderList) {
                        order.setNewestBomVersion(productRev);
                        if (!productRev.equals(order.getProductVersionId())) {//新版本则锁定工单
                            order.setBomCheckFlag(true);
                            order.setBomLockFlag(true);
                        }
                        orderService.save(order);
                    }
                }

                if (aaItemCategory == null || "".equals(aaItemCategory.toString().trim())) {
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                }
                //.1 null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null || "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = "";
                    explosionLvl = "";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = kkDocumentType.getStringCellValue();
                    partNo = llDocument.getStringCellValue();
                    partQty = 0;
                    baseUnit = "";
                    partRev = mmDocumentVer.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = nnDescription.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    isRetrospect = false;
                    retrospectType = "";
                }
                //.1 !null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "1";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if ("x".equalsIgnoreCase(phantomItem)) {
                        phantomItemXFlag = true;
                        isRetrospect = false;
                        retrospectType = "";
                    } else {
                        phantomItemXFlag = false;
                        if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                            SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                            if (sparePart != null) {
                                isRetrospect = sparePart.isRetrospect();
                                retrospectType = sparePart.getRetrospectType();
                            } else {
                                isRetrospect = false;
                                retrospectType = "";
                            }
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    }
                }
                //..2 null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                        coutNum++;
                    }

                }
                //..2 !null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim())) && phantomItemXFlag) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "2";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                        SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                        if (sparePart != null) {
                            isRetrospect = sparePart.isRetrospect();
                            retrospectType = sparePart.getRetrospectType();
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    } else {
                        isRetrospect = false;
                        retrospectType = "";
                    }

                }
                //...3 null
                if ((bbExplosionLvl != null && "...3".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                    }
                }

                if (rowNum == xssfSheet.getLastRowNum()) {
                    System.out.println("$$$$$$$$$" + (rowNum + 1) + "$$$$$$$$$");
                    bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                            partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                    coutNum++;
                }
            }
        }
        refreshGrid();
        objectGrid.select(bomReturn);
        return coutNum;
    }

    public Bom saveDataToBom(String productNo, String productRev, String itemCategory, String explosionLvl,
                             String sortString, String itemNo, String partNo, int partQty, String baseUnit,
                             String partRev, String legacyRev, String partDes, String longText,
                             String phantomItem, boolean isRetrospect, String retrospectType) {
        Bom bomSaved = bomService.getByNoRevItemPartNoPartRev(productNo, productRev, itemNo, partNo, partRev);
        if (bomSaved == null) {
            bomSaved = new Bom();
            bomSaved.setProductNo(productNo);
            bomSaved.setProductRev(productRev);
            bomSaved.setItemNo(itemNo);
            bomSaved.setPartNo(partNo);
            bomSaved.setPartRev(partRev);
        }
        bomSaved.setItemCategory(itemCategory);
        bomSaved.setExplosionLevel(explosionLvl);
        bomSaved.setSortString(sortString);
        bomSaved.setBaseUnit(baseUnit);
        bomSaved.setLegacyRev(legacyRev);
        bomSaved.setPhantomItem(phantomItem);
        bomSaved.setPartDes(partDes);
        bomSaved.setLongText(longText);
        bomSaved.setPartQuantity(partQty);
        bomSaved.setRetrospect(isRetrospect);
        if (isRetrospect) {
            bomSaved.setRetrospectType(retrospectType);
        }
        bomService.save(bomSaved);
        return bomSaved;
    }

    /**
     * 导入零件信息（.xls）
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public int importXls(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        Bom bomReturn = null;
        // *****.xlsx
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        Boolean isSaveFlag = false;// 控制零件开始结束
        Boolean startEndFlag = false;// 控制零件开始结束
        Boolean phantomItemXFlag = false;
        Boolean RetrospectFlag = true;//追溯标识

        String productNo = "";
        String productRev = "";
        String itemCategory = "";
        String explosionLvl = "";
        String sortString = "";
        String itemNo = "";
        String partNo = "";
        int partQty = 0;
        String baseUnit = "";
        String partRev = "";
        String legacyRev = "";
        String partDes = "";
        String longText = "";
        String phantomItem = "";
        Boolean isRetrospect = false;
        String retrospectType = "";

        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                HSSFCell aaItemCategory = hssfRow.getCell(0);//Item category
                HSSFCell bbExplosionLvl = hssfRow.getCell(1);//Explosion level
                HSSFCell ccSortString = hssfRow.getCell(2);//Sort String
                HSSFCell ddItemNumber = hssfRow.getCell(3);//Item Number
                HSSFCell eeComponent = hssfRow.getCell(4);//Component number
                HSSFCell ffObjectDes = hssfRow.getCell(5);//Object Description
                HSSFCell ggRevisionLvl = hssfRow.getCell(6);//Revision Level
                HSSFCell hhLegacyRev = hssfRow.getCell(7);//Legacy rev
                HSSFCell iiCompQty = hssfRow.getCell(8);//Comp. Qty (BUn)
                HSSFCell jjBaseUnit = hssfRow.getCell(9);//Base Unit of Measure
                HSSFCell kkDocumentType = hssfRow.getCell(10);//Document Type
                HSSFCell llDocument = hssfRow.getCell(11);//Document
                HSSFCell mmDocumentVer = hssfRow.getCell(12);//Document version
                HSSFCell nnDescription = hssfRow.getCell(13);//Description
                HSSFCell ooLongText = hssfRow.getCell(14);//Long Text
                HSSFCell ppPhantomItem = hssfRow.getCell(15);//Phantom item

                //0 !null
                if ((bbExplosionLvl != null && "0".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    isSaveFlag = true;
                    startEndFlag = true;

                    productNo = eeComponent.getStringCellValue();
                    productRev = ggRevisionLvl.getStringCellValue();

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "0";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                    if (sparePart == null || sparePart.isRetrospect() == false || (!"A".equalsIgnoreCase(itemCategory) && !"L".equalsIgnoreCase(itemCategory))) {
                        RetrospectFlag = false;
                    } else {
                        RetrospectFlag = true;
                    }
                    if (RetrospectFlag) {
                        isRetrospect = sparePart.isRetrospect();
                        retrospectType = sparePart.getRetrospectType();
                    }
                    //更新产品订单中最新版本号用于bom管控
                    List<ProductionOrder> orderList = orderService.getByPartNoPartNoRev(productNo, "");
                    for (ProductionOrder order : orderList) {
                        order.setNewestBomVersion(productRev);
                        if (!productRev.equals(order.getProductVersionId())) {//新版本则锁定工单
                            order.setBomLockFlag(true);
                            order.setBomCheckFlag(true);
                        }
                        orderService.save(order);
                    }
                }

                if (aaItemCategory == null || "".equals(aaItemCategory.toString().trim())) {
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                }
                //.1 null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null || "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = "";
                    explosionLvl = "";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = kkDocumentType.getStringCellValue();
                    partNo = llDocument.getStringCellValue();
                    partQty = 0;
                    baseUnit = "";
                    partRev = mmDocumentVer.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = nnDescription.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    isRetrospect = false;
                    retrospectType = "";
                }
                //.1 !null
                if ((bbExplosionLvl != null && ".1".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "1";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if ("x".equalsIgnoreCase(phantomItem)) {
                        phantomItemXFlag = true;
                        isRetrospect = false;
                        retrospectType = "";
                    } else {
                        phantomItemXFlag = false;
                        if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                            SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                            if (sparePart != null) {
                                isRetrospect = sparePart.isRetrospect();
                                retrospectType = sparePart.getRetrospectType();
                            } else {
                                isRetrospect = false;
                                retrospectType = "";
                            }
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    }
                }
                //..2 null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                        coutNum++;
                    }

                }
                //..2 !null
                if ((bbExplosionLvl != null && "..2".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber != null && !"".equals(ddItemNumber.toString().trim())) && phantomItemXFlag) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        coutNum++;
                    }
                    isSaveFlag = true;
                    startEndFlag = true;

                    itemCategory = aaItemCategory.getStringCellValue();
                    explosionLvl = "2";
                    sortString = ccSortString.getStringCellValue();
                    itemNo = ddItemNumber.getStringCellValue();
                    partNo = eeComponent.getStringCellValue();
                    partQty = (int) iiCompQty.getNumericCellValue();
                    baseUnit = jjBaseUnit.getStringCellValue();
                    partRev = ggRevisionLvl.getStringCellValue();
                    legacyRev = hhLegacyRev.getStringCellValue();
                    partDes = ffObjectDes.getStringCellValue();
                    phantomItem = ppPhantomItem.getStringCellValue();
                    if (startEndFlag) {
                        longText += ooLongText.getStringCellValue();
                    }
                    if (RetrospectFlag && ("A".equalsIgnoreCase(itemCategory) || "L".equalsIgnoreCase(itemCategory))) {
                        SparePart sparePart = sparePartService.getByNoRev(partNo, partRev);
                        if (sparePart != null) {
                            isRetrospect = sparePart.isRetrospect();
                            retrospectType = sparePart.getRetrospectType();
                        } else {
                            isRetrospect = false;
                            retrospectType = "";
                        }
                    } else {
                        isRetrospect = false;
                        retrospectType = "";
                    }

                }
                //...3 null
                if ((bbExplosionLvl != null && "...3".equals(bbExplosionLvl.toString().trim()))
                        && (ddItemNumber == null && "".equals(ddItemNumber.toString().trim()))) {
                    if (isSaveFlag) {
                        bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                                partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                        longText = "";
                        isSaveFlag = false;
                        startEndFlag = false;
                    }
                }

                if (rowNum == hssfSheet.getLastRowNum()) {
                    System.out.println("$$$$$$$$$" + (rowNum + 1) + "$$$$$$$$$");
                    bomReturn = saveDataToBom(productNo, productRev, itemCategory, explosionLvl, sortString, itemNo,
                            partNo, partQty, baseUnit, partRev, legacyRev, partDes, longText, phantomItem, isRetrospect, retrospectType);
                    coutNum++;
                }
            }
        }
        refreshGrid();
        objectGrid.select(bomReturn);

        return coutNum;
    }

}
