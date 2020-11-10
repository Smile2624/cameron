package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.UniqueDimensionInspectionDao;
import com.ags.lumosframework.entity.UniqueDimensionInspectionEntity;
import com.ags.lumosframework.handler.IUniqueDimensionInspectionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public class UniqueDimensionInspectionHandler extends AbstractBaseEntityHandler<UniqueDimensionInspectionEntity> implements IUniqueDimensionInspectionHandler {

	private static final long serialVersionUID = 6966023397324830713L;

	@Autowired
	private UniqueDimensionInspectionDao dao;
	@Override
	protected BaseEntityDao<UniqueDimensionInspectionEntity> getDao() {
		return dao;
	}

}
