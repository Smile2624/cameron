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
@Table(name = "ASSEMBLING")
public class AssemblingEntity extends BaseEntity {
    private static final long serialVersionUID = -5750457284516643551L;

    public static final String SN_BATCH = "snBatch";
    public static final String ORDER_NO = "orderNo";
    public static final String PART_NO = "partNo";
    public static final String PART_DESC = "partDesc";
    public static final String PLM_REV = "plmRev";
    public static final String PART_REV = "partRev";
    public static final String BATCH_QTY = "batchQty";
    public static final String SERIAL_NO = "serialNo";
    public static final String BATCH = "batch";
    public static final String HEAT_NO_LOT = "heatNoLot";
    public static final String HARDNESS = "hardness";
    public static final String MAT_TYPE = "matType";
    public static final String C_OR_D = "cOrD";
    public static final String E_OR_D = "eOrD";
    public static final String QC_CHECK = "qcCheck";
    public static final String RETROSPECT_TYPE = "retrospectType";

    @Column(name = "SN_BATCH", length = 80)
    private String snBatch;

    @Column(name = "ORDER_NO", length = 80)
    private String orderNo;

    @Column(name = "PART_NO", length = 80)
    private String partNo;

    @Column(name = "PART_DESC", length = 80)
    private String partDesc;

    @Column(name = "PLM_REV", length = 80)
    private String plmRev;

    @Column(name = "PART_REV", length = 80)
    private String partRev;

    @Column(name = "BATCH_QTY")
    private int batchQty;

    @Column(name = "SERIAL_NO", length = 80)
    private String serialNo;

    @Column(name = "BATCH", length = 80)
    private String batch;

    @Column(name = "HEAT_NO_LOT", length = 80)
    private String heatNoLot;

    @Column(name = "HARDNESS", length = 80)
    private String hardness;

    @Column(name = "MAT_TYPE", length = 80)
    private String matType;

    @Column(name = "C_OR_D", length = 80)
    private String cOrD;

    @Column(name = "E_OR_D", length = 80)
    private String eOrD;

    @Column(name = "QC_CHECK", length = 80)
    private String qcCheck;

    @Column(name = "RETROSPECT_TYPE", length = 80)
    private String retrospectType;

}
