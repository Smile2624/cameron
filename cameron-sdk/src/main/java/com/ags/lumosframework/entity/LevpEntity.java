package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "LEVP")
public class LevpEntity extends BaseEntity {

    public static final String PRODUCTION_ORDER_NO = "productionOrderNo";
    public static final String QC_RLT = "qcRlt";
    public static final String QC_CHECKER = "qcChecker";
    public static final String QC_CHECKED_DATE = "qcCheckedDate";
    public static final String WH_RLT = "whRlt";
    public static final String WH_CHECKER = "whChecker";
    public static final String WH_CHECKED_DATE = "whCheckedDate";

    @Column(name = "PRODUCTION_ORDER_NO", length = 80)
    private String productionOrderNo;

    @Column(name = "QC_RLT", length = 80)
    private String qcRlt;

    @Column(name = "QC_CHECKER", length = 80)
    private String qcChecker;

    @Column(name = "QC_CHECKED_DATE")
    private Date qcCheckedDate;

    @Column(name = "WH_RLT", length = 80)
    private String whRlt;

    @Column(name = "WH_CHECKER", length = 80)
    private String whChecker;

    @Column(name = "WH_CHECKED_DATE")
    private Date whCheckedDate;
}
