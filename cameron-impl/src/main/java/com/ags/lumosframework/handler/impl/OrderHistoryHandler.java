package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.OrderHistoryDao;
import com.ags.lumosframework.entity.OrderHistoryEntity;
import com.ags.lumosframework.handler.IOrderHistoryHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OrderHistoryHandler extends AbstractBaseEntityHandler<OrderHistoryEntity> implements IOrderHistoryHandler{
	private static final long serialVersionUID = -1036604867286185243L;
	@Autowired
	private OrderHistoryDao dao;
	@Override
	protected BaseEntityDao<OrderHistoryEntity> getDao() {
		return dao;
	}

}
