package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.DimensionInspectionDialogResult;
import com.ags.lumosframework.pojo.HardnessTestReportItems;
import com.ags.lumosframework.service.IAssemblingService;
import com.ags.lumosframework.service.IHardnessTestReportItemsService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope("prototype")
public class QAHardnessTestReportDialog extends BaseDialog {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4230352062889988678L;

	private Grid<DimensionInspectionDialogResult> grid = new Grid<DimensionInspectionDialogResult>();
	
	private String caption="来料硬度检验数据查看";
	
	private List<DimensionInspectionDialogResult> dimensionInspectionResultTatalList;
	
	@Autowired
	private IAssemblingService assemblingService;
	
	@Autowired
	private IHardnessTestReportItemsService hardnessTestReportItemsService;
	
	private String hardnessTestReportItemsResult;//来料硬度检测结果
	
	HorizontalLayout hlLayout= new HorizontalLayout();
	
	@I18Support(caption="工单号",captionKey="")
    private LabelWithSamleLineCaption lblOrderNo = new LabelWithSamleLineCaption();
	
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, true, true, callBack);
	}

	public void setData(String orderNo) {
		
		
		lblOrderNo.setValue(orderNo);
		List<Assembling> assemblingList = assemblingService.getByOrderNo(orderNo);//获取工单中的SN集合
		
		if(assemblingList != null && assemblingList.size() > 0) {
			dimensionInspectionResultTatalList = new ArrayList<DimensionInspectionDialogResult>();
			for(int i=0;i<assemblingList.size();i++) {
				String sn = "";
				Assembling assembling = assemblingList.get(i);
				String single = assembling.getSerialNo();
				String batch = assembling.getBatch();
				sn = !Strings.isNullOrEmpty(single)?single:!Strings.isNullOrEmpty(batch)?batch:"";
				
				if(!Strings.isNullOrEmpty(sn)) {
					//新建一个来料展示类
					DimensionInspectionDialogResult dimensionInspectionDialogResult = new DimensionInspectionDialogResult();
					dimensionInspectionDialogResult.setOrderSn(assembling.getSnBatch());
					dimensionInspectionDialogResult.setSn(sn);
					HardnessTestReportItems hardnessTestReportItems = hardnessTestReportItemsService.getByPartNoSerialNo(sn);
					if(hardnessTestReportItems!=null) {
						dimensionInspectionDialogResult.setResult(hardnessTestReportItems.getResult());
						dimensionInspectionResultTatalList.add(dimensionInspectionDialogResult);
					}
					
				}
			}
			grid.setDataProvider(DataProvider.ofCollection(dimensionInspectionResultTatalList));
		}
	}
	
	@Override
	protected void okButtonClicked() throws Exception {
//		if(dimensionInspectionResultTatalList!=null && dimensionInspectionResultTatalList.size()>0) {
//			if(checkHardnessTestReportItemsResult(dimensionInspectionResultTatalList)) {
//				this.setHardnessTestReportItemsResult(AppConstant.RESULT_OK);
//			}else {
//				this.setHardnessTestReportItemsResult(AppConstant.RESULT_NG);
//			}
//		}else {
//			this.setHardnessTestReportItemsResult("");
//		}
	}
	
	@Override
	protected void cancelButtonClicked() {
		
	}

	@Override
	protected Component getDialogContent() {
		
		VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        hlLayout.setSizeFull();
        hlLayout.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlLayout.addComponent(lblOrderNo);
//        hlLayout.addComponent(lblSN);
//        hlLayout.addComponent(btnNext);
        vlRoot.addComponent(hlLayout);
        
        grid.setSizeFull();
        grid.addColumn(DimensionInspectionDialogResult::getOrderSn).setCaption(I18NUtility.getValue("DimensionInspectionDialog.OrderSn", "OrderSn"));
        grid.addColumn(DimensionInspectionDialogResult::getSn).setCaption(I18NUtility.getValue("DimensionInspectionDialog.SN", "SN"));
        grid.addColumn(DimensionInspectionDialogResult::getResult).setCaption(I18NUtility.getValue("DimensionInspectionDialog.Result", "Result"));
        
        
        grid.setHeight("200px");
        vlRoot.addComponent(grid);
        
        this.setSizeFull();
        return vlRoot;
	}

	@Override
	protected void initUIData() {
		
	}

	public String getHardnessTestReportItemsResult() {
		return this.hardnessTestReportItemsResult;
	}
	
	public void setHardnessTestReportItemsResult(String hardnessTestReportItemsResult) {
		this.hardnessTestReportItemsResult = hardnessTestReportItemsResult;
	}
	
	public boolean checkHardnessTestReportItemsResult(List<DimensionInspectionDialogResult> dimensionInspectionDialogResultList) {
		boolean flag = true;
		if(dimensionInspectionDialogResultList!=null && dimensionInspectionDialogResultList.size()>0) {
			for(DimensionInspectionDialogResult dimensionInspectionDialogResult : dimensionInspectionDialogResultList) {
				if(AppConstant.RESULT_NG.equals(dimensionInspectionDialogResult.getResult())) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
