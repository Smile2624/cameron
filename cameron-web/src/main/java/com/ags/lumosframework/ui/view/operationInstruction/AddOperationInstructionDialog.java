package com.ags.lumosframework.ui.view.operationInstruction;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.OperationInstruction;
import com.ags.lumosframework.sdk.domain.Department;
import com.ags.lumosframework.sdk.service.api.IDepartmentService;
import com.ags.lumosframework.service.IOperationInstructionService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.uploadbutton.UploadButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope("prototype")
public class AddOperationInstructionDialog extends BaseDialog {

    @I18Support(caption = "Instruction_Name", captionKey = "view.operationinstruction.instructionname")
    private TextField tfInstructionName = new TextField();

    @I18Support(caption = "Instruction_Desc", captionKey = "view.operationinstruction.instructiondesc")
    private TextField tfInstructionDesc = new TextField();

    @I18Support(caption = "Instruction_Rev", captionKey = "view.operationinstruction.instructionrev")
    private TextField tfInstructionRev = new TextField();

    @I18Support(caption = "Modified_Date", captionKey = "view.operationinstruction.instructionmodifydate")
    private DateField dfModifiedDate = new DateField();

    @I18Support(caption = "Department(WI-)", captionKey = "view.operationinstruction.widepartment")
    private ComboBox<String> cbDepartment= new ComboBox();

    @I18Support(caption = "upload_pdf", captionKey = "view.operationinstruction.uploadpdf")
    private UploadButton btnUploadPdf = new UploadButton();
    private UploadFinishEvent uploadEvent = null;

    private CheckBox ckPdf = new CheckBox();

    private Binder<OperationInstruction> binder = new Binder<>();

    private String caption;

    private String action;
    //维护权限
    private List<String> roleAuthoritys = new ArrayList<>();

    private final String tocFacilityProcedures = "FacilityProcedures";
    private final String tocWorkInstructions = "WorkInstructions";
    private final String tocFacilityForms = "FacilityForms";

    private OperationInstruction opertionInstruction;

    @Autowired
    private IOperationInstructionService operationService;
    @Autowired
    private IDepartmentService departmentService;

    public AddOperationInstructionDialog(IOperationInstructionService operationService) {
        this.operationService = operationService;
    }

    public void setObject(OperationInstruction object, List<String> roleAuthorityList) {
        String captionName = I18NUtility.getValue("view.operationinstruction.caption", "OperationInstruction");
        if (object == null) {
            tfInstructionName.setReadOnly(false);
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            action = "NEW";
            object = new OperationInstruction(null);
        } else {
            tfInstructionName.setReadOnly(true);
            action = "EDIT";
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.roleAuthoritys = roleAuthorityList;
        this.opertionInstruction = object;
        binder.readBean(object);
        ckPdf.setValue(false);
    }

    @Override
    protected void initUIData() {
        List<Department> departments = departmentService.listDepartmentName();
        List<String> departmentNames = new ArrayList<>();
        departmentNames.add("-");
        departments.forEach(de->departmentNames.add(de.getName()));
        cbDepartment.setItems(departmentNames);
        binder.forField(tfInstructionName)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(OperationInstruction::getInstructionName, OperationInstruction::setInstructionName);
        binder.bind(tfInstructionDesc, OperationInstruction::getInstructionDesc, OperationInstruction::setInstructionDesc);
        binder.bind(tfInstructionRev, OperationInstruction::getInstructionRev, OperationInstruction::setInstructionRev);
        binder.forField(dfModifiedDate).bind(OperationInstruction::getModifiedDate1, OperationInstruction::setModifiedDate1);
        binder.forField(cbDepartment).bind(OperationInstruction::getWiDepartment, OperationInstruction::setWiDepartment);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(opertionInstruction);
        if ("NEW".equals(action)) {
            String name = tfInstructionName.getValue().trim();
            OperationInstruction instance = operationService.getByInspectionName(name);
            if (instance != null) {
                throw new PlatformException(I18NUtility.getValue("view.operationinstruction.instructionexist", "This Instruction Has Exist."));
            }
        }
        //SET 模块类型
        if (tfInstructionName.getValue().trim().equals(tocFacilityProcedures)
                || tfInstructionName.getValue().trim().equals(tocWorkInstructions)
                || tfInstructionName.getValue().trim().equals(tocFacilityForms)) {
            opertionInstruction.setTocType("Super");
            opertionInstruction.setWiDepartment("");
            ckPdf.setValue(true);
        }
        else if (ckPdf.getValue() == false) {
            throw new PlatformException("pdf文件未上传，请先上传！");
        }
        else if (tfInstructionName.getValue().trim().startsWith("CSI-")) {
            if (roleAuthoritys.contains(tocFacilityProcedures)) {
                opertionInstruction.setTocType(tocFacilityProcedures);
                opertionInstruction.setWiDepartment("");
            } else {
                throw new PlatformException("权限不足！无法操作该类文件！");
            }

        } else if (tfInstructionName.getValue().trim().startsWith("WI-")) {
            if (roleAuthoritys.contains(tocWorkInstructions)) {
                opertionInstruction.setTocType(tocWorkInstructions);
            } else {
                throw new PlatformException("权限不足！无法操作该类文件！");
            }
            if(Strings.isNullOrEmpty(cbDepartment.getValue())){
                throw new PlatformException("WI-文件的部门不能为空！");
            }
        } else {
            if (roleAuthoritys.contains(tocFacilityForms)) {
                opertionInstruction.setTocType(tocFacilityForms);
                opertionInstruction.setWiDepartment("");
            } else {
                throw new PlatformException("权限不足！无法操作该类文件！");
            }
        }

        OperationInstruction save = operationService.save(opertionInstruction);
        result.setObj(save);
        //更新父类日期
        OperationInstruction superIns = save.getSuper();
        superIns.setModifiedDate1(LocalDate.now());
        operationService.save(superIns);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        tfInstructionName.setWidth("100%");
        tfInstructionDesc.setWidth("100%");
        tfInstructionRev.setWidth("100%");
        dfModifiedDate.setWidth("100%");
        cbDepartment.setWidth("100%");

        FileUploader fileUploader = new FileUploader(event -> uploadEvent = event);
        btnUploadPdf.setIcon(VaadinIcons.UPLOAD);
        btnUploadPdf.setImmediateMode(true);
        btnUploadPdf.setReceiver(fileUploader);
        btnUploadPdf.addSucceededListener(fileUploader);
        btnUploadPdf.addFinishedListener(new Upload.FinishedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                try {
                    if (uploadEvent.getFileName().endsWith(".pdf") || uploadEvent.getFileName().endsWith(".PDF")) {
                        uploadPDF(uploadEvent.getUploadFile(), uploadEvent.getFileName());
                        NotificationUtils.notificationInfo("成功上传" + uploadEvent.getFileName());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError(e.getMessage());
                }
            }
        });
        HorizontalLayout hlPdf = new HorizontalLayout();
        hlPdf.addComponents(btnUploadPdf, ckPdf);
        ckPdf.setEnabled(false);

        vlContent.addComponents(tfInstructionName, tfInstructionDesc, tfInstructionRev, dfModifiedDate, cbDepartment,hlPdf);
        return vlContent;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();

        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    public void uploadPDF(InputStream fileStream, String fileName) throws IOException {
        System.out.println(fileName);
        if (tfInstructionName.getValue().trim().equals(fileName.split("\\.")[0])) {
            File targetFile = new File("D:\\Facility_Documents\\" + fileName);
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            ckPdf.setValue(true);
        } else {
            ckPdf.setValue(false);
            throw new PlatformException("上传pdf文件名不匹配！请检查！");
        }


    }
}
