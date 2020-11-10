package com.ags.lumosframework.ui.view.gage;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.Gage;
import com.ags.lumosframework.service.IGageService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.PermissionConstants;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Menu(caption = "GageInfo", captionI18NKey = "Cameron.Gage", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 1)
@SpringView(name = "GageInfo", ui = CameronUI.class)
@Secured("GageInfo")
public class GageView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 6704698938270813136L;

    //    @Secured(DemoPermissionConstants.BOOK_ADD)
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

    @I18Support(caption = "Import Excel", captionKey = "common.import")
    private UploadButton upload = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    private IDomainObjectGrid<Gage> objectGrid = new PaginationDomainObjectList<>();
    
    private int num = 0;

    private Gage gage;
    @Autowired
    private IGageService gageService;

    public GageView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(GageConditions.class), this.objectGrid, this);
        hlToolBox.addComponent(c);
        hlToolBox.setComponentAlignment(c, Alignment.MIDDLE_RIGHT);

//        for (Button btn : btns) {
//            hlTempToolBox.addComponent(btn);
//            btn.addClickListener(this);
//            btn.setDisableOnClick(true);
//        }
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);

        //*******************
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
            private static final long serialVersionUID = 1L;

			@Override
			public void uploadFinished(Upload.FinishedEvent event) {
				try {
					String fileName = event.getFilename();
					if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
						num = importsxlsx(uploadEvent.getUploadFileInByte());
					} else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
						num = importsxls(uploadEvent.getUploadFileInByte());
					} else {
						NotificationUtils.notificationError("错误文件！");
					}

				} catch (Exception e) {
					NotificationUtils.notificationError("导入发生异常,请确认文件是否正确！");
				}
				objectGrid.refresh();
				NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
				num = 0;

			}

        });
        hlTempToolBox.addComponent(upload);

        objectGrid.addColumn(Gage::getGageName).setCaption(I18NUtility.getValue("Gage.GageName", "GageName"));
        objectGrid.addColumn(Gage::getGageNo).setCaption(I18NUtility.getValue("Gage.GageNo", "GageNo"));
        objectGrid.addColumn(Gage::getDescription).setCaption(I18NUtility.getValue("Gage.Description", "Description"));
        objectGrid.addColumn(source -> {
            return source.getActiveDate() == null ? "" : source.getActiveDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }).setCaption(I18NUtility.getValue("Gage.ActiveDate", "ActiveDate"));
        objectGrid.addColumn(Gage::getStatus).setCaption(I18NUtility.getValue("Gage.Status", "Status"));
        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });

        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

    }

    private void setButtonStatus(Optional<Gage> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(IGageService.class);
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

        } else if (btnEdit.equals(button)) {

        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                gageService.delete((Gage) objectGrid.getSelectedObject());
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

    public int importsxlsx(byte[] bytes) throws IOException, ParseException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        //*****.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        // 循环行Row
        for (int rowNum = 4; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell nameB = xssfRow.getCell(1);
                XSSFCell descriptionC = xssfRow.getCell(2);
                XSSFCell numberH = xssfRow.getCell(7);
                XSSFCell passL = xssfRow.getCell(11);
                XSSFCell activeDateM = xssfRow.getCell(12);

                //*****.xlsx
                System.out.println("*************" + rowNum);
                String gageNo = numberH.getStringCellValue().trim();
                if(!Strings.isNullOrEmpty(gageNo) && !"-".equals(gageNo.trim())) {
                    gage  = gageService.getByGageNo(gageNo);
                }
                if(gage == null) {
                	coutNum++;
                	gage = new Gage();
                }
                gage.setGageName(nameB != null ? nameB.getStringCellValue() : "");
                gage.setGageNo(numberH != null ? numberH.getStringCellValue() : "~");
                if ("-".equals(activeDateM.toString().trim()) || "".equals(activeDateM.toString().trim())) {
                    System.out.println(activeDateM);
                } else if ("Permanent".equals(activeDateM.toString().trim())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse("2100-01-01");
                    gage.setActiveDate(UDateToZonedDateTime(date));
                } else {
                    Date activeDate = activeDateM != null ? activeDateM.getDateCellValue() : null;
                    gage.setActiveDate(activeDate != null ? UDateToZonedDateTime(activeDate) : null);
                }
                gage.setDescription(descriptionC !=null?descriptionC.getStringCellValue():"");
                gage.setStatus(passL != null ? passL.getStringCellValue():"");
                gageService.save(gage);
            }
        }
        return coutNum;
    }
    
    public int importsxls(byte[] bytes) throws IOException, ParseException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        //*****.xls
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        // 循环行Row
        for (int rowNum = 4; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                HSSFCell nameB = hssfRow.getCell(1);
                HSSFCell descriptionC = hssfRow.getCell(2);
                HSSFCell numberH = hssfRow.getCell(7);
                HSSFCell activeDateM = hssfRow.getCell(12);
                HSSFCell passL = hssfRow.getCell(11);
                //*****.xlsx
                System.out.println("*************" + rowNum);
                String gageNo = numberH.getStringCellValue().trim();
                if(!Strings.isNullOrEmpty(gageNo) && !"-".equals(gageNo.trim())) {
                    gage  = gageService.getByGageNo(gageNo);
                }
                if(gage == null) {
                	coutNum++;
                	gage = new Gage();
                }
                gage.setGageName(nameB != null ? nameB.getStringCellValue() : "");
                gage.setGageNo(numberH != null ? numberH.getStringCellValue() : "~");
                if ("-".equals(activeDateM.toString().trim()) || "".equals(activeDateM.toString().trim())) {
                    System.out.println(activeDateM);
                } else if ("Permanent".equals(activeDateM.toString().trim())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse("2100-01-01");
                    gage.setActiveDate(UDateToZonedDateTime(date));
                } else {
                    Date activeDate = activeDateM != null ? activeDateM.getDateCellValue() : null;
                    gage.setActiveDate(activeDate != null ? UDateToZonedDateTime(activeDate) : null);
                }
                gage.setDescription(descriptionC !=null?descriptionC.getStringCellValue():"");
                gage.setStatus(passL != null ? passL.getStringCellValue():"");
                gageService.save(gage);
            }
        }
        return coutNum;
    }

    public ZonedDateTime UDateToZonedDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.atZone(ZoneId.systemDefault());
    }


}
