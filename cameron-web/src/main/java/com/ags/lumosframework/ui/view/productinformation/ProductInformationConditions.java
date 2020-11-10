package com.ags.lumosframework.ui.view.productinformation;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductInformationEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IProductInformationService;
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
public class ProductInformationConditions extends BaseConditions implements IConditions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2446291066864653333L;

	// 查询区域控件
		
		private TextField tfProductId = new TextField();
		
		@I18Support(caption = "ProductInformation ProductVersionId", captionKey = "ProductInformation.ProductVersionId")
		private TextField tfProductVerId = new TextField();

//		@I18Support(caption = "Post Name", captionKey = "view.post.name")
//		private TextField tfName = new TextField(); 

		private HasValue<?>[] fields = { tfProductId, tfProductVerId };

		private Component[] components = { tfProductId };

		private Component[] temp = { tfProductVerId,tfProductVerId };

		FormLayout hlSearchPanel = new FormLayout();

		public ProductInformationConditions() {
			tfProductId.setPlaceholder(I18NUtility.getValue("ProductInformation.ProductId", "ProductId",new Object[0]));
			hlSearchPanel.setWidthUndefined();
			for (Component component : temp) {
//				com.vaadin.ui.Component componentRes = component;
				component.setWidthUndefined();
				hlSearchPanel.addComponent(component);
			}
			setElementsId();
		}

		private void setElementsId() {
			tfProductId.setId("tf_ProductId");
			tfProductVerId.setId("tf_ProductVerId");
		}

		@Override
		public Component[] getComponent() {
			return components;
		}

		@Override
		public EntityFilter getFilter() {
			IProductInformationService productInformationService = BeanManager.getService(IProductInformationService.class);
			EntityFilter createFilter = productInformationService.createFilter();
			if (tfProductId.getValue() != null && !tfProductId.getValue().equals("")) {
				createFilter.fieldEqualTo(ProductInformationEntity.PRODUCT_ID, tfProductId.getValue());
			}
			if (tfProductVerId.getValue() != null && !tfProductVerId.getValue().equals("")) {
				createFilter.fieldContains(ProductInformationEntity.PRODUCT_VERSION_ID, tfProductVerId.getValue());
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
