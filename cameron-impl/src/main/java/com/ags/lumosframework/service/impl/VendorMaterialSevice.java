package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.VendorMaterialEntity;
import com.ags.lumosframework.handler.IVendorMaterialHandler;
import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IVendorMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class VendorMaterialSevice  extends AbstractBaseDomainObjectService<VendorMaterial, VendorMaterialEntity>
                            implements IVendorMaterialService {

    @Autowired
    private IVendorMaterialHandler handler;
    @Override
    protected IBaseEntityHandler<VendorMaterialEntity> getEntityHandler() {
        return handler;
    }
}
