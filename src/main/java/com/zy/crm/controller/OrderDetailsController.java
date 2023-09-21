package com.zy.crm.controller;

import com.zy.crm.base.BaseController;
import com.zy.crm.query.OrderDetailsQuery;
import com.zy.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;
@RequestMapping("order_details")
@Controller
public class OrderDetailsController extends BaseController {
    @Resource
    private OrderDetailsService orderDetailsService;

    /**
     * 分页查询订单详情列表
     * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery){
        return orderDetailsService.queryOrderDetailsByParams(orderDetailsQuery);
    }
}
