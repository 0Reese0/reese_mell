package com.reese.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reese.entity.BigType;
import com.reese.entity.Product;
import com.reese.entity.ProductSwiperImage;
import com.reese.entity.R;
import com.reese.service.IProductService;
import com.reese.service.IProductSwiperImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 商品Controller
* */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductSwiperImageService productSwiperImageService;

    /*
    * 查询轮播图
    * */
    @GetMapping("/findSwiper")
    public R findSwiper(){
        List<Product> swiperProductList=productService.list(new QueryWrapper<Product>().eq("isSwiper",true).orderByAsc("swiperSort"));
        Map<String,Object> map=new HashMap<>();
        map.put("message",swiperProductList);
        return R.ok(map);
    }

    /*
     * 查询热门推荐商品8个
     * */
    @GetMapping("/findHot")
    public R findHot(){
        Page<Product> page=new Page<>(0,8);
        Page<Product> productPage=productService.page(page,new QueryWrapper<Product>().eq("isHot",true).orderByAsc("hotDateTime"));
        List<Product> hotProductList=productPage.getRecords();
        Map<String,Object> map=new HashMap<>();
        map.put("message",hotProductList);
        return R.ok(map);
    }

    /*
    * 根据Id查询商品信息
    * */
    @GetMapping("/detail")
    public R detail(Integer id){
        Product product=productService.getById(id);
        List<ProductSwiperImage> productSwiperImageList=productSwiperImageService.list(new QueryWrapper<ProductSwiperImage>().eq("productId",id).orderByAsc("sort"));
        product.setProductSwiperImageList(productSwiperImageList);
        Map<String,Object> map=new HashMap<>();
        map.put("message",product);
        return R.ok(map);
    }

    /*
    * 商品搜索
    * */
    @GetMapping("/search")
    public R search(String q){
        List<Product> productList=productService.list(new QueryWrapper<Product>().like("name",q));
        Map<String,Object> map=new HashMap<>();
        map.put("productList",productList);
        return R.ok(map);
    }
}
