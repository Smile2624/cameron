package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.HardnessEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class Hardness extends ObjectBaseImpl<HardnessEntity> {

    private static final long serialVersionUID = 8403878093279895445L;

    public Hardness() {
        super(null);
    }

    public Hardness(HardnessEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getHardnessName() {
        return getInternalObject().getHardnessName();
    }

    public void setHardnessName(String hardnessName) {
        getInternalObject().setHardnessName(hardnessName);
    }

    public String getHardnessStand() {
        return getInternalObject().getHardnessStand();
    }

    public void setHardnessStand(String hardnessStand) {
        getInternalObject().setHardnessStand(hardnessStand);
    }

    public float getHardnessUpLimit() {
        return getInternalObject().getHardnessUpLimit();
    }

    public void setHardnessUpLimit(float hardnessUpLimit) {
        getInternalObject().setHardnessUpLimit(hardnessUpLimit);
    }

    public float getHardnessDownLimit() {
        return getInternalObject().getHardnessDownLimit();
    }

    public void setHardnessDownLimit(float hardnessDownLimit) {
        getInternalObject().setHardnessDownLimit(hardnessDownLimit);
    }
}
