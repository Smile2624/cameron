package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PaintingInformationEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class PaintingInformation extends ObjectBaseImpl<PaintingInformationEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5903469167725855636L;

	@Override
	public String getName() {
		return null;
	}
	
	public PaintingInformation() {
		super(null);
	}
	
	public PaintingInformation(PaintingInformationEntity entity) {
		super(entity);
	}
	
	public String getWorkOrderSN() {
		return this.getInternalObject().getWorkOrderSN();
	}
	public void setWorkOrderSN(String workOrderSN) {
		this.getInternalObject().setWorkOrderSN(workOrderSN);
	}
	
	public boolean getIsClear() {
		return this.getInternalObject().isClear();
	}
	public void setIsClear(boolean isClear) {
		this.getInternalObject().setClear(isClear);
	}
	
	public boolean getIsProtect() {
		return this.getInternalObject().isProtect();
	}
	public void setIsProtect(boolean isProtect) {
		this.getInternalObject().setProtect(isProtect);
	}
	
	public Float getPrimerAirTemp() {
		return this.getInternalObject().getPrimerAirTemp();
	}
	public void setPrimerAirTemp(Float primerAirTemp) {
		this.getInternalObject().setPrimerAirTemp(primerAirTemp);
	}
	
	public Float getPrimerSurfaceTemp() {
		return this.getInternalObject().getPrimerSurfaceTemp();
	}
	public void setPrimerSurfaceTemp(Float primerSurfaceTemp) {
		this.getInternalObject().setPrimerSurfaceTemp(primerSurfaceTemp);
	}
	
	public Float getPrimerHumidity() {
		return this.getInternalObject().getPrimerHumidity();
	}
	public void setPrimerHumidity(Float primerHumidity) {
		this.getInternalObject().setPrimerHumidity(primerHumidity);
	}
	
	public Float getPrimerDewPoint() {
		return this.getInternalObject().getPrimerDewPoint();
	}
	public void setPrimerDewPoint(Float primerDewPoint) {
		this.getInternalObject().setPrimerDewPoint(primerDewPoint);
	}
	
	public String getPrimerPaintApplied() {
		return this.getInternalObject().getPrimerPaintApplied();
	}
	public void setPrimerPaintApplied(String primerPaintApplied) {
		this.getInternalObject().setPrimerPaintApplied(primerPaintApplied);
	}
	
	public String getPrimerDryFilmThickness() {
		return this.getInternalObject().getPrimerDryFilmThickness();
	}
	public void setPrimerDryFilmThickness(String primerDryFilmThickness) {
		this.getInternalObject().setPrimerDryFilmThickness(primerDryFilmThickness);
	}
	
	public Float getPrimerDryAirTemp() {
		return this.getInternalObject().getPrimerDryAirTemp();
	}
	public void setPrimerDryAirTemp(Float primerDryAirTemp) {
		this.getInternalObject().setPrimerDryAirTemp(primerDryAirTemp);
	}
	
	public Float getPrimerDryHumidity() {
		return this.getInternalObject().getPrimerDryHumidity();
	}
	public void setPrimerDryHumidity(Float primerDryHumidity) {
		this.getInternalObject().setPrimerDryHumidity(primerDryHumidity);
	}
	
	public String getPrimerDryMethod() {
		return this.getInternalObject().getPrimerDryMethod();
	}
	public void setPrimerDryMethod(String primerDryMethod) {
		this.getInternalObject().setPrimerDryMethod(primerDryMethod);
	}
	
	public Float getPrimerDryTime() {
		return this.getInternalObject().getPrimerDryTime();
	}
	public void setPrimerDryTime(Float primerDryTime) {
		this.getInternalObject().setPrimerDryTime(primerDryTime);
	}
	
	public Float getFinalCoatAirTemp() {
		return this.getInternalObject().getFinalCoatAirTemp();
	}
	public void setFinalCoatAirTemp(Float finalCoatAirTemp) {
		this.getInternalObject().setFinalCoatAirTemp(finalCoatAirTemp);
	}
	
	public Float getFinalCoatSurfaceTemp() {
		return this.getInternalObject().getFinalCoatSurfaceTemp();
	}
	public void setFinalCoatSurfaceTemp(Float finalCoatSurfaceTemp) {
		this.getInternalObject().setFinalCoatSurfaceTemp(finalCoatSurfaceTemp);
	}
	
	public Float getFinalCoatHumidity() {
		return this.getInternalObject().getFinalCoatHumidity();
	}
	public void setFinalCoatHumidity(Float finalCoatHumidity) {
		this.getInternalObject().setFinalCoatHumidity(finalCoatHumidity);
	}
	
	public Float getFinalCoatDewPoint() {
		return this.getInternalObject().getFinalCoatDewPoint();
	}
	public void setFinalCoatDewPoint(Float finalCoatDewPoint) {
		this.getInternalObject().setFinalCoatDewPoint(finalCoatDewPoint);
	}
	
	public String getFinalCoatPaintApplied() {
		return this.getInternalObject().getFinalCoatPaintApplied();
	}
	public void setFinalCoatPaintApplied(String finalCoatPaintApplied) {
		this.getInternalObject().setFinalCoatPaintApplied(finalCoatPaintApplied);
	}
	
	public String getFinalCoatColor() {
		return this.getInternalObject().getFinalCoatColor();
	}
	public void setFinalCoatColor(String finalCoatColor) {
		this.getInternalObject().setFinalCoatColor(finalCoatColor);
	}
	
	public String getFinalCoatTotalDryFilmThickness() {
		return this.getInternalObject().getFinalCoatTotalDryFilmThickness();
	}
	public void setFinalCoatTotalDryFilmThickness(String finalCoatTotalDryFilmThickness) {
		this.getInternalObject().setFinalCoatTotalDryFilmThickness(finalCoatTotalDryFilmThickness);
	}
	
	public Float getFinalCoatDryAirTemp() {
		return this.getInternalObject().getFinalCoatDryAirTemp();
	}
	public void setFinalCoatDryAirTemp(Float finalCoatDryAirTemp) {
		this.getInternalObject().setFinalCoatDryAirTemp(finalCoatDryAirTemp);
	}
	
	public Float getFinalCoatDryHumidity() {
		return this.getInternalObject().getFinalCoatDryHumidity();
	}
	public void setFinalCoatDryHumidity(Float finalCoatDryHumidity) {
		this.getInternalObject().setFinalCoatDryHumidity(finalCoatDryHumidity);
	}
	
	public String getFinalCoatDryMethod() {
		return this.getInternalObject().getFinalCoatDryMethod();
	}
	public void setFinalCoatDryMethod(String finalCoatDryMethod) {
		this.getInternalObject().setFinalCoatDryMethod(finalCoatDryMethod);
	}
	
	public Float getFinalCoatDryTime() {
		return this.getInternalObject().getFinalCoatDryTime();
	}
	public void setFinalCoatDryTime(Float finalCoatDryTime) {
		this.getInternalObject().setFinalCoatDryTime(finalCoatDryTime);
	}
	
	public String getVisualInspection() {
		return this.getInternalObject().getVisualInspection();
	}
	public void setVisualInspection(String visualInspection) {
		this.getInternalObject().setVisualInspection(visualInspection);
	}
	
	public String getVisualTotalDryFilmThickness() {
		return this.getInternalObject().getVisualTotalDryFilmThickness();
	}
	public void setVisualTotalDryFilmThickness(String visualTotalDryFilmThickness) {
		this.getInternalObject().setVisualTotalDryFilmThickness(visualTotalDryFilmThickness);
	}
	
	public String getVisualGageNo() {
		return this.getInternalObject().getVisualGageNo();
	}
	public void setVisualGageNo(String visualGageNo) {
		this.getInternalObject().setVisualGageNo(visualGageNo);
	}
	
	public String getComments() {
		return this.getInternalObject().getComments();
	}
	public void setComments(String comments) {
		this.getInternalObject().setComments(comments);
	}
	
	public Float getIntermediate() {
		return this.getInternalObject().getIntermediate();
	}
	public void setIntermediate(Float intermediate) {
		this.getInternalObject().setIntermediate(intermediate);
	}
	
	public String getOpUser() {
		return this.getInternalObject().getOpUser();
	}
	
	public void setOpUser(String opUser) {
		this.getInternalObject().setOpUser(opUser);
	}
	
	public String getQConfirmer() {
		return this.getInternalObject().getQcConfirmer();
	}
	
	public void setQcConfirmer(String qcConfirmer) {
		this.getInternalObject().setQcConfirmer(qcConfirmer);
	}
}
