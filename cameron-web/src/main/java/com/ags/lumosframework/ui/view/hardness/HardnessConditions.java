package com.ags.lumosframework.ui.view.hardness;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.HardnessEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IHardnessService;
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
public class HardnessConditions extends BaseConditions implements IConditions {

    private static final long serialVersionUID = -7552148060488632419L;

    FormLayout hlRoot = new FormLayout();
    private TextField tfHardnessStand = new TextField();
    @I18Support(caption = "hardnessUpLimit", captionKey = "Hardness.hardnessUpLimit")
    private TextField tfHardnessUpLimit = new TextField();
    @I18Support(caption = "hardnessDownLimit", captionKey = "Hardness.hardnessDownLimit")
    private TextField tfHardnessDownLimit = new TextField();

    private Component[] components;
    private Component[] temp;

    public HardnessConditions() {
        this.components = new Component[]{this.tfHardnessStand};
        this.temp = new Component[]{this.tfHardnessStand, this.tfHardnessUpLimit, this.tfHardnessDownLimit};
        this.tfHardnessStand.setPlaceholder(I18NUtility.getValue("Hardness.hardnessName", "HardnessName", new Object[0]));
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
        this.tfHardnessStand.setId("tf_HardnessStand");
    }

    public Object getFilter() {
        IHardnessService hardnessService = (IHardnessService) BeanManager.getService(IHardnessService.class);
        EntityFilter createFilter = hardnessService.createFilter();
        if (this.tfHardnessStand.getValue() != null && !this.tfHardnessStand.getValue().equals("")) {
            createFilter.fieldEqualTo(HardnessEntity.HARDNESS_STAND, this.tfHardnessStand.getValue());
        }
        if (this.tfHardnessUpLimit.getValue() != null && !this.tfHardnessUpLimit.getValue().equals("")) {
            createFilter.fieldEqualTo(HardnessEntity.HARDNESS_UP_LIMIT, Float.parseFloat(this.tfHardnessUpLimit.getValue()));
        }
        if (this.tfHardnessDownLimit.getValue() != null && !this.tfHardnessDownLimit.getValue().equals("")) {
            createFilter.fieldEqualTo(HardnessEntity.HARDNESS_DOWN_LIMIT, Float.parseFloat(this.tfHardnessDownLimit.getValue()));
        }
        return createFilter;
    }

    public BasePresenter<?> getPresenter() {
        return null;
    }

    public void reset() {
        this.tfHardnessStand.clear();
    }

    public Component[] getComponent() {
        return this.components;
    }

    public AbstractLayout getLayout() {
        return this.hlRoot;
    }
}
