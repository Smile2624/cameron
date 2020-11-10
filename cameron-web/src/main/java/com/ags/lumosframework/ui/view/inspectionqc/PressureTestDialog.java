package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.pojo.PressureTest;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.service.IPressureTestService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peyton
 * @date 2019/10/29 15:08
 */
@SpringComponent
@Scope("prototype")
public class PressureTestDialog extends BaseDialog {

    private Grid<PressureTest> grid = new Grid<>();

    @I18Support(caption = "工单号", captionKey = "")
    private LabelWithSamleLineCaption lblOrderNo = new LabelWithSamleLineCaption();

    private String caption = "压力测试结果";

    private List<PressureTest> pressureTestList;

    @Autowired
    private IPressureTestService pressureTestService;
    @Autowired
    private IProductionOrderService orderService;

    @Override
    protected void initUIData() {

    }

    @Override
    protected void okButtonClicked() throws Exception {

    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        HorizontalLayout hlLayout = new HorizontalLayout();
        hlLayout.setSizeFull();
        hlLayout.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlLayout.addComponent(lblOrderNo);
        vlRoot.addComponent(hlLayout);

        grid.setSizeFull();
        grid.addColumn(PressureTest::getProductSN).setCaption(I18NUtility.getValue("PressureTest.PorductSn", "PorductSn"));
        grid.addColumn(PressureTest::getTestResult).setCaption(I18NUtility.getValue("PressureTest.TestResult", "TestResult"));
        vlRoot.addComponent(grid);

        this.setSizeFull();
        return vlRoot;
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, "500px", null, true, true, callBack);
    }

    public void setObject(String orderNo) {
        lblOrderNo.setValue(orderNo);
        ProductionOrder order = orderService.getByNo(orderNo);

        for (int i = 0; i < order.getProductNumber(); i++) {
            String productSN = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(productSN);
            if (pressureTest == null) {
                pressureTest = new PressureTest();
                pressureTest.setProductSN(productSN);
                pressureTest.setTestResult("-");
            }
            pressureTestList.add(pressureTest);
        }

        if (pressureTestList != null && pressureTestList.size() > 0) {
            grid.setItems(pressureTestList);
        } else {
            grid.setItems(new ArrayList<>());
        }
    }
}
