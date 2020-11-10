package com.ags.lumosframework.ui.view.hardnesstestreport;

import com.ags.lumosframework.pojo.HardnessTestReport;
import com.ags.lumosframework.pojo.HardnessTestReportItems;
import com.ags.lumosframework.service.IHardnessTestReportItemsService;
import com.ags.lumosframework.service.IHardnessTestReportService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Menu(caption = "hardnessTestInfo", captionI18NKey = "view.hardnessTestInfo.caption", iconPath = "images/icon/text-blob.png", groupName = "Result", order = 2)
@SpringView(name = "hardnessTestInfo", ui = CameronUI.class)
public class HardnessTestInfoView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = -119709916213193217L;

    private TextField tfPurchaseOrder = new TextField();

    @I18Support(caption = "Search", captionKey = "common.search")
    private Button btnSearch = new Button();

    Grid<HardnessTestReport> objectGrid = new Grid<>();

    Grid<HardnessTestReportItems> objectItemGrid = new Grid<>();

    @Autowired
    private IHardnessTestReportService hardnessTestReportService;

    @Autowired
    private IHardnessTestReportItemsService hardnessTestReportItemsService;

    public HardnessTestInfoView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        HorizontalLayout hlToolBox = new HorizontalLayout();
        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);

        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        hlTempToolBox.addComponent(tfPurchaseOrder);
        tfPurchaseOrder.setPlaceholder(I18NUtility.getValue("view.HardnessTestReportItems.purchasingOrder", "purchasingOrder"));// "采购单号"
        hlTempToolBox.addComponent(btnSearch);
        btnSearch.addClickListener(this);
        btnSearch.setDisableOnClick(true);
        btnSearch.setIcon(VaadinIcons.SEARCH);

        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(400.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        objectGrid.setSizeFull();
        objectGrid.addColumn(HardnessTestReport::getPurchaseOrder).setCaption(I18NUtility.getValue("view.hardnessTestReport.PurchaseOrder", "PurchaseOrde"));
        objectGrid.addColumn(HardnessTestReport::getPurchaseOrderSubitem).setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"));
        objectGrid.addColumn(HardnessTestReport::getSAPBatchNo).setCaption(I18NUtility.getValue("view.materialinspection.sapinspectionlot", "SapInspectionLot"));
        objectGrid.addColumn(HardnessTestReport::getPartQuantity).setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "Quantity"));
        objectGrid.addSelectionListener(event -> {
            Optional<HardnessTestReport> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                setDateToItem(optional.get());
            } else {
                setDateToItem(null);
            }
        });
        hlSplitPanel.setFirstComponent(objectGrid);

        objectItemGrid.setSizeFull();
        objectItemGrid.addColumn(bean -> {
            return bean.getPartNo() + "/" + bean.getPartNoRev();
        }).setCaption(I18NUtility.getValue("view.hardnessTestReport.PartNo/Rev", "PartNo/Rev"));
        objectItemGrid.addColumn(HardnessTestReportItems::getSerialNo).setCaption(I18NUtility.getValue("view.HardnessTestReportItems.SerialNo", "SerialNo"));
        objectItemGrid.addColumn(HardnessTestReportItems::getHeatNo).setCaption(I18NUtility.getValue("view.HardnessTestReportItems.HeatNo", "HeatNo"));
        objectItemGrid.addColumn(HardnessTestReportItems::getHTLotNo).setCaption(I18NUtility.getValue("view.HardnessTestReportItems.HTLotNo", "HTLotNo"));
        objectItemGrid.addColumn(HardnessTestReportItems::getActualHardnessValue).setCaption(I18NUtility.getValue("view.HardnessTestReportItems.ActualHardnessValue", "ActualHardnessValue"));
        objectItemGrid.addColumn(HardnessTestReportItems::getResult).setCaption(I18NUtility.getValue("view.HardnessTestReportItems.Result", "Result"));

        hlSplitPanel.setSecondComponent(objectItemGrid);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

        KeyAction kaPurchaseOrder = new KeyAction(ShortcutAction.KeyCode.ENTER, new int[]{});
        kaPurchaseOrder.addKeypressListener(new KeyAction.KeyActionListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                freshGrid();
            }
        });
        kaPurchaseOrder.extend(tfPurchaseOrder);
    }

    private void setDateToItem(HardnessTestReport hardnessTestReport) {
        if (hardnessTestReport != null) {
            objectItemGrid.setItems(hardnessTestReportItemsService.getByNoBatchSubitem(hardnessTestReport.getPurchaseOrder(),
                    hardnessTestReport.getSAPBatchNo(), hardnessTestReport.getPurchaseOrderSubitem()));
        } else {
            objectItemGrid.setItems(new ArrayList());
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        if (btnSearch.equals(button)) {
            freshGrid();
            btnSearch.setEnabled(true);
        }
    }

    public void freshGrid() {
        List<HardnessTestReport> hardnessTestReports = hardnessTestReportService.getByPurchasingOrder(tfPurchaseOrder.getValue().trim());
        if (hardnessTestReports != null && hardnessTestReports.size() > 0) {
            objectGrid.setItems(hardnessTestReports);
        } else {
        	NotificationUtils.notificationError("输入的单号不存在或者没有完成硬度测试");
        	tfPurchaseOrder.clear();
            objectGrid.setItems(new ArrayList());
        }
    }
}
