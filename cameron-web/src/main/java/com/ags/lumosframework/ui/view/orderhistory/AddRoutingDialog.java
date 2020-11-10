package com.ags.lumosframework.ui.view.orderhistory;

import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.service.IOrderHistoryService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddRoutingDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = -5638945860642660844L;

    @I18Support(caption = "ProductOrderId", captionKey = "ProductionOrder.productOrderId")
    private TextField tfProductOrderId = new TextField();

    @I18Support(caption = "Operation", captionKey = "OrderHistory.OperationNo")
    private TextField tfOperation = new TextField();

    @I18Support(caption = "Description", captionKey = "OrderHistory.OperationDesc")
    private TextField tfDescription = new TextField();

    @I18Support(caption = "Attention", captionKey = "ProductRouting.Attention")
    private TextArea taLongText = new TextArea();

    private Binder<OrderHistory> binder = new Binder<>();

    private String caption;

    private OrderHistory orderHistory;

    private IOrderHistoryService orderHistoryService;

    public AddRoutingDialog(IOrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    public void setObject(OrderHistory orderHistory) {
        String captionName = I18NUtility.getValue("ProductRouting.view.caption", "Routing");
        if (orderHistory == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            orderHistory = new OrderHistory();
        } else if (orderHistory.getOperationNo() == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.orderHistory = orderHistory;
        binder.readBean(orderHistory);
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
                .bind(OrderHistory::getOrderNo, OrderHistory::setOrderNo);
        binder.forField(tfOperation)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(OrderHistory::getOperationNo, OrderHistory::setOperationNo);
        binder.bind(tfDescription, OrderHistory::getOperationDesc, OrderHistory::setOperationDesc);
        binder.bind(taLongText, OrderHistory::getOperationAttention, OrderHistory::setOperationAttention);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(orderHistory);
        OrderHistory save = orderHistoryService.save(orderHistory);
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
        tfOperation.setWidth("100%");
        tfDescription.setWidth("100%");
        taLongText.setWidth("100%");
        vlContent.addComponents(tfProductOrderId, tfOperation, tfDescription, taLongText);
        return pTemp;
    }

}
