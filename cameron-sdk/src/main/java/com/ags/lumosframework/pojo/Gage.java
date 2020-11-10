package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.GageEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.time.ZonedDateTime;

public class Gage extends ObjectBaseImpl<GageEntity> {

    private static final long serialVersionUID = -1074467880399655870L;

    public Gage() {
        super(null);
    }

    public Gage(GageEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getGageName() {
        return getInternalObject().getGageName();
    }

    public void setGageName(String gageName) {
        getInternalObject().setGageName(gageName);
    }

    public String getGageNo() {
        return getInternalObject().getGageNo();
    }

    public void setGageNo(String gageNo) {
        getInternalObject().setGageNo(gageNo);
    }

    public ZonedDateTime getActiveDate() {
        return getInternalObject().getActiveDate();
    }

    public void setActiveDate(ZonedDateTime activeDate) {
        getInternalObject().setActiveDate(activeDate);
    }
    
    public String getStatus() {
    	return this.getInternalObject().getStatus();
    }
    
    public void setStatus(String status) {
    	this.getInternalObject().setStatus(status);
    }
    
}
