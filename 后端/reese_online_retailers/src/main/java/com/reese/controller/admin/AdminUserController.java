package com.reese.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reese.entity.PageBean;
import com.reese.entity.R;
import com.reese.entity.WxUserInfo;
import com.reese.service.IWxUserInfoService;
import com.reese.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 *  Admin 用户Controller
 * */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean){
        String query=pageBean.getQuery().trim();
        Page<WxUserInfo> page=new Page<>(pageBean.getPageNum(),pageBean.getPageSize());
        Page<WxUserInfo> pageResult=wxUserInfoService.page(page,
                new QueryWrapper<WxUserInfo>().like(StringUtil.isNotEmpty(query),"nickName",query));
        Map<String,Object> map=new HashMap<>();
        map.put("userList",pageResult.getRecords());
        map.put("total",pageResult.getTotal());
        return R.ok(map);
    }
}
