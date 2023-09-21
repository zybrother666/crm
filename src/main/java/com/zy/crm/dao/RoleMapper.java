package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有的角色列表
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    //通过角色名查询角色记录
    public Role selectByRoleName(String roleName);
}