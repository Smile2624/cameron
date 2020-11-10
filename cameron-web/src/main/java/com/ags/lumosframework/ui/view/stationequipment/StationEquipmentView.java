package com.ags.lumosframework.ui.view.stationequipment;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.StationEquipment;
import com.ags.lumosframework.service.IStationEquipmentService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Menu(caption = "StationEquipment", captionI18NKey = "Cameron.StationEquipment.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 11)
@SpringView(name = "StationEquipment", ui = CameronUI.class)
@Secured("StationEquipment")
public class StationEquipmentView extends BaseView implements Button.ClickListener, IFilterableView  {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 5477571592666694904L;

//  @Secured(DemoPermissionConstants.BOOK_ADD)
  @I18Support(caption = "Add", captionKey = "common.add")
  private Button btnAdd = new Button();

  //    @Secured(DemoPermissionConstants.BOOK_EDIT)
  @I18Support(caption = "Edit", captionKey = "common.edit")
  private Button btnEdit = new Button();

  //    @Secured(DemoPermissionConstants.BOOK_DELETE)
  @I18Support(caption = "Delete", captionKey = "common.delete")
  private Button btnDelete = new Button();

  //    @Secured(DemoPermissionConstants.BOOK_REFRESH)
  @I18Support(caption = "Refresh", captionKey = "common.refresh")
  private Button btnRefresh = new Button();

  private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh};

  private HorizontalLayout hlToolBox = new HorizontalLayout();

  private IDomainObjectGrid<StationEquipment> objectGrid = new PaginationDomainObjectList<>();

  @Autowired
  private AddStationEquipmentDialog addStationEquipmentDialog;

  @Autowired
  private IStationEquipmentService stationEquipmentService;

  public StationEquipmentView() {
      VerticalLayout vlRoot = new VerticalLayout();
      vlRoot.setMargin(false);
      vlRoot.setSizeFull();

      hlToolBox.setWidth("100%");
      hlToolBox.addStyleName(CoreTheme.TOOLBOX);
      hlToolBox.setMargin(true);
      vlRoot.addComponent(hlToolBox);
      HorizontalLayout hlTempToolBox = new HorizontalLayout();
      hlToolBox.addComponent(hlTempToolBox);

      SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(StationEquipmentConditions.class), this.objectGrid, this);
      hlToolBox.addComponent(c);
      hlToolBox.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);

      for (Button btn : btns) {
          hlTempToolBox.addComponent(btn);
          btn.addClickListener(this);
          btn.setDisableOnClick(true);
      }
      btnAdd.setIcon(VaadinIcons.PLUS);
      btnEdit.setIcon(VaadinIcons.EDIT);
      btnDelete.setIcon(VaadinIcons.TRASH);
      btnRefresh.setIcon(VaadinIcons.REFRESH);

      objectGrid.addColumn(StationEquipment::getEquipmentNo).setCaption(I18NUtility.getValue("StationEquipment.EquipmentNo", "EquipmentNo"));
      objectGrid.addColumn(StationEquipment::getEquipmentType).setCaption(I18NUtility.getValue("StationEquipment.EquipmentType", "EquipmentType"));
      objectGrid.addColumn(StationEquipment::getStation).setCaption(I18NUtility.getValue("StationEquipment.Station", "Station"));
      objectGrid.addColumn(StationEquipment::getProcedureNo).setCaption(I18NUtility.getValue("StationEquipment.ProcedureNo", "ProcedureNo"));
      objectGrid.addColumn(StationEquipment::getIpAdress).setCaption(I18NUtility.getValue("StationEquipment.IpAdress", "IpAdress"));


      objectGrid.setObjectSelectionListener(event -> {
          setButtonStatus(event.getFirstSelectedItem());
      });
      vlRoot.addComponents((Component) objectGrid);
      vlRoot.setExpandRatio((Component) objectGrid, 1);

      this.setSizeFull();
      this.setCompositionRoot(vlRoot);
  }

  private void setButtonStatus(Optional<StationEquipment> optional) {
      boolean enable = optional.isPresent();
      btnEdit.setEnabled(enable);
      btnDelete.setEnabled(enable);
  }

  @Override
  protected void init() {
      objectGrid.setServiceClass(IStationEquipmentService.class);
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
    	  addStationEquipmentDialog.setObject(null);
    	  addStationEquipmentDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
                  objectGrid.refresh();
              }
          });
      } else if (btnEdit.equals(button)) {
    	  StationEquipment stationEquipment = (StationEquipment) objectGrid.getSelectedObject();
          addStationEquipmentDialog.setObject(stationEquipment);
          addStationEquipmentDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
            	  StationEquipment temp = (StationEquipment) result.getObj();
                  objectGrid.refresh(temp);
              }
          });
      } else if (btnDelete.equals(button)) {
          ConfirmDialog.show(getUI(),
                  I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                      if (ConfirmResult.Result.OK.equals(result.getResult())) {
                          try {
                        	  stationEquipmentService.delete((StationEquipment) objectGrid.getSelectedObject());
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

  }
}
