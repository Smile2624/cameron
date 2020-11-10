package com.ags.lumosframework.ui.view.inspectionplan;

import com.ags.lumosframework.pojo.InspectionPlan;
import com.ags.lumosframework.service.IInspectionPlanService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
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
public class AddInspectionPlanDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @I18Support(caption = "ProductNo", captionKey = "InspectionPlan.ProductNo")
    private TextField tfProductNo = new TextField();

    @I18Support(caption = "ProductDesc", captionKey = "InspectionPlan.ProductDesc")
    private TextField tfProductDesc = new TextField();

    @I18Support(caption = "InsPlan", captionKey = "InspectionPlan.InsPlan")
    private TextField tfInsPlan = new TextField();

    @I18Support(caption = "QCode", captionKey = "InspectionPlan.QCode")
    private TextField tfQCode = new TextField();

    @I18Support(caption = "ChemicalAnalysis", captionKey = "InspectionPlan.ChemicalAnalysis")
    private TextField tfChemicalAnalysis = new TextField();

    @I18Support(caption = "HeatDimension", captionKey = "InspectionPlan.HeatDimension")
    private TextField tfHeatDimension = new TextField();

    @I18Support(caption = "ForgHeatControl", captionKey = "InspectionPlan.ForgHeatControl")
    private TextField tfForgHeatControl = new TextField();

    @I18Support(caption = "MechanicalTest", captionKey = "InspectionPlan.MechanicalTest")
    private TextField tfMechanicalTest = new TextField();

    @I18Support(caption = "VolumNde", captionKey = "InspectionPlan.VolumNde")
    private TextField tfVolumNde = new TextField();

    @I18Support(caption = "TraceMark", captionKey = "InspectionPlan.TraceMark")
    private TextField tfTraceMark = new TextField();

    @I18Support(caption = "SurNde", captionKey = "InspectionPlan.SurNde")
    private TextField tfSurNde = new TextField();

    @I18Support(caption = "PartHardness", captionKey = "InspectionPlan.PartHardness")
    private TextField tfPartHardness = new TextField();

    @I18Support(caption = "VisualExam", captionKey = "InspectionPlan.VisualExam")
    private TextField tfVisualExam = new TextField();

    @I18Support(caption = "WeldOverlay", captionKey = "InspectionPlan.WeldOverlay")
    private TextField tfWeldOverlay = new TextField();

    @I18Support(caption = "WeldPrepNde", captionKey = "InspectionPlan.WeldPrepNde")
    private TextField tfWeldPrepNde = new TextField();

    @I18Support(caption = "FinalNde", captionKey = "InspectionPlan.FinalNde")
    private TextField tfFinalNde = new TextField();

    @I18Support(caption = "DimensionInpection", captionKey = "InspectionPlan.DimensionInpection")
    private TextField tfDimensionInpection = new TextField();

    @I18Support(caption = "CoatPaint", captionKey = "InspectionPlan.CoatPaint")
    private TextField tfCoatPaint = new TextField();

    @I18Support(caption = "CocElastomer", captionKey = "InspectionPlan.CocElastomer")
    private TextField tfCocElastomer = new TextField();

    @I18Support(caption = "CocCameron", captionKey = "InspectionPlan.CocCameron")
    private TextField tfCocCameron = new TextField();


    private Binder<InspectionPlan> binder = new Binder<>();

    private String caption;

    private InspectionPlan inspectionPlan;

    private IInspectionPlanService inspectionPlanService;

    public AddInspectionPlanDialog(IInspectionPlanService inspectionPlanService) {
        this.inspectionPlanService = inspectionPlanService;
    }

    public void setObject(InspectionPlan inspectionPlan) {
        String captionName = I18NUtility.getValue("InspectionPlan.view.caption", "InspectionPlan");
        if (inspectionPlan == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            inspectionPlan = new InspectionPlan();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.inspectionPlan = inspectionPlan;
        binder.readBean(inspectionPlan);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfProductNo)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(InspectionPlan::getProductNo, InspectionPlan::setProductNo);
        binder.bind(tfProductDesc, InspectionPlan::getProductDesc, InspectionPlan::setProductDesc);
        binder.bind(tfInsPlan, InspectionPlan::getInsPlan, InspectionPlan::setInsPlan);
        binder.bind(tfQCode, InspectionPlan::getQCode, InspectionPlan::setQCode);
        binder.bind(tfChemicalAnalysis, InspectionPlan::getChemicalAnalysis, InspectionPlan::setChemicalAnalysis);
        binder.bind(tfHeatDimension, InspectionPlan::getPreHeatDimension, InspectionPlan::setPreHeatDimension);
        binder.bind(tfForgHeatControl, InspectionPlan::getForgHeatControl, InspectionPlan::setForgHeatControl);
        binder.bind(tfMechanicalTest, InspectionPlan::getMechanicalTest, InspectionPlan::setMechanicalTest);
        binder.bind(tfVolumNde, InspectionPlan::getVolumNde, InspectionPlan::setVolumNde);
        binder.bind(tfTraceMark, InspectionPlan::getTraceMark, InspectionPlan::setTraceMark);
        binder.bind(tfSurNde, InspectionPlan::getSurNde, InspectionPlan::setSurNde);
        binder.bind(tfPartHardness, InspectionPlan::getPartHardness, InspectionPlan::setPartHardness);
        binder.bind(tfVisualExam, InspectionPlan::getVisualExam, InspectionPlan::setVisualExam);
        binder.bind(tfWeldOverlay, InspectionPlan::getWeldOverlay, InspectionPlan::setWeldOverlay);
        binder.bind(tfWeldPrepNde, InspectionPlan::getWeldPrepNde, InspectionPlan::setWeldPrepNde);
        binder.bind(tfFinalNde, InspectionPlan::getFinalNde, InspectionPlan::setFinalNde);
        binder.bind(tfDimensionInpection, InspectionPlan::getDimensionInpection, InspectionPlan::setDimensionInpection);
        binder.bind(tfCoatPaint, InspectionPlan::getCoatPaint, InspectionPlan::setCoatPaint);
        binder.bind(tfCocElastomer, InspectionPlan::getCocElastomer, InspectionPlan::setCocElastomer);
        binder.bind(tfCocCameron, InspectionPlan::getCocCameron, InspectionPlan::setCocCameron);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(inspectionPlan);
        InspectionPlan save = inspectionPlanService.save(inspectionPlan);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();

        tfProductNo.setWidth("100%");
        tfProductDesc.setWidth("100%");
        tfInsPlan.setWidth("100%");
        tfQCode.setWidth("100%");
        tfChemicalAnalysis.setWidth("100%");
        tfHeatDimension.setWidth("100%");
        tfForgHeatControl.setWidth("100%");
        tfMechanicalTest.setWidth("100%");
        tfVolumNde.setWidth("100%");
        tfTraceMark.setWidth("100%");
        tfSurNde.setWidth("100%");
        tfPartHardness.setWidth("100%");
        tfVisualExam.setWidth("100%");
        tfWeldOverlay.setWidth("100%");
        tfWeldPrepNde.setWidth("100%");
        tfFinalNde.setWidth("100%");
        tfDimensionInpection.setWidth("100%");
        tfCoatPaint.setWidth("100%");
        tfCocElastomer.setWidth("100%");
        tfCocCameron.setWidth("100%");

        vlContent.addComponents(tfProductNo, tfProductDesc, tfInsPlan, tfQCode,
                tfChemicalAnalysis, tfHeatDimension, tfForgHeatControl, tfMechanicalTest,
                tfVolumNde, tfTraceMark, tfSurNde, tfPartHardness, tfVisualExam, tfWeldOverlay,
                tfWeldPrepNde, tfFinalNde, tfDimensionInpection, tfCoatPaint, tfCocElastomer,
                tfCocCameron, tfCocCameron);
        return vlContent;
    }

}
