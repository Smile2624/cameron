package com.ags.lumosframework.ui.view.dimensionresult;

import com.ags.lumosframework.pojo.DimensionInspectionResult;
import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.service.IDimensionInspectionService;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.google.common.base.Strings;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;
import org.vaadin.artur.KeyAction.KeyActionEvent;
import org.vaadin.artur.KeyAction.KeyActionListener;

import java.util.ArrayList;
import java.util.List;

@Menu(caption = "DimensionResultView", captionI18NKey = "view.dimensionresult.caption", iconPath = "images/icon/text-blob.png", groupName = "Result", order = 1)
@SpringView(name = "DimensionResultView", ui = CameronUI.class)
public class DimensionInspectionResultView extends BaseView implements Button.ClickListener {

	private static final long serialVersionUID = -5881921658681194159L;

	private TextField tfPurchasingNo = new TextField();

	@I18Support(caption="Refresh",captionKey="common.refresh")
	private Button btnRefresh = new Button();
	
	HorizontalLayout hlToolBox = new HorizontalLayout();

	private Grid<PurchasingOrderInfo> gridObject = new Grid<>();
	
	private Grid<DimensionInspectionResult> gridObjectItems = new Grid<>();
	
	@Autowired
	private IPurchasingOrderService orderService;
	
	@Autowired
	private IDimensionInspectionService dimensionInspectionService;
	
	public DimensionInspectionResultView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		tfPurchasingNo.setPlaceholder("采购单号");
		hlTempToolBox.addComponent(btnRefresh);
		btnRefresh.setIcon(VaadinIcons.REFRESH);
		btnRefresh.addClickListener(this);
		hlTempToolBox.addComponent(tfPurchasingNo);
		hlTempToolBox.setComponentAlignment(tfPurchasingNo, Alignment.MIDDLE_LEFT);
		KeyAction ka = new KeyAction(KeyCode.ENTER, new int[] {});
		ka.addKeypressListener(new KeyActionListener() {

			private static final long serialVersionUID = -3220687710828989615L;

			@Override
			public void keyPressed(KeyActionEvent keyPressEvent) {
				// 清空当前信息
				String purchasingOrder = tfPurchasingNo.getValue().trim();
				if(!Strings.isNullOrEmpty(purchasingOrder)) {
					List<PurchasingOrderInfo> listPurchasingOrderInfo = orderService.getCheckedOrder(purchasingOrder, "DIMENSION");
					if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
						// 数据填入grid
						gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
					}
				}
			}
		});
		ka.extend(tfPurchasingNo);
		
		VerticalSplitPanel vlSplitPanel = new VerticalSplitPanel();
		vlSplitPanel.setSplitPosition(200.0F, Unit.PIXELS);
		vlSplitPanel.setSizeFull();
		vlRoot.addComponent(vlSplitPanel);
		vlRoot.setExpandRatio(vlSplitPanel, 1);
		
		gridObject.setSizeFull();
		gridObject.addColumn(PurchasingOrderInfo::getPurchasingNo).setCaption(I18NUtility.getValue("view.materialinspection.purchasingno", "PurchasingNo"));
		gridObject.addColumn(PurchasingOrderInfo::getPurchasingItemNo).setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"));
		gridObject.addColumn(PurchasingOrderInfo::getSapInspectionLot).setCaption(I18NUtility.getValue("view.materialinspection.sapinspectionlot", "SapInspectionLot"));
		gridObject.addColumn(PurchasingOrderInfo::getMaterialNo).setCaption(I18NUtility.getValue("view.materialinspection.materialno", "MaterialNo"));
		gridObject.addColumn(PurchasingOrderInfo::getMaterialRev).setCaption(I18NUtility.getValue("view.materialinspection.materialrev", "MaterialRev"));
		gridObject.addColumn(PurchasingOrderInfo::getMaterialQuantity).setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "MaterialQuantity"));
		gridObject.addSelectionListener(new SelectionListener<PurchasingOrderInfo>() {
			
			private static final long serialVersionUID = 1522905269067696217L;

			@Override
			public void selectionChange(SelectionEvent<PurchasingOrderInfo> event) {
				if(event.getFirstSelectedItem().isPresent()) {
					PurchasingOrderInfo purchasingInfo =event.getFirstSelectedItem().get();
					setDataToItems(purchasingInfo);
				}else {
					setDataToItems(null);
				}
			}
		});
		
		gridObjectItems.setSizeFull();
		gridObjectItems.addColumn(DimensionInspectionResult::getMaterialSN).setCaption(I18NUtility.getValue("view.dimensioninspectionresult.materialsn", "MaterialSN"));
		gridObjectItems.addColumn(DimensionInspectionResult::getInspectionName).setCaption(I18NUtility.getValue("view.dimensioninspectionresult.inspectionname", "InspectionName"));
		gridObjectItems.addColumn(DimensionInspectionResult::getInspectionValue).setCaption(I18NUtility.getValue("view.dimensioninspectionresult.inspectionvalue", "InspectionValue"));
		gridObjectItems.addColumn(new ValueProvider<DimensionInspectionResult, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String apply(DimensionInspectionResult source) {
				if(source.getIsPass()) {
					return "OK";
				}else{
					return "NG";
				}
				
			}
		}).setCaption(I18NUtility.getValue("view.dimensioninspectionresult.inspectionresult", "InspectionResult"));
		gridObjectItems.addColumn(DimensionInspectionResult::getGageInfo).setCaption(I18NUtility.getValue("view.dimensioninspectionresult.gageinfo", "GageInfo"));
		
		vlSplitPanel.setFirstComponent(gridObject);
		vlSplitPanel.setSecondComponent(gridObjectItems);
		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		Button btn = event.getButton();
		if(btnRefresh.equals(btn)) {
			refreshData();
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		refreshData();
	}
	
	public void refreshData() {
		List<PurchasingOrderInfo> listPurchasingOrderInfo = orderService.getCheckedOrder(null, "DIMENSION");
		if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
			// 数据填入grid
			gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
		}
	}
	
	public void setDataToItems(PurchasingOrderInfo instance) {
		if(instance != null) {
			String sapLotNo = instance.getSapInspectionLot();
			List<DimensionInspectionResult> list = dimensionInspectionService.getBySapLotNo(sapLotNo);
			gridObjectItems.setDataProvider(DataProvider.ofCollection(list));
		}else {
			gridObjectItems.setDataProvider(DataProvider.ofCollection(new ArrayList<>()));
		}
	}
}
