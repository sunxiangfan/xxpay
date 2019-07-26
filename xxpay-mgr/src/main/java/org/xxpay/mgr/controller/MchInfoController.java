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
import org.xxpay.common.enumm.UserType;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PercentUtils;
import org.xxpay.dal.dao.model.MchCompany;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.common.enumm.MchType;
import org.xxpay.dal.dao.model.User;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.MchCompanyService;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.service.UserService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/mch_info")
public class MchInfoController {

    private final static MyLog _log = MyLog.getLog(MchInfoController.class);

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private MchCompanyService mchCompanyService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(String agentId, ModelMap model) {
        if (null != agentId) {
            model.put("agentId", agentId);
        }
        return "mch_info/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, String agentId, ModelMap model) {
        MchInfo item = null;
        MchCompany mchCompany = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchInfoService.select(id);
            mchCompany = mchCompanyService.selectByMchId(id);
        }
        if (item == null) {
            item = new MchInfo();
            item.setAgentId(agentId);
        }
        if (mchCompany == null) {
            mchCompany = new MchCompany();
        }
        JSONObject object=(JSONObject) JSON.toJSON(item);
        if(item.getD0Rate()!=null)
            object.put("d0Rate",PercentUtils.convertDecimal2Percent(item.getD0Rate()+""));
        model.put("item", object);
        model.put("company", mchCompany);
        return "mch_info/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchInfo info, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = mchInfoService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchInfo> userList = mchInfoService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (MchInfo mi : userList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateTime()));
                if (mi.getD0Rate() != null)
                    object.put("d0Rate", PercentUtils.convertDecimal2Percent(mi.getD0Rate() + ""));
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
            MchInfo info = new MchInfo();
            MchCompany company = null;
            String id = po.getString("id");

            if (StringUtils.isNoneBlank(id)) {
                company = mchCompanyService.selectByMchId(id);
                Assert.notNull(company, "无进件信息！商户号： " + id);
            } else {
                company = new MchCompany();
            }

            info.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            String companyName = po.getString("name");
            Assert.isTrue(StringUtils.isNoneBlank(companyName), "商户名称不能为空！");
            info.setName(companyName);
            String mobile = po.getString("mobile");
            Assert.isTrue(StringUtils.isNoneBlank(mobile), "手机号不能为空！");
            info.setMobile(mobile);
            info.setEmail(po.getString("email"));
            String contactName = po.getString("contactName");
            Assert.isTrue(StringUtils.isNoneBlank(contactName), "联系人姓名不能为空！");
            info.setContactName(contactName);
            String reqKey = po.getString("reqKey");
            Assert.isTrue(StringUtils.isNoneBlank(reqKey), "请求密钥不能为空！");
            info.setReqKey(reqKey);
            String resKey = po.getString("resKey");
            Assert.isTrue(StringUtils.isNoneBlank(resKey), "响应密钥不能为空！");
            info.setResKey(resKey);
            info.setD0Rate(Double.parseDouble(PercentUtils.convertPercent2Decimal(po.getString("d0Rate"))));
            company.setCompanyName(companyName);
            company.setCompanyTel(po.getString("companyTel"));
            company.setCorpType(po.getString("corpType"));
            company.setShortName(po.getString("shortName"));
            company.setAddress(po.getString("address"));
            company.setCompanyType(po.getString("companyType"));
            company.setBusinessNo(po.getString("businessNo"));
            company.setBusinessNoExpiryDate(po.getString("businessNoExpiryDate"));
            company.setCorporationName(po.getString("corporationName"));
            company.setCorporationId(po.getString("corporationId"));
            company.setCorporationMobile(po.getString("corporationMobile"));
            company.setIdCardFrontUrl(po.getString("idCardFrontUrl"));
            company.setIdCardBackUrl(po.getString("idCardBackUrl"));
            company.setBusinessNoUrl(po.getString("businessNoUrl"));

            String password = po.getString("password");
            password = StringUtils.isNoneBlank(password) ? password : null;
            info.setPassword(password);
            int updateRows;
            String currentLoginUserId = sessionUtil.getLoginInfo().getUserId();
            if (StringUtils.isBlank(id)) {
                // 添加
                String agentId = po.getString("agentId");
                Assert.isTrue(StringUtils.isNoneBlank(agentId), "代理商户号不能为空！");
                info.setAgentId(agentId);
                Assert.isTrue(StringUtils.isNoneBlank(password), "密码不能为空！");
                info.setCreateBy(currentLoginUserId);
                company.setId(UUID.randomUUID().toString());
                company.setCreateBy(currentLoginUserId);
                updateRows = mchInfoService.add(info, company);
            } else {
                // 修改
                info.setId(id);
                info.setUpdateBy(currentLoginUserId);
                company.setUpdateBy(currentLoginUserId);
                updateRows = mchInfoService.update(info, company);
            }
            _log.info("保存子商户信息记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存代理商户信息成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            _log.info("保存子商户信息记录失败,详情:{}", ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }

    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        MchInfo item = null;
        MchCompany mchCompany = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchInfoService.select(id);
            mchCompany = mchCompanyService.selectByMchId(id);
        }
        if (item == null) item = new MchInfo();
        if (mchCompany == null) mchCompany = new MchCompany();
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getD0Rate() != null)
            object.put("d0Rate", PercentUtils.convertDecimal2Percent(item.getD0Rate() + ""));
        model.put("item", object);
        model.put("company", mchCompany);
        return "mch_info/view";
    }
}
