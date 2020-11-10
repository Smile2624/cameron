package com.ags.lumosframework.ui.view.caconfig;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
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
 * @date 2019/10/18 11:31
 */
@SpringComponent
@Scope("prototype")
public class ConfirmRoutingConfigDialog extends BaseDialog {


    TextField tfMin = new TextField("工序确认最小间隔时间（min）：");

    @Autowired
    private ICaConfigService caConfigService;

    private CaConfig caConfig = null;

    @Override
    protected void initUIData() {
        caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_ROUTING_MIN);
        tfMin.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    @Override
    protected void okButtonClicked() throws Exception {
        if (Strings.isNullOrEmpty(tfMin.getValue().trim())) {
            throw new PlatformException("最小时间值未填写！");
        } else {
            if (RegExpValidatorUtils.isIsPositive(tfMin.getValue().trim())) {
                if (caConfig == null) {
                    caConfig = new CaConfig();
                }
                caConfig.setConfigType(AppConstant.CA_CONFIRM_ROUTING_MIN);
                caConfig.setConfigValue(tfMin.getValue().trim());
                caConfigService.save(caConfig);
            } else {
                throw new PlatformException("最小时间值应为整数数值！");
            }
        }

    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
        vlRoot.addComponent(tfMin);
        return vlRoot;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, "工序确认时间配置", "300px", null, false, true, callBack);
    }
}
