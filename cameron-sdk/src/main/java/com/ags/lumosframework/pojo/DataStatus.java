package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.DataStatusEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class DataStatus extends ObjectBaseImpl<DataStatusEntity> {
    public DataStatus(DataStatusEntity entity) {
        super(entity);
    }

    public DataStatus() {
        super(null);
    }
    @Override
    public String getName() {
        return null;
    }

    public String getProductNo(){return this.getInternalObject().getProductNo();}

    public void setProductNo(String productNo){this.getInternalObject().setProductNo(productNo);}

    public boolean isBomChecked(){return this.getInternalObject().isBomChecked();}
    public void setBomChecked(boolean isBomChecked){this.getInternalObject().setBomChecked(isBomChecked);}

    public boolean isRtgChecked(){return this.getInternalObject().isRtgChecked();}
    public void setRtgChecked(boolean isRtgChecked){this.getInternalObject().setRtgChecked(isRtgChecked);}

    public boolean isAllChecked(){return this.getInternalObject().isAllChecked();}
    public void setAllChecked(boolean isAllChecked){this.getInternalObject().setAllChecked(isAllChecked);}
}
