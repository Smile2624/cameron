package com.ags.lumosframework.ui.view.sparepart;

import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.service.ISparePartService;
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
public class AddSparePartDialog extends BaseDialog {

    private static final long serialVersionUID = -1688216774334155608L;

    @I18Support(caption = "SparePartNo", captionKey = "SparePart.SparePartNo")
    private TextField tfSparePartNo = new TextField();
    @I18Support(caption = "SparePartRev", captionKey = "SparePart.SparePartRev")
    private TextField tfSparePartRev = new TextField();
    @I18Support(caption = "SparePartDec", captionKey = "SparePart.SparePartDec")
    private TextField tfSparePartDec = new TextField();
    @I18Support(caption = "QaPlan", captionKey = "SparePart.QaPlan")
    private TextField tfQaPlan = new TextField();
    @I18Support(caption = "QaPlanRev", captionKey = "SparePart.QaPlanRev")
    private TextField tfQaPlanRev = new TextField();
    @I18Support(caption = "DrawNo", captionKey = "SparePart.DrawNo")
    private TextField tfDrawNo = new TextField();
    @I18Support(caption = "DrawRev", captionKey = "SparePart.DrawRev")
    private TextField tfDrawRev = new TextField();
    @I18Support(caption = "HardnessFile", captionKey = "SparePart.HardnessFile")
    private TextField tfHardnessFile = new TextField();
    @I18Support(caption = "HardnessRev", captionKey = "SparePart.HardnessRev")
    private TextField tfHardnessRev = new TextField();
    @I18Support(caption = "DNote", captionKey = "SparePart.DNote")
    private TextField tfDNote = new TextField();
    @I18Support(caption = "DNoteRev", captionKey = "SparePart.DNoteRev")
    private TextField tfDNoteRev = new TextField();
    @I18Support(caption = "Coating", captionKey = "SparePart.Coating")
    private TextField tfCoating = new TextField();
    @I18Support(caption = "CoatingRev", captionKey = "SparePart.CoatingRev")
    private TextField tfCoatingRev = new TextField();
    @I18Support(caption = "Welding", captionKey = "SparePart.Welding")
    private TextField tfWelding = new TextField();
    @I18Support(caption = "WeldingRev", captionKey = "SparePart.WeldingRev")
    private TextField tfWeldingRev = new TextField();
    @I18Support(caption = "PslLevelStand", captionKey = "SparePart.PslLevelStand")
    private TextField tfPslLevelStand = new TextField();
    @I18Support(caption = "ApiStand", captionKey = "SparePart.ApiStand")
    private TextField tfApiStand = new TextField();
//    @I18Support(caption = "PlmRev", captionKey = "SparePart.PlmRev")
//    private TextField tfPlmRev = new TextField();
//    @I18Support(caption = "PartRev", captionKey = "SparePart.PartRev")
//    private TextField tfPartRev = new TextField();

//    private AbstractComponent[] fields = {tfSparePartNo, tfSparePartRev, tfSparePartDec, tfQaPlan,
//            tfQaPlanRev, tfDrawNo, tfDrawRev, tfHardnessFile, tfHardnessRev, tfDNote, tfDNoteRev,
//            tfCoating, tfCoatingRev, tfWelding, tfWeldingRev, tfPslLevelStand, tfApiStand};//tfPlmRev, tfPartRev

    private Binder<SparePart> binder = new Binder<>();

    private String caption;

    private SparePart sparePart;

    private ISparePartService sparePartService;

    public AddSparePartDialog(ISparePartService sparePartService) {
        this.sparePartService = sparePartService;
    }

    public void setObject(SparePart sparePart) {
        String captionName = I18NUtility.getValue("Cameron.SparePart", "SparePart");
        if (sparePart == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            sparePart = new SparePart();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.sparePart = sparePart;
        binder.readBean(sparePart);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.LARGE_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        binder.forField(tfSparePartNo)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(SparePart::getSparePartNo, SparePart::setSparePartNo);
        binder.forField(tfSparePartRev)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(SparePart::getSparePartRev, SparePart::setSparePartRev);
        binder.bind(tfSparePartDec, SparePart::getSparePartDec, SparePart::setSparePartDec);
        binder.bind(tfQaPlan, SparePart::getQaPlan, SparePart::setQaPlan);
        binder.bind(tfQaPlanRev, SparePart::getQaPlanRev, SparePart::setQaPlanRev);
        binder.bind(tfDrawNo, SparePart::getDrawNo, SparePart::setDrawNo);
        binder.bind(tfDrawRev, SparePart::getDrawRev, SparePart::setDrawRev);
        binder.bind(tfHardnessFile, SparePart::getHardnessFile, SparePart::setHardnessFile);
        binder.bind(tfHardnessRev, SparePart::getHardnessRev, SparePart::setHardnessRev);
        binder.bind(tfDNote, SparePart::getDNote, SparePart::setDNote);
        binder.bind(tfDNoteRev, SparePart::getDNoteRev, SparePart::setDNoteRev);
        binder.bind(tfCoating, SparePart::getCoating, SparePart::setCoating);
        binder.bind(tfCoatingRev, SparePart::getCoatingRev, SparePart::setCoatingRev);
        binder.bind(tfWelding, SparePart::getWelding, SparePart::setWelding);
        binder.bind(tfWeldingRev, SparePart::getWeldingRev, SparePart::setWeldingRev);
        binder.bind(tfPslLevelStand, SparePart::getPslLevelStand, SparePart::setPslLevelStand);
        binder.bind(tfApiStand, SparePart::getApiStand, SparePart::setApiStand);
//        binder.bind(tfPlmRev, SparePart::getPlmRev, SparePart::setPlmRev);
//        binder.bind(tfPartRev, SparePart::getPartRev, SparePart::setPartRev);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(sparePart);
        SparePart save = sparePartService.save(sparePart);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {
    }

    @Override
    protected Component getDialogContent() {
//        ResponsiveLayout rl = new ResponsiveLayout();
//        ResponsiveRow addRow = rl.addRow();
//        addRow.setVerticalSpacing(ResponsiveRow.SpacingSize.SMALL, true);
//        addRow.setHorizontalSpacing(true);
//        for (AbstractComponent field : fields) {
//            field.setWidth("100%");
//            addRow.addColumn().withDisplayRules(12, 12, 6, 6).withComponent(field);
//
//        }
//        return rl;

        Panel pTemp = new Panel();
        pTemp.setWidth("100%");
        pTemp.setHeightUndefined();

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        pTemp.setContent(vlContent);

        tfSparePartNo.setWidth("100%");
        tfSparePartRev.setWidth("100%");
        tfSparePartDec.setWidth("100%");
        tfPslLevelStand.setWidth("100%");
        tfApiStand.setWidth("100%");
        tfQaPlan.setWidth("100%");
        tfQaPlanRev.setWidth("100%");
        tfDrawNo.setWidth("100%");
        tfDrawRev.setWidth("100%");
        tfHardnessFile.setWidth("100%");
        tfHardnessRev.setWidth("100%");
        tfDNote.setWidth("100%");
        tfDNoteRev.setWidth("100%");
        tfCoating.setWidth("100%");
        tfCoatingRev.setWidth("100%");
        tfWelding.setWidth("100%");
        tfWeldingRev.setWidth("100%");

        vlContent.addComponents(tfSparePartNo, tfSparePartRev, tfSparePartDec,
                tfPslLevelStand, tfApiStand, tfQaPlan, tfQaPlanRev, tfDrawNo,
                tfDrawRev, tfHardnessFile, tfHardnessRev, tfDNote, tfDNoteRev,
                tfCoating, tfCoatingRev, tfWelding, tfWeldingRev);
        return pTemp;
    }

}
