package com.ags.lumosframework.ui;

import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.vaadin.base.BaseUIHasMenu;
import com.ags.lumosframework.web.vaadin.base.annotation.MenuGroup;
import com.ags.lumosframework.web.vaadin.base.annotation.WebEntry;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;

@WebEntry(shortCaption = "Main System",
longCaption = "主系统",
shortCaptionI18NKey="ui.cameron.title",
description="进入主系统",
descriptionI18NKey = "ui.cameron.Description",
iconPath = "images/mes.png",
order = 1,
viewGroups= {
	@MenuGroup(name="Data",caption="Data",captionI18NKey="ui.view.Data",order=0,iconPath="mes.png") ,
	@MenuGroup(name="CommonFunction",caption="CommonFunction",captionI18NKey="ui.view.CommonFunction",order=1,iconPath="mes.png"),
	@MenuGroup(name="Production",caption="Production",captionI18NKey="ui.view.Production",order=2,iconPath="mes.png"),
	@MenuGroup(name="Quality",caption="Quality",captionI18NKey="ui.view.Quality",order=3,iconPath="mes.png"),
	@MenuGroup(name="Result",caption="Result",captionI18NKey="ui.view.Result",order=4,iconPath="mes.png")}
)
@SpringUI(path = "Cameron")
@Theme("light")
@Push(transport = Transport.WEBSOCKET_XHR)
public class CameronUI extends BaseUIHasMenu {


	private static final long serialVersionUID = -7630620882753216246L;

	@Override
	protected void setTitle() {
		Page.getCurrent().setTitle(I18NUtility.getValue("ui.title", "Main System"));
	}

}
