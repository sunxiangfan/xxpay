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
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.enumm.UserType;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.PasswordUtil;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.User;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.UserService;
import org.xxpay.mgr.util.SessionUtil;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final static MyLog _log = MyLog.getLog(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "user/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String userId, ModelMap model) {
        User item = null;
        if (StringUtils.isNotBlank(userId)) {
            item = userService.selectUser(userId);
        }
        if (item == null) item = new User();
        model.put("item", item);
        return "user/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute User user, Integer page, Integer limit) {
        PageModel pageModel = new PageModel();
        int count = userService.count(user);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<User> userList = userService.getUserList((page - 1) * limit, limit, user);
        if (!CollectionUtils.isEmpty(userList)) {
            JSONArray array = new JSONArray();
            for (User mi : userList) {
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
            User user = new User();
            String userId = po.getString("userId");
            user.setLoginAccount(po.getString("loginAccount"));
            String rawPassword = po.getString("password");
            rawPassword = StringUtils.isNoneBlank(rawPassword) ? rawPassword : null;
            user.setPassword(rawPassword);
            user.setName(po.getString("name"));
            user.setMobile(po.getString("mobile"));
            user.setDepartment(po.getString("department"));
            user.setState((byte) ("on".equalsIgnoreCase(po.getString("state")) ? 1 : 0));
            int updateRows;
            if (StringUtils.isBlank(userId)) {
                // 添加
                user.setType(UserType.PLATFORM.getCode());
                user.setCreateBy(sessionUtil.getLoginInfo().getUserId());
                updateRows = userService.addUser(user);
            } else {
                // 修改
                user.setUserId(userId);
                updateRows = userService.updateUser(user);
            }
            _log.info("保存用户记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/change_state", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResult changeState(@RequestParam String params) {
        try {
            JSONObject po = JSONObject.parseObject(params);
            User user = new User();
            String userId = po.getString("userId");
            Assert.notNull(StringUtils.isNoneBlank(userId), "用户id不能为空！");
            String state = po.getString("state");
            Assert.notNull(StringUtils.isNoneBlank(state), "状态不能为空！");
            // 修改
            user.setUserId(userId);
            user.setState(Byte.parseByte(state));
            int updateRows = userService.updateUser(user);

            _log.info("保存用户记录,返回:{}", updateRows);
            SimpleResult result = SimpleResult.buildSucRes("保存成功！");
            return result;
        } catch (IllegalArgumentException ex) {
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("/view.html")
    public String viewInput(String userId, ModelMap model) {
        User item = null;
        if (StringUtils.isNotBlank(userId)) {
            item = userService.selectUser(userId);
        }
        if (item == null) item = new User();
        model.put("item", item);
        return "user/view";
    }
}
