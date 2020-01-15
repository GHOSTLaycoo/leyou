package com.leyou.order.util;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.enums.PayState;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class PayHelper {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayConfig config;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    public String createOrder(Long orderId,Long totalPay,String desc){
        try{
            Map<String,String> data = new HashMap<>();
            data.put("body",desc);
            data.put("out_trade_no",orderId.toString());
            data.put("total_fee",totalPay.toString());
            data.put("spbill_create_ip","127.0.0.1");
            data.put("notify_url",config.getNotifyUrl());
            data.put("trade_type","NATIVE");

            Map<String,String> result = wxPay.unifiedOrder(data);

            isSuccess(result);

            String url = result.get("code_url");
            return url;
        } catch (Exception e) {
            log.error("[微信下单]创建预交易订单异常失效",e);
            return null;
        }
    }

    public void isSuccess(Map<String,String> result){
        //判断通信标识
        String returnCode = result.get("return_code");
        if(WXPayConstants.FAIL.equals(returnCode)){
            log.error("[微信下单] 微信下单通信失败,原因:{}",result.get("return_msg"));
            throw new LyException(ExceptionEnums.WX_PAY_ORDER_FAIL);
        }

        //判断业务标识
        String resultCode = result.get("result_code");
        if(WXPayConstants.FAIL.equals(resultCode)){
            log.error("[微信下单] 微信下单业务失败,原因:{}",result.get("err_code_des"));
            throw new LyException(ExceptionEnums.WX_PAY_ORDER_FAIL);
        }
    }

    public void isValidSign(Map<String, String> data) {
        //重新生成签名
        try {
            String sign1 = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.HMACSHA256);
            String sign2 = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5);

            String sign = data.get("sign");
            if(!StringUtils.equals(sign,sign1)&&!StringUtils.equals(sign,sign1)){
                throw new LyException(ExceptionEnums.INVALID_SIGN_ERROR);
            }
        } catch (Exception e) {
            throw new LyException(ExceptionEnums.INVALID_SIGN_ERROR);
        }
        //和原来做对比

    }

    public PayState queryPayState(Long orderId) {
        try{
            //组织请求参数
            Map<String,String> data = new HashMap<>();
            //订单号
            data.put("out_trade_no",orderId.toString());
            //查询状态
            Map<String,String> result = wxPay.orderQuery(data);

            //校验状态
            isSuccess(result);

            //校验签名
            isValidSign(result);

            //校验金额
            String totalFee = result.get("total_fee");
            String tradeNo = result.get("out_trade_no");
            if(StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(tradeNo)){
                throw new LyException(ExceptionEnums.INVALID_ORDER_PARAM);
            }
            Long total = Long.valueOf(totalFee);
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if(total!=/*order.getActualPay()*/1){
                throw new LyException(ExceptionEnums.INVALID_ORDER_PARAM);
            }

            String state = result.get("trade_state");
            if(WXPayConstants.SUCCESS.equals(state)){
                //修改订单状态
                OrderStatus status = new OrderStatus();
                status.setStatus(OrderStatusEnum.PAYED.value());
                status.setOrderId(orderId);
                status.setPaymentTime(new Date());

                int count = statusMapper.updateByPrimaryKeySelective(status);
                if(count!=1){
                    throw new LyException(ExceptionEnums.UPDATE_ORDER_STATUS_ERROR);
                }

                return PayState.SUCCESS;
            }

            if("NOTPAY".equals(state)||"USERPAYING".equals(state)){
                return PayState.NOT_PAY;
            }

            return PayState.FAIL;
        }catch (Exception e){
            return PayState.NOT_PAY;
        }
    }
}