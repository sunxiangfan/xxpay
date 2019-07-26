package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.merchant.service.MchInfoService;
import org.xxpay.merchant.util.SessionUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchInfo;

@Controller
@RequestMapping("/mch_info")
public class MchInfoController {

    private final static MyLog _log = MyLog.getLog(MchInfoController.class);

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/index.html")
    public String indexInput( ModelMap model) {
        String mchId=sessionUtil.getLoginInfo().getMchId();
        MchInfo item = null;
        if(StringUtils.isNotBlank(mchId)) {
            item = mchInfoService.select(mchId);
        }
        if(item == null) item = new MchInfo();

        JSONObject object= (JSONObject) JSON.toJSON(item);
        if(item.getD0Rate() !=null)
            object.put("d0Rate", PercentUtils.convertDecimal2Percent(item.getD0Rate()+""));
        model.put("item", object);
        return "mch_info/index";
    }

}