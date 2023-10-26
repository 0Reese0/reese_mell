package com.reese.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.OrderDetail;
import com.reese.entity.R;
import com.reese.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Admin 订单详情Controller
 * */
@RestController
@RequestMapping("/admin/orderDetail")
public class AdminOrderDetailController {

    @Autowired
    private IOrderDetailService orderDetailService;


    @GetMapping("/list/{id}")
    public R listByOrderId(@PathVariable(value = "id")Integer id){
        List<OrderDetail> orderDetailList= orderDetailService.list(new QueryWrapper<OrderDetail>().eq("mId",id));
        Map<String,Object> map=new HashMap<>();
        map.put("list",orderDetailList);
        return R.ok(map);
    }
}
