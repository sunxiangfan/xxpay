package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.mq.Mq4PayNotify;
import org.xxpay.service.service.PayOrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class NotifyController extends Notify4BasePay {
    private static final MyLog _log = MyLog.getLog(NotifyController.class);
    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private Mq4PayNotify mq4PayNotify;

    @RequestMapping(value = "/pay/renotify_mch.htm")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        _log.info(">>>>>> PAY开始回调通知业务系统 <<<<<<");
        response.setContentType("application/json;charset=utf-8");
        String payOrderId = request.getParameter("payOrderId");
        // 发起后台通知业务系统
        PayOrder payOrder = null;
        try {
            Assert.isTrue(!StringUtils.isEmpty(payOrderId), "payOrderId不能为空！");
            payOrder = payOrderService.selectPayOrder(payOrderId);
            String notifyUrl = request.getParameter("notifyUrl");
            if (!StringUtils.isEmpty(notifyUrl)) {
                payOrder.setNotifyUrl(notifyUrl);
            }
            Assert.notNull(payOrder, "未找到该支付订单！payOrderId:" + payOrderId);
            boolean paySucceed = payOrder.getStatus().equals(PayConstant.PAY_STATUS_SUCCESS) || payOrder.getStatus().equals(PayConstant.PAY_STATUS_COMPLETE);
            Assert.isTrue(paySucceed, "该订单未支付成功！status:" + payOrder.getStatus());
            JSONObject object = createNotifyInfo(payOrder);
            mq4PayNotify.send(object.toJSONString());
            SimpleResult result = SimpleResult.buildSucRes("通知成功！");
            response.getWriter().print(JSON.toJSONString(result));
        } catch (IllegalArgumentException e) {
            SimpleResult result = SimpleResult.buildFailRes("通知失败！详情：" + e.getMessage());
            response.getWriter().print(JSON.toJSONString(result));
        } catch (Exception e) {
            _log.error("payOrderId={},sendMessage error.", payOrder != null ? payOrder.getId() : "", e);
            SimpleResult result = SimpleResult.buildSucRes("通知出错！");
            response.getWriter().print(JSON.toJSONString(result));
        } finally {
            _log.info(">>>>>> PAY回调通知业务系统完成 <<<<<<");
        }
    }

    @RequestMapping(value = "/pay/front_url/{payOrderId}.htm")
    public void notifyFront(@PathVariable("payOrderId") String payOrderId, HttpServletResponse response) throws IOException {
        _log.info(">>>>>> PAY开始回跳业务系统前端地址 <<<<<<");
        response.setContentType("application/json;charset=utf-8");
        // 发起后台通知业务系统
        PayOrder payOrder = null;
        try {
            Assert.isTrue(!StringUtils.isEmpty(payOrderId), "payOrderId不能为空！");
            payOrder = payOrderService.selectPayOrder(payOrderId);
            Assert.notNull(payOrder, "未找到该支付订单！payOrderId:" + payOrderId);
            String frontUrl = payOrder.getFrontUrl();
            _log.info("单号：{}跳转至: {}", payOrderId, frontUrl);
            response.sendRedirect(frontUrl);
        } catch (IllegalArgumentException e) {
            _log.info(e.getMessage());
            response.getWriter().print("充值成功！");
        } catch (Exception e) {
            _log.error("payOrderId={},front_url error.", payOrder != null ? payOrder.getId() : "", e);
            response.getWriter().print("充值成功！");
        } finally {
            _log.info(">>>>>> PAY回跳业务系统前端地址完成 <<<<<<");
        }
    }
}
