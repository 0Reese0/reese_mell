package com.reese.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reese.entity.OrderDetail;
import com.reese.mapper.OrderDetailMapper;
import com.reese.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 订单详情Service实现类
* */
@Service("orderDetailService")
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
}
