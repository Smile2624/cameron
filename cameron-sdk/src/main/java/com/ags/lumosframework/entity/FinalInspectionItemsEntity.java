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
@Table(name="FINAL_INSPECTION_ITEMS")
public class FinalInspectionItemsEntity extends BaseEntity{
	
	private static final long serialVersionUID = -3860242533184462517L;

	public static final String INSPECTION_ITEM_NAME = "inspectionItemName";
	public static final String DEFAULT_RESULT = "defaultResult";
	
	//终检检验项 工作路线包，装配记录压力测试。。。。
	@Column(name="INSPECTION_ITEM_NAME",length=80)
	private String inspectionItemName;
	
	@Column(name="DEFAULT_RESULT" ,length=1025)
	private String defaultResult;
}
