package org.xxpay.service.controller.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PayDigestUtil;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.controller.Notify4BasePay;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description:  支付宝H5
 * @date 2019-03-26
 * @Copyright: www.xxpay.org
 */
@RestController
public class Notify4HuitongController extends Notify4BasePay {

    private static final MyLog _log = MyLog.getLog(Notify4HuitongController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    private static final String logPrefix = "【汇通支付回调通知】";

    private static final String MCH_ID = "";
    private static final String KEY = "";

    /**
     * 后台通知响应
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/pay/huitongPayNotifyRes.htm")
    public void notifyRes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = doPayRes(request, response);
        if (result != null) {
            _log.info(" notify response: {}",logPrefix, result);
            response.setContentType("text/html");
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.print(result);

            } catch (IOException e) {
                _log.error("Pay response write exception.", e);
            }
        }

    }

    public String doPayRes(HttpServletRequest request, HttpServletResponse response)  {
        _log.info("====== 开始接收回调通知 ======");
        _log.info(logPrefix);

        Map<String, String> params = new HashMap<String, String>();
        params.put("payOrderId", request.getParameter("payOrderId"));
        params.put("mchId", request.getParameter("mchId"));
        params.put("appId", request.getParameter("appId"));
        params.put("productId", request.getParameter("productId"));
        params.put("mchOrderNo", request.getParameter("mchOrderNo"));
        params.put("amount", request.getParameter("amount"));
        params.put("status", request.getParameter("status"));
        params.put("channelOrderNo", request.getParameter("channelOrderNo"));
        params.put("channelAttach", request.getParameter("channelAttach"));
        params.put("param1", request.getParameter("param1"));
        params.put("param2", request.getParameter("param2"));
        params.put("paySuccTime", request.getParameter("paySuccTime"));
        params.put("backType", request.getParameter("backType"));
        params.put("sign", request.getParameter("sign"));

        _log.info("{}通知请求数据:reqStr={}", logPrefix, params);
        if (params.isEmpty()) {
            _log.error("{}请求参数为空", logPrefix);
            return PayConstant.RETURN_ALIPAY_VALUE_FAIL;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);

        if (!verifyParams(payContext)) {
            return PayConstant.RETURN_ALIPAY_VALUE_FAIL;
        }
        _log.info("{}验证请求数据及签名通过", logPrefix);
        String transaction_id = params.get("payOrderId");
        payOrder = (PayOrder) payContext.get("payOrder");
        int updatePayOrderRows;
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
        if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
            updatePayOrderRows = payOrderService.updateStatus4Success(payOrder.getId(), transaction_id);
            if (updatePayOrderRows != 1) {
                _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
                _log.info("{}响应给上游结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return PayConstant.RETURN_ALIPAY_VALUE_FAIL;
            }
            _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
            payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        }
        doNotify(payOrder);

        _log.info(logPrefix);
        _log.info("====== 完成接收回调通知 ======");
        return "success";
    }

    /**
     * 验证支付宝支付通知参数
     *
     * @return
     */
    public boolean verifyParams(Map<String, Object> payContext) {
        Map<String, Object> params = (Map<String, Object>) payContext.get("parameters");
        String mchOrderNo = (String)params.get("mchOrderNo");//商户id
        String amount = (String)params.get("amount");
        String payOrderId =(String) params.get("payOrderId");//上游id
        String status = (String)params.get("status");

        String key= KEY;
        String tmpSign = PayDigestUtil.getSign(params,key,"sign");

        String sign=(String)params.get("sign");

        String errorMessage;
        if (!tmpSign.equalsIgnoreCase(sign)) {
            errorMessage = "sign check failed.";
            _log.error(" Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        //查看支付状态
        if (!(status.equals("2")|| status.equals("3"))) {
            _log.error("支付状态trade_status={},pay_order_id={}不做业务处理 ", status, mchOrderNo);
            payContext.put("retMsg", "fail");
            return false;
        }

        if (StringUtils.isEmpty(mchOrderNo)) {
            _log.error("Notify parameter mchOrderNo is empty. mchOrderNo={}", mchOrderNo);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (null ==amount) {
            _log.error("Notify parameter amount is empty. amount={}", amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        // 查询payOrder记录
        PayOrder payOrder = payOrderService.selectPayOrder(mchOrderNo);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", mchOrderNo);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        // 查询payChannel记录
        String mchId = payOrder.getMchId();
        String channelId = payOrder.getPayChannelId();
        PayChannel payChannel = payChannelService.selectPayChannelById(channelId);
        if (payChannel == null) {
            _log.error("Can't found payChannel form db. mchId={} channelId={}, ", payOrderId, mchId, channelId);
            payContext.put("retMsg", "Can't found payChannel");
            return false;
        }

        // 核对金额
        long dbPayAmt = payOrder.getAmount().longValue();
        long aliPayAmt = new BigDecimal(amount).longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount.total_amount={} db_total_amount={},payOrderId={}", aliPayAmt, dbPayAmt, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
