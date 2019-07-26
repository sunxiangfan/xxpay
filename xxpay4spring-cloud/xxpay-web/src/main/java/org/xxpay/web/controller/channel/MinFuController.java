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
import org.xxpay.web.service.PayOrderServiceClient;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.web.controller.channel
 * @ClassName: MinFuController
 * @Description:
 * @CreateDate: 2019/6/17 20:09
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
public class MinFuController {


    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @RequestMapping(value = "/pay/minfu")
    public String payOrder(@RequestParam Map<String, String> params, HttpServletResponse response, ModelMap modelMap){
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
            //params.put("");

            JSONObject po = (JSONObject) JSON.toJSON(params);
            String result =payOrderServiceClient.doTengjingTransferReq(JSONObject.toJSONString(po));
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
            //_log.error(e, "");
            return "error";
        }
    }
}
