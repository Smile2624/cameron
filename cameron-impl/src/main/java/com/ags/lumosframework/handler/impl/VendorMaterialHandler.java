package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.VendorMaterialDao;
import com.ags.lumosframework.entity.VendorMaterialEntity;
import com.ags.lumosframework.handler.IVendorMaterialHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class VendorMaterialHandler extends
        AbstractBaseEntityHandler<VendorMaterialEntity> implements IVendorMaterialHandler {

    @Autowired
    private VendorMaterialDao dao;
    @Override
    protected BaseEntityDao<VendorMaterialEntity> getDao() {
        return dao;
    }
}
