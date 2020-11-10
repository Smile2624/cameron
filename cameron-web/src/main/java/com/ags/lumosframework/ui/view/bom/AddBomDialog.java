package com.ags.lumosframework.ui.view.bom;

import com.ags.lumosframework.enums.RetrospectType;
import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.service.IBomService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddBomDialog extends BaseDialog {

    private static final long serialVersionUID = -1688216774334155608L;

    @I18Support(caption = "ProductNo", captionKey = "Bom.ProductNo")
    private TextField tfProductNo = new TextField();
    @I18Support(caption = "ProductRev", captionKey = "Bom.ProductRev")
    private TextField tfProductRev = new TextField();

    @I18Support(caption = "ItemNo", captionKey = "Bom.ItemNo")
    private TextField tfItemNo = new TextField();

    @I18Support(caption = "SparePartNo", captionKey = "SparePart.SparePartNo")
    private TextField tfSparePartNo = new TextField();
    @I18Support(caption = "SparePartRev", captionKey = "SparePart.SparePartRev")
    private TextField tfSparePartRev = new TextField();
    @I18Support(caption = "SparePartDec", captionKey = "Bom.PartQuantity")
    private TextField tfPartQuantity = new TextField();

    @I18Support(caption = "IsRetrospect", captionKey = "Bom.IsRetrospect")
    private ComboBox<Boolean> cbIsRetrospect = new ComboBox<Boolean>();
    @I18Support(caption = "ApiStand", captionKey = "Bom.RetrospectType")
    private ComboBox<String> cbRetrospectType = new ComboBox<String>();

    private AbstractComponent[] fields = {tfProductNo, tfProductRev, tfItemNo, tfSparePartNo,
            tfSparePartRev, tfPartQuantity, cbIsRetrospect, cbRetrospectType};

    private Binder<Bom> binder = new Binder<>();

    private String caption;

    private Bom bom;

    private IBomService bomService;

    public AddBomDialog(IBomService bomService) {
        this.bomService = bomService;
    }

    public void setObject(Bom bom) {
        String captionName = I18NUtility.getValue("Cameron.Bom", "Bom");
        if (bom == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            bom = new Bom();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.bom = bom;
        binder.readBean(bom);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        cbIsRetrospect.setEmptySelectionAllowed(false);
        cbIsRetrospect.setTextInputAllowed(false);
        cbIsRetrospect.setItems(true, false);
        cbIsRetrospect.setItemCaptionGenerator(item -> item == true ? "是" : "否");
        cbIsRetrospect.addSelectionListener(new SingleSelectionListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<Boolean> event) {
                if (event.getValue()) {
                    cbRetrospectType.setEmptySelectionAllowed(false);
                }else{
                    cbRetrospectType.setValue("");
                    cbRetrospectType.setEmptySelectionAllowed(true);
                }
            }
        });

        cbRetrospectType.setTextInputAllowed(false);
        cbRetrospectType.setItems(RetrospectType.SINGLE.getType(), RetrospectType.BATCH.getType());
        cbRetrospectType.setItemCaptionGenerator(item -> {
            if(RetrospectType.SINGLE.getType().equals(item)){
                return "单件";
            }else if(RetrospectType.BATCH.getType().equals(item)){
                return "批次";
            }
            return "";
        });


        binder.forField(tfProductNo)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(Bom::getProductNo, Bom::setProductNo);
        binder.forField(tfProductRev)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(Bom::getProductRev, Bom::setProductRev);
        binder.bind(tfItemNo, Bom::getItemNo, Bom::setItemNo);
        binder.bind(tfSparePartNo, Bom::getPartNo, Bom::setPartNo);
        binder.bind(tfSparePartRev, Bom::getPartRev, Bom::setPartRev);
        binder.forField(tfPartQuantity)
                .withConverter(
                        new StringToIntegerConverter(I18NUtility.getValue("Common.OnlyIntegerAllowed", "Only Integer is allowed")))
                .withValidator(new IntegerRangeValidator(
                        I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0,
                        Integer.MAX_VALUE))
                .bind(Bom::getPartQuantity, Bom::setPartQuantity);
        binder.bind(cbIsRetrospect, Bom::isRetrospect, Bom::setRetrospect);
        binder.bind(cbRetrospectType, Bom::getRetrospectType, Bom::setRetrospectType);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(bom);
        Bom save = bomService.save(bom);
        result.setObj(save);
    }

    @Override
    protected void cancelButtonClicked() {
    }

    @Override
    protected Component getDialogContent() {
        ResponsiveLayout rl = new ResponsiveLayout();
        ResponsiveRow addRow = rl.addRow();
        addRow.setVerticalSpacing(ResponsiveRow.SpacingSize.SMALL, true);
        addRow.setHorizontalSpacing(true);

        for (AbstractComponent field : fields) {
            field.setWidth("100%");
            addRow.addColumn().withDisplayRules(12, 12, 6, 6).withComponent(field);
        }
        return rl;
    }
}
