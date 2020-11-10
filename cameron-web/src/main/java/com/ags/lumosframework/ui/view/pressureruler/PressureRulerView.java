package com.ags.lumosframework.ui.view.pressureruler;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.PressureTypeEnum;
import com.ags.lumosframework.pojo.PressureRuler;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.service.IPressureRulerService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Menu(caption = "PressureRuler", captionI18NKey = "Cameron.PressureRuler", iconPath = "images/icon/text-blob.png", groupName = "Data", order = 12)
@SpringView(name = "PressureRuler", ui = CameronUI.class)
@Secured("PressureRuler")
public class PressureRulerView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = 9209101403563067390L;

    private TextField tfProductNo = new TextField();

    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();

    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();

    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();

    @I18Support(caption = "Refresh", captionKey = "common.search")
    private Button btnSearch = new Button();

    Grid<PressureRuler> objectGrid = new Grid<>();

    private Button[] btns = new Button[]{btnSearch, btnAdd, btnEdit, btnDelete};

    @Autowired
    private AddPressureRulerDialog addPressureDialog;

    @Autowired
    private IPressureRulerService pressureRulerService;

    public PressureRulerView() {
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
        tfProductNo.setPlaceholder(I18NUtility.getValue("PressureRuler.ProductNo", "ProductNo"));
        hlTempToolBox.addComponent(tfProductNo);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(false);
        }
        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);

        objectGrid.setSizeFull();
        objectGrid.addColumn(PressureRuler::getProductNo).setCaption(I18NUtility.getValue("PressureRuler.ProductNo", "ProductNo"));
        objectGrid.addColumn(bean ->
                PressureTypeEnum.getValue(bean.getPressureType()).getType()
        ).setCaption(I18NUtility.getValue("PressureRuler.PressureType", "PressureType"));
        objectGrid.addColumn(PressureRuler::getTestPressureValue).setCaption(I18NUtility.getValue("PressureRuler.TestPressureValue", "TestPressureValue(psi)"));
        objectGrid.addColumn(PressureRuler::getMaxPressureValue).setCaption(I18NUtility.getValue("PressureRuler.MaxPressureValue", "MaxPressureValue(psi)"));
        objectGrid.addColumn(PressureRuler::getDifferencePressureValue).setCaption(I18NUtility.getValue("PressureRuler.DiffPressureValue", "DiffPressureValue(psi)"));
        objectGrid.addColumn(PressureRuler::getFirstTime).setCaption(I18NUtility.getValue("PressureRuler.FirstTime", "FirstTime(min)"));
        objectGrid.addColumn(PressureRuler::getSecondTime).setCaption(I18NUtility.getValue("PressureRuler.SecondTime", "SecondTime(min)"));
        objectGrid.addColumn(PressureRuler::getThirdTime).setCaption(I18NUtility.getValue("PressureRuler.ThirdTime", "ThirdTime(min)"));
        objectGrid.addColumn(PressureRuler::getTorqueValue).setCaption(I18NUtility.getValue("PressureRuler.TorqueValue", "TorqueValue"));
        objectGrid.addSelectionListener(new SelectionListener<PressureRuler>() {
            private static final long serialVersionUID = -3097455814672364357L;

            @Override
            public void selectionChange(SelectionEvent<PressureRuler> event) {
                Optional<PressureRuler> optional = event.getFirstSelectedItem();
                if (optional.isPresent()) {
                    setButtonStatus(Optional.of(optional.get()));
                } else {
                    setButtonStatus(Optional.empty());
                }
            }
        });
        vlRoot.addComponent(objectGrid);
        vlRoot.setExpandRatio((Component) objectGrid, 1);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        refreshGrid();
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button btn = event.getButton();
        if (btn.equals(btnAdd)) {
            addPressureDialog.setObject(null);
            addPressureDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    refreshGrid();
                }
            });
        } else if (btn.equals(btnEdit)) {
            PressureRuler ruler = objectGrid.asSingleSelect().getValue();
            addPressureDialog.setObject(ruler);
            addPressureDialog.show(getUI(), result -> refreshGrid());
        } else if (btn.equals(btnDelete)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                pressureRulerService.delete(objectGrid.asSingleSelect().getValue());
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            refreshGrid();
                        }
                    });
        } else if (btn.equals(btnSearch)) {
            refreshGrid();
        }
    }

    private void setButtonStatus(Optional<PressureRuler> optional) {
        boolean enable = optional.isPresent();
        if(RequestInfo.current().getUserName().equals("admin")){
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(enable);
            btnDelete.setEnabled(enable);
        }else{
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void refreshGrid() {
        List<PressureRuler> pressureRulerList = pressureRulerService.getAllByProductNo(tfProductNo.getValue().trim());

        objectGrid.setDataProvider(DataProvider.ofCollection(pressureRulerList));
    }

}
