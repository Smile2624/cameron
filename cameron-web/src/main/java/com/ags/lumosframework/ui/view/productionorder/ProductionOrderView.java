package com.ags.lumosframework.ui.view.productionorder;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.pojo.FinalInspectionItems;
import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectClickListener;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
import com.google.common.base.Strings;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.ItemClick;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Menu(caption = "ProductionOrder", captionI18NKey = "ProductionOrder.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 6)
@SpringView(name = "ProductionOrder", ui = CameronUI.class)
@Secured("ProductionOrder")
public class ProductionOrderView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 4997643622270022526L;

    // @Secured(PermissionConstants.POST_ADD)
    @I18Support(caption = "Add", captionKey = "common.add")
    private final Button btnAdd = new Button();
    //
    // @Secured(PermissionConstants.POST_EDIT)
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private final Button btnEdit = new Button();
    //
    // @Secured(PermissionConstants.POST_DELETE)
    @I18Support(caption = "Delete", captionKey = "common.delete")
    private final Button btnDelete = new Button();
    //
    // @Secured(PermissionConstants.POST_REFRESH)
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private final Button btnRefresh = new Button();

    @I18Support(caption = "Import Excel", captionKey = "common.import") // ProductionOrder.ImportExcel
    private final UploadButton upload = new UploadButton();
    //
    private final Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh};

    // 查询区域控件
    @I18Support(caption = "productOrderId", captionKey = "ProductionOrder.productOrderId")
    private final TextField tfproductOrderId = new TextField();

    @I18Support(caption = "ProductId", captionKey = "ProductionOrder.ProductId")
    private final TextField tfProductId = new TextField();

    @I18Support(caption = "Search", captionKey = "common.search")
    private final Button btnSearch = new Button();
    private final HorizontalLayout hlToolBox = new HorizontalLayout();
    private final Boolean isCovered = false;
    private final IDomainObjectGrid<ProductionOrder> objectGrid = new PaginationDomainObjectList<>();
    AbstractComponent[] fields = {tfproductOrderId, tfProductId, btnSearch};
    private UploadFinishEvent uploadEvent = null;
    private ProductionOrder productionOrder;
    @Autowired
    private IProductionOrderService productionOrderService;

    @Autowired
    private AddProductionOrderDialog addProductionOrderDialog;

    public ProductionOrderView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        SearchPanelBuilder sp = new SearchPanelBuilder(BeanManager.getService(ProductionOrderConditions.class),
                objectGrid, this);
        hlToolBox.addComponent(sp);
        hlToolBox.setComponentAlignment(sp, Alignment.MIDDLE_RIGHT);
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

        // upload.addStyleName("upload-small");
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
                    objectGrid.refresh();
                    NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认数据模板是否正确");
                }

            }
        });
        hlTempToolBox.addComponent(upload);
        objectGrid.addColumn(ProductionOrder::getProductOrderId)
                .setCaption(I18NUtility.getValue("ProductionOrder.productOrderId", "productOrderId"));
        objectGrid.addColumn(ProductionOrder::getProductId)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductId", "ProductId"));
        objectGrid.addColumn(ProductionOrder::getProductVersionId)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductVersionId", "ProductVersionId"));

        objectGrid.addComponentColumn(new ValueProvider<ProductionOrder, HorizontalLayout>() {

            @Override
            public HorizontalLayout apply(ProductionOrder productionOrder) {
                HorizontalLayout layout = new HorizontalLayout();
                TextField textField = new TextField();
                Button btnConfirmOK = new Button();
                Button btnConfirmNG = new Button();

                textField.setReadOnly(true);
                textField.setWidth("50px");
                textField.setValue(Strings.isNullOrEmpty(productionOrder.getNewestBomVersion())?"":productionOrder.getNewestBomVersion());
                if (Strings.isNullOrEmpty(textField.getValue())) {
                    textField.addStyleName(CoreTheme.BACKGROUND_WHITE);
                    btnConfirmOK.setEnabled(false);
                    btnConfirmNG.setEnabled(false);
                } else if (productionOrder.getProductVersionId().equals(textField.getValue())) {
                    textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                    btnConfirmOK.setEnabled(false);
                    btnConfirmNG.setEnabled(false);
                } else {
                    textField.addStyleName(CoreTheme.BACKGROUND_RED);
                    btnConfirmOK.setEnabled(true);
                    btnConfirmNG.setEnabled(true);
                }
                btnConfirmOK.setIcon(VaadinIcons.CHECK);
//                btnConfirmOK.setCaption(I18NUtility.getValue("ProductionOrder.BomVersionCheck", "Check"));
                btnConfirmOK.setCaption("new");
                btnConfirmOK.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        productionOrder.setProductVersionId(textField.getValue().trim());
                        productionOrder.setBomCheckFlag(false);
                        productionOrder.setBomLockFlag(false);
                        productionOrderService.save(productionOrder);
                        objectGrid.refresh();
                    }
                });
                btnConfirmNG.setIcon(VaadinIcons.CLOSE);
                btnConfirmNG.setCaption("old");
                btnConfirmNG.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        productionOrder.setBomLockFlag(false);
                        productionOrderService.save(productionOrder);
                        objectGrid.refresh();
                    }
                });

                layout.addComponents(textField, btnConfirmOK,btnConfirmNG);
                return layout;
            }
        }).setCaption(I18NUtility.getValue("ProductionOrder.NewestBomVersion", "NewestBomVersion(Check)"));


        objectGrid.addColumn(ProductionOrder::getProductNumber)
                .setCaption(I18NUtility.getValue("ProductionOrder.productNumber", "productQty"));
//		objectGrid.addColumn(ProductionOrder::getRoutingGroup)
//				.setCaption(I18NUtility.getValue("ProductionOrder.routingGroup", "RoutingGroup"));
//		objectGrid.addColumn(ProductionOrder::getInnerGroupNo)
//				.setCaption(I18NUtility.getValue("ProductionOrder.innerGroupNo", "InnerGroupNo"));
        objectGrid.addColumn(ProductionOrder::getProductDesc)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductDesc", "ProductDesc"));
        objectGrid.addColumn(ProductionOrder::getDescription)
                .setCaption(I18NUtility.getValue("ProductionOrder.scheduledate", "Schedule Date"));
//		objectGrid.addColumn(ProductionOrder::getCustomerCode).setCaption(I18NUtility.getValue("ProductionOrder.customerCode", "CustomerCode"));
//		objectGrid.addColumn(ProductionOrder::getSalesOrder).setCaption(I18NUtility.getValue("ProductionOrder.salesOrder", "SalesOrder"));
//		objectGrid.addColumn(ProductionOrder::getSalesOrderItem).setCaption(I18NUtility.getValue("ProductionOrder.salesOrderItem", "SalesOrderItem"));
//		objectGrid.addColumn(ProductionOrder::getPaintSpecification).setCaption(I18NUtility.getValue("ProductionOrder.paintSpecification", "PaintSpecification"));
        objectGrid.addColumn(ProductionOrder::getComments).setCaption(I18NUtility.getValue("ProductionOrder.comments", "Comments"));
        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });
        objectGrid.setObjectClickListener(new IObjectClickListener<ProductionOrder>() {

            private static final long serialVersionUID = 725795934767388550L;

            @Override
            public void itemClicked(ItemClick<ProductionOrder> event) {
                ProductionOrder order = event.getItem();
                if (event.getMouseEventDetails().isDoubleClick()) {
                    VaadinUtils.setPageLocation("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\Drawings\\SK-DWG\\" + order.getProductId() + ".pdf", true);
                }
            }
        });
        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<ProductionOrder> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(IProductionOrderService.class);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        IProductionOrderService productionOrderService = BeanManager.getService(IProductionOrderService.class);
        EntityFilter createFilter = productionOrderService.createFilter();
        createFilter.orderBy(ProductionOrderEntity.PRODUCT_ORDER_ID, false);
        objectGrid.setFilter(createFilter);
        objectGrid.refresh();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnAdd.equals(button)) {
            OrderHistory orderHistory1 = new OrderHistory();
            addProductionOrderDialog.setObject(null);
            addProductionOrderDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    objectGrid.refresh();
                }
            });
        } else if (btnEdit.equals(button)) {
            ProductionOrder productionOrder = objectGrid.getSelectedObject();
            addProductionOrderDialog.setObject(productionOrder);
            addProductionOrderDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    ProductionOrder temp = (ProductionOrder) result.getObj();
                    objectGrid.refresh(temp);
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                productionOrderService.delete(objectGrid.getSelectedObject());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            objectGrid.refresh();
                        }
                    });
        } else if (btnRefresh.equals(button)) {
            objectGrid.refresh();
        }
    }

    public int importsxlsx(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String scheduleDate = "";
        // .xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell productOrderId = xssfRow.getCell(0);
                XSSFCell productId = xssfRow.getCell(1);
                XSSFCell productVersionId = xssfRow.getCell(2);
                XSSFCell productDesc = xssfRow.getCell(3);
                XSSFCell productNumber = xssfRow.getCell(4);
                XSSFCell routingGroup = xssfRow.getCell(6);
                XSSFCell routingGroupCNT = xssfRow.getCell(7);

                XSSFCell scheduleData = xssfRow.getCell(8);
                XSSFCell customerCode = xssfRow.getCell(9);
                XSSFCell salesOrder = xssfRow.getCell(10);
                XSSFCell salesOrderItem = xssfRow.getCell(11);
                XSSFCell paintSpecification = xssfRow.getCell(12);
                XSSFCell comments = xssfRow.getCell(13);
                productOrderId.setCellType(CellType.STRING);
                productId.setCellType(CellType.STRING);
                productVersionId.setCellType(CellType.STRING);
                productDesc.setCellType(CellType.STRING);
                productNumber.setCellType(CellType.NUMERIC);
                routingGroup.setCellType(CellType.STRING);
                routingGroupCNT.setCellType(CellType.STRING);

                String strsScheduleData = getCellValue(scheduleData);
                String strCustomerCode = getCellValue(customerCode);
                String strSalesOrder = getCellValue(salesOrder);
                String strSalesOrderItem = getCellValue(salesOrderItem);
                String strPaintSpecification = getCellValue(paintSpecification);
                String strComments = getCellValue(comments);
                if (!Strings.isNullOrEmpty(strsScheduleData)) {
                    strsScheduleData = DoubleToDate(Double.parseDouble(strsScheduleData));
                }

//				scheduleData.setCellType(CellType.FORMULA);
//				customerCode.setCellType(CellType.FORMULA);
//				salesOrder.setCellType(CellType.FORMULA);
//				salesOrderItem.setCellType(CellType.FORMULA);
//				paintSpecificatin.setCellType(CellType.FORMULA);
//				comments.setCellType(CellType.FORMULA);
//				if(DateUtil.isCellDateFormatted(scheduleData)){
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//					scheduleDate = sdf.format(scheduleData.getDateCellValue());
//				}
                productionOrder = productionOrderService
                        .getByNo(productOrderId != null ? productOrderId.getStringCellValue() : "");
                if (productionOrder == null) {
                    productionOrder = new ProductionOrder();
                    productionOrder.setNewestBomVersion(productVersionId != null ? productVersionId.getStringCellValue() : "");
                    coutNum++;
                }
                productionOrder.setProductOrderId(productOrderId != null ? productOrderId.getStringCellValue() : "");
                productionOrder.setProductId(productId != null ? productId.getStringCellValue() : "");
                productionOrder.setProductVersionId(productVersionId != null ? productVersionId.getStringCellValue() : "");
                productionOrder.setProductDesc(productDesc != null ? productDesc.getStringCellValue() : "");
                productionOrder.setProductNumber(productNumber != null ? (int) productNumber.getNumericCellValue() : 0);// Integer.parseInt()
                productionOrder.setRoutingGroup(routingGroup.getStringCellValue() != null ? routingGroup.getStringCellValue() : "");
                productionOrder.setInnerGroupNo(routingGroupCNT.getStringCellValue() != null ? routingGroupCNT.getStringCellValue() : "");
                //
                productionOrder.setDescription(strsScheduleData);
                productionOrder.setCustomerCode(strCustomerCode);
                productionOrder.setSalesOrder(strSalesOrder);
                productionOrder.setSalesOrderItem(strSalesOrderItem);
                productionOrder.setPaintSpecification(strPaintSpecification);
                productionOrder.setComments(strComments);
                productionOrderService.save(productionOrder);
            }
        }
        xssfWorkbook.close();
        return coutNum;
    }


    public String DoubleToDate(double dVal) {
        java.util.Date oDate = new Date();
        @SuppressWarnings("deprecation")
        long localOffset = oDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        oDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        return myFmt.format(oDate);
    }

    public String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType type = cell.getCellType();
        String result;
        if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                short format = cell.getCellStyle().getDataFormat();
                SimpleDateFormat sdf = null;
                if (format == 14 || format == 31 || format == 57 || format == 58
                        || format == 176 || format == 177) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (format == 20 || format == 21 || format == 32) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                }
                Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                result = sdf.format(date);
            } else {
                result = String.valueOf(cell.getNumericCellValue());
            }
        } else if (type == CellType.STRING) {
            result = cell.getStringCellValue();
        } else if (type == CellType.BLANK) {
            result = "";
        } else if (type == CellType.FORMULA) {
            try {
                result = String.valueOf(cell.getNumericCellValue());
            } catch (IllegalStateException e) {
                result = String.valueOf(cell.getRichStringCellValue());
            }
        } else {
            result = cell.getCellComment().getString().toString();
        }
        return result;
    }

    public int importsxls(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        String scheduleDate = "";
        // *****.xls
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {

                HSSFCell productOrderId = hssfRow.getCell(0);
                HSSFCell productId = hssfRow.getCell(1);
                HSSFCell productVersionId = hssfRow.getCell(2);
                HSSFCell productDesc = hssfRow.getCell(3);
                HSSFCell productNumber = hssfRow.getCell(4);
                HSSFCell routingGroup = hssfRow.getCell(6);
                HSSFCell routingGroupCNT = hssfRow.getCell(7);
                HSSFCell scheduleData = hssfRow.getCell(8);
                HSSFCell customerCode = hssfRow.getCell(9);
                HSSFCell salesOrder = hssfRow.getCell(10);
                HSSFCell salesOrderItem = hssfRow.getCell(11);
                HSSFCell paintSpecification = hssfRow.getCell(12);
                HSSFCell comments = hssfRow.getCell(13);
                productOrderId.setCellType(CellType.STRING);
                productId.setCellType(CellType.STRING);
                productVersionId.setCellType(CellType.STRING);
                productDesc.setCellType(CellType.STRING);
                productNumber.setCellType(CellType.NUMERIC);
                routingGroup.setCellType(CellType.STRING);
                routingGroupCNT.setCellType(CellType.STRING);
                customerCode.setCellType(CellType.STRING);
                salesOrder.setCellType(CellType.STRING);
                salesOrderItem.setCellType(CellType.STRING);
                paintSpecification.setCellType(CellType.STRING);
                comments.setCellType(CellType.STRING);
                if (DateUtil.isCellDateFormatted(scheduleData)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    scheduleDate = sdf.format(scheduleData.getDateCellValue());
                }
                productionOrder = productionOrderService
                        .getByNo(productOrderId != null ? productOrderId.getStringCellValue() : "");
                if (productionOrder == null) {
                    productionOrder = new ProductionOrder();
                    coutNum++;
                }
                productionOrder.setProductOrderId(productOrderId != null ? productOrderId.getStringCellValue() : "");
                productionOrder.setProductId(productId != null ? productId.getStringCellValue() : "");
                productionOrder
                        .setProductVersionId(productVersionId != null ? productVersionId.getStringCellValue() : "");
                productionOrder.setProductDesc(productDesc != null ? productDesc.getStringCellValue() : "");
                productionOrder.setProductNumber(productNumber != null ? (int) productNumber.getNumericCellValue() : 0);// Integer.parseInt()
                productionOrder.setRoutingGroup(
                        routingGroup.getStringCellValue() != null ? routingGroup.getStringCellValue() : "");
                productionOrder.setInnerGroupNo(
                        routingGroupCNT.getStringCellValue() != null ? routingGroupCNT.getStringCellValue() : "");
                productionOrder.setDescription(scheduleDate);
                productionOrder.setCustomerCode(customerCode.getStringCellValue() != null ? customerCode.getStringCellValue() : "");
                productionOrder.setSalesOrder(salesOrder.getStringCellValue() != null ? salesOrder.getStringCellValue() : "");
                productionOrder.setSalesOrderItem(salesOrderItem.getStringCellValue() != null ? salesOrderItem.getStringCellValue() : "");
                productionOrder.setPaintSpecification(paintSpecification.getStringCellValue() != null ? paintSpecification.getStringCellValue() : "");
                productionOrder.setComments(comments.getStringCellValue() != null ? comments.getStringCellValue() : "");
                productionOrderService.save(productionOrder);
            }
        }
        hssfWorkbook.close();
        return coutNum;
    }

    @Override
    public void updateAfterFilterApply() {

    }

}
