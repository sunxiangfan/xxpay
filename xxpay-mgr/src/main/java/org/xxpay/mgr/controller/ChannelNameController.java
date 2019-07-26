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
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.ChannelName;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.ChannelNameService;

import java.util.List;

@Controller
@RequestMapping("/channel_name")
public class ChannelNameController {

    private final static MyLog _log = MyLog.getLog(ChannelNameController.class);

    @Autowired
    private ChannelNameService channelNameService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "channel_name/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        ChannelName item = null;
        if (StringUtils.isNotBlank(id)) {
            item = channelNameService.select(id);
        }
        if (item == null) {
            item = new ChannelName();
            model.put("item", item);
            return "channel_name/edit";
        }


        JSONObject object = (JSONObject) JSON.toJSON(item);
        model.put("item", object);
        return "channel_name/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute ChannelName info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = channelNameService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<ChannelName> userList = channelNameService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (ChannelName mi : userList) {
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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult save(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            ChannelName info = new ChannelName();
            String id = po.getString("id");
            info.setName(po.getString("name"));
            int updateRows;
            if (StringUtils.isBlank(id)) {
                // 添加
                updateRows = channelNameService.add(info);
            } else {
                // 修改
                info.setId(id);
                updateRows = channelNameService.update(info);
            }
            _log.info("保存支付类型记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存成功");
            return result;
        } catch (IllegalArgumentException ex) {
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        ChannelName item = null;
        if (StringUtils.isNotBlank(id)) {
            item = channelNameService.select(id);
        }

        if (item == null) {
            item = new ChannelName();
            model.put("item", item);
            return "channel_name/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        model.put("item", object);
        return "channel_name/view";
    }
}
