package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.FinalInspectionResult;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IFinalInspectionResultService extends IBaseDomainObjectService<FinalInspectionResult> {
	
	List<FinalInspectionResult> getFinalInspectionResultByOrderNo(String orderNo);
	
	List<FinalInspectionResult> getFinalInspectionResultByOrderNo(String orderNo , String type);


	FinalInspectionResult getByOrderNo(String orderNo);

	List<FinalInspectionResult> getByReportNo(String prefix);
}
