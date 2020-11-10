package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.CertificateOfConformance;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface ICertificateOfConformanceService  extends IBaseDomainObjectService<CertificateOfConformance> {
	CertificateOfConformance getByOrder(String workOrderSN);

	CertificateOfConformance getByCertificateNo(String cocNo);
	
	int getCertificateNumberNext(String prefix);
}
