package org.xxpay.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public class MD5Util{

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    public static String encode(String origin, String charsetname){
        String resultString = null;
        resultString = new String(origin);
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("MD5");
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
        if(charsetname == null || "".equals(charsetname)){
            resultString = Hex.encodeHexString(md.digest(resultString.getBytes()));
        }else{
            try{
                resultString = Hex.encodeHexString(md.digest(resultString.getBytes(charsetname)));
            }catch(UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
        }
        return resultString;
    }
    
    public static String MD5Encode(String origin,String charsetName) {
		origin =origin.trim();
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes(charsetName)));
		} catch (Exception ex) {
		}
		return resultString;
	}
    public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
    /**
	 * J 转换byte到16进制
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	public static void main(String[] args) {
		
		//代付
		String merid="0002900F0345178";
    	String key="123456";
    	String reqtype="payforreq";
    	String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><payforreq><cityno>1000</cityno><ver>1.00</ver><orderno>201702280001</orderno><merdt>20170228</merdt><accntnm>???</accntnm><bankno>0301</bankno><amt>10000</amt><accntno>6222620170000939236</accntno></payforreq>";
    	String str=merid+"|"+key+"|"+reqtype+"|"+xml;
    	System.out.println(str);
    	String mac= MD5Util.MD5Encode(str,"UTF-8");
    	System.out.println();
    	System.out.println(mac);
		
		//项目录入
    	/*String merid="0002220F0306644";
    	String key="u8q09wbd7qh7agxvaa7zbxaj7awndu8c";
    	String xml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><project><ver>2.00</ver><orderno>201702120001</orderno><mchnt_nm>代收付测试商户</mchnt_nm><project_ssn>0001</project_ssn><project_nm>获取项目id 等待扣款 测试</project_nm><project_usage>test扣款</project_usage><project_amt>1000</project_amt><contract_nm>Test001</contract_nm><project_deadline>50</project_deadline><raise_deadline>30</raise_deadline><bor_nm>周标</bor_nm><id_tp>0</id_tp><id_no>420114198811144314</id_no><card_no>6217920100098941</card_no><mobile_no>13818478194</mobile_no><project_desc>测试</project_desc></project>";
    	String str=merid+"|"+key+"|"+xml;
    	System.out.println(str);
    	String mac= MD5Util.MD5Encode(str,"UTF-8");
    	System.out.println();
    	System.out.println(mac);*/
    	//查询交易
    	/*String merid="0002900F0345178";
    	String key="123456";
    	String reqtype="qrytransreq";
    	String xml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><qrytransreq><ver>1.00</ver><busicd>AP01</busicd><orderno>20170221104314</orderno><startdt>20170221</startdt><enddt>20170221</enddt></qrytransreq>";
    	String str=merid+"|"+key+"|"+reqtype+"|"+xml;
    	System.out.println(str);
    	String mac= MD5Util.MD5Encode(str,"UTF-8");
    	System.out.println();
    	System.out.println(mac);*/
    	
    	
	}
}