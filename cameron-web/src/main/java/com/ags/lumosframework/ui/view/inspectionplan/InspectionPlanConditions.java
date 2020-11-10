package com.ags.lumosframework.ui.view.inspectionplan;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.InspectionPlanEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IInspectionPlanService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class InspectionPlanConditions extends BaseConditions implements IConditions {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 查询区域控件

    private TextField tfProductNo = new TextField();

//		@I18Support(caption = "Post Name", captionKey = "view.post.name")
//		private TextField tfName = new TextField();

    private HasValue<?>[] fields = {tfProductNo};

    private Component[] components = {tfProductNo};

    private Component[] temp = {tfProductNo};

    FormLayout hlSearchPanel = new FormLayout();

    public InspectionPlanConditions() {
        tfProductNo.setPlaceholder(I18NUtility.getValue("ProductInformation.ProductId", "ProductId", new Object[0]));
        hlSearchPanel.setWidthUndefined();
        for (Component component : temp) {
            component.setWidthUndefined();
            hlSearchPanel.addComponent(component);
        }
        setElementsId();
    }

    private void setElementsId() {
        tfProductNo.setId("tf_ProductNo");
    }

    @Override
    public Component[] getComponent() {
        return components;
    }

    @Override
    public EntityFilter getFilter() {
        IInspectionPlanService inspectionPlanService = BeanManager.getService(IInspectionPlanService.class);
        EntityFilter createFilter = inspectionPlanService.createFilter();
        if (tfProductNo.getValue() != null && !tfProductNo.getValue().equals("")) {
            createFilter.fieldEqualTo(InspectionPlanEntity.PRODUCT_NO, tfProductNo.getValue());
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
