package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.Bill;
import org.xxpay.dal.dao.model.BillExample;
import org.xxpay.dal.dao.model.PayOrderExample;
import org.xxpay.dal.dao.model.StatementModel;
import org.xxpay.dal.dao.model.dto.BillGroupCreateDateSumAmountModel;

import java.util.List;

/**
 * 分润明细Mapper
 */
public interface BillMapper extends BaseMapper<Bill, BillExample>{
    Long sumPlatformCommissionByExample(BillExample example);
    Long sumAgentCommissionByExample(BillExample example);

    /**
     * 统计
     *
     * @param example
     * @return
     */
    List<BillGroupCreateDateSumAmountModel> groupCreateDateSumAmountByExample(BillExample example);


    /**
     * 统计数据条数
     *
     * @param example
     * @return
     */
    int countGroupCreateDateSumAmountByExample(BillExample example);
}