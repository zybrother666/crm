package com.zy.crm.controller;

import com.zy.crm.base.BaseController;
import com.zy.crm.base.ResultInfo;
import com.zy.crm.enums.StateStatus;
import com.zy.crm.query.CusDevPlanQuery;
import com.zy.crm.query.SaleChanceQuery;
import com.zy.crm.service.CusDevPlanService;
import com.zy.crm.service.SaleChanceService;
import com.zy.crm.utils.LoginUserUtil;
import com.zy.crm.vo.CusDevPlan;
import com.zy.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;
    /*
    *进入客户开发计划页面
    * */
    @RequestMapping("index")
    public String index(){

        return "cusDevPlan/cus_dev_plan";
    }

    /*
    * 打开计划项开发与详情页面
    * */
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域中
        request.setAttribute("saleChance",saleChance);

        return "cusDevPlan/cus_dev_plan_data";
    }

    /*
     * 客户开发计划数据查询(分页多条件查询)
     *
     * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /*
    * 添加计划项
    * */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功！");
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项删除成功！");
    }

    /*
    * 进入添加或修改页面
    * */
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(HttpServletRequest request,Integer sId,Integer id){
        //将营销机会ID设置到请求域中，给计划项页面获取
        request.setAttribute("sId",sId);

        //通过计划项ID查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

}
