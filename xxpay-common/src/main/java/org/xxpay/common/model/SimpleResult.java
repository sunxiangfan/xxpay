package org.xxpay.common.model;

public class SimpleResult {
    private int state;
    private  int code;
    private String msg;

    public  static  SimpleResult buildSucRes(String msg){
        SimpleResult result=new SimpleResult();
        result.setCode(0);
        result.setState(0);
        result.setMsg(msg);
        return  result;
    }

    public  static  SimpleResult buildFailRes(String msg){
        SimpleResult result=new SimpleResult();
        result.setCode(400);
        result.setState(400);
        result.setMsg(msg);
        return  result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
