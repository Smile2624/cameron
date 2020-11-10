package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.DimensionInspectionResult;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IDimensionInspectionService extends IBaseDomainObjectService<DimensionInspectionResult>{

	String getInspectionDate(String sapInspectionLot);
	void deleteRecords(String sapLotNo);
	
	int inspectionedCount(String sapLotNo);
	
	List<DimensionInspectionResult> getBySapLotNo(String sapInspectionLot);
	/**
	 * 
	 * @param sapLotNo
	 * @return
	 * 在多人检验的时候，用来获取已经检验过的检验项
	 */
	List<String> getCheckedItems(String sapLotNo);
	
	List<DimensionInspectionResult> getByMaterialSn(String materialSn);
	
	List<String> getCheckedSN(String sapLotNo);
	
	String getInspectionValue(String sapLot,String inspectionName,String materialSN);
	
	String getGageNo(String sapLot,String inspectionName);
	
	DimensionInspectionResult getCheckedItem(String sapLot,String materialSn,String inspectionName);
	
	String getInspector(String sapLot,String itemName);
}
