package com.ags.lumosframework.ui.view.electronicsignaturelogomaintain;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.ICaMediaService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;
import org.vaadin.uploadbutton.UploadButton;

import java.util.Optional;

@SpringComponent
@Scope("prototype")
public class AddElectronicSignatureLoGoMaintainDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = 8722074125468605672L;


    @I18Support(caption = "ESignatureLoGoType", captionKey = "ElectronicSignatureLoGoMaintain.ESignatureLoGoType")
    ComboBox<String> cbESignatureLoGoType = new ComboBox<String>();//电子签名LoGo类型

    @I18Support(caption = "导入图片", captionKey = "")
    private UploadButton upload = new UploadButton();

    @I18Support(caption = "FileName", captionKey = "ElectronicSignatureLoGoMaintain.FileName")
    private TextField tfFileName = new TextField();

    private Binder<Media> binder = new Binder<Media>();

    private String caption;

    private AbstractComponent[] fields = {tfFileName, cbESignatureLoGoType, upload};

    private String[] pictureTypes = new String[]{"JPG", "JPEG", "JPE", "JFIF", "PNG", "GIF"};

    // 完成事件
    private UploadFinishEvent uploadEvent = null;

    private Media media;

    private ICaMediaService mediaService;
    
    private String action = "";

    public AddElectronicSignatureLoGoMaintainDialog(ICaMediaService mediaService) {
        this.mediaService = mediaService;
    }

    public void setObject(Media media) {
        String captionName = I18NUtility.getValue("ElectronicSignatureLoGoMaintain.view.caption", "ElectronicSignatureLoGo");
        if (media == null) {
            action = "NEW";
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            media = new Media();
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
            action = "EDIT";
        }
        this.media = media;
        binder.readBean(media);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        System.out.println("init");
        binder.forField(tfFileName)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(Media::getName, Media::setName);
        binder.forField(cbESignatureLoGoType)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(Media::getCategory, Media::setCategory);
    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(media);
        if (Strings.isNullOrEmpty(media.getType())) {
            throw new PlatformException("请上传一个图片文件！");
        }
        String eSignatureLoGoType = "";
        Optional<String> optional = cbESignatureLoGoType.getSelectedItem();
        if (optional.isPresent()) {
            eSignatureLoGoType = optional.get();
        }
        String fileName = tfFileName.getValue().trim();
        if ("NEW".equals(action)) {
            Media mediaSaved = mediaService.getByTypeName(eSignatureLoGoType, fileName);
            if (mediaSaved != null) {
                mediaSaved.setType(media.getType());
                mediaSaved.setMediaContent(media.getMediaContent());
                Media save = mediaService.save(mediaSaved);
                result.setObj(save);
            } else {
                Media save = mediaService.save(media);
                result.setObj(save);
            }
        } else {
//        	Media mediaTemp = mediaService.getByTypeName(eSignatureLoGoType, fileName);
//        	if(mediaTemp != media) {
//        		NotificationUtils.notificationError("当前类型下已经存在 名称为:" + fileName+"的电子签名。");
//        		return;
//        	}
            Media save = mediaService.save(media);
            result.setObj(save);
        }
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {

        cbESignatureLoGoType.setItems("ES", "LG","SEAL");
        cbESignatureLoGoType.setItemCaptionGenerator(new ItemCaptionGenerator<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(String item) {
                if(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey().equals(item)){
                    return "电子签名";
                }
                if(ElectronicSignatureLoGoType.LOGO.getKey().equals(item)){
                    return "Logo";
                }
                if(ElectronicSignatureLoGoType.SEAL.getKey().equals(item)){
                    return "QA公章";
                }
                return  "";
            }
        });

        FileUploader fileUploader = new FileUploader(new UploadFinishedListener() {
            @Override
            public void finish(UploadFinishEvent event) {
                uploadEvent = event;
            }
        });

        upload.setIcon(VaadinIcons.UPLOAD);
        upload.setImmediateMode(true);
        upload.setReceiver(fileUploader);
        upload.addSucceededListener(fileUploader);
        upload.addFinishedListener(new Upload.FinishedListener() {

            private static final long serialVersionUID = -7181196043001323827L;

            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                try {
                    String eSignatureLoGoType = "";
                    String fileName = fileUploader.getFileName();
                    fileName.split(".");
                    if (fileName == null || "".equals(fileName) || !checkIsPicture(fileName)) {
                        throw new PlatformException("请选择图片格式的文件！");
                    }
                    Optional<String> optional = cbESignatureLoGoType.getSelectedItem();
                    if (optional.isPresent()) {
                        eSignatureLoGoType = optional.get();
                    } else {
                        throw new PlatformException("请选择类型！");
                    }
//                	tfFileName.setValue(fileUploader.getFileName());
                    media.setCategory(eSignatureLoGoType);
                    media.setMediaContent(uploadEvent.getUploadFileInByte());
//    				media.setName(fileUploader.getFileName());
                    media.setType(uploadEvent.getFileType());
                    System.out.println(fileUploader.getFileName() + "," + uploadEvent.getFileType());
                    NotificationUtils.notificationInfo("获取文件成功！");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("获取文件发生异常" + e.getMessage());
                }
            }


        });

        ResponsiveLayout rl = new ResponsiveLayout();
        ResponsiveRow addRow = rl.addRow();
        addRow.setVerticalSpacing(ResponsiveRow.SpacingSize.SMALL, true);
        addRow.setHorizontalSpacing(true);

        for (AbstractComponent field : fields) {
            field.setWidth("100%");
            addRow.addColumn().withDisplayRules(12, 12, 6, 6).withComponent(field);
        }
        return rl;
    }

    public boolean checkIsPicture(String fileName) {
        boolean falg = false;
        if (fileName != null && !fileName.equals("")) {
            String[] arr = fileName.split("\\.");
            if (arr != null && arr.length > 0) {
                String type = arr[1].toUpperCase();
                for (String pictureType : pictureTypes) {
                    if (pictureType.equals(type)) {
                        falg = true;
                    }
                }
            }
        }
        return falg;
    }
}
