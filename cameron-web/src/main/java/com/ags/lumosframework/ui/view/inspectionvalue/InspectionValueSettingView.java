package com.ags.lumosframework.ui.view.inspectionvalue;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.InspectionValue;
import com.ags.lumosframework.service.IInspectionValueService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.PermissionConstants;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.Optional;
//
//@Menu(caption = "InspectionValueSetting", captionI18NKey = "view.inspectionvalue.caption", iconPath = "images/icon/text-blob.png",groupName="BaseInfo", order = 2)
@SpringView(name = "InspectionValueSetting", ui = CameronUI.class)
public class InspectionValueSettingView extends BaseView implements Button.ClickListener , IFilterableView{

	private static final long serialVersionUID = 4854162164548450226L;

	
	@Secured(PermissionConstants.INSPECTIONTYPE_ADD)
	@I18Support(caption = "Add", captionKey = "common.add")
	private Button btnAdd = new Button();

	@Secured(PermissionConstants.INSPECTIONTYPE_EDIT)
	@I18Support(caption = "Edit", captionKey = "common.edit")
	private Button btnEdit = new Button();

	@Secured(PermissionConstants.INSPECTIONTYPE_DELETE)
	@I18Support(caption = "Delete", captionKey = "common.delete")
	private Button btnDelete = new Button();

	@Secured(PermissionConstants.INSPECTIONTYPE_REFRESH)
	@I18Support(caption = "Refresh", captionKey = "common.refresh")
	private Button btnRefresh = new Button();

	private Button[] btns = new Button[] { btnAdd, btnEdit, btnDelete, btnRefresh };
	
	private HorizontalLayout hlToolBox = new HorizontalLayout();
	
//	private IObjectListGrid<InspectionValue> objectGrid = new PaginationObjectListGrid<>(false);
	private IDomainObjectGrid<InspectionValue> objectGrid = new PaginationDomainObjectList<>();

	@Autowired
	private IInspectionValueService inspectionValueService;

	@Autowired
	private AddInspectionValueDialog addInspectionValueDialog;
	
	public InspectionValueSettingView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		SearchPanelBuilder sp = new SearchPanelBuilder( BeanManager.getService(InspectionValueConditions.class), objectGrid, this);
		hlToolBox.addComponent(sp);
		hlToolBox.setComponentAlignment(sp, Alignment.MIDDLE_RIGHT);
		for (Button btn : btns) {
			hlTempToolBox.addComponent(btn);
			btn.addClickListener(this);
			btn.setDisableOnClick(true);
		}
		btnAdd.setIcon(VaadinIcons.PLUS);
		btnEdit.setIcon(VaadinIcons.EDIT);
		btnDelete.setIcon(VaadinIcons.TRASH);
		btnRefresh.setIcon(VaadinIcons.REFRESH);

		objectGrid.addColumn(InspectionValue::getInspection_name).setCaption(I18NUtility.getValue("view.inspectionvalue.inspectionname", "Inspection_Name"));
		objectGrid.addColumn(InspectionValue::getProduct_specification).setCaption(I18NUtility.getValue("view.inspectionvalue.productspecification", "Product_Specification"));
		objectGrid.addColumn(InspectionValue::getAppearance_desc).setCaption(I18NUtility.getValue("view.inspectionvalue.appearancedesc", "Appearance_Desc"));
        objectGrid.addColumn(source ->{
            double price = source.getMin_value();
            return NumberFormat.getInstance().format(price);
        }).setCaption(I18NUtility.getValue("view.inspectionvalue.minvalue","Min_Value"));
        objectGrid.addColumn(source ->{
            double price = source.getMax_value();
            return NumberFormat.getInstance().format(price);
        }).setCaption(I18NUtility.getValue("view.inspectionvalue.maxvalue","Max_Value"));
		objectGrid.setObjectSelectionListener(event -> {
			setButtonStatus(event.getFirstSelectedItem());
		});
		vlRoot.addComponents((Component) objectGrid);
		vlRoot.setExpandRatio((Component) objectGrid, 1);

		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<InspectionValue> optional) {
		boolean enable = optional.isPresent();
		btnEdit.setEnabled(enable);
		btnDelete.setEnabled(enable);
	}

	@Override
	protected void init() {
		objectGrid.setServiceClass(IInspectionValueService.class);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		setButtonStatus(Optional.empty());
		objectGrid.refresh();
	}
	
	
	
	@Override
	public void updateAfterFilterApply() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		button.setEnabled(true);
		if (btnAdd.equals(button)) {
			addInspectionValueDialog.setObject(null);
			addInspectionValueDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					objectGrid.refresh();
				}
			});
		} else if (btnEdit.equals(button)) {
			InspectionValue inspectionvalue = (InspectionValue) objectGrid.getSelectedObject();
			addInspectionValueDialog.setObject(inspectionvalue);
			addInspectionValueDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					InspectionValue temp = (InspectionValue) result.getObj();
					objectGrid.refresh(temp);
				}
			});
		} else if (btnDelete.equals(button)) {
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								inspectionValueService.delete((InspectionValue) objectGrid.getSelectedObject());
							} catch (PlatformException e) {
								notificationError("Common.RelationShipCheckFailed", e.getMessage());
								return;
							}
							objectGrid.refresh();
						}
					});
		} else if (btnRefresh.equals(button)) {
			objectGrid.refresh();
		}		
	}

}
