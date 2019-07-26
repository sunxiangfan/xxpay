//package org.xxpay.merchant.service;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//import org.xxpay.common.enumm.UserState;
//import org.xxpay.common.enumm.UserType;
//import org.xxpay.common.util.PasswordUtil;
//import org.xxpay.dal.dao.mapper.UserMapper;
//import org.xxpay.dal.dao.model.User;
//import org.xxpay.dal.dao.model.UserExample;
//
//import java.util.List;
//
//@Component
//public class UserService {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    public int addUser(User user) {
//        user.generateUserId();
//        String userId = user.getUserId();
//        String rawPassword = user.getPassword();
//        String encryptionPassword = PasswordUtil.getPassword(rawPassword, userId);
//        user.setPassword(encryptionPassword);
//        return userMapper.insertSelective(user);
//    }
//
//    public int updateUser(User user) {
//        return userMapper.updateByPrimaryKeySelective(user);
//    }
//
//    public User selectUser(String userId) {
//        return userMapper.selectByPrimaryKey(userId);
//    }
//
//    public User selectByBizId(String bizId) {
//        UserExample example = new UserExample();
//        UserExample.Criteria criteria = example.createCriteria();
//        if (StringUtils.isNoneBlank(bizId)) {
//            criteria.andBizIdEqualTo(bizId);
//        }
//        List<User> result = userMapper.selectByExample(example);
//        if (CollectionUtils.isEmpty(result)) {
//            return null;
//        }
//        Assert.isTrue(result.size() == 1, "查到多条用户数据。业务id：" + bizId);
//        return result.get(0);
//    }
//
//    public List<User> getUserList(int offset, int limit, User user) {
//        UserExample example = new UserExample();
//        example.setOrderByClause("createTime DESC");
//        example.setOffset(offset);
//        example.setLimit(limit);
//        UserExample.Criteria criteria = example.createCriteria();
//        setCriteria(criteria, user);
//        return userMapper.selectByExample(example);
//    }
//
//    public User login(String loginAccount, String password) {
//        Assert.isTrue(StringUtils.isNoneBlank(loginAccount), "用户名不能为空！");
//        Assert.isTrue(StringUtils.isNoneBlank(password), "密码不能为空！");
//        UserExample example = new UserExample();
//        example.setLimit(1);
//        UserExample.Criteria criteria = example.createCriteria();
//        User user = new User();
//        user.setLoginAccount(loginAccount);
//        setCriteria(criteria, user);
//        List<User> list = userMapper.selectByExample(example);
//        Assert.notEmpty(list, "用户名或密码错误！");
//        User findUserByAccount = list.get(0);
//        String userId = findUserByAccount.getUserId();
//        String encryptionPassword = PasswordUtil.getPassword(password, userId);
//        Assert.isTrue(encryptionPassword.equals(findUserByAccount.getPassword()), "用户名或密码错误！");
//        Assert.isTrue(UserType.SUB_MERCHANT.getCode().equals(findUserByAccount.getType()), "该账号类型不允许登录！");
//        Assert.isTrue(findUserByAccount.getState().equals(UserState.ENABLE.getCode()), "该账号已停用！");
//        Assert.isTrue(StringUtils.isNoneBlank(findUserByAccount.getBizId()),"该账号无业务id！");
//        return findUserByAccount;
//    }
//
//    public Integer count(User user) {
//        UserExample example = new UserExample();
//        UserExample.Criteria criteria = example.createCriteria();
//        setCriteria(criteria, user);
//        return userMapper.countByExample(example);
//    }
//
//    void setCriteria(UserExample.Criteria criteria, User user) {
//        if (user != null) {
//            if (StringUtils.isNotBlank(user.getUserId())) criteria.andUserIdEqualTo(user.getUserId());
//            if (StringUtils.isNotBlank(user.getLoginAccount())) criteria.andLoginAccountEqualTo(user.getLoginAccount());
//            if (user.getState() != null) criteria.andStateEqualTo(user.getState());
//        }
//    }
//}
