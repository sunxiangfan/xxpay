package org.xxpay.web.controller.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.web.controller.PayOrderController;
import org.xxpay.web.service.PayOrderServiceClient;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class PayTengjingController {

    private final MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @RequestMapping(value = "/pay/tengjing/transfer")
    public String payOrder(@RequestParam Map<String, String> params, HttpServletResponse response, ModelMap modelMap) {
        String logPrefix = "【tengjing线下转账】";
        try {
            String payOrderId = params.get("payOrderId");
            if(StringUtils.isEmpty(payOrderId)){
                modelMap.put("retMsg","订单号为空！");
                return "error";
            }
            String acc_no = params.get("acc_no");
            if(StringUtils.isEmpty(acc_no)){
                modelMap.put("retMsg","银行卡号不能为空！");
                return "error";
            }
            JSONObject po = (JSONObject) JSON.toJSON(params);
            String result = payOrderServiceClient.doTengjingTransferReq(getJsonParam(new String[]{"payOrderId", "acc_no"}, new Object[]{payOrderId, acc_no}));
            JSONObject resultObj = JSON.parseObject(result);
            String retCode = resultObj.getString(PayConstant.RESULT_PARAM_RESCODE);
            if (retCode.equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                Map<String, String> payParams = (Map<String, String>) resultObj.get("payParams");
                modelMap.put("payParams", payParams);
                modelMap.put("action", resultObj.getString("payUrl"));
                modelMap.put("method", "POST");
                return "payForm";
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
