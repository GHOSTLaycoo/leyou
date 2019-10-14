package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/27 0027 - 下午 13:42
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi{
}
