package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.PayOrderExample;
import org.xxpay.dal.dao.model.ReserveAmount;
import org.xxpay.dal.dao.model.ReserveAmountExample;

/**
 * 账户金额变动Mapper
 */
public interface ReserveAmountMapper extends BaseMapper<ReserveAmount, ReserveAmountExample>{

    Long sumAmountByExample(PayOrderExample example);
}