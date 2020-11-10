package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.OrderHistoryEntity;
import com.ags.lumosframework.handler.IOrderHistoryHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.OrderHistory;
import com.ags.lumosframework.pojo.OrderRoutingConfirmInfo;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IOrderHistoryService;
import com.ags.lumosframework.service.IProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class OrderHistoryService extends AbstractBaseDomainObjectService<OrderHistory, OrderHistoryEntity> implements IOrderHistoryService{

	
	@Autowired
	private IOrderHistoryHandler handler;
	
	@Autowired
	private IProductionOrderService orderService;
	
	@Autowired
	private DBHandler dbHandler;
	@Override
	public List<OrderRoutingConfirmInfo> getByOrderNo(String orderNo) {
		List<OrderRoutingConfirmInfo> orderRoutingConfirmInfoList = new ArrayList<>();
		ProductionOrder order = orderService.getByNo(orderNo);
		String routingGroup = order.getRoutingGroup();
		String innerGroupNo = order.getInnerGroupNo();
		String sql ="SELECT A.OPRATION_NO,A.OPRATION_DESC,A.ATTENTION,B.CONFIRM_BY,B.RECONFIRM_BY FROM PRODUCT_ROUTING A " + 
				"LEFT JOIN ORDER_HISTORY B ON " + 
				"A.OPRATION_NO = B.OPERATION_NO AND B.ORDER_NO='"+orderNo+"' " + 
				"WHERE A.ROUTING_GROUP='"+routingGroup+"' " + 
				"AND A.INNER_GROUP_NO='"+innerGroupNo+"' ";
		List<?> list = dbHandler.listBySql(sql, null);
		for(Object obj : list) {
			Object[] fields = (Object[]) obj;
			OrderRoutingConfirmInfo confirmInfo = new OrderRoutingConfirmInfo();
			confirmInfo.setOperationNo(fields[0].toString());
			confirmInfo.setOperationDesc(fields[1].toString());
			confirmInfo.setOperationAttention(fields[2]==null ? "":fields[2].toString());
			confirmInfo.setConfirmBy(fields[3]==null ? "":fields[3].toString());
			confirmInfo.setReConfirmBy(fields[4]==null ?"":fields[4].toString());
			orderRoutingConfirmInfoList.add(confirmInfo);
		}
		return orderRoutingConfirmInfoList;
		
		
	}

	@Override
	protected IBaseEntityHandler<OrderHistoryEntity> getEntityHandler() {
		return handler;
	}
	
	@Override
	public OrderHistory getByOrderNoOPerationNo(String orderNo, String operationNo){
		EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)){
            filter.fieldEqualTo(OrderHistoryEntity.ORDER_NO, orderNo);
        }
        if (operationNo != null && !"".equals(operationNo)){
            filter.fieldEqualTo(OrderHistoryEntity.OPERATION_NO, operationNo);
        }

        return getByFilter(filter);
	}
	
	@Override
	public List<OrderHistory> getByOrderId(String orderNo){
		EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)){
            filter.fieldEqualTo(OrderHistoryEntity.ORDER_NO, orderNo);
        }
		filter.fieldEqualTo(OrderHistoryEntity.DELETE_FLAG,false);
        filter.orderBy(OrderHistoryEntity.OPERATION_NO,false);
        return listByFilter(filter);
	}

	@Override
	public List<OrderHistory> getConfirmedOrderById(String orderNo){
		EntityFilter filter = createFilter();
		if (orderNo != null && !"".equals(orderNo)){
			filter.fieldEqualTo(OrderHistoryEntity.ORDER_NO, orderNo);
		}
		filter.fieldEqualTo(OrderHistoryEntity.DELETE_FLAG,false);
		filter.fieldIsNull(OrderHistoryEntity.CONFIRM_BY,false);
		filter.orderBy(OrderHistoryEntity.CONFIRM_DATE,false);
		return listByFilter(filter);
	}
}
