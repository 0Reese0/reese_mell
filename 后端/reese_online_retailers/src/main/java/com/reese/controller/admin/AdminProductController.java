package com.reese.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.*;
import com.reese.service.IProductService;
import com.reese.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Admin 商品Controller
 * */
@RestController
@RequestMapping("/admin/product")
public class  AdminProductController {

    @Autowired
    private IProductService productService;

    @Value("${productImagesFilePath}")
    private String productImagesFilePath;

    @Value("${swiperImagesFilePath}")
    private String swiperImagesFilePath;


    @RequestMapping("list")
    public R list(@RequestBody PageBean pageBean){
        Map<String,Object> map=new HashMap<>();
        map.put("name",pageBean.getQuery().trim());
        map.put("start",pageBean.getStart());
        map.put("pageSize",pageBean.getPageSize());
        List<Product> productList=productService.list(map);
        Long total=productService.getTotal(map);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("productList",productList);
        resultMap.put("total",total);
        return R.ok(resultMap);
    }

    @GetMapping("/updateHot/{id}/state/{hot}")
    public R updateHot(@PathVariable(value = "id")Integer id,@PathVariable(value = "hot")boolean hot){
        Product product=productService.getById(id);
        product.setHot(hot);
        if(hot){
            product.setHotDateTime(new Date());
        }else {
            product.setHotDateTime(null);
        }
        productService.saveOrUpdate(product);
        return R.ok();
    }

    @GetMapping("/updateSwiper/{id}/state/{swiper}")
    public R updateSwiper(@PathVariable(value = "id")Integer id,@PathVariable(value = "swiper")boolean swiper){
        Product product=productService.getById(id);
        product.setSwiper(swiper);
        productService.saveOrUpdate(product);
        return R.ok();
    }

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id")Integer id){
        productService.removeById(id);
        return R.ok();
    }

    @RequestMapping("/uploadImage")
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            //获取文件名
            String originalFilename=file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(productImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/product/"+newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }

    @RequestMapping("/save")
    public R save(@RequestBody Product product){
        if (product.getId()==null || product.getId()==-1){
            productService.add(product);
        }else {
            productService.update(product);
        }
        return R.ok();
    }


    @RequestMapping("/uploadSwiperImage")
    public Map<String,Object> uploadSwiperImage(MultipartFile file)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            //获取文件名
            String originalFilename=file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(swiperImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/swiper/"+newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }

    @RequestMapping("/{id}")
    public R findById(@PathVariable(value = "id")Integer id){
        Product product=productService.findById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("product",product);
        return R.ok(map);
    }

}
