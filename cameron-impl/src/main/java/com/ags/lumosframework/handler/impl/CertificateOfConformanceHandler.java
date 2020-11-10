package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.CertificateOfConformanceDao;
import com.ags.lumosframework.entity.CertificateOfConformanceEntity;
import com.ags.lumosframework.handler.ICertificateOfConformanceHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CertificateOfConformanceHandler extends AbstractBaseEntityHandler<CertificateOfConformanceEntity> implements ICertificateOfConformanceHandler {
	
	private static final long serialVersionUID = -2414038986816282099L;
	
	@Autowired
	private CertificateOfConformanceDao certificateOfConformanceDao;
	
	@Override
	protected BaseEntityDao<CertificateOfConformanceEntity> getDao() {
		return certificateOfConformanceDao;
	}
	
}
