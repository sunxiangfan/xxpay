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
import org.xxpay.service.channel.Xq316Config;
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
 * @Description: 快捷
 * @date 2019-03-26
 * @Copyright: www.xxpay.org
 */
@RestController
public class Notify4O9nController extends Notify4BasePay {

    private static final MyLog _log = MyLog.getLog(Notify4O9nController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    /**
     * 支付后台通知响应
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/pay/o9nPayNotifyRes.htm")
    public void aliPayNotifyRes(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException {
        String result = doPayRes(request, response);
        if (result != null) {
            _log.info("o9n notify response: {}", result);
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

    public String doPayRes(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException  {
        String logPrefix = "【o9n支付回调通知】";
        _log.info("====== 开始接收o9n支付回调通知 ======");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        params.put("memberid",request.getParameter("memberid"));
        params.put("orderid",request.getParameter("orderid"));
        params.put("amount",request.getParameter("amount"));
        params.put("datetime",request.getParameter("datetime"));
        params.put("returncode",request.getParameter("returncode"));
        params.put("transaction_id",request.getParameter("transaction_id"));
        params.put("attach",request.getParameter("attach"));
        params.put("sign",request.getParameter("sign"));

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
        payOrder = (PayOrder) payContext.get("payOrder");
        String channelOrderId=request.getParameter("transaction_id");
        int updatePayOrderRows;
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
        if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
            updatePayOrderRows = payOrderService.updateStatus4Success(payOrder.getId(), channelOrderId);
            if (updatePayOrderRows != 1) {
                _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
                _log.info("{}响应给上游结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return PayConstant.RETURN_ALIPAY_VALUE_FAIL;
            }
            _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getId(), PayConstant.PAY_STATUS_SUCCESS);
            payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        }
        doNotify(payOrder);
        _log.info("====== 完成接收支付回调通知 ======");
        return "ok";
    }

    /**
     * 验证支付宝支付通知参数
     *
     * @return
     */
    public boolean verifyParams(Map<String, Object> payContext) throws IllegalAccessException{
        String logPrefix = "【o9n支付回调通知】";
        Map<String, String> params = (Map<String, String>) payContext.get("parameters");
        String keyValue="";

        String attach=params.get("attach");
        String sign=params.get("sign");
        params.remove("attach");
        params.remove("sign");

        String memberid=params.get("memberid");
        String orderid=params.get("orderid");
        String amount=params.get("amount");
        String datetime=params.get("datetime");
        String returncode=params.get("returncode");
        String transaction_id = params.get("transaction_id");
//        String SignTemp="amount="+amount+"+datetime="+datetime+"+memberid="+memberid+"+orderid="+orderid+"+returncode="+returncode+"+transaction_id="+transaction_id+"+key="+keyValue+"";
//        String md5sign=PayDigestUtil.md5(SignTemp,"utf-8");
        String md5sign=PayDigestUtil.getSign(params,keyValue);
        String errorMessage;
        if(!sign.equals(md5sign)){
            errorMessage = "sign check failed.";
            _log.error("Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return  false;
        }
        String out_trade_no=orderid;
        if(!returncode.equals("00")){
            _log.error("支付状态trade_status={},pay_order_id={}不做业务处理 ",returncode, orderid);
            payContext.put("retMsg", "fail");
            return  false;
        }

        String total_amount = AmountUtil.convertDollar2Cent(amount);        // 支付金额
        if (StringUtils.isEmpty(out_trade_no)) {
            _log.error("Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (StringUtils.isEmpty(total_amount)) {
            _log.error("Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
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
        long aliPayAmt = new BigDecimal(total_amount).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount.total_amount={} db_total_amount={},payOrderId={}", total_amount,dbPayAmt, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
