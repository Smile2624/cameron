package com.ags.lumosframework.ui.view.gage;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.GageEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IGageService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BasePresenter;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class GageConditions extends BaseConditions implements IConditions {

    private static final long serialVersionUID = -6709454761430938319L;

    FormLayout hlRoot = new FormLayout();
    @I18Support(caption = "GageName", captionKey = "Gage.GageName")
    private TextField tfGageName = new TextField();

    private TextField tfGageNo = new TextField();

    private Component[] components;
    private Component[] temp;

    public GageConditions() {
        this.components = new Component[]{this.tfGageNo};
        this.temp = new Component[]{this.tfGageNo, this.tfGageName};
        this.tfGageNo.setPlaceholder(I18NUtility.getValue("Gage.GageNo", "GageNo", new Object[0]));
        this.hlRoot.setWidthUndefined();
        Component[] var1 = this.temp;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            com.vaadin.ui.Component component = var1[var3];
            component.setWidthUndefined();
            this.hlRoot.addComponent(component);
        }
        this.setElementsId();
    }

    private void setElementsId() {
        this.tfGageNo.setId("tf_GageNo");
        this.tfGageName.setId("tf_GageName");
    }

    public Object getFilter() {
        IGageService gageService = (IGageService) BeanManager.getService(IGageService.class);
        EntityFilter createFilter = gageService.createFilter();
        if (this.tfGageNo.getValue() != null && !this.tfGageNo.getValue().equals("")) {
            createFilter.fieldEqualTo(GageEntity.GAGE_NO, this.tfGageNo.getValue());
        }
        if (this.tfGageName.getValue() != null && !this.tfGageName.getValue().equals("")) {
            createFilter.fieldContains(GageEntity.GAGE_NAME, this.tfGageName.getValue());
        }
        return createFilter;
    }

    public BasePresenter<?> getPresenter() {
        return null;
    }

    public void reset() {
        this.tfGageName.clear();
        this.tfGageNo.clear();
    }

    public Component[] getComponent() {
        return this.components;
    }

    public AbstractLayout getLayout() {
        return this.hlRoot;
    }
}
