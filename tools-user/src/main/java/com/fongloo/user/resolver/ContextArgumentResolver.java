package com.fongloo.user.resolver;

import com.fongloo.user.annotation.LoginUser;
import com.fongloo.user.feign.UserQuery;
import com.fongloo.user.feign.UserResolveApi;
import com.fongloo.user.interceptor.ContextHandlerInterceptor;
import com.fongloo.user.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * Token转化SysUser
 */
@Slf4j
public class ContextArgumentResolver implements HandlerMethodArgumentResolver {

    private UserResolveApi userResolveApi;

    public ContextArgumentResolver(UserResolveApi userResolveApi) {
        this.userResolveApi = userResolveApi;
    }

    /**
     * 筛选出带有
     *
     * @param methodParameter 参数集合
     * @return 如果为true则调用resolveArgument()
     * @LoginUser注解并且类型为SysUser的参数 才生效
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // SysUser && @LoginUser
        return methodParameter.getParameterType().equals(SysUser.class) && methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    /**
     * @param methodParameter       参数集合
     * @param modelAndViewContainer model和view
     * @param nativeWebRequest      web
     * @param webDataBinderFactory  参数解析
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // TODO 从TheadLocal中取出用户信息
        Map<String, String> map = ContextHandlerInterceptor.threadLocal.get();

        Long userId = Long.valueOf(map.get("userid"));
        String account = map.get("account");
        String name = map.get("name");
        Long orgId = Long.valueOf(map.get("orgid"));
        Long stationId = Long.valueOf(map.get("stationid"));

        // 根据@LoginUser注解 进行注入SysUser
        SysUser user = SysUser.builder()
                .id(userId)
                .account(account)
                .name(name)
                .orgId(orgId)
                .stationId(stationId)
                .build();

        try {
            LoginUser loginUser = methodParameter.getParameterAnnotation(LoginUser.class);
            boolean isFull = loginUser.isFull();

            if (isFull || loginUser.isStation() || loginUser.isOrg() || loginUser.isRoles()) {
                UserQuery userQuery = UserQuery.builder()
                        .full(isFull)
                        .org(loginUser.isOrg())
                        .station(loginUser.isStation())
                        .roles(loginUser.isRoles())
                        .build();

                // TODO 此处应为状态对象
                boolean byId = userResolveApi.getById(userId, userQuery);
                // TODO 判断处理是否成功
            }
        } catch (Exception ex) {
            log.warn("注入登录人信息时，发生异常，原因: {}", user, ex);
        }
        return user;
    }
}
