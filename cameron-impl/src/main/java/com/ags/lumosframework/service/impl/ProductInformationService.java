package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.ProductInformationEntity;
import com.ags.lumosframework.handler.IProductInformationHandler;
import com.ags.lumosframework.pojo.ProductInformation;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IProductInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ProductInformationService extends AbstractBaseDomainObjectService<ProductInformation, ProductInformationEntity> implements IProductInformationService {

    @Autowired
    private IProductInformationHandler productInformationHandler;

    @Override
    protected IBaseEntityHandler<ProductInformationEntity> getEntityHandler() {
        return productInformationHandler;
    }

    @Override
    public ProductInformation getByNoRev(String no, String rev) {
        EntityFilter filter = createFilter();
        if (no != null && !"".equals(no)) {
            filter.fieldEqualTo(ProductInformationEntity.PRODUCT_ID, no);
        }
        if (rev != null && !"".equals(rev)) {
            filter.fieldEqualTo(ProductInformationEntity.PRODUCT_VERSION_ID, rev);
        }
        return getByFilter(filter);
    }

}
