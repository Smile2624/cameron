package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.HardnessTestReport;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IHardnessTestReportService extends IBaseDomainObjectService<HardnessTestReport> {
    HardnessTestReport getByNoBatchSubitem(String purchasingOrder, String sapInspectionLot, String purchasingItemNo);

    String getInspectionTime(String sapLotNo);

    List<HardnessTestReport> getByPurchasingOrder(String purchasingOrder);

    void deleteBysapLot(String sapLotNo);
}
