package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.InspectionValueDao;
import com.ags.lumosframework.entity.InspectionValueEntity;
import com.ags.lumosframework.handler.IInspectionValueHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public class InspectionValueHandler extends AbstractBaseEntityHandler<InspectionValueEntity> implements IInspectionValueHandler{

	
	private static final long serialVersionUID = 3758403146011672712L;
	
	@Autowired
	private InspectionValueDao inspectionValueDao;
	@Override
	protected BaseEntityDao<InspectionValueEntity> getDao() {
		return inspectionValueDao;
	}

}
