package com.fongloo.auth.client;

import com.fongloo.auth.client.configuration.AuthClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动客户端授权
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuthClientConfiguration.class)
@Documented
@Inherited
public @interface EnableAuthClient {
}
