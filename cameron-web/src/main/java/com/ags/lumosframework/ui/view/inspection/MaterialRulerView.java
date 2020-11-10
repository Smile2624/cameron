package com.ags.lumosframework.ui.view.inspection;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.pojo.VendorMaterialInspectionItems;
import com.ags.lumosframework.service.IInspectionPlanService;
import com.ags.lumosframework.service.IVendorMaterialInspectionItemService;
import com.ags.lumosframework.service.IVendorMaterialService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectSelectionListener;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.vaadin.data.ValueProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 供应商零件的检验像维护页面，用于用户按照零件号加零件版本维护检验项
 * 包括零件号及版本的增删改查以及对检验项的增删改查
 * 检验项需要维护对应的检验标准(数值型--定值和上下阀阈值，描述型)
 *
 */
//@Menu(caption = "MaterialRuler", captionI18NKey = "MaterialRuler.view.caption", iconPath = "images/icon/text-blob.png", groupName = "CommonFunction", order = 2)
//@SpringView(name = "MaterialRuler", ui = CameronUI.class)
public class MaterialRulerView extends BaseView implements Button.ClickListener{

    @I18Support(captionKey = "materilruler.add-material" ,caption = "AddMaterial")
    private Button btnCreateMaterial = new Button();
    @I18Support(captionKey = "materilruler.edit-material" ,caption = "EditMaterial")
    private Button btnModifyMaterial = new Button();
    @I18Support(captionKey = "materilruler.del-material" ,caption = "DelMaterial")
    private Button btnDelMaterial = new Button();
    @I18Support(captionKey = "materilruler.add-material-item" ,caption = "AddItem")
    private Button btnCreateItem = new Button();
    @I18Support(captionKey = "materilruler.edit-material-item" ,caption = "EditItem")
    private Button btnModifyItem = new Button();
    @I18Support(captionKey = "materilruler.del-material-item" ,caption = "DelItem")
    private Button btnDelItem = new Button();

    private Button btnRefresh = new Button();

    private  IDomainObjectGrid<VendorMaterial> mainGrid = new PaginationDomainObjectList<>();

    private IDomainObjectGrid<VendorMaterialInspectionItems> itemGrid = new PaginationDomainObjectList<>();

    private HorizontalLayout hlToolBox = new HorizontalLayout();

    private Button[] btns = new Button[]{btnCreateMaterial,btnModifyMaterial,btnDelMaterial,btnCreateItem,btnModifyItem,btnDelItem};


    @Autowired
    private AddMaterialDialog addMaterialDialog;

    @Autowired
    private AddMaterialInspectionItemsDialog addMaterialInspectionItemsDialog;

    @Autowired
    private IVendorMaterialService vendorMaterialService;

    @Autowired
    private IVendorMaterialInspectionItemService vendorMaterialInspectionItemService;

    public MaterialRulerView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);

        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlTempToolBox.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlToolBox.addComponent(hlTempToolBox);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnCreateMaterial.setIcon(VaadinIcons.PLUS);
        btnModifyMaterial.setIcon(VaadinIcons.EDIT);
        btnDelMaterial.setIcon(VaadinIcons.TRASH);
        btnCreateItem.setIcon(VaadinIcons.PLUS);
        btnModifyItem.setIcon(VaadinIcons.EDIT);
        btnDelItem.setIcon(VaadinIcons.TRASH);
        btnRefresh.setIcon(VaadinIcons.REFRESH);



        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
        hlSplitPanel.setSizeFull();
        vlRoot.addComponent(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        //grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        mainGrid.addColumn(VendorMaterial::getMaterialNo)
                .setCaption(I18NUtility.getValue("materilruler.material-no", "MaterialNo"));
        mainGrid.addColumn(VendorMaterial::getMaterialRev)
                .setCaption(I18NUtility.getValue("materilruler.material-rev", "MaterialRev"));
        mainGrid.setObjectSelectionListener(new IObjectSelectionListener<VendorMaterial>() {
            @Override
            public void itemClicked(SelectionEvent<VendorMaterial> event) {
                if(event.getFirstSelectedItem().isPresent()){
                    List<VendorMaterialInspectionItems> vendorMaterialInspectionItems
                            = vendorMaterialInspectionItemService.listById(event.getFirstSelectedItem().get().getId());
                    itemGrid.setData(vendorMaterialInspectionItems);
                }else{
                    itemGrid.setData(new ArrayList<VendorMaterialInspectionItems>());
                }
                itemGrid.refresh();
                setButtonStatus(event.getFirstSelectedItem());
            }
        });
        hlSplitPanel.setFirstComponent((Component) mainGrid);

        itemGrid.addColumn(VendorMaterialInspectionItems::getInspectionItemName)
                .setCaption(I18NUtility.getValue("materilruler.item-name", "ItemName"));
        itemGrid.addColumn(VendorMaterialInspectionItems::getInspectionItemType)
                .setCaption(I18NUtility.getValue("materilruler.item-type", "ItemType"));
        itemGrid.addColumn(new ValueProvider<VendorMaterialInspectionItems, String>() {
            @Override
            public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                if(vendorMaterialInspectionItems.getMaxValue()==0.0){
                    return "";
                }else{
                    return String.valueOf(vendorMaterialInspectionItems.getMaxValue());
                }
            }
        }).setCaption(I18NUtility.getValue("materilruler.max-value", "MaxValue"));
        itemGrid.addColumn(new ValueProvider<VendorMaterialInspectionItems, String>() {
            @Override
            public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                if(vendorMaterialInspectionItems.getMinValue()==0.0){
                    return "";
                }else{
                    return String.valueOf(vendorMaterialInspectionItems.getMinValue());
                }
            }
        }).setCaption(I18NUtility.getValue("materilruler.min-value", "MinValue"));
        itemGrid.addColumn(new ValueProvider<VendorMaterialInspectionItems, String>() {
            @Override
            public String apply(VendorMaterialInspectionItems vendorMaterialInspectionItems) {
                if(vendorMaterialInspectionItems.getStandardValue()==0.0){
                    return "";
                }else{
                    return String.valueOf(vendorMaterialInspectionItems.getStandardValue());
                }
            }
        }).setCaption(I18NUtility.getValue("materilruler.standard-value", "StandardValue"));
        itemGrid.addColumn(VendorMaterialInspectionItems::getValue)
                .setCaption(I18NUtility.getValue("materilruler.value", "Value"));
        itemGrid.setObjectSelectionListener(event -> {
            setButtonStatusItem(event.getFirstSelectedItem());
        });
        hlSplitPanel.setSecondComponent((Component) itemGrid);

        vlRoot.addComponents((Component) hlSplitPanel);
        vlRoot.setExpandRatio((Component) hlSplitPanel, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    void setButtonStatusItem(Optional<VendorMaterialInspectionItems> optional) {
        btnModifyItem.setEnabled(optional.isPresent());
        btnDelItem.setEnabled(optional.isPresent());
    }

    @Override
    protected void init() {
        mainGrid.setServiceClass(IVendorMaterialService.class);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        setButtonStatusItem(Optional.empty());
        mainGrid.refresh();
    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {

        Button button = clickEvent.getButton();
        button.setEnabled(true);
        if(btnCreateMaterial.equals(button)){
            //增加零件信息
            addMaterialDialog.setObject(null);
            addMaterialDialog.show(getUI(), new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if(ConfirmResult.Result.OK.equals(result.getResult())){
                        mainGrid.refresh();
                    }
                }
            });
        }else if (btnModifyMaterial.equals(button)){
            //修改零件信息
            addMaterialDialog.setObject(mainGrid.getSelectedObject());
            addMaterialDialog.show(getUI(), new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if(ConfirmResult.Result.OK.equals(result.getResult())){
                        mainGrid.refresh();
                    }
                }
            });
        }else if (btnDelMaterial.equals(button)){
            //删除零件信息
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                VendorMaterial vendorMaterial = mainGrid.getSelectedObject();
                                long id = vendorMaterial.getId();
                                //删除零件信息
                                vendorMaterialService.delete(vendorMaterial);
                                //删除零件检验项信息
                                List<Long> itemIds
                                        = vendorMaterialInspectionItemService.listByMaterialId(id);
                                vendorMaterialInspectionItemService.deleteByIds(itemIds);
                                mainGrid.refresh();
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                        }
                    });
        }else if (btnCreateItem.equals(button)){
            //增加检验项信息
            addMaterialInspectionItemsDialog.setObject(mainGrid.getSelectedObject(), null);
            addMaterialInspectionItemsDialog.show(getUI(), new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if(ConfirmResult.Result.OK.equals(result.getResult())){
                        List<VendorMaterialInspectionItems> vendorMaterialInspectionItems
                                = vendorMaterialInspectionItemService.listById(mainGrid.getSelectedObject().getId());
                        itemGrid.setData(vendorMaterialInspectionItems);
                        itemGrid.refresh();
                    }
                }
            });
        }else if(btnModifyItem.equals(button)){
            addMaterialInspectionItemsDialog.setObject(mainGrid.getSelectedObject(), itemGrid.getSelectedObject());
            addMaterialInspectionItemsDialog.show(getUI(), new DialogCallBack() {
                @Override
                public void done(ConfirmResult result) {
                    if(ConfirmResult.Result.OK.equals(result.getResult())){
                        List<VendorMaterialInspectionItems> vendorMaterialInspectionItems
                                = vendorMaterialInspectionItemService.listById(mainGrid.getSelectedObject().getId());
                        itemGrid.setData(vendorMaterialInspectionItems);
                        itemGrid.refresh();
                    }
                }
            });
        }else{
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                //删除零件检验项信息
                                vendorMaterialInspectionItemService.delete(itemGrid.getSelectedObject());
                                List<VendorMaterialInspectionItems> vendorMaterialInspectionItems
                                        = vendorMaterialInspectionItemService.listById(mainGrid.getSelectedObject().getId());
                                itemGrid.setData(vendorMaterialInspectionItems);
                                itemGrid.refresh();
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                        }
                    });
        }
    }

        void setButtonStatus(Optional<VendorMaterial> optional){
            btnModifyMaterial.setEnabled(optional.isPresent());
            btnDelMaterial.setEnabled(optional.isPresent());
            btnCreateItem.setEnabled(optional.isPresent());
        };
}
