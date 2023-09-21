package com.zy.crm.service;

import com.zy.crm.base.BaseService;
import com.zy.crm.dao.UserRoleMapper;
import com.zy.crm.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer>{
    @Resource
    private UserRoleMapper userRoleMapper;
}
