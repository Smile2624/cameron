package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IProductionOrderService extends IBaseDomainObjectService<ProductionOrder> {
    ProductionOrder getByNo(String no);
    
    List<ProductionOrder> getByPartNoPartNoRev(String partNo, String partNoRev);

    List<ProductionOrder> getAllOrder();

    List<ProductionOrder> getBySuperiorOrder(String supWoNo);
    //Changed by Cameron: 加入getChildCount、hasChildren、fetchChildren，用于展示子工单
    int getChildCount(ProductionOrder productionOrder);

    boolean hasChildren(ProductionOrder productionOrder);

    List<ProductionOrder> fetchChildren(ProductionOrder productionOrder);
}
