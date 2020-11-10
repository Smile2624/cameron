package com.ags.lumosframework.ui.view.caconfig;

import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.service.ICaConfigService;
import com.ags.lumosframework.ui.CameronUI;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.security.annotation.Secured;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult;
import com.ags.lumosframework.web.vaadin.base.annotation.Menu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author peyton
 * @date 2019/10/18 10:49
 */
@Menu(caption = "CaConfig", captionI18NKey = "Cameron.CaConfig", iconPath = "images/icon/system-setting.png", groupName = "Data", order = 14)
@SpringView(name = "CaConfig", ui = CameronUI.class)
@Secured("CaConfig")
public class CaConfigView extends BaseView implements Button.ClickListener {
    private static final long serialVersionUID = -6058694217236710937L;

    private Button btnEditRoute = new Button();
    private Label labValueRoute = new Label();

    private Button btnEditReport = new Button();
    private Label labValueReport = new Label();

    private Button btnEditSqliteFM = new Button();
    private Label labValueSqliteFM = new Label();

    private Button btnEditSqliteJK = new Button();
    private Label labValueSqliteJK = new Label();

//    private Button btnEditAccessFM = new Button();
//    private Label labValueAccessFM = new Label();
//
//    private Button btnEditAccessJK = new Button();
//    private Label labValueAccessJK = new Label();

    private Button btnEditLabview = new Button();
    private Label labValueLabview = new Label();


    @Autowired
    private ICaConfigService caConfigService;
    @Autowired
    private ConfirmRoutingConfigDialog configDialog;
    @Autowired
    private ReportSaveConfigDialog reportConfigDialog;
    @Autowired
    private PressTestSqliteFMConfigDialog sqliteFMConfigDialog;
    @Autowired
    private PressTestSqliteJKConfigDialog sqliteJKConfigDialog;
//    @Autowired
//    private PressTestAccessFMConfigDialog accessFMConfigDialog;
//    @Autowired
//    private PressTestAccessJKConfigDialog accessJKConfigDialog;
    @Autowired
    private PressTestLabviewConfigDialog labviewConfigDialog;


    public CaConfigView() {
        VerticalLayout vlRoot = new VerticalLayout();
        vlRoot.setMargin(false);
        vlRoot.setSizeFull();
        vlRoot.addStyleNames(new String[]{"scroll-y"});

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setWidth("80%");
        vlContent.setMargin(true);

        Panel panel1 = new Panel("工序确认时间配置");
        panel1.setSizeFull();
        VerticalLayout vlConfig1 = new VerticalLayout();
        vlConfig1.setSizeFull();
        btnEditRoute.addClickListener(this);
        btnEditRoute.setDisableOnClick(true);
        btnEditRoute.setIcon(VaadinIcons.EDIT);
        vlConfig1.addComponent(btnEditRoute);
        HorizontalLayout hlConfig1 = new HorizontalLayout();
        hlConfig1.addComponents(new Label("工序确认最小间隔时间（min）："), labValueRoute);
        vlConfig1.addComponent(hlConfig1);
        panel1.setContent(vlConfig1);
        vlContent.addComponent(panel1);

        Panel panel2 = new Panel("报告保存路径配置");
        panel2.setSizeFull();
        VerticalLayout vlConfig2 = new VerticalLayout();
        vlConfig2.setSizeFull();
        btnEditReport.addClickListener(this);
        btnEditReport.setDisableOnClick(true);
        btnEditReport.setIcon(VaadinIcons.EDIT);
        vlConfig2.addComponent(btnEditReport);
        HorizontalLayout hlConfig2 = new HorizontalLayout();
        hlConfig2.addComponents(new Label("报告保存根路径："), labValueReport);
        vlConfig2.addComponent(hlConfig2);
        panel2.setContent(vlConfig2);
        vlContent.addComponent(panel2);

        Panel panel3 = new Panel("压力数据存放目录（Sqlite阀门）配置");
        panel3.setSizeFull();
        VerticalLayout vlConfig3 = new VerticalLayout();
        vlConfig3.setSizeFull();
        btnEditSqliteFM.addClickListener(this);
        btnEditSqliteFM.setDisableOnClick(true);
        btnEditSqliteFM.setIcon(VaadinIcons.EDIT);
        vlConfig3.addComponent(btnEditSqliteFM);
        HorizontalLayout hlConfig3 = new HorizontalLayout();
        hlConfig3.addComponents(new Label("压力数据存放目录（Sqlite阀门）："), labValueSqliteFM);
        vlConfig3.addComponent(hlConfig3);
        panel3.setContent(vlConfig3);
        vlContent.addComponent(panel3);

        Panel panel3A = new Panel("压力数据存放目录（Sqlite井口）配置");
        panel3A.setSizeFull();
        VerticalLayout vlConfig3A = new VerticalLayout();
        vlConfig3A.setSizeFull();
        btnEditSqliteJK.addClickListener(this);
        btnEditSqliteJK.setDisableOnClick(true);
        btnEditSqliteJK.setIcon(VaadinIcons.EDIT);
        vlConfig3A.addComponent(btnEditSqliteJK);
        HorizontalLayout hlConfig3A = new HorizontalLayout();
        hlConfig3A.addComponents(new Label("压力数据存放目录（Sqlite井口）："), labValueSqliteJK);
        vlConfig3A.addComponent(hlConfig3A);
        panel3A.setContent(vlConfig3A);
        vlContent.addComponent(panel3A);

//        Panel panel4 = new Panel("压力数据存放目录（Access阀门）配置");
//        panel4.setSizeFull();
//        VerticalLayout vlConfig4 = new VerticalLayout();
//        vlConfig4.setSizeFull();
//        btnEditAccessFM.addClickListener(this);
//        btnEditAccessFM.setDisableOnClick(true);
//        btnEditAccessFM.setIcon(VaadinIcons.EDIT);
//        vlConfig4.addComponent(btnEditAccessFM);
//        HorizontalLayout hlConfig4 = new HorizontalLayout();
//        hlConfig4.addComponents(new Label("压力数据存放目录（Access阀门）："), labValueAccessFM);
//        vlConfig4.addComponent(hlConfig4);
//        panel4.setContent(vlConfig4);
//        vlContent.addComponent(panel4);
//
//        Panel panel5 = new Panel("压力数据存放目录（Access井口）配置");
//        panel5.setSizeFull();
//        VerticalLayout vlConfig5 = new VerticalLayout();
//        vlConfig5.setSizeFull();
//        btnEditAccessJK.addClickListener(this);
//        btnEditAccessJK.setDisableOnClick(true);
//        btnEditAccessJK.setIcon(VaadinIcons.EDIT);
//        vlConfig5.addComponent(btnEditAccessJK);
//        HorizontalLayout hlConfig5 = new HorizontalLayout();
//        hlConfig5.addComponents(new Label("压力数据存放目录（Access井口）："), labValueAccessJK);
//        vlConfig5.addComponent(hlConfig5);
//        panel5.setContent(vlConfig5);
//        vlContent.addComponent(panel5);

        Panel panel6 = new Panel("压力数据存放目录（Labview）配置");
        panel6.setSizeFull();
        VerticalLayout vlConfig6 = new VerticalLayout();
        vlConfig6.setSizeFull();
        btnEditLabview.addClickListener(this);
        btnEditLabview.setDisableOnClick(true);
        btnEditLabview.setIcon(VaadinIcons.EDIT);
        vlConfig6.addComponent(btnEditLabview);
        HorizontalLayout hlConfig6 = new HorizontalLayout();
        hlConfig6.addComponents(new Label("压力数据存放根目录（Labview）："), labValueLabview);
        vlConfig6.addComponent(hlConfig6);
        panel6.setContent(vlConfig6);
        vlContent.addComponent(panel6);


        vlRoot.addComponent(vlContent);
        vlRoot.setComponentAlignment(vlContent, Alignment.TOP_CENTER);
        this.setSizeFull();
        this.setCompositionRoot(vlRoot);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button button = event.getButton();
        button.setEnabled(true);
        if (btnEditRoute.equals(button)) {
            configDialog.initUIData();
            configDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshRouteConfigValue();
                }
            });
        } else if (btnEditReport.equals(button)) {
            reportConfigDialog.initUIData();
            reportConfigDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshReportConfigValue();
                }
            });
        } else if (btnEditSqliteFM.equals(button)) {
            sqliteFMConfigDialog.initUIData();
            sqliteFMConfigDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshSqliteFMConfigValue();
                }
            });
        } else if (btnEditSqliteJK.equals(button)) {
            sqliteJKConfigDialog.initUIData();
            sqliteJKConfigDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshSqliteJKConfigValue();
                }
            });
        }else if (btnEditLabview.equals(button)) {
            labviewConfigDialog.initUIData();
            labviewConfigDialog.show(getUI(), result -> {
                if (ConfirmResult.Result.OK.equals(result.getResult())) {
                    freshLabviewConfigValue();
                }
            });
        }
//        else if (btnEditAccessFM.equals(button)) {
//            accessFMConfigDialog.initUIData();
//            accessFMConfigDialog.show(getUI(), result -> {
//                if (ConfirmResult.Result.OK.equals(result.getResult())) {
//                    freshAccessFMConfigValue();
//                }
//            });
//        }else if (btnEditAccessJK.equals(button)) {
//            accessJKConfigDialog.initUIData();
//            accessJKConfigDialog.show(getUI(), result -> {
//                if (ConfirmResult.Result.OK.equals(result.getResult())) {
//                    freshAccessJKConfigValue();
//                }
//            });
//        }
    }

    public void freshRouteConfigValue() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_ROUTING_MIN);
        labValueRoute.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    public void freshReportConfigValue() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
        labValueReport.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    public void freshSqliteFMConfigValue() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_FM);
        labValueSqliteFM.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }
    public void freshSqliteJKConfigValue() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_SQLITE_JK);
        labValueSqliteJK.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

//    public void freshAccessFMConfigValue() {
//        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_ACCESS_FM);
//        labValueAccessFM.setValue(caConfig == null ? "" : caConfig.getConfigValue());
//    }
//
//    public void freshAccessJKConfigValue() {
//        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_ACCESS_JK);
//        labValueAccessJK.setValue(caConfig == null ? "" : caConfig.getConfigValue());
//    }

    public void freshLabviewConfigValue() {
        CaConfig caConfig = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_PRESSTEST_LABVIEW);
        labValueLabview.setValue(caConfig == null ? "" : caConfig.getConfigValue());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        freshRouteConfigValue();
        freshReportConfigValue();
        freshSqliteFMConfigValue();
        freshSqliteJKConfigValue();
//        freshAccessFMConfigValue();
//        freshAccessJKConfigValue();
        freshLabviewConfigValue();
    }
}
