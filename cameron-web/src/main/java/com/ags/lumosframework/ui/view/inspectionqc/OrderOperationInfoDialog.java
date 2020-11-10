package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.pojo.OrderRoutingConfirmInfo;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringComponent
@Scope("prototype")
public class OrderOperationInfoDialog extends BaseDialog{

	

	private static final long serialVersionUID = 1L;
	
	@I18Support(caption="工序操作注意事项" , captionKey="inspection。view.attention")
	private TextArea taOperationAttention = new TextArea();
	
	private Grid<OrderRoutingConfirmInfo> objectGrid = new Grid<OrderRoutingConfirmInfo>();
	
	List<OrderRoutingConfirmInfo> orderHistoryList;
	
	private String caption="工单操作信息列表";
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption,"100%","100%", true, true, callBack);
	}
	
	@Override
	protected void cancelButtonClicked() {
		
	}
	
	
	public void refreshData() {
		objectGrid.setDataProvider(DataProvider.ofCollection(orderHistoryList));
	}
	public void setObject(List<OrderRoutingConfirmInfo> orderHistory) {
		 if(orderHistory != null && orderHistory.size() > 0) {
			 this.orderHistoryList = orderHistory;
		 }else {
			 this.orderHistoryList = new ArrayList<OrderRoutingConfirmInfo>();
		 }
		 
	 }

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlContent = new VerticalLayout();
	       vlContent.setSizeFull();

			objectGrid.setSizeFull();
			objectGrid.setHeight("65%");
			objectGrid.setCaption("工单生产历史记录");
			objectGrid.addColumn(OrderRoutingConfirmInfo::getOperationNo).setCaption("工序编号");
			objectGrid.addColumn(OrderRoutingConfirmInfo::getOperationDesc).setCaption("工序描述");
			objectGrid.addColumn(OrderRoutingConfirmInfo::getOperationAttention).setCaption("工序操作要领").setWidth(100.0F);
			objectGrid.addColumn(OrderRoutingConfirmInfo::getConfirmBy).setCaption("确认人");
			objectGrid.addColumn(OrderRoutingConfirmInfo::getReConfirmBy).setCaption("二次确认人");
			
			objectGrid.addSelectionListener( event -> {
	            Optional<OrderRoutingConfirmInfo> optional = event.getFirstSelectedItem();
	            if (optional.isPresent()) {
	            	OrderRoutingConfirmInfo orderHistorySelected = optional.get();
	            	taOperationAttention.setValue(orderHistorySelected.getOperationAttention());
	            	taOperationAttention.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
	            } else {
	            	taOperationAttention.clear();
	            }
	        });
			taOperationAttention.setWidth("100%");
			taOperationAttention.setEnabled(false);
			objectGrid.setHeightByRows(7);
	        vlContent.addComponents(objectGrid);
	        vlContent.addComponent(taOperationAttention);
	        return vlContent;
	}

	@Override
	protected void initUIData() {
		
	}

	@Override
	protected void okButtonClicked() throws Exception {
		
	}

}
