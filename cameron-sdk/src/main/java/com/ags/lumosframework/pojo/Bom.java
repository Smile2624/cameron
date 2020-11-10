package com.ags.lumosframework.pojo;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.BomEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.service.ISparePartService;

public class Bom extends ObjectBaseImpl<BomEntity> {
    private static final long serialVersionUID = 1402704567059847422L;

    public Bom() {
        super(null);
    }

    public Bom(BomEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getProductNo() {
        return getInternalObject().getProductNo();
    }

    public void setProductNo(String productNo) {
        getInternalObject().setProductNo(productNo);
    }

    public String getProductRev() {
        return getInternalObject().getProductRev();
    }

    public void setProductRev(String productRev) {
        getInternalObject().setProductRev(productRev);
    }

    public String getPartNo() {
        return getInternalObject().getPartNo();
    }

    public void setPartNo(String partNo) {
        getInternalObject().setPartNo(partNo);
    }

    public String getPartRev() {
        return getInternalObject().getPartRev();
    }

    public void setPartRev(String partRev) {
        getInternalObject().setPartRev(partRev);
    }

    public String getPartDes() {
        return getInternalObject().getPartDes();
    }

    public void setPartDes(String partDes) {
        getInternalObject().setPartDes(partDes);
    }

    public int getPartQuantity() {
        return getInternalObject().getPartQuantity();
    }

    public void setPartQuantity(int partQuantity) {
        getInternalObject().setPartQuantity(partQuantity);
    }

    public boolean isRetrospect() {
        return getInternalObject().isRetrospect();
    }

    public void setRetrospect(boolean retrospect) {
        getInternalObject().setRetrospect(retrospect);
    }

    public String getRetrospectType() {
        return getInternalObject().getRetrospectType();
    }

    public void setRetrospectType(String retrospectType) {
        getInternalObject().setRetrospectType(retrospectType);
    }

    public String getItemNo() {
        return getInternalObject().getItemNo();
    }

    public void setItemNo(String itemNo) {
        getInternalObject().setItemNo(itemNo);
    }

    public String getLongText() {
        return getInternalObject().getLongText();
    }

    public void setLongText(String longText) {
        getInternalObject().setLongText(longText);
    }

    public String getQaPlan() {
        return getInternalObject().getQaPaln();
    }

    public void setQaPlan(String qaPlan) {
        getInternalObject().setQaPaln(qaPlan);
    }

    public String getQaPlanRev() {
        return getInternalObject().getQaPalnRev();
    }

    public void setQaPlanRev(String qaPlanRev) {
        getInternalObject().setQaPalnRev(qaPlanRev);
    }

    public String getDrawNo() {
        return getInternalObject().getDrawNo();
    }

    public void setDrawNo(String drawNo) {
        getInternalObject().setDrawNo(drawNo);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPressureTest() {
        return getInternalObject().getPressureTest();
    }

    public void setPressureTest(String pressureTest) {
        getInternalObject().setPressureTest(pressureTest);
    }

    public String getPressureTestRev() {
        return getInternalObject().getPressureTestRev();
    }

    public void setPressureTestRev(String pressureTestRev) {
        getInternalObject().setPressureTestRev(pressureTestRev);
    }

    public String getItemCategory() {
        return this.getInternalObject().getItemCategory();
    }

    public void setItemCategory(String itemCategory) {
        this.getInternalObject().setItemCategory(itemCategory);
    }

    public String getExplosionLevel() {
        return this.getInternalObject().getExplosionLevel();
    }

    public void setExplosionLevel(String explosionLevel) {
        this.getInternalObject().setExplosionLevel(explosionLevel);
    }

    public String getSortString() {
        return this.getInternalObject().getSortString();
    }

    public void setSortString(String sortString) {
        this.getInternalObject().setSortString(sortString);
    }

    public String getLegacyRev() {
        return this.getInternalObject().getLegacyRev();
    }

    public void setLegacyRev(String legacyRev) {
        this.getInternalObject().setLegacyRev(legacyRev);
    }

    public String getBaseUnit() {
        return this.getInternalObject().getBaseUnit();
    }

    public void setBaseUnit(String baseUnit) {
        this.getInternalObject().setBaseUnit(baseUnit);
    }

    public String getPhantomItem() {
        return this.getInternalObject().getPhantomItem();
    }

    public void setPhantomItem(String phantomItem) {
        this.getInternalObject().setPhantomItem(phantomItem);
    }


    //根据零件号+版本号 获取零件信息
    public SparePart getSparePart() {
        ISparePartService partService = BeanManager.getService(ISparePartService.class);

        return partService.getByNoRev(getPartNo(), getPartRev());
    }
}
