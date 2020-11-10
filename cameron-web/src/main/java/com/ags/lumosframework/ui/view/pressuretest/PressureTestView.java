package com.ags.lumosframework.ui.view.pressuretest;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.enums.PressureTypeEnum;
import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.domain.User;
import com.ags.lumosframework.sdk.service.api.IUserService;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.*;

/**
 * @author peyton
 * @date 2019/9/12 13:36
 */
@Menu(caption = "PressureTestView", captionI18NKey = "Cameron.PressureTestView", iconPath = "images/icon/text-blob.png", groupName = "Production", order = 2)
@SpringView(name = "PressureTestView", ui = CameronUI.class)
public class PressureTestView extends BaseView implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    private static final int wdFormatPDF = 17;
    private TextField tfProductSn = new TextField();
    private ComboBox<String> cbPressureType = new ComboBox<>();//测压类型：井口/阀门
    @I18Support(caption = "ConfirmSn", captionKey = "PressureTest.ConfirmSn")
    private Button btnConfirmSn = new Button();

    //    @I18Support(caption = "GetDates", captionKey = "PressureTest.GetDates")
//    private final Button btnGetDates = new Button();
    @I18Support(caption = "CreateReport", captionKey = "PressureTest.CreateReport")
    private Button btnCreateReport = new Button();
    private final Button[] btns = new Button[]{btnConfirmSn, btnCreateReport};


    private final HorizontalLayout hlToolBox = new HorizontalLayout();
    private final TabSheet tabSheet = new TabSheet();
    private final VerticalLayout vlFirst = new VerticalLayout();
    private final VerticalLayout vlSecond = new VerticalLayout();
    private final VerticalLayout vlThird = new VerticalLayout();
    private final VerticalLayout vlFourth = new VerticalLayout();
    //*********************************************************************
    //FM-title
    @I18Support(caption = "PartNo", captionKey = "PressureTest.PartNo")
    private final LabelWithSamleLineCaption labPartNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "PartRev", captionKey = "PressureTest.PartRev")
    private final LabelWithSamleLineCaption labPartRev = new LabelWithSamleLineCaption();
    @I18Support(caption = "WorkOrder", captionKey = "PressureTest.WorkOrder")
    private final LabelWithSamleLineCaption labWorkOrder = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "PressureTest.Description")
    private final LabelWithSamleLineCaption labDescription = new LabelWithSamleLineCaption();
    @I18Support(caption = "TempRating", captionKey = "PressureTest.TempRating")
    private final LabelWithSamleLineCaption labTempRating = new LabelWithSamleLineCaption();
    @I18Support(caption = "MaterialClass", captionKey = "PressureTest.MaterialClass")
    private final LabelWithSamleLineCaption labMaterialClass = new LabelWithSamleLineCaption();
    @I18Support(caption = "PslLevel", captionKey = "PressureTest.PslLevel")
    private final LabelWithSamleLineCaption labPslLevel = new LabelWithSamleLineCaption();
    @I18Support(caption = "SerialNo", captionKey = "PressureTest.SerialNo")
    private final LabelWithSamleLineCaption labSerialNo = new LabelWithSamleLineCaption();
    @I18Support(caption = "TestProcedure", captionKey = "PressureTest.TestProcedure")
    private final LabelWithSamleLineCaption labTestProcedure = new LabelWithSamleLineCaption();
    private final TextField tfGageNoSize = new TextField();//通径规编号尺寸
    private final TextField tfProcedureNo = new TextField();//压力传感器编号
    private final TextField tfTorqueSensor = new TextField();//扭矩传感器编号
    private final LabelWithSamleLineCaption[] labFMs = new LabelWithSamleLineCaption[]{labPartNo, labPartRev,
            labWorkOrder, labDescription, labTempRating, labMaterialClass, labPslLevel, labSerialNo, labTestProcedure};

    @I18Support(caption = "Hydrostatic", captionKey = "PressureTest.Hydrostatic")
    private final CheckBox cbHydrostatic = new CheckBox();
    @I18Support(caption = "Gas", captionKey = "PressureTest.Gas")
    private final CheckBox cbGas = new CheckBox();
    @I18Support(caption = "Primary", captionKey = "PressureTest.Primary")
    private final Label labPrimary = new Label();
    @I18Support(caption = "Secondary", captionKey = "PressureTest.Secondary")
    private final Label labSecondary = new Label();
    @I18Support(caption = "Tertiary", captionKey = "PressureTest.Tertiary")
    private final Label labTertiary = new Label();
    @I18Support(caption = "Comments", captionKey = "PressureTest.Comments")
    private final Label labComments = new Label();
    //**
    @I18Support(caption = "OpenBody：", captionKey = "PressureTest.OpenBody")
    private final CheckBox ckOpenBody = new CheckBox();
    //    private final Label labOpenBody = new Label();
    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureOpen = new Label();
    private final Label labOpenPone = new Label();
    private final Label labOpenPtwo = new Label();
    private final Label labOpenPthree = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeOpen = new Label();
    private final Label labOpenTone = new Label();
    private final Label labOpenTtwo = new Label();
    private final Label labOpenTthree = new Label();
    private final Label labOpenComments = new Label();
    //**
    @I18Support(caption = "Downstream：", captionKey = "PressureTest.Downstream")
    private final CheckBox ckDownstream = new CheckBox();
    //    private final Label labDownstream = new Label();
    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureDown = new Label();
    private final Label labDownPone = new Label();
    private final Label labDownPtwo = new Label();
    private final Label labDownPthree = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeDown = new Label();
    private final Label labDownTone = new Label();
    private final Label labDownTtwo = new Label();
    private final Label labDownTthree = new Label();
    private final Label labDownComments = new Label();
    //**
    @I18Support(caption = "Upstream：", captionKey = "PressureTest.Upstream")
    private final CheckBox ckUpstream = new CheckBox();
    //    private final Label labUpstream = new Label();
    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureUp = new Label();
    private final Label labUpPone = new Label();
    private final Label labUpPtwo = new Label();
    private final Label labUpPthree = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeUp = new Label();
    private final Label labUpTone = new Label();
    private final Label labUpTtwo = new Label();
    private final Label labUpTthree = new Label();
    private final Label labUpComments = new Label();
    //**
    @I18Support(caption = "BlowDown Test：", captionKey = "PressureTest.BlowDown")
    private final Label labBlowDown = new Label();
    @I18Support(caption = "OpenUnder", captionKey = "PressureTest.OpenUnder")
    private final CheckBox ckOpenUnder = new CheckBox();
    //**
    @I18Support(caption = "Dirft Test：", captionKey = "PressureTest.Dirff")
    private final Label labDirff = new Label();
    @I18Support(caption = "Pass", captionKey = "PressureTest.Pass")
    private final CheckBox cbPass = new CheckBox();
    @I18Support(caption = "Test By：", captionKey = "PressureTest.TestBy")
    private final Label labDirffTestBy = new Label();
    private final ComboBox<User> cbDirffTestBy = new ComboBox<>();
    //**
    @I18Support(caption = "Torque Value(Nm)", captionKey = "PressureTest.TorqueValue")
    private final Label labTorque = new Label();
    private final TextField tfTorqueValue1 = new TextField();
    private final TextField tfTorqueValue2 = new TextField();
    private final TextField tfTorqueValue3 = new TextField();
    private final TextField tfTorqueValue4 = new TextField();
    //**
    @I18Support(caption = "TestBy:", captionKey = "PressureTest.TestBy")
    private final Label labTestBy = new Label();
    private final Label labTestBy0 = new Label();
    @I18Support(caption = "Date:", captionKey = "PressureTest.Date")
    private final Label labDate = new Label();
    private final Label labDate0 = new Label();

    private final Label[] labOpenFMs = new Label[]{labOpenPone, labOpenPtwo, labOpenPthree,
            labOpenTone, labOpenTtwo, labOpenTthree, labOpenComments};
    private final Label[] labDownFMs = new Label[]{labDownPone, labDownPtwo, labDownPthree,
            labDownTone, labDownTtwo, labDownTthree, labDownComments};
    private final Label[] labUpFMs = new Label[]{labUpPone, labUpPtwo, labUpPthree,
            labUpTone, labUpTtwo, labUpTthree, labUpComments};

    private final ComboBox<StationEquipment> cbStationBodyFM = new ComboBox<>();
    private Button btnConfirmBodyFM = new Button();
    private final ComboBox<StationEquipment> cbStationDownFM = new ComboBox<>();
    private Button btnConfirmDownFM = new Button();
    private final ComboBox<StationEquipment> cbStationUpFM = new ComboBox<>();
    private Button btnConfirmUpFM = new Button();
    private final List<ComboBox<StationEquipment>> fmStationCbs = new ArrayList<ComboBox<StationEquipment>>() {
        {
            add(cbStationBodyFM);
            add(cbStationDownFM);
            add(cbStationUpFM);
        }
    };
    private final Button[] fmConfirmBtns = new Button[]{btnConfirmBodyFM, btnConfirmDownFM, btnConfirmUpFM};

    private ComboBox<String> cbRecordBodyFM = new ComboBox<>();
    private Button btnGetBodyFM = new Button();
    private ComboBox<String> cbRecordDownFM = new ComboBox<>();
    private Button btnGetDownFM = new Button();
    private ComboBox<String> cbRecordUpFM = new ComboBox<>();
    private Button btnGetUpFM = new Button();
    private final ComboBox[] fmRecordCbs = new ComboBox[]{cbRecordBodyFM, cbRecordDownFM, cbRecordUpFM};
    private final Button[] fmGetBtns = new Button[]{btnGetBodyFM, btnGetDownFM, btnGetUpFM};

    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveOpenFM = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveDownFM = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveUpFM = new Button();

    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveTorque = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveDirft = new Button();
    private final Button[] fmSaveBtns = new Button[]{btnSaveOpenFM, btnSaveDownFM, btnSaveUpFM, btnSaveTorque, btnSaveDirft};

    //************************************************************************
    //JK-title
    @I18Support(caption = "PartNo", captionKey = "PressureTest.PartNo")
    private final LabelWithSamleLineCaption labPartNoJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "PartRev", captionKey = "PressureTest.PartRev")
    private final LabelWithSamleLineCaption labPartRevJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "SerialNo", captionKey = "PressureTest.SerialNo")
    private final LabelWithSamleLineCaption labSerialNoJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "WorkOrder", captionKey = "PressureTest.WorkOrder")
    private final LabelWithSamleLineCaption labWorkOrderJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "TestProcedure", captionKey = "PressureTest.TestProcedure")
    private final LabelWithSamleLineCaption labTestProcedureJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "Description", captionKey = "PressureTest.Description")
    private final LabelWithSamleLineCaption labDescriptionJK = new LabelWithSamleLineCaption();
    @I18Support(caption = "PslLevel", captionKey = "PressureTest.PslLevel")
    private final LabelWithSamleLineCaption labPslLevelJK = new LabelWithSamleLineCaption();
    private final TextField tfGageNoSizeJK = new TextField();//通径规编号尺寸
    private final TextField tfProcedureNoJK = new TextField();//压力传感器编号
    private final LabelWithSamleLineCaption[] labJks = new LabelWithSamleLineCaption[]{labPartNoJK, labPartRevJK,
            labSerialNoJK, labWorkOrderJK, labTestProcedureJK, labDescriptionJK, labPslLevelJK};

    @I18Support(caption = "Hydrostatic", captionKey = "PressureTest.Hydrostatic")
    private final CheckBox cbHydrostaticJK = new CheckBox();
    @I18Support(caption = "Gas", captionKey = "PressureTest.Gas")
    private final CheckBox cbGasJK = new CheckBox();
    @I18Support(caption = "Primary", captionKey = "PressureTest.Primary")
    private final Label labPrimaryJK = new Label();
    @I18Support(caption = "Secondary", captionKey = "PressureTest.Secondary")
    private final Label labSecondaryJK = new Label();
    @I18Support(caption = "Tertiary", captionKey = "PressureTest.Tertiary")
    private final Label labTertiaryJK = new Label();
    @I18Support(caption = "Tester Signature", captionKey = "PressureTest.Signature")
    private final Label labTesterSignatureJK = new Label();
    @I18Support(caption = "Tester Date", captionKey = "PressureTest.Date")
    private final Label labTesterDateJK = new Label();
    //**
    @I18Support(caption = "Open Body Test：", captionKey = "PressureTest.OpenBody")
    private final CheckBox ckOpenBodyJK = new CheckBox();
    private final Label labOpenBodySignatureJK = new Label();
    private final Label labOpenBodyDateJK = new Label();
    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureOpenJK = new Label();
    private final Label labOpenPoneJK = new Label();
    private final Label labOpenPtwoJK = new Label();
    private final Label labOpenPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeOpenJK = new Label();
    private final Label labOpenToneJK = new Label();
    private final Label labOpenTtwoJK = new Label();
    private final Label labOpenTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsOpenJK = new Label();
    private final Label labOpenRoneJK = new Label();
    private final Label labOpenRtwoJK = new Label();
    private final Label labOpenRthreeJK = new Label();
    //**
    @I18Support(caption = "Crossover Body Test：", captionKey = "PressureTest.CrossoverBody")
    private final CheckBox ckCrossoverBodyJK = new CheckBox();
    private final Label labCrossoverBodySignatureJK = new Label();
    private final Label labCrossoverBodyDateJK = new Label();
    @I18Support(caption = "TOP Pressure(psi)", captionKey = "PressureTest.PressureTop")
    private final Label labPressureCrossoverBodyTOPJK = new Label();
    private final Label labCrossoverBodyTOPPoneJK = new Label();
    private final Label labCrossoverBodyTOPPtwoJK = new Label();
    private final Label labCrossoverBodyTOPPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeCrossoverBodyTOPJK = new Label();
    private final Label labCrossoverBodyTOPToneJK = new Label();
    private final Label labCrossoverBodyTOPTtwoJK = new Label();
    private final Label labCrossoverBodyTOPTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsCrossoverBodyTOPJK = new Label();
    private final Label labCrossoverBodyTOPRoneJK = new Label();
    private final Label labCrossoverBodyTOPRtwoJK = new Label();
    private final Label labCrossoverBodyTOPRthreeJK = new Label();
    @I18Support(caption = "BTM Pressure(psi)", captionKey = "PressureTest.PressureBtm")
    private final Label labPressureCrossoverBodyBTMJK = new Label();
    private final Label labCrossoverBodyBTMPoneJK = new Label();
    private final Label labCrossoverBodyBTMPtwoJK = new Label();
    private final Label labCrossoverBodyBTMPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeCrossoverBodyBTMJK = new Label();
    private final Label labCrossoverBodyBTMToneJK = new Label();
    private final Label labCrossoverBodyBTMTtwoJK = new Label();
    private final Label labCrossoverBodyBTMTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsCrossoverBodyBTMJK = new Label();
    private final Label labCrossoverBodyBTMRoneJK = new Label();
    private final Label labCrossoverBodyBTMRtwoJK = new Label();
    private final Label labCrossoverBodyBTMRthreeJK = new Label();
    //**
    @I18Support(caption = "Christmas Tree Test：", captionKey = "PressureTest.ChristmasTree")
    private final CheckBox ckChristmasTreeJK = new CheckBox();
    private final Label labChristmasTreeSignatureJK = new Label();
    private final Label labChristmasTreeDateJK = new Label();

    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureChristmasTreeJK = new Label();
    private final Label labChristmasTreePoneJK = new Label();
    private final Label labChristmasTreePtwoJK = new Label();
    private final Label labChristmasTreePthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeChristmasTreeJK = new Label();
    private final Label labChristmasTreeToneJK = new Label();
    private final Label labChristmasTreeTtwoJK = new Label();
    private final Label labChristmasTreeTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsChristmasTreeJK = new Label();
    private final Label labChristmasTreeRoneJK = new Label();
    private final Label labChristmasTreeRtwoJK = new Label();
    private final Label labChristmasTreeRthreeJK = new Label();
    //**
    @I18Support(caption = "Wellhead Assembly Test：", captionKey = "PressureTest.WellheadAssembly")
    private final CheckBox ckWellheadAssemblyJK = new CheckBox();
    private final Label labWellheadAssemblySignatureJK = new Label();
    private final Label labWellheadAssemblyDateJK = new Label();
    @I18Support(caption = "Pressure(psi)", captionKey = "PressureTest.Pressure")
    private final Label labPressureWellheadAssemblyJK = new Label();
    private final Label labWellheadAssemblyPoneJK = new Label();
    private final Label labWellheadAssemblyPtwoJK = new Label();
    private final Label labWellheadAssemblyPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeWellheadAssemblyJK = new Label();
    private final Label labWellheadAssemblyToneJK = new Label();
    private final Label labWellheadAssemblyTtwoJK = new Label();
    private final Label labWellheadAssemblyTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsWellheadAssemblyJK = new Label();
    private final Label labWellheadAssemblyRoneJK = new Label();
    private final Label labWellheadAssemblyRtwoJK = new Label();
    private final Label labWellheadAssemblyRthreeJK = new Label();
    //**
    @I18Support(caption = "Crossover Assembly Test：", captionKey = "PressureTest.CrossoverAssembly")
    private final CheckBox ckCrossoverAssemblyJK = new CheckBox();
    private final Label labCrossoverAssemblySignatureJK = new Label();
    private final Label labCrossoverAssemblyDateJK = new Label();
    @I18Support(caption = "TOP Pressure(psi)", captionKey = "PressureTest.PressureTop")
    private final Label labPressureCrossoverAssemblyTOPJK = new Label();
    private final Label labCrossoverAssemblyTOPPoneJK = new Label();
    private final Label labCrossoverAssemblyTOPPtwoJK = new Label();
    private final Label labCrossoverAssemblyTOPPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeCrossoverAssemblyTOPJK = new Label();
    private final Label labCrossoverAssemblyTOPToneJK = new Label();
    private final Label labCrossoverAssemblyTOPTtwoJK = new Label();
    private final Label labCrossoverAssemblyTOPTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsCrossoverAssemblyTOPJK = new Label();
    private final Label labCrossoverAssemblyTOPRoneJK = new Label();
    private final Label labCrossoverAssemblyTOPRtwoJK = new Label();
    private final Label labCrossoverAssemblyTOPRthreeJK = new Label();
    @I18Support(caption = "BTM Pressure(psi)", captionKey = "PressureTest.PressureBtm")
    private final Label labPressureCrossoverAssemblyBTMJK = new Label();
    private final Label labCrossoverAssemblyBTMPoneJK = new Label();
    private final Label labCrossoverAssemblyBTMPtwoJK = new Label();
    private final Label labCrossoverAssemblyBTMPthreeJK = new Label();
    @I18Support(caption = "HoldTime(min)", captionKey = "PressureTest.HoldTime")
    private final Label labHoldTimeCrossoverAssemblyBTMJK = new Label();
    private final Label labCrossoverAssemblyBTMToneJK = new Label();
    private final Label labCrossoverAssemblyBTMTtwoJK = new Label();
    private final Label labCrossoverAssemblyBTMTthreeJK = new Label();
    @I18Support(caption = "Results", captionKey = "PressureTest.Results")
    private final Label labResultsCrossoverAssemblyBTMJK = new Label();
    private final Label labCrossoverAssemblyBTMRoneJK = new Label();
    private final Label labCrossoverAssemblyBTMRtwoJK = new Label();
    private final Label labCrossoverAssemblyBTMRthreeJK = new Label();
    //**
    @I18Support(caption = "Drift Test for Tree:", captionKey = "PressureTest.DriftTest")
    private final Label labDriftTestJK = new Label();
    @I18Support(caption = "Pass", captionKey = "PressureTest.Pass")
    private final CheckBox cbPassJK = new CheckBox();
    @I18Support(caption = "Test By：", captionKey = "PressureTest.TestBy")
    private final Label labDirffTestByJK = new Label();
    private final ComboBox<User> cbDirffTestByJK = new ComboBox<>();

    private final Label[] labSignatureJks = new Label[]{labOpenBodySignatureJK, labOpenBodyDateJK,
            labCrossoverBodySignatureJK, labCrossoverBodyDateJK,
            labChristmasTreeSignatureJK, labChristmasTreeDateJK,
            labWellheadAssemblySignatureJK, labWellheadAssemblyDateJK,
            labCrossoverAssemblySignatureJK, labCrossoverAssemblyDateJK};
    //测试项对应lab
    private final Label[] labOpenJks = new Label[]{labOpenPoneJK, labOpenToneJK, labOpenRoneJK,
            labOpenPtwoJK, labOpenTtwoJK, labOpenRtwoJK,
            labOpenPthreeJK, labOpenTthreeJK, labOpenRthreeJK};
    private final Label[] labCrossBodyTopJks = new Label[]{labCrossoverBodyTOPPoneJK, labCrossoverBodyTOPToneJK, labCrossoverBodyTOPRoneJK,
            labCrossoverBodyTOPPtwoJK, labCrossoverBodyTOPTtwoJK, labCrossoverBodyTOPRtwoJK,
            labCrossoverBodyTOPPthreeJK, labCrossoverBodyTOPTthreeJK, labCrossoverBodyTOPRthreeJK};
    private final Label[] labCrossBodyBtmJks = new Label[]{labCrossoverBodyBTMPoneJK, labCrossoverBodyBTMToneJK, labCrossoverBodyBTMRoneJK,
            labCrossoverBodyBTMPtwoJK, labCrossoverBodyBTMTtwoJK, labCrossoverBodyBTMRtwoJK,
            labCrossoverBodyBTMPthreeJK, labCrossoverBodyBTMTthreeJK, labCrossoverBodyBTMRthreeJK};
    private final Label[] labChrisTreeJks = new Label[]{labChristmasTreePoneJK, labChristmasTreeToneJK, labChristmasTreeRoneJK,
            labChristmasTreePtwoJK, labChristmasTreeTtwoJK, labChristmasTreeRtwoJK,
            labChristmasTreePthreeJK, labChristmasTreeTthreeJK, labChristmasTreeRthreeJK};
    private final Label[] labWellAssemJks = new Label[]{labWellheadAssemblyPoneJK, labWellheadAssemblyToneJK, labWellheadAssemblyRoneJK,
            labWellheadAssemblyPtwoJK, labWellheadAssemblyTtwoJK, labWellheadAssemblyRtwoJK,
            labWellheadAssemblyPthreeJK, labWellheadAssemblyTthreeJK, labWellheadAssemblyRthreeJK};
    private final Label[] labCrossAssemTopJks = new Label[]{labCrossoverAssemblyTOPPoneJK, labCrossoverAssemblyTOPToneJK, labCrossoverAssemblyTOPRoneJK,
            labCrossoverAssemblyTOPPtwoJK, labCrossoverAssemblyTOPTtwoJK, labCrossoverAssemblyTOPRtwoJK,
            labCrossoverAssemblyTOPPthreeJK, labCrossoverAssemblyTOPTthreeJK, labCrossoverAssemblyTOPRthreeJK};
    private final Label[] labCrossAssemBtmJks = new Label[]{labCrossoverAssemblyBTMPoneJK, labCrossoverAssemblyBTMToneJK, labCrossoverAssemblyBTMRoneJK,
            labCrossoverAssemblyBTMPtwoJK, labCrossoverAssemblyBTMTtwoJK, labCrossoverAssemblyBTMRtwoJK,
            labCrossoverAssemblyBTMPthreeJK, labCrossoverAssemblyBTMTthreeJK, labCrossoverAssemblyBTMRthreeJK};

    private ComboBox<StationEquipment> cbStationOpenBodyJK = new ComboBox<>();
    private Button btnConfirmOpenBodyJK = new Button();
    private ComboBox<StationEquipment> cbStationCrossBodyTopJK = new ComboBox<>();
    private Button btnConfirmCrossBodyTopJK = new Button();
    private ComboBox<StationEquipment> cbStationCrossBodyBtmJK = new ComboBox<>();
    private Button btnConfirmCrossBodyBtmJK = new Button();
    private ComboBox<StationEquipment> cbStationChristmasJK = new ComboBox<>();
    private Button btnConfirmChristmasJK = new Button();
    private ComboBox<StationEquipment> cbStationWellheadJK = new ComboBox<>();
    private Button btnConfirmWellheadJK = new Button();
    private ComboBox<StationEquipment> cbStationCrossAssemTopJK = new ComboBox<>();
    private Button btnConfirmCrossAssemTopJK = new Button();
    private ComboBox<StationEquipment> cbStationCrossAssemBtmJK = new ComboBox<>();
    private Button btnConfirmCrossAssemBtmJK = new Button();
    private final List<ComboBox<StationEquipment>> jkStationCbs = new ArrayList<ComboBox<StationEquipment>>() {
        {
            add(cbStationOpenBodyJK);
            add(cbStationCrossBodyTopJK);
            add(cbStationCrossBodyBtmJK);
            add(cbStationChristmasJK);
            add(cbStationWellheadJK);
            add(cbStationCrossAssemTopJK);
            add(cbStationCrossAssemBtmJK);
        }
    };

    private final Button[] jkConfirmBtns = new Button[]{btnConfirmOpenBodyJK, btnConfirmCrossBodyTopJK, btnConfirmCrossBodyBtmJK,
            btnConfirmChristmasJK, btnConfirmWellheadJK, btnConfirmCrossAssemTopJK, btnConfirmCrossAssemBtmJK};

    private ComboBox<String> jkOpenBodyRecord = new ComboBox<>();
    private Button btnGetOpenBodyJK = new Button();
    private ComboBox<String> jkCrossoverBodyTopRecord = new ComboBox<>();
    private Button btnGetCrossBodyTJK = new Button();
    private ComboBox<String> jkCrossoverBodyBtmRecord = new ComboBox<>();
    private Button btnGetCrossBodyBJK = new Button();
    private ComboBox<String> jkChristmasTreeRecord = new ComboBox<>();
    private Button btnGetChristmasJK = new Button();
    private ComboBox<String> jkWellheadAssemblyRecord = new ComboBox<>();
    private Button btnGetWellheadJK = new Button();
    private ComboBox<String> jkCrossoverAssemblyTopRecord = new ComboBox<>();
    private Button btnGetCrossAssemTJK = new Button();
    private ComboBox<String> jkCrossoverAssemblyBtmRecord = new ComboBox<>();
    private Button btnGetCrossAssemBJK = new Button();
    private final ComboBox[] jkRecordCbs = new ComboBox[]{jkOpenBodyRecord, jkCrossoverBodyTopRecord, jkCrossoverBodyBtmRecord,
            jkChristmasTreeRecord, jkWellheadAssemblyRecord, jkCrossoverAssemblyTopRecord, jkCrossoverAssemblyBtmRecord};
    private final Button[] jkGetBtns = new Button[]{btnGetOpenBodyJK, btnGetCrossBodyTJK, btnGetCrossBodyBJK,
            btnGetChristmasJK, btnGetWellheadJK, btnGetCrossAssemTJK, btnGetCrossAssemBJK};

    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveOpenJK = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveCrossBodyJK = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveChristmasJK = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveWellheadJK = new Button();
    @I18Support(caption = "Save", captionKey = "PressureTest.Save")
    private Button btnSaveCrossAssembJK = new Button();

    private final Button[] jkSaveBtns = new Button[]{btnSaveOpenJK, btnSaveCrossBodyJK, btnSaveChristmasJK, btnSaveWellheadJK, btnSaveCrossAssembJK};

    //***阀门表头
    GridLayout glTitleLayout = new GridLayout(3, 5);
    //****井口表头
    GridLayout glTitleLayoutJK = new GridLayout(3, 5);
    //***阀门测压数据
    GridLayout glDatasLayout = new GridLayout(7, 14);
    //***阀门曲线图
    GridLayout glXYlineLayout = new GridLayout(1, 3);
    //****井口测压数据
    GridLayout glDatasLayoutJK = new GridLayout(8, 23);
    //****井口曲线图
    GridLayout glXYlineLayoutJK = new GridLayout(1, 7);


    private final SimpleDateFormat myFmt0 = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat myFmt1 = new SimpleDateFormat("HH:mm:ss");

    private CaConfig sqliteConfigFM = null;
    private CaConfig sqliteConfigJK = null;
    private CaConfig labviewConfig = null;


    private String[] strLabViewInfo;

    //sqlite 对应字段数组
    private final String[][] openArr = {{"PR1", "DP2", "DP3", "DP4", "DP5", "DP6", "ENB1"}, {"PR2", "ENB2", "ENB3", "ENB4", "ENB5", "ENB6", "DP1_PV"}, {"PR3", "DP2_PV", "DP3_PV", "DP4_PV", "DP5_PV", "DP6_PV", "Temperature"}};
    private final String[][] downArr = {{"PR4", "T1", "T2", "T3", "T4", "T344", "T247"}, {"PR5", "T112", "T411", "T114", "T2474", "T3448", "T41"}, {"PR6", "T34", "T21", "T11", "T4115", "T115", "T116"}};
    private final String[][] upArr = {{"HT1", "117", "T1187", "T1197", "T12177", "T1207", "T1215"}, {"HT2", "T119", "T118", "T120", "T121", "T122", "T123"}, {"HT3", "T124", "T125", "T126", "T127", "T128", "T129"}};


    @Autowired
    private IStationEquipmentService stationEquipmentService;
    @Autowired
    private IPressureTestService pressureTestService;
    @Autowired
    private IProductionOrderService productionOrderService;
    @Autowired
    private IProductInformationService productInformationService;
    @Autowired
    private ICaMediaService caMediaService;
    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPressureRulerService pressureRulerService;
//    @Autowired
//    private IFinalInspectionResultService finalInspectionResultService;

    public PressureTestView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();

        hlToolBox.setWidth("100%");
        hlToolBox.addStyleName(CoreTheme.TOOLBOX);
        hlToolBox.setMargin(true);
        CssLayout cssToolBox = new CssLayout();
        cssToolBox.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE + " " + CoreTheme.TOOLBOX_CSSLAYOUT);
        cbPressureType.setItems("井口", "阀门");
        cssToolBox.addComponents(tfProductSn, cbPressureType, btnConfirmSn, btnCreateReport);

        tfProductSn.setPlaceholder(I18NUtility.getValue("PressureTest.PorductSn", "PorductSn"));
        cbPressureType.setPlaceholder(I18NUtility.getValue("PressureTest.PressureType", "PressureType"));
        for (Button btn : btns) {
            btn.addClickListener(this);
            btn.setDisableOnClick(true);
        }
        btnConfirmSn.setIcon(VaadinIcons.CHECK);
        btnCreateReport.setIcon(VaadinIcons.CLIPBOARD_PULSE);
        btnCreateReport.setEnabled(false);

        cbPressureType.addSelectionListener(new SingleSelectionListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<String> event) {
                if (event.getValue() == null || "".equals(event.getValue().trim())) {
                    tabSheet.getTab(vlFirst).setVisible(false);
                    tabSheet.getTab(vlSecond).setVisible(false);
                    tabSheet.getTab(vlThird).setVisible(false);
                    tabSheet.getTab(vlFourth).setVisible(false);
                } else {
                    if ("阀门".equals(event.getValue())) {
                        tabSheet.getTab(vlFirst).setVisible(true);
                        tabSheet.getTab(vlSecond).setVisible(true);
                        tabSheet.getTab(vlThird).setVisible(false);
                        tabSheet.getTab(vlFourth).setVisible(false);
                    } else if ("井口".equals(event.getValue())) {
                        tabSheet.getTab(vlFirst).setVisible(false);
                        tabSheet.getTab(vlSecond).setVisible(false);
                        tabSheet.getTab(vlThird).setVisible(true);
                        tabSheet.getTab(vlFourth).setVisible(true);

                    }
                }
            }
        });

        hlToolBox.addComponent(cssToolBox);
        vlRoot.addComponent(hlToolBox);

        tabSheet.setSizeFull();
        tabSheet.addStyleName("framed");

        //*************************************************************阀门
        for (ComboBox cbStation : fmStationCbs) {
            cbStation.setPlaceholder(I18NUtility.getValue("PressureTest.Station", "Station"));
            cbStation.setEnabled(false);
        }
        for (ComboBox cbValidRecord : fmRecordCbs) {
            cbValidRecord.setPlaceholder(I18NUtility.getValue("PressureTest.Validate", "Validate"));
            cbValidRecord.setEnabled(false);
        }
        for (Button fmBtn : fmConfirmBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }
        for (Button fmBtn : fmGetBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }
        for (Button fmBtn : fmSaveBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }
        ckOpenBody.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationBodyFM.setEnabled(event.getValue());
                btnConfirmBodyFM.setEnabled(false);
                cbRecordBodyFM.setEnabled(event.getValue());
                btnGetBodyFM.setEnabled(false);
                btnSaveOpenFM.setEnabled(false);
                if (!event.getValue()) {
                    for (Label labOpenFM : labOpenFMs) {
                        labOpenFM.setValue("");
                    }
                }
            }
        });
        cbStationBodyFM.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmBodyFM.setEnabled(event.getValue() != null);
            }
        });
        cbRecordBodyFM.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetBodyFM.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        ckDownstream.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationDownFM.setEnabled(event.getValue());
                btnConfirmDownFM.setEnabled(false);
                cbRecordDownFM.setEnabled(event.getValue());
                btnGetDownFM.setEnabled(false);
                btnSaveDownFM.setEnabled(false);
                if (!event.getValue()) {
                    for (Label labDownFM : labDownFMs) {
                        labDownFM.setValue("");
                    }
                }
            }
        });
        cbStationDownFM.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmDownFM.setEnabled(event.getValue() != null);
            }
        });
        cbRecordDownFM.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetDownFM.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });


        ckUpstream.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationUpFM.setEnabled(event.getValue());
                btnConfirmUpFM.setEnabled(false);
                cbRecordUpFM.setEnabled(event.getValue());
                btnGetUpFM.setEnabled(false);
                btnSaveUpFM.setEnabled(false);
                if (!event.getValue()) {
                    for (Label labUpFM : labUpFMs) {
                        labUpFM.setValue("");
                    }
                }
            }
        });
        cbStationUpFM.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmUpFM.setEnabled(event.getValue() != null);
            }
        });
        cbRecordUpFM.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetUpFM.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });
        cbDirffTestBy.addValueChangeListener(new HasValue.ValueChangeListener<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<User> event) {
                btnSaveDirft.setEnabled(event.getValue() != null);
            }
        });

//        VerticalLayout vlSecond = new VerticalLayout();
        vlFirst.setMargin(false);
        vlFirst.setSizeFull();
        //****表头信息
        Panel panelTitle = new Panel();
        panelTitle.setWidth("100%");
        panelTitle.setSizeFull();
        glTitleLayout.setSpacing(true);
        glTitleLayout.setMargin(true);
        glTitleLayout.setWidth("100%");

        glTitleLayout.addComponent(this.labPartNo, 0, 0);
        glTitleLayout.addComponent(this.labPartRev, 1, 0);
        glTitleLayout.addComponent(this.labWorkOrder, 2, 0);

        glTitleLayout.addComponent(this.labDescription, 0, 1);

        glTitleLayout.addComponent(this.labTempRating, 0, 2);
        glTitleLayout.addComponent(this.labMaterialClass, 1, 2);
        glTitleLayout.addComponent(this.labPslLevel, 2, 2);

        glTitleLayout.addComponent(this.labSerialNo, 0, 3);
        glTitleLayout.addComponent(this.labTestProcedure, 1, 3);

        HorizontalLayout hlt = new HorizontalLayout();
        Label label = new Label(I18NUtility.getValue("PressureTest.GageNoSize", "GageNoSize"));
        hlt.addComponents(label, tfGageNoSize);

        HorizontalLayout hlt2 = new HorizontalLayout();
        Label label2 = new Label(I18NUtility.getValue("PressureTest.ProcedureNo", "ProcedureNo"));
        hlt2.addComponents(label2, tfProcedureNo);

        HorizontalLayout hlt4 = new HorizontalLayout();
        Label label4 = new Label(I18NUtility.getValue("PressureTest.TorqueSensorNo", "Torque Sensor No"));
        hlt4.addComponents(label4, tfTorqueSensor);

        glTitleLayout.addComponent(hlt, 0, 4);
        glTitleLayout.addComponent(hlt2, 1, 4);
        glTitleLayout.addComponent(hlt4, 2, 4);

        panelTitle.setContent(glTitleLayout);
        vlFirst.addComponent(panelTitle);
        vlFirst.setExpandRatio(panelTitle, 0.3f);

        //*****阀门试验数据
        Panel panelDatas = new Panel();
        panelDatas.setWidth("100%");
        panelDatas.setSizeFull();
        glDatasLayout.setSpacing(true);
        glDatasLayout.setMargin(true);
        glDatasLayout.setWidth("100%");
        glDatasLayout.addComponent(this.cbHydrostatic, 0, 0);
        glDatasLayout.addComponent(this.cbGas, 1, 0);
        glDatasLayout.addComponent(this.labPrimary, 2, 0);
        glDatasLayout.addComponent(this.labSecondary, 3, 0);
        glDatasLayout.addComponent(this.labTertiary, 4, 0);
        glDatasLayout.addComponent(this.labComments, 5, 0);
        cbHydrostatic.setValue(true);
        cbHydrostatic.setEnabled(false);
        cbGas.setEnabled(false);
        //OpenBody 本体
        glDatasLayout.addComponent(this.ckOpenBody, 0, 1);

        HorizontalLayout hltBody = new HorizontalLayout();
        btnConfirmBodyFM.setIcon(VaadinIcons.CHECK);
        hltBody.addComponents(cbStationBodyFM, btnConfirmBodyFM);
        glDatasLayout.addComponent(hltBody, 0, 2);
        glDatasLayout.addComponent(this.labPressureOpen, 1, 2);
        glDatasLayout.addComponent(this.labOpenPone, 2, 2);
        glDatasLayout.addComponent(this.labOpenPtwo, 3, 2);
        glDatasLayout.addComponent(this.labOpenPthree, 4, 2);
        glDatasLayout.addComponent(this.labOpenComments, 5, 2);

        HorizontalLayout hltBody1 = new HorizontalLayout();
        btnGetBodyFM.setIcon(VaadinIcons.CHECK);
        hltBody1.addComponents(cbRecordBodyFM, btnGetBodyFM);
        glDatasLayout.addComponent(hltBody1, 0, 3);
        glDatasLayout.addComponent(this.labHoldTimeOpen, 1, 3);
        glDatasLayout.addComponent(this.labOpenTone, 2, 3);
        glDatasLayout.addComponent(this.labOpenTtwo, 3, 3);
        glDatasLayout.addComponent(this.labOpenTthree, 4, 3);
        glDatasLayout.addComponent(btnSaveOpenFM, 6, 3);

        //Downstream 下游
        glDatasLayout.addComponent(this.ckDownstream, 0, 4);

        HorizontalLayout hltDown = new HorizontalLayout();
        btnConfirmDownFM.setIcon(VaadinIcons.CHECK);
        hltDown.addComponents(cbStationDownFM, btnConfirmDownFM);
        glDatasLayout.addComponent(hltDown, 0, 5);
        glDatasLayout.addComponent(this.labPressureDown, 1, 5);
        glDatasLayout.addComponent(this.labDownPone, 2, 5);
        glDatasLayout.addComponent(this.labDownPtwo, 3, 5);
        glDatasLayout.addComponent(this.labDownPthree, 4, 5);
        glDatasLayout.addComponent(this.labDownComments, 5, 5);

        HorizontalLayout hltDown1 = new HorizontalLayout();
        btnGetDownFM.setIcon(VaadinIcons.CHECK);
        hltDown1.addComponents(cbRecordDownFM, btnGetDownFM);
        glDatasLayout.addComponent(hltDown1, 0, 6);
        glDatasLayout.addComponent(this.labHoldTimeDown, 1, 6);
        glDatasLayout.addComponent(this.labDownTone, 2, 6);
        glDatasLayout.addComponent(this.labDownTtwo, 3, 6);
        glDatasLayout.addComponent(this.labDownTthree, 4, 6);
        glDatasLayout.addComponent(btnSaveDownFM, 6, 6);

        //Upstream 上游
        glDatasLayout.addComponent(this.ckUpstream, 0, 7);

        HorizontalLayout hltUp = new HorizontalLayout();
        btnConfirmUpFM.setIcon(VaadinIcons.CHECK);
        hltUp.addComponents(cbStationUpFM, btnConfirmUpFM);
        glDatasLayout.addComponent(hltUp, 0, 8);
        glDatasLayout.addComponent(this.labPressureUp, 1, 8);
        glDatasLayout.addComponent(this.labUpPone, 2, 8);
        glDatasLayout.addComponent(this.labUpPtwo, 3, 8);
        glDatasLayout.addComponent(this.labUpPthree, 4, 8);
        glDatasLayout.addComponent(this.labUpComments, 5, 8);
        HorizontalLayout hltUp1 = new HorizontalLayout();
        btnGetUpFM.setIcon(VaadinIcons.CHECK);
        hltUp1.addComponents(cbRecordUpFM, btnGetUpFM);
        glDatasLayout.addComponent(hltUp1, 0, 9);
        glDatasLayout.addComponent(this.labHoldTimeUp, 1, 9);
        glDatasLayout.addComponent(this.labUpTone, 2, 9);
        glDatasLayout.addComponent(this.labUpTtwo, 3, 9);
        glDatasLayout.addComponent(this.labUpTthree, 4, 9);
        glDatasLayout.addComponent(btnSaveUpFM, 6, 9);

        //带压开启
        glDatasLayout.addComponent(this.labBlowDown, 0, 10);
        glDatasLayout.addComponent(this.ckOpenUnder, 1, 10);

        //扭矩值记录
        glDatasLayout.addComponent(this.labTorque, 0, 11);
        glDatasLayout.addComponent(this.tfTorqueValue1, 1, 11);
        glDatasLayout.addComponent(this.tfTorqueValue2, 2, 11);
        glDatasLayout.addComponent(this.tfTorqueValue3, 3, 11);
        glDatasLayout.addComponent(this.tfTorqueValue4, 4, 11);

        glDatasLayout.addComponent(this.btnSaveTorque, 5, 11);
        // 通径测试
        glDatasLayout.addComponent(this.labDirff, 0, 12);
        glDatasLayout.addComponent(this.cbPass, 1, 12);
        glDatasLayout.addComponent(this.labDirffTestBy, 2, 12);
        glDatasLayout.addComponent(this.cbDirffTestBy, 3, 12);

        glDatasLayout.addComponent(this.btnSaveDirft, 4, 12);

        //测试人员 + 日期
        glDatasLayout.addComponent(this.labTestBy, 0, 13);
        glDatasLayout.addComponent(this.labTestBy0, 1, 13);
        glDatasLayout.addComponent(this.labDate, 2, 13);
        glDatasLayout.addComponent(this.labDate0, 3, 13);

        panelDatas.setContent(glDatasLayout);
        vlFirst.addComponent(panelDatas);
        vlFirst.setExpandRatio(panelDatas, 0.7f);
        vlFirst.setHeight("150%");
        tabSheet.addTab(vlFirst, "压力试验数据");

//****************************************************JK
        for (ComboBox cbStation : jkStationCbs) {
            cbStation.setPlaceholder(I18NUtility.getValue("PressureTest.Station", "Station"));
            cbStation.setEnabled(false);
        }
        for (ComboBox cbValidRecord : jkRecordCbs) {
            cbValidRecord.setPlaceholder(I18NUtility.getValue("PressureTest.Validate", "Validate"));
            cbValidRecord.setEnabled(false);
        }
        for (Button fmBtn : jkConfirmBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }
        for (Button fmBtn : jkGetBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }
        for (Button fmBtn : jkSaveBtns) {
            fmBtn.addClickListener(this::buttonClick);
            fmBtn.setDisableOnClick(true);
            fmBtn.setIcon(VaadinIcons.CHECK);
            fmBtn.setEnabled(false);
        }

        ckOpenBodyJK.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationOpenBodyJK.setEnabled(event.getValue());
                btnConfirmOpenBodyJK.setEnabled(false);
                jkOpenBodyRecord.setEnabled(event.getValue());
                btnGetOpenBodyJK.setEnabled(false);
                btnSaveOpenJK.setEnabled(false);
                if (!event.getValue()) {
                    for (Label lab : labOpenJks) {
                        lab.setValue("");
                    }
                    labOpenBodySignatureJK.setValue("");
                    labOpenBodyDateJK.setValue("");
                }
            }
        });
        cbStationOpenBodyJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmOpenBodyJK.setEnabled(event.getValue() != null);
            }
        });
        jkOpenBodyRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetOpenBodyJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        ckCrossoverBodyJK.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationCrossBodyTopJK.setEnabled(event.getValue());
                btnConfirmCrossBodyTopJK.setEnabled(false);
                jkCrossoverBodyTopRecord.setEnabled(event.getValue());
                btnGetCrossBodyTJK.setEnabled(false);

                cbStationCrossBodyBtmJK.setEnabled(event.getValue());
                btnConfirmCrossBodyBtmJK.setEnabled(false);
                jkCrossoverBodyBtmRecord.setEnabled(event.getValue());
                btnGetCrossBodyBJK.setEnabled(false);

                btnSaveCrossBodyJK.setEnabled(false);
                if (!event.getValue()) {
                    for (Label lab : labCrossBodyTopJks) {
                        lab.setValue("");
                    }
                    for (Label lab : labCrossBodyBtmJks) {
                        lab.setValue("");
                    }
                    labCrossoverBodySignatureJK.setValue("");
                    labCrossoverBodyDateJK.setValue("");
                }
            }
        });
        cbStationCrossBodyTopJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmCrossBodyTopJK.setEnabled(event.getValue() != null);
            }
        });
        jkCrossoverBodyTopRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetCrossBodyTJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });
        cbStationCrossBodyBtmJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmCrossBodyBtmJK.setEnabled(event.getValue() != null);
            }
        });
        jkCrossoverBodyBtmRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetCrossBodyBJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        ckChristmasTreeJK.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationChristmasJK.setEnabled(event.getValue());
                btnConfirmChristmasJK.setEnabled(false);
                jkChristmasTreeRecord.setEnabled(event.getValue());
                btnGetChristmasJK.setEnabled(false);
                btnSaveChristmasJK.setEnabled(false);
                if (!event.getValue()) {
                    for (Label lab : labChrisTreeJks) {
                        lab.setValue("");
                    }
                    labChristmasTreeSignatureJK.setValue("");
                    labChristmasTreeDateJK.setValue("");
                }
            }
        });
        cbStationChristmasJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmChristmasJK.setEnabled(event.getValue() != null);
            }
        });
        jkChristmasTreeRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetChristmasJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        ckWellheadAssemblyJK.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationWellheadJK.setEnabled(event.getValue());
                btnConfirmWellheadJK.setEnabled(false);
                jkWellheadAssemblyRecord.setEnabled(event.getValue());
                btnGetWellheadJK.setEnabled(false);
                btnSaveWellheadJK.setEnabled(false);
                if (!event.getValue()) {
                    for (Label lab : labWellAssemJks) {
                        lab.setValue("");
                    }
                    labWellheadAssemblySignatureJK.setValue("");
                    labWellheadAssemblyDateJK.setValue("");
                }
            }
        });
        cbStationWellheadJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmWellheadJK.setEnabled(event.getValue() != null);
            }
        });
        jkWellheadAssemblyRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetWellheadJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        ckCrossoverAssemblyJK.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                cbStationCrossAssemTopJK.setEnabled(event.getValue());
                btnConfirmCrossAssemTopJK.setEnabled(false);
                jkCrossoverAssemblyTopRecord.setEnabled(event.getValue());
                btnGetCrossAssemTJK.setEnabled(false);

                cbStationCrossAssemBtmJK.setEnabled(event.getValue());
                btnConfirmCrossAssemBtmJK.setEnabled(false);
                jkCrossoverAssemblyBtmRecord.setEnabled(event.getValue());
                btnGetCrossAssemBJK.setEnabled(false);

                btnSaveCrossAssembJK.setEnabled(false);
                if (!event.getValue()) {
                    for (Label lab : labCrossAssemTopJks) {
                        lab.setValue("");
                    }
                    for (Label lab : labCrossAssemBtmJks) {
                        lab.setValue("");
                    }
                    labCrossoverAssemblySignatureJK.setValue("");
                    labCrossoverAssemblyDateJK.setValue("");
                }
            }
        });
        cbStationCrossAssemTopJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmCrossAssemTopJK.setEnabled(event.getValue() != null);
            }
        });
        jkCrossoverAssemblyTopRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetCrossAssemTJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });
        cbStationCrossAssemBtmJK.addValueChangeListener(new HasValue.ValueChangeListener<StationEquipment>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<StationEquipment> event) {
                btnConfirmCrossAssemBtmJK.setEnabled(event.getValue() != null);
            }
        });
        jkCrossoverAssemblyBtmRecord.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
                btnGetCrossAssemBJK.setEnabled(!Strings.isNullOrEmpty(event.getValue()));
            }
        });

        //******阀门曲线图
//        VerticalLayout vlSecond = new VerticalLayout();
        vlSecond.setMargin(false);
        vlSecond.setSizeFull();
        Panel panelpic = new Panel();
        panelpic.setWidth("100%");
        panelpic.setSizeFull();
        glXYlineLayout.setMargin(true);
        glXYlineLayout.setSpacing(true);
        glXYlineLayout.setWidth("75%");
        panelpic.setContent(glXYlineLayout);
        vlSecond.addComponent(panelpic);
        vlSecond.setExpandRatio(panelpic, 1.0f);
        tabSheet.addTab(vlSecond, "曲线图(阀门)");

        //********井口
//        VerticalLayout vlThird = new VerticalLayout();
        vlThird.setMargin(false);
        vlThird.setSizeFull();
        //****表头信息
        Panel panelTitleJK = new Panel();
        panelTitleJK.setWidth("100%");
        panelTitleJK.setSizeFull();
        glTitleLayoutJK.setSpacing(true);
        glTitleLayoutJK.setMargin(true);
        glTitleLayoutJK.setWidth("100%");

        glTitleLayoutJK.addComponent(this.labPartNoJK, 0, 0);
        glTitleLayoutJK.addComponent(this.labPartRevJK, 1, 0);

        glTitleLayoutJK.addComponent(this.labSerialNoJK, 0, 1);
        glTitleLayoutJK.addComponent(this.labWorkOrderJK, 1, 1);

        glTitleLayoutJK.addComponent(this.labTestProcedureJK, 0, 2);
        glTitleLayoutJK.addComponent(this.labPslLevelJK, 1, 2);

        glTitleLayoutJK.addComponent(this.labDescriptionJK, 0, 3);

        HorizontalLayout hltJK = new HorizontalLayout();
        Label labelJK = new Label(I18NUtility.getValue("PressureTest.GageNoSize", "GageNoSize"));
        hltJK.addComponents(labelJK, tfGageNoSizeJK);

        HorizontalLayout hlt3 = new HorizontalLayout();
        Label label3 = new Label(I18NUtility.getValue("PressureTest.ProcedureNo", "ProcedureNo"));
        hlt3.addComponents(label3, tfProcedureNoJK);

        glTitleLayoutJK.addComponent(hltJK, 0, 4);
        glTitleLayoutJK.addComponent(hlt3, 1, 4);

        panelTitleJK.setContent(glTitleLayoutJK);
        vlThird.addComponent(panelTitleJK);
        vlThird.setExpandRatio(panelTitleJK, 0.3f);
        vlThird.setHeight("150%");

        //*****试验数据
        Panel panelDatasJK = new Panel();
        panelDatasJK.setWidth("100%");
        panelDatasJK.setSizeFull();
        glDatasLayoutJK.setSpacing(true);
        glDatasLayoutJK.setMargin(true);
        glDatasLayoutJK.setWidth("100%");
        glDatasLayoutJK.addComponent(this.cbHydrostaticJK, 0, 0);
        glDatasLayoutJK.addComponent(this.cbGasJK, 1, 0);
        glDatasLayoutJK.addComponent(this.labPrimaryJK, 2, 0);
        glDatasLayoutJK.addComponent(this.labSecondaryJK, 3, 0);
        glDatasLayoutJK.addComponent(this.labTertiaryJK, 4, 0);
        glDatasLayoutJK.addComponent(this.labTesterSignatureJK, 5, 0);
        glDatasLayoutJK.addComponent(this.labTesterDateJK, 6, 0);
        cbHydrostaticJK.setValue(true);
        cbHydrostaticJK.setEnabled(false);
        cbGasJK.setEnabled(false);
        //OpenBody 本体
        glDatasLayoutJK.addComponent(this.ckOpenBodyJK, 0, 1);
        glDatasLayoutJK.addComponent(this.labPressureOpenJK, 1, 1);
        glDatasLayoutJK.addComponent(this.labOpenPoneJK, 2, 1);
        glDatasLayoutJK.addComponent(this.labOpenPtwoJK, 3, 1);
        glDatasLayoutJK.addComponent(this.labOpenPthreeJK, 4, 1);

        HorizontalLayout hltOpenBody = new HorizontalLayout();
        btnConfirmOpenBodyJK.setIcon(VaadinIcons.CHECK);
        hltOpenBody.addComponents(cbStationOpenBodyJK, btnConfirmOpenBodyJK);
        glDatasLayoutJK.addComponent(hltOpenBody, 0, 2);
        glDatasLayoutJK.addComponent(this.labHoldTimeOpenJK, 1, 2);
        glDatasLayoutJK.addComponent(this.labOpenToneJK, 2, 2);
        glDatasLayoutJK.addComponent(this.labOpenTtwoJK, 3, 2);
        glDatasLayoutJK.addComponent(this.labOpenTthreeJK, 4, 2);
        glDatasLayoutJK.addComponent(this.labOpenBodySignatureJK, 5, 2);
        glDatasLayoutJK.addComponent(this.labOpenBodyDateJK, 6, 2);
        glDatasLayoutJK.addComponent(btnSaveOpenJK, 7, 2);

        HorizontalLayout hltOpenBody1 = new HorizontalLayout();
        btnGetOpenBodyJK.setIcon(VaadinIcons.CHECK);
        hltOpenBody1.addComponents(jkOpenBodyRecord, btnGetOpenBodyJK);
        glDatasLayoutJK.addComponent(hltOpenBody1, 0, 3);
        glDatasLayoutJK.addComponent(this.labResultsOpenJK, 1, 3);
        glDatasLayoutJK.addComponent(this.labOpenRoneJK, 2, 3);
        glDatasLayoutJK.addComponent(this.labOpenRtwoJK, 3, 3);
        glDatasLayoutJK.addComponent(this.labOpenRthreeJK, 4, 3);
        //CrossoverBody 转换接头本体
        //top
        glDatasLayoutJK.addComponent(this.ckCrossoverBodyJK, 0, 4);
        glDatasLayoutJK.addComponent(this.labPressureCrossoverBodyTOPJK, 1, 4);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPPoneJK, 2, 4);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPPtwoJK, 3, 4);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPPthreeJK, 4, 4);

        HorizontalLayout hltCrossBodyTop = new HorizontalLayout();
        btnConfirmCrossBodyTopJK.setIcon(VaadinIcons.CHECK);
        hltCrossBodyTop.addComponents(cbStationCrossBodyTopJK, btnConfirmCrossBodyTopJK);
        glDatasLayoutJK.addComponent(hltCrossBodyTop, 0, 5);
        glDatasLayoutJK.addComponent(this.labHoldTimeCrossoverBodyTOPJK, 1, 5);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPToneJK, 2, 5);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPTtwoJK, 3, 5);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPTthreeJK, 4, 5);

        HorizontalLayout hltCrossBodyT = new HorizontalLayout();
        btnGetCrossBodyTJK.setIcon(VaadinIcons.CHECK);
        hltCrossBodyT.addComponents(jkCrossoverBodyTopRecord, btnGetCrossBodyTJK);
        glDatasLayoutJK.addComponent(hltCrossBodyT, 0, 6);
        glDatasLayoutJK.addComponent(this.labResultsCrossoverBodyTOPJK, 1, 6);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPRoneJK, 2, 6);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPRtwoJK, 3, 6);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyTOPRthreeJK, 4, 6);
        glDatasLayoutJK.addComponent(this.labCrossoverBodySignatureJK, 5, 6);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyDateJK, 6, 6);
        glDatasLayoutJK.addComponent(btnSaveCrossBodyJK, 7, 6);

        //btm
        glDatasLayoutJK.addComponent(this.labPressureCrossoverBodyBTMJK, 1, 7);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMPoneJK, 2, 7);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMPtwoJK, 3, 7);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMPthreeJK, 4, 7);

        HorizontalLayout hltCrossBodyBtm = new HorizontalLayout();
        btnConfirmCrossBodyBtmJK.setIcon(VaadinIcons.CHECK);
        hltCrossBodyBtm.addComponents(cbStationCrossBodyBtmJK, btnConfirmCrossBodyBtmJK);
        glDatasLayoutJK.addComponent(hltCrossBodyBtm, 0, 8);
        glDatasLayoutJK.addComponent(this.labHoldTimeCrossoverBodyBTMJK, 1, 8);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMToneJK, 2, 8);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMTtwoJK, 3, 8);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMTthreeJK, 4, 8);

        HorizontalLayout hltCrossBodyB = new HorizontalLayout();
        btnGetCrossBodyBJK.setIcon(VaadinIcons.CHECK);
        hltCrossBodyB.addComponents(jkCrossoverBodyBtmRecord, btnGetCrossBodyBJK);
        glDatasLayoutJK.addComponent(hltCrossBodyB, 0, 9);
        glDatasLayoutJK.addComponent(this.labResultsCrossoverBodyBTMJK, 1, 9);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMRoneJK, 2, 9);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMRtwoJK, 3, 9);
        glDatasLayoutJK.addComponent(this.labCrossoverBodyBTMRthreeJK, 4, 9);
        //ChristmasTree 采油树
        glDatasLayoutJK.addComponent(this.ckChristmasTreeJK, 0, 10);
        glDatasLayoutJK.addComponent(this.labPressureChristmasTreeJK, 1, 10);
        glDatasLayoutJK.addComponent(this.labChristmasTreePoneJK, 2, 10);
        glDatasLayoutJK.addComponent(this.labChristmasTreePtwoJK, 3, 10);
        glDatasLayoutJK.addComponent(this.labChristmasTreePthreeJK, 4, 10);

        HorizontalLayout hltChristmas = new HorizontalLayout();
        btnConfirmChristmasJK.setIcon(VaadinIcons.CHECK);
        hltChristmas.addComponents(cbStationChristmasJK, btnConfirmChristmasJK);
        glDatasLayoutJK.addComponent(hltChristmas, 0, 11);
        glDatasLayoutJK.addComponent(this.labHoldTimeChristmasTreeJK, 1, 11);
        glDatasLayoutJK.addComponent(this.labChristmasTreeToneJK, 2, 11);
        glDatasLayoutJK.addComponent(this.labChristmasTreeTtwoJK, 3, 11);
        glDatasLayoutJK.addComponent(this.labChristmasTreeTthreeJK, 4, 11);
        glDatasLayoutJK.addComponent(this.labChristmasTreeSignatureJK, 5, 11);
        glDatasLayoutJK.addComponent(this.labChristmasTreeDateJK, 6, 11);
        glDatasLayoutJK.addComponent(btnSaveChristmasJK, 7, 11);

        HorizontalLayout hltChristmas1 = new HorizontalLayout();
        btnGetChristmasJK.setIcon(VaadinIcons.CHECK);
        hltChristmas1.addComponents(jkChristmasTreeRecord, btnGetChristmasJK);
        glDatasLayoutJK.addComponent(hltChristmas1, 0, 12);
        glDatasLayoutJK.addComponent(this.labResultsChristmasTreeJK, 1, 12);
        glDatasLayoutJK.addComponent(this.labChristmasTreeRoneJK, 2, 12);
        glDatasLayoutJK.addComponent(this.labChristmasTreeRtwoJK, 3, 12);
        glDatasLayoutJK.addComponent(this.labChristmasTreeRthreeJK, 4, 12);

        //WellheadAssembly 井口头
        glDatasLayoutJK.addComponent(this.ckWellheadAssemblyJK, 0, 13);
        glDatasLayoutJK.addComponent(this.labPressureWellheadAssemblyJK, 1, 13);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyPoneJK, 2, 13);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyPtwoJK, 3, 13);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyPthreeJK, 4, 13);

        HorizontalLayout hltWellhead = new HorizontalLayout();
        btnConfirmWellheadJK.setIcon(VaadinIcons.CHECK);
        hltWellhead.addComponents(cbStationWellheadJK, btnConfirmWellheadJK);
        glDatasLayoutJK.addComponent(hltWellhead, 0, 14);

        glDatasLayoutJK.addComponent(this.labHoldTimeWellheadAssemblyJK, 1, 14);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyToneJK, 2, 14);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyTtwoJK, 3, 14);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyTthreeJK, 4, 14);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblySignatureJK, 5, 14);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyDateJK, 6, 14);
        glDatasLayoutJK.addComponent(btnSaveWellheadJK, 7, 14);

        HorizontalLayout hltWellhead1 = new HorizontalLayout();
        btnGetWellheadJK.setIcon(VaadinIcons.CHECK);
        hltWellhead1.addComponents(jkWellheadAssemblyRecord, btnGetWellheadJK);
        glDatasLayoutJK.addComponent(hltWellhead1, 0, 15);
        glDatasLayoutJK.addComponent(this.labResultsWellheadAssemblyJK, 1, 15);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyRoneJK, 2, 15);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyRtwoJK, 3, 15);
        glDatasLayoutJK.addComponent(this.labWellheadAssemblyRthreeJK, 4, 15);
        //CrossoverAssembly 转换接头组件
        //top
        glDatasLayoutJK.addComponent(this.ckCrossoverAssemblyJK, 0, 16);
        glDatasLayoutJK.addComponent(this.labPressureCrossoverAssemblyTOPJK, 1, 16);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPPoneJK, 2, 16);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPPtwoJK, 3, 16);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPPthreeJK, 4, 16);

        HorizontalLayout hltCrossAssemTop = new HorizontalLayout();
        btnConfirmCrossAssemTopJK.setIcon(VaadinIcons.CHECK);
        hltCrossAssemTop.addComponents(cbStationCrossAssemTopJK, btnConfirmCrossAssemTopJK);
        glDatasLayoutJK.addComponent(hltCrossAssemTop, 0, 17);
        glDatasLayoutJK.addComponent(this.labHoldTimeCrossoverAssemblyTOPJK, 1, 17);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPToneJK, 2, 17);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPTtwoJK, 3, 17);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPTthreeJK, 4, 17);

        HorizontalLayout hltCrossAssemT = new HorizontalLayout();
        btnGetCrossAssemTJK.setIcon(VaadinIcons.CHECK);
        hltCrossAssemT.addComponents(jkCrossoverAssemblyTopRecord, btnGetCrossAssemTJK);
        glDatasLayoutJK.addComponent(hltCrossAssemT, 0, 18);
        glDatasLayoutJK.addComponent(this.labResultsCrossoverAssemblyTOPJK, 1, 18);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPRoneJK, 2, 18);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPRtwoJK, 3, 18);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyTOPRthreeJK, 4, 18);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblySignatureJK, 5, 18);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyDateJK, 6, 18);
        glDatasLayoutJK.addComponent(btnSaveCrossAssembJK, 7, 18);
        //btm

        glDatasLayoutJK.addComponent(this.labPressureCrossoverAssemblyBTMJK, 1, 19);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMPoneJK, 2, 19);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMPtwoJK, 3, 19);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMPthreeJK, 4, 19);

        HorizontalLayout hltCrossAssemBtm = new HorizontalLayout();
        btnConfirmCrossAssemBtmJK.setIcon(VaadinIcons.CHECK);
        hltCrossAssemBtm.addComponents(cbStationCrossAssemBtmJK, btnConfirmCrossAssemBtmJK);
        glDatasLayoutJK.addComponent(hltCrossAssemBtm, 0, 20);
        glDatasLayoutJK.addComponent(this.labHoldTimeCrossoverAssemblyBTMJK, 1, 20);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMToneJK, 2, 20);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMTtwoJK, 3, 20);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMTthreeJK, 4, 20);

        HorizontalLayout hltCrossAssemB = new HorizontalLayout();
        btnGetCrossAssemBJK.setIcon(VaadinIcons.CHECK);
        hltCrossAssemB.addComponents(jkCrossoverAssemblyBtmRecord, btnGetCrossAssemBJK);
        glDatasLayoutJK.addComponent(hltCrossAssemB, 0, 21);
        glDatasLayoutJK.addComponent(this.labResultsCrossoverAssemblyBTMJK, 1, 21);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMRoneJK, 2, 21);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMRtwoJK, 3, 21);
        glDatasLayoutJK.addComponent(this.labCrossoverAssemblyBTMRthreeJK, 4, 21);
        //DriftTree 采油树通径
        glDatasLayoutJK.addComponent(this.labDriftTestJK, 0, 22);
        glDatasLayoutJK.addComponent(this.cbPassJK, 1, 22);
        glDatasLayoutJK.addComponent(this.labDirffTestByJK, 4, 22);
        glDatasLayoutJK.addComponent(this.cbDirffTestByJK, 5, 22);

        panelDatasJK.setContent(glDatasLayoutJK);
        vlThird.addComponent(panelDatasJK);
        vlThird.setExpandRatio(panelDatasJK, 0.7f);
        tabSheet.addTab(vlThird, "压力试验数据");

        //******井口曲线图
//        VerticalLayout vlFourth = new VerticalLayout();
        vlFourth.setMargin(false);
        vlFourth.setSizeFull();
        Panel panelpicK = new Panel();
        panelpicK.setWidth("100%");
        panelpicK.setSizeFull();
        glXYlineLayoutJK.setMargin(true);
        glXYlineLayoutJK.setSpacing(true);
        glXYlineLayoutJK.setWidth("75%");
        panelpicK.setContent(glXYlineLayoutJK);
        vlFourth.addComponent(panelpicK);
        vlFourth.setExpandRatio(panelpicK, 1.0f);
        tabSheet.addTab(vlFourth, "曲线图(井口)");
        //初始隐藏
        tabSheet.getTab(vlFirst).setVisible(false);
        tabSheet.getTab(vlSecond).setVisible(false);
        tabSheet.getTab(vlThird).setVisible(false);
        tabSheet.getTab(vlFourth).setVisible(false);

        vlRoot.addComponent(tabSheet);
        vlRoot.setExpandRatio(tabSheet, 1f);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }


    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnConfirmSn.equals(button)) {
            btnConfirmSnMethod();
        } else if (btnCreateReport.equals(button)) {
            btnCreateReportMethod();
        } else if (btnSaveTorque.equals(button)) {//阀门-保存扭矩值
            btnSaveTorqueMethod();
        } else if (btnSaveDirft.equals(button)) {//阀门-保存通径
            btnSaveDirftMethod();
        } else if (btnConfirmBodyFM.equals(button)) {//（阀门-本体）
            btnConfirmBodyFMMethod();
        } else if (btnGetBodyFM.equals(button)) {
            btnGetBodyFMMethod();
        } else if (btnSaveOpenFM.equals(button)) {
            btnSaveOpenFMMethod();
        } else if (btnConfirmDownFM.equals(button)) {//（阀门-下游）
            btnConfirmDownFMMethod();
        } else if (btnGetDownFM.equals(button)) {
            btnGetDownFMMethod();
        } else if (btnSaveDownFM.equals(button)) {
            btnSaveDownFMMethod();
        } else if (btnConfirmUpFM.equals(button)) {//（阀门-上游）
            btnConfirmUpFMMethod();
        } else if (btnGetUpFM.equals(button)) {
            btnGetUpFMMethod();
        } else if (btnSaveUpFM.equals(button)) {
            btnSaveUpFMMethod();
        } else if (btnConfirmOpenBodyJK.equals(button)) {//（井口-本体）
            btnConfirmOpenBodyJKMethod();
        } else if (btnGetOpenBodyJK.equals(button)) {
            btnGetOpenBodyJKMethod();
        } else if (btnSaveOpenJK.equals(button)) {
            btnSaveOpenJKMethod();
        } else if (btnConfirmCrossBodyTopJK.equals(button)) {//（井口-转换接头本体）
            btnConfirmCrossBodyTopJKMethod();
        } else if (btnGetCrossBodyTJK.equals(button)) {
            btnGetCrossBodyTJKMethod();
        } else if (btnConfirmCrossBodyBtmJK.equals(button)) {//（井口-转换接头本体）
            btnConfirmCrossBodyBtmJKMethod();
        } else if (btnGetCrossBodyBJK.equals(button)) {
            btnGetCrossBodyBJKMethod();
        } else if (btnSaveCrossBodyJK.equals(button)) {
            btnSaveCrossBodyJKMethod();
        } else if (btnConfirmChristmasJK.equals(button)) {//（井口-采油树）
            btnConfirmChristmasJKMethod();
        } else if (btnGetChristmasJK.equals(button)) {
            btnGetChristmasJKMethod();
        } else if (btnSaveChristmasJK.equals(button)) {
            btnSaveChristmasJKMethod();
        } else if (btnConfirmWellheadJK.equals(button)) {//（井口-井口头）
            btnConfirmWellheadJKMethod();
        } else if (btnGetWellheadJK.equals(button)) {
            btnGetWellheadJKMethod();
        } else if (btnSaveWellheadJK.equals(button)) {
            btnSaveWellheadJKMethod();
        } else if (btnConfirmCrossAssemTopJK.equals(button)) {//（井口-转换接头组件）
            btnConfirmCrossAssemTopJKMethod();
        } else if (btnGetCrossAssemTJK.equals(button)) {
            btnGetCrossAssemTJKMethod();
        } else if (btnConfirmCrossAssemBtmJK.equals(button)) {//（井口-转换接头组件）
            btnConfirmCrossAssemBtmJKMethod();
        } else if (btnGetCrossAssemBJK.equals(button)) {
            btnGetCrossAssemBJKMethod();
        } else if (btnSaveCrossAssembJK.equals(button)) {
            btnSaveCrossAssembJKMethod();
        }
    }

    /***
     * 确认序列号
     */
    private void btnConfirmSnMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            resetFM();
            resetJK();
            btnCreateReport.setEnabled(true);
            sqliteConfigFM = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_FM);
            sqliteConfigJK = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_JK);
            labviewConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_LABVIEW);
            if (sqliteConfigFM == null || sqliteConfigJK == null || labviewConfig == null) {
                throw new PlatformException("请先配置压力数据存放根目录");
            }
            String productSn = tfProductSn.getValue().trim();
            //通过成品序列号获得数据
            String strOrderNo = (productSn).substring(0, productSn.length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                throw new PlatformException("该成品序列号不存在！请检查是否输入有误！");
            }
            if(productionOrder.getBomLockFlag()){
                throw new PlatformException(I18NUtility.getValue("ProductionOrder.BomLocked", "This Order is locked, Please contact the engineer to solve !"));
            }
            ProductInformation productInfo;

            PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(productSn);
            if (pressureTest == null) {
                pressureTest = new PressureTest();
                productInfo = productInformationService.getByNoRev(productionOrder.getProductId(), productionOrder.getProductVersionId());
            } else {
                cbPressureType.setSelectedItem(pressureTest.getTestType());
                productInfo = productInformationService.getByNoRev(pressureTest.getPartNo(), pressureTest.getPartRev());
            }
            //根据压力标准来设定可测试项
            List<PressureRuler> pressureRulers = pressureRulerService.getByProductNo(productionOrder.getProductId());
            List<String> strTypes = new ArrayList<>();
            pressureRulers.forEach(ruler -> strTypes.add(ruler.getPressureType()));

            if (strTypes.contains(PressureTypeEnum.OPEN_BODY_FM.getKey())) {
                ckOpenBody.setEnabled(true);
            } else {
                ckOpenBody.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.DOWNSTREAM_FM.getKey())) {
                ckDownstream.setEnabled(true);
            } else {
                ckDownstream.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.UPSTREAM_FM.getKey())) {
                ckUpstream.setEnabled(true);
            } else {
                ckUpstream.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.OPEN_BODY_JK.getKey())) {
                ckOpenBodyJK.setEnabled(true);
            } else {
                ckOpenBodyJK.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.CROSS_OVER_BODY_TOP_JK.getKey())
                    || strTypes.contains(PressureTypeEnum.CROSS_OVER_BODY_BTM_JK.getKey())) {
                ckCrossoverBodyJK.setEnabled(true);
            } else {
                ckCrossoverBodyJK.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.CHRISTMAS_TREE_JK.getKey())) {
                ckChristmasTreeJK.setEnabled(true);
            } else {
                ckChristmasTreeJK.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.WELLHEAD_JK.getKey())) {
                ckWellheadAssemblyJK.setEnabled(true);
            } else {
                ckWellheadAssemblyJK.setEnabled(false);
            }
            if (strTypes.contains(PressureTypeEnum.CROSS_OVER_ASSEMBLY_TOP_JK.getKey())
                    || strTypes.contains(PressureTypeEnum.CROSS_OVER_ASSEMBLY_BTM_JK.getKey())) {
                ckCrossoverAssemblyJK.setEnabled(true);
            } else {
                ckCrossoverAssemblyJK.setEnabled(false);
            }

            //获取表头信息及测试数据
            if ("阀门".equals(cbPressureType.getValue())) {
                //1-表头
                labPartNo.setValue(Strings.isNullOrEmpty(pressureTest.getPartNo()) ? productionOrder.getProductId() : pressureTest.getPartNo());
                labPartRev.setValue(Strings.isNullOrEmpty(pressureTest.getPartRev()) ? productionOrder.getProductVersionId() : pressureTest.getPartRev());
                labWorkOrder.setValue(Strings.isNullOrEmpty(pressureTest.getWorkOrder()) ? productionOrder.getProductOrderId() : pressureTest.getWorkOrder());
                labDescription.setValue(productInfo.getProductDesc() == null ? "" : productInfo.getProductDesc());
                labTempRating.setValue(productInfo.getTemperatureRating() == null ? "" : productInfo.getTemperatureRating());
                labMaterialClass.setValue(productInfo.getMaterialRating() == null ? "" : productInfo.getMaterialRating());
                labPslLevel.setValue(productInfo.getPSLRating() == null ? "" : productInfo.getPSLRating());
                labSerialNo.setValue(Strings.isNullOrEmpty(pressureTest.getProductSN()) ? tfProductSn.getValue() : pressureTest.getProductSN());
                labTestProcedure.setValue(productInfo.getPressureInspectionProcedure() == null ? "" : productInfo.getPressureInspectionProcedure() + " REV" + productInfo.getPressureInspectionProcedureVersion());
                tfProcedureNo.setValue(Strings.isNullOrEmpty(pressureTest.getProcedureNo()) ? "" : pressureTest.getProcedureNo());
                tfTorqueSensor.setValue(Strings.isNullOrEmpty(pressureTest.getTorqueSensorNo()) ? "" : pressureTest.getTorqueSensorNo());
                tfGageNoSize.setValue(Strings.isNullOrEmpty(pressureTest.getGageNoSize()) ? "" : pressureTest.getGageNoSize());
                //2-本体 TODO
                ckOpenBody.setValue(pressureTest.isFmOpenBodyFlag());
                if (pressureTest.isFmOpenBodyFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getFmOpenBodyStationName(), pressureTest.getFmOpenBodyStationNo());
                    cbStationBodyFM.setSelectedItem(stationEquipment);
                    cbRecordBodyFM.setSelectedItem(pressureTest.getFmOpenBodyRecord());

                    labOpenPone.setValue(String.valueOf(pressureTest.getFmOpenP1()));
                    labOpenPtwo.setValue(String.valueOf(pressureTest.getFmOpenP2()));
                    labOpenPthree.setValue(String.valueOf(pressureTest.getFmOpenP3()));
                    labOpenTone.setValue(String.valueOf(pressureTest.getFmOpenT1()));
                    labOpenTtwo.setValue(String.valueOf(pressureTest.getFmOpenT2()));
                    labOpenTthree.setValue(String.valueOf(pressureTest.getFmOpenT3()));

                    labOpenComments.setValue(pressureTest.getFmOpenResult());

                }
                //3-下游 TODO
                ckDownstream.setValue(pressureTest.isFmDownFlag());
                if (pressureTest.isFmDownFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getFmDownStationName(), pressureTest.getFmDownStationNo());
                    cbStationDownFM.setSelectedItem(stationEquipment);
                    cbRecordDownFM.setSelectedItem(pressureTest.getFmDownRecord());

                    labDownPone.setValue(String.valueOf(pressureTest.getFmDownP1()));
                    labDownPtwo.setValue(String.valueOf(pressureTest.getFmDownP2()));
                    labDownPthree.setValue(String.valueOf(pressureTest.getFmDownP3()));
                    labDownTone.setValue(String.valueOf(pressureTest.getFmDownT1()));
                    labDownTtwo.setValue(String.valueOf(pressureTest.getFmDownT2()));
                    labDownTthree.setValue(String.valueOf(pressureTest.getFmDownT3()));

                    labDownComments.setValue(pressureTest.getFmDownResult());

                }
                //4-上游 TODO
                ckUpstream.setValue(pressureTest.isFmUpFlag());
                if (pressureTest.isFmUpFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getFmUpStationName(), pressureTest.getFmUpStationNo());
                    cbStationUpFM.setSelectedItem(stationEquipment);
                    cbRecordUpFM.setSelectedItem(pressureTest.getFmUpRecord());

                    labUpPone.setValue(String.valueOf(pressureTest.getFmUpP1()));
                    labUpPtwo.setValue(String.valueOf(pressureTest.getFmUpP2()));
                    labUpPthree.setValue(String.valueOf(pressureTest.getFmUpP3()));
                    labUpTone.setValue(String.valueOf(pressureTest.getFmUpT1()));
                    labUpTtwo.setValue(String.valueOf(pressureTest.getFmUpT2()));
                    labUpTthree.setValue(String.valueOf(pressureTest.getFmUpT3()));

                    labUpComments.setValue(pressureTest.getFmUpResult());

                }
                //5-带压
                ckOpenUnder.setValue(pressureTest.getBlowDownTest());
                //6-扭矩值
                if (pressureTest.isTorqueDriftSaved()) {
                    tfTorqueValue1.setValue(pressureTest.getTorqueValue1() == null ? "" : pressureTest.getTorqueValue1());
                    tfTorqueValue2.setValue(pressureTest.getTorqueValue2() == null ? "" : pressureTest.getTorqueValue2());
                    tfTorqueValue3.setValue(pressureTest.getTorqueValue3() == null ? "" : pressureTest.getTorqueValue3());
                    tfTorqueValue4.setValue(pressureTest.getTorqueValue4() == null ? "" : pressureTest.getTorqueValue4());
                }
                btnSaveTorque.setEnabled(true);

                //7-通径
                cbPass.setValue(pressureTest.getDriftTest());
                if (!Strings.isNullOrEmpty(pressureTest.getDriftTestBy())) {
                    User user = userService.getByName(pressureTest.getDriftTestBy());
                    cbDirffTestBy.setValue(user);
                }
                //8-测试人员+日期
                labTestBy0.setValue(Strings.isNullOrEmpty(pressureTest.getTestBy()) ? RequestInfo.current().getUserName() : pressureTest.getTestBy());
                labDate0.setValue(Strings.isNullOrEmpty(pressureTest.getTestDate()) ? myFmt0.format(new Date()) : pressureTest.getTestDate());

                //初始化压力曲线图
                DrawDiagram(productSn);

            } else if ("井口".equals(cbPressureType.getValue())) {
                //1-表头
                labPartNoJK.setValue(Strings.isNullOrEmpty(pressureTest.getPartNo()) ? productionOrder.getProductId() : pressureTest.getPartNo());
                labPartRevJK.setValue(Strings.isNullOrEmpty(pressureTest.getPartRev()) ? productionOrder.getProductVersionId() : pressureTest.getPartRev());
                labWorkOrderJK.setValue(Strings.isNullOrEmpty(pressureTest.getWorkOrder()) ? productionOrder.getProductOrderId() : pressureTest.getWorkOrder());
                labSerialNoJK.setValue(Strings.isNullOrEmpty(pressureTest.getProductSN()) ? productSn : pressureTest.getProductSN());
                labTestProcedureJK.setValue(productInfo.getPressureInspectionProcedure() == null ? "" : productInfo.getPressureInspectionProcedure() + " REV" + productInfo.getPressureInspectionProcedureVersion());
                labPslLevelJK.setValue(productInfo.getPSLRating() == null ? "" : productInfo.getPSLRating());
                labDescriptionJK.setValue(productInfo.getProductDesc() == null ? "" : productInfo.getProductDesc());
                tfProcedureNoJK.setValue(Strings.isNullOrEmpty(pressureTest.getProcedureNo()) ? "" : pressureTest.getProcedureNo());
                tfGageNoSizeJK.setValue(Strings.isNullOrEmpty(pressureTest.getGageNoSize()) ? "" : pressureTest.getGageNoSize());
                //2-本体
                ckOpenBodyJK.setValue(pressureTest.isJkOpenBodyFlag());
                if (pressureTest.isJkOpenBodyFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkOpenBodyStationName(), pressureTest.getJkOpenBodyStationNo());
                    cbStationOpenBodyJK.setSelectedItem(stationEquipment);
                    jkOpenBodyRecord.setSelectedItem(pressureTest.getJkOpenBodyRecord());
                    //
                    labOpenPoneJK.setValue(String.valueOf(pressureTest.getJkOpenP1()));
                    labOpenToneJK.setValue(String.valueOf(pressureTest.getJkOpenT1()));
                    labOpenRoneJK.setValue(pressureTest.getJkOpenR1());

                    labOpenPtwoJK.setValue(String.valueOf(pressureTest.getJkOpenP2()));
                    labOpenTtwoJK.setValue(String.valueOf(pressureTest.getJkOpenT2()));
                    labOpenRtwoJK.setValue(pressureTest.getJkOpenR2());

                    labOpenPthreeJK.setValue(String.valueOf(pressureTest.getJkOpenP3()));
                    labOpenTthreeJK.setValue(String.valueOf(pressureTest.getJkOpenT3()));
                    labOpenRthreeJK.setValue(pressureTest.getJkOpenR3());
                    //
                    labOpenBodySignatureJK.setValue(pressureTest.getJkOpenUser());
                    labOpenBodyDateJK.setValue(pressureTest.getJkOpenDate());
                    DrawDiagramJK(productSn);
                }
                //3-转换接头本体
                ckCrossoverBodyJK.setValue(pressureTest.isJkCrossoverBodyFlag());
                if (pressureTest.isJkCrossoverBodyFlag()) {
                    StationEquipment stationEquipmentTop = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkCrossoverBodyTopStationName(), pressureTest.getJkCrossoverBodyTopStationNo());
                    cbStationCrossBodyTopJK.setSelectedItem(stationEquipmentTop);
                    jkCrossoverBodyTopRecord.setSelectedItem(pressureTest.getJkCrossoverBodyTopRecord());

                    StationEquipment stationEquipmentBtm = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkCrossoverBodyBtmStationName(), pressureTest.getJkCrossoverBodyBtmStationNo());
                    cbStationCrossBodyBtmJK.setSelectedItem(stationEquipmentBtm);
                    jkCrossoverBodyBtmRecord.setSelectedItem(pressureTest.getJkCrossoverBodyBtmRecord());
                    //
                    labCrossoverBodyTOPPoneJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopP1()));
                    labCrossoverBodyTOPToneJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopT1()));
                    labCrossoverBodyTOPRoneJK.setValue(pressureTest.getJkCrossoverBodyTopR1());

                    labCrossoverBodyTOPPtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopP2()));
                    labCrossoverBodyTOPTtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopT2()));
                    labCrossoverBodyTOPRtwoJK.setValue(pressureTest.getJkCrossoverBodyTopR2());

                    labCrossoverBodyTOPPthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopP3()));
                    labCrossoverBodyTOPTthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyTopT3()));
                    labCrossoverBodyTOPRthreeJK.setValue(pressureTest.getJkCrossoverBodyTopR3());
                    //
                    labCrossoverBodyBTMPoneJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmP1()));
                    labCrossoverBodyBTMToneJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmT1()));
                    labCrossoverBodyBTMRoneJK.setValue(pressureTest.getJkCrossoverBodyBtmR1());

                    labCrossoverBodyBTMPtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmP2()));
                    labCrossoverBodyBTMTtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmT2()));
                    labCrossoverBodyBTMRtwoJK.setValue(pressureTest.getJkCrossoverBodyBtmR2());

                    labCrossoverBodyBTMPthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmP3()));
                    labCrossoverBodyBTMTthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverBodyBtmT3()));
                    labCrossoverBodyBTMRthreeJK.setValue(pressureTest.getJkCrossoverBodyBtmR3());
                    //
                    labCrossoverBodySignatureJK.setValue(pressureTest.getJkCrossoverBodyUser());
                    labCrossoverBodyDateJK.setValue(pressureTest.getJkCrossoverBodyDate());

                }
                //4-采油树
                ckChristmasTreeJK.setValue(pressureTest.isJkChristmasTreeFlag());
                if (pressureTest.isJkChristmasTreeFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkChristmasTreeStationName(), pressureTest.getJkChristmasTreeStationNo());
                    cbStationChristmasJK.setSelectedItem(stationEquipment);
                    jkChristmasTreeRecord.setSelectedItem(pressureTest.getJkChristmasTreeRecord());
                    //
                    labChristmasTreePoneJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeP1()));
                    labChristmasTreeToneJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeT1()));
                    labChristmasTreeRoneJK.setValue(pressureTest.getJkChristmasTreeR1());

                    labChristmasTreePtwoJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeP2()));
                    labChristmasTreeTtwoJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeT2()));
                    labChristmasTreeRtwoJK.setValue(pressureTest.getJkChristmasTreeR2());

                    labChristmasTreePthreeJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeP3()));
                    labChristmasTreeTthreeJK.setValue(String.valueOf(pressureTest.getJkChristmasTreeT3()));
                    labChristmasTreeRthreeJK.setValue(pressureTest.getJkChristmasTreeR3());
                    //
                    labChristmasTreeSignatureJK.setValue(pressureTest.getJkChristmasTreeUser());
                    labChristmasTreeDateJK.setValue(pressureTest.getJkChristmasTreeDate());

                }
                //5-井口头
                ckWellheadAssemblyJK.setValue(pressureTest.isJkWellheadAssemblyFlag());
                if (pressureTest.isJkWellheadAssemblyFlag()) {
                    StationEquipment stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkWellheadAssemblyStationName(), pressureTest.getJkWellheadAssemblyStationNo());
                    cbStationWellheadJK.setSelectedItem(stationEquipment);
                    jkWellheadAssemblyRecord.setSelectedItem(pressureTest.getJkWellheadAssemblyRecord());
                    //
                    labWellheadAssemblyPoneJK.setValue(String.valueOf(pressureTest.getJkWellheadP1()));
                    labWellheadAssemblyToneJK.setValue(String.valueOf(pressureTest.getJkWellheadT1()));
                    labWellheadAssemblyRoneJK.setValue(pressureTest.getJkWellheadR1());

                    labWellheadAssemblyPtwoJK.setValue(String.valueOf(pressureTest.getJkWellheadP2()));
                    labWellheadAssemblyTtwoJK.setValue(String.valueOf(pressureTest.getJkWellheadT2()));
                    labWellheadAssemblyRtwoJK.setValue(pressureTest.getJkWellheadR2());

                    labWellheadAssemblyPthreeJK.setValue(String.valueOf(pressureTest.getJkWellheadP3()));
                    labWellheadAssemblyTthreeJK.setValue(String.valueOf(pressureTest.getJkWellheadT3()));
                    labWellheadAssemblyRthreeJK.setValue(pressureTest.getJkWellheadR3());
                    //
                    labWellheadAssemblySignatureJK.setValue(pressureTest.getTestBy());
                    labWellheadAssemblyDateJK.setValue(pressureTest.getTestDate());

                }
                //6-转换接头组件
                ckCrossoverAssemblyJK.setValue(pressureTest.isJkCrossoverAssemblyFlag());
                if (pressureTest.isJkCrossoverAssemblyFlag()) {
                    StationEquipment stationEquipmentTop = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkCrossoverAssemblyTopStationName(), pressureTest.getJkCrossoverAssemblyTopStationNo());
                    cbStationCrossAssemTopJK.setSelectedItem(stationEquipmentTop);
                    jkCrossoverAssemblyTopRecord.setSelectedItem(pressureTest.getJkCrossoverAssemblyTopRecord());

                    StationEquipment stationEquipmentBtm = stationEquipmentService.getStationEquipmentByNameNo(pressureTest.getJkCrossoverAssemblyBtmStationName(), pressureTest.getJkCrossoverAssemblyBtmStationNo());
                    cbStationCrossAssemBtmJK.setSelectedItem(stationEquipmentBtm);
                    jkCrossoverAssemblyBtmRecord.setSelectedItem(pressureTest.getJkCrossoverAssemblyBtmRecord());
                    //
                    labCrossoverAssemblyTOPPoneJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopP1()));
                    labCrossoverAssemblyTOPToneJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopT1()));
                    labCrossoverAssemblyTOPRoneJK.setValue(pressureTest.getJkCrossoverAssemblyTopR1());

                    labCrossoverAssemblyTOPPtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopP2()));
                    labCrossoverAssemblyTOPTtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopT2()));
                    labCrossoverAssemblyTOPRtwoJK.setValue(pressureTest.getJkCrossoverAssemblyTopR2());

                    labCrossoverAssemblyTOPPthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopP3()));
                    labCrossoverAssemblyTOPTthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyTopT3()));
                    labCrossoverAssemblyTOPRthreeJK.setValue(pressureTest.getJkCrossoverAssemblyTopR3());
                    //
                    labCrossoverAssemblyBTMPoneJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmP1()));
                    labCrossoverAssemblyBTMToneJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmT1()));
                    labCrossoverAssemblyBTMRoneJK.setValue(pressureTest.getJkCrossoverAssemblyBtmR1());

                    labCrossoverAssemblyBTMPtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmP2()));
                    labCrossoverAssemblyBTMTtwoJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmT2()));
                    labCrossoverAssemblyBTMRtwoJK.setValue(pressureTest.getJkCrossoverAssemblyBtmR2());

                    labCrossoverAssemblyBTMPthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmP3()));
                    labCrossoverAssemblyBTMTthreeJK.setValue(String.valueOf(pressureTest.getJkCrossoverAssemblyBtmT3()));
                    labCrossoverAssemblyBTMRthreeJK.setValue(pressureTest.getJkCrossoverAssemblyBtmR3());
                    //
                    labCrossoverAssemblySignatureJK.setValue(pressureTest.getJkCrossoverAssemblyUser());
                    labCrossoverAssemblyDateJK.setValue(pressureTest.getJkCrossoverAssemblyDate());

                }
                //7-通径

                cbPassJK.setValue(pressureTest.getDriftTest());
                if (!Strings.isNullOrEmpty(pressureTest.getDriftTestBy())) {
                    User user = userService.getByName(pressureTest.getDriftTestBy());
                    cbDirffTestByJK.setValue(user);
                }
                //曲线图
                DrawDiagramJK(productSn);
            }
        }
    }

    /***
     * 生成报告
     */
    private void btnCreateReportMethod() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        if (caConfig == null) {
            throw new PlatformException("请先配置文档报告存放路径");
        }
        String path = caConfig.getConfigValue() + AppConstant.PRODUCTION_PREFIX + AppConstant.PRESSURE_REPORT;

        try {
            PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
            if (pressureTest == null) {
                pressureTest = new PressureTest();
            }
            pressureTest.setProductSN(tfProductSn.getValue().trim());
            pressureTest.setTorqueDriftSaved(true);
            pressureTest.setTestType(cbPressureType.getValue());
            //保存压力记录
            if ("阀门".equals(cbPressureType.getValue())) {
                pressureTest.setPartNo(labPartNo.getValue());
                pressureTest.setPartRev(labPartRev.getValue());
                pressureTest.setWorkOrder(labWorkOrder.getValue());
                pressureTest.setProductSN(labSerialNo.getValue());
                pressureTest.setGageNoSize(tfGageNoSize.getValue());
                pressureTest.setProcedureNo(tfProcedureNo.getValue());
                pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                pressureTest.setTestBy(labTestBy0.getValue());
                pressureTest.setTestDate(labDate0.getValue());

                pressureTest.setTorqueValue1(tfTorqueValue1.getValue() == null ? "" : tfTorqueValue1.getValue());
                pressureTest.setTorqueValue2(tfTorqueValue2.getValue() == null ? "" : tfTorqueValue2.getValue());
                pressureTest.setTorqueValue3(tfTorqueValue3.getValue() == null ? "" : tfTorqueValue3.getValue());
                pressureTest.setTorqueValue4(tfTorqueValue4.getValue() == null ? "" : tfTorqueValue4.getValue());
//                final String blowdownTorque = productInformationService.getByNoRev(labPartNo.getValue(), labPartRev.getValue()).getBlowdownTorque();
//                boolean blowdownPassed = false;
//                if (blowdownTorque != null && RegExpValidatorUtils.isNumber(blowdownTorque)) {
//                    if (!(RegExpValidatorUtils.isNumber(tfTorqueValue1.getValue())
//                            && RegExpValidatorUtils.isNumber(tfTorqueValue2.getValue())
//                            && RegExpValidatorUtils.isNumber(tfTorqueValue3.getValue())
//                            && RegExpValidatorUtils.isNumber(tfTorqueValue4.getValue()))) {
//                        NotificationUtils.notificationWarning(I18NUtility.getValue("Pressuretest.ValidTorque", "Please input valid torque!"));
//                        return;
//                    } else if ((Double.parseDouble(tfTorqueValue1.getValue()) <= Double.parseDouble(blowdownTorque)) &&
//                            (Double.parseDouble(tfTorqueValue2.getValue()) <= Double.parseDouble(blowdownTorque)) &&
//                            (Double.parseDouble(tfTorqueValue3.getValue()) <= Double.parseDouble(blowdownTorque)) &&
//                            (Double.parseDouble(tfTorqueValue4.getValue()) <= Double.parseDouble(blowdownTorque))) {
//                        blowdownPassed = true;
//                    }
//                } else {
//                    blowdownPassed = true;
//                }
                pressureTest.setDriftTest(cbPass.getValue());
                pressureTest.setDriftTestBy(cbDirffTestBy.getValue() == null ? null : cbDirffTestBy.getValue().getName());

                String testResult = "OK";
                //检查产品对应标准是否都进行了测试
                if (ckOpenBody.isEnabled()) {
                    if (Strings.isNullOrEmpty(labOpenComments.getValue())) {
                        testResult = "NG";
                    } else {
                        pressureTest = setOpenFM(pressureTest);
                    }
                }
                if (ckDownstream.isEnabled()) {
                    if (Strings.isNullOrEmpty(labDownComments.getValue())) {
                        testResult = "NG";
                    } else {
                        pressureTest = setDownFM(pressureTest);
                    }
                }
                if (ckUpstream.isEnabled()) {
                    if (Strings.isNullOrEmpty(labUpComments.getValue())) {
                        testResult = "NG";
                    } else {
                        pressureTest = setUpFM(pressureTest);
                    }
                }

                //检查测试的压力项是否都合格
                if ("NG".equals(labOpenComments.getValue()) || "NG".equals(labDownComments.getValue()) || "NG".equals(labUpComments.getValue())) {
                    testResult = "NG";
                }

                pressureTest.setTestResult(testResult);
                pressureTestService.save(pressureTest);
                if ("NG".equals(testResult)) {
                    ConfirmDialog.show(getUI(),
                            I18NUtility.getValue("PressureTest.NgSure", "This Product PressureTest Result is NG, Are you sure to save the record ?"),
                            result -> {
                                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                    //生成doc报告
                                    try {
                                        createDocReport(path);
//                                        if (cbDirffTestBy.getValue() != null) {
                                        wordToPDF(path + tfProductSn.getValue() + ".doc");
//                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    //生成doc报告
                    createDocReport(path);
//                    if (cbDirffTestBy.getValue() != null) {
                    wordToPDF(path + tfProductSn.getValue() + ".doc");
//                    }
                }


            } else if ("井口".equals(cbPressureType.getValue())) {
                pressureTest.setPartNo(labPartNoJK.getValue());
                pressureTest.setPartRev(labPartRevJK.getValue());
                pressureTest.setWorkOrder(labWorkOrderJK.getValue());
                pressureTest.setProductSN(labSerialNoJK.getValue());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest.setTestBy(RequestInfo.current().getUserName());
                pressureTest.setTestDate(myFmt0.format(new Date()));

                if (cbPassJK.getValue()) {
                    pressureTest.setDriftTest(cbPassJK.getValue());
                    pressureTest.setDriftTestBy(cbDirffTestByJK.getValue() == null ? RequestInfo.current().getUserName() : cbDirffTestByJK.getValue().getName());
                    pressureTest.setDriftTestDate(myFmt0.format(new Date()));
                }

                String testResult = "OK";
                if (ckOpenBodyJK.isEnabled()) {//要求检测
                    if (ckOpenBodyJK.getValue()) {//检测
                        if ("NG".equals(labOpenRoneJK.getValue()) || "NG".equals(labOpenRtwoJK.getValue()) || "NG".equals(labOpenRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if (!pressureTest.isJkOpenBodyFlag() && !Strings.isNullOrEmpty(labOpenBodySignatureJK.getValue().trim())) {
                            pressureTest = setOpenJK(pressureTest);
                        }
                    } else {//未检测
                        testResult = "NG";
                    }
                }
                if (ckCrossoverBodyJK.isEnabled()) {//要求检测

                    if (ckCrossoverBodyJK.getValue()) {//检测
                        if ("NG".equals(labCrossoverBodyTOPRoneJK.getValue()) || "NG".equals(labCrossoverBodyTOPRtwoJK.getValue()) || "NG".equals(labCrossoverBodyTOPRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if ("NG".equals(labCrossoverBodyBTMRoneJK.getValue()) || "NG".equals(labCrossoverBodyBTMRtwoJK.getValue()) || "NG".equals(labCrossoverBodyBTMRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if (!pressureTest.isJkCrossoverBodyFlag() && !Strings.isNullOrEmpty(labCrossoverBodySignatureJK.getValue().trim())) {
                            pressureTest = setCrossBodyJK(pressureTest);
                        }
                    } else {//未检测
                        testResult = "NG";
                    }
                }
                if (ckChristmasTreeJK.isEnabled()) {//要求检测

                    if (ckChristmasTreeJK.getValue()) {//检测
                        if ("NG".equals(labChristmasTreeRoneJK.getValue()) || "NG".equals(labChristmasTreeRtwoJK.getValue()) || "NG".equals(labChristmasTreeRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if (!pressureTest.isJkChristmasTreeFlag() && !Strings.isNullOrEmpty(labChristmasTreeSignatureJK.getValue().trim())) {
                            pressureTest = setChristmasJK(pressureTest);
                        }
                    } else {//未检测
                        testResult = "NG";
                    }
                }
                if (ckWellheadAssemblyJK.isEnabled()) {//要求检测

                    if (ckWellheadAssemblyJK.getValue()) {//检测
                        if ("NG".equals(labWellheadAssemblyRoneJK.getValue()) || "NG".equals(labWellheadAssemblyRtwoJK.getValue()) || "NG".equals(labWellheadAssemblyRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if (!pressureTest.isJkWellheadAssemblyFlag() && !Strings.isNullOrEmpty(labWellheadAssemblySignatureJK.getValue().trim())) {
                            pressureTest = setWellheadJK(pressureTest);
                        }
                    } else {//未检测
                        testResult = "NG";
                    }
                }
                if (ckCrossoverAssemblyJK.isEnabled()) {//要求检测

                    if (ckCrossoverAssemblyJK.getValue()) {//检测
                        if ("NG".equals(labCrossoverAssemblyTOPRoneJK.getValue()) || "NG".equals(labCrossoverAssemblyTOPRtwoJK.getValue()) || "NG".equals(labCrossoverAssemblyTOPRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if ("NG".equals(labCrossoverAssemblyBTMRoneJK.getValue()) || "NG".equals(labCrossoverAssemblyBTMRtwoJK.getValue()) || "NG".equals(labCrossoverAssemblyBTMRthreeJK.getValue())) {
                            testResult = "NG";//检测结果NG
                        }
                        if (!pressureTest.isJkCrossoverAssemblyFlag() && !Strings.isNullOrEmpty(labCrossoverAssemblySignatureJK.getValue().trim())) {
                            pressureTest = setCrossAssembJK(pressureTest);
                        }
                    } else {//未检测
                        testResult = "NG";
                    }
                }

                pressureTest.setTestResult(testResult);
                pressureTestService.save(pressureTest);
                if ("NG".equals(testResult)) {
                    ConfirmDialog.show(getUI(),
                            I18NUtility.getValue("PressureTest.NgSure", "This Product PressureTest Result is NG, Are you sure to save the record ?"),
                            result -> {
                                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                                    //生成doc报告
                                    try {
                                        createDocReportJK(path);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    //生成doc报告
                    createDocReportJK(path);
                }
            }
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }

    //FM阀门 按钮button处理事件
    //阀门-本体
    private void btnConfirmBodyFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationBodyFM.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationBodyFM.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationBodyFM.getValue().getEquipmentType())) {
                    getValidataBysqliteFM(productSn, ip, cbRecordBodyFM);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, cbRecordBodyFM);
                }
                btnSaveOpenFM.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetBodyFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNo.getValue().trim(), PressureTypeEnum.OPEN_BODY_FM.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationBodyFM.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteFM(cbRecordBodyFM.getValue(), equipment, openArr, labOpenFMs, "Open", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewFM(pressureRuler, equipment, cbRecordBodyFM.getValue(), labOpenFMs, "Open");
                }
                btnSaveOpenFM.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagram(productSn);
        }

    }

    private void btnSaveOpenFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSize.getValue());
                pressureTest.setProcedureNo(tfProcedureNo.getValue());
                pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                pressureTest.setBlowDownTest(ckOpenUnder.getValue());

                pressureTest = setOpenFM(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("本体测试数据保存成功！");
            }
        }
    }

    private PressureTest setOpenFM(PressureTest pressureTest) {
        pressureTest.setFmOpenBodyFlag(ckOpenBody.getValue());
        pressureTest.setFmOpenBodyStationName(cbStationBodyFM.getValue().getStation());
        pressureTest.setFmOpenBodyStationNo(cbStationBodyFM.getValue().getEquipmentNo());
        pressureTest.setFmOpenBodyRecord(cbRecordBodyFM.getValue());

        pressureTest.setFmOpenP1(Strings.isNullOrEmpty(labOpenPone.getValue()) ? 0d : Double.parseDouble(labOpenPone.getValue().trim()));
        pressureTest.setFmOpenP2(Strings.isNullOrEmpty(labOpenPtwo.getValue()) ? 0d : Double.parseDouble(labOpenPtwo.getValue().trim()));
        pressureTest.setFmOpenP3(Strings.isNullOrEmpty(labOpenPthree.getValue()) ? 0d : Double.parseDouble(labOpenPthree.getValue().trim()));

        pressureTest.setFmOpenT1(Strings.isNullOrEmpty(labOpenTone.getValue()) ? 0d : Double.parseDouble(labOpenTone.getValue().trim()));
        pressureTest.setFmOpenT2(Strings.isNullOrEmpty(labOpenTtwo.getValue()) ? 0d : Double.parseDouble(labOpenTtwo.getValue().trim()));
        pressureTest.setFmOpenT3(Strings.isNullOrEmpty(labOpenTthree.getValue()) ? 0d : Double.parseDouble(labOpenTthree.getValue().trim()));

        pressureTest.setFmOpenResult(labOpenComments.getValue().trim());
        pressureTest.setFmOpenUser(RequestInfo.current().getUserName());
        pressureTest.setFmOpenDate(myFmt0.format(new Date()));
        return pressureTest;
    }

    //阀门-下游
    private void btnConfirmDownFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationDownFM.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationDownFM.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationDownFM.getValue().getEquipmentType())) {
                    getValidataBysqliteFM(productSn, ip, cbRecordDownFM);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, cbRecordDownFM);
                }
                btnSaveDownFM.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetDownFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            // 获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNo.getValue().trim(), PressureTypeEnum.DOWNSTREAM_FM.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            // 获取压力数据
            StationEquipment equipment = cbStationDownFM.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteFM(cbRecordDownFM.getValue(), equipment, downArr, labDownFMs, "Down", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewFM(pressureRuler, equipment, cbRecordDownFM.getValue(), labDownFMs, "Down");
                }
                btnSaveDownFM.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //判断扭矩值
            if (pressureRuler.getTorqueValue() != 0D) {
                if (Strings.isNullOrEmpty(tfTorqueValue1.getValue().trim())
                        || Strings.isNullOrEmpty(tfTorqueValue2.getValue().trim())) {
                    labDownComments.setValue("NG");
                } else {
                    if (Double.parseDouble(tfTorqueValue1.getValue().trim()) > pressureRuler.getTorqueValue()
                            || Double.parseDouble(tfTorqueValue2.getValue().trim()) > pressureRuler.getTorqueValue()) {
                        labDownComments.setValue("NG");
                    }
                }
            }
            DrawDiagram(productSn);
        }
    }

    private void btnSaveDownFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSize.getValue());
                pressureTest.setProcedureNo(tfProcedureNo.getValue());
                pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                pressureTest.setBlowDownTest(ckOpenUnder.getValue());

                pressureTest = setDownFM(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("下游密封测试数据保存成功！");
            }
        }
    }

    private PressureTest setDownFM(PressureTest pressureTest) {
        pressureTest.setFmDownFlag(ckDownstream.getValue());
        pressureTest.setFmDownStationName(cbStationDownFM.getValue().getStation());
        pressureTest.setFmDownStationNo(cbStationDownFM.getValue().getEquipmentNo());
        pressureTest.setFmDownRecord(cbRecordDownFM.getValue());

        pressureTest.setFmDownP1(Strings.isNullOrEmpty(labDownPone.getValue()) ? 0d : Double.parseDouble(labDownPone.getValue().trim()));
        pressureTest.setFmDownP2(Strings.isNullOrEmpty(labDownPtwo.getValue()) ? 0d : Double.parseDouble(labDownPtwo.getValue().trim()));
        pressureTest.setFmDownP3(Strings.isNullOrEmpty(labDownPthree.getValue()) ? 0d : Double.parseDouble(labDownPthree.getValue().trim()));

        pressureTest.setFmDownT1(Strings.isNullOrEmpty(labDownTone.getValue()) ? 0d : Double.parseDouble(labDownTone.getValue().trim()));
        pressureTest.setFmDownT2(Strings.isNullOrEmpty(labDownTtwo.getValue()) ? 0d : Double.parseDouble(labDownTtwo.getValue().trim()));
        pressureTest.setFmDownT3(Strings.isNullOrEmpty(labDownTthree.getValue()) ? 0d : Double.parseDouble(labDownTthree.getValue().trim()));

        pressureTest.setFmDownResult(labDownComments.getValue().trim());
        pressureTest.setFmDownUser(RequestInfo.current().getUserName());
        pressureTest.setFmDownDate(myFmt0.format(new Date()));
        return pressureTest;
    }

    //阀门-上游
    private void btnConfirmUpFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationUpFM.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationUpFM.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationUpFM.getValue().getEquipmentType())) {
                    getValidataBysqliteFM(productSn, ip, cbRecordUpFM);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, cbRecordUpFM);
                }
                btnSaveUpFM.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetUpFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            // 获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNo.getValue().trim(), PressureTypeEnum.UPSTREAM_FM.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            // 获取压力数据
            StationEquipment equipment = cbStationUpFM.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteFM(cbRecordUpFM.getValue(), equipment, upArr, labUpFMs, "Up", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewFM(pressureRuler, equipment, cbRecordUpFM.getValue(), labUpFMs, "Up");
                }
                btnSaveUpFM.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //判断扭矩值
            if (pressureRuler.getTorqueValue() != 0D) {
                if (Strings.isNullOrEmpty(tfTorqueValue3.getValue().trim())
                        || Strings.isNullOrEmpty(tfTorqueValue4.getValue().trim())) {
                    labUpComments.setValue("NG");
                } else {
                    if (Double.parseDouble(tfTorqueValue3.getValue().trim()) > pressureRuler.getTorqueValue()
                            || Double.parseDouble(tfTorqueValue4.getValue().trim()) > pressureRuler.getTorqueValue()) {
                        labUpComments.setValue("NG");
                    }
                }
            }
            DrawDiagram(productSn);
        }
    }

    private void btnSaveUpFMMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSize.getValue());
                pressureTest.setProcedureNo(tfProcedureNo.getValue());
                pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                pressureTest.setBlowDownTest(ckOpenUnder.getValue());

                pressureTest = setUpFM(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("上游密封测试数据保存成功！");
            }
        }
    }

    private PressureTest setUpFM(PressureTest pressureTest) {
        pressureTest.setFmUpFlag(ckUpstream.getValue());
        pressureTest.setFmUpStationName(cbStationUpFM.getValue().getStation());
        pressureTest.setFmUpStationNo(cbStationUpFM.getValue().getEquipmentNo());
        pressureTest.setFmUpRecord(cbRecordUpFM.getValue());

        pressureTest.setFmUpP1(Strings.isNullOrEmpty(labUpPone.getValue()) ? 0d : Double.parseDouble(labUpPone.getValue().trim()));
        pressureTest.setFmUpP2(Strings.isNullOrEmpty(labUpPtwo.getValue()) ? 0d : Double.parseDouble(labUpPtwo.getValue().trim()));
        pressureTest.setFmUpP3(Strings.isNullOrEmpty(labUpPthree.getValue()) ? 0d : Double.parseDouble(labUpPthree.getValue().trim()));

        pressureTest.setFmUpT1(Strings.isNullOrEmpty(labUpTone.getValue()) ? 0d : Double.parseDouble(labUpTone.getValue().trim()));
        pressureTest.setFmUpT2(Strings.isNullOrEmpty(labUpTtwo.getValue()) ? 0d : Double.parseDouble(labUpTtwo.getValue().trim()));
        pressureTest.setFmUpT3(Strings.isNullOrEmpty(labUpTthree.getValue()) ? 0d : Double.parseDouble(labUpTthree.getValue().trim()));

        pressureTest.setFmUpResult(labUpComments.getValue().trim());
        pressureTest.setFmUpUser(RequestInfo.current().getUserName());
        pressureTest.setFmUpDate(myFmt0.format(new Date()));
        return pressureTest;
    }

    //扭矩+通径
    private void btnSaveTorqueMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSize.getValue());
                pressureTest.setProcedureNo(tfProcedureNo.getValue());
                pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                pressureTest.setBlowDownTest(ckOpenUnder.getValue());

                pressureTest.setTorqueValue1(tfTorqueValue1.getValue() == null ? "" : tfTorqueValue1.getValue());
                pressureTest.setTorqueValue2(tfTorqueValue1.getValue() == null ? "" : tfTorqueValue2.getValue());
                pressureTest.setTorqueValue3(tfTorqueValue1.getValue() == null ? "" : tfTorqueValue3.getValue());
                pressureTest.setTorqueValue4(tfTorqueValue1.getValue() == null ? "" : tfTorqueValue4.getValue());
                pressureTest.setTorqueDriftSaved(true);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("扭矩值保存成功！");
            }
        }
    }

    private void btnSaveDirftMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    NotificationUtils.notificationError("序列号对应压力数据尚未上传，请先进行压力数据上传！");
                } else {
                    pressureTest.setGageNoSize(tfGageNoSize.getValue());
                    pressureTest.setProcedureNo(tfProcedureNo.getValue());
                    pressureTest.setTorqueSensorNo(tfTorqueSensor.getValue());
                    pressureTest.setBlowDownTest(ckOpenUnder.getValue());

                    pressureTest.setDriftTest(cbPass.getValue());
                    pressureTest.setDriftTestBy(cbDirffTestBy.getValue() == null ? null : cbDirffTestBy.getValue().getName());
                    pressureTestService.save(pressureTest);
                    NotificationUtils.notificationInfo("通径测试结果保存成功！");
                }
            }
        }
    }

    //--JK井口 按钮button处理事件
    //井口-本体
    private void btnConfirmOpenBodyJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationOpenBodyJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationOpenBodyJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationOpenBodyJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkOpenBodyRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkOpenBodyRecord);
                }
                btnSaveOpenJK.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetOpenBodyJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.OPEN_BODY_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationOpenBodyJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkOpenBodyRecord.getValue(), equipment, labOpenJks, "OpenBodyJk", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkOpenBodyRecord.getValue(), labOpenJks, "OpenBodyJk");
                }
                labOpenBodySignatureJK.setValue(RequestInfo.current().getUserName());
                labOpenBodyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveOpenJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }
    }

    private void btnSaveOpenJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest = setOpenJK(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("本体测试数据保存成功！");
            }
        }
    }

    private PressureTest setOpenJK(PressureTest pressureTest) {
        pressureTest.setJkOpenBodyFlag(ckOpenBodyJK.getValue());
        pressureTest.setJkOpenBodyStationName(cbStationOpenBodyJK.getValue().getStation());
        pressureTest.setJkOpenBodyStationNo(cbStationOpenBodyJK.getValue().getEquipmentNo());
        pressureTest.setJkOpenBodyRecord(jkOpenBodyRecord.getValue());

        pressureTest.setJkOpenP1(Strings.isNullOrEmpty(labOpenPoneJK.getValue()) ? 0d : Double.parseDouble(labOpenPoneJK.getValue().trim()));
        pressureTest.setJkOpenP2(Strings.isNullOrEmpty(labOpenPtwoJK.getValue()) ? 0d : Double.parseDouble(labOpenPtwoJK.getValue().trim()));
        pressureTest.setJkOpenP3(Strings.isNullOrEmpty(labOpenPthreeJK.getValue()) ? 0d : Double.parseDouble(labOpenPthreeJK.getValue().trim()));

        pressureTest.setJkOpenT1(Strings.isNullOrEmpty(labOpenToneJK.getValue()) ? 0d : Double.parseDouble(labOpenToneJK.getValue().trim()));
        pressureTest.setJkOpenT2(Strings.isNullOrEmpty(labOpenTtwoJK.getValue()) ? 0d : Double.parseDouble(labOpenTtwoJK.getValue().trim()));
        pressureTest.setJkOpenT3(Strings.isNullOrEmpty(labOpenTthreeJK.getValue()) ? 0d : Double.parseDouble(labOpenTthreeJK.getValue().trim()));

        pressureTest.setJkOpenR1(Strings.isNullOrEmpty(labOpenRoneJK.getValue()) ? "" : labOpenRoneJK.getValue().trim());
        pressureTest.setJkOpenR2(Strings.isNullOrEmpty(labOpenRtwoJK.getValue()) ? "" : labOpenRtwoJK.getValue().trim());
        pressureTest.setJkOpenR3(Strings.isNullOrEmpty(labOpenRthreeJK.getValue()) ? "" : labOpenRthreeJK.getValue().trim());

        pressureTest.setJkOpenUser(labOpenBodySignatureJK.getValue());
        pressureTest.setJkOpenDate(labOpenBodyDateJK.getValue());
        return pressureTest;
    }

    //井口-转换头本体
    private void btnConfirmCrossBodyTopJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationCrossBodyTopJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationCrossBodyTopJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationCrossBodyTopJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkCrossoverBodyTopRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkCrossoverBodyTopRecord);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetCrossBodyTJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.CROSS_OVER_BODY_TOP_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationCrossBodyTopJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkCrossoverBodyTopRecord.getValue(), equipment, labCrossBodyTopJks, "CrossoverBodyTopJK", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkCrossoverBodyTopRecord.getValue(), labCrossBodyTopJks, "CrossoverBodyTopJK");
                }
                labCrossoverBodySignatureJK.setValue(RequestInfo.current().getUserName());
                labCrossoverBodyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveCrossBodyJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }
    }

    private void btnConfirmCrossBodyBtmJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationCrossBodyBtmJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationCrossBodyBtmJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationCrossBodyBtmJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkCrossoverBodyBtmRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkCrossoverBodyBtmRecord);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetCrossBodyBJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.CROSS_OVER_BODY_BTM_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationCrossBodyBtmJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkCrossoverBodyBtmRecord.getValue(), equipment, labCrossBodyBtmJks, "CrossoverBodyBtmJK", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkCrossoverBodyBtmRecord.getValue(), labCrossBodyBtmJks, "CrossoverBodyBtmJK");
                }
                labCrossoverBodySignatureJK.setValue(RequestInfo.current().getUserName());
                labCrossoverBodyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveCrossBodyJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }
    }

    private void btnSaveCrossBodyJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest = setCrossBodyJK(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("接口头本体测试数据保存成功！");
            }
        }
    }

    private PressureTest setCrossBodyJK(PressureTest pressureTest) {
        pressureTest.setJkCrossoverBodyFlag(ckCrossoverBodyJK.getValue());
        pressureTest.setJkCrossoverBodyTopStationName(cbStationCrossBodyTopJK.getValue().getStation());
        pressureTest.setJkCrossoverBodyTopStationNo(cbStationCrossBodyTopJK.getValue().getEquipmentNo());
        pressureTest.setJkCrossoverBodyTopRecord(jkCrossoverBodyTopRecord.getValue());

        pressureTest.setJkCrossoverBodyBtmStationName(cbStationCrossBodyBtmJK.getValue().getStation());
        pressureTest.setJkCrossoverBodyBtmStationNo(cbStationCrossBodyBtmJK.getValue().getEquipmentNo());
        pressureTest.setJkCrossoverBodyBtmRecord(jkCrossoverBodyBtmRecord.getValue());
        //top
        pressureTest.setJkCrossoverBodyTopP1(Strings.isNullOrEmpty(labCrossoverBodyTOPPoneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPPoneJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyTopP2(Strings.isNullOrEmpty(labCrossoverBodyTOPPtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPPtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyTopP3(Strings.isNullOrEmpty(labCrossoverBodyTOPPthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPPthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverBodyTopT1(Strings.isNullOrEmpty(labCrossoverBodyTOPToneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPToneJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyTopT2(Strings.isNullOrEmpty(labCrossoverBodyTOPTtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPTtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyTopT3(Strings.isNullOrEmpty(labCrossoverBodyTOPTthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyTOPTthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverBodyTopR1(Strings.isNullOrEmpty(labCrossoverBodyTOPRoneJK.getValue()) ? "" : labCrossoverBodyTOPRoneJK.getValue().trim());
        pressureTest.setJkCrossoverBodyTopR2(Strings.isNullOrEmpty(labCrossoverBodyTOPRtwoJK.getValue()) ? "" : labCrossoverBodyTOPRtwoJK.getValue().trim());
        pressureTest.setJkCrossoverBodyTopR3(Strings.isNullOrEmpty(labCrossoverBodyTOPRthreeJK.getValue()) ? "" : labCrossoverBodyTOPRthreeJK.getValue().trim());
        //btm
        pressureTest.setJkCrossoverBodyBtmP1(Strings.isNullOrEmpty(labCrossoverBodyBTMPoneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMPoneJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyBtmP2(Strings.isNullOrEmpty(labCrossoverBodyBTMPtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMPtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyBtmP3(Strings.isNullOrEmpty(labCrossoverBodyBTMPthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMPthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverBodyBtmT1(Strings.isNullOrEmpty(labCrossoverBodyBTMToneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMToneJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyBtmT2(Strings.isNullOrEmpty(labCrossoverBodyBTMTtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMTtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverBodyBtmT3(Strings.isNullOrEmpty(labCrossoverBodyBTMTthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverBodyBTMTthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverBodyBtmR1(Strings.isNullOrEmpty(labCrossoverBodyBTMRoneJK.getValue()) ? "" : labCrossoverBodyBTMRoneJK.getValue().trim());
        pressureTest.setJkCrossoverBodyBtmR2(Strings.isNullOrEmpty(labCrossoverBodyBTMRtwoJK.getValue()) ? "" : labCrossoverBodyBTMRtwoJK.getValue().trim());
        pressureTest.setJkCrossoverBodyBtmR3(Strings.isNullOrEmpty(labCrossoverBodyBTMRthreeJK.getValue()) ? "" : labCrossoverBodyBTMRthreeJK.getValue().trim());

        pressureTest.setJkCrossoverBodyUser(labCrossoverBodySignatureJK.getValue());
        pressureTest.setJkCrossoverBodyDate(labCrossoverBodyDateJK.getValue());
        return pressureTest;
    }

    //井口-采油树
    private void btnConfirmChristmasJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationChristmasJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationChristmasJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationChristmasJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkChristmasTreeRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkChristmasTreeRecord);
                }
                btnSaveChristmasJK.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetChristmasJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.CHRISTMAS_TREE_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationChristmasJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkChristmasTreeRecord.getValue(), equipment, labChrisTreeJks, "ChristmasTreeJk", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkChristmasTreeRecord.getValue(), labChrisTreeJks, "ChristmasTreeJk");
                }
                labChristmasTreeSignatureJK.setValue(RequestInfo.current().getUserName());
                labChristmasTreeDateJK.setValue(myFmt0.format(new Date()));
                btnSaveChristmasJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }

    }

    private void btnSaveChristmasJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest = setChristmasJK(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("采油树测试数据保存成功！");
            }
        }
    }

    private PressureTest setChristmasJK(PressureTest pressureTest) {
        pressureTest.setJkChristmasTreeFlag(ckChristmasTreeJK.getValue());
        pressureTest.setJkChristmasTreeStationName(cbStationChristmasJK.getValue().getStation());
        pressureTest.setJkChristmasTreeStationNo(cbStationChristmasJK.getValue().getEquipmentNo());
        pressureTest.setJkChristmasTreeRecord(jkChristmasTreeRecord.getValue());

        pressureTest.setJkChristmasTreeP1(Strings.isNullOrEmpty(labChristmasTreePoneJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreePoneJK.getValue().trim()));
        pressureTest.setJkChristmasTreeP2(Strings.isNullOrEmpty(labChristmasTreePtwoJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreePtwoJK.getValue().trim()));
        pressureTest.setJkChristmasTreeP3(Strings.isNullOrEmpty(labChristmasTreePthreeJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreePthreeJK.getValue().trim()));

        pressureTest.setJkChristmasTreeT1(Strings.isNullOrEmpty(labChristmasTreeToneJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreeToneJK.getValue().trim()));
        pressureTest.setJkChristmasTreeT2(Strings.isNullOrEmpty(labChristmasTreeTtwoJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreeTtwoJK.getValue().trim()));
        pressureTest.setJkChristmasTreeT3(Strings.isNullOrEmpty(labChristmasTreeTthreeJK.getValue()) ? 0d : Double.parseDouble(labChristmasTreeTthreeJK.getValue().trim()));

        pressureTest.setJkChristmasTreeR1(Strings.isNullOrEmpty(labChristmasTreeRoneJK.getValue()) ? "" : labChristmasTreeRoneJK.getValue().trim());
        pressureTest.setJkChristmasTreeR2(Strings.isNullOrEmpty(labChristmasTreeRtwoJK.getValue()) ? "" : labChristmasTreeRtwoJK.getValue().trim());
        pressureTest.setJkChristmasTreeR3(Strings.isNullOrEmpty(labChristmasTreeRthreeJK.getValue()) ? "" : labChristmasTreeRthreeJK.getValue().trim());

        pressureTest.setJkChristmasTreeUser(labChristmasTreeSignatureJK.getValue());
        pressureTest.setJkChristmasTreeDate(labChristmasTreeDateJK.getValue());
        return pressureTest;
    }

    //井口-井口头
    private void btnConfirmWellheadJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationWellheadJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationWellheadJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationWellheadJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkWellheadAssemblyRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkWellheadAssemblyRecord);
                }

                btnSaveWellheadJK.setEnabled(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetWellheadJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.WELLHEAD_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationWellheadJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkWellheadAssemblyRecord.getValue(), equipment, labWellAssemJks, "WellheadAssemblyJk", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkWellheadAssemblyRecord.getValue(), labWellAssemJks, "WellheadAssemblyJk");
                }
                labWellheadAssemblySignatureJK.setValue(RequestInfo.current().getUserName());
                labWellheadAssemblyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveWellheadJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }

    }

    private void btnSaveWellheadJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest = setWellheadJK(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("井口头测试数据保存成功！");
            }
        }
    }

    private PressureTest setWellheadJK(PressureTest pressureTest) {
        pressureTest.setJkWellheadAssemblyFlag(ckWellheadAssemblyJK.getValue());
        pressureTest.setJkWellheadAssemblyStationName(cbStationWellheadJK.getValue().getStation());
        pressureTest.setJkWellheadAssemblyStationNo(cbStationWellheadJK.getValue().getEquipmentNo());
        pressureTest.setJkWellheadAssemblyRecord(jkWellheadAssemblyRecord.getValue());

        pressureTest.setJkWellheadP1(Strings.isNullOrEmpty(labWellheadAssemblyPoneJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyPoneJK.getValue().trim()));
        pressureTest.setJkWellheadP2(Strings.isNullOrEmpty(labWellheadAssemblyPtwoJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyPtwoJK.getValue().trim()));
        pressureTest.setJkWellheadP3(Strings.isNullOrEmpty(labWellheadAssemblyPthreeJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyPthreeJK.getValue().trim()));

        pressureTest.setJkWellheadT1(Strings.isNullOrEmpty(labWellheadAssemblyToneJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyToneJK.getValue().trim()));
        pressureTest.setJkWellheadT2(Strings.isNullOrEmpty(labWellheadAssemblyTtwoJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyTtwoJK.getValue().trim()));
        pressureTest.setJkWellheadT3(Strings.isNullOrEmpty(labWellheadAssemblyTthreeJK.getValue()) ? 0d : Double.parseDouble(labWellheadAssemblyTthreeJK.getValue().trim()));

        pressureTest.setJkWellheadR1(Strings.isNullOrEmpty(labWellheadAssemblyRoneJK.getValue()) ? "" : labWellheadAssemblyRoneJK.getValue().trim());
        pressureTest.setJkWellheadR2(Strings.isNullOrEmpty(labWellheadAssemblyRtwoJK.getValue()) ? "" : labWellheadAssemblyRtwoJK.getValue().trim());
        pressureTest.setJkWellheadR3(Strings.isNullOrEmpty(labWellheadAssemblyRthreeJK.getValue()) ? "" : labWellheadAssemblyRthreeJK.getValue().trim());

        pressureTest.setJkWellheadUser(labWellheadAssemblySignatureJK.getValue());
        pressureTest.setJkWellheadDate(labWellheadAssemblyDateJK.getValue());
        return pressureTest;
    }

    //井口-转换头组件
    private void btnConfirmCrossAssemTopJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationCrossAssemTopJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationCrossAssemTopJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationCrossAssemTopJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkCrossoverAssemblyTopRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkCrossoverAssemblyTopRecord);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetCrossAssemTJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.CROSS_OVER_ASSEMBLY_TOP_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationCrossAssemTopJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkCrossoverAssemblyTopRecord.getValue(), equipment, labCrossAssemTopJks, "CrossoverAssemblyTop", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkCrossoverAssemblyTopRecord.getValue(), labCrossAssemTopJks, "CrossoverAssemblyTop");
                }
                labCrossoverAssemblySignatureJK.setValue(RequestInfo.current().getUserName());
                labCrossoverAssemblyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveCrossAssembJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }
    }

    private void btnConfirmCrossAssemBtmJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            String ip = Strings.isNullOrEmpty(cbStationCrossAssemBtmJK.getValue().getIpAdress()) ?
                    RequestInfo.current().getUserIpAddress() : cbStationCrossAssemBtmJK.getValue().getIpAdress();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(cbStationCrossAssemBtmJK.getValue().getEquipmentType())) {
                    getValidataBysqliteJK(productSn, ip, jkCrossoverAssemblyBtmRecord);
                } else {
                    getValidataByLabViewFmJk(productSn, ip, jkCrossoverAssemblyBtmRecord);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void btnGetCrossAssemBJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String productSn = tfProductSn.getValue().trim();
            //获取压力标准
            PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNoJK.getValue().trim(), PressureTypeEnum.CROSS_OVER_ASSEMBLY_BTM_JK.getKey());
            if (pressureRuler == null) {
                throw new PlatformException("压力标准未配置，请先配置！");
            }
            //获取压力数据
            StationEquipment equipment = cbStationCrossAssemBtmJK.getValue();
            try {
                if (AppConstant.PRESSURE_SQLITE.equals(equipment.getEquipmentType())) {
                    getPressureDataBySqliteJK(jkCrossoverAssemblyBtmRecord.getValue(), equipment, labCrossAssemBtmJks, "CrossoverAssemblyBtm", pressureRuler);
                } else {
                    getPressureAndDiagramByLabViewJK(pressureRuler, equipment, jkCrossoverAssemblyBtmRecord.getValue(), labCrossAssemBtmJks, "CrossoverAssemblyBtm");
                }
                labCrossoverAssemblySignatureJK.setValue(RequestInfo.current().getUserName());
                labCrossoverAssemblyDateJK.setValue(myFmt0.format(new Date()));
                btnSaveCrossAssembJK.setEnabled(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DrawDiagramJK(productSn);
        }
    }

    private void btnSaveCrossAssembJKMethod() {
        if (Strings.isNullOrEmpty(tfProductSn.getValue().trim())) {
            NotificationUtils.notificationError("请先输入成品序列号");
        } else {
            String strOrderNo = (tfProductSn.getValue().trim()).substring(0, tfProductSn.getValue().trim().length() - 4);
            ProductionOrder productionOrder = productionOrderService.getByNo(strOrderNo);
            if (productionOrder == null) {
                NotificationUtils.notificationError("序列号对应订单号不存在，请检查！");
            } else {
                PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
                if (pressureTest == null) {
                    pressureTest = new PressureTest();
                }
                pressureTest.setProductSN(tfProductSn.getValue().trim());
                pressureTest.setTestType(cbPressureType.getValue().trim());
                pressureTest.setGageNoSize(tfGageNoSizeJK.getValue());
                pressureTest.setProcedureNo(tfProcedureNoJK.getValue());

                pressureTest = setCrossAssembJK(pressureTest);
                pressureTestService.save(pressureTest);
                NotificationUtils.notificationInfo("接口头组件测试数据保存成功！");
            }
        }
    }

    private PressureTest setCrossAssembJK(PressureTest pressureTest) {
        pressureTest.setJkCrossoverAssemblyFlag(ckCrossoverAssemblyJK.getValue());
        pressureTest.setJkCrossoverAssemblyTopStationName(cbStationCrossAssemTopJK.getValue().getStation());
        pressureTest.setJkCrossoverAssemblyTopStationNo(cbStationCrossAssemTopJK.getValue().getEquipmentNo());
        pressureTest.setJkCrossoverAssemblyTopRecord(jkCrossoverAssemblyTopRecord.getValue());

        pressureTest.setJkCrossoverAssemblyBtmStationName(cbStationCrossAssemBtmJK.getValue().getStation());
        pressureTest.setJkCrossoverAssemblyBtmStationNo(cbStationCrossAssemBtmJK.getValue().getEquipmentNo());
        pressureTest.setJkCrossoverAssemblyBtmRecord(jkCrossoverAssemblyBtmRecord.getValue());
        //top
        pressureTest.setJkCrossoverAssemblyTopP1(Strings.isNullOrEmpty(labCrossoverAssemblyTOPPoneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPPoneJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyTopP2(Strings.isNullOrEmpty(labCrossoverAssemblyTOPPtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPPtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyTopP3(Strings.isNullOrEmpty(labCrossoverAssemblyTOPPthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPPthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverAssemblyTopT1(Strings.isNullOrEmpty(labCrossoverAssemblyTOPToneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPToneJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyTopT2(Strings.isNullOrEmpty(labCrossoverAssemblyTOPTtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPTtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyTopT3(Strings.isNullOrEmpty(labCrossoverAssemblyTOPTthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyTOPTthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverAssemblyTopR1(Strings.isNullOrEmpty(labCrossoverAssemblyTOPRoneJK.getValue()) ? "" : labCrossoverAssemblyTOPRoneJK.getValue().trim());
        pressureTest.setJkCrossoverAssemblyTopR2(Strings.isNullOrEmpty(labCrossoverAssemblyTOPRtwoJK.getValue()) ? "" : labCrossoverAssemblyTOPRtwoJK.getValue().trim());
        pressureTest.setJkCrossoverAssemblyTopR3(Strings.isNullOrEmpty(labCrossoverAssemblyTOPRthreeJK.getValue()) ? "" : labCrossoverAssemblyTOPRthreeJK.getValue().trim());
        //btm
        pressureTest.setJkCrossoverAssemblyBtmP1(Strings.isNullOrEmpty(labCrossoverAssemblyBTMPoneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMPoneJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyBtmP2(Strings.isNullOrEmpty(labCrossoverAssemblyBTMPtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMPtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyBtmP3(Strings.isNullOrEmpty(labCrossoverAssemblyBTMPthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMPthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverAssemblyBtmT1(Strings.isNullOrEmpty(labCrossoverAssemblyBTMToneJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMToneJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyBtmT2(Strings.isNullOrEmpty(labCrossoverAssemblyBTMTtwoJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMTtwoJK.getValue().trim()));
        pressureTest.setJkCrossoverAssemblyBtmT3(Strings.isNullOrEmpty(labCrossoverAssemblyBTMTthreeJK.getValue()) ? 0d : Double.parseDouble(labCrossoverAssemblyBTMTthreeJK.getValue().trim()));

        pressureTest.setJkCrossoverAssemblyBtmR1(Strings.isNullOrEmpty(labCrossoverAssemblyBTMRoneJK.getValue()) ? "" : labCrossoverAssemblyBTMRoneJK.getValue().trim());
        pressureTest.setJkCrossoverAssemblyBtmR2(Strings.isNullOrEmpty(labCrossoverAssemblyBTMRtwoJK.getValue()) ? "" : labCrossoverAssemblyBTMRtwoJK.getValue().trim());
        pressureTest.setJkCrossoverAssemblyBtmR3(Strings.isNullOrEmpty(labCrossoverAssemblyBTMRthreeJK.getValue()) ? "" : labCrossoverAssemblyBTMRthreeJK.getValue().trim());

        pressureTest.setJkCrossoverAssemblyUser(labCrossoverAssemblySignatureJK.getValue());
        pressureTest.setJkCrossoverAssemblyDate(labCrossoverAssemblyDateJK.getValue());
        return pressureTest;
    }

    /**
     * 获取测试记录列表供用户选择出有效记录(Sqlite)阀门
     *
     * @param productSn
     * @param ip
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getValidataBysqliteFM(String productSn, String ip, ComboBox<String> cbRecordFM) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigFM.getConfigValue() + "\\MaximatorHP.db3";

        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();
        System.out.println("filePath************" + urlStr);
        ResultSet rs = stat.executeQuery("select TableName from TestInfo where SampleInfo1='" + productSn + "';");
        List<String> strRecords = new ArrayList<>();
        while (rs.next()) {
            System.out.println("TableName = " + rs.getString("TableName"));
            strRecords.add(rs.getString("TableName"));
        }

        cbRecordFM.clear();
        cbRecordFM.setItems(strRecords);


        rs.close();
        conn.close();
    }

    /**
     * 获取测试记录列表供用户选择出有效记录(Sqlite)井口
     *
     * @param productSn
     * @param ip
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getValidataBysqliteJK(String productSn, String ip, ComboBox<String> cbRecordJK) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigJK.getConfigValue() + "\\MaximatorHP.db3";
        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();
        System.out.println("filePath************" + urlStr);
        ResultSet rs = stat.executeQuery("select TableName from TestInfo where SampleInfo1='" + productSn.substring(9) + "' " +
                "and SampleInfo3='" + productSn.substring(0, 9) + "';");//sqLite井口与阀门的序列号保存方式不同，注意区别
        List<String> strRecords = new ArrayList<>();
        while (rs.next()) {
            System.out.println("TableName = " + rs.getString("TableName"));
            strRecords.add(rs.getString("TableName"));
        }

        cbRecordJK.clear();
        cbRecordJK.setItems(strRecords);

        rs.close();
        conn.close();
    }


    /**
     * 获取测试记录列表供用户选择出有效记录(LabView)阀门&井口
     *
     * @param productSn
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getValidataByLabViewFmJk(String productSn, String ip, ComboBox<String> cbRecordFmJk) {
        String filePath = "\\\\" + ip + "\\" + labviewConfig.getConfigValue();

        List<String> recordFileNames = getFileName(filePath, "test" + productSn);

        cbRecordFmJk.clear();
        cbRecordFmJk.setItems(recordFileNames);
    }

    //获取 test序列号XXXXX.csv文件列表
    public List<String> getFileName(String path, String valueStr) {
        List<String> records = new ArrayList<>();
        File file = new File(path);
        String[] fileName = file.list();
        for (String s : fileName) {
            if (s.startsWith(valueStr) && s.endsWith("csv")) {
                records.add(s);
            }
        }
        return records;
    }

    /**
     * 查询有效记录对应的试验数据(sqlite)阀门
     *
     * @param tableName
     * @param equipment
     * @param selectFlag
     * @param selectArr
     * @param labels
     * @param pressureRuler
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getPressureDataBySqliteFM(String tableName, StationEquipment equipment, String[][] selectArr, Label[] labels, String selectFlag, PressureRuler pressureRuler) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigFM.getConfigValue() + "\\MaximatorHP.db3";
        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();
        System.out.println("filePath************" + urlStr);
        ResultSet rs = stat.executeQuery("select * from TestInfo where TableName='" + tableName + "';");

        Boolean[] flagArr = new Boolean[3];
        String[] strInfos = new String[3];

        while (rs.next()) {
            System.out.println("--------------------------");

            for (int i = 0; i < selectArr.length; i++) { //遍历二维数组，遍历出来的每一个元素是一个一维数组
                if (rs.getDouble(selectArr[i][0]) == 1.0) {//是否检测
                    double standTime;
                    switch (i) {
                        case 0:
                            standTime = pressureRuler.getFirstTime();
                            break;
                        case 1:
                            standTime = pressureRuler.getSecondTime();
                            break;
                        case 2:
                            standTime = pressureRuler.getThirdTime();
                            break;
                        default:
                            standTime = 0D;
                            break;
                    }
                    //判定实际值是否OK
                    if (rs.getDouble(selectArr[i][3]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || rs.getDouble(selectArr[i][4]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(rs.getDouble(selectArr[i][3]) - rs.getDouble(selectArr[i][4])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(DoubleToDate(rs.getDouble(selectArr[i][5])), DoubleToDate(rs.getDouble(selectArr[i][6]))) < standTime) {//保压持续时间 小于 标准时间
                        flagArr[i] = false;
                    } else {
                        flagArr[i] = true;
                    }
                    strInfos[i] = "StartP" + (i + 1) + " " + rs.getDouble(selectArr[i][3]) + "  StartT" + (i + 1) + " " + myFmt1.format(DoubleToDate(rs.getDouble(selectArr[i][5])))
                            + "   StopP" + (i + 1) + " " + rs.getDouble(selectArr[i][4]) + "  StopT" + (i + 1) + " " + myFmt1.format(DoubleToDate(rs.getDouble(selectArr[i][6])));
                } else {
                    //判断保压次数是否等于标准要求次数
                    switch (i) {
                        case 0:
                            flagArr[i] = pressureRuler.getFirstTime() == 0;
                            break;
                        case 1:
                            flagArr[i] = pressureRuler.getSecondTime() == 0;
                            break;
                        case 2:
                            flagArr[i] = pressureRuler.getThirdTime() == 0;
                            break;
                        default:
                            flagArr[i] = false;
                            break;
                    }
                    strInfos[i] = null;
                }
            }
            //.1
            labels[0].setValue(pressureRuler.getFirstTime() == 0D ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[3].setValue(pressureRuler.getFirstTime() + "");
            //.2
            labels[1].setValue(pressureRuler.getSecondTime() == 0D ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[4].setValue(pressureRuler.getSecondTime() + "");
            //.3
            labels[2].setValue(pressureRuler.getThirdTime() == 0D ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[5].setValue(pressureRuler.getThirdTime() + "");
            if (flagArr[0] && flagArr[1] && flagArr[2]) {
                labels[6].setValue("PASS");
            } else {
                labels[6].setValue("NG");
            }
        }
        rs.close();
        conn.close();
        //绘制曲线图
        getDiagramDataBySqlite(tableName, equipment, strInfos, selectFlag);
    }

    /**
     * 查询有效记录对应的试验数据(sqlite)井口
     *
     * @param tableName
     * @param equipment
     * @param labels
     * @param testItem
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getPressureDataBySqliteJK(String tableName, StationEquipment equipment, Label[] labels, String testItem, PressureRuler pressureRuler) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigJK.getConfigValue() + "\\MaximatorHP.db3";
        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();
        System.out.println("filePath************" + urlStr);
        ResultSet rs = stat.executeQuery("select * from TestInfo where TableName='" + tableName + "';");

        String[][] openArr = {{"PR1", "DP2", "DP3", "DP4", "DP5", "DP6", "ENB1"}, {"PR2", "ENB2", "ENB3", "ENB4", "ENB5", "ENB6", "DP1_PV"}, {"PR3", "DP2_PV", "DP3_PV", "DP4_PV", "DP5_PV", "DP6_PV", "Temperature"}};
        Boolean[] flagArr = new Boolean[3];
        String[] strInfoBody = new String[3];

        while (rs.next()) {
            System.out.println("-----------jk默认取本体---------------");
            System.out.println("jk本体1 PR1 = " + rs.getDouble("PR1"));
            System.out.println("jk本体2 PR2 = " + rs.getDouble("PR2"));
            System.out.println("jk本体3 PR3 = " + rs.getDouble("PR3"));

            for (int i = 0; i < openArr.length; i++) { //遍历二维数组，遍历出来的每一个元素是一个一维数组
                if (rs.getDouble(openArr[i][0]) == 1.0) {//是否检测
                    double standTime;
                    switch (i) {
                        case 0:
                            standTime = pressureRuler.getFirstTime();
                            break;
                        case 1:
                            standTime = pressureRuler.getSecondTime();
                            break;
                        case 2:
                            standTime = pressureRuler.getThirdTime();
                            break;
                        default:
                            standTime = 0D;
                            break;
                    }
                    //判定实际值是否OK
                    if (rs.getDouble(openArr[i][3]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || rs.getDouble(openArr[i][4]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(rs.getDouble(openArr[i][3]) - rs.getDouble(openArr[i][4])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(DoubleToDate(rs.getDouble(openArr[i][5])), DoubleToDate(rs.getDouble(openArr[i][6]))) < standTime) {//保压持续时间 小于 标准时间
                        flagArr[i] = false;
                    } else {
                        flagArr[i] = true;
                    }
                    strInfoBody[i] = "StartP" + (i + 1) + " " + rs.getDouble(openArr[i][3]) + "  StartT" + (i + 1) + " " + myFmt1.format(DoubleToDate(rs.getDouble(openArr[i][5])))
                            + "   StopP" + (i + 1) + " " + rs.getDouble(openArr[i][4]) + "  StopT" + (i + 1) + " " + myFmt1.format(DoubleToDate(rs.getDouble(openArr[i][6])));
                } else {
                    //判断保压次数是否等于标准要求次数
                    switch (i) {
                        case 0:
                            flagArr[i] = pressureRuler.getFirstTime() == 0;
                            break;
                        case 1:
                            flagArr[i] = pressureRuler.getSecondTime() == 0;
                            break;
                        case 2:
                            flagArr[i] = pressureRuler.getThirdTime() == 0;
                            break;
                        default:
                            flagArr[i] = false;
                            break;
                    }
                    strInfoBody[i] = null;
                }
            }

            labels[0].setValue(pressureRuler.getFirstTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[1].setValue(pressureRuler.getFirstTime() + "");
            labels[2].setValue(flagArr[0] ? "OK" : "NG");
            labels[3].setValue(pressureRuler.getSecondTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[4].setValue(pressureRuler.getSecondTime() + "");
            labels[5].setValue(flagArr[1] ? "OK" : "NG");
            labels[6].setValue(pressureRuler.getThirdTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
            labels[7].setValue(pressureRuler.getThirdTime() + "");
            labels[8].setValue(flagArr[2] ? "OK" : "NG");

        }
        rs.close();
        conn.close();
        //绘制曲线图
        getDiagramDataBySqliteJK(tableName, equipment, strInfoBody, testItem);
    }

    /**
     * 获取曲线图数据（LabView）阀门
     *
     * @param pressureRuler
     * @param equipment
     * @param valueRecord
     * @param labels
     * @param testItem
     */
    public void getPressureAndDiagramByLabViewFM(PressureRuler pressureRuler, StationEquipment equipment, String valueRecord, Label[] labels, String testItem) {

        Map<Double, Double> dataMap = new LinkedHashMap<>();
        Boolean[] booleans = new Boolean[3];
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();

        booleans[0] = pressureRuler.getFirstTime() != 0;
        booleans[1] = pressureRuler.getSecondTime() != 0;
        booleans[2] = pressureRuler.getThirdTime() != 0;

        if (booleans[0] || booleans[1] || booleans[2]) {
            String filePath = "\\\\" + ip + "\\" + labviewConfig.getConfigValue() + "\\" + valueRecord;
            dataMap = getDatasByCsvFM(filePath, pressureRuler, booleans, labels);
        }

        labels[3].setValue(pressureRuler.getFirstTime() + "");
        labels[0].setValue(pressureRuler.getFirstTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");

        labels[4].setValue(pressureRuler.getSecondTime() + "");
        labels[1].setValue(pressureRuler.getSecondTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");

        labels[5].setValue(pressureRuler.getThirdTime() + "");
        labels[2].setValue(pressureRuler.getThirdTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");

        // 曲线图
        createDiagramPic(dataMap, testItem, strLabViewInfo, equipment.getEquipmentNo());

    }

    /**
     * 获取曲线图数据（LabView）井口
     *
     * @param pressureRuler
     * @param equipment
     * @param valueRecord
     * @param labels
     * @param testItem
     */
    public void getPressureAndDiagramByLabViewJK(PressureRuler pressureRuler, StationEquipment equipment, String valueRecord, Label[] labels, String testItem) {
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();
        Map<Double, Double> dataMap = new LinkedHashMap<>();
        Boolean[] booleans = new Boolean[3];

        booleans[0] = pressureRuler.getFirstTime() != 0;
        booleans[1] = pressureRuler.getSecondTime() != 0;
        booleans[2] = pressureRuler.getThirdTime() != 0;

        if (booleans[0] || booleans[1] || booleans[2]) {
            String filePath = "\\\\" + ip + "\\" + labviewConfig.getConfigValue() + "\\" + valueRecord;
            dataMap = getDatasByCsvJK(filePath, pressureRuler, booleans, labels);
        }

        labels[0].setValue(pressureRuler.getFirstTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
        labels[1].setValue(pressureRuler.getFirstTime() + "");

        labels[3].setValue(pressureRuler.getSecondTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
        labels[4].setValue(pressureRuler.getSecondTime() + "");

        labels[6].setValue(pressureRuler.getThirdTime() == 0 ? "0" : pressureRuler.getTestPressureValue() + "");
        labels[7].setValue(pressureRuler.getThirdTime() + "");

        // 曲线图
        createDiagramPic(dataMap, testItem, strLabViewInfo, equipment.getEquipmentNo());

    }

    /**
     * 获取曲线图数据（sqlite）阀门
     *
     * @param tableName
     * @param equipment
     * @param strInfos
     * @param selectFlag
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getDiagramDataBySqlite(String tableName, StationEquipment equipment, String[] strInfos, String selectFlag) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigFM.getConfigValue() + "\\MaximatorHP.db3";
        System.out.println("filePath************" + urlStr);
        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery("select * from '" + tableName + "'" + " order by 'TestDateTime' asc ;");

        Map<Integer, Map<Double, Double>> map = new HashMap<>();
        Map<Double, Double> map1 = new LinkedHashMap<>();
        int i = 0;
        while (rs.next()) {
            if (rs.getDouble("Pressure") == -1) {
                System.out.println(i + "--------------" + map1.size());
                if (map1.size() > 0) {
                    map.put(i, map1);
                }
                i++;
                map1 = new LinkedHashMap<>();

            }
            map1.put(rs.getDouble("Pressure") == -1D ? 0D : rs.getDouble("Pressure"), rs.getDouble("Temperature"));

        }
        System.out.println(i + "--------------" + map1.size());
        map.put(i, map1);


        ResultSet rst = stat.executeQuery("select * from TestInfo where TableName='" + tableName + "';");

        Map<Double, Double> mapOpen = new LinkedHashMap<>();
        Map<Double, Double> mapDown = new LinkedHashMap<>();
        Map<Double, Double> mapUp = new LinkedHashMap<>();
        Map<Double, Double> mapTemp = new LinkedHashMap<>();

        try {
            int j = 0;
            double dEnd;
            double dTemp;
            while (rst.next()) {
                System.out.println("***********************本体*****************************");
                dEnd = 0;
                dTemp = 0;
                if (rs.getDouble("PR1") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }

                }
                dEnd += dTemp;
                if (rs.getDouble("PR2") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("PR3") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                System.out.println("***********************下游*****************************");
                dEnd = 0;
                dTemp = 0;
                if (rs.getDouble("PR4") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapDown.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("PR5") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapDown.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("PR6") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        mapDown.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                System.out.println("***********************上游*****************************");
                dEnd = 0;
                dTemp = 0;
                if (rs.getDouble("HT1") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapUp.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("HT2") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapUp.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("HT3") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        mapUp.put(m.getKey() + dEnd, m.getValue());
                    }
                }
            }
        } catch (Exception e) {
            throw new PlatformException("测试数据异常，请检查选择的有效记录是否正确！");
        }

        switch (selectFlag) {
            case "Open":// 本体曲线图
                createDiagramPic(mapOpen, "Open", strInfos, equipment.getEquipmentNo());
                break;
            case "Down":// 下游曲线图
                createDiagramPic(mapDown, "Down", strInfos, equipment.getEquipmentNo());
                break;
            case "Up":// 上游曲线图
                createDiagramPic(mapUp, "Up", strInfos, equipment.getEquipmentNo());
                break;
            default:
                System.out.println("阀门压力曲线图create");
                break;

        }

        rs.close();
        rst.close();
        conn.close();
    }

    /**
     * 获取曲线图数据（sqlite）井口
     *
     * @param tableName
     * @param equipment
     * @param strInfoBody
     * @param testItem
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getDiagramDataBySqliteJK(String tableName, StationEquipment equipment, String[] strInfoBody, String testItem) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        String ip = Strings.isNullOrEmpty(equipment.getIpAdress()) ? RequestInfo.current().getUserIpAddress() : equipment.getIpAdress();
        String urlStr = "jdbc:sqlite:\\\\" + ip + "\\" + sqliteConfigJK.getConfigValue() + "\\MaximatorHP.db3";
        System.out.println("filePath************" + urlStr);
        Connection conn = DriverManager.getConnection(urlStr);
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery("select * from '" + tableName + "'" + " order by 'TestDateTime' asc ;");

        Map<Integer, Map<Double, Double>> map = new HashMap<>();
        Map<Double, Double> map1 = new LinkedHashMap<>();
        int i = 0;
        while (rs.next()) {
            if (rs.getDouble("Pressure") == -1) {
                System.out.println(i + "--------------" + map1.size());
                if (map1.size() > 0) {
                    map.put(i, map1);
                }
                i++;
                map1 = new LinkedHashMap<>();

            }
            map1.put(rs.getDouble("Pressure") == -1D ? 0D : rs.getDouble("Pressure"), rs.getDouble("Temperature"));

        }
        System.out.println(i + "--------------" + map1.size());
        map.put(i, map1);

        ResultSet rst = stat.executeQuery("select * from TestInfo where TableName='" + tableName + "';");

        Map<Double, Double> mapOpen = new LinkedHashMap<>();
        Map<Double, Double> mapTemp = new LinkedHashMap<>();

        try {
            int j = 0;
            double dEnd;
            double dTemp;
            while (rst.next()) {
                System.out.println("***********************本体*****************************");
                dEnd = 0;
                dTemp = 0;
                if (rs.getDouble("PR1") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }

                }
                dEnd += dTemp;
                if (rs.getDouble("PR2") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        dTemp = m.getKey();
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }
                }
                dEnd += dTemp;
                if (rs.getDouble("PR3") == 1) {
                    j += 1;
                    mapTemp = map.get(j);
                    for (Map.Entry<Double, Double> m : mapTemp.entrySet()) {
                        mapOpen.put(m.getKey() + dEnd, m.getValue());
                    }
                }
            }
        } catch (Exception e) {
            throw new PlatformException("测试数据异常，请检查选择的有效记录是否正确！");
        }
        // 曲线图
        createDiagramPic(mapOpen, testItem, strInfoBody, equipment.getEquipmentNo());

        rs.close();
        rst.close();
        conn.close();
    }


    /**
     * 根据曲线数据生成曲线图片并保存
     *
     * @param dateMap
     * @param selectFlag
     */
    public void createDiagramPic(Map<Double, Double> dateMap, String selectFlag, String[] strInfo, String stationNoPath) {

        String snPic = tfProductSn.getValue().trim();
        if (Strings.isNullOrEmpty(snPic)) {
            throw new PlatformException("序列号不能为空！！");
        }
        String picFilePath = AppConstant.DOC_XML_FILE_PATH + "\\" + "PressurePictures" + "\\" + snPic.substring(0, snPic.length() - 4) + "\\" + snPic + "\\";
        if ("Open".equals(selectFlag)) {
            // 本体曲线图
            XYDataset xyDatasetOpen = createXYDataset(dateMap);
            JFreeChart freeChartOpen = createXYChart(xyDatasetOpen, "本体测试曲线", strInfo);
            saveAsFile(freeChartOpen, picFilePath + snPic + "-FmOpenTest.jpg", 1000, 500);
        } else if ("Down".equals(selectFlag)) {
            // 下游曲线图
            XYDataset xyDatasetDown = createXYDataset(dateMap);
            JFreeChart freeChartDown = createXYChart(xyDatasetDown, "下游密封测试曲线", strInfo);
            saveAsFile(freeChartDown, picFilePath + snPic + "-FmDownTest.jpg", 1000, 500);
        } else if ("Up".equals(selectFlag)) {
            // 上游曲线图
            XYDataset xyDatasetUp = createXYDataset(dateMap);
            JFreeChart freeChartUp = createXYChart(xyDatasetUp, "上游密封测试曲线", strInfo);
            saveAsFile(freeChartUp, picFilePath + snPic + "-FmUpTest.jpg", 1000, 500);
        } else if ("OpenBodyJk".equals(selectFlag)) {
            // 本体曲线图
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "本体试验曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkOpenBody.jpg", 1000, 500);
        } else if ("CrossoverBodyTopJK".equals(selectFlag)) {
            // 转换接头本体TOP
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "转换接头本体试验(TOP)曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkCrossoverBodyTop.jpg", 1000, 500);
        } else if ("CrossoverBodyBtmJK".equals(selectFlag)) {
            // 转换接头本体BTM
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "转换接头本体试验(BTM)曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkCrossoverBodyBtm.jpg", 1000, 500);
        } else if ("ChristmasTreeJk".equals(selectFlag)) {
            // 采油树
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "采油树测试曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkChristmasTree.jpg", 1000, 500);
        } else if ("WellheadAssemblyJk".equals(selectFlag)) {
            // 井口头组件
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "井口头组件测试曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkWellheadAssembly.jpg", 1000, 500);
        } else if ("CrossoverAssemblyTop".equals(selectFlag)) {
            // 转换接头组件TOP
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "转换接头本体组件试验(TOP)曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkCrossoverAssemblyTop.jpg", 1000, 500);
        } else if ("CrossoverAssemblyBtm".equals(selectFlag)) {
            // 转换接头组件BTM
            XYDataset xyDatasetJk = createXYDataset(dateMap);
            JFreeChart freeChartJK = createXYChart(xyDatasetJk, "转换接头组件试验(BTM)曲线", strInfo);
            saveAsFile(freeChartJK, picFilePath + snPic + "-JkCrossoverAssemblyBtm.jpg", 1000, 500);
        }
    }

    /**
     * 解析曲线数据文件获得压力测试曲线图数据（LabView阀门）
     *
     * @param filePath
     * @param pressureRuler
     * @param booleans
     * @param labels
     * @return
     */
    public Map<Double, Double> getDatasByCsvFM(String filePath, PressureRuler pressureRuler, Boolean[] booleans, Label[] labels) {
        //第一步：先获取csv文件的路径，通过BufferedReader类去读该路径中的文件
        File csv = new File(filePath);
        System.out.println("csv-Path:  " + filePath);
        Map<Double, Double> mapDatas = new LinkedHashMap<>();
        String[] strInfo = new String[3];
        try {
            //第二步：从字符输入流读取文本，缓冲各个字符，从而实现字符、数组和行（文本的行数通过回车符来进行判定）的高效读取。
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String line;
            int rowFlag = 0;//标识行号
            double index = 0.0;//曲线横轴X
            String[] arrData = new String[12];
            while ((line = textFile.readLine()) != null) {
                rowFlag++;
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (rowFlag < 13) {
                    arrData[rowFlag - 1] = item[0];
                }
                if (rowFlag >= 14) {
                    String[] item2 = item[0].split("  ");
                    String[] item3 = item2[1].split(" ");
                    for (String s : item3) {
                        System.out.println("--* " + s);
                    }
                    Double valueB = Double.parseDouble(item3[1]);//纵轴Y

                    mapDatas.put(index++, valueB);
                }
                System.out.println("***************" + rowFlag);
            }
            String isPass = "PASS";
            String strNA = "N/A";
            if (booleans[0]) {//第一次
                if ("N/A".equals(arrData[0]) || "N/A".equals(arrData[1]) || "N/A".equals(arrData[2]) || "N/A".equals(arrData[3])) {
                    isPass = "NG";
                    strInfo[0] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[1]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[3]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[1]) - Double.parseDouble(arrData[3])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[0]), strToDate(arrData[2])) < pressureRuler.getFirstTime()) {//保压持续时间 小于 标准时间
                        isPass = "NG";
                    }
                    strInfo[0] = "StartP1 " + arrData[1] + "  StartT1 " + myFmt1.format(strToDate(arrData[0]))
                            + "   StopP1 " + arrData[3] + "  StopT1 " + myFmt1.format(strToDate(arrData[2]));
                }
            } else {
                if (!"N/A".equals(arrData[0]) && !"N/A".equals(arrData[1]) && !"N/A".equals(arrData[2]) && !"N/A".equals(arrData[3])) {
                    isPass = "NG";
                    strInfo[0] = "StartP1 " + arrData[1] + "  StartT1 " + myFmt1.format(strToDate(arrData[0]))
                            + "   StopP1 " + arrData[3] + "  StopT1 " + myFmt1.format(strToDate(arrData[2]));
                } else {
                    strInfo[0] = null;
                }
            }
            if (booleans[1]) {//第二次
                if ("N/A".equals(arrData[4]) || "N/A".equals(arrData[5]) || "N/A".equals(arrData[6]) || "N/A".equals(arrData[7])) {
                    isPass = "NG";
                    strInfo[1] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[5]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[7]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[5]) - Double.parseDouble(arrData[7])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[4]), strToDate(arrData[6])) < pressureRuler.getSecondTime()) {//保压持续时间 小于 标准时间
                        isPass = "NG";
                    }
                    strInfo[1] = "StartP2 " + arrData[5] + "  StartT2 " + myFmt1.format(strToDate(arrData[4]))
                            + "   StopP2 " + arrData[7] + "  StopT2 " + myFmt1.format(strToDate(arrData[6]));
                }
            } else {
                if (!"N/A".equals(arrData[4]) && !"N/A".equals(arrData[5]) && !"N/A".equals(arrData[6]) && !"N/A".equals(arrData[7])) {
                    isPass = "NG";
                    strInfo[1] = "StartP2 " + arrData[5] + "  StartT2 " + myFmt1.format(strToDate(arrData[4]))
                            + "   StopP2 " + arrData[7] + "  StopT2 " + myFmt1.format(strToDate(arrData[6]));
                } else {
                    strInfo[1] = null;
                }
            }
            if (booleans[2]) {//第三次
                if ("N/A".equals(arrData[8]) || "N/A".equals(arrData[9]) || "N/A".equals(arrData[10]) || "N/A".equals(arrData[11])) {
                    isPass = "NG";
                    strInfo[2] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[9]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[11]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[9]) - Double.parseDouble(arrData[11])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[8]), strToDate(arrData[10])) < pressureRuler.getThirdTime()) {//保压持续时间 小于 标准时间
                        isPass = "NG";
                    }
                    strInfo[2] = "StartP3 " + arrData[9] + "  StartT3 " + myFmt1.format(strToDate(arrData[8]))
                            + "   StopP3 " + arrData[11] + "  StopT3 " + myFmt1.format(strToDate(arrData[10]));
                }
            } else {
                if (!"N/A".equals(arrData[8]) && !"N/A".equals(arrData[9]) && !"N/A".equals(arrData[10]) && !"N/A".equals(arrData[11])) {
                    isPass = "NG";
                    strInfo[2] = "StartP3 " + arrData[9] + "  StartT3 " + myFmt1.format(strToDate(arrData[8]))
                            + "   StopP3 " + arrData[11] + "  StopT3 " + myFmt1.format(strToDate(arrData[10]));
                } else {
                    strInfo[2] = null;
                }
            }

            strLabViewInfo = new String[3];
            strLabViewInfo = strInfo;
            labels[6].setValue(isPass);

            if (textFile != null) {
                textFile.close();
            }
        } catch (FileNotFoundException e) {
            throw new PlatformException("没有找到压力数据文件!");
        } catch (ParseException e) {
            throw new PlatformException("数据异常转换!");
        } catch (IOException e) {
            throw new PlatformException("文件异常，读写错误！");
        }
        return mapDatas;
    }

    /**
     * 解析曲线数据文件获得压力测试曲线图数据（LabView井口）
     *
     * @param filePath
     * @param pressureRuler
     * @param booleans
     * @param labels
     * @return
     */
    public Map<Double, Double> getDatasByCsvJK(String filePath, PressureRuler pressureRuler, Boolean[] booleans, Label[] labels) {
        //第一步：先获取csv文件的路径，通过BufferedReader类去读该路径中的文件
        File csv = new File(filePath);
        System.out.println("csv-Path: " + filePath);
        Map<Double, Double> mapDatas = new LinkedHashMap<>();
        String[] strInfo = new String[3];
        try {
            //第二步：从字符输入流读取文本，缓冲各个字符，从而实现字符、数组和行（文本的行数通过回车符来进行判定）的高效读取。
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String line;
            int rowFlag = 0;//标识行号
            double index = 0.0;//曲线横轴X
            String[] arrData = new String[12];
            while ((line = textFile.readLine()) != null) {
                rowFlag++;
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (rowFlag < 13) {
                    arrData[rowFlag - 1] = item[0];
                }
                if (rowFlag >= 14) {
                    String[] item2 = item[0].split("  ");
                    String[] item3 = item2[1].split(" ");
                    for (String s : item3) {
                        System.out.println(rowFlag + "--* " + s);
                    }
                    Double valueB = Double.parseDouble(item3[1]);//纵轴Y
                    mapDatas.put(index++, valueB);
                }
            }
            Boolean[] isPass = new Boolean[3];
            String strNA = "N/A";
            if (booleans[0]) {//第一次
                if ("N/A".equals(arrData[0]) || "N/A".equals(arrData[1]) || "N/A".equals(arrData[2]) || "N/A".equals(arrData[3])) {
                    isPass[0] = false;
                    strInfo[0] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[1]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[3]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[1]) - Double.parseDouble(arrData[3])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[0]), strToDate(arrData[2])) < pressureRuler.getFirstTime()) {//保压持续时间 小于 标准时间
                        isPass[0] = false;
                    } else {
                        isPass[0] = true;
                    }
                    strInfo[0] = "StartP1 " + arrData[1] + "  StartT1 " + myFmt1.format(strToDate(arrData[0]))
                            + "   StopP1 " + arrData[3] + "  StopT1 " + myFmt1.format(strToDate(arrData[2]));
                }
            } else {
                if (!"N/A".equals(arrData[0]) && !"N/A".equals(arrData[1]) && !"N/A".equals(arrData[2]) && !"N/A".equals(arrData[3])) {
                    isPass[0] = false;
                    strInfo[0] = "StartP1 " + arrData[1] + "  StartT1 " + myFmt1.format(strToDate(arrData[0]))
                            + "   StopP1 " + arrData[3] + "  StopT1 " + myFmt1.format(strToDate(arrData[2]));
                } else {
                    isPass[0] = true;
                    strInfo[0] = null;
                }

            }
            if (booleans[1]) {//第二次
                if ("N/A".equals(arrData[4]) || "N/A".equals(arrData[5]) || "N/A".equals(arrData[6]) || "N/A".equals(arrData[7])) {
                    isPass[1] = false;
                    strInfo[1] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[5]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[7]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[5]) - Double.parseDouble(arrData[7])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[4]), strToDate(arrData[6])) < pressureRuler.getSecondTime()) {//保压持续时间 小于 标准时间
                        isPass[1] = false;
                    } else {
                        isPass[1] = true;
                    }
                    strInfo[1] = "StartP2 " + arrData[5] + "  StartT2 " + myFmt1.format(strToDate(arrData[4]))
                            + "   StopP2 " + arrData[7] + "  StopT2 " + myFmt1.format(strToDate(arrData[6]));
                }
            } else {
                if (!"N/A".equals(arrData[4]) && !"N/A".equals(arrData[5]) && !"N/A".equals(arrData[6]) && !"N/A".equals(arrData[7])) {
                    isPass[1] = false;
                    strInfo[1] = "StartP2 " + arrData[5] + "  StartT2 " + myFmt1.format(strToDate(arrData[4]))
                            + "   StopP2 " + arrData[7] + "  StopT2 " + myFmt1.format(strToDate(arrData[6]));
                } else {
                    isPass[1] = true;
                    strInfo[1] = null;
                }

            }
            if (booleans[2]) {//第三次
                if ("N/A".equals(arrData[8]) || "N/A".equals(arrData[9]) || "N/A".equals(arrData[10]) || "N/A".equals(arrData[11])) {
                    isPass[2] = false;
                    strInfo[2] = "StartP1 " + strNA + "  StartT1 " + strNA
                            + "   StopP1 " + strNA + "  StopT1 " + strNA;
                } else {
                    if (Double.parseDouble(arrData[9]) > pressureRuler.getMaxPressureValue() //开始压力 大于 最大压力
                            || Double.parseDouble(arrData[11]) < pressureRuler.getTestPressureValue() //结束压力 小于 测试压力
                            || Math.abs(Double.parseDouble(arrData[9]) - Double.parseDouble(arrData[11])) > pressureRuler.getDifferencePressureValue()//压力下降 超过 压力差
                            || getDateDiffMin(strToDate(arrData[8]), strToDate(arrData[10])) < pressureRuler.getThirdTime()) {//保压持续时间 小于 标准时间
                        isPass[2] = false;
                    } else {
                        isPass[2] = true;
                    }
                    strInfo[2] = "StartP3 " + arrData[9] + "  StartT3 " + myFmt1.format(strToDate(arrData[8]))
                            + "   StopP3 " + arrData[11] + "  StopT3 " + myFmt1.format(strToDate(arrData[10]));
                }
            } else {
                if (!"N/A".equals(arrData[8]) && !"N/A".equals(arrData[9]) && !"N/A".equals(arrData[10]) && !"N/A".equals(arrData[11])) {
                    isPass[2] = false;
                    strInfo[2] = "StartP3 " + arrData[9] + "  StartT3 " + myFmt1.format(strToDate(arrData[8]))
                            + "   StopP3 " + arrData[11] + "  StopT3 " + myFmt1.format(strToDate(arrData[10]));
                } else {
                    isPass[2] = true;
                    strInfo[2] = null;
                }
            }
            //测压三次起始压力时间信息 + 三次ok/ng结果
            strLabViewInfo = new String[3];
            strLabViewInfo = strInfo;
            labels[2].setValue(isPass[0] ? "OK" : "NG");
            labels[5].setValue(isPass[1] ? "OK" : "NG");
            labels[8].setValue(isPass[2] ? "OK" : "NG");

            if (textFile != null) {
                textFile.close();
            }
        } catch (FileNotFoundException e) {
            throw new PlatformException("没有找到压力数据文件!");
        } catch (ParseException e) {
            throw new PlatformException("数据异常转换!");
        } catch (IOException e) {
            throw new PlatformException("文件异常，读写错误！");
        }
        return mapDatas;
    }

    /**
     * 获取生成的曲线图并显示在界面(阀门)
     */
    private void DrawDiagram(String snPic) {

        glXYlineLayout.removeAllComponents();

        String picFilePath = AppConstant.DOC_XML_FILE_PATH + "\\" + "PressurePictures" + "\\" + snPic.substring(0, snPic.length() - 4) + "\\" + snPic + "\\";

        List<InputStream> streamsIn = Lists.newArrayList();
        try {
            if (ckOpenBody.getValue()) {
                if (cbRecordBodyFM.getValue() != null && !"".equals(cbRecordBodyFM.getValue())) {
                    Image img = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream = null;
                            try {
                                inputStream =
                                        new FileInputStream(picFilePath + snPic + "-FmOpenTest.jpg");
                                streamsIn.add(inputStream);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream;
                        }
                    }, "FmOpenTest"));

                    img.setSizeFull();
                    img.setCaption(null);
                    glXYlineLayout.addComponent(img, 0, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsIn) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<InputStream> streamsIn1 = Lists.newArrayList();
        try {
            if (ckDownstream.getValue()) {
                if (cbRecordDownFM.getValue() != null && !"".equals(cbRecordDownFM.getValue())) {
                    Image img1 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream1 = null;
                            try {
                                inputStream1 = new FileInputStream(picFilePath + snPic + "-FmDownTest.jpg");
                                streamsIn1.add(inputStream1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream1;
                        }

                    }, "FmDownTest"));
                    img1.setSizeFull();
                    img1.setCaption(null);
                    glXYlineLayout.addComponent(img1, 0, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsIn1) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<InputStream> streamsIn2 = Lists.newArrayList();
        try {
            if (ckUpstream.getValue()) {
                if (cbRecordUpFM.getValue() != null && !"".equals(cbRecordUpFM.getValue())) {
                    Image img2 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream2 = null;
                            try {
                                inputStream2 = new FileInputStream(picFilePath + snPic + "-FmUpTest.jpg");
                                streamsIn2.add(inputStream2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream2;
                        }

                    }, "FmUpTest"));
                    img2.setSizeFull();
                    img2.setCaption(null);
                    glXYlineLayout.addComponent(img2, 0, 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsIn2) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取生成的曲线图并显示在界面（井口）
     */
    private void DrawDiagramJK(String snPicJK) {
        glXYlineLayoutJK.removeAllComponents();

        String picFilePath = AppConstant.DOC_XML_FILE_PATH + "\\" + "PressurePictures" + "\\" + snPicJK.substring(0, snPicJK.length() - 4) + "\\" + snPicJK + "\\";

        List<InputStream> streamsInJK = Lists.newArrayList();
        try {
            if (ckOpenBodyJK.getValue()) {
                if (jkOpenBodyRecord.getValue() != null && !"".equals(jkOpenBodyRecord.getValue())) {
                    Image img = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream = null;
                            try {
                                inputStream =
                                        new FileInputStream(picFilePath + snPicJK + "-JkOpenBody.jpg");
                                streamsInJK.add(inputStream);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream;
                        }

                    }, "JkOpenBody"));
                    img.setSizeFull();
                    img.setCaption(null);
                    glXYlineLayoutJK.addComponent(img, 0, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsInJK) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ckCrossoverBodyJK.getValue()) {
            List<InputStream> streamsInJK1 = Lists.newArrayList();
            try {
                if (jkCrossoverBodyTopRecord.getValue() != null && !"".equals(jkCrossoverBodyTopRecord.getValue())) {
                    Image img1 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream1 = null;
                            try {
                                inputStream1 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkCrossoverBodyTop.jpg");
                                streamsInJK1.add(inputStream1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream1;
                        }

                    }, "JkCrossoverBodyTop"));
                    img1.setSizeFull();
                    img1.setCaption(null);
                    glXYlineLayoutJK.addComponent(img1, 0, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    for (InputStream streamIn : streamsInJK1) {
                        streamIn.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            List<InputStream> streamsInJK2 = Lists.newArrayList();
            try {
                if (jkCrossoverBodyBtmRecord.getValue() != null && !"".equals(jkCrossoverBodyBtmRecord.getValue())) {
                    Image img2 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream2 = null;
                            try {
                                inputStream2 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkCrossoverBodyBtm.jpg");
                                streamsInJK2.add(inputStream2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream2;
                        }

                    }, "JkCrossoverBodyBtm"));
                    img2.setSizeFull();
                    img2.setCaption(null);
                    glXYlineLayoutJK.addComponent(img2, 0, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    for (InputStream streamIn : streamsInJK2) {
                        streamIn.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        List<InputStream> streamsInJK3 = Lists.newArrayList();
        try {
            if (ckChristmasTreeJK.getValue()) {
                if (jkChristmasTreeRecord.getValue() != null && !"".equals(jkChristmasTreeRecord.getValue())) {
                    Image img3 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream3 = null;
                            try {
                                inputStream3 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkChristmasTree.jpg");
                                streamsInJK3.add(inputStream3);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream3;
                        }

                    }, "JkChristmasTree"));
                    img3.setSizeFull();
                    img3.setCaption(null);
                    glXYlineLayoutJK.addComponent(img3, 0, 3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsInJK3) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<InputStream> streamsInJK4 = Lists.newArrayList();
        try {
            if (ckWellheadAssemblyJK.getValue()) {
                if (jkWellheadAssemblyRecord.getValue() != null && !"".equals(jkWellheadAssemblyRecord.getValue())) {
                    Image img4 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream4 = null;
                            try {
                                inputStream4 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkWellheadAssembly.jpg");
                                streamsInJK4.add(inputStream4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream4;
                        }

                    }, "JkWellheadAssembly"));
                    img4.setSizeFull();
                    img4.setCaption(null);
                    glXYlineLayoutJK.addComponent(img4, 0, 4);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (InputStream streamIn : streamsInJK4) {
                    streamIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ckCrossoverAssemblyJK.getValue()) {
            List<InputStream> streamsInJK5 = Lists.newArrayList();
            try {
                if (jkCrossoverAssemblyTopRecord.getValue() != null && !"".equals(jkCrossoverAssemblyTopRecord.getValue())) {
                    Image img5 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream5 = null;
                            try {
                                inputStream5 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkCrossoverAssemblyTop.jpg");
                                streamsInJK5.add(inputStream5);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream5;
                        }

                    }, "JkCrossoverAssemblyTop"));
                    img5.setSizeFull();
                    img5.setCaption(null);
                    glXYlineLayoutJK.addComponent(img5, 0, 5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    for (InputStream streamIn : streamsInJK5) {
                        streamIn.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            List<InputStream> streamsInJK6 = Lists.newArrayList();
            try {
                if (jkCrossoverAssemblyBtmRecord.getValue() != null && !"".equals(jkCrossoverAssemblyBtmRecord.getValue())) {
                    Image img6 = new Image("", new StreamResource(new StreamResource.StreamSource() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public InputStream getStream() {
                            InputStream inputStream6 = null;
                            try {
                                inputStream6 =
                                        new FileInputStream(picFilePath + snPicJK + "-JkCrossoverAssemblyBtm.jpg");
                                streamsInJK6.add(inputStream6);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return inputStream6;
                        }

                    }, "JkCrossoverAssemblyBtm"));
                    img6.setSizeFull();
                    img6.setCaption(null);
                    glXYlineLayoutJK.addComponent(img6, 0, 6);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    for (InputStream streamIn : streamsInJK6) {
                        streamIn.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 创建XYDataset 对象
     *
     * @return
     */
    public static XYDataset createXYDataset(Map<Double, Double> map) {
        XYSeries dataSeries = new XYSeries("");
        for (Map.Entry<Double, Double> m : map.entrySet()) {
            dataSeries.add(m.getKey(), m.getValue());
        }
        XYSeriesCollection xyDataset = new XYSeriesCollection();
        xyDataset.addSeries(dataSeries);
        return xyDataset;
    }

    /**
     * 根据XYDataset 生成JFreeChart
     *
     * @param xyDataset
     * @return
     */
    public static JFreeChart createXYChart(XYDataset xyDataset, String titleName, String[] strInfo) {
        // 设置中文主题样式 解决乱码
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        mChartTheme.setLargeFont(new Font("宋体", Font.BOLD, 15));//xy标题
        mChartTheme.setExtraLargeFont(new Font("黑体", Font.PLAIN, 20));//标题
        mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));//标注
        mChartTheme.setPlotBackgroundPaint(Color.WHITE);// 绘制区域
        mChartTheme.setPlotOutlinePaint(Color.WHITE);// 绘制区域外边框
        ChartFactory.setChartTheme(mChartTheme);
        // 创建JFreeChart对象：
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
                titleName, // 标题
                "时间(s)", // categoryAxisLabel （category轴，横轴，X轴标签）
                "压力值(psi)", // valueAxisLabel（value轴，纵轴，Y轴的标签）
                xyDataset, // dataset
                PlotOrientation.VERTICAL,
                true, // legend
                false, // tooltips
                false); // URLs
        jfreechart.setTextAntiAlias(false);//必须设置文本抗锯齿为false,防止乱码
        jfreechart.getLegend().setVisible(false);//设置标注是否可见
        jfreechart.getLegend().setFrame(new BlockBorder(Color.WHITE));//设置标注表框
        TextTitle subT1 = new TextTitle(strInfo[0] == null ? "" : strInfo[0]);//副标题，显示开始结束时间
        TextTitle subT2 = new TextTitle(strInfo[1] == null ? "" : strInfo[1]);
        TextTitle subT3 = new TextTitle(strInfo[2] == null ? "" : strInfo[2]);
        jfreechart.addSubtitle(0, subT1);
        jfreechart.addSubtitle(1, subT2);
        jfreechart.addSubtitle(2, subT3);

        // 使用CategoryPlot设置各种参数。以下设置可以省略。
        XYPlot plot = (XYPlot) jfreechart.getPlot();
        // 背景色 透明度
        plot.setBackgroundAlpha(1f);
        // 前景色 透明度
        plot.setForegroundAlpha(1f);
        // 网格线
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setRangeGridlinesVisible(true);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();//x轴设置
        domainAxis.setTickUnit(new NumberTickUnit(60));//每100个刻度显示一个刻度值
        NumberFormat numFormat = new DecimalFormat("#");//千分位数值显示去掉“，”
        domainAxis.setNumberFormatOverride(numFormat);
        domainAxis.setVerticalTickLabels(true);//x纵向显示刻度标签
        // 其他设置 参考 CategoryPlot类
        new DateTickUnit(DateTickUnitType.SECOND, 5, new SimpleDateFormat("mm"));
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);//series 连接线颜色
        renderer.setDefaultShapesVisible(false); // series 点（即数据点）可见
        renderer.setDefaultLinesVisible(true); // series 点（即数据点）间有连线可见
        renderer.setDefaultItemLabelGenerator(new StandardXYItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(false);//数据标签是否可见
        return jfreechart;
    }

    /**
     * 保存为图片
     *
     * @param chart
     * @param outputPath
     * @param weight
     * @param height
     */
    public void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 保存为PNG
            // ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
            // 保存为JPEG
            ChartUtils.writeChartAsJPEG(out, chart, weight, height);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 计算两个时间差值（分钟min）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private static double getDateDiffMin(Date startDate, Date endDate) {
        double nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        double diff = Math.round(endDate.getTime() - startDate.getTime());
        double min = diff / nm;
        return min;
    }

    /**
     * 将InputStream转换为byte[], 缓存1024字节
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
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
     * 生成报告（阀门）
     *
     * @param savePath
     * @throws Exception
     */
    public void createDocReport(String savePath) throws Exception {

        String snPic = tfProductSn.getValue().trim();
        if (Strings.isNullOrEmpty(snPic)) {
            throw new PlatformException("序列号不能为空！！");
        }
        String picFilePath = AppConstant.DOC_XML_FILE_PATH + "\\" + "PressurePictures" + "\\" + snPic.substring(0, snPic.length() - 4) + "\\" + snPic + "\\";

        // 准备填充数据

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("PartNo", labPartNo.getValue());
        dataMap.put("PartRev", labPartRev.getValue());
        dataMap.put("WorkOrder", labWorkOrder.getValue());
        dataMap.put("Description", labDescription.getValue().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        dataMap.put("TempRating", labTempRating.getValue());
        dataMap.put("MaterialClass", labMaterialClass.getValue());
        dataMap.put("PslLevel", labPslLevel.getValue());
        dataMap.put("SerialNo", labSerialNo.getValue());
        dataMap.put("TestProcedure", labTestProcedure.getValue());

        dataMap.put("ProcedureNo", tfProcedureNo.getValue() == null ? "" : tfProcedureNo.getValue());
        dataMap.put("GageNoSize", tfGageNoSize.getValue() == null ? "" : tfGageNoSize.getValue());
        dataMap.put("TorqueSensor", tfTorqueSensor.getValue() == null ? "" : tfTorqueSensor.getValue());

        dataMap.put("OpenPone", labOpenPtwo.getValue() == null ? "" : labOpenPtwo.getValue());
        dataMap.put("OpenPtwo", labOpenPtwo.getValue() == null ? "" : labOpenPtwo.getValue());
        dataMap.put("OpenPthree", labOpenPthree.getValue() == null ? "" : labOpenPthree.getValue());
        dataMap.put("OpenTone", labOpenTone.getValue() == null ? "" : labOpenTone.getValue());
        dataMap.put("OpenTtwo", labOpenTtwo.getValue() == null ? "" : labOpenTtwo.getValue());
        dataMap.put("OpenTthree", labOpenTthree.getValue() == null ? "" : labOpenTthree.getValue());
        dataMap.put("OpenComments", labOpenComments.getValue() == null ? "" : labOpenComments.getValue());

        dataMap.put("DownPone", labDownPone.getValue() == null ? "" : labDownPone.getValue());
        dataMap.put("DownPtwo", labDownPtwo.getValue() == null ? "" : labDownPtwo.getValue());
        dataMap.put("DownPthree", labDownPthree.getValue() == null ? "" : labDownPthree.getValue());
        dataMap.put("DownTone", labDownTone.getValue() == null ? "" : labDownTone.getValue());
        dataMap.put("DownTtwo", labDownTtwo.getValue() == null ? "" : labDownTtwo.getValue());
        dataMap.put("DownTthree", labDownTthree.getValue() == null ? "" : labDownTthree.getValue());
        dataMap.put("DownComments", labDownComments.getValue() == null ? "" : labDownComments.getValue());

        dataMap.put("UpPone", labUpPone.getValue() == null ? "" : labUpPone.getValue());
        dataMap.put("UpPtwo", labUpPtwo.getValue() == null ? "" : labUpPtwo.getValue());
        dataMap.put("UpPthree", labUpPthree.getValue() == null ? "" : labUpPthree.getValue());
        dataMap.put("UpTone", labUpTone.getValue() == null ? "" : labUpTone.getValue());
        dataMap.put("UpTtwo", labUpTtwo.getValue() == null ? "" : labUpTtwo.getValue());
        dataMap.put("UpTthree", labUpTthree.getValue() == null ? "" : labUpTthree.getValue());
        dataMap.put("UpComments", labUpComments.getValue() == null ? "" : labUpComments.getValue());

        dataMap.put("BlowTest", ckOpenUnder.getValue() ? "√" : "×");
        dataMap.put("DriftTest", cbPass.getValue() ? "√" : "×");

        dataMap.put("TestDate", labDate0.getValue());

        dataMap.put("Torque1", tfTorqueValue1.getValue());
        dataMap.put("Torque2", tfTorqueValue2.getValue());
        dataMap.put("Torque3", tfTorqueValue3.getValue());
        dataMap.put("Torque4", tfTorqueValue4.getValue());

        PressureRuler pressureRuler = pressureRulerService.getByProductNoAndPressureType(labPartNo.getValue(), PressureTypeEnum.DOWNSTREAM_FM.getKey());
        if (pressureRuler != null) {
            dataMap.put("MaxTorque", pressureRuler.getTorqueValue() + "");
        } else {
            dataMap.put("MaxTorque", "/");
        }

        // 将Media的InputStream转换为byte[] 然后进行BASE64编码
        BASE64Encoder encoder = new BASE64Encoder();

        List<InputStream> streamList = Lists.newArrayList();

        InputStream inputStream = new FileInputStream(AppConstant.DOC_XML_FILE_PATH + "\\emp.jpg");
        if (cbDirffTestBy.getValue() == null || "".equals(cbDirffTestBy.getValue())) {
            dataMap.put("imageTestBy2", encoder.encode(inputStream2byte(inputStream)));
            streamList.add(inputStream);
        } else {
            Media mediaImage2 = caMediaService.getMediaByName(cbDirffTestBy.getValue().getName());
            if (mediaImage2 == null) {
                throw new PlatformException("用户：" + cbDirffTestBy.getValue().getName() + " 签名logo未配置添加！");
            }
            dataMap.put("imageTestBy2", encoder.encode(inputStream2byte(mediaImage2.getMediaStream())));
        }
        Media mediaImage = caMediaService.getMediaByName(labTestBy0.getValue());
        //Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
        if (mediaImage == null) {
            throw new PlatformException("用户：" + labTestBy0.getValue() + " 签名logo未配置添加！");
//            throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
        }
        dataMap.put("imageTestBy", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));

        if (ckOpenBody.getValue() && !Strings.isNullOrEmpty(labOpenComments.getValue())) {
            InputStream imageOpen =
                    new FileInputStream(picFilePath + snPic + "-FmOpenTest.jpg");
            dataMap.put("imageOpen", encoder.encode(inputStream2byte(imageOpen)));
            streamList.add(imageOpen);
        } else {
            dataMap.put("imageOpen", encoder.encode(inputStream2byte(inputStream)));
        }

        if (ckDownstream.getValue() && !Strings.isNullOrEmpty(labDownComments.getValue())) {
            InputStream imageDown =
                    new FileInputStream(picFilePath + snPic + "-FmDownTest.jpg");
            dataMap.put("imageDown", encoder.encode(inputStream2byte(imageDown)));
            streamList.add(imageDown);
        } else {
            dataMap.put("imageDown", encoder.encode(inputStream2byte(inputStream)));
        }
        if (ckUpstream.getValue() && !Strings.isNullOrEmpty(labUpComments.getValue())) {
            InputStream imageUp =
                    new FileInputStream(picFilePath + snPic + "-FmUpTest.jpg");
            dataMap.put("imageUp", encoder.encode(inputStream2byte(imageUp)));
            streamList.add(imageUp);
        } else {
            dataMap.put("imageUp", encoder.encode(inputStream2byte(inputStream)));
        }

        //添加主管签字、日期；
        User user = userService.getByName(labTestBy0.getValue());
        if (user.getManager() == null) {
            throw new PlatformException("用户：" + labTestBy0.getValue() + " 的主管未配置！");
        }
        Media mediaImage3 = caMediaService.getMediaByName(user.getManager().getUserName());
        if (mediaImage3 == null) {
            throw new PlatformException("主管：" + user.getManager().getUserName() + " 的签名未配置！");
        }
        dataMap.put("SupervisorSign", encoder.encode(inputStream2byte(mediaImage3.getMediaStream())));

//        FinalInspectionResult finalInspectionResult = finalInspectionResultService.getByOrderNo(labWorkOrder.getValue());
//        if (finalInspectionResult != null) {
//            Media mediaImage4 = caMediaService.getMediaByName(finalInspectionResult.getQcConfirmUser());
//            if (mediaImage4 == null) {
//                throw new PlatformException("检验员：" + finalInspectionResult.getQcConfirmUser() + " 的签名未配置！");
//            }
//            dataMap.put("InsSign", encoder.encode(inputStream2byte(mediaImage4.getMediaStream())));
//            dataMap.put("InsRLT", finalInspectionResult.getPressureTestReportResult() == "OK" ? "PASS" : finalInspectionResult.getPressureTestReportResult());
//            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//            dataMap.put("InsDate", format1.format(finalInspectionResult.getQcConfirmDate()));
//        } else {
        Media mediaImage4 = caMediaService.getMediaByName("Blank");
        if (mediaImage4 == null) {
            dataMap.put("InsSign", encoder.encode(inputStream2byte(inputStream)));
        }else{
            dataMap.put("InsSign", encoder.encode(inputStream2byte(mediaImage4.getMediaStream())));
        }
        dataMap.put("InsRLT", ".");
        dataMap.put("InsDate", ".");
//        }

        // Configuration用于读取模板文件(XML、FTL文件或迅是我们这里用的StringTemplateLoader)
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        // 指定模板文件所在目录的路径,而不是文件的路径
//            String jPath = System.getProperty("user.dir") + "\\cameron-web\\src\\main\\resources\\static\\docx";
        String jPath = AppConstant.DOC_XML_FILE_PATH;
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        // 以utf-8的编码读取模板文件
        Template template = configuration.getTemplate("PressureFM.xml", "utf-8");

        //输出文件
        File outFile = new File(savePath + tfProductSn.getValue() + ".doc");

        //生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")), 1024 * 1024);
        template.process(dataMap, out);
        out.flush();
        out.close();
        streamList.forEach(stream -> {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("-----------成功生成压力测试报告----------------");
        NotificationUtils.notificationInfo("成功生成压力测试报告!");
    }

    /**
     * 生成报告（井口）
     *
     * @param savePath
     * @throws Exception
     */
    public void createDocReportJK(String savePath) throws Exception {
        PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());

        String snPic = tfProductSn.getValue().trim();
        if (Strings.isNullOrEmpty(snPic)) {
            throw new PlatformException("序列号不能为空！！");
        }
        String picFilePath = AppConstant.DOC_XML_FILE_PATH + "\\" + "PressurePictures" + "\\" + snPic.substring(0, snPic.length() - 4) + "\\" + snPic + "\\";

        // 准备填充数据

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("PartNo", labPartNoJK.getValue());
        dataMap.put("PartNo1", labPartNoJK.getValue());
        dataMap.put("PartRev", labPartRevJK.getValue());
        dataMap.put("PartRev1", labPartRevJK.getValue());
        dataMap.put("SerialNo", labSerialNoJK.getValue());
        dataMap.put("SerialNo1", labSerialNoJK.getValue());
        dataMap.put("WorkOrder", labWorkOrderJK.getValue());
        dataMap.put("TestProcedure", labTestProcedureJK.getValue());
        dataMap.put("PslLevel", labPslLevelJK.getValue());
        dataMap.put("Description", labDescriptionJK.getValue().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

        dataMap.put("ProcedureNo", tfProcedureNoJK.getValue());
        dataMap.put("GageNoSize", tfGageNoSizeJK.getValue());

        //本体
        dataMap.put("OpenPone", labOpenPoneJK.getValue() == null ? "" : labOpenPoneJK.getValue());
        dataMap.put("OpenTone", labOpenToneJK.getValue() == null ? "" : labOpenToneJK.getValue());
        dataMap.put("OpenRone", labOpenRoneJK.getValue() == null ? "" : labOpenRoneJK.getValue());

        dataMap.put("OpenPtwo", labOpenPtwoJK.getValue() == null ? "" : labOpenPtwoJK.getValue());
        dataMap.put("OpenTtwo", labOpenTtwoJK.getValue() == null ? "" : labOpenTtwoJK.getValue());
        dataMap.put("OpenRtwo", labOpenRtwoJK.getValue() == null ? "" : labOpenRtwoJK.getValue());

        dataMap.put("OpenPthree", labOpenPthreeJK.getValue() == null ? "" : labOpenPthreeJK.getValue());
        dataMap.put("OpenTthree", labOpenTthreeJK.getValue() == null ? "" : labOpenTthreeJK.getValue());
        dataMap.put("OpenRthree", labOpenRthreeJK.getValue() == null ? "" : labOpenRthreeJK.getValue());

        dataMap.put("Date1", labOpenBodyDateJK.getValue() == null ? "" : labOpenBodyDateJK.getValue());

        //转接头本体（TOP）
        dataMap.put("BodyTOPPone", labCrossoverBodyTOPPoneJK.getValue() == null ? "" : labCrossoverBodyTOPPoneJK.getValue());
        dataMap.put("BodyTOPTone", labCrossoverBodyTOPToneJK.getValue() == null ? "" : labCrossoverBodyTOPToneJK.getValue());
        dataMap.put("BodyTOPRone", labCrossoverBodyTOPRoneJK.getValue() == null ? "" : labCrossoverBodyTOPRoneJK.getValue());

        dataMap.put("BodyTOPPtwo", labCrossoverBodyTOPPtwoJK.getValue() == null ? "" : labCrossoverBodyTOPPtwoJK.getValue());
        dataMap.put("BodyTOPTtwo", labCrossoverBodyTOPTtwoJK.getValue() == null ? "" : labCrossoverBodyTOPTtwoJK.getValue());
        dataMap.put("BodyTOPRtwo", labCrossoverBodyTOPRtwoJK.getValue() == null ? "" : labCrossoverBodyTOPRtwoJK.getValue());

        dataMap.put("BodyTOPPthree", labCrossoverBodyTOPPthreeJK.getValue() == null ? "" : labCrossoverBodyTOPPthreeJK.getValue());
        dataMap.put("BodyTOPTthree", labCrossoverBodyTOPTthreeJK.getValue() == null ? "" : labCrossoverBodyTOPTthreeJK.getValue());
        dataMap.put("BodyTOPRthree", labCrossoverBodyTOPRthreeJK.getValue() == null ? "" : labCrossoverBodyTOPRthreeJK.getValue());
        //转接头本体（BTM）
        dataMap.put("BodyBTMPone", labCrossoverBodyBTMPoneJK.getValue() == null ? "" : labCrossoverBodyBTMPoneJK.getValue());
        dataMap.put("BodyBTMTone", labCrossoverBodyBTMToneJK.getValue() == null ? "" : labCrossoverBodyBTMToneJK.getValue());
        dataMap.put("BodyBTMRone", labCrossoverBodyBTMRoneJK.getValue() == null ? "" : labCrossoverBodyBTMRoneJK.getValue());

        dataMap.put("BodyBTMPtwo", labCrossoverBodyBTMPtwoJK.getValue() == null ? "" : labCrossoverBodyBTMPtwoJK.getValue());
        dataMap.put("BodyBTMTtwo", labCrossoverBodyBTMTtwoJK.getValue() == null ? "" : labCrossoverBodyBTMTtwoJK.getValue());
        dataMap.put("BodyBTMRtwo", labCrossoverBodyBTMRtwoJK.getValue() == null ? "" : labCrossoverBodyBTMRtwoJK.getValue());

        dataMap.put("BodyBTMPthree", labCrossoverBodyBTMPthreeJK.getValue() == null ? "" : labCrossoverBodyBTMPthreeJK.getValue());
        dataMap.put("BodyBTMTthree", labCrossoverBodyBTMTthreeJK.getValue() == null ? "" : labCrossoverBodyBTMTthreeJK.getValue());
        dataMap.put("BodyBTMRthree", labCrossoverBodyBTMRthreeJK.getValue() == null ? "" : labCrossoverBodyBTMRthreeJK.getValue());

        dataMap.put("Date2", labCrossoverBodyDateJK.getValue() == null ? "" : labCrossoverBodyDateJK.getValue());

        //采油树测试
        dataMap.put("TreePone", labChristmasTreePoneJK.getValue() == null ? "" : labChristmasTreePoneJK.getValue());
        dataMap.put("TreeTone", labChristmasTreeToneJK.getValue() == null ? "" : labChristmasTreeToneJK.getValue());
        dataMap.put("TreeRone", labChristmasTreeRoneJK.getValue() == null ? "" : labChristmasTreeRoneJK.getValue());

        dataMap.put("TreePtwo", labChristmasTreePtwoJK.getValue() == null ? "" : labChristmasTreePtwoJK.getValue());
        dataMap.put("TreeTtwo", labChristmasTreeTtwoJK.getValue() == null ? "" : labChristmasTreeTtwoJK.getValue());
        dataMap.put("TreeRtwo", labChristmasTreeRtwoJK.getValue() == null ? "" : labChristmasTreeRtwoJK.getValue());

        dataMap.put("TreePthree", labChristmasTreePthreeJK.getValue() == null ? "" : labChristmasTreePthreeJK.getValue());
        dataMap.put("TreeTthree", labChristmasTreeTthreeJK.getValue() == null ? "" : labChristmasTreeTthreeJK.getValue());
        dataMap.put("TreeRthree", labChristmasTreeRthreeJK.getValue() == null ? "" : labChristmasTreeRthreeJK.getValue());

        dataMap.put("Date3", labChristmasTreeDateJK.getValue() == null ? "" : labChristmasTreeDateJK.getValue());

        //井口头组件
        dataMap.put("WellheadPone", labWellheadAssemblyPoneJK.getValue() == null ? "" : labWellheadAssemblyPoneJK.getValue());
        dataMap.put("WellheadTone", labWellheadAssemblyToneJK.getValue() == null ? "" : labWellheadAssemblyToneJK.getValue());
        dataMap.put("WellheadRone", labWellheadAssemblyRoneJK.getValue() == null ? "" : labWellheadAssemblyRoneJK.getValue());

        dataMap.put("WellheadPtwo", labWellheadAssemblyPtwoJK.getValue() == null ? "" : labWellheadAssemblyPtwoJK.getValue());
        dataMap.put("WellheadTtwo", labWellheadAssemblyTtwoJK.getValue() == null ? "" : labWellheadAssemblyTtwoJK.getValue());
        dataMap.put("WellheadRtwo", labWellheadAssemblyRtwoJK.getValue() == null ? "" : labWellheadAssemblyRtwoJK.getValue());

        dataMap.put("WellheadPthree", labWellheadAssemblyPthreeJK.getValue() == null ? "" : labWellheadAssemblyPthreeJK.getValue());
        dataMap.put("WellheadTthree", labWellheadAssemblyTthreeJK.getValue() == null ? "" : labWellheadAssemblyTthreeJK.getValue());
        dataMap.put("WellheadRthree", labWellheadAssemblyRthreeJK.getValue() == null ? "" : labWellheadAssemblyRthreeJK.getValue());

        //转接头组件（TOP）
        dataMap.put("AssTOPPone", labCrossoverAssemblyTOPPoneJK.getValue() == null ? "" : labCrossoverAssemblyTOPPoneJK.getValue());
        dataMap.put("AssTOPTone", labCrossoverAssemblyTOPToneJK.getValue() == null ? "" : labCrossoverAssemblyTOPToneJK.getValue());
        dataMap.put("AssTOPRone", labCrossoverAssemblyTOPRoneJK.getValue() == null ? "" : labCrossoverAssemblyTOPRoneJK.getValue());

        dataMap.put("AssTOPPtwo", labCrossoverAssemblyTOPPtwoJK.getValue() == null ? "" : labCrossoverAssemblyTOPPtwoJK.getValue());
        dataMap.put("AssTOPTtwo", labCrossoverAssemblyTOPTtwoJK.getValue() == null ? "" : labCrossoverAssemblyTOPTtwoJK.getValue());
        dataMap.put("AssTOPRtwo", labCrossoverAssemblyTOPRtwoJK.getValue() == null ? "" : labCrossoverAssemblyTOPRtwoJK.getValue());

        dataMap.put("AssTOPPthree", labCrossoverAssemblyTOPPthreeJK.getValue() == null ? "" : labCrossoverAssemblyTOPPthreeJK.getValue());
        dataMap.put("AssTOPTthree", labCrossoverAssemblyTOPTthreeJK.getValue() == null ? "" : labCrossoverAssemblyTOPTthreeJK.getValue());
        dataMap.put("AssTOPRthree", labCrossoverAssemblyTOPRthreeJK.getValue() == null ? "" : labCrossoverAssemblyTOPRthreeJK.getValue());

        dataMap.put("Date4", labWellheadAssemblyDateJK.getValue() == null ? "" : labWellheadAssemblyDateJK.getValue());

        //转接头组件（BTM）
        dataMap.put("AssBTMPone", labCrossoverAssemblyBTMPoneJK.getValue() == null ? "" : labCrossoverAssemblyBTMPoneJK.getValue());
        dataMap.put("AssBTMTone", labCrossoverAssemblyBTMToneJK.getValue() == null ? "" : labCrossoverAssemblyBTMToneJK.getValue());
        dataMap.put("AssBTMRone", labCrossoverAssemblyBTMRoneJK.getValue() == null ? "" : labCrossoverAssemblyBTMRoneJK.getValue());

        dataMap.put("AssBTMPtwo", labCrossoverAssemblyBTMPtwoJK.getValue() == null ? "" : labCrossoverAssemblyBTMPtwoJK.getValue());
        dataMap.put("AssBTMTtwo", labCrossoverAssemblyBTMTtwoJK.getValue() == null ? "" : labCrossoverAssemblyBTMTtwoJK.getValue());
        dataMap.put("AssBTMRtwo", labCrossoverAssemblyBTMRtwoJK.getValue() == null ? "" : labCrossoverAssemblyBTMRtwoJK.getValue());

        dataMap.put("AssBTMPthree", labCrossoverAssemblyBTMPthreeJK.getValue() == null ? "" : labCrossoverAssemblyBTMPthreeJK.getValue());
        dataMap.put("AssBTMTthree", labCrossoverAssemblyBTMTthreeJK.getValue() == null ? "" : labCrossoverAssemblyBTMTthreeJK.getValue());
        dataMap.put("AssBTMRthree", labCrossoverAssemblyBTMRthreeJK.getValue() == null ? "" : labCrossoverAssemblyBTMRthreeJK.getValue());

        dataMap.put("Date5", labCrossoverAssemblyDateJK.getValue() == null ? "" : labCrossoverAssemblyDateJK.getValue());

        //采油树通径
        dataMap.put("Accepted", cbPassJK.getValue() ? "√" : " ");
        dataMap.put("NotAccepted", "");
        dataMap.put("Date6", cbPassJK.getValue() ? pressureTest.getDriftTestDate() : "");

        dataMap.put("Date7", pressureTest.getTestDate());

        String[] strPic = new String[]{"image1", "image2", "image3", "image4", "image5", "image6", "image7"};
        int countPic = 0;
        List<InputStream> streamList = Lists.newArrayList();

        InputStream inputStream = new FileInputStream(AppConstant.DOC_XML_FILE_PATH + "\\emp.jpg");
        streamList.add(inputStream);

        // 将Media的InputStream转换为byte[] 然后进行BASE64编码
        BASE64Encoder encoder = new BASE64Encoder();

        //PressureTest pressureTest = pressureTestService.getPressureTestByProductSN(tfProductSn.getValue().trim());
        Media mediaImage7;
        if (Strings.isNullOrEmpty(pressureTest.getTestBy())) {
            mediaImage7 = caMediaService.getMediaByName(RequestInfo.current().getUserName());
            if (mediaImage7 == null) {
                throw new PlatformException("用户：" + RequestInfo.current().getUserName() + " 签名logo未配置添加！");
            }
        } else {
            mediaImage7 = caMediaService.getMediaByName(pressureTest.getTestBy());
            if (mediaImage7 == null) {
                throw new PlatformException("用户：" + pressureTest.getTestBy() + " 签名logo未配置添加！");
            }
        }
        dataMap.put("TS7", encoder.encode(inputStream2byte(mediaImage7.getMediaStream())));

        Media mediaImage6;
        if (cbPassJK.getValue()) {
            if (Strings.isNullOrEmpty(cbDirffTestByJK.getValue().getName())) {
                dataMap.put("TS6", encoder.encode(inputStream2byte(inputStream)));
            } else {
                mediaImage6 = caMediaService.getMediaByName(cbDirffTestByJK.getValue().getName());
                if (mediaImage6 == null) {
                    throw new PlatformException("用户：" + cbDirffTestByJK.getValue().getName() + " 签名logo未配置添加！");
                }
                dataMap.put("TS6", encoder.encode(inputStream2byte(mediaImage6.getMediaStream())));
            }
        } else {
            dataMap.put("TS6", encoder.encode(inputStream2byte(inputStream)));
        }

        if (ckOpenBodyJK.getValue()) {
            if (Strings.isNullOrEmpty(labOpenBodySignatureJK.getValue())) {
                dataMap.put("TS1", encoder.encode(inputStream2byte(inputStream)));
            } else {
                Media mediaImage1 = caMediaService.getMediaByName(labOpenBodySignatureJK.getValue());
                if (mediaImage1 == null) {
                    throw new PlatformException("用户：" + labOpenBodySignatureJK.getValue() + " 签名logo未配置添加！");
                }
                dataMap.put("TS1", encoder.encode(inputStream2byte(mediaImage1.getMediaStream())));
            }
            if (jkOpenBodyRecord.getValue() != null || !"".equals(jkOpenBodyRecord.getValue())) {
                InputStream imageIS1 =
                        new FileInputStream(picFilePath + snPic + "-JkOpenBody.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS1)));
                countPic++;
                streamList.add(imageIS1);
            }

        } else {
            dataMap.put("TS1", encoder.encode(inputStream2byte(inputStream)));
        }
        if (ckCrossoverBodyJK.getValue()) {
            if (Strings.isNullOrEmpty(labCrossoverBodySignatureJK.getValue())) {
                dataMap.put("TS2", encoder.encode(inputStream2byte(inputStream)));
            } else {
                Media mediaImage2 = caMediaService.getMediaByName(labCrossoverBodySignatureJK.getValue());
                if (mediaImage2 == null) {
                    throw new PlatformException("用户：" + labCrossoverBodySignatureJK.getValue() + " 签名logo未配置添加！");
                }
                dataMap.put("TS2", encoder.encode(inputStream2byte(mediaImage2.getMediaStream())));
            }
            if (jkCrossoverBodyTopRecord.getValue() != null || !"".equals(jkCrossoverBodyTopRecord.getValue())) {
                InputStream imageIS2 =
                        new FileInputStream(picFilePath + snPic + "-JkCrossoverBodyTop.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS2)));
                countPic++;
                streamList.add(imageIS2);
            }
            if (jkCrossoverBodyBtmRecord.getValue() != null || !"".equals(jkCrossoverBodyBtmRecord.getValue())) {
                InputStream imageIS3 =
                        new FileInputStream(picFilePath + snPic + "-JkCrossoverBodyBtm.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS3)));
                countPic++;
                streamList.add(imageIS3);
            }

        } else {
            dataMap.put("TS2", encoder.encode(inputStream2byte(inputStream)));
        }
        if (ckChristmasTreeJK.getValue()) {
            if (Strings.isNullOrEmpty(labChristmasTreeSignatureJK.getValue())) {
                dataMap.put("TS3", encoder.encode(inputStream2byte(inputStream)));
            } else {
                Media mediaImage3 = caMediaService.getMediaByName(labChristmasTreeSignatureJK.getValue());
                if (mediaImage3 == null) {
                    throw new PlatformException("用户：" + labChristmasTreeSignatureJK.getValue() + " 签名logo未配置添加！");
                }
                dataMap.put("TS3", encoder.encode(inputStream2byte(mediaImage3.getMediaStream())));
            }
            if (jkChristmasTreeRecord.getValue() != null || !"".equals(jkChristmasTreeRecord.getValue())) {
                InputStream imageIS4 =
                        new FileInputStream(picFilePath + snPic + "-JkChristmasTree.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS4)));
                countPic++;
                streamList.add(imageIS4);
            }
        } else {
            dataMap.put("TS3", encoder.encode(inputStream2byte(inputStream)));
        }
        if (ckWellheadAssemblyJK.getValue()) {
            if (Strings.isNullOrEmpty(labWellheadAssemblySignatureJK.getValue())) {
                dataMap.put("TS4", encoder.encode(inputStream2byte(inputStream)));
            } else {
                Media mediaImage4 = caMediaService.getMediaByName(labWellheadAssemblySignatureJK.getValue());
                if (mediaImage4 == null) {
                    throw new PlatformException("用户：" + labWellheadAssemblySignatureJK.getValue() + " 签名logo未配置添加！");
                }
                dataMap.put("TS4", encoder.encode(inputStream2byte(mediaImage4.getMediaStream())));
            }
            if (jkWellheadAssemblyRecord.getValue() != null || !"".equals(jkWellheadAssemblyRecord.getValue())) {
                InputStream imageIS5 =
                        new FileInputStream(picFilePath + snPic + "-JkWellheadAssembly.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS5)));
                countPic++;
                streamList.add(imageIS5);
            }
        } else {
            dataMap.put("TS4", encoder.encode(inputStream2byte(inputStream)));
        }
        if (ckCrossoverAssemblyJK.getValue()) {
            if (Strings.isNullOrEmpty(labCrossoverAssemblySignatureJK.getValue())) {
                dataMap.put("TS5", encoder.encode(inputStream2byte(inputStream)));
            } else {
                Media mediaImage5 = caMediaService.getMediaByName(labCrossoverAssemblySignatureJK.getValue());
                if (mediaImage5 == null) {
                    throw new PlatformException("用户：" + labCrossoverAssemblySignatureJK.getValue() + " 签名logo未配置添加！");
                }
                dataMap.put("TS5", encoder.encode(inputStream2byte(mediaImage5.getMediaStream())));
            }
            if (jkCrossoverAssemblyTopRecord.getValue() != null || !"".equals(jkCrossoverAssemblyTopRecord.getValue())) {
                InputStream imageIS6 =
                        new FileInputStream(picFilePath + snPic + "-JkCrossoverAssemblyTop.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS6)));
                countPic++;
                streamList.add(imageIS6);
            }
            if (jkCrossoverAssemblyBtmRecord.getValue() != null || !"".equals(jkCrossoverAssemblyBtmRecord.getValue())) {
                InputStream imageIS7 =
                        new FileInputStream(picFilePath + snPic + "-JkCrossoverAssemblyBtm.jpg");
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(imageIS7)));
                countPic++;
                streamList.add(imageIS7);
            }
        } else {
            dataMap.put("TS5", encoder.encode(inputStream2byte(inputStream)));
        }
        if (countPic < 4) {
            for (; countPic < 4; countPic++) {
                dataMap.put(strPic[countPic], encoder.encode(inputStream2byte(inputStream)));
            }
        }
        // Configuration用于读取模板文件(XML、FTL文件或迅是我们这里用的StringTemplateLoader)
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        // 指定模板文件所在目录的路径,而不是文件的路径
//            String jPath = System.getProperty("user.dir") + "\\cameron-web\\src\\main\\resources\\static\\docx";
        String jPath = AppConstant.DOC_XML_FILE_PATH;
        configuration.setDirectoryForTemplateLoading(new File(jPath));
        // 以utf-8的编码读取模板文件
        Template template = configuration.getTemplate("PressureJK.xml", "utf-8");

        //输出文件
        File outFile = new File(savePath + tfProductSn.getValue() + ".doc");

        //生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("UTF-8")), 1024 * 1024);
        template.process(dataMap, out);
        out.flush();
        out.close();
        streamList.forEach(stream -> {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        wordToPDF(savePath + tfProductSn.getValue() + ".doc");
        System.out.println("-----------成功生成压力测试报告----------------");
        NotificationUtils.notificationInfo("成功生成压力测试报告!");
    }

    /**
     * double 转  Date 时间
     *
     * @param dVal
     */
    public Date DoubleToDate(double dVal) {
        Date oDate = new Date();
        @SuppressWarnings("deprecation")
        long localOffset = oDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        oDate.setTime(Math.round((dVal - 25569) * 24 * 3600 * 1000 + localOffset));

        return oDate;
    }

    /**
     * String 转  Date 时间
     *
     * @param strDate
     * @return
     */
    public Date strToDate(String strDate) throws ParseException {
        SimpleDateFormat myFmt = new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH);
        Date date = myFmt.parse(strDate);
        return date;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        List<StationEquipment> stationEquipments = stationEquipmentService.getStationEquipmentAll();
        fmStationCbs.forEach(cbStation -> {
            cbStation.setItems(stationEquipments);
            cbStation.setItemCaptionGenerator(station -> station.getStation() + " (" + station.getEquipmentNo() + ")");
        });

        jkStationCbs.forEach(cbStation -> {
            cbStation.setItems(stationEquipments);
            cbStation.setItemCaptionGenerator(station -> station.getStation() + " (" + station.getEquipmentNo() + ")");
        });

        List<User> users = userService.getByNameLazzy("");
        cbDirffTestBy.setItems(users);
        cbDirffTestBy.setItemCaptionGenerator(user -> user.getUserName());
        cbDirffTestByJK.setItems(users);
        cbDirffTestByJK.setItemCaptionGenerator(user -> user.getUserName());

        resetFM();
        resetJK();
    }

    public void wordToPDF(final String filePath) {
        final Document document = new Document(filePath);
        final String overFile = filePath.replace(".doc", ".pdf");
        document.saveToFile(overFile, FileFormat.PDF);
        System.out.println("pdf------------------");

//        //另存为PDF（MS Office）
//        ComThread.InitSTA();
//        ActiveXComponent app = null;
//        Dispatch doc = null;
//        //转换前的文件路径
//        String startFile = filePath;
//        //转换后的文件路劲
//        String overFile = "";
//        try {
//            app = new ActiveXComponent("Word.Application");
//            app.setProperty("Visible", new Variant(false));
//            Dispatch docs = app.getProperty("Documents").toDispatch();
//
//            overFile = startFile.replace(".doc", ".pdf");
//
//            doc = Dispatch.call(docs, "Open", startFile).toDispatch();
//            File tofile = new File(overFile);
//            if (tofile.exists()) {
//                tofile.delete();
//            }
//            Dispatch.call(doc, "SaveAs", overFile, 17);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            Dispatch.call(doc, "Close", false);
//            if (app != null) {
//                app.invoke("Quit", new Variant[]{});
//            }
//        }
//        //结束后关闭进程
//        ComThread.Release();

        Page.getCurrent().open("http://163.184.139.107:8080/CameronPDFView/viewpdf?filename=" + overFile,
                "_blank", Page.getCurrent().getBrowserWindowWidth(), Page.getCurrent().getBrowserWindowHeight(), BorderStyle.NONE);
    }

    public void resetFM() {
        //表头数据
        for (LabelWithSamleLineCaption lab : labFMs) {
            lab.setValue("");
        }
        tfGageNoSize.clear();
        tfProcedureNo.clear();
        tfTorqueSensor.clear();
        //表格数据
        for (Label lab : labOpenFMs) {
            lab.setValue("");
        }
        for (Label lab : labDownFMs) {
            lab.setValue("");
        }
        for (Label lab : labUpFMs) {
            lab.setValue("");
        }
        ckOpenBody.clear();
        ckOpenBody.setEnabled(false);
        ckDownstream.clear();
        ckDownstream.setEnabled(false);
        ckUpstream.clear();
        ckUpstream.setEnabled(false);
        fmStationCbs.forEach(cbStation -> cbStation.setSelectedItem(null));
        for (Button fmConfirmBtn : fmConfirmBtns) {
            fmConfirmBtn.setEnabled(false);
        }
        for (ComboBox fmRecordCb : fmRecordCbs) {
            fmRecordCb.clear();
        }
        for (Button fmGetBtn : fmGetBtns) {
            fmGetBtn.setEnabled(false);
        }
        for (Button fmSaveBtn : fmSaveBtns) {
            fmSaveBtn.setEnabled(false);
        }
        ckOpenUnder.clear();
        tfTorqueValue1.clear();
        tfTorqueValue2.clear();
        tfTorqueValue3.clear();
        tfTorqueValue4.clear();
        cbPass.clear();
        cbDirffTestBy.clear();
        //图片
        glXYlineLayout.removeAllComponents();
    }

    public void resetJK() {
        //表头数据
        for (LabelWithSamleLineCaption lab : labJks) {
            lab.setValue("");
        }
        tfGageNoSizeJK.clear();
        tfProcedureNoJK.clear();
        //表格数据
        for (Label lab : labOpenJks) {
            lab.setValue("");
        }
        for (Label lab : labCrossBodyTopJks) {
            lab.setValue("");
        }
        for (Label lab : labCrossBodyBtmJks) {
            lab.setValue("");
        }
        for (Label lab : labChrisTreeJks) {
            lab.setValue("");
        }
        for (Label lab : labWellAssemJks) {
            lab.setValue("");
        }
        for (Label lab : labCrossAssemTopJks) {
            lab.setValue("");
        }
        for (Label lab : labCrossAssemBtmJks) {
            lab.setValue("");
        }
        for (Label lab : labSignatureJks) {
            lab.setValue("");
        }
        jkStationCbs.forEach(cbStation -> cbStation.setSelectedItem(null));
        for (Button jkConfirmBtn : jkConfirmBtns) {
            jkConfirmBtn.setEnabled(false);
        }
        ckOpenBodyJK.clear();
        ckOpenBodyJK.setEnabled(false);
        ckCrossoverBodyJK.clear();
        ckCrossoverBodyJK.setEnabled(false);
        ckChristmasTreeJK.clear();
        ckChristmasTreeJK.setEnabled(false);
        ckWellheadAssemblyJK.clear();
        ckWellheadAssemblyJK.setEnabled(false);
        ckCrossoverAssemblyJK.clear();
        ckCrossoverAssemblyJK.setEnabled(false);
        for (ComboBox cb : jkRecordCbs) {
            cb.clear();
        }
        for (Button jkGetBtn : jkGetBtns) {
            jkGetBtn.setEnabled(false);
        }
        for (Button jkSaveBtn : jkSaveBtns) {
            jkSaveBtn.setEnabled(false);
        }
        cbPassJK.clear();
        cbDirffTestByJK.clear();

        //图片
        glXYlineLayoutJK.removeAllComponents();
    }

}
