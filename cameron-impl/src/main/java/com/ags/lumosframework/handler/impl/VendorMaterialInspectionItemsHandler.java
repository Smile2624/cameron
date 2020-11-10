package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.VendorMaterialDao;
import com.ags.lumosframework.dao.VendorMaterialInspectionItemsDao;
import com.ags.lumosframework.entity.VendorMaterialEntity;
import com.ags.lumosframework.entity.VendorMaterialInspectionItemsEntity;
import com.ags.lumosframework.handler.IVendorMaterialHandler;
import com.ags.lumosframework.handler.IVendorMaterialInspectionItemHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class VendorMaterialInspectionItemsHandler extends
        AbstractBaseEntityHandler<VendorMaterialInspectionItemsEntity> implements IVendorMaterialInspectionItemHandler {

    @Autowired
    private VendorMaterialInspectionItemsDao dao;
    @Override
    protected BaseEntityDao<VendorMaterialInspectionItemsEntity> getDao() {
        return dao;
    }
}
