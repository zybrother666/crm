package com.zy.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zy.crm.base.BaseService;
import com.zy.crm.dao.CustomerMapper;
import com.zy.crm.query.CustomerQuery;
import com.zy.crm.query.SaleChanceQuery;
import com.zy.crm.utils.AssertUtil;
import com.zy.crm.utils.PhoneUtil;
import com.zy.crm.vo.Customer;
import com.zy.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;

    /*
     * 多条件分页查询客户（返回的数据格式必须满足LayUi中数据表格要求的格式）
     * */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        //得到对应分页对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户操作
     * 1.参数校验
     *     客户名称  name 非空 不可重复
     *     phone 联系电话  非空 格式合法
     *     法人  fr 非空
     * 2.参数默认值
     *     isValid
     *     createDate
     *     updateDate
     *     state  流失状态  0-未流失 1-已流失
     *3.执行添加 判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        /*1.参数校验*/
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //判断客户名的唯一性
        Customer temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户名称是否存在
        AssertUtil.isTrue(null != temp,"客户名称已存在，请重新输入！");

        /*2.设置参数的默认值*/
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setState(0);
        //客户编号
        String khno = "KH" + System.currentTimeMillis();
        customer.setKhno(khno);

        /*3.执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.insertSelective(customer) < 1,"添加客户信息失败！");
    }

    /**
     * 修改客户信息
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer){
        /*1.参数校验*/
        AssertUtil.isTrue(null == customer.getId(),"待更新记录不存在！");
        //通过客户ID查询客户记录
        Customer temp = customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        //参数校验
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //通过客户名称查询客户记录
        temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户记录是否存在，且客户id是否与更新记录的id一致
        AssertUtil.isTrue(null != temp && !(temp.getId()).equals(customer.getId()),"客户名称已存在，请重新输入！");

        /*2.设置参数的默认值*/
        customer.setUpdateDate(new Date());
        /*3.执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1,"修改客户信息失败！");

    }

    /**
     * 参数校验
     * */
    private void checkCustomerParams(String name, String fr, String phone) {
        //客户名称 name     非空
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空！");
        //法人代表  fr      非空
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人代表不能为空！");
        //手机号码 phone    非空
//        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空！");
        //手机号码 phone    格式要正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确！");

    }

    /**
     * 删除客户信息
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer id) {
        //判断id是否为空，数据是否存在
        AssertUtil.isTrue(null == id,"待删除记录不存在！");
        //通过id查询客户记录
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == customer,"待删除记录不存在！");

        //设置状态为失效
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());

        //执行删除（更新）操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1,"待删除客户信息失败！");
    }
}
