package com.ags.lumosframework.ui.view.inspectiontype;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.InspectionTypeEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IInspectionTypeService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BasePresenter;
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
public class InspectionTypeConditions extends BaseConditions implements IConditions {

	private static final long serialVersionUID = -7291096544628851805L;

	
	//查询区域控件
//	@I18Support(caption = "InspectionName", captionKey = "view.inspection.inspectionname")
	private TextField tfInspectionName = new TextField();
	
	@I18Support(caption = "InspectionDesc", captionKey = "view.inspection.inspectiondesc")
	private TextField tfInspectionDesc = new TextField();
	
	private HasValue<?>[] fields = { tfInspectionName, tfInspectionDesc};
	
	private Component[] components = { tfInspectionName};
	
	private Component[] temp = { tfInspectionDesc };
	FormLayout hlSearchPanel = new FormLayout();
	
	public InspectionTypeConditions() {

		tfInspectionName.setPlaceholder(I18NUtility.getValue("view.inspection.inspectionname", "InspectionName"));
		for (Component component : temp) {
			component.setWidth("100%");
			hlSearchPanel.addComponent(component);
		}
		setElementsId();
	}
	
	private void setElementsId() {
		tfInspectionName.setId("tf_InspectionName");
		tfInspectionDesc.setId("tf_InspectionDesc");
	}

	@Override
	public EntityFilter getFilter() {
		IInspectionTypeService inspectionService = BeanManager.getService(IInspectionTypeService.class);
		EntityFilter createFilter = inspectionService.createFilter();
		if (tfInspectionName.getValue() != null && !tfInspectionName.getValue().equals("")) {
			createFilter.fieldEqualTo(InspectionTypeEntity.INSPECTION_NAME, tfInspectionName.getValue());
		}
		if(tfInspectionDesc.getValue() != null && !tfInspectionDesc.getValue().equals("")) {
			createFilter.fieldContains(InspectionTypeEntity.INSPECTION_DESC, tfInspectionDesc.getValue());
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
