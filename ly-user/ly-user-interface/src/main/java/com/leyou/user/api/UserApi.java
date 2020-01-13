package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/10 0010 - 下午 15:15
 */
public interface UserApi {

    @GetMapping("/query")
    public User queryUserByUserNameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );
}
