package com.ags.lumosframework.ui.view.paintingspecification;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.PaintingSpecificationEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IPaintingSpecificationService;
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
public class PaintingSpecificationConditions extends BaseConditions implements IConditions {
	
	private static final long serialVersionUID = -883998094775501758L;

	//	private TextField tfProductOrderId = new TextField();
	private TextField tfPaintingSpecificationFile = new TextField();
	
	@I18Support(caption = "Primer", captionKey = "PaintingSpecification.Primer")
	private TextField tfPrimer = new TextField();

//	@I18Support(caption = "Post Name", captionKey = "view.post.name")
//	private TextField tfName = new TextField();

	private HasValue<?>[] fields = { tfPaintingSpecificationFile,tfPrimer };

	private Component[] components = { tfPaintingSpecificationFile };

	private Component[] temp = { tfPaintingSpecificationFile,tfPrimer };

	FormLayout hlSearchPanel = new FormLayout();

	public PaintingSpecificationConditions() {
		tfPaintingSpecificationFile.setPlaceholder(I18NUtility.getValue("PaintingSpecification.PaintingSpecificationFile", "PaintingSpecificationFile",new Object[0]));
		hlSearchPanel.setWidthUndefined();
		for (Component component : temp) {
//			com.vaadin.ui.Component componentRes = component;
			component.setWidthUndefined();
			hlSearchPanel.addComponent(component);
		}
		setElementsId();
	}

	private void setElementsId() {
		tfPaintingSpecificationFile.setId("tf_PaintingSpecificationFile");
		tfPrimer.setId("tf_Primer");
	}

	@Override
	public Component[] getComponent() {
		return components;
	}

	@Override
	public EntityFilter getFilter() {
		IPaintingSpecificationService paintingSpecificationService = BeanManager.getService(IPaintingSpecificationService.class);
		EntityFilter createFilter = paintingSpecificationService.createFilter();
		if (tfPaintingSpecificationFile.getValue() != null && !tfPaintingSpecificationFile.getValue().equals("")) {
			createFilter.fieldEqualTo(PaintingSpecificationEntity.PAINTING_SPECIFICATION_FILE, tfPaintingSpecificationFile.getValue());
		}
		if (tfPrimer.getValue() != null && !tfPrimer.getValue().equals("")) {
			createFilter.fieldContains(PaintingSpecificationEntity.PRIMER, tfPrimer.getValue());
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
