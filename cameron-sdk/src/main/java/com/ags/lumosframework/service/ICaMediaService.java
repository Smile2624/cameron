package com.ags.lumosframework.service;

import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.sdk.service.api.IMediaService;

/**
 * @author peyton
 * @date 2019/9/30 10:22
 */
public interface ICaMediaService extends IMediaService {

    Media getMediaByName(String mediaName);
    
    Media getByTypeName(String eSignatureLoGoType,String fileName);
}
