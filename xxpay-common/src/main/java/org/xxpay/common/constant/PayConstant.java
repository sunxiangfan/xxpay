package org.xxpay.common.constant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 支付常量类
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
public class PayConstant {

    public final static String PAY_CHANNEL_WX_JSAPI = "WX_JSAPI";                // 微信公众号支付
    public final static String PAY_CHANNEL_WX_NATIVE = "WX_NATIVE";                // 微信原生扫码支付
    public final static String PAY_CHANNEL_WX_APP = "WX_APP";                    // 微信APP支付
    public final static String PAY_CHANNEL_WX_MWEB = "WX_MWEB";                    // 微信H5支付
    public final static String PAY_CHANNEL_IAP = "IAP";                            // 苹果应用内支付
    public final static String PAY_CHANNEL_ALIPAY_MOBILE = "ALIPAY_MOBILE";        // 支付宝移动支付
    public final static String PAY_CHANNEL_ALIPAY_PC = "ALIPAY_PC";                // 支付宝PC支付
    public final static String PAY_CHANNEL_ALIPAY_WAP = "ALIPAY_WAP";              // 支付宝WAP支付
    public final static String PAY_CHANNEL_ALIPAY_QR = "ALIPAY_QR";                // 支付宝当面付之扫码支付
    public final static String PAY_CHANNEL_TENGJING_TRANSFER = "TENGJING_TRANSFER"; // 新农行线下转账支付
    public final static String PAY_CHANNEL_BLZQZBS_FAST_PAY = "BLZQZBS_FAST_PAY"; // 快捷1
    public final static String PAY_CHANNEL_XQ316_GATEWAY = "XQ316_GATEWAY"; // 网银2
    public final static String PAY_CHANNEL_MF_GATEWAY = "MF_GATEWAY"; // 网银3

    public final static String PAY_CHANNEL_O9N_FAST_PAY = "O9N_FAST_PAY"; // 快捷1 T1
    public final static String PAY_CHANNEL_THREE10000_GATEWAY = "THREE10000_GATEWAY"; // 网银2
    public final static String PAY_CHANNEL_THREE10000_FAST_PAY = "THREE10000_FAST_PAY"; // 快捷2
    public final static String PAY_CHANNEL_O9N2_FAST_PAY = "O9N2_FAST_PAY"; // 快捷3 D0
    public final static String PAY_CHANNEL_DS_FAST_PAY = "DS_FAST_PAY"; // 快捷3 D0


    public final static String CHANNEL_NAME_WX = "WX";                // 渠道名称:微信
    public final static String CHANNEL_NAME_ALIPAY = "ALIPAY";        // 渠道名称:支付宝
    public final static String CHANNEL_NAME_TENGJING = "TENGJING";        // 渠道名称:新农行
    public final static String CHANNEL_NAME_THREE10000 = "THREE10000";        // 渠道名称:网关1
    public final static String CHANNEL_NAME_O9N = "O9N";        // 渠道名称:快捷T1
    public final static String CHANNEL_NAME_O9N2 = "O9N2";        // 渠道名称:快捷D0
    public final static String CHANNEL_NAME_DS = "DS";        // 渠道名称:快捷DS
    public final static String CHANNEL_NAME_XQ316 = "XQ316";
    public final static String CHANNEL_NAME_MZWYQ = "MZWYQ";//
    public final static String CHANNEL_NAME_HUITONG = "HUITONG";//汇通
    public final static String CHANNEL_NAME_MF = "MF";        // 渠道名称:网关MF(敏付)
    public final static String CHANNEL_NAME_MFDF = "MFDF";        // 渠道名称:代付(敏付)
    public final static String CHANNEL_NAME_CJ_GATEWAY = "CJ_GATEWAY";        // 渠道名称:畅捷网银
    public final static String CHANNEL_NAME_CJ_FAST_PAY = "CJ_FAST_PAY";        // 渠道名称:畅捷快捷
    public final static String CHANNEL_NAME_CJ_FAST_PAY_WAP = "CJ_FAST_PAY_WAP";        // 渠道名称:畅捷快捷（wap前台）


    public final static String CHANNEL_NAME_MF_FAST_PAY = "MF_FAST_PAY";        // 渠道名称:网关MF(敏付)

    public final static String PAY_TYPE_FAST_PAY = "FAST_PAY"; //支付类型：快捷支付
    public final static String PAY_TYPE_GATEWAY = "GATEWAY"; //支付类型：网银支付
    public final static String PAY_TYPE_TRANSFER = "TRANSFER"; //支付类型：线下转账
    public final static String PAY_TYPE_ALIPAY_WAP = "ALIPAY_WAP"; //支付类型：支付宝H5

    public final static byte PAY_STATUS_EXPIRED = -2;    // 订单过期
    public final static byte PAY_STATUS_FAILED = -1;    // 支付失败
    public final static byte PAY_STATUS_INIT = 0;        // 初始态
    public final static byte PAY_STATUS_PAYING = 1;    // 支付中
    public final static byte PAY_STATUS_SUCCESS = 2;    // 支付成功
    public final static byte PAY_STATUS_COMPLETE = 3;    // 业务完成

    public final static byte TRANS_STATUS_INIT = 0;        // 初始态
    public final static byte TRANS_STATUS_TRANING = 1;        // 转账中
    public final static byte TRANS_STATUS_SUCCESS = 2;        // 成功
    public final static byte TRANS_STATUS_FAIL = 3;        // 失败
    public final static byte TRANS_STATUS_COMPLETE = 4;    // 业务完成

    public final static byte TRANS_RESULT_INIT = 0;        // 不确认结果
    public final static byte TRANS_RESULT_REFUNDING = 1;    // 等待手动处理
    public final static byte TRANS_RESULT_SUCCESS = 2;        // 确认成功
    public final static byte TRANS_RESULT_FAIL = 3;        // 确认失败

    public final static byte REFUND_STATUS_INIT = 0;        // 初始态
    public final static byte REFUND_STATUS_REFUNDING = 1;    // 转账中
    public final static byte REFUND_STATUS_SUCCESS = 2;    // 成功
    public final static byte REFUND_STATUS_FAIL = 3;        // 失败
    public final static byte REFUND_STATUS_COMPLETE = 4;    // 业务完成

    public final static byte REFUND_RESULT_INIT = 0;        // 不确认结果
    public final static byte REFUND_RESULT_REFUNDING = 1;    // 等待手动处理
    public final static byte REFUND_RESULT_SUCCESS = 2;    // 确认成功
    public final static byte REFUND_RESULT_FAIL = 3;        // 确认失败

    public final static String MCH_NOTIFY_TYPE_PAY = "1";        // 商户通知类型:支付订单
    public final static String MCH_NOTIFY_TYPE_TRANS = "2";        // 商户通知类型:转账订单
    public final static String MCH_NOTIFY_TYPE_REFUND = "3";    // 商户通知类型:退款订单

    public final static byte MCH_NOTIFY_STATUS_NOTIFYING = 1;    // 通知中
    public final static byte MCH_NOTIFY_STATUS_SUCCESS = 2;        // 通知成功
    public final static byte MCH_NOTIFY_STATUS_FAIL = 3;        // 通知失败


    public final static String RESP_UTF8 = "UTF-8";            // 通知业务系统使用的编码

    public static final String RETURN_PARAM_RETCODE = "retCode";
    public static final String RETURN_PARAM_RETMSG = "retMsg";
    public static final String RESULT_PARAM_RESCODE = "resCode";
    public static final String RESULT_PARAM_ERRCODE = "errCode";
    public static final String RESULT_PARAM_ERRCODEDES = "errCodeDes";
    public static final String RESULT_PARAM_SIGN = "signNew";

    public static final String RETURN_VALUE_SUCCESS = "SUCCESS";
    public static final String RETURN_VALUE_FAIL = "FAIL";

    public static final String RETURN_ALIPAY_VALUE_SUCCESS = "success";
    public static final String RETURN_ALIPAY_VALUE_FAIL = "fail";

    public static class JdConstant {
        public final static String CONFIG_PATH = "jd" + File.separator + "jd";    // 京东支付配置文件路径
    }

    public static class WxConstant {
        public final static String TRADE_TYPE_APP = "APP";                                    // APP支付
        public final static String TRADE_TYPE_JSPAI = "JSAPI";                                // 公众号支付或小程序支付
        public final static String TRADE_TYPE_NATIVE = "NATIVE";                            // 原生扫码支付
        public final static String TRADE_TYPE_MWEB = "MWEB";                                // H5支付

    }

    public static class IapConstant {
        public final static String CONFIG_PATH = "iap" + File.separator + "iap";        // 苹果应用内支付
    }

    public static class AlipayConstant {
        public final static String CONFIG_PATH = "alipay" + File.separator + "alipay";    // 支付宝移动支付
        public final static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY";        // 交易创建,等待买家付款
        public final static String TRADE_STATUS_CLOSED = "TRADE_CLOSED";        // 交易关闭
        public final static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";        // 交易成功
        public final static String TRADE_STATUS_FINISHED = "TRADE_FINISHED";    // 交易成功且结束
    }

    public static final String NOTIFY_BUSI_PAY = "NOTIFY_VV_PAY_RES";
    public static final String NOTIFY_BUSI_TRANS = "NOTIFY_VV_TRANS_RES";

}
