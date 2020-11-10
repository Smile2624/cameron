package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.entity.IssueMaterialListEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IIssueMaterialHandler extends IBaseEntityHandler<IssueMaterialListEntity> {
}
