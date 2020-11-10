package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.CertificateOfConformanceEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.util.Date;

public class CertificateOfConformance extends ObjectBaseImpl<CertificateOfConformanceEntity> {

	
	private static final long serialVersionUID = -8954614839344766389L;

	public CertificateOfConformance() {
		super(null);
	}
	
	public CertificateOfConformance(CertificateOfConformanceEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getOrderNo() {
        return getInternalObject().getOrderNo();
    }
    public void setOrderNo(String orderNo) {
        getInternalObject().setOrderNo(orderNo);
    }
    
    public String getLogo() {
        return getInternalObject().getLogo();
    }
    public void setLogo(String logo) {
        getInternalObject().setLogo(logo);
    }
	
	public String getCertificateNumber() {
        return getInternalObject().getCertificateNumber();
    }
    public void setCertificateNumber(String certificateNumber) {
        getInternalObject().setCertificateNumber(certificateNumber);
    }

    public String getCustomer() {
        return getInternalObject().getCustomer();
    }
    public void setCustomer(String customer) {
        getInternalObject().setCustomer(customer);
    }
    
    public String getCustomerPONumber() {
        return getInternalObject().getCustomerPONumber();
    }
    public void setCustomerPONumber(String customerPONumber) {
        getInternalObject().setCustomerPONumber(customerPONumber);
    }
    
    public Date getDate() {
        return getInternalObject().getDate();
    }
    public void setDate(Date date) {
        getInternalObject().setDate(date);
    }
    
    public String getSalesOrderNumber() {
        return getInternalObject().getSalesOrderNumber();
    }
    public void setSalesOrderNumber(String salesOrderNumber) {
        getInternalObject().setSalesOrderNumber(salesOrderNumber);
    }
    
    public String getInternalTrackingNumber() {
        return getInternalObject().getInternalTrackingNumber();
    }
    public void setInternalTrackingNumber(String internalTrackingNumber) {
        getInternalObject().setInternalTrackingNumber(internalTrackingNumber);
    }
    
    public String getItem() {
        return getInternalObject().getItem();
    }
    public void setItem(String item) {
        getInternalObject().setItem(item);
    }
    
    public String getNotes() {
        return getInternalObject().getNotes();
    }
    public void setNotes(String notes) {
        getInternalObject().setNotes(notes);
    }
    
    public String getLicenseNumber() {
        return getInternalObject().getLicenseNumber();
    }
    public void setLicenseNumber(String licenseNumber) {
        getInternalObject().setLicenseNumber(licenseNumber);
    }
    
    public String getQualityRepresentative() {
        return getInternalObject().getQualityRepresentative();
    }
    public void setQualityRepresentative(String qualityRepresentative) {
        getInternalObject().setQualityRepresentative(qualityRepresentative);
    }
	
}
