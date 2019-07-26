package org.xxpay.mgr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.dal.dao.model.User;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
public class SessionUtil {

    @Autowired
    HttpSession httpSession;

    public LoginInfo getLoginInfo() {
//        LoginInfo loginInfo=new LoginInfo();
//        loginInfo.setLoginAccount("admin");
//        loginInfo.setUserId("09dd5c3c-29cf-11e9-803a-00090faa0001");
//        loginInfo.setName("管理员");
//        loginInfo.setDepartment("平台");
//        return  loginInfo;
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
        private String loginAccount;
        private String userId;
        private String name;
        private String department;
        private Date loginTime;

        public static LoginInfo buildByUser(User user) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setLoginAccount(user.getLoginAccount());
            loginInfo.setUserId(user.getUserId());
            loginInfo.setName(user.getName());
            loginInfo.setDepartment(user.getDepartment());
            loginInfo.setLoginTime(new Date());
            return loginInfo;
        }

        public String getLoginAccount() {
            return loginAccount;
        }

        public void setLoginAccount(String loginAccount) {
            this.loginAccount = loginAccount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Date getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
        }
    }
}
