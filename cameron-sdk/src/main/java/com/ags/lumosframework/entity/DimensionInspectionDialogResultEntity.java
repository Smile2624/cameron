package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class DimensionInspectionDialogResultEntity extends BaseEntity {
	
	private static final long serialVersionUID = 8436668353982704528L;
	
	public static final String ORDER_SN = "orderSn";//序列号
	public static final String SN = "sn";//SN
	public static final String INSPECTION_ITEM="inspectionItem";
	public static final String IS_PASS = "isPass";//是否通过
	public static final String RESULT = "result";//SN
	
	@Column(name = "ORDER_NO", length = 80)
    private String orderSn;
    
    @Column(name = "SN", length = 80)
    private String sn;
    
    @Column(name="IS_PASS")
	private boolean isPass;
    
    @Column(name = "RESULT", length = 80)
    private String result;
    
    private String inspectionItem;
}
