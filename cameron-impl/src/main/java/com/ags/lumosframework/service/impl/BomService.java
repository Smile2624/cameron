package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.BomEntity;
import com.ags.lumosframework.handler.IBomHandler;
import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IBomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class BomService extends AbstractBaseDomainObjectService<Bom, BomEntity> implements IBomService {

    @Autowired
    private IBomHandler bomHandler;

    @Override
    protected IBaseEntityHandler<BomEntity> getEntityHandler() {
        return bomHandler;
    }

    @Override
    public List<Bom> getBomsByNoRev(String no, String rev) {
        EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)){
            filter.fieldEqualTo(BomEntity.PRODUCT_NO, no);
        }
        if (rev != null && !"".equals(rev)){
            filter.fieldEqualTo(BomEntity.PRODUCT_REV, rev);
        }
//        filter.fieldNotEqualTo(BomEntity.ITEM_NO, "0000");
        filter.orderBy(BomEntity.PRODUCT_NO,true);
        filter.orderBy(BomEntity.PRODUCT_REV,true);
        return listByFilter(filter);
    }

    @Override
    public List<Bom> getBomsByNoRevType(String no, String rev, String type) {
        EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)){
            filter.fieldEqualTo(BomEntity.PRODUCT_NO, no);
        }
        if (type != null && !"".equals(type)){
            filter.fieldEqualTo(BomEntity.RETROSPECT_TYPE, type);
        }
        if (rev != null && !"".equals(rev)){
            filter.fieldEqualTo(BomEntity.PRODUCT_REV, rev);
        }
        filter.fieldNotEqualTo(BomEntity.ITEM_NO, "0000");
        return listByFilter(filter);
    }

	@Override
	public List<Bom> getByMaterialNo(String partNo) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(BomEntity.PART_NO, partNo);
		return listByFilter(filter);
	}

	@Override
	public Bom getByNoRevItemPartNoPartRev(String bomNo, String bomRev, String itemNo, String partNo, String partRev) {
		EntityFilter filter = createFilter();
		if (bomNo != null && !"".equals(bomNo)){
            filter.fieldEqualTo(BomEntity.PRODUCT_NO, bomNo);
        }
        if (bomRev != null && !"".equals(bomRev)){
            filter.fieldEqualTo(BomEntity.PRODUCT_REV, bomRev);
        }
        if (itemNo != null && !"".equals(itemNo)){
            filter.fieldEqualTo(BomEntity.ITEM_NO, itemNo);
        }
        if (partNo != null && !"".equals(partNo)){
            filter.fieldEqualTo(BomEntity.PART_NO, partNo);
        }
        if (partRev != null && !"".equals(partRev)){
            filter.fieldEqualTo(BomEntity.PART_REV, partRev);
        }
		return getByFilter(filter);
	}

    @Override
    public void deleteBomList(String bom, String rev) {
        EntityFilter filter = createFilter();
            filter.fieldEqualTo(BomEntity.PRODUCT_NO, bom);
            filter.fieldEqualTo(BomEntity.PRODUCT_REV, rev);
            List<Bom> list = listByFilter(filter);
            if(list !=null && list.size() > 0){
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    delete(list.get(i));
                }
            }
    }

}
