package org.xxpay.dal.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.dal.dao.model.MchPayChannel;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayChannelExample;

import java.util.List;

public interface PayChannelMapper extends  BaseMapper<PayChannel, PayChannelExample> {
    List<PayChannel> selectByMchIdAndPayType(@Param("mchId")String mchId,@Param("payType") String payType);
}