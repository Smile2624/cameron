package com.ags.lumosframework.ui.view.sparepart;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.SparePartEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.ISparePartService;
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
public class SparePartConditions extends BaseConditions implements IConditions {

    private static final long serialVersionUID = 2052100817845850642L;

    FormLayout hlRoot = new FormLayout();

    private TextField tfSPratNo = new TextField();

    @I18Support(caption = "SparePartRev", captionKey = "SparePart.SparePartRev")
    private TextField tfSPratRev = new TextField();

    private Component[] components;
    private Component[] temp;

    public SparePartConditions() {
        this.components = new Component[]{this.tfSPratNo};
        this.temp = new Component[]{this.tfSPratNo, this.tfSPratRev};
        this.tfSPratNo.setPlaceholder(I18NUtility.getValue("SparePart.SparePartNo", "SparePartNo", new Object[0]));
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
        this.tfSPratNo.setId("tf_SPartNo");
        this.tfSPratRev.setId("tf_SPratRev");
    }

    public Object getFilter() {
        ISparePartService sparePartService = (ISparePartService) BeanManager.getService(ISparePartService.class);
        EntityFilter createFilter = sparePartService.createFilter();
        if (this.tfSPratNo.getValue() != null && !this.tfSPratNo.getValue().equals("")) {
            createFilter.fieldContains(SparePartEntity.SPARE_PART_NO, this.tfSPratNo.getValue());
        }

        if (this.tfSPratRev.getValue() != null && !"".equals(this.tfSPratRev.getValue())) {
            createFilter.fieldContains(SparePartEntity.SPARE_PART_REV, this.tfSPratRev.getValue());
        }
        return createFilter;
    }

    public BasePresenter<?> getPresenter() {
        return null;
    }

    public void reset() {
        this.tfSPratNo.clear();
        this.tfSPratRev.clear();
    }

    public Component[] getComponent() {
        return this.components;
    }

    public AbstractLayout getLayout() {
        return this.hlRoot;
    }
}
