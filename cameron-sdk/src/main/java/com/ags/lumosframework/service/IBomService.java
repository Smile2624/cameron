package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.Bom;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IBomService extends IBaseDomainObjectService<Bom> {

    List<Bom> getBomsByNoRev(String no, String rev);

    List<Bom> getBomsByNoRevType(String no, String rev, String type);
    
    List<Bom> getByMaterialNo(String partNo);
    
    Bom getByNoRevItemPartNoPartRev(String bomNo, String bomRev, String itemNo, String partNo,String partRev);

    void deleteBomList(String bom,String rev);
}
