package com.ags.lumosframework.ui.view.assembling;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.InspectionValueEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IInspectionTypeService;
import com.ags.lumosframework.service.IInspectionValueService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BasePresenter;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AssemblingConditions extends BaseConditions implements IConditions {

	private static final long serialVersionUID = -7291096544628851805L;

//	@I18Support(caption="Inspection_Name", captionKey="view.inspectionvalue.inspectionname")
	private ComboBox<String> cbInspectionType = new ComboBox<>();
	
	@I18Support(caption="Product_Specification", captionKey="view.inspectionvalue.productspecification")
	private TextField tfProductSpecification = new TextField();
	
	@I18Support(caption="Appearance_Desc", captionKey="view.inspectionvalue.appearancedesc")
	private TextField tfAppreanceDesc = new TextField();
	
	@I18Support(caption="Min_Value", captionKey="view.inspectionvalue.minvalue")
	private TextField tfMinValue = new TextField();
	
	@I18Support(caption="Max_Value", captionKey="view.inspectionvalue.maxvalue")
	private TextField tfMaxValue = new TextField();
	
	private HasValue<?>[] fields = { cbInspectionType, tfProductSpecification,tfAppreanceDesc,tfMinValue,tfMaxValue};
	
	private Component[] components = { cbInspectionType};
	
	private Component[] temp = { tfProductSpecification, tfAppreanceDesc ,tfMinValue ,tfMaxValue};
	FormLayout hlSearchPanel = new FormLayout();
	
	public AssemblingConditions() {

		cbInspectionType.setPlaceholder(I18NUtility.getValue("view.inspection.inspectionname", "InspectionName"));
		for (Component component : temp) {
			component.setWidth("100%");
			hlSearchPanel.addComponent(component);
		}
		cbInspectionType.setItems(BeanManager.getService(IInspectionTypeService.class).getAllInspectionType());
		setElementsId();
	}
	
	private void setElementsId() {
		cbInspectionType.setId("cb_InspectionType");
		tfProductSpecification.setId("tf_ProductSpecification");
		tfAppreanceDesc.setId("tf_AppreanceDesc");
		tfMinValue.setId("tf_MinValue");
		tfMaxValue.setId("tf_MaxValue");
	}

	@Override
	public EntityFilter getFilter() {
		IInspectionValueService inspectionService = BeanManager.getService(IInspectionValueService.class);
		EntityFilter createFilter = inspectionService.createFilter();
		if (cbInspectionType.getValue() != null && !"".equals(cbInspectionType.getValue())) {
			createFilter.fieldEqualTo(InspectionValueEntity.INSPECTION_NAME, cbInspectionType.getValue());
		}
		if(tfProductSpecification.getValue() != null && !"".equals(tfProductSpecification.getValue())) {
			createFilter.fieldEqualTo(InspectionValueEntity.PRODUCT_SPECIFICATION, tfProductSpecification.getValue());
		}
		if(tfAppreanceDesc.getValue() != null && !"".equals(tfAppreanceDesc.getValue())) {
			createFilter.fieldEqualTo(InspectionValueEntity.APPEARANCE_DESC, tfAppreanceDesc.getValue());
		}
		if(tfMinValue.getValue() != null && !"".equals(tfMinValue.getValue())) {
			createFilter.fieldEqualTo(InspectionValueEntity.MIN_VALUE,Double.parseDouble(tfMinValue.getValue()));
		}
		if(tfMaxValue.getValue() != null && !"".equals(tfMaxValue.getValue())) {
			createFilter.fieldEqualTo(InspectionValueEntity.MAX_VALUE, Double.parseDouble(tfMaxValue.getValue()));
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

	@Override
	public Component[] getComponent() {
		return components;
	}
	
	@Override
	public BasePresenter<?> getPresenter() {
		return null;
	}

}
