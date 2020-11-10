package com.ags.lumosframework.ui.view.inspectionplan;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.InspectionPlan;
import com.ags.lumosframework.service.IInspectionPlanService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Menu(caption = "InspectionPlan", captionI18NKey = "InspectionPlan.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 16)
@SpringView(name = "InspectionPlan", ui = CameronUI.class)
@Secured("InspectionPlan")
public class InspectionPlanView extends BaseView implements Button.ClickListener, IFilterableView {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //	@Secured(PermissionConstants.POST_ADD)
    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();
    //
//	@Secured(PermissionConstants.POST_EDIT)
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();
    //
//	@Secured(PermissionConstants.POST_DELETE)
    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();
    //
//	@Secured(PermissionConstants.POST_REFRESH)
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh};

    // 查询区域控件
    @I18Support(caption = "ProductId", captionKey = "ProductInformation.ProductId")
    private TextField tfProductId = new TextField();

    @I18Support(caption = "Search", captionKey = "common.search")
    private Button btnSearch = new Button();

    AbstractComponent[] fields = {tfProductId, btnSearch};

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    //	private IObjectListGrid<Post> objectGrid = new PaginationObjectListGrid<>(false);
    private IDomainObjectGrid<InspectionPlan> objectGrid = new PaginationDomainObjectList<>();

    @Autowired
    private IInspectionPlanService inspectionPlanService;

    @Autowired
    private AddInspectionPlanDialog addInspectionPlanDialog;

    public InspectionPlanView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        SearchPanelBuilder sp = new SearchPanelBuilder(BeanManager.getService(InspectionPlanConditions.class), objectGrid, this);
        hlToolBox.addComponent(sp);
        hlToolBox.setComponentAlignment(sp, Alignment.MIDDLE_RIGHT);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);

        objectGrid.addColumn(InspectionPlan::getProductNo).setCaption(I18NUtility.getValue("InspectionPlan.ProductNo", "ProductNo"));
        objectGrid.addColumn(InspectionPlan::getProductDesc).setCaption(I18NUtility.getValue("InspectionPlan.ProductDesc", "ProductDesc"));
        objectGrid.addColumn(InspectionPlan::getInsPlan).setCaption(I18NUtility.getValue("InspectionPlan.InsPlan", "InsPlan"));
        objectGrid.addColumn(InspectionPlan::getQCode).setCaption(I18NUtility.getValue("InspectionPlan.QCode", "QCode"));
        objectGrid.addColumn(InspectionPlan::getChemicalAnalysis).setCaption(I18NUtility.getValue("InspectionPlan.ChemicalAnalysis", "ChemicalAnalysis"));
        objectGrid.addColumn(InspectionPlan::getPreHeatDimension).setCaption(I18NUtility.getValue("InspectionPlan.HeatDimension", "HeatDimension"));
        objectGrid.addColumn(InspectionPlan::getForgHeatControl).setCaption(I18NUtility.getValue("InspectionPlan.ForgHeatControl", "ForgHeatControl"));
        objectGrid.addColumn(InspectionPlan::getMechanicalTest).setCaption(I18NUtility.getValue("InspectionPlan.MechanicalTest", "MechanicalTest"));
        objectGrid.addColumn(InspectionPlan::getVolumNde).setCaption(I18NUtility.getValue("InspectionPlan.VolumNde", "VolumNde"));
        objectGrid.addColumn(InspectionPlan::getTraceMark).setCaption(I18NUtility.getValue("InspectionPlan.TraceMark", "TraceMark"));
        objectGrid.addColumn(InspectionPlan::getSurNde).setCaption(I18NUtility.getValue("InspectionPlan.SurNde", "SurNde"));
        objectGrid.addColumn(InspectionPlan::getPartHardness).setCaption(I18NUtility.getValue("InspectionPlan.PartHardness", "PartHardness"));
        objectGrid.addColumn(InspectionPlan::getVisualExam).setCaption(I18NUtility.getValue("InspectionPlan.VisualExam", "VisualExam"));
        objectGrid.addColumn(InspectionPlan::getWeldOverlay).setCaption(I18NUtility.getValue("InspectionPlan.WeldOverlay", "WeldOverlay"));
        objectGrid.addColumn(InspectionPlan::getWeldPrepNde).setCaption(I18NUtility.getValue("InspectionPlan.WeldPrepNde", "WeldPrepNde"));
        objectGrid.addColumn(InspectionPlan::getFinalNde).setCaption(I18NUtility.getValue("InspectionPlan.FinalNde", "FinalNde"));
        objectGrid.addColumn(InspectionPlan::getDimensionInpection).setCaption(I18NUtility.getValue("InspectionPlan.DimensionInpection", "DimensionInpection"));
        objectGrid.addColumn(InspectionPlan::getCoatPaint).setCaption(I18NUtility.getValue("InspectionPlan.CoatPaint", "CoatPaint"));
        objectGrid.addColumn(InspectionPlan::getCocElastomer).setCaption(I18NUtility.getValue("InspectionPlan.CocElastomer", "CocElastomer"));
        objectGrid.addColumn(InspectionPlan::getCocCameron).setCaption(I18NUtility.getValue("InspectionPlan.CocCameron", "CocCameron"));
        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });
        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<InspectionPlan> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(IInspectionPlanService.class);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        objectGrid.refresh();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnAdd.equals(button)) {
            addInspectionPlanDialog.setObject(null);
            addInspectionPlanDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    objectGrid.refresh();
                }
            });
        } else if (btnEdit.equals(button)) {
            InspectionPlan inspectionPlan = (InspectionPlan) objectGrid.getSelectedObject();
            addInspectionPlanDialog.setObject(inspectionPlan);
            addInspectionPlanDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    InspectionPlan temp = (InspectionPlan) result.getObj();
                    objectGrid.refresh(temp);
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                inspectionPlanService.delete((InspectionPlan) objectGrid.getSelectedObject());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            objectGrid.refresh();
                        }
                    });
        } else if (btnRefresh.equals(button)) {
            objectGrid.refresh();
        }
    }

    @Override
    public void updateAfterFilterApply() {

    }

}
