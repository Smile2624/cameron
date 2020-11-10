package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "GAGE")
public class GageEntity extends BaseEntity {

    private static final long serialVersionUID = -8662704039318263180L;

    public static final String GAGE_NAME = "gageName";//名称
    public static final String GAGE_NO = "gageNo";//编号
    public static final String ACTIVE_DATE = "activeDate";//有效期
    public static final String STATUS ="status";

    @Column(name = "GAGE_NAME", length = 80)
    private String gageName;

    @Column(name = "GAGE_NO", length = 80)
    private String gageNo;

    @Column(name = "ACTIVE_DATE")
    private ZonedDateTime activeDate;

    @Column(name = "STATUS")
    private String status;

}
