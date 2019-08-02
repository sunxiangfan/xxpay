package org.xxpay.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.PayChannelMapper;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayChannelExample;

import java.util.List;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class PayChannelService {

    @Autowired
    private PayChannelMapper payChannelMapper;

    public PayChannel selectPayChannelById(String id) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<PayChannel> payChannelList = payChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payChannelList)) return null;
        return payChannelList.get(0);
    }

    public PayChannel selectPayChannelByCode( String code) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code);
        List<PayChannel> payChannelList = payChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payChannelList)) return null;
        return payChannelList.get(0);
    }

    public PayChannel selectMchPayChannelByMchIdAndPayType(String mchId, String payType){
        List<PayChannel> payChannels=payChannelMapper.selectByMchIdAndPayType(mchId,payType);
        if(CollectionUtils.isEmpty(payChannels)) return null;
        return  payChannels.get(0);
    }
}
