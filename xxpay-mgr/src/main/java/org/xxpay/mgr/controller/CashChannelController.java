package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.model.CashChannel;
import org.xxpay.dal.dao.model.ChannelName;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.CashChannelService;
import org.xxpay.mgr.service.ChannelNameService;
import org.xxpay.mgr.service.PayChannelService;
import org.xxpay.mgr.service.PayTypeService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;

@Controller
@RequestMapping("/cash_channel")
public class CashChannelController {

    private final static MyLog _log = MyLog.getLog(CashChannelController.class);

    @Autowired
    private CashChannelService cashChannelService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private PayTypeService payTypeService;

    @Autowired
    private ChannelNameService channelNameService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "cash_channel/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute CashChannel cashChannel, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = cashChannelService.count(cashChannel);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<CashChannel> payChannelList = cashChannelService.getList((page - 1) * limit, limit, cashChannel);
        if (!CollectionUtils.isEmpty(payChannelList)) {
            JSONArray array = new JSONArray();
            for (CashChannel pc : payChannelList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
                object.put("createTime", DateUtil.date2Str(pc.getCreateTime()));
                if (pc.getMaxTransactionAmount() != null)
                    object.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(pc.getMaxTransactionAmount() + ""));
                if (pc.getMinTransactionAmount() != null)
                    object.put("minTransactionAmount", AmountUtil.convertCent2Dollar(pc.getMinTransactionAmount() + ""));
                if (pc.getThirdDeduction() != null)
                    object.put("thirdDeduction", AmountUtil.convertCent2Dollar(pc.getThirdDeduction() + ""));
                array.add(object);
            }
            pageModel.setData(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }
    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        CashChannel item = null;
        if (StringUtils.isNotBlank(id)) {
            item = cashChannelService.select(id);
        }
        if (item == null) item = new CashChannel();
        model.put("item", item);

        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getMaxTransactionAmount() != null)
            object.put("maxTransactionAmount", AmountUtil.convertCent2Dollar(item.getMaxTransactionAmount() + ""));
        if (item.getMinTransactionAmount() != null)
            object.put("minTransactionAmount", AmountUtil.convertCent2Dollar(item.getMinTransactionAmount() + ""));
        if (item.getThirdDeduction() != null)
            object.put("thirdDeduction", AmountUtil.convertCent2Dollar(item.getThirdDeduction() + ""));
        model.put("item", object);
        return "cash_channel/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult save(@RequestParam String params) {
        SimpleResult result;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String channelName=po.getString("name");
            Assert.isTrue(StringUtils.isNoneBlank(channelName), "渠道名称不能为空！");

            String label=po.getString("label");
            Assert.notNull(label,"支付通道名称不能为空！");

            String param = po.getString("param");
            CashChannel info = new CashChannel();
            info.setName(channelName);

            String id = po.getString("id");
            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            info.setMaxTransactionAmount(Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("maxTransactionAmount"))));
            info.setMinTransactionAmount(Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("minTransactionAmount"))));
            info.setThirdDeduction(Long.parseLong(AmountUtil.convertDollar2Cent(po.getString("thirdDeduction"))));

            info.setLabel(label);
            info.setParam(param);
            info.setRemark(po.getString("remark"));
            int updateRows;
            if (id == null) {
                // 添加
                updateRows = cashChannelService.add(info);
            } else {
                // 修改
                info.setId(id);
                updateRows = cashChannelService.update(info);
            }
            _log.info("保存代付渠道记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("保存代付渠道失败，详情：{}", ex.getMessage());
            result = SimpleResult.buildFailRes(ex.getMessage());
        }
        return result;
    }


}