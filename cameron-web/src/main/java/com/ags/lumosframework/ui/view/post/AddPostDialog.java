package com.ags.lumosframework.ui.view.post;

import com.ags.lumosframework.pojo.Post;
import com.ags.lumosframework.service.IPostService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddPostDialog extends BaseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504457744122203286L;

	@I18Support(caption = "Code", captionKey = "post.code")
	private TextField tfCode = new TextField();

	@I18Support(caption = "Name", captionKey = "post.name")
	private TextField tfName = new TextField();

	private Binder<Post> binder = new Binder<>();

	private String caption;

	private Post post;

	private IPostService postService;

	public AddPostDialog(IPostService postService) {
		this.postService = postService;
	}

	public void setObject(Post post) {
		String captionName = I18NUtility.getValue("post.caption", "Post");
		if (post == null) {
			this.caption = I18NUtility.getValue("common.new", "New", captionName);
			post = new Post();
		} else {
			this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
		}
		this.post = post;
		binder.readBean(post);
	}

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
	}

	@Override
	protected void initUIData() {
		binder.forField(tfCode)
				.asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
				.bind(Post::getCode, Post::setCode);
		binder.bind(tfName, Post::getName, Post::setName);

	}

	@Override
	protected void okButtonClicked() throws Exception {
		binder.writeBean(post);
		Post save = postService.save(post);
		result.setObj(save);
	}

	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlContent = new VerticalLayout();
		vlContent.setSizeFull();

		tfCode.setWidth("100%");
		tfName.setWidth("100%");

		vlContent.addComponents(tfCode, tfName);
		return vlContent;
	}
}
