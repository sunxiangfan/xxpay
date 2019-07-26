package org.xxpay.service.controller.notify;

import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PayDigestUtil;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.minfu.MinFuConfig;
import org.xxpay.service.controller.Notify4BasePay;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghaibo
 * @version V1.0
 * @Description:  敏付支付回调
 * @date 2019-03-26
 * @Copyright: www.xxpay.org
 */
@RestController
public class NotifyMinFuController extends Notify4BasePay {

    private static final MyLog _log = MyLog.getLog(NotifyMinFuController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    private static final String logPrefix = "【敏付支付回调通知】";

    @Autowired
    private MinFuConfig minFuConfig;
    /**
     * 后台通知响应
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/pay/minFuPayNotifyRes.htm")
    public void notifyRes(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    public String doPayRes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        _log.info("====== 开始接收回调通知 ======");
        _log.info(logPrefix);
        String payOrderId =request.getParameter("order_no");
        // 接收返回的参数

        _log.info("订单号为{},{}通知请求数据:reqStr={}",payOrderId ,logPrefix, request.getParameterMap());
        PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
        if (!verifyParams(request,payOrder)) {
            return PayConstant.RETURN_ALIPAY_VALUE_FAIL;
        }
        _log.info("{}验证请求数据及签名通过", logPrefix);
        int updatePayOrderRows;
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
        if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
            updatePayOrderRows = payOrderService.updateStatus4Success(payOrderId, null);
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
     * 验证敏付支付通知参数
     *
     * @return
     */
    public boolean verifyParams(HttpServletRequest request,PayOrder payOrder) throws Exception {

        /** 数据签名
         签名规则定义如下：
         （1）参数列表中，除去sign_type、sign两个参数外，其它所有非空的参数都要参与签名，值为空的参数不用参与签名；
         （2）签名参数排序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，组成规则如下：
         参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n		*/

        StringBuilder signStr = new StringBuilder();
        if(null != request.getParameter("bank_seq_no") && !request.getParameter("bank_seq_no").equals("")) {
            signStr.append("bank_seq_no=").append(request.getParameter("bank_seq_no")).append("&");
        }

        if(null != request.getParameter("extra_return_param") && !request.getParameter("extra_return_param").equals("")) {
            signStr.append("extra_return_param=").append(request.getParameter("extra_return_param")).append("&");
        }
        signStr.append("interface_version=").append(request.getParameter("interface_version")).append("&");
        signStr.append("merchant_code=").append(request.getParameter("merchant_code")).append("&");
        signStr.append("notify_id=").append(request.getParameter("notify_id")).append("&");
        signStr.append("notify_type=").append(request.getParameter("notify_type")).append("&");
        signStr.append("order_amount=").append(request.getParameter("order_amount")).append("&");
        signStr.append("order_no=").append(request.getParameter("order_no")).append("&");
        signStr.append("order_time=").append(request.getParameter("order_time")).append("&");
        signStr.append("trade_no=").append(request.getParameter("trade_no")).append("&");
        signStr.append("trade_status=").append(request.getParameter("trade_status")).append("&");
        signStr.append("trade_time=").append(request.getParameter("trade_time"));
        String sign_type = request.getParameter("sign_type");
        String signInfo = signStr.toString();
        String dinpaySign =  request.getParameter("sign");
        System.out.println("返回的签名参数排序：" + signInfo.length() + " -->" + signInfo);
        System.out.println("返回的签名：" + dinpaySign.length() + " -->" + dinpaySign);
        boolean result = false;
        // 查询payOrder记录
        String payOrderId = request.getParameter("order_no");
        //PayOrder payOrder = payOrderService.selectPayOrder(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            return false;
        }
        PayChannel payChannel = payChannelService.selectPayChannelById(payOrder.getPayChannelId());
        MinFuConfig minFuConfig1 = minFuConfig.getMinFuPayConfig(payChannel.getParam());
        if("RSA-S".equals(sign_type)){ // sign_type = "RSA-S"
            /**
             1)dinpay_public_key，公钥，每个商家对应一个固定的公钥（不是使用工具生成的商户公钥merchant_public_key，不要混淆），
             即为商家后台"支付管理"->"公钥管理"->"公钥"里的绿色字符串内容
             2)demo提供的dinpay_public_key是测试商户号345123345001的公钥，请自行复制对应商户号的公钥进行调整和替换	*/
            result = RSAWithSoftware.validateSignByPublicKey(signInfo, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMWccxG63zj56xt8ELB+8CJoxqjs8us7NoxhYNGG+PIgnwZ+HAayGr5N1O5EycOWBKIJmX0ZcL1eZ6z/2twxCtppSrl8konPzYd6dG/cVEmmKIDt2czq6Wvkhl8wxKpEUBIVml3kOWyHsRnjdQugFrKQHs8LvFp/oem4V3T/n1qQIDAQAB ", dinpaySign);	// 验签   signInfo返回的签名参数排序， dinpay_public_key公钥， dinpaySign返回的签名
        }
        // 验证签名
        String errorMessage;
        if (!result) {
            errorMessage = "rsaCheckV1 failed.";
            _log.error("AliPay Notify parameter {}", errorMessage);
            return false;
        }
        // 核对金额
        long aliPayAmt = new BigDecimal(request.getParameter("order_amount")).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", request.getParameter("order_amount"), payOrderId);
            return false;
        }
        //查看支付状态
        if (payOrder.getStatus().toString().equals("2")|| payOrder.getStatus().toString().equals("3")) {
            _log.error("支付状态trade_status={},pay_order_id={}不做业务处理 ", payOrder.getStatus(), payOrder.getId());
            return false;
        }
        return true;
    }

    public static void main(String[]args) throws Exception {

        PayOrder order=new PayOrder();
        order.setStatus(new Byte("1"));
        System.out.println(order.getStatus().toString().equals("1"));
        String tt="bank_seq_no=null&interface_version=V3.0&merchant_code=100001005118&notify_id=f4e78d4865cd4711ad7c5ae61acfb22c&notify_type=offline_notify&order_amount=2&order_no=P00201906201638460003&order_time=2019-06-20 16:39:19&trade_no=MF1000497140&trade_status=SUCCESS&trade_time=2019-06-20 16:38:52";
        String dinpaySign="LjFu76aQVPJ6Oq3GWUpa2s1P48B8+buk3IjWzo2hK6whEbMvhRlwBejnQIrksoLVcFoJ2GCWjHEGhwIIXzmSoA6UcVVJnHEWjFR2acG+Xs1JYaa95uRlG/Whdz0BSEZkySDp40LHS7eWPFqvZjKYQHzTM2UTTMBZv2Ul1FqX9/U=";
        Boolean  result = RSAWithSoftware.validateSignByPublicKey(tt, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMWccxG63zj56xt8ELB+8CJoxqjs8us7NoxhYNGG+PIgnwZ+HAayGr5N1O5EycOWBKIJmX0ZcL1eZ6z/2twxCtppSrl8konPzYd6dG/cVEmmKIDt2czq6Wvkhl8wxKpEUBIVml3kOWyHsRnjdQugFrKQHs8LvFp/oem4V3T/n1qQIDAQAB ", dinpaySign);
        // 验签   signInfo返回的签名参数排序， dinpay_public_key公钥， dinpaySign返回的签名
        System.out.println(result);

    }
}
