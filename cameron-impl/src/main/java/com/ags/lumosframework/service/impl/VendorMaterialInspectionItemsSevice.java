package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.VendorMaterialEntity;
import com.ags.lumosframework.entity.VendorMaterialInspectionItemsEntity;
import com.ags.lumosframework.handler.IVendorMaterialHandler;
import com.ags.lumosframework.handler.IVendorMaterialInspectionItemHandler;
import com.ags.lumosframework.pojo.VendorMaterial;
import com.ags.lumosframework.pojo.VendorMaterialInspectionItems;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IVendorMaterialInspectionItemService;
import com.ags.lumosframework.service.IVendorMaterialService;
import org.apache.commons.collections.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
public class VendorMaterialInspectionItemsSevice extends AbstractBaseDomainObjectService<VendorMaterialInspectionItems, VendorMaterialInspectionItemsEntity>
                            implements IVendorMaterialInspectionItemService {

    @Autowired
    private IVendorMaterialInspectionItemHandler handler;
    @Override
    protected IBaseEntityHandler<VendorMaterialInspectionItemsEntity> getEntityHandler() {
        return handler;
    }

    //通过零件的id获取零件的所有检验项的id，用于在删除零件的时候同时删除检验项
    @Override
    public List<Long> listByMaterialId(long id) {
        List<Long> list = new ArrayList<>();
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(VendorMaterialInspectionItemsEntity.MATERIAL_ID, id);
        List<VendorMaterialInspectionItems> vendorMaterialInspectionItems = listByFilter(filter);
        for(VendorMaterialInspectionItems item : vendorMaterialInspectionItems){
            list.add(item.getId());
        }
        return list;
    }

    //过零件的id获取零件的所有检验项，用于页面展示结果
    @Override
    public List<VendorMaterialInspectionItems> listById(long id) {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(VendorMaterialInspectionItemsEntity.MATERIAL_ID, id);
        return listByFilter(filter);
    }


}
