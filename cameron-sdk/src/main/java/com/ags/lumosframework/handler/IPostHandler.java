package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.PostEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IPostHandler extends IBaseEntityHandler<PostEntity> {

}
