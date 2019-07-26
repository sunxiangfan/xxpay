package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.*;
import org.xxpay.mgr.util.SessionUtil;

import java.util.*;

@Controller
@RequestMapping("/platform_withdraw")
public class PlatformWithdrawController {

    private final static MyLog _log = MyLog.getLog(PlatformWithdrawController.class);

    @Autowired
    private PlatformWithdrawService platformWithdrawService;

    @Autowired
    private CashChannelService cashChannelService;


    @RequestMapping("/list.html")
    public String listInput(String mchType, ModelMap model) {
//        List<CashChannel> cashChannels = cashChannelService.getAllEnableList();
//        model.put("cashChannels", cashChannels);
//        model.put("mchType", mchType);
        return "platform_withdraw/list";
    }


    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PlatformWithdraw info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = platformWithdrawService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<PlatformWithdraw> list = platformWithdrawService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();

            List<String> cashChannelIds = new ArrayList<>();
            for (PlatformWithdraw mp : list) {
                cashChannelIds.add(mp.getCashChannelId());
            }

            List<CashChannel> cashChannels = cashChannelService.select(cashChannelIds);

            for (PlatformWithdraw mi : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                if (mi.getApplyAmount() != null)
                    object.put("applyAmount", AmountUtil.convertCent2Dollar(mi.getApplyAmount() + ""));
                if (mi.getThirdDeduction() != null)
                    object.put("thirdDeduction", AmountUtil.convertCent2Dollar(mi.getThirdDeduction() + ""));
                Optional<CashChannel> passagewayOptional = cashChannels.stream().filter(t -> t.getId().equals(mi.getCashChannelId())).findFirst();
                if (passagewayOptional.isPresent()) {
                    CashChannel cashChannel = passagewayOptional.get();
                    object.put("cashChannel", cashChannel);
                }
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel, SerializerFeature.DisableCircularReferenceDetect);
    }

}
