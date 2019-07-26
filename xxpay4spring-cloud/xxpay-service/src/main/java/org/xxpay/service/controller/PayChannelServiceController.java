package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.service.service.PayChannelService;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 支付渠道接口
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannelServiceController {

    private final MyLog _log = MyLog.getLog(PayChannelServiceController.class);

    @Autowired
    private PayChannelService payChannelService;

    @RequestMapping(value = "/pay_channel/select")
    public String selectPayChannel(@RequestParam String jsonParam) {
        // TODO 参数校验
        _log.info("selectPayChannel << {}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String id = paramObj.getString("id");
        String code = paramObj.getString("code");
        PayChannel payChannel = null;
        if (StringUtils.isNotBlank(id)) {
            payChannel = payChannelService.selectPayChannelById(id);
        } else if (StringUtils.isNotBlank(code)) {
            payChannel = payChannelService.selectPayChannelByCode(code);
        }
        if (payChannel == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(payChannel));
        _log.info("selectPayChannel >> {}", retObj);
        return retObj.toJSONString();
    }

    @RequestMapping(value = "/pay_channel/select_by_mchid_paytype")
    public String selectByMchIdAndPayType(@RequestParam String jsonParam) {
        _log.info("selectByMchIdAndPayType << {}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String mchId = paramObj.getString("mchId");
        String payType = paramObj.getString("payType");
        PayChannel mchPayChannel = payChannelService.selectMchPayChannelByMchIdAndPayType(mchId, payType);
        if (mchPayChannel == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(mchPayChannel));
        _log.info("selectByMchIdAndPayType >> {}", retObj);
        return retObj.toJSONString();
    }
}
