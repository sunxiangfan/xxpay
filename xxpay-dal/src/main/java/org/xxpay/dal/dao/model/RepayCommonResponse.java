package org.xxpay.dal.dao.model;

/**
 * 代付外层请求参数
 * @author 王奋强
 * @date 2019/7/23
 */
public class RepayCommonResponse {
    private String code;
    private String msg;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
