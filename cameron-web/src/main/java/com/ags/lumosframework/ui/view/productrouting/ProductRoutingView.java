package com.ags.lumosframework.ui.view.productrouting;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
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
import org.apache.poi.ss.usermodel.CellType;
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

@Menu(caption = "ProductRouting", captionI18NKey = "ProductRouting.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 5)
@SpringView(name = "ProductRouting", ui = CameronUI.class)
@Secured("ProductRouting")
public class ProductRoutingView extends BaseView implements Button.ClickListener, IFilterableView {

    /**
     *
     */
    private static final long serialVersionUID = -4371403857347102599L;

    //	@Secured(PermissionConstants.POST_ADD)
    @I18Support(caption = "Add", captionKey = "common.add")
    private final Button btnAdd = new Button();
    //
//	@Secured(PermissionConstants.POST_EDIT)
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private final Button btnEdit = new Button();
    //
//	@Secured(PermissionConstants.POST_DELETE)
    @I18Support(caption = "Delete", captionKey = "common.delete")
    private final Button btnDelete = new Button();
    //
//	@Secured(PermissionConstants.POST_REFRESH)
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private final Button btnRefresh = new Button();
    //
    private final Button[] btns = new Button[]{btnRefresh};
//    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh};

    // 查询区域控件
//	@I18Support(caption = "ProductId", captionKey = "ProductRouting.ProductId")
    private final TextField tfProductId = new TextField();

    //	@I18Support(caption = "RoutingGroup", captionKey = "ProductRouting.RoutingGroup")
    private final TextField tfRoutingGroup = new TextField();

    //	@I18Support(caption = "InnerGroupNo", captionKey = "ProductRouting.InnerGroupNo")
    private final TextField tfInnerGroupNo = new TextField();

    //	@I18Support(caption = "Search", captionKey = "common.search")
    private final Button btnSearch = new Button();

//	AbstractComponent[] fields = { tfProductId,tfProductVersionId,btnSearch };

    private final HorizontalLayout hlToolBox = new HorizontalLayout();

    //	private IDomainObjectGrid<ProductRouting> objectGrid = new PaginationDomainObjectList<>();
    private final Grid<ProductRouting> objectGrid = new Grid<>();

    private final Grid<ProductRouting> objectGridItem = new Grid<>();

    @I18Support(caption = "Import Excel", captionKey = "common.import")
    private final UploadButton upload = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    @Autowired
    private IProductRoutingService productRoutingService;

    @Autowired
    private AddProductRoutingDialog addProductRoutingDialog;

    public ProductRoutingView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);

        //增删改查按钮
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        //右侧查询按钮
        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_LEFT);

        hlTemp.addComponents(tfProductId, tfRoutingGroup, tfInnerGroupNo, btnSearch);
        tfProductId.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductId", "ProductId"));
        tfRoutingGroup.setPlaceholder(I18NUtility.getValue("ProductRouting.RoutingGroup", "RoutingGroup"));
        tfInnerGroupNo.setPlaceholder(I18NUtility.getValue("ProductRouting.InnerGroupNo", "InnerGroupNo"));
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
                int num = 0;
                try {
                    if (uploadEvent.getFileName().endsWith(".xls") || uploadEvent.getFileName().endsWith(".XLS")) {
                        num = importXls(uploadEvent.getUploadFileInByte());
                    } else if (uploadEvent.getFileName().endsWith(".xlsx") || uploadEvent.getFileName().endsWith(".XLSX")) {
                        num = importXlsx(uploadEvent.getUploadFileInByte());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                    refreshGrid();
                    NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常：" + e.getMessage() + " ，请确认导入文件是否正确！");
                }
            }


        });
        hlTempToolBox.addComponent(upload);

        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(550.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        objectGrid.setSizeFull();
        objectGrid.addColumn(ProductRouting::getProductId).setCaption(I18NUtility.getValue("ProductRouting.ProductId", "ProductId")).setWidth(130.0);
        objectGrid.addColumn(ProductRouting::getRoutingGroup).setCaption(I18NUtility.getValue("ProductRouting.RoutingGroup", "RoutingGroup"));
        objectGrid.addColumn(ProductRouting::getInnerGroupNo).setCaption(I18NUtility.getValue("ProductRouting.InnerGroupNo", "InnerGroupNo"));
        objectGrid.addColumn(ProductRouting::getRoutingDesc).setCaption(I18NUtility.getValue("ProductRouting.RoutingDesc", "RoutingDesc"));
        objectGrid.addSelectionListener(event -> {
            Optional<ProductRouting> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                setDateToItem(optional.get());
            } else {
                setDateToItem(null);
            }
        });
        hlSplitPanel.setFirstComponent(objectGrid);

        objectGridItem.setSizeFull();
        objectGridItem.addColumn(ProductRouting::getOprationNo).setCaption(I18NUtility.getValue("ProductRouting.OprationNo", "OprationNo"));
        objectGridItem.addColumn(ProductRouting::getOprationDesc).setCaption(I18NUtility.getValue("ProductRouting.OprationDesc", "OprationDesc"));
        objectGridItem.addColumn(ProductRouting::getAttention).setCaption(I18NUtility.getValue("ProductRouting.Attention", "Attention"));
        objectGridItem.addSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });

        hlSplitPanel.setSecondComponent(objectGridItem);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<ProductRouting> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    private void setDateToItem(ProductRouting productRouting) {
        if (productRouting != null) {
            objectGridItem.setItems(productRoutingService.getByGroupNoAndInnerNo(productRouting.getRoutingGroup(), productRouting.getInnerGroupNo()));
        } else {
            objectGridItem.setItems(new ArrayList());
        }
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
        List<ProductRouting> productRoutingList = productRoutingService.getProductRoutingsByIdGroupNo(tfProductId.getValue(), tfRoutingGroup.getValue(), tfInnerGroupNo.getValue(), "");
        objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicateBom(productRoutingList)));
    }

    //去重（no+rev）
    private ArrayList<ProductRouting> removeDuplicateBom(List<ProductRouting> users) {
        Set<ProductRouting> set = new TreeSet<ProductRouting>(new Comparator<ProductRouting>() {
            @Override
            public int compare(ProductRouting o1, ProductRouting o2) {
                return (o1.getProductId() + o1.getRoutingGroup() + o1.getInnerGroupNo()).compareTo(o2.getProductId() + o2.getRoutingGroup() + o2.getInnerGroupNo());
            }
        });
        set.addAll(users);
        return new ArrayList<ProductRouting>(set);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnAdd.equals(button)) {
            addProductRoutingDialog.setObject(null);
            addProductRoutingDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    refreshGrid();
                }
            });
        } else if (btnEdit.equals(button)) {
            ProductRouting productRouting = objectGridItem.asSingleSelect().getValue();
            addProductRoutingDialog.setObject(productRouting);
            addProductRoutingDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    setDateToItem(productRouting);
                    refreshGrid();
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                productRoutingService.delete(objectGridItem.asSingleSelect().getValue());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            setDateToItem(objectGrid.asSingleSelect().getValue());
                            refreshGrid();
                        }
                    });
        } else if (btnRefresh.equals(button)) {
            tfProductId.clear();
            tfRoutingGroup.clear();
            tfInnerGroupNo.clear();
            refreshGrid();
        } else if (btnSearch.equals(button)) {
            refreshGrid();
        }
    }

    /**
     * 导入routing信息（.xls）
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public int importXls(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        List<ProductRouting> prList = new ArrayList<ProductRouting>();
        //*****.xls
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            ProductRouting productRoutingCheck = new ProductRouting();

            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                HSSFCell productIdCell = hssfRow.getCell(0);
                HSSFCell productDescCell = hssfRow.getCell(1);
                HSSFCell routingGroupCell = hssfRow.getCell(2);
                HSSFCell innerGroupNoCell = hssfRow.getCell(3);
                HSSFCell routingDescCell = hssfRow.getCell(4);
                HSSFCell operationNoCell = hssfRow.getCell(5);
                HSSFCell operationDescCell = hssfRow.getCell(6);
                HSSFCell attentionCell = hssfRow.getCell(7);

                routingGroupCell.setCellType(CellType.STRING);
                innerGroupNoCell.setCellType(CellType.STRING);
                operationNoCell.setCellType(CellType.STRING);
                String productId = productIdCell != null ? productIdCell.getStringCellValue() : "";
                String productDesc = productDescCell != null ? productDescCell.getStringCellValue() : "";
                String routingGroup = routingGroupCell != null ? routingGroupCell.getStringCellValue() : "";
                String innerGroupNo = innerGroupNoCell != null ? innerGroupNoCell.getStringCellValue() : "";
                //此列用作排序
                int innerGroupNoInt = "".equals(innerGroupNo) ? 0 : Integer.parseInt(innerGroupNo);
                String routingDesc = routingDescCell != null ? routingDescCell.getStringCellValue() : "";
                String operationNo = operationNoCell != null ? operationNoCell.getStringCellValue() : "";
                String operationDesc = operationDescCell != null ? operationDescCell.getStringCellValue() : "";
                String attention = attentionCell != null ? attentionCell.getStringCellValue() : "";

                productRoutingCheck.setRoutingGroup(routingGroup);
                productRoutingCheck.setInnerGroupNo(innerGroupNo);

                if (checkIsAdded(prList, productRoutingCheck)) {//已添加  routingGroup，innerGroupNo到 prList 说明以前的数据已经删除
                    //直接新建插入数据
                    saveDataToProductRouting(productId, productDesc, routingGroup, innerGroupNo, routingDesc,
                            operationNo, operationDesc, attention, innerGroupNoInt);
                } else {
                    //未添加  routingGroup，innerGroupNo到 prList 说明以前的数据要进行检查  检查完添加到prList
                    List<ProductRouting> prHavedList = productRoutingService.getByGroupNoAndInnerNo(routingGroup, innerGroupNo);
                    //若有保存    删除已保存的数据
                    deleteProductRouting(prHavedList);
                    //保存新数据
                    saveDataToProductRouting(productId, productDesc, routingGroup, innerGroupNo, routingDesc,
                            operationNo, operationDesc, attention, innerGroupNoInt);
                    //添加routingGroup，innerGroupNo到 prList
                    prList.add(productRoutingCheck);
                }
                coutNum++;
                System.out.println("------" + (rowNum + 1) + "--------");
            }
        }
        hssfWorkbook.close();
        return coutNum;
    }

    /**
     * 导入routing信息（.xlsx）
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public int importXlsx(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        List<ProductRouting> prList = new ArrayList<ProductRouting>();
        //.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            ProductRouting productRoutingCheck = new ProductRouting();

            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {

                XSSFCell productIdCell = xssfRow.getCell(0);
                XSSFCell productDescCell = xssfRow.getCell(1);
                XSSFCell routingGroupCell = xssfRow.getCell(2);
                XSSFCell innerGroupNoCell = xssfRow.getCell(3);
                XSSFCell routingDescCell = xssfRow.getCell(4);
                XSSFCell operationNoCell = xssfRow.getCell(5);
                XSSFCell operationDescCell = xssfRow.getCell(6);
                XSSFCell attentionCell = xssfRow.getCell(7);

                routingGroupCell.setCellType(CellType.STRING);
                innerGroupNoCell.setCellType(CellType.STRING);
                operationNoCell.setCellType(CellType.STRING);
                String productId = productIdCell != null ? productIdCell.getStringCellValue() : "";
                String productDesc = productDescCell != null ? productDescCell.getStringCellValue() : "";
                String routingGroup = routingGroupCell != null ? routingGroupCell.getStringCellValue() : "";
                String innerGroupNo = innerGroupNoCell != null ? innerGroupNoCell.getStringCellValue() : "";
                //此列用作排序
                int innerGroupNoInt = "".equals(innerGroupNo) ? 0 : Integer.parseInt(innerGroupNo);
                String routingDesc = routingDescCell != null ? routingDescCell.getStringCellValue() : "";
                String operationNo = operationNoCell != null ? operationNoCell.getStringCellValue() : "";
                String operationDesc = operationDescCell != null ? operationDescCell.getStringCellValue() : "";
                String attention = attentionCell != null ? attentionCell.getStringCellValue() : "";

                productRoutingCheck.setRoutingGroup(routingGroup);
                productRoutingCheck.setInnerGroupNo(innerGroupNo);

                if (checkIsAdded(prList, productRoutingCheck)) {//已添加  routingGroup，innerGroupNo到 prList 说明以前的数据已经删除
                    //直接新建插入数据
                    saveDataToProductRouting(productId, productDesc, routingGroup, innerGroupNo, routingDesc,
                            operationNo, operationDesc, attention, innerGroupNoInt);
                } else {
                    //未添加  routingGroup，innerGroupNo到 prList 说明以前的数据要进行检查  检查完添加到prList
                    List<ProductRouting> prHavedList = productRoutingService.getByGroupNoAndInnerNo(routingGroup, innerGroupNo);
                    //若有保存    删除已保存的数据
                    deleteProductRouting(prHavedList);
                    //保存新数据
                    saveDataToProductRouting(productId, productDesc, routingGroup, innerGroupNo, routingDesc,
                            operationNo, operationDesc, attention, innerGroupNoInt);
                    //添加routingGroup，innerGroupNo到 prList
                    prList.add(productRoutingCheck);
                }
                coutNum++;
            }
        }
        xssfWorkbook.close();
        return coutNum;
    }

    public boolean checkIsAdded(List<ProductRouting> prList, ProductRouting productRoutingCheck) {
        boolean flag = false;
        String routingGroup = productRoutingCheck.getRoutingGroup();
        String innerGroupNo = productRoutingCheck.getInnerGroupNo();
        for (ProductRouting productRouting : prList) {
            String routingGroupAdded = productRouting.getRoutingGroup();
            String innerGroupNoAdded = productRouting.getInnerGroupNo();
            if (!Strings.isNullOrEmpty(routingGroup) && !Strings.isNullOrEmpty(innerGroupNo)
                    && routingGroup.equals(routingGroupAdded) && innerGroupNo.equals(innerGroupNoAdded)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void deleteProductRouting(List<ProductRouting> prHavedList) {
        if (prHavedList != null && prHavedList.size() > 0) {
            for (ProductRouting productRouting : prHavedList) {
                productRoutingService.delete(productRouting);
            }
        }
    }

    public void saveDataToProductRouting(String productId, String productDesc, String routingGroup,
                                         String innerGroupNo, String routingDesc,
                                         String oprationNo, String oprationDesc, String attention, int innerGroupNoInt) {
        ProductRouting productRouting = new ProductRouting();
        productRouting.setRoutingGroup(routingGroup);
        productRouting.setInnerGroupNo(innerGroupNo);
        productRouting.setProductId(productId);
        productRouting.setProductDesc(productDesc);
        productRouting.setRoutingDesc(routingDesc);
        productRouting.setOprationNo(oprationNo);
        productRouting.setOprationDesc(oprationDesc);
        productRouting.setAttention(attention);
        productRouting.setInnerGroupNoInt(innerGroupNoInt);
        productRouting.setCheckStatus("waitCheck");
        productRoutingService.save(productRouting);
    }


    @Override
    public void updateAfterFilterApply() {

    }
}
