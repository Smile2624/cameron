package com.ags.lumosframework.ui.view.paintinginspection;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.ElectronicSignatureLoGoType;
import com.ags.lumosframework.enums.PaintingInspectionResult;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.CoreTheme;
import com.ags.lumosframework.web.vaadin.base.IFilterableView;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Menu(caption = "PaintingInspection", captionI18NKey = "PaintingInspection.view.caption", iconPath = "images/icon/text-blob.png", groupName = "Quality", order = 6)
@SpringView(name = "PaintingInspection", ui = CameronUI.class)
public class PaintingInspectionView extends BaseView implements Button.ClickListener, IFilterableView {


    /**
     *
     */
    private static final long serialVersionUID = 6984362572446773590L;
    ComboBox<PaintingInformation> cbOrderNo = new ComboBox<PaintingInformation>();//
    @I18Support(caption = "Result", captionKey = "PaintingInspection.Result")
    ComboBox<PaintingInspectionResult> cbResult = new ComboBox<PaintingInspectionResult>();//
    GridLayout glLayoutText = new GridLayout(5, 9);
    String paintSpecificationDesc = "";
    @I18Support(caption = "Save", captionKey = "PaintingInformation.Save")
    private Button btnSave = new Button();
    @I18Support(caption = "Primer", captionKey = "PaintingInformation.Primer")
    private LabelWithSamleLineCaption tfPrimer = new LabelWithSamleLineCaption();//底漆
    @I18Support(caption = "PrimerAirTemp", captionKey = "PaintingInformation.PrimerAirTemp")
    private TextField tfPrimerAirTemp = new TextField();//底漆空气温度
    @I18Support(caption = "PrimerSurfaceTemp", captionKey = "PaintingInformation.PrimerSurfaceTemp")
    private TextField tfPrimerSurfaceTemp = new TextField();//底漆表面温度
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
    //    private DateTimeField dtPrimerDryTime = new DateTimeField();
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
    //    private DateTimeField dtFinalCoatDryTime = new DateTimeField();
    @I18Support(caption = "FinalCoatDryMethod", captionKey = "PaintingInformation.FinalCoatDryMethod")
    private TextField tfFinalCoatDryMethod = new TextField();//面漆干燥方式
    @I18Support(caption = "FinalCoatDryTime", captionKey = "PaintingInformation.FinalCoatDryTime")
    private TextField tfFinalCoatDryTime = new TextField();//面漆干燥时间
    @I18Support(caption = "Visual", captionKey = "PaintingInformation.Visual")
    private LabelWithSamleLineCaption tfVisual = new LabelWithSamleLineCaption();//外观
    @I18Support(caption = "VisualInspection", captionKey = "PaintingInformation.VisualInspection")
    private TextField tfVisualInspection = new TextField();//外观检查
    @I18Support(caption = "VisualTotalDryFilmThickness", captionKey = "PaintingInformation.VisualTotalDryFilmThickness")
    private TextField tfVisualTotalDryFilmThickness = new TextField();//外观总干膜厚度
    @I18Support(caption = "VisualGageNo", captionKey = "PaintingInformation.VisualGageNo")
    private TextField tfVisualGageNo = new TextField();//量具编号
    @I18Support(caption = "Comments", captionKey = "PaintingInformation.Comments")
    private TextField tfComments = new TextField();//结论
    @I18Support(caption = "Intermediate", captionKey = "PaintingInformation.Intermediate")
    private TextField tfIntermediate = new TextField();//中间层
    private Button[] btns = new Button[]{btnSave};//btnGetInfo, ,btnExport
    private TextField[] textFields = new TextField[]{tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerHumidity, tfPrimerDewPoint, tfPrimerDryFilmThickness,
            tfPrimerDryAirTemp, tfPrimerDryHumidity, tfPrimerDryTime,
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatHumidity, tfFinalCoatDewPoint, tfFinalCoatTotalDryFilmThickness,
            tfFinalCoatDryAirTemp, tfFinalCoatDryHumidity, tfFinalCoatDryTime, tfVisualTotalDryFilmThickness, tfIntermediate};
    private TextField[] textFieldAll = new TextField[]{
            tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerHumidity, tfPrimerDewPoint, tfPrimerPaintApplied,
            tfPrimerDryFilmThickness, tfPrimerDryAirTemp, tfPrimerDryHumidity, tfPrimerDryMethod, tfPrimerDryTime,
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatHumidity, tfFinalCoatDewPoint,
            tfFinalCoatPaintApplied, tfFinalCoatColor, tfFinalCoatTotalDryFilmThickness,
            tfFinalCoatDryAirTemp, tfFinalCoatDryHumidity, tfFinalCoatDryMethod, tfFinalCoatDryTime,
            tfVisualInspection, tfVisualTotalDryFilmThickness, tfVisualGageNo, tfComments, tfIntermediate};
    private TextField[] opInputInfo = new TextField[]{
            tfPrimerAirTemp, tfPrimerSurfaceTemp, tfPrimerHumidity, tfPrimerDewPoint, tfPrimerPaintApplied,
            tfPrimerDryFilmThickness, tfPrimerDryAirTemp, tfPrimerDryHumidity, tfPrimerDryMethod, tfPrimerDryTime,
            tfFinalCoatAirTemp, tfFinalCoatSurfaceTemp, tfFinalCoatHumidity, tfFinalCoatDewPoint,
            tfFinalCoatPaintApplied, tfFinalCoatColor, tfFinalCoatTotalDryFilmThickness,
            tfFinalCoatDryAirTemp, tfFinalCoatDryHumidity, tfFinalCoatDryMethod, tfFinalCoatDryTime,
    };
    private TextField[] textFieldCheck = new TextField[]{
            tfPrimerHumidity, tfPrimerDewPoint, tfPrimerDryFilmThickness,
            tfPrimerDryHumidity,
            tfFinalCoatHumidity, tfFinalCoatDewPoint, tfFinalCoatTotalDryFilmThickness,
            tfFinalCoatDryHumidity,
            tfIntermediate
    };
    private HorizontalLayout hlToolBox = new HorizontalLayout();
    private HorizontalLayout hlToolRes = new HorizontalLayout();
    private ProductionOrder order;
    private SparePart sparePart;
    private ProductInformation productInformation;
    private List<ProductRouting> productRoutingList;
    private PaintingSpecification paintingSpecification;
    private boolean qualified = true;
    private String orderNo = "";
    private String paintingType = "";
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IOperationInstructionService operationInstructionService;
    @Autowired
    private ISparePartService sparePartService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private IPaintingInformationService paintingInformationService;
    @Autowired
    private IProductRoutingService productRoutingService;
    @Autowired
    private IPaintingSpecificationService paintingSpecificationService;
    @Autowired
    private IPaintingInspectionService paintingInspectionService;
    @Autowired
    private ICaMediaService mediaService;
    @Autowired
    private ICaConfigService caConfigService;
    private List<PaintingInformation> paintingInformationList;
    private PaintingInformation paintInformation;

    public PaintingInspectionView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        vlRoot.addComponent(hlToolBox);
        HorizontalLayout hlTempToolBox = new HorizontalLayout();
        hlToolBox.addComponent(hlTempToolBox);
        cbOrderNo.setPlaceholder(I18NUtility.getValue("PaintingInformation.productOrderId", "productOrderId"));

        cbOrderNo.setItemCaptionGenerator(new ItemCaptionGenerator<PaintingInformation>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(PaintingInformation item) {
                return I18NUtility.getValue(item.getWorkOrderSN(), item.getWorkOrderSN());
            }
        });
        cbOrderNo.addValueChangeListener(new ValueChangeListener<PaintingInformation>() {

            private static final long serialVersionUID = 210275423830056456L;

            @Override
            public void valueChange(ValueChangeEvent<PaintingInformation> event) {
                Optional<PaintingInformation> optional = cbOrderNo.getSelectedItem();
                initTextField();
                cbResult.clear();
                qualified = true;
                if (optional.isPresent()) {
                    PaintingInformation paintingInformation = optional.get();
                    String productOrderId = paintingInformation.getWorkOrderSN();
                    if (!Strings.isNullOrEmpty(productOrderId)) {
                        orderNo = productOrderId;
                        order = productionOrderService.getByNo(productOrderId);
                        if (order != null) {
                            //判断该订单是否锁定
                            if(order.getBomLockFlag()){
                                throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked",
                                        "This Order is locked,Please contact the engineer to solve !"));
                            }
                            paintSpecificationDesc = order.getPaintSpecification();
                            if (paintSpecificationDesc == null) {
                                return;
                            }
                            paintingSpecification = getPaintRuler(paintSpecificationDesc);
                            if (paintingSpecification == null) {
                                return;
                            }
//                            if (paintingSpecification != null) {
//                                tfPrimerDryFilmThickness.setValue(paintingSpecification.getPrimer());
//                                float[] thickness = sumPaintThinckness(paintingSpecification);
//                                tfFinalCoatTotalDryFilmThickness.setValue(thickness[0] + "-" + thickness[1]);
//                            }
                            //获取喷漆信息
                            List<PaintingInformation> paintingInformationList = paintingInformationService.getBySn(productOrderId);
                            if (paintingInformationList != null && paintingInformationList.size() > 0) {// && !Strings.isNullOrEmpty(paintingNo)
                                paintInformation = paintingInformationList.get(0);
                                setValueTofields(paintInformation);
                            } else {
                                NotificationUtils.notificationError("此工单号没有喷漆检测信息！");
                            }
                        } else {
                            NotificationUtils.notificationError("工单号不存在");
                        }
                    }
                }
            }

        });

//		hlTempToolBox.addComponent(tfproductOrderId);
        hlTempToolBox.addComponent(cbOrderNo);
        for (Button btn : btns) {
            hlTempToolBox.addComponent(btn);
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnSave.setIcon(VaadinIcons.PACKAGE);


        hlToolRes.setWidth("100%");
        hlToolRes.addStyleName(CoreTheme.TOOLBOX);
        hlToolRes.setMargin(true);
        vlRoot.addComponent(hlToolRes);
        HorizontalLayout hlTempToolRes = new HorizontalLayout();
        hlToolRes.addComponent(hlTempToolRes);
        cbResult.setItems(PaintingInspectionResult.values());
        cbResult.setItemCaptionGenerator(new ItemCaptionGenerator<PaintingInspectionResult>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(PaintingInspectionResult item) {
                return I18NUtility.getValue(item.getKey(), item.getResult());
            }
        });
        hlTempToolRes.addComponent(cbResult);
        cbResult.addValueChangeListener(new ValueChangeListener<PaintingInspectionResult>() {

            private static final long serialVersionUID = 210275423830056456L;

            @Override
            public void valueChange(ValueChangeEvent<PaintingInspectionResult> event) {
                Optional<PaintingInspectionResult> optional = cbResult.getSelectedItem();
                if (optional.isPresent()) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }

        });

        Panel panelText = new Panel();
        panelText.setWidth("100%");
//        panelText.setHeightUndefined();
        panelText.setSizeFull();

        //inputdisplayinline
        glLayoutText.setSpacing(true);
        glLayoutText.setMargin(true);
        glLayoutText.setWidth("100%");
        glLayoutText.setHeightUndefined();

        //第二行
        glLayoutText.addComponent(this.tfPrimer, 0, 1);
        glLayoutText.addComponent(this.tfPrimerAirTemp, 1, 1);
        glLayoutText.addComponent(this.tfPrimerSurfaceTemp, 2, 1);
        glLayoutText.addComponent(this.tfPrimerHumidity, 3, 1);
        glLayoutText.addComponent(this.tfPrimerDewPoint, 4, 1);

        glLayoutText.addComponent(this.tfPrimerPaintApplied, 1, 2);
        glLayoutText.addComponent(this.tfPrimerDryFilmThickness, 2, 2);


        //第三行
        glLayoutText.addComponent(this.tfPrimerDry, 0, 3);
        glLayoutText.addComponent(this.tfPrimerDryAirTemp, 1, 3);
        glLayoutText.addComponent(this.tfPrimerDryHumidity, 2, 3);
        glLayoutText.addComponent(this.tfPrimerDryMethod, 3, 3);
        glLayoutText.addComponent(this.tfPrimerDryTime, 4, 3);
        //第四行
        glLayoutText.addComponent(this.tfFinalCoat, 0, 4);
        glLayoutText.addComponent(this.tfFinalCoatAirTemp, 1, 4);
        glLayoutText.addComponent(this.tfFinalCoatSurfaceTemp, 2, 4);
        glLayoutText.addComponent(this.tfFinalCoatHumidity, 3, 4);
        glLayoutText.addComponent(this.tfFinalCoatDewPoint, 4, 4);

        glLayoutText.addComponent(this.tfFinalCoatPaintApplied, 1, 5);
        glLayoutText.addComponent(this.tfFinalCoatColor, 2, 5);
        glLayoutText.addComponent(this.tfFinalCoatTotalDryFilmThickness, 3, 5);
        //第五行
        glLayoutText.addComponent(this.tfFinalCoatDry, 0, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryAirTemp, 1, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryHumidity, 2, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryMethod, 3, 6);
        glLayoutText.addComponent(this.tfFinalCoatDryTime, 4, 6);
        //第六行
        glLayoutText.addComponent(this.tfVisual, 0, 7);
        glLayoutText.addComponent(this.tfVisualInspection, 1, 7);
        glLayoutText.addComponent(this.tfVisualTotalDryFilmThickness, 2, 7);
        glLayoutText.addComponent(this.tfVisualGageNo, 3, 7);
        glLayoutText.addComponent(this.tfComments, 4, 7);

//        glLayoutText.addComponent(this.tfIntermediate, 1, 8);

        for (TextField textField : textFieldCheck) {
            textField.addValueChangeListener(new ValueChangeListener<String>() {

                private static final long serialVersionUID = 210275423830056456L;

                @Override
                public void valueChange(ValueChangeEvent<String> event) {
                    TextField textField = (TextField) event.getSource();
                    String textValue = textField.getValue().trim();
                    if (textValue.equals("/")) {
                        return;
                    }
                    if (tfPrimerDryFilmThickness.equals(textField)) {
//						String primerDown = "";
//						String primerUp = "";
//
//						String primer = paintingSpecification.getPrimer();
//						if(!Strings.isNullOrEmpty(primer)) {
//							String[] arrPrimer = primer.split("-");
//							primerDown = arrPrimer[0];
//							primerUp = arrPrimer[1];
//							if(!Strings.isNullOrEmpty(primerUp) && !Strings.isNullOrEmpty(primerDown) && !Strings.isNullOrEmpty(textValue)) {
//								float textValueFloat = Float.parseFloat(textValue);
//								float primerUpFloat = Float.parseFloat(primerUp);
//								float primerDownFloat = Float.parseFloat(primerDown);
//								if(textValueFloat>=primerUpFloat || textValueFloat<=primerDownFloat) {
//									qualified = false;
//									textField.addStyleName(CoreTheme.BACKGROUND_RED);
//								}else {
//									textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
//								}
//							}
//						}
                    } else if (tfIntermediate.equals(textField)) {
                        String intermediateDown = "";
                        String intermediateUp = "";

                        String intermediate = paintingSpecification.getIntermediate();
                        if (!Strings.isNullOrEmpty(intermediate) && "OUTSOURCING".equals(paintingType)) {
                            String[] arrIntermediate = intermediate.split("-");
                            intermediateDown = arrIntermediate[0];
                            intermediateUp = arrIntermediate[1];
                            if (!Strings.isNullOrEmpty(intermediateDown) && !Strings.isNullOrEmpty(intermediateUp) && !Strings.isNullOrEmpty(textValue)) {
                                float textValueFloat = Float.parseFloat(textValue);
                                float intermediateUpFloat = Float.parseFloat(intermediateUp);
                                float intermediateDownFloat = Float.parseFloat(intermediateDown);
                                if (textValueFloat >= intermediateUpFloat || textValueFloat <= intermediateDownFloat) {
                                    qualified = false;
                                    textField.addStyleName(CoreTheme.BACKGROUND_RED);
                                } else {
                                    textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                                }
                            }
                        }
                    } else if (tfFinalCoatTotalDryFilmThickness.equals(textField)) {
//						String finalsDown = "";
//						String finalsUp = "";
//
//						String finals = paintingSpecification.getFinals();
//						if(!Strings.isNullOrEmpty(finals)) {
//							String[] arrFinals = finals.split("-");
//							finalsDown = arrFinals[0];
//							finalsUp = arrFinals[1];
//							if(!Strings.isNullOrEmpty(finalsUp) && !Strings.isNullOrEmpty(finalsDown) && !Strings.isNullOrEmpty(textValue)) {
//								float textValueFloat = Float.parseFloat(textValue);
//								float finalsUpFloat = Float.parseFloat(finalsUp);
//								float finalsDownFloat = Float.parseFloat(finalsDown);
//								if(textValueFloat>=finalsUpFloat || textValueFloat<=finalsDownFloat) {
//									qualified = false;
//									textField.addStyleName(CoreTheme.BACKGROUND_RED);
//								}else {
//									textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
//								}
//							}
//						}
                    } else if (tfPrimerHumidity.equals(textField) ||
                            tfPrimerDryHumidity.equals(textField) ||
                            tfFinalCoatHumidity.equals(textField) ||
                            tfFinalCoatDryHumidity.equals(textField)) {
                        String humidity = paintingSpecification.getHumidity();
                        if (!Strings.isNullOrEmpty(humidity) && !Strings.isNullOrEmpty(textValue)) {
                            float textValueFloat = Float.parseFloat(textValue);
                            float humidityFloat = Float.parseFloat(humidity) * 100;
                            if (textValueFloat > humidityFloat) {
                                qualified = false;
                                textField.addStyleName(CoreTheme.BACKGROUND_RED);
                            } else {
                                textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                            }
                        }

                    } else if (tfPrimerDewPoint.equals(textField)) {
                        String aboveDewPoint = paintingSpecification.getAboveDewPoint();
                        String primerSurfaceTemp = tfPrimerSurfaceTemp.getValue().trim();//表面温度
                        if (!Strings.isNullOrEmpty(aboveDewPoint) && !Strings.isNullOrEmpty(textValue) && !Strings.isNullOrEmpty(primerSurfaceTemp)) {
                            float textValueFloat = Float.parseFloat(textValue);
                            float aboveDewPointFloat = Float.parseFloat(aboveDewPoint);
                            float primerSurfaceTempFloat = Float.parseFloat(primerSurfaceTemp);
                            if (primerSurfaceTempFloat <= (aboveDewPointFloat + textValueFloat)) {
                                qualified = false;
                                textField.addStyleName(CoreTheme.BACKGROUND_RED);
                            } else {
                                textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                            }
                        }
                    } else if (tfFinalCoatDewPoint.equals(textField)) {
                        String aboveDewPoint = paintingSpecification.getAboveDewPoint();
                        String finalCoatSurfaceTemp = tfFinalCoatSurfaceTemp.getValue().trim();
                        if (!Strings.isNullOrEmpty(aboveDewPoint) && !Strings.isNullOrEmpty(textValue) && !Strings.isNullOrEmpty(finalCoatSurfaceTemp)) {
                            float textValueFloat = Float.parseFloat(textValue);
                            float aboveDewPointFloat = Float.parseFloat(aboveDewPoint);
                            float finalCoatSurfaceTempFloat = Float.parseFloat(finalCoatSurfaceTemp);
                            if (finalCoatSurfaceTempFloat <= (aboveDewPointFloat + textValueFloat)) {
                                qualified = false;
                                textField.addStyleName(CoreTheme.BACKGROUND_RED);
                            } else {
                                textField.addStyleName(CoreTheme.BACKGROUND_GREEN);
                            }
                        }
                    }
                    if (!qualified) {
                        cbResult.setSelectedItem(PaintingInspectionResult.NG);
                    } else {
                        cbResult.setSelectedItem(PaintingInspectionResult.OK);
                    }
                }

            });
        }

        panelText.setContent(glLayoutText);
        vlRoot.addComponent(panelText);
        vlRoot.setExpandRatio(panelText, 0.1f);


        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    public static String getLetter(String a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if ((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getPaintNo(String a) {
        int i = 0;
        for (; i < a.length(); i++) {
            char c = a.charAt(i);
            if ((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A')) {
                break;
            }
        }
        int index = a.indexOf("(");
        int index1 = a.indexOf(")");
        if (index == -1) {
            return a.substring(i, a.length());
        } else {
            if (i < index) {
                return a.substring(i, index);
            } else if (i > index1) {
                return a.substring(i, a.length());
            } else {
                return "";
            }
        }

    }

    public static String[] getChineseCharacter(String a) {
        if ((!(a.length() == a.getBytes().length))) {
            String reg1 = "[^\u4e00-\u9fa5]";
            String str = a.replaceAll(reg1, "");
            return new String[]{"H", str};
        } else {
            return new String[]{"L", getLetter(a)};
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        String productOrderId = "";
        Optional<PaintingInformation> optional = cbOrderNo.getSelectedItem();
        if (optional.isPresent()) {
            PaintingInformation paintingInformation = optional.get();
            productOrderId = paintingInformation.getWorkOrderSN();
        }
        if (!cbResult.getSelectedItem().isPresent()) {
            NotificationUtils.notificationError("请选择检验结果");
            cbResult.focus();
            return;
        }
        if (btnSave.equals(button)) {
            if (!"".equals(productOrderId) && productionOrderService.getByNo(productOrderId) != null) {
                if (productOrderId.equals(orderNo)) {
                    PaintingInspection paintingInspectionSaved = paintingInspectionService.getByNo(orderNo);
                    if (paintingInspectionSaved == null) {
                        Optional<PaintingInspectionResult> openter = cbResult.getSelectedItem();
                        if (openter.isPresent()) {
                            //检查导出报告的路径是否正确
                            String path = "";
                            CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
                            if (caConfig == null) {
                                NotificationUtils.notificationError("导出报告路径没有配置，请到系统参数界面进行配置！");
                                return;
                            }
                            path = caConfig.getConfigValue();
                            //是否有电子签名
                            Media iqaImage = null;
                            iqaImage = mediaService.getByTypeName(ElectronicSignatureLoGoType.ELECTRONICSIGNATURE.getKey(),
                                    RequestInfo.current().getUserName());
                            if (iqaImage == null) {
                                NotificationUtils.notificationError("当前登录用户没有维护电子签名，请首先维护电子签名");
                                return;
                            }
                            PaintingInspectionResult paintingInspectionResult = openter.get();
                            String key = paintingInspectionResult.getKey();
                            //喷漆检测
                            paintInformation.setVisualInspection(tfVisualInspection.getValue().trim());
                            paintInformation.setVisualTotalDryFilmThickness(Strings.isNullOrEmpty(tfVisualTotalDryFilmThickness.getValue().trim()) ? "/" : tfVisualTotalDryFilmThickness.getValue().trim());
                            paintInformation.setVisualGageNo(tfVisualGageNo.getValue().trim());
                            paintInformation.setComments(tfComments.getValue().trim());
                            paintInformation.setIntermediate(0.0F);
                            paintInformation.setQcConfirmer(RequestInfo.current().getUserName());
                            paintingInformationService.save(paintInformation);
                            PaintingInspection paintingInspection = new PaintingInspection();
                            paintingInspection.setWorkOrderSN(orderNo);
                            paintingInspection.setResult(key);
                            paintingInspectionService.save(paintingInspection);
                            //生成报告文件
                            try {
                                createReport(path, iqaImage, orderNo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            initTextField();
                            cbOrderNo.setSelectedItem(null);
                            List<PaintingInformation> list = paintingInformationService.getBySn("");
                            if (list != null && paintingInformationService.getBySn("").size() > 0) {
                                cbOrderNo.setItems(list);
                            } else {
                                cbOrderNo.setItems(new ArrayList<PaintingInformation>());
                            }
                        }
                    } else {
                        NotificationUtils.notificationError("工单：" + orderNo + "的检测信息已保存！");
                    }
                } else {
                    NotificationUtils.notificationError("文本框中待输入的工单号没有进行检测，请点击 Enter 键进行喷漆信息检验！");
                }
            } else {
                NotificationUtils.notificationError("请输入有效的工单号！");
            }
        }
    }

    public void initTextField() {
        for (TextField textField : textFieldAll) {
            textField.clear();
            if (!Strings.isNullOrEmpty(textField.getStyleName())) {
                textField.removeStyleName(textField.getStyleName());
            }
        }
    }

    public void setValueTofields(PaintingInformation paintingInformation) {
        if (paintingInformation != null) {
            tfPrimerAirTemp.setValue(paintingInformation.getPrimerAirTemp() == null ? "/" : Float.toString(paintingInformation.getPrimerAirTemp()));
            tfPrimerSurfaceTemp.setValue(paintingInformation.getPrimerSurfaceTemp() == null ? "/" : Float.toString(paintingInformation.getPrimerSurfaceTemp()));
            tfPrimerHumidity.setValue(paintingInformation.getPrimerHumidity() == null ? "/" : Float.toString(paintingInformation.getPrimerHumidity()));
            tfPrimerDewPoint.setValue(paintingInformation.getPrimerDewPoint() == null ? "/" : Float.toString(paintingInformation.getPrimerDewPoint()));
            tfPrimerPaintApplied.setValue(paintingInformation.getPrimerPaintApplied());
            tfPrimerDryFilmThickness.setValue(paintingInformation.getPrimerDryFilmThickness() == null ? "" : paintingInformation.getPrimerDryFilmThickness());
            tfPrimerDryAirTemp.setValue(paintingInformation.getPrimerDryAirTemp() == null ? "/" : Float.toString(paintingInformation.getPrimerDryAirTemp()));
            tfPrimerDryHumidity.setValue(paintingInformation.getPrimerDryHumidity() == null ? "/" : Float.toString(paintingInformation.getPrimerDryHumidity()));
            tfPrimerDryMethod.setValue(paintingInformation.getPrimerDryMethod());
            tfPrimerDryTime.setValue(paintingInformation.getPrimerDryTime() == null ? "/" : Float.toString(paintingInformation.getPrimerDryTime()));
            tfFinalCoatAirTemp.setValue(paintingInformation.getFinalCoatAirTemp() == null ? "/" : Float.toString(paintingInformation.getFinalCoatAirTemp()));
            tfFinalCoatSurfaceTemp.setValue(paintingInformation.getFinalCoatSurfaceTemp() == null ? "/" : Float.toString(paintingInformation.getFinalCoatSurfaceTemp()));
            tfFinalCoatHumidity.setValue(paintingInformation.getFinalCoatHumidity() == null ? "/" : Float.toString(paintingInformation.getFinalCoatHumidity()));
            tfFinalCoatDewPoint.setValue(paintingInformation.getFinalCoatDewPoint() == null ? "/" : Float.toString(paintingInformation.getFinalCoatDewPoint()));
            tfFinalCoatPaintApplied.setValue(paintingInformation.getFinalCoatPaintApplied());
            tfFinalCoatColor.setValue(paintingInformation.getFinalCoatColor());
            tfFinalCoatTotalDryFilmThickness.setValue(paintingInformation.getFinalCoatTotalDryFilmThickness() == null ? "" : paintingInformation.getFinalCoatTotalDryFilmThickness());
            tfFinalCoatDryAirTemp.setValue(paintingInformation.getFinalCoatDryAirTemp() == null ? "/" : Float.toString(paintingInformation.getFinalCoatDryAirTemp()));
            tfFinalCoatDryHumidity.setValue(paintingInformation.getFinalCoatDryHumidity() == null ? "/" : Float.toString(paintingInformation.getFinalCoatDryHumidity()));
            tfFinalCoatDryMethod.setValue(paintingInformation.getFinalCoatDryMethod());
            tfFinalCoatDryTime.setValue(paintingInformation.getFinalCoatDryTime() == null ? "/" : Float.toString(paintingInformation.getFinalCoatDryTime()));

        }
    }

    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public boolean valueIsNumber(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            boolean isNumber = RegExpValidatorUtils.isIsPositive(value);
            if (!isNumber) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateAfterFilterApply() {

    }

    @Override
    protected void init() {
        btnSave.setEnabled(false);
        for (TextField textField : opInputInfo) {
            textField.setReadOnly(true);
        }
        List<PaintingInformation> list = paintingInformationService.getBySn("");
        if (list != null && paintingInformationService.getBySn("").size() > 0) {
            cbOrderNo.setItems(list);
        } else {
            cbOrderNo.setItems(new ArrayList<PaintingInformation>());
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        btnSave.setEnabled(false);
        for (TextField textField : opInputInfo) {
            textField.setReadOnly(true);
        }
        List<PaintingInformation> list = paintingInformationService.getBySn("");
        if (list != null && paintingInformationService.getBySn("").size() > 0) {
            cbOrderNo.setItems(list);
        } else {
            cbOrderNo.setItems(new ArrayList<PaintingInformation>());
        }
    }

    public void createReport(String path, Media media, String orderNo) throws Exception {//sparePart productInformation

        ProductionOrder order = productionOrderService.getByNo(orderNo);
        String partNo = order.getProductId();
        String partRev = order.getProductVersionId();
        int quantity = order.getProductNumber();
        String paintSpecification = order.getPaintSpecification();//喷漆信息
        PaintingSpecification paintingSpecification = paintingSpecificationService.getBySpecificationFile(paintSpecification);

        SparePart part = sparePartService.getByNoRev(partNo, partRev);

        PaintingInformation paintInfo = paintingInformationService.getByOrderNo(orderNo);
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("partNOAndRev", partNo + " REV" + partRev);
        params.put("partNODesc", order.getProductDesc());
        params.put("productOrderId", order.getProductOrderId() + "0001" + (quantity > 1 ? "~" + String.format("%04d", quantity) : ""));
        params.put("qualityPlanStandard", part.getQaPlan());
        params.put("qPRev", part.getQaPlanRev());
        params.put("paintingSpecification", paintSpecification);
        params.put("pSRev", operationInstructionService.getByInspectionName("WI-CSI-E-14").getInstructionRev());
        params.put("primerAirTemp", paintInfo.getPrimerAirTemp() == null ? "/" : paintInfo.getPrimerAirTemp());
        params.put("pSTemp", paintInfo.getPrimerSurfaceTemp() == null ? "/" : paintInfo.getPrimerSurfaceTemp());
        params.put("primerHumidity", paintInfo.getPrimerHumidity() == null ? "/" : paintInfo.getPrimerHumidity());
        params.put("primerDewPoint", paintInfo.getPrimerDewPoint() == null ? "/" : paintInfo.getPrimerDewPoint());
        params.put("primerPaintApplied", paintInfo.getPrimerPaintApplied() == null ? "/" : paintInfo.getPrimerPaintApplied());
        params.put("pDF", paintInfo.getPrimerPaintApplied().equals("/") ? "/" : paintingSpecification.getPrimer());
        params.put("pDryAirTemp", paintInfo.getPrimerDryAirTemp() == null ? "/" : paintInfo.getPrimerDryAirTemp());
        params.put("primerDryHumidity", paintInfo.getPrimerDryHumidity() == null ? "/" : paintInfo.getPrimerDryHumidity());
        params.put("primerDryMethod", paintInfo.getPrimerDryMethod() == null ? "/" : paintInfo.getPrimerDryMethod());
        params.put("primerDryTime", paintInfo.getPrimerDryTime() == null ? "/" : paintInfo.getPrimerDryTime());
        params.put("fCoatAirTemp", paintInfo.getFinalCoatAirTemp() == null ? "/" : paintInfo.getFinalCoatAirTemp());
        params.put("fCSTemp", paintInfo.getFinalCoatSurfaceTemp() == null ? "/" : paintInfo.getFinalCoatSurfaceTemp());
        params.put("finalCoatHumidity", paintInfo.getFinalCoatHumidity() == null ? "/" : paintInfo.getFinalCoatHumidity());
        params.put("finalCoatDewPoint", paintInfo.getFinalCoatDewPoint() == null ? "/" : paintInfo.getFinalCoatDewPoint());
        params.put("finalCoatPaintApplied", paintInfo.getFinalCoatPaintApplied() == null ? "/" : paintInfo.getFinalCoatPaintApplied());
        params.put("finalCoatColor", paintInfo.getFinalCoatColor() == null ? "/" : paintInfo.getFinalCoatColor());
        float[] thickness = sumPaintThinckness(paintingSpecification);
        params.put("TDFT", paintInfo.getFinalCoatTotalDryFilmThickness().equals("/") ? "/" : paintInfo.getFinalCoatTotalDryFilmThickness());
        params.put("fCDryAirTemp", paintInfo.getFinalCoatDryAirTemp() == null ? "/" : paintInfo.getFinalCoatDryAirTemp());
        params.put("finalCoatDryHumidity", paintInfo.getFinalCoatDryHumidity() == null ? "/" : paintInfo.getFinalCoatDryHumidity());
        params.put("finalCoatDryMethod", paintInfo.getFinalCoatDryMethod() == null ? "/" : paintInfo.getFinalCoatDryMethod());
        params.put("finalCoatDryTime", paintInfo.getFinalCoatDryTime() == null ? "/" : paintInfo.getFinalCoatDryTime());
        params.put("visualInspection", paintInfo.getVisualInspection() == null ? "/" : paintInfo.getVisualInspection());
        params.put("visualTotalDryFilmThickness", paintInfo.getVisualTotalDryFilmThickness() == null ? "/" : paintInfo.getVisualTotalDryFilmThickness());
        params.put("visualGageNo", paintInfo.getVisualGageNo() == null ? "/" : paintInfo.getVisualGageNo());
        params.put("comments", paintInfo.getComments() == null ? "/" : paintInfo.getComments());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        params.put("AndDate", "/" + sdf.format(new Date()));

        BASE64Encoder encoder = new BASE64Encoder();
        String opUserName = paintInfo.getOpUser();
        Media opImage = null;
        opImage = mediaService.getByTypeName("ES", opUserName);
        params.put("operator", encoder.encode(inputStream2byte(opImage.getMediaStream())));
        params.put("qcinspector", encoder.encode(inputStream2byte(media.getMediaStream())));
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        configuration.setDirectoryForTemplateLoading(new File(AppConstant.DOC_XML_FILE_PATH));

        Template template = configuration.getTemplate("paintingInformation.xml", "utf-8");

        // 输出文件
        File outFile = new File(
                path + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT + orderNo + ".doc");// C:\\Users\\bruce_yang\\Desktop\\喀麦隆文件\\生成的word.doc

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")),
                1024 * 1024);
        template.process(params, out);
        out.flush();
        out.close();
        final Document document = new Document(path + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT + orderNo + ".doc");
        final String overFile = path + AppConstant.PRODUCTION_PREFIX + AppConstant.PAINT_REPORT + orderNo + ".pdf";
        document.saveToFile(overFile, FileFormat.PDF);
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
        PaintingSpecification specification = paintingSpecificationService.getBySpecificationFile(routingDesc);
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
