package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.enumm.MchType;
import org.xxpay.mgr.service.MchBankCardService;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.util.SessionUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchBankCard;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.List;

@Controller
@RequestMapping("/mch_bank_card")
public class MchBankCardController {

    private final static MyLog _log = MyLog.getLog(MchBankCardController.class);

    @Autowired
    private MchBankCardService mchBankCardService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "mch_bank_card/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        MchBankCard item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchBankCardService.select(id);
        }
        if (item == null) item = new MchBankCard();
        model.put("item", item);
        return "mch_bank_card/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchBankCard info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        info.setMchType(MchType.MERCHANT.getCode());
        int count = mchBankCardService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchBankCard> userList = mchBankCardService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (MchBankCard mi : userList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        MchBankCard item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchBankCardService.select(id);
        }
        if (item == null) item = new MchBankCard();
        model.put("item", item);
        return "mch_bank_card/view";
    }

}
