package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.DepositGroupMchIdSumAmount;

import java.util.List;

/**
 * 保证金Mapper
 */
public interface DepositAmountMapper extends BaseMapper<DepositAmount, DepositAmountExample>{

    /**
     * 统计保证金
     * @param example
     * @return
     */
    Long sumAmountByExample(DepositAmountExample example);

    /**
     * 按商户号分组统计保证金
     * @param example
     * @return
     */
    List<DepositGroupMchIdSumAmount> groupMchIdSumAmountByExample(DepositAmountExample example);
}