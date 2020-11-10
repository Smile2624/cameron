package com.ags.lumosframework.ui.view.materialinspection;

import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class EditPurchasingOrderInfoDialog extends BaseDialog{

	private static final long serialVersionUID = 140335460714581836L;

	@I18Support(caption="PurchasingNo",captionKey="view.materialinspection.purchasingno")
	private LabelWithSamleLineCaption lblPurchasingNo = new LabelWithSamleLineCaption();
	
	@I18Support(caption="PurchasingItemNo",captionKey="view.materialinspection.purchasingitemno")
	private LabelWithSamleLineCaption lblPurchasingItemNo = new LabelWithSamleLineCaption();
	
	@I18Support(caption="MaterialRev",captionKey="view.materialinspection.materialrev")
	private TextField tfMaterilRev = new TextField();
	
	@I18Support(caption="Inspection Quantity",captionKey="view.materialinspection.inspectionquantity")
	private TextField tfInspectionQuantity = new TextField();
	
	private String caption = I18NUtility.getValue("view.materialinspection.modifypurchasinginfo", "Modify Purchasing Info");
	
	private Binder<PurchasingOrderInfo> binder = new Binder<>();
	
	private IPurchasingOrderService purchasingOrderService;
	
	VerticalLayout vlLayout = new VerticalLayout();

	private PurchasingOrderInfo purchasingOrderInfo;
	
	private int quantity = 0;
	
    @Autowired
    private UserService userService;

    @Autowired
    private IRoleService roleService;
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void cancelButtonClicked() {
		
	}

	public EditPurchasingOrderInfoDialog(IPurchasingOrderService purchasingOrderService) {
		this.purchasingOrderService = purchasingOrderService;
	}
	
	@Override
	protected Component getDialogContent() {
		vlLayout.addComponent(lblPurchasingNo);
		vlLayout.addComponent(lblPurchasingItemNo);
		vlLayout.addComponent(tfMaterilRev);
		vlLayout.addComponent(tfInspectionQuantity);
		lblPurchasingNo.setWidth("100%");
		lblPurchasingItemNo.setWidth("100%");
		tfMaterilRev.setWidth("100%");
		tfInspectionQuantity.setWidth("100%");
		String loginUserName = RequestInfo.current().getUserName();
        List<Role> role = userService.getByName(loginUserName).getRole();
        if(!role.contains(roleService.getByName("QCLeader"))) {
        	tfMaterilRev.setEnabled(false);
        	tfInspectionQuantity.setEnabled(false);
        }
		return vlLayout;
	}

	@Override
	protected void initUIData() {
		binder.bind(lblPurchasingNo, PurchasingOrderInfo::getPurchasingNo,PurchasingOrderInfo::setPurchasingNo);
		binder.bind(lblPurchasingItemNo, PurchasingOrderInfo::getPurchasingItemNo,PurchasingOrderInfo::setPurchasingItemNo);
		binder.bind(tfMaterilRev, PurchasingOrderInfo::getMaterialRev,PurchasingOrderInfo::setMaterialRev);
		binder.forField(tfInspectionQuantity).withConverter(new StringToIntegerConverter(I18NUtility.getValue("Common.OnlyIntegerAllowed", "Only Integer is allowed")))
		.withValidator(new IntegerRangeValidator(
                I18NUtility.getValue("view.material.quantitylimit", "Must Larger Than 0 And Litter Than Material Quantity"), 0,
                Integer.MAX_VALUE))
		.bind(PurchasingOrderInfo::getInspectionQuantity,PurchasingOrderInfo::setInspectionQuantity);
	}

	@Override
	protected void okButtonClicked() throws Exception {
		if(Integer.parseInt(tfInspectionQuantity.getValue().trim()) > purchasingOrderInfo.getMaterialQuantity()) {
			NotificationUtils.notificationError(I18NUtility.getValue("view.material.quantitylimit", "Must Larger Than 0 And Litter Than Material Quantity"));
			return;
		}
		binder.writeBean(purchasingOrderInfo);
		PurchasingOrderInfo instance = purchasingOrderService.save(purchasingOrderInfo);
		result.setObj(instance);
	}

	public void setObject(PurchasingOrderInfo purchasingOrderInfo) {
        if (purchasingOrderInfo == null) {
        	purchasingOrderInfo = new PurchasingOrderInfo(null);
        }else{
        }
        this.purchasingOrderInfo = purchasingOrderInfo;
        quantity = purchasingOrderInfo.getMaterialQuantity();
        binder.readBean(purchasingOrderInfo);		
	}

}
