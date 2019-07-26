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
import org.xxpay.dal.dao.model.Bank;
import org.xxpay.dal.dao.model.ChannelName;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayType;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.BankService;
import org.xxpay.mgr.service.ChannelNameService;
import org.xxpay.mgr.service.PayChannelService;
import org.xxpay.mgr.service.PayTypeService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;

@Controller
@RequestMapping("/bank")
public class BankController {

    private final static MyLog _log = MyLog.getLog(BankController.class);

    @Autowired
    private BankService bankService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "bank/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        Bank item = null;
        if (StringUtils.isNotBlank(id)) {
            item = bankService.select(id);
        }
        if (item == null) item = new Bank();
        model.put("item", item);
        return "bank/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute Bank info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = bankService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<Bank> payChannelList = bankService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(payChannelList)) {
            JSONArray array = new JSONArray();
            for (Bank pc : payChannelList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(pc);
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
            String name=po.getString("name");
            Assert.isTrue(StringUtils.isNoneBlank(name), "银行名称不能为空！");

            String code = po.getString("code");
            Assert.isTrue(StringUtils.isNoneBlank(code), "银行编码不能为空！");
            code=code.toUpperCase();

            Bank info=new Bank();
            info.setName(name);
            info.setCode(code);
            String id = po.getString("id");
            int updateRows;
            if (id == null) {
                // 添加
                updateRows = bankService.add(info);
            } else {
                // 修改
                info.setId(id);
                updateRows = bankService.update(info);
            }
            _log.info("保存银行名称记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("保存银行名称失败，详情：{}", ex.getMessage());
            result = SimpleResult.buildFailRes(ex.getMessage());
        }
        return result;
    }


}