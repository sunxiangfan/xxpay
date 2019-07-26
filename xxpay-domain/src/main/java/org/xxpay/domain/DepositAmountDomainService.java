package org.xxpay.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.xxpay.common.enumm.AmountFlowType;
import org.xxpay.common.enumm.AmountType;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.mapper.AmountFlowMapper;
import org.xxpay.dal.dao.mapper.DepositAmountMapper;
import org.xxpay.dal.dao.model.AmountFlow;
import org.xxpay.dal.dao.model.AmountFlowExample;
import org.xxpay.dal.dao.model.DepositAmount;
import org.xxpay.dal.dao.model.DepositAmountExample;

import java.util.List;
import java.util.UUID;

/**
 * Created by tanghaibo on 19/3/10.
 */
@Component
public class DepositAmountDomainService {


    @Autowired
    private DepositAmountMapper depositAmountMapper;

    public Long sumLockedAmountByMchId(String mchId) {
        DepositAmountExample example = new DepositAmountExample();
        DepositAmountExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andStateEqualTo((byte) 0);
        Long result = depositAmountMapper.sumAmountByExample(example);
        result = result == null ? 0 : result;
        return result;
    }

}
