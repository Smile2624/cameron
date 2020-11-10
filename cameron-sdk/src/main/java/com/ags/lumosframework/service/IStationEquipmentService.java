package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.StationEquipment;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IStationEquipmentService extends IBaseDomainObjectService<StationEquipment> {
	List<StationEquipment> getStationEquipmentAll();

	StationEquipment getStationEquipmentByStation(String station);
	StationEquipment getStationEquipmentByNameNo(String station,String equNo);
}
