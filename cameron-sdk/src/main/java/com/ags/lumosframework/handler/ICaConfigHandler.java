package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.CaConfigEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

/**
 * @author peyton
 * @date 2019/10/18 10:28
 */
@RpcSupport
public interface ICaConfigHandler  extends IBaseEntityHandler<CaConfigEntity> {
}
