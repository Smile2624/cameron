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
@Table(name = "ASSEMBLING_TEMP")
public class AssemblingTempEntity extends BaseEntity {

    private static final long serialVersionUID = 8137943509694013070L;

    public static final String ORDER_NO = "orderNo";
    public static final String SN_BATCH = "snBatch";
    public static final String X_VALUE = "xValue";
    public static final String Y_VALUE = "yValue";
    public static final String COUNT_NO = "countNo";
    public static final String SINGLE_OR_BATCH = "singleOrBatch";
    public static final String CHECK_TYPE = "checkType";

    @Column(name = "ORDER_NO", length = 80)
    private String orderNo;

    @Column(name = "SN_BATCH", length = 80)
    private String snBatch;

    @Column(name = "X_VALUE")
    private int xValue;

    @Column(name = "Y_VALUE")
    private int yValue;

    @Column(name = "COUNT_NO")
    private int countNo;

    @Column(name = "SINGLE_OR_BATCH")
    private boolean singleOrBatch;

    @Column(name = "CHECK_TYPE", length = 80)
    private String checkType;

}
