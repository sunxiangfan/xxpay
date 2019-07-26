package org.xxpay.mgr.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itrus.util.sign.RSAWithSoftware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.HttpClientUtilNew;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.MchWithdrawApply;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.mgr.util.MinFuUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.mgr.service.channel
 * @ClassName: ChannelMinFu
 * @Description:
 * @CreateDate: 2019/7/2 11:53
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ChannelMinFu {

    private final MyLog _log = MyLog.getLog(ChannelMinFu.class);
    private static final String mfTransferUrl="https://transfer.minfupay.cn/transfer";

    private static  final  String MCH_ID="100001005119";

    private static final String mchKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANoZDMbLBFOfdtCA\n" +
            "GUieVjP4+ABHhTbmWzHvfhpZcuGcyQDHBUvJMpUfhccEfOOo4DlP+UUejne4jdHA\n" +
            "Czc4Sa+IQMLcZTIIkZBL/WzhVIK/mQ96PapyQY/shBmt+5MhNfhgwwyz0NDeLTEK\n" +
            "2slZK1Au4MrzozsS/MJswU4386qFAgMBAAECgYAL4ItalBnDOA7vYdp+oujM4cUX\n" +
            "ts1hZQAYYxN5+d8QvEoR+cSnEOjpSBMv+GWYvEyhsmI/yl81094dSxkRQ8Z3Z/Xe\n" +
            "rqToG6r6ZDGn4NtNiuXNnF1nS+vIGVYOFsMhBehnt7ZRalLdNsVqJHLCYD/tbzdJ\n" +
            "gR11LV7ZhwmSZTjJDQJBAPAcNcbO9/yx7z2aRHr6dptGF1qdh20W27W7d9zUfkdb\n" +
            "2dwZKKTV9XEGbm2Z5mKIe11frFwIbjWFELTUWGsgHu8CQQDoh+vNGTikbznTfp/v\n" +
            "Vziry4JC1+TzlnBr/tQcqtzUAuNVZE4HEdfwFUO8ZAvZIj6JSyC3GKnNU584dNrq\n" +
            "pw3LAkBbwwwyPGq9oerCajB9pzaoxLKsKMPWxwOUgl6egH76Gno6CUtlI3e3iDOv\n" +
            "oz9OrMYouyWNzpL/1Si7UuOvnQj1AkA0VZMOoCSNbbMYFKGuXnUUxULWBH01DaDT\n" +
            "zZCzuwjkdpnvuBmPocZ19HNATUwVXI2ynMQKMXTWD4IAVNyIf3ytAkEA5x72ADxb\n" +
            "om00nOZaySYZ2YkdXHVyKbt4caAB0NwFt1dO0E1FlvKRmWVJGjU/PakWTJsbHVN3\n" +
            "fnPyECLXAc+sCg==";
    /**
     * 敏付企业转账接口（企业转账）
     * 文档：https://merchants.minfupay.cn/merchantUserLogin
     *
     * @param apply
     * @return
     */
    public JSONObject doMinFuTransferReq(MchWithdrawApply apply, long actualApplyAmount) {
        _log.info("===========敏付 代付接口开始处理============");
            String logPrefix = "【企业转账】";
            JSONObject result = new JSONObject();
            try {
                Map<String,Object> requestParam=buildDoMinFuTransferReq(apply, actualApplyAmount);
                //requestParam.put("payOrderId", payOrder.getId());
                //int result = payOrderService.updateStatus4Ing(payOrder.getId(), null);
                //_log.info("更新第三方支付订单号:payOrderId={},result={}", payOrder.getId(), result);
                String resp= HttpClientUtilNew.doPost(mfTransferUrl,requestParam);
                Map<String,Object> response= MinFuUtil.parseXml("minfupay",resp); /*response.get("is_success")*/ ;
                if ("0".equals(response.get("result_code"))){
                    //正常返回
                    result.put("state", true);
                    result.put("msg", response.get("recv_info").toString());
                }else {
                    result.put("state", false);
                    result.put("msg", response.get("recv_info").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                _log.info("{}企业转账请求失败：message={}", logPrefix, e.getMessage());
                result.put("state", false);
                result.put("msg", "企业转账请求失败");
            }
            return result;
    }
    private Map<String, Object> buildDoMinFuTransferReq(MchWithdrawApply apply, long actualApplyAmount) throws Exception {
        Map<String,Object> requestParam=new HashMap<>();
        transferVerifySign(apply,requestParam);
        requestParam.put("sign_type","RSA-S");
        return requestParam;
    }

    private void transferVerifySign(MchWithdrawApply apply,Map<String,Object> requestParam) throws Exception {
        StringBuffer signSrc = new StringBuffer();
        requestParam.put("interface_version", "V3.1.0");
        requestParam.put("mer_transfer_no", apply.getId());
        requestParam.put("merchant_no", MCH_ID);//商户号
        requestParam.put("tran_code", "DMTI");
        requestParam.put("recv_bank_code", apply.getBankName());
        requestParam.put("recv_accno", apply.getNumber());
        requestParam.put("recv_name", apply.getAccountName());
        requestParam.put("recv_province", apply.getProvince());
        requestParam.put("recv_city", apply.getCity());
        requestParam.put("tran_amount", "2.00");
        requestParam.put("tran_fee_type", "0");
        requestParam.put("tran_type", "0");
        signSrc.append("interface_version=").append("V3.1.0").append("&");
        signSrc.append("mer_transfer_no=").append(requestParam.get("mer_transfer_no")).append("&");
        signSrc.append("merchant_no=").append(MCH_ID).append("&");
        signSrc.append("recv_accno=").append(apply.getNumber()).append("&");
        signSrc.append("recv_bank_code=").append(apply.getBankName()).append("&");
        signSrc.append("recv_city=").append(apply.getCity()).append("&");
        signSrc.append("recv_name=").append(apply.getAccountName()).append("&");
        signSrc.append("recv_province=").append(apply.getProvince()).append("&");
        signSrc.append("tran_amount=").append("2.00").append("&");
        signSrc.append("tran_code=").append("DMTI").append("&");
        signSrc.append("tran_fee_type=").append("0").append("&"); //0：从转账金额中扣除 1：从账户余额中扣除
        signSrc.append("tran_type=").append("0");//0普通转账
        String signInfo = signSrc.toString();
        String sign = "" ;
        if("RSA-S".equals("RSA-S")) {
            sign = RSAWithSoftware.signByPrivateKey(signInfo,mchKey) ;  // 商家签名（签名后报文发往dinpay）
            _log.info("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            _log.info("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        requestParam.put("sign_info",sign);
    }

}
