package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.AppearanceInstrumentationResult;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IAppearanceInstrumentationResultService extends IBaseDomainObjectService<AppearanceInstrumentationResult> {
	List<AppearanceInstrumentationResult> getByNo(String orderNo);
	AppearanceInstrumentationResult getBySN(String sn);//Changed by Cameron: 根据序列号获取外观检验结果
}
