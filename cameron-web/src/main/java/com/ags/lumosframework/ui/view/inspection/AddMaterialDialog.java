package com.ags.lumosframework.ui.view.inspection;

import com.ags.lumosframework.pojo.InspectionPlan;
import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.service.IVendorMaterialService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddMaterialDialog extends BaseDialog {

    @I18Support(caption = "MaterialNo", captionKey = "materilruler.material-no")
    private TextField tfMaterialNo = new TextField();

    @I18Support(caption = "MaterialRev", captionKey = "materilruler.material-rev")
    private TextField tfMaterialRev = new TextField();

    @I18Support(caption = "MaterialDesc", captionKey = "materilruler.material-desc")
    private TextField tfMaterialDesc = new TextField();

    private String caption;

    private VendorMaterial vendorMaterial;

    private Binder<VendorMaterial> binder = new Binder<>();


    @Autowired
    private IVendorMaterialService vendorMaterialService;
    public AddMaterialDialog(IVendorMaterialService vendorMaterialService){
        this.vendorMaterialService = vendorMaterialService;
    }

    public void setObject(VendorMaterial vendorMaterial) {
        if (vendorMaterial == null) {
            this.caption = I18NUtility.getValue("materilruler.add-material", "AddMaterial");
            vendorMaterial = new VendorMaterial();
        } else {
            this.caption = I18NUtility.getValue("materilruler.edit-material", "EditMaterial");
        }
        this.vendorMaterial = vendorMaterial;
        binder.readBean(vendorMaterial);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfMaterialNo)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(VendorMaterial::getMaterialNo, VendorMaterial::setMaterialNo);
        binder.forField(tfMaterialRev)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(VendorMaterial::getMaterialRev, VendorMaterial::setMaterialRev);
        binder.bind(tfMaterialDesc, VendorMaterial::getMaterialDesc, VendorMaterial::setMaterialDesc);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(vendorMaterial);
        VendorMaterial save = vendorMaterialService.save(vendorMaterial);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        tfMaterialNo.setWidth("100%");
        tfMaterialRev.setWidth("100%");
        tfMaterialDesc.setWidth("100%");
        vlContent.addComponents(tfMaterialNo,tfMaterialRev,tfMaterialDesc);
        return vlContent;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }
}
