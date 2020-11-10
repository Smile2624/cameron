package com.ags.lumosframework.ui.view.assembling;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
//Changed by Cameron: 根据车间使用情况，去除语音输入，加入二维码扫码录入功能

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Menu(caption = "Assembling", captionI18NKey = "view.Assembling.caption", iconPath = "images/icon/text-blob.png", groupName = "Production", order = 0)
@SpringView(name = "Assembling", ui = {CameronUI.class})
@Secured("Assembling")
public class AssemblingView2 extends BaseView implements Button.ClickListener, IFilterableView {
    private static final long serialVersionUID = 4854162164548450226L;
    VerticalLayout vlRoot = new VerticalLayout();
    GridLayout glLayout = new GridLayout(3, 2);
    Panel panelDetail = new Panel();
    HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
    VerticalLayout vlDetailGrid = new VerticalLayout();
    GridLayout singletonGrid;
    GridLayout batchGrid;
    GridLayout detailLayoutGrid;
    GridLayout titleLayoutGrid;//用于显示左侧零件信息
    Boolean singleOrBatch = true;//用于标识当前录入按件还是批次：single->true;batch->false
    private ComboBox<ProductionOrder> cbOrder = new ComboBox<>();
    private ComboBox<String> cbType = new ComboBox<>();
    @I18Support(caption = "Material", captionKey = "AssemblingInfo.Material")
    private LabelWithSamleLineCaption tfProductNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "Rev", captionKey = "AssemblingInfo.Rev")
    private LabelWithSamleLineCaption tfProductRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "AssemblingInfo.Description")
    private LabelWithSamleLineCaption tfProductDec = new LabelWithSamleLineCaption();
    //    @I18Support(caption = "Order", captionKey = "AssemblingInfo.Order")
//    private LabelWithSamleLineCaption tfOrder = new LabelWithSamleLineCaption();
    private DateField dfDate = new DateField();
    @I18Support(caption = "Qty", captionKey = "AssemblingInfo.Qty")
    private LabelWithSamleLineCaption tfQty = new LabelWithSamleLineCaption();
    @I18Support(caption = "S/N Batch", captionKey = "AssemblingInfo.SnBatch")
    private LabelWithSamleLineCaption tfSnBatch = new LabelWithSamleLineCaption();
    @I18Support(caption = "S/N Batch", captionKey = "AssemblingInfo.SnBatch")
    private ComboBox<String> cbSnBatch = new ComboBox<>();
    private Button btnConfirm = new Button();
    @I18Support(caption = "SaveCommit", captionKey = "AssemblingInfo.SaveCommit")
    private Button btnSaveCommit = new Button();
    private TextField tfInput = new TextField();
    private Button btnScan = new Button();
    private Button[] btns = new Button[]{btnConfirm, btnScan, btnSaveCommit};//btnOpenVoice
    private Button btnPrevious = new Button();
    private Button btnNext = new Button();
    private Button btnClear = new Button();
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private int snCount;
    private ProductionOrder order;

    private List<Assembling> singletonDetail = new ArrayList<>();

    private List<Assembling> batchDetail = new ArrayList<>();

    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IAssemblingService assemblingService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private IBomService bomService;

    public AssemblingView2() {
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();

        hlTempToolBox.addComponent(cbOrder);
        cbOrder.setTextInputAllowed(true);
        cbOrder.setPlaceholder(I18NUtility.getValue("AssemblingInfo.OrderNo", "OrderNo"));
        hlTempToolBox.addComponent(cbType);
        cbType.setTextInputAllowed(false);
        cbType.setEmptySelectionAllowed(false);
        cbType.setPlaceholder(I18NUtility.getValue("AssemblingInfo.RecordType", "RecordType"));

//        cbType.setEnabled(false);//锁定只能使用分组
//        cbType.setValue("bType");

        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        hlTempToolBox.addComponent(tfInput);
        hlTempToolBox.addComponents(btnPrevious, btnNext, btnClear);
        tfInput.setVisible(false);
        btnPrevious.setVisible(false);
        btnNext.setVisible(false);
        btnClear.setVisible(false);
        btnConfirm.setIcon(VaadinIcons.CHECK);
        btnScan.setIcon(VaadinIcons.QRCODE);
        btnScan.setCaption("开始扫码");
        btnSaveCommit.setIcon(VaadinIcons.PACKAGE);
        btnScan.setEnabled(false);
        btnSaveCommit.setEnabled(false);
        btnPrevious.setIcon(VaadinIcons.ARROW_BACKWARD);
        btnNext.setIcon(VaadinIcons.ARROW_FORWARD);
        btnPrevious.setCaption("扫描上一件");
        btnNext.setCaption("扫描下一件");
        btnPrevious.addClickListener(this);
        btnNext.addClickListener(this);
        btnClear.setIcon(VaadinIcons.TRASH);
        btnClear.setCaption("清空当前输入产品");
        btnClear.addClickListener(this);
        hlToolBox.addComponent(hlTempToolBox);
        vlRoot.addComponent(hlToolBox);

        Panel panel = new Panel();
        panel.setWidth("100%");
        panel.setHeightUndefined();
        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(this.tfProductNo, 0, 0);
        glLayout.addComponent(this.tfProductRev, 1, 0);
        glLayout.addComponent(this.tfProductDec, 2, 0);
//        glLayout.addComponent(this.tfOrder, 0, 1);
        glLayout.addComponent(this.dfDate, 0, 1);
        dfDate.setDateFormat("yyyy.MM.dd");
        dfDate.setCaption("装配日期");
        dfDate.setPlaceholder("请选择装配日期");
        glLayout.addComponent(this.tfQty, 1, 1);

        panel.setContent(glLayout);
        vlRoot.addComponent(panel);
        panelDetail.setSizeFull();
        vlRoot.addComponent(panelDetail);
        vlRoot.setExpandRatio(panelDetail, 0.1f);

        this.setSizeFull();
        this.setCompositionRoot(vlRoot);

        KeyAction ka = new KeyAction(37, new int[]{});//left arrow键
        ka.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                tfInput.clear();
                if (snCount > 1) {
                    if (Strings.isNullOrEmpty(cbType.getValue())) {
                        throw new PlatformException("请先选择录入类型！");
                    } else if (cbType.getValue().equals("sType")) {
                        saveCommitStype(false);
                        snCount--;
                        focusOnSnSType();
                    } else if (cbType.getValue().equals("bType")) {
                        defocusOnSnBType(snCount);
                        focusOnSnBType(--snCount);
                        if (singletonDetail.size() > 0) {
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, 1)).focus();
                        } else if (batchDetail.size() > 0) {
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, singletonDetail.size() + 2)).focus();
                        }
                    }
                } else {
                    NotificationUtils.notificationInfo("已经到该工单第一个产品");
                }
            }
        });
        ka.extend(tfInput);
        KeyAction ka2 = new KeyAction(39, new int[]{});//right arrow键
        ka2.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                tfInput.clear();
                if (snCount < cbOrder.getValue().getProductNumber()) {
                    if (Strings.isNullOrEmpty(cbType.getValue())) {
                        throw new PlatformException("请先选择录入类型！");
                    } else if (cbType.getValue().equals("sType")) {
                        saveCommitStype(false);
                        snCount++;
                        focusOnSnSType();
                    } else if (cbType.getValue().equals("bType")) {
                        defocusOnSnBType(snCount);
                        focusOnSnBType(++snCount);
                        if (singletonDetail.size() > 0) {
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, 1)).focus();
                        } else if (batchDetail.size() > 0) {
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, singletonDetail.size() + 2)).focus();
                        }
                    }
                } else {
                    NotificationUtils.notificationInfo("已经录入到该工单最后一个产品");
                }
            }
        });
        ka2.extend(tfInput);
        KeyAction ka3 = new KeyAction(13, new int[]{});//回车键
        ka3.addKeypressListener(new KeyAction.KeyActionListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void keyPressed(KeyAction.KeyActionEvent keyPressEvent) {
                String input = tfInput.getValue();
                if (input.equals("[")) {
                    btnPrevious.click();
                } else if (input.equals("]")) {
                    btnNext.click();
                } else {
                    if (Strings.isNullOrEmpty(input)) {
                        return;
                    }
                    if (cbType.getValue().equals("bType")) {
                        inputValueBType(input);
                    } else if (cbType.getValue().equals("sType")) {
                        inputValueSType(input);
                    }
                    tfInput.clear();
                }
            }
        });
        ka3.extend(tfInput);
        tfInput.addBlurListener(event -> tfInput.selectAll());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnConfirm.equals(button)) {
            tfInput.setVisible(false);
            btnPrevious.setVisible(false);
            btnNext.setVisible(false);
            btnClear.setVisible(false);
            snCount = 1;
            if (cbOrder.getValue() == null) {
                NotificationUtils.notificationError("请选择订单号");
            } else {
                order = cbOrder.getValue();
                //判断工单是否锁定
                if(order.getBomLockFlag()){
                    throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                            "This Order is locked,Please contact the engineer to solve !"));
                }
                if (bomService.getBomsByNoRev(order.getProductId(), order.getProductVersionId()) == null
                        || bomService.getBomsByNoRev(order.getProductId(), order.getProductVersionId()).size() == 0) {
                    throw new PlatformException("该工单缺少BOM信息，请联系工程师维护！");
                }
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    glLayout.removeComponent(2, 1);
                    glLayout.addComponent(this.cbSnBatch, 2, 1);
                    freshGrid();
                } else if (cbType.getValue().equals("bType")) {
                    glLayout.removeComponent(2, 1);
                    glLayout.addComponent(this.tfSnBatch, 2, 1);
                    freshGridBtype();
                }
            }
        } else if (btnScan.equals(button)) {
            if (!tfInput.isVisible()) {
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    tfInput.setVisible(true);
                    btnPrevious.setVisible(true);
                    btnNext.setVisible(true);
                    btnClear.setVisible(true);
                    tfInput.selectAll();
                    focusOnSnSType();
                    btnScan.setCaption("停止扫码");
                    //单件录入
                } else if (cbType.getValue().equals("bType")) {
                    focusOnSnBType(snCount);
                    tfInput.setVisible(true);
                    btnPrevious.setVisible(true);
                    btnNext.setVisible(true);
                    btnClear.setVisible(true);
                    tfInput.selectAll();
                    btnScan.setCaption("停止扫码");
                    //分组录入
                }
            } else {
                tfInput.setVisible(false);
                btnPrevious.setVisible(false);
                btnNext.setVisible(false);
                btnClear.setVisible(false);
                if (cbType.getValue().equals("bType")) {
                    defocusOnSnBType(snCount);
                }
                btnScan.setCaption("开始扫码");
            }
        } else if (btnSaveCommit.equals(button)) {
            if (cbType.getValue().equals("sType")) {
                saveCommitStype(true);//单件
            } else if (cbType.getValue().equals("bType")) {//分组
                //判断是否录入完成
                if (checkFulfil()) {//录完
                    saveCommitBtype(true);
                } else {
                    saveCommitBtype(false);
                }
            }
        } else if (btnPrevious.equals(button)) {
            tfInput.clear();
            if (snCount > 1) {
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    saveCommitStype(false);
                    snCount--;
                    focusOnSnSType();
                } else if (cbType.getValue().equals("bType")) {
                    defocusOnSnBType(snCount);
                    focusOnSnBType(--snCount);
                    if (singletonDetail.size() > 0) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, 1)).focus();
                    } else if (batchDetail.size() > 0) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, singletonDetail.size() + 2)).focus();
                    }
                }
            } else {
                NotificationUtils.notificationInfo("已经到该工单第一个产品");
            }
        } else if (btnNext.equals(button)) {
            tfInput.clear();
            if (snCount < cbOrder.getValue().getProductNumber()) {
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    saveCommitStype(false);
                    snCount++;
                    focusOnSnSType();
                } else if (cbType.getValue().equals("bType")) {
                    defocusOnSnBType(snCount);
                    focusOnSnBType(++snCount);
                    if (singletonDetail.size() > 0) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, 1)).focus();
                    } else if (batchDetail.size() > 0) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, singletonDetail.size() + 2)).focus();
                    }
                }
            } else {
                NotificationUtils.notificationInfo("已经录入到该工单最后一个产品");
            }
        } else if (btnClear.equals(button)) {
            ConfirmDialog.show(getUI(), "确定要清空当前输入产品吗？", result -> {
                if (Strings.isNullOrEmpty(cbType.getValue())) {
                    throw new PlatformException("请先选择录入类型！");
                } else if (cbType.getValue().equals("sType")) {
                    for (int i = 1; i <= singletonDetail.size(); i++) {
                        ((TextField) singletonGrid.getComponent(5, i)).clear();
                        ((TextField) singletonGrid.getComponent(6, i)).clear();
                    }
                    for (int i = 1; i <= batchDetail.size(); i++) {
                        ((TextField) batchGrid.getComponent(5, i)).clear();
                        ((TextField) batchGrid.getComponent(6, i)).clear();
                    }
                } else if (cbType.getValue().equals("bType")) {
                    for (int i = 1; i <= singletonDetail.size(); i++) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).clear();
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).clear();
                    }
                    for (int i = 1; i <= batchDetail.size(); i++) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, singletonDetail.size() + i + 1)).clear();
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, singletonDetail.size() + i + 1)).clear();
                    }
                }
            });
        }
    }

    /**
     * 分组方式检测最后一个单元格信息是否录入完成
     *
     * @return
     */
    public Boolean checkFulfil() {
        TextField lastTa;
        if (batchDetail.size() == 0) {
            lastTa = (TextField) detailLayoutGrid.getComponent(detailLayoutGrid.getColumns() - 1, detailLayoutGrid.getRows() - 2);
        } else {
            lastTa = (TextField) detailLayoutGrid.getComponent(detailLayoutGrid.getColumns() - 1, detailLayoutGrid.getRows() - 1);
        }
        if (Strings.isNullOrEmpty(lastTa.getValue())) {//未录完
            return false;
        } else {
            return true;
        }
    }

    /**
     * 单件方式录入后提交保存操作
     */
    public void saveCommitStype(Boolean flag) {
        //保存装配记录
        saveAssembReocrd();
        if (flag) {
            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (caConfig == null) {
                throw new PlatformException("请先配置文档报告存放路径");
            }
            String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT;

            //生成报告
            reportDatesStype(path);
        }
    }

    /**
     * 分组方式录入后提交保存操作
     */
    public void saveCommitBtype(Boolean flag) {
        //TODO 保存装配记录
        saveAssembReocrdBtype();
        if (flag) {//完成
            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
            if (caConfig == null) {
                throw new PlatformException("请先配置文档报告存放路径");
            }
            String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.ASSEMBLY_REPORT;
            //TODO 生成报告
            reportDatesBtype(path);
            ConfirmDialog.show(getUI(),
                    "已完成该订单装配！请前往“工序记录”页面进行装配工序的完成确认！", result -> {
                        if (ConfirmResult.Result.OK.equals(result.getResult())) {

                        }
                    });
        } else {//暂存，不生成报告
            NotificationUtils.notificationInfo("录入信息已暂存");
        }

    }

    /**
     * 单件保存装配记录
     */
    public void saveAssembReocrd() {
        //****单件
        for (int s = 0; s < singletonDetail.size(); s++) {

            TextField tfPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            String tempValue0 = tfPartNo.getValue();

            Assembling singleAssemb = assemblingService.getBySnAndPartNo(cbSnBatch.getValue(), tempValue0, Integer.toString(s));
            if (singleAssemb == null) {
                singleAssemb = new Assembling();
            }
            singleAssemb.setOrderNo(order.getProductOrderId());
            singleAssemb.setRetrospectType("singleton");
            singleAssemb.setSnBatch(cbSnBatch.getValue());

            singleAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) singletonGrid.getComponent(1, s + 1);
            String tempValue1 = tfPartDesc.getValue();
            singleAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

            TextField tfPlmRev = (TextField) singletonGrid.getComponent(2, s + 1);
            String tempValue2 = tfPlmRev.getValue();
            singleAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) singletonGrid.getComponent(3, s + 1);
            String tempValue3 = tfPartRev.getValue();
            singleAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

            TextField tfSerialNo = (TextField) singletonGrid.getComponent(5, s + 1);
            String tempValue4 = tfSerialNo.getValue();
            singleAssemb.setSerialNo(tempValue4 == null ? "" : tempValue4);

            TextField tfHeatNoLot = (TextField) singletonGrid.getComponent(6, s + 1);
            String tempValue5 = tfHeatNoLot.getValue();
            singleAssemb.setHeatNoLot(tempValue5 == null ? "" : tempValue5);

            singleAssemb.setHardness("/");
            singleAssemb.setMatType("/");
            singleAssemb.setQcCheck("/");
            singleAssemb.setDescription(Integer.toString(s));

            assemblingService.save(singleAssemb);
        }
        //*******批次
        for (int b = 0; b < batchDetail.size(); b++) {
            TextField tfPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            String tempValue0 = tfPartNo.getValue();

            Assembling bAssemb = assemblingService.getBySnAndPartNo(cbSnBatch.getValue(), tempValue0, Integer.toString(b));
            if (bAssemb == null) {
                bAssemb = new Assembling();
            }
            bAssemb.setOrderNo(order.getProductOrderId());
            bAssemb.setRetrospectType("batch");
            bAssemb.setSnBatch(cbSnBatch.getValue());

            bAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) batchGrid.getComponent(1, b + 1);
            String tempValue1 = tfPartDesc.getValue();
            bAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

            TextField tfPlmRev = (TextField) batchGrid.getComponent(2, b + 1);
            String tempValue2 = tfPlmRev.getValue();
            bAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) batchGrid.getComponent(3, b + 1);
            String tempValue3 = tfPartRev.getValue();
            bAssemb.setPartRev(tempValue3 == null ? "" : tempValue3);

            TextField tfBatchQty = (TextField) batchGrid.getComponent(4, b + 1);
            String tempValue4 = tfBatchQty.getValue();
            bAssemb.setBatchQty(new Integer(tempValue4));

            TextField tfBatch = (TextField) batchGrid.getComponent(5, b + 1);
            String tempValue5 = tfBatch.getValue();
            bAssemb.setBatch(tempValue5 == null ? "" : tempValue5);

            TextField tfHeatNoLot = (TextField) batchGrid.getComponent(6, b + 1);
            String tempValue6 = tfHeatNoLot.getValue();
            bAssemb.setHeatNoLot(tempValue6 == null ? "" : tempValue6);
            bAssemb.setHardness("/");
            bAssemb.setCOrD("/");
            bAssemb.setEOrD("/");
            bAssemb.setQcCheck("/");
            bAssemb.setDescription(Integer.toString(b));

            assemblingService.save(bAssemb);
        }

    }

    /**
     * 单件方式装配报告数据
     *
     * @param path
     */
    public void reportDatesStype(String path) {
        List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
        //****单件
        for (int s = 0; s < singletonDetail.size(); s++) {
            Map<String, String> sMap = new HashMap<>();
            TextField tfPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            String tempValue0 = tfPartNo.getValue();
            sMap.put("sPartNo", tempValue0 == null ? "" : tempValue0);
            TextArea tfPartDesc = (TextArea) singletonGrid.getComponent(1, s + 1);
            String tempValue1 = tfPartDesc.getValue();
            sMap.put("sPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

            TextField tfPlmRev = (TextField) singletonGrid.getComponent(2, s + 1);
            String tempValue2 = tfPlmRev.getValue();
            sMap.put("sPlmRev", tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) singletonGrid.getComponent(3, s + 1);
            String tempValue3 = tfPartRev.getValue();
            sMap.put("sPartRev", tempValue3 == null ? "" : tempValue3);

            TextField tfSerialNo = (TextField) singletonGrid.getComponent(5, s + 1);
            String tempValue4 = tfSerialNo.getValue();
            sMap.put("sSN", tempValue4 == null ? "" : tempValue4);

            TextField tfHeatNoLot = (TextField) singletonGrid.getComponent(6, s + 1);
            String tempValue5 = tfHeatNoLot.getValue();
            sMap.put("sHeat", tempValue5 == null ? "" : tempValue5);

            sMap.put("sHardness", "/");
            sMap.put("sMat", "/");
            sMap.put("sQC", "/");
            sList.add(sMap);
        }
        //****批次
        List<Map<String, String>> bList = new ArrayList<Map<String, String>>();
        for (int b = 0; b < batchDetail.size(); b++) {
            Map<String, String> bMap = new HashMap<>();

            TextField tfPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            String tempValue0 = tfPartNo.getValue();
            bMap.put("bPartNo", tempValue0 == null ? "" : tempValue0);

            TextArea tfPartDesc = (TextArea) batchGrid.getComponent(1, b + 1);
            String tempValue1 = tfPartDesc.getValue();
            bMap.put("bPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

            TextField tfPlmRev = (TextField) batchGrid.getComponent(2, b + 1);
            String tempValue2 = tfPlmRev.getValue();
            bMap.put("bPlmRev", tempValue2 == null ? "" : tempValue2);

            TextField tfPartRev = (TextField) batchGrid.getComponent(3, b + 1);
            String tempValue3 = tfPartRev.getValue();
            bMap.put("bPartRev", tempValue3 == null ? "" : tempValue3);

            TextField tfBatchQty = (TextField) batchGrid.getComponent(4, b + 1);
            String tempValue4 = tfBatchQty.getValue();
            int bQty = new Integer(tempValue4);
            bMap.put("bQty", bQty < 0 ? "0" : bQty + "");

            TextField tfBatch = (TextField) batchGrid.getComponent(5, b + 1);
            String tempValue5 = tfBatch.getValue();
            bMap.put("bBatch", tempValue5 == null ? "" : tempValue5);

            TextField tfHeatNoLot = (TextField) batchGrid.getComponent(6, b + 1);
            String tempValue6 = tfHeatNoLot.getValue();
            bMap.put("bHeat", tempValue6 == null ? "" : tempValue6);
            bMap.put("bHardness", "/");
            bMap.put("bCD", "/");
            bMap.put("bED", "/");
            bMap.put("bQC", "/");
            bList.add(bMap);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tSnBatch", cbSnBatch.getValue());
        dataMap.put("tQty", tfQty.getValue());
        dataMap.put("slist", sList);
        dataMap.put("blist", bList);
        String pathFileName = path + cbSnBatch.getValue() + ".doc";
        try {
            //生成报告
            createReport(pathFileName, dataMap);
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }

    /**
     * 分组方式保存装配记录
     */
    public void saveAssembReocrdBtype() {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();

        for (int i = 0; i < order.getProductNumber(); i++) {
            String snBatch = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            //****单件
            for (int s = 0; s < sSize; s++) {
                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, s + 1);
                String partNo = tfPartNo.getValue();

                Assembling singleAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(s));
                if (singleAssemb == null) {
                    singleAssemb = new Assembling();
                }
                singleAssemb.setOrderNo(order.getProductOrderId());
                singleAssemb.setRetrospectType("singleton");
                singleAssemb.setSnBatch(snBatch);

                String tempValue0 = tfPartNo.getValue();
                singleAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

                TextArea tfPartDesc = (TextArea) titleLayoutGrid.getComponent(1, s + 1);
                String tempValue1 = tfPartDesc.getValue();
                singleAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3 * i, s + 1);
                String tempValue2 = tfPartRev.getValue();
                singleAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);

                singleAssemb.setPartRev(tempValue2 == null ? "" : tempValue2);

                TextField tfSerialNo = (TextField) detailLayoutGrid.getComponent(3 * i + 1, s + 1);
                String tempValue4 = tfSerialNo.getValue();
                singleAssemb.setSerialNo(tempValue4 == null ? "" : tempValue4);

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(3 * i + 2, s + 1);
                String tempValue5 = tfHeatNoLot.getValue();
                singleAssemb.setHeatNoLot(tempValue5 == null ? "" : tempValue5);

                singleAssemb.setHardness("/");
                singleAssemb.setMatType("/");
                singleAssemb.setQcCheck("/");
                singleAssemb.setDescription(Integer.toString(s));

                assemblingService.save(singleAssemb);
            }
            //*******批次
            for (int b = 0; b < bSize; b++) {

                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, b + sSize + 2);
                String partNo = tfPartNo.getValue();

                Assembling bAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(b));
                if (bAssemb == null) {
                    bAssemb = new Assembling();
                }
                bAssemb.setOrderNo(order.getProductOrderId());
                bAssemb.setRetrospectType("batch");
                bAssemb.setSnBatch(snBatch);

                String tempValue0 = tfPartNo.getValue();
                bAssemb.setPartNo(tempValue0 == null ? "" : tempValue0);

                TextArea tfPartDesc = (TextArea) titleLayoutGrid.getComponent(1, b + sSize + 2);
                String tempValue1 = tfPartDesc.getValue();
                bAssemb.setPartDesc(tempValue1 == null ? "" : tempValue1);

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(i * 3, b + sSize + 2);
                String tempValue2 = tfPartRev.getValue();
                bAssemb.setPlmRev(tempValue2 == null ? "" : tempValue2);
                bAssemb.setPartRev(tempValue2 == null ? "" : tempValue2);

                TextField tfBatchQty = (TextField) titleLayoutGrid.getComponent(2, b + sSize + 2);
                String tempValue4 = tfBatchQty.getValue();
                bAssemb.setBatchQty(new Integer(tempValue4));

                TextField tfBatch = (TextField) detailLayoutGrid.getComponent(i * 3 + 1, b + sSize + 2);
                String tempValue5 = tfBatch.getValue();
                bAssemb.setBatch(tempValue5 == null ? "" : tempValue5);

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(i * 3 + 2, b + sSize + 2);
                String tempValue6 = tfHeatNoLot.getValue();
                bAssemb.setHeatNoLot(tempValue6 == null ? "" : tempValue6);

                bAssemb.setHardness("/");
                bAssemb.setCOrD("/");
                bAssemb.setEOrD("/");
                bAssemb.setQcCheck("/");
                bAssemb.setDescription(Integer.toString(b));
                assemblingService.save(bAssemb);
            }
        }
    }

    /**
     * 分组方式装配报告数据
     *
     * @param path
     */
    public void reportDatesBtype(String path) {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();
        for (int i = 0; i < order.getProductNumber(); i++) {
            //****单件
            List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
            for (int s = 0; s < sSize; s++) {
                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, s + 1);
                String tempValue0 = tfPartNo.getValue();

                TextArea tfPartDesc = (TextArea) titleLayoutGrid.getComponent(1, s + 1);
                String tempValue1 = tfPartDesc.getValue();

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(i * 3, s + 1);
                String tempValue2 = tfPlmRev.getValue();

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(i * 3, s + 1);
                String tempValue3 = tfPartRev.getValue();

                TextField tfSerialNo = (TextField) detailLayoutGrid.getComponent(i * 3 + 1, s + 1);
                String tempValue4 = tfSerialNo.getValue();

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(i * 3 + 2, s + 1);
                String tempValue5 = tfHeatNoLot.getValue();

                Map<String, String> sMap = new HashMap<>();
                sMap.put("sPartNo", tempValue0 == null ? "" : tempValue0);
                sMap.put("sPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                sMap.put("sPlmRev", tempValue2 == null ? "" : tempValue2);
                sMap.put("sPartRev", tempValue3 == null ? "" : tempValue3);
                sMap.put("sSN", tempValue4 == null ? "" : tempValue4);
                sMap.put("sHeat", tempValue5 == null ? "" : tempValue5);
                sMap.put("sHardness", "/");
                sMap.put("sMat", "/");
                sMap.put("sQC", "/");
                sList.add(sMap);
            }
            //批次
            List<Map<String, String>> bList = new ArrayList<Map<String, String>>();
            for (int b = 0; b < bSize; b++) {
                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, b + sSize + 2);
                String tempValue0 = tfPartNo.getValue();

                TextArea tfPartDesc = (TextArea) titleLayoutGrid.getComponent(1, b + sSize + 2);
                String tempValue1 = tfPartDesc.getValue();

                TextField tfPlmRev = (TextField) detailLayoutGrid.getComponent(i * 3, b + sSize + 2);
                String tempValue2 = tfPlmRev.getValue();

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(i * 3, b + sSize + 2);
                String tempValue3 = tfPartRev.getValue();

                TextField tfBatchQty = (TextField) titleLayoutGrid.getComponent(2, b + sSize + 2);
                String tempValue4 = tfBatchQty.getValue();

                TextField tfBatch = (TextField) detailLayoutGrid.getComponent(i * 3 + 1, b + sSize + 2);
                String tempValue5 = tfBatch.getValue();

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(i * 3 + 2, b + sSize + 2);
                String tempValue6 = tfHeatNoLot.getValue();

                Map<String, String> bMap = new HashMap<>();
                bMap.put("bPartNo", tempValue0 == null ? "" : tempValue0);
                bMap.put("bPartDesc", tempValue1 == null ? "" : tempValue1.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                bMap.put("bPlmRev", tempValue2 == null ? "" : tempValue2);
                bMap.put("bPartRev", tempValue3 == null ? "" : tempValue3);
                bMap.put("bQty", new Integer(tempValue4) < 1 ? "1" : new Integer(tempValue4) + "");
                bMap.put("bBatch", tempValue5 == null ? "" : tempValue5);
                bMap.put("bHeat", tempValue6 == null ? "" : tempValue6);
                bMap.put("bHardness", "/");
                bMap.put("bCD", "/");
                bMap.put("bED", "/");
                bMap.put("bQC", "/");
                bList.add(bMap);
            }
            Map<String, Object> dataMap = new HashMap<>();
            String strSnBatch = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            dataMap.put("tSnBatch", strSnBatch);
            dataMap.put("tQty", i + 1 + " of " + order.getProductNumber());
            dataMap.put("slist", sList);
            dataMap.put("blist", bList);
            String pathFileName = path + strSnBatch + ".doc";
            try {
                //生成报告
                createReport(pathFileName, dataMap);
            } catch (Exception e) {
                throw new PlatformException(e.getMessage());
            }
        }

    }

    /**
     * 生成报告
     */
    public void createReport(String pathFileName, Map<String, Object> dataMap) throws Exception {

        // 准备填充公共数据
        dataMap.put("tMaterial", tfProductNo.getValue());
        dataMap.put("tRev", tfProductRev.getValue());
        dataMap.put("tDesc", tfProductDec.getValue().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        dataMap.put("tOrder", cbOrder.getValue().getProductOrderId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        dataMap.put("RDate", formatter.format(dfDate.getValue()));
        dataMap.put("tDate", formatter.format(dfDate.getValue()));

        // 将Media的InputStream转换为byte[] 然后进行BASE64编码
        BASE64Encoder encoder = new BASE64Encoder();

        Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
        if (mediaImage == null) {
            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
        }
        dataMap.put("RecordByImage", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));

        // Configuration用于读取模板文件(XML、FTL文件或迅是我们这里用的StringTemplateLoader)
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        // 指定模板文件所在目录的路径,而不是文件的路径
        String jPath = AppConstant.DOC_XML_FILE_PATH;
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        // 以utf-8的编码读取模板文件
        Template template = configuration.getTemplate("assembling.xml", "utf-8");

        //输出文件
        File outFile = new File(pathFileName);

        //将模板和数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                1024 * 1024);
        template.process(dataMap, out);
        out.flush();
        out.close();
        wordToPDF(pathFileName);
        System.out.println("*********成功生成装配报告-" + pathFileName + "***********");
        NotificationUtils.notificationInfo("成功生成装配报告-" + pathFileName);
    }

    private byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        return outputStream.toByteArray();
    }

    /**
     * 单件type标题数据
     */
    public void freshLab() {
//        tfOrder.setValue(order.getProductOrderId());
        dfDate.setValue(LocalDate.now());
        List<String> strList = new ArrayList<>();
        for (int i = 1; i <= order.getProductNumber(); i++) {
            strList.add(order.getProductOrderId() + String.format("%4d", i).replace(" ", "0"));
        }
        cbSnBatch.clear();
        cbSnBatch.setItems(strList);
        cbSnBatch.setEmptySelectionAllowed(false);
        cbSnBatch.setTextInputAllowed(false);
        cbSnBatch.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 6L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                if (Strings.isNullOrEmpty(event.getValue())) {
                    tfQty.setValue("");
                    btnSaveCommit.setEnabled(false);
                } else {
                    String strSeq = event.getValue().substring(event.getValue().length() - 2);
                    tfQty.setValue(Integer.parseInt(strSeq) + " of " + order.getProductNumber());

                    //动态生成gridlayout
                    autoSetDetailGrid();
                    //set已存值
                    setStypeValue(event.getValue());
                    btnSaveCommit.setEnabled(true);
                }

            }
        });
        tfProductNo.setValue(order.getProductId());
        tfProductRev.setValue(order.getProductVersionId());
        tfProductDec.setValue(order.getProductInformation().getProductDesc() == null ? "" : order.getProductInformation().getProductDesc());
    }

    public void setStypeValue(String snBatch) {
        //单件明细
        for (int s = 0; s < singletonDetail.size(); s++) {
            TextField txPartNo = (TextField) singletonGrid.getComponent(0, s + 1);
            Assembling assembling = assemblingService.getBySnAndPartNo(snBatch, txPartNo.getValue().trim(), Integer.toString(s));
            TextField taSN = (TextField) singletonGrid.getComponent(5, s + 1);
            TextField taHeat = (TextField) singletonGrid.getComponent(6, s + 1);
            if (assembling != null) {
                taSN.setValue(assembling.getSerialNo() == null ? "" : assembling.getSerialNo());
                taHeat.setValue(assembling.getHeatNoLot() == null ? "" : assembling.getHeatNoLot());
            } else {
                taSN.clear();
                taHeat.clear();
            }
        }
        //批次明细
        for (int b = 0; b < batchDetail.size(); b++) {
            TextField txPartNo = (TextField) batchGrid.getComponent(0, b + 1);
            Assembling assembling = assemblingService.getBySnAndPartNo(snBatch, txPartNo.getValue().trim(), Integer.toString(b));
            TextField taSN = (TextField) batchGrid.getComponent(5, b + 1);
            TextField taHeat = (TextField) batchGrid.getComponent(6, b + 1);
            if (assembling != null) {
                taSN.setValue(assembling.getBatch() == null ? "" : assembling.getBatch());
                taHeat.setValue(assembling.getHeatNoLot() == null ? "" : assembling.getHeatNoLot());
            } else {
                taSN.clear();
                taHeat.clear();
            }
        }

    }

    /**
     * 分组type标题数据
     */
    public void freshLabBtype() {
//        tfOrder.setValue(order.getProductOrderId());
        dfDate.setValue(LocalDate.now());
        tfQty.setValue(order.getProductNumber() + "");
        tfSnBatch.setValue(order.getProductOrderId() + String.format("%4d", 1).replace(" ", "0") +
                " -- " + order.getProductOrderId() + String.format("%4d", order.getProductNumber()).replace(" ", "0"));
        tfProductNo.setValue(order.getProductId());
        tfProductRev.setValue(order.getProductVersionId());
        tfProductDec.setValue(order.getProductInformation().getProductDesc() == null ? "" : order.getProductInformation().getProductDesc());
    }

    /**
     * 单件type追溯零件
     */
    public void freshGrid() {
        singletonDetail = new ArrayList<>();
        batchDetail = new ArrayList<>();


        List<Bom> singletonBomList = order.getSingletonBoms();
        for (Bom bom : singletonBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling single = bomToAssemb(bom, "singleton");
            for (int i = 0; i < single.getBatchQty(); i++) {
                singletonDetail.add(single);
            }
        }

        List<Bom> batchBomList = order.geBatchBoms();
        for (Bom bom : batchBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling batch = bomToAssemb(bom, "batch");
            batchDetail.add(batch);
        }
        //加载页面显示数据
        freshLab();
        autoSetDetailGrid();
        btnScan.setEnabled(true);
    }

    public void autoSetDetailGrid() {
        vlDetailGrid.setMargin(true);
//        vlDetailGrid.setSizeFull();
        vlDetailGrid.setSpacing(true);
        panelDetail.setContent(vlDetailGrid);

        //***********单件
        singletonGrid = new GridLayout(7, singletonDetail.size() + 1);
        singletonGrid.setSpacing(true);
        singletonGrid.setWidth("100%");
        singletonGrid.setMargin(true);
        singletonGrid.setHeightUndefined();
        if (singletonDetail.size() > 0) {
            //第一行
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）")), 0, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No")), 5, 0);
            singletonGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot")), 6, 0);

            //明细
            for (int s = 0; s < singletonDetail.size(); s++) {
                Assembling singleAssembling = singletonDetail.get(s);
                singletonGrid.addComponent(getTextField(singleAssembling.getPartNo(), false, "125px"), 0, s + 1);
                singletonGrid.addComponent(getTextArea(singleAssembling.getPartDesc(), false, "200px"), 1, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getPlmRev(), true, "50px"), 2, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getPartRev(), true, "50px"), 3, s + 1);
                TextField tfQty = new TextField();
                tfQty.setWidth("1px");
                Integer value = singleAssembling.getBatchQty();
                tfQty.setValue(value == null ? "1" : value.toString());
                tfQty.setVisible(false);
                singletonGrid.addComponent(tfQty, 4, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getSerialNo(), true, "200px"), 5, s + 1);
                singletonGrid.addComponent(getTextField(singleAssembling.getHeatNoLot(), true, "120px"), 6, s + 1);
            }
        }

        //**********批次
        batchGrid = new GridLayout(7, batchDetail.size() + 1);
        batchGrid.setSpacing(true);
        batchGrid.setMargin(true);
        batchGrid.setWidth("100%");
        batchGrid.setHeightUndefined();
        if (batchDetail.size() > 0) {
            //第一行
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）")), 0, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PLMRev", "PLM Rev")), 2, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Part Rev")), 3, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Qty", "Qty")), 4, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Batch")), 5, 0);
            batchGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot")), 6, 0);
            //明细
            for (int b = 0; b < batchDetail.size(); b++) {
                Assembling bAssembling = batchDetail.get(b);
                batchGrid.addComponent(getTextField(bAssembling.getPartNo(), false, "125px"), 0, b + 1);
                batchGrid.addComponent(getTextArea(bAssembling.getPartDesc(), false, "200px"), 1, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getPlmRev(), true, "50px"), 2, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getPartRev(), true, "50px"), 3, b + 1);
                TextField tfQty = new TextField();
                tfQty.setWidth("30px");
                Integer value = bAssembling.getBatchQty();
                tfQty.setValue(value == null ? "1" : value.toString());
                tfQty.addValueChangeListener(event -> {
                    String newValue = event.getValue().trim();
                    if (!Strings.isNullOrEmpty(newValue)) {
                        boolean isNumber = RegExpValidatorUtils.isIsPositive(newValue);
                        if (!isNumber) {
                            NotificationUtils.notificationError(I18NUtility.getValue("Common.OnlyNumberAllowed", "please input greater than zero number!"));
                        }
                    }

                });
                batchGrid.addComponent(tfQty, 4, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getBatch(), true, "200px"), 5, b + 1);
                batchGrid.addComponent(getTextField(bAssembling.getHeatNoLot(), true, "120px"), 6, b + 1);
            }
        }
        vlDetailGrid.removeAllComponents();
        vlDetailGrid.addComponents(singletonGrid, batchGrid);

        if (singletonDetail.size() == 0) {
            singleOrBatch = false;
        } else {
            singleOrBatch = true;
        }
    }

    /**
     * 分组type追溯零件
     */
    public void freshGridBtype() {
        singletonDetail = new ArrayList<>();
        batchDetail = new ArrayList<>();
        List<Bom> singletonBomList = order.getSingletonBoms();
        for (Bom bom : singletonBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling single = bomToAssembBtype(bom, "singleton");
            for (int i = 0; i < single.getBatchQty(); i++) {
                singletonDetail.add(single);
            }
        }
        List<Bom> batchBomList = order.geBatchBoms();
        for (Bom bom : batchBomList) {
            if (bom.getSparePart() == null) {
                continue;
            }
            Assembling batch = bomToAssembBtype(bom, "batch");
            batchDetail.add(batch);
        }
        //动态生成gridlayout
        autoSetDetailGridBtype();
        freshLabBtype();
        btnScan.setEnabled(true);
        btnSaveCommit.setEnabled(true);
        loadSavedDataBType();
    }

    public void autoSetDetailGridBtype() {
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();
        int columns = order.getProductNumber() * 3;
        int rows = sSize + bSize + 2;
        titleLayoutGrid = new GridLayout(3, rows);
        titleLayoutGrid.setSpacing(true);
        titleLayoutGrid.setMargin(true);
        detailLayoutGrid = new GridLayout(columns, rows);
        detailLayoutGrid.setSpacing(true);
        detailLayoutGrid.setMargin(true);
        hlSplitPanel.setFirstComponent(titleLayoutGrid);
        hlSplitPanel.setSecondComponent(detailLayoutGrid);
        hlSplitPanel.setSizeFull();
        hlSplitPanel.setSplitPosition(400.0F, Unit.PIXELS);
        panelDetail.setContent(hlSplitPanel);

        //singleton第一行
        if (sSize > 0) {
            titleLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberSingleton", "Part Number（Singleton）")), 0, 0);
            titleLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, 0);
            for (int n = 0, i = 1; n < columns; n += 3, i++) {
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Rev") + "(" + i + ")"), n, 0);
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Serial No") + "(" + i + ")"), n + 1, 0);
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot") + "(" + i + ")"), n + 2, 0);
            }
        }
        //明细
        for (int s = 0; s < sSize; s++) {
            Assembling singleAssembling = singletonDetail.get(s);
            titleLayoutGrid.addComponent(getTextField(singleAssembling.getPartNo(), false, "125px"), 0, s + 1);
            titleLayoutGrid.addComponent(getTextArea(singleAssembling.getPartDesc(), false, "200px"), 1, s + 1);
            TextField tfQty = new TextField();
            tfQty.setWidth("1px");
            Integer value = singleAssembling.getBatchQty();
            tfQty.setValue(value == null ? "1" : value.toString());
            tfQty.setVisible(false);
            titleLayoutGrid.addComponent(tfQty, 2, s + 1);
            for (int n = 0; n < columns; n += 3) {
                detailLayoutGrid.addComponent(getTextField(singleAssembling.getPartRev(), true, "50px"), n, s + 1);
                detailLayoutGrid.addComponent(getTextField(singleAssembling.getSerialNo(), true, "200px"), n + 1, s + 1);
                detailLayoutGrid.addComponent(getTextField(singleAssembling.getHeatNoLot(), true, "120px"), n + 2, s + 1);
            }
        }

        //batch第一行
        if (bSize > 0) {
            titleLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartNumberBatch", "Part Number（Batch）")), 0, sSize + 1);
            titleLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Description", "Description")), 1, sSize + 1);
            titleLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.Qty", "Qty")), 2, sSize + 1);
            for (int n = 0, i = 1; n < columns; n += 3, i++) {
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.PartRev", "Rev") + "(" + i + ")"), n, sSize + 1);
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.SerialNo", "Batch") + "(" + i + ")"), n + 1, sSize + 1);
                detailLayoutGrid.addComponent(new Label(I18NUtility.getValue("AssemblingInfo.HeatNoLot", "Heat No. Heat Lot") + "(" + i + ")"), n + 2, sSize + 1);
            }
        }
        //明细
        for (int b = 0; b < batchDetail.size(); b++) {
            Assembling bAssembling = batchDetail.get(b);
            titleLayoutGrid.addComponent(getTextField(bAssembling.getPartNo(), false, "125px"), 0, sSize + 1 + b + 1);
            titleLayoutGrid.addComponent(getTextArea(bAssembling.getPartDesc(), false, "200px"), 1, sSize + 1 + b + 1);
            TextField tfQty = new TextField();
            tfQty.setWidth("30px");
            Integer value = bAssembling.getBatchQty();
            tfQty.setValue(value == null ? "1" : value.toString());
            tfQty.addValueChangeListener(event -> {
                String newValue = event.getValue().trim();
                if (!Strings.isNullOrEmpty(newValue)) {
                    boolean isNumber = RegExpValidatorUtils.isIsPositive(newValue);
                    if (!isNumber) {
                        NotificationUtils.notificationError(I18NUtility.getValue("Common.OnlyNumberAllowed", "please input greater than zero number!"));
                    }
                }

            });
            titleLayoutGrid.addComponent(tfQty, 2, sSize + 1 + b + 1);
            for (int n = 0; n < columns; n += 3) {
                detailLayoutGrid.addComponent(getTextField(bAssembling.getPartRev(), true, "50px"), n, sSize + 1 + b + 1);
                detailLayoutGrid.addComponent(getTextField(bAssembling.getBatch(), true, "200px"), n + 1, sSize + 1 + b + 1);
                detailLayoutGrid.addComponent(getTextField(bAssembling.getHeatNoLot(), true, "120px"), n + 2, sSize + 1 + b + 1);
            }
        }
        hlSplitPanel.setHeight(Math.max(titleLayoutGrid.getHeight(), detailLayoutGrid.getHeight()), detailLayoutGrid.getHeightUnits());
        System.out.println(detailLayoutGrid.getColumns() + "------------" + detailLayoutGrid.getRows());
    }

    public Assembling bomToAssemb(Bom bom, String type) {
        Assembling assemb = new Assembling();
        assemb.setOrderNo(order.getProductOrderId());
        assemb.setRetrospectType(type);
        assemb.setPartNo(bom.getSparePart().getSparePartNo() == null ? "" : bom.getSparePart().getSparePartNo());
        assemb.setPartDesc(bom.getSparePart().getSparePartDec() == null ? "" : bom.getSparePart().getSparePartDec());
        assemb.setPlmRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setPartRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setHardness(bom.getSparePart().getHardnessFile() == null ? "" : bom.getSparePart().getHardnessFile());
        assemb.setBatchQty(bom.getPartQuantity() < 1 ? 1 : bom.getPartQuantity());
        return assemb;
    }

    public Assembling bomToAssembBtype(Bom bom, String type) {
        Assembling assemb = new Assembling();
        assemb.setOrderNo(order.getProductOrderId());
        assemb.setRetrospectType(type);
        assemb.setPartNo(bom.getSparePart().getSparePartNo() == null ? "" : bom.getSparePart().getSparePartNo());
        assemb.setPartDesc(bom.getSparePart().getSparePartDec() == null ? "" : bom.getSparePart().getSparePartDec());
        assemb.setPlmRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setPartRev(bom.getSparePart().getSparePartRev() == null ? "" : bom.getSparePart().getSparePartRev());
        assemb.setHardness(bom.getSparePart().getHardnessFile() == null ? "" : bom.getSparePart().getHardnessFile());
        assemb.setBatchQty(bom.getPartQuantity() < 1 ? 1 : bom.getPartQuantity());
        return assemb;
    }

    public TextField getTextField(String tempValue, Boolean isAble, String strWidth) {
        TextField tf = new TextField();
        tf.setWidth(strWidth);
        tf.setHeight("50px");
        if (!isAble) {
            tf.setReadOnly(true);
            tf.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        }

        tf.setValue(tempValue == null ? "" : tempValue);
        return tf;
    }

    public TextArea getTextArea(String tempValue, Boolean isAble, String strWidth) {
        TextArea ta = new TextArea();
        ta.setWidth(strWidth);
        ta.setHeight("50px");
        if (!isAble) {
            ta.setReadOnly(true);
            ta.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        }

        ta.setValue(tempValue == null ? "" : tempValue);
        return ta;
    }

    @Override
    protected void init() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        List<ProductionOrder> orderList = productionOrderService.getAllOrder();
        cbOrder.setItems(orderList);
        cbOrder.setItemCaptionGenerator(order -> order.getProductOrderId());
        cbType.setItems("sType", "bType");
        cbType.setItemCaptionGenerator(select -> select.equals("sType") ? "单件" : "分组");
    }


    @Override
    public void updateAfterFilterApply() {
        // TODO Auto-generated method stub

    }

    public void wordToPDF(String filePath) {
        final Document document = new Document(filePath);
        final String overFile = filePath.replace(".doc", ".pdf");
        document.saveToFile(overFile, FileFormat.PDF);
    }

    public void inputValueBType(String input) {
        String[] inputValues = new String[4];
        for (int i = 0; i < 4; i++) {
            if (i < input.split("%").length) {
                inputValues[i] = input.split("%")[i];
            } else {
                inputValues[i] = "";
            }
        }
        int i = 0;
        for (Assembling a : singletonDetail) {
            i++;
            if (a.getPartNo().equals(inputValues[0])) {//若零件为Q1
                if (a.getBatchQty() == 1) {//若零件只有1件
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).focus();
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).setValue(inputValues[2]);//输入序列号
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).setValue(inputValues[3]);//输入炉号
                    return;
                } else {//若零件多于1件
                    for (int j = 0; j < a.getBatchQty(); j++) {//查看所有零件的序列号是否已经输入
                        if (Strings.isNullOrEmpty(((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i + j)).getValue())) {
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i + j)).focus();
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i + j)).setValue(inputValues[1]);//输入Rev
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i + j)).setValue(inputValues[2]);//输入序列号
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i + j)).setValue(inputValues[3]);//输入炉号
                            return;
                        }
                    }
                    //若全部录入，则覆盖第1件并清空剩余已录入信息重新录入
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).focus();
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).setValue(inputValues[2]);//输入序列号
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).setValue(inputValues[3]);//输入炉号
                    for (int j = 1; j < a.getBatchQty(); j++) {
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i + j)).clear();//清空序列号
                        ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i + j)).clear();//清空炉号
                    }
                    return;
                }
            }
        }
        i = singletonDetail.size() + 1;
        for (Assembling a : batchDetail) {
            i++;
            if (a.getPartNo().equals(inputValues[0])) {
                ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).focus();
                ((TextField) detailLayoutGrid.getComponent(3 * snCount - 3, i)).setValue(inputValues[1]);//输入Rev
                if (Strings.isNullOrEmpty(((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).getValue())) {
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).setValue(inputValues[2]);//输入序列号
                } else {
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).setValue(
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 2, i)).getValue() + "," + inputValues[2]);//输入序列号
                }
                if (Strings.isNullOrEmpty(((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).getValue())) {
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).setValue(inputValues[3]);//输入炉号
                } else {
                    ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).setValue(
                            ((TextField) detailLayoutGrid.getComponent(3 * snCount - 1, i)).getValue() + "," + inputValues[3]);//输入炉号
                }
                return;
            }
        }
        NotificationUtils.notificationInfo("没有找到零件号，请确认扫描信息！");
    }

    public void inputValueSType(String input) {
        String[] inputValues = new String[4];
        for (int i = 0; i < 4; i++) {
            if (i < input.split("%").length) {
                inputValues[i] = input.split("%")[i];
            } else {
                inputValues[i] = "";
            }
        }
        int i = 0;
        for (Assembling a : singletonDetail) {
            i++;
            if (a.getPartNo().equals(inputValues[0])) {//若零件为Q1
                if (a.getBatchQty() == 1) {//若零件只有1件
                    ((TextField) singletonGrid.getComponent(2, i)).focus();
                    ((TextField) singletonGrid.getComponent(2, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) singletonGrid.getComponent(3, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) singletonGrid.getComponent(5, i)).setValue(inputValues[2]);//输入序列号
                    ((TextField) singletonGrid.getComponent(6, i)).setValue(inputValues[3]);//输入炉号
                    return;
                } else {//若零件多于1件
                    for (int j = 0; j < a.getBatchQty(); j++) {//查看所有零件的序列号是否已经输入
                        if (Strings.isNullOrEmpty(((TextField) singletonGrid.getComponent(5, i + j)).getValue())) {
                            ((TextField) singletonGrid.getComponent(2, i + j)).focus();
                            ((TextField) singletonGrid.getComponent(2, i + j)).setValue(inputValues[1]);//输入Rev
                            ((TextField) singletonGrid.getComponent(3, i + j)).setValue(inputValues[1]);//输入Rev
                            ((TextField) singletonGrid.getComponent(5, i + j)).setValue(inputValues[2]);//输入序列号
                            ((TextField) singletonGrid.getComponent(6, i + j)).setValue(inputValues[3]);//输入炉号
                            return;
                        }
                    }
                    //若全部录入，则覆盖第1件并清空剩余已录入信息重新录入
                    ((TextField) singletonGrid.getComponent(2, i)).focus();
                    ((TextField) singletonGrid.getComponent(2, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) singletonGrid.getComponent(3, i)).setValue(inputValues[1]);//输入Rev
                    ((TextField) singletonGrid.getComponent(5, i)).setValue(inputValues[2]);//输入序列号
                    ((TextField) singletonGrid.getComponent(6, i)).setValue(inputValues[3]);//输入炉号
                    for (int j = 1; j < a.getBatchQty(); j++) {
                        ((TextField) singletonGrid.getComponent(5, i + j)).clear();//清空序列号
                        ((TextField) singletonGrid.getComponent(6, i + j)).clear();//清空炉号
                    }
                    return;
                }
            }
        }
        i = 0;
        for (Assembling a : batchDetail) {
            i++;
            if (a.getPartNo().equals(inputValues[0])) {
                ((TextField) batchGrid.getComponent(2, i)).focus();
                ((TextField) batchGrid.getComponent(2, i)).setValue(inputValues[1]);//输入Rev
                ((TextField) batchGrid.getComponent(3, i)).setValue(inputValues[1]);//输入Rev
                if (Strings.isNullOrEmpty(((TextField) batchGrid.getComponent(5, i)).getValue())) {
                    ((TextField) batchGrid.getComponent(5, i)).setValue(inputValues[2]);//输入序列号
                } else {
                    ((TextField) batchGrid.getComponent(5, i)).setValue(
                            ((TextField) batchGrid.getComponent(5, i)).getValue() + "," + inputValues[2]);//输入序列号
                }
                if (Strings.isNullOrEmpty(((TextField) batchGrid.getComponent(6, i)).getValue())) {
                    ((TextField) batchGrid.getComponent(6, i)).setValue(inputValues[3]);//输入炉号
                } else {
                    ((TextField) batchGrid.getComponent(6, i)).setValue(
                            ((TextField) batchGrid.getComponent(6, i)).getValue() + "," + inputValues[3]);//输入炉号
                }
                return;
            }
        }
        NotificationUtils.notificationInfo("没有找到零件号，请确认扫描信息！");
    }

    public void focusOnSnBType(int iComponent) {
        for (int i = 3 * iComponent - 3; i < 3 * iComponent; i++) {
            for (int j = 0; j < detailLayoutGrid.getRows(); j++) {
                if (detailLayoutGrid.getComponent(i, j) != null) {
                    detailLayoutGrid.getComponent(i, j).addStyleName(CoreTheme.FONT_PRIMARY);
                }
            }
        }
    }

    public void defocusOnSnBType(int iComponent) {
        for (int i = 3 * iComponent - 3; i < 3 * iComponent; i++) {
            for (int j = 0; j < detailLayoutGrid.getRows(); j++) {
                if (detailLayoutGrid.getComponent(i, j) != null) {
                    detailLayoutGrid.getComponent(i, j).removeStyleName(CoreTheme.FONT_PRIMARY);
                }
            }
        }
    }

    public void focusOnSnSType() {
        cbSnBatch.setSelectedItem(order.getProductOrderId() + String.format("%4d", snCount).replace(" ", "0"));
        //TODO:Highlight current sn SType
    }

    public void loadSavedDataBType() {
        if (assemblingService.getByOrderNo(order.getProductOrderId()).size() <= 0) {
            return;
        }
        int sSize = singletonDetail.size();
        int bSize = batchDetail.size();

        for (int i = 0; i < order.getProductNumber(); i++) {
            String snBatch = order.getProductOrderId() + String.format("%4d", i + 1).replace(" ", "0");
            //****单件
            for (int s = 0; s < sSize; s++) {
                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, s + 1);
                String partNo = tfPartNo.getValue();

                Assembling singleAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(s));
                if (singleAssemb == null) {
                    continue;
                }

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(3 * i, s + 1);
                tfPartRev.setValue(singleAssemb.getPartRev());

                TextField tfSerialNo = (TextField) detailLayoutGrid.getComponent(3 * i + 1, s + 1);
                tfSerialNo.setValue(singleAssemb.getSerialNo());

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(3 * i + 2, s + 1);
                tfHeatNoLot.setValue(singleAssemb.getHeatNoLot());
            }
            //*******批次
            for (int b = 0; b < bSize; b++) {

                TextField tfPartNo = (TextField) titleLayoutGrid.getComponent(0, b + sSize + 2);
                String partNo = tfPartNo.getValue();

                Assembling bAssemb = assemblingService.getBySnAndPartNo(snBatch, partNo, Integer.toString(b));
                if (bAssemb == null) {
                    continue;
                }

                TextField tfPartRev = (TextField) detailLayoutGrid.getComponent(i * 3, b + sSize + 2);
                tfPartRev.setValue(bAssemb.getPartRev());

                TextField tfBatch = (TextField) detailLayoutGrid.getComponent(i * 3 + 1, b + sSize + 2);
                tfBatch.setValue(bAssemb.getBatch());

                TextField tfHeatNoLot = (TextField) detailLayoutGrid.getComponent(i * 3 + 2, b + sSize + 2);
                tfHeatNoLot.setValue(bAssemb.getHeatNoLot());
            }
        }
    }
}
