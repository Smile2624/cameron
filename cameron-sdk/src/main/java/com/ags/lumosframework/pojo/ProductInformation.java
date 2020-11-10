package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.ProductInformationEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class ProductInformation extends ObjectBaseImpl<ProductInformationEntity> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 872340174283674458L;
	
	public ProductInformation() {
		super(null);
	}
	
	public ProductInformation(ProductInformationEntity entity) {
		super(entity);
		
	}

	@Override
	public String getName() {
		return null;
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
	public String getTemperatureRating() {
		return this.getInternalObject().getTemperatureRating();
	}
	public void setTemperatureRating(String temperatureRating ) {
		this.getInternalObject().setTemperatureRating(temperatureRating );
	}
	public String getMaterialRating() {
		return this.getInternalObject().getMaterialRating();
	}
	public void setMaterialRating(String materialRating) {
		this.getInternalObject().setMaterialRating(materialRating);
	}
	public String getPSLRating() {
		return this.getInternalObject().getPSLRating();
	}
	public void setPSLRating(String PSLRating) {
		this.getInternalObject().setPSLRating(PSLRating);
	}
	public String getPressureInspectionProcedure() {
		return this.getInternalObject().getPressureInspectionProcedure();
	}
	public void setPressureInspectionProcedure(String pressureInspectionProcedure) {
		this.getInternalObject().setPressureInspectionProcedure(pressureInspectionProcedure);
	}
	public String getPressureInspectionProcedureVersion() {
		return this.getInternalObject().getPressureInspectionProcedureVersion();
	}
	public void setPressureInspectionProcedureVersion(String pressureInspectionProcedureVersion) {
		this.getInternalObject().setPressureInspectionProcedureVersion(pressureInspectionProcedureVersion);
	}
	public String getPaintingSpecificationFile() {
		return this.getInternalObject().getPaintingSpecificationFile();
	}
	public void setPaintingSpecificationFile(String paintingSpecificationFile) {
		this.getInternalObject().setPaintingSpecificationFile(paintingSpecificationFile);
	}
	
	public String getPaintingSpecificationFileRev() {
		return this.getInternalObject().getPaintingSpecificationFileRev();
	}
	public void setPaintingSpecificationFileRev(String paintingSpecificationFileRev) {
		this.getInternalObject().setPaintingSpecificationFileRev(paintingSpecificationFileRev);
	}

	public String getQulityPlan() {
		return this.getInternalObject().getQulityPlan();
	}
	
	public void setQualityPlan(String qulityPlan) {
		this.getInternalObject().setQulityPlan(qulityPlan);
	}
	
	public String getQulityPlanRev() {
		return this.getInternalObject().getQulityPlanRev();
	}
	
	public void setQulityPlanRev(String qulityPalnRev) {
		this.getInternalObject().setQulityPlanRev(qulityPalnRev);
	}

	public String getBlowdownTorque() {
		return this.getInternalObject().getBlowdownTorque();
	}

	public void setBlowdownTorque(String blowdownTorque) {
		this.getInternalObject().setBlowdownTorque(blowdownTorque);
	}

	public boolean isReviewed() {
		return this.getInternalObject().isReviewed();
	}

	public void setReviewed(boolean reviewed) {
		this.getInternalObject().setReviewed(reviewed);
	}

	public String getGasTest() {
		return this.getInternalObject().getGasTest();
	}

	public void setGasTest(String gasTest) {
		this.getInternalObject().setGasTest(gasTest);
	}

	public String getGasTestRev() {
		return this.getInternalObject().getGasTestRev();
	}

	public void setGasTestRev(String gasTestRev) {
		this.getInternalObject().setGasTestRev(gasTestRev);
	}

	public String getLevp() {
		return this.getInternalObject().getLevp();
	}

	public void setLevp(String levp) {
		this.getInternalObject().setLevp(levp);
	}

	public String getLevpRev() {
		return this.getInternalObject().getLevpRev();
	}

	public void setLevpRev(String levpRev) {
		this.getInternalObject().setLevpRev(levpRev);
	}
}
