package com.reese.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.Order;
import com.reese.entity.R;
import com.reese.service.IOrderService;
import com.reese.service.impl.IOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/main")
public class TestController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/test")
    public R test(){
        return R.ok("牛逼");
    }


    @GetMapping("/test1")
    public R test1(){
        String orderNo="Reese20221127041008000000586";
        Order order=orderService.getOne(new QueryWrapper<Order>().eq("orderNo",orderNo));
        Map map=new HashMap();
        map.put("order",order);
        return R.ok(map);
    }




}
