package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.HardnessTestReportDao;
import com.ags.lumosframework.entity.HardnessTestReportEntity;
import com.ags.lumosframework.handler.IHardnessTestReportHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HardnessTestReportHandler extends AbstractBaseEntityHandler<HardnessTestReportEntity> implements IHardnessTestReportHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3631734580515811290L;
	
	@Autowired
	private HardnessTestReportDao hardnessTestReportDao;
	
	@Override
	protected BaseEntityDao<HardnessTestReportEntity> getDao() {
		return hardnessTestReportDao;
	}
	
}
