package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.FinalInspectionItemsDao;
import com.ags.lumosframework.entity.FinalInspectionItemsEntity;
import com.ags.lumosframework.handler.IFinalInspectionItemsHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FinalInspectionItemsHandler extends AbstractBaseEntityHandler<FinalInspectionItemsEntity> implements IFinalInspectionItemsHandler{

	private static final long serialVersionUID = 1593826581471926063L;
	@Autowired
	FinalInspectionItemsDao finalInspectionItemsDao;
	
	@Override
	protected BaseEntityDao<FinalInspectionItemsEntity> getDao() {
		return finalInspectionItemsDao;
	}

}
