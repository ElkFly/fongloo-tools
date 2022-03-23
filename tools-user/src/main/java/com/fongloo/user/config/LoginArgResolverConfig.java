package com.fongloo.user.config;

import com.fongloo.user.feign.UserResolveApi;
import com.fongloo.user.interceptor.ContextHandlerInterceptor;
import com.fongloo.user.resolver.ContextArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 公共配置类
 */
public class LoginArgResolverConfig implements WebMvcConfigurer {

    @Lazy
    @Autowired
    private UserResolveApi userResolveApi;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ContextArgumentResolver(userResolveApi));
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (getHandlerInterceptor() == null) {
            return;
        }

        String[] excludePath = getExcludeCommonPathPatterns();
        registry.addInterceptor(getHandlerInterceptor())
                .addPathPatterns("/**")
                .order(10)
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    HandlerInterceptor getHandlerInterceptor() {
        return new ContextHandlerInterceptor();
    }

    /**
     * auth-client 中拦截器需要排除的地址
     */
    String[] getExcludeCommonPathPatterns() {
        String[] urls = {
                "/error",
                "/login",
                "/v2/api-docs",
                "/v2/api-docs-ext",
                "/swagger-resources/**",
                "/webjars/**",
                "/",
                "/csrf",
                "/META-INF/resources/**",
                "/resources/**",
                "/static/**",
                "/public/**",
                "classpath:/META-INF/resources/**",
                "classpath:/resources/**",
                "classpath:/static/**",
                "classpath:/public/**",
                "/cache/**",
                "/swagger-ui.html**",
                "/doc.html**"
        };
        return urls;
    }
}
