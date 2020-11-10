package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.PostDao;
import com.ags.lumosframework.entity.PostEntity;
import com.ags.lumosframework.handler.IPostHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PostHandler extends AbstractBaseEntityHandler<PostEntity> implements IPostHandler {

	private static final long serialVersionUID = 798452960571324769L;

	@Autowired
	private PostDao postDao;

	@Override
	protected BaseEntityDao<PostEntity> getDao() {
		return postDao;
	}

}
