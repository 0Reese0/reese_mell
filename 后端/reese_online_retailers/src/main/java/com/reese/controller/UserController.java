package com.reese.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.constant.SystemConstant;
import com.reese.entity.R;
import com.reese.properties.WeixinProperties;
import com.reese.entity.WxUserInfo;
import com.reese.service.IWxUserInfoService;
import com.reese.util.HttpClientUtil;
import com.reese.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 微信用户Controller
 * */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    //微信用户登录
    @RequestMapping("/wxlogin")
    public R wxLogin(@RequestBody WxUserInfo wxUserInfo){
//        System.out.println(wxUserInfo.getCode());
        String jscode2sessionUrl=weixinProperties.getJscode2sessionUrl()+
                "?appid="+weixinProperties.getAppid()+
                "&secret="+weixinProperties.getSecret()+
                "&js_code="+wxUserInfo.getCode()+
                "&grant_type=authorization_code";
//        System.out.println(jscode2session);
        String result=httpClientUtil.sendHttpGet(jscode2sessionUrl);
//        System.out.println(result);
        JSONObject jsonObject= JSON.parseObject(result);
        String openid=jsonObject.get("openid").toString();
//        System.out.println(openid);
        //插入或更新用户到数据库
        WxUserInfo  resultWxUserInfo=wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid",openid));
        if(resultWxUserInfo==null){
            //不存在 插入
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setLastLoginDate(new Date());
            wxUserInfo.setRegisterDate(new Date());
            wxUserInfoService.save(wxUserInfo);
        }else{
            //存在 更新
            resultWxUserInfo.setNickName(wxUserInfo.getNickName());
            resultWxUserInfo.setAvatarUrl(wxUserInfo.getAvatarUrl());
            resultWxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.updateById(resultWxUserInfo);
        }

        //利用jwt生成token返回到前端

        String token= JwtUtils.createJWT(openid,wxUserInfo.getNickName(), SystemConstant.JWT_TTL);
        Map<String,Object> map=new HashMap<>();
        map.put("token",token);

        return R.ok(map);

    }

}
