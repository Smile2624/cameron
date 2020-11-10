package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PaintingInspection;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IPaintingInspectionService extends IBaseDomainObjectService<PaintingInspection> {
	PaintingInspection getByNo(String orderNo);
}
