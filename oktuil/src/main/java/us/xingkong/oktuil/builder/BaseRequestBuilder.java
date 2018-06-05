package us.xingkong.oktuil.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.ResponseInterface;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 20:55
 * @描述:
 * @更新日志:
 */
public abstract class BaseRequestBuilder<T extends BaseRequestBuilder> {

    String mUrl;
    Object mTag;
    Map<String, String> mHeaders;
    OkUtil mOkUtil;

    public abstract void enqueue(ResponseInterface responseInterface);

    BaseRequestBuilder(OkUtil okUtil) {
        mOkUtil = okUtil;
    }

    public T url(String url) {
        mUrl = url;
        return (T) this;
    }

    public T tag(Object tag) {
        mTag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        mHeaders = headers;
        return (T) this;
    }

    public T addHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key, value);
        return (T) this;
    }

    void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            headersBuilder.add(key, headers.get(key));
        }
        builder.headers(headersBuilder.build());
    }
}
