package com.reese.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reese.entity.WxUserInfo;

/**
 * 微信用户Mapper接口
 */
public interface WxUserInfoMapper extends BaseMapper<WxUserInfo> {

    public WxUserInfo findByOpenId(String openId);

}
