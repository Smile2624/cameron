package com.ags.lumosframework.enums;

public enum PressureTypeEnum {

    OPEN_BODY_FM("OPEN_BODY_FM", "阀门-本体"),
    DOWNSTREAM_FM("DOWNSTREAM_FM", "阀门-下游"),
    UPSTREAM_FM("UPSTREAM_FM", "阀门-上游"),
    OPEN_BODY_JK("OPEN_BODY_JK", "井口-本体"),
    CROSS_OVER_BODY_TOP_JK("CROSS_OVER_BODY_TOP_JK", "井口-转化接头本体TOP"),
    CROSS_OVER_BODY_BTM_JK("CROSS_OVER_BODY_BTM_JK", "井口-转化接头本体BTM"),
    CHRISTMAS_TREE_JK("CHRISTMAS_TREE_JK", "井口-采油树"),
    WELLHEAD_JK("WELLHEAD_JK", "井口-井口头组件"),
    CROSS_OVER_ASSEMBLY_TOP_JK("CROSS_OVER_ASSEMBLY_TOP_JK", "井口-转换接头组件TOP"),
    CROSS_OVER_ASSEMBLY_BTM_JK("CROSS_OVER_ASSEMBLY_BTM_JK", "井口-转换接头组件BTM");

    private String key;
    private String type;

    PressureTypeEnum(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static PressureTypeEnum getValue(String key) {
        for (PressureTypeEnum a : PressureTypeEnum.values()) {
            if (a.getKey().equals(key)) {
                return a;
            }
        }
        return null;
    }

}
