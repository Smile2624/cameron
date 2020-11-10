//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ags.lumosframework.ui.view.productrouting;

import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.ConfirmDialog;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult.Result;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Menu(
        caption = "RoutingCheck",
        captionI18NKey = "Cameron.RoutingCheck",
        iconPath = "images/icon/text-blob.png",
        groupName = "Data",
        order = 16
)
@SpringView(
        name = "RoutingCheck",
        ui = {CameronUI.class}
)
@Secured("RoutingCheck")
public class RoutingCheckView extends BaseView implements ClickListener, IFilterableView {
    private TextField tfProductId = new TextField();
    private TextField tfRoutingGroup = new TextField();
    private TextField tfInnerGroupNo = new TextField();
    private ComboBox<String> cbStatus = new ComboBox();
    @I18Support(
            caption = "Search",
            captionKey = "common.search"
    )
    private Button btnSearch = new Button();
    @I18Support(
            caption = "Refresh",
            captionKey = "common.refresh"
    )
    private Button btnRefresh = new Button();
    @I18Support(
            caption = "Pass",
            captionKey = "Routing.CheckStatus.Pass"
    )
    private Button btnPass = new Button();
    @I18Support(
            caption = "NoPass",
            captionKey = "Routing.CheckStatus.NoPass"
    )
    private Button btnNoPass = new Button();
    private Button[] btns;
    private HorizontalLayout hlToolBox;
    private Grid<ProductRouting> objectGrid;
    @Autowired
    private IProductRoutingService productRoutingService;

    public RoutingCheckView() {
        this.btns = new Button[]{this.btnSearch, this.btnRefresh, this.btnPass, this.btnNoPass};
        this.hlToolBox = new HorizontalLayout();
        this.objectGrid = new Grid();
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        this.hlToolBox.setWidth("100%");
        this.hlToolBox.addStyleName("card-0 card-no-padding");
        this.hlToolBox.setMargin(true);
        vlRoot.addComponent(this.hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        this.hlToolBox.addComponent(hlTempToolBox);
        hlTempToolBox.addComponents(new Component[]{this.tfProductId, this.tfRoutingGroup, this.tfInnerGroupNo, this.cbStatus});
        this.tfProductId.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductId", "ProductId", new Object[0]));
        this.tfRoutingGroup.setPlaceholder(I18NUtility.getValue("ProductRouting.RoutingGroup", "RoutingGroup", new Object[0]));
        this.tfInnerGroupNo.setPlaceholder(I18NUtility.getValue("ProductRouting.InnerGroupNo", "InnerGroupNo", new Object[0]));
        this.cbStatus.setTextInputAllowed(false);
        this.cbStatus.setEmptySelectionAllowed(true);
        this.cbStatus.setPlaceholder(I18NUtility.getValue("ProductRouting.CheckStatus", "CheckStatus", new Object[0]));
        Button[] var3 = this.btns;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Button btn = var3[var5];
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }

        this.btnSearch.setIcon(VaadinIcons.SEARCH);
        this.btnRefresh.setIcon(VaadinIcons.REFRESH);
        this.btnPass.setIcon(VaadinIcons.CHECK);
        this.btnNoPass.setIcon(VaadinIcons.CLOSE);
        this.objectGrid.setSizeFull();
        this.objectGrid.setSelectionMode(SelectionMode.MULTI);
        this.objectGrid.addColumn(ProductRouting::getProductId).setCaption(I18NUtility.getValue("ProductRouting.ProductId", "ProductId", new Object[0])).setWidth(130.0D);
        this.objectGrid.addColumn(ProductRouting::getRoutingGroup).setCaption(I18NUtility.getValue("ProductRouting.RoutingGroup", "RoutingGroup", new Object[0]));
        this.objectGrid.addColumn(ProductRouting::getInnerGroupNo).setCaption(I18NUtility.getValue("ProductRouting.InnerGroupNo", "InnerGroupNo", new Object[0]));
        this.objectGrid.addColumn((bean) -> {
            if ("waitCheck".equals(bean.getCheckStatus())) {
                return I18NUtility.getValue("Routing.CheckStatus.Wait", "waitCheck", new Object[0]);
            } else if ("passCheck".equals(bean.getCheckStatus())) {
                return I18NUtility.getValue("Routing.CheckStatus.Pass", "passCheck", new Object[0]);
            } else {
                return "noPassCheck".equals(bean.getCheckStatus()) ? I18NUtility.getValue("Routing.CheckStatus.NoPass", "noPassCheck", new Object[0]) : "";
            }
        }).setCaption(I18NUtility.getValue("ProductRouting.CheckStatus", "CheckStatus", new Object[0]));
        this.objectGrid.addColumn(ProductRouting::getRoutingDesc).setCaption(I18NUtility.getValue("ProductRouting.RoutingDesc", "RoutingDesc", new Object[0]));
        this.objectGrid.addSelectionListener((event) -> {
            Optional<ProductRouting> optional = event.getFirstSelectedItem();
            this.setButtonStatus(optional);
        });
        vlRoot.addComponent(this.objectGrid);
        vlRoot.setExpandRatio(this.objectGrid, 1.0F);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    protected void init() {
    }

    public void enter(ViewChangeEvent event) {
        this.cbStatus.setItems(new String[]{"waitCheck", "noPassCheck"});
        this.cbStatus.setItemCaptionGenerator((select) -> {
            if ("waitCheck".equals(select)) {
                return I18NUtility.getValue("Routing.CheckStatus.Wait", "waitCheck", new Object[0]);
            } else {
                return "noPassCheck".equals(select) ? I18NUtility.getValue("Routing.CheckStatus.NoPass", "noPassCheck", new Object[0]) : "";
            }
        });
        this.setButtonStatus(Optional.empty());
        this.refreshGrid();
    }

    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (this.btnSearch.equals(button)) {
            this.refreshGrid();
        } else if (this.btnPass.equals(button)) {
            ConfirmDialog.show(this.getUI(), I18NUtility.getValue("Common.SureToCheckPass", "Are you sure these selected items check Pass?", new Object[0]), (result) -> {
                if (Result.OK.equals(result.getResult())) {
                    this.checkRouting(this.objectGrid.getSelectedItems(), "passCheck");
                    this.refreshGrid();
                }

            });
        } else if (this.btnNoPass.equals(button)) {
            ConfirmDialog.show(this.getUI(), I18NUtility.getValue("Common.SureToCheckNoPass", "Are you sure these selected items check NoPass?", new Object[0]), (result) -> {
                if (Result.OK.equals(result.getResult())) {
                    this.checkRouting(this.objectGrid.getSelectedItems(), "noPassCheck");
                    this.refreshGrid();
                }

            });
        } else if (this.btnRefresh.equals(button)) {
            this.tfProductId.clear();
            this.tfRoutingGroup.clear();
            this.tfInnerGroupNo.clear();
            this.cbStatus.clear();
            this.refreshGrid();
        }

    }

    private void setButtonStatus(Optional<ProductRouting> optional) {
        boolean enable = optional.isPresent();
        this.setEnable(this.btnPass, enable);
        this.setEnable(this.btnNoPass, enable);
    }

    private void checkRouting(Set<ProductRouting> routingSet, String status) {
        Iterator var3 = routingSet.iterator();

        while(var3.hasNext()) {
            ProductRouting productRouting = (ProductRouting)var3.next();
            List<ProductRouting> routingList = productRoutingService.getProductRoutingsByIdGroupNo(productRouting.getProductId(), productRouting.getRoutingGroup(), productRouting.getInnerGroupNo(), "");
            Iterator var6 = routingList.iterator();

            while(var6.hasNext()) {
                ProductRouting routing = (ProductRouting)var6.next();
                routing.setCheckStatus(status);
                this.productRoutingService.save(routing);
            }
        }

    }

    private void refreshGrid() {
        List<ProductRouting> productRoutingList = productRoutingService.getProductRoutingsByIdGroupNo(this.tfProductId.getValue(), this.tfRoutingGroup.getValue(), this.tfInnerGroupNo.getValue(), Strings.isNullOrEmpty((String)this.cbStatus.getValue()) ? "All" : (String)this.cbStatus.getValue());
        this.objectGrid.setDataProvider(DataProvider.ofCollection(this.removeDuplicateBom(productRoutingList)));
    }

    private ArrayList<ProductRouting> removeDuplicateBom(List<ProductRouting> users) {
        Set<ProductRouting> set = new TreeSet(new Comparator<ProductRouting>() {
            public int compare(ProductRouting o1, ProductRouting o2) {
                return (o1.getProductId() + o1.getRoutingGroup() + o1.getInnerGroupNo()).compareTo(o2.getProductId() + o2.getRoutingGroup() + o2.getInnerGroupNo());
            }
        });
        set.addAll(users);
        return new ArrayList(set);
    }

    public void updateAfterFilterApply() {
    }
}
