package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class RoutingDetailInfoDialog extends BaseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1983829823115741461L;
	
	@I18Support(caption = "ProductId", captionKey = "ProductRouting.ProductId")
	private LabelWithSamleLineCaption tfProductId = new LabelWithSamleLineCaption();

	@I18Support(caption = "ProductDesc", captionKey = "ProductRouting.ProductDesc")
	private LabelWithSamleLineCaption tfProductDesc = new LabelWithSamleLineCaption();
	
	@I18Support(caption = "RoutingGroup", captionKey = "ProductRouting.routingGroup")
	private LabelWithSamleLineCaption tfRoutingGroup = new LabelWithSamleLineCaption();
	
	@I18Support(caption = "InnerGroupNo", captionKey = "ProductRouting.innerGroupNo")
	private LabelWithSamleLineCaption tfInnerGroupNo = new LabelWithSamleLineCaption();
	
	@I18Support(caption = "RoutingDesc", captionKey = "ProductRouting.routingDesc")
	private LabelWithSamleLineCaption tfRoutingDesc = new LabelWithSamleLineCaption();
	
	@I18Support(caption = "OprationNo", captionKey = "ProductRouting.OprationNo")
	private LabelWithSamleLineCaption tfOprationNo = new LabelWithSamleLineCaption();

	@I18Support(caption = "OprationDesc", captionKey = "ProductRouting.OprationDesc")
	private LabelWithSamleLineCaption tfOprationDesc = new LabelWithSamleLineCaption();
	
	@I18Support(caption = "Attention", captionKey = "ProductRouting.Attention")
	private TextArea tfAttention = new TextArea();

	

	private Binder<ProductRouting> binder = new Binder<>();

	private String caption;

	private ProductRouting productRouting;

	private IProductRoutingService productRoutingService;

	public RoutingDetailInfoDialog(IProductRoutingService productRoutingService) {
		this.productRoutingService = productRoutingService;
	}

	public void setObject(ProductRouting productRouting) {
		String captionName = I18NUtility.getValue("QcInspection.view.RoutingInfo", "RoutingDetailInfo");
		this.caption = captionName;
		this.productRouting = productRouting;
		binder.readBean(productRouting);
	}

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, "100%","100%", true, true, callBack);
	}

	@Override
	protected void initUIData() {
		binder.forField(tfProductId)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getProductId, ProductRouting::setProductId);
		binder.forField(tfProductDesc)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getProductDesc, ProductRouting::setProductDesc);
		binder.bind(tfOprationNo, ProductRouting::getOprationNo, ProductRouting::setOprationNo);
		binder.bind(tfOprationDesc, ProductRouting::getOprationDesc, ProductRouting::setOprationDesc);
		binder.bind(tfAttention, ProductRouting::getAttention, ProductRouting::setAttention);
		binder.bind(tfRoutingGroup, ProductRouting::getRoutingGroup,ProductRouting::setRoutingGroup);
		binder.bind(tfInnerGroupNo, ProductRouting::getInnerGroupNo,ProductRouting::setInnerGroupNo);
		binder.bind(tfRoutingDesc, ProductRouting::getRoutingDesc,ProductRouting::setRoutingDesc);
	}

	@Override
	protected void okButtonClicked() throws Exception {
//		binder.writeBean(productRouting);
//		ProductRouting save = productRoutingService.save(productRouting);
//		result.setObj(save);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlContent = new VerticalLayout();
		vlContent.setSizeFull();
		
		
		tfProductId.setWidth("100%");
		tfProductDesc.setWidth("100%");
		tfOprationNo.setWidth("100%");
		tfOprationDesc.setWidth("100%");
		tfAttention.setWidth("100%");
		tfRoutingGroup.setWidth("100%");
		tfInnerGroupNo.setWidth("100%");
		tfRoutingDesc.setWidth("100%");
		vlContent.addComponents(tfProductId, tfProductDesc,tfRoutingGroup,tfInnerGroupNo,tfRoutingDesc,tfOprationNo,tfOprationDesc,tfAttention);
		return vlContent;
	}
	
}
