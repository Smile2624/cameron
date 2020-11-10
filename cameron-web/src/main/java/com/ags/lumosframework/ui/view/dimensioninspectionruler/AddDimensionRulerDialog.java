package com.ags.lumosframework.ui.view.dimensioninspectionruler;


import com.ags.lumosframework.pojo.DimensionRuler;
import com.ags.lumosframework.service.IDimensionRulerService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddDimensionRulerDialog extends BaseDialog {

	private static final long serialVersionUID = -6230428947808654930L;

	@I18Support(caption="Material_No", captionKey="view.DimensionRuler.MaterialNo")
	private TextField tfMaterialNo = new TextField();
	
	@I18Support(caption="Material_Rev", captionKey="view.DimensionRuler.MaterialRev")
	private TextField tfMaterialRev = new TextField();
	
	@I18Support(caption="InspectionItem_Name", captionKey="view.DimensionRuler.InspectionItemName")
	private TextField tfInspectionItemName = new TextField();
	
//	@I18Support(caption="InspectionItem_Type", captionKey="view.DimensionRuler.InspectionItemType")
//	private TextField tfInspectionItemType = new TextField();
	
	@I18Support(caption = "InspectionItem_Type", captionKey = "view.DimensionRuler.InspectionItemType")
	ComboBox<String> cbInspectionItemType = new ComboBox<String>();
	
	@I18Support(caption="Min_Value", captionKey="view.DimensionRuler.minvalue")
	private TextField tfMinValue = new TextField();
	
	@I18Support(caption="Max_Value", captionKey="view.DimensionRuler.maxvalue")
	private TextField tfMaxValue = new TextField();
	
	private Binder<DimensionRuler> binder = new Binder<>();
	

	private String caption;
	
	private String action;
	
	private DimensionRuler dimensionRuler;
	
//	@Autowired
	private IDimensionRulerService dimensionRulerService;
	
	public AddDimensionRulerDialog(IDimensionRulerService dimensionRulerService) {
		this.dimensionRulerService = dimensionRulerService;
	}
	
	public void setObject(DimensionRuler dimensionRuler) {
		
        String captionName = I18NUtility.getValue("view.dimensionruler.caption", "Dimensionruler");
        if (dimensionRuler == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            action = "NEW";
            dimensionRuler = new DimensionRuler(null);
        }else{
        	action = "EDIT";
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
        }
        this.dimensionRuler = dimensionRuler;
        binder.readBean(dimensionRuler);
    }
    
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        tfMaterialNo.setWidth("100%");
        tfMaterialRev.setWidth("100%");
        tfInspectionItemName.setWidth("100%");
        cbInspectionItemType.setWidth("100%");
        cbInspectionItemType.setItems("NUMBRIC","BOOLEAN");//NUMBER  BOOLEAN
        cbInspectionItemType.setItemCaptionGenerator(new ItemCaptionGenerator<String>() {
	            private static final long serialVersionUID = 1L;

	            @Override
	            public String apply(String item) {
	            	return "NUMBRIC".equals(item)?"数字型":"BOOLEAN".equals(item)?"布尔型":"";
	            }
		});
        tfMinValue.setWidth("100%");
        tfMaxValue.setWidth("100%");
        vlContent.addComponents(tfMaterialNo, tfMaterialRev,tfInspectionItemName,cbInspectionItemType,tfMinValue,tfMaxValue);
        return vlContent;
	}

	@Override
	protected void initUIData() {
	    binder.forField(tfMaterialNo)
	    		.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
	    		.bind(DimensionRuler::getMaterialNo, DimensionRuler::setMaterialNo);
        binder.forField(tfMaterialRev)
        		.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
        		.bind( DimensionRuler::getMaterialRev, DimensionRuler::setMaterialRev);
        binder.bind(tfInspectionItemName, DimensionRuler::getInspectionItemName, DimensionRuler::setInspectionItemName);
        binder.bind(cbInspectionItemType, DimensionRuler::getInspectionItemType, DimensionRuler::setInspectionItemType);
        binder.forField(tfMinValue).withConverter(new StringToDoubleConverter(
                I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float is allowed")))
                .withValidator(new DoubleRangeValidator(I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0D, Double.MAX_VALUE))
                .bind(DimensionRuler::getMinValue, DimensionRuler::setMinValue);
        binder.forField(tfMaxValue).withConverter(new StringToDoubleConverter(
                I18NUtility.getValue("Common.OnlyFloatAllowed", "Only float is allowed")))
                .withValidator(new DoubleRangeValidator(I18NUtility.getValue("Common.MustEqualOrLargerThan0", "Must equals or larger than 0"), 0D, Double.MAX_VALUE))
                .bind(DimensionRuler::getMaxValue, DimensionRuler::setMaxValue);
    }

	@Override
	protected void okButtonClicked() throws Exception {
        binder.writeBean(dimensionRuler);
        DimensionRuler save = dimensionRulerService.save(dimensionRuler);
        result.setObj(save);
	}

	
}
