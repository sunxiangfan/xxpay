package org.xxpay.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xxpay.dal.dao.mapper.MchPayChannelMapper;
import org.xxpay.dal.dao.mapper.PayChannelMapper;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.MchPayChannelExample;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayChannelExample;

import java.util.List;

/**
 * @Description:
 * @author tanghaibo
 * @date 2019-02-26
 * @version V1.0
 */
@Component
public class MchPayChannelService {

    @Autowired
    private MchPayChannelMapper mchPayChannelMapper;

    public MchPayChannel selectMchPayChannel(String payChannelId, String mchId) {
        MchPayChannelExample example = new MchPayChannelExample();
        MchPayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andPayChannelIdEqualTo(payChannelId);
        criteria.andMchIdEqualTo(mchId);
        List<MchPayChannel> payChannelList = mchPayChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payChannelList)) return null;
        return payChannelList.get(0);
    }

}
