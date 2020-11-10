package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "VISUAL_INSPECTION")
public class VisualInspectionEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String PURCHASING_ORDER = "purchasingOrder";
    public static final String PURCHASING_ORDER_ITEM = "purchasingOrderItem";
    public static final String SAP_INSPECTION_NO = "sapInspectionNo";

    public static final String PACKING_RESULT = "packingResult";
    public static final String RAW_MATERIAL_RESULT = "rawMaterialResult";
    public static final String MACHINED_SURFACE_RESULT = "machinedSurfaceResult";
    public static final String NONMETAL_PARTS_RESULT = "nonmetalPartsResult";
    public static final String COATING_RESULT = "coatingResult";
    public static final String OTHERS_RESULT = "otherResult";
    public static final String COMMENT = "comment";
    public static final String QC_CONFIRM_DATE = "qcConfirmDate";
    public static final String QC_CHECKER = "qcChecker";
    public static final String QC_WITNESS = "qcWitness";
    public static final String API_SPEC = "apiSpec";
    public static final String PSL = "psl";
    public static final String SN = "sn";

    @Column(name = "PURCHASING_ORDER", length = 80)
    private String purchasingOrder;

    @Column(name = "PURCHASING_ORDER_ITEM", length = 80)
    private String purchasingOrderItem;

    @Column(name = "SAP_INSPECTION_NO", length = 80)
    private String sapInspectionNo;

    @Column(name = "PACKING_RESULT", length = 80)
    private String packingResult;

    @Column(name = "RAW_MATERIAL_RESULT", length = 80)
    private String rawMaterialResult;

    @Column(name = "MACHINED_SURFACE_RESULT", length = 80)
    private String machinedSurfaceResult;

    @Column(name = "NONMETAL_PARTS_RESULT", length = 80)
    private String nonmetalPartsResult;

    @Column(name = "COATING_RESULT", length = 80)
    private String coatingResult;

    @Column(name = "OTHERS_RESULT", length = 80)
    private String otherResult;

    @Column(name = "COMMENT", length = 80)
    private String comment;

    @Column(name = "QC_CONFIRM_DATE", length = 80)
    private String qcConfirmDate;

    @Column(name = "API_SPEC", length = 80)
    private String apiSpec;

    @Column(name = "PSL", length = 80)
    private String psl;

    @Column(name = "QC_CHECKER", length = 80)
    private String qcChecker;

    @Column(name = "SN", length = 80)
    private String sn;

    @Column(name = "QC_WITNESS", length = 80)
    private String qcWitness;
}
