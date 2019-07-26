package org.xxpay.web.service.channel;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.PayDigestUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayO9n2Service {
    public Map<String, Object> buildRequestData(JSONObject payOrder) throws NoSuchAlgorithmException {
        String merchantId = "";
        String keyValue = "";
        String pay_bankcode = "908";
        String pay_memberid = merchantId;//商户id
        String pay_orderid = payOrder.getString("id");//20位订单号 时间戳+6位随机字符串组成
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pay_applydate = simpleDateFormat.format(new Date());//yyyy-MM-dd HH:mm:ss
        String pay_notifyurl = "http://localhost:3030/notify/pay/o9n2PayNotifyRes.htm";//通知地址
//        String pay_callbackurl = payOrder.getString("frontUrl");//回调地址
        String pay_callbackurl = "http://localhost:3030/notify/pay/front_url/" + pay_orderid + ".htm";//回调地址
//        String pay_callbackurl="";
        String pay_amount = AmountUtil.convertCent2Dollar(payOrder.getLong("amount") + "");
        String pay_productname = payOrder.getString("subject");
        String pay_productnum = "";
        String pay_productdesc = payOrder.getString("body");
        Map<String, Object> result = new HashMap<>();
        result.put("pay_memberid", pay_memberid);
        result.put("pay_orderid", pay_orderid);
        result.put("pay_applydate", pay_applydate);
        result.put("pay_bankcode", pay_bankcode);
        result.put("pay_notifyurl", pay_notifyurl);
        result.put("pay_callbackurl", pay_callbackurl);
        result.put("pay_amount", pay_amount);
        String pay_md5sign = PayDigestUtil.getSign(result, keyValue);
        result.put("pay_md5sign", pay_md5sign);

        result.put("pay_productname", pay_productname);
        result.put("pay_productnum", pay_productnum);
        result.put("pay_productdesc", pay_productdesc);

        return result;
    }

    public static String md5(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            //字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
            //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
