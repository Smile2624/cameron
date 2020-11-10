package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PurchasingOrderDao;
import com.ags.lumosframework.entity.PurchasingOrderInfoEntity;
import com.ags.lumosframework.handler.IPurchasingOrderHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PurchasingOrderHandler extends AbstractBaseEntityHandler<PurchasingOrderInfoEntity> 
											implements IPurchasingOrderHandler{

	private static final long serialVersionUID = 1004629887364123918L;

	@Autowired
	private PurchasingOrderDao purchasingOrderDao;
	
	@Override
	protected BaseEntityDao<PurchasingOrderInfoEntity> getDao() {
		return purchasingOrderDao;
	}

}
