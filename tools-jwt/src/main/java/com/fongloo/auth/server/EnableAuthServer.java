package com.fongloo.auth.server;

import com.fongloo.auth.server.configuration.AuthServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用服务端配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AuthServerConfiguration.class)
public @interface EnableAuthServer {
}
