package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: oyyb
 * @data: 2020-04-02
 * @version: 1.0.0
 * @descript:
 */
@FeignClient(value = "item-service")
public interface BrandCliecnt extends BrandApi {
}
