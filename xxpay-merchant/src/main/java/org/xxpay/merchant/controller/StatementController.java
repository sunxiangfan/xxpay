package org.xxpay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.model.StatementModel;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.merchant.service.PayOrderService;
import org.xxpay.merchant.util.SessionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private PayOrderService payOrderService;

    @RequestMapping("list.html")
    public String list(ModelMap model) {
        return "statement/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayOrder info, Integer page, Integer limit, String dateFrom, String dateTo) throws ParseException {
        Date dateFromValue = null, dateToValue = null;
        if (StringUtils.isNoneBlank(dateFrom) && StringUtils.isNoneBlank(dateTo)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFromValue = simpleDateFormat.parse(dateFrom);
            dateToValue = simpleDateFormat.parse(dateTo);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateToValue);
            calendar.add(Calendar.DATE, 1);
            dateToValue = calendar.getTime();
        }
        if (null == info) {
            info = new PayOrder();
        }
        info.setMchId(sessionUtil.getLoginInfo().getMchId());
        PageModel pageModel = new PageModel();
        int count = payOrderService.countStatementByInfo(info, dateFromValue, dateToValue);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<StatementModel> result = payOrderService.statementByInfo((page - 1) * limit, limit, info, dateFromValue, dateToValue);
        if (!CollectionUtils.isEmpty(result)) {
            Set<String> mchIds = new HashSet<>();
            for (StatementModel mp : result) {
                mchIds.add(mp.getMchId());
            }
            String mchId = sessionUtil.getLoginInfo().getMchId();
            String mchInfoName = sessionUtil.getLoginInfo().getName();
            JSONArray array = new JSONArray();
            for (StatementModel item : result) {
                JSONObject object = (JSONObject) JSON.toJSON(item);
                if (item.getCreateDate() != null)
                    object.put("createDate", DateUtil.date2Str(item.getCreateDate(), DateUtil.FORMAT_YYYY_MM_DD));

                if (item.getApplyAmount() != null)
                    object.put("applyAmount", AmountUtil.convertCent2Dollar(item.getApplyAmount() + ""));
                if (item.getSuccessAmount() != null)
                    object.put("successAmount", AmountUtil.convertCent2Dollar(item.getSuccessAmount() + ""));
                if (item.getSuccessActualAmount() != null)
                    object.put("successActualAmount", AmountUtil.convertCent2Dollar(item.getSuccessActualAmount() + ""));

                JSONObject mchInfo = new JSONObject();
                mchInfo.put("id", mchId);
                mchInfo.put("name", mchInfoName);
                object.put("mchInfo", mchInfo);
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
