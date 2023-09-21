package com.zy.crm.controller;

import com.zy.crm.base.BaseController;
import com.zy.crm.base.ResultInfo;
import com.zy.crm.model.TreeModel;
import com.zy.crm.service.ModuleService;
import com.zy.crm.service.RoleService;
import com.zy.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("module")
@Controller
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的资源列表
     * */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return  moduleService.queryAllModules(roleId);
    }


    /**
     * 进入授权页面
     * */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        //将需要授权的角色ID设置到请求域中
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 查询资源列表
     * */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 进入资源管理页面
     * */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /*添加资源*/
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功！");
    }
    /*修改资源*/
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("更新资源成功！");
    }
    /*删除资源*/
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除资源成功！");
    }

    /**
     * 打开添加资源的页面
     */
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade, Integer parentId,HttpServletRequest request){
        //将数据设置到请求域中
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);

        return "module/add";
    }

    /**
     * 打开修改资源的页面
     * */
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model){

        //将要修改的资源对象设置到请求域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }
}
