package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: oyyb
 * @data: 2020-04-01
 * @version: 1.0.0
 * @descript:
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
