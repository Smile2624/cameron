package com.ags.lumosframework.ui.view.sparepart;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.enums.RetrospectType;
import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.service.IBomService;
import com.ags.lumosframework.service.ISparePartService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Menu(caption = "SparePart", captionI18NKey = "Cameron.SparePart", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 2)
@SpringView(name = "SparePart", ui = CameronUI.class)
@Secured("SparePart")
public class SparePartView extends BaseView implements Button.ClickListener, IFilterableView {

    /**
     *
     */
    private static final long serialVersionUID = -4058690122811482003L;

    //    @Secured(DemoPermissionConstants.BOOK_ADD)
    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    //    @Secured(DemoPermissionConstants.BOOK_EDIT)
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();

    //    @Secured(DemoPermissionConstants.BOOK_DELETE)
    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();

    @I18Support(caption = "Review", captionKey = "common.review")
    private Button btnReview = new Button();

    //    @Secured(DemoPermissionConstants.BOOK_REFRESH)
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    @I18Support(caption = "ImportExcel", captionKey = "cameron.ImportExcelSparePart")
    private UploadButton upload = new UploadButton();

    private UploadFinishEvent uploadEvent = null;

    private Button[] btns = new Button[]{btnAdd, btnEdit, btnDelete, btnRefresh, btnReview};

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private IDomainObjectGrid<SparePart> objectGrid = new PaginationDomainObjectList<>();

    @Autowired
    private AddSparePartDialog addSparePartDialog;

    @Autowired
    private ISparePartService sparePartService;

    @Autowired
    private IBomService bomService;

    public SparePartView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(SparePartConditions.class), this.objectGrid, this);
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
        btnReview.setIcon(VaadinIcons.CHECK_SQUARE_O);

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
                int num = 0;
                try {
                    String fileName = event.getFilename();
                    if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
                        num = importsxlsx(uploadEvent.getUploadFileInByte());
                    } else if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                        num = importsxls(uploadEvent.getUploadFileInByte());
                    } else {
                        NotificationUtils.notificationError("错误文件！");
                    }
                    objectGrid.refresh();
                    NotificationUtils.notificationInfo("成功修改,共" + num + "条记录");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认数据模板是否正确");
                }
            }


        });
        hlTempToolBox.addComponent(upload);


        objectGrid.addColumn(source -> {
            return source.isReviewed() == true ? "√" : "×";
        }).setCaption(I18NUtility.getValue("Common.Reviewed", "Reviewed"));
        objectGrid.addColumn(SparePart::getSparePartNo).setCaption(I18NUtility.getValue("SparePart.SparePartNo", "SparePartNo"));
        objectGrid.addColumn(SparePart::getSparePartRev).setCaption(I18NUtility.getValue("SparePart.SparePartRev", "SparePartRev"));
        objectGrid.addColumn(SparePart::getSparePartDec).setCaption(I18NUtility.getValue("SparePart.SparePartDec", "SparePartDec"));
        objectGrid.addColumn(SparePart::getPslLevelStand).setCaption(I18NUtility.getValue("SparePart.PslLevelStand", "PslLevelStand"));
        objectGrid.addColumn(SparePart::getApiStand).setCaption(I18NUtility.getValue("SparePart.ApiStand", "ApiStand"));
        objectGrid.addColumn(SparePart::getQaPlan).setCaption(I18NUtility.getValue("SparePart.QaPlan", "QaPlan"));
        objectGrid.addColumn(SparePart::getQaPlanRev).setCaption(I18NUtility.getValue("SparePart.QaPlanRev", "QaPlanRev"));
        objectGrid.addColumn(SparePart::getDrawNo).setCaption(I18NUtility.getValue("SparePart.DrawNo", "DrawNo"));
        objectGrid.addColumn(SparePart::getDrawRev).setCaption(I18NUtility.getValue("SparePart.DrawRev", "Rev"));
        objectGrid.addColumn(SparePart::getHardnessFile).setCaption(I18NUtility.getValue("SparePart.HardnessFile", "MS"));
        objectGrid.addColumn(SparePart::getHardnessRev).setCaption(I18NUtility.getValue("SparePart.HardnessRev", "Rev"));
        objectGrid.addColumn(SparePart::getDNote).setCaption(I18NUtility.getValue("SparePart.DNote", "D-Note"));
        objectGrid.addColumn(SparePart::getDNoteRev).setCaption(I18NUtility.getValue("SparePart.DNoteRev", "Rev"));
        objectGrid.addColumn(SparePart::getCoating).setCaption(I18NUtility.getValue("SparePart.Coating", "Coating"));
        objectGrid.addColumn(SparePart::getCoatingRev).setCaption(I18NUtility.getValue("SparePart.CoatingRev", "Rev"));
        objectGrid.addColumn(SparePart::getWelding).setCaption(I18NUtility.getValue("SparePart.Welding", "Welding"));
        objectGrid.addColumn(SparePart::getWeldingRev).setCaption(I18NUtility.getValue("SparePart.WeldingRev", "Rev"));
//        objectGrid.addColumn(SparePart::getPlmRev).setCaption(I18NUtility.getValue("SparePart.PlmRev", "PlmRev"));
//        objectGrid.addColumn(SparePart::getPartRev).setCaption(I18NUtility.getValue("SparePart.PartRev", "PartRev"));

        //bruce 添加追溯项
        objectGrid.addColumn(source -> {
            return source.isRetrospect() == true ? "是" : "否";
        }).setCaption(I18NUtility.getValue("Bom.IsRetrospect", "IsRetrospect"));
        objectGrid.addColumn(source -> {
            if (source.getRetrospectType() == null || "".equals(source.getRetrospectType())) {
                return "";
            } else {
                return RetrospectType.SINGLE.getType().equals(source.getRetrospectType()) ? "序列号" : "批号";
            }
        }).setCaption(I18NUtility.getValue("Bom.RetrospectType", "RetrospectType"));
        //objectGrid.addColumn(SparePart::getLongText).setCaption(I18NUtility.getValue("Bom.LongText", "LongText"));


        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });
        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<SparePart> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(ISparePartService.class);
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
            addSparePartDialog.setObject(null);
            addSparePartDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    objectGrid.refresh();
                }
            });
        } else if (btnEdit.equals(button)) {
            SparePart sparePart = (SparePart) objectGrid.getSelectedObject();
            addSparePartDialog.setObject(sparePart);
            addSparePartDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    SparePart temp = (SparePart) result.getObj();
                    objectGrid.refresh(temp);
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                sparePartService.delete((SparePart) objectGrid.getSelectedObject());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            objectGrid.refresh();
                        }
                    });
        } else if (btnRefresh.equals(button)) {
            objectGrid.refresh();
        } else if (btnReview.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToReview", "Are you sure all the information are correct?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                SparePart sparePart = (SparePart) objectGrid.getSelectedObject();
                                SparePart sparePart2 =
                                        sparePartService.getByNoRev(sparePart.getSparePartNo(), sparePart.getSparePartRev());
                                sparePart2.setReviewed(true);
                                sparePartService.save(sparePart2);
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            objectGrid.refresh();
                        }
                    });
        }
    }

    @Override
    public void updateAfterFilterApply() {

    }

    public int importsxlsx(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        //*****.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        //*****.xls
//        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
//        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        List<SparePart> spList = new ArrayList<SparePart>();

        // 循环行Row
//		for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
        for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
//			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (xssfRow != null) {
//			  HSSFCell material = hssfRow.getCell(0);

                XSSFCell material = xssfRow.getCell(0);
                material.setCellType(CellType.STRING);
                String sparePartNo = material.getStringCellValue();
                SparePart sparePartAdd = new SparePart();
                sparePartAdd.setSparePartNo(sparePartNo);
                //不为空 且  spList 中为标记 就继续取
                if (!Strings.isNullOrEmpty(sparePartNo) && !checkIsAdded(spList, sparePartAdd)) {
//				  HSSFCell characteristicName = hssfRow.getCell(3);
                    XSSFCell characteristicName = xssfRow.getCell(3);
                    if (AppConstant.RETROSPECTREQUIRE.equals(characteristicName.getStringCellValue())) {
                        //添加标记表示 这个零件号已处理追溯类型
                        spList.add(sparePartAdd);
                        List<SparePart> SparePartSavedList = sparePartService.getByNo(sparePartNo);
                        if (SparePartSavedList != null && SparePartSavedList.size() > 0) {
//						  HSSFCell characteristicCell  = hssfRow.getCell(5);
                            XSSFCell characteristicCell = xssfRow.getCell(5);
                            String characteristicValue = characteristicCell.getStringCellValue();
                            String[] arr = characteristicValue.split("-");
                            if (arr != null && arr.length > 0) {
                                for (SparePart sparePart : SparePartSavedList) {
                                    if (AppConstant.RETROSPECTSINGLE.equals(arr[0])) {
                                        sparePart.setRetrospect(true);
                                        sparePart.setRetrospectType(RetrospectType.SINGLE.getType());
                                        updateBomRetrospectinfo(sparePartNo, RetrospectType.SINGLE.getType());
                                    } else if (AppConstant.RETROSPECTBATCH.equals(arr[0])) {
                                        sparePart.setRetrospect(true);
                                        sparePart.setRetrospectType(RetrospectType.BATCH.getType());
                                        updateBomRetrospectinfo(sparePartNo, RetrospectType.BATCH.getType());
                                    } else {
                                        sparePart.setRetrospect(false);
                                    }
                                    sparePartService.save(sparePart);
                                }
                                coutNum++;
                            }
                        }
                    }
                }
            }
        }
        xssfWorkbook.close();
        return coutNum;
    }

    public int importsxls(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        //*****.xlsx

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        List<SparePart> spList = new ArrayList<SparePart>();

        // 循环行Row
        for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                HSSFCell material = hssfRow.getCell(0);

                material.setCellType(CellType.STRING);
                String sparePartNo = material.getStringCellValue();
                SparePart sparePartAdd = new SparePart();
                sparePartAdd.setSparePartNo(sparePartNo);
                //不为空 且  spList 中为标记 就继续取
                if (!Strings.isNullOrEmpty(sparePartNo) && !checkIsAdded(spList, sparePartAdd)) {
                    HSSFCell characteristicName = hssfRow.getCell(3);
                    if (AppConstant.RETROSPECTREQUIRE.equals(characteristicName.getStringCellValue())) {
                        //添加标记表示 这个零件号已处理追溯类型
                        spList.add(sparePartAdd);
                        List<SparePart> SparePartSavedList = sparePartService.getByNo(sparePartNo);
                        if (SparePartSavedList != null && SparePartSavedList.size() > 0) {
                            HSSFCell characteristicCell = hssfRow.getCell(5);
                            String characteristicValue = characteristicCell.getStringCellValue();
                            String[] arr = characteristicValue.split("-");
                            if (arr != null && arr.length > 0) {
                                for (SparePart sparePart : SparePartSavedList) {
                                    if (AppConstant.RETROSPECTSINGLE.equals(arr[0])) {
                                        sparePart.setRetrospect(true);
                                        sparePart.setRetrospectType(RetrospectType.SINGLE.getType());
                                        //将Bom表中的该版本的零件追溯信息同时更改
                                        updateBomRetrospectinfo(sparePartNo, RetrospectType.SINGLE.getType());
                                    } else if (AppConstant.RETROSPECTBATCH.equals(arr[0])) {
                                        sparePart.setRetrospect(true);
                                        sparePart.setRetrospectType(RetrospectType.BATCH.getType());
                                        updateBomRetrospectinfo(sparePartNo, RetrospectType.BATCH.getType());
                                    } else {
                                        sparePart.setRetrospect(false);
                                    }
                                    sparePartService.save(sparePart);
                                }
                                coutNum++;
                            }
                        }
                    }
                }
            }
        }
        hssfWorkbook.close();
        return coutNum;
    }

    public boolean checkIsAdded(List<SparePart> spList, SparePart sparePart) {
        boolean flag = false;
        String sparePartNo = sparePart.getSparePartNo();
        for (SparePart sparePartObj : spList) {
            String sparePartAdded = sparePartObj.getSparePartNo();
            if (!Strings.isNullOrEmpty(sparePartNo) && sparePartNo.equals(sparePartAdded)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void updateBomRetrospectinfo(String partNo, String type) {
        List<Bom> bomList = bomService.getByMaterialNo(partNo);
        for (Bom instance : bomList) {
            instance.setRetrospect(true);
            instance.setRetrospectType(type);
        }
        bomService.saveAll(bomList);
    }
}
