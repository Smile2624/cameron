package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.VisualInspection;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IVisualInspectionService extends IBaseDomainObjectService<VisualInspection>{

    VisualInspection getBySapLotNo(String sapLotNo);
}
