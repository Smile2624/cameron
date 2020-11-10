package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.OrderHistoryEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IOrderHistoryHandler extends IBaseEntityHandler<OrderHistoryEntity>{

}
