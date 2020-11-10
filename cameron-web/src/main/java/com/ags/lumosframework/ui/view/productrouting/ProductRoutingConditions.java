package com.ags.lumosframework.ui.view.productrouting;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductRoutingEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IProductRoutingService;
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
public class ProductRoutingConditions extends BaseConditions implements IConditions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5915644237102008458L;
	
	// 查询区域控件
	
	private TextField tfProductId = new TextField();
	
//	@I18Support(caption = "ProductRouting ProductVersionId", captionKey = "ProductRouting.ProductVersionId")
//	private TextField tfProductVersionId = new TextField();
	
	@I18Support(caption = "ProductRouting RoutingGroup", captionKey = "ProductRouting.RoutingGroup")
	private TextField tfRoutingGroup = new TextField();
	
	@I18Support(caption = "ProductRouting InnerGroupNo", captionKey = "ProductRouting.InnerGroupNo")
	private TextField tfInnerGroupNo = new TextField();

//				@I18Support(caption = "Post Name", captionKey = "view.post.name")
//				private TextField tfName = new TextField();

	private HasValue<?>[] fields = { tfProductId,tfRoutingGroup,tfInnerGroupNo };

	private Component[] components = { tfProductId };

	private Component[] temp = { tfProductId,tfRoutingGroup,tfInnerGroupNo };

	FormLayout hlSearchPanel = new FormLayout();

	public ProductRoutingConditions() {
		tfProductId.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductId", "ProductId",new Object[0]));
		hlSearchPanel.setWidthUndefined();
		for (Component component : temp) {
//						com.vaadin.ui.Component componentRes = component;
			component.setWidthUndefined();
			hlSearchPanel.addComponent(component);
		}
		setElementsId();
	}

	private void setElementsId() {
		tfProductId.setId("tf_ProductId");
//		tfProductVersionId.setId("tf_ProductVersionId");
		tfRoutingGroup.setId("tf_RoutingGroup");
		tfInnerGroupNo.setId("tf_InnerGroupNo");
	}

	@Override
	public Component[] getComponent() {
		return components;
	}

	@Override
	public EntityFilter getFilter() {
		IProductRoutingService productRoutingService = BeanManager.getService(IProductRoutingService.class);
		EntityFilter createFilter = productRoutingService.createFilter();
		if (tfProductId.getValue() != null && !tfProductId.getValue().equals("")) {
			createFilter.fieldEqualTo(ProductRoutingEntity.PRODUCT_ID, tfProductId.getValue());
		}
//		if (tfProductVersionId.getValue() != null && !tfProductVersionId.getValue().equals("")) {
//			createFilter.fieldContains(ProductRoutingEntity.PRODUCT_DESC, tfProductVersionId.getValue());
//		}
		if (tfRoutingGroup.getValue() != null && !tfRoutingGroup.getValue().equals("")) {
			createFilter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, tfRoutingGroup.getValue());
		}
		if (tfInnerGroupNo.getValue() != null && !tfInnerGroupNo.getValue().equals("")) {
			createFilter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, tfInnerGroupNo.getValue());
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
