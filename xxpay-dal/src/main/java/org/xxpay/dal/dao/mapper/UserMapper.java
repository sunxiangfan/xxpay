package org.xxpay.dal.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.dal.dao.model.User;
import org.xxpay.dal.dao.model.UserExample;

import java.util.List;

public interface UserMapper extends  BaseMapper<User,UserExample> {

}
