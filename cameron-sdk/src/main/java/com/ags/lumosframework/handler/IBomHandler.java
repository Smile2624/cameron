package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.BomEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IBomHandler extends IBaseEntityHandler<BomEntity> {
}
