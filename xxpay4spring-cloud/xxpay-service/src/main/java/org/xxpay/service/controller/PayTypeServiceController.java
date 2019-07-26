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
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayTypeService;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 支付渠道接口
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayTypeServiceController {

    private final MyLog _log = MyLog.getLog(PayTypeServiceController.class);

    @Autowired
    private PayTypeService payTypeService;

    @RequestMapping(value = "/pay_type/select")
    public String selectPayType(@RequestParam String jsonParam) {
        // TODO 参数校验
        _log.info("selectPayType << {}", jsonParam);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "0000");
        if (StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String id = paramObj.getString("id");
        PayType payType = null;
        if (StringUtils.isNotBlank(id)) {
            payType = payTypeService.selectPayTypeById(id);
        }
        if (payType == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("result", JSON.toJSON(payType));
        _log.info("selectPayType >> {}", retObj);
        return retObj.toJSONString();
    }

}
