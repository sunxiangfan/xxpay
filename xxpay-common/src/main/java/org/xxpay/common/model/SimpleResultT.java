package org.xxpay.common.model;

public class SimpleResultT<T> extends SimpleResult {
    private int state;
    private  int code;
    private String msg;
    private T data;

    public  static <T> SimpleResultT<T>  buildSucRes(String msg,T data){
        SimpleResultT<T> result=new SimpleResultT<T>();
        result.setCode(0);
        result.setState(0);
        result.setMsg(msg);
        result.setData(data);
        return  result;
    }

    public  static <T> SimpleResultT<T> buildFailRes(String msg,T data){
        SimpleResultT<T> result=new SimpleResultT<T>();
        result.setCode(400);
        result.setState(400);
        result.setMsg(msg);
        result.setData(data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
