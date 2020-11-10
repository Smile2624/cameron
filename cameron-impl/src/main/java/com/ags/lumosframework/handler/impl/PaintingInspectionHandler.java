package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PaintingInspectionDao;
import com.ags.lumosframework.entity.PaintingInspectionEntity;
import com.ags.lumosframework.handler.IPaintingInspectionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PaintingInspectionHandler extends AbstractBaseEntityHandler<PaintingInspectionEntity> implements IPaintingInspectionHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8623127023860821038L;
	
	@Autowired
	private PaintingInspectionDao paintingInspectionDao;
	
	@Override
	protected BaseEntityDao<PaintingInspectionEntity> getDao() {
		return paintingInspectionDao;
	}
	
}
