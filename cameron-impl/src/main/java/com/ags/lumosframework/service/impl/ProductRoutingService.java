package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.ProductRoutingEntity;
import com.ags.lumosframework.handler.IProductRoutingHandler;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IProductRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@Primary
public class ProductRoutingService extends AbstractBaseDomainObjectService<ProductRouting, ProductRoutingEntity> implements IProductRoutingService {

    @Autowired
    private IProductRoutingHandler productRoutingHandler;

    @Override
    protected IBaseEntityHandler<ProductRoutingEntity> getEntityHandler() {
        return productRoutingHandler;
    }

    @Override
    public List<ProductRouting> getProductRoutingsByIdGroupNo(String ProductId, String RoutingGroup, String InnerGroupNo, String checkStatus) {
        EntityFilter filter = createFilter();
        if (ProductId != null && !"".equals(ProductId)) {
            filter.fieldEqualTo(ProductRoutingEntity.PRODUCT_ID, ProductId);
        }
        if (RoutingGroup != null && !"".equals(RoutingGroup)) {
            filter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, RoutingGroup);
        }
        if (InnerGroupNo != null && !"".equals(InnerGroupNo)) {
            filter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, InnerGroupNo);
        }
        if (checkStatus != null && !"".equals(checkStatus)) {
            if ("All".equals(checkStatus)) {
                filter.fieldNotEqualTo("checkStatus", "passCheck");
            } else {
                filter.fieldEqualTo("checkStatus", checkStatus);
            }
        }
        filter.orderBy(ProductRoutingEntity.PRODUCT_ID, true);
        filter.orderBy(ProductRoutingEntity.ROUTING_GROUP, true);
        filter.orderBy(ProductRoutingEntity.INNER_GROUP_NO_INT, true);
        return listByFilter(filter);
    }


    @Override
    public List<ProductRouting> getByGroupNoAndInnerNo(String groupNo, String innerNo) {
        EntityFilter filter = this.createFilter();
        if (innerNo != groupNo) {
            filter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, groupNo);
        }
        if (innerNo != null) {
            filter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, innerNo);
        }
        filter.orderBy(ProductRoutingEntity.OPRATION_NO, false);
        return listByFilter(filter);
    }

    @Override
    public List<ProductRouting> getProductRoutingsByGroupNoOpration(String RoutingGroup, String InnerGroupNo, String opration) {
        EntityFilter filter = createFilter();
        if (RoutingGroup != null && !"".equals(RoutingGroup)) {
            filter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, RoutingGroup);
        }
        if (InnerGroupNo != null && !"".equals(InnerGroupNo)) {
            filter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, InnerGroupNo);
        }
        if (opration != null && !"".equals(opration)) {
            filter.fieldEqualTo(ProductRoutingEntity.OPRATION_NO, opration);
        }
        return listByFilter(filter);
    }

    @Override
    public String getProductRoutingDesc(String groupNo, String innerNo) {
        return null;
    }

    @Override
    public Boolean checkRoutingStatus(String routingGroup, String innerGroupNo) {
        List<ProductRouting> productRoutingList = this.getByGroupNoAndInnerNo(routingGroup, innerGroupNo);
        if (productRoutingList != null && productRoutingList.size() != 0) {
            Iterator var4 = productRoutingList.iterator();
            ProductRouting routing;
            do {
                if (!var4.hasNext()) {
                    return true;
                }
                routing = (ProductRouting) var4.next();
            } while ("passCheck".equals(routing.getCheckStatus()));

            return false;
        } else {
            return false;
        }
    }

    @Override
    public Object[] getPullMaterialStep(String groupNo, String innerNo, String desc) {
        boolean routingExist = false;
        String patern = "PULL MATERIAL PER BOM";
        ProductRouting productRouting = null;
        EntityFilter filter = this.createFilter();
        if (innerNo != groupNo) {
            filter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, groupNo);
        }
        if (innerNo != null) {
            filter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, innerNo);
        }
        filter.orderBy(ProductRoutingEntity.OPRATION_NO, false);
        List<ProductRouting> productRoutings = listByFilter(filter);
        if (productRoutings != null && productRoutings.size() > 0) {
            routingExist = true;
            ProductRouting temp = productRoutings.get(0);
            String operationDesc = temp.getOprationDesc();
            if (operationDesc.contains("发料") || operationDesc.toUpperCase().contains(patern.toUpperCase())) {
                productRouting = temp;
            }
        }
        return new Object[]{routingExist, productRouting};
    }

    @Override
    public List<ProductRouting> getOrderRouting(String groupNo, String innerNo) {
        EntityFilter filter = this.createFilter();
        if (innerNo != groupNo) {
            filter.fieldEqualTo(ProductRoutingEntity.ROUTING_GROUP, groupNo);
        }
        if (innerNo != null) {
            filter.fieldEqualTo(ProductRoutingEntity.INNER_GROUP_NO, innerNo);
        }
        filter.orderBy(ProductRoutingEntity.OPRATION_NO, false);
        return listByFilter(filter);
    }
}
