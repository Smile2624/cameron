package com.ags.lumosframework.ui.view.caconfig;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.google.common.base.Strings;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * @author peyton
 * @date 2019/12/27 12:25
 */
@SpringComponent
@Scope("prototype")
public class PressTestSqliteJKConfigDialog extends BaseDialog {


    TextField tfPath = new TextField("压力数据存放目录（Sqlite井口）：");

    @Autowired
    private ICaConfigService caConfigService;

    private CaConfig caConfig = null;

    @Override
    protected void initUIData() {
        caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_JK);
        tfPath.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    @Override
    protected void okButtonClicked() throws Exception {
        if (Strings.isNullOrEmpty(tfPath.getValue().trim())) {
            throw new PlatformException("压力数据存放目录（Sqlite井口）未填写！");
        } else {
            //TODO 路径校验
            if (caConfig == null) {
                caConfig = new CaConfig();
            }
            caConfig.setConfigType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_JK);
            caConfig.setConfigValue(tfPath.getValue().trim());
            caConfigService.save(caConfig);
        }

    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
        tfPath.setWidth("90%");
        vlRoot.addComponent(tfPath);
        return vlRoot;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, "压力数据存放目录（Sqlite井口）配置", "350px", null, false, true, callBack);
    }
}
