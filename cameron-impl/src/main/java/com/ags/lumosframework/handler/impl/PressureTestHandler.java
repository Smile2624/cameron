package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PressureTestDao;
import com.ags.lumosframework.entity.PressureTestEntity;
import com.ags.lumosframework.handler.IPressureTestHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PressureTestHandler extends AbstractBaseEntityHandler<PressureTestEntity> implements IPressureTestHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5587815601654684458L;
	
	@Autowired
	private PressureTestDao pressureTestDao;

	@Override
	protected BaseEntityDao<PressureTestEntity> getDao() {
		return pressureTestDao;
	}
}
