package com.ags.lumosframework.handler.impl;


import com.ags.lumosframework.dao.InspectionTypeDao;
import com.ags.lumosframework.entity.InspectionTypeEntity;
import com.ags.lumosframework.handler.IInspectionTypeHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public class InspectionTypeHandler extends AbstractBaseEntityHandler<InspectionTypeEntity> implements IInspectionTypeHandler {

	private static final long serialVersionUID = -6231946991455627278L;

	@Autowired
	private InspectionTypeDao inspectionTypeDao;

	@Override
	protected BaseEntityDao<InspectionTypeEntity> getDao() {
		return inspectionTypeDao;
	}
	
	
}
