package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/10 0010 - 下午 15:17
 */

@FeignClient("user-service")
public interface UserClient extends UserApi{
}
