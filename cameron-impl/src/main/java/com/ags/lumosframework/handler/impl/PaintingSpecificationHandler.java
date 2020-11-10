package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PaintingSpecificationDao;
import com.ags.lumosframework.entity.PaintingSpecificationEntity;
import com.ags.lumosframework.handler.IPaintingSpecificationHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PaintingSpecificationHandler extends AbstractBaseEntityHandler<PaintingSpecificationEntity> implements IPaintingSpecificationHandler {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6831521138505117735L;
	
	@Autowired
	private PaintingSpecificationDao paintingSpecificationDao;

	@Override
	protected BaseEntityDao<PaintingSpecificationEntity> getDao() {
		return paintingSpecificationDao;
	}
	
	
}
