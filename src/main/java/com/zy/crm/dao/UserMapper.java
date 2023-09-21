package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    //通过用户名查询用户记录，返回用户对象
    public User queryUserByName(String userName);
    public List<Map<String,Object>> queryAllSales();
}