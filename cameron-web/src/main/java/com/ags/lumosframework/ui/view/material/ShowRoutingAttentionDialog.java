package com.ags.lumosframework.ui.view.material;

import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.google.common.base.Strings;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class ShowRoutingAttentionDialog extends BaseDialog {

    private static final long serialVersionUID = -6976242993367375960L;

    private TextArea textArea = new TextArea();

    @Override
    protected void initUIData() {

    }

    public void setObject(String attention) {
        if (!Strings.isNullOrEmpty(attention)) {
            textArea.setValue(attention);
            textArea.addStyleName(ValoTheme.LABEL_H4 );
        }else{
            textArea.setValue("无");
            textArea.addStyleName(ValoTheme.LABEL_H4 );
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
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        textArea.setSizeFull();
        textArea.setHeight("900px");
        vlContent.addComponent(textArea);

        return vlContent;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, "注意事项", "100%", "100%", true, true, callBack);
    }
}
