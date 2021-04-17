package com.keruyun.infra.commons.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class OKHttp3Utils {

    private static final OkHttpClient OK_HTTP_CLIENT =new OkHttpClient().newBuilder().connectTimeout(5,TimeUnit.SECONDS).build();
            //new OkHttpClient.Builder().setConnectTimeout(5).build();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static final MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
    /**
     * GET请求
     *
     * @param url
     * @return Optional<String>
     */
    public static Optional<String> get(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        return Optional.of(OK_HTTP_CLIENT.newCall(request).execute().body().string());

    }

    /**
     * POST请求，参数为json格式。
     *
     * @param url
     * @param json
     * @return Optional<String>
     */
    public static Optional<String> post(String url, String json) throws Exception {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return Optional.of(OK_HTTP_CLIENT.newCall(request).execute().body().string());
    }

    public static Optional<String> postByFormType(String url, String form) throws Exception {
        RequestBody body = RequestBody.create(FORM_TYPE, form);
        Request request = new Request.Builder().url(url).post(body).build();
        return Optional.of(OK_HTTP_CLIENT.newCall(request).execute().body().string());
    }

    /**
     * 根据不同的类型和requestbody类型来接续参数
     * @param url
     * @param mediaType
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Optional<String> post(String url, MediaType mediaType, InputStream inputStream) throws Exception {
        RequestBody body = createRequestBody(mediaType, inputStream);
        Request request = new Request.Builder().url(url).post(body).build();
        return Optional.ofNullable(OK_HTTP_CLIENT.newCall(request).execute().body().string());
    }

    private static RequestBody createRequestBody(final MediaType mediaType, final InputStream inputStream){
        return new RequestBody() {
           // @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() throws IOException {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

}
