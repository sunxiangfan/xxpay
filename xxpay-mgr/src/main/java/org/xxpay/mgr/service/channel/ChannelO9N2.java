package org.xxpay.mgr.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.xxpay.common.constant.CashConstant;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.HttpClientOkHttp;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PayDigestUtil;
import org.xxpay.dal.dao.model.MchWithdrawApply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class ChannelO9N2 {
    private static  final  String MCH_ID="";
    private static final String KEY = "";
    private final static MyLog _log = MyLog.getLog(ChannelO9N2.class);
    public JSONObject apply(MchWithdrawApply apply, long actualApplyAmount) throws UnsupportedEncodingException, IllegalAccessException, IOException {
        _log.info("===========ChannelO9N2 代付接口开始处理============");
        _log.info("===========提现申请单id: {}============", apply.getId());
        String payUrl = "";
        Map<String, String> signData = new HashMap<>();
        signData.put("mchid", MCH_ID);
        signData.put("out_trade_no", apply.getId());
        signData.put("money", AmountUtil.convertCent2Dollar(actualApplyAmount + ""));
        signData.put("bankname", apply.getBankName());
        signData.put("subbranch", apply.getRegisteredBankName());
        signData.put("accountname", apply.getAccountName());
        signData.put("cardnumber", apply.getNumber());
        signData.put("province", apply.getProvince());
        signData.put("city", apply.getCity());
        JSONObject extendsObj = new JSONObject();
        extendsObj.put("mobile", apply.getMobile());
        String extendsJson = extendsObj.toJSONString();
        String extendsStr = new String(Base64Utils.encode(extendsJson.getBytes("UTF-8")), "UTF-8");
        signData.put("extends", extendsStr);
        String sign = PayDigestUtil.getSign(signData, KEY);
        signData.put("pay_md5sign", sign);

        try (Response res = HttpClientOkHttp.formPost(payUrl, signData)) {
            String json = res.body().string();
            JSONObject resultJson = JSON.parseObject(json);
            JSONObject result = new JSONObject();
            _log.info("===========ChannelO9N2 代付接口返回============");
            _log.info(json);
            result.put("state", "success".equalsIgnoreCase(resultJson.getString("status")));
            result.put("msg", resultJson.getString("msg"));
            result.put("channelOrderNo", resultJson.getString("transaction_id"));
            return result;
        }
    }

    public JSONObject queryCashState(String cashOrderId) throws Exception {
        String payUrl = "";
        Map<String, String> signData = new HashMap<>();
        signData.put("mchid", MCH_ID);
        signData.put("out_trade_no", cashOrderId);
        String sign = PayDigestUtil.getSign(signData, KEY);
        signData.put("pay_md5sign", sign);
        try (Response res = HttpClientOkHttp.formPost(payUrl, signData)) {
            String json = res.body().string();
            JSONObject resultJson = JSON.parseObject(json);
            Assert.isTrue("success".equals(resultJson.getString("status")),"返回失败！"+resultJson.getString("msg"));
            String refCode=resultJson.getString("refCode");
            //refCode: 1成功  2 失败 3 处理中 4待处理其他待确认
            JSONObject result=new JSONObject();
            if("1".equals(refCode)){
                result.put("state", CashConstant.CASH_STATUS_SUCCESS);
                result.put("amount",AmountUtil.convertDollar2Cent(resultJson.getString("amount")));
                result.put("cashSuccTime",resultJson.getString("success_time"));
                result.put("cashOrderId",resultJson.getString("out_trade_no"));
            }
            else if("2".equalsIgnoreCase(refCode)){
                result.put("state",CashConstant.CASH_STATUS_FAIL);
                result.put("cashOrderId",resultJson.getString("out_trade_no"));
            }
            else {
                result.put("state",CashConstant.CASH_STATUS_APPLY);
                result.put("cashOrderId",resultJson.getString("out_trade_no"));
            }
            return  result;
        }

    }
}
