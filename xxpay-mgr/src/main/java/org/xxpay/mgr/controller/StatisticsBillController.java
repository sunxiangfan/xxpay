package org.xxpay.mgr.controller;

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
import org.xxpay.dal.dao.model.*;
import org.xxpay.dal.dao.model.dto.BillGroupCreateDateSumAmountModel;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.service.PayChannelService;
import org.xxpay.mgr.service.PayOrderService;
import org.xxpay.mgr.service.StatisticsBillService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/statistics_bill")
public class StatisticsBillController {

    @Autowired
    private StatisticsBillService statisticsBillService;

    @Autowired
    private PayChannelService payChannelService;

    @RequestMapping("list.html")
    public String list(ModelMap model) {
        List<PayChannel> payChannels = payChannelService.getAllList();
        model.put("payChannels", payChannels);
        return "statistics_bill/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute Bill info, Integer page, Integer limit, String dateFrom, String dateTo) throws ParseException {
        Date dateFromValue=null,dateToValue=null;
        if(StringUtils.isNoneBlank(dateFrom) && StringUtils.isNoneBlank(dateTo)){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            dateFromValue=simpleDateFormat.parse(dateFrom);
            dateToValue=simpleDateFormat.parse(dateTo);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(dateToValue);
            calendar.add(Calendar.DATE,1);
            dateToValue=calendar.getTime();
        }

        PageModel pageModel = new PageModel();
        int count = statisticsBillService.countGroupCreateDateByInfo(info,dateFromValue,dateToValue);
        if(count <= 0) return JSON.toJSONString(pageModel);
        List<BillGroupCreateDateSumAmountModel> result = statisticsBillService.groupCreateDateByInfo((page-1)*limit, limit,info,dateFromValue,dateToValue);
        if(!CollectionUtils.isEmpty(result)) {
            JSONArray array = new JSONArray();
            for(BillGroupCreateDateSumAmountModel item : result) {
                JSONObject object = (JSONObject) JSON.toJSON(item);
                if (item.getCreateDate() != null)
                    object.put("createDate", DateUtil.date2Str(item.getCreateDate(), DateUtil.FORMAT_YYYY_MM_DD));

                if (item.getAmount() != null)
                    object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount() + ""));
                if (item.getThirdDeduction() != null)
                    object.put("thirdDeduction", AmountUtil.convertCent2Dollar(item.getThirdDeduction() + ""));
                if (item.getAgentMchCommission() != null)
                    object.put("agentMchCommission", AmountUtil.convertCent2Dollar(item.getAgentMchCommission() + ""));
                if (item.getPlatformCommission() != null)
                    object.put("platformCommission", AmountUtil.convertCent2Dollar(item.getPlatformCommission() + ""));
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
