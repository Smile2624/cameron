package com.ags.lumosframework.ui.view.DataStatus;

import com.ags.lumosframework.pojo.DataStatus;
import com.ags.lumosframework.service.IDataStatusService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.objectlist.ObjectListGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Menu(caption = "DataReview", captionI18NKey = "view.datareview.caption", iconPath = "images/icon/text-blob.png",groupName="CommonFunction", order = 16)
@SpringView(name = "DataReview", ui = CameronUI.class)
public class DataStatusView extends BaseView implements Button.ClickListener{
    //@Secured(PermissionConstants.OPERATIONINSTRUCTION_REFRESH)
    @I18Support(caption = "Refresh", captionKey = "common.refresh")
    private Button btnRefresh = new Button();


    private Button[] btns = new Button[] { btnRefresh };

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    //private IDomainObjectGrid<OperationInstruction> objectGrid = new PaginationDomainObjectList<>();
    private IDomainObjectGrid<DataStatus> objectGrid = new ObjectListGrid<>();

    TextField tfProductNo= new TextField();
    Button btnSearch = new Button();

    @Autowired
    private IDataStatusService dataStatusService;
    public DataStatusView(){
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);

        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        HorizontalLayout hlTemp = new HorizontalLayout();
        hlTemp.addStyleName("v-component-group material");
        hlTemp.setSpacing(false);

        hlToolBox.addComponent(hlTemp);
        hlToolBox.setComponentAlignment(hlTemp, Alignment.MIDDLE_RIGHT);

        hlTemp.addComponents(tfProductNo,btnSearch);
        tfProductNo.setPlaceholder(I18NUtility.getValue("view.datastatus.productno", "Product No"));
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnSearch.addStyleName("primary");
        btnSearch.addClickListener(this);
        btnRefresh.setIcon(VaadinIcons.REFRESH);

        //objectGrid.grid.removeAllColumns();
        objectGrid.addColumn(DataStatus::getProductNo).setCaption(I18NUtility.getValue("view.datastatus.productno", "Product No"));
        objectGrid.addColumn(DataStatus::isBomChecked).setCaption(I18NUtility.getValue("view.datastatus.isbomchecked", "BOM checked"));
        objectGrid.addColumn(DataStatus::isRtgChecked).setCaption(I18NUtility.getValue("view.datastatus.isrtgchecked", "RTG checked"));
        objectGrid.addColumn(DataStatus::isAllChecked).setCaption(I18NUtility.getValue("view.datastatus.isallchecked", "ALL checked"));
        objectGrid.setObjectSelectionListener((IObjectSelectionListener<DataStatus>) event -> setButtonStatus(event.getFirstSelectedItem()));
        vlRoot.addComponents((Component) objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    private void setButtonStatus(Optional<DataStatus> optional) {
        boolean enable = optional.isPresent();
    }
    @Override
    protected void init() {
        objectGrid.setServiceClass(IDataStatusService.class);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        objectGrid.refresh();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button btn = event.getButton();
        btn.setEnabled(true);
        if(btn.equals(btnSearch)){
            refreshGrid(tfProductNo.getValue().trim());
            objectGrid.refresh();
        }else{
            refreshGrid(null);
            objectGrid.refresh();
        }
    }

    private void refreshGrid(String productNo) {
        List<DataStatus> list = dataStatusService.listByProductNo(productNo);
        objectGrid.setData(list);
    }
}