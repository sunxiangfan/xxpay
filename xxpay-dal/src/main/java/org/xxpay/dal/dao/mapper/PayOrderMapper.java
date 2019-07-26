package org.xxpay.dal.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.model.PayOrderExample;
import org.xxpay.dal.dao.model.StatementModel;
import org.xxpay.dal.dao.model.dto.PayOrderGroupCreateDateSumAmountModel;

public interface PayOrderMapper extends BaseMapper<PayOrder, PayOrderExample> {
    Long sumAmountByExample(PayOrderExample example);

    Long sumMchActualAmountByExample(PayOrderExample example);

    /**
     * 对账单
     *
     * @param example
     * @return
     */
    List<StatementModel> statementByExample(PayOrderExample example);


    /**
     * 对账单条数
     *
     * @param example
     * @return
     */
    int countStatementByExample(PayOrderExample example);

    /**
     * 分组统计每天的成交金额
     *
     * @param example
     * @return
     */
    List<PayOrderGroupCreateDateSumAmountModel> groupCreateDateSumAmountByExample(PayOrderExample example);
}