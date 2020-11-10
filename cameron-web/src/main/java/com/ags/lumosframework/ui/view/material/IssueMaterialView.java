package com.ags.lumosframework.ui.view.material;


import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.pojo.IssueMaterialList;
import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IIssueMaterialService;
import com.ags.lumosframework.service.IOrderHistoryService;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.PaginationDomainObjectList;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@Menu(caption = "IssueMaterial", captionI18NKey = "IssueMaterial.view.caption", iconPath = "images/icon/text-blob.png", groupName = "CommonFunction", order = 2)
@SpringView(name = "IssueMaterial", ui = CameronUI.class)
public class IssueMaterialView extends BaseView implements Button.ClickListener {
    private static final long serialVersionUID = 4997643622270022526L;

    @I18Support(captionKey = "issueMaterial.pull-material", caption = "PullMaterial")
    private Button btnPull = new Button();

    @I18Support(captionKey = "issueMaterial.detail-info", caption = "Detail")
    private Button btnInfo = new Button();

    @I18Support(captionKey = "issuematerial.remark", caption = "Remark")
    private Button btnRemark = new Button();

    private final Button[] btns = new Button[]{btnInfo, btnPull, btnRemark};

    private final HorizontalLayout hlToolBox = new HorizontalLayout();

    private TreeGrid<ProductionOrder> grid = new TreeGrid<>();

    private final IDomainObjectGrid<IssueMaterialList> objectGrid = new PaginationDomainObjectList<>();

    private final String STAUTS_NO_MAT = "工单发料信息未导入";
    private final String STAUTS_NO_ROUTING = "工单产品对应的Routing未创建";
    private final String STAUTS_NO_PULL = "工单产品对应的Routing不需要发料操作";

    @Autowired
    IIssueMaterialService issueMaterialService;

    @Autowired
    IProductRoutingService productRoutingService;

    @Autowired
    IProductionOrderService productionOrderService;

    @Autowired
    IOrderHistoryService orderHistoryService;

    @Autowired
    ShowRoutingAttentionDialog showRoutingAttentionDialog;

    @Autowired
    MatRemarkDialog matRemarkDialog;

    public IssueMaterialView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        hlTempToolBox.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnInfo.setIcon(VaadinIcons.OPEN_BOOK);
        btnPull.setIcon(VaadinIcons.TRUCK);
        btnPull.setEnabled(false);
        btnRemark.setIcon(VaadinIcons.TAGS);
        btnRemark.setEnabled(false);

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSplitPosition(200.0F, Unit.PIXELS);
        verticalSplitPanel.setSizeFull();
        vlRoot.addComponent(verticalSplitPanel);
        vlRoot.setExpandRatio(verticalSplitPanel, 1);

        grid.setSizeFull();
        grid.addColumn(ProductionOrder::getProductOrderId).setCaption(I18NUtility.getValue("issuematerial.order-no", "OrderNo"));
        grid.addColumn(bean ->
                bean.getPullMatFlag() ? "是" : "否"
        ).setCaption(I18NUtility.getValue("issuematerial.status", "Status"));
        grid.addColumn(bean ->
                checkStatus(bean)
        ).setCaption(I18NUtility.getValue("issuematerial.remark", "Remark"));
        grid.addSelectionListener((SelectionListener<ProductionOrder>) selectionEvent -> {
            setButtonStatus(selectionEvent.getFirstSelectedItem());
            Optional<ProductionOrder> firstSelectedItem =
                    selectionEvent.getFirstSelectedItem();
            setDataToRight(firstSelectedItem);
        });

        verticalSplitPanel.setFirstComponent(grid);
        objectGrid.addColumn(IssueMaterialList::getMaterialNo)
                .setCaption(I18NUtility.getValue("issuematerial.material-no", "MaterialNo"));
        objectGrid.addColumn(IssueMaterialList::getMaterialDesc)
                .setCaption(I18NUtility.getValue("issuematerial.material-desc", "MaterialDesc"));
        objectGrid.addColumn(IssueMaterialList::getRequirementQuantity)
                .setCaption(I18NUtility.getValue("issuematerial.require-quantity", "RequirementQuantity"));
        objectGrid.addColumn(IssueMaterialList::getQuantityWithdrawn)
                .setCaption(I18NUtility.getValue("issuematerial.quantity-with-drawn", "QuantityWithdrawn"));
        objectGrid.addColumn(IssueMaterialList::getShortage)
                .setCaption(I18NUtility.getValue("issuematerial.shortage", "Shortage"));
        objectGrid.addColumn(IssueMaterialList::getProdStorage)
                .setCaption(I18NUtility.getValue("issuematerial.prod-storage", "Peod.Storage"));
        objectGrid.addColumn(IssueMaterialList::getDescription)
                .setCaption(I18NUtility.getValue("issuematerial.remark", "Remark"));
        objectGrid.setObjectSelectionListener(event -> {
            setItemButtonStatus(event.getFirstSelectedItem());
        });
        verticalSplitPanel.setSecondComponent((Component) objectGrid);

        vlRoot.addComponents(verticalSplitPanel);
        vlRoot.setExpandRatio(verticalSplitPanel, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
        System.out.println("1++" + LocalDateTime.now());
    }

    private void setDataToRight(Optional<ProductionOrder> item) {
        btnPull.setEnabled(item.isPresent());
        if (item.isPresent()) {
            List<IssueMaterialList> issueMaterialLists = issueMaterialService.listByOrderNo(item.get().getProductOrderId());
            objectGrid.setData(issueMaterialLists);
        } else {
            objectGrid.setData(new ArrayList<>());
        }
        objectGrid.refresh();
    }

    private void setButtonStatus(Optional<ProductionOrder> optional) {
        boolean enable = optional.isPresent();
        btnInfo.setEnabled(enable);
    }

    private void setItemButtonStatus(Optional<IssueMaterialList> optional) {
        boolean enable = optional.isPresent();
        btnRemark.setEnabled(enable);
    }

    @Override
    protected void init() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setButtonStatus(Optional.empty());
        setItemButtonStatus(Optional.empty());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            System.out.println(event.getParameters());
            ProductionOrder order = productionOrderService.getByNo(event.getParameters().split("-")[1]);
            refresh(order.getProductOrderId());
        }
        System.out.println("2++" + LocalDateTime.now());
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnPull.equals(button)) {
            //点击完成发料，并完成工序确认第一步发料
            ProductionOrder selectedObject = grid.asSingleSelect().getValue();
            if (selectedObject.getPullMatFlag()) {
                throw new PlatformException("已发料工单无法重复发料");
            }
            //判断工单是否锁定
            if (selectedObject.getBomLockFlag()) {
                throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                        "This Order is locked,Please contact the engineer to solve !"));
            }
            String checkResult = checkStatus(selectedObject);
            if (STAUTS_NO_MAT.equals(checkResult)) {
                throw new PlatformException("请先导入发料信息");
            }
            if (STAUTS_NO_ROUTING.equals(checkResult)) {
                throw new PlatformException("请将工单绑定对应的Routing");
            }
            if (STAUTS_NO_PULL.equals(checkResult)) {
                throw new PlatformException("工单产品对应的Routing不需要发料操作");
            }
            List<IssueMaterialList> issueMaterialLists = issueMaterialService.listByOrderNo(selectedObject.getProductOrderId());
            //更新对应order下面所有物料的发料状态
            List<IssueMaterialList> tempNew = new ArrayList<>();
            for (IssueMaterialList issueMaterialList : issueMaterialLists) {
                issueMaterialList.setStatus(AppConstant.STATUS_Y);
                tempNew.add(issueMaterialList);
            }
            issueMaterialService.saveAll(tempNew);
            //工序记录确认
            //通过工单获取产品信息--> routing信息
            ProductionOrder order = productionOrderService.getByNo(selectedObject.getProductOrderId());
            Object[] result = productRoutingService.getPullMaterialStep(order.getRoutingGroup(), order.getInnerGroupNo(), "Material");
            ProductRouting routingStep = (ProductRouting) result[1];

            OrderHistory oh = orderHistoryService.getByOrderNoOPerationNo(order.getProductOrderId(), routingStep.getOprationNo());
            if (oh == null) {
                oh = new OrderHistory();
                oh.setOrderNo(selectedObject.getProductOrderId());
                oh.setOperationNo(routingStep.getOprationNo());
                oh.setOperationDesc(routingStep.getOprationDesc());
                oh.setOperationAttention(routingStep.getAttention());
            }
            oh.setConfirmBy(RequestInfo.current().getUserName());
            oh.setConfirmDate(new Date());
            orderHistoryService.save(oh);
            //更新工单发料状态
            selectedObject.setPullMatFlag(true);
            productionOrderService.save(selectedObject);
            //刷新数据
            refresh(selectedObject.getProductOrderId());
        } else if (btnInfo.equals(button)) {
            ProductionOrder selectedObject = grid.asSingleSelect().getValue();
            String checkResult = checkStatus(selectedObject);
            showRoutingAttentionDialog.setObject(checkResult);
            showRoutingAttentionDialog.show(getUI(), null);
        } else if (btnRemark.equals(button)) {
            ProductionOrder selectedObject = grid.asSingleSelect().getValue();
            IssueMaterialList obj = objectGrid.getSelectedObject();
            matRemarkDialog.setObject(obj);
            matRemarkDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
//                    refresh(selectedObject.getProductOrderId());
                    grid.select(selectedObject);
                    setDataToRight(Optional.ofNullable(selectedObject));

                }
            });
        }
    }


    private void refresh(String orderNo) {
        EntityFilter createFilter = productionOrderService.createFilter();

        createFilter.fieldEqualTo(ProductionOrderEntity.PRODUCT_ORDER_ID, orderNo);

        List<ProductionOrder> poList = productionOrderService.listByFilter(createFilter);
        grid.setItems(poList, ProductionOrder::getChild);
    }


    public String checkStatus(ProductionOrder order) {

        List<IssueMaterialList> issueMaterialLists = issueMaterialService.listByOrderNo(order.getProductOrderId());
        if (issueMaterialLists == null || issueMaterialLists.size() == 0) {
            return STAUTS_NO_MAT;
        } else {
            List<ProductRouting> productRoutings = productRoutingService.getOrderRouting(order.getRoutingGroup(), order.getInnerGroupNo());
            if (productRoutings == null || productRoutings.size() == 0) {
                return STAUTS_NO_ROUTING;
            } else {
                ProductRouting routing = productRoutings.get(0);
                if (routing.getOprationDesc().contains("发料") || routing.getOprationDesc().toUpperCase().contains("PULL MATERIAL PER BOM")) {
                    return routing.getAttention();
                } else {
                    return STAUTS_NO_PULL;
                }
            }
        }
    }
}
