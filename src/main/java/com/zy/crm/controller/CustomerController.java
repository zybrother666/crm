package com.zy.crm.controller;

import com.zy.crm.base.BaseController;
import com.zy.crm.base.ResultInfo;
import com.zy.crm.query.CustomerQuery;
import com.zy.crm.service.CustomerService;
import com.zy.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    /**
     * 分页条件查询客户列表
     * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 进入客户管理信息页面
     * */
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 添加客户
     * */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加客户信息成功！");
    }

    /**
     * 修改客户
     * */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("修改客户信息成功！");
    }

    /**
     * 进入添加或修改客户的页面
     * */
    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, HttpServletRequest request){

        //如果id不为空，则查询客户记录
        if(null != id){
            //通过id查询客户记录
            Customer customer = customerService.selectByPrimaryKey(id);
            //将客户记录存到作用域中
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    /**
     * 删除客户信息
     * */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("删除客户信息成功！");
    }

    /**
     * 打开客户的订单页面
     * */
    @RequestMapping("orderInfoPage")
    public String orderInfoPage(Integer customerId, Model model){
        //通过客户ID查询客户记录，设置到请求域中
        model.addAttribute("customer",customerService.selectByPrimaryKey(customerId));
        return "customer/customer_order";
    }
}
