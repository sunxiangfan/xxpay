package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.*;

import java.util.List;

/**
 * 平台提现Mapper
 */
public interface PlatformWithdrawMapper extends BaseMapper<PlatformWithdraw, PlatformWithdrawExample> {
    Long sumApplyAmountByExample(PlatformWithdrawExample example);

    Long sumThirdDeductionByExample(PlatformWithdrawExample example);
}
