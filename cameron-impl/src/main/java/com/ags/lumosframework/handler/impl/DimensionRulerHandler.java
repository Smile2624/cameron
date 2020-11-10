package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.DimensionRulerDao;
import com.ags.lumosframework.entity.DimensionRulerEntity;
import com.ags.lumosframework.handler.IDimensionRulerHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DimensionRulerHandler extends AbstractBaseEntityHandler<DimensionRulerEntity> implements IDimensionRulerHandler {
	
	private static final long serialVersionUID = 7946599139675041416L;
	
	@Autowired
	private DimensionRulerDao dimensionRulerDao;
	
	@Override
	protected BaseEntityDao<DimensionRulerEntity> getDao() {
		return dimensionRulerDao;
	}

}
