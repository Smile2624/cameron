package com.ags.lumosframework.ui.view.inspectiontype;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.InspectionType;
import com.ags.lumosframework.service.IInspectionTypeService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

//@Menu(caption = "InspectionType", captionI18NKey = "view.inspection.caption", iconPath = "images/icon/text-blob.png",groupName="BaseInfo", order = 0)
@SpringView(name = "InspectionType", ui = CameronUI.class)
public class InspectionTypeView extends BaseView implements Button.ClickListener , IFilterableView{
	
	private static final long serialVersionUID = 8614413267302733571L;

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
	
	//查询区域控件
	@I18Support(caption = "InspectionName", captionKey = "view.inspection.inspectionname")
	private TextField tfInspectionName = new TextField();
	
	@I18Support(caption = "InspectionDesc", captionKey = "view.inspection.inspectiondesc")
	private TextField tfInspectionDesc = new TextField();
	
	@I18Support(caption = "Refresh", captionKey = "common.search")
	private Button btnSearch = new Button();

	private Button[] btns = new Button[] { btnAdd, btnEdit, btnDelete, btnRefresh };
	
	AbstractComponent[] fields = {tfInspectionName,tfInspectionDesc,btnSearch};

	private HorizontalLayout hlToolBox = new HorizontalLayout();
	
//	private HorizontalLayout hlSearchBox = new HorizontalLayout();

	private IDomainObjectGrid<InspectionType> objectGrid = new PaginationDomainObjectList<>();

	@Autowired
	private IInspectionTypeService inspectionTypeService;

	@Autowired
	private AddInspectionTypeDialog addInspectionTypeDialog;

	public InspectionTypeView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		SearchPanelBuilder sp = new SearchPanelBuilder( BeanManager.getService(InspectionTypeConditions.class), objectGrid, this);
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

		objectGrid.addColumn(InspectionType::getInspection_name).setCaption(I18NUtility.getValue("view.inspection.inspectionname", "InspectionName"));
		objectGrid.addColumn(InspectionType::getInspection_desc).setCaption(I18NUtility.getValue("view.inspection.inspectiondesc", "InspectionDesc"));

		objectGrid.setObjectSelectionListener(event -> {
			setButtonStatus(event.getFirstSelectedItem());
		});
		vlRoot.addComponents((Component) objectGrid);
		vlRoot.setExpandRatio((Component) objectGrid, 1);

		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<InspectionType> optional) {
		boolean enable = optional.isPresent();
		btnEdit.setEnabled(enable);
		btnDelete.setEnabled(enable);
	}

	@Override
	protected void init() {
		objectGrid.setServiceClass(IInspectionTypeService.class);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		setButtonStatus(Optional.empty());
		objectGrid.refresh();
	}

	@Override
	public void buttonClick(Button.ClickEvent event) {
		Button button = event.getButton();
		button.setEnabled(true);
		if (btnAdd.equals(button)) {
			addInspectionTypeDialog.setObject(null);
			addInspectionTypeDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					objectGrid.refresh();
				}
			});
		} else if (btnEdit.equals(button)) {
			InspectionType inspectionType = (InspectionType) objectGrid.getSelectedObject();
			addInspectionTypeDialog.setObject(inspectionType);
			addInspectionTypeDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					InspectionType temp = (InspectionType) result.getObj();
					objectGrid.refresh(temp);
				}
			});
		} else if (btnDelete.equals(button)) {
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								inspectionTypeService.delete((InspectionType) objectGrid.getSelectedObject());
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

	@Override
	public void updateAfterFilterApply() {
		// TODO Auto-generated method stub
		
	}
}
