package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.SparePart;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface ISparePartService extends IBaseDomainObjectService<SparePart> {
    SparePart getByNoRev(String no, String rev);
    
    List<SparePart> getByNo(String no);
}
