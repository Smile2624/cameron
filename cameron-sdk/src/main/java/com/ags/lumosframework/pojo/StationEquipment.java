package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.StationEquipmentEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class StationEquipment extends ObjectBaseImpl<StationEquipmentEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2900932317094801131L;
	
	public StationEquipment() {
		super(null);
	}
	
	public StationEquipment(StationEquipmentEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getStation() {
        return getInternalObject().getStation();
    }

    public void setStation(String station) {
        getInternalObject().setStation(station);
    }

    public String getEquipmentNo() {
        return getInternalObject().getEquipmentNo();
    }

    public void setEquipmentNo(String equipmentNo) {
        getInternalObject().setEquipmentNo(equipmentNo);
    }

    public String getIpAdress() {
        return getInternalObject().getIpAdress();
    }

    public void setIpAdress(String ipAdress) {
        getInternalObject().setIpAdress(ipAdress);
    }

    public String getEquipmentType() {
        return getInternalObject().getEquipmentType();
    }

    public void setEquipmentType(String equipmentType) {
        getInternalObject().setEquipmentType(equipmentType);
    }

    public String getProcedureNo() {
        return getInternalObject().getProcedureNo();
    }

    public void setProcedureNo(String procedureNo) {
        getInternalObject().setProcedureNo(procedureNo);
    }

}
