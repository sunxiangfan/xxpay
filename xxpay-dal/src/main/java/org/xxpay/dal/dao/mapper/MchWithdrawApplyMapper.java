package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.dto.CashGroupSumAmountModel;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.MchWithdrawApplyExample;
import org.xxpay.dal.dao.model.dto.CashSumAmountModel;

import java.util.List;

/**
 * 商户提现申请Mapper
 */
public interface MchWithdrawApplyMapper extends BaseMapper<MchWithdrawApply, MchWithdrawApplyExample> {
    Long sumApplyAmountByExample(MchWithdrawApplyExample example);

    Long sumCommissionByExample(MchWithdrawApplyExample example);

    List<CashGroupSumAmountModel> groupMchIdSumAmountByExample(MchWithdrawApplyExample example);

    List<CashSumAmountModel> sumAmountByExample(MchWithdrawApplyExample example);
}
