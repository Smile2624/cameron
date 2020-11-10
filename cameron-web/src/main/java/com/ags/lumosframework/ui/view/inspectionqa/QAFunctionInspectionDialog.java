package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.pojo.FunctionDetectionResult;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.IFunctionDetectionResultService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class QAFunctionInspectionDialog extends BaseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1729419023125806310L;
	
private GridLayout gridLayout = new GridLayout(9,2);
	
	private TextField tfGageNo = new TextField();
	private TextField tfRuler= new TextField();
	private TextField tfOrderSN1 = new TextField();
	private TextField tfOrderSN2 = new TextField();
	private TextField tfOrderSN3 = new TextField();
	private TextField tfOrderSN4 = new TextField();
	private TextField tfOrderSN5 = new TextField();
	private TextField tfOrderSN6 = new TextField();
	private TextField tfOrderSN7 = new TextField();
	private TextField tfOrderSN8 = new TextField();
	private ComboBox<String> cbResult1 = new ComboBox<String>();
	private ComboBox<String> cbResult2 = new ComboBox<String>();
	private ComboBox<String> cbResult3 = new ComboBox<String>();
	private ComboBox<String> cbResult4 = new ComboBox<String>();
	private ComboBox<String> cbResult5 = new ComboBox<String>();
	private ComboBox<String> cbResult6 = new ComboBox<String>();
	private ComboBox<String> cbResult7 = new ComboBox<String>();
	private ComboBox<String> cbResult8 = new ComboBox<String>();
	
	private String orderNo;
	
	private boolean functionInspectionResult;//功能检查结果
	
	private List<FunctionDetectionResult> functionDetectionResultList;
	
	@Autowired
	private IFunctionDetectionResultService functionDetectionResultService;
	
	TextField[] textFields = new TextField[] {tfOrderSN1,tfOrderSN2,tfOrderSN3,tfOrderSN4,tfOrderSN5,tfOrderSN6,tfOrderSN7,tfOrderSN8};
	ComboBox[] fields = new ComboBox[] {cbResult1,cbResult2,cbResult3,cbResult4,cbResult5,cbResult6,cbResult7,cbResult8};
	String caption = "功能检验结果输入";
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, true, true, callBack);
	}
	
	@SuppressWarnings("unchecked")
	public void setData(ProductionOrder productionOrder,List<FunctionDetectionResult> functionDetectionResultList) {//
		if(productionOrder!=null) {
			this.orderNo = productionOrder.getProductOrderId();
		}
		
		if(functionDetectionResultList!=null && functionDetectionResultList.size()>0) {
			this.functionDetectionResultList = functionDetectionResultList;
			tfGageNo.setValue(functionDetectionResultList.get(0).getGageNO());
			tfGageNo.setReadOnly(true);
			for(int i=0;i<functionDetectionResultList.size();i++) {//FunctionDetectionResult functionDetectionResult: functionDetectionResultList
				String sn = functionDetectionResultList.get(i).getSn();
				String result = functionDetectionResultList.get(i).getFunctionInspectionResult();
				String caption = sn.split("_")[1];
				textFields[i].setValue(caption);
				textFields[i].setReadOnly(true);
				fields[i].setSelectedItem(result);
				fields[i].setEnabled(false);
			}
			
		}
	}
	
	@Override
	protected void okButtonClicked() throws Exception {
//		boolean flag = true;
//		if(functionDetectionResultList==null || functionDetectionResultList.size()<=0) {
//			for(int i=0;i<textFields.length;i++) {//TextField textField:textFields
//				String caption = textFields[i].getValue().trim();
//				String resultValue = (String) fields[i].getValue();
//				if(!Strings.isNullOrEmpty(caption) && !Strings.isNullOrEmpty(resultValue)) {
//					FunctionDetectionResult functionDetectionResult = new FunctionDetectionResult();
//					functionDetectionResult.setOrderNo(orderNo);
//					functionDetectionResult.setSn(orderNo+"_"+caption);//.substring(caption.length() -2,caption.length())
//					functionDetectionResult.setGageNO(tfRuler.getValue().trim());
//					functionDetectionResult.setFunctionInspectionResult(resultValue);
//					functionDetectionResultService.save(functionDetectionResult);
//					if(flag && ("NG".equals(resultValue))) {
//						flag = false;
//					}
//				}
//			}
//			this.setFunctionInspectionResult(flag);
//		}
	}
	
	@Override
	protected void cancelButtonClicked() {
		
	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlLayout = new VerticalLayout();
		gridLayout.addComponent(tfGageNo,0,0);
		tfGageNo.setPlaceholder("输入量具编号");
		gridLayout.addComponent(tfOrderSN1,1,0);
		gridLayout.addComponent(tfOrderSN2,2,0);
		gridLayout.addComponent(tfOrderSN3,3,0);
		gridLayout.addComponent(tfOrderSN4,4,0);
		gridLayout.addComponent(tfOrderSN5,5,0);
		gridLayout.addComponent(tfOrderSN6,6,0);
		gridLayout.addComponent(tfOrderSN7,7,0);
		gridLayout.addComponent(tfOrderSN8,8,0);
		
		gridLayout.addComponent(tfRuler,0,1);
		gridLayout.addComponent(cbResult1,1,1);
		gridLayout.addComponent(cbResult2,2,1);
		gridLayout.addComponent(cbResult3,3,1);
		gridLayout.addComponent(cbResult4,4,1);
		gridLayout.addComponent(cbResult5,5,1);
		gridLayout.addComponent(cbResult6,6,1);
		gridLayout.addComponent(cbResult7,7,1);
		gridLayout.addComponent(cbResult8,8,1);
		tfRuler.setValue(AppConstant.TORQUE_RULER);
		tfRuler.setEnabled(false);
		tfGageNo.setWidth("100px");
		tfRuler.setWidth("100px");
		
		for(TextField field : textFields) {
			field.setPlaceholder("序号");
			field.setWidth("60px");
		}
		for(ComboBox<String> field : fields ) {
			field.setWidth("60px");
			field.setItems("OK","NG");
		}
		vlLayout.addComponent(gridLayout);
		return vlLayout;
	}

	@Override
	protected void initUIData() {
		
	}
	
	public boolean getFunctionInspectionResult() {
		return this.functionInspectionResult;
	}
	
	public void setFunctionInspectionResult(boolean functionInspectionResult) {
		this.functionInspectionResult = functionInspectionResult;
	}

}
