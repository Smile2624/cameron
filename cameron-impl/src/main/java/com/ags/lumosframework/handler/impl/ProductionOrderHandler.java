package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.ProductionOrderDao;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.handler.IProductionOrderHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ProductionOrderHandler extends AbstractBaseEntityHandler<ProductionOrderEntity> implements IProductionOrderHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4886833270745643116L;
	
	@Autowired
	private ProductionOrderDao productionOrderDao;

	@Override
	protected BaseEntityDao<ProductionOrderEntity> getDao() {
		return productionOrderDao;
	}
	
	
	
}
