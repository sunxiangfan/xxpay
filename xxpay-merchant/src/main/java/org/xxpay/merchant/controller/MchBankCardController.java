package org.xxpay.merchant.controller;

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
import org.xxpay.common.util.BigDecimalUtils;
import org.xxpay.dal.dao.model.Bank;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.merchant.service.BankService;
import org.xxpay.merchant.service.DepositAmountService;
import org.xxpay.merchant.service.MchBankCardService;
import org.xxpay.merchant.service.MchInfoService;
import org.xxpay.merchant.util.SessionUtil;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.MchBankCard;
import org.xxpay.dal.dao.plugin.PageModel;

import java.util.List;

@Controller
@RequestMapping("/mch_bank_card")
public class MchBankCardController {

    private final static MyLog _log = MyLog.getLog(MchBankCardController.class);

    @Autowired
    private MchBankCardService mchBankCardService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private DepositAmountService depositAmountService;
    @Autowired
    private BankService bankService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        String loginMchId = sessionUtil.getLoginInfo().getMchId();
        MchInfo mchInfo = mchInfoService.select(loginMchId);
        Long balance = mchInfo.getBalance();
        Long cashDeduction = mchInfo.getCashDeduction();
        model.put("cashDeduction", AmountUtil.convertCent2Dollar(cashDeduction + ""));
        model.put("lockAmount", AmountUtil.convertCent2Dollar(mchInfo.getLockAmount() + ""));
        if (balance == null) {
            model.put("balance", 0);
            model.put("effectiveBalance", 0);
        } else {
            String balanceDollor = AmountUtil.convertCent2Dollar(balance + "");
            model.put("balance", balanceDollor);

            Long depositAmount = depositAmountService.sumLockedAmountByMchId(loginMchId);
            if (depositAmount == null) {
                depositAmount = 0L;
            }
            model.put("depositAmount", AmountUtil.convertCent2Dollar(depositAmount + ""));
            long effectiveBalance = BigDecimalUtils.sub(balance.longValue(), depositAmount.longValue());
            model.put("effectiveBalance", AmountUtil.convertCent2Dollar(effectiveBalance + ""));
        }
        return "mch_bank_card/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String id, ModelMap model) {
        MchBankCard item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchBankCardService.select(id);
        }
        if (item == null) item = new MchBankCard();
        model.put("item", item);

        List<Bank> banks = bankService.getAllList();
        model.put("banks", banks);
        return "mch_bank_card/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute MchBankCard info, Integer page, Integer limit) {
        String loginMchId = sessionUtil.getLoginInfo().getMchId();
        info.setMchId(loginMchId);

        PageModel pageModel = new PageModel();
        int count = mchBankCardService.count(info);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<MchBankCard> userList = mchBankCardService.getList((page - 1) * limit, limit, info);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (MchBankCard mi : userList) {
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
        SimpleResult result = null;
        try {
            String loginMchId = sessionUtil.getLoginInfo().getMchId();
            JSONObject po = JSONObject.parseObject(params);
            MchBankCard info = new MchBankCard();
            String id = po.getString("id");
            info.setType((byte) ("on".equalsIgnoreCase(po.getString("type")) ? 1 : 0));
            info.setNumber(po.getString("number"));
            info.setRegisteredBankName(po.getString("registeredBankName"));
            info.setBankName(po.getString("bankName"));
            info.setRemark(po.getString("remark"));
            info.setMobile(po.getString("mobile"));
            info.setAccountName(po.getString("accountName"));
            info.setIdCard(po.getString("idCard"));
            info.setProvince(po.getString("province"));
            info.setCity(po.getString("city"));
            Assert.isTrue(StringUtils.isNoneBlank(info.getBankName()), "银行不能为空！");
            Assert.isTrue(StringUtils.isNoneBlank(info.getMobile()), "预留手机号不能为空！");
            Assert.isTrue(StringUtils.isNoneBlank(info.getRegisteredBankName()), "开户行不能为空！");
            Assert.isTrue(StringUtils.isNoneBlank(info.getNumber()), "银行卡号不能为空！");
            Assert.isTrue(StringUtils.isNoneBlank(info.getProvince()), "省不能为空！");
            Assert.isTrue(StringUtils.isNoneBlank(info.getProvince()), "市不能为空！");
            String bankUnionCode = po.getString("bankUnionCode");
            Assert.isTrue(StringUtils.isNoneBlank(bankUnionCode), "开户行银联号不能为空！");
            info.setBankUnionCode(bankUnionCode);
            int updateRows;
            if (StringUtils.isBlank(id)) {
                // 添加
                info.setCreateBy(loginMchId);
                info.setMchId(loginMchId);
                updateRows = mchBankCardService.add(info);
            } else {
                // 修改
                info.setId(id);
                info.setUpdateBy(loginMchId);
                updateRows = mchBankCardService.update(info);
            }
            _log.info("保存商户银行卡记录,返回:{}", result);
            result = SimpleResult.buildSucRes("保存成功！");
        } catch (IllegalArgumentException ex) {
            result = SimpleResult.buildFailRes("保存失败！详情：" + ex.getMessage());
        }
        return result;
    }

    @RequestMapping("/view.html")
    public String viewInput(String id, ModelMap model) {
        MchBankCard item = null;
        if (StringUtils.isNotBlank(id)) {
            item = mchBankCardService.select(id);
        }
        if (item == null) item = new MchBankCard();
        model.put("item", item);
        return "mch_bank_card/view";
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult del(@RequestParam String params) {
        SimpleResult result = null;
        try {
            JSONObject po = JSONObject.parseObject(params);
            String id = po.getString("id");
            Assert.notNull(id, "主键不能为空！");
            int updateRows = mchBankCardService.deleteByPrimaryKey(id);
            _log.info("删除银行卡记录,返回:{}", updateRows);
            result = SimpleResult.buildSucRes("操作成功！");
        } catch (IllegalArgumentException ex) {
            _log.info("删除银行卡记录失败,返回:{}", ex.getMessage());
            result = SimpleResult.buildFailRes("操作失败！详情：" + ex.getMessage());
        }
        return result;
    }
}
