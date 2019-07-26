package org.xxpay.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.PayChannelMapper;
import org.xxpay.dal.dao.mapper.PayTypeMapper;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayChannelExample;
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.dal.dao.model.PayTypeExample;

import java.util.List;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class PayTypeService {

    @Autowired
    private PayTypeMapper payTypeMapper;

    public PayType selectPayTypeById(String id) {
        PayTypeExample example = new PayTypeExample();
        PayTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<PayType> payTypeList = payTypeMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payTypeList)) return null;
        return payTypeList.get(0);
    }

}
