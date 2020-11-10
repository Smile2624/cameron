package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.ReceivingInspectionDao;
import com.ags.lumosframework.entity.ReceivingInspectionEntity;
import com.ags.lumosframework.handler.IReceivingInspectionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ReceivingInspectionHandler extends AbstractBaseEntityHandler<ReceivingInspectionEntity> implements IReceivingInspectionHandler{

	private static final long serialVersionUID = -3835265716346767L;

	@Autowired
	private ReceivingInspectionDao dao;
	@Override
	protected BaseEntityDao<ReceivingInspectionEntity> getDao() {
		return dao;
	}

}
