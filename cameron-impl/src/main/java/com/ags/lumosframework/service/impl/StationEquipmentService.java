package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.StationEquipmentEntity;
import com.ags.lumosframework.handler.IStationEquipmentHandler;
import com.ags.lumosframework.pojo.StationEquipment;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IStationEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class StationEquipmentService extends AbstractBaseDomainObjectService<StationEquipment,StationEquipmentEntity> implements IStationEquipmentService {
	
	@Autowired
	private IStationEquipmentHandler stationEquipmentHandler;

	@Override
	protected IBaseEntityHandler<StationEquipmentEntity> getEntityHandler() {
		return stationEquipmentHandler;
	}

	@Override
	public List<StationEquipment> getStationEquipmentAll() {
		EntityFilter filter = createFilter();
		return listByFilter(filter);
	}

	@Override
	public StationEquipment getStationEquipmentByStation(String station){
		EntityFilter filter = createFilter();
        if (station != null && !"".equals(station)){
            filter.fieldEqualTo(StationEquipmentEntity.STATION, station);
        }
        return getByFilter(filter);
	}

	@Override
	public StationEquipment getStationEquipmentByNameNo(String station, String equNo) {
		EntityFilter filter = createFilter();
		if (station != null && !"".equals(station)){
			filter.fieldEqualTo(StationEquipmentEntity.STATION, station);
		}
		if (equNo != null && !"".equals(equNo)){
			filter.fieldEqualTo(StationEquipmentEntity.EQUIPMENT_NO, equNo);
		}
		return getByFilter(filter);
	}
}
