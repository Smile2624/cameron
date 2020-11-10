package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.ags.lumosframework.web.vaadin.utility.VaadinUtils;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class SubOrderSelectDialog extends BaseDialog {
    private String caption;
    private List<ProductionOrder> productionOrder;

    @Autowired
    private ICaConfigService caConfigService;

    private VerticalLayout vlContent = new VerticalLayout();
    private Button[] btns;

    public SubOrderSelectDialog() {
    }

    public void setObject(List<ProductionOrder> productionOrder) {
        this.caption = "选择子工单";
        this.productionOrder = productionOrder;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        vlContent.removeAllComponents();
        int i = 0;
        btns=new Button[productionOrder.size()];
        for (final ProductionOrder p : productionOrder) {
            btns[i] = new Button();
            btns[i].setCaption(p.getProductOrderId());
            btns[i].setSizeFull();
            btns[i].addClickListener(clickEvent -> {
                String rootUrl = VaadinUtils.getRootURL();
                VaadinUtils.setPageLocation(rootUrl + "Cameron#!QCInspection/" + p.getProductOrderId());
                ((Window)this.getParent()).close();
            });
            vlContent.addComponent(btns[i]);
            i = i + 1;
        }
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);

    }

    @Override
    protected void initUIData() {

    }

    @Override
    protected void okButtonClicked() throws Exception {
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        vlContent.setSizeFull();
        return vlContent;
    }
}
