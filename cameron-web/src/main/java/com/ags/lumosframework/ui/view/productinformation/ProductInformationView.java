package com.ags.lumosframework.ui.view.productinformation;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.service.IProductInformationService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.ags.lumosframework.web.vaadin.component.searchpanel.SearchPanelBuilder;
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
import java.util.Optional;

@Menu(caption = "ProductInformation" , captionI18NKey = "ProductInformation.view.caption" ,iconPath = "images/icon/text-blob.png", groupName ="Data", order = 4 )
@SpringView(name = "ProductInformation", ui = CameronUI.class)
@Secured("ProductInformation")
public class ProductInformationView extends BaseView implements Button.ClickListener, IFilterableView  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3686528550247110745L;
	
//	@Secured(PermissionConstants.POST_ADD) 
	@I18Support(caption = "Add", captionKey = "common.add")
	private Button btnAdd = new Button();
//
//	@Secured(PermissionConstants.POST_EDIT)
	@I18Support(caption = "Edit", captionKey = "common.edit")
	private Button btnEdit = new Button();
//
//	@Secured(PermissionConstants.POST_DELETE)
	@I18Support(caption = "Delete", captionKey = "common.delete")
	private Button btnDelete = new Button();
//
//	@Secured(PermissionConstants.POST_REFRESH)
	@I18Support(caption = "Refresh", captionKey = "common.refresh")
	private Button btnRefresh = new Button();

	@I18Support(caption = "Review", captionKey = "common.review")
	private Button btnReview = new Button();
	
	@I18Support(caption = "Import Excel", captionKey = "common.import") // ProductionOrder.ImportExcel
	private UploadButton upload = new UploadButton();
	
	private Button[] btns = new Button[] { btnAdd, btnEdit, btnDelete, btnRefresh, btnReview };
	
	private UploadFinishEvent uploadEvent = null;
	// 查询区域控件
	@I18Support(caption = "ProductId", captionKey = "ProductInformation.ProductId")
	private TextField tfProductId = new TextField();

	@I18Support(caption = "ProductVersionId", captionKey = "ProductInformation.ProductVersionId")
	private TextField tfProductVerId = new TextField();

	@I18Support(caption = "Search", captionKey = "common.search")
	private Button btnSearch = new Button();

	AbstractComponent[] fields = { tfProductId, tfProductVerId, btnSearch };

	private HorizontalLayout hlToolBox = new HorizontalLayout();

//	private IObjectListGrid<Post> objectGrid = new PaginationObjectListGrid<>(false);
	private IDomainObjectGrid<ProductInformation> objectGrid = new PaginationDomainObjectList<>();

	@Autowired
	private IProductInformationService productInformationService;

	@Autowired
	private AddProductInformationDialog addProductInformationDialog;

	public ProductInformationView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		SearchPanelBuilder sp = new SearchPanelBuilder(BeanManager.getService(ProductInformationConditions.class), objectGrid, this);
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
		btnReview.setIcon(VaadinIcons.CHECK_SQUARE_O);

		FileUploader fileUploader = new FileUploader(new UploadFinishedListener() {
			@Override
			public void finish(UploadFinishEvent event) {
				uploadEvent = event;
			}
		});

		// upload.addStyleName("upload-small");
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
						NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
					} catch (Exception e) {
						e.printStackTrace();
						NotificationUtils.notificationError("导入发生异常,请确认数据模板是否正确");
					}

			}
		});
		hlTempToolBox.addComponent(upload);

		objectGrid.addColumn(source -> {
			return source.isReviewed() == true ? "√" : "×";
		}).setCaption(I18NUtility.getValue("Common.Reviewed","Reviewed"));
		objectGrid.addColumn(ProductInformation::getProductId).setCaption(I18NUtility.getValue("ProductInformation.ProductId", "ProductId"));
		objectGrid.addColumn(ProductInformation::getProductVersionId).setCaption(I18NUtility.getValue("ProductInformation.ProductVersionId", "ProductVersionId"));
		objectGrid.addColumn(ProductInformation::getProductDesc).setCaption(I18NUtility.getValue("ProductInformation.ProductDesc", "ProductDesc"));
		objectGrid.addColumn(ProductInformation::getTemperatureRating).setCaption(I18NUtility.getValue("ProductInformation.TemperatureRating", "TemperatureRating"));
		objectGrid.addColumn(ProductInformation::getMaterialRating).setCaption(I18NUtility.getValue("ProductInformation.MaterialRating", "MaterialRating"));
		objectGrid.addColumn(ProductInformation::getPSLRating).setCaption(I18NUtility.getValue("ProductInformation.PSLRating", "PSLRating"));
		objectGrid.addColumn(ProductInformation::getQulityPlan).setCaption(I18NUtility.getValue("ProductInformation.QualityPlan","Quality Plan"));
		objectGrid.addColumn(ProductInformation::getQulityPlanRev).setCaption(I18NUtility.getValue("ProductInformation.QualityPlanRev","Quality Plan Rev"));
		objectGrid.addColumn(ProductInformation::getPressureInspectionProcedure).setCaption(I18NUtility.getValue("ProductInformation.PressureInspectionProcedure", "PressureInspectionProcedure"));
		objectGrid.addColumn(ProductInformation::getPressureInspectionProcedureVersion).setCaption(I18NUtility.getValue("ProductInformation.PressureInspectionProcedureVersion", "PressureInspectionProcedureVersion"));
		objectGrid.addColumn(ProductInformation::getBlowdownTorque).setCaption(I18NUtility.getValue("ProductInformation.BlowdownTorque","Torque"));
		objectGrid.addColumn(ProductInformation::getGasTest).setCaption(I18NUtility.getValue("ProductInformation.GasTest", "Gas Test"));
		objectGrid.addColumn(ProductInformation::getGasTestRev).setCaption(I18NUtility.getValue("ProductInformation.GasTestRev", "Rev"));
		objectGrid.addColumn(ProductInformation::getLevp).setCaption(I18NUtility.getValue("ProductInformation.Levp", "LEVP"));
		objectGrid.addColumn(ProductInformation::getLevpRev).setCaption(I18NUtility.getValue("ProductInformation.LevpRev", "Rev"));
		objectGrid.addColumn(ProductInformation::getPaintingSpecificationFile).setCaption(I18NUtility.getValue("ProductInformation.PaintingSpecificationFile", "PaintingSpecificationFile"));
		objectGrid.addColumn(ProductInformation::getPaintingSpecificationFileRev).setCaption(I18NUtility.getValue("ProductInformation.PaintingSpecificationFileRev", "PaintingSpecificationFileRev"));
		objectGrid.setObjectSelectionListener(event -> {
			setButtonStatus(event.getFirstSelectedItem());
		});
		vlRoot.addComponents((Component) objectGrid);
		vlRoot.setExpandRatio((Component) objectGrid, 1);

		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<ProductInformation> optional) {
		boolean enable = optional.isPresent();
		btnEdit.setEnabled(enable);
		btnDelete.setEnabled(enable);
	}

	@Override
	protected void init() {
		objectGrid.setServiceClass(IProductInformationService.class);
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
			addProductInformationDialog.setObject(null);
			addProductInformationDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					objectGrid.refresh();
				}
			});
		} else if (btnEdit.equals(button)) {
			ProductInformation productInformation = (ProductInformation) objectGrid.getSelectedObject();
			addProductInformationDialog.setObject(productInformation);
			addProductInformationDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					ProductInformation temp = (ProductInformation) result.getObj();
					objectGrid.refresh(temp);
				}
			});
		} else if (btnDelete.equals(button)) {
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								productInformationService.delete((ProductInformation) objectGrid.getSelectedObject());
							} catch (PlatformException e) {
								notificationError("Common.RelationShipCheckFailed", e.getMessage());
								return;
							}
							objectGrid.refresh();
						}
					});
		} else if (btnRefresh.equals(button)) {
			objectGrid.refresh();
		}else if (btnReview.equals(button)){
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToReview", "Are you sure all the information are correct?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								ProductInformation productInformation = (ProductInformation) objectGrid.getSelectedObject();
								ProductInformation productInformation2 =
										productInformationService.getByNoRev(productInformation.getProductId(), productInformation.getProductVersionId());
								productInformation2.setReviewed(true);
								productInformationService.save(productInformation2);
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
		// .xlsx
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

		// 循环行Row
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if (xssfRow != null) {
				XSSFCell productIdCell = xssfRow.getCell(0);
				XSSFCell productRevCell = xssfRow.getCell(1);
				productRevCell.setCellType(CellType.STRING);
				XSSFCell productDescCell = xssfRow.getCell(2);
				XSSFCell productTempLevelCell = xssfRow.getCell(3);
				XSSFCell productMaterialLevelCell = xssfRow.getCell(4);
				XSSFCell productPSLLevelCell = xssfRow.getCell(5);
				productPSLLevelCell.setCellType(CellType.STRING);
//				XSSFCell productPressureTestSpecificationCell = xssfRow.getCell(6);
//				XSSFCell productPressureTestSpecificationRevCell = xssfRow.getCell(7);
//				productPressureTestSpecificationRevCell.setCellType(CellType.STRING);
				ProductInformation productInformation = 
						productInformationService.getByNoRev(productIdCell.getStringCellValue(), productRevCell.getStringCellValue());
				if(productInformation == null) {
					coutNum ++;
					productInformation = new ProductInformation();
					productInformation.setProductId(productIdCell.getStringCellValue());
					productInformation.setProductVersionId(productRevCell.getStringCellValue());
					productInformation.setProductDesc(productDescCell.getStringCellValue());
				}
				
				productInformation.setTemperatureRating(productTempLevelCell.getStringCellValue());
				productInformation.setMaterialRating(productMaterialLevelCell.getStringCellValue());
				productInformation.setPSLRating(productPSLLevelCell.getStringCellValue());
//				productInformation.setPressureInspectionProcedure(productPressureTestSpecificationCell.getStringCellValue());
//				productInformation.setPressureInspectionProcedureVersion(productPressureTestSpecificationRevCell.getStringCellValue());
				productInformationService.save(productInformation);
			}
		}
		xssfWorkbook.close();
		return coutNum;
	}

	public int importsxls(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
		int coutNum = 0;
		// *****.xls
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

		// 循环行Row
		for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			if (hssfRow != null) {
				HSSFCell productIdCell = hssfRow.getCell(0);
				HSSFCell productRevCell = hssfRow.getCell(1);
				productRevCell.setCellType(CellType.STRING);
				HSSFCell productDescCell = hssfRow.getCell(2);
				HSSFCell productTempLevelCell = hssfRow.getCell(3);
				HSSFCell productMaterialLevelCell = hssfRow.getCell(4);
				HSSFCell productPSLLevelCell = hssfRow.getCell(5);
				productPSLLevelCell.setCellType(CellType.STRING);
//				HSSFCell productPressureTestSpecificationCell = hssfRow.getCell(6);
//				HSSFCell productPressureTestSpecificationRevCell = hssfRow.getCell(7);
//				productPressureTestSpecificationRevCell.setCellType(CellType.STRING);
				ProductInformation productInformation = 
						productInformationService.getByNoRev(productIdCell.getStringCellValue(), productRevCell.getStringCellValue());
				if(productInformation == null) {
					coutNum ++ ;
					productInformation = new ProductInformation();
					productInformation.setProductId(productIdCell.getStringCellValue());
					productInformation.setProductVersionId(productRevCell.getStringCellValue());
					productInformation.setProductDesc(productDescCell.getStringCellValue());
				}
				
				productInformation.setTemperatureRating(productTempLevelCell.getStringCellValue());
				productInformation.setMaterialRating(productMaterialLevelCell.getStringCellValue());
				productInformation.setPSLRating(productPSLLevelCell.getStringCellValue());
//				productInformation.setPressureInspectionProcedure(productPressureTestSpecificationCell.getStringCellValue());
//				productInformation.setPressureInspectionProcedureVersion(productPressureTestSpecificationRevCell.getStringCellValue());
				productInformationService.save(productInformation);
			}
		}
		hssfWorkbook.close();
		return coutNum;
	}

}
