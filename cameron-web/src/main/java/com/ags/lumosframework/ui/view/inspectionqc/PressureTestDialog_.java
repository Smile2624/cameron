package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.File;

@SpringComponent
@Scope("prototype")
public class PressureTestDialog_ extends BaseDialog {
    private String caption;
    private ProductionOrder productionOrder;

    @Autowired
    private ICaConfigService caConfigService;

    private VerticalLayout vlContent = new VerticalLayout();
    private Button[] btns;

    public PressureTestDialog_() {
    }

    public void setObject(ProductionOrder productionOrder) {
        this.caption = "查看测压PDF";
        this.productionOrder = productionOrder;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }
        File folder = new File(caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT);
        vlContent.removeAllComponents();
        int i = 0;
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                    i = i + 1;
                }
            }
            Label lbSnResult = new Label();
            lbSnResult.setValue("Total: " + productionOrder.getProductNumber() + " ; Actual:" + i);
            vlContent.addComponent(lbSnResult);
            if (i > 0) {
                btns = new Button[i];
                i = 0;
                for (final File f : folder.listFiles()) {
                    if (f.getName().startsWith(productionOrder.getProductOrderId()) && f.getName().endsWith(".pdf")) {
                        btns[i] = new Button();
                        btns[i].setCaption(f.getName().replaceAll(".pdf", ""));
                        btns[i].setSizeFull();
                        btns[i].addClickListener(clickEvent -> {
                            Page.getCurrent().open("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=" + f.getAbsolutePath(), "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
                        });
                        vlContent.addComponent(btns[i]);
                        i = i + 1;
                    }
                }
            }
            showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
        } else {
            NotificationUtils.notificationError("该工单没有PDF测压报告");
        }
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
