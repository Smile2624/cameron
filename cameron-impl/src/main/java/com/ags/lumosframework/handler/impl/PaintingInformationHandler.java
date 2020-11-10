package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PaintingInformationDao;
import com.ags.lumosframework.entity.PaintingInformationEntity;
import com.ags.lumosframework.handler.IPaintingInformationHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PaintingInformationHandler extends AbstractBaseEntityHandler<PaintingInformationEntity> implements IPaintingInformationHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3019240865172374044L;
	
	@Autowired
	private PaintingInformationDao PaintingInformationDao;
	
	@Override
	protected BaseEntityDao<PaintingInformationEntity> getDao() {
		return PaintingInformationDao;
	}
	
	
	
}
