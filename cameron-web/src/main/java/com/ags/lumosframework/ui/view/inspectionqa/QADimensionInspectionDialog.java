package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.DimensionInspectionDialogResult;
import com.ags.lumosframework.pojo.DimensionInspectionResult;
import com.ags.lumosframework.service.IAssemblingService;
import com.ags.lumosframework.service.IDimensionInspectionService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
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
public class QADimensionInspectionDialog extends BaseDialog {
	
	private static final long serialVersionUID = 806476504703694847L;

	private Grid<DimensionInspectionDialogResult> grid = new Grid<DimensionInspectionDialogResult>();
	
	private String caption="来料尺寸检验数据查看";
	
	private List<DimensionInspectionDialogResult> dimensionInspectionResultTatalList;
	
	@Autowired
	private IAssemblingService assemblingService;
	
	@Autowired
	private IDimensionInspectionService dimensionInspectionService;
//	private QCInspectionView QCInspectionView = new QCInspectionView();
	
	private String dimensionInspectionResult;//来料尺寸检测结果
	
	HorizontalLayout hlLayout= new HorizontalLayout();
	
	@I18Support(caption="工单号",captionKey="")
    private LabelWithSamleLineCaption lblOrderNo = new LabelWithSamleLineCaption();
	
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption,"100%","100%", true, true, callBack);
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
					//获取sn对应的来料检验信息
					List<DimensionInspectionResult> dimensionInspectionResultList = dimensionInspectionService.getByMaterialSn(sn);
					if(dimensionInspectionResultList!=null && dimensionInspectionResultList.size()>0) {
						for(DimensionInspectionResult dimensionInspectionResult : dimensionInspectionResultList) {
							if(dimensionInspectionResult!=null) {
								//新建一个来料展示类
								DimensionInspectionDialogResult dimensionInspectionDialogResult = new DimensionInspectionDialogResult();
								dimensionInspectionDialogResult.setOrderSn(assembling.getSnBatch());
								dimensionInspectionDialogResult.setSn(sn);
								dimensionInspectionDialogResult.setIsPass(dimensionInspectionResult.getIsPass());
								dimensionInspectionResultTatalList.add(dimensionInspectionDialogResult);
							}
						}
					}
				}
			}
			
			
			grid.setDataProvider(DataProvider.ofCollection(dimensionInspectionResultTatalList));
		}
	}
	
	@Override
	protected void okButtonClicked() throws Exception {
//		if(dimensionInspectionResultTatalList!=null && dimensionInspectionResultTatalList.size()>0) {
//			if(checkDimensionInspectionResult(dimensionInspectionResultTatalList)) {
//				this.setDimensionInspectionResult(AppConstant.RESULT_OK);
//			}else {
//				this.setDimensionInspectionResult(AppConstant.RESULT_NG);
//			}
//		}else {
//			this.setDimensionInspectionResult("");
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
//        grid.addColumn(DimensionInspectionDialogResult::getIsPass).setCaption(I18NUtility.getValue("DimensionInspectionDialog.Result", "Result"));
        grid.addColumn(result-> {
            return result.getIsPass()? "通过" : "NG";
        }).setCaption(I18NUtility.getValue("DimensionInspectionDialog.Result", "Result"));
        
        
        grid.setHeight("600px");
        vlRoot.addComponent(grid);
        
        this.setSizeFull();
        return vlRoot;
	}

	@Override
	protected void initUIData() {
		
	}

	public String getDimensionInspectionResult() {
		return this.dimensionInspectionResult;
	}
	
	public void setDimensionInspectionResult(String dimensionInspectionResult) {
		this.dimensionInspectionResult = dimensionInspectionResult;
	}
	
	public boolean checkDimensionInspectionResult(List<DimensionInspectionDialogResult> dimensionInspectionResultTatalList) {
		boolean flag = true;
		if(dimensionInspectionResultTatalList!=null && dimensionInspectionResultTatalList.size()>0) {
			for(DimensionInspectionDialogResult dimensionInspectionDialogResult : dimensionInspectionResultTatalList) {
				if(!dimensionInspectionDialogResult.getIsPass()) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
