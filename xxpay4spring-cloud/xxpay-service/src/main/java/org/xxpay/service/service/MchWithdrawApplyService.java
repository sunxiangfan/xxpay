package org.xxpay.service.service;

import com.alipay.api.domain.BankCardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.enumm.MchType;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.mapper.PayOrderMapper;
import org.xxpay.dal.dao.model.*;
import org.xxpay.domain.MchWithdrawApplyDomainService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description:
 * @date 2019-04-01
 * @Copyright: www.xxpay.org
 */
@Component
public class MchWithdrawApplyService {

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;

    public MchWithdrawApply selectCashOrderByMchIdAndMchOrderNo(String mchId, String mchOrderNo) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andMchOrderNoEqualTo(mchOrderNo);
        List<MchWithdrawApply> list = mchWithdrawApplyMapper.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public int createCashOrder(String mchId, MchType mchType, MchBankCard bankCardInfo, Long applyAmount,String mchOrderNo) {
        return mchWithdrawApplyDomainService.applyCash(mchId,mchType, bankCardInfo,applyAmount,mchOrderNo);
    }
}
