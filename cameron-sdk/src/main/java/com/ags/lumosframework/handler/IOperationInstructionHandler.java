package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.OperationInstructionEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IOperationInstructionHandler extends IBaseEntityHandler<OperationInstructionEntity> {
}
