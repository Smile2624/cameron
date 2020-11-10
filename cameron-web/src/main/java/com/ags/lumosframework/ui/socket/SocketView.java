package com.ags.lumosframework.ui.socket;


import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.BrowserFrame;

//@Menu(caption = "SOCKET", captionI18NKey = "view.socket.caption", iconPath = "images/icon/text-blob.png",groupName="UserOperation", order = 0)
@SpringView(name = "SOCKET", ui = CameronUI.class)
public class SocketView extends BaseView {

	
	private static final long serialVersionUID = 6105508205433498937L;
	
    public SocketView() {
    	BrowserFrame browserFrame = new BrowserFrame();
    	browserFrame.setSource(new ExternalResource("html/WebSocket.html"));
    	browserFrame.setHeight("100%");
    	browserFrame.setWidth("100%");
    	this.setCompositionRoot(browserFrame);
    	this.setSizeFull();
    }

}
