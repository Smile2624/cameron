package com.ags.lumosframework.ui.view.caconfig;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.google.common.base.Strings;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peyton
 * @date 2019/10/18 11:31
 */
@SpringComponent
@Scope("prototype")
public class ReportSaveConfigDialog extends BaseDialog {


    TextField tfPath = new TextField("报告保存根路径：");

    @Autowired
    private ICaConfigService caConfigService;

    private CaConfig caConfig = null;

    @Override
    protected void initUIData() {
        caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        tfPath.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    @Override
    protected void okButtonClicked() throws Exception {
    	String path = tfPath.getValue().trim();
        if (Strings.isNullOrEmpty(path)) {
            throw new PlatformException("报告保存根路径未填写！");
        } else {
            //TODO 路径校验
        	if(!createDirectory(path)) {
        		 NotificationUtils.notificationError("路径创建失败，请检查目录是否存在");
        		 return;
        	}
            if (caConfig == null) {
                caConfig = new CaConfig();
            }
            caConfig.setConfigType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
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
        tfPath.setPlaceholder("例：D:\\fileName1\\fileName2");
        vlRoot.addComponent(tfPath);
        return vlRoot;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, "报告保存路径配置", "350px", null, false, true, callBack);
    }
    
    public boolean createDirectory(String path) {
    	boolean flag = true;
    	List<String> pathList = new ArrayList<String>();
    	pathList.add(path + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT);
    	pathList.add(path + AppConstant.MATERIAL_PREFIX + AppConstant.HARDNESS_INSPECTION_REPORT);
    	pathList.add(path + AppConstant.MATERIAL_PREFIX + AppConstant.RECEIVING_REPORT);
    	
    	pathList.add(path + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT);
    	pathList.add(path + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT);
    	pathList.add(path + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT);
    	pathList.add(path + AppConstant.PRODUCTION_PREFIX + AppConstant.FINAL_REPORT);
    	pathList.add(path + AppConstant.PRODUCTION_PREFIX + AppConstant.COC);
    	pathList.add(path + AppConstant.VENDOR);
    	pathList.add(path + AppConstant.PDF);
    	for(int index = 0; index < pathList.size() ; index++) {
    		File file = new File(pathList.get(index));
    		if(!file.exists()) {
    			if(!file.mkdirs()) {
    				flag = false;
    				break;
    			}
    			
    		}
    	}
    	return flag;
    }
}
