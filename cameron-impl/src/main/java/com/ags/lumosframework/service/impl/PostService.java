package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PostEntity;
import com.ags.lumosframework.handler.IPostHandler;
import com.ags.lumosframework.pojo.Post;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PostService extends AbstractBaseDomainObjectService<Post, PostEntity> implements IPostService{

	@Autowired
	private IPostHandler postHandler;

	@Override
	protected IBaseEntityHandler<PostEntity> getEntityHandler() {
		return postHandler;
	}

}
