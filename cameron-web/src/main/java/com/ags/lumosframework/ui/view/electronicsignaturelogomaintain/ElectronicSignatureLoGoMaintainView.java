package com.ags.lumosframework.ui.view.electronicsignaturelogomaintain;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.ICaMediaService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.picturepreview.IPicturePreview;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Menu(caption = "ElectronicSignatureLoGoMaintain", captionI18NKey = "ElectronicSignatureLoGoMaintain.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 10)
@SpringView(name = "ElectronicSignatureLoGoMaintain", ui = CameronUI.class)
@Secured("ElectronicSignatureLoGo")
public class ElectronicSignatureLoGoMaintainView extends BaseView implements Button.ClickListener, IFilterableView {
	
	private static final long serialVersionUID = 9136266114154619443L;

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

  private IDomainObjectGrid<Media> objectGrid = new PaginationDomainObjectList<>();

  @Autowired
  private AddElectronicSignatureLoGoMaintainDialog addElectronicSignatureLoGoMaintainDialog;

  @Autowired
  private ICaMediaService mediaService;
  
//  @Autowired
//  private PicturePreviewDialog picturePreviewDialog;
  
  @Inject
  private IPicturePreview picturePreview;

  public ElectronicSignatureLoGoMaintainView() {
      VerticalLayout vlRoot = new VerticalLayout();
      vlRoot.setMargin(false);
      vlRoot.setSizeFull();

      hlToolBox.setWidth("100%");
      hlToolBox.addStyleName(CoreTheme.TOOLBOX);
      hlToolBox.setMargin(true);
      vlRoot.addComponent(hlToolBox);
      HorizontalLayout hlTempToolBox = new HorizontalLayout();
      hlToolBox.addComponent(hlTempToolBox);

      SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(ElectronicSignatureLoGoMaintainConditions.class), this.objectGrid, this);
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
      
      objectGrid.addColumn(Media::getName).setCaption(I18NUtility.getValue("ElectronicSignatureLoGoMaintain.FileName", "FileName"));

      objectGrid.addColumn(new ValueProvider<Media, String>(){

		private static final long serialVersionUID = -3336100835566748042L;

		@Override
		public String apply(Media source) {
			String category = source.getCategory();
			if(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey().equals(category)){
			    return "电子签名";
            }
			if(ElectronicSignatureLoGoType.LOGO.getKey().equals(category)){
			    return "Logo";
            }
			if(ElectronicSignatureLoGoType.SEAL.getKey().equals(category)){
			    return "QA公章";
            }
			return "";
		}
    	  
      }).setCaption(I18NUtility.getValue("ElectronicSignatureLoGoMaintain.ESignatureLoGoType", "ESignatureLoGoType"));
      objectGrid.addComponentColumn(new ValueProvider<Media, HorizontalLayout>(){
			private static final long serialVersionUID = 7304599177569403315L;

			@Override
			public HorizontalLayout apply(Media source) {
				HorizontalLayout layout = new HorizontalLayout();
				Button btnPreview = new Button();

				btnPreview.setIcon(VaadinIcons.VIEWPORT);
	            btnPreview.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	            btnPreview.addStyleName(ValoTheme.BUTTON_QUIET);
				btnPreview.setCaption("预览");
				btnPreview.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 753594212447522087L;
					@Override
					public void buttonClick(ClickEvent event) {
							picturePreview.setInputStreams(source.getMediaStream());
							picturePreview.show(getUI());
					}
				});
				layout.addComponent(btnPreview);//btnView
				return layout;
			}
		}).setCaption("预览");
      
      objectGrid.addColumn(Media::getCreateUserName).setCaption(I18NUtility.getValue("ElectronicSignatureLoGoMaintain.CreateUserName", "CreateUserName"));
      objectGrid.addColumn(new ValueProvider<Media, String>(){

		private static final long serialVersionUID = 409004113859123613L;

		@Override
		public String apply(Media source) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return sdf.format(Date.from(source.getCreateTime().toInstant()));
		}
    	  
      }).setCaption(I18NUtility.getValue("ElectronicSignatureLoGoMaintain.CreateTime", "CreateTime"));

      objectGrid.setObjectSelectionListener(event -> {
          setButtonStatus(event.getFirstSelectedItem());
      });
      vlRoot.addComponents((Component) objectGrid);
      vlRoot.setExpandRatio((Component) objectGrid, 1);

      this.setSizeFull();
      this.setCompositionRoot(vlRoot);
  }

  private void setButtonStatus(Optional<Media> optional) {
      boolean enable = optional.isPresent();
      btnEdit.setEnabled(enable);
      btnDelete.setEnabled(enable);
  }

  @Override
  protected void init() {
      objectGrid.setServiceClass(ICaMediaService.class);
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
    	  addElectronicSignatureLoGoMaintainDialog.setObject(null);
    	  addElectronicSignatureLoGoMaintainDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
                  objectGrid.refresh();
              }
          });
      } else if (btnEdit.equals(button)) {
    	  Media media = (Media) objectGrid.getSelectedObject();
    	  addElectronicSignatureLoGoMaintainDialog.setObject(media);
    	  addElectronicSignatureLoGoMaintainDialog.show(getUI(), result -> {
              if (ConfirmResult.Result.OK.equals(result.getResult())) {
            	  Media temp = (Media) result.getObj();
            	  if(temp !=null) {
            		  objectGrid.refresh(temp);
            	  }else {
            		  objectGrid.refresh();
            	  }
              }
          });
      } else if (btnDelete.equals(button)) {
          ConfirmDialog.show(getUI(),
                  I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                      if (ConfirmResult.Result.OK.equals(result.getResult())) {
                          try {
                        	  mediaService.delete((Media) objectGrid.getSelectedObject());
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
