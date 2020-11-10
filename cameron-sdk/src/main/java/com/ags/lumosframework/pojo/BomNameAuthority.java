package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.BomNameAuthorityEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class BomNameAuthority extends ObjectBaseImpl<BomNameAuthorityEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1100705646791430985L;

	public BomNameAuthority() {
		super(null);
	}
	
	public BomNameAuthority(BomNameAuthorityEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getAuthorityType() {
        return getInternalObject().getAuthorityType();
    }

    public void setAuthorityType(String authorityType) {
        getInternalObject().setAuthorityType(authorityType);
    }

    public String getNameEigenvalue() {
        return getInternalObject().getNameEigenvalue();
    }

    public void setNameEigenvalue(String nameEigenvalue) {
        getInternalObject().setNameEigenvalue(nameEigenvalue);
    }
}
