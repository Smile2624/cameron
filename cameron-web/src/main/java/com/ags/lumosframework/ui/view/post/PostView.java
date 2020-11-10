package com.ags.lumosframework.ui.view.post;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.Post;
import com.ags.lumosframework.service.IPostService;
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

//@Menu(caption = "Post", captionI18NKey = "post.view.caption", iconPath = "images/icon/text-blob.png", groupName = "BaseInfo", order = 1)
@SpringView(name = "Post", ui = CameronUI.class)
public class PostView extends BaseView implements Button.ClickListener, IFilterableView {

	private static final long serialVersionUID = -491870696518023971L;

	@Secured(PermissionConstants.POST_ADD)
	@I18Support(caption = "Add", captionKey = "common.add")
	private Button btnAdd = new Button();

	@Secured(PermissionConstants.POST_EDIT)
	@I18Support(caption = "Edit", captionKey = "common.edit")
	private Button btnEdit = new Button();

	@Secured(PermissionConstants.POST_DELETE)
	@I18Support(caption = "Delete", captionKey = "common.delete")
	private Button btnDelete = new Button();

	@Secured(PermissionConstants.POST_REFRESH)
	@I18Support(caption = "Refresh", captionKey = "common.refresh")
	private Button btnRefresh = new Button();

	private Button[] btns = new Button[] { btnAdd, btnEdit, btnDelete, btnRefresh };

	// 查询区域控件
	@I18Support(caption = "PostCode", captionKey = "view.post.code")
	private TextField tfCode = new TextField();

	@I18Support(caption = "PostName", captionKey = "view.post.name")
	private TextField tfName = new TextField();

	@I18Support(caption = "Search", captionKey = "common.search")
	private Button btnSearch = new Button();

	AbstractComponent[] fields = { tfCode, tfName, btnSearch };

	private HorizontalLayout hlToolBox = new HorizontalLayout();

//	private IObjectListGrid<Post> objectGrid = new PaginationObjectListGrid<>(false);
	private IDomainObjectGrid<Post> objectGrid = new PaginationDomainObjectList<>();

	@Autowired
	private IPostService postService;

	@Autowired
	private AddPostDialog addPostDialog;

	public PostView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		SearchPanelBuilder sp = new SearchPanelBuilder(BeanManager.getService(PostConditions.class), objectGrid, this);
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

		objectGrid.addColumn(Post::getCode).setCaption(I18NUtility.getValue("post.code", "Code"));
		objectGrid.addColumn(Post::getName).setCaption(I18NUtility.getValue("post.name", "Name"));

		objectGrid.setObjectSelectionListener(event -> {
			setButtonStatus(event.getFirstSelectedItem());
		});
		vlRoot.addComponents((Component) objectGrid);
		vlRoot.setExpandRatio((Component) objectGrid, 1);

		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<Post> optional) {
		boolean enable = optional.isPresent();
		btnEdit.setEnabled(enable);
		btnDelete.setEnabled(enable);
	}

	@Override
	protected void init() {
		objectGrid.setServiceClass(IPostService.class);
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
			addPostDialog.setObject(null);
			addPostDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					objectGrid.refresh();
				}
			});
		} else if (btnEdit.equals(button)) {
			Post post = (Post) objectGrid.getSelectedObject();
			addPostDialog.setObject(post);
			addPostDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					Post temp = (Post) result.getObj();
					objectGrid.refresh(temp);
				}
			});
		} else if (btnDelete.equals(button)) {
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								postService.delete((Post) objectGrid.getSelectedObject());
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
