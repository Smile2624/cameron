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
@Table(name = "HARDNESS")
public class HardnessEntity extends BaseEntity {

    private static final long serialVersionUID = -2752262103502644280L;

    public static final String HARDNESS_NAME = "hardnessName";//硬度文件名
    public static final String HARDNESS_STAND = "hardnessStand";//硬度文件名
    public static final String HARDNESS_UP_LIMIT = "hardnessUpLimit";//硬度文件名
    public static final String HARDNESS_DOWN_LIMIT = "hardnessDownLimit";//硬度文件名

    @Column(name = "HARDNESS_NAME", length = 80)
    private String hardnessName;

    @Column(name = "HARDNESS_STAND", length = 80)
    private String hardnessStand;

    @Column(name = "HARDNESS_UP_LIMIT")
    private float hardnessUpLimit;

    @Column(name = "HARDNESS_DOWN_LIMIT")
    private float hardnessDownLimit;

}
