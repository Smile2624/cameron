package com.ags.lumosframework.ui.view.uniquedimensionreport;

import com.ags.lumosframework.pojo.UniqueDimensionInspection;
import com.ags.lumosframework.pojo.UniqueDimensionInspectionResult;
import com.ags.lumosframework.service.IUniqueDimensionInspectionResultService;
import com.ags.lumosframework.service.IUniqueDimensionInspectionService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.FileUploader;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishEvent;
import com.ags.lumosframework.web.vaadin.component.uploadlistener.UploadFinishedListener;
import com.google.common.base.Strings;
import com.vaadin.data.ValueProvider;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.uploadbutton.UploadButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

//@Menu(caption = "UniqueDimensionInspectionView", captionI18NKey = "view.uniquedimensioninspection.caption", iconPath = "images/icon/text-blob.png", groupName = "VendorInfo", order = 0)
@SpringView(name = "UniqueDimensionInspectionView", ui = CameronUI.class)
@Secured("administration:manage")
public class UniqueDimensionInspectionView extends BaseView implements Button.ClickListener{

	private static final long serialVersionUID = -2681386474724240649L;

	@I18Support(caption = "Add", captionKey = "common.add")
	private Button btnAdd = new Button();

	@I18Support(caption = "Edit", captionKey = "common.edit")
	private Button btnEdit = new Button();

	@I18Support(caption = "Delete", captionKey = "common.delete")
	private Button btnDelete = new Button();

	@I18Support(caption = "Refresh", captionKey = "common.refresh")
	private Button btnRefresh = new Button();
	
	@I18Support(caption="Import",captionKey="common.import")
	private UploadButton upload = new UploadButton();
	
	private TextField tfPurchasingNo = new TextField();
	
	private Button btnSearch = new Button();
	
	private UploadFinishEvent uploadEvent = null;
	
	private Button[] btns = new Button[] {  btnRefresh };//btnAdd ,btnEdit, btnDelete,
	
	private HorizontalLayout hlToolBox = new HorizontalLayout();
	

    private Grid<UniqueDimensionInspection> objectGrid = new Grid<>();

    private Grid<UniqueDimensionInspection> objectItemGrid = new Grid<>();
	
	@Autowired
	private IUniqueDimensionInspectionService uniqueDimensionInspectionService;
	@Autowired
	private IUniqueDimensionInspectionResultService uniqueDimensionInspectionResultService;
	
	List<String> inspectionItemsList = new ArrayList<>();//保存每一个sheet的检验项。sheet中的第四行第1列开始
	
	private UniqueDimensionInspection selectedInstance= null;
	
	List<UniqueDimensionInspection> uniqueDimensionInspectionList = new ArrayList<>();
	
	private boolean pass= true;//表示该检验单是否通过检验
	public  UniqueDimensionInspectionView() {
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
                try {
                	String fileName = event.getFilename();
                	if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
                		importsxlsx(uploadEvent.getUploadFileInByte());
                	}else if(fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
                		importsxls(uploadEvent.getUploadFileInByte());
                	}else {
                		NotificationUtils.notificationError("错误文件！");
                	}
                    
                    NotificationUtils.notificationInfo("数据导入成功");
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
	        objectGrid.addComponentColumn(new ValueProvider<UniqueDimensionInspection, Label>() {

				private static final long serialVersionUID = 327387132463211717L;

				@Override
				public Label apply(UniqueDimensionInspection source) {
					UniqueDimensionInspectionResult result = uniqueDimensionInspectionResultService.getByPo(source.getPoNo());
					Label lblPass = new Label();
					lblPass.setSizeFull();
					if(result.getPass()) {
						lblPass.setValue("合格");
						lblPass.addStyleName(CoreTheme.BACKGROUND_GREEN);
					}else {
						lblPass.setValue("不合格");
						lblPass.addStyleName(CoreTheme.BACKGROUND_RED);
					}
					return lblPass;
				}
			}).setCaption("检验结果").setWidth(90);
	        objectGrid.addColumn(UniqueDimensionInspection::getPoNo).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.po", "PO"));
	        objectGrid.addColumn(UniqueDimensionInspection::getMaterialNo).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.materialno", "MaterialNo"));
	        objectGrid.addColumn(UniqueDimensionInspection::getMaterialRev).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.materialrev", "MaterialRev"));
	        objectGrid.addSelectionListener(event -> {
	            Optional<UniqueDimensionInspection> optional = event.getFirstSelectedItem();
	            if (optional.isPresent()) {
	            	selectedInstance = optional.get();
	                setDateToItem(optional.get());
	            } else {
	            	selectedInstance = null;
	                setDateToItem(null);
	            }
	        });
	        hlSplitPanel.setFirstComponent((Component) objectGrid);

	        objectItemGrid.setSizeFull();
	        objectItemGrid.addColumn(UniqueDimensionInspection::getSerialSN).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.sn", "SerialSN"));
	        objectItemGrid.addColumn(UniqueDimensionInspection::getInspectionItem).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.item", "InspectionItem"));
	        objectItemGrid.addColumn(UniqueDimensionInspection::getInspectionValue).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.value", "InspectionValue"));
	        objectItemGrid.addColumn(UniqueDimensionInspection::getInspectionResult).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.result", "InspectionResult"));
	        objectItemGrid.addColumn(UniqueDimensionInspection::getGaugeSN).setCaption(I18NUtility.getValue("view.uniquedimensioninspectionview.gaguesn", "GaugeSN"));
	        objectItemGrid.addSelectionListener(event -> {
	            setButtonStatus(event.getFirstSelectedItem());
	        });

	        hlSplitPanel.setSecondComponent((Component) objectItemGrid);
		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<UniqueDimensionInspection> firstSelectedItem) {
		if(firstSelectedItem.isPresent()) {
			btnEdit.setEnabled(true);
			btnDelete.setEnabled(true);
		}else {
			btnEdit.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	private void setDateToItem(UniqueDimensionInspection uniqueDimensionInspection) {
		if(uniqueDimensionInspection == null) {
			objectItemGrid.setItems(new ArrayList<UniqueDimensionInspection>());
		}else {
			objectItemGrid.setDataProvider(DataProvider.ofCollection(uniqueDimensionInspectionService.getByPoAndMaterailInfo(selectedInstance.getPoNo(), selectedInstance.getMaterialNo(), selectedInstance.getMaterialRev())));
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		button.setEnabled(true);
		if(button.equals(btnEdit)) {
		}else if(button.equals(btnDelete)) {
		}else if(button.equals(btnRefresh)) {
			tfPurchasingNo.clear();
			refreshGrid(tfPurchasingNo.getValue().trim());
		}else {
			refreshGrid(tfPurchasingNo.getValue().trim());
		}
	}
	
	public int importsxlsx(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        int sheetCount = xssfWorkbook.getNumberOfSheets();
        // 循环行Row
		String inspector = "";
		String inspectionDate = "";
		String vendor = "";
		int orderQuantity= 0;
		String materialInfo = "";
		String drawInfo = "";
		String po= "";
		int inspectionQuantity= 0;
		String description = "";
		String appearanceIns = "";
        UniqueDimensionInspectionResult inspectionResult = new UniqueDimensionInspectionResult();
        for(int sheetIndex = 0 ; sheetIndex < sheetCount ; sheetIndex ++) {
        	XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
        	//每个sheet需要更新一次变量值
            inspectionItemsList.clear();
            uniqueDimensionInspectionList.clear();
            po = getMergedRegionValue(xssfSheet,2,10);
            if(uniqueDimensionInspectionResultService.getByPo(po) == null ) {
            	NotificationUtils.notificationError("当前采购Po:" + po + "已经导入，请勿重复导入");
            	break;
            }
        	for(int rowIndex = 0 ; rowIndex < xssfSheet.getLastRowNum() ; rowIndex ++) {
        		XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
        		//第一行--获取检验员，检验日期数量
        		if(sheetIndex == 0) {
        			//订单检验信息，只需要获取一次
        			if(rowIndex == 1) {
    					inspector =getMergedRegionValue(xssfSheet,rowIndex,1);//检验员
    					SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
            			double longDate = Double.parseDouble(getMergedRegionValue(xssfSheet,rowIndex,5));//日期
            			Date date = DateUtil.getJavaDate(longDate);
            			inspectionDate =sdf.format(date);
            			vendor = getMergedRegionValue(xssfSheet,rowIndex,10);//供应商
            			XSSFCell cell = xssfRow.getCell(15);
            			orderQuantity = (int)cell.getNumericCellValue();//数量
            			
            		}
            		if(rowIndex == 2) {
            			materialInfo = getMergedRegionValue(xssfSheet,rowIndex,1);
            			drawInfo = getMergedRegionValue(xssfSheet,rowIndex,5);
            			XSSFCell cell = xssfRow.getCell(15);
            			inspectionQuantity = (int)cell.getNumericCellValue();//数量
            		}
            		if(rowIndex == 3) {
            			description = getMergedRegionValue(xssfSheet,rowIndex,1);
            		}
        		}
        		
        		if(rowIndex == 5) {
        			//从第一列到第14列表示检测项的类容，15列表示戒烟结果
        			for(int columnIndex = 0;columnIndex < 14 ; columnIndex ++) {
        				XSSFCell cell = xssfRow.getCell(columnIndex + 1);
        				cell.setCellType(CellType.STRING);
        				String inspectionName = cell.getStringCellValue();
        				if(inspectionName != null && inspectionName.length() > 0) {
        					inspectionItemsList.add(inspectionName);
        				}else {
        					break;
        				}
        			}
        		}
        		if(rowIndex > 6 && rowIndex < xssfSheet.getLastRowNum() - 4) {
        			XSSFCell cellSN = xssfRow.getCell(0);
        			String serialSN = cellSN.getStringCellValue();
        			if(!Strings.isNullOrEmpty(serialSN)) {
        				String result = xssfRow.getCell(15).getStringCellValue();
        				if(!"OK".contentEquals(result)) {
        					pass = false;
        				}
        				for(int i = 0 ; i < inspectionItemsList.size(); i ++) {
            				XSSFCell cellValue = xssfRow.getCell(i + 1);
            				cellValue.setCellType(CellType.STRING);
            				String value = cellValue.getStringCellValue();
            				String item = inspectionItemsList.get(i);
            				UniqueDimensionInspection instance = new UniqueDimensionInspection();
            				instance.setPoNo(po==null?"":po);
            				instance.setMaterialNo(materialInfo==null?"":materialInfo.split("\\n")[0]);
            				instance.setMaterialRev(materialInfo==null?"":materialInfo.split("\\n")[1].split("\\.")[1]);
            				instance.setSerialSN(serialSN==null?"":serialSN);
            				instance.setDrawNo(drawInfo==null?"":drawInfo.split("\\n")[0]);
            				instance.setDrawRev(drawInfo==null?"":drawInfo.split("\\n")[1].split("\\.")[1]);
            				instance.setInspectionItem(item==null?"":item);
            				instance.setInspectionValue(value==null?"":value);
            				instance.setInspectionResult(result==null?"":result);
            				uniqueDimensionInspectionList.add(instance);
            			}
        			}
        		}
        		if(rowIndex == xssfSheet.getLastRowNum() - 3) {
        			//量具型号
        			for(int i = 0 ; i <uniqueDimensionInspectionList.size(); i++) {
        				XSSFCell gageTypeCell = xssfRow.getCell(i % 14 + 1);
        				String gageType = gageTypeCell.getStringCellValue();
        				uniqueDimensionInspectionList.get(i).setGaugeType(gageType);
        			}
        			
        		}
        		if(rowIndex == xssfSheet.getLastRowNum() - 2) {
        			//量具型号
        			for(int i = 0 ; i <uniqueDimensionInspectionList.size(); i++) {
        				XSSFCell gageSNCell = xssfRow.getCell(i % 14 + 1);
        				String gageSN = gageSNCell.getStringCellValue();
        				uniqueDimensionInspectionList.get(i).setGaugeSN(gageSN);
        			}
        			
        		}
        		
        		if(rowIndex == xssfSheet.getLastRowNum() - 1) {
        			appearanceIns = getMergedRegionValue(xssfSheet,rowIndex,2);
        		}
        	}
        	//保存每一个序列号的检验记录
        	uniqueDimensionInspectionService.saveAll(uniqueDimensionInspectionList);
        }
        if(!Strings.isNullOrEmpty(materialInfo)) {
        	//保存采购单的检验结果信息
        	inspectionResult.setInspector(inspector);
        	inspectionResult.setInspectDate(inspectionDate);
        	inspectionResult.setMaterialNo(materialInfo.split("\\n")[0]);
        	inspectionResult.setMaterialRev(materialInfo.split("\\n")[1].split("\\.")[1]);
        	inspectionResult.setPoNo(po);
        	inspectionResult.setInspectionQuantity(orderQuantity);
        	inspectionResult.setActiveQuantity(inspectionQuantity);
        	inspectionResult.setAppearanceIns(appearanceIns);
        	inspectionResult.setVendor(vendor);
        	inspectionResult.setPass(pass);
        	uniqueDimensionInspectionResultService.save(inspectionResult);
        }
    	pass = true;
        xssfWorkbook.close();
        return coutNum;
	}

	public int importsxls(byte[] bytes) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
        int sheetCount = xssfWorkbook.getNumberOfSheets();
        // 循环行Row
		String inspector = "";
		String inspectionDate = "";
		String vendor = "";
		int orderQuantity= 0;
		String materialInfo = "";
		String drawInfo = "";
		String po= "";
		int inspectionQuantity= 0;
		String description = "";
		String appearanceIns = "";
        UniqueDimensionInspectionResult inspectionResult = new UniqueDimensionInspectionResult();
        for(int sheetIndex = 0 ; sheetIndex < sheetCount ; sheetIndex ++) {
        	HSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
        	//每个sheet需要更新一次变量值
            inspectionItemsList.clear();
            uniqueDimensionInspectionList.clear();
            po = getMergedRegionValue(xssfSheet,2,10);
            if(uniqueDimensionInspectionResultService.getByPo(po) != null ) {
            	NotificationUtils.notificationError("当前采购Po:" + po + "已经导入，请勿重复导入");
            	break;
            }
        	for(int rowIndex = 0 ; rowIndex < xssfSheet.getLastRowNum() ; rowIndex ++) {
        		HSSFRow xssfRow = xssfSheet.getRow(rowIndex);
        		//第一行--获取检验员，检验日期数量
        		if(sheetIndex == 0) {
        			//订单检验信息，只需要获取一次
        			if(rowIndex == 1) {
    					inspector =getMergedRegionValue(xssfSheet,rowIndex,1);//检验员
    					SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
            			double longDate = Double.parseDouble(getMergedRegionValue(xssfSheet,rowIndex,5));//日期
            			Date date = DateUtil.getJavaDate(longDate);
            			inspectionDate =sdf.format(date);
            			vendor = getMergedRegionValue(xssfSheet,rowIndex,10);//供应商
            			HSSFCell cell = xssfRow.getCell(15);
            			orderQuantity = (int)cell.getNumericCellValue();//数量
            		}
            		if(rowIndex == 2) {
            			materialInfo = getMergedRegionValue(xssfSheet,rowIndex,1);
            			drawInfo = getMergedRegionValue(xssfSheet,rowIndex,5);
            			
            			HSSFCell cell = xssfRow.getCell(15);
            			inspectionQuantity = (int)cell.getNumericCellValue();//数量
            		}
            		if(rowIndex == 3) {
            			description = getMergedRegionValue(xssfSheet,rowIndex,1);
            		}
        		}
        		
        		if(rowIndex == 5) {
        			//从第一列到第14列表示检测项的类容，15列表示戒烟结果
        			for(int columnIndex = 0;columnIndex < 14 ; columnIndex ++) {
        				HSSFCell cell = xssfRow.getCell(columnIndex + 1);
        				cell.setCellType(CellType.STRING);
        				String inspectionName = cell.getStringCellValue();
        				if(inspectionName != null && inspectionName.length() > 0) {
        					inspectionItemsList.add(inspectionName);
        				}else {
        					break;
        				}
        			}
        		}
        		if(rowIndex > 6 && rowIndex < xssfSheet.getLastRowNum() - 4) {
        			HSSFCell cellSN = xssfRow.getCell(0);
        			String serialSN = cellSN.getStringCellValue();
        			if(!Strings.isNullOrEmpty(serialSN)) {
        				String result = xssfRow.getCell(15).getStringCellValue();
        				if(!"OK".contentEquals(result)) {
        					pass = false;
        				}
        				for(int i = 0 ; i < inspectionItemsList.size(); i ++) {
        					HSSFCell cellValue = xssfRow.getCell(i + 1);
            				cellValue.setCellType(CellType.STRING);
            				String value = cellValue.getStringCellValue();
            				String item = inspectionItemsList.get(i);
            				UniqueDimensionInspection instance = new UniqueDimensionInspection();
            				instance.setPoNo(po==null?"":po);
            				instance.setMaterialNo(materialInfo==null?"":materialInfo.split("\\n")[0]);
            				instance.setMaterialRev(materialInfo==null?"":materialInfo.split("\\n")[1].split("\\.")[1]);
            				instance.setSerialSN(serialSN==null?"":serialSN);
            				instance.setDrawNo(drawInfo==null?"":drawInfo.split("\\n")[0]);
            				instance.setDrawRev(drawInfo==null?"":drawInfo.split("\\n")[1].split("\\.")[1]);
            				instance.setInspectionItem(item==null?"":item);
            				instance.setInspectionValue(value==null?"":value);
            				instance.setInspectionResult(result==null?"":result);
            				uniqueDimensionInspectionList.add(instance);
            			}
        			}
        		}
        		if(rowIndex == xssfSheet.getLastRowNum() - 3) {
        			//量具型号
        			for(int i = 0 ; i <uniqueDimensionInspectionList.size(); i++) {
        				HSSFCell gageTypeCell = xssfRow.getCell(i % 14 + 1);
        				String gageType = gageTypeCell.getStringCellValue();
        				uniqueDimensionInspectionList.get(i).setGaugeType(gageType);
        			}
        			
        		}
        		if(rowIndex == xssfSheet.getLastRowNum() - 2) {
        			//量具型号
        			for(int i = 0 ; i <uniqueDimensionInspectionList.size(); i++) {
        				HSSFCell gageSNCell = xssfRow.getCell(i % 14 + 1);
        				String gageSN = gageSNCell.getStringCellValue();
        				uniqueDimensionInspectionList.get(i).setGaugeSN(gageSN);
        			}
        			
        		}
        		
        		if(rowIndex == xssfSheet.getLastRowNum() - 1) {
        			appearanceIns = getMergedRegionValue(xssfSheet,rowIndex,2);
        		}
        	}
        	//保存每一个序列号的检验记录
        	uniqueDimensionInspectionService.saveAll(uniqueDimensionInspectionList);
        }
      //保存采购单的检验结果信息
        if(!Strings.isNullOrEmpty(materialInfo)) {
        	inspectionResult.setInspector(inspector);
        	inspectionResult.setInspectDate(inspectionDate);
        	inspectionResult.setMaterialNo(materialInfo.split("\\n")[0]);
        	inspectionResult.setMaterialRev(materialInfo.split("\\n")[1].split("\\.")[1]);
        	inspectionResult.setPoNo(po);
        	inspectionResult.setInspectionQuantity(orderQuantity);
        	inspectionResult.setActiveQuantity(inspectionQuantity);
        	inspectionResult.setAppearanceIns(appearanceIns);
        	inspectionResult.setVendor(vendor);
        	inspectionResult.setPass(pass);
        	uniqueDimensionInspectionResultService.save(inspectionResult);
        }
    	pass = true;
        xssfWorkbook.close();
        return coutNum;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        refreshGrid(tfPurchasingNo.getValue().trim());
	}

	private void refreshGrid(String po) {
		List<UniqueDimensionInspection> uniqueDimensionInspectionList = uniqueDimensionInspectionService.getAll(po);
		 objectGrid.setDataProvider(DataProvider.ofCollection(removeDuplicate(uniqueDimensionInspectionList)));
	}
	
    private ArrayList<UniqueDimensionInspection> removeDuplicate(List<UniqueDimensionInspection> list) {
        Set<UniqueDimensionInspection> set = new TreeSet<UniqueDimensionInspection>(new Comparator<UniqueDimensionInspection>() {
            @Override
            public int compare(UniqueDimensionInspection o1, UniqueDimensionInspection o2) {
                return (o1.getPoNo()).compareTo(o2.getPoNo());
            }
        });
        set.addAll(list);
        return new ArrayList<UniqueDimensionInspection>(set);
    }
    
  //判断是否有合并单元格
  	private  boolean isMergedRegion(Sheet sheet,int row ,int column) {
          int sheetMergeCount = sheet.getNumMergedRegions();
          for (int i = 0; i < sheetMergeCount; i++) {
              CellRangeAddress range = sheet.getMergedRegion(i);
              int firstColumn = range.getFirstColumn();
              int lastColumn = range.getLastColumn();
              int firstRow = range.getFirstRow();
              int lastRow = range.getLastRow();
              if(row >= firstRow && row <= lastRow){
                  if(column >= firstColumn && column <= lastColumn){
                      return true;
                  }
              }
          }
          return false;
      }
  	//获取合并单元格的值
      public  String getMergedRegionValue(Sheet sheet ,int row , int column){
          int sheetMergeCount = sheet.getNumMergedRegions();

          for(int i = 0 ; i < sheetMergeCount ; i++){
              CellRangeAddress ca = sheet.getMergedRegion(i);
              int firstColumn = ca.getFirstColumn();
              int lastColumn = ca.getLastColumn();
              int firstRow = ca.getFirstRow();
              int lastRow = ca.getLastRow();

              if(row >= firstRow && row <= lastRow){
                  if(column >= firstColumn && column <= lastColumn){
                      Row fRow = sheet.getRow(firstRow);
                      Cell fCell = fRow.getCell(firstColumn);
                      return getCellValue(fCell) ;
                  }
              }
          }
          return "";
      }
      
      public String getCellValue(Cell cell){

          if(cell == null) return "";      
          if(cell.getCellType() == CellType.STRING){      
              return cell.getStringCellValue();      
          }else if(cell.getCellType() == CellType.BOOLEAN){      
              return String.valueOf(cell.getBooleanCellValue());      
          }else if(cell.getCellType() == CellType.FORMULA){      
              return cell.getCellFormula() ;      
          }else if(cell.getCellType() == CellType.NUMERIC){      
              return String.valueOf(cell.getNumericCellValue());      
          }  
          return "";    
      }
}
