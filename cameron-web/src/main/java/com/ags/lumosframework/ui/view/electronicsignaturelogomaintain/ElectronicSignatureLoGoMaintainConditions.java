package com.ags.lumosframework.ui.view.electronicsignaturelogomaintain;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.entity.MediaEntity;
import com.ags.lumosframework.service.ICaMediaService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

import java.util.Optional;

@SpringComponent
@Scope("prototype")
public class ElectronicSignatureLoGoMaintainConditions extends BaseConditions implements IConditions {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4522900595846940532L;

	private ComboBox<ElectronicSignatureLoGoType> cbESignatureLoGoType = new ComboBox<ElectronicSignatureLoGoType>();
	
	@I18Support(caption = "FileName", captionKey = "ElectronicSignatureLoGoMaintain.FileName")
	private TextField tfFileName = new TextField();

	private HasValue<?>[] fields = { cbESignatureLoGoType,tfFileName };

	private Component[] components = { cbESignatureLoGoType };

	private Component[] temp = { cbESignatureLoGoType,tfFileName };

	FormLayout hlSearchPanel = new FormLayout();

	public ElectronicSignatureLoGoMaintainConditions() {
		cbESignatureLoGoType.setPlaceholder(I18NUtility.getValue("ElectronicSignatureLoGoMaintain.ESignatureLoGoType", "ESignatureLoGoType",new Object[0]));
		cbESignatureLoGoType.setItems(ElectronicSignatureLoGoType.values());
		cbESignatureLoGoType.setItemCaptionGenerator(item -> {
			return I18NUtility.getValue(item.getKey(), item.getType());
		});
		hlSearchPanel.setWidthUndefined();
		for (Component component : temp) {
//			com.vaadin.ui.Component componentRes = component;
			component.setWidthUndefined();
			hlSearchPanel.addComponent(component);
		}
		setElementsId();
	}

	private void setElementsId() {
		cbESignatureLoGoType.setId("cb_ESignatureLoGoType");
		tfFileName.setId("tf_FileName");
	}

	@Override
	public Component[] getComponent() {
		return components;
	}

	@Override
	public EntityFilter getFilter() {
		ICaMediaService mediaService = BeanManager.getService(ICaMediaService.class);
		EntityFilter createFilter = mediaService.createFilter();
		Optional<ElectronicSignatureLoGoType> optional = cbESignatureLoGoType.getSelectedItem();//.get();
		if (optional.isPresent()) {
			createFilter.fieldEqualTo("category", optional.get().getKey());
		}
		if (tfFileName.getValue() != null && !tfFileName.getValue().equals("")) {
			createFilter.fieldContains(MediaEntity.NAME, tfFileName.getValue());
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
