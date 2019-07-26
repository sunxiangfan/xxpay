package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchBankCard;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.service.MchWithdrawApplyService;
import org.xxpay.service.service.PayOrderService;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description: 提现申请接口
 * @date 2019-04-01
 * @Copyright: www.xxpay.org
 */
@RestController
public class MchWithdrawApplyServiceController {

    private final MyLog _log = MyLog.getLog(MchWithdrawApplyServiceController.class);

    @Autowired
    private MchWithdrawApplyService mchWithdrawApplyService;

    @RequestMapping(value = "/cash/create")
    public String createPayOrder(@RequestParam String jsonParam) {
        _log.info("接收创建支付订单请求,jsonParam={}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001");
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        try {
            JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
            String mchId = paramObj.getString("mchId");
            Byte byteMchType = paramObj.getByte("mchType");
            MchType mchType;
            if (byteMchType.equals(MchType.AGENT.getCode())) {
                mchType = MchType.AGENT;
            } else if (byteMchType.equals(MchType.MERCHANT.getCode())) {
                mchType = MchType.MERCHANT;
            } else {
                mchType = null;
            }
            MchBankCard mchBankCard = JSON.parseObject(paramObj.toJSONString(), MchBankCard.class);
            Long applyAmount = paramObj.getLong("applyAmount");
            String mchOrderNo = paramObj.getString("mchOrderNo");
            int result = mchWithdrawApplyService.createCashOrder(mchId, mchType, mchBankCard, applyAmount,mchOrderNo);
            retObj.put("result", result);
        } catch (IllegalArgumentException ex) {
            retObj.put("code", PayEnum.ERR_0014.getCode());
            retObj.put("msg", "参数错误。详情：" + ex.getMessage());
        } catch (Exception e) {
            retObj.put("code", "9999"); // 系统错误
            retObj.put("msg", "系统错误");
        }
        return retObj.toJSONString();
    }

    @RequestMapping(value = "/cash/query")
    public String queryPayOrder(@RequestParam String jsonParam) {
        _log.info("selectCashOrder << {}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String mchId = paramObj.getString("mchId");
        String mchOrderNo = paramObj.getString("mchOrderNo");
        MchWithdrawApply cashOrder;
        cashOrder = mchWithdrawApplyService.selectCashOrderByMchIdAndMchOrderNo(mchId, mchOrderNo);
        if (cashOrder == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "提现订单不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(cashOrder));
        _log.info("selectCashOrder >> {}", retObj);
        return retObj.toJSONString();
    }

}
