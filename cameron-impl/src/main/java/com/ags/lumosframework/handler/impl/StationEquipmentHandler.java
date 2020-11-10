package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.StationEquipmentDao;
import com.ags.lumosframework.entity.StationEquipmentEntity;
import com.ags.lumosframework.handler.IStationEquipmentHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class StationEquipmentHandler extends AbstractBaseEntityHandler<StationEquipmentEntity> implements IStationEquipmentHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4514722766794483555L;
	
	@Autowired
	private StationEquipmentDao stationEquipmentDao;

	@Override
	protected BaseEntityDao<StationEquipmentEntity> getDao() {
		return stationEquipmentDao;
	}
	
	
}
