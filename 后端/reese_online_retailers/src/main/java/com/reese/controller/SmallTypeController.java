package com.reese.controller;

import com.reese.service.ISmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
* 商品小类Controller
* */
@RestController
@RequestMapping("/smallType")
public class SmallTypeController {

    @Autowired
    private ISmallTypeService smallTypeService;
    


}
