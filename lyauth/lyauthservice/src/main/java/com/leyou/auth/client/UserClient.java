package com.leyou.auth.client;

import com.leyou.item.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: oyyb
 * @data: 2020-04-11
 * @version: 1.0.0
 * @descript:
 */
@FeignClient
public interface UserClient extends UserApi {
}
