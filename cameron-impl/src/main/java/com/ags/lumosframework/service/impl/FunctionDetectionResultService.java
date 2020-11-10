package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.AppearanceInstrumentationResultEntity;
import com.ags.lumosframework.entity.FunctionDetectionResultEntity;
import com.ags.lumosframework.handler.IFunctionDetectionResultHandler;
import com.ags.lumosframework.pojo.FunctionDetectionResult;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IFunctionDetectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class FunctionDetectionResultService extends AbstractBaseDomainObjectService<FunctionDetectionResult,FunctionDetectionResultEntity> implements IFunctionDetectionResultService {
	
	@Autowired
	private IFunctionDetectionResultHandler functionDetectionResultHandler;

	@Override
	protected IBaseEntityHandler<FunctionDetectionResultEntity> getEntityHandler() {
		return functionDetectionResultHandler;
	}
	
	@Override
	public List<FunctionDetectionResult> getByNo(String orderNo){
		EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)){
            filter.fieldEqualTo(AppearanceInstrumentationResultEntity.ORDER_NO, orderNo);
        }
        return listByFilter(filter);
	}
}
