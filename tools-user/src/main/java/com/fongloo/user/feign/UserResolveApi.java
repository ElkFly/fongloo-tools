package com.fongloo.user.feign;

import com.fongloo.user.feign.fallback.UserResolveApiFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户操作API
 */
@FeignClient(name = "${feign.authrity-server:auth-server}", fallbackFactory = UserResolveApiFallBack.class)
public interface UserResolveApi {
    /**
     * 根据ID 查询用户详情
     */
    // TODO 返回对象应该是一个自定义状态码对象
    @PostMapping(value = "/user/anno/id/{id}")
    boolean getById(@PathVariable("id") Long id, @RequestBody UserQuery userQuery);
}
