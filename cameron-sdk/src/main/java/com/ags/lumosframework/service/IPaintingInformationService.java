package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PaintingInformation;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IPaintingInformationService extends IBaseDomainObjectService<PaintingInformation>{
	/*
	 * 获取所有工单中，已经做过喷漆信息录入但是qc还没有做过检验的工单信息
	 * 当OrderSn传入空字符表示获取所有，传入特定值时表示获取指定工单信息
	 * **/
	List<PaintingInformation>  getBySn(String workOrderSN);
	/**
	 * 获取已经完成检验的喷漆用户输出喷漆报告
	 * */
	PaintingInformation getByOrderNo(String orderNo);
	
	/**
	 * 录入喷漆信息页面，用来获取未录入信息的工单
	 * */
	List<String> getAllButPaintOrder();
}
