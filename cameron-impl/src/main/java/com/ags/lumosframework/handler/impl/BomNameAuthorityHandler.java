package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.BomNameAuthorityDao;
import com.ags.lumosframework.entity.BomNameAuthorityEntity;
import com.ags.lumosframework.handler.IBomNameAuthorityHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class BomNameAuthorityHandler extends AbstractBaseEntityHandler<BomNameAuthorityEntity> implements IBomNameAuthorityHandler {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2663582754663774860L;
	
	@Autowired
	private BomNameAuthorityDao bomNameAuthorityDao;

	@Override
	protected BaseEntityDao<BomNameAuthorityEntity> getDao() {
		return bomNameAuthorityDao;
	}
	
	
}
