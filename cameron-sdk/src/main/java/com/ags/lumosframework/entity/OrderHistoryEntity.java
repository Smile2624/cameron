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
@Table(name="ORDER_HISTORY")
public class OrderHistoryEntity extends BaseEntity{

	private static final long serialVersionUID = 3879562271626380897L;

	/*
	 * 记录Order历史数据
	 * 指的是order在生产过程中，Routing在每个step操作信息。作为终检时QC检查的依据
	 * */
	public static final String ORDER_NO="orderNo";
	public static final String OPERATION_NO="operationNo";
	public static final String OPERATION_DESC="operationDesc";
	public static final String OPERATION_ATTENTION="operationAttention";
	public static final String CONFIRM_BY="confirmBy";
	public static final String RECONFIRM_BY="reConfirmBy";
	public static final String CONFIRM_DATE="confirmDate";
	public static final String RECONFIRM_DATE="reConfirmDate";
	public static final String RECONFIRM_NEEDED = "reconfirmNeeded";//是否需要二次确认
	public static final String DELETE_FLAG = "deleteFlag";//

	@Column(name="ORDER_NO",length=80)
	private String orderNo;
	
	@Column(name="OPERATION_NO",length=80)
	private String operationNo;
	
	@Column(name="OPERATION_DESC",length=80)
	private String operationDesc;
	
	@Column(name="OPERATION_ATTENTION",columnDefinition="text")
	private String operationAttention;
	
	@Column(name="CONFIRM_BY" ,length=80)
	private String confirmBy;
	
	@Column(name="RECONFIRM_BY",length=80)
	private String reConfirmBy;
	
	@Column(name="CONFIRM_DATE")
	private Date confirmDate;
	
	@Column(name="RECONFIRM_DATE")
	private Date reConfirmDate;

	@Column(name = "RECONFIRM_NEEDED")
	private boolean reconfirmNeeded;

	@Column(name = "DELETE_FLAG")
	private boolean deleteFlag;


}
