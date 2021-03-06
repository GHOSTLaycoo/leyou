package com.leyou.order.service;

import com.leyou.common.dto.CartDTO;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.IdWorker;
import com.leyou.item.pojo.Sku;
import com.leyou.order.client.AddressClient;
import com.leyou.order.client.GoodsClient;
import com.leyou.order.dto.AddressDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.enums.PayState;
import com.leyou.order.interceptor.UserInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.util.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/14 0014 - 下午 17:15
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PayHelper payHelper;

    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        //新增订单
        Order order = new Order();
        //订单编号,基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());
        //用户信息
        UserInfo user = UserInterceptor.getUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        //收货人地址
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverZip(addr.getZipCode());
        order.setReceiverState(addr.getState());

        //金额
        Map<Long, Integer> numMap = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId,CartDTO::getNum));

        Set<Long> ids = numMap.keySet();
        List<Sku> skus = goodsClient.querySkuByIds(new ArrayList<>(ids));

        List<OrderDetail> details = new ArrayList<>();
        Long totalPay = 0L;

        for (Sku sku : skus) {
            totalPay += sku.getPrice() * numMap.get(sku.getId());
            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(),"."));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOrderId(orderId);
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSkuId(sku.getId());
            detail.setTitle(sku.getTitle());
            details.add(detail);
        }
        order.setTotalPay(totalPay);
        order.setActualPay(totalPay + order.getPostFee() - 0);
        int count = orderMapper.insertSelective(order);
        if(count!=1){
            log.error("[创建订单] 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnums.CREATE_ORDER_ERROR);
        }

        //新增订单详情
        count = detailMapper.insertList(details);
        if(count!=details.size()){
            log.error("[创建订单] 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnums.CREATE_ORDER_ERROR);
        }
        //新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = statusMapper.insertSelective(orderStatus);
        if(count!=1){
            log.error("[创建订单] 创建订单失败,orderId:{}",orderId);
            throw new LyException(ExceptionEnums.CREATE_ORDER_ERROR);
        }
        //减库存
        List<CartDTO> cartDTOS = orderDTO.getCarts();
        goodsClient.decreseStock(cartDTOS);
        return orderId;
    }

    public Order queryOrderById(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if(order==null){
            throw new LyException(ExceptionEnums.ORDER_NOT_FOUND);
        }

        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> select = detailMapper.select(detail);
        if(CollectionUtils.isEmpty(select)){
            throw new LyException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        order.setOrderDetails(select);
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(id);
        if(orderStatus==null){
            throw new LyException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    public String createPayUrl(Long orderId) {
        //查询订单
        Order order = queryOrderById(orderId);
        Integer status = order.getOrderStatus().getStatus();
        if(status != OrderStatusEnum.UN_PAY.value()){
            throw new LyException(ExceptionEnums.ORDER_STATUS_ERROR);
        }
        //支付金额
        Long actualPay = /*order.getActualPay()*/ 1L;
        //商品描述
        OrderDetail detail = order.getOrderDetails().get(0);
        String desc = detail.getTitle();
        return payHelper.createOrder(orderId,actualPay,desc);
    }

    public void handleNotify(Map<String, String> result) {
        //数据校验
        payHelper.isSuccess(result);

        //校验签名
        payHelper.isValidSign(result);

        //校验金额
        String totalFee = result.get("total_fee");
        String tradeNo = result.get("out_trade_no");
        if(StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(tradeNo)){
            throw new LyException(ExceptionEnums.INVALID_ORDER_PARAM);
        }
        Long total = Long.valueOf(totalFee);
        Long orderId = Long.valueOf(tradeNo);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(total!=/*order.getActualPay()*/1){
            throw new LyException(ExceptionEnums.INVALID_ORDER_PARAM);
        }

        //修改订单状态
        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusEnum.PAYED.value());
        status.setOrderId(orderId);
        status.setPaymentTime(new Date());

        int count = statusMapper.updateByPrimaryKeySelective(status);
        if(count!=1){
            throw new LyException(ExceptionEnums.UPDATE_ORDER_STATUS_ERROR);
        }

        log.info("[订单回调],订单支付成功!订单编号:{}",orderId);
    }

    public PayState queryOrderState(Long orderId) {
        //查询订单状态
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        //判断是否支付
        if(status!=OrderStatusEnum.UN_PAY.value()){
            //如果已支付,真的是已支付
            return PayState.SUCCESS;
        }

        //如果未支付,但其实不一定是未支付,必须去微信查询支付状态
        return payHelper.queryPayState(orderId);
    }
}
