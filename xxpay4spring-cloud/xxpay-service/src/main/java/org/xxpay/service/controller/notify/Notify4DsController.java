package org.xxpay.service.controller.notify;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.enumm.RetEnum;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.RSA;
import org.xxpay.common.util.RpcUtil;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.tengjing.TengjingProperties;
import org.xxpay.service.controller.Notify4BasePay;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 接收处理微信通知
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@RestController
public class Notify4DsController extends Notify4BasePay {

    private static final MyLog _log = MyLog.getLog(Notify4DsController.class);

    private static final String PLAT_PUBLIC_KEY_TEXT = "";

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    /**
     * 微信支付(统一下单接口)后台通知响应
     *
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/pay/dsPayNotifyRes.htm")
    @ResponseBody
    public String wxPayNotifyRes(@RequestParam HashMap<String, String> map, HttpServletRequest request) throws ServletException, IOException {
        _log.info("====== 开始接收ds支付回调通知 ======");
        String notifyRes = doPayRes(map, request);
        _log.info("响应给ds支付:{}", notifyRes);
        _log.info("====== 完成接收ds支付回调通知 ======");
        return notifyRes;
    }

    public String doPayRes(Map<String, String> map, HttpServletRequest request) throws ServletException, IOException {
        String logPrefix = "【ds支付回调通知】";
        _log.info("{} formData={}", logPrefix, map);
        return handlePayNotify(map);
//		return  "SUCCESS";
    }


    public String handlePayNotify(Map paramMap) {
        Map<String, Object> result = doPayNotify(paramMap);
        return RpcUtil.isSuccess(result) ? "SUCCESS" : "FAILURE";
    }

    public Map doPayNotify(Map paramMap) {
        String logPrefix = "【处理ds支付回调】";
        _log.info("====== 开始{} ======", logPrefix);
        try {
            // 验证业务数据是否正确,验证通过后返回PayOrder对象
            if (!verifyTingjingPayParams(paramMap)) {
                return RpcUtil.createFailResult(null, RetEnum.RET_PARAM_INVALID);
            }
            PayOrder payOrder = (PayOrder) paramMap.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            String trade_no = (String) paramMap.get("trade_no");
            String channelOrderNo = trade_no;
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = payOrderService.updateStatus4Success(payOrder.getId(), channelOrderNo);
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
                    return RpcUtil.createBizResult(null, WxPayNotifyResponse.fail("处理订单失败"));
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setChannelOrderNo(channelOrderNo);
            }
            // 业务系统后端通知
            doNotify(payOrder);
            _log.info("====== 完成{} ======", logPrefix);
            return RpcUtil.createBizResult(null, WxPayNotifyResponse.success("OK"));
        } catch (IllegalArgumentException e) {
            _log.error("{},异常原因：{}", logPrefix, e.getMessage());
            return RpcUtil.createBizResult(null, WxPayNotifyResponse.fail(e.getMessage()));
        } catch (Exception e) {
            _log.error(e, "{},异常原因", logPrefix);
            return RpcUtil.createBizResult(null, WxPayNotifyResponse.fail(e.getMessage()));
        }
    }

    /**
     * 验证微信支付通知参数
     *
     * @return
     */
    public boolean verifyTingjingPayParams(Map params) {
        String sign = (String) params.remove("sign");
        TreeMap treeMap=new TreeMap(params);
        String signContent = toQueryString(treeMap);
        PublicKey platPublicKey = RSA.toPublicKey(PLAT_PUBLIC_KEY_TEXT);
        boolean verify = RSA.verify(signContent, sign, platPublicKey);
        if (!verify) {
            _log.error("签名验证失败。param={},sign={}", params, sign);
            params.put("retMsg", "sign check failed");
            return false;
        }
        //校验结果是否成功
        if (!"P000".equalsIgnoreCase((String) params.get("ret_code"))) {
            _log.error("ret_code={},ret_msg={}", params.get("ret_code"), params.get("ret_msg"));
            params.put("retMsg", "notify data failed");
            return false;
        }
        String amount = (String) params.get("amount");// 总金额,单位分
        String out_order_no = (String) params.get("out_order_no"); // 商户系统订单号

        // 查询payOrder记录
        String payOrderId = out_order_no;
        PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            params.put("retMsg", "Can't found payOrder");
            return false;
        }

        // 核对金额
        long wxPayAmt = new BigDecimal(amount).longValue();
        long dbPayAmt = payOrder.getPayAmount().longValue();
        if (dbPayAmt != wxPayAmt) {
            _log.error("db payOrder record payPrice not equals total_fee. amount={},payOrderId={}", amount, payOrderId);
            params.put("retMsg", "total_fee is not the same");
            return false;
        }

        params.put("payOrder", payOrder);
        return true;
    }

    protected static String toQueryString(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(32);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
