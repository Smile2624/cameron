package com.ags.lumosframework.impl.config;

import com.ags.lumosframework.impl.base.autoconfig.CommonsDataConfigurer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.ags.lumosframework.dao", "com.ags.lumosframework.handler.impl","com.ags.lumosframework.service.impl"})
@CommonsDataConfigurer(entityPackages = {"com.ags.lumosframework.entity"}, changelog = "db-changelog/cameron/changelog.xml", order = 10)
public class ImplAutoConfig {

}
