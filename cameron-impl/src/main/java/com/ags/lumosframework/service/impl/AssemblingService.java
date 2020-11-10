package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.handler.IAssemblingHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IAssemblingService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class AssemblingService  extends AbstractBaseDomainObjectService<Assembling, AssemblingEntity> implements IAssemblingService {

    @Autowired
    private IAssemblingHandler assemblingHandler;
    
	@Autowired
	private DBHandler dbHandler;

    @Override
    protected IBaseEntityHandler<AssemblingEntity> getEntityHandler() {
        return assemblingHandler;
    }

    @Override
    public List<Assembling>  getBySn(String sn) {
        EntityFilter filter = createFilter();
        if (sn != null && !"".equals(sn)) {
            filter.fieldEqualTo(AssemblingEntity.SN_BATCH, sn);
        }
        return listByFilter(filter);
    }

    @Override
    public List<Assembling> getBySnType(String sn, String type) {
        EntityFilter filter = createFilter();
        if (sn != null && !"".equals(sn)) {
            filter.fieldEqualTo(AssemblingEntity.SN_BATCH, sn);
        }
        if (type != null && !"".equals(type)) {
            filter.fieldEqualTo(AssemblingEntity.RETROSPECT_TYPE, type);
        }
        return listByFilter(filter);
    }

	@Override
	public List<Assembling> getByOrderNo(String orderNo) {
		EntityFilter filter = createFilter();
		if(!Strings.isNullOrEmpty(orderNo)) {
			filter.fieldEqualTo(AssemblingEntity.ORDER_NO, orderNo);
		}
		filter.orderBy(AssemblingEntity.SN_BATCH, false);
		return listByFilter(filter);
	}

    @Override
    public Assembling getBySnAndPartNo(String snBatch, String partNo, String rowIndex) {
        EntityFilter filter = createFilter();
        if (snBatch != null && !"".equals(snBatch)) {
            filter.fieldEqualTo(AssemblingEntity.SN_BATCH, snBatch);
        }
        if (partNo != null && !"".equals(partNo)) {
            filter.fieldEqualTo(AssemblingEntity.PART_NO, partNo);
        }
        if (rowIndex != null && !"".equals(rowIndex)){
            filter.fieldEqualTo(AssemblingEntity.DESCRIPTION, rowIndex);
        }
        return getByFilter(filter);
    }

    @Override
	public List<String> getOrderSNList(String orderNo) {
		String sql ="SELECT DISTINCT SN_BATCH FROM ASSEMBLING WHERE ORDER_NO='"+orderNo+"' ORDER BY SN_BATCH";
		return (List<String>) dbHandler.listBySql(sql, null);
		
	}
}
