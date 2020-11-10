package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PaintingSpecificationEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class PaintingSpecification extends ObjectBaseImpl<PaintingSpecificationEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -229779189974905106L;
	
	public PaintingSpecification() {
		super(null);
	}

	public PaintingSpecification(PaintingSpecificationEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getPaintingSpecificationFile() {
		return this.getInternalObject().getPaintingSpecificationFile();
	}
	public void setPaintingSpecificationFile(String paintingSpecificationFile) {
		this.getInternalObject().setPaintingSpecificationFile(paintingSpecificationFile);
	}
	
	public String getPrimer() {
		return this.getInternalObject().getPrimer();
	}
	public void setPrimer(String primer) {
		this.getInternalObject().setPrimer(primer);
	}
	
	public String getIntermediate() {
		return this.getInternalObject().getIntermediate();
	}
	public void setIntermediate(String intermediate) {
		this.getInternalObject().setIntermediate(intermediate);
	}
	
	public String getFinals() {
		return this.getInternalObject().getFinals();
	}
	public void setFinals(String finals) {
		this.getInternalObject().setFinals(finals);
	}
	
	public String getColor() {
		return this.getInternalObject().getColor();
	}
	public void setColor(String color) {
		this.getInternalObject().setColor(color);
	}
	
	public String getHumidity() {
		return this.getInternalObject().getHumidity();
	}
	public void setHumidity(String humidity) {
		this.getInternalObject().setHumidity(humidity);
	}
	
	public String getAboveDewPoint() {
		return this.getInternalObject().getAboveDewPoint();
	}
	public void setAboveDewPoint(String aboveDewPoint) {
		this.getInternalObject().setAboveDewPoint(aboveDewPoint);
	}
	//Changed by Cameron: 加入喷漆标准版本、总干膜厚度
	public String getRev() {
		return this.getInternalObject().getRev();
	}
	public void setRev(String rev) {
		this.getInternalObject().setRev(rev);
	}

	public String getTotal() {
		return this.getInternalObject().getTotal();
	}
	public void setTotal(String total) {
		this.getInternalObject().setTotal(total);
	}
}
