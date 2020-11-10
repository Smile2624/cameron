package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.DataStatus;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IDataStatusService extends IBaseDomainObjectService<DataStatus>{
    DataStatus getByProductNo(String productNo);
    List<DataStatus> listByProductNo(String productNo);
}