package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.UniqueTraceability;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IUniqueTraceabilityService extends IBaseDomainObjectService<UniqueTraceability>{
	
	/**
	 * 
	 * @param custOrder 客户内部订单
	 * @return 
	 * 通过客户的内部生产订单来确认该订单信息是否已经导入
	 */
	public int countByCustOrder(String custOrder);
	/**
	 * 
	 * @param po 页面输入框输入的采购单号，不需要输入item，只需要订单号
	 * @return
	 * 返回PURCHASING_ORDER=po开头的所有记录
	 */
	public List<UniqueTraceability> getByPo(String po);
	/**
	 * 
	 * @param po grid中选中的采购单的posn
	 * @return
	 * 返回以PURCHASING_NO中包含po的所有记录
	 */
	public List<UniqueTraceability> getByPoSn(String po);
	
	public UniqueTraceability getByItem(String serialNo);
}
