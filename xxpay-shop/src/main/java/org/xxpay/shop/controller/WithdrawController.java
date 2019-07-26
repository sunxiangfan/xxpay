package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.PayDigestUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.shop.controller
 * @ClassName: WithdrawController
 * @Description:
 * @CreateDate: 2019/7/2 10:25
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/withdraw")
public class WithdrawController {
    private AtomicLong seq = new AtomicLong(0L);
    static final String baseUrl = "http://106.12.13.47:3010";
    static final String notifyUrl = "http://www.baidu.com";
    static final String reqKey = "yN8Oj38HY88OIIV4VvYAQdKqYrdVhCYCvYRQxbX34DVSYQ8RqwKn4Vh3jqvp3V8d4YQV7VYVHr44NbArhXdxwqKp";


    @RequestMapping("/apply.do")
    public  String withdrawApply(@RequestParam HashMap<String,String> params, HttpServletResponse response){
        String mchId="10007";
        String goodsOrderId = String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        String payType=params.get("payType");
        //String amount=params.get("amount");
        //String centAmount= AmountUtil.convertDollar2Cent(amount);
        long amount = 600;
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                       // 商户ID
        paramMap.put("mchOrderNo", goodsOrderId);           // 商户订单号
        //paramMap.put("payType", payType);             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);             // 支付金额（单位分）
        paramMap.put("currency", "cny");             // 币种
        paramMap.put("number", "6214623521001915597");             // 卡号
        paramMap.put("bankName", "GDB");             // 银行名称
        paramMap.put("registeredBankName", "广发银行");             // 开户行
        paramMap.put("accountName", "王魁圆");             // 账户名
        paramMap.put("mobile", "12545245445");             // 预留手机号
        paramMap.put("province", "31");             // 省
        paramMap.put("city", "2900");             // 市
        paramMap.put("idCard", "412445");             //  //身份证号
        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心申请提现接口,请求数据:" + reqData);
        String url = baseUrl + "/cash/create_order";

        String html = "<html><body onload='form1.submit()'><form id='form1' action='" + url + "' method='post'>"
                + createForm(paramMap)

                + "<button type='submit'>提交</button></form></body></html>";
        try {
            response.setHeader("content-type", "text/html;charset=utf-8");
            response.getWriter().write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String createForm(Map<String, Object> map) {
        String form = "";
        for (String key : map.keySet()) {
            form += String.format("<input type='text' name='%s' value='%s' ></input>", key, map.get(key));
        }
        return form;

    }
}
