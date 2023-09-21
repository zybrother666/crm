package com.zy.crm.service;

import com.zy.crm.base.BaseService;
import com.zy.crm.dao.UserMapper;
import com.zy.crm.dao.UserRoleMapper;
import com.zy.crm.model.UserModel;
import com.zy.crm.utils.AssertUtil;
import com.zy.crm.utils.Md5Util;
import com.zy.crm.utils.PhoneUtil;
import com.zy.crm.utils.UserIDBase64;
import com.zy.crm.vo.User;
import com.zy.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /*
    * 用户登录操作
    * 1. 参数判断，判断用户姓名、用户密码非空
         如果参数为空，抛出异常（异常被控制层捕获并处理）
      2. 调用数据访问层，通过用户名查询用户记录，返回用户对象
      3. 判断用户对象是否为空
         如果对象为空，抛出异常（异常被控制层捕获并处理）
      4. 判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
         如果密码不相等，抛出异常（异常被控制层捕获并处理）
      5. 如果密码正确，登录成功
    * */
    public UserModel userLogin(String userName,String userPwd){
        //1. 参数判断，判断用户姓名、用户密码非空
        checkLoginParams(userName,userPwd);

        //2.调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);

        //3.判断用户对象是否为空
        AssertUtil.isTrue(user==null,"用户姓名不存在！");
        
        //4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
        checkUserPwd(userPwd,user.getUserPwd());

        //5.返回构建用户对象
        return buildUserInfo(user);
    }

    /**
     * 用户密码修改
     * 1. 参数校验
     * 用户ID：userId 非空 用户对象必须存在
     * 原始密码：oldPassword 非空 与数据库中密文密码保持一致
     * 新密码：newPassword 非空 与原始密码不能相同
     * 确认密码：confirmPassword 非空 与新密码保持一致
     * 2. 设置用户新密码
     * 新密码进行加密处理
     * 3. 执行更新操作
     * 受影响的行数小于1，则表示修改失败
     *
     * 注：在对应的更新方法上，添加事务控制*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        //通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(null == user,"待更新记录不存在！");
        //参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败！");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空！");
        //判断原始密码是否正确（查询的用户对象中的用户密码是否与原始密码保持一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //判断新密码是否与原始密码一致（不允许新密码与原始密码一致）
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同！");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空！");
        //判断确认密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致！");
    }

    /*
    * 构建需要返回给客户端的用户对象
    * */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //userModel.setUserId(user.getId());
        //设置加密的用户ID
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /*
    * 密码判断
    * 先将客户端传递的密码加密，再与数据库中查询到的密码做比较
    * */
    private void checkUserPwd(String userPwd, String Pwd) {
        //将客户端传递的密码加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(Pwd),"用户密码不正确！");
    }

    //参数判断
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空！");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }
    /*
    * 查询所有的销售人员
    * */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //1.参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);

        //2.设置参数的默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码
        user.setUserPwd(Md5Util.encode("123456"));

        //3.执行添加操作,判断受影响的行数
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败!");

        //用户角色关联
        /**
         * 用户ID
         *  userId
         * 角色ID
         *  roleIds
         * */
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 用户角色关联
     * 添加操作
     *      原始角色不存在
     *          1.不添加新的角色记录     不操作用户角色信息
     *          2.添加新的角色记录       给指定用户绑定相关的角色记录
     * 更新操作
     *      原始角色不存在
     *          1.不添加新的角色记录     不操作用户角色信息
     *          2.添加新的角色记录       给指定用户绑定相关的角色记录
     *      原始角色存在
     *      1.添加新的角色记录           判断已有的角色记录，添加没有的角色记录
     *      2.清空所有的角色记录         删除用户绑定的角色记录
     *      3.移除部分角色记录           删除不存在的角色记录，存在的角色记录保留
     *      4.移除部分角色，添加新的角色     删除不存在的角色记录，存在的角色记录保留，添加的新的角色
     *
     *如何进行角色分配？？？
     *      判断用户对应的角色记录是否存在，将用户原有的角色记录删除，添加新的角色记录
     *
     *
     *删除操作
     *      删除指定用户绑定的角色记录
     * */
    private void relationUserRole(Integer userId, String roleIds) {
        //通过用户ID查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        //判断角色记录是否存在
        if(count > 0){
            //如果角色记录存在，则删除该用户对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败！");
        }

        //判断角色ID是否存在，如果存在，则添加该用户对应的角色记录
        if(StringUtils.isNotBlank(roleIds)){
            //将用户角色数据设置到集合中，执行批量添加
            List<UserRole> userRoleList = new ArrayList<>();
            //将角色ID字符串转换成数组
            String[] roleIdsArray = roleIds.split(",");
            //遍历数组，得到对应的用户角色对象，并设置到集合中
            for (String roleId : roleIdsArray){
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                //设置到集合中
                userRoleList.add(userRole);
            }
            //批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),"用户添加记录失败！");
        }

    }

    /**
     * 参数校验
     * */
    private void checkUserParams(String userName, String email, String phone,Integer userId) {
        //判断用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        //判断用户名的唯一性
        //通过用户名查询用户对象
        User temp = userMapper.queryUserByName(userName);
        //如果用户对象为空,则表示用户名可用;如果用户对象不为空,则表示用户名不可用
        //添加操作和修改操作不一样
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(userId)),"用户名已存在,请重新输入!");
        //邮箱 非空
        AssertUtil.isTrue(StringUtils.isBlank(email),"用户邮箱不能为空!");
        //手机号   非空
        AssertUtil.isTrue(StringUtils.isBlank(phone),"用户手机号码不能为空!");
        //手机号   格式判断
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确!");
    }

    /**
     * 更新用户
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //判断用户ID是否为空,且数据存在
        AssertUtil.isTrue(null == user.getId(),"待更新记录不存在!");
        //通过id查询数据
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //判断是否存在
        AssertUtil.isTrue(null==temp,"待更新记录不存在!");
        //参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());

        //设置默认值
        user.setUpdateDate(new Date());

        //执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)!=1,"用户更新失败！");

        /*用户角色关联*/
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 用户删除
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在！");
        //执行删除操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"用户删除失败！");

        //遍历用户ID的数组
        for(Integer userId : ids){
            //通过用户ID查询对应的用户角色记录
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            //判断用户角色记录是否存在
            if(count > 0){
                //通过用户ID删除对应的用户角色记录
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"删除用户记录失败!");
            }
        }
    }

}
