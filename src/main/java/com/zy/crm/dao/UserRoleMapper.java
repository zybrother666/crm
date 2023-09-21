package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //根据用户ID查询用户记录
    Integer countUserRoleByUserId(Integer userId);

    //根据用户ID删除用户记录
    Integer deleteUserRoleByUserId(Integer userId);

}