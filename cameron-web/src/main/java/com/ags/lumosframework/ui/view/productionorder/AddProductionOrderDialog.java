package com.ags.lumosframework.ui.view.productionorder;

import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;

@SpringComponent
@Scope("prototype")
public class AddProductionOrderDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = -5638945860642660844L;

    @I18Support(caption = "ProductOrderId", captionKey = "ProductionOrder.productOrderId")
    private TextField tfProductOrderId = new TextField();

    @I18Support(caption = "ProductId", captionKey = "ProductInformation.ProductId")
    private TextField tfProductId = new TextField();

    @I18Support(caption = "ProductVersionId", captionKey = "ProductInformation.ProductVersionId")
    private TextField tfProductVerId = new TextField();

    @I18Support(caption = "ProductDesc", captionKey = "ProductionOrder.ProductDesc")
    private TextField tfProductDesc = new TextField();

    @I18Support(caption = "ProductNumber", captionKey = "ProductionOrder.productNumber")
    private TextField tfProductNumber = new TextField();

    @I18Support(caption = "RoutingGroup", captionKey = "ProductionOrder.routingGroup")
    private TextField tfRoutingGroup = new TextField();

    @I18Support(caption = "InnerGroupNo", captionKey = "ProductionOrder.innerGroupNo")
    private TextField tfInnerGroupNo = new TextField();

    @I18Support(caption = "Schedule Date", captionKey = "ProductionOrder.scheduleDate")
    //private TextField tfScheduleDate = new TextField();
    private DateField tfScheduleDate = new DateField();

    @I18Support(caption = "Comments", captionKey = "ProductionOrder.comments")
    private TextArea taComments = new TextArea();

    //ADD 客户 销售订单 销售订单项目号喷漆标准

    @I18Support(caption = "Customer", captionKey = "ProductionOrder.customerCode")
    private TextField tfCustomer = new TextField();

    @I18Support(caption = "Sales Order", captionKey = "ProductionOrder.salesOrder")
    private TextField tfSalesCode = new TextField();

    @I18Support(caption = "Sales Order Item", captionKey = "ProductionOrder.salesOrderItem")
    private TextField tfSalesOrderItem = new TextField();

    @I18Support(caption = "Paint Specification", captionKey = "ProductionOrder.paintSpecification")
    private TextField tfPaintSpecification = new TextField();

    @I18Support(caption = "Workshop", captionKey = "ProductionOrder.Workshop")
    private ComboBox<String> cbWorkshop = new ComboBox<>();

    @I18Support(caption = "SuperiorOrder", captionKey = "ProductionOrder.SuperiorOrder")
    private TextField tfSupOrder = new TextField();

    private Binder<ProductionOrder> binder = new Binder<>();

    private String caption;

    private ProductionOrder productionOrder;

    private IProductionOrderService productionOrderService;

    public AddProductionOrderDialog(IProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    public void setObject(ProductionOrder productionOrder) {
        String captionName = I18NUtility.getValue("ProductionOrder.view.caption", "ProductionOrder");
        if (productionOrder == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            productionOrder = new ProductionOrder();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.productionOrder = productionOrder;
        binder.readBean(productionOrder);
        if (productionOrder.getDescription() != null) {
            String year = productionOrder.getDescription().substring(0, 4);
            String month = productionOrder.getDescription().substring(5, 7);
            String day = productionOrder.getDescription().substring(8, 10);
            tfScheduleDate.setValue(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));
        } else {
            tfScheduleDate.clear();
        }
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfProductOrderId)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(ProductionOrder::getProductOrderId, ProductionOrder::setProductOrderId);
        binder.bind(tfProductId, ProductionOrder::getProductId, ProductionOrder::setProductId);
        binder.bind(tfProductVerId, ProductionOrder::getProductVersionId, ProductionOrder::setProductVersionId);
        binder.bind(tfProductDesc, ProductionOrder::getProductDesc, ProductionOrder::setProductDesc);
        binder.forField(tfProductNumber)
                .withConverter(new StringToIntegerConverter(I18NUtility.getValue("Common.OnlyIntegerAllowed", "Only Integer is allowed")))
                .withValidator(new IntegerRangeValidator(
                        I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0,
                        Integer.MAX_VALUE))
                .bind(ProductionOrder::getProductNumber, ProductionOrder::setProductNumber);
        binder.bind(tfRoutingGroup, ProductionOrder::getRoutingGroup, ProductionOrder::setRoutingGroup);
        binder.bind(tfInnerGroupNo, ProductionOrder::getInnerGroupNo, ProductionOrder::setInnerGroupNo);
//        binder.bind(tfScheduleDate, ProductionOrder::getDescription, ProductionOrder::setDescription);
        binder.bind(tfCustomer, ProductionOrder::getCustomerCode, ProductionOrder::setCustomerCode);
        binder.bind(tfSalesCode, ProductionOrder::getSalesOrder, ProductionOrder::setSalesOrder);
        binder.bind(tfSalesOrderItem, ProductionOrder::getSalesOrderItem, ProductionOrder::setSalesOrderItem);
        binder.bind(tfPaintSpecification, ProductionOrder::getPaintSpecification, ProductionOrder::setPaintSpecification);
        binder.bind(taComments, ProductionOrder::getComments, ProductionOrder::setComments);
        binder.bind(cbWorkshop, ProductionOrder::getWorkshop, ProductionOrder::setWorkshop);
        binder.bind(tfSupOrder, ProductionOrder::getSuperiorOrder, ProductionOrder::setSuperiorOrder);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(productionOrder);
        if (!tfScheduleDate.isEmpty()) {
            Integer year = tfScheduleDate.getValue().getYear();
            Integer month = tfScheduleDate.getValue().getMonthValue();
            String smonth;
            String sday;
            if (month < 10) {
                smonth = "0" + month.toString();
            } else {
                smonth = month.toString();
            }
            Integer day = tfScheduleDate.getValue().getDayOfMonth();
            if (day < 10) {
                sday = "0" + day.toString();
            } else {
                sday = day.toString();
            }
            String date = year.toString() + "-" + smonth + "-" + sday;
            productionOrder.setDescription(date);
        }
        ProductionOrder save = productionOrderService.save(productionOrder);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        Panel pTemp = new Panel();
        pTemp.setWidth("100%");
        pTemp.setHeightUndefined();

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        pTemp.setContent(vlContent);

        tfProductOrderId.setWidth("100%");
        tfProductId.setWidth("100%");
        tfProductVerId.setWidth("100%");
        tfProductDesc.setWidth("100%");
        tfProductNumber.setWidth("100%");
        tfRoutingGroup.setWidth("100%");
        tfInnerGroupNo.setWidth("100%");
        tfScheduleDate.setWidth("100%");
        tfScheduleDate.setDateFormat("yyyy-MM-dd");
        taComments.setWidth("100%");
        tfCustomer.setWidth("100%");
        tfSalesCode.setWidth("100%");
        tfSalesOrderItem.setWidth("100%");
        tfPaintSpecification.setWidth("100%");
        cbWorkshop.setItems("#4", "#5", "#6");
        tfSupOrder.setWidth("100%");
        vlContent.addComponents(tfProductOrderId, tfProductId, tfProductVerId, tfProductDesc, tfProductNumber, tfRoutingGroup, tfInnerGroupNo, tfScheduleDate, cbWorkshop, tfCustomer, tfSalesCode, tfSalesOrderItem, tfPaintSpecification, tfSupOrder, taComments);
        return pTemp;
    }

}
