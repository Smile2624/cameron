package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.AppearanceInstrumentationResultDao;
import com.ags.lumosframework.entity.AppearanceInstrumentationResultEntity;
import com.ags.lumosframework.handler.IAppearanceInstrumentationResultHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AppearanceInstrumentationResultHandler extends AbstractBaseEntityHandler<AppearanceInstrumentationResultEntity> implements IAppearanceInstrumentationResultHandler {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7168924668414623667L;
	
	@Autowired
	private AppearanceInstrumentationResultDao appearanceInstrumentationResultDao;
	
	@Override
	protected BaseEntityDao<AppearanceInstrumentationResultEntity> getDao() {
		return appearanceInstrumentationResultDao;
	}
	
}
