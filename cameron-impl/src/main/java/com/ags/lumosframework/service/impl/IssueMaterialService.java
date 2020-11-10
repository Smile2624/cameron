package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.entity.IssueMaterialListEntity;
import com.ags.lumosframework.entity.IssueMaterilWithRoutingInfo;
import com.ags.lumosframework.handler.IAssemblingHandler;
import com.ags.lumosframework.handler.IIssueMaterialHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.IssueMaterialList;
import com.ags.lumosframework.pojo.ProductRouting;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.sdk.handler.api.IDBHandler;
import com.ags.lumosframework.service.IAssemblingService;
import com.ags.lumosframework.service.IIssueMaterialService;
import com.ags.lumosframework.service.IProductRoutingService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class IssueMaterialService extends AbstractBaseDomainObjectService<IssueMaterialList, IssueMaterialListEntity> implements IIssueMaterialService {


    @Autowired
    IIssueMaterialHandler issueMaterialHandler;

    @Override
    protected IBaseEntityHandler<IssueMaterialListEntity> getEntityHandler() {
        return issueMaterialHandler;
    }

    @Autowired
    IProductionOrderService productionOrderService;

    @Autowired
    IProductRoutingService productRoutingService;


    @Override
    public List<IssueMaterialList> getAll() {
        EntityFilter ef = createFilter();
        return listByFilter(ef);
    }

    @Override
    public List<IssueMaterialList> getAllNot(String orderNo) {
        EntityFilter ef = createFilter();
        if (!Strings.isNullOrEmpty(orderNo)) {
            ef.fieldEqualTo(IssueMaterialListEntity.ORDER_NO, orderNo);
        }
        ef.fieldEqualTo(IssueMaterialListEntity.STATUS, "N");
        return listByFilter(ef);
    }

    @Override
    public List<IssueMaterialList> listByOrderNo(String orderNo) {
        EntityFilter ef = createFilter();
        if (!Strings.isNullOrEmpty(orderNo)) {
            ef.fieldEqualTo(IssueMaterialListEntity.ORDER_NO, orderNo);
        }
        return listByFilter(ef);
    }

    /**
     * 获取自动导入的发料信息和工单对应的routing的发料步骤的remark信息
     * 通过工单获取产品信息，再有产品信息获取routing信息
     *
     * @return
     */
    @Override
    public List<IssueMaterilWithRoutingInfo> getMainInfo(String orderno) {
        //1.获取发料工单的工单号和状态
        List<IssueMaterilWithRoutingInfo> result = new ArrayList<>();
        String sql = "select distinct order_no ,status from ISSUE_MATERIAL_LIST where status ='N'";
        if (!Strings.isNullOrEmpty(orderno)) {
            sql += " and order_no='" + orderno + "'";
        }
        List<?> objects = BeanManager.getService(IDBHandler.class).listBySql(sql, null);
        if (objects != null && objects.size() > 0) {
            for (Object obj : objects) {
                IssueMaterilWithRoutingInfo instance = new IssueMaterilWithRoutingInfo();
                Object[] fields = (Object[]) obj;
                String orderNo = (String) fields[0];
                String status = fields[1] != null ? (String) fields[1] : "N";
                //通过orderNo获取工单对象
                ProductionOrder order = productionOrderService.getByNo(orderNo);
                instance.setOrderNo(orderNo);
                if (order == null) {
                    instance.setRemark("工单未创建");
                } else {
                    Object[] objects1 = productRoutingService.getPullMaterialStep(order.getRoutingGroup(), order.getInnerGroupNo(), "");
                    if (!(boolean) objects1[0]) {
                        instance.setRemark("工单产品对应的Routing未创建");
                    } else {
                        if (objects1[1] == null) {
                            //说明不需要发料操作
                            instance.setRemark("工单产品对应的Routing不需要发料操作");
                            continue;
                        } else {
                            instance.setRemark(((ProductRouting) objects1[1]).getAttention());
                        }
                    }
                }
                instance.setStatus(status);
                result.add(instance);
            }
        }
        return result;
    }

    @Override
    public IssueMaterialList getByOrderNoAndMatNo(String orderNo, String MatNo) {
        EntityFilter filter = createFilter();
        if (!Strings.isNullOrEmpty(orderNo)) {
            filter.fieldEqualTo(IssueMaterialListEntity.ORDER_NO, orderNo);
        }
        if (!Strings.isNullOrEmpty(MatNo)) {
            filter.fieldEqualTo(IssueMaterialListEntity.MATERIAL_NO, MatNo);
        }
        return getByFilter(filter);
    }


}
