package org.xxpay.mgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.AgentMapper;
import org.xxpay.dal.dao.mapper.AmountFlowMapper;
import org.xxpay.dal.dao.mapper.MchInfoMapper;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/3/10.
 */
@Component
public class AmountFlowService {


    @Autowired
    private AmountFlowMapper amountFlowMapper;

    @Autowired
    private AgentService agentService;

    @Autowired
    private MchInfoService mchInfoService;

    @Transactional
    public int lockAmount(String mchId, MchType mchType, Long amount, Long preBalance, String remark) {
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setMchId(mchId);
        amountFlow.setAmount(-amount);
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertDecrease(amountFlow, AmountType.LOCK_AMOUNT);
        Assert.isTrue(updateRows == 1, "保存失败！");
        return updateRows;
    }

    @Transactional
    public int unlockAmount(String mchId, MchType mchType, Long amount, Long preBalance, String remark) {
        AmountFlow amountFlow = new AmountFlow();
        amountFlow.setMchId(mchId);
        amountFlow.setAmount(amount);
        amountFlow.setRemark(remark);
        amountFlow.setPreBalance(preBalance);
        int updateRows = this.insertIncrease(amountFlow, AmountType.UNLOCK_AMOUNT);
        Assert.isTrue(updateRows == 1, "保存失败！");
        return updateRows;
    }

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
        long afterDecreaseAmount=BigDecimalUtils.add(amountFlow.getAmount().longValue(),amountFlow.getPreBalance().longValue());
        Assert.isTrue(afterDecreaseAmount>=0,"调减金额不能大于余额。");
        Assert.isTrue(amountFlow.getAmount().compareTo(0L) < 0, "调减金额不能大于等于0。传入金额为：" + amountFlow.getAmount());
        amountFlow.setId(UUID.randomUUID().toString());
        amountFlow.setFlowType(amountFlowType.getCode().byteValue());
        amountFlow.setAmountType(amountType.getCode());
        return amountFlowMapper.insertSelective(amountFlow);
    }


    public List<AmountFlow> getList(int offset, int limit, AmountFlow info) {
        AmountFlowExample example = new AmountFlowExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return amountFlowMapper.selectByExample(example);
    }

    public Integer count(AmountFlow info) {
        AmountFlowExample example = new AmountFlowExample();
        AmountFlowExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return amountFlowMapper.countByExample(example);
    }

    void setCriteria(AmountFlowExample.Criteria criteria, AmountFlow info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (info.getFlowType() != null && info.getFlowType() != -99)
                criteria.andFlowTypeEqualTo(info.getFlowType());
            if (StringUtils.isNotBlank(info.getAmountType())) criteria.andAmountTypeEqualTo(info.getAmountType());
        }
    }

}
