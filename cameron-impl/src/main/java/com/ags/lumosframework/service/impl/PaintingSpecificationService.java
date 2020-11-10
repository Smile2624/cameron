package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PaintingSpecificationEntity;
import com.ags.lumosframework.handler.IPaintingSpecificationHandler;
import com.ags.lumosframework.pojo.PaintingSpecification;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPaintingSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PaintingSpecificationService extends AbstractBaseDomainObjectService<PaintingSpecification,PaintingSpecificationEntity> implements IPaintingSpecificationService {
	
	@Autowired
	private IPaintingSpecificationHandler paintingSpecificationHandler;
	
	@Override
	protected IBaseEntityHandler<PaintingSpecificationEntity> getEntityHandler() {
		return paintingSpecificationHandler;
	}

	@Override
	public PaintingSpecification getBySpecificationFile(String paintingSpecificationFileValue) {
		EntityFilter filter = createFilter();
		if(paintingSpecificationFileValue!=null && !paintingSpecificationFileValue.equals("")) {
			filter.fieldEqualTo(PaintingSpecificationEntity.PAINTING_SPECIFICATION_FILE, paintingSpecificationFileValue);
		}
		return getByFilter(filter);
	}

	@Override
	public List<PaintingSpecification> getAll() {
		EntityFilter filter = createFilter();
		filter.orderBy(PaintingSpecificationEntity.PAINTING_SPECIFICATION_FILE, false);
		return listByFilter(filter);
	}
	
}
