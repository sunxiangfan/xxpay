package org.xxpay.agent.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xxpay.agent.service.AgentService;
import org.xxpay.agent.service.MchCompanyService;
import org.xxpay.agent.util.SessionUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.MchCompany;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.agent.service.MchInfoService;

@Controller
@RequestMapping("/mch_info")
public class MchInfoController {

    private final static MyLog _log = MyLog.getLog(MchInfoController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private MchCompanyService mchCompanyService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/index.html")
    public String indexInput(ModelMap model) {
        String mchId = sessionUtil.getLoginInfo().getAgentId();
        Agent item = null;
        if (StringUtils.isNotBlank(mchId)) {
            item = agentService.select(mchId);
        }
        if (item == null) item = new Agent();
        model.put("item", item);
        return "mch_info/index";
    }

}