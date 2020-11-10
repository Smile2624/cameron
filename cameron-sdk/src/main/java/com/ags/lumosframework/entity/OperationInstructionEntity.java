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
@Table(name = "OPERATION_INSTRUCTION")
public class OperationInstructionEntity extends BaseEntity {
    private static final long serialVersionUID = -2072454131031965397L;

    public static final String INSTRUCTION_NAME = "instructionName";
    public static final String INSTRUCTION_DESC = "instructionDesc";
    public static final String INSTRUCTION_REV = "instructionRev";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String TOC_TYPE = "tocType";
    public static final String WI_DEPARTMENT = "wiDepartment";
    public static final String DELETE_FLAG = "deleteFlag";

    @Column(name = "INSTRUCTION_NAME", length = 255)
    private String instructionName;

    @Column(name = "INSTRUCTION_DESC", length = 255)
    private String instructionDesc;

    @Column(name = "INSTRUCTION_REV", length = 80)
    private String instructionRev;

    @Column(name = "MODIFIED_DATE", length = 80)
    private String modifiedDate;

    @Column(name = "TOC_TYPE", length = 80)
    private String tocType;

    @Column(name = "WI_DEPARTMENT", length = 80)
    private String wiDepartment;

    @Column(name = "DELETE_FLAG", length = 80)
    private boolean deleteFlag;


}
