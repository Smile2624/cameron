package com.ags.lumosframework.ui.view.materialinspection;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
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
import java.util.*;

@Menu(caption = "MaterialInspectionListView", captionI18NKey = "view.materialinspection.caption", iconPath = "images/icon/text-blob.png",groupName="Data", order = 13)
@SpringView(name = "MaterialInspectionListView", ui = CameronUI.class)
@Secured("MaterialInspectionListView")
public class MaterialInspectionListView extends BaseView implements Button.ClickListener ,IFilterableView{

	private static final long serialVersionUID = 5873541754306366599L;

//	@Secured(PermissionConstants.INSPECTIONTYPE_ADD)
	@I18Support(caption = "Add", captionKey = "common.add")
	private Button btnAdd = new Button();

//	@Secured(PermissionConstants.INSPECTIONTYPE_EDIT)
	@I18Support(caption = "Edit", captionKey = "common.edit")
	private Button btnEdit = new Button();

//	@Secured(PermissionConstants.INSPECTIONTYPE_DELETE)
	@I18Support(caption = "Delete", captionKey = "common.delete")
	private Button btnDelete = new Button();

//	@Secured(PermissionConstants.INSPECTIONTYPE_REFRESH)
	@I18Support(caption = "Refresh", captionKey = "common.refresh")
	private Button btnRefresh = new Button();
	
	@I18Support(caption="Import",captionKey="common.import")
	private UploadButton upload = new UploadButton();
	
	private TextField tfPurchasingNo = new TextField();
	
	private Button btnSearch = new Button();
	
	private UploadFinishEvent uploadEvent = null;
	
	private Button[] btns = new Button[] { btnEdit, btnDelete ,btnRefresh };//btnAdd,
	
	private HorizontalLayout hlToolBox = new HorizontalLayout();
	

    private Grid<PurchasingOrderInfo> objectGrid = new Grid<>();

    private Grid<PurchasingOrderInfo> objectItemGrid = new Grid<>();
	
    @Autowired
    private EditPurchasingOrderInfoDialog editPurchasingOrderInfoDialog;
	@Autowired
	private IPurchasingOrderService purchasingOrderService;
	
	
	public  MaterialInspectionListView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();
		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		
        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);
        hlTemp.addComponents(tfPurchasingNo , btnSearch);
        tfPurchasingNo.setPlaceholder(I18NUtility.getValue("view.materialinspection.purchasingno", "PurchasingNo"));
		for (Button btn : btns) {
			hlTempToolBox.addComponent(btn);
			btn.addClickListener(this);
			btn.setDisableOnClick(true);
		}
		btnAdd.setIcon(VaadinIcons.PLUS);
		btnEdit.setIcon(VaadinIcons.EDIT);
		btnDelete.setIcon(VaadinIcons.TRASH);
		btnRefresh.setIcon(VaadinIcons.REFRESH);
		btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("primary");
        btnSearch.addClickListener(this);
		//hlTempToolBox.addComponent(upload);
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
					refreshGrid();
                    NotificationUtils.notificationInfo("成功导入,共" + num + "条记录");
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationUtils.notificationError("导入发生异常,请确认文件格式是否正确");
                }
            }


        });
		upload.setIcon(VaadinIcons.UPLOAD);
		
		 HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
	        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
	        hlSplitPanel.setSizeFull();
	        vlRoot.addComponent(hlSplitPanel);
	        vlRoot.setExpandRatio(hlSplitPanel, 1);

	        objectGrid.setSizeFull();
	        objectGrid.addColumn(PurchasingOrderInfo::getPurchasingNo).setCaption(I18NUtility.getValue("view.materialinspection.purchasingno", "PurchasingNo"));
//	        objectGrid.addColumn(PurchasingOrderInfo::getVendorName).setCaption(I18NUtility.getValue("view.materialinspection.vendorname", "Vendor"));
	        objectGrid.addColumn(PurchasingOrderInfo::getMaterialDesc).setCaption(I18NUtility.getValue("view.materialinspection.materialdesc", "MaterialDesc"));
	        objectGrid.addSelectionListener(event -> {
	            Optional<PurchasingOrderInfo> optional = event.getFirstSelectedItem();
	            if (optional.isPresent()) {
	                setDateToItem(optional.get());
					btnDelete.setEnabled(true);
	            } else {
	                setDateToItem(null);
					btnDelete.setEnabled(false);
	            }
	        });
	        hlSplitPanel.setFirstComponent((Component) objectGrid);

	        objectItemGrid.setSizeFull();
	        objectItemGrid.addColumn(PurchasingOrderInfo::getSapInspectionLot).setCaption(I18NUtility.getValue("view.materialinspection.sapinspectionlot", "SapInspectionLot"));
	        objectItemGrid.addColumn(PurchasingOrderInfo::getPurchasingItemNo).setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"));
	        objectItemGrid.addColumn(PurchasingOrderInfo::getMaterialNo).setCaption(I18NUtility.getValue("view.materialinspection.materialno", "MaterialNo"));
	        objectItemGrid.addColumn(PurchasingOrderInfo::getMaterialRev).setCaption(I18NUtility.getValue("view.materialinspection.materialrev", "MaterialRev"));
	        objectItemGrid.addColumn(PurchasingOrderInfo::getMaterialQuantity).setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "MaterialQuantity"));
	        objectItemGrid.addColumn(PurchasingOrderInfo::getInspectionQuantity).setCaption(I18NUtility.getValue("view.materialinspection.inspectionquantity", "InspectionQuantity"));
	        objectItemGrid.addSelectionListener(event -> {
	            setButtonStatus(event.getFirstSelectedItem());
	        });

	        hlSplitPanel.setSecondComponent((Component) objectItemGrid);
		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<PurchasingOrderInfo> firstSelectedItem) {
		if(firstSelectedItem.isPresent()) {
			btnEdit.setEnabled(true);
//			btnDelete.setEnabled(true);
		}else {
			btnEdit.setEnabled(false);
//			btnDelete.setEnabled(false);
		}
	}

	private void setDateToItem(PurchasingOrderInfo purchasingOrderInfo) {
		if(purchasingOrderInfo == null) {
			objectItemGrid.setItems(new ArrayList<PurchasingOrderInfo>());
		}else {
			objectItemGrid.setDataProvider(DataProvider.ofCollection(purchasingOrderService.getByPurchasingNo(purchasingOrderInfo.getPurchasingNo())));
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		button.setEnabled(true);
		if(button.equals(btnEdit)) {
			PurchasingOrderInfo purchasingOrderInfo = objectItemGrid.asSingleSelect().getValue();
			editPurchasingOrderInfoDialog.setObject(purchasingOrderInfo);
			editPurchasingOrderInfoDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    setDateToItem(purchasingOrderInfo);
//                    refreshGrid();
                }
            });
		}else if(button.equals(btnDelete)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"), result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                            	purchasingOrderService.deleteOrderList(objectGrid.asSingleSelect().getValue().getPurchasingNo());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
//                            setDateToItem(objectGrid.asSingleSelect().getValue());
							refreshGrid();
                        }
                    });
		}else if(button.equals(btnRefresh)) {
			tfPurchasingNo.clear();
			refreshGrid();
		}else {
			refreshGrid();
		}
	}
	
	public int importsxlsx(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        
        // 循环行Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
            	
            	XSSFCell sapInspectionLotCell = xssfRow.getCell(0);
                XSSFCell materialNoCell = xssfRow.getCell(1);
                XSSFCell mateirialQuantityCell = xssfRow.getCell(2);
                XSSFCell materialUnitCell  = xssfRow.getCell(3);
                XSSFCell materialDescCell = xssfRow.getCell(4);
                XSSFCell materialRevCell = xssfRow.getCell(5);  
                XSSFCell purchasingNoCell = xssfRow.getCell(6);
                XSSFCell purchasingItemNoCell = xssfRow.getCell(7);
                XSSFCell vendorNameCell = xssfRow.getCell(8);
                sapInspectionLotCell.setCellType(CellType.STRING);
                materialRevCell.setCellType(CellType.STRING);
                purchasingNoCell.setCellType(CellType.STRING);
                purchasingItemNoCell.setCellType(CellType.STRING);
                String sapInspectionLot = sapInspectionLotCell.getStringCellValue();
                String materialNo  = materialNoCell.getStringCellValue();
        		int materialQuantity = (int) mateirialQuantityCell.getNumericCellValue();
        		String materialUnit = materialUnitCell.getStringCellValue();
        		String materialDesc = materialDescCell.getStringCellValue();
        		String materialRev = materialRevCell.getStringCellValue();//可能有缺失
        		//将采购单号的列的格式转成String；
        		purchasingNoCell.setCellType(CellType.STRING);
        		String purchasingNo = purchasingNoCell.getStringCellValue();// getNumericCellValue()
        		String purchasingItemNo =purchasingItemNoCell.getStringCellValue();
        		String vendorName = vendorNameCell.getStringCellValue();
        		
        		PurchasingOrderInfo purchasingInfo = purchasingOrderService.getBySapInspectionLot(sapInspectionLot);
                if(purchasingInfo ==null) {
                	//不存在，写入purchasingorder信息
                	PurchasingOrderInfo purchasingOrder = new PurchasingOrderInfo();
                	purchasingOrder.setSapInspectionLot(sapInspectionLot);
                	purchasingOrder.setMaterialNo(materialNo);
                	purchasingOrder.setMaterialQuantity(materialQuantity);
                	purchasingOrder.setMaterialUnit(materialUnit);
                	purchasingOrder.setMaterialDesc(materialDesc);
                	purchasingOrder.setMaterialRev(materialRev == null ? "":materialRev);
                	purchasingOrder.setPurchasingNo(purchasingNo);
                	purchasingOrder.setPurchasingItemNo(purchasingItemNo);
                	purchasingOrder.setVendorName(vendorName);
                	purchasingOrderService.save(purchasingOrder);
                	coutNum ++;
            	}else {
            		//已经存在，不再显示
            		NotificationUtils.notificationError("当前检验批号:"+sapInspectionLot+"已经存在，请勿重复导入");
            	}
            }
        }
        xssfWorkbook.close();
        return coutNum;
	}
	
	public int importsxls(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        
        // 循环行Row
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
        	
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
            	
            	HSSFCell sapInspectionLotCell = hssfRow.getCell(0);
                HSSFCell materialNoCell = hssfRow.getCell(1);
                HSSFCell mateirialQuantityCell = hssfRow.getCell(2);
                HSSFCell materialUnitCell  = hssfRow.getCell(3);
                HSSFCell materialDescCell = hssfRow.getCell(4);
                HSSFCell materialRevCell = hssfRow.getCell(5);  
                HSSFCell purchasingNoCell = hssfRow.getCell(6);
                HSSFCell purchasingItemNoCell = hssfRow.getCell(7);
                HSSFCell vendorNameCell = hssfRow.getCell(8);
                sapInspectionLotCell.setCellType(CellType.STRING);
                materialRevCell.setCellType(CellType.STRING);
                purchasingNoCell.setCellType(CellType.STRING);
                purchasingItemNoCell.setCellType(CellType.STRING);
                String sapInspectionLot = sapInspectionLotCell.getStringCellValue();
                String materialNo  = materialNoCell.getStringCellValue();
        		int materialQuantity = (int) mateirialQuantityCell.getNumericCellValue();
        		String materialUnit = materialUnitCell.getStringCellValue();
        		String materialDesc = materialDescCell.getStringCellValue();
        		String materialRev = materialRevCell.getStringCellValue();//可能有缺失
        		//将采购单号的列的格式转成String；
        		purchasingNoCell.setCellType(CellType.STRING);
        		String purchasingNo = purchasingNoCell.getStringCellValue();// getNumericCellValue()
        		String purchasingItemNo =purchasingItemNoCell.getStringCellValue();
        		String vendorName = vendorNameCell.getStringCellValue();
        		
        		PurchasingOrderInfo purchasingInfo = purchasingOrderService.getBySapInspectionLot(sapInspectionLot);
                if(purchasingInfo ==null) {
                	//不存在，写入purchasingorder信息
                	PurchasingOrderInfo purchasingOrder = new PurchasingOrderInfo();
                	purchasingOrder.setSapInspectionLot(sapInspectionLot);
                	purchasingOrder.setMaterialNo(materialNo);
                	purchasingOrder.setMaterialQuantity(materialQuantity);
                	purchasingOrder.setMaterialUnit(materialUnit);
                	purchasingOrder.setMaterialDesc(materialDesc);
                	purchasingOrder.setMaterialRev(materialRev == null ? "":materialRev);
                	purchasingOrder.setPurchasingNo(purchasingNo);
                	purchasingOrder.setPurchasingItemNo(purchasingItemNo);
                	purchasingOrder.setVendorName(vendorName);
                	purchasingOrderService.save(purchasingOrder);
                	coutNum ++;
            	}else {
            		//已经存在，不再显示
            		NotificationUtils.notificationError("当前检验批号:"+sapInspectionLot+"已经存在，请勿重复导入");
            	}
            }
        }
        hssfWorkbook.close();
        return coutNum;
	}

	@Override
	public void updateAfterFilterApply() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        refreshGrid();
	}

	private void refreshGrid() {
		List<PurchasingOrderInfo> purchasingOrderList = purchasingOrderService.getByPurchasingNo(tfPurchasingNo.getValue().trim());
		 objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicateBom(purchasingOrderList)));
	}
	
    private ArrayList<PurchasingOrderInfo> removeDuplicateBom(List<PurchasingOrderInfo> list) {
        Set<PurchasingOrderInfo> set = new TreeSet<PurchasingOrderInfo>(new Comparator<PurchasingOrderInfo>() {
            @Override
            public int compare(PurchasingOrderInfo o1, PurchasingOrderInfo o2) {
                return (o1.getPurchasingNo()).compareTo(o2.getPurchasingNo());
            }
        });
        set.addAll(list);
        return new ArrayList<PurchasingOrderInfo>(set);
    }
}
