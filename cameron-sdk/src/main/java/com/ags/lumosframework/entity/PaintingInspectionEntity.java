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
@Table(name="PAINTING_INSPECTION")
public class PaintingInspectionEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	public static final String WO_SN ="workOrderSN";//等同产品订单号
	public static final String RESULT = "result";//检测结果
	
	@Column(name="WO_SN" ,length=80)
	private String workOrderSN;
	
	@Column(name="RESULT" ,length=80)
	private String result;
}
