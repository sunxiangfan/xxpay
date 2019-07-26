package org.xxpay.dal.dao.plugin;

import java.util.List;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class PageModel<T> {

    public List<T> data;
    public Integer count = 0;
    public String msg;
    public Boolean rel;
    private Integer code=0;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getRel() {
        return rel;
    }

    public void setRel(Boolean rel) {
        this.rel = rel;
    }
}
