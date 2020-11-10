package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.UniqueTraceabilityDao;
import com.ags.lumosframework.entity.UniqueTraceabilityEntity;
import com.ags.lumosframework.handler.IUniqueTraceabilityHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UniqueTraceabilityhandler extends AbstractBaseEntityHandler<UniqueTraceabilityEntity> implements IUniqueTraceabilityHandler{

	private static final long serialVersionUID = -7757212069846234632L;

	@Autowired
	private UniqueTraceabilityDao dao;
	@Override
	protected BaseEntityDao<UniqueTraceabilityEntity> getDao() {
		return dao;
	}

}
