package com.ags.lumosframework.ui.view.stationequipment;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.StationEquipment;
import com.ags.lumosframework.service.IStationEquipmentService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class AddStationEquipmentDialog extends BaseDialog {


    /**
     *
     */
    private static final long serialVersionUID = -4391812236012226513L;

    @I18Support(caption = "Station", captionKey = "StationEquipment.Station")
    private TextField tfStation = new TextField();

    @I18Support(caption = "EquipmentNo", captionKey = "StationEquipment.EquipmentNo")
    private TextField tfEquipmentNo = new TextField();

    @I18Support(caption = "EquipmentType", captionKey = "StationEquipment.EquipmentType")
    private ComboBox<String> cbEquipmentType = new ComboBox<>();

    @I18Support(caption = "ProcedureNo", captionKey = "StationEquipment.ProcedureNo")
    private TextField tfProcedureNo = new TextField();

    @I18Support(caption = "IpAdress", captionKey = "StationEquipment.IpAdress")
    private TextField tfIpAdress = new TextField();


    private Binder<StationEquipment> binder = new Binder<>();

    private String caption;

    private Boolean isNew;

    private StationEquipment stationEquipment;

    private IStationEquipmentService stationEquipmentService;

    public AddStationEquipmentDialog(IStationEquipmentService stationEquipmentService) {
        this.stationEquipmentService = stationEquipmentService;
    }

    public void setObject(StationEquipment stationEquipment) {
        String captionName = I18NUtility.getValue("Cameron.StationEquipment.caption", "StationEquipment");
        if (stationEquipment == null) {
            this.caption = I18NUtility.getValue("common.new", "New", captionName);
            stationEquipment = new StationEquipment();
            tfStation.setEnabled(true);
            tfEquipmentNo.setEnabled(true);
            isNew=true;
        } else {
            this.caption = I18NUtility.getValue("common.modify", "Modify", captionName);
            tfStation.setEnabled(false);
            tfEquipmentNo.setEnabled(false);
            isNew=false;
        }
        this.stationEquipment = stationEquipment;
        binder.readBean(stationEquipment);
    }

    @Override
    public void show(UI parentUI, DialogCallBack callBack) {
        setHeightUnDefinedMode();
        showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, false, true, callBack);
    }

    @Override
    protected void initUIData() {
        cbEquipmentType.setItems(AppConstant.PRESSURE_SQLITE,AppConstant.PRESSURE_LABVIEW);

        binder.forField(tfEquipmentNo)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(StationEquipment::getEquipmentNo, StationEquipment::setEquipmentNo);
        binder.forField(cbEquipmentType)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(StationEquipment::getEquipmentType, StationEquipment::setEquipmentType);
        binder.forField(tfStation)
                .asRequired(I18NUtility.getValue("Common.RequiredFiledNotEmpty", "Required filed cannot be empty"))
                .bind(StationEquipment::getStation, StationEquipment::setStation);
        binder.bind(tfProcedureNo, StationEquipment::getProcedureNo, StationEquipment::setProcedureNo);
        binder.bind(tfIpAdress, StationEquipment::getIpAdress, StationEquipment::setIpAdress);

    }

    @Override
    protected void okButtonClicked() throws Exception {
        binder.writeBean(stationEquipment);
        String station = tfStation.getValue().trim();
        String equNo = tfEquipmentNo.getValue().trim();
        if (checkIsSaved(station, equNo)) {
            if(isNew){
                throw new PlatformException("设备编号：" + equNo +" 工位：" + station +  " 已存在！");
            }else{
                stationEquipment = stationEquipmentService.getStationEquipmentByNameNo(station, equNo);
            }
        }
        stationEquipment.setEquipmentType(cbEquipmentType.getValue());
        stationEquipment.setProcedureNo(tfProcedureNo.getValue().trim());
        stationEquipment.setIpAdress(tfIpAdress.getValue().trim());
        StationEquipment save = stationEquipmentService.save(stationEquipment);
        result.setObj(save);
    }

    public boolean checkIsSaved(String station, String equNo) {
        boolean flag = false;
        if (!Strings.isNullOrEmpty(station)) {
            StationEquipment stationEquipmentList = stationEquipmentService.getStationEquipmentByNameNo(station, equNo);
            if (stationEquipmentList != null) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    protected void cancelButtonClicked() {

    }

    @Override
    protected Component getDialogContent() {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeFull();

        tfEquipmentNo.setWidth("100%");
        cbEquipmentType.setWidth("100%");
        tfStation.setWidth("100%");
        tfProcedureNo.setWidth("100%");
        tfIpAdress.setWidth("100%");

        vlContent.addComponents(tfEquipmentNo, cbEquipmentType, tfStation,tfProcedureNo, tfIpAdress);
        return vlContent;
    }
}
