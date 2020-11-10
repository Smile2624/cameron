package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.HardnessTestReportItemsDao;
import com.ags.lumosframework.entity.HardnessTestReportItemsEntity;
import com.ags.lumosframework.handler.IHardnessTestReportItemsHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HardnessTestReportItemsHandler extends AbstractBaseEntityHandler<HardnessTestReportItemsEntity> implements IHardnessTestReportItemsHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7395034762420744099L;
	
	@Autowired
	private HardnessTestReportItemsDao hardnessTestReportItemsDao;
	
	@Override
	protected BaseEntityDao<HardnessTestReportItemsEntity> getDao() {
		return hardnessTestReportItemsDao;
	}

}
