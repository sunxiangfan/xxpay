package org.xxpay.agent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.common.enumm.MchType;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.agent.service.SubMchInfoService;

import java.util.List;

@Controller
@RequestMapping("/sub_mch_info")
public class SubMchInfoController {

    private final static MyLog _log = MyLog.getLog(SubMchInfoController.class);

    @Autowired
    private SubMchInfoService subMchInfoService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "sub_mch_info/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchInfo info, Integer page, Integer limit) {
        String loginMchId=sessionUtil.getLoginInfo().getAgentId();
        info.setAgentId(loginMchId);

        PageModel pageModel = new PageModel();
        int count = subMchInfoService.count(info);
        if(count <= 0) return JSON.toJSONString(pageModel);
        List<MchInfo> userList = subMchInfoService.getList((page-1)*limit, limit, info);
        if(!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for(MchInfo mi : userList) {
                mi.cleanBackgroundInfo();
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
        MchInfo item = null;
        if(StringUtils.isNotBlank(id)) {
            item = subMchInfoService.select(id);
            item.cleanBackgroundInfo();
        }
        if(item == null) item = new MchInfo();
        model.put("item", item);
        return "sub_mch_info/view";
    }
}
