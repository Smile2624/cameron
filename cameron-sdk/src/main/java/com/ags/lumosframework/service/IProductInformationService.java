package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IProductInformationService extends IBaseDomainObjectService<ProductInformation> {
    ProductInformation getByNoRev(String no, String rev);
}
