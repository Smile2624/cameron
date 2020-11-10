package com.ags.lumosframework.ui.view.assembling;

import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.service.IAssemblingService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;

import java.util.ArrayList;
import java.util.List;

@Menu(caption = "AssemblingInfo", captionI18NKey = "view.AssemblingInfo.caption", iconPath = "images/icon/r_part_track.png", groupName = "Result", order = 3)
@SpringView(name = "AssemblingInfo", ui = CameronUI.class)
public class AssemblingInfoView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 4854162164548450226L;

    private TextField tfAssemblingSn = new TextField();

    @I18Support(caption = "Search", captionKey = "common.search")
    private Button btnSearch = new Button();

    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();

    private Button[] btns = new Button[]{btnSearch, btnRefresh};

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private Grid<Assembling> gridSingleton = new Grid<>();

    private Grid<Assembling> gridBatch = new Grid<>();

    GridLayout glLayout = new GridLayout(2, 1);

    @I18Support(caption = "Order", captionKey = "AssemblingInfo.Order")
    private LabelWithSamleLineCaption tfOrder = new LabelWithSamleLineCaption();
    @I18Support(caption = "S/N Batch", captionKey = "AssemblingInfo.SnBatch")
    private LabelWithSamleLineCaption tfSnBatch = new LabelWithSamleLineCaption();

    @Autowired
    private IAssemblingService assemblingService;

    public AssemblingInfoView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlTempToolBox.addComponent(tfAssemblingSn);
        tfAssemblingSn.setPlaceholder(I18NUtility.getValue("AssemblingInfo.SnBatch", "S/N Batch"));
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);

        hlToolBox.addComponent(hlTempToolBox);
        vlRoot.addComponent(hlToolBox);

        Panel panel = new Panel();
        panel.setWidth("100%");
        panel.setHeightUndefined();
        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(this.tfSnBatch, 0, 0);
        glLayout.addComponent(this.tfOrder, 1, 0);
        panel.setContent(glLayout);
        vlRoot.addComponent(panel);
        vlRoot.setExpandRatio(panel, 0.1f);

        //*****************
        gridSingleton.setSizeFull();
        gridSingleton.addColumn(Assembling::getPartNo).setId("PARTNO").setCaption(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）"));
        gridSingleton.addColumn(Assembling::getPartDesc).setCaption(I18NUtility.getValue("AssemblingInfo.Description", "Description"));
        gridSingleton.addColumn(Assembling::getPlmRev).setCaption(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev"));
        gridSingleton.addColumn(Assembling::getPartRev).setCaption(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev"));
        gridSingleton.addColumn(Assembling::getSerialNo).setCaption(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No"));
        gridSingleton.addColumn(Assembling::getHeatNoLot).setCaption(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot"));
        gridSingleton.addColumn(Assembling::getHardness).setCaption(I18NUtility.getValue("AssemblingInfo.Hardness", "Hardness"));
        gridSingleton.addColumn(Assembling::getMatType).setCaption("Mat'l Type");
        gridSingleton.addColumn(Assembling::getQcCheck).setCaption("Q/C Check");
        vlRoot.addComponent(gridSingleton);
        vlRoot.setExpandRatio(gridSingleton, 0.4f);
        //**************ERIC EDIT 7-APR-20  gridSingleton.sort("PARTNO");*******************
        gridSingleton.sort("PARTNO");
        //*****************
        gridBatch.setSizeFull();
        //**************ERIC EDIT 7-APR-20  setID("PARTNO")*******************
        gridBatch.addColumn(Assembling::getPartNo).setId("PARTNO").setCaption(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）"));
        gridBatch.addColumn(Assembling::getPartDesc).setCaption(I18NUtility.getValue("AssemblingInfo.Description", "Description"));
        gridBatch.addColumn(Assembling::getPlmRev).setCaption(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev"));
        gridBatch.addColumn(Assembling::getPartRev).setCaption(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev"));
        gridBatch.addColumn(Assembling::getBatchQty).setCaption(I18NUtility.getValue("AssemblingInfo.Qty", "QTY"));
        gridBatch.addColumn(Assembling::getBatch).setCaption(I18NUtility.getValue("AssemblingInfo.SerialNo", "Batch"));
        gridBatch.addColumn(Assembling::getHeatNoLot).setCaption(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot"));
        gridBatch.addColumn(Assembling::getHardness).setCaption(I18NUtility.getValue("AssemblingInfo.Hardness", "Hardness"));
        gridBatch.addColumn(Assembling::getCOrD).setCaption("C/D");
        gridBatch.addColumn(Assembling::getEOrD).setCaption("E/D");
        gridBatch.addColumn(Assembling::getQcCheck).setCaption("Q/C Check");
        vlRoot.addComponent(gridBatch);
        vlRoot.setExpandRatio(gridBatch, 0.4f);
        //**************ERIC EDIT 7-APR-20  gridBatch.sort("PARTNO");*******************
        gridBatch.sort("PARTNO");
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

        KeyAction kaAssemblingSn = new KeyAction(ShortcutAction.KeyCode.ENTER, new int[]{});
        kaAssemblingSn.addKeypressListener(new KeyAction.KeyActionListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                freshGrid();
            }
        });
        kaAssemblingSn.extend(tfAssemblingSn);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    @Override
    public void updateAfterFilterApply() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnSearch.equals(button)) {
            freshGrid();
        } else if (btnRefresh.equals(button)) {
            tfAssemblingSn.clear();
            freshGrid();
        }
    }

    public void freshGrid() {
        if (tfAssemblingSn.getValue().trim().equals("")) {
            tfOrder.clear();
            tfSnBatch.clear();
            gridSingleton.setDataProvider(DataProvider.ofCollection(new ArrayList<>()));
            gridBatch.setDataProvider(DataProvider.ofCollection(new ArrayList<>()));
            return;
        }
        List<Assembling> assemblings = assemblingService.getBySn(tfAssemblingSn.getValue().trim());
        if (assemblings != null && assemblings.size() > 0) {
            tfSnBatch.setValue(assemblings.get(0).getSnBatch());
            tfOrder.setValue(assemblings.get(0).getOrderNo());
        }
        //单件
        List<Assembling> singletonDetail = assemblingService.getBySnType(tfAssemblingSn.getValue().trim(), "singleton");
        gridSingleton.setDataProvider(DataProvider.ofCollection(singletonDetail));
        //批次
        List<Assembling> batchDetail = assemblingService.getBySnType(tfAssemblingSn.getValue().trim(), "batch");
        gridBatch.setDataProvider(DataProvider.ofCollection(batchDetail));
    }

}
