package org.xxpay.agent.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.common.enumm.UserType;
import org.xxpay.dal.dao.model.Agent;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.User;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
public class SessionUtil {

    @Autowired
    HttpSession httpSession;

    public LoginInfo getLoginInfo() {
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setMchId("A10001");
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
        private String agentId;
        private String userId;
        private Date loginTime;
        private byte userType;
        private String name;

        public static LoginInfo buildByUser(Agent user) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(user.getId());
            loginInfo.setAgentId(user.getId());
            loginInfo.setUserType(UserType.MERCHANT.getCode());
            loginInfo.setLoginTime(new Date());
            loginInfo.setName(user.getName());
            return loginInfo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
    }
}
