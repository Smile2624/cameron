package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PurchasingOrderInfoEntity;
import com.ags.lumosframework.handler.IPurchasingOrderHandler;
import com.ags.lumosframework.pojo.PurchasingOrderInfo;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPurchasingOrderService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class PurchasingOrderService extends AbstractBaseDomainObjectService<PurchasingOrderInfo, PurchasingOrderInfoEntity>
        implements IPurchasingOrderService {

    @Autowired
    private IPurchasingOrderHandler purchasingOrderHandler;

    @Override
    protected IBaseEntityHandler<PurchasingOrderInfoEntity> getEntityHandler() {
        return purchasingOrderHandler;
    }

    @Override
    public PurchasingOrderInfo getBySapInspectionLot(String SapInspectionLot) {
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(SapInspectionLot)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.SAP_INSPECTION_LOT, SapInspectionLot);
        }
        return this.getByFilter(filter);
    }

    @Override
    public List<PurchasingOrderInfo> getByPurchasingNo(String purchasingNo) {
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.PURCHASING_NO, purchasingNo);
        }
        filter.orderBy(PurchasingOrderInfoEntity.PURCHASING_NO, false);
        return this.listByFilter(filter);
    }

    @Override
    public List<PurchasingOrderInfo> getUncheckedOrder(String purchasingNo, String type) {
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.PURCHASING_NO, purchasingNo);
        }
        if ("DIMENSION".equals(type)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, false);
        } else if ("HARDNESS".equals(type)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, false);
        } else if ("VISUAL".equals(type)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.VISUAL_CHECKED, false);
        } else {
//			filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, false);
//			filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, false);
        }

        filter.orderBy(PurchasingOrderInfoEntity.PURCHASING_NO, false);
        return this.listByFilter(filter);
    }

    @Override
    public List<PurchasingOrderInfo> getInspectionedOrder(String orderNo) {
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(orderNo)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.PURCHASING_NO, orderNo);
        }
        filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, true);
        filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, true);
        return this.listByFilter(filter);
    }

    @Override
    public List<PurchasingOrderInfo> getCheckedOrder(String purchasingNo, String type) {
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(purchasingNo)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.PURCHASING_NO, purchasingNo);
        }
        if ("DIMENSION".equals(type)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, true);
        } else if ("HARDNESS".equals(type)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, true);
        } else {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, true);
            filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, true);
        }

        filter.orderBy(PurchasingOrderInfoEntity.PURCHASING_NO, false);
        return this.listByFilter(filter);
    }

    @Override
    public List<String> getPurchasingNo(String type) {
        List<String> list = new ArrayList<String>();
        EntityFilter filter = this.createFilter();
        if (type.equals("HARDNESS")) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, false);
        } else if (type.equals("DIMENSION")) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, false);
        } else if (type.equals("VISUAL")) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.VISUAL_CHECKED, false);
        } else {
//			filter.fieldEqualTo(PurchasingOrderInfoEntity.DIMENSION_CHECKED, true);
//			filter.fieldEqualTo(PurchasingOrderInfoEntity.HARDNESS_CHECKED, true);
        }
        List<PurchasingOrderInfo> result = listByFilter(filter);
        if (result != null && result.size() > 0) {
            for (PurchasingOrderInfo instance : result) {
                if (!list.contains(instance.getPurchasingNo())) {
                    list.add(instance.getPurchasingNo());
                }
            }
        }
        return list;
    }

    @Override
    public void deleteOrderList(String orderNo) {
        List<PurchasingOrderInfo> list = new ArrayList<PurchasingOrderInfo>();
        EntityFilter filter = this.createFilter();
        if (!Strings.isNullOrEmpty(orderNo)) {
            filter.fieldEqualTo(PurchasingOrderInfoEntity.PURCHASING_NO, orderNo);
        }
        list = listByFilter(filter);
        if (list.size() > 0) {
            int count = list.size();
            for (int i = 0; i < count; i++) {
                delete(list.get(i));
            }
        }
    }

}
