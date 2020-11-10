package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.ProductRoutingDao;
import com.ags.lumosframework.entity.ProductRoutingEntity;
import com.ags.lumosframework.handler.IProductRoutingHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ProductRoutingHandler extends AbstractBaseEntityHandler<ProductRoutingEntity> implements IProductRoutingHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -444840981369242743L;
	
	@Autowired
	private ProductRoutingDao productRoutingDao;
	
	@Override
	protected BaseEntityDao<ProductRoutingEntity> getDao() {
		return productRoutingDao;
	}
	
	
	
}
