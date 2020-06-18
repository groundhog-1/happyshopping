package com.leyou.order.utils;

import com.github.wxpay.sdk.WXPay;
import static com.github.wxpay.sdk.WXPayConstants.*;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class PayHelper {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayConfig config;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatusMapper statusMapper;

    @Autowired
    private OrderMapper orderMapper;

    public PayHelper(PayConfig payConfig) {
        // 真实开发时
        wxPay = new WXPay(payConfig);
        // 测试时
        // wxPay = new WXPay(payConfig, WXPayConstants.SignType.MD5, true);
    }

    public String createPayUrl(Long orderId,Long totalPay,String desc) {
        String key = "ly.pay.url." + orderId;
        try {
            String url = this.redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(url)) {
                return url;
            }
        } catch (Exception e) {
            log.error("查询缓存付款链接异常,订单编号：{}", orderId, e);
        }

        try {
            Map<String, String> data = new HashMap<>();
            // 商品描述
            data.put("body", desc);
            // 订单号
            data.put("out_trade_no", orderId.toString());
            //货币
            data.put("fee_type", "CNY");
            //金额，单位是分
            data.put("total_fee", totalPay.toString());
            //调用微信支付的终端IP（estore商城的IP）
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", config.getNotifyUrl());
            // 交易类型为扫码支付
            data.put("trade_type", "NATIVE");
            //商品id,使用假数据
            //data.put("product_id", "1234567");

            //利用wxPay，完成下单
            Map<String, String> result = this.wxPay.unifiedOrder(data);


            isSuccess(result);


            String url = result.get("code_url");
            return url;
        } catch (Exception e) {
            log.error("创建预交易订单异常", e);
            return null;
        }
    }

    public void isSuccess(Map<String, String> result) {
        //Long orderId = ; String key = ;
        //判断通信提示
        String returnCode = result.get("return_code");
        if (FAIL.equals(returnCode)){
            //                //通信失败
            log.error("[微信下单] 微信下单通信失败，失败原因：{}",result.get("return_msg"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);

        }
        //判断业务提示
        String resultCode = result.get("result_code");

        if (FAIL.equals(resultCode)) {
            String url = result.get("code_url");
            // 将付款地址缓存，时间为10分钟
            /*try {
                this.redisTemplate.opsForValue().set(key, url, 10, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("缓存付款链接异常,订单编号：{}", orderId, e);
            }*/
            log.error("创建预交易订单失败，错误信息：{}", result.get("return_msg"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);

        }
    }

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    public PayState queryOrder(Long orderId) {
        Map<String, String> data = new HashMap<>();
        // 订单号
        data.put("out_trade_no", orderId.toString());
        try {
            Map<String, String> result = this.wxPay.orderQuery(data);
            if (result == null) {
                // 未查询到结果，认为是未付款
                return PayState.NOT_PAY;
            }
            String state = result.get("trade_state");
            if ("SUCCESS".equals(state)) {
                // success，则认为付款成功

                // 修改订单状态
                //this.orderService.updateStatus(orderId, 2);
                return PayState.SUCCESS;
            } else if (StringUtils.equals("USERPAYING", state) || StringUtils.equals("NOTPAY", state)) {
                // 未付款或正在付款，都认为是未付款
                return PayState.NOT_PAY;
            } else {
                // 其它状态认为是付款失败
                return PayState.FAIL;
            }
        } catch (Exception e) {
            log.error("查询订单状态异常", e);
            return PayState.NOT_PAY;
        }
    }

    public void isValidSign(Map<String,String> data){
        //重新生成签名
        try {
            String sign1 = WXPayUtil.generateSignature(data, config.getKey(),
                    SignType.HMACSHA256);
            String sign2 = WXPayUtil.generateSignature(data, config.getKey(),
                    SignType.MD5);

            //和传过来的签名进行比较
            String sign = data.get("sign");
            if (!StringUtils.equals(sign,sign1) && !StringUtils.equals(sign,sign2)){
                throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
            }

        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
        }
    }

    /**
     *SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（付款码支付）
     * USERPAYING--用户支付中（付款码支付）
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    public PayState queryPayState(Long orderId) {
        try{
            //组织请求参数
            Map<String,String> data = new HashMap<>();
            //订单号
            data.put("out_trade_no",orderId.toString());
            //查询状态
            Map<String, String> result = wxPay.orderQuery(data);
            //检验状态
            isSuccess(result);
            //校验签名
            isValidSign(result);
            //检验金额
            //3.校验金额
            String totalFeeStr = result.get("total_fee");
            String tradeNo = result.get("out_trade_no");

            if (org.apache.commons.lang.StringUtils.isEmpty(totalFeeStr)){
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }

            //3.1获取结果中的金额
            Long totalFee = Long.valueOf(totalFeeStr);
            //3.2获取订单金额

            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (totalFee != /*order.getActualPay()*/ 1){
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }

            String state = result.get("trade_state");
            if (SUCCESS.equals(state)){
                //支付成功
                //4.修改订单状态
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setStatus(OrderStatusEnum.PAYED.value());
                orderStatus.setOrderId(orderId);
                orderStatus.setPaymentTime(new Date());
                int count = statusMapper.updateByPrimaryKeySelective(orderStatus);
                if (count != 1){
                    throw new LyException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
                }
                //返回成功
                return PayState.SUCCESS;
            }

            if ("NOTPAY".equals(state) || "USERPAYING".equals(state)){
                return PayState.NOT_PAY;
            }

            return PayState.FAIL;
            
        }catch (Exception e){
            return PayState.NOT_PAY;
        }

    }
}
