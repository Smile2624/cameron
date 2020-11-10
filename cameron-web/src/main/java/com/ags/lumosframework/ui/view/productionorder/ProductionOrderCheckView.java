package com.ags.lumosframework.ui.view.productionorder;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectClickListener;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

//@Menu(caption = "ProductionOrderCheck", captionI18NKey = "ProductionOrderCheck.view.caption", iconPath = "images/icon/text-blob.png", groupName = "CommonFunction", order = 4)
//@SpringView(name = "ProductionOrderCheck", ui = CameronUI.class)
public class ProductionOrderCheckView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 1L;

    @I18Support(caption = "Upload PDF Attachment", captionKey = "common.uploadPdf")
    private UploadButton btnUploadPdf = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    @I18Support(caption = "View PDF Attachment", captionKey = "common.viewPdf")
    private Button btnViewPdf = new Button();

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private ProductionOrder productionOrder;

    private IDomainObjectGrid<ProductionOrder> objectGrid = new PaginationDomainObjectList<>();

    private DateField dfDate = new DateField();
    private ComboBox<String> cbWorkshop = new ComboBox<>();

    @Autowired
    private SelectWoPdfDialog selectWoPdfDialog;

    private TextField tfPO = new TextField();

    public ProductionOrderCheckView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        hlToolBox.addComponent(tfPO);
        hlToolBox.setComponentAlignment(tfPO, Alignment.MIDDLE_RIGHT);
        hlTempToolBox.addComponent(cbWorkshop);
        hlTempToolBox.addComponent(dfDate);
        hlTempToolBox.addComponent(btnUploadPdf);
        hlTempToolBox.addComponent(btnViewPdf);
        btnViewPdf.addClickListener(this);
        cbWorkshop.setItems("#4", "#5", "#6");
        cbWorkshop.setPlaceholder("选择查看车间");
        cbWorkshop.addValueChangeListener(event -> {
            refreshAll();
        });
        dfDate.setDateFormat("yyyy-MM-dd");
        dfDate.addValueChangeListener(event -> {
            refreshAll();
        });
        dfDate.setPlaceholder("选择日期");
        tfPO.addValueChangeListener(event -> {
            refreshAll();
        });
        tfPO.setValueChangeTimeout(100);
        tfPO.setPlaceholder("工单号");
        FileUploader fileUploader = new FileUploader(event -> uploadEvent = event);
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


        objectGrid.addColumn(ProductionOrder::getProductOrderId)
                .setCaption(I18NUtility.getValue("ProductionOrder.productOrderId", "productOrderId"));
        objectGrid.addColumn(ProductionOrder::getProductId)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductId", "ProductId"));
        objectGrid.addColumn(ProductionOrder::getProductVersionId)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductVersionId", "ProductVersionId"));
        objectGrid.addColumn(ProductionOrder::getProductNumber)
                .setCaption(I18NUtility.getValue("ProductionOrder.productNumber", "productQty"));
        objectGrid.addColumn(ProductionOrder::getProductDesc)
                .setCaption(I18NUtility.getValue("ProductionOrder.ProductDesc", "ProductDesc"));
        objectGrid.addColumn(ProductionOrder::getDescription)
                .setCaption(I18NUtility.getValue("ProductionOrder.scheduledate", "Schedule Date"));
        objectGrid.addColumn(ProductionOrder::getComments).setCaption(I18NUtility.getValue("ProductionOrder.comments", "Comments"));
        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
            productionOrder = objectGrid.getSelectedObject();
        });
        objectGrid.setObjectClickListener(new IObjectClickListener<ProductionOrder>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void itemClicked(Grid.ItemClick<ProductionOrder> event) {
                ProductionOrder order = event.getItem();
                if (event.getMouseEventDetails().isDoubleClick()) {
                    String rootUrl = VaadinUtils.getRootURL();
                    String pdfUrl = rootUrl.replace("cameron", "CameronPDFView");
                    VaadinUtils.setPageLocation(pdfUrl + "viewpdf?filename=D:\\Drawings\\SK-DWG\\" + order.getProductId() + ".pdf", true);
//                    VaadinUtils.setPageLocation(rootUrl + "Cameron#!Bom/" + order.getProductId() + "/" + order.getProductVersionId());
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
        btnViewPdf.setEnabled(enable);
        btnUploadPdf.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(IProductionOrderService.class);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
//        dfDate.setValue(LocalDate.now());
//        cbWorkshop.clear();
        refreshAll();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnViewPdf.equals(button)) {
            selectWoPdfDialog.setObject(productionOrder);
            selectWoPdfDialog.show(getUI(), result -> {
            });
        }
    }

    @Override
    public void updateAfterFilterApply() {
    }

    public void uploadPDF(InputStream fileStream, String fileName) throws IOException {
        File targetFile = new File("D:\\Attachment\\" + productionOrder.getProductOrderId() + "\\" + fileName);
        FileUtils.copyInputStreamToFile(fileStream, targetFile);
    }

    public void refreshAll() {
        IProductionOrderService productionOrderService = BeanManager.getService(IProductionOrderService.class);
        EntityFilter createFilter = productionOrderService.createFilter();
        if (!dfDate.isEmpty()) {
            Integer year = dfDate.getValue().getYear();
            Integer month = dfDate.getValue().getMonthValue();
            String smonth;
            String sday;
            if (month < 10) {
                smonth = "0" + month.toString();
            } else {
                smonth = month.toString();
            }
            Integer day = dfDate.getValue().getDayOfMonth();
            if (day < 10) {
                sday = "0" + day.toString();
            } else {
                sday = day.toString();
            }
            String date = year.toString() + "-" + smonth + "-" + sday;
            createFilter.fieldEqualTo(ProductionOrderEntity.DESCRIPTION, date);
        }
        if (!cbWorkshop.isEmpty() && !cbWorkshop.getValue().equals("")) {
            createFilter.fieldEqualTo(ProductionOrderEntity.WORKSHOP, cbWorkshop.getValue());
        }
        if (!tfPO.isEmpty() && !tfPO.getValue().equals("")) {
            createFilter.fieldContains(ProductionOrderEntity.PRODUCT_ORDER_ID, tfPO.getValue());
        }
        createFilter.orderBy(ProductionOrderEntity.DESCRIPTION, true);
        objectGrid.setFilter(createFilter);
        objectGrid.setStartPage(1, 0);
        objectGrid.refresh();
    }
}
