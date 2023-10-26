package com.reese.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reese.entity.ProductSwiperImage;
import com.reese.mapper.ProductSwiperImageMapper;
import com.reese.service.IProductSwiperImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 产品轮播图片Service实现类
* */
@Service("productSwiperImageService")
public class IProductSwiperImageServiceImpl extends ServiceImpl<ProductSwiperImageMapper, ProductSwiperImage> implements IProductSwiperImageService {

    @Autowired
    private ProductSwiperImageMapper productSwiperImageMapper;
}
