package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.AppearanceInstrumentationResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class AppearanceInstrumentationResult extends ObjectBaseImpl<AppearanceInstrumentationResultEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7647921939045660997L;

	public AppearanceInstrumentationResult(AppearanceInstrumentationResultEntity entity) {
		super(entity);
	}
	
	public AppearanceInstrumentationResult() {
		super(null);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getOrderNo() {
		return this.getInternalObject().getOrderNo();
	}
	
	public void setOrderNo(String orderNo) {
		this.getInternalObject().setOrderNo(orderNo);
	}
	
	public String getSn() {
		return this.getInternalObject().getSn();
	}
	
	public void setSn(String sn) {
		this.getInternalObject().setSn(sn);
	}
	
	public Float getPaintingThicknessResult() {
		return this.getInternalObject().getPaintingThicknessResult();
	}
	
	public void setPaintingThicknessResult(Float paintingThicknessResult) {
		this.getInternalObject().setPaintingThicknessResult(paintingThicknessResult);
	}
	
	public String getVisualExaminationDesc() {
		return this.getInternalObject().getVisualExaminationDesc();
	}
	
	public void setVisualExaminationDesc(String visualExaminationDesc) {
		this.getInternalObject().setVisualExaminationDesc(visualExaminationDesc);
	}

	public String getGageInfo(){return this.getInternalObject().getGageinfo();}
	public void setGageInfo(String gageInfo){this.getInternalObject().setGageinfo(gageInfo);}
}
