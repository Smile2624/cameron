package com.ags.lumosframework.ui.view.receivinginspectionreport;

import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;
import pl.pdfviewer.PdfViewer;

import java.io.InputStream;

@SpringComponent
@Scope("prototype")
public class PDFPreviewDialog extends BaseDialog  {

	private static final long serialVersionUID = -4847871254264443081L;
	
	VerticalLayout file = new VerticalLayout();
	
	private BrowserFrame frame = new BrowserFrame();
	
	
	PdfViewer pdfViewer = new PdfViewer();

    private Media media;
    
    @I18Support(caption="工单号",captionKey="")
    private LabelWithSamleLineCaption lblOrderNo = new LabelWithSamleLineCaption();
    
    String caption = "PDF预览";

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, true, true, callBack);
        
	}

	@Override
	protected void cancelButtonClicked() {
		
	}

	@Override
	protected Component getDialogContent() {
		file.removeAllComponents();
		return file;
	}

	@Override
	protected void initUIData() {
		
	}

	@Override
	protected void okButtonClicked() throws Exception {
		
	}
	
	public void setMedia(Media media) {
        this.media = media;
        pdfViewer.setAngleButtonVisible(true);
		pdfViewer.setDownloadBtnVisible(true);
		pdfViewer.setWidth(800, Unit.PIXELS);
	
		pdfViewer.setSizeFull();
		file.addComponent(pdfViewer);
		pdfViewer.setFile(new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 1L;
			@Override
			public InputStream getStream() {
				return media.getMediaStream();
			}
		},media.getName()));
    }

}
