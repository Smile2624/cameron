package com.ags.lumosframework.ui.view.bomnameauthority;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.BomNameAuthorityEntity;
import com.ags.lumosframework.enums.AuthorityType;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IBomNameAuthorityService;
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
public class BomNameAuthorityConditions extends BaseConditions implements IConditions {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6544181251942962042L;
	
//	private TextField tfProductOrderId = new TextField();
	private ComboBox<AuthorityType> cbAuthorityType = new ComboBox<AuthorityType>();
	
	@I18Support(caption = "BomNameAuthority NameEigenvalue", captionKey = "BomNameAuthority.NameEigenvalue")
	private TextField tfNameEigenvalue = new TextField();

//	@I18Support(caption = "Post Name", captionKey = "view.post.name")
//	private TextField tfName = new TextField();

	private HasValue<?>[] fields = { cbAuthorityType,tfNameEigenvalue };

	private Component[] components = { cbAuthorityType };

	private Component[] temp = { cbAuthorityType,tfNameEigenvalue };

	FormLayout hlSearchPanel = new FormLayout();

	public BomNameAuthorityConditions() {
		cbAuthorityType.setPlaceholder(I18NUtility.getValue("BomNameAuthority.AuthorityType", "AuthorityType",new Object[0]));
		cbAuthorityType.setItems(AuthorityType.values());
		cbAuthorityType.setItemCaptionGenerator(item -> {
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
		cbAuthorityType.setId("cb_AuthorityType");
		tfNameEigenvalue.setId("tf_NameEigenvalue");
	}

	@Override
	public Component[] getComponent() {
		return components;
	}

	@Override
	public EntityFilter getFilter() {
		IBomNameAuthorityService bomNameAuthorityService = BeanManager.getService(IBomNameAuthorityService.class);
		EntityFilter createFilter = bomNameAuthorityService.createFilter();
		Optional<AuthorityType> optional = cbAuthorityType.getSelectedItem();//.get();
		if (optional.isPresent()) {
			createFilter.fieldEqualTo(BomNameAuthorityEntity.AUTHORITY_TYPE, optional.get().getKey());
		}
		if (tfNameEigenvalue.getValue() != null && !tfNameEigenvalue.getValue().equals("")) {
			createFilter.fieldContains(BomNameAuthorityEntity.NAME_EIGENVALUE, tfNameEigenvalue.getValue());
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
