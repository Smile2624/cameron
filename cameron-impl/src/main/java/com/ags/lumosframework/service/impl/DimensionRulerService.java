package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.DimensionRulerEntity;
import com.ags.lumosframework.handler.IDimensionRulerHandler;
import com.ags.lumosframework.pojo.DimensionRuler;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IDimensionRulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DimensionRulerService extends AbstractBaseDomainObjectService<DimensionRuler,DimensionRulerEntity> implements IDimensionRulerService {

	@Autowired
	private IDimensionRulerHandler dimensionRulerHandler;
	
	@Override
	protected IBaseEntityHandler<DimensionRulerEntity> getEntityHandler() {
		return dimensionRulerHandler;
	}

	@Override
	public List<DimensionRuler> getByNoRev(String materialNo, String materialRev) {
		EntityFilter filter = createFilter();
        if (materialNo != null && !"".equals(materialNo)){
            filter.fieldContains(DimensionRulerEntity.MATRRIAL_NO, materialNo);
        }
        if (materialRev != null && !"".equals(materialRev)){
            filter.fieldContains(DimensionRulerEntity.MATERIAL_REV, materialRev);
        }
        
        return listByFilter(filter);
	}

	@Override
	public DimensionRuler getByNoRevItemName(String materialNo, String materialRev, String inspectionItemName) {
		EntityFilter filter = createFilter();
		if(materialNo!=null && !materialNo.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.MATRRIAL_NO, materialNo);
		}
		if(materialRev!=null && !materialRev.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.MATERIAL_REV, materialRev);
		}
		if(inspectionItemName!=null && !inspectionItemName.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.INSPECTION_ITEM_NAME, inspectionItemName);
		}
		return getByFilter(filter);
	}

	@Override
	public void deleteRulerList(String materialNo, String materialRev) {
		EntityFilter filter = createFilter();
		if(materialNo!=null && !materialNo.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.MATRRIAL_NO, materialNo);
		}
		if(materialRev!=null && !materialRev.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.MATERIAL_REV, materialRev);
		}
		List<DimensionRuler> list = listByFilter(filter);

		if(list != null && list.size() > 0){
			int count = list.size();
			for(int i = 0 ; i < count; i ++){
				delete(list.get(i));
			}
		}
	}
	//*********************Eric new delete line item**************************************
	@Override
	public void deleteRulerListdetail(String materialNo,String inspectionItemName) {
		EntityFilter filter = createFilter();
		if(materialNo!=null && !materialNo.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.MATRRIAL_NO, materialNo);
		}
		if(inspectionItemName!=null && !inspectionItemName.equals("")) {
			filter.fieldEqualTo(DimensionRulerEntity.INSPECTION_ITEM_NAME, inspectionItemName);
		}

		List<DimensionRuler> list = listByFilter(filter);

		if(list != null && list.size() > 0){
			int count = list.size();
			for(int i = 0 ; i < count; i ++){
				delete(list.get(i));
			}
		}
	}

}
