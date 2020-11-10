package com.ags.lumosframework.ui.view.inspectionvalue;


import com.ags.lumosframework.pojo.InspectionValue;
import com.ags.lumosframework.service.IInspectionTypeService;
import com.ags.lumosframework.service.IInspectionValueService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddInspectionValueDialog extends BaseDialog {

	private static final long serialVersionUID = -6230428947808654930L;

	@I18Support(caption="Inspection_Name", captionKey="view.inspectionvalue.inspectionname")
	private ComboBox<String> cbInspectionType = new ComboBox<>();
	
	@I18Support(caption="Product_Specification", captionKey="view.inspectionvalue.productspecification")
	private TextField tfProductSpecification = new TextField();
	
	@I18Support(caption="Appearance_Desc", captionKey="view.inspectionvalue.appearancedesc")
	private TextField tfAppreanceDesc = new TextField();
	
	@I18Support(caption="Min_Value", captionKey="view.inspectionvalue.minvalue")
	private TextField tfMinValue = new TextField();
	
	@I18Support(caption="Max_Value", captionKey="view.inspectionvalue.maxvalue")
	private TextField tfMaxValue = new TextField();
	
	private Binder<InspectionValue> binder = new Binder<>();
	

	private String caption;
	
	private String action;
	
	private InspectionValue inspectionValue;
	
	private IInspectionValueService inspectionValueService;
	@Autowired
	private IInspectionTypeService inspectionTypeService;
	
	public AddInspectionValueDialog(IInspectionValueService inspectionValueService) {
		this.inspectionValueService = inspectionValueService;
	}
	
	public void setObject(InspectionValue inspectionValue) {
		
        String captionName = I18NUtility.getValue("view.inspectionvalue.caption", "InspectionValue");
        if (inspectionValue == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            action = "NEW";
            inspectionValue = new InspectionValue(null);
        }else{
        	action = "EDIT";
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.inspectionValue = inspectionValue;
        binder.readBean(inspectionValue);
    }
    
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();

        cbInspectionType.setWidth("100%");
        cbInspectionType.setItems(inspectionTypeService.getAllInspectionType());
        tfProductSpecification.setWidth("100%");
        tfAppreanceDesc.setWidth("100%");
        tfMinValue.setWidth("100%");
        tfMaxValue.setWidth("100%");
        vlContent.addComponents(cbInspectionType, tfProductSpecification,tfAppreanceDesc,tfMinValue,tfMaxValue);
        return vlContent;
	}

	@Override
	protected void initUIData() {
	    binder.forField(cbInspectionType).asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
	    .bind(InspectionValue::getInspection_name, InspectionValue::setInspection_name);
        binder.forField(tfProductSpecification).asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
        .bind( InspectionValue::getProduct_specification, InspectionValue::setProduct_specification);
        binder.bind(tfAppreanceDesc, InspectionValue::getAppearance_desc, InspectionValue::setAppearance_desc);
        binder.forField(tfMinValue).withConverter(new StringToDoubleConverter(
                I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float is allowed")))
                .withValidator(new DoubleRangeValidator(I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0D, Double.MAX_VALUE))
                .bind(InspectionValue::getMin_value, InspectionValue::setMin_value);
        binder.forField(tfMaxValue).withConverter(new StringToDoubleConverter(
                I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float is allowed")))
                .withValidator(new DoubleRangeValidator(I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0D, Double.MAX_VALUE))
                .bind(InspectionValue::getMax_value, InspectionValue::setMax_value);
    }

	@Override
	protected void okButtonClicked() throws Exception {
        binder.writeBean(inspectionValue);
        if("NEW".equals(action)) {
            String name = inspectionValue.getInspection_name();
            String specification = inspectionValue.getProduct_specification();
            InspectionValue instance = inspectionValueService.getByNameAndSpecification(name, specification);
            if(instance != null && instance.getInspection_name().length() > 0) {
            	NotificationUtils.notificationError(I18NUtility.getValue("view.inspectionvalue.addinspectionvalue.inspectionvalueexist", "This InspectionValue-Specification Has Exist."));
            	return;
            }
        }

        InspectionValue save = inspectionValueService.save(inspectionValue);
        result.setObj(save);
	}

	
}
