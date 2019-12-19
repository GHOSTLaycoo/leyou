package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/27 0027 - 下午 13:05
 */

@FeignClient("item-service")
public interface CategoryClient extends CategoryApi{

}
