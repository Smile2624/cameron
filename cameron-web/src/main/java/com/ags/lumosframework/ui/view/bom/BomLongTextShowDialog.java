package com.ags.lumosframework.ui.view.bom;

import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.google.common.base.Strings;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class BomLongTextShowDialog extends BaseDialog {

    @I18Support(caption = "Attention", captionKey = "bom.view.attention")
    private TextArea taLongText = new TextArea();

    String caption = "";
    @Override
    protected void initUIData() {

    }

    public void setObject(Bom bom){
        caption = I18NUtility.getValue("bom.view.title","Attention Information");
        taLongText.setStyleName(ValoTheme.LABEL_LARGE);
        if(!Strings.isNullOrEmpty(bom.getLongText())){
            taLongText.setValue(bom.getLongText());
        }else{
            taLongText.setValue("无");
        }
    }
    @Override
    protected void okButtonClicked() throws Exception {

    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        HorizontalLayout hlLayout = new HorizontalLayout();
        hlLayout.setSizeFull();
        taLongText.setSizeFull();
        taLongText.setHeight("900px");
        hlLayout.addComponent(taLongText);
        return hlLayout;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, "100%", "100%", true, true, callBack);
    }
}
