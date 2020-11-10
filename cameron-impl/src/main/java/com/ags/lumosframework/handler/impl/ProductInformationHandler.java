package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.ProductInformationDao;
import com.ags.lumosframework.entity.ProductInformationEntity;
import com.ags.lumosframework.handler.IProductInformationHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ProductInformationHandler extends AbstractBaseEntityHandler<ProductInformationEntity> implements IProductInformationHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3107340465794978767L;
	
	@Autowired
	private ProductInformationDao productInformationDao;

	@Override
	protected BaseEntityDao<ProductInformationEntity> getDao() {
		return productInformationDao;
	}

}
