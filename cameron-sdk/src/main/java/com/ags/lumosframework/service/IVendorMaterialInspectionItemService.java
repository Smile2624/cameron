package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.pojo.VendorMaterialInspectionItems;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IVendorMaterialInspectionItemService extends IBaseDomainObjectService<VendorMaterialInspectionItems> {

    List<Long> listByMaterialId(long id);

    List<VendorMaterialInspectionItems> listById(long id);
}
