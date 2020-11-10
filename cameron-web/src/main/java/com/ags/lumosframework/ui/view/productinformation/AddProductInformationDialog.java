package com.ags.lumosframework.ui.view.productinformation;

import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.service.IProductInformationService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddProductInformationDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = 7899007459584392989L;

    @I18Support(caption = "ProductId", captionKey = "ProductInformation.ProductId")
    private TextField tfProductId = new TextField();

    @I18Support(caption = "ProductVersionId", captionKey = "ProductInformation.ProductVersionId")
    private TextField tfProductVerId = new TextField();

    @I18Support(caption = "ProductDesc", captionKey = "ProductInformation.ProductDesc")
    private TextField tfProductDesc = new TextField();

    @I18Support(caption = "TemperatureRating", captionKey = "ProductInformation.TemperatureRating")
    private TextField tfTemperatureRating = new TextField();

    @I18Support(caption = "MaterialRating", captionKey = "ProductInformation.MaterialRating")
    private TextField tfMaterialRating = new TextField();

    @I18Support(caption = "PSLRating", captionKey = "ProductInformation.PSLRating")
    private TextField tfPSLRating = new TextField();

    @I18Support(caption = "Quality Plan", captionKey = "ProductInformation.QualityPlan")
    private TextField tfQualityPlan = new TextField();

    @I18Support(caption = "Quality Plan Rev", captionKey = "ProductInformation.QualityPlanRev")
    private TextField tfQualityPlanRev = new TextField();

    @I18Support(caption = "PressureInspectionProcedure", captionKey = "ProductInformation.PressureInspectionProcedure")
    private TextField tfPressureInspectionProcedure = new TextField();

    @I18Support(caption = "PressureInspectionProcedureVersion", captionKey = "ProductInformation.PressureInspectionProcedureVersion")
    private TextField tfPressureInspectionProcedureVersion = new TextField();

    @I18Support(caption = "Blowdown Torque", captionKey = "ProductInformation.BlowdownTorque")
    private TextField tfBlowdownTorque = new TextField();

    @I18Support(caption = "Gas Test", captionKey = "ProductInformation.GasTest")
    private TextField tfGasTest = new TextField();

    @I18Support(caption = "Gas Test Rev", captionKey = "ProductInformation.GasTestRev")
    private TextField tfGasTestRev = new TextField();

    @I18Support(caption = "LEVP", captionKey = "ProductInformation.Levp")
    private TextField tfLevp = new TextField();

    @I18Support(caption = "Gas Test Rev", captionKey = "ProductInformation.LevpRev")
    private TextField tfLevpRev = new TextField();

    @I18Support(caption = "PaintingSpecificationFile", captionKey = "ProductInformation.PaintingSpecificationFile")
    private TextField tfPaintingSpecificationFile = new TextField();

    @I18Support(caption = "PaintingSpecificationFileRev", captionKey = "ProductInformation.PaintingSpecificationFileRev")
    private TextField tfPaintingSpecificationFileRev = new TextField();


    private Binder<ProductInformation> binder = new Binder<>();

    private String caption;

    private ProductInformation productInformation;

    private IProductInformationService productInformationService;

    public AddProductInformationDialog(IProductInformationService productInformationService) {
        this.productInformationService = productInformationService;
    }

    public void setObject(ProductInformation productInformation) {
        String captionName = I18NUtility.getValue("ProductInformation.view.caption", "ProductInformation");
        if (productInformation == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            productInformation = new ProductInformation();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.productInformation = productInformation;
        binder.readBean(productInformation);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfProductId)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(ProductInformation::getProductId, ProductInformation::setProductId);
        binder.forField(tfProductVerId)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(ProductInformation::getProductVersionId, ProductInformation::setProductVersionId);
        binder.bind(tfProductDesc, ProductInformation::getProductDesc, ProductInformation::setProductDesc);
        binder.bind(tfTemperatureRating, ProductInformation::getTemperatureRating, ProductInformation::setTemperatureRating);
        binder.bind(tfMaterialRating, ProductInformation::getMaterialRating, ProductInformation::setMaterialRating);
        binder.bind(tfPSLRating, ProductInformation::getPSLRating, ProductInformation::setPSLRating);
        binder.bind(tfQualityPlan, ProductInformation::getQulityPlan, ProductInformation::setQualityPlan);
        binder.bind(tfQualityPlanRev, ProductInformation::getQulityPlanRev, ProductInformation::setQulityPlanRev);
        binder.bind(tfPressureInspectionProcedure, ProductInformation::getPressureInspectionProcedure, ProductInformation::setPressureInspectionProcedure);
        binder.bind(tfPressureInspectionProcedureVersion, ProductInformation::getPressureInspectionProcedureVersion, ProductInformation::setPressureInspectionProcedureVersion);
        binder.bind(tfBlowdownTorque, ProductInformation::getBlowdownTorque, ProductInformation::setBlowdownTorque);
        binder.bind(tfGasTest, ProductInformation::getGasTest, ProductInformation::setGasTest);
        binder.bind(tfGasTestRev, ProductInformation::getGasTestRev, ProductInformation::setGasTestRev);
        binder.bind(tfLevp, ProductInformation::getLevp, ProductInformation::setLevp);
        binder.bind(tfLevpRev, ProductInformation::getLevpRev, ProductInformation::setLevpRev);
        binder.bind(tfPaintingSpecificationFile, ProductInformation::getPaintingSpecificationFile, ProductInformation::setPaintingSpecificationFile);
        binder.bind(tfPaintingSpecificationFileRev, ProductInformation::getPaintingSpecificationFileRev, ProductInformation::setPaintingSpecificationFileRev);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(productInformation);
        ProductInformation save = productInformationService.save(productInformation);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        Panel pTemp = new Panel();
        pTemp.setWidth("100%");
        pTemp.setHeightUndefined();

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        pTemp.setContent(vlContent);

        tfProductId.setWidth("100%");
        tfProductVerId.setWidth("100%");
        tfProductDesc.setWidth("100%");
        tfTemperatureRating.setWidth("100%");
        tfMaterialRating.setWidth("100%");
        tfPSLRating.setWidth("100%");
        tfQualityPlan.setWidth("100%");
        tfQualityPlanRev.setWidth("100%");
        tfPressureInspectionProcedure.setWidth("100%");
        tfPressureInspectionProcedureVersion.setWidth("100%");
        tfBlowdownTorque.setWidth("100%");
        tfGasTest.setWidth("100%");
        tfGasTestRev.setWidth("100%");
        tfLevp.setWidth("100%");
        tfLevpRev.setWidth("100%");
        tfPaintingSpecificationFile.setWidth("100%");
        tfPaintingSpecificationFileRev.setWidth("100%");

        vlContent.addComponents(tfProductId, tfProductVerId, tfProductDesc, tfTemperatureRating,
                tfMaterialRating, tfPSLRating, tfQualityPlan, tfQualityPlanRev, tfPressureInspectionProcedure,
                tfPressureInspectionProcedureVersion, tfBlowdownTorque, tfGasTest, tfGasTestRev,
                tfLevp, tfLevpRev, tfPaintingSpecificationFile, tfPaintingSpecificationFileRev);
        return pTemp;
    }

}
