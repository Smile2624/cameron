package com.ags.lumosframework.ui.view.material;

import com.ags.lumosframework.pojo.IssueMaterialList;
import com.ags.lumosframework.service.IIssueMaterialService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class MatRemarkDialog extends BaseDialog {

    private static final long serialVersionUID = -4749254869133405571L;

    TextArea tfRemark = new TextArea();

    @Autowired
    IIssueMaterialService issueMaterialService;

    IssueMaterialList materialList = null;

    @Override
    protected void initUIData() {
        tfRemark.setValue(materialList == null ? "" : materialList.getDescription());
    }

    public void setObject(IssueMaterialList materialList) {
        tfRemark.setValue(materialList.getDescription() == null ? "" : materialList.getDescription());
        this.materialList = materialList;
    }

    @Override
    protected void okButtonClicked() throws Exception {
        materialList.setDescription(tfRemark.getValue().trim());
        issueMaterialService.save(materialList);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        vlContent.addComponent(tfRemark);
        tfRemark.setWidth("100%");
        return vlContent;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, I18NUtility.getValue("issuematerial.remark", "Remark"), "350px", null, false, true, callBack);
    }
}
