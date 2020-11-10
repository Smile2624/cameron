package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.DimensionInspectionResultEntity;
import com.ags.lumosframework.handler.IDimensionInspectionHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.DimensionInspectionResult;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IDimensionInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
@Primary
public class DimensionInspectionService extends AbstractBaseDomainObjectService<DimensionInspectionResult, DimensionInspectionResultEntity> 
implements IDimensionInspectionService{

	@Autowired
	private IDimensionInspectionHandler dimensionInspectionHandler;
	
	@Override
	protected IBaseEntityHandler<DimensionInspectionResultEntity> getEntityHandler() {
		return dimensionInspectionHandler;
	}

	@Autowired
	private DBHandler dbHandler;
	@Override
	public String getInspectionDate(String sapInspectionLot) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO, sapInspectionLot);
		filter.orderBy(DimensionInspectionResultEntity.CREATE_TIME, true);
		List<DimensionInspectionResult> list = listByFilter(filter);
		DimensionInspectionResult instance = list.get(0);
		return instance.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}

	@Override
	public void deleteRecords(String sapLotNo) {
		EntityFilter filter = this.createFilter();
		List<Long> listIDs = new ArrayList<>();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO, sapLotNo);
		List<DimensionInspectionResult> list = listByFilter(filter);
		for(DimensionInspectionResult instance : list) {
			listIDs.add(instance.getId());
		}
		deleteByIds(listIDs);
	}

	@Override
	public int inspectionedCount(String sapLotNo) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO, sapLotNo);
		return  countByFilter(filter);
		
	}

	@Override
	public List<DimensionInspectionResult> getBySapLotNo(String sapInspectionLot) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO, sapInspectionLot);
		filter.orderBy(DimensionInspectionResultEntity.MATERIAL_SN, false);
		return listByFilter(filter);
	}

	@Override
	public List<String> getCheckedItems(String sapLotNo) {
		List<String> checkedItemList = new ArrayList<>();
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO, sapLotNo);
		filter.fieldEqualTo(DimensionInspectionResultEntity.DELETED,false);
		List<DimensionInspectionResult> list = listByFilter(filter);
		for(DimensionInspectionResult instance:list) {
			checkedItemList.add(instance.getInspectionName());
		}
		return checkedItemList;
	}
	
	@Override
	public List<DimensionInspectionResult> getByMaterialSn(String materialSn) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.MATERIAL_SN, materialSn);
		return listByFilter(filter);
	}

	@Override
	public List<String> getCheckedSN(String sapLotNo) {
		List<String> snList = new ArrayList<>();
		String sql ="select distinct material_sn from dimension_inspection_result where sap_inspection_no ='"+sapLotNo+"' and deleted = false order by material_sn";
		List<String> list = (List<String>) dbHandler.listBySql(sql, null);
		for(String str:list) {
			snList.add(str.split("-")[2]);
		}
		
		return snList;
	}

	@Override
	public String getInspectionValue(String sapLot, String inspectionName, String materialSN) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO,sapLot);
		filter.fieldEqualTo(DimensionInspectionResultEntity.INSPECTION_NAME,inspectionName);
		filter.fieldEqualTo(DimensionInspectionResultEntity.MATERIAL_SN, materialSN);
		filter.fieldEqualTo(DimensionInspectionResultEntity.DELETED,false);
		DimensionInspectionResult inspectionResult =  getByFilter(filter);
		if(inspectionResult != null) {
			return inspectionResult.getInspectionValue();
		}else {
			return "";
		}
	}

	@Override
	public String getGageNo(String sapLot, String inspectionName) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO,sapLot);
		filter.fieldEqualTo(DimensionInspectionResultEntity.INSPECTION_NAME,inspectionName);
		DimensionInspectionResult inspectionResult =  getByFilter(filter);
		if(inspectionResult != null) {
			return inspectionResult.getGageInfo();
		}else {
			return "";
		}
		
	}

	@Override
	public DimensionInspectionResult getCheckedItem(String sapLot, String materialSn, String inspectionName) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO,sapLot);
		filter.fieldEqualTo(DimensionInspectionResultEntity.INSPECTION_NAME,inspectionName);
		filter.fieldEqualTo(DimensionInspectionResultEntity.MATERIAL_SN,materialSn);
		filter.fieldEqualTo(DimensionInspectionResultEntity.DELETED,false);
		return getByFilter(filter);
	}

	@Override
	public String getInspector(String sapLot, String itemName) {
		// TODO Auto-generated method stub
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(DimensionInspectionResultEntity.SAP_INSPECTION_NO,sapLot);
		filter.fieldEqualTo(DimensionInspectionResultEntity.INSPECTION_NAME,itemName);
		DimensionInspectionResult instance = listByFilter(filter).get(0);
		
		return instance.getCreateUserName();
	}

}
