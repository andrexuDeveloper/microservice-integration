package com.keruyun.infra.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {


    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final Integer CONNECTION_TIMEOUT = 1000;
    private static final Integer SO_TIMEOUT = 2000;
    public static final PoolingClientConnectionManager httpConnectionPool = new PoolingClientConnectionManager();

    static {
        httpConnectionPool.setMaxTotal(200);
        httpConnectionPool.setDefaultMaxPerRoute(20);
    }

    /**
     * http get 方法返回string
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGet(String url) throws Exception {
        return httpGet(url, 0);
    }


    /**
     * http get 方法返回string
     *
     * @param timeout 连接过期时间，单位毫秒
     * @return
     * @throws IOException
     */
    public static String httpGet(String url, int timeout) throws Exception {
        HttpGet httpGet = null;
        HttpClient httpClient = null;
        try {
            httpClient = getHttpClient(timeout);
            httpGet = new HttpGet(replace(url));
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(response.getEntity());
                }
                return null;
            } else {
                throw new Exception("请求URL:" + url + "失败, HttpStatus: " + statusCode);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new Exception("请求URL:" + url + "失败: " + e.getMessage());
        } finally {// 释放连接资源
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    /**
     * post 提交
     *
     * @param url
     * @param content
     * @param contentType
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, String content, String contentType, String encoding) throws Exception {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        HttpPost httpPost = null;
        HttpClient httpClient = null;

        try {
            httpClient = getHttpClient(0);
            HttpPost httppost = new HttpPost(url);
            if (StringUtils.isNotBlank(contentType)) {
                httppost.addHeader("Content-Type", contentType);
            }
            httppost.setEntity(new StringEntity(content, encoding));
            HttpResponse response = httpClient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(response.getEntity());
                }
            }
            throw new Exception("请求URL:" + url + "失败");
        } finally {// 释放连接资源
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
//            if (httpClient != null) {
//                httpClient.getConnectionManager().shutdown();
//            }
        }
    }


    public static String httpPostMap(String url, Map params, String contentType, String encoding) throws Exception {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        HttpPost httpPost = null;
        HttpClient httpClient = null;

        try {
            httpClient = getHttpClient(0);
            HttpPost httppost = new HttpPost(url);
            if (StringUtils.isNotBlank(contentType)) {
                httppost.addHeader("Content-Type", contentType);
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }

            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpClient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(response.getEntity());
                }
            }
            throw new Exception("请求URL:" + url + "失败");
        } finally {// 释放连接资源
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }


    public static HttpClient getHttpClient(int timeout) {
        DefaultHttpClient httpClient = new DefaultHttpClient(httpConnectionPool);
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(1,false));
//            if (timeout == 0) {
//                // 设置连接超时
//                httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
//                // 设置读取超时
//                httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, SO_TIMEOUT);
//            } else {
//                // 设置连接超时
//                httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
//                // 设置读取超时
//                httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, timeout * 2);
//            }

        return httpClient;
    }

    /**
     * 编码
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String replace(String s) throws UnsupportedEncodingException {
        return s.replaceAll("\\+", "%20");

    }


}
