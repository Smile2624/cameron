

package com.ags.lumosframework.ui.view.paintingspecification;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.PaintingSpecification;
import com.ags.lumosframework.service.IPaintingSpecificationService;
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
import java.util.Optional;

@Menu(caption = "PaintingSpecification", captionI18NKey = "PaintingSpecification.view.caption" ,iconPath = "images/icon/text-blob.png", groupName ="Data", order = 9 )
@SpringView(name = "PaintingSpecification", ui = CameronUI.class)
@Secured("PaintingSpecification")
public class PaintingSpecificationView extends BaseView implements Button.ClickListener, IFilterableView {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2061364096058427732L;
	
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
	
	@I18Support(caption = "Import Excel", captionKey = "common.import")//ProductionOrder.ImportExcel
    private UploadButton upload = new UploadButton();
//
	private Button[] btns = new Button[] { btnAdd, btnEdit, btnDelete, btnRefresh };

	// 查询区域控件
	@I18Support(caption = "productOrderId", captionKey = "ProductionOrder.productOrderId")
	private TextField tfproductOrderId = new TextField();

	@I18Support(caption = "ProductId", captionKey = "ProductionOrder.ProductId")
	private TextField tfProductId = new TextField();

	@I18Support(caption = "Search", captionKey = "common.search")
	private Button btnSearch = new Button();

	private HorizontalLayout hlToolBox = new HorizontalLayout();
	
	private UploadFinishEvent uploadEvent = null;

	private IDomainObjectGrid<PaintingSpecification> objectGrid = new PaginationDomainObjectList<>();

	@Autowired
	private IPaintingSpecificationService paintingSpecificationService;

	@Autowired
	private AddPaintingSpecificationDialog addPaintingSpecificationDialog;

	public PaintingSpecificationView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		SearchPanelBuilder sp = new SearchPanelBuilder(BeanManager.getService(PaintingSpecificationConditions.class), objectGrid, this);
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
                int[] num = {0};
            	ConfirmDialog.show(getUI(), "上传的数据若存在,是否覆盖？", result -> {
    				if (ConfirmResult.Result.OK.equals(result.getResult())) {
    					try {
    						String fileName = event.getFilename();
    						if (fileName.endsWith(".xlsx")) {
    							num[0] = importsxlsx(uploadEvent.getUploadFileInByte(),true);
    						} else if(fileName.endsWith(".xls")) {
    							num[0] = importsxls(uploadEvent.getUploadFileInByte(),true);
    						}else {
    	                        NotificationUtils.notificationError("错误文件！");
    	                    }
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
                        objectGrid.refresh();
                        NotificationUtils.notificationInfo("成功导入,共" + num[0] + "条记录");
    				}else {
//    					try {
//    						String fileName = event.getFilename();
//    						if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
//    							num[0] = importsxlsx(uploadEvent.getUploadFileInByte(),false);
//    						} else if(fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
//    							num[0] = importsxls(uploadEvent.getUploadFileInByte(),false);
//    						}else {
//    	                        NotificationUtils.notificationError("错误文件！");
//    	                    }
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (ParseException e) {
//							e.printStackTrace();
//						}
//                        objectGrid.refresh();
//                        NotificationUtils.notificationInfo("成功导入,共" + num[0] + "条记录");
    				}
    			});
            }


        });
        hlTempToolBox.addComponent(upload);
		
		objectGrid.addColumn(PaintingSpecification::getPaintingSpecificationFile).setCaption(I18NUtility.getValue("PaintingSpecification.PaintingSpecificationFile", "PaintingSpecificationFile"));
		objectGrid.addColumn(PaintingSpecification::getRev).setCaption(I18NUtility.getValue("PaintingSpecification.Revision", "Revision"));
		objectGrid.addColumn(PaintingSpecification::getPrimer).setCaption(I18NUtility.getValue("PaintingSpecification.Primer", "Primer"));
		objectGrid.addColumn(PaintingSpecification::getIntermediate).setCaption(I18NUtility.getValue("PaintingSpecification.Intermediate", "Intermediate"));
		objectGrid.addColumn(PaintingSpecification::getFinals).setCaption(I18NUtility.getValue("PaintingSpecification.Finals", "Finals"));
		objectGrid.addColumn(PaintingSpecification::getTotal).setCaption(I18NUtility.getValue("PaintingSpecification.Total", "Total"));
		objectGrid.addColumn(PaintingSpecification::getColor).setCaption(I18NUtility.getValue("PaintingSpecification.Color", "Color"));
		objectGrid.addColumn(PaintingSpecification::getHumidity).setCaption(I18NUtility.getValue("PaintingSpecification.Humidity", "Humidity"));
		objectGrid.addColumn(PaintingSpecification::getAboveDewPoint).setCaption(I18NUtility.getValue("PaintingSpecification.AboveDewPoint", "AboveDewPoint"));
		objectGrid.setObjectSelectionListener(event -> {
			setButtonStatus(event.getFirstSelectedItem());
		});
		vlRoot.addComponents((Component) objectGrid);
		vlRoot.setExpandRatio((Component) objectGrid, 1);

		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	private void setButtonStatus(Optional<PaintingSpecification> optional) {
		boolean enable = optional.isPresent();
		btnEdit.setEnabled(enable);
		btnDelete.setEnabled(enable);
	}

	@Override
	protected void init() {
		objectGrid.setServiceClass(IPaintingSpecificationService.class);
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
			addPaintingSpecificationDialog.setObject(null);
			addPaintingSpecificationDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					objectGrid.refresh();
				}
			});
		} else if (btnEdit.equals(button)) {
			PaintingSpecification paintingSpecification = (PaintingSpecification) objectGrid.getSelectedObject();
			addPaintingSpecificationDialog.setObject(paintingSpecification);
			addPaintingSpecificationDialog.show(getUI(), result -> {
				if (ConfirmResult.Result.OK.equals(result.getResult())) {
					PaintingSpecification temp = (PaintingSpecification) result.getObj();
					objectGrid.refresh(temp);
				}
			});
		} else if (btnDelete.equals(button)) {
			ConfirmDialog.show(getUI(),
					I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
					result -> {
						if (ConfirmResult.Result.OK.equals(result.getResult())) {
							try {
								paintingSpecificationService.delete((PaintingSpecification) objectGrid.getSelectedObject());
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
	
	public int importsxlsx(byte[] bytes,boolean flag) throws IOException, ParseException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;

        //.xlsx
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        
        // 循环行Row
        for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
            	
                XSSFCell paintingSpecificationFile = xssfRow.getCell(0);
                String paintingSpecificationFileValue = paintingSpecificationFile.getStringCellValue();
                if(Strings.isNullOrEmpty(paintingSpecificationFileValue)) {
                	continue;
                }
                PaintingSpecification paintingSpecification = paintingSpecificationService.getBySpecificationFile(paintingSpecificationFileValue);
            	if(flag) {//覆盖
                	//删除已有的数据
                	if(paintingSpecification !=null) {
                		paintingSpecificationService.delete(paintingSpecification);
//                		System.out.println("覆盖，删除第 "+rowNum+" 行，保存第 "+rowNum+" 行，");
                	}
                	//保存数据
                	saveDataToPaintingSpecification(xssfRow);
                	coutNum++;
                }else if(!flag && paintingSpecification==null) {//不覆盖  则已有的数据不保存 只保存新数据
            		//保存数据
                	saveDataToPaintingSpecification(xssfRow);
//                	System.out.println("不覆盖，保存第 "+rowNum+" 行，");
                	coutNum++;
                }else if(paintingSpecification==null) {
                	//保存数据
                	saveDataToPaintingSpecification(xssfRow);
//                	System.out.println("直接保存第 "+rowNum+" 行，");
                	coutNum++;
                }
            }
        }
        xssfWorkbook.close();
        return coutNum;
    }
	
	
	public int importsxls(byte[] bytes,boolean flag) throws IOException, ParseException {
        InputStream is = new ByteArrayInputStream(bytes);
        int coutNum = 0;
        //*****.xls
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        
        // 循环行Row
        for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
            	
                HSSFCell paintingSpecificationFile = hssfRow.getCell(0);
                String paintingSpecificationFileValue = paintingSpecificationFile.getStringCellValue();
                if(Strings.isNullOrEmpty(paintingSpecificationFileValue)) {
                	continue;
                }
                PaintingSpecification paintingSpecification = paintingSpecificationService.getBySpecificationFile(paintingSpecificationFileValue);
            	if(flag) {//覆盖
                	//删除已有的数据
                	if(paintingSpecification !=null) {
                		paintingSpecificationService.delete(paintingSpecification);
                	}
                	//保存数据
                	saveDataToPaintingSpecification(hssfRow);
                	coutNum++;
                }else if(!flag && paintingSpecification==null) {//不覆盖  则已有的数据不保存 只保存新数据
            		//保存数据
                	saveDataToPaintingSpecification(hssfRow);
                	coutNum++;
                }else if(paintingSpecification==null) {
                	//保存数据
                	saveDataToPaintingSpecification(hssfRow);
                	coutNum++;
                }
            }
        }
        hssfWorkbook.close();
        return coutNum;
    }

	@Override
	public void updateAfterFilterApply() {
		
	}
	
	public void saveDataToPaintingSpecification(XSSFRow xssfRow) {
		XSSFCell paintingSpecificationFile = xssfRow.getCell(0);
		XSSFCell primer = xssfRow.getCell(1);
        XSSFCell intermediate  = xssfRow.getCell(2);
        XSSFCell finals = xssfRow.getCell(3);
        XSSFCell color = xssfRow.getCell(4);
        XSSFCell humidity = xssfRow.getCell(5);
        XSSFCell aboveDewPoint = xssfRow.getCell(6);
        
        PaintingSpecification paintingSpecification = new PaintingSpecification();
        paintingSpecification.setPaintingSpecificationFile(paintingSpecificationFile !=null?paintingSpecificationFile.getStringCellValue():"");
        paintingSpecification.setPrimer(primer !=null ? primer.getStringCellValue():"");
        paintingSpecification.setIntermediate(intermediate!=null?intermediate.getStringCellValue():"");
        paintingSpecification.setFinals(finals !=null ?finals.getStringCellValue():"");
        paintingSpecification.setColor(color!=null ? color.getStringCellValue():"");
        paintingSpecification.setHumidity(humidity !=null ?String.valueOf(humidity.getNumericCellValue()):"");
        paintingSpecification.setAboveDewPoint(aboveDewPoint!=null ? String.valueOf(aboveDewPoint.getNumericCellValue()):"");
        
        paintingSpecificationService.save(paintingSpecification);
	}
	
	public void saveDataToPaintingSpecification(HSSFRow hssfRow) {
		HSSFCell paintingSpecificationFile = hssfRow.getCell(0);
		HSSFCell primer = hssfRow.getCell(1);
        HSSFCell intermediate  = hssfRow.getCell(2);
        HSSFCell finals = hssfRow.getCell(3);
        HSSFCell color = hssfRow.getCell(4);
        HSSFCell humidity = hssfRow.getCell(5);
        HSSFCell aboveDewPoint = hssfRow.getCell(6);
        
        PaintingSpecification paintingSpecification = new PaintingSpecification();
        paintingSpecification.setPaintingSpecificationFile(paintingSpecificationFile !=null?paintingSpecificationFile.getStringCellValue():"");
        paintingSpecification.setPrimer(primer !=null ? primer.getStringCellValue():"");
        paintingSpecification.setIntermediate(intermediate!=null?intermediate.getStringCellValue():"");
        paintingSpecification.setFinals(finals !=null ?finals.getStringCellValue():"");
        paintingSpecification.setColor(color!=null ? color.getStringCellValue():"");
        paintingSpecification.setHumidity(humidity !=null ?String.valueOf(humidity.getNumericCellValue()):"");
        paintingSpecification.setAboveDewPoint(aboveDewPoint!=null ? String.valueOf(aboveDewPoint.getNumericCellValue()):"");
        
        paintingSpecificationService.save(paintingSpecification);
	}
}
