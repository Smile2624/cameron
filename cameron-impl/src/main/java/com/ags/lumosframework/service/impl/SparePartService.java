package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.SparePartEntity;
import com.ags.lumosframework.handler.ISparePartHandler;
import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.ISparePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SparePartService extends AbstractBaseDomainObjectService<SparePart, SparePartEntity> implements ISparePartService {

    @Autowired
    private ISparePartHandler sparePartHandler;

    @Override
    protected IBaseEntityHandler<SparePartEntity> getEntityHandler() {
        return sparePartHandler;
    }


    @Override
    public SparePart getByNoRev(String no, String rev) {
        EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)) {
            filter.fieldEqualTo(SparePartEntity.SPARE_PART_NO, no);
        }
        if (rev != null && !"".equals(rev)) {
            filter.fieldEqualTo(SparePartEntity.SPARE_PART_REV, rev);
        }
        return getByFilter(filter);
    }
    
    public List<SparePart> getByNo(String no){
    	EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)) {
            filter.fieldEqualTo(SparePartEntity.SPARE_PART_NO, no);
        }
    	return listByFilter(filter);
    }
}
