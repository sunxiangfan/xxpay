package org.xxpay.common.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtilNew {
    private static final Logger logger= LoggerFactory.getLogger(HttpClientUtil.class);
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT =30000;//默认30秒超时

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        //解决Cookie rejected 问题
        configBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);
        requestConfig = configBuilder.build();
    }


    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            logger.info("[http工具类]请求地址:{}请求参数:{}",apiUrl,params);
            response = httpClient.execute(httpPost);
            /*if(302==response.getStatusLine().getStatusCode()){
                Header header = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD 中的
               String newUri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
              return doPost(newUri,params);
            }*/
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
            logger.info("[http工具类]响应内容:{}",httpStr);
        } catch (IOException e) {
            logger.error("[http工具类]请求发生IO异常:",e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    logger.error("[http工具类]请求发生IO异常1:",e);
                }
            }
        }
        return httpStr;
    }


    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param reqParams 请求参数
     * @return
     */
    /*public static String doPostHtml(String apiUrl, String reqParams) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity=new StringEntity(reqParams,"utf-8");
            stringEntity.setContentType("text/xml;charset=utf-8");
            httpPost.setEntity(stringEntity);
            logger.info("[http工具类]请求地址:{}请求参数:{}",apiUrl,reqParams);
            response = httpClient.execute(httpPost);
            if(302==response.getStatusLine().getStatusCode()){
                Header header = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD 中的
                String newUri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
                return doPost(newUri,reqParams);
            }
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
            logger.info("[http工具类]响应内容:{}",httpStr);
        } catch (IOException e) {
            logger.error("[http工具类]请求发生IO异常:",e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    logger.error("[http工具类]请求发生IO异常1:",e);
                }
            }
        }
        return httpStr;
    }*/

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl
     * @param json json对象
     * @return
     */
   /* public static String doPost(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            logger.debug("[http工具类]请求地址:请求参数:",apiUrl,json.toString());
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            logger.debug("[http工具类]响应状态码:",response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            logger.error("[http工具类]请求发生IO异常:",e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    logger.error("[http工具类]请求发生IO异常1:",e);
                }
            }
        }
        return httpStr;
    }*/



}
