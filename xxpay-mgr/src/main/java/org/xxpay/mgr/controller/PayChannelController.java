package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.ChannelName;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.ChannelNameService;
import org.xxpay.mgr.service.PayChannelService;
import org.xxpay.mgr.service.PayTypeService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pay_channel")
public class PayChannelController {

    private final static MyLog _log = MyLog.getLog(PayChannelController.class);

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private PayTypeService payTypeService;

    @Autowired
    private ChannelNameService channelNameService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "pay_channel/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        PayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = payChannelService.select(id);
        }
        if (item == null) item = new PayChannel();
        model.put("item", item);
        List<PayType> payTypes = payTypeService.getAllList();
        model.put("payTypes", payTypes);
        List<ChannelName> channelNames = channelNameService.getAllList();
        model.put("channelNames", channelNames);

        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getMaxTransactionAmount() != null)
            object.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(item.getMaxTransactionAmount() + ""));
        if (item.getMinTransactionAmount() != null)
            object.put("minTransactionAmount", AmountUtil.convertCent2Dollar(item.getMinTransactionAmount() + ""));
        if (item.getThirdDeductionRate() != null)
            object.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(item.getThirdDeductionRate() + ""));
        if (item.getT1Rate() != null)
            object.put("t1Rate", PercentUtils.convertDecimal2Percent(item.getT1Rate() + ""));
        model.put("item", object);
        return "pay_channel/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayChannel payChannel, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = payChannelService.count(payChannel);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<PayChannel> payChannelList = payChannelService.getList((page - 1) * limit, limit, payChannel);
        if (!CollectionUtils.isEmpty(payChannelList)) {
            JSONArray array = new JSONArray();
            for (PayChannel pc : payChannelList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
                object.put("createTime", DateUtil.date2Str(pc.getCreateTime()));
                if (pc.getMaxTransactionAmount() != null)
                    object.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(pc.getMaxTransactionAmount() + ""));
                if (pc.getMinTransactionAmount() != null)
                    object.put("minTransactionAmount", AmountUtil.convertCent2Dollar(pc.getMinTransactionAmount() + ""));
                if (pc.getThirdDeductionRate() != null)
                    object.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(pc.getThirdDeductionRate() + ""));
                if (pc.getT1Rate() != null)
                    object.put("t1Rate", PercentUtils.convertDecimal2Percent(pc.getT1Rate() + ""));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult save(@RequestParam String params) {
        SimpleResult result;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String channelName=po.getString("name");
            Assert.isTrue(StringUtils.isNoneBlank(channelName), "渠道名称不能为空！");

            String payType = po.getString("payType");
            Assert.isTrue(StringUtils.isNoneBlank(payType), "支付方式不能为空！");
            String label=po.getString("label");
            Assert.notNull(label,"支付通道名称不能为空！");

            String code = PayChannelUtils.buildChannelCode(channelName,payType);
            String param = po.getString("param");
            PayChannel info = new PayChannel();
            info.setName(channelName);
            info.setPayType(payType);
            info.setCode(code);
            String id = po.getString("id");
            info.setMaterial((byte) ("on".equalsIgnoreCase(po.getString("material")) ? 1 : 0));
            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            info.setAccountingCycle(po.getString("accountingCycle"));
            info.setMaxTransactionAmount(Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("maxTransactionAmount"))));
            info.setMinTransactionAmount(Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("minTransactionAmount"))));
            info.setStartTime(po.getString("startTime"));
            info.setEndTime(po.getString("endTime"));
            info.setThirdDeductionRate(Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("thirdDeductionRate"))));
            info.setT1Rate(Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("t1Rate"))));
            info.setLabel(label);
            info.setParam(param);
            info.setRemark(po.getString("remark"));
            int updateRows;
            if (id == null) {
                // 添加
                info.setCreateBy(sessionUtil.getLoginInfo().getUserId());
                updateRows = payChannelService.add(info);
            } else {
                // 修改
                info.setId(id);
                updateRows = payChannelService.update(info);
            }
            _log.info("保存渠道记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("保存渠道失败，详情：{}", ex.getMessage());
            result = SimpleResult.buildFailRes(ex.getMessage());
        }
        return result;
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        PayChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = payChannelService.select(id);
        }
        if (item == null) {
            item = new PayChannel();
            model.put("item", item);
            return "pay_channel/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getMaxTransactionAmount() != null)
            object.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(item.getMaxTransactionAmount() + ""));
        if (item.getMinTransactionAmount() != null)
            object.put("minTransactionAmount", AmountUtil.convertCent2Dollar(item.getMinTransactionAmount() + ""));
        if (item.getThirdDeductionRate() != null)
            object.put("thirdDeductionRate", PercentUtils.convertDecimal2Percent(item.getThirdDeductionRate() + ""));
        if (item.getT1Rate() != null)
            object.put("t1Rate", PercentUtils.convertDecimal2Percent(item.getT1Rate() + ""));
        model.put("item", object);
        return "pay_channel/view";
    }

}