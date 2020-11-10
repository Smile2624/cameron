package com.ags.lumosframework.handler.impl;


import com.ags.lumosframework.dao.UniqueDimensionInspectionResultDao;
import com.ags.lumosframework.entity.UniqueDimensionInspectionResultEntity;
import com.ags.lumosframework.handler.IUniqueDimensionInspectionResultHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public class UniqueDimensionInspectionResultHandler extends AbstractBaseEntityHandler<UniqueDimensionInspectionResultEntity> implements IUniqueDimensionInspectionResultHandler {

	private static final long serialVersionUID = 8749473832335450128L;

	@Autowired
	private UniqueDimensionInspectionResultDao dao;
	@Override
	protected BaseEntityDao<UniqueDimensionInspectionResultEntity> getDao() {
		return dao;
	}

	

}
