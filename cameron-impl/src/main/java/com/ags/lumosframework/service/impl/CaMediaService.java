package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.entity.MediaEntity;
import com.ags.lumosframework.sdk.service.MediaService;
import com.ags.lumosframework.service.ICaMediaService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author peyton
 * @date 2019/9/30 10:25
 */
@Service
@Primary
public class CaMediaService extends MediaService implements ICaMediaService {
    @Override
    public Media getMediaByName(String mediaName) {
        EntityFilter filter = createFilter();
        if (mediaName != null && !"".equals(mediaName)) {
            filter.fieldEqualTo(MediaEntity.NAME, mediaName);
        } else {
            return null;//Changed by Cameron: 若不返回null，存在该员工未配置签名，但生成报告不报错的情况
        }
        return getByFilter(filter);
    }

	@Override
	public Media getByTypeName(String eSignatureLoGoType, String fileName) {
		EntityFilter filter = createFilter();
        if (eSignatureLoGoType != null && !"".equals(eSignatureLoGoType)) {
            filter.fieldEqualTo("category", eSignatureLoGoType);
        }
        if (fileName != null && !"".equals(fileName)) {
            filter.fieldEqualTo(MediaEntity.NAME, fileName);
        }
        return getByFilter(filter);
	}
    
    
}
