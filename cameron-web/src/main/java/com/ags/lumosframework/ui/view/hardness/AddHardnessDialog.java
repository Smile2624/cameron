package com.ags.lumosframework.ui.view.hardness;

import com.ags.lumosframework.pojo.Hardness;
import com.ags.lumosframework.service.IHardnessService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.validator.FloatRangeValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddHardnessDialog extends BaseDialog {

    private static final long serialVersionUID = -9020203949020045952L;

    @I18Support(caption = "HardnessName", captionKey = "Hardness.hardnessName")
    private TextField tfHardnessName = new TextField();
    @I18Support(caption = "HardnessStand", captionKey = "Hardness.hardnessStand")
    private TextField tfHardnessStand = new TextField();

    @I18Support(caption = "HardnessUpLimit", captionKey = "Hardness.hardnessUpLimit")
    private TextField tfHardnessUpLimit = new TextField();
    @I18Support(caption = "HardnessDownLimit", captionKey = "Hardness.hardnessDownLimit")
    private TextField tfHardnessDownLimit = new TextField();


    private AbstractComponent[] fields = { tfHardnessStand, tfHardnessUpLimit, tfHardnessDownLimit};//tfHardnessName,

    private Binder<Hardness> binder = new Binder<>();

    private String caption;

    private Hardness hardness;

    private IHardnessService hardnessService;

    public AddHardnessDialog(IHardnessService hardnessService) {
        this.hardnessService = hardnessService;
    }

    public void setObject(Hardness hardness) {
        String captionName = I18NUtility.getValue("Cameron.Hardness", "Hardness");
        if (hardness == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            hardness = new Hardness();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.hardness = hardness;
        binder.readBean(hardness);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
//        binder.forField(tfHardnessName)
//                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
//                .bind(Hardness::getHardnessName, Hardness::setHardnessName);
        binder.forField(tfHardnessStand)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(Hardness::getHardnessStand, Hardness::setHardnessStand);
        binder.forField(tfHardnessUpLimit)
                .withConverter(
                        new StringToFloatConverter(I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float value is allowed")))
                .withValidator(new FloatRangeValidator(
                        I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0f,
                        Float.MAX_VALUE))
                .bind(Hardness::getHardnessUpLimit, Hardness::setHardnessUpLimit);
        binder.forField(tfHardnessDownLimit)
                .withConverter(
                        new StringToFloatConverter(I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float value is allowed")))
                .withValidator(new FloatRangeValidator(
                        I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0f,
                        Float.MAX_VALUE))
                .bind(Hardness::getHardnessDownLimit, Hardness::setHardnessDownLimit);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(hardness);
        Hardness save = hardnessService.save(hardness);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {
    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();

        for (AbstractComponent field : fields) {
            field.setWidth("100%");
            vlContent.addComponent(field);
        }

        return vlContent;
    }
}
