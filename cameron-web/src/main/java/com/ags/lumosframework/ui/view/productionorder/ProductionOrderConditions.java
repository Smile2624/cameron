package com.ags.lumosframework.ui.view.productionorder;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class ProductionOrderConditions extends BaseConditions implements IConditions {

    /**
     *
     */
    private static final long serialVersionUID = 2935334446012764859L;

    // 查询区域控件
    private TextField tfProductOrderId = new TextField();

    @I18Support(caption = "ProductInformation ProductId", captionKey = "ProductInformation.ProductId")
    private TextField tfProductId = new TextField();

    @I18Support(caption = "ScheduleDate", captionKey = "ProductionOrder.scheduledate")
    private TextField tfSchedule = new TextField();

    @I18Support(caption = "NewBomCheck", captionKey = "ProductionOrder.NewBomCheck")
    private CheckBox cbBomCkeck = new CheckBox();

    private HasValue<?>[] fields = {tfProductOrderId, tfProductId, tfSchedule};

    private Component[] components = {tfProductOrderId};

    private Component[] temp = {tfProductOrderId, tfProductId, tfSchedule, cbBomCkeck};

    FormLayout hlSearchPanel = new FormLayout();

    public ProductionOrderConditions() {
        tfProductOrderId.setPlaceholder(I18NUtility.getValue("ProductionOrder.productOrderId", "productOrderId", new Object[0]));
        hlSearchPanel.setWidthUndefined();
        for (Component component : temp) {
//					com.vaadin.ui.Component componentRes = component;
            component.setWidthUndefined();
            hlSearchPanel.addComponent(component);
        }
        setElementsId();
    }

    private void setElementsId() {
        tfProductOrderId.setId("tf_ProductOrderId");
        tfProductId.setId("tf_ProductId");
        tfSchedule.setId("tf_Schedule");
        cbBomCkeck.setId("ck_BomCkeck");
    }

    @Override
    public Component[] getComponent() {
        return components;
    }

    @Override
    public EntityFilter getFilter() {
        IProductionOrderService productionOrderService = BeanManager.getService(IProductionOrderService.class);
        EntityFilter createFilter = productionOrderService.createFilter();
        if (tfProductOrderId.getValue() != null && !tfProductOrderId.getValue().equals("")) {
            createFilter.fieldContains(ProductionOrderEntity.PRODUCT_ORDER_ID, tfProductOrderId.getValue());
        }
        if (tfProductId.getValue() != null && !tfProductId.getValue().equals("")) {
            createFilter.fieldContains(ProductionOrderEntity.PRODUCT_ID, tfProductId.getValue());
        }
        if (tfSchedule.getValue() != null && !tfSchedule.getValue().equals("")) {
            createFilter.fieldContains(ProductionOrderEntity.DESCRIPTION, tfSchedule.getValue());
        }
        if (cbBomCkeck.getValue() != null && cbBomCkeck.getValue() == true) {
            createFilter.fieldEqualTo(ProductionOrderEntity.BOM_CHECK_FLAG, true);
        }
        return createFilter;
    }

    @Override
    public AbstractLayout getLayout() {
        return hlSearchPanel;
    }

    @Override
    public void reset() {
        for (HasValue<?> field : fields) {
            field.clear();
        }
    }

}
