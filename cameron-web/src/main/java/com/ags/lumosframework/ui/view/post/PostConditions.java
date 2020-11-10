package com.ags.lumosframework.ui.view.post;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.PostEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IPostService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
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
public class PostConditions extends BaseConditions implements IConditions {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6273399551929939009L;

	// 查询区域控件
	private TextField tfCode = new TextField();

	@I18Support(caption = "Post Name", captionKey = "view.post.name")
	private TextField tfName = new TextField();

	private HasValue<?>[] fields = { tfCode, tfName };

	private Component[] components = { tfCode };

	private Component[] temp = { tfName };

	FormLayout hlSearchPanel = new FormLayout();

	public PostConditions() {
		tfCode.setPlaceholder(I18NUtility.getValue("view.post.code", "PostCode"));
		for (Component component : temp) {
			component.setWidth("100%");
			hlSearchPanel.addComponent(component);
		}
		setElementsId();
	}

	private void setElementsId() {
		tfCode.setId("tf_Code");
		tfName.setId("tf_Name");
	}

	@Override
	public Component[] getComponent() {
		return components;
	}

	@Override
	public EntityFilter getFilter() {
		IPostService postService = BeanManager.getService(IPostService.class);
		EntityFilter createFilter = postService.createFilter();
		if (tfCode.getValue() != null && !tfCode.getValue().equals("")) {
			createFilter.fieldEqualTo(PostEntity.POST_CODE, tfCode.getValue());
		}
		if (tfName.getValue() != null && !tfName.getValue().equals("")) {
			createFilter.fieldContains(PostEntity.POST_NAME, tfName.getValue());
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
