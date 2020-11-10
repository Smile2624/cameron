package com.ags.lumosframework.ui.view.orderhistory;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.google.common.base.Strings;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Menu(caption = "OrderHistory", captionI18NKey = "OrderHistory.view.caption", iconPath = "images/icon/text-blob.png", groupName = "CommonFunction", order = 2)
@SpringView(name = "OrderHistory", ui = CameronUI.class)
public class OrderHistoryView extends BaseView implements Button.ClickListener, IFilterableView {

    private static final long serialVersionUID = 1729856915796463818L;
    String productOrderId;
    SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    @I18Support(caption = "Search", captionKey = "OrderHistory.Search")
    private Button btnSearch = new Button();
    @I18Support(caption = "Confirm", captionKey = "OrderHistory.Confirm")
    private Button btnConfirm = new Button();//首次确认
    @I18Support(caption = "ReConfirm", captionKey = "OrderHistory.ReConfirm")
    private Button btnReConfirm = new Button();//再次确认
    @I18Support(caption = "CreatReport", captionKey = "OrderHistory.CreatReport")
    private Button btnCreatReport = new Button();
    @I18Support(caption = "Add", captionKey = "common.add")
    private Button btnAdd = new Button();
    @I18Support(caption = "Edit", captionKey = "common.edit")
    private Button btnEdit = new Button();
    @I18Support(caption = "Delete", captionKey = "common.delete")
    private Button btnDelete = new Button();
    private Button[] btns = new Button[]{btnSearch, btnConfirm, btnReConfirm, btnCreatReport, btnAdd, btnEdit, btnDelete};//btnSearch,
    //private Grid<ProductRouting> routingGrid = new Grid<>();
    private Grid<OrderHistory> routingGrid = new Grid<>();
    private Grid<OrderHistory> historyGrid = new Grid<>();
    //    private List<ProductRouting> productRoutingList = new ArrayList<>();
    private List<OrderHistory> orderHistoryList = new ArrayList<>();
    //    private List<OrderHistory> confirmedOperationList = new ArrayList<>();
    private Boolean isOperatorLeader;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IProductRoutingService productRoutingService;
    @Autowired
    private IOrderHistoryService orderHistoryService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private ShowRouteDetailsDialog showRouteDetailsDialog;
    @Autowired
    private UserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private AddRoutingDialog addRoutingDialog;

    public OrderHistoryView() {
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

        hlTempToolBox.addComponents(cbOrder);
        cbOrder.setPlaceholder(I18NUtility.getValue("ProductRouting.ProductOrderId", "ProductOrderId"));
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnConfirm.setEnabled(false);
        btnReConfirm.setEnabled(false);
        btnCreatReport.setEnabled(false);
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        btnSearch.setIcon(VaadinIcons.SEARCH);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnReConfirm.setIcon(VaadinIcons.CHECK);
        btnCreatReport.setIcon(VaadinIcons.BOOK);
        btnAdd.setIcon(VaadinIcons.PLUS);
        btnEdit.setIcon(VaadinIcons.EDIT);
        btnDelete.setIcon(VaadinIcons.TRASH);
        cbOrder.addSelectionListener(new SingleSelectionListener<ProductionOrder>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<ProductionOrder> event) {
                if (event.getValue() == null) {
                    btnCreatReport.setEnabled(false);
                    btnAdd.setEnabled(false);
                } else {
                    btnCreatReport.setEnabled(true);
                    btnAdd.setEnabled(true);
                }

            }
        });

        HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
        hlSplitPanel.setSizeFull();
        //hlSplitPanel.setSplitPosition(330.0F, Unit.PIXELS);
        hlSplitPanel.setSplitPosition(50.0F, Unit.PERCENTAGE);

        routingGrid.setSizeFull();
        routingGrid.addColumn(OrderHistory::getOperationNo).setCaption(I18NUtility.getValue("ProductRouting.OprationNo", "OprationNo")).setWidth(150.0F);
        routingGrid.addColumn(OrderHistory::getOperationDesc).setCaption(I18NUtility.getValue("ProductRouting.OprationDesc", "OprationDesc"));
//        routingGrid.addColumn(ProductRouting::getAttention).setCaption(I18NUtility.getValue("ProductRouting.Attention", "Attention"));
        routingGrid.addComponentColumn((ValueProvider<OrderHistory, Button>) source -> {
            Button btnCheck = new Button();
            btnCheck.setCaption("查看工序详情");
            btnCheck.addStyleName(ValoTheme.BUTTON_SMALL);
            if (Strings.isNullOrEmpty(source.getOperationAttention())) {
                btnCheck.setVisible(false);
            } else {
                btnCheck.setVisible(true);
            }
            btnCheck.addClickListener(event -> {
                showRouteDetailsDialog.setObject(source.getOperationAttention());
                showRouteDetailsDialog.show(getUI(), null);
            });
            return btnCheck;
        }).setWidth(150.0F);
        routingGrid.addSelectionListener(event -> {
            Optional<OrderHistory> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                btnConfirm.setEnabled(true);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
            } else {
                btnConfirm.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
            }
        });
        hlSplitPanel.setFirstComponent(routingGrid);
        //双击一行弹出工序描述长文本
//        routingGrid.addItemClickListener(event -> {
//            if (event.getMouseEventDetails().isDoubleClick()) {
//                if (event.getItem() != null) {
//                    ProductRouting productRouting = event.getItem();
//                    showRouteDetailsDialog.setObject(productRouting.getAttention());
//                    showRouteDetailsDialog.show(getUI(), null);
//                }
//            }
//        });


        historyGrid.setSizeFull();
        historyGrid.addColumn(OrderHistory::getOperationNo).setCaption(I18NUtility.getValue("OrderHistory.OperationNo", "OperationNo"));
//        historyGrid.addColumn(OrderHistory::getOperationDesc).setCaption(I18NUtility.getValue("OrderHistory.OperationDesc", "OperationDesc"));
        historyGrid.addColumn(history -> {
            return history.getConfirmDate() == null ? "" : myFmt.format(history.getConfirmDate());
        }).setCaption(I18NUtility.getValue("OrderHistory.ConfirmDate", "ConfirmDate"));
        historyGrid.addColumn(OrderHistory::getConfirmBy).setCaption(I18NUtility.getValue("OrderHistory.ConfirmBy", "ConfirmBy"));
        historyGrid.addColumn(history -> {
            return history.getReConfirmDate() == null ? "" : myFmt.format(history.getReConfirmDate());
        }).setCaption(I18NUtility.getValue("OrderHistory.ReConfirmDate", "ReConfirmDate"));
        historyGrid.addColumn(OrderHistory::getReConfirmBy).setCaption(I18NUtility.getValue("OrderHistory.ReConfirmBy", "ReConfirmBy"));
        historyGrid.addSelectionListener(event -> {
            Optional<OrderHistory> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                if (isOperatorLeader) {
                    btnReConfirm.setEnabled(true);
                }
            } else {
                btnReConfirm.setEnabled(false);
            }
        });
        hlSplitPanel.setSecondComponent(historyGrid);

        vlRoot.addComponents(hlSplitPanel);
        vlRoot.setExpandRatio(hlSplitPanel, 1);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

//        KeyAction kaOrderNumber = new KeyAction(ShortcutAction.KeyCode.ENTER, new int[]{});
//        kaOrderNumber.addKeypressListener(new KeyAction.KeyActionListener() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
//                getOrderRouting();
//            }
//        });
//        kaOrderNumber.extend(tfproductOrderId);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnSearch.equals(button)) {
            getOrderRouting();
        } else if (btnConfirm.equals(button)) {
            confirmRouting();
        } else if (btnReConfirm.equals(button)) {
            reConfirmRouting();
        } else if (btnCreatReport.equals(button)) {
            try {
                this.creatGxReport();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        } else if (btnAdd.equals(button)) {
            OrderHistory oh1 = new OrderHistory();
            oh1.setOrderNo(cbOrder.getValue().getProductOrderId());
            addRoutingDialog.setObject(oh1);
            addRoutingDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshRoutingGrid(oh1.getOrderNo());
                }
            });
        } else if (btnEdit.equals(button)) {
            OrderHistory oh1 = routingGrid.asSingleSelect().getValue();
            addRoutingDialog.setObject(oh1);
            addRoutingDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshRoutingGrid(oh1.getOrderNo());
                }
            });
        } else if (btnDelete.equals(button)) {
            ConfirmDialog.show(getUI(),
                    I18NUtility.getValue("Common.SureToDelete", "Are you sure to delete the selected item?"),
                    result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                            try {
                                OrderHistory orderHistory = routingGrid.asSingleSelect().getValue();
                                orderHistory.setDeleteFlag(true);
                                orderHistoryService.save(orderHistory);
                            } catch (PlatformException e) {
                                notificationError("Common.RelationShipCheckFailed", e.getMessage());
                                return;
                            }
                            freshRoutingGrid(cbOrder.getValue().getProductOrderId());
                            freshHistoryGrid(cbOrder.getValue().getProductOrderId());
                        }
                    });
        }
    }

    /**
     * 获取工单对应Routing工序
     */
    public void getOrderRouting() {
        if (cbOrder.getValue() == null) {
            NotificationUtils.notificationError("请选择订单号");
        } else {
            ProductionOrder productionOrder = cbOrder.getValue();
            if (productionOrder != null) {
                //判断工单是否锁定
                if(productionOrder.getBomLockFlag()){
                    throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                            "This Order is locked,Please contact the engineer to solve !"));
                }

                List<ProductRouting> productRoutingList = productRoutingService.getByGroupNoAndInnerNo(productionOrder.getRoutingGroup(), productionOrder.getInnerGroupNo());
                if (productRoutingList == null || productRoutingList.size() == 0) {
                    throw new PlatformException("该工单尚未绑定工艺Routing，请先对工单工艺路径进行维护！");
                }

                for (ProductRouting pr : productRoutingList) {
                    OrderHistory oh = orderHistoryService.getByOrderNoOPerationNo(productionOrder.getProductOrderId(), pr.getOprationNo());
                    if (oh == null) {
                        oh = new OrderHistory();
                        oh.setOrderNo(productionOrder.getProductOrderId());
                        oh.setOperationNo(pr.getOprationNo());
                        oh.setOperationDesc(pr.getOprationDesc());
                        oh.setOperationAttention(pr.getAttention());
                        oh.setReconfirmNeeded(pr.isReconfirmNeeded());
                        orderHistoryService.save(oh);
                    }
                }
                productOrderId = cbOrder.getValue().getProductOrderId();
                freshRoutingGrid(productOrderId);
                freshHistoryGrid(productOrderId);
            } else {
                NotificationUtils.notificationError("工单号不存在，请检查是否输入有误！");
            }
        }
    }

    private void freshRoutingGrid(String productOrderId) {
        orderHistoryList = orderHistoryService.getByOrderId(productOrderId);
        routingGrid.setDataProvider(DataProvider.ofCollection(orderHistoryList));
    }

    /**
     * 获取已确认工序
     *
     * @param productOrderId
     */
    private void freshHistoryGrid(String productOrderId) {
        List<OrderHistory> confirmedOperationList = orderHistoryService.getConfirmedOrderById(productOrderId);
        historyGrid.setDataProvider(DataProvider.ofCollection(confirmedOperationList));
    }

    /**
     * 首次确认
     */
    public void confirmRouting() {
        CaConfig caConfig = caConfigService.getConfigByType("CA_CONFIRM_ROUTING_MIN");
        if (caConfig == null) {
            NotificationUtils.notificationError("工序确认最小间隔时间未配置，请联系管理员进行配置！");
        } else {
            long confirmMin = Long.valueOf(caConfig.getConfigValue());
            OrderHistory productRouting = routingGrid.asSingleSelect().getValue();
            if (productRouting.getConfirmBy() != null) {
                NotificationUtils.notificationError("首次确认已经完成，只能二次确认！");
            } else {
                Date tempPreDate = null;

                for (OrderHistory routing : orderHistoryList) {
                    if (routing == productRouting) {
                        if (tempPreDate != null) {
                            if (getDateDiffMin(tempPreDate, new Date()) < confirmMin) {
                                NotificationUtils.notificationError("距离上一工序确认时间 不满足规定的最小间隔时间，请稍后再确认！");
                                break;
                            }
                        }
                        routing.setConfirmBy(RequestInfo.current().getUserName());
                        routing.setConfirmDate(new Date());
                        orderHistoryService.save(routing);
                        freshRoutingGrid(routing.getOrderNo());
                        freshHistoryGrid(routing.getOrderNo());
                        System.out.println(productRouting.getOperationNo());
                        System.out.println(orderHistoryList.get(orderHistoryList.size() - 1).getOperationNo());
                        if (productRouting.getOperationNo().equals(orderHistoryList.get(orderHistoryList.size() - 1).getOperationNo()) && (!productRouting.isReconfirmNeeded())) {
                            ConfirmDialog.show(getUI(),
                                    "所有工序都已确认，是否生成报告？",
                                    result -> {
                                        if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                            try {
                                                creatGxReport();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                        if (productRouting.isReconfirmNeeded()) {
                            NotificationUtils.notificationInfo("该工序需要主管或领班进行二次确认！");
                        }
                        break;
                    }
                    if (routing.getConfirmBy() != null) {
                        if (routing.isReconfirmNeeded() && routing.getReConfirmBy() == null) {
                            NotificationUtils.notificationError("工序" + routing.getOperationNo() + "需要二次确认！");
                            return;
                        }
                        tempPreDate = routing.getConfirmDate();
                        continue;
                    } else {
                        NotificationUtils.notificationError("上一工序 " + routing.getOperationNo() + " 尚未确认完成！");
                        break;
                    }

                }
            }
        }

    }

    /**
     * 二次确认
     */
    public void reConfirmRouting() {
        OrderHistory orderHistory = historyGrid.asSingleSelect().getValue();
        if (orderHistory.getReConfirmBy() != null || orderHistory.getReConfirmDate() != null) {
            NotificationUtils.notificationError("二次确认已经完成，不能再二次确认！");
        } else {
            orderHistory.setReConfirmBy(RequestInfo.current().getUserName());
            orderHistory.setReConfirmDate(new Date());
            orderHistoryService.save(orderHistory);
            freshRoutingGrid(orderHistory.getOrderNo());
            freshHistoryGrid(orderHistory.getOrderNo());
            if (orderHistory.getOperationNo().equals(orderHistoryList.get(orderHistoryList.size() - 1).getOperationNo())) {
                ConfirmDialog.show(getUI(),
                        "所有工序都已确认，是否生成报告？",
                        result -> {
                            if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                try {
                                    creatGxReport();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
    }

    @Override
    protected void init() {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //权限设置
        String loginUserName = RequestInfo.current().getUserName();
        List<Role> role = userService.getByName(loginUserName).getRole();
        if (loginUserName.equals("admin")) {
            isOperatorLeader = true;
            btnAdd.setVisible(true);
            btnEdit.setVisible(true);
            btnDelete.setVisible(true);
        } else if (role.contains(roleService.getByName("OperatorLeader"))) {
            isOperatorLeader = true;
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        } else {
            isOperatorLeader = false;
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        }

        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        if (event.getParameters() != null && !event.getParameters().equals("")) {
            if (event.getParameters().equals("sync")) {
                syncAllRoutings();
                return;
            }
            cbOrder.setValue(productionOrderService.getByNo(event.getParameters()));
            btnSearch.click();
        }
    }

    //将所有进行到一般的工单routing从PRODUCT_ROUTING表复制到ORDER_HISTORY表
    public void syncAllRoutings() {
        List<ProductionOrder> allPO = productionOrderService.getAllOrder();
        for (ProductionOrder po : allPO) {
            List<ProductRouting> allPR = productRoutingService.getByGroupNoAndInnerNo(po.getRoutingGroup(), po.getInnerGroupNo());
            if (allPR == null || allPR.size() == 0) {
                continue;
            }
            for (ProductRouting pr : allPR) {
                if (orderHistoryService.getByOrderNoOPerationNo(po.getProductOrderId(), pr.getOprationNo()) == null) {
                    OrderHistory ohTemp = new OrderHistory();
                    ohTemp.setOrderNo(po.getProductOrderId());
                    ohTemp.setOperationNo(pr.getOprationNo());
                    ohTemp.setOperationDesc(pr.getOprationDesc());
                    ohTemp.setOperationAttention(pr.getAttention());
                    ohTemp.setReconfirmNeeded(pr.isReconfirmNeeded());
                    orderHistoryService.save(ohTemp);
                }
            }
        }
    }

    @Override
    public void updateAfterFilterApply() {

    }

    /**
     * 计算两个时间差值（分钟min）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private long getDateDiffMin(Date startDate, Date endDate) {
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        long min = diff / nm;
        return min;
    }

    public Map<String, Object> getGXrecordDatas() throws Exception {
        Map<String, Object> dataMap = new HashMap();
        ProductionOrder productionOrder = (ProductionOrder) this.cbOrder.getValue();
        if (productionOrder != null) {
            List<OrderHistory> routingList = this.orderHistoryService.getByOrderId(productionOrder.getProductOrderId());
            if (routingList != null && routingList.size() != 0) {
                String start = "<w:vmerge w:val='restart'/>";
                String end = "<w:vmerge/>";
                List<Map<String, Object>> listT = new ArrayList();
                Iterator var7 = routingList.iterator();

                while (var7.hasNext()) {
                    OrderHistory routing = (OrderHistory) var7.next();
                    Map<String, Object> dataMapT = new HashMap();
                    if (routing.getConfirmBy() == null) {
                        throw new PlatformException("该工单对应得 " + routing.getOperationNo() + " 工序尚未确认！");
                    }

                    SimpleDateFormat myFmt;
                    BASE64Encoder encoder;
                    Media mediaImage;
                    if (Strings.isNullOrEmpty(routing.getReConfirmBy())) {
                        dataMapT.put("operation", routing.getOperationNo());
                        dataMapT.put("description", routing.getOperationDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                                .replaceAll("'", "&apos;"));
                        dataMapT.put("empOrSup", "Employee");
                        dataMapT.put("employeId", routing.getConfirmBy());
                        myFmt = new SimpleDateFormat("yyyy.MM.dd");
                        dataMapT.put("date", routing.getConfirmDate() == null ? "" : myFmt.format(routing.getConfirmDate()));
                        encoder = new BASE64Encoder();
                        mediaImage = this.caMediaService.getMediaByName(routing.getConfirmBy());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
                        }

                        dataMapT.put("signature", encoder.encode(this.inputStream2byte(mediaImage.getMediaStream())));
                        if (routing.getOperationAttention() == null || routing.getOperationAttention().equals("")) {
                            dataMapT.put("longtext", "");
                        } else {
                            dataMapT.put("longtext", "<w:tr wsp:rsidR=\"008D52A5\" wsp:rsidRPr=\"00D85E4D\" wsp:rsidTr=\"00B305DD\"><w:trPr>" +
                                    "<w:trHeight w:val=\"693\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"9576\" w:type=\"dxa\"/><w:gridSpan w:val=\"5\"/>" +
                                    "<w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p wsp:rsidR=\"008D52A5\" wsp:rsidRPr=\"00D85E4D\" wsp:rsidRDefault=\"008D52A5\">" +
                                    "<w:r><w:rPr><w:rFonts w:hint=\"fareast\"/></w:rPr></w:r><w:r><w:t>"
                                    + routing.getOperationAttention().replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                                    .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                                    .replaceAll("'", "&apos;").replaceAll("\n", "</w:t></w:r></w:p><w:p><w:r><w:t>")
                                    + "</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/></w:rPr></w:r></w:p></w:tc></w:tr>");
                        }
                        listT.add(dataMapT);
                    } else {
                        dataMapT.put("start", start);
                        dataMapT.put("start1", start);
                        dataMapT.put("operation", routing.getOperationNo());
                        dataMapT.put("description", routing.getOperationDesc().replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                                .replaceAll("'", "&apos;"));
                        dataMapT.put("empOrSup", "Employee");
                        dataMapT.put("employeId", routing.getConfirmBy());
                        myFmt = new SimpleDateFormat("yyyy.MM.dd");
                        dataMapT.put("date", routing.getConfirmDate() == null ? "" : myFmt.format(routing.getConfirmDate()));
                        encoder = new BASE64Encoder();
                        mediaImage = this.caMediaService.getMediaByName(routing.getConfirmBy());
                        if (mediaImage == null) {
                            throw new PlatformException("用户：" + routing.getConfirmBy() + " 签名logo未配置添加！");
                        }

                        dataMapT.put("signature", encoder.encode(this.inputStream2byte(mediaImage.getMediaStream())));
                        dataMapT.put("longtext", "");
                        listT.add(dataMapT);
                        Map<String, Object> dataMapT2 = new HashMap();
                        dataMapT2.put("operation", routing.getOperationNo());
                        dataMapT2.put("description", routing.getOperationDesc());
                        dataMapT2.put("empOrSup", "Supervisor");
                        dataMapT2.put("employeId", routing.getReConfirmBy());
                        dataMapT2.put("date", routing.getReConfirmDate() == null ? "" : myFmt.format(routing.getReConfirmDate()));
                        Media mediaImage2 = this.caMediaService.getMediaByName(routing.getReConfirmBy());
                        if (mediaImage2 == null) {
                            throw new PlatformException("用户：" + routing.getReConfirmBy() + " 签名logo未配置添加！");
                        }

                        dataMapT2.put("signature", encoder.encode(this.inputStream2byte(mediaImage2.getMediaStream())));
                        dataMapT2.put("end", end);
                        dataMapT2.put("end1", end);
                        if (routing.getOperationAttention() == null || routing.getOperationAttention().equals("")) {
                            dataMapT2.put("longtext", "");
                        } else {
                            dataMapT2.put("longtext", "<w:tr wsp:rsidR=\"008D52A5\" wsp:rsidRPr=\"00D85E4D\" wsp:rsidTr=\"00B305DD\"><w:trPr>" +
                                    "<w:trHeight w:val=\"693\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"9576\" w:type=\"dxa\"/><w:gridSpan w:val=\"5\"/>" +
                                    "<w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p wsp:rsidR=\"008D52A5\" wsp:rsidRPr=\"00D85E4D\" wsp:rsidRDefault=\"008D52A5\">" +
                                    "<w:r><w:rPr><w:rFonts w:hint=\"fareast\"/></w:rPr></w:r><w:r><w:t>"
                                    + routing.getOperationAttention().replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                                    .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                                    .replaceAll("'", "&apos;").replaceAll("\n", "</w:t></w:r></w:p><w:p><w:r><w:t>")
                                    + "</w:t></w:r><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/></w:rPr></w:r></w:p></w:tc></w:tr>");
                        }
                        listT.add(dataMapT2);
                    }
                }

                dataMap.put("gxlist", listT);
                return dataMap;
            } else {
                throw new PlatformException("该工单尚未绑定工艺Routing，请先对工单工艺路径进行维护！");
            }
        } else {
            NotificationUtils.notificationError("工单号不存在，请检查是否输入有误！");
            return null;
        }
    }

    public void creatGxReport() throws Exception {
        Map<String, Object> dataMap = getGXrecordDatas();
        dataMap.put("orderNo", (cbOrder.getValue()).getProductOrderId());
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        String jPath = "D:\\CameronQualityFiles\\DOCS";
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        Template template = configuration.getTemplate("GXrecord.xml", "utf-8");
        CaConfig caConfig = this.caConfigService.getConfigByType("CA_CONFIRM_REPORT_SAVE_PATH");
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        } else {
            String path = caConfig.getConfigValue() + "\\Production\\" + "GxRecord\\";
            String pathName = path + (cbOrder.getValue()).getProductOrderId() + ".doc";
            File outFile = new File(pathName);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")), 1048576);
            template.process(dataMap, out);
            out.flush();
            out.close();
            wordToPDF(path + (cbOrder.getValue()).getProductOrderId() + ".doc");
            System.out.println("*********成功生成工序确认报告*********");
            NotificationUtils.notificationInfo("成功生成工序确认报告！");
        }
    }

    private byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean var4 = true;

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        return outputStream.toByteArray();
    }

    public void wordToPDF(String filePath) {
        ComThread.InitSTA();
        ActiveXComponent app = null;
        Dispatch doc = null;
        //转换前的文件路径
        String startFile = filePath;
        ;
        //转换后的文件路劲
        String overFile = "";
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            overFile = startFile.replace(".doc", ".pdf");

            doc = Dispatch.call(docs, "Open", startFile).toDispatch();
            File tofile = new File(overFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", overFile, 17);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            if (app != null) {
                app.invoke("Quit", new Variant[]{});
            }
        }
        //结束后关闭进程
        System.out.println(LocalDateTime.now());
        ComThread.Release();
    }
}
