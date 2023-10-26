package com.reese.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.Order;
import com.reese.entity.OrderDetail;
import com.reese.entity.PageBean;
import com.reese.entity.R;
import com.reese.service.IOrderDetailService;
import com.reese.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Admin 订单Controller
* */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @RequestMapping("list")
    public R list(@RequestBody PageBean pageBean){
        Map<String,Object> map=new HashMap<>();
        map.put("orderNo",pageBean.getQuery().trim());
        map.put("start",pageBean.getStart());
        map.put("pageSize",pageBean.getPageSize());
        List<Order> orderList=orderService.list(map);
        Long total=orderService.getTotal(map);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("orderList",orderList);
        resultMap.put("total",total);
        return R.ok(resultMap);
    }

    @PostMapping("/updateStatus")
    public R updateStatus(@RequestBody Order order){
        Order resultOrder=orderService.getById(order.getId());
        resultOrder.setStatus(order.getStatus());
        orderService.saveOrUpdate(resultOrder);
        return R.ok();
    }

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id")Integer id){
        orderDetailService.remove(new QueryWrapper<OrderDetail>().eq("mId",id));
        orderService.removeById(id);
        return R.ok();
    }


}
