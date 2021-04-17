package com.keruyun.infra.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils3 {


    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils3.class);

    private static final Integer CONNECTION_TIMEOUT = 1000;
    private static final Integer SO_TIMEOUT = 2000;
    private static PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();


    static {
        httpClientConnectionManager.setMaxTotal(200);
        httpClientConnectionManager.setDefaultMaxPerRoute(20);
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
     * @param url
     * @param content
     * @param timeout 秒
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, String content, int timeout) throws Exception {
        return httpPost(url, content, null, null, timeout, 0);
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
    public static String httpPost(String url, String content, String contentType, String encoding, int timeout, int type) throws Exception {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            httpClient = getHttpClient(type);
            HttpPost httppost = new HttpPost(url);


            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout((timeout * 1000))   //设置连接超时时间
                    .setConnectionRequestTimeout((timeout * 1000)) // 设置请求超时时间
                    .setSocketTimeout((timeout * 1000))
                    .setRedirectsEnabled(false)//默认允许自动重定向
                    .build();

            httppost.setConfig(requestConfig);

            if (StringUtils.isNotBlank(contentType)) {
                httppost.addHeader("Content-Type", contentType);
            }
            httppost.setEntity(new StringEntity(content, encoding));


            response = httpClient.execute(httppost);

            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功
                if (entity != null) {
                    return EntityUtils.toString(response.getEntity());
                }
            }
//            EntityUtils.consume();

            throw new Exception("请求URL:" + url + "失败");
        } finally {// 释放连接资源
//            if (response != null) {
//                response.close();
//            }
            if (httpClient != null) {
//                httpClient.close();
            }

            logger.info("request url {} ,total time {} ms", url, (System.currentTimeMillis() - start));

        }
    }


    public static String httpPostMap(String url, Map params, String contentType, String encoding) throws Exception {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        HttpPost httpPost = null;
        HttpClient httpClient = null;

        try {
            httpClient = getHttpClient(0);
            httpPost = new HttpPost(url);
            if (StringUtils.isNotBlank(contentType)) {
                httpPost.addHeader("Content-Type", contentType);
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功

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


    public static String upload(String url, Map params, Map<String, byte[]> uploadFile, String contentType, String encoding) throws Exception {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        HttpPost httpPost = null;
        HttpClient httpClient = null;

        try {
            httpClient = getHttpClient(0);
            httpPost = new HttpPost(url);
            if (StringUtils.isNotBlank(contentType)) {
                httpPost.addHeader("Content-Type", contentType);
            }


            MultipartEntity reqEntity = new MultipartEntity();
            for (Map.Entry<String, byte[]> file : uploadFile.entrySet()) {
                reqEntity.addPart(file.getKey(),
                        new ByteArrayBody(file.getValue(), "re-signup.PNG"));
            }


            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                reqEntity.addPart(name, new StringBody(value));
            }

            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) { // 成功

                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
            throw new Exception("请求URL:" + url + "失败");
        } finally {// 释放连接资源
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }

    public static CloseableHttpClient getHttpClient(int type) {
        CloseableHttpClient httpClient = null;
        if (type == 1) {
            httpClient = HttpClients.createMinimal(httpClientConnectionManager);
        } else {
            httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
//                    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, false))
//                    .disableAutomaticRetries()
                    .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy(2, 10000))
                    .build();

        }
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
