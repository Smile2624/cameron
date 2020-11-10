package com.ags.lumosframework.ui.view.hardness;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.Hardness;
import com.ags.lumosframework.service.IHardnessService;
import com.ags.lumosframework.ui.CameronUI;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Menu(caption = "Hardness", captionI18NKey = "Cameron.Hardness", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 8)
@SpringView(name = "Hardness", ui = CameronUI.class)
@Secured("Hardness")
public class HardnessView extends BaseView implements Button.ClickListener, IFilterableView {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9050240850855997340L;

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
    
	@I18Support(caption = "Import", captionKey = "common.import")
	private UploadButton upload = new UploadButton();

    private Button[] btns = new Button[]{ btnEdit, btnDelete, btnRefresh};//btnDelete,btnAdd,

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private IDomainObjectGrid<Hardness> objectGrid = new PaginationDomainObjectList<>();
    
    private UploadFinishEvent uploadEvent = null;

    @Autowired
    private AddHardnessDialog addHardnessDialog;

    @Autowired
    private IHardnessService hardnessService;

    public HardnessView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        SearchPanelBuilder c = new SearchPanelBuilder((IConditions) BeanManager.getService(HardnessConditions.class), this.objectGrid, this);
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
        hlTempToolBox.addComponent(upload);
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
					} else if(fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
						num = importsxls(uploadEvent.getUploadFileInByte());
					}else {
                        NotificationUtils.notificationError("错误文件！");
                    }

					NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
				} catch (Exception e) {
					e.printStackTrace();
					NotificationUtils.notificationError("导入发生异常,请确认数据模板是否正确" );
				}
			}

		});
//        objectGrid.addColumn(Hardness::getHardnessName).setCaption(I18NUtility.getValue("Hardness.hardnessName", "hardnessName"));
        objectGrid.addColumn(Hardness::getHardnessStand).setCaption(I18NUtility.getValue("Hardness.hardnessStand", "hardnessStand"));
        objectGrid.addColumn(Hardness::getHardnessUpLimit).setCaption(I18NUtility.getValue("Hardness.hardnessUpLimit", "hardnessUpLimit"));
        objectGrid.addColumn(Hardness::getHardnessDownLimit).setCaption(I18NUtility.getValue("Hardness.hardnessDownLimit", "hardnessDownLimit"));

        objectGrid.setObjectSelectionListener(event -> {
            setButtonStatus(event.getFirstSelectedItem());
        });
        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<Hardness> optional) {
        boolean enable = optional.isPresent();
        btnEdit.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }

    @Override
    protected void init() {
        objectGrid.setServiceClass(IHardnessService.class);
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
            addHardnessDialog.setObject(null);
            addHardnessDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    objectGrid.refresh();
                }
            });
        } else if (btnEdit.equals(button)) {
            Hardness hardness = (Hardness) objectGrid.getSelectedObject();
            addHardnessDialog.setObject(hardness);
            addHardnessDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    Hardness temp = (Hardness) result.getObj();
                    objectGrid.refresh(temp);
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                hardnessService.delete((Hardness) objectGrid.getSelectedObject());
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
    
    /**
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * 
	 *导入。xlsx格式的文件
	 */
	public int importsxlsx(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		int sheetCount = xssfWorkbook.getNumberOfSheets();// 文件中含有多个sheet,需要便利每个sheet，知道出现空值的时候结束遍历
		List<Hardness> hardnessList = new ArrayList<>();
		for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
			XSSFSheet xssfsheet = xssfWorkbook.getSheetAt(sheetIndex);
			int rowCount = xssfsheet.getLastRowNum();
			for(int rowIndex = 0; rowIndex <= rowCount && rowCount !=0  ; rowIndex ++) {
				XSSFRow xssfrow = xssfsheet.getRow(rowIndex);
				XSSFCell rulerNameCell = xssfrow.getCell(0);
				XSSFCell valueCell = xssfrow.getCell(1);
				float minValue = 0;
				float maxValue = 0;
				String rulerName = rulerNameCell.getStringCellValue();
				String value = valueCell.getStringCellValue();//范围+" " + 单位，需要处理获取最大值最小值
//				System.out.println(value);
				if(value.indexOf("HBW") > -1) {
					value = value.replace("HBW", "").replace(" ", "");
				}
				System.out.println(value);
				if(value.indexOf("-")> -1) {
					minValue = Float.parseFloat(value.split("-")[0]);
					maxValue = Float.parseFloat(value.split("-")[1]);
				}else if(value.indexOf("–")> -1){
					minValue = Float.parseFloat(value.split("–")[0]);
					maxValue = Float.parseFloat(value.split("–")[1]);
				}else {
					minValue = Float.parseFloat(value);
					maxValue = Float.parseFloat(value);
				}
				Hardness hardness = hardnessService.getByRuleName(rulerName);
				if(hardness == null) {
					Hardness instance = new Hardness();
					instance.setHardnessStand(rulerName);
					instance.setHardnessDownLimit(minValue);
					instance.setHardnessUpLimit(maxValue);
					hardnessList.add(instance);
				}

			}
		}
		hardnessService.saveAll(hardnessList);
		xssfWorkbook.close();
		return hardnessList.size();
	}

	public int importsxls(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
		int sheetCount = xssfWorkbook.getNumberOfSheets();// 文件中含有多个sheet,需要便利每个sheet，知道出现空值的时候结束遍历
		List<Hardness> hardnessList = new ArrayList<>();
		for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
			HSSFSheet xssfsheet = xssfWorkbook.getSheetAt(sheetIndex);
			int rowCount = xssfsheet.getLastRowNum();
			for(int rowIndex = 0; rowIndex <= rowCount && rowCount !=0  ; rowIndex ++) {
				HSSFRow xssfrow = xssfsheet.getRow(rowIndex);
				HSSFCell rulerNameCell = xssfrow.getCell(0);
				HSSFCell valueCell = xssfrow.getCell(1);
				float minValue = 0;
				float maxValue = 0;
				String rulerName = rulerNameCell.getStringCellValue();
				String value = valueCell.getStringCellValue();//范围+" " + 单位，需要处理获取最大值最小值
//				System.out.println(value);
				if(value.indexOf("HBW") > -1) {
					value = value.replace("HBW", "").replace(" ", "");
				}
				System.out.println(value);
				if(value.indexOf("-")> -1) {
					minValue = Float.parseFloat(value.split("-")[0]);
					maxValue = Float.parseFloat(value.split("-")[1]);
				}else if(value.indexOf("–")> -1){
					minValue = Float.parseFloat(value.split("–")[0]);
					maxValue = Float.parseFloat(value.split("–")[1]);
				}else {
					minValue = Float.parseFloat(value);
					maxValue = Float.parseFloat(value);
				}
				Hardness hardness = hardnessService.getByRuleName(rulerName);
				if(hardness == null) {
					Hardness instance = new Hardness();
					instance.setHardnessStand(rulerName);
					instance.setHardnessDownLimit(minValue);
					instance.setHardnessUpLimit(maxValue);
					hardnessList.add(instance);
				}

			}
		}
		hardnessService.saveAll(hardnessList);
		xssfWorkbook.close();
		return hardnessList.size();
	}
}
