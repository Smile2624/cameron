package com.ags.lumosframework.service;

import com.ags.lumosframework.entity.IssueMaterilWithRoutingInfo;
import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.IssueMaterialList;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IIssueMaterialService extends IBaseDomainObjectService<IssueMaterialList> {

    List<IssueMaterialList> getAll();

    List<IssueMaterialList> getAllNot(String orderNo);

    List<IssueMaterialList> listByOrderNo(String orderNo);

    List<IssueMaterilWithRoutingInfo> getMainInfo(String orderNo);

    IssueMaterialList getByOrderNoAndMatNo(String orderNo,String MatNo);
}
