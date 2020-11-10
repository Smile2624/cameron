package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.pojo.OrderRoutingConfirmInfo;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IOrderHistoryService extends IBaseDomainObjectService<OrderHistory>{

	List<OrderRoutingConfirmInfo> getByOrderNo(String orderNo);
	
	OrderHistory getByOrderNoOPerationNo(String orderNo, String operationNo);
	
	List<OrderHistory> getByOrderId(String orderNo);

	List<OrderHistory> getConfirmedOrderById(String orderNo);
}
