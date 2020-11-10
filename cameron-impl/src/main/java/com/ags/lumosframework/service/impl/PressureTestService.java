package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PressureTestEntity;
import com.ags.lumosframework.handler.IPressureTestHandler;
import com.ags.lumosframework.pojo.PressureTest;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPressureTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PressureTestService extends AbstractBaseDomainObjectService<PressureTest, PressureTestEntity> implements IPressureTestService {

    @Autowired
    private IPressureTestHandler pressureTestHandler;

    @Override
    protected IBaseEntityHandler<PressureTestEntity> getEntityHandler() {
        return pressureTestHandler;
    }

    @Override
    public PressureTest getPressureTestByProductSN(String productSN) {
        EntityFilter filter = createFilter();
        if (productSN != null && !"".equals(productSN)) {
            filter.fieldEqualTo(PressureTestEntity.PRODUCT_SN, productSN);
        }
        return getByFilter(filter);
    }

	@Override
	public List<PressureTest> getPressureTestByOrder(String orderNo) {
		EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)){
            filter.fieldEqualTo(PressureTestEntity.WORK_ORDER, orderNo);
        }
        
        return listByFilter(filter);
	}
}
