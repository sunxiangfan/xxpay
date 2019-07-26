package org.xxpay.merchant.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.common.enumm.MchType;
import org.xxpay.common.enumm.UserType;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.User;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
public class SessionUtil {

    @Autowired
    HttpSession httpSession;

    public LoginInfo getLoginInfo() {
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setParentId("A10001");
//        loginInfo.setMchId("10002");
//        return loginInfo;
        return (LoginInfo) httpSession.getAttribute("loginInfo");
    }

    public void setLoginInfo(LoginInfo info) {
        httpSession.setAttribute("loginInfo", info);
    }
    public String getVerifyCode() {
        return (String) httpSession.getAttribute("verifyCode");
    }

    public void setVerifyCode(String verifyCode) {
        httpSession.setAttribute("verifyCode", verifyCode);
    }

    public static class LoginInfo {
        private  String userId;
        private String mchId;
        private String parentId;
        private  byte userType;
        private  String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private Date loginTime;
        public static LoginInfo buildByMchInfo(MchInfo mchInfo) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(mchInfo.getId());
            loginInfo.setMchId(mchInfo.getId());
            loginInfo.setParentId(mchInfo.getAgentId());
            loginInfo.setUserType(UserType.SUB_MERCHANT.getCode());
            loginInfo.setLoginTime(new Date());
            loginInfo.setName(mchInfo.getName());
            return loginInfo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public byte getUserType() {
            return userType;
        }

        public void setUserType(byte userType) {
            this.userType = userType;
        }

        public Date getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
        }
    }
}
