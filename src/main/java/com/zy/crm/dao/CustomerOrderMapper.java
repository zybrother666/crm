package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    //通过订单ID查询订单记录
    Map<String, Object> queryOrderById(Integer orderId);
}