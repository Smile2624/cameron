package com.ags.lumosframework.ui.view.operationInstruction;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.OperationInstruction;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.service.IOperationInstructionService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Menu(caption = "OperationInstruction", captionI18NKey = "view.operationinstruction.caption", iconPath = "images/icon/text-blob.png", groupName = "CommonFunction", order = 3)
@SpringView(name = "OperationInstruction", ui = CameronUI.class)
public class OperationInstructionView extends BaseView implements Button.ClickListener {

    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();

    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();

    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    @I18Support(caption = "View", captionKey = "common.view")
    private Button btnPreview = new Button();

    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh, btnPreview};

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private TreeGrid<OperationInstruction> objectGrid = new TreeGrid<>();

    private final String tocFacilityProcedures = "FacilityProcedures";
    private final String tocWorkInstructions = "WorkInstructions";
    private final String tocFacilityForms = "FacilityForms";
    //角色权限
    private List<String> roleAuthorityList = new ArrayList<>();

    private TextField tfInstructionName = new TextField();
    private Button btnSearch = new Button();

    @Autowired
    private AddOperationInstructionDialog addInstructionDialog;

    @Autowired
    private IOperationInstructionService instructionService;

    @Autowired
    private UserService userService;

    public OperationInstructionView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);

        hlTemp.addComponents(tfInstructionName, btnSearch);
        tfInstructionName.setPlaceholder(I18NUtility.getValue("view.operationinstruction.instructionname", "InstructionName"));
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("primary");
        btnSearch.addClickListener(this);
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);
        btnPreview.setIcon(VaadinIcons.FILE_O);

        objectGrid.setSizeFull();
        objectGrid.addColumn(OperationInstruction::getInstructionName).setCaption(I18NUtility.getValue("view.operationinstruction.instructionname", "Instruction_ame"));
        objectGrid.addColumn(OperationInstruction::getInstructionDesc).setCaption(I18NUtility.getValue("view.operationinstruction.instructiondesc", "InstructionDesc"));
        objectGrid.addColumn(OperationInstruction::getInstructionRev).setCaption(I18NUtility.getValue("view.operationinstruction.instructionrev", "InstructionRev"));
        objectGrid.addColumn(OperationInstruction::getModifiedDate).setCaption(I18NUtility.getValue("view.operationinstruction.instructionmodifydate", "ModifiedDate"));
        objectGrid.addColumn(OperationInstruction::getWiDepartment).setCaption(I18NUtility.getValue("view.operationinstruction.widepartment", "WiDepartment"));
        objectGrid.addItemClickListener(event -> {
            OperationInstruction object = event.getItem();
            if (event.getMouseEventDetails().isDoubleClick()) {
                String fileName = "Super".equals(object.getTocType()) ? "TOC-" + object.getInstructionName() + ".pdf" : object.getInstructionName() + ".pdf";
//            Page.getCurrent().open("http://localhost:8080/CameronPDFView/viewpdf?filename=D:\\Facility_Documents\\" + fileName, "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
                Page.getCurrent().open("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\Facility_Documents\\" + fileName, "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
            }
        });

        objectGrid.addSelectionListener(event -> {
            Optional<OperationInstruction> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                OperationInstruction op = optional.get();
                if (roleAuthorityList.contains(op.getTocType()) || roleAuthorityList.contains(op.getInstructionName())) {
                    setButtonStatus(true);
                } else {
                    setButtonStatus(false);
                }
            } else {
                setButtonStatus(false);
            }
        });

        vlRoot.addComponent((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(boolean enable) {

        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
        btnPreview.setEnabled(enable);
    }

    @Override
    protected void init() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        btnAdd.setEnabled(true);
        setButtonStatus(false);
        refreshGrid();

        //获取权限
        String loginUserName = RequestInfo.current().getUserName();
        List<Role> role = userService.getByName(loginUserName).getRole();
        List<String> roleNames = new ArrayList<>();
        role.forEach(r -> roleNames.add(r.getRoleName()));
        roleAuthorityList = new ArrayList<>();
        if ("admin".equals(loginUserName)) {
            roleAuthorityList.add(tocFacilityProcedures);
            roleAuthorityList.add(tocWorkInstructions);
            roleAuthorityList.add(tocFacilityForms);
        } else {
            if (roleNames.contains("TOC-FacilityProcedures")) {
                roleAuthorityList.add(tocFacilityProcedures);
            }
            if (roleNames.contains("TOC-WorkInstructions")) {
                roleAuthorityList.add(tocWorkInstructions);
            }
            if (roleNames.contains("TOC-FacilityForms")) {
                roleAuthorityList.add(tocFacilityForms);
            }
        }

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button btn = event.getButton();
        btn.setEnabled(true);
        if (btn.equals(btnSearch)) {
            refreshGrid();
        } else if (btn.equals(btnAdd)) {
            addInstructionDialog.setObject(null, roleAuthorityList);
            addInstructionDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    refreshGrid();
                    OperationInstruction newObj = (OperationInstruction) result.getObj();
                    if (newObj != null) {
                        objectGrid.select(newObj);
                    }
                }
            });
        } else if (btn.equals(btnEdit)) {
            OperationInstruction object = objectGrid.asSingleSelect().getValue();
            addInstructionDialog.setObject(object, roleAuthorityList);
            addInstructionDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    refreshGrid();
                    OperationInstruction editObj = (OperationInstruction) result.getObj();
                    if (editObj != null) {
                        objectGrid.select(editObj);
                    }
                }
            });
        } else if (btn.equals(btnDelete)) {
            OperationInstruction object = objectGrid.asSingleSelect().getValue();
            if ("Super".equals(object.getTocType())) {
                NotificationUtils.notificationError("该条记录不可删除！");
                return;
            }
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                object.setDeleteFlag(true);
                                instructionService.save(object);
                                OperationInstruction op = object.getSuper();
                                op.setModifiedDate1(LocalDate.now());
                                instructionService.save(op);
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            refreshGrid();
                        }
                    });
        } else if (btn.equals(btnPreview)) {
            OperationInstruction object = objectGrid.asSingleSelect().getValue();
            String fileName = "Super".equals(object.getTocType()) ? "TOC-" + object.getInstructionName() + ".pdf" : object.getInstructionName() + ".pdf";
//            Page.getCurrent().open("http://localhost:8080/CameronPDFView/viewpdf?filename=D:\\Facility_Documents\\" + fileName, "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
            Page.getCurrent().open("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=D:\\Facility_Documents\\" + fileName, "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
        } else if (btn.equals(btnRefresh)) {
            tfInstructionName.clear();
            refreshGrid();
        }
    }

    private void refreshGrid() {
        List<OperationInstruction> list = instructionService.getSuperInstructions();
        objectGrid.setItems(list, bean->bean.getChild(tfInstructionName.getValue().trim(),true));

    }
}