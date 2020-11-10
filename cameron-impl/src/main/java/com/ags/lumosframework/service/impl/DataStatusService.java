package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.DataStatusEntity;
import com.ags.lumosframework.handler.IDataStatusHandler;
import com.ags.lumosframework.pojo.DataStatus;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IDataStatusService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DataStatusService extends AbstractBaseDomainObjectService<DataStatus, DataStatusEntity> implements IDataStatusService{
    @Autowired
    private IDataStatusHandler handler;
    @Override
    protected IBaseEntityHandler<DataStatusEntity> getEntityHandler() {
        return handler;
    }

    @Override
    public DataStatus getByProductNo(String ProductNo) {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(DataStatusEntity.PRODUCT_NO,ProductNo);
        return getByFilter(filter);
    }

    @Override
    public List<DataStatus> listByProductNo(String ProductNo) {
        EntityFilter filter = createFilter();
        if(!Strings.isNullOrEmpty(ProductNo)){
//            filter.fieldEqualTo(OperationInstructionEntity.INSTRUCTION_NAME,name);
            filter.fieldContains(DataStatusEntity.PRODUCT_NO,ProductNo);
        }
        return listByFilter(filter);
    }
}
