package com.ags.lumosframework.ui.view.bomnameauthority;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.BomNameAuthority;
import com.ags.lumosframework.service.IBomNameAuthorityService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class AddBomNameAuthorityDialog extends BaseDialog {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1026688593386480121L;
	
	@I18Support(caption = "AuthorityType", captionKey = "BomNameAuthority.AuthorityType")
	ComboBox<String> cbAuthorityType = new ComboBox<String>();//规范类型

	
	@I18Support(caption = "NameEigenvalue", captionKey = "BomNameAuthority.NameEigenvalue")
	private TextField tfNameEigenvalue = new TextField();

	private Binder<BomNameAuthority> binder = new Binder<BomNameAuthority>();

	private String caption;

	private BomNameAuthority bomNameAuthority;

	private IBomNameAuthorityService bomNameAuthorityService;

	public AddBomNameAuthorityDialog(IBomNameAuthorityService bomNameAuthorityService) {
		this.bomNameAuthorityService = bomNameAuthorityService;
	}

	public void setObject(BomNameAuthority bomNameAuthority) {
		String captionName = I18NUtility.getValue("BomNameAuthority.view.caption", "BomNameAuthority");
		if (bomNameAuthority == null) {
			this.caption = I18NUtility.getValue("common.new", "New", captionName);
			bomNameAuthority = new BomNameAuthority();
		} else {
			this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
		}
		this.bomNameAuthority = bomNameAuthority;
		binder.readBean(bomNameAuthority);
	}

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void initUIData() {
		binder.forField(cbAuthorityType)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(BomNameAuthority::getAuthorityType, BomNameAuthority::setAuthorityType);
		binder.forField(tfNameEigenvalue)
		.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
		.bind(BomNameAuthority::getNameEigenvalue, BomNameAuthority::setNameEigenvalue);
	}

	@Override
	protected void okButtonClicked() throws Exception {
		binder.writeBean(bomNameAuthority);
		String authorityType = cbAuthorityType.getSelectedItem().get();
		String nameEigenvalue = tfNameEigenvalue.getValue().trim();
		
		if(bomNameAuthority==null) {
			BomNameAuthority bomNameAuthority = bomNameAuthorityService.getByTypeValue(authorityType,nameEigenvalue);
			if(bomNameAuthority!=null) {
				throw new PlatformException("规范类型："+authorityType+"，名称特征值："+nameEigenvalue+"已存在！");
			}
		}else {
			long id = bomNameAuthority.getId();
			List<BomNameAuthority> bomNameAuthorityList = bomNameAuthorityService.getByTypeValue(authorityType,nameEigenvalue,id);
			if(bomNameAuthorityList!=null && bomNameAuthorityList.size()>0) {
				throw new PlatformException("规范类型："+authorityType+"，名称特征值："+nameEigenvalue+"已存在！");
			}
		}
		
		BomNameAuthority save = bomNameAuthorityService.save(bomNameAuthority);
		result.setObj(save);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlContent = new VerticalLayout();
		vlContent.setSizeFull();
		
		cbAuthorityType.setWidth("100%");
		cbAuthorityType.setItems("H","QP","P");
		cbAuthorityType.setItemCaptionGenerator(
//				item -> {
//			return I18NUtility.getValue(item.getKey(), item.getType());
			
			new ItemCaptionGenerator<String>() {
	            private static final long serialVersionUID = 1L;

	            @Override
	            public String apply(String item) {
	            	return "H".equals(item)?"硬度":"QP".equals(item)?"质量计划":"P".equals(item)?"压力":"";
//	                return I18NUtility.getValue(item.getKey(), item.getType());
	            }
		});
		tfNameEigenvalue.setWidth("100%");

		vlContent.addComponents(cbAuthorityType,tfNameEigenvalue);
		return vlContent;
	}
}
