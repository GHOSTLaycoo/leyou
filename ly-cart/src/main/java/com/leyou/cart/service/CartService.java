package com.leyou.cart.service;

import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/14 0014 - 下午 13:37
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";

    public void addCart(Cart cart) {
        //获取当前用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        String hashKey = cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //判断当前购物车商品是否存在
        if(operation.hasKey(hashKey)){
            String json = operation.get(hashKey).toString();
            Cart cacheCart = JsonUtils.parse(json, Cart.class);
            cacheCart.setNum(cacheCart.getNum()+cart.getNum());
            operation.put(hashKey,JsonUtils.serialize(cacheCart));
        }else {
            operation.put(hashKey,JsonUtils.serialize(cart));
        }
    }

    public List<Cart> querCartList() {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX +user.getId();
        if(!redisTemplate.hasKey(key)){
            throw new LyException(ExceptionEnums.CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        List<Cart> carts = operation.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());

        return carts;
    }

    public void updateNum(Long skuId, Integer num) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        if(!operations.hasKey(skuId.toString())){
            throw new LyException(ExceptionEnums.CART_NOT_FOUND);
        }
        Cart cart = JsonUtils.parse(operations.get(skuId.toString()).toString(), Cart.class);
        cart.setNum(num);

        //写回redis
        operations.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        redisTemplate.opsForHash().delete(key,skuId.toString());
    }
}
