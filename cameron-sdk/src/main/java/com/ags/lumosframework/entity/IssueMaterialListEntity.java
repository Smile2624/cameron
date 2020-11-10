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
@Table(name="ISSUE_MATERIAL_LIST")
public class IssueMaterialListEntity extends BaseEntity {
    public static final String ORDER_NO = "orderNo";
    public static final String MATERIAL_NO ="materialNo";
    public static final String MATERIA_DESC = "materialDesc";
    public static final String REQUIREMENT_QUANTITY = "requirementQuantity";
    public static final String QUANTITY_WITHDRAWN = "quantityWithdrawn";
    public static final String SHORTAGE = "shortage";
    public static final String PROD_STORAGE = "prodStorage";
    public static final String STATUS ="status";


    @Column(name = "ORDER_NO",length = 80)
    private String orderNo;

    @Column(name = "MATERIAL_NO" ,length = 80)
    private String materialNo;

    @Column(name = "MATERIA_DESC" ,length = 255)
    private String materialDesc;

    @Column(name = "REQUIREMENT_QUANTITY" ,length = 80)
    private double requirementQuantity;

    @Column(name = "QUANTITY_WITHDRAWN" ,length = 80)
    private double quantityWithdrawn;

    @Column(name = "SHORTAGE" ,length = 80)
    private double shortage;

    @Column(name = "PROD_STORAGE" ,length = 80)
    private String prodStorage;

    @Column(name="STATUS",length = 80)
    private String status;
}
