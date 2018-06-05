package us.xingkong.oktuil.builder;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.body.ProgressResponseBody;
import us.xingkong.oktuil.callback.DownloadCallback;
import us.xingkong.oktuil.response.BaseDownloadResponse;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 21:35
 * @描述:
 * @更新日志:
 */
public class DownloadBuilder {

    private OkUtil mOkUtil;
    private String mUrl = "";
    private Object mTag;
    private Map<String, String> mHeaders;

    private String mFileDir = "";
    private String mFileName = "";
    private String mFilePath = "";

    private Long mCompleteBytes = 0L;

    public DownloadBuilder(OkUtil okUtil) {
        mOkUtil = okUtil;
    }

    public DownloadBuilder url(@NonNull String url) {
        mUrl = url;
        return this;
    }

    public DownloadBuilder tag(@NonNull Object tag) {
        mTag = tag;
        return this;
    }

    public DownloadBuilder headers(@NonNull Map<String, String> headers) {
        mHeaders = headers;
        return this;
    }

    public DownloadBuilder fileDir(@NonNull String fileDir) {
        mFileDir = fileDir;
        return this;
    }

    public DownloadBuilder fileName(@NonNull String fileName) {
        mFileName = fileName;
        return this;
    }

    public DownloadBuilder filePath(@NonNull String filePath) {
        mFilePath = filePath;
        return this;
    }

    public DownloadBuilder addHeader(@NonNull String key, @NonNull String value) {
        if (mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key, value);
        return this;
    }

    public DownloadBuilder setCompleteBytes(@NonNull Long completeBytes) {
        if (completeBytes > 0L) {
            mCompleteBytes = completeBytes;
            addHeader("RANGE", "bytes=" + completeBytes + "-");
        }
        return this;
    }

    public Call enqueue(final BaseDownloadResponse downloadResponse) {
        try {
            if (mUrl.length() == 0) {
                throw new IllegalArgumentException("Null Url");
            }

            if (mFilePath.length() == 0) {
                if (mFileDir.length() == 0 || mFileName.length() == 0)
                    throw new IllegalArgumentException("Null Path");
                else
                    mFilePath = mFileDir + mFileName;
            }

            checkFilePath(mFilePath, mCompleteBytes);
            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            Call call = OkUtil.getOkHttpClient()
                    .newBuilder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse
                                    .newBuilder()
                                    .body(new ProgressResponseBody(originalResponse.body(), downloadResponse))
                                    .build();
                        }
                    })
                    .build()
                    .newCall(request);
            call.enqueue(new DownloadCallback(mFilePath, mCompleteBytes, downloadResponse));
            return call;
        } catch (Exception e) {
            downloadResponse.onFailure(e.getMessage());
            return null;
        }
    }

    private void checkFilePath(String filePath, Long completeBytes) throws Exception {
        File file = new File(filePath);
        if (file.exists()) return;
        if (completeBytes > 0L) {
            throw new Exception("断点续传文件" + filePath + "不存在！");
        }

        if (filePath.endsWith(File.separator)) {
            throw new Exception("创建文件" + filePath + "失败，目标文件不能为目录！");
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new Exception("创建目标文件所在目录失败！");
            }
        }
    }

    private void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            headersBuilder.add(key, headers.get(key));
        }
        builder.headers(headersBuilder.build());
    }
}
