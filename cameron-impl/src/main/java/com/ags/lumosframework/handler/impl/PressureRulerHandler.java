package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PressureRulerDao;
import com.ags.lumosframework.entity.PressureRulerEntity;
import com.ags.lumosframework.handler.IPressureRulerHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PressureRulerHandler extends AbstractBaseEntityHandler<PressureRulerEntity> implements IPressureRulerHandler{

	private static final long serialVersionUID = -1449682992591999462L;

	@Autowired
	private PressureRulerDao dao;
	@Override
	protected BaseEntityDao<PressureRulerEntity> getDao() {
		return dao;
	}

}
