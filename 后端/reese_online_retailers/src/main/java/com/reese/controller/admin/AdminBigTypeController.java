package com.reese.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reese.entity.PageBean;
import com.reese.entity.R;
import com.reese.entity.BigType;
import com.reese.entity.SmallType;
import com.reese.service.IBigTypeService;
import com.reese.service.ISmallTypeService;
import com.reese.util.DateUtil;
import com.reese.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
 *  Admin 商品大类Controller
 * */
@RestController
@RequestMapping("/admin/bigType")
public class AdminBigTypeController {

    @Autowired
    private IBigTypeService bigTypeService;

    @Autowired
    private ISmallTypeService smallTypeService;

    @Value("${bigTypeImagesFilePath}")
    private String bigTypeImagesFilePath;

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean){
        String query=pageBean.getQuery().trim();
        Page<BigType> page=new Page<>(pageBean.getPageNum(),pageBean.getPageSize());
        Page<BigType> pageResult=bigTypeService.page(page,
                new QueryWrapper<BigType>().like(StringUtil.isNotEmpty(query),"name",query));
        Map<String,Object> map=new HashMap<>();
        map.put("bigTypeList",pageResult.getRecords());
        map.put("total",pageResult.getTotal());
        return R.ok(map);
    }

    @RequestMapping("/save")
    public R save(@RequestBody BigType bigType){
        if (bigType.getId()==null || bigType.getId()==-1){
            bigTypeService.save(bigType);
        }else {
            bigTypeService.saveOrUpdate(bigType);
        }
        return R.ok();
    }

    @RequestMapping("/{id}")
    public R findById(@PathVariable(value = "id")Integer id){
        BigType bigType=bigTypeService.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("bigType",bigType);
        return R.ok(map);
    }

    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id")Integer id){
        if(smallTypeService.count(new QueryWrapper<SmallType>().eq("bigTypeId",id))>0){
            return R.error(500,"大类下面有小类，不能删除！");
        }else {
            bigTypeService.removeById(id);
            return R.ok();
        }
    }

    @RequestMapping("/uploadImage")
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            //获取文件名
            String originalFilename=file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(bigTypeImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","/image/bigType/"+newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }

    @RequestMapping("/listAll")
    public R listAll(){
        Map<String,Object> map=new HashMap<>();
        map.put("bigTypeList",bigTypeService.list());
        return R.ok(map);
    }

}
