package com.ags.lumosframework.ui.view.bomnameauthority;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.BomNameAuthority;
import com.ags.lumosframework.service.IBomNameAuthorityService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

//@Menu(caption = "BomNameAuthority", captionI18NKey = "BomNameAuthority.view.caption", iconPath = "images/icon/text-blob.png", groupName = "BaseInfo", order = 6)
@SpringView(name = "BomNameAuthority", ui = CameronUI.class)
public class BomNameAuthorityView extends BaseView implements Button.ClickListener, IFilterableView {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5017815372406830368L;

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

  private IDomainObjectGrid<BomNameAuthority> objectGrid = new PaginationDomainObjectList<>();

  @Autowired
  private AddBomNameAuthorityDialog addBomNameAuthorityDialog;

  @Autowired
  private IBomNameAuthorityService bomNameAuthorityService;

  public BomNameAuthorityView() {
      VerticalLayout vlRoot = new VerticalLayout();
      vlRoot.setMargin(false);
      vlRoot.setSizeFull();

      hlToolBox.setWidth("100%");
      hlToolBox.addStyleName(CoreTheme.TOOLBOX);
      hlToolBox.setMargin(true);
      vlRoot.addComponent(hlToolBox);
      HorizontalLayout hlTempToolBox = new HorizontalLayout();
      hlToolBox.addComponent(hlTempToolBox);

      SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(BomNameAuthorityConditions.class), this.objectGrid, this);
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

      objectGrid.addColumn(new ValueProvider<BomNameAuthority, String>(){

		private static final long serialVersionUID = -3336100835566748042L;

		@Override
		public String apply(BomNameAuthority source) {
			String authorityType = source.getAuthorityType();
			return "H".equals(authorityType)?"硬度":"QP".equals(authorityType)?"质量计划":"P".equals(authorityType)?"压力":"";
		}
    	  
      }).setCaption(I18NUtility.getValue("BomNameAuthority.AuthorityType", "AuthorityType"));
      objectGrid.addColumn(BomNameAuthority::getNameEigenvalue).setCaption(I18NUtility.getValue("BomNameAuthority.NameEigenvalue", "NameEigenvalue"));
      objectGrid.addColumn(BomNameAuthority::getCreateUserName).setCaption(I18NUtility.getValue("BomNameAuthority.CreateUserName", "CreateUserName"));
      objectGrid.addColumn(new ValueProvider<BomNameAuthority, String>(){

		private static final long serialVersionUID = 409004113859123613L;

		@Override
		public String apply(BomNameAuthority source) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return sdf.format(Date.from(source.getCreateTime().toInstant()));
		}
    	  
      }).setCaption(I18NUtility.getValue("BomNameAuthority.CreateTime", "CreateTime"));

      objectGrid.setObjectSelectionListener(event -> {
          setButtonStatus(event.getFirstSelectedItem());
      });
      vlRoot.addComponents((Component) objectGrid);
      vlRoot.setExpandRatio((Component) objectGrid, 1);

      this.setSizeFull();
      this.setCompositionRoot(vlRoot);
  }

  private void setButtonStatus(Optional<BomNameAuthority> optional) {
      boolean enable = optional.isPresent();
      btnEdit.setEnabled(enable);
      btnDelete.setEnabled(enable);
  }

  @Override
  protected void init() {
      objectGrid.setServiceClass(IBomNameAuthorityService.class);
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
    	  addBomNameAuthorityDialog.setObject(null);
    	  addBomNameAuthorityDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
                  objectGrid.refresh();
              }
          });
      } else if (btnEdit.equals(button)) {
    	  BomNameAuthority bomNameAuthority = (BomNameAuthority) objectGrid.getSelectedObject();
          addBomNameAuthorityDialog.setObject(bomNameAuthority);
          addBomNameAuthorityDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
            	  BomNameAuthority temp = (BomNameAuthority) result.getObj();
                  objectGrid.refresh(temp);
              }
          });
      } else if (btnDelete.equals(button)) {
          ConfirmDialog.show(getUI(),
                  I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                      if (ConfirmResult.Result.OK.equals(result.getResult())) {
                          try {
                        	  bomNameAuthorityService.delete((BomNameAuthority) objectGrid.getSelectedObject());
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
