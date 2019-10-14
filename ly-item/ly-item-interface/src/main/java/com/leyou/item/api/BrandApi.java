package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/27 0027 - 下午 13:37
 */
public interface BrandApi {

    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

    @GetMapping("brand/list")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
}
