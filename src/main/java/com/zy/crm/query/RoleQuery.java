package com.zy.crm.query;

import com.zy.crm.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    private String roleName;//角色名称

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
