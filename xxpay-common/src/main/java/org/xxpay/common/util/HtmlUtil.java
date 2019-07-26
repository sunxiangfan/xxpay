package org.xxpay.common.util;

import java.util.Map;

public class HtmlUtil {
    private HtmlUtil() {
    }

    public static String createFormHtml(String action, String method, Map<String, String> params, boolean autoSubmit) {
        String html = "<html><head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"><title>支付中 </title></head>";
        html += String.format("<body onload='%s'><form id='form1' action='%s' method='%s'>", autoSubmit ? "form1.submit()" : "", action, method);
        for (String key : params.keySet()) {
            html += String.format("<input type='hidden' name='%s' value='%s' />", key, params.get(key));
        }
        html += "<button type='submit'>提交</button>";
        html += "</form></body></html>";
        return html;
    }
}
