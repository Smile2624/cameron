package com.ags.lumosframework.ui.view.paintingspecification;

import com.ags.lumosframework.pojo.PaintingSpecification;
import com.ags.lumosframework.service.IPaintingSpecificationService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddPaintingSpecificationDialog extends BaseDialog {
	
	private static final long serialVersionUID = 262656253069731269L;

//	@I18Support(caption = "ESignatureLoGoType", captionKey = "ElectronicSignatureLoGoMaintain.ESignatureLoGoType")
//	ComboBox<String> cbESignatureLoGoType = new ComboBox<String>();//电子签名LoGo类型
//	
//	@I18Support(caption = "导入图片", captionKey = "")
//    private UploadButton upload = new UploadButton();
	
	@I18Support(caption = "PaintingSpecificationFile", captionKey = "PaintingSpecification.PaintingSpecificationFile")
	private TextField tfPaintingSpecificationFile = new TextField();

	@I18Support(caption = "Revision", captionKey = "PaintingSpecification.Revision")
	private TextField tfRev = new TextField();

	@I18Support(caption = "Primer", captionKey = "PaintingSpecification.Primer")
	private TextField tfPrimer = new TextField();
	
	@I18Support(caption = "Intermediate", captionKey = "PaintingSpecification.Intermediate")
	private TextField tfIntermediate = new TextField();
	
	@I18Support(caption = "Finals", captionKey = "PaintingSpecification.Finals")
	private TextField tfFinals = new TextField();

	@I18Support(caption = "Total", captionKey = "PaintingSpecification.Total")
	private TextField tfTotal = new TextField();
	
	@I18Support(caption = "Color", captionKey = "PaintingSpecification.Color")
	private TextField tfColor = new TextField();
	
	@I18Support(caption = "Humidity", captionKey = "PaintingSpecification.Humidity")
	private TextField tfHumidity = new TextField();
	
	@I18Support(caption = "AboveDewPoint", captionKey = "PaintingSpecification.AboveDewPoint")
	private TextField tfAboveDewPoint = new TextField();

	private Binder<PaintingSpecification> binder = new Binder<PaintingSpecification>();

	private String caption;

	private PaintingSpecification paintingSpecification;

	private IPaintingSpecificationService paintingSpecificationService;
	
	private AbstractComponent[] fields = {tfPaintingSpecificationFile,tfRev,tfPrimer,tfIntermediate,
											tfFinals,tfTotal,tfColor,tfHumidity,tfAboveDewPoint};

	public AddPaintingSpecificationDialog(IPaintingSpecificationService paintingSpecificationService) {
		this.paintingSpecificationService = paintingSpecificationService;
	}

	public void setObject(PaintingSpecification paintingSpecification) {
		String captionName = I18NUtility.getValue("PaintingSpecification.view.caption", "PaintingSpecification");
		if (paintingSpecification == null) {
			this.caption = I18NUtility.getValue("common.new", "New", captionName);
			paintingSpecification = new PaintingSpecification();
		} else {
			this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
		}
		this.paintingSpecification = paintingSpecification;
		binder.readBean(paintingSpecification);
	}

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void initUIData() {
		System.out.println("init");
		binder.forField(tfPaintingSpecificationFile)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(PaintingSpecification::getPaintingSpecificationFile, PaintingSpecification::setPaintingSpecificationFile);
		binder.forField(tfRev)
				.bind(PaintingSpecification::getRev, PaintingSpecification::setRev);
		binder.forField(tfPrimer)
		.bind(PaintingSpecification::getPrimer, PaintingSpecification::setPrimer);
		binder.forField(tfIntermediate)
		.bind(PaintingSpecification::getIntermediate, PaintingSpecification::setIntermediate);
		binder.forField(tfFinals)
		.bind(PaintingSpecification::getFinals, PaintingSpecification::setFinals);
		binder.forField(tfTotal)
				.bind(PaintingSpecification::getTotal, PaintingSpecification::setTotal);
		binder.forField(tfColor)
		.bind(PaintingSpecification::getColor, PaintingSpecification::setColor);
		binder.forField(tfHumidity)
		.bind(PaintingSpecification::getHumidity, PaintingSpecification::setHumidity);
		binder.forField(tfAboveDewPoint)
		.bind(PaintingSpecification::getAboveDewPoint, PaintingSpecification::setAboveDewPoint);
	}

	@Override
	protected void okButtonClicked() throws Exception {
		binder.writeBean(paintingSpecification);
//		String authorityType = cbAuthorityType.getSelectedItem().get();
//		String nameEigenvalue = tfNameEigenvalue.getValue().trim();
//		
//		if(bomNameAuthority==null) {
//			BomNameAuthority bomNameAuthority = bomNameAuthorityService.getByTypeValue(authorityType,nameEigenvalue);
//			if(bomNameAuthority!=null) {
//				throw new PlatformException("规范类型："+authorityType+"，名称特征值："+nameEigenvalue+"已存在！");
//			}
//		}else {
//			long id = bomNameAuthority.getId();
//			List<BomNameAuthority> bomNameAuthorityList = bomNameAuthorityService.getByTypeValue(authorityType,nameEigenvalue,id);
//			if(bomNameAuthorityList!=null && bomNameAuthorityList.size()>0) {
//				throw new PlatformException("规范类型："+authorityType+"，名称特征值："+nameEigenvalue+"已存在！");
//			}
//		}
		
		PaintingSpecification save = paintingSpecificationService.save(paintingSpecification);
		result.setObj(save);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
		System.out.println("DialogContent");
		
		
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
