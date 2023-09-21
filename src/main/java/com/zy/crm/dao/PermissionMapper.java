package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    Integer countPermissionByRoleId(Integer roleId);

    void deletePermissionByRoleId(Integer roleId);

    //查询角色拥有的所有资源的资源ID
    List<Integer> queryRoleHasModuleByRoleId(Integer roleId);

    //通过用户ID查询对应的资源列表（资源权限码）
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    //查询资源
    Integer countPermissionByModuleId(Integer id);

    //通过资源ID删除权限记录
    Integer deletePermissionByModuleId(Integer id);
}

