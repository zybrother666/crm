package com.zy.crm.dao;

import com.zy.crm.base.BaseMapper;
import com.zy.crm.model.TreeModel;
import com.zy.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    //查询所有的资源列表
    public List<TreeModel> queryAllModules();

    //查询所有的资源数据
    public List<Module> queryModuleList();

    //通过层级与模块名查询资源对象
    Module queryModuleByGradeAndModuleName(Integer grade, String moduleName);

    //通过层级与URL查询资源对象
    Module queryModuleByGradeAndUrl(Integer grade, String url);

    //通过权限码查询资源对象
    Module queryModuleByOptValue(String optValue);

    //查询指定资源是否存在子记录
    Integer queryModuleByParentId(Integer id);
}