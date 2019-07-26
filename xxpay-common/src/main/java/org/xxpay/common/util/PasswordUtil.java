package org.xxpay.common.util;

public class PasswordUtil {
    public static String getPassword(String psd, String salt) {
        String encodePsd = String.format("name_%s_psd_%s@xx126", salt, psd);
        String md5Psd = PayDigestUtil.md5(encodePsd, "utf-8").toLowerCase();
        return md5Psd;
    }
    public  static  void main(String[] args){
        String psd="123456";
        String userid="88888888-8888-8888-8888-888888888888";
        String encryptionPsd=getPassword(psd,userid);
        System.out.println(encryptionPsd);
    }
}
