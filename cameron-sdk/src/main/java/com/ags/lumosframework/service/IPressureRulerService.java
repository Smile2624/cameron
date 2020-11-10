package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PressureRuler;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IPressureRulerService extends IBaseDomainObjectService<PressureRuler> {

    List<PressureRuler> getAllByProductNo(String productNo);

    List<PressureRuler> getByRulerNo(String rulerNo);

    List<PressureRuler> getByProductNo(String productNo);

    PressureRuler getByProductNoAndPressureType(String rulerNo, String suffixNo);
}
