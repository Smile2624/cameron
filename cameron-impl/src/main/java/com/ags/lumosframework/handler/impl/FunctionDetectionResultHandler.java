package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.FunctionDetectionResultDao;
import com.ags.lumosframework.entity.FunctionDetectionResultEntity;
import com.ags.lumosframework.handler.IFunctionDetectionResultHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FunctionDetectionResultHandler extends AbstractBaseEntityHandler<FunctionDetectionResultEntity> implements IFunctionDetectionResultHandler {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2367215725719134987L;
	
	@Autowired
	private FunctionDetectionResultDao FunctionDetectionResultDao;
	
	@Override
	protected BaseEntityDao<FunctionDetectionResultEntity> getDao() {
		return FunctionDetectionResultDao;
	}
}
