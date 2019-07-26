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
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.AgentService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private final static MyLog _log = MyLog.getLog(AgentController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "agent/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(id)) {
            item = agentService.select(id);
        }
        if (item == null) {
            item = new Agent();
        }
        model.put("item", item);
        return "agent/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute Agent info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = agentService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<Agent> userList = agentService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (Agent mi : userList) {
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
            Agent info = new Agent();
            String id = po.getString("id");
            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            String name = po.getString("name");
            Assert.isTrue(StringUtils.isNoneBlank(name), "商户名称不能为空！");
            info.setName(name);
            String mobile = po.getString("mobile");
            Assert.isTrue(StringUtils.isNoneBlank(mobile), "手机号不能为空！");
            info.setMobile(mobile);
            info.setEmail(po.getString("email"));
            String reqKey = po.getString("reqKey");
//            Assert.isTrue(StringUtils.isNoneBlank(reqKey), "请求密钥不能为空！");
            info.setReqKey(reqKey);
            String resKey = po.getString("resKey");
//            Assert.isTrue(StringUtils.isNoneBlank(resKey), "响应密钥不能为空！");
            info.setResKey(resKey);

            String password = po.getString("password");
            password = StringUtils.isNoneBlank(password) ? password : null;
            int updateRows;
            String currentLoginUserId = sessionUtil.getLoginInfo().getUserId();
            info.setPassword(password);
            if (StringUtils.isBlank(id)) {
                // 添加
                Assert.isTrue(StringUtils.isNoneBlank(password), "密码不能为空！");
                info.setCreateBy(currentLoginUserId);
                updateRows = agentService.add(info);
            } else {
                // 修改
                info.setId(id);
                info.setUpdateBy(currentLoginUserId);
                updateRows = agentService.update(info);
            }
            _log.info("保存代理商户信息记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存代理商户信息成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            _log.info("保存代理商户信息记录失败,详情:{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        Agent item = null;
        if (StringUtils.isNotBlank(id)) {
            item = agentService.select(id);
        }
        if (item == null) {
            item = new Agent();
        }
        model.put("item", item);
        return "agent/view";
    }
}
