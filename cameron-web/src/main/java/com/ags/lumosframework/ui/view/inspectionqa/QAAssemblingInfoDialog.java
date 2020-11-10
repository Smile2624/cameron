package com.ags.lumosframework.ui.view.inspectionqa;

import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.service.IAssemblingService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class QAAssemblingInfoDialog extends BaseDialog {

    /**
     *
     */
    private static final long serialVersionUID = 958989479293371247L;
    HorizontalLayout hlLayout = new HorizontalLayout();
    private Grid<Assembling> gridSingleton = new Grid<>();
    private Grid<Assembling> gridBatch = new Grid<>();
    @I18Support(caption = "工单号", captionKey = "")
    private LabelWithSamleLineCaption lblOrderNo = new LabelWithSamleLineCaption();

    @I18Support(caption = "数量", captionKey = "")
    private LabelWithSamleLineCaption lblQty = new LabelWithSamleLineCaption();

    @I18Support(caption = "上一个SN", captionKey = "")
    private Button btnPrevious = new Button();

    @I18Support(caption = "下一个SN", captionKey = "")
    private Button btnNext = new Button();

    @I18Support(caption = "当前SN", captionKey = "")
    private LabelWithSamleLineCaption lblSN = new LabelWithSamleLineCaption();

    @Autowired
    private IAssemblingService assemblingSevice;

    @Autowired
    private IProductionOrderService productionOrderService;


    private String caption = "装配记录信息";
    //追溯类型是批次的集合
    private List<Assembling> assemblingListBatch;
    //追溯类型是到件的集合
    private List<Assembling> assemblingListPiece;

    private List<String> orderSNList;

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, "100%", "100%", true, true, callBack);
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        hlLayout.setSizeFull();
        hlLayout.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        hlLayout.addComponent(lblOrderNo);
        hlLayout.addComponent(lblQty);
        hlLayout.addComponent(lblSN);
        hlLayout.addComponent(btnPrevious);
        hlLayout.addComponent(btnNext);
        btnPrevious.setIcon(VaadinIcons.ARROW_CIRCLE_LEFT);
        btnNext.setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT);
        vlRoot.addComponent(hlLayout);

        btnPrevious.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                String currentSn = lblSN.getValue().trim();
                if (!Strings.isNullOrEmpty(currentSn)) {
                    //1.获取当前Order的SN总数
                    int count = orderSNList.size();
                    //2.当前SN的index
                    int index = orderSNList.indexOf(currentSn);
                    int prevSNIndex = index - 1;
                    if (prevSNIndex == -1) {
                        NotificationUtils.notificationError("当前SN:" + currentSn + "已经是Order:" + lblOrderNo.getValue() + "的第一个SN");
                        return;
                    }
                    String prevSN = orderSNList.get(prevSNIndex);
                    lblSN.setValue(prevSN);
                    assemblingListBatch = assemblingSevice.getBySnType(prevSN, AppConstant.RETROSPECT_TYPE_BATCH);
                    assemblingListPiece = assemblingSevice.getBySnType(prevSN, AppConstant.RETROSPECT_TYPE_PIECE);
                    gridSingleton.setItems(assemblingListPiece);
                    gridBatch.setItems(assemblingListBatch);
                }
            }
        });

        btnNext.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 4561031816491059018L;

            @Override
            public void buttonClick(ClickEvent event) {
                String currentSn = lblSN.getValue().trim();
                if (!Strings.isNullOrEmpty(currentSn)) {
                    //1.获取当前Order的SN总数
                    int count = orderSNList.size();
                    //2.当前SN的index
                    int index = orderSNList.indexOf(currentSn);
                    int nextSNIndex = index + 1;
                    if (nextSNIndex == count) {
                        NotificationUtils.notificationError("当前SN:" + currentSn + "已经是Order:" + lblOrderNo.getValue() + "的最后一个SN");
                        return;
                    }
                    String nextSN = orderSNList.get(nextSNIndex);
                    lblSN.setValue(nextSN);
                    assemblingListBatch = assemblingSevice.getBySnType(nextSN, AppConstant.RETROSPECT_TYPE_BATCH);
                    assemblingListPiece = assemblingSevice.getBySnType(nextSN, AppConstant.RETROSPECT_TYPE_PIECE);
                    gridSingleton.setItems(assemblingListPiece);
                    gridBatch.setItems(assemblingListBatch);
                }
            }
        });
        //*****************
        gridSingleton.setSizeFull();
        gridSingleton.addColumn(Assembling::getPartNo).setCaption(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）"));
        gridSingleton.addColumn(Assembling::getPartDesc).setCaption(I18NUtility.getValue("AssemblingInfo.Description", "Description"));
        gridSingleton.addColumn(Assembling::getPlmRev).setCaption(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev"));
        gridSingleton.addColumn(Assembling::getPartRev).setCaption(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev"));
        gridSingleton.addColumn(Assembling::getSerialNo).setCaption(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No"));
        gridSingleton.addColumn(Assembling::getHeatNoLot).setCaption(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot"));
        gridSingleton.addColumn(Assembling::getHardness).setCaption(I18NUtility.getValue("AssemblingInfo.Hardness", "Hardness"));
        gridSingleton.addColumn(Assembling::getMatType).setCaption("Mat Type");
        gridSingleton.addColumn(Assembling::getQcCheck).setCaption("Q/C Check");
        gridSingleton.setHeight("200px");
        vlRoot.addComponent(gridSingleton);

        //*****************
        gridBatch.setSizeFull();
        gridBatch.addColumn(Assembling::getPartNo).setCaption(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）"));
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
        gridBatch.setHeight("200px");
        vlRoot.addComponent(gridBatch);

        this.setSizeFull();
        return vlRoot;
    }

    @Override
    protected void initUIData() {

    }

    @Override
    protected void okButtonClicked() throws Exception {

    }

    public void setData(String orderNo) {
        lblOrderNo.setValue(orderNo);
        lblQty.setValue(Integer.toString(productionOrderService.getByNo(orderNo).getProductNumber()));
        orderSNList = assemblingSevice.getOrderSNList(orderNo);
        if (orderSNList != null && orderSNList.size() > 0) {
            String orderSn = orderSNList.get(0);
            lblSN.setValue(orderSn);
            assemblingListBatch = assemblingSevice.getBySnType(orderSn, AppConstant.RETROSPECT_TYPE_BATCH);
            assemblingListPiece = assemblingSevice.getBySnType(orderSn, AppConstant.RETROSPECT_TYPE_PIECE);
            gridSingleton.setItems(assemblingListPiece);
            gridBatch.setItems(assemblingListBatch);
        }
    }
}
