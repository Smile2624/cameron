package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.FunctionDetectionResult;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IFunctionDetectionResultService extends IBaseDomainObjectService<FunctionDetectionResult> {
	List<FunctionDetectionResult> getByNo(String orderNo);
}
