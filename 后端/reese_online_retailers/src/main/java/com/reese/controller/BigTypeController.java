package com.reese.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.BigType;
import com.reese.entity.Product;
import com.reese.entity.R;
import com.reese.entity.SmallType;
import com.reese.service.IBigTypeService;
import com.reese.service.IProductService;
import com.reese.service.ISmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 商品大类Controller
* */
@RestController
@RequestMapping("/bigType")
public class BigTypeController {

    @Autowired
    private IBigTypeService bigTypeService;

    @Autowired
    private ISmallTypeService smallTypeService;

    @Autowired
    private IProductService productService;

    /*
    * 查询所有商品大类
    * */
    @GetMapping("/findAll")
    public R findAll(){
        List<BigType> bigTypeList=bigTypeService.list();
        Map<String,Object> map=new HashMap<>();
        map.put("message",bigTypeList);
        return R.ok(map);
    }

    /*
    * 获取所有菜单信息
    * */
    @GetMapping("/findCategories")
    public R findCategories(){
        List<BigType> bigTypeList=bigTypeService.list();
        for(BigType bigType:bigTypeList){
            List<SmallType> smallTypeList=smallTypeService.list(new QueryWrapper<SmallType>().eq("bigTypeId",bigType.getId()));
            bigType.setSmallTypeList(smallTypeList);
            for(SmallType smallType:smallTypeList){
                List<Product> productList=productService.list(new QueryWrapper<Product>().eq("typeId",smallType.getId()));
                smallType.setProductList(productList);
            }

        }
        Map<String,Object> map=new HashMap<>();
        map.put("message",bigTypeList);
        return R.ok(map);
    }

}
