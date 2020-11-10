package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.handler.IProductionOrderHandler;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ProductionOrderService extends AbstractBaseDomainObjectService<ProductionOrder, ProductionOrderEntity> implements IProductionOrderService {

    @Autowired
    private IProductionOrderHandler productionOrderHandler;

    @Override
    protected IBaseEntityHandler<ProductionOrderEntity> getEntityHandler() {
        return productionOrderHandler;
    }

    @Override
    public ProductionOrder getByNo(String no) {
        EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)) {
            filter.fieldEqualTo(ProductionOrderEntity.PRODUCT_ORDER_ID, no);
        }
        return getByFilter(filter);
    }
    
    @Override
	public List<ProductionOrder> getByPartNoPartNoRev(String partNo, String partNoRev){
		EntityFilter filter = createFilter();
        if (partNo != null && !"".equals(partNo)){
            filter.fieldEqualTo(ProductionOrderEntity.PRODUCT_ID, partNo);
        }
        if (partNoRev != null && !"".equals(partNoRev)){
            filter.fieldEqualTo(ProductionOrderEntity.PRODUCT_VERSION_ID, partNoRev);
        }
        
        return listByFilter(filter);
	}

    @Override
    public List<ProductionOrder> getAllOrder() {
        EntityFilter filter = createFilter();
        return listByFilter(filter);
    }

    @Override
    public List<ProductionOrder> getBySuperiorOrder(String supWoNo) {
        EntityFilter filter = createFilter();
        if (supWoNo != null && !"".equals(supWoNo)){
            filter.fieldEqualTo(ProductionOrderEntity.SUPERIOR_ORDER, supWoNo);
        }
        return listByFilter(filter);
    }

    //Changed by Cameron: 加入getChildCount、hasChildren、fetchChildren方法，用于展示子工单
    @Override
    public int getChildCount(ProductionOrder productionOrder) {
        EntityFilter filter = createFilter();
        if (productionOrder != null && !"".equals(productionOrder.getProductOrderId())) {
            filter.fieldEqualTo(ProductionOrderEntity.SUPERIOR_ORDER, productionOrder.getProductOrderId());
        }
        return listByFilter(filter).size();
    }

    @Override
    public boolean hasChildren(ProductionOrder productionOrder) {
        EntityFilter filter = createFilter();
        if (productionOrder != null && !"".equals(productionOrder.getProductOrderId())) {
            filter.fieldEqualTo(ProductionOrderEntity.SUPERIOR_ORDER, productionOrder.getProductOrderId());
        }
        return (listByFilter(filter).size() > 0);
    }

    @Override
    public List<ProductionOrder> fetchChildren(ProductionOrder productionOrder) {
        EntityFilter filter = createFilter();
        if (productionOrder != null && !"".equals(productionOrder.getProductOrderId())) {
            filter.fieldEqualTo(ProductionOrderEntity.SUPERIOR_ORDER, productionOrder.getProductOrderId());
        }
        return listByFilter(filter);
    }
}
