package com.ags.lumosframework.ui.view.receivinginspectionreport;

import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;

import java.io.File;

@SpringComponent
@Scope("prototype")
public class SelectPdfDialog extends BaseDialog {
    private String caption;
    private PurchasingOrderInfo purchasingOrderInfo;
    private IPurchasingOrderService purchasingOrderService;
    private File folder;
    private VerticalLayout vlContent = new VerticalLayout();
    private Button[] btns;

    public SelectPdfDialog(IPurchasingOrderService purchasingOrderService) {
        this.purchasingOrderService = purchasingOrderService;
    }

    public void setObject(PurchasingOrderInfo purchasingOrderInfo) {
        this.caption = "选择PDF文件";
        this.purchasingOrderInfo = purchasingOrderInfo;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        File folder = new File("D:\\Attachment\\" + purchasingOrderInfo.getSapInspectionLot());
        vlContent.removeAllComponents();
        int i = 0;
        if (folder.exists()) {
            for (final File f : folder.listFiles()) {
                if (f.isFile()) {
                    i = i + 1;
                }
            }
            if (i > 0) {
                btns = new Button[i];
                i = 0;
                for (final File f : folder.listFiles()) {
                    if (f.isFile()) {
                        btns[i] = new Button();
                        btns[i].setCaption(f.getName());
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
            NotificationUtils.notificationError("该检验任务没有PDF附件");
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
