package com.ags.lumosframework.config;

import com.ags.lumosframework.sdk.dubbo.RpcServiceScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.ags.lumosframework.service.impl"})
@RpcServiceScan({"com.ags.lumosframework.handler"})
public class SDKAutoConfig {

}
