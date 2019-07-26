package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.RandomStrUtils;

@Controller
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/get_mch_key")
    @ResponseBody
    public String getMchKey() {
        String key = RandomStrUtils.getInstance().getMchKey();
        JSONObject object = new JSONObject();
        object.put("code", 0);
        object.put("data", key);
        return object.toJSONString();
    }
}
