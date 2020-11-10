package com.ags.lumosframework.handler;

import com.ags.lumosframework.entity.ProductInformationEntity;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.dubbo.RpcSupport;

@RpcSupport
public interface IProductInformationHandler extends IBaseEntityHandler<ProductInformationEntity> {

}
