package com.reese.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reese.entity.BigType;

/**
 * 商品大类Mapper接口
 */
public interface BigTypeMapper extends BaseMapper<BigType> {

    public BigType findById(Integer id);

}
