package org.xxpay.service.channel.changjie;


import javax.xml.crypto.Data;

public class ChangJieEntity {

    private String notify_id;
    private String notify_type;
    private Data prnotify_time;
    private String _input_charset;
    private String sign;
    private String sign_type;
    private String version;
    private String outer_trade_no;
    private String inner_trade_no;
    private String status;
    private String pay_msg;
    private String extension;

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public Data getPrnotify_time() {
        return prnotify_time;
    }

    public void setPrnotify_time(Data prnotify_time) {
        this.prnotify_time = prnotify_time;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOuter_trade_no() {
        return outer_trade_no;
    }

    public void setOuter_trade_no(String outer_trade_no) {
        this.outer_trade_no = outer_trade_no;
    }

    public String getInner_trade_no() {
        return inner_trade_no;
    }

    public void setInner_trade_no(String inner_trade_no) {
        this.inner_trade_no = inner_trade_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_msg() {
        return pay_msg;
    }

    public void setPay_msg(String pay_msg) {
        this.pay_msg = pay_msg;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
