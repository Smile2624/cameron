package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.DimensionInspectionDao;
import com.ags.lumosframework.entity.DimensionInspectionResultEntity;
import com.ags.lumosframework.handler.IDimensionInspectionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DimensionInspectionHandler extends AbstractBaseEntityHandler<DimensionInspectionResultEntity> implements IDimensionInspectionHandler{

	private static final long serialVersionUID = -2475455228554937388L;

	@Autowired
	private DimensionInspectionDao dimensionInspectionDao;
	@Override
	protected BaseEntityDao<DimensionInspectionResultEntity> getDao() {
		return dimensionInspectionDao;
	}

}
