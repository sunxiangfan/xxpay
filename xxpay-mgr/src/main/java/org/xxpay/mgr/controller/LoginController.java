package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.User;
import org.xxpay.mgr.service.MchInfoService;
import org.xxpay.mgr.service.UserService;
import org.xxpay.mgr.util.SessionUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Controller
public class LoginController {
    private final static MyLog _log = MyLog.getLog(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping("/login.html")
    public String index(ModelMap model) {
        return "login/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam String params) {
        JSONObject po = JSONObject.parseObject(params);
        String loginAccount = po.getString("userName");
        String password = po.getString("password");
        String verifyCode=po.getString("verifyCode");
        try {
            Assert.isTrue(StringUtils.isNoneBlank(verifyCode),"验证码不能为空！");
            Assert.isTrue(verifyCode.equalsIgnoreCase(sessionUtil.getVerifyCode()),"验证码不正确！");
            User loginUser = userService.login(loginAccount, password);
            SessionUtil.LoginInfo loginInfo = SessionUtil.LoginInfo.buildByUser(loginUser);
            sessionUtil.setLoginInfo(loginInfo);
            SimpleResult result = SimpleResult.buildSucRes("登录成功！");
            JSONObject object = (JSONObject) JSON.toJSON(result);
            object.put("loginInfo", loginInfo);
            _log.info("总后台登录成功。账号：{}", loginAccount);
            return object.toJSONString();
        } catch (IllegalArgumentException ex) {
            _log.info("总后台登录失败。账号：{}，信息：{}", loginAccount, ex.getMessage());
            SimpleResult result = SimpleResult.buildFailRes("登录失败！详情：" + ex.getMessage());
            return JSON.toJSONString(result);
        }
    }

    @RequestMapping("/verify.jpg")
    public  void vrifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            sessionUtil.setVerifyCode(createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
