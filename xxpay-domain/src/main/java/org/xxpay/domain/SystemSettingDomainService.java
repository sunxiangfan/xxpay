package org.xxpay.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xxpay.common.constant.SystemSettingConstant;
import org.xxpay.common.enumm.MchType;
import org.xxpay.dal.dao.mapper.SystemSettingMapper;
import org.xxpay.dal.dao.model.SystemSetting;

@Service
public class SystemSettingDomainService {
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    /**
     * 查询提现手续费
     * @param mchType
     * @return
     */
    public long getCashFee(MchType mchType) {
        SystemSetting mchServiceChargeSetting = null;
        String settingKey = null;
        if (MchType.MERCHANT.equals(mchType)) {
            settingKey = SystemSettingConstant.KEY_SUB_MCH_CASH_SERVICE_CHARGE;
        } else if (MchType.AGENT.equals(mchType)) {
            settingKey = SystemSettingConstant.KEY_MCH_CASH_SERVICE_CHARGE;
        } else {
            throw new IllegalArgumentException("无效的商户类型。mchType: " + mchType);
        }
        mchServiceChargeSetting = systemSettingMapper.selectByName(settingKey);

        Assert.notNull(mchServiceChargeSetting, "提现手续费不能为空！settingKey: " + settingKey);
        Long mchServiceCharge = Long.parseLong(mchServiceChargeSetting.getParamValue());
        Assert.notNull(mchServiceCharge, "提现手续费不能为空！settingKey: " + settingKey);
        Assert.isTrue(mchServiceCharge.compareTo(0L) >= 0, "提现手续费不能为负数！settingKey: " + settingKey);
        return mchServiceCharge.longValue();
    }
}
