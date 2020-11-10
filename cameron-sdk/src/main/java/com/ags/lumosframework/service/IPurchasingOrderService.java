package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IPurchasingOrderService  extends IBaseDomainObjectService<PurchasingOrderInfo>{

	PurchasingOrderInfo getBySapInspectionLot(String SapInspectionLot);
	
	List<PurchasingOrderInfo> getByPurchasingNo(String purchasingNo);
	
	List<PurchasingOrderInfo> getUncheckedOrder(String purchasingNo,String type);
	
	List<PurchasingOrderInfo> getInspectionedOrder(String orderNo);//获取已经完成尺寸检验跟因固定检验的采购单
	
	List<PurchasingOrderInfo> getCheckedOrder(String purchasingNo,String type);
	
	List<String> getPurchasingNo(String type);//获取所有未检order作为尺寸检验页面的order下拉框元素

	void deleteOrderList(String orderNo);
}
