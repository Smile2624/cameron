package com.ags.lumosframework.ui.view.productrouting;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class AddProductRoutingDialog extends BaseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1983829823115741461L;
	
	@I18Support(caption = "ProductId", captionKey = "ProductRouting.ProductId")
	private TextField tfProductId = new TextField();

	@I18Support(caption = "ProductDesc", captionKey = "ProductRouting.ProductDesc")
	private TextField tfProductDesc = new TextField();
	
	@I18Support(caption = "OprationNo", captionKey = "ProductRouting.OprationNo")
	private TextField tfOprationNo = new TextField();

	@I18Support(caption = "OprationDesc", captionKey = "ProductRouting.OprationDesc")
	private TextField tfOprationDesc = new TextField();
	
	@I18Support(caption = "Attention", captionKey = "ProductRouting.Attention")
	private TextField tfAttention = new TextField();
	
	@I18Support(caption = "RoutingGroup", captionKey = "ProductRouting.RoutingGroup")
	private TextField tfRoutingGroup = new TextField();

	@I18Support(caption = "InnerGroupNo", captionKey = "ProductRouting.InnerGroupNo")
	private TextField tfInnerGroupNo = new TextField();
	
	@I18Support(caption = "RoutingDesc", captionKey = "ProductRouting.RoutingDesc")
	private TextField tfRoutingDesc = new TextField();
	
	private AbstractComponent[] fields = {tfProductId, tfProductDesc,tfOprationNo,tfOprationDesc,tfAttention,
			tfRoutingGroup,tfInnerGroupNo,tfRoutingDesc};
	

	private Binder<ProductRouting> binder = new Binder<>();

	private String caption;

	private ProductRouting productRouting;

	private IProductRoutingService productRoutingService;

	public AddProductRoutingDialog(IProductRoutingService productRoutingService) {
		this.productRoutingService = productRoutingService;
	}

	public void setObject(ProductRouting productRouting) {
		String captionName = I18NUtility.getValue("ProductRouting.view.caption", "ProductRouting");
		if (productRouting == null) {
			this.caption = I18NUtility.getValue("common.new", "New", captionName);
			productRouting = new ProductRouting();
		} else {
			this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
		}
		this.productRouting = productRouting;
		binder.readBean(productRouting);
	}

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void initUIData() {
		binder.forField(tfProductId)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getProductId, ProductRouting::setProductId);
		binder.forField(tfProductDesc)
//				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getProductDesc, ProductRouting::setProductDesc);
		binder.forField(tfOprationNo)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getOprationNo, ProductRouting::setOprationNo);
		binder.bind(tfOprationDesc, ProductRouting::getOprationDesc, ProductRouting::setOprationDesc);
		binder.bind(tfAttention, ProductRouting::getAttention, ProductRouting::setAttention);
		
		binder.forField(tfRoutingGroup)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getRoutingGroup, ProductRouting::setRoutingGroup);
		binder.forField(tfInnerGroupNo)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(ProductRouting::getInnerGroupNo, ProductRouting::setInnerGroupNo);
		binder.bind(tfRoutingDesc, ProductRouting::getRoutingDesc, ProductRouting::setRoutingDesc);

	}

	@Override
	protected void okButtonClicked() throws Exception {
		String routingGroup = tfRoutingGroup.getValue().trim();
		String innerGroupNo = tfInnerGroupNo.getValue().trim();
		String oprationNo = tfOprationNo.getValue().trim();
//		String caption = I18NUtility.getValue("common.new", "New", I18NUtility.getValue("ProductRouting.view.caption", "ProductRouting"));
		if("New ProductRouting".equals(caption) || "新增产品Routing".equals(caption)) {
			if (checkIsSaved(routingGroup,innerGroupNo,oprationNo)) {
				//NotificationUtils.notificationError()
				//I18NUtility.getValue("Common.ActivityTypeNotRepeated", "Activity type cannot be repeated")
	            throw new PlatformException("工艺组号："+routingGroup+"，组内编号："+innerGroupNo+"下的工序编号："+oprationNo+"已存在！");
	        }
		}
		binder.writeBean(productRouting);
		ProductRouting save = productRoutingService.save(productRouting);
		result.setObj(save);
	}

	@Override
	protected void cancelButtonClicked() {

	}
	
	public boolean checkIsSaved(String routingGroup,String innerGroupNo,String oprationNo) {
		boolean flag = false;
		if(!Strings.isNullOrEmpty(routingGroup) && !Strings.isNullOrEmpty(innerGroupNo) && !Strings.isNullOrEmpty(oprationNo)) {
			List<ProductRouting> productRoutingList = productRoutingService.getProductRoutingsByGroupNoOpration(routingGroup,innerGroupNo,oprationNo);
			if(productRoutingList!=null && productRoutingList.size()>0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	protected Component getDialogContent() {
//		VerticalLayout vlContent = new VerticalLayout();
//		vlContent.setSizeFull();
//		
//		
//		tfProductId.setWidth("100%");
//		tfProductDesc.setWidth("100%");
//		tfOprationNo.setWidth("100%");
//		tfOprationDesc.setWidth("100%");
//		tfAttention.setWidth("100%");
//		
//		tfRoutingGroup.setWidth("100%");
//		tfInnerGroupNo.setWidth("100%");
//		tfRoutingDesc.setWidth("100%");
//		
//		
//		
//
//		vlContent.addComponents(tfProductId, tfProductDesc,tfOprationNo,tfOprationDesc,tfAttention,
//				tfRoutingGroup,tfInnerGroupNo,tfRoutingDesc);
//		return vlContent;
		
		ResponsiveLayout rl = new ResponsiveLayout();
        ResponsiveRow addRow = rl.addRow();
        addRow.setVerticalSpacing(ResponsiveRow.SpacingSize.SMALL, true);
        addRow.setHorizontalSpacing(true);

        for (AbstractComponent field : fields) {
            field.setWidth("100%");
            addRow.addColumn().withDisplayRules(12, 12, 6, 6).withComponent(field);
        }
        return rl;
		
	}
	
}
