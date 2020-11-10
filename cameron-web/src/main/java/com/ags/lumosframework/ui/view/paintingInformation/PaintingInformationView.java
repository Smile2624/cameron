package com.ags.lumosframework.ui.view.paintingInformation;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.KeyAction;
import org.vaadin.artur.KeyAction.KeyActionEvent;
import org.vaadin.artur.KeyAction.KeyActionListener;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Menu(caption = "PaintingInformation", captionI18NKey = "PaintingInformation.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Production", order = 3)
@SpringView(name = "PaintingInformation", ui = CameronUI.class)
public class PaintingInformationView extends BaseView implements Button.ClickListener, IFilterableView {

    /**
     *
     */
    private static final long serialVersionUID = -5803487926336036966L;
    private static final String DOCTEMPLATE = "IQADocExportTemplate";
    GridLayout glLayout = new GridLayout(4, 2);
    GridLayout glLayoutText = new GridLayout(5, 9);
    List<Role> role = null;
    StringBuilder returnMessage = new StringBuilder();
    SocketClient client = null;
    boolean isInputOk = false;
    String errorMessage = "";
    String paintSpecificationDesc = "";
    @I18Support(caption = "Save", captionKey = "PaintingInformation.Save")
    private Button btnSave = new Button();
    @I18Support(caption = "Start", captionKey = "common.start")
    private Button btnStart = new Button();
    private ComboBox<String> tfproductOrderId = new ComboBox<String>();
    @I18Support(caption = "PartNO", captionKey = "PaintingInformation.PartNO")
    private LabelWithSamleLineCaption tfPartNO = new LabelWithSamleLineCaption();//零件号
    @I18Support(caption = "PartNORev", captionKey = "PaintingInformation.PartNORev")
    private LabelWithSamleLineCaption tfPartNORev = new LabelWithSamleLineCaption();//零件号版本
    @I18Support(caption = "PartNODesc", captionKey = "PaintingInformation.PartNODesc")
    private LabelWithSamleLineCaption tfPartNODesc = new LabelWithSamleLineCaption();//零件描述
    @I18Support(caption = "QualityPlanStandard", captionKey = "PaintingInformation.QualityPlanStandard")
    private LabelWithSamleLineCaption tfQualityPlanStandard = new LabelWithSamleLineCaption();//质量计划标准
    @I18Support(caption = "QualityPlanRev", captionKey = "PaintingInformation.QualityPlanRev")
    private LabelWithSamleLineCaption tfQualityPlanRev = new LabelWithSamleLineCaption();//质量计划标准版本
    @I18Support(caption = "PaintingSpecification", captionKey = "PaintingInformation.PaintingSpecification")
    private LabelWithSamleLineCaption tfPaintingSpecification = new LabelWithSamleLineCaption();//喷漆规范
    @I18Support(caption = "PaintingSpecificationRev", captionKey = "PaintingInformation.PaintingSpecificationRev")
    private LabelWithSamleLineCaption tfPaintingSpecificationRev = new LabelWithSamleLineCaption();//版本
    @I18Support(caption = "IsClear", captionKey = "PaintingInformation.IsClear")
    private CheckBox cbIsClear = new CheckBox();//是否清洗
    @I18Support(caption = "IsProtect", captionKey = "PaintingInformation.IsProtect")
    private CheckBox cbIsProtect = new CheckBox();//是否保护
    @I18Support(caption = "底漆")
    private CheckBox cbPrimer = new CheckBox();//有无底漆
    @I18Support(caption = "面漆")
    private CheckBox cbFinal = new CheckBox();//有无面漆
    @I18Support(caption = "Primer", captionKey = "PaintingInformation.Primer")
    private LabelWithSamleLineCaption tfPrimer = new LabelWithSamleLineCaption();//底漆
    @I18Support(caption = "PrimerAirTemp", captionKey = "PaintingInformation.PrimerAirTemp")
    private TextField tfPrimerAirTemp = new TextField();//底漆空气温度
    @I18Support(caption = "PrimerSurfaceTemp", captionKey = "PaintingInformation.PrimerSurfaceTemp")
    private TextField tfPrimerSurfaceTemp = new TextField();//底漆表面湿度
    //    private DateTimeField dtPrimerDryTime = new DateTimeField();
    @I18Support(caption = "PrimerHumidity", captionKey = "PaintingInformation.PrimerHumidity")
    private TextField tfPrimerHumidity = new TextField();//底漆湿度
    @I18Support(caption = "PrimerDewPoint", captionKey = "PaintingInformation.PrimerDewPoint")
    private TextField tfPrimerDewPoint = new TextField();//底漆漏点温度
    @I18Support(caption = "PrimerPaintApplied", captionKey = "PaintingInformation.PrimerPaintApplied")
    private TextField tfPrimerPaintApplied = new TextField();//底漆油漆型号
    @I18Support(caption = "PrimerDryFilmThickness", captionKey = "PaintingInformation.PrimerDryFilmThickness")
    private TextField tfPrimerDryFilmThickness = new TextField();//底漆干膜厚度要求
    @I18Support(caption = "PrimerDry", captionKey = "PaintingInformation.PrimerDry")
    private LabelWithSamleLineCaption tfPrimerDry = new LabelWithSamleLineCaption();//底漆干燥
    @I18Support(caption = "PrimerDryAirTemp", captionKey = "PaintingInformation.PrimerDryAirTemp")
    private TextField tfPrimerDryAirTemp = new TextField();//底漆干燥空气温度
    @I18Support(caption = "PrimerDryHumidity", captionKey = "PaintingInformation.PrimerDryHumidity")
    private TextField tfPrimerDryHumidity = new TextField();//底漆干燥湿度
    @I18Support(caption = "PrimerDryMethod", captionKey = "PaintingInformation.PrimerDryMethod")
    private TextField tfPrimerDryMethod = new TextField();//底漆干燥方式
    @I18Support(caption = "PrimerDryTime", captionKey = "PaintingInformation.PrimerDryTime")
    private TextField tfPrimerDryTime = new TextField();//底漆干燥时间
    @I18Support(caption = "FinalCoat", captionKey = "PaintingInformation.FinalCoat")
    private LabelWithSamleLineCaption tfFinalCoat = new LabelWithSamleLineCaption();//面漆
    @I18Support(caption = "FinalCoatAirTemp", captionKey = "PaintingInformation.FinalCoatAirTemp")
    private TextField tfFinalCoatAirTemp = new TextField();//面漆空气温度
    @I18Support(caption = "FinalCoatSurfaceTemp", captionKey = "PaintingInformation.FinalCoatSurfaceTemp")
    private TextField tfFinalCoatSurfaceTemp = new TextField();//面漆表面湿度
    @I18Support(caption = "FinalCoatHumidity", captionKey = "PaintingInformation.FinalCoatHumidity")
    private TextField tfFinalCoatHumidity = new TextField();//面漆湿度
    @I18Support(caption = "FinalCoatDewPoint", captionKey = "PaintingInformation.FinalCoatDewPoint")
    private TextField tfFinalCoatDewPoint = new TextField();//面漆漏点温度
    @I18Support(caption = "FinalCoatPaintApplied", captionKey = "PaintingInformation.FinalCoatPaintApplied")
    private TextField tfFinalCoatPaintApplied = new TextField();//面漆油漆型号
    @I18Support(caption = "FinalCoatColor", captionKey = "PaintingInformation.FinalCoatColor")
    private TextField tfFinalCoatColor = new TextField();//面漆颜色
    @I18Support(caption = "FinalCoatTotalDryFilmThickness", captionKey = "PaintingInformation.FinalCoatTotalDryFilmThickness")
    private TextField tfFinalCoatTotalDryFilmThickness = new TextField();//面漆总干膜厚度要求
    @I18Support(caption = "FinalCoatDry", captionKey = "PaintingInformation.FinalCoatDry")
    private LabelWithSamleLineCaption tfFinalCoatDry = new LabelWithSamleLineCaption();//面漆干燥
    @I18Support(caption = "FinalCoatDryAirTemp", captionKey = "PaintingInformation.FinalCoatDryAirTemp")
    private TextField tfFinalCoatDryAirTemp = new TextField();//面漆干燥空气温度
    @I18Support(caption = "FinalCoatDryHumidity", captionKey = "PaintingInformation.FinalCoatDryHumidity")
    private TextField tfFinalCoatDryHumidity = new TextField();//面漆干燥湿度
    @I18Support(caption = "FinalCoatDryMethod", captionKey = "PaintingInformation.FinalCoatDryMethod")
    private TextField tfFinalCoatDryMethod = new TextField();//面漆干燥方式
    @I18Support(caption = "FinalCoatDryTime", captionKey = "PaintingInformation.FinalCoatDryTime")
    private TextField tfFinalCoatDryTime = new TextField();//面漆干燥时间
    private Button[] btns = new Button[]{btnSave};//btnStart, btnGetInfo, ,btnExport
    private CheckBox[] cBoxs = new CheckBox[]{cbIsClear, cbIsProtect};
    private TextField[] textFields = new TextField[]{tfPrimerHumidity,
            tfPrimerDryHumidity, tfPrimerDryTime, tfFinalCoatHumidity,
            tfFinalCoatDryHumidity, tfFinalCoatDryTime};//,tfVisualTotalDryFilmThickness,tfIntermediate,tfPrimerDryFilmThickness,tfFinalCoatTotalDryFilmThickness
    private TextField[] textFieldTemps = new TextField[]{tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerDewPoint, tfPrimerDryAirTemp,
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatDewPoint, tfFinalCoatDryAirTemp};
    private TextField[] textFieldAll = new TextField[]{
            tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerHumidity, tfPrimerDewPoint, tfPrimerPaintApplied,
            tfPrimerDryFilmThickness, tfPrimerDryAirTemp, tfPrimerDryHumidity, tfPrimerDryMethod, tfPrimerDryTime,
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatHumidity, tfFinalCoatDewPoint,
            tfFinalCoatPaintApplied, tfFinalCoatColor, tfFinalCoatTotalDryFilmThickness,
            tfFinalCoatDryAirTemp, tfFinalCoatDryHumidity, tfFinalCoatDryMethod, tfFinalCoatDryTime
    };//tfVisualInspection,tfVisualTotalDryFilmThickness,tfVisualGageNo,tfComments,tfIntermediate
    private TextField[] tfPrimers = new TextField[]{
            tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerHumidity, tfPrimerDewPoint,
            tfPrimerPaintApplied, tfPrimerDryFilmThickness, tfPrimerDryAirTemp,
            tfPrimerDryHumidity, tfPrimerDryMethod, tfPrimerDryTime
    };
    private TextField[] tfFinals = new TextField[]{
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatHumidity,
            tfFinalCoatDewPoint, tfFinalCoatPaintApplied, tfFinalCoatColor,
            tfFinalCoatTotalDryFilmThickness, tfFinalCoatDryAirTemp,
            tfFinalCoatDryHumidity, tfFinalCoatDryMethod, tfFinalCoatDryTime};
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private ProductionOrder order;
    private SparePart sparePart;
    private ProductInformation productInformation;
    private List<ProductRouting> productRoutingList;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private IPaintingInformationService paintingInformationService;
    @Autowired
    private IProductRoutingService productRoutingService;
    @Autowired
    private ICaMediaService mediaService;
    @Autowired
    private ICaConfigService caConfigService;
    private String loginUserName = "";
    @Autowired
    private UserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPaintingSpecificationService paintSpecificationService;
    // x,y 当前数据输入位置
    private int x = 0;
    private int y = 0;
    private String ipAddress = "";
    private String prefixSend = "";// 当前发送的语音指令，用于判断返回信息并执行什么操作

    public PaintingInformationView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        tfproductOrderId.setPlaceholder(I18NUtility.getValue("PaintingInformation.productOrderId", "productOrderId"));


        KeyAction ka = new KeyAction(KeyCode.ENTER, new int[]{});
        ka.addKeypressListener(new KeyActionListener() {

            private static final long serialVersionUID = 935732852893745159L;

            @Override
            public void keyPressed(KeyActionEvent keyPressEvent) {
                String productOrderId = tfproductOrderId.getValue().trim();
                if (!"".equals(productOrderId)) {
                    initTextField();
                    initLabel();
                    order = productionOrderService.getByNo(productOrderId);
                    //判断工单是否锁定
                    if(order.getBomLockFlag()){
                        throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked", "This Order is locked,Please contact the engineer to solve !"));
                    }
                    if (order != null) {
                        List<PaintingInformation> list = paintingInformationService.getBySn(productOrderId);
                        if (list == null || list.size() <= 0) {
                            String productId = order.getProductId();
                            String productVersionId = order.getProductVersionId();
                            String routingGroup = order.getRoutingGroup();
                            String innerGroupNo = order.getInnerGroupNo();
                            if (!Strings.isNullOrEmpty(productId) && !Strings.isNullOrEmpty(productVersionId)) {
                                sparePart = sparePartService.getByNoRev(productId, productVersionId);
                            }
                            paintSpecificationDesc = order.getPaintSpecification();
                            PaintingSpecification rlt = getPaintRuler(paintSpecificationDesc);
                            if (rlt != null) {
                                tfPrimerDryFilmThickness.setValue(rlt.getPrimer());
                                float[] thickness = sumPaintThinckness(rlt);
//                                tfFinalCoatTotalDryFilmThickness.setValue(Math.round(thickness[0]) + "-" + Math.round(thickness[1]));
                                tfFinalCoatTotalDryFilmThickness.setValue(rlt.getTotal());//Changed by Cameron: 结果中直接保存总干膜厚度
                            }
                            freshLab(order);
                        } else {
                            NotificationUtils.notificationError("工单号：" + productOrderId + " 的喷漆信息已保存！");
                        }
                    } else {
                        NotificationUtils.notificationError("工单号不存在");
                    }
                } else {
                    NotificationUtils.notificationError("请输入工单号");
                }
            }
        });
        ka.extend(tfproductOrderId);

        tfproductOrderId.addValueChangeListener(new ValueChangeListener<String>() {

            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                String productOrderId = tfproductOrderId.getValue();
                if (!Strings.isNullOrEmpty(productOrderId)) {
                    initTextField();
                    initLabel();
                    order = productionOrderService.getByNo(productOrderId);
                    if(order.getBomLockFlag()){
                        throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked", "This Order is locked,Please contact the engineer to solve !"));
                    }
                    if (order != null) {
                        List<PaintingInformation> list = paintingInformationService.getBySn(productOrderId);
                        if (list == null || list.size() <= 0) {
                            String productId = order.getProductId();
                            String productVersionId = order.getProductVersionId();
                            String routingGroup = order.getRoutingGroup();
                            String innerGroupNo = order.getInnerGroupNo();
                            if (!Strings.isNullOrEmpty(productId) && !Strings.isNullOrEmpty(productVersionId)) {
                                sparePart = sparePartService.getByNoRev(productId, productVersionId);
                            }
                            paintSpecificationDesc = order.getPaintSpecification();
                            PaintingSpecification rlt = getPaintRuler(paintSpecificationDesc);
                            if (rlt != null) {
                                tfPrimerDryFilmThickness.setValue(rlt.getPrimer());
                                float[] thickness = sumPaintThinckness(rlt);
//                                tfFinalCoatTotalDryFilmThickness.setValue(Math.round(thickness[0])+ "-" + Math.round(thickness[1]));
                                  tfFinalCoatTotalDryFilmThickness.setValue(rlt.getTotal());//Changed by Cameron: 结果中直接保存总干膜厚度
                            }
                            freshLab(order);
                        } else {
                            NotificationUtils.notificationError("工单号：" + productOrderId + " 的喷漆信息已保存！");
                        }
                    } else {
                        NotificationUtils.notificationError("工单号不存在");
                    }
                } else {
                    initTextField();
                    initLabel();
                }
            }
        });
        hlTempToolBox.addComponent(tfproductOrderId);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnSave.setIcon(VaadinIcons.PACKAGE);
        btnStart.setIcon(VaadinIcons.START_COG);
        for (CheckBox cBox : cBoxs) {
            cBox.addValueChangeListener(new ValueChangeListener<Boolean>() {


                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent<Boolean> event) {
                    boolean isClear = cbIsClear.getValue();
                    boolean isProtect = cbIsProtect.getValue();
                    if (isClear && isProtect && role.contains(roleService.getByName("operator"))) {
                        btnSave.setEnabled(true);
                    } else {
                        btnSave.setEnabled(false);
                    }
                }
            });
        }


        Panel panel = new Panel();
        panel.setWidth("100%");
        panel.setHeightUndefined();
        glLayout.setSpacing(true);
        glLayout.setMargin(true);
        glLayout.setWidth("100%");
        glLayout.addComponent(this.tfPartNO, 0, 0);
        glLayout.addComponent(this.tfPartNORev, 1, 0);
        glLayout.addComponent(this.tfPartNODesc, 2, 0);
        glLayout.addComponent(this.tfQualityPlanStandard, 3, 0);
        glLayout.addComponent(this.tfQualityPlanRev, 0, 1);
        glLayout.addComponent(this.tfPaintingSpecification, 1, 1);
        glLayout.addComponent(this.tfPaintingSpecificationRev, 2, 1);
        panel.setContent(glLayout);
        vlRoot.addComponent(panel);

        Panel panelText = new Panel();
        panelText.setWidth("100%");
        panelText.setSizeFull();

        glLayoutText.setSpacing(true);
        glLayoutText.setMargin(true);
        glLayoutText.setWidth("100%");
        glLayoutText.setHeightUndefined();

        //第一行
        glLayoutText.addComponent(this.cbIsClear, 0, 0);
        glLayoutText.addComponent(this.cbIsProtect, 1, 0);
        glLayoutText.addComponent(cbPrimer, 2, 0);
        glLayoutText.addComponent(cbFinal, 3, 0);
        cbPrimer.addValueChangeListener(event -> {
            if (event.getValue() == true) {
                for (TextField tf : tfPrimers) {
                    tf.setValue("");
                    tf.setReadOnly(false);
                }
            } else if (event.getValue() == false) {
                for (TextField tf : tfPrimers) {
                    tf.setValue("/");
                    tf.setReadOnly(true);
                }
            }
        });
        cbFinal.addValueChangeListener(event -> {
            if (event.getValue() == true) {
                for (TextField tf : tfFinals) {
                    tf.setValue("");
                    tf.setReadOnly(false);
                }
            } else if (event.getValue() == false) {
                for (TextField tf : tfFinals) {
                    tf.setValue("/");
                    tf.setReadOnly(true);
                }
            }
        });

        //第二行
        glLayoutText.addComponent(this.tfPrimer, 0, 1);
        glLayoutText.addComponent(this.tfPrimerAirTemp, 1, 1);
        glLayoutText.addComponent(this.tfPrimerSurfaceTemp, 2, 1);
        glLayoutText.addComponent(this.tfPrimerHumidity, 3, 1);
        glLayoutText.addComponent(this.tfPrimerDewPoint, 4, 1);
        //第三行
        glLayoutText.addComponent(this.tfPrimerPaintApplied, 1, 2);
        glLayoutText.addComponent(this.tfPrimerDryFilmThickness, 2, 2);
        //tfPrimerDryFilmThickness.setReadOnly(true);


        //第四行
        glLayoutText.addComponent(this.tfPrimerDry, 0, 3);
        glLayoutText.addComponent(this.tfPrimerDryAirTemp, 1, 3);
        glLayoutText.addComponent(this.tfPrimerDryHumidity, 2, 3);
        glLayoutText.addComponent(this.tfPrimerDryMethod, 3, 3);
        glLayoutText.addComponent(this.tfPrimerDryTime, 4, 3);
        //第五行
        glLayoutText.addComponent(this.tfFinalCoat, 0, 4);
        glLayoutText.addComponent(this.tfFinalCoatAirTemp, 1, 4);
        glLayoutText.addComponent(this.tfFinalCoatSurfaceTemp, 2, 4);
        glLayoutText.addComponent(this.tfFinalCoatHumidity, 3, 4);
        glLayoutText.addComponent(this.tfFinalCoatDewPoint, 4, 4);
        //第六行
        glLayoutText.addComponent(this.tfFinalCoatPaintApplied, 1, 5);
        glLayoutText.addComponent(this.tfFinalCoatColor, 2, 5);
        glLayoutText.addComponent(this.tfFinalCoatTotalDryFilmThickness, 3, 5);
        //tfFinalCoatTotalDryFilmThickness.setReadOnly(true);
        //第七行
        glLayoutText.addComponent(this.tfFinalCoatDry, 0, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryAirTemp, 1, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryHumidity, 2, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryMethod, 3, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryTime, 4, 6);
        //第八行

//        for(TextField textField : textFields) {
//
//        	textField.addBlurListener(new BlurListener() {
//                /**
//				 *
//				 */
//				private static final long serialVersionUID = -6922056973844720398L;
//				@Override
//				public void blur(BlurEvent event){
//					TextField textField = (TextField)event.getSource();
//					String textValue = textField.getValue().trim();
//					if(!Strings.isNullOrEmpty(textValue) && !tfTorqueValue1.getValue()(textValue)) {
//						textField.clear();
//						NotificationUtils.notificationError(textField.getCaption()+",请填入大于零的数字！");
//					}
//				}
//            });
//        }
//
//        for(TextField textField : textFieldTemps) {
//
//        	textField.addBlurListener(new BlurListener() {
//                /**
//				 *
//				 */
//				private static final long serialVersionUID = -6922056973844720398L;
//				@Override
//				public void blur(BlurEvent event){
//					TextField textField = (TextField)event.getSource();
//					String textValue = textField.getValue().trim();
//					if(!Strings.isNullOrEmpty(textValue) && !valueIsTemp(textValue)) {
//						textField.clear();
//						NotificationUtils.notificationError(textField.getCaption()+",请填入数字，可小数、可正负！");
//					}
//				}
//            });
//        }

        panelText.setContent(glLayoutText);
        vlRoot.addComponent(panelText);
        vlRoot.setExpandRatio(panelText, 0.1f);


        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        String productOrderId = tfproductOrderId.getValue().trim();
        if (btnSave.equals(button)) {
            if (!"".equals(productOrderId) && order != null) {
                if (productOrderId.equals(order.getProductOrderId())) {
                    List<PaintingInformation> list = paintingInformationService.getBySn(productOrderId);
                    if (list == null || list.size() <= 0) {
                        //检查导出报告的路径是否正确
                        String path = "";
                        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                        if (caConfig != null) {
                            path = caConfig.getConfigValue();
                            if (Strings.isNullOrEmpty(path)) {
                                NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                                return;
                            }
                        }
                        //是否有电子签名
                        Media iqaImage = null;
                        iqaImage = mediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(),
                                RequestInfo.current().getUserName());
                        if (iqaImage == null) {
                            NotificationUtils.notificationError("当前登录用户没有维护电子签名，请首先维护电子签名");
                            return;
                        }
                        //获取喷漆信息
                        PaintingInformation paintingInformation = setValueToPaintingInformation();
                        //保存喷漆信息 并导出报告
                        if (paintingInformation != null) {
                            //导出报告
                            try {
                                //保存信息
                                paintingInformationService.save(paintingInformation);

                                initTextField();
                                initLabel();
                                tfproductOrderId.setSelectedItem(null);
                                List<String> orderNoList = paintingInformationService.getAllButPaintOrder();
                                if (list != null && list.size() > 0) {
                                    tfproductOrderId.setItems(orderNoList);
                                } else {
                                    tfproductOrderId.setItems(new ArrayList<String>());
                                }
                                NotificationUtils.notificationInfo("喷漆信息保存成功！");
                                if (client != null) {
                                    client.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                NotificationUtils.notificationError("导出文件或保存信息出现异常：" + e.getMessage());
                            }
                        } else {
                            NotificationUtils.notificationError("工单信息获取失败！");
                        }
                    } else {
                        NotificationUtils.notificationError("此工单号已保存到喷漆信息，请不要重复操作！");
                    }
                } else {
                    NotificationUtils.notificationError("输入的工单号和查询的内容不匹配，请输入有效的工单号并进行查询,点击Enter键！");
                }
            } else {
                NotificationUtils.notificationError("请输入有效的工单号！");
            }
        } else if (btnStart.equals(button)) {
            // 检验开始,光标聚焦gridlayout(2,0)
            TextField field = (TextField) glLayoutText.getComponent(2, 0);
            x = 0;
            y = 0;
            // 光标聚焦之后，连接Socket，并且发送语音播报第一条指令
            ipAddress = RequestInfo.current().getUserIpAddress();
            System.out.println(ipAddress);
            try {
                client = new SocketClient(ipAddress, AppConstant.PORT);

                client.setReceiveListener(message -> {
                    // 定义 OnMessage 后执行事件。
                    if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)//OC
                            && AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
                        // [OC]语音播放结束
                        System.out.println("Receive Message -> " + message);
                        // 发送请求语音识别的指令
                        try {
                            prefixSend = AppConstant.PREFIXSTARTRECORD;
                            client.sendMessage("[BR]");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)//OR
                            && AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
                        // 发送语音识别请求并且返回的是语音识别结束字串
                        // 判断返回结果是否复核标准范围
                        // 获取检验项名称
                        // message去除信息头
                        String messageBody = message.split("]")[1];
                        if (!message.split("\\|")[1].equals("1")) {
                            returnMessage.append(messageBody.split("\\|")[0]);
                        }
                    } else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {//ES
                        if (returnMessage.length() == 0) {
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (y == 0) {
                                    CheckBox checkBox = (CheckBox) glLayoutText.getComponent(x, 0);
                                    if (checkBox != null) {
                                        client.sendMessage("[PT]" + checkBox.getCaption() + "请输入是或否");
                                    }
                                } else {
                                    String caption = "";
                                    if (y < 6) {
                                        LabelWithSamleLineCaption lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y));
                                        if (lable == null) {
                                            lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y - 1));
                                        }
                                        caption = lable.getCaption();
                                    }
                                    TextField textField = ((TextField) glLayoutText.getComponent(x, y));
                                    if (textField != null) {
                                        client.sendMessage("[PT]请输入" + caption + textField.getCaption() + "的值");
                                    }
                                }
                            } catch (Exception e) {
                                NotificationUtils.notificationError("Socket连接中断");
                            }
                            // 没有识别当前检测项的值，任然播报当前检测项的信息
                        } else {
                            getUI().accessSynchronously(new Runnable() {
                                @Override
                                public void run() {
                                    String returnMessageStr = returnMessage.toString().trim();
                                    if (y == 0) {
                                        CheckBox component = (CheckBox) glLayoutText.getComponent(x, 0);
                                        if ("是".equals(returnMessageStr) || "否".equals(returnMessageStr)) {
                                            if (component != null) {
                                                component.setValue("是".equals(returnMessageStr));
                                                isInputOk = true;
                                            }
                                        } else {
                                            errorMessage = component.getCaption() + "请输入是或否";
                                        }
                                    } else {
                                        TextField component = (TextField) glLayoutText.getComponent(x, y);
                                        if (component != null) {
                                            //检测输入的值 是否是数字
                                            if (checkfield(component, textFields)) {
                                                if ("空".equals(returnMessageStr)) {
                                                    component.setValue("");
                                                    isInputOk = true;
                                                } else {
                                                    if (valueIsNumber(returnMessageStr)) {
                                                        component.setValue(returnMessageStr);
                                                        isInputOk = true;
                                                    } else {
                                                        errorMessage = component.getCaption() + "请输入大于零的数字";
                                                    }
                                                }

                                            } else if (checkfield(component, textFieldTemps)) {//检测输入的值 是否是数字  可正负
                                                if ("空".equals(returnMessageStr)) {
                                                    component.setValue("");
                                                    isInputOk = true;
                                                } else {
                                                    if (valueIsTempOther(returnMessageStr)) {
                                                        String firstValue = returnMessageStr.substring(0, 1);
                                                        if ("正".equals(firstValue)) {
                                                            component.setValue(returnMessageStr.substring(1, returnMessageStr.length()));
                                                        } else if ("负".equals(firstValue)) {
                                                            component.setValue("-" + returnMessageStr.substring(1, returnMessageStr.length()));
                                                        } else {
                                                            component.setValue(returnMessageStr);
                                                        }
                                                        isInputOk = true;
                                                    } else {
                                                        errorMessage = component.getCaption() + "请输入数字 可以是正负 可为小数";
                                                    }
                                                }
                                            } else {
                                                component.setValue(returnMessageStr);
                                                isInputOk = true;
                                            }
                                        }
                                    }
                                    returnMessage.delete(0, returnMessage.length());
                                }
                            });
                            // 播报下一个输入项
                            prefixSend = AppConstant.PREFIXPLAYTEXT;
                            try {
                                if (isInputOk && y == 0) {
                                    isInputOk = false;
                                    x++;
                                    CheckBox checkBox = (CheckBox) glLayoutText.getComponent(x, 0);
                                    if (checkBox != null) {
                                        client.sendMessage("[PT]" + checkBox.getCaption() + "请输入是或否");
                                    } else {//下一个输入项 若为null  该换行了
                                        x = 1;
                                        y++;
                                        LabelWithSamleLineCaption lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y));
                                        TextField textField = ((TextField) glLayoutText.getComponent(x, y));
                                        if (lable != null && textField != null) {
                                            client.sendMessage("[PT]请输入" + lable.getCaption() + textField.getCaption() + "的值");
                                        }
                                    }
                                } else if (isInputOk && y > 0 && y < 6) {
                                    isInputOk = false;
                                    x++;
                                    LabelWithSamleLineCaption lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y));
                                    if (lable == null) {
                                        lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y - 1));
                                    }

                                    TextField textField = ((TextField) glLayoutText.getComponent(x, y));
                                    if (lable != null && textField != null && !tfPrimerDryFilmThickness.equals(textField) && !tfFinalCoatTotalDryFilmThickness.equals(textField)) {
                                        client.sendMessage("[PT]请输入" + lable.getCaption() + textField.getCaption() + "的值");
                                    } else {
                                        x = 1;
                                        y++;
                                        String caption = "";
                                        if (y < 7) {
                                            lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y));
                                            if (lable == null) {
                                                lable = ((LabelWithSamleLineCaption) glLayoutText.getComponent(0, y - 1));
                                            }
                                            caption = lable.getCaption();
                                        }
                                        textField = ((TextField) glLayoutText.getComponent(x, y));
                                        if (lable != null && textField != null) {
                                            client.sendMessage("[PT]请输入" + caption + textField.getCaption() + "的值");
                                        } else {
                                            prefixSend = AppConstant.INSPECTIONDONE;
                                            client.sendMessage("[PT]喷漆信息录入结束");
                                        }
                                    }
                                } else if (isInputOk && y >= 6 && y <= glLayoutText.getRows()) {
                                    isInputOk = false;
                                    x++;
                                    TextField textField = ((TextField) glLayoutText.getComponent(x, y));
                                    if (textField != null && !tfPrimerDryFilmThickness.equals(textField) && !tfFinalCoatTotalDryFilmThickness.equals(textField)) {
                                        client.sendMessage("[PT]请输入" + textField.getCaption() + "的值");
                                    } else {
                                        x = 1;
                                        y++;
                                        textField = ((TextField) glLayoutText.getComponent(x, y));
                                        if (textField != null) {
                                            client.sendMessage("[PT]请输入" + textField.getCaption() + "的值");
                                        } else {
                                            if (y == glLayoutText.getRows()) {
                                                prefixSend = AppConstant.INSPECTIONDONE;
                                                client.sendMessage("[PT]喷漆信息录入结束");
                                            }
                                        }
                                    }
                                } else if (!isInputOk) {
                                    client.sendMessage("[PT]" + errorMessage);
                                    errorMessage = "";
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                prefixSend = AppConstant.PREFIXPLAYTEXT;
                client.sendMessage("[PT]现在开始喷漆信息录入,请输入是否清洗");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initTextField() {
        for (TextField textField : textFieldAll) {
            textField.clear();
        }
        cbIsClear.setValue(false);
        cbIsProtect.setValue(false);
    }

    public void initLabel() {
        tfPartNO.setValue("");
        tfPartNORev.setValue("");
        tfPartNODesc.setValue("");
        tfQualityPlanStandard.setValue("");
        tfQualityPlanRev.setValue("");
        tfPaintingSpecification.setValue("");
        tfPaintingSpecificationRev.setValue("");
    }

    public void freshLab(ProductionOrder order) {
        if (sparePart != null) {
            tfPartNO.setValue(sparePart.getSparePartNo());
            tfPartNORev.setValue(sparePart.getSparePartRev());
            tfPartNODesc.setValue(sparePart.getSparePartDec());
            tfQualityPlanStandard.setValue(sparePart.getQaPlan());
            tfQualityPlanRev.setValue(sparePart.getQaPlanRev());
        }
        tfPaintingSpecification.setValue(order.getPaintSpecification());
        cbPrimer.setValue(true);
        cbFinal.setValue(true);
//		tfPaintingSpecificationRev.setValue(initGroupNo(productRouting.getInnerGroupNo()));
        tfPaintingSpecificationRev.setValue(paintSpecificationService.getBySpecificationFile(order.getPaintSpecification()).getRev());//Changed by Cameron: 读取喷漆标准版本
    }

    public PaintingInformation setValueToPaintingInformation() {
        PaintingInformation paintingInformation = new PaintingInformation();

        paintingInformation.setWorkOrderSN(tfproductOrderId.getValue().trim());
        paintingInformation.setIsClear(cbIsClear.getValue());
        paintingInformation.setIsProtect(cbIsProtect.getValue());
        paintingInformation.setOpUser(RequestInfo.current().getUserName());

        String primerAirTemp = tfPrimerAirTemp.getValue().trim();
        if (valueIsTemp(primerAirTemp)) {
            paintingInformation.setPrimerAirTemp(Float.parseFloat(primerAirTemp));
        }

        String primerSurfaceTemp = tfPrimerSurfaceTemp.getValue().trim();
        if (valueIsTemp(primerSurfaceTemp)) {
            paintingInformation.setPrimerSurfaceTemp(Float.parseFloat(primerSurfaceTemp));
        }

        String primerHumidity = tfPrimerHumidity.getValue().trim();
        if (valueIsNumber(primerHumidity)) {
            paintingInformation.setPrimerHumidity(Float.parseFloat(primerHumidity));
        }

        String primerDewPoint = tfPrimerDewPoint.getValue().trim();
        if (valueIsTemp(primerDewPoint)) {
            paintingInformation.setPrimerDewPoint(Float.parseFloat(primerDewPoint));
        }

        paintingInformation.setPrimerPaintApplied(tfPrimerPaintApplied.getValue().trim());

//    	String primerDryFilmThickness = tfPrimerDryFilmThickness.getValue().trim();
//    	if(valueIsNumber(primerDryFilmThickness)) {
        //paintingInformation.setPrimerDryFilmThickness(0.0F);
//    	}
        paintingInformation.setPrimerDryFilmThickness(tfPrimerDryFilmThickness.getValue().trim());

        String primerDryAirTemp = tfPrimerDryAirTemp.getValue().trim();
        if (valueIsTemp(primerDryAirTemp)) {
            paintingInformation.setPrimerDryAirTemp(Float.parseFloat(primerDryAirTemp));
        }

        String primerDryHumidity = tfPrimerDryHumidity.getValue().trim();
        if (valueIsNumber(primerDryHumidity)) {
            paintingInformation.setPrimerDryHumidity(Float.parseFloat(primerDryHumidity));
        }

        paintingInformation.setPrimerDryMethod(tfPrimerDryMethod.getValue().trim());

        String primerDryTime = tfPrimerDryTime.getValue().trim();
        if (valueIsNumber(primerDryTime)) {
            paintingInformation.setPrimerDryTime(Float.parseFloat(primerDryTime));
        }

        String finalCoatAirTemp = tfFinalCoatAirTemp.getValue().trim();
        if (valueIsTemp(finalCoatAirTemp)) {
            paintingInformation.setFinalCoatAirTemp(Float.parseFloat(finalCoatAirTemp));
        }

        String finalCoatSurfaceTemp = tfFinalCoatSurfaceTemp.getValue().trim();
        if (valueIsTemp(finalCoatSurfaceTemp)) {
            paintingInformation.setFinalCoatSurfaceTemp(Float.parseFloat(finalCoatSurfaceTemp));
        }

        String finalCoatHumidity = tfFinalCoatHumidity.getValue().trim();
        if (valueIsNumber(finalCoatHumidity)) {
            paintingInformation.setFinalCoatHumidity(Float.parseFloat(finalCoatHumidity));
        }

        String finalCoatDewPoint = tfFinalCoatDewPoint.getValue().trim();
        if (valueIsTemp(finalCoatDewPoint)) {
            paintingInformation.setFinalCoatDewPoint(Float.parseFloat(finalCoatDewPoint));
        }

        paintingInformation.setFinalCoatPaintApplied(tfFinalCoatPaintApplied.getValue().trim());
        paintingInformation.setFinalCoatColor(tfFinalCoatColor.getValue().trim());

//    	String finalCoatTotalDryFilmThickness = tfFinalCoatTotalDryFilmThickness.getValue().trim();
//    	if(valueIsNumber(finalCoatTotalDryFilmThickness)) {
        //paintingInformation.setFinalCoatTotalDryFilmThickness(0.0F);
//    	}
        paintingInformation.setFinalCoatTotalDryFilmThickness(tfFinalCoatTotalDryFilmThickness.getValue().trim());

        String finalCoatDryAirTemp = tfFinalCoatDryAirTemp.getValue().trim();
        if (valueIsTemp(finalCoatDryAirTemp)) {
            paintingInformation.setFinalCoatDryAirTemp(Float.parseFloat(finalCoatDryAirTemp));
        }

        String finalCoatDryHumidity = tfFinalCoatDryHumidity.getValue().trim();
        if (valueIsNumber(finalCoatDryHumidity)) {
            paintingInformation.setFinalCoatDryHumidity(Float.parseFloat(finalCoatDryHumidity));
        }

        paintingInformation.setFinalCoatDryMethod(tfFinalCoatDryMethod.getValue().trim());

        String finalCoatDryTime = tfFinalCoatDryTime.getValue().trim();
        if (valueIsNumber(finalCoatDryTime)) {
            paintingInformation.setFinalCoatDryTime(Float.parseFloat(finalCoatDryTime));
        }

        return paintingInformation;
    }

    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public boolean valueIsNumber(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            boolean isNumber = RegExpValidatorUtils.isIsNumber(value);
            if (!isNumber) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean valueIsTemp(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            boolean isTemp = RegExpValidatorUtils.isIsTemp(value);
            if (!isTemp) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean valueIsTempOther(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            boolean isTemp = RegExpValidatorUtils.isIsTempOther(value);
            if (!isTemp) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean checkfield(TextField textField, TextField[] textFields) {
        if (textField != null) {
            for (TextField field : textFields) {
                if (textField.equals(field)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String initGroupNo(String groupNo) {
        String reGroupNo = groupNo;
        if (!Strings.isNullOrEmpty(groupNo)) {
            if (groupNo.length() == 1) {
                reGroupNo = "0" + groupNo;
            }
        }
        return reGroupNo;
    }

    @Override
    public void updateAfterFilterApply() {

    }

    @Override
    protected void init() {
        btnSave.setEnabled(false);
        initLabel();

        //初始化下拉框
        List<String> list = paintingInformationService.getAllButPaintOrder();
        if (list != null && list.size() > 0) {
            tfproductOrderId.setItems(list);
        } else {
            tfproductOrderId.setItems(new ArrayList<String>());
        }
        cbPrimer.setValue(true);
        cbFinal.setValue(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //权限设置
        loginUserName = RequestInfo.current().getUserName();
        role = userService.getByName(loginUserName).getRole();
        if (role.contains(roleService.getByName("operator"))) {
            btnStart.setEnabled(true);
        } else {
            btnStart.setEnabled(false);
            NotificationUtils.notificationError("当前登录人员：" + loginUserName + " 没有Operation输入权限！");
        }
    }

    public void createReport(String path, PaintingInformation paintingInformation) throws Exception {//sparePart productInformation
        String productOrderId = tfproductOrderId.getValue().trim();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("partNOAndRev", tfPartNO.getValue() + "/" + tfPartNORev.getValue());//partNOAndRev sparePart.getSparePartNo()+"/"+sparePart.getSparePartRev()
        params.put("partNODesc", tfPartNODesc.getValue());
        params.put("productOrderId", productOrderId);
        params.put("qualityPlanStandard", tfQualityPlanStandard.getValue());
        params.put("qPRev", tfQualityPlanRev.getValue());
        params.put("paintingSpecification", tfPaintingSpecification.getValue().trim());//productInformation.getPaintingSpecificationFile()
        params.put("pSRev", tfPaintingSpecificationRev.getValue().trim());//productInformation.getPaintingSpecificationFileRev()
        params.put("primerAirTemp", paintingInformation.getPrimerAirTemp() == null ? "" : paintingInformation.getPrimerAirTemp().toString());
        params.put("pSTemp", paintingInformation.getPrimerSurfaceTemp() == null ? "" : paintingInformation.getPrimerSurfaceTemp().toString());
        params.put("primerHumidity", paintingInformation.getPrimerHumidity() == null ? "" : paintingInformation.getPrimerHumidity().toString());
        params.put("primerDewPoint", paintingInformation.getPrimerDewPoint() == null ? "" : paintingInformation.getPrimerDewPoint().toString());
        params.put("primerPaintApplied", paintingInformation.getPrimerPaintApplied());
        params.put("pDF", paintingInformation.getPrimerDryFilmThickness() == null ? "" : paintingInformation.getPrimerDryFilmThickness().toString());
        params.put("pDryAirTemp", paintingInformation.getPrimerDryAirTemp() == null ? "" : paintingInformation.getPrimerDryAirTemp().toString());
        params.put("primerDryHumidity", paintingInformation.getPrimerDryHumidity() == null ? "" : paintingInformation.getPrimerDryHumidity().toString());
        params.put("primerDryMethod", paintingInformation.getPrimerDryMethod());
        params.put("primerDryTime", paintingInformation.getPrimerDryTime() == null ? "" : paintingInformation.getPrimerDryTime().toString());
        params.put("fCoatAirTemp", paintingInformation.getFinalCoatAirTemp() == null ? "" : paintingInformation.getFinalCoatAirTemp().toString());
        params.put("fCSTemp", paintingInformation.getFinalCoatSurfaceTemp() == null ? "" : paintingInformation.getFinalCoatSurfaceTemp().toString());
        params.put("finalCoatHumidity", paintingInformation.getFinalCoatHumidity() == null ? "" : paintingInformation.getFinalCoatHumidity().toString());
        params.put("finalCoatDewPoint", paintingInformation.getFinalCoatDewPoint() == null ? "" : paintingInformation.getFinalCoatDewPoint().toString());
        params.put("finalCoatPaintApplied", paintingInformation.getFinalCoatPaintApplied());
        params.put("finalCoatColor", paintingInformation.getFinalCoatColor());
        params.put("TDFT", paintingInformation.getFinalCoatTotalDryFilmThickness() == null ? "" : paintingInformation.getFinalCoatTotalDryFilmThickness().toString());
        params.put("fCDryAirTemp", paintingInformation.getFinalCoatDryAirTemp() == null ? "" : paintingInformation.getFinalCoatDryAirTemp().toString());
        params.put("finalCoatDryHumidity", paintingInformation.getFinalCoatDryHumidity() == null ? "" : paintingInformation.getFinalCoatDryHumidity().toString());
        params.put("finalCoatDryMethod", paintingInformation.getFinalCoatDryMethod());
        params.put("finalCoatDryTime", paintingInformation.getFinalCoatDryTime() == null ? "" : paintingInformation.getFinalCoatDryTime().toString());
        params.put("visualInspection", paintingInformation.getVisualInspection());
        params.put("visualTotalDryFilmThickness", paintingInformation.getVisualTotalDryFilmThickness() == null ? "" : paintingInformation.getVisualTotalDryFilmThickness().toString());
        params.put("visualGageNo", paintingInformation.getVisualGageNo());
        params.put("comments", paintingInformation.getComments());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        params.put("AndDate", "/" + sdf.format(new Date()));

        BASE64Encoder encoder = new BASE64Encoder();
        Media iqaImage = null;
        iqaImage = mediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(),
                RequestInfo.current().getUserName());
        if (iqaImage == null) {
            throw new PlatformException(
                    "工单为：" + productOrderId + " 的喷漆保存信息操作人员" + RequestInfo.current().getUserName() + " 没有配置电子签名！");
        }
        params.put("operator", encoder.encode(inputStream2byte(iqaImage.getMediaStream())));

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        configuration.setDirectoryForTemplateLoading(new File(AppConstant.DOC_XML_FILE_PATH));

        Template template = configuration.getTemplate("paintingInformation.xml", "utf-8");

        // 输出文件
        File outFile = new File(
                path + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT + productOrderId + ".doc");// C:\\Users\\bruce_yang\\Desktop\\喀麦隆文件\\生成的word.doc

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                1024 * 1024);
        template.process(params, out);
        out.flush();
        out.close();
        NotificationUtils.notificationInfo("喷漆信息导出成功！");

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

    //通过工单的Routing信息获取喷漆标准
    public PaintingSpecification getPaintRuler(String routingDesc) {
        PaintingSpecification specification = paintSpecificationService.getBySpecificationFile(routingDesc);
        return specification;
    }

    public float[] sumPaintThinckness(PaintingSpecification instance) {

        float minSum = 0;
        float maxSum = 0;
        if (instance != null) {
            String primer = instance.getPrimer();
            String finals = instance.getFinals();
            if (!Strings.isNullOrEmpty(primer)) {
                if (primer.contains("-")) {
                    float min = Float.parseFloat(primer.split("-")[0].trim());
                    minSum += min;
                    float max = Float.parseFloat(primer.split("-")[1].trim());
                    maxSum += max;
                } else {
                    minSum += Float.parseFloat(primer.trim());
                    maxSum += Float.parseFloat(primer.trim());
                }
            }
            if (!Strings.isNullOrEmpty(finals)) {
                if (finals.contains("-")) {
                    float min = Float.parseFloat(finals.split("-")[0].trim());
                    minSum += min;
                    float max = Float.parseFloat(finals.split("-")[1].trim());
                    maxSum += max;
                } else {
                    minSum += Float.parseFloat(finals.trim());
                    maxSum += Float.parseFloat(finals.trim());
                }
            }
        }
        return new float[]{minSum, maxSum};
    }

}
