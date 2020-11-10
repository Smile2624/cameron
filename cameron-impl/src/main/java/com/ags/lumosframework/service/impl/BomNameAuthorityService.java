package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.BomNameAuthorityEntity;
import com.ags.lumosframework.handler.IBomNameAuthorityHandler;
import com.ags.lumosframework.pojo.BomNameAuthority;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IBomNameAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class BomNameAuthorityService extends AbstractBaseDomainObjectService<BomNameAuthority,BomNameAuthorityEntity> implements IBomNameAuthorityService {
	
	
	@Autowired
	private IBomNameAuthorityHandler bomNameAuthorityHandler;

	@Override
	protected IBaseEntityHandler<BomNameAuthorityEntity> getEntityHandler() {
		return bomNameAuthorityHandler;
	}

	@Override
	public BomNameAuthority getByTypeValue(String authorityType, String nameEigenvalue) {
		EntityFilter filter = createFilter();
        if (authorityType != null && !"".equals(authorityType)) {
            filter.fieldEqualTo(BomNameAuthorityEntity.AUTHORITY_TYPE, authorityType);
        }
        if (nameEigenvalue != null && !"".equals(nameEigenvalue)) {
            filter.fieldEqualTo(BomNameAuthorityEntity.NAME_EIGENVALUE, nameEigenvalue);
        }
        return getByFilter(filter);
	}

	@Override
	public List<BomNameAuthority> getByTypeValue(String authorityType, String nameEigenvalue, long id) {
		EntityFilter filter = createFilter();
        if (authorityType != null && !"".equals(authorityType)) {
            filter.fieldEqualTo(BomNameAuthorityEntity.AUTHORITY_TYPE, authorityType);
        }
        if (nameEigenvalue != null && !"".equals(nameEigenvalue)) {
            filter.fieldEqualTo(BomNameAuthorityEntity.NAME_EIGENVALUE, nameEigenvalue);
        }
        filter.notEquals(BomNameAuthorityEntity.ID, true, id);
        return listByFilter(filter);
	}
	
	
}
