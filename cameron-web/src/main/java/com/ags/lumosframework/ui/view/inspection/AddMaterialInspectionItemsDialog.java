package com.ags.lumosframework.ui.view.inspection;

import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.pojo.VendorMaterialInspectionItems;
import com.ags.lumosframework.service.IVendorMaterialInspectionItemService;
import com.ags.lumosframework.service.IVendorMaterialService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddMaterialInspectionItemsDialog extends BaseDialog {

    @I18Support(caption = "ItemName", captionKey = "materilruler.item-name")
    private TextField tfItemName = new TextField();

    @I18Support(caption = "ItemType", captionKey = "materilruler.item-type")
    private ComboBox<String> cbItemType = new ComboBox();

    @I18Support(caption = "MaxValue", captionKey = "materilruler.max-value")
    private TextField tfMaxValue = new TextField();

    @I18Support(caption = "MinValue", captionKey = "materilruler.min-value")
    private TextField tfMinValue = new TextField();

    @I18Support(caption = "StandardValue", captionKey = "materilruler.standard-value")
    private TextField tfStandardValue = new TextField();

    @I18Support(caption = "Value", captionKey = "materilruler.value")
    private TextField tfValue = new TextField();

    private String caption;

    private VendorMaterialInspectionItems vendorMaterialInspectionItems;

    private Binder<VendorMaterialInspectionItems> binder = new Binder<>();


    @Autowired
    private IVendorMaterialInspectionItemService vendorMaterialInspectionItemService;

    public AddMaterialInspectionItemsDialog(IVendorMaterialInspectionItemService vendorMaterialInspectionItemService) {
        this.vendorMaterialInspectionItemService = vendorMaterialInspectionItemService;
    }

    public void setObject(VendorMaterial vendorMaterial, VendorMaterialInspectionItems vendorMaterialInspectionItems) {
        if (vendorMaterialInspectionItems == null) {
            this.caption = I18NUtility.getValue("materilruler.add-material-item", "AddItem");
            vendorMaterialInspectionItems = new VendorMaterialInspectionItems();
            vendorMaterialInspectionItems.setMaterialId(vendorMaterial.getId());
        } else {
            this.caption = I18NUtility.getValue("materilruler.edit-material-item", "EditItem");
        }
        this.vendorMaterialInspectionItems = vendorMaterialInspectionItems;
        binder.readBean(vendorMaterialInspectionItems);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfItemName)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(VendorMaterialInspectionItems::getInspectionItemName, VendorMaterialInspectionItems::setInspectionItemName);
        binder.forField(cbItemType)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(VendorMaterialInspectionItems::getInspectionItemType, VendorMaterialInspectionItems::setInspectionItemType);
        binder.forField(tfMaxValue)
                .bind(new ValueProvider<VendorMaterialInspectionItems, String>() {
                    @Override
                    public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                        return vendorMaterialInspectionItems.getMaxValue() == 0.0 ? "" : String.valueOf(vendorMaterialInspectionItems.getMaxValue());
                    }
                }, new Setter<VendorMaterialInspectionItems, String>() {
                    @Override
                    public void accept(VendorMaterialInspectionItems vendorMaterialInspectionItems, String s) {
                        vendorMaterialInspectionItems.setMaxValue(Strings.isNullOrEmpty(s) ? 0.0 : Double.valueOf(s));
                    }
                });
        binder.forField(tfMinValue)
                .bind(new ValueProvider<VendorMaterialInspectionItems, String>() {
                    @Override
                    public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                        return vendorMaterialInspectionItems.getMinValue() == 0 ? "" : String.valueOf(vendorMaterialInspectionItems.getMinValue());
                    }
                }, new Setter<VendorMaterialInspectionItems, String>() {
                    @Override
                    public void accept(VendorMaterialInspectionItems vendorMaterialInspectionItems, String s) {
                        vendorMaterialInspectionItems.setMinValue(Strings.isNullOrEmpty(s) ? 0.0 : Double.valueOf(s));
                    }
                });
        binder.forField(tfStandardValue)
                .bind(new ValueProvider<VendorMaterialInspectionItems, String>() {
                    @Override
                    public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                        return vendorMaterialInspectionItems.getStandardValue() == 0 ? "" : String.valueOf(vendorMaterialInspectionItems.getStandardValue());
                    }
                }, new Setter<VendorMaterialInspectionItems, String>() {
                    @Override
                    public void accept(VendorMaterialInspectionItems vendorMaterialInspectionItems, String s) {
                        vendorMaterialInspectionItems.setStandardValue(Strings.isNullOrEmpty(s) ? 0.0 : Double.valueOf(s));
                    }
                });
        binder.bind(tfValue, VendorMaterialInspectionItems::getValue, VendorMaterialInspectionItems::setValue);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(vendorMaterialInspectionItems);
        VendorMaterialInspectionItems save = vendorMaterialInspectionItemService.save(vendorMaterialInspectionItems);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        tfItemName.setWidth("100%");
        cbItemType.setWidth("100%");
        cbItemType.setItems("阀阈值", "标准值", "描述信息");
        cbItemType.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            //当检验项的检验标准改变时，更改下面的检验值输入控件的状态
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                if ("阀阈值".equals(valueChangeEvent.getValue())) {
                    tfMaxValue.setEnabled(true);
                    tfMinValue.setEnabled(true);
                } else {
                    tfMaxValue.setEnabled(false);
                    tfMaxValue.clear();
                    tfMinValue.setEnabled(false);
                    tfMinValue.clear();
                }
                if ("标准值".equals(valueChangeEvent.getValue())) {
                    tfStandardValue.setEnabled(true);
                } else {
                    tfStandardValue.setEnabled(false);
                    tfStandardValue.clear();
                }
                if ("描述信息".equals(valueChangeEvent.getValue())) {
                    tfValue.setEnabled(true);
                } else {
                    tfValue.setEnabled(false);
                    tfValue.clear();
                }
            }
        });
        tfMaxValue.setWidth("100%");
        tfMinValue.setWidth("100%");
        tfStandardValue.setWidth("100%");
        tfValue.setWidth("100%");
        vlContent.addComponents(tfItemName, cbItemType, tfMaxValue, tfMinValue, tfStandardValue, tfValue);
        return vlContent;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }
}
