package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.FinalInspectionResultDao;
import com.ags.lumosframework.entity.FinalInspectionResultEntity;
import com.ags.lumosframework.handler.IFinalInspectionResultHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FinalInspectionResultHandler extends AbstractBaseEntityHandler<FinalInspectionResultEntity> implements IFinalInspectionResultHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -741182394548177968L;
	
	@Autowired
	private FinalInspectionResultDao finalInspectionResultDao;

	@Override
	protected BaseEntityDao<FinalInspectionResultEntity> getDao() {
		return finalInspectionResultDao;
	}
	
}
