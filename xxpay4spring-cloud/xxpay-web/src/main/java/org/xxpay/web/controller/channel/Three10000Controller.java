package org.xxpay.web.controller.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.web.controller.PayOrderController;
import org.xxpay.web.service.PayOrderServiceClient;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class Three10000Controller {

    private final MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @RequestMapping(value = "/pay/three10000/gateway")
    public String payOrder(@RequestParam Map<String, String> params, HttpServletResponse response, ModelMap modelMap) {
        String logPrefix = "【three10000线下转账】";
        try {
            String payOrderId = params.get("payOrderId");
            if(StringUtils.isEmpty(payOrderId)){
                modelMap.put("retMsg","订单号为空！");
                return "error";
            }
            String pay_bankcode = params.get("pay_bankcode");
            if(StringUtils.isEmpty(pay_bankcode)){
                modelMap.put("retMsg","支付银行不能为空！");
                return "error";
            }
            JSONObject po = (JSONObject) JSON.toJSON(params);
            String result = payOrderServiceClient.doThree10000Req(getJsonParam(new String[]{"payOrderId", "pay_bankcode"}, new Object[]{payOrderId, pay_bankcode}));
            JSONObject resultObj = JSON.parseObject(result);
            String retCode = resultObj.getString(PayConstant.RESULT_PARAM_RESCODE);
            if (retCode.equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                String type=resultObj.getString("type");
                String data=resultObj.getString("data");
                if("1".equals(type)){
                    response.getWriter().write(data);
                    return null;
                }
                else if("2".equals(type)){
                    modelMap.put("action", data);
                    modelMap.put("method", "POST");
                    return "payForm";
                }
                else{
                    return "error";
                }
            } else {
                modelMap.put("retMsg",resultObj.getString(PayConstant.RESULT_PARAM_ERRCODEDES));
                return "error";
            }
        } catch (Exception e) {
            _log.error(e, "");
            return "error";
        }
    }

    String getJsonParam(String[] names, Object[] values) {
        JSONObject jsonParam = new JSONObject();
        for (int i = 0; i < names.length; i++) {
            jsonParam.put(names[i], values[i]);
        }
        return jsonParam.toJSONString();
    }

    String getJsonParam(String name, Object value) {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(name, value);
        return jsonParam.toJSONString();
    }
}
