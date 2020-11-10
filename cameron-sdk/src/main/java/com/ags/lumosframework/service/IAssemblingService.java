package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IAssemblingService extends IBaseDomainObjectService<Assembling> {
    List<Assembling> getBySn(String no);

    List<Assembling> getBySnType(String no, String type);

    List<Assembling> getByOrderNo(String orderNo);

    Assembling getBySnAndPartNo(String snBatch, String partNo, String rowIndex);

    //获取一个工单所有的工单SN集合
    List<String> getOrderSNList(String orderNo);
}
