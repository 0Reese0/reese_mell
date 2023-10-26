package com.reese.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reese.entity.BigType;
import com.reese.entity.SmallType;
import com.reese.entity.PageBean;
import com.reese.entity.R;

import com.reese.service.IBigTypeService;
import com.reese.service.ISmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Admin 商品小类Controller
* */
@RestController
@RequestMapping("/admin/smallType")
public class AdminSmallTypeController {

    @Autowired
    private ISmallTypeService smallTypeService;

    @Autowired
    private IBigTypeService bigTypeService;


    @RequestMapping("list")
    public R list(@RequestBody PageBean pageBean){
        Map<String,Object> map=new HashMap<>();
        map.put("name",pageBean.getQuery().trim());
        map.put("start",pageBean.getStart());
        map.put("pageSize",pageBean.getPageSize());
        List<SmallType> smallTypeList=smallTypeService.list(map);
        Long total=smallTypeService.getTotal(map);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("smallTypeList",smallTypeList);
        resultMap.put("total",total);
        return R.ok(resultMap);
    }
    

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id")Integer id){
        smallTypeService.removeById(id);
        return R.ok();
    }

    @RequestMapping("/save")
    public R save(@RequestBody SmallType smallType){
        smallType.setBigTypeId(smallType.getBigType().getId());
        if (smallType.getId()==null || smallType.getId()==-1){
            smallTypeService.save(smallType);
        }else {
            smallTypeService.saveOrUpdate(smallType);
        }
        return R.ok();
    }

    @RequestMapping("/{id}")
    public R findById(@PathVariable(value = "id")Integer id){
        SmallType smallType=smallTypeService.getById(id);
        smallType.setBigType(bigTypeService.getById(smallType.getBigTypeId()));
        Map<String,Object> map=new HashMap<>();
        map.put("smallType",smallType);
        return R.ok(map);
    }


    @GetMapping("/listAll/{bigTypeId}")
    public R listAll(@PathVariable(value = "bigTypeId")Integer bigTypeId){
        Map<String,Object> map=new HashMap<>();
        map.put("smallTypeList",smallTypeService.list(new QueryWrapper<SmallType>().eq("bigTypeId",bigTypeId)));
        return R.ok(map);
    }
}
