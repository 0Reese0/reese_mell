package com.reese.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reese.entity.Admin;
import com.reese.mapper.AdminMapper;
import com.reese.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 管理员用户Service实现类
* */
@Service("adminService")
public class IAdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
}
