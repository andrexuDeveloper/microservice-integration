package com.keruyun.infra.commons.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author
 * @Date: 15/11/26 16:34
 */
public class RequestUtils {
  private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static boolean isRequestExpired(long timestamp, long expiredSeconds) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - timestamp > expiredSeconds * 1000) {
            return true;
        }
        return false;
    }

    public static String queryString(SortedMap<String, Object> params, String key) {
        return queryString(params, key, false);
    }

    public static String queryString(SortedMap<String, Object> params, String key, boolean isEcoding) {
        return queryString(params, "key", key, isEcoding);
    }

    //added by fw 2015-6-3 用于调用h5订单状态接口生成签名时  产生不带key=xx的查询参数
    public static String queryString(SortedMap<String, Object> params) {
        return queryString(params, null, "", false);
    }

    public static String queryString(
            SortedMap<String, Object> params, String keyName, String keyValue, boolean isEcoding) {
        StringBuilder templateParam = new StringBuilder();
        int i = 0;
        int size = params.size();
        String charset = "UTF-8";
        Object obj;
        for (Map.Entry<String, Object> e : params.entrySet()) {
            i++;
            String name = e.getKey();
            obj = e.getValue();
            String value;
            if (null == obj) {
                value = "";
            } else {
                value = "" + obj;
            }
            if (isEcoding) {
                name = encode(name, charset);
                value = encode(value, charset);
            }
            templateParam.append(name)
                    .append("=")
                    .append(value);
            if (i < size || !StringUtils.isBlank(keyValue)) {
                templateParam.append("&");
            }
        }
        if (StringUtils.isNotBlank(keyValue)) {
            templateParam.append(keyName)
                    .append("=")
                    .append(keyValue);
        }
        return templateParam.toString();
    }


    public static String getSign(String queryString) {
        return DigestUtils.md5Hex(queryString);
    }


    public static String encode(final String content, final String charset) {
        if (content == null) {
            return null;
        }
        try {
            return URLEncoder.encode(content, charset != null ? charset : "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * 编码 get querystring 而不是application/x-www-form-urlencoded（URLEncoder.encode(content, encoding)）
     *
     * @param content
     * @param encoding
     * @return
     */
//    public static String encodingForQueryString(String content, String encoding) {
//        try {
//            if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(encoding)) {
//                return URIUtil.encodeQuery(content, encoding);
//            }
//            return content;
//        } catch (URIException e) {
//            logger.error(e.getMessage(), e);
//            return "";
//        }
//    }


    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     *
     * https://www.cnblogs.com/edgedance/p/6979602.html
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data
     *            被签名的字符串
     * @param key
     *            密钥
     * @return
    加密后的字符串
     */
    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes("utf-8"), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes("utf-8"));
            result =rawHmac;
                    //Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null != result) {
            return  DigestUtils.md5Hex(result);
        } else {
            return null;
        }
    }

}
