package org.xxpay.dal.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.CashChannelExample;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayChannelExample;

import java.util.List;

public interface CashChannelMapper extends  BaseMapper<CashChannel, CashChannelExample> {
}