package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IProductRoutingService extends IBaseDomainObjectService<ProductRouting> {
    List<ProductRouting> getProductRoutingsByIdGroupNo(String ProductId, String RoutingGroup, String InnerGroupNo, String checkStatus);

    List<ProductRouting> getByGroupNoAndInnerNo(String groupNo, String innerNo);

    List<ProductRouting> getProductRoutingsByGroupNoOpration(String RoutingGroup, String InnerGroupNo, String opration);

    String getProductRoutingDesc(String groupNo, String innerNo);

    Boolean checkRoutingStatus(String routingGroup, String innerGroupNo);

    Object[] getPullMaterialStep(String groupNo, String innerNo, String desc);

    List<ProductRouting> getOrderRouting(String groupNo, String innerNo);
}
