package com.reese.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reese.entity.WxUserInfo;
import com.reese.mapper.WxUserInfoMapper;
import com.reese.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 微信用户Service实现类
* */
@Service("wxUserInfoService")
public class IWxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper, WxUserInfo> implements IWxUserInfoService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;
}
