package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PostEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class Post extends ObjectBaseImpl<PostEntity> {

	private static final long serialVersionUID = 6600701660473723426L;

	public Post() {
		super(null);
	}

	public Post(PostEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return getInternalObject().getName();
	}

	public void setName(String name) {
		getInternalObject().setName(name);
	}

	public String getCode() {
		return getInternalObject().getCode();
	}

	public void setCode(String code) {
		getInternalObject().setCode(code);
	}

}
