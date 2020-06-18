package com.leyou.order.web;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
@RestController
@RequestMapping("order")
@Api("订单服务接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ApiOperation(value = "创建订单接口，返回订单号",notes = "创建订单")
    @ApiImplicitParam(name = "order",required = true,value = "订单的json对象，包含订单条目和物流信息")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
        //创建订单
        return ResponseEntity.ok(orderService.createOrder(orderDTO));

    }

    @GetMapping("{id}")
    @ApiOperation(value = "创建订单接口，返回订单号",notes = "创建订单")
    @ApiImplicitParam(name = "order",required = true,value = "订单的json对象，包含订单条目和物流信息")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id")Long id){
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<String> createUrl(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.createPayUrl(orderId));
    }

    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.queryOrderState(orderId).getValue());
    }
}
