package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "SPARE_PART_INSPECTION_RULER")
public class SparePartInspectionRulerEntity extends BaseEntity {
    public static final String PART_NO ="partNo";
    public static final String PART_REV ="partRev";
    public static final String ITEM_NAME="itemName";
    public static final String MAX_VALUE="maxValue";//数值型
    public static final String MIN_VALUE="minValue";
    public static final String COMMET_VALUE="commentValue";//描述型

    @Column(name = "PART_NO",length = 80)
    private String partNo;

    @Column(name="PART_REV",length = 80)
    private String partRev;

    @Column(name="ITEM_NAME",length = 255)
    private String itemName;

    @Column(name="MAX_VALUE")
    private double maxValue;

    @Column(name="MIN_VALUE")
    private double minValue;

    @Column(name="COMMET_VALUE",length = 255)
    private String commentValue;
}
