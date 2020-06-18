package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: oyyb
 * @data: 2020-04-02
 * @version: 1.0.0
 * @descript:
 */
public interface BrandApi {
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

    @GetMapping("brand/brands")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
}
