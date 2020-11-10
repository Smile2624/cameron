package com.ags.lumosframework.ui.view.inspectiontype;

import com.ags.lumosframework.pojo.InspectionType;
import com.ags.lumosframework.service.IInspectionTypeService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;



@SpringComponent
@Scope("prototype")
public class AddInspectionTypeDialog extends BaseDialog{

	private static final long serialVersionUID = -549195919746281784L;

	@I18Support(caption = "InspectionName", captionKey = "view.inspection.inspectionname")
    private TextField tfName = new TextField();

    @I18Support(caption = "InspectionDesc", captionKey = "view.inspection.inspectiondesc")
    private TextField tfDesc = new TextField();

    private Binder<InspectionType> binder = new Binder<>();

    private String caption;
    
    private String action;

    private InspectionType inspectionType;

    private IInspectionTypeService inspectionTypeService;

    public AddInspectionTypeDialog(IInspectionTypeService inspectionTypeService) {
        this.inspectionTypeService = inspectionTypeService;
    }

    public void setObject(InspectionType inspectionType) {
        String captionName = I18NUtility.getValue("view.inspection.caption", "InspectionType");
        if (inspectionType == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            action = "NEW";
            inspectionType = new InspectionType(null);
        }else{
        	action = "EDIT";
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.inspectionType = inspectionType;
        binder.readBean(inspectionType);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfName).asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(InspectionType::getInspection_name, InspectionType::setInspection_name);
        binder.bind(tfDesc, InspectionType::getInspection_desc, InspectionType::setInspection_desc);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(inspectionType);
        if("NEW".equals(action)) {
            String name = inspectionType.getInspection_name();
            InspectionType instance = inspectionTypeService.getByName(name);
            if(instance != null && instance.getInspection_name().length() > 0) {
            	NotificationUtils.notificationError(I18NUtility.getValue("view.inspection.addinspection.inspectionexist", "This Inspection Name Has Exist."));
            	return;
            }
        }

        InspectionType save = inspectionTypeService.save(inspectionType);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {}

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();

        tfName.setWidth("100%");
        tfDesc.setWidth("100%");

        vlContent.addComponents(tfName, tfDesc);
        return vlContent;
    }
}
