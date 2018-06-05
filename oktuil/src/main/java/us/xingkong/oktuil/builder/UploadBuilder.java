package us.xingkong.oktuil.builder;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.body.ProgressRequestBody;
import us.xingkong.oktuil.callback.Callback;
import us.xingkong.oktuil.response.ResponseInterface;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 10:40
 * @描述:
 * @更新日志:
 */
public class UploadBuilder extends BaseRequestBuilderWithParams<UploadBuilder> {

    private Map<String, File> mFiles;
    private ArrayList<MultipartBody.Part> mParts;

    public UploadBuilder(OkUtil okUtil) {
        super(okUtil);
    }

    public UploadBuilder files(Map<String, File> params) {
        mFiles = params;
        return this;
    }

    public UploadBuilder addFile(String key, File file) {
        if (mFiles == null) {
            mFiles = new LinkedHashMap<>();
        }
        mFiles.put(key, file);
        return this;
    }

    public UploadBuilder addFile(String key, String fileName, byte[] fileContent) {
        if (mParts == null) {
            mParts = new ArrayList<>();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), fileContent);
        mParts.add(MultipartBody.Part
                .create(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""), fileBody));
        return this;
    }

    @Override
    public void enqueue(ResponseInterface responseInterface) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("Null Url");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            appendParams(multipartBuilder, mParams);
            appendFiles(multipartBuilder, mFiles);
            appendParts(multipartBuilder, mParts);

            builder.post(new ProgressRequestBody(multipartBuilder.build(), responseInterface));

            Request request = builder.build();

            OkUtil.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new Callback(responseInterface));
        } catch (Exception e) {
            responseInterface.onFailure(0, e.getMessage());
        }
    }

    private void appendParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void appendFiles(MultipartBody.Builder builder, Map<String, File> files) {
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
    }

    private void appendParts(MultipartBody.Builder builder, List<MultipartBody.Part> parts) {
        if (parts != null && parts.size() > 0) {
            for (int i = 0; i < parts.size(); i++) {
                builder.addPart(parts.get(i));
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
