package com.reese.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reese.entity.*;
import com.reese.service.IBigTypeService;
import com.reese.service.IProductSwiperImageService;
import com.reese.service.ISmallTypeService;
import com.reese.util.DateUtil;
import com.reese.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Admin 商品轮播图Controller
 * */
@RestController
@RequestMapping("/admin/productSwiperImage")
public class AdminProductSwiperImageController {

    @Autowired
    private IProductSwiperImageService productSwiperImageService;

    @Value("${productSwiperImagesFilePath}")
    private String productSwiperImagesFilePath;


    @GetMapping("/list/{id}")
    public R list(@PathVariable(value = "id")Integer id){
        List<ProductSwiperImage> list=productSwiperImageService.list(new QueryWrapper<ProductSwiperImage>().eq("productId",id));
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("list",list);
        return R.ok(resultMap);
    }

    @RequestMapping("/add")
    public R add(@RequestBody ProductSwiperImage productSwiperImage){
        productSwiperImageService.saveOrUpdate(productSwiperImage);
        return R.ok();
    }

    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id")Integer id){
        productSwiperImageService.removeById(id);
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
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(productSwiperImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/productSwiper/"+newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }


}
