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
@Table(name="PRODUCT_ROUTING")
public class ProductRoutingEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final String PRODUCT_ID = "productId";
	public static final String PRODUCT_DESC = "productDesc";
	public static final String OPRATION_NO ="oprationNo";
	public static final String OPRATION_DESC = "oprationDesc";
	public static final String ATTENTION = "attention";
	public static final String ROUTING_GROUP = "routingGroup";
	public static final String INNER_GROUP_NO = "innerGroupNo";
	public static final String ROUTING_DESC = "routingDesc";
	public static final String INNER_GROUP_NO_INT = "innerGroupNoInt";//用作排序
	public static final String CHECK_STATUS = "checkStatus";
	public static final String RECONFIRM_NEEDED = "reconfirmNeeded";//是否需要二次确认
	//因客户提供资料与前期确认信息由误，以下根据资料修改
	@Column(name="PRODUCT_ID" ,length=80)//产品号
	private String productId;
	
	@Column(name="PRODUCT_DESC" , length=255)//产品描述
	private String productDesc;
	
	@Column(name="OPRATION_NO" , length=80)//操作序号
	private String oprationNo;
	
	@Column(name="OPRATION_DESC" , length=1024)//操作描述
	private String oprationDesc;
	
	@Column(name="ATTENTION" , columnDefinition="text")//长文本信息
	private String attention;
	
	//新增
	@Column(name="ROUTING_GROUP",length=80)//routing组号
	private String routingGroup;
	
	@Column(name="INNER_GROUP_NO",length=80)//组内编号，同一个产品只有一个Routing名称即routing组号，通过不能的组内编号确认不停的routing
	private String innerGroupNo;
	
	@Column(name="ROUTING_DESC",length=255)//Routing描述，从这里获取喷漆规范
	private String routingDesc;
	
	@Column(name="INNER_GROUP_NO_INT")
	private int innerGroupNoInt;

	@Column(name = "CHECK_STATUS",length = 80)
	private String checkStatus;

	@Column(name = "RECONFIRM_NEEDED")
	private boolean reconfirmNeeded;
}
