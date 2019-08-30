package org.xxpay.mgr.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.mapper.MchWithdrawApplyMapper;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.MchWithdrawApplyExample;
import org.xxpay.domain.MchWithdrawApplyDomainService;

import java.util.*;

@Service
public class MchWithdrawApplyService {

    private final static MyLog _log = MyLog.getLog(MchWithdrawApplyService.class);

    final String baseUrl = "http://localhost:3000/pay/zx/transfer";

    @Autowired
    private MchWithdrawApplyMapper mchWithdrawApplyMapper;

    @Autowired
    private MchWithdrawApplyDomainService mchWithdrawApplyDomainService;


    public int add(MchWithdrawApply info) {
        info.setId(UUID.randomUUID().toString());
        return mchWithdrawApplyMapper.insertSelective(info);
    }

    @Transactional
    public String audit(String id, boolean isPass, String auditor) {
        String request = "1";
        int i = mchWithdrawApplyDomainService.audit(id, isPass, auditor);
        if (i == 1 && isPass) {
            MchWithdrawApply mchWithdraw = mchWithdrawApplyMapper.selectByPrimaryKey(id);
            Map<String, String> map = new HashMap<>();
            map.put("bankCardMobile", mchWithdraw.getMobile());
            map.put("bankCardNo", mchWithdraw.getNumber());
            map.put("bankCardOwnerName", mchWithdraw.getAccountName());
            map.put("bankName", mchWithdraw.getBankName());
            map.put("idCardNo", mchWithdraw.getIdCard());
            map.put("mchRequestNo", mchWithdraw.getMchOrderNo());
            map.put("transferAmt", String.valueOf(mchWithdraw.getApplyAmount()));
            map.put("callbackUrl", "http://baidu.com");

            request = HttpRequest.post(baseUrl).charset("UTF-8")
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONString(map))
                    .execute().body();
            _log.info("通过提现申请记录,返回:{}", request);
            JSONObject jsonParam = JSONObject.parseObject(request);
            if (!"0000".equals(jsonParam.getString("code"))) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return request;
    }


    public int update(MchWithdrawApply info) {
        info.setUpdateTime(new Date());
        return mchWithdrawApplyMapper.updateByPrimaryKeySelective(info);
    }

    public MchWithdrawApply select(String id) {
        return mchWithdrawApplyMapper.selectByPrimaryKey(id);
    }

    public List<MchWithdrawApply> getList(int offset, int limit, MchWithdrawApply info) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchWithdrawApplyMapper.selectByExample(example);
    }

    public Integer count(MchWithdrawApply info) {
        MchWithdrawApplyExample example = new MchWithdrawApplyExample();
        MchWithdrawApplyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return mchWithdrawApplyMapper.countByExample(example);
    }


    void setCriteria(MchWithdrawApplyExample.Criteria criteria, MchWithdrawApply info) {
        if (info != null) {
            if (StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
            if (StringUtils.isNotBlank(info.getMchId())) criteria.andMchIdEqualTo(info.getMchId());
            if (info.getMchType() != null) criteria.andMchTypeEqualTo(info.getMchType());
            if (info.getState() != null) criteria.andStateEqualTo(info.getState());
            if (info.getCashState() != null) criteria.andCashStateEqualTo(info.getCashState());
        }
    }
}
