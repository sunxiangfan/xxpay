package org.xxpay.mgr.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.MchInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MchAccountServiceTest {
    @Autowired
    private AgentService agentService;

    static final String mchId = "A10006";

    static final Long lockAmount = 8000L;

    @Test
    public void lockAmount() {
        Agent mchInfo = agentService.select(mchId);
        Long preBalance = mchInfo.getBalance();
        if(preBalance<lockAmount){
            return;
        }
        Long expectAfterBalance = BigDecimalUtils.sub(preBalance, lockAmount);
        Long preLockAmount = mchInfo.getLockAmount();
        Long expectAfterLockAmount = BigDecimalUtils.add(preLockAmount, lockAmount);
        int preVersion = mchInfo.getVersion();
        int expectAfterVersion = preVersion + 1;
        int rows = agentService.lockAmount(mchId, lockAmount,"单元测试");
        Assert.isTrue(rows > 0, "冻结失败！");
        Agent afterMchInfo = agentService.select(mchId);
        Long actualAfterBalance = afterMchInfo.getBalance();
        Long actualAfterLockAmount = afterMchInfo.getLockAmount();
        int actualAfterVersion = afterMchInfo.getVersion();
        Assert.isTrue(actualAfterBalance.compareTo(expectAfterBalance) == 0, "余额不对！");
        Assert.isTrue(actualAfterLockAmount.compareTo(expectAfterLockAmount) == 0, "锁定金额不对！");
        Assert.isTrue(actualAfterVersion == expectAfterVersion, "版本号不对！");
    }

    @Test
    public void unlockAmount() {
        Agent mchInfo = agentService.select(mchId);
        Long preLockAmount = mchInfo.getLockAmount();
        if(preLockAmount.compareTo(lockAmount)<0){
            return;
        }
        Long expectAfterLockAmount = BigDecimalUtils.sub(preLockAmount, lockAmount);
        Long preBalance = mchInfo.getBalance();
        Long expectAfterBalance = BigDecimalUtils.add(preBalance, lockAmount);
        int preVersion = mchInfo.getVersion();
        int expectAfterVersion = preVersion + 1;
        int rows = agentService.unlockAmount(mchId, lockAmount,"单元测试");
        Assert.isTrue(rows > 0, "解冻失败！");
        Agent afterMchInfo = agentService.select(mchId);
        Long actualAfterBalance = afterMchInfo.getBalance();
        Long actualAfterLockAmount = afterMchInfo.getLockAmount();
        int actualAfterVersion = afterMchInfo.getVersion();
        Assert.isTrue(actualAfterBalance.compareTo(expectAfterBalance) == 0, "余额不对！expect: "+expectAfterBalance+" , actual: "+actualAfterBalance);
        Assert.isTrue(actualAfterLockAmount.compareTo(expectAfterLockAmount) == 0, "锁定金额不对！expect: "+expectAfterLockAmount+" ,actual: "+actualAfterLockAmount);
        Assert.isTrue(actualAfterVersion == expectAfterVersion, "版本号不对！expect: "+ expectAfterVersion+" , actual: "+expectAfterVersion);
    }
}
