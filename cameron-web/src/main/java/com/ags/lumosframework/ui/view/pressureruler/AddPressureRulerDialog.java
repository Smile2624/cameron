package com.ags.lumosframework.ui.view.pressureruler;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.AuthorityType;
import com.ags.lumosframework.enums.PressureTypeEnum;
import com.ags.lumosframework.pojo.PressureRuler;
import com.ags.lumosframework.service.IPressureRulerService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;


@SpringComponent
@Scope("prototype")
public class AddPressureRulerDialog extends BaseDialog {

    private static final long serialVersionUID = -549195919746281784L;

    @I18Support(caption = "ProductNo", captionKey = "PressureRuler.ProductNo")
    private TextField tfProductNo = new TextField();

    @I18Support(caption = "PressureType", captionKey = "PressureRuler.PressureType")
    private ComboBox<PressureTypeEnum> cbPressureType = new ComboBox<>();

    @I18Support(caption = "TestPressureValue(psi)", captionKey = "PressureRuler.TestPressureValue")
    private TextField tfTestPressureValue = new TextField();
    @I18Support(caption = "MaxPressureValue(psi)", captionKey = "PressureRuler.MaxPressureValue")
    private TextField tfMaxPressureValue = new TextField();
    @I18Support(caption = "DifferencePressureValue(psi)", captionKey = "PressureRuler.DiffPressureValue")
    private TextField tfDifferencePressureValue = new TextField();

    @I18Support(caption = "FirstTime(min)", captionKey = "PressureRuler.FirstTime")
    private TextField tfFirstTime = new TextField();
    @I18Support(caption = "SecondTime(min)", captionKey = "PressureRuler.SecondTime")
    private TextField tfSecondTime = new TextField();
    @I18Support(caption = "ThirdTime(min)", captionKey = "PressureRuler.ThirdTime")
    private TextField tfThirdTime = new TextField();

    @I18Support(caption = "TorqueValue", captionKey = "PressureRuler.TorqueValue")
    private TextField tfTorqueValue = new TextField();

    private AbstractComponent[] fields = {tfProductNo, cbPressureType, tfTestPressureValue, tfFirstTime, tfMaxPressureValue,
            tfSecondTime, tfDifferencePressureValue, tfThirdTime, tfTorqueValue};

    private Binder<PressureRuler> binder = new Binder<>();

    private String caption;

    private String action;

    private PressureRuler pressureRuler;

    private IPressureRulerService pressureRulerService;

    public AddPressureRulerDialog(IPressureRulerService pressureRulerService) {
        this.pressureRulerService = pressureRulerService;
    }

    public void setObject(PressureRuler pressureRuler) {
        String captionName = I18NUtility.getValue("Cameron.PressureRuler", "PressureRuler");
        if (pressureRuler == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            action = "NEW";
            pressureRuler = new PressureRuler(null);
            tfProductNo.setEnabled(true);
            cbPressureType.setEnabled(true);
        } else {
            action = "EDIT";
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
            tfProductNo.setEnabled(false);
            cbPressureType.setEnabled(false);
        }
        this.pressureRuler = pressureRuler;
        binder.readBean(pressureRuler);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, "700px", null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        cbPressureType.setItems(PressureTypeEnum.values());
        cbPressureType.setItemCaptionGenerator(item -> {
            return I18NUtility.getValue(item.getKey(), item.getType());
        });


        binder.forField(tfProductNo).asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(PressureRuler::getProductNo, PressureRuler::setProductNo);
        binder.forField(cbPressureType).asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(PressureRuler::getPressureTypeEnum, PressureRuler::setPressureType);
        binder.forField(tfTestPressureValue)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyIntegerAllowed", "Only Integer is allowed")))
                .bind(PressureRuler::getTestPressureValue, PressureRuler::setTestPressureValue);
        binder.forField(tfMaxPressureValue)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getMaxPressureValue, PressureRuler::setMaxPressureValue);
        binder.forField(tfDifferencePressureValue)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getDifferencePressureValue, PressureRuler::setDifferencePressureValue);
        binder.forField(tfFirstTime)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getFirstTime, PressureRuler::setFirstTime);
        binder.forField(tfSecondTime)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getSecondTime, PressureRuler::setSecondTime);
        binder.forField(tfThirdTime)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getThirdTime, PressureRuler::setThirdTime);
        binder.forField(tfTorqueValue)
                .withConverter(new StringToDoubleConverter(I18NUtility.getValue("Common.OnlyDoubleAllowed", "Only Double is allowed")))
                .bind(PressureRuler::getTorqueValue, PressureRuler::setTorqueValue);

    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(pressureRuler);
        if ("NEW".equals(action)) {
            String productNo = pressureRuler.getProductNo();
            String type = pressureRuler.getPressureType();
            PressureRuler instance = pressureRulerService.getByProductNoAndPressureType(productNo, type);
            if (instance != null) {
                throw new PlatformException(I18NUtility.getValue("PressureRuler.RulerExist", "This ruler Has Exist！"));
            }
        }
        PressureRuler save = pressureRulerService.save(pressureRuler);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {
    }

    @Override
    protected Component getDialogContent() {
        ResponsiveLayout glLayout = new ResponsiveLayout();
        ResponsiveRow addRow = glLayout.addRow();
        addRow.setVerticalSpacing(ResponsiveRow.SpacingSize.SMALL, true);
        addRow.setHorizontalSpacing(true);

        for (AbstractComponent field : fields) {
            field.setWidth("100%");
            addRow.addColumn().withDisplayRules(12, 12, 6, 6).withComponent(field);
        }
        return glLayout;
    }
}
