package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchInfoExample;

public interface MchInfoMapper extends BaseMapper<MchInfo,MchInfoExample>{
    Long selectBalanceById(String id);

    /**
     * 汇总余额
     * @param example
     * @return
     */
    Long sumBalanceByExample(MchInfoExample example);
}