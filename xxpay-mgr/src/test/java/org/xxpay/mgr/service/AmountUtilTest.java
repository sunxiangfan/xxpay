package org.xxpay.mgr.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.xxpay.common.util.AmountUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmountUtilTest {

    @Test
    public  void  test01(){
        long amount=20000;
        long result= AmountUtil.randomSuffix(amount);
        Assert.isTrue(result>amount);
        Assert.isTrue(result-amount<=150);
    }
}
