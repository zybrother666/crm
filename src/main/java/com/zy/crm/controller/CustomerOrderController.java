package com.zy.crm.controller;

import com.zy.crm.base.BaseController;
import com.zy.crm.query.CustomerOrderQuery;
import com.zy.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping("order")
@Controller
public class CustomerOrderController extends BaseController {

    @Resource
    CustomerOrderService customerOrderService;

    /**
     * 多条件分页查询
     * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
    }

    /**
     * 打开订单详情的页面
     * */
    @RequestMapping("orderDetailPage")
    public String orderDetailPage(Integer orderId, Model model){

        //通过订单ID查询对应的订单记录
        Map<String,Object> map = customerOrderService.queryOrderById(orderId);
        //将数据设置到请求域中
        model.addAttribute("order",map);

        return "customer/customer_order_detail";
    }
}
