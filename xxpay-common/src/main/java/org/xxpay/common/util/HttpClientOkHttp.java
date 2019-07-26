package org.xxpay.common.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.omg.CORBA.NameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author PaoKing
 * @since 1.0
 */
public class HttpClientOkHttp {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static Response formPost(String url, Map<String, String> params) {
        // 构建一个 Form 表单，使用 Post 提交
        FormBody.Builder fb = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            fb.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = fb.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", USER_AGENT)
                .post(requestBody)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        try {
            return call.execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public  static  Response jsonPost(String url, String json){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
         try {
                    return call.execute();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

    }

}
