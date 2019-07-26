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
import org.xxpay.common.enumm.BooleanType;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.SystemSetting;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.SettingService;

import java.util.List;

@Controller
@RequestMapping("/system_setting")
public class SystemSettingController {

    private final static MyLog _log = MyLog.getLog(SystemSettingController.class);

    @Autowired
    private SettingService settingService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "system_setting/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        SystemSetting item = null;
        if (StringUtils.isNotBlank(id)) {
            item = settingService.select(id);
        }
        if (item == null) {
            item = new SystemSetting();
            model.put("item", item);
            return "system_setting/edit";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (BooleanType.TRUE.getCode().equals(item.getFlagMoney())) {
            if (item.getParamValue() != null)
                object.put("paramValue", AmountUtil.convertCent2Dollar(item.getParamValue()));
        }
        model.put("item", object);
        return "system_setting/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute SystemSetting info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = settingService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<SystemSetting> list = settingService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(list)) {
            JSONArray array = new JSONArray();
            for (SystemSetting item : list) {
                JSONObject object = (JSONObject) JSONObject.toJSON(item);
                object.put("createTime", DateUtil.date2Str(item.getCreateTime()));
                object.put("updateTime", DateUtil.date2Str(item.getUpdateTime()));
                if (BooleanType.TRUE.getCode().equals(item.getFlagMoney())) {
                    if (item.getParamValue() != null)
                        object.put("paramValue", AmountUtil.convertCent2Dollar(item.getParamValue()));
                }
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
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            Assert.isTrue(StringUtils.isNoneBlank(id), "id不能为空！");
            SystemSetting info = settingService.select(id);
            Assert.notNull(info, "未找到该系统参数！id: " + id);
            Assert.isTrue(BooleanType.TRUE.getCode().equals(info.getFlagEditable()), "该系统参数不允许修改！paramName:" + info.getParamName());

            String paramValue = po.getString("paramValue");
            boolean nullable = BooleanType.TRUE.getCode().equals(info.getFlagNullable());
            Assert.isTrue(!nullable && StringUtils.isNoneBlank(paramValue), "该参数项不能为空！" + info.getParamName());

            if (BooleanType.TRUE.getCode().equals(info.getFlagMoney())) {
                String moneyValue = AmountUtil.convertDollar2Cent(paramValue);
                info.setParamValue(moneyValue);
            }else{
                info.setParamValue(paramValue);
            }
            String paramOrder=po.getString("paramOrder");
            Assert.isTrue(StringUtils.isNoneBlank(paramOrder),"参数排序不能为空！");
            info.setParamOrder(Integer.parseInt(paramOrder));
            info.setVersion(info.getVersion() + 1);
            int updateRows = settingService.update(info);
            _log.info("保存系统设置记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            _log.info("保存系统设置记录失败,详情:{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        SystemSetting item = null;
        if (StringUtils.isNotBlank(id)) {
            item = settingService.select(id);
        }

        if (item == null) {
            item = new SystemSetting();
            model.put("item", item);
            return "system_setting/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (BooleanType.TRUE.getCode().equals(item.getFlagMoney())) {
            if (item.getParamValue() != null)
                object.put("paramValue", AmountUtil.convertCent2Dollar(item.getParamValue()));
        }
        model.put("item", object);
        return "system_setting/view";
    }
}
