package us.xingkong.oktuil.builder;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.callback.Callback;
import us.xingkong.oktuil.response.ResponseInterface;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 22:48
 * @描述:
 * @更新日志:
 */
public class PostBuilder extends BaseRequestBuilderWithParams<PostBuilder> {

    private String mJson = "";

    public PostBuilder(OkUtil okUtil) {
        super(okUtil);
    }

    public PostBuilder jsonParams(String json) {
        mJson = json;
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

            if (mJson.length() > 0) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJson);
                builder.post(body);
            } else {
                FormBody.Builder encodingBuilder = new FormBody.Builder();
                addParams(encodingBuilder, mParams);
                builder.post(encodingBuilder.build());
            }

            Request request = builder.build();

            OkUtil.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new Callback(responseInterface));
        } catch (Exception e) {
            e.printStackTrace();
            responseInterface.onFailure(0, e.getMessage());
        }
    }

    private void addParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
