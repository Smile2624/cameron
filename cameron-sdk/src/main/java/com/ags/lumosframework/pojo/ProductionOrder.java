package com.ags.lumosframework.pojo;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.ProductionOrderEntity;
import com.ags.lumosframework.enums.RetrospectType;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.service.IBomService;
import com.ags.lumosframework.service.IProductInformationService;
import com.ags.lumosframework.service.IProductionOrderService;

import java.util.List;

public class ProductionOrder extends ObjectBaseImpl<ProductionOrderEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1307459778321734021L;

	@Override
	public String getName() {
		return null;
	}
	
	public ProductionOrder() {
		super(null);
	}
	
	public ProductionOrder(ProductionOrderEntity entity) {
		super(entity);
	}
	
	public String getProductOrderId() {
		return this.getInternalObject().getProductOrderId();
	}
	public void setProductOrderId(String productOrderId) {
		this.getInternalObject().setProductOrderId(productOrderId);
	}
	
	public String getProductId() {
		return this.getInternalObject().getProductId();
	}
	public void setProductId(String productId) {
		this.getInternalObject().setProductId(productId);
	}
	public String getProductVersionId() {
		return this.getInternalObject().getProductVersionId();
	}
	public void setProductVersionId(String productVersionId) {
		this.getInternalObject().setProductVersionId(productVersionId);
	}
	
	public String getProductDesc() {
		return this.getInternalObject().getProductDesc();
	}
	public void setProductDesc(String productDesc) {
		this.getInternalObject().setProductDesc(productDesc);
	}
	
	public int getProductNumber() {
		return this.getInternalObject().getProductQty();
	}
	public void setProductNumber(int productQty) {
		this.getInternalObject().setProductQty(productQty);
	}
	
	public String getRoutingGroup() {
		return this.getInternalObject().getRoutingGroup();
	}
	
	public void setRoutingGroup(String routingGroup) {
		this.getInternalObject().setRoutingGroup(routingGroup);
	}

	public String getInnerGroupNo() {
		return this.getInternalObject().getInnerGroupNo();
	}
	
	public void setInnerGroupNo(String innerGroupNo) {
		this.getInternalObject().setInnerGroupNo(innerGroupNo);
	}

	public String getCustomerCode(){return this.getInternalObject().getCustomerCode();}

	public void setCustomerCode(String customerCode){this.getInternalObject().setCustomerCode(customerCode);}

	public String getSalesOrder(){return this.getInternalObject().getSalesOrder();}

	public void setSalesOrder(String salesOrder){this.getInternalObject().setSalesOrder(salesOrder);}

	public String getSalesOrderItem(){return this.getInternalObject().getSalesOrderItem();}

	public void setSalesOrderItem(String salesOrderItem){this.getInternalObject().setSalesOrderItem(salesOrderItem);}

	public String getPaintSpecification(){return this.getInternalObject().getPaintSpecification();}

	public void setPaintSpecification(String paintSpecification){this.getInternalObject().setPaintSpecification(paintSpecification);}

	public String getComments(){return this.getInternalObject().getComments();}

	public void setComments(String comments){this.getInternalObject().setComments(comments);}

	public String getWorkshop(){return  this.getInternalObject().getWorkshop();}
	public void setWorkshop(String workshop){this.getInternalObject().setWorkshop(workshop);}

    //产品号+版本号 获取产品信息
    public ProductInformation getProductInformation() {
        IProductInformationService productInformationService = BeanManager.getService(IProductInformationService.class);

        return productInformationService.getByNoRev(getProductId(), getProductVersionId());
    }

    //单件追溯类型
    public List<Bom> getSingletonBoms() {
        IBomService bomService = BeanManager.getService(IBomService.class);

        return bomService.getBomsByNoRevType(getProductId(), getProductVersionId(), RetrospectType.SINGLE.getType());
    }

    //批次追溯类型
    public List<Bom> geBatchBoms() {
        IBomService bomService = BeanManager.getService(IBomService.class);

        return bomService.getBomsByNoRevType(getProductId(), getProductVersionId(), RetrospectType.BATCH.getType());
    }

	public String getSuperiorOrder() {
		return this.getInternalObject().getSuperiorOrder();
	}
	public void setSuperiorOrder(String superiorOrder) {
		this.getInternalObject().setSuperiorOrder(superiorOrder);
	}

	//Changed by Cameron: 获取子工单
	public List<ProductionOrder> getChild() {
		IProductionOrderService productionOrderService = BeanManager.getService(IProductionOrderService.class);
		return productionOrderService.fetchChildren(this);
	}

	public String getNewestBomVersion() {
		return this.getInternalObject().getNewestBomVersion();
	}

	public void setNewestBomVersion(String newestBomVersion) {
		this.getInternalObject().setNewestBomVersion(newestBomVersion);
	}
	public boolean getPullMatFlag() {
		return this.getInternalObject().isPullMatFlag();
	}

	public void setPullMatFlag(boolean pullMatFlag) {
		this.getInternalObject().setPullMatFlag(pullMatFlag);
	}

	public boolean getBomCheckFlag() {
		return this.getInternalObject().isBomCheckFlag();
	}

	public void setBomCheckFlag(boolean bomCheckFlag) {
		this.getInternalObject().setBomCheckFlag(bomCheckFlag);
	}

	public boolean getBomLockFlag() {
		return this.getInternalObject().isBomLockFlag();
	}

	public void setBomLockFlag(boolean bomLockFlag) {
		this.getInternalObject().setBomLockFlag(bomLockFlag);
	}

}
