package com.fongloo.user.feign.fallback;

import com.fongloo.user.feign.UserQuery;
import com.fongloo.user.feign.UserResolveApi;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户API熔断
 * 如果远程RPC调用失败，则
 */
@Slf4j
public class UserResolveApiFallBack implements FallbackFactory<UserResolveApi> {
    @Override
    public UserResolveApi create(Throwable throwable) {
        return new UserResolveApi() {
            @Override
            public boolean getById(Long id, UserQuery userQuery) {
                log.error("通过用户名查询用户异常: {}", id, throwable);
                // TODO 这里应该返回一个状态码对象
                return false;
            }
        };
    }
}
