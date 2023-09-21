package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.Customer;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //通过客户名称查询客户对象
    Customer queryCustomerByName(String name);
}