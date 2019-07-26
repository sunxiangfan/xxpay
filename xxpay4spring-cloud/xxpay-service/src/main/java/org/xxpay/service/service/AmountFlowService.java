package org.xxpay.service.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.AmountFlowMapper;
import org.xxpay.dal.dao.mapper.BillMapper;
import org.xxpay.dal.dao.mapper.MchPayChannelMapper;
import org.xxpay.dal.dao.model.AmountFlow;
import org.xxpay.dal.dao.model.Bill;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.PayOrder;

import java.util.UUID;

@Service
public class AmountFlowService {
    @Autowired
    private AmountFlowMapper amountFlowMapper;

    @Autowired
    private BillMapper billMapper;

    @Transactional
    public int insertIncrease(AmountFlow amountFlow, AmountType amountType) {
        Assert.notNull(amountFlow, "商户账户金额变动表数据不能为空！");
        Assert.isTrue(StringUtils.isNotBlank(amountFlow.getMchId()), "商户号不能为空！");
        Assert.notNull(amountFlow.getAmount(), "调增金额不能为空！");
        Assert.notNull(amountFlow.getPreBalance(), "调增前账户余额不能为空！");
        Assert.notNull(amountType, "金额变动类型不能为空！");
        AmountFlowType amountFlowType = amountType.getFlowType();
        Assert.isTrue(amountFlowType.equals(AmountFlowType.IN), "调增类型不匹配！传入类型为：" + amountType);
        Assert.isTrue(amountFlow.getAmount().compareTo(0L) > 0, "调增金额不能小于等于0。传入金额为：" + amountFlow.getAmount());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setFlowType(amountFlowType.getCode().byteValue());
        amountFlow.setAmountType(amountType.getCode());
        return amountFlowMapper.insertSelective(amountFlow);
    }

    @Transactional
    public int insertDecrease(AmountFlow amountFlow, AmountType amountType) {
        Assert.notNull(amountFlow, "商户账户金额变动表数据不能为空！");
        Assert.isTrue(StringUtils.isNotBlank(amountFlow.getMchId()), "商户号不能为空！");
        Assert.notNull(amountFlow.getAmount(), "调减金额不能为空！");
        Assert.notNull(amountFlow.getPreBalance(), "调减前账户余额不能为空！");
        Assert.notNull(amountType, "金额变动类型不能为空！");
        AmountFlowType amountFlowType = amountType.getFlowType();
        Assert.isTrue(amountFlowType.equals(AmountFlowType.OUT), "调减类型不匹配！传入类型为：" + amountType);
        Assert.isTrue(amountFlow.getAmount().compareTo(0L) < 0, "调减金额不能大于等于0。传入金额为：" + amountFlow.getAmount());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setFlowType(amountFlowType.getCode().byteValue());
        amountFlow.setAmountType(amountType.getCode());
        return amountFlowMapper.insertSelective(amountFlow);
    }


}
